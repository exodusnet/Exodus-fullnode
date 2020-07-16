package org.exodus.bean.message;

import com.alibaba.fastjson.JSONObject;
import org.exodus.bean.wallet.KeyPair;
import org.exodus.core.Constant;
import org.exodus.mnemonic.Mnemonic;
import org.exodus.utils.CryptoUtil;
import org.exodus.utils.DSA;
import org.exodus.utils.SignUtil;
import org.exodus.utils.StringUtils;
import org.bitcoinj.core.ECKey;

import java.math.BigInteger;
import java.time.Instant;

/**
 * 快照消息
 * @author Clare
 * @date   2018/8/31 0031.
 */
public class SnapshotMessage extends BaseMessage {
    /**
     * 本快照版本号
     */
    private BigInteger snapVersion;
    /**
     * 上一个快照的Hash
     */
    private String preHash;
    /**
     * 本快照的Hash
     */
    private String hash;
    /**
     * 本快照对应的快照点
     */
    private SnapshotPoint snapshotPoint;
    /**
     * 所属打包Event的hash值
     */
    private String eHash;

    public SnapshotMessage() {
    }

    private SnapshotMessage(Builder builder) {
        setVers(builder.vers);
        setFromAddress(builder.fromAddress);
        setType(builder.type);
        setSignature(builder.signature);
        setPubkey(builder.pubkey);
        setTimestamp(builder.timestamp);
        setSnapVersion(builder.snapVersion);
        setPreHash(builder.preHash);
        setHash(builder.hash);
        setSnapshotPoint(builder.snapshotPoint);
        seteHash(builder.eHash);
    }

    public BigInteger getSnapVersion() {
        return snapVersion;
    }

    public void setSnapVersion(BigInteger snapVersion) {
        this.snapVersion = snapVersion;
    }

    public String getPreHash() {
        return preHash;
    }

    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public SnapshotPoint getSnapshotPoint() {
        return snapshotPoint;
    }

    public void setSnapshotPoint(SnapshotPoint snapshotPoint) {
        this.snapshotPoint = snapshotPoint;
    }

    public String geteHash() {
        return eHash;
    }

    public void seteHash(String eHash) {
        this.eHash = eHash;
    }

