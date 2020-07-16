package org.exodus.threads;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.exodus.bean.wallet.KeyPair;
import org.exodus.bean.node.*;
import org.exodus.core.*;
import org.exodus.exception.InveException;
import org.exodus.node.GeneralNode;
import org.exodus.node.SqliteDAO;
import org.exodus.util.StringUtils;
import org.exodus.utils.DSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 
 * @date 2018/10/16 0016 下午 6:49
 **/
public class PbftThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger("PbftThread.class");
    private GeneralNode node;

    public PbftThread(GeneralNode node) {
        this.node = node;
    }

    @Override
    public void run() {
        Pbft algo = new Pbft(this.node);
        int num =0;
        Instant first = Instant.now();
        while (true) {
            logger.info("### node-{}:{}  STARTING ROUND {}",
                    node.parameters.seedGossipAddress.pubIP, node.parameters.seedGossipAddress.rpcPort, (num++));
            Block block = null;
            if (null != this.node.getPendingBlock()) {
                logger.warn("exist pending block. continue consensus...");
                block = this.node.getPendingBlock();
            } else {
                // 新建分片区块
                try {
                    block = createBlock();
                } catch (InveException e) {
                    logger.error("create block error : {}", e);
                    e.printStackTrace();
                    continue;
                }
                if (null==block) {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                // 将新区块放入pending状态
                this.node.setPendingBlock(block);
            }
            try {
                String data = algo.normalFunction(JSON.toJSONString(block));
                logger.info("### CONSENSUS RESULT: \n{}", data);
                if (!StringUtils.isEmpty(data)) {
                    logger.info("============================ {}", Duration.between(first, Instant.now()).toMillis() );
                    // 反转共识区块
                    Block consensusBlock = this.convertBlock(data);
                    if (null != consensusBlock) {
                        // 共识区块入库
                        saveNewConsensusBlock(consensusBlock);
                        // 彻底清空缓存
                        algo.wholeCleanUp();
                        num = 0;
                        // 更新最新区块
                        this.node.setCurrBlock(consensusBlock);
                        // 更新共识中区块
                        this.node.setPendingBlock(null);
                    } else {
                        logger.warn("consensus Block is null.");
                    }
                } else {
                    logger.warn("consensus data is null.");
                }
                if (false) {
                    throw new ConnectException();
                    // java complained that ConnectException would never be raised in here, BUT IT IS!
                    // This sh*t is why java is so ugly.
                }
            } catch (Exception e) {
                logger.error("### Round Abort ### {}", e.getMessage());
            } finally {
                logger.info("Main.class invoke clean up...");
                algo.cleanUp();
            }
        }
    }

    /**
     * 共识数据反转成区块
     * @param data 共识数据
     * @return 区块
     */
    private Block convertBlock(String data) {
        try {
            return JSON.parseObject(data, Block.class);
        } catch (Exception e) {
            logger.error("{}", e);
            return null;
        }
    }

    /**
     * 判断并封装待共识的节点
     * @param n 节点
     * @return 待共识节点
     */
    private BaseNode handleWaiConsensusNode(BaseNode n) {
        switch (n.getStatus()) {
            case NodeStatus.WAIT_CONSENSUS:
                n.setStatus(NodeStatus.IN_CONSENSUS);
                break;
            case NodeStatus.WAIT_DELETE_HAS_CONSENSUSED:
                n.setStatus(NodeStatus.DELETTING_HAS_CONSENSUSED);
                break;
            case NodeStatus.WAIT_DELETE_IN_CONSENSUS:
                n.setStatus(NodeStatus.DELETTING_IN_CONSENSUS);
                break;
            case NodeStatus.WAIT_UPDATE_HAS_CONSENSUSED:
                n.setStatus(NodeStatus.UPDATTING_HAS_CONSENSUSED);
                break;
            case NodeStatus.WAIT_UPDATE_IN_CONSENSUS:
                n.setStatus(NodeStatus.UPDATTING_IN_CONSENSUS);
                break;
            default: n = null; break;
        }
        return n;
    }

    /**
     * 新建一个分片区块
     * @return 新区块
     */
    private Block createBlock() throws InveException {
        logger.info("createBlock()...");
        while (this.node.getFullNodeList().values().stream().filter(BaseNode::checkIfConsensusNode).count() < 4 ) {
            logger.warn("Insufficient full nodes...");
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (null!=this.node.getPendingBlock()) {
            logger.warn("exist a pending block...");
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // get last block
        long shardIndex = this.node.getLocalFullNodeList().values().stream()
                .map(LocalFullNode::getShard).filter(Objects::nonNull).distinct().count();
        Block preBlock = this.node.getCurrBlock();
        Hash preHash = (null == preBlock || null == preBlock.getHash()) ? new Hash("-1".getBytes()) : preBlock.getHash();
        long index = (null == preBlock || 0 > preBlock.getIndex()) ? 0 : preBlock.getIndex() + 1;

        // full node
        long size = this.node.getFullNodeList().values().stream().filter(BaseNode::checkIfWaiConsensusNode).count();
        logger.info("full node wait consensus size: {}", size);
        // relay node
        size += this.node.getRelayNodeList().values().stream().filter(BaseNode::checkIfWaiConsensusNode).count();
        logger.info("full & relay node wait consensus size: {}", size);
        // local full node
        long consShardNodeSize = this.node.getLocalFullNodeList().values().stream()
                .filter(BaseNode::checkIfConsensusNode).count();
        long count = (node.parameters.staticSharding)
                ? node.parameters.shardNodeSize*node.parameters.shardSize : node.parameters.shardNodeSize;
        if (!node.parameters.staticSharding || consShardNodeSize <= 0) {
            long size1 = this.node.getLocalFullNodeList().values().stream()
                    .filter(n -> n.getStatus()==NodeStatus.WAIT_CONSENSUS)
                    .limit(count).count();
            if (size1>=count) {
                size += size1;
                logger.info("full & relay & local full node wait consensus size: {}", size);
            }
        }

        // 如果存在需要共识的信息，则生成新的block
        if (size>0) {
            this.node.getLocalFullNodeList().values().forEach(n->logger.warn("shard:{}, index:{}, status:{}",
                    n.getShard(), n.getIndex(), n.getStatus()));
            // get data and construct
            // full node
            List<BaseNode> waitHandleNodes = new ArrayList<>();
            for (BaseNode n : this.node.getFullNodeList().values()) {
                n = handleWaiConsensusNode(n);
                if (null!=n) {
                    waitHandleNodes.add(n);
                }
            }
            logger.info("create block...0 node size: {}", waitHandleNodes.size());
            // relay node
            for (BaseNode n : this.node.getRelayNodeList().values()) {
                n = handleWaiConsensusNode(n);
                if (null!=n) {
                    waitHandleNodes.add(n);
                }
            }
            logger.info("create block...1 node size: {}", waitHandleNodes.size());
            // local full node
            if (!node.parameters.staticSharding || (consShardNodeSize<=0)) {
                List<LocalFullNode> waitConsensusNodes = this.node.getLocalFullNodeList().values().stream()
                        .filter(n -> n.getStatus()==NodeStatus.WAIT_CONSENSUS)
                        .limit(count)
                        .collect(Collectors.toList());
                logger.warn("count: {}, waitConsensusNodes.size(): {}", count, waitConsensusNodes.size());
                if (waitConsensusNodes.size()==count) {
                    int i=0;
                    for (LocalFullNode localFullNode : waitConsensusNodes) {
                        localFullNode.setShard(""+shardIndex);
                        localFullNode.setIndex(""+(i++));
                        localFullNode.setStatus(NodeStatus.IN_CONSENSUS);
                        if (i==node.parameters.shardNodeSize) {
                            shardIndex++;
                            i=0;
                        }
                    }
                    waitHandleNodes.addAll(waitConsensusNodes);
                }
            }

            logger.info("create block...2 node size: {}", waitHandleNodes.size());
            // new block
            long timestamp = Instant.now().toEpochMilli();
            byte[] hash = Hash.hash(preHash.getHash(), index, timestamp, JSONArray.toJSONString(waitHandleNodes).getBytes());
            Block block = new Block.Builder().preHash(preHash)
                    .timestamp(timestamp).index(index)
                    .data(JSONArray.toJSONString(waitHandleNodes).getBytes())
                    .hash(new Hash(hash)).build();
            block.setSignature(Crypto.sign(hash, this.node.privateKey));
            printBlock(block);
            return block;
        } else {
            return null;
        }
    }

    /**
     * 保存新区块
     * @param consensusBlock 新区块
     */
    private void saveNewConsensusBlock(Block consensusBlock) {
        final String hash = DSA.encryptBASE64(consensusBlock.getHash().getHash());
        // 保存区块
        SqliteDAO.addBlock(this.node.parameters.dbFile, consensusBlock);
        // 保存共识信息
        JSONArray objects = JSONArray.parseArray(new String(consensusBlock.getData()));
        objects.forEach(object -> {
            BaseNode baseNode = BaseNode.nodeConvert((JSONObject) object);
            logger.info("consensus-{}:{}", consensusBlock.getIndex(), JSON.toJSONString(baseNode));
            // full node
            if (baseNode instanceof FullNode){
                FullNode n = (FullNode) baseNode;
                switch (n.getStatus()) {
                    case NodeStatus.DELETTING_HAS_CONSENSUSED:
                    case NodeStatus.DELETTING_IN_CONSENSUS:
                        SqliteDAO.deleteFullNode(this.node.parameters.dbFile, n.getPubkey());
                        this.node.getFullNodeList().remove(n.getPubkey());
                        break;
                    case NodeStatus.UPDATTING_HAS_CONSENSUSED:
                    case NodeStatus.UPDATTING_IN_CONSENSUS:
                    case NodeStatus.IN_CONSENSUS:
                    default:
                        n.setStatus(NodeStatus.HAS_CONSENSUSED);
                        n.setHash(hash);
                        SqliteDAO.saveFullNode(this.node.parameters.dbFile, n);
                        this.node.getFullNodeList().put(n.getPubkey(), n);
                        break;
                }
            }
            // relay node
            if (baseNode instanceof RelayNode){
                RelayNode n = (RelayNode) baseNode;
                switch (n.getStatus()) {
                    case NodeStatus.DELETTING_HAS_CONSENSUSED:
                    case NodeStatus.DELETTING_IN_CONSENSUS:
                        SqliteDAO.deleteRelayNode(this.node.parameters.dbFile, n.getPubkey());
                        this.node.getRelayNodeList().remove(n.getPubkey());
                        break;
                    case NodeStatus.UPDATTING_HAS_CONSENSUSED:
                    case NodeStatus.UPDATTING_IN_CONSENSUS:
                    case NodeStatus.IN_CONSENSUS:
                    default:
                        n.setStatus(NodeStatus.HAS_CONSENSUSED);
                        n.setHash(hash);
                        SqliteDAO.saveRelayNode(this.node.parameters.dbFile, n);
                        this.node.getRelayNodeList().put(n.getPubkey(), n);
                        break;
                }
            }
            // local full node
            if (baseNode instanceof LocalFullNode){
                LocalFullNode n = (LocalFullNode)baseNode;
                n.setStatus(NodeStatus.HAS_SHARDED);
                n.setHash(hash);
                SqliteDAO.saveLocalfullnode(this.node.parameters.dbFile, n);
                this.node.getLocalFullNodeList().put(n.getPubkey(), n);
            }
        });
    }

    private void printBlock(Block block) {
        logger.info("preHash: {}", new String(block.getPreHash().getHash()));
        logger.info("hash: {}", new String(block.getHash().getHash()));
        logger.info("signature: {}", new String(block.getSignature()));
        logger.info("timestamp: {}", block.getTimestamp());
        logger.info("index: {}", block.getIndex());
        logger.info("data: {}", new String(block.getData()));

        logger.info("verify sign: {}",
                Crypto.verifySignature(block.getHash().getHash(), block.getSignature(), this.node.publicKey));
    }

    public static void main(String[] args) throws InveException {
        Crypto crypto = new Crypto();
        KeyPair keyPair = crypto.getKeyPair();
//
//        Seed seed = new Seed();
//        seed.privateKey = keyPair.getPrivateKey();
//        seed.publicKey = keyPair.getPublicKey();

        byte[] preHash = "Hash".getBytes();
        byte[] hash = Hash.hash(preHash, -1, Instant.now().toEpochMilli(), "test".getBytes());
        System.out.println("\n++++++++++++hash: " + DSA.encryptBASE64(hash));

        byte[] signature = Crypto.sign(hash, keyPair.getPrivateKey());
        System.out.println("\n++++++++++++signature: " + DSA.encryptBASE64(signature));

        boolean verify = Crypto.verifySignature(hash, signature, keyPair.getPublicKey());
        System.out.println("\n++++++++++++verify: " + verify);

//
//        Block block0 = new Block.Builder().preHash(new Hash(preHash)).hash(new Hash(hash))
//                .signature(Crypto.sign(hash, seed.privateKey))
//                .timestamp(Instant.now().toEpochMilli()).index(0).data("sdfsdfasdfsdfasdf".getBytes()).build();
//        seed.setCurrBlock(block0);
//
//        PbftThread thread = new PbftThread(seed);
//        thread.printBlock(seed.getCurrBlock());
//        thread.start();
    }
}
