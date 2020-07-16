package org.exodus.utils;


import org.exodus.bean.wallet.KeyPair;
import org.exodus.core.Crypto;
import org.exodus.exception.InveException;
import org.junit.Test;

import java.util.Arrays;

/**
 * 抗量子攻击算法测试
 */
public class NTRUTest {


    @Test
    public void NtrusignTest() throws InveException {

//
        String msg = "Whatever is worth doing is worth doing well";
        String msg2 = "Whatever is worth doing is worth doing well1";

        NTRU ntru = new NTRU();
        byte[] pri = ntru.getPrivateKey();
        byte[] pub = ntru.getPublicKey();
        byte[] buf_2_sign = msg.getBytes();

        String priS = DSA.encryptBASE64(pri);
        byte[] priArr = DSA.decryptBASE64(priS);
        boolean aaa = Arrays.equals(pri,priArr);


        Crypto crypto = new Crypto();
        KeyPair keyPair = crypto.getKeyPair();

        //签名
        byte[] signature = new byte[0];
        try {
            signature = Crypto.sign(buf_2_sign,keyPair.getPrivateKey());
        } catch (InveException e) {
            e.printStackTrace();
        }
        //验证
        boolean rs = Crypto.verifySignature(buf_2_sign,signature,keyPair.getPublicKey());
        System.out.println("===========");
        System.out.println(rs);


        byte[] pubkey = NTRU.getPublicKey(pri);

        System.out.printf("");



    }



    @Test
    public void pathTest() {


        System.out.println( getClass().getProtectionDomain().getCodeSource().getLocation().getPath() );


    }





    public static void main(String[] args) {
        for(int i=0; i<1; i++) {
            TestThread t = new TestThread();
            t.start();
        }
    }
}

class TestThread extends Thread {
    @Override
    public void run() {
        Crypto crypto = new Crypto();
        KeyPair keyPair = crypto.getKeyPair();
        System.out.println("pubkey: " + DSA.encryptBASE64(keyPair.getPublicKey()));
    }



}
