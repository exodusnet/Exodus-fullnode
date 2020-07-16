package org.exodus.bean.wallet;

import org.exodus.mnemonic.Mnemonic;
import org.exodus.utils.DSA;

import java.math.BigInteger;
import java.util.stream.Collectors;

/**
 * 钱包工具包
 * @author Clare
 * @date   2018/6/28 0028.
 */
public class WalletBuilder {

    public static Mnemonic getMnemonicStruct() throws Exception {
        return new Mnemonic("", "");
    }
    public static Mnemonic getMnemonicStruct(String word) throws Exception {
        return new Mnemonic(word, "");
    }
    public static Mnemonic getMnemonicStruct(String word, String randomStr) throws Exception {
        return new Mnemonic(word, randomStr);
    }

    public static Wallet generateWallet() throws Exception {
        return generateWallet("", "");
    }

    public static Wallet generateWallet(String word) throws Exception {
        return generateWallet(word, "");
    }

    public static Wallet generateWallet(String word, String randomStr) throws Exception {
        // 助记词
        Mnemonic mnemonic = getMnemonicStruct(word, randomStr);
        // 主密钥对
        String privkey  = DSA.encryptBASE64(mnemonic.getxPrivKey());
        String pubkey  = DSA.encryptBASE64(mnemonic.getPubKey());
        Keys keys = new Keys.Builder()
                .pubKey(pubkey)
                .privKey(privkey)
                .build();
        // 扩展秘钥对
        KeyPair extendKeys = mnemonic.getBIPpriKey(44, 0, 0, 0, 0);
        String extPubkey = DSA.encryptBASE64(extendKeys.getPublicKey());
        String extPrivkey = DSA.encryptBASE64(extendKeys.getPrivateKey());
        Keys extKeys = new Keys.Builder()
                .pubKey(extPubkey)
                .privKey(extPrivkey)
                .build();
        // 钱包账户地址
        String address = mnemonic.getAddress();

        // 钱包
        Wallet wallet = new Wallet.Builder()
                .mnemonic(mnemonic.getMnemonic().stream().collect(Collectors.joining(" ")))
                .keys(keys)
                .extKeys(extKeys)
                .address(address)
                .build();
        return wallet;
    }

    public static String generateNewAddress(String word, String randomStr, int index) throws Exception {
        // 助记词
        Mnemonic mnemonic = getMnemonicStruct(word, randomStr);
        return mnemonic.getNewAddress(0, BigInteger.valueOf(index));
    }

    public static String generateNewAddress(String word, String randomStr, long index) throws Exception {
        // 助记词
        Mnemonic mnemonic = getMnemonicStruct(word, randomStr);
        return mnemonic.getNewAddress(0, BigInteger.valueOf(index));
    }

    public static String generateNewAddress(String word, String randomStr, BigInteger index) throws Exception {
        // 助记词
        Mnemonic mnemonic = getMnemonicStruct(word, randomStr);
        return mnemonic.getNewAddress(0, index);
    }

    public static void main(String[] args) throws Exception {
        String mnemonicWord = "vault action nerve office eyebrow silver you panel seminar quote manage common";
        String passphrase = "111";

        Mnemonic mnemonic = getMnemonicStruct(mnemonicWord, passphrase);

        BigInteger index = BigInteger.ZERO;
        String address = null;
        address = mnemonic.getNewAddress(0, index);
        System.out.println("index: " + index + "address: " + address);

        index = BigInteger.valueOf(1024);
        address = mnemonic.getNewAddress(0, index);
        System.out.println("index: " + index + ", address: " + address);

        index = BigInteger.valueOf(1026);
        address = mnemonic.getNewAddress(0, index);
        System.out.println("index: " + index + ", address: " + address);

        index = BigInteger.valueOf(1048576);
        address = mnemonic.getNewAddress(0, index);
        System.out.println("index: " + index + ", address: " + address);

        index = BigInteger.valueOf(1070596096L);
        address = mnemonic.getNewAddress(0, index);
        System.out.println("index: " + index + ", address: " + address);

        index = BigInteger.valueOf(1096290402304L);
        address = mnemonic.getNewAddress(0, index);
        System.out.println("index: " + index + ", address: " + address);

        index = BigInteger.valueOf(1122601371959296L);
        address = mnemonic.getNewAddress(0, index);
        System.out.println("index: " + index + ", address: " + address);

        index = BigInteger.valueOf(1149543804886319104L);
        address = mnemonic.getNewAddress(0, index);
        System.out.println("index: " + index + ", address: " + address);

        index = new BigInteger("1177132856203590762496");
        address = mnemonic.getNewAddress(0, index);
        System.out.println("index: " + index + ", address: " + address);
    }
}
