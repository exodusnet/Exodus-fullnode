package org.exodus.bean.wallet;

/**
 * 密钥
 * @author Clare
 * @date   2018/6/10.
 */
public class Keys {
    /**
     * 钱包公钥
     */
    private String pubKey;
    /**
     * 钱包私钥
     */
    private String privKey;

    public Keys() {
    }

    private Keys(Builder builder) {
        setPubKey(builder.pubKey);
        setPrivKey(builder.privKey);
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }


    public String getPrivKey() {
        return privKey;
    }

    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }

    public static final class Builder {
        private String pubKey;
        private String privKey;

        public Builder() {
        }

        public Builder pubKey(String val) {
            pubKey = val;
            return this;
        }

        public Builder privKey(String val) {
            privKey = val;
            return this;
        }

        public Keys build() {
            return new Keys(this);
        }
    }
}
