package org.exodus.bean.node;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 节点
 * @author Clare
 * @date   2018/11/19 0029.
 */
public class BaseNode {
    private static final Logger logger = LoggerFactory.getLogger("BaseNode.class");

    /**
     * 节点类型: NodeType
     */
    private int type;
    /**
     /**
     * 状态: NodeStates
     */
    private int status;
    /**
     * 公钥
     */
    private String pubkey;
    /**
     * 收款地址
     */
    private String address;
    /**
     * 公网ip地址
     */
    private String ip;
    /**
     * rpc服务端口
     */
    private int rpcPort;
    /**
     * http服务端口
     */
    private int httpPort;
    /**
     * http服务域名
     */
    private String url;

    public BaseNode() {
    }

    private BaseNode(Builder builder) {
        setType(builder.type);
        setStatus(builder.status);
        setPubkey(builder.pubkey);
        setAddress(builder.address);
        setIp(builder.ip);
        setRpcPort(builder.rpcPort);
        setHttpPort(builder.httpPort);
        setUrl(builder.url);
    }

    /**
     * 节点转换
     * @param data 节点json对象
     * @return 节点对象
     * @throws JSONException 异常
     */
    public static BaseNode nodeConvert(JSONObject data) throws JSONException {
        int type = 0;
        try {
            type = data.getInteger("type");
        } catch (JSONException e) {
            logger.error("{}", e);
        }
        if (NodeTypes.FULLNODE == type){
            return JSONObject.toJavaObject(data, FullNode.class);
        }
        if (NodeTypes.RELAYNODE == type){
            return JSONObject.toJavaObject(data, RelayNode.class);
        }
        if (NodeTypes.LOCALFULLNODE == type){
            return JSONObject.toJavaObject(data, LocalFullNode.class);
        }
        return JSONObject.toJavaObject(data, BaseNode.class);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRpcPort() {
        return rpcPort;
    }

    public void setRpcPort(int rpcPort) {
        this.rpcPort = rpcPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public static final class Builder {
        private int type;
        private int status;
        private String pubkey;
        private String address;
        private String ip;
        private int rpcPort;
        private int httpPort;
        private String url;

        public Builder() {
        }

        public Builder type(int val) {
            type = val;
            return this;
        }

        public Builder status(int val) {
            status = val;
            return this;
        }

        public Builder pubkey(String val) {
            pubkey = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder ip(String val) {
            ip = val;
            return this;
        }

        public Builder rpcPort(int val) {
            rpcPort = val;
            return this;
        }

        public Builder httpPort(int val) {
            httpPort = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public BaseNode build() {
            return new BaseNode(this);
        }
    }

    public static boolean checkIfWaiConsensusNode(BaseNode n) {
        return n.getStatus()==NodeStatus.WAIT_CONSENSUS
                || n.getStatus()==NodeStatus.WAIT_DELETE_HAS_CONSENSUSED
                || n.getStatus()==NodeStatus.WAIT_DELETE_IN_CONSENSUS
                || n.getStatus()==NodeStatus.WAIT_UPDATE_HAS_CONSENSUSED
                || n.getStatus()==NodeStatus.WAIT_UPDATE_IN_CONSENSUS;
    }

    public static boolean checkIfConsensusNode(BaseNode n) {
        return n.getStatus()== NodeStatus.HAS_SHARDED
                || n.getStatus()== NodeStatus.HAS_CONSENSUSED
                || n.getStatus()==NodeStatus.WAIT_UPDATE_HAS_CONSENSUSED
                || n.getStatus()==NodeStatus.UPDATTING_HAS_CONSENSUSED;
    }
}
