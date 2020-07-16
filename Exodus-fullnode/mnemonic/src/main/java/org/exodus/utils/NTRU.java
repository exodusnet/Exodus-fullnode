package org.exodus.utils;


import ntrusign.Ntrusign;
import org.exodus.exception.InveException;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * @Author: zz
 * @Description: 抗量子攻击加密算法
 * @Date: 下午 5:02 2018/9/17 0017
 * @Modified By
 */
public class NTRU {
    public static   Logger logger   = Logger.getLogger(NTRU.class);

    private  Ntrusign ntru    = null;


    private  byte[] keyPair   = null;

    /**
     * 初始化NTRU
     * @param key 默认为空
     */
    public NTRU(String key) {
        ntru = new Ntrusign();
        keyPair = ntru.InitialKey(key);
    }

    /**
     * 初始化NTRU
     */
    public NTRU() {
        ntru = new Ntrusign();
        keyPair = ntru.InitialKey("");
    }




//    /**
//     * 初始化 恢复公私钥
//     * @param prikey 默认为空
//     * @return
//     */
//    public static Ntrusign generateNtru(byte[] prikey) {
//        new NTRU(prikey);
//        return ntru;
//    }




    /**
     *  签名
     * @param data 签名数据
     * @param privatekey  私钥
     * @return signature
     */
    public static byte[] sign(byte[] data , byte[] privatekey) throws InveException {
        if(data.length < 33) {
            logger.error("签名字节长度最少33位~!");
            throw new InveException("签名字节长度最少33位~!");
        }
//        if(ntru == null)
//            ntru = new Ntrusign();
        return Ntrusign.Sign(data,privatekey);
    }

    /**
     * 签名验证
     * @param data 签名数据
     * @param signature 签名
     * @param publickey 公钥
     * @return true or false
     */
    public static boolean verify(byte[] data, byte[] signature, byte[] publickey) {
//        if(ntru == null)
//            ntru = new Ntrusign();
//        int n = Ntrusign.Verify(signature,data,publickey);
        return Ntrusign.Verify(signature,data,publickey) == 1;
    }


    /**
     * 获取私钥
     * @return private key
     */
    public byte[] getPrivateKey() {
        return Arrays.copyOfRange(keyPair,2065,4669);
    }

    /**
     * 获取公钥
     * @return public key
     */
    public byte[] getPublicKey() {
        return Arrays.copyOfRange(keyPair,0,2065);
    }


    /**
     * 私钥 恢复 公钥
     * @param prikey
     * @return public key
     */
    public static byte[] getPublicKey(byte[] prikey) throws InveException {
        if(prikey ==null || prikey.length == 0){
            throw new InveException("private key cannot be null");
        }
        return Ntrusign.Secret2Public(prikey);
    }


}
