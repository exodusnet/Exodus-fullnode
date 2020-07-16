package org.exodus.mnemonic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.subgraph.orchid.data.Base32;
import org.exodus.bean.wallet.KeyPair;
import org.exodus.core.Chash;
import org.exodus.core.Constant;
import org.exodus.utils.ByteUtil;
import org.exodus.utils.DSA;
import org.exodus.utils.SignUtil;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.*;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * @Author: zz
 * @Description:  助记词 公私钥生成
 * @Date:  10:00 2018/6/11 0011
 * @Modified By
 */
public class Mnemonic {

    private List<String> MNEMONIC = null;
    private MnemonicCode mc;

    private String xPrivKeyStr;
    private byte[] PubKey;

    private byte[] seed = null;

    private byte[]  xPrivKeyByte;
    private static final BigInteger ADDRESS_INDEX_LIMIT = BigInteger.valueOf(1024L);

    /**
     * 初始化/恢复  助记词、私钥
     * @param words 助记词
     * @param passphrase 随机盐
     * @throws Exception
     */
    public Mnemonic(String words , String passphrase) throws Exception {
        mc = new MnemonicCode();
        generateMnemonic(words);
        generatePrivKey(passphrase);
    }


    /**
*      *  * 扩展公私钥生成方法
*      *  * inve钱包地址采用BIP44提议 由定义生成钱包地址
*      *  * 定义的模板 [\"sig\", {\"pubkey\":\""+ DSA.encryptBASE64(public) +"\"}]
*      *  * 公钥为  m44'/0'/0'/0 /0
     *
     * @param purpose
     * @param coinType
     * @param account
     * @param isChange
     * @param addressIndex
     */
    public KeyPair getBIPpriKey(int purpose, int coinType, int account, int isChange, int addressIndex) {
        DeterministicKey devicePrivKey = getDevicePrivKey(purpose, coinType, account,
                isChange, BigInteger.valueOf(addressIndex));

        KeyPair keys = new KeyPair();
        keys.setPrivateKey(devicePrivKey.getPrivKeyBytes());
        keys.setPublicKey(devicePrivKey.getPubKey());
        return keys;
    }

    /**
     *
     * @param purpose
     * @param coinType
     * @param account
     * @param isChange
     * @param addressIndex
     * @return
     */
    private DeterministicKey getDevicePrivKey(int purpose, int coinType, int account,
                                              int isChange, BigInteger addressIndex) {
        //m/44'/0'/0'/
        DeterministicKey devicePrivKey = masterPrivKey.derive(purpose).derive(coinType).derive(account);
        //m/purpose/coinType/account/isChange
        DeterministicKey devicePrivKeyChange = HDKeyDerivation.deriveChildKey(devicePrivKey, isChange);
        if (addressIndex.compareTo(ADDRESS_INDEX_LIMIT) >= 0) {
            BigInteger parentIndex = addressIndex.divide(ADDRESS_INDEX_LIMIT);
            int childIndex = addressIndex.mod(ADDRESS_INDEX_LIMIT).intValue();

            DeterministicKey devicePrivKeyPaIndex = null;
            DeterministicKey devicePrivKeyChIndex = null;

            do {
                System.out.println("parent index: " + parentIndex + ", child index: " + childIndex);
                devicePrivKeyPaIndex = HDKeyDerivation.deriveChildKey(devicePrivKeyChange, parentIndex.intValue());
                devicePrivKeyChIndex = HDKeyDerivation.deriveChildKey(devicePrivKeyPaIndex, childIndex);

                if (parentIndex.compareTo(ADDRESS_INDEX_LIMIT) < 0) {
                    return devicePrivKeyChIndex;
                } else {
                    devicePrivKeyChange = devicePrivKeyChIndex;
                    parentIndex = parentIndex.divide(ADDRESS_INDEX_LIMIT);
                }
            } while (true);
        } else {
            //m/purpose/coinType/account/addressIndex
            DeterministicKey devicePrivKeyIndex = HDKeyDerivation.deriveChildKey(devicePrivKeyChange,
                    addressIndex.intValue());
            return devicePrivKeyIndex;
        }
    }


