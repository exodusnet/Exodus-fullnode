package org.exodus.core;

import org.exodus.bean.wallet.KeyPair;
import org.exodus.exception.InveException;
import org.exodus.utils.NTRU;
import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Author: zz
 * @Description:
 * @Date: 下午 3:03 2018/10/26 0026
 * @Modified By
 */
public class Crypto {
    public static Logger                    logger              = Logger.getLogger(Crypto.class);

    static final String                     SIG_PROVIDER        = "Ntru";
    static final String                     SIG_TYPE2           = "SHA3whthNTRU";
    static final String                     HASH_TYPE           = "SHA-3";
    static final int                        numCryptoThreads    = 32;
    private static final ExecutorService    cryptoThreadPool    = Executors.newFixedThreadPool(32);


    public static byte[] sign(byte[] data, byte[] privKey) throws InveException {
        if(SIG_TYPE2.equals("SHA3whthNTRU")) {
            try {
                byte[] signature = NTRU.sign(data,privKey);
                return signature;
            } catch (InveException e) {
                logger.error("error", e);
                throw new InveException("签名字节长度最少33位~!");
            }
        }else{
            return null;
        }
    }


    public static boolean verifySignature(byte[] data, byte[] signature, byte[] publicKey) {
        if(SIG_TYPE2.equals("SHA3whthNTRU")) {
            try {
                return NTRU.verify(data,signature,publicKey);
            } catch (Exception e) {
                logger.error("error", e);
            }
            return false;
        }else {
            return false;
        }
    }


    public KeyPair getKeyPair()  {
        KeyPair keyPair = null;
        if(SIG_PROVIDER.equals("Ntru")) {
            NTRU ntru = new NTRU();
            byte[] privateKey = ntru.getPrivateKey();
            byte[] publicKey = ntru.getPublicKey();
            keyPair = new KeyPair(publicKey, privateKey);
            return keyPair;
        }else {
            return keyPair;
        }
    }

}
