package org.exodus.bean.node;

/**
 * 节点
 * @author Clare
 * @date   2018/11/19 0029.
 */
public class FullNode extends BaseNode {
    /**
     * 对应区块hash值(变更节点信息会更新)
     */
    private String hash;

    public FullNode() {
    }

    private FullNode(Builder builder) {
        setType(builder.type);
        setStatus(builder.status);
        setPubkey(builder.pubkey);
        setAddress(builder.address);
        setIp(builder.ip);
        setRpcPort(builder.rpcPort);
        setHttpPort(builder.httpPort);
        setUrl(builder.url);
        setHash(builder.hash);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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
        private String hash;

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

        public Builder hash(String val) {
            hash = val;
            return this;
        }

        public FullNode build() {
            return new FullNode(this);
        }
    }
}