    /**
     * generate address
     * @return address
     */
    public String getAddress() {
        return getNewAddress(0, BigInteger.ZERO);
    }

    /**
     * generate new address
     *
     * @param isChange isChange
     * @param addressIndex addressIndex
     * @return new address
     */
    public String getNewAddress(int isChange, BigInteger addressIndex) {
        // 计算对应的扩展公钥
        DeterministicKey devicePrivKey = getDevicePrivKey(44, 0, 0, isChange, addressIndex);
        String pubKey = DSA.encryptBASE64(devicePrivKey.getPubKey());

        //生成定义
        JSONArray definition = JSONObject.parseArray("[\"sig\", {\"pubkey\":\""+ pubKey +"\"}]");
        String data = SignUtil.getSourceString(definition);
        String address = getAddressByDefinition(data);

        return address.toUpperCase();
    }

    /**
     * 由定义生成地址
     * @param definition
     * @return
     */
    public static String getAddressByDefinition(String definition) {
        //对定义进行RIPEMD160 hash处理
        RIPEMD160Digest digest = new RIPEMD160Digest();
        byte[] hash = new byte[20];
        digest.update(definition.getBytes(),0,definition.getBytes().length);
        digest.doFinal(hash,0);

        //去除定义hash的前四位   生成 truncated_hash
        byte[] truncated_hash = Arrays.copyOfRange(hash ,4, hash.length);
        //利用truncated_hash生成校验码 ： truncated_hash -- sha256 --  取5 13 21 29 位的字节为checksum
        byte[] checksum = Chash.getChecksum(truncated_hash);
        //转换成二进制串
        String binCleanData = ByteUtil.bytes2bin(truncated_hash);
        String binChecksum = ByteUtil.bytes2bin(checksum);
        //将校验位的二进制的每一位按照规律  穿插进  binCleanData的hash二进制中
        //1,4,4,8,16,17,22,26,28,32,39,47,53,61,63,64,66,73,76,81,82,87,90,92,94,101,103,104,110,118,122,123
        String binChash = Chash.mixChecksumIntoCleanData(binCleanData, binChecksum);
        byte[] chash = ByteUtil.bin2bytes(binChash);
        //对chash进行base32编码生成地址
        return Base32.base32Encode(chash);
    }

    /**
     * get seed
     * @return seed
     */
    public byte[] getSeed() {
        return seed;
    }

    /**
     * getPubKey
     * @return bytes of public key
     */
    public byte[] getPubKey() {
        return PubKey;
    }

    /**
     * get xPrivKey byte
     * @return bytes of private key
     */
    public byte[] getxPrivKey() {
        return xPrivKeyByte;
    }

    /**
     * generate PrivKey
     * @param passphrase
     */
    private void generatePrivKey(String passphrase) {
        toHDPrivateKey(passphrase);
    }

    /**
     * get private key
     * @return bigInteger of private key
     */
    public BigInteger getxPrivKeyBig() {
        return masterPrivKey.getPrivKey();
    }

    /** 确定私钥对象 */
    private DeterministicKey masterPrivKey;

    /**
     * generate key by passphrase and words
     * @param passphrase
     */
    private void toHDPrivateKey(String passphrase) {
        byte[] seed = null;
        try {
            seed = toSeed(passphrase);
        } catch (Exception e) {
            System.out.println("generate seed faild~!");
            e.printStackTrace();
        }
        masterPrivKey = HDKeyDerivation.createMasterPrivateKey(seed);

        xPrivKeyByte = masterPrivKey.getPrivKeyBytes();

        masterPrivKey.getPrivKeyBytes();

        BigInteger xPrivKey = masterPrivKey.getPrivKey();

        PubKey = masterPrivKey.getPubKey();

        xPrivKeyStr = xPrivKey.toString(16);
    }

    /**
     * generate seed by passphrase and words
     * @param passphrase
     * @return seed
     * @throws IOException
     */
    private byte[] toSeed(String passphrase) throws Exception {
        seed = MnemonicCode.toSeed(MNEMONIC , passphrase);
        return seed;
    }


