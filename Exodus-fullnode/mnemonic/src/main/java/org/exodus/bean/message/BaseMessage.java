package org.exodus.bean.message;

/**
 * @author Clare
 * @date   2018/8/31 0031
 */
public class BaseMessage {
    /**
     * 类型: 1-trans , 2-contract, 3-shapshot, 4-text
     */
    private int type;
    /**
     * 交易消息版本号（为了兼容性考虑）
     */
    private String vers = MessageVersion.PRO_2_0;
    /**
     * 发送地址
     */
    private String fromAddress;
    /**
     * 签名
     */
    private String signature;
    /**
     * 公钥
     */
    private String pubkey;
    /**
     * 时间戳
     */
    private long timestamp = 0L;

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPubkey() {
        return pubkey;
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getVers() {
        return vers;
    }

    public void setVers(String vers) {
        this.vers = vers;
    }
}
