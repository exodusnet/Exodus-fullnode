package org.exodus.bean.message;

import com.alibaba.fastjson.JSONObject;
import org.exodus.bean.wallet.KeyPair;
import org.exodus.core.Constant;
import org.exodus.mnemonic.Mnemonic;
import org.exodus.utils.DSA;
import org.exodus.utils.SignUtil;
import org.exodus.utils.StringUtils;
import org.bitcoinj.core.ECKey;

import java.math.BigInteger;
import java.time.Instant;

/**
 * 智能合约消息
 * @author Clare
 * @date   2018/8/31 0031.
 */
public class ContractMessage extends BaseMessage {
    /**
     * 合约相关内容
     */
    private byte[] data;
    /**
     * 合约消息hash值
     */
    private String hash;
    /**
     * 所属打包Event的hash值
     */
    private String eHash;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String geteHash() {
        return eHash;
    }

    public void seteHash(String eHash) {
        this.eHash = eHash;
    }

    public ContractMessage() {
    }

    public ContractMessage(String words, byte[] data, String fromAddress) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==data || data.length<=0) {
            throw new RuntimeException("data is illegal.");
        }
        this.setType(MessageType.CONTRACT.getIndex());
        this.setData(data);
        this.setTimestamp(Instant.now().toEpochMilli());
        this.setFromAddress(fromAddress);

        signMessage(words,"");
    }

    public ContractMessage(String words, String passphrase, byte[] data, String fromAddress) throws Exception {
        if (StringUtils.isEmpty(words) || words.split(" ").length != Constant.MNEMONIC_WORD_SIZE ) {
            throw new RuntimeException("words is illegal.");
        }
        if (StringUtils.isEmpty(fromAddress) ) {
            throw new RuntimeException("fromAddress is illegal.");
        }
        if (null==data || data.length<=0) {
            throw new RuntimeException("data is illegal.");
        }
        this.setData(data);
        this.setType(MessageType.CONTRACT.getIndex());
        this.setTimestamp(Instant.now().toEpochMilli());
        this.setFromAddress(fromAddress);

        signMessage(words, StringUtils.isEmpty(passphrase) ? "" : passphrase);
    }

    /**
     * 对数据结构进行签名
     * @param words 助记词
     * @param passphrase 键入值
     * @throws Exception
     */
    private void signMessage(String words, String passphrase) throws Exception {
        Mnemonic mn = new Mnemonic(words,passphrase);
        KeyPair keys = mn.getBIPpriKey(44,0,0,0,0);
        String pub = DSA.encryptBASE64(keys.getPublicKey());
        this.setPubkey(pub);
        this.setSignature(SignUtil.sign(this.getMessage(), ECKey.fromPrivate(keys.getPrivateKey())));
    }

    /**
     * 获取签名数据结构
     * @return 合约消息
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
        if ( this.getType() != MessageType.CONTRACT.getIndex() ) {
            throw new RuntimeException("type is illegal.");
        } else {
            json.put("type", this.getType());
        }
        if (null==this.getData() || this.getData().length <= 0 ) {
            throw new RuntimeException("data is illegal.");
        } else {
            json.put("data", DSA.encryptBASE64(this.getData()));
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
        if (null==this.getData() || this.getData().length <= 0 ) {
            throw new RuntimeException("data is illegal.");
        } else {
            message.append("\",\"data\":\"").append(DSA.encryptBASE64(this.getData()));
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
