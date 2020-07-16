package org.exodus.rpcImpl.fullnode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zeroc.Ice.Current;
import org.exodus.bean.node.*;
import org.exodus.core.*;
import org.exodus.node.GeneralNode;
import org.exodus.node.SqliteDAO;
import org.exodus.rpc.fullnode.Register;
import org.exodus.util.ResponseUtils;
import org.exodus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * register rpc接口实现类
 * Created by Clarelau61803@gmail.com on 2018/6/5.
 */
public class RegisterRpcImpl implements Register {
    private static final Logger logger = LoggerFactory.getLogger("RegisterRpcImpl.class");
    GeneralNode node;
    public RegisterRpcImpl(GeneralNode node){
        this.node = node;
    }

    /**
     * 获取nrg 价格
     * @param current 连接信息
     * @return 全节点列表
     */
    @Override
    public String getNrgPrice(Current current) {
        String priceInfo = RegisterService.getNrgPrice();
        return StringUtils.isEmpty(priceInfo)
                ? ResponseUtils.handleExceptionResponse() : ResponseUtils.normalResponse(priceInfo);
    }

    /**
     * 注册新的全节点
     * @param pubkey 新注册节点公钥
     * @param current 连接信息
     * @return 注册成功""，注册失败则返回null
     */
    @Override
    public String registerFullNode(String ip, int rpcPort, int httpPort,  String pubkey, Current current) {
        logger.info("registerFullNode...");
        return RegisterService.registerFullNode(pubkey, ip, rpcPort, httpPort, "", node);
    }

    @Override
    public String addNewFullNode(String fullnodeStr, Current current) {
        return "invalid interface.";
    }

    /**
     * 注销已有的全节点
     * @param pubkey 被注销节点公钥
     * @param current 连接信息
     * @return 注销成功则返回“”，否则返回失败信息
     */
    @Override
    public String logoutFullNode(String pubkey, Current current) {
        return RegisterService.logoutFullNode(pubkey, node);
    }

    /**
     * 查询全节点列表
     * @param pubkey 请求者公钥
     * @param current 连接信息
     * @return 全节点列表
     */
    @Override
    public String getFullNodeList(String pubkey, Current current) {
        return RegisterService.queryFullNodeList(pubkey, node);
    }

    /**
     * （轻节点等）获取（局部全节点）邻居列表
     * @param pubkey 请求者公钥
     * @param current 连接信息
     * @return 局部全节点邻居列表
     */
    @Override
    public String getNeighborLocalFullNodeList(String pubkey, Current current) {
        System.out.println(">>> getLocalfullnodeListInShard() ... ");
        long shardCount = node.getLocalFullNodeList().values().stream()
                .filter(n -> n.getStatus()== NodeStatus.HAS_SHARDED)
                .map(LocalFullNode::getShard).distinct().count();
        if (node.openServiceState == 2 && shardCount > 0) {
            List<LocalFullNode> neighbors = RegisterService.queryLocalfullnodeNeighborList(
                    (shardCount==1) ? "0" : RegisterService.generateShardIdByPubKey((int)shardCount, pubkey),
                    node);
            return (neighbors.size()<=0)
                        ? ResponseUtils.normalResponse()
                        : ResponseUtils.normalResponse(JSONArray.toJSONString(neighbors));
        } else {
            return ResponseUtils.handleExceptionResponse("service not start now.");
        }
    }

    /**
     * 获取现有所有局部全节点列表
     * @param pubkey 请求者公钥
     * @param current 连接信息
     * @return 所有局部全节点列表
     */
    @Override
    public String getLocalFullNodeList(String pubkey, Current current) {
        if (node.openServiceState == 2) {
            List<LocalFullNode> localfullnodes = RegisterService.queryLocalfullnodeList(node);
            return (null==localfullnodes || localfullnodes.size()<=0)
                    ? ResponseUtils.normalResponse()
                    : ResponseUtils.normalResponse(JSONArray.toJSONString(localfullnodes));
        } else {
            return ResponseUtils.handleExceptionResponse("service not start now.");
        }
    }