    /**
     * generate temp keys
     * @param deviceName
     * @param passphrase
     * @return temp keys
     */
    public Map<String ,String> getkey(String deviceName, String passphrase) {
        PrivKey pk = new PrivKey(deviceName, passphrase);
        byte[] deviceTempPrivKey = pk.getDeviceTempPrivKey();
        byte[] devicePrevTempPrivKey = pk.getDevicePrevTempPrivKey();
        return  pk.createKeys(passphrase , deviceTempPrivKey , devicePrevTempPrivKey);
    }

    /**
     * get mnemonic
     * @return mnemonic words
     */
    public List<String> getMnemonic() {
        return MNEMONIC;
    }

    /**
     * generate mnemonic
     */
    private void generateMnemonic(String words)  {
        try {
            if (!words.isEmpty()) {
                String[] arr = words.split(" ");
                if (arr.length != Constant.MNEMONIC_WORD_SIZE ) {
                    throw new RuntimeException("Mnemonic length check failed~!");
                }

                mc.check(Arrays.asList(arr));
                MNEMONIC = Arrays.asList(arr);
                return;
            }
            List wordlist = mc.getWordList();
            //var ent, phrase, seed;
            int ent = 128;

            //TODO   if (Buffer.isBuffer(data)) seed = data;
            List MNEMONIC_ = _mnemonic(ent, wordlist);
            int i = 0;
            //check Mnemonic
            while (i == 0) {
                try {
                    mc.check(MNEMONIC_);
                    MNEMONIC = MNEMONIC_;
                    i = 1;
                } catch (Exception e) {
                    MNEMONIC_ = _mnemonic(ent, wordlist);
                    i = 0;
                }
            }
        } catch (MnemonicException e) {
            e.printStackTrace();
            throw new RuntimeException("Mnemonic recovery check failed~!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Mnemonic generate failed~!");
        }

    }

    /**
     * generate mnemonic words
     * @param ENT
     * @param wordlist
     * @return mnemonic words
     */
    private List<String> _mnemonic(int ENT, List wordlist) throws Exception {
        byte[] buf ;
        buf = getRandomBuffer(ENT / 8);
        List<String> mnemonic = mc.toMnemonic(buf);

        return mnemonic;
    }


    /**
     * generate random numbers
     * @param size
     * @return
     */
    private byte[] getRandomBuffer(int size) {
        return randomBytes(size);
    }


    private byte[] randomBytes(int size) {
        //TODO
        SecureRandom csprng = new SecureRandom();
        byte[] randomBytes = new byte[size];
        csprng.nextBytes(randomBytes);
        return randomBytes;
    }




    /**
     * generate address （Btc）
     * @return btc address
     */
    public String getBtcAddress() {
        //m/44'/0'/0'/
        DeterministicKey devicePrivKey = masterPrivKey.derive(44).derive(0).derive(0);
        //m/0/0
        DeterministicKey devicePrivKeyM0 = HDKeyDerivation.deriveChildKey(devicePrivKey,ChildNumber.ZERO);
        byte[] publicKey = HDKeyDerivation.deriveChildKeyBytesFromPublic(devicePrivKeyM0,ChildNumber.ZERO,HDKeyDerivation.PublicDeriveMode.NORMAL).keyBytes;

        byte[] payload = Utils.sha256hash160(publicKey);
        //版本
        byte [] version = {0x00};
        byte[] vp = new byte[version.length + payload.length];

        System.arraycopy(version , 0 , vp , 0 , version.length);
        System.arraycopy(payload ,0,vp,version.length,payload.length);
        //校验码
        byte[] checksum = Sha256Hash.hash(Sha256Hash.hash(vp));

        byte[] vpc = new byte[vp.length + 4];
        System.arraycopy(vp,0,vpc ,0 ,vp.length);
        System.arraycopy(checksum,0,vpc,vp.length,4);

        String address = Base58.encode(vpc);

        return address;
    }

}