    /**
     * @param words 助记词
     * @param fromAddress 发送地址
     * @param snapVersion 快照版本
     * @param preHash 上一快照hash
     * @param snapshotPoint 当前快照点
     */
    public SnapshotMessage(String words, String fromAddress, BigInteger snapVersion, String preHash,
                           SnapshotPoint snapshotPoint) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==snapVersion || snapVersion.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("snapVersion is illegal.");
        }
        if (null==snapshotPoint) {
            throw new RuntimeException("snapshotPoint is illegal.");
        }
        this.setType(MessageType.SNAPSHOT.getIndex());
        this.setFromAddress(fromAddress);
        this.setSnapVersion(snapVersion);
        this.setPreHash(preHash);
        this.setSnapshotPoint(snapshotPoint);
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words,"");
    }

    /**
     * @param words 助记词
     * @param passphrase 生成助记词的随机字符串
     * @param fromAddress 发送地址
     * @param snapVersion 快照版本
     * @param preHash 上一快照hash
     * @param snapshotPoint 当前快照点
     */
    public SnapshotMessage(String words, String passphrase, String fromAddress, BigInteger snapVersion, String preHash,
                           SnapshotPoint snapshotPoint) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==snapVersion || snapVersion.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("snapVersion is illegal.");
        }
        if (null==snapshotPoint) {
            throw new RuntimeException("snapshotPoint is illegal.");
        }
        this.setType(MessageType.SNAPSHOT.getIndex());
        this.setFromAddress(fromAddress);
        this.setSnapVersion(snapVersion);
        this.setPreHash(preHash);
        this.setSnapshotPoint(snapshotPoint);
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words, (StringUtils.isEmpty(passphrase)) ? "" : passphrase);
    }

    /**
     * 对数据结构进行签名
     * @param words 助记词
     * @param passphrase 键入值
     * @throws Exception 异常
     */
    private void signMessage(String words, String passphrase) throws Exception {
        Mnemonic mn = new Mnemonic(words,passphrase);
        KeyPair keys = mn.getBIPpriKey(44,0,0,0,0);
        String pub = DSA.encryptBASE64(keys.getPublicKey());

        this.setPubkey(pub);
        this.setSignature(SignUtil.sign(getMessage(), ECKey.fromPrivate(keys.getPrivateKey())));
        this.hash = DSA.encryptBASE64(CryptoUtil.sha256hash(DSA.decryptBASE64(this.getSignature().substring(2))));
    }

    /**
     * 获取签名数据结构
     * @return 组装之后的message json串
     */
    public String getMessage() {
        if (StringUtils.isEmpty(this.getVers())) {
            return getMessageNoVersion();
        } else {
            String signature = this.getSignature();
            JSONObject json = new JSONObject();
            if (signature == null) {
                return mkMessageJson(signature).toJSONString();
            } else {
                json.put("message", mkMessageJson(signature));
                return json.toString();
            }
        }
    }

    private JSONObject mkMessageJson(String msgSignature) {
        JSONObject json = new JSONObject();
        if (StringUtils.isEmpty(this.getFromAddress()) ) {
            throw new RuntimeException("fromAddress is illegal.");
        } else {
            json.put("fromAddress", this.getFromAddress());
        }
        if (StringUtils.isNotEmpty(this.getPreHash()) ) {
            json.put("preHash", this.getPreHash());
        } else {
            json.put("preHash", null);
        }
        if (StringUtils.isEmpty(this.getPubkey()) ) {
            throw new RuntimeException("pubkey is illegal.");
        } else {
            json.put("pubkey", this.getPubkey());
        }
        if (null==this.getSnapshotPoint()) {
            throw new RuntimeException("snapshotPoint is illegal.");
        } else {
            json.put("snapshotPoint", this.getSnapshotPoint());
        }
        if (null==this.getSnapVersion() || this.getSnapVersion().compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("snapVersion is illegal.");
        } else {
            json.put("snapVersion", this.getSnapVersion());
        }
        json.put("timestamp", this.getTimestamp());
        if ( this.getType() != MessageType.SNAPSHOT.getIndex() ) {
            throw new RuntimeException("type is illegal.");
        } else {
            json.put("type", this.getType());
        }
        if (StringUtils.isNotEmpty(msgSignature)) {
            json.put("signature", msgSignature);
            json.put("hash", msgSignature);
        }
        json.put("vers", this.getVers());

        return json;
    }

    private String getMessageNoVersion() {
        JSONObject json = new JSONObject();
        StringBuilder message = new StringBuilder();
        if (StringUtils.isEmpty(this.getFromAddress()) ) {
            throw new RuntimeException("fromAddress is illegal.");
        } else {
            message.append("{\"fromAddress\":\"").append(this.getFromAddress());
        }
        message.append("\",\"preHash\":\"").append(this.getPreHash());
        if (StringUtils.isEmpty(this.getPubkey()) ) {
            throw new RuntimeException("pubkey is illegal.");
        } else {
            message.append("\",\"pubkey\":\"").append(this.getPubkey());
        }
        if (null==this.getSnapshotPoint()) {
            throw new RuntimeException("snapshotPoint is illegal.");
        } else {
            message.append("\",\"snapshotPoint\":\"").append(this.getSnapshotPoint());
        }
        if (null==this.getSnapVersion() || this.getSnapVersion().compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("snapVersion is illegal.");
        } else {
            message.append("\",\"snapVersion\":\"").append(this.getSnapVersion());
        }
        message.append("\",\"timestamp\":\"").append(this.getTimestamp());
        if ( this.getType() != MessageType.SNAPSHOT.getIndex() ) {
            throw new RuntimeException("type is illegal.");
        } else {
            message.append("\",\"type\":\"").append(this.getType());
        }

        if (StringUtils.isEmpty(this.getSignature())) {
            message.append("\"}");
            return message.toString();
        } else {
            message.append("\",\"signature\":\"").append(this.getSignature());
//            message.append("\",\"hash\":\"").append(this.getHash());
            message.append("\"}");

            json.put("message", message.toString());
            return json.toString();
        }
    }

    public static final class Builder {
        private String vers;
        private String fromAddress;
        private int type;
        private String signature;
        private String pubkey;
        private long timestamp;
        private BigInteger snapVersion;
        private String preHash;
        private String hash;
        private SnapshotPoint snapshotPoint;
        private String eHash;

        public Builder() {
        }

        public Builder vers(String val) {
            vers = val;
            return this;
        }

        public Builder fromAddress(String val) {
            fromAddress = val;
            return this;
        }

        public Builder type(int val) {
            type = val;
            return this;
        }

        public Builder signature(String val) {
            signature = val;
            return this;
        }

        public Builder pubkey(String val) {
            pubkey = val;
            return this;
        }

        public Builder timestamp(long val) {
            timestamp = val;
            return this;
        }

        public Builder snapVersion(BigInteger val) {
            snapVersion = val;
            return this;
        }

        public Builder preHash(String val) {
            preHash = val;
            return this;
        }

        public Builder hash(String val) {
            hash = val;
            return this;
        }

        public Builder snapshotPoint(SnapshotPoint val) {
            snapshotPoint = val;
            return this;
        }

        public Builder eHash(String val) {
            eHash = val;
            return this;
        }

        public SnapshotMessage build() {
            return new SnapshotMessage(this);
        }
    }
}