    /**
     * (局部全节点)获取自己分片信息
     * @param pubkey 请求者公钥
     * @param current 连接信息
     * @return 请求者分片信息
     */
    @Override
    public String getNodeShardInfo(String pubkey, Current current) {
        if (node.openServiceState == 2) {
            if (StringUtils.isEmpty(pubkey)) {
                logger.error("ERROR: need param pubkey!");
                throw new RuntimeException("ERROR: need param pubkey!");
            }
            LocalFullNode n = node.getLocalFullNodeList().get(pubkey);
            return (null!=n && n.getStatus()==NodeStatus.HAS_SHARDED)
                    ? ResponseUtils.normalResponse() : ResponseUtils.normalResponse(JSON.toJSONString(n));
        } else {
            return ResponseUtils.handleExceptionResponse("service not start now.");
        }
    }

    /**
     * 获取所有分片信息
     * @param current 连接信息
     * @return 所有局部全节点分片信息，没有则返回null
     */
    @Override
    public String getShardInfoList(Current current) {
        if (node.openServiceState == 2) {
            List<LocalFullNode> list = node.getLocalFullNodeList().values().parallelStream()
                    .filter(n -> n.getStatus()== NodeStatus.HAS_SHARDED ).collect(Collectors.toList());
            return (list.size()<=0)
                    ? ResponseUtils.normalResponse() : ResponseUtils.normalResponse(JSONArray.toJSONString(list));
        } else {
            return ResponseUtils.handleExceptionResponse("service not start now.");
        }
    }

    /**
     * 获取所有全节点公钥
     * @param current 连接信息
     * @return 所有全节点公钥
     */
    @Override
    public String[] getFullNodePublicKeyList(Current current) {
        return (null==node.getFullNodeList() || node.getFullNodeList().size()<=0) ? null
                : node.getFullNodeList().values().stream()
                .filter(BaseNode::checkIfConsensusNode)
                .map(BaseNode::getPubkey).toArray(String[]::new);
    }

    /**
     * 获取所有局部全节点公钥
     * @param current 连接信息
     * @return 所有局部全节点公钥
     */
    @Override
    public String[][] getLocalFullNodePublicKeyList(Current current) {
        logger.info(">>> getLocalFullNodePublicKeyList()...");
        if (node.openServiceState == 2 ) {
            List<LocalFullNode> list = node.getLocalFullNodeList().values().stream()
                    .filter(n -> n.getStatus()== NodeStatus.HAS_SHARDED ).collect(Collectors.toList());
            long shardCount = list.stream().map(LocalFullNode::getShard).distinct().count();
            if (list.size() < shardCount*node.parameters.shardNodeSize) {
                return null;
            }

            String[][] results = new String[(int)shardCount][node.parameters.shardNodeSize];
            list.forEach(n -> results[Integer.parseInt(n.getShard())][Integer.parseInt(n.getIndex())] = n.getPubkey());
            return results;
        } else {
            return null;
        }
    }

    /**
     * 注册中继节点
     * @param pubkey 中继节点申请者公钥
     * @param current ice连接信息
     * @return 注册成功""，注册失败则返回null
     */
    @Override
    public String registerRelayNode(String pubkey, String ip, int port, Current current) {
        try {
            return RegisterService.registerRelayNode(pubkey, ip, port, null, node);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.handleExceptionResponse();
        }
    }

    /**
     * 更新中继节点的心跳时间
     * @param pubkey 中继节点的公钥
     * @param current ice连接对象
     * @return 无
     */
    @Override
    public String aliveHeartRelayNode(String pubkey, Current current) {
        RelayNode relay = node.getRelayNodeList().get(pubkey);
        if(null!=relay) {
            relay.setLastAliveTimestamp(Instant.now().toEpochMilli());
            node.getRelayNodeList().put(relay.getPubkey(), relay);
            SqliteDAO.updateRelayNode(node.parameters.dbFile, relay);
        }
        return "";
    }

