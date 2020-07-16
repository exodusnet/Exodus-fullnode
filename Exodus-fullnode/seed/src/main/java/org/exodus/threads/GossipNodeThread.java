package org.exodus.threads;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.exodus.bean.node.*;
import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;
import org.exodus.node.Main;
import org.exodus.node.SqliteDAO;
import org.exodus.rpc.fullnode.RegisterPrx;
import org.exodus.util.HnKeyUtils;
import org.exodus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * seed gossip网络维护线程
 * Created by Clare on 2018/9/6 0006.
 */
public class GossipNodeThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger("GossipNodeThread.class");
    private Main main;
    public static ArrayList<Member> FullNodeNeighborPools = new ArrayList<>();
    public static ArrayList<Member> LocalFullNodeNeighborPools = new ArrayList<>();

    public GossipNodeThread(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        logger.info("start gossip network...");
        String pubkey = HnKeyUtils.getString4PublicKey(this.main.publicKey);
        ClusterConfig nodeConfig = ClusterConfig.builder()
                .memberHost(this.main.parameters.seedGossipAddress.pubIP)
                .port(this.main.parameters.seedGossipAddress.gossipPort)
                .portAutoIncrement(false)
                .addMetadata("level", "" + NodeTypes.SEED)
                .addMetadata("rpcPort", "" +this.main.parameters.seedGossipAddress.rpcPort)
                .addMetadata("httpPort", "" +this.main.parameters.seedGossipAddress.httpPort)
                .addMetadata("pubkey", pubkey)
                .build();
        Cluster cluster = Cluster.joinAwait(nodeConfig);
        logger.info("full node has started!");

        main.setSeedPubkey(pubkey);
        while (true) {
            Instant first = Instant.now();
            this.updateFullNodeNeighborPools(cluster.members());

            // Francis.Deng 4/6/2019
            // track available local full node neighbor(TALFNN)
            logger.info("TALFNN - local full node neighbor size in seed(before filtering process) = {}",cluster.otherMembers().size());
            cluster.otherMembers().stream().forEach((mbr) -> {
                logger.info(mbr.toString());
            });

            this.updateLocalFullNodeNeighborPools(cluster.otherMembers());
            this.broadcastFullNodeRegisterEvent();           // 广播全节点注册事件
            this.broadcastFullNodeLogoutEvent();             // 广播全节点注销事件
            long interval = Duration.between(first, Instant.now()).toMillis();
            if (interval < main.parameters.gossipInterval) {
                try {
                    sleep(main.parameters.gossipInterval-interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 广播全节点注册事件(全节点前3个注册都是通过广播确认的)
     */
    private void broadcastFullNodeRegisterEvent() {
         List<FullNode> validFullNodes = main.getFullNodeList().values().stream()
                .filter(BaseNode::checkIfConsensusNode)
                 .collect(Collectors.toList());
        if ( validFullNodes.size() < 4 ) {
            if (null != main.getPendingBlock()) {
                logger.warn("busy: some nodes are in consensus.");
                return;
            }

            List<FullNode> candidateNodes = main.getFullNodeList().values().parallelStream()
                    .filter(n -> n.getStatus()== NodeStatus.WAIT_CONSENSUS)
                    .collect(Collectors.toList());

            // 广播新注册候选节点给所有全节点
            validFullNodes.forEach(n -> {
                // 连接
                RegisterPrx prx = main.buildRegisterConnection2FullNode(n);
                while (null == prx) {
                    prx = main.buildRegisterConnection2FullNode(n);
                }
                // 添加全节点
                for(FullNode fullNode : candidateNodes) {
                    prx.addNewFullNode(JSON.toJSONString(fullNode));
                }
            });

            // 向候选节点通知共识成功并更新最新全节点列表
            List<FullNode> fullNodes = new ArrayList<>();
            fullNodes.addAll(validFullNodes);
            fullNodes.addAll(candidateNodes);
            candidateNodes.forEach(n -> {
                n.setStatus(NodeStatus.HAS_CONSENSUSED);
                // 连接
                RegisterPrx prx = main.buildRegisterConnection2FullNode(n);
                while (null == prx) {
                    prx = main.buildRegisterConnection2FullNode(n);
                }
                // 添加全节点
                for(FullNode fullNode : fullNodes) {
                    prx.addNewFullNode(JSON.toJSONString(fullNode));
                }
                SqliteDAO.saveFullNode(main.parameters.dbFile, n);
            });
        }
    }

    /**
     * 广播全节点注销事件(全节点前3个注销都是通过广播确认的)
     */
    private void broadcastFullNodeLogoutEvent() {
        List<FullNode> validFullNodes = main.getFullNodeList().values().stream()
                .filter(BaseNode::checkIfConsensusNode)
                .collect(Collectors.toList());

        if (validFullNodes.size() < 4 ) {
            if (null != main.getPendingBlock()) {
                logger.warn("busy: some nodes are in consensus.");
                return;
            }

            List<FullNode> deleteNodes = main.getFullNodeList().values().parallelStream()
                    .filter(n -> n.getStatus()== NodeStatus.WAIT_DELETE_HAS_CONSENSUSED)
                    .collect(Collectors.toList());

            // 广播注销节点给所有全节点
            validFullNodes.forEach(node -> {
                // 连接
                RegisterPrx prx = main.buildRegisterConnection2FullNode(node);
                while (null == prx) {
                    prx = main.buildRegisterConnection2FullNode(node);
                }
                // 注销全节点
                for(FullNode deleteNode : deleteNodes) {
                    prx.logoutFullNode(deleteNode.getPubkey());
                }
            });
            // 更新本地列表
            deleteNodes.forEach(n -> main.getFullNodeList().remove(n.getPubkey()));
        }
    }

    /**
     * 更新全节点邻居列表
     * @param list 全节点邻居列表
     */
    private void updateFullNodeNeighborPools(Collection<Member> list) {
        FullNodeNeighborPools = list.stream()
                .filter(member -> main.parameters.whiteList.contains(member.address().host())
                        && !main.parameters.blackList.contains(member.address().host())
                        && checkFullNodePubkey(""+member.metadata().get("pubkey"))
                        && (NodeTypes.FULLNODE==Integer.parseInt(member.metadata().getOrDefault("level", "-1"))
                            || NodeTypes.SEED==Integer.parseInt(member.metadata().getOrDefault("level", "-1"))) )
                .collect( Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o ->
                                o.address().host()
                                        +"_"+o.address().port()
                                        +"_"+o.metadata().get("rpcPort")
                                        +"_"+o.metadata().get("httpPort")))),
                        ArrayList::new));

        logger.info("=== node-{} FullNodeNeighborPools size: {}",
                this.main.parameters.seedGossipAddress.gossipPort, FullNodeNeighborPools.size());

    }

    /**
     * 更新局部全节点邻居列表
     * @param list 局部全节点邻居列表
     */
    private void updateLocalFullNodeNeighborPools(Collection<Member> list) {
        list.stream().forEach((member) -> {
            if (!(main.parameters.whiteList.contains(member.address().host())
                    && !main.parameters.blackList.contains(member.address().host())
                    && checkLocalFullNodePubkey(""+member.metadata().get("pubkey"))
                    && NodeTypes.LOCALFULLNODE==Integer.parseInt(member.metadata().getOrDefault("level", "-1")))){
                logger.info("verbose seed updateLocalFullNodeNeighborPools - whitelist={}\nblacklist={}\npubkey={}\nLocalFullNodeList={}\nNodeTypes={}",
                        JSONObject.toJSONString(main.parameters.whiteList),
                        JSONObject.toJSONString(main.parameters.blackList),
                        member.metadata().get("pubkey"),
                        JSONObject.toJSONString(main.getLocalFullNodeList()),
                        member.metadata().getOrDefault("level", "-1")
                );
                logger.warn("denied was [{}],which is a bad sign",member.toString());

            }
        });

        LocalFullNodeNeighborPools = list.stream()
                .filter(member -> main.parameters.whiteList.contains(member.address().host())
                        && !main.parameters.blackList.contains(member.address().host())
                        && checkLocalFullNodePubkey(""+member.metadata().get("pubkey"))
                        && NodeTypes.LOCALFULLNODE==Integer.parseInt(member.metadata().getOrDefault("level", "-1")) )
                .collect( Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o ->
                                o.address().host()
                                        +"_"+o.address().port()
                                        +"_"+o.metadata().get("rpcPort")
                                        +"_"+o.metadata().get("httpPort")))),
                        ArrayList::new));

