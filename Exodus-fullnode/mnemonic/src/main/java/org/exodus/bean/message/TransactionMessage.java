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
 * @author Clare
 * @date   2018/8/31 0031.
 */
public class TransactionMessage extends BaseMessage {
    /**
     * 接收地址
     */
    private String toAddress;
    /**
     * 交易消息内容hash值
     */
    private String hash;
    /**
     * 交易金额
     */
    private BigInteger amount = BigInteger.ZERO;
    /**
     * 交易手续费（从1.1dev版本开始，单位由Atom改为NRG）
     */
    private BigInteger fee = BigInteger.ZERO;
    /**
     * NRG价格
     */
    private BigInteger nrgPrice = BigInteger.ZERO;
    /**
     * 交易附言(限制50)
     */
    private String remark = null;
    /**
     * 交易ID(构建交易时不需要,但是内部解析入库前需要用)
     */
    private BigInteger id = BigInteger.ZERO;
    /**
     * 所属打包Event的hash值
     */
    private String eHash;

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String geteHash() {
        return eHash;
    }

    public void seteHash(String eHash) {
        this.eHash = eHash;
    }

    public TransactionMessage() {
    }

    /**
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     */
    public TransactionMessage(String words, String fromAddress, String toAddress, BigInteger amount) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark("");
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                throw new RuntimeException("fee is illegal.");
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                throw new RuntimeException("nrgPrice is illegal.");
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words,"");
    }

    /**
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param fee 费用
     */
    public TransactionMessage(String words, String fromAddress, String toAddress, BigInteger amount,
                              BigInteger fee, BigInteger nrgPrice) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark("");
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                if (null==fee || fee.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    this.setFee(fee);
                }
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                BigInteger calcFee = FeeUtils.calculateFee(0);
                this.setFee(fee.compareTo(calcFee) > 0 ? fee : calcFee);
                if (null==nrgPrice || nrgPrice.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                }
                this.setNrgPrice(nrgPrice);
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words,"");
    }

    /**
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param fee 费用
     */
    public TransactionMessage(String words, String fromAddress, String toAddress, BigInteger amount,
                              BigInteger fee) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark("");
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

        this.signMessage(words,"");
    }

    /**
     *
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param remark 附带留言信息
     */
    public TransactionMessage(String words, String fromAddress, String toAddress, BigInteger amount,
                              String remark) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark(remark);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                throw new RuntimeException("fee is illegal.");
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                throw new RuntimeException("nrgPrice is illegal.");
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words,"");
    }

    /**
     *
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param fee 费用
     * @param remark 附带留言信息
     */
    public TransactionMessage(String words, String fromAddress, String toAddress, BigInteger amount,
                              BigInteger fee, String remark) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark(remark);
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

        this.signMessage(words,"");
    }

    /**
     *
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param fee 费用
     * @param remark 附带留言信息
     */
    public TransactionMessage(String words, String fromAddress, String toAddress, BigInteger amount,
                              BigInteger fee, BigInteger nrgPrice, String remark) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark(remark);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                if (null==fee || fee.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    this.setFee(fee);
                }
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                BigInteger calcFee = FeeUtils.calculateFee(StringUtils.isEmpty(remark)?0:remark.length());
                this.setFee(fee.compareTo(calcFee) > 0 ? fee : calcFee);
                if (null==nrgPrice || nrgPrice.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                }
                this.setNrgPrice(nrgPrice);
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words,"");
    }

    /**
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     */
    public TransactionMessage(String words, String passphrase, String fromAddress, String toAddress, BigInteger amount) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark("");
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                throw new RuntimeException("fee is illegal.");
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                throw new RuntimeException("nrgPrice is illegal.");
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param fee 费用
     */
    public TransactionMessage(String words, String passphrase, String fromAddress, String toAddress, BigInteger amount,
                              BigInteger fee, BigInteger nrgPrice) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark("");
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                if (null==fee || fee.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    this.setFee(fee);
                }
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                BigInteger calcFee = FeeUtils.calculateFee(0);
                this.setFee(fee.compareTo(calcFee) > 0 ? fee : calcFee);
                if (null==nrgPrice || nrgPrice.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                }
                this.setNrgPrice(nrgPrice);
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param fee 费用
     */
    public TransactionMessage(String words, String passphrase, String fromAddress, String toAddress,
                              BigInteger amount, BigInteger fee) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark("");
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

        this.signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     *
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param remark 附带留言信息
     */
    public TransactionMessage(String words, String passphrase, String fromAddress, String toAddress,
                              BigInteger amount, String remark) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark(remark);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                throw new RuntimeException("fee is illegal.");
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                throw new RuntimeException("nrgPrice is illegal.");
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     *
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param fee 费用
     * @param remark 附带留言信息
     */
    public TransactionMessage(String words, String passphrase, String fromAddress, String toAddress,
                              BigInteger amount, BigInteger fee, String remark) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark(remark);
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

        this.signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     *
     * @param fromAddress 发送地址
     * @param toAddress 接收地址
     * @param amount 金额
     * @param fee 费用
     * @param remark 附带留言信息
     */
    public TransactionMessage(String words, String passphrase, String fromAddress, String toAddress,
                              BigInteger amount, BigInteger fee, BigInteger nrgPrice, String remark) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (StringUtils.isEmpty(toAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==amount || amount.compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        }
        this.setType(MessageType.TRANSACTIONS.getIndex());
        this.setFromAddress(fromAddress);
        this.setToAddress(toAddress);
        this.setAmount(amount);
        this.setRemark(remark);
        if (Constant.NEED_FEE) {
            if (MessageVersion.DEV_1_0.equals(this.getVers())) {
                if (null==fee || fee.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("fee is illegal.");
                } else {
                    this.setFee(fee);
                }
            } else if (MessageVersion.PRO_2_0.equals(this.getVers())) {
                BigInteger calcFee = FeeUtils.calculateFee(StringUtils.isEmpty(remark)?0:remark.length());
                this.setFee(fee.compareTo(calcFee) > 0 ? fee : calcFee);
                if (null==nrgPrice || nrgPrice.compareTo(BigInteger.ZERO) <=0) {
                    throw new RuntimeException("nrgPrice is illegal.");
                }
                this.setNrgPrice(nrgPrice);
            }
        }
        this.setTimestamp(Instant.now().toEpochMilli());

        this.signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
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
        if (StringUtils.isEmpty(this.getToAddress()) ) {
            throw new RuntimeException("fromAddress is illegal.");
        } else {
            json.put("toAddress", this.getToAddress());
        }
        if (null==this.getAmount() || this.getAmount().compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        } else {
            json.put("amount", this.getAmount().toString());
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
        if ( this.getType() != MessageType.TRANSACTIONS.getIndex() ) {
            throw new RuntimeException("type is illegal.");
        } else {
            json.put("type", this.getType());
        }
        json.put("remark", this.getRemark());
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
        if (StringUtils.isEmpty(this.getToAddress()) ) {
            throw new RuntimeException("fromAddress is illegal.");
        } else {
            message.append("\",\"toAddress\":\"").append(this.getToAddress());
        }
        if (null==this.getAmount() || this.getAmount().compareTo(BigInteger.ZERO)<=0) {
            throw new RuntimeException("amount is illegal.");
        } else {
            message.append("\",\"amount\":\"").append(this.getAmount());
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
        message.append("\",\"fee\":\"").append(this.getFee());
        if ( this.getType() != MessageType.TRANSACTIONS.getIndex() ) {
            throw new RuntimeException("type is illegal.");
        } else {
            message.append("\",\"type\":\"").append(this.getType());
        }
        message.append("\",\"remark\":\"").append(this.getRemark());

        if ( StringUtils.isEmpty(this.getSignature()) ) {
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
