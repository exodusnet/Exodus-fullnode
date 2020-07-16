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
 * 上链文本/上链数据消息
 * @author Clare
 * @date   2018/8/31 0031.
 */
public class TextMessage extends BaseMessage {
    /**
     * 整个消息的哈希值(type,fromAddress,toAddress,fee,timestamp,context)
     */
    private String hash;
    /**
     * 接收地址（可选字段）
     */
    private String toAddress;
    /**
     * 手续费（可选字段，根据场景和业务方需求设置；从1.1dev版本开始，单位由Atom改为NRG）
     */
    private BigInteger fee = BigInteger.ZERO;
    /**
     * NRG价格
     */
    private BigInteger nrgPrice = BigInteger.ZERO;
    /**
     * 上链文本/上链数据
     */
    private byte[] context;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigInteger getFee() {
        return fee;
    }

    public void setFee(BigInteger fee) {
        this.fee = fee;
    }

    public BigInteger getNrgPrice() {
        return nrgPrice;
    }

    public void setNrgPrice(BigInteger nrgPrice) {
        this.nrgPrice = nrgPrice;
    }

    public byte[] getContext() {
        return context;
    }

    public void setContext(byte[] context) {
        this.context = context;
    }

    public TextMessage() {
    }

    /**
     * @param words 主助记词
     * @param fromAddress 发送地址
     * @param context 文本数据、上链数据内容
     */
    public TextMessage(String words, String fromAddress, byte[] context) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==context || context.length<=0) {
            throw new RuntimeException("context is illegal.");
        }
        this.setType(MessageType.TEXT.getIndex());
        this.setFromAddress(fromAddress);
        this.setContext(context);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                throw new RuntimeException("fee is illegal.");
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                throw new RuntimeException("nrgPrice is illegal.");
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        signMessage(words,"");
    }

    /**
     * @param words 主助记词
     * @param fromAddress 发送地址
     * @param context 文本数据、上链数据内容
     * @param nrgPrice 手续费NRG价格
     */
    public TextMessage(String words, String fromAddress, byte[] context, BigInteger nrgPrice) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==context || context.length<=0) {
            throw new RuntimeException("context is illegal.");
        }
        this.setType(MessageType.TEXT.getIndex());
        this.setFromAddress(fromAddress);
        this.setContext(context);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                throw new RuntimeException("fee is illegal.");
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                BigInteger calcFee = FeeUtils.calculateFee(context.length);
                this.setFee(fee.compareTo(calcFee) > 0 ? fee : calcFee);
                if (null==nrgPrice || nrgPrice.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                }
                this.setNrgPrice(nrgPrice);
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        signMessage(words,"");
    }

    /**
     * @param words 主助记词
     * @param fromAddress 发送地址
     * @param context 文本数据、上链数据内容
     * @param passphrase 生成助记词时的密码串
     */
    public TextMessage(String words, String fromAddress, byte[] context, String passphrase) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==context || context.length<=0) {
            throw new RuntimeException("context is illegal.");
        }
        this.setType(MessageType.TEXT.getIndex());
        this.setFromAddress(fromAddress);
        this.setContext(context);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                throw new RuntimeException("fee is illegal.");
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                throw new RuntimeException("nrgPrice is illegal.");
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     * @param words 主助记词
     * @param fromAddress 发送地址
     * @param context 文本数据、上链数据内容
     * @param passphrase 生成助记词时的密码串
     */
    public TextMessage(String words, String fromAddress, byte[] context, String passphrase,
                       BigInteger nrgPrice) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==context || context.length<=0) {
            throw new RuntimeException("context is illegal.");
        }
        this.setType(MessageType.TEXT.getIndex());
        this.setFromAddress(fromAddress);
        this.setContext(context);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                throw new RuntimeException("fee is illegal.");
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                BigInteger calcFee = FeeUtils.calculateFee(context.length);
                this.setFee(fee.compareTo(calcFee) > 0 ? fee : calcFee);
                if (null==nrgPrice || nrgPrice.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                }
                this.setNrgPrice(nrgPrice);
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     * @param words 主助记词
     * @param fromAddress 发送地址
     * @param context 文本数据、上链数据内容
     * @param fee 数据上链手续费
     */
    public TextMessage(String words, String fromAddress, byte[] context,
                       BigInteger fee, BigInteger nrgPrice) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==context || context.length<=0) {
            throw new RuntimeException("context is illegal.");
        }
        this.setType(MessageType.TEXT.getIndex());
        this.setFromAddress(fromAddress);
        this.setContext(context);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                if (null==fee || fee.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    this.setFee(fee);
                }
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                BigInteger calcFee = FeeUtils.calculateFee(context.length);
                this.setFee(fee.compareTo(calcFee) > 0 ? fee : calcFee);
                if (null==nrgPrice || nrgPrice.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                }
                this.setNrgPrice(nrgPrice);
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        signMessage(words,"");
    }

    /**
     * @param words 主助记词
     * @param fromAddress 发送地址
     * @param context 文本数据、上链数据内容
     * @param fee 数据上链手续费
     * @param passphrase 生成助记词时的密码串
     */
    public TextMessage(String words, String passphrase, String fromAddress, byte[] context,
                       BigInteger fee) throws Exception {
        if (null!=fee && !fee.equals(BigInteger.ZERO)) {
            throw new RuntimeException("nrgPrice is illegal.");
        }
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==context || context.length<=0) {
            throw new RuntimeException("context is illegal.");
        }
        this.setType(MessageType.TEXT.getIndex());
        this.setFromAddress(fromAddress);
        this.setContext(context);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                if (null==fee || fee.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    this.setFee(fee);
                }
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                throw new RuntimeException("nrgPrice is illegal.");
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     * @param words 主助记词
     * @param fromAddress 发送地址
     * @param context 文本数据、上链数据内容
     * @param fee 数据上链手续费
     * @param passphrase 生成助记词时的密码串
     */
    public TextMessage(String words, String passphrase, String fromAddress, byte[] context,
                       BigInteger fee, BigInteger nrgPrice) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==context || context.length<=0) {
            throw new RuntimeException("context is illegal.");
        }
        this.setType(MessageType.TEXT.getIndex());
        this.setFromAddress(fromAddress);
        this.setContext(context);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                if (null==fee || fee.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    this.setFee(fee);
                }
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                BigInteger calcFee = FeeUtils.calculateFee(context.length);
                this.setFee(fee.compareTo(calcFee) > 0 ? fee : calcFee);
                if (null==nrgPrice || nrgPrice.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                }
                this.setNrgPrice(nrgPrice);
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     * @param fromAddress 发送地址
     * @param toAddress 接收地址(可选)
     * @param fee 费用
     */
    public TextMessage(String words, String passphrase, String fromAddress, byte[] context,
                       BigInteger fee, String toAddress) throws Exception {
        if (null!=fee && !fee.equals(BigInteger.ZERO)) {
            throw new RuntimeException("nrgPrice is illegal.");
        }
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==context || context.length<=0) {
            throw new RuntimeException("context is illegal.");
        }
        this.setType(MessageType.TEXT.getIndex());
        this.setFromAddress(fromAddress);
        this.setContext(context);
        this.setToAddress(toAddress);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                if (null==fee || fee.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    this.setFee(fee);
                }
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                throw new RuntimeException("nrgPrice is illegal.");
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        signMessage(words, (StringUtils.isEmpty(passphrase)) ? "" : passphrase);
    }

    /**
     * @param fromAddress 发送地址
     * @param toAddress 接收地址(可选)
     * @param fee 费用
     */
    public TextMessage(String words, String passphrase, String fromAddress, byte[] context,
                       BigInteger fee, String toAddress, BigInteger nrgPrice) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==context || context.length<=0) {
            throw new RuntimeException("context is illegal.");
        }
        this.setType(MessageType.TEXT.getIndex());
        this.setFromAddress(fromAddress);
        this.setContext(context);
        this.setToAddress(toAddress);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                if (null==fee || fee.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    this.setFee(fee);
                }
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                BigInteger calcFee = FeeUtils.calculateFee(context.length);
                this.setFee(fee.compareTo(calcFee) > 0 ? fee : calcFee);
                if (null==nrgPrice || nrgPrice.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                }
                this.setNrgPrice(nrgPrice);
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        signMessage(words, (StringUtils.isEmpty(passphrase)) ? "" : passphrase);
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
            if ( StringUtils.isEmpty(signature)) {
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
        if (null==this.getContext() || this.getContext().length <= 0 ) {
            throw new RuntimeException("context is illegal.");
        } else {
            json.put("context", DSA.encryptBASE64(this.getContext()));
        }
        if ( Instant.now().toEpochMilli() - this.getTimestamp() > Constant.MESSAGE_PRESEND_TIME_OUT
                || this.getTimestamp() > Instant.now().toEpochMilli() ) {
            throw new RuntimeException("timestamp is illegal.");
        } else {
            json.put("timestamp", this.getTimestamp());
        }
        if (StringUtils.isEmpty(this.getPubkey()) ) {
            throw new RuntimeException("pubkey is illegal.");
        } else {
            json.put("pubkey", this.getPubkey());
        }
        if ( this.getType() != MessageType.TEXT.getIndex() ) {
            throw new RuntimeException("type is illegal.");
        } else {
            json.put("type", this.getType());
        }
        if (StringUtils.isNotEmpty(this.getToAddress())) {
            json.put("toAddress", this.getToAddress());
        }
        if (MessageVersion.DEV_1_0.equals(this.getVers())) {
            if (null==this.getFee() || this.getFee().compareTo(BigInteger.ZERO) <=0) {
                throw new RuntimeException("fee is illegal.");
            } else {
                json.put("fee", this.getFee().toString());
            }
        } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
            if(Constant.NEED_FEE) {
                if (null==this.getFee() || this.getFee().compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    json.put("fee", this.getFee().toString());
                }
                if (null==this.getNrgPrice() || this.getNrgPrice().compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                } else {
                    json.put("nrgPrice", this.getNrgPrice().toString());
                }
            }
        }
        if (StringUtils.isNotEmpty(msgSignature)) {
            json.put("signature", msgSignature);
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
        if (null==this.getContext() || this.getContext().length <= 0 ) {
            throw new RuntimeException("context is illegal.");
        } else {
            message.append("\",\"context\":\"").append(DSA.encryptBASE64(this.getContext()));
        }
        if ( Instant.now().toEpochMilli() - this.getTimestamp() > Constant.MESSAGE_PRESEND_TIME_OUT
                || this.getTimestamp() > Instant.now().toEpochMilli() ) {
            throw new RuntimeException("timestamp is illegal.");
        } else {
            message.append("\",\"timestamp\":\"").append(this.getTimestamp());
        }
        if (StringUtils.isEmpty(this.getPubkey()) ) {
            throw new RuntimeException("pubkey is illegal.");
        } else {
            message.append("\",\"pubkey\":\"").append(this.getPubkey());
        }
        if ( this.getType() != MessageType.CONTRACT.getIndex() ) {
            throw new RuntimeException("type is illegal.");
        } else {
            message.append("\",\"type\":\"").append(this.getType());
        }
        if (StringUtils.isNotEmpty(this.getToAddress())) {
            message.append("\",\"toAddress\":\"").append(this.getToAddress());
        }
        if (null!=this.getFee() && this.getFee().compareTo(BigInteger.ZERO)>0) {
            message.append("\",\"fee\":\"").append(this.getFee());
        }

        if (StringUtils.isEmpty(this.getSignature())) {
            message.append("\"}");
            return message.toString();
        } else {
            message.append("\",\"signature\":\"").append(this.getSignature());
            message.append("\"}");

            json.put("message", message.toString());
            return json.toString();
        }
    }

}