//        logger.info("=== node-{} LocalFullNodeNeighborPools size: {}",
//                this.main.parameters.seedGossipAddress.gossipPort, LocalFullNodeNeighborPools.size());

        logger.info("LFNNSIS - local full node neighbor size in seed(after filtering process) = {}",LocalFullNodeNeighborPools.size());
        LocalFullNodeNeighborPools.forEach(member -> {
            logger.info("{}:{}, rpcPort={}, httpPort={}, shardId={}, index={}",
                    member.address().host(), member.address().port(),
                    member.metadata().get("rpcPort"), member.metadata().get("httpPort"), member.metadata().get("shard"),
                    member.metadata().get("index"));
        });
    }

    /**
     * 校验公钥合法性
     * @param pubkey 公钥
     * @return 是否合法
     */
    private boolean checkFullNodePubkey(String pubkey) {
        if (StringUtils.isEmpty(pubkey)) {
            logger.error("pubkey is null.");
            return false;
        }
        return main.getFullNodeList().values().parallelStream()
                .filter(BaseNode::checkIfConsensusNode)
                .anyMatch(p->p.getPubkey().equals(pubkey));
    }

    /**
     * 校验公钥合法性
     * @param pubkey 公钥
     * @return 是否合法
     */
    private boolean checkLocalFullNodePubkey(String pubkey) {
        if (StringUtils.isEmpty(pubkey)) {
            logger.error("pubkey is null.");
            return false;
        }
        return main.getLocalFullNodeList().values().parallelStream()
                .filter(n -> n.getStatus()==NodeStatus.HAS_SHARDED)
                .anyMatch(p->p.getPubkey().equals(pubkey));
    }

}
