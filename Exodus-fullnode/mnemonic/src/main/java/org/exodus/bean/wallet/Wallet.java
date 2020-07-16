package org.exodus.bean.wallet;

/**
 * 钱包
 * @author Clare
 * @date   2018/6/10.
 */
public class Wallet {
    /**
     * 助记词
     */
    public String mnemonic;
    /**
     * 收款地址
     */
    public String address;
    /**
     * 主密钥对
     */
    public Keys keys;
    /**
     * 扩展密钥对
     */
    public Keys extKeys;

    public Wallet() {
    }

    private Wallet(Builder builder) {
        setMnemonic(builder.mnemonic);
        setAddress(builder.address);
        setKeys(builder.keys);
        setExtKeys(builder.extKeys);
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Keys getKeys() {
        return keys;
    }

    public void setKeys(Keys keys) {
        this.keys = keys;
    }

    public Keys getExtKeys() {
        return extKeys;
    }

    public void setExtKeys(Keys extKeys) {
        this.extKeys = extKeys;
    }

    public static final class Builder {
        private String mnemonic;
        private String address;
        private Keys keys;
        private Keys extKeys;

        public Builder() {
        }

        public Builder mnemonic(String val) {
            mnemonic = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Builder keys(Keys val) {
            keys = val;
            return this;
        }

        public Builder extKeys(Keys val) {
            extKeys = val;
            return this;
        }

        public Wallet build() {
            return new Wallet(this);
        }
    }
}
