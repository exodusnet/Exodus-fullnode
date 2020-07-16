package org.exodus.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.exodus.bean.node.BaseNode;
import org.exodus.bean.node.FullNode;
import org.exodus.node.GeneralNode;
import org.exodus.rpc.fullnode.RegisterPrx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This handles sending messages.
 */
public class Sender {
    private static final Logger logger = LoggerFactory.getLogger("Sender.class");

    GeneralNode node;

    public Sender() {
    }

    public Sender(GeneralNode node) {
        this.node = node;
    }

    /**
     * Make the message object to a string and give it to the method broadcast.
     * @param msg object from message with specific data.
     */
    public void sendMessage(Message msg) {
        String json = msg.messageEncode().toString();
        broadcast(json);
    }

    /**
     * Send broadcast messages in the network of registered nodes.
     * @param message String with the specific message (init, void...).
     */
    public void broadcast(String message) {
        List<FullNode> fullnodes = this.node.getFullNodeList().values().stream()
                .filter(BaseNode::checkIfConsensusNode).collect(Collectors.toList());

        JSONObject json = JSON.parseObject(message);
        Message msg = Message.messageConvert(json);
        logger.info("=== msg type: {}", msg.getType());

        fullnodes.parallelStream().forEach(fullNode -> {
            logger.info("=== send {} msg to {}:{}", msg.getTypeString(), fullNode.getIp(), fullNode.getRpcPort());
            Boolean sent = false;
            while(!sent){
                try {
                    RegisterPrx prx = buildRegisterConnection2FullNode(fullNode);
                    if (null!=prx) {
                        prx.sendPbftMessage(message);
                        sent = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 与全节点建立register rpc连接
     * @param fullNode 全节点
     * @return register rpc连接
     */
    public RegisterPrx buildRegisterConnection2FullNode(FullNode fullNode) {
        logger.info("buildRegisterConnection2FullNode...");
        RegisterPrx prx = null;
        try {
            prx = RegisterPrx.checkedCast(node.getCommunicator().stringToProxy(
                    "Register:default -h " + fullNode.getIp() + " -p " + fullNode.getRpcPort()));
        } catch (Exception e) {
            logger.error("{}", e);
        }
        if (null == prx) {
            logger.error(">>>>>> Invalid full node Connnection Object.");
        }
        return prx;
    }
}
