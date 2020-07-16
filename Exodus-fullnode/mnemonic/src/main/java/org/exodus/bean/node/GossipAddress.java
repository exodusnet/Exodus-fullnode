package org.exodus.bean.node;

/**
 * 节点地址
 * @author Clare
 * @date   2018/6/10.
 */
public class GossipAddress {

    public String ip;
    public String pubIP;
    public int rpcPort;
    public int httpPort;
    public int gossipPort;

    public GossipAddress() {
    }

    private GossipAddress(Builder builder) {
        ip = builder.ip;
        pubIP = builder.pubIP;
        rpcPort = builder.rpcPort;
        gossipPort = builder.gossipPort;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPubIP() {
        return pubIP;
    }

    public void setPubIP(String pubIP) {
        this.pubIP = pubIP;
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

    public int getGossipPort() {
        return gossipPort;
    }

    public void setGossipPort(int gossipPort) {
        this.gossipPort = gossipPort;
    }

    public static final class Builder {
        private String ip;
        private String pubIP;
        private int rpcPort;
        private int gossipPort;

        public Builder() {
        }

        public Builder ip(String val) {
            ip = val;
            return this;
        }

        public Builder pubIP(String val) {
            pubIP = val;
            return this;
        }

        public Builder rpcPort(int val) {
            rpcPort = val;
            return this;
        }

        public Builder gossipPort(int val) {
            gossipPort = val;
            return this;
        }

        public GossipAddress build() {
            return new GossipAddress(this);
        }
    }
}