    /**
     * 注册局部全节点
     * @param pubkey 局部全节点申请者公钥
     * @param address 局部全节点申请者地址
     * @param current ice连接信息
     * @return 注册成功""，注册失败则返回null
     */
    @Override
    public String registerLocalFullNode(String pubkey, String address, Current current) {
        logger.info(">>> registerLocalFullNode()... address: {}", address);
        if (node.openServiceState >= 1 ) {
            // 验证公钥合法性
            if (StringUtils.isEmpty(pubkey) || StringUtils.isEmpty(address)) {
                String msg = "public key or address is null.";
                logger.error(msg);
                return ResponseUtils.paramIllegalResponse();
            }

            Enumeration<String> pubkeys = node.getLocalFullNodeList().keys();
            while (pubkeys.hasMoreElements()) {
                String p = pubkeys.nextElement();
                if (p.equals(pubkey)) {
                    logger.warn("public key exist. pubkey size: {}, addr: {}",
                            node.getLocalFullNodeList().size(), address);
                    return ResponseUtils.normalResponse();
                }
            }
            logger.info(">>> registerLocalFullNode()... list size: {}, address:{}",
                    node.getLocalFullNodeList().size(), address);

            if (null!=node.getPendingBlock()) {
                return ResponseUtils.handleExceptionResponse("busy: some nodes are in consensus.");
            }

            try {
                long consShardNodeSize = this.node.getLocalFullNodeList().values().stream().filter(BaseNode::checkIfConsensusNode).count();
                // allow new node to participate in sharding
                // if (!node.parameters.staticSharding || consShardNodeSize<=0) {
                if (!node.parameters.staticSharding || consShardNodeSize <= 0
                        || this.node.getLocalFullNodeList().size() < node.parameters.shardNodeSize) {
                    node.getLocalFullNodeList().put(pubkey, new LocalFullNode.Builder()
                            .type(NodeTypes.LOCALFULLNODE)
                            .pubkey(pubkey).address(address)
                            .status(NodeStatus.WAIT_CONSENSUS)
                            .build());
                    logger.info(">>> registerLocalFullNode() add addr-{} into queue, curr size: {}",
                            address, node.getLocalFullNodeList().size());
                    return ResponseUtils.normalResponse();
                } else {
                    return ResponseUtils.handleExceptionResponse("Registration has end.");
                }
            } catch (Exception e) {
                logger.error("register local full node exception: {}", e);
                return ResponseUtils.normalResponse("register exception.");
            }
        } else {
            String msg = "register service not begin.";
            logger.warn(msg);
            return ResponseUtils.handleExceptionResponse(msg);
        }
    }

    /**
     * 发送pbft相关消息
     * @param message 消息
     * @param current 连接信息
     * @return 是否成功
     */
    @Override
    public boolean sendPbftMessage(String message, Current current) {
        if (node.openServiceState >= 1 ) {
            boolean result = false;
            try {
                JSONObject json = JSON.parseObject(message);
                Message msg = Message.messageConvert(json);
                if (msg.sequence_no < this.node.getCurrentSequenceNo()) {
                    logger.info("Dropped old {} message with sequence_no {}. Delay: {}",
                            msg.getTypeString(), msg.sequence_no, (this.node.getCurrentSequenceNo() - msg.sequence_no)
                    );
                    return false;
                } else {
                    logger.info(">>>> received {} message with sequence_no {}", msg.getTypeString(), msg.sequence_no);
                }
                node.mq.add(msg);
            } catch (JSONException e) {
                logger.error("Convert the String to JSONObject failed: {}", e);
            }
            return result;
        } else {
            return false;
        }
    }

    /**
     * 查询分片区块
     * @param index 区块索引即高度
     * @param current 连接信息
     * @return 区块
     */
    @Override
    public String queryBlock(String index, Current current) {
        Block block = SqliteDAO.queryBlockFromDatabase(node.parameters.dbFile, index);
        return (null==block) ? null : JSON.toJSONString(block);
    }

    public static void main(String[] args) {
        ShardInfo shardInfo = new ShardInfo.Builder().pubkey("1111").shard("0").index("1").rpcPort("1111").build();
        String data = JSON.toJSONString(shardInfo);
        ProposeMessage proposeMessage = new ProposeMessage(
                System.currentTimeMillis()/5000,
                "pubkey".getBytes(),
                true,
                "sign".getBytes(),
                data.getBytes()
        );
        try {
            JSONObject json = JSON.parseObject(proposeMessage.messageEncode().toString());
            Message msg = Message.messageConvert(json);
            if (msg.sequence_no < System.currentTimeMillis()/5000) {
                System.out.println("44");
            } else {
                System.out.println("55");
            }
            new MessageQueue().add(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
