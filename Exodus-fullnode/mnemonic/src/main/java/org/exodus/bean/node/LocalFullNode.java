package org.exodus.bean.node;

/**
 * 节点
 * @author Clare
 * @date   2018/11/19 0029.
 */
public class LocalFullNode extends BaseNode {
    /**
     * 对应区块hash值(变更节点信息会更新)
     */
    private String hash;
    /**
     * 节点所属分片ID
     */
    private String shard;
    /**
     * 节点在所属分片内的索引
     */
    private String index;

    public LocalFullNode() {
    }

    private LocalFullNode(Builder builder) {
        setType(builder.type);
        setStatus(builder.status);
        setPubkey(builder.pubkey);
        setAddress(builder.address);
        setIp(builder.ip);
        setRpcPort(builder.rpcPort);
        setHttpPort(builder.httpPort);
        setUrl(builder.url);
        setHash(builder.hash);
        setShard(builder.shard);
        setIndex(builder.index);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getShard() {
        return shard;
    }

    public void setShard(String shard) {
        this.shard = shard;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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
        private String shard;
        private String index;

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

        public Builder shard(String val) {
            shard = val;
            return this;
        }

        public Builder index(String val) {
            index = val;
            return this;
        }

        public LocalFullNode build() {
            return new LocalFullNode(this);
        }
    }
}
