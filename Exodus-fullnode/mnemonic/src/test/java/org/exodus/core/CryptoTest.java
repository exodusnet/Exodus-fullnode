package org.exodus.core;

import org.exodus.bean.wallet.KeyPair;
import org.exodus.exception.InveException;
import org.exodus.utils.DSA;
import org.junit.Test;
import java.util.Random;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 2:49 2018/10/29 0029
 * @Modified By
 */
public class CryptoTest implements Runnable{


    /**
     * 抗量子攻击算法案例
     * @throws InveException
     */
    @Test
    public void ntruTest() throws InveException {


        Crypto crypto = new Crypto();
        int i = 0;
//        while(true){
        KeyPair keyPair = crypto.getKeyPair();
        String message = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String msg = "11111111111";
        byte [] data = message.getBytes();

        byte[] signature = Crypto.sign(data,keyPair.getPrivateKey());
        String aaa = DSA.encryptBASE64(signature);

        Boolean fl = Crypto.verifySignature(data,signature,keyPair.getPublicKey());

        System.out.println(fl);
//            if(++i==10000) {
//                break;
//            }
//        }

    }



    /**
     * 测试抗量子攻击算法
     */
    @Test
    public void ntruTPSTest() {
        Crypto crypto = new Crypto();
        KeyPair keyPair = crypto.getKeyPair();
    }


    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    public static void main(String[] args) throws InveException {
        Crypto crypto = new Crypto();
        int i = 0;
        KeyPair keyPair = crypto.getKeyPair();

        long d = System.currentTimeMillis();

//        while(true){

            String message = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
            String msg = "11111111111";
            byte [] data = message.getBytes();

            long a = System.currentTimeMillis();
            byte[] signature = Crypto.sign(data,keyPair.getPrivateKey());
            long b = System.currentTimeMillis();
            System.out.println("签名时间:"+(b - a));

//            String aaa = DSA.encryptBASE64(signature);

            boolean fl = Crypto.verifySignature(data,signature,keyPair.getPublicKey());
            long c = System.currentTimeMillis();
            System.out.println("验证时间"+(c - b));
//            if(fl && ++i==10000000) {
//                System.out.println(fl);
//                break;
//            }
//        }
        long e = System.currentTimeMillis();
        System.out.println("总时间:"+(e - d));

    }

    /**
     * 测试抗量子攻击算法
     */
    @Test
    public void ntruTPSTest_多线程() {
        CryptoTest test1 = new CryptoTest();

        CryptoTest test2 = new CryptoTest();

        CryptoTest test3 = new CryptoTest();

        CryptoTest test4 = new CryptoTest();

        CryptoTest test5 = new CryptoTest();
//        test4.run();
//        test3.run();
//        test2.run();
//        test1.run();
//        test5.run();

    }

//    public CryptoTest(String i) {
//        System.out.println("线程" + i + "启动...");
//    }

    private static int i = 1;
    public CryptoTest() {
        System.out.println("线程" + i++ +  "启动...");
    }

    private static  Crypto crypto = new Crypto();
    private static KeyPair kp = crypto.getKeyPair();
    @Override
    public void run() {
        long d = System.currentTimeMillis();
        int i = 0;
//        while(true){
//            String message = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//            String msg = "11111111111";
//            byte [] data = message.getBytes();
//
//            long a = System.currentTimeMillis();
//            byte[] signature = new byte[0];
//            try {
//                signature = Crypto.sign(data,kp.getPrivateKey());
//            } catch (InveException e) {
//                System.out.println("error~!");
//                System.exit(0);
//            }
//            long b = System.currentTimeMillis();
//            System.out.println("签名时间:"+(b - a));
//
////            String aaa = DSA.encryptBASE64(signature);
//
//            boolean fl = Crypto.verifySignature(data,signature,kp.getPublicKey());
//            long c = System.currentTimeMillis();
//            System.out.println("验证时间"+(c - b));
//            if(fl && ++i==10000) {
//                System.out.println(fl);
//                break;
//            }
//        }
        long e = System.currentTimeMillis();
        System.out.println("总时间:"+(e - d));
    }
}
