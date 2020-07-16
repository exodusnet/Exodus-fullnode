package org.exodus.bean.wallet;

/**
 * @Author: zz
 * @Description: 公私钥对
 * @Date: 上午 11:41 2018/10/29 0029
 * @Modified By
 */
public class KeyPair {

    private byte[] privateKey;
    private byte[] publicKey;

    public KeyPair() {
    }

    public KeyPair(byte[] publicKey, byte[] privateKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }


    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
}
