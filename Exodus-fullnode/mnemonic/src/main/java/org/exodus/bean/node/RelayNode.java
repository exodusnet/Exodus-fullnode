package org.exodus.bean.node;

import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

/**
 * 节点
 * @author Clare
 * @date   2018/11/19 0029.
 */
public class RelayNode extends BaseNode {
    /**
     * 对应区块hash值(变更节点信息会更新)
     */
    private String hash;
    /**
     * 兑换比例
     */
    private HashMap<String, String> exchangeRatios;
    /**
     * 中继节点各主链币种地址
     * key-INVE, BTC, ETH, SNC
     * value-地址
     */
    private HashMap<String, String> addresses;
    /**
     * 跨链交易手续费率（取值范围：0.0-1.0, 精度：小数点6位）
     */
    private double feeRatio;
    /**
     * 上次更新时间戳
     */
    private long registerTimestamp;
    /**
     * 上次更新时间戳
     */
    private long lastAliveTimestamp;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 告警联系电话
     */
    private String phone;
    /**
     * 告警联系邮箱
     */
    private String email;

    public RelayNode() {
    }

    private RelayNode(Builder builder) {
        setType(builder.type);
        setStatus(builder.status);
        setPubkey(builder.pubkey);
        setAddress(builder.address);
        setIp(builder.ip);
        setRpcPort(builder.rpcPort);
        setHttpPort(builder.httpPort);
        setUrl(builder.url);
        setHash(builder.hash);
        setExchangeRatios(builder.exchangeRatios);
        setAddresses(builder.addresses);
        setFeeRatio(builder.feeRatio);
        setRegisterTimestamp(builder.registerTimestamp);
        setLastAliveTimestamp(builder.lastAliveTimestamp);
        setName(builder.name);
        setPhone(builder.phone);
        setEmail(builder.email);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public HashMap<String, String> getExchangeRatios() {
        return exchangeRatios;
    }

    public void setExchangeRatios(HashMap<String, String> exchangeRatios) {
        this.exchangeRatios = exchangeRatios;
    }

    public HashMap<String, String> getAddresses() {
        return addresses;
    }

    public void setAddresses(HashMap<String, String> addresses) {
        this.addresses = addresses;
    }

    public double getFeeRatio() {
        return feeRatio;
    }

    public void setFeeRatio(double feeRatio) {
        this.feeRatio = feeRatio;
    }

    public long getRegisterTimestamp() {
        return registerTimestamp;
    }

    public void setRegisterTimestamp(long registerTimestamp) {
        this.registerTimestamp = registerTimestamp;
    }

    public long getLastAliveTimestamp() {
        return lastAliveTimestamp;
    }

    public void setLastAliveTimestamp(long lastAliveTimestamp) {
        this.lastAliveTimestamp = lastAliveTimestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RelayNode relayNode = (RelayNode) o;
        return Objects.equals(getPubkey(), relayNode.getPubkey()) &&
                Objects.equals(getIp(), relayNode.getIp()) &&
                Objects.equals(getHttpPort(), relayNode.getHttpPort()) &&
                Objects.equals(getFeeRatio(), relayNode.getFeeRatio()) &&
                Objects.equals(getAddresses(), relayNode.getAddresses()) &&
                Objects.equals(name, relayNode.name) &&
                Objects.equals(phone, relayNode.phone) &&
                Objects.equals(email, relayNode.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getPubkey(), this.getIp(), this.getHttpPort(), this.getFeeRatio(), name, phone, email);
    }

    public static boolean checkIfAliveNode(RelayNode n, long interval) {
        return Instant.now().toEpochMilli()-n.getLastAliveTimestamp() < interval;
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
        private HashMap<String, String> exchangeRatios;
        private HashMap<String, String> addresses;
        private double feeRatio;
        private long registerTimestamp;
        private long lastAliveTimestamp;
        private String name;
        private String phone;
        private String email;

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

        public Builder exchangeRatios(HashMap<String, String> val) {
            exchangeRatios = val;
            return this;
        }

        public Builder addresses(HashMap<String, String> val) {
            addresses = val;
            return this;
        }

        public Builder feeRatio(double val) {
            feeRatio = val;
            return this;
        }

        public Builder registerTimestamp(long val) {
            registerTimestamp = val;
            return this;
        }

        public Builder lastAliveTimestamp(long val) {
            lastAliveTimestamp = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public RelayNode build() {
            return new RelayNode(this);
        }
    }
}
