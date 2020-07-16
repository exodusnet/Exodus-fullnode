package org.exodus.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.exodus.bean.message.TransactionMessage;
import org.exodus.core.Crypto;
import org.exodus.exception.InveException;
import org.exodus.mnemonic.Mnemonic;
import org.bitcoinj.core.ECKey;
import org.junit.Test;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 3:26 2018/9/22 0022
 * @Modified By
 */
public class SignUtilTest {


    /**
     * 轻接点3.0 验证方法
     * @throws Exception
     */
    @Test
    public void VerifyTest() throws Exception {
        //交易数据
        String message;
        message =
            "{\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"toAddress\":\"G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H\",\"amount\":\"1000000\",\"timestamp\":1540967406,\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"fee\":\"0\",\"type\":1,\"signature\":\"5JfY/J5kQm/SHCpa271rykjbhtfQ4u0beIwUyLWelNVJykKm/AAjB0Ye4ghlIG+H391fNeGlv/1vfGsvsW/ZBg==\"}";
        ;
        TransactionMessage tran = new TransactionMessage("shield salmon sport horse cool hole pool panda embark wrap fancy equip", "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7","G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H",BigInteger.valueOf(1000000),BigInteger.ZERO);
//        tran.setSignature("5JfY/J5kQm/SHCpa271rykjbhtfQ4u0beIwUyLWelNVJykKm/AAjB0Ye4ghlIG+H391fNeGlv/1vfGsvsW/ZBg==");
//        tran.setTimestamp(1540967406L);
//        tran.setType(1);
        boolean aaa = tran.getMessage().equals(message);

        Mnemonic mn = new Mnemonic("shield salmon sport horse cool hole pool panda embark wrap fancy equip","");
        mn.getAddress();


        boolean flag = false;

        message = tran.getMessage();

        JSONObject json = JSON.parseObject(message);
        //进行验证
//        while (result) {
        try {
            flag = SignUtil.verify(json.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!flag)
            ;
        System.out.println(flag);
//        }

        System.out.println("over");

    }

    /**
     * 轻接点3.0 签名验证接口案例方法
     * @throws Exception
     */
    @Test
    public void signTest1() throws Exception {
        Mnemonic mn = new Mnemonic("shield salmon sport horse cool hole pool panda embark wrap fancy equip","");
        mn.getAddress();

        //构造签名交易数据
        TransactionMessage tran = new TransactionMessage("shield salmon sport horse cool hole pool panda embark wrap fancy equip", "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7","G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H",BigInteger.valueOf(1000000),BigInteger.ZERO, "test transaction.");
//        tran.setTimestamp(1540967406L);
//        tran.setType(1);
//        tran.setFromAddress(mn.getAddress());

        //进行签名
//        tran.signMessage("shield salmon sport horse cool hole pool panda embark wrap fancy equip","");
//        String message = tran.getMessage();
        JSONObject json = JSONObject.parseObject(tran.getMessage());
        String data = json.getString("message");

        //验证签名
        System.out.println(data);
        System.out.println(SignUtil.verify(data));
    }

    /**
     * 轻接点3.0 签名验证接口案例方法
     * @throws Exception
     */
    @Test
    public void signTest2() throws Exception {
        Mnemonic mn = new Mnemonic("shield salmon sport horse cool hole pool panda embark wrap fancy equip","");
        mn.getAddress();

        //构造签名交易数据
        TransactionMessage tran = new TransactionMessage("shield salmon sport horse cool hole pool panda embark wrap fancy equip", "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7","G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H",BigInteger.valueOf(1000000),BigInteger.ZERO, "test transaction.");
        JSONObject json = JSONObject.parseObject(tran.getMessage());
        String data = json.getString("message");

        //验证签名
        System.out.println(data);
        System.out.println(SignUtil.verify(JSONObject.parseObject(data)));
    }
    /**
     * 轻接点3.0 签名验证接口案例方法
     * @throws Exception
     */
    @Test
    public void signTest3() throws Exception {
        Mnemonic mn = new Mnemonic("shield salmon sport horse cool hole pool panda embark wrap fancy equip","");
        mn.getAddress();

        //构造签名交易数据
        TransactionMessage tran = new TransactionMessage("shield salmon sport horse cool hole pool panda embark wrap fancy equip", "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7","G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H",BigInteger.valueOf(1000000),BigInteger.ZERO, "test transaction.");
        JSONObject json = JSONObject.parseObject(tran.getMessage());
        String data = json.getString("message");

        JSONObject o = JSONObject.parseObject(data);
        System.out.println(o.toJSONString());

        //验证签名
        Instant time = Instant.now();
        long size = 1;
        for (int i=0; i<size; i++) {
            SignUtil.verify((JSONObject) o.clone());
        }
        System.out.println("size: " + size + ", cost: " + Duration.between(time, Instant.now()).toMillis());
    }
    /**
     * 轻接点3.0 签名验证接口案例方法
     * @throws Exception
     */
    @Test
    public void signTest4() throws Exception {
        Mnemonic mn = new Mnemonic("shield salmon sport horse cool hole pool panda embark wrap fancy equip","");
        mn.getAddress();

        //构造签名交易数据
        TransactionMessage tran = new TransactionMessage("shield salmon sport horse cool hole pool panda embark wrap fancy equip", "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7","G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H",BigInteger.valueOf(1000000),BigInteger.ZERO, "test transaction.");
        JSONObject json = JSONObject.parseObject(tran.getMessage());
        String data = json.getString("message");

        JSONObject o = JSONObject.parseObject(data);
        System.out.println(o.toJSONString());

        //验证签名
        Instant time = Instant.now();
        long size = 1;
        List<JSONObject> list = new ArrayList<>();
        for (int i=0; i<size; i++) {
            list.add((JSONObject) o.clone());
        }

        forkJoinVerifyTxSignTest(list);
        System.out.println("size: " + size + ", cost: " + Duration.between(time, Instant.now()).toMillis());
    }
    @Test
    public void testSingleTxSignature() {
        Mnemonic mnemonic = null;
        try {
            mnemonic = new Mnemonic("", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fromMnenonic = mnemonic.getMnemonic().stream().collect(Collectors.joining(" "));
        String fromAddress = mnemonic.getAddress();

        String msg = null;
        try {
            msg = new TransactionMessage(fromMnenonic, fromAddress, "4444444444444", BigInteger.valueOf(1000000),BigInteger.ZERO).getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msg1 = JSONObject.parseObject(msg).get("message").toString();
        if (!SignUtil.verify(msg1)) {
            System.out.println("msg verify failed. \nmsg: " + msg1);
        }
        if (!SignUtil.verify(JSON.parseObject(msg1))) {
            System.out.println("msg verify failed. \nmsg: " + msg1);
        }
    }

    @Test
    public void signTest5() throws Exception {
        Mnemonic mn = new Mnemonic("shield salmon sport horse cool hole pool panda embark wrap fancy equip","");
        mn.getAddress();

        //构造签名交易数据
        TransactionMessage tran = new TransactionMessage("steel more error puppy debate glory glide knife firm ketchup damp minimum",
                "DOBJWS6H7YEFHDI7SNZNMRJ2AHHX3ATK","HH3XMK7PETKGR3DO7NG54C3VPKGOKRQ2",BigInteger.valueOf(1000000),BigInteger.ZERO, "");
        JSONObject json = JSONObject.parseObject(tran.getMessage(), Feature.OrderedField);
        String data = json.getString("message");

        JSONObject o = JSONObject.parseObject(data, Feature.OrderedField);
//        System.out.println(o.toJSONString());


        String privateKey = "wm+8R7SAYjaIsc2R+jRjYHqshgX3BI4D6Z+T9rG9ufw=";
        String publicKey = "ArOqG1+7weaPIdo/5TtMbkQKciSMrJOru5u0rzd/ittU";
//
//        Mnemonic mnemonic = new Mnemonic("shield salmon sport horse cool hole pool panda embark wrap fancy equip", "");
//        KeyPair keys = mnemonic.getBIPpriKey(44,0,0,0,0);
//        BASE64Encoder encoder = new BASE64Encoder();
//        String privkey  = encoder.encode(keys.getPrivateKey());
//        String pubkey  = encoder.encode(keys.getPublicKey());
//        String address = mnemonic.getAddress();
//        System.out.println("privateKey: " + privateKey.equals(privkey));
//        System.out.println("publickey: " + publicKey.equals(pubkey));
//
//        System.out.println("from: " + o.toJSONString().contains("DOBJWS6H7YEFHDI7SNZNMRJ2AHHX3ATK"));
//        System.out.println("to: " + o.toJSONString().contains("HH3XMK7PETKGR3DO7NG54C3VPKGOKRQ2"));
//        System.out.println("amount: " + o.toJSONString().contains("1000000"));
//        System.out.println("timestamp: " + o.toJSONString().contains("1544164267544"));
//        System.out.println("pubkey: " + o.toJSONString().contains("ArOqG1+7weaPIdo/5TtMbkQKciSMrJOru5u0rzd/ittU"));
//        System.out.println("address: " + address.equals("DOBJWS6H7YEFHDI7SNZNMRJ2AHHX3ATK"));
//
//        System.out.println("signature: " + o.toJSONString().contains("32s4CHyNGyjFkWYkkQJ2EnYFmUKUaqX3ss3acf1e25I+084v+wIUZ1e2u5amU9Fu8wPMiFz6h24pVuZInxsKQneA=="));
        //33ALOAh8jRsoxZFmJJECdhJ2BZlClGql97LN2nH9XtuSPtPOL/sCFGdXtruWplPRbvMDzIhc+oduKVbmSJ8bCkJ3g=

        String msg = "{\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"toAddress\":\"DOBJWS6H7YEFHDI7SNZNMRJ2AHHX3ATK\",\"amount\":\"434534000000\",\"timestamp\":1544164126081,\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"fee\":\"0\",\"type\":1,\"remark\":\"\",\"signature\":\"32j7vyTTvX6YS4FveJdDfFGFYqioscHLkzwr+/9Y+wBzRoGvJDqfQ4V7og77VaFqN/FFH0rTcEdOhWC3I0lO5/FQ==\"}";
        System.out.println(SignUtil.verify(msg));
        System.out.println(SignUtil.verify(JSON.parseObject(msg)));
    }

    private static void forkJoinVerifyTxSignTest(List<JSONObject> list) {
        System.out.println("available processors: " + Runtime.getRuntime().availableProcessors());
        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        AddTask task = new AddTask(list);
        pool.submit(task);
        List<JSONObject> lists = task.join();
    }

    static class AddTask extends RecursiveTask<List<JSONObject>> {
        List<JSONObject> o;
        public AddTask(List<JSONObject> o) {
            this.o = o;
        }

        @Override
        protected List<JSONObject> compute() {
            if (null == o || o.size() < 1) {
                return null;
            } else if (o.size()==1) {
                o.get(0).put("isValid", SignUtil.verify(o.get(0)));
                return o;
            } else {
                AddTask task1 = new AddTask(o.subList(0, o.size()/2));
                AddTask task2 = new AddTask(o.subList(o.size()/2, o.size()));
                task1.fork();
                task2.fork();
                List<JSONObject> r = new ArrayList<>();
                if (null != task1.join()) {
                    r.addAll(task1.join());
                }
                if (null != task2.join()) {
                    r.addAll(task2.join());
                }
                return r;
            }
        }
    }

    /**
     * 轻接点2.0 交易签名接口案例
     * @throws Exception
     */
    @Test
    public void signTestDeno1() throws Exception {
        Mnemonic mn = new Mnemonic("","");
        byte[] privateKey = mn.getxPrivKey();
        System.out.println(Arrays.toString(privateKey));
        byte[] puKey = mn.getPubKey();


        BigInteger priKBig = mn.getxPrivKeyBig();
        System.out.println(priKBig);
        ECKey key = ECKey.fromPrivate(priKBig);

//        byte[] output = key.sign();


        String unit = "{\"unit\":{\"walletId\":\"anBEaWwydzN0M0RsSmdrWXZ4d0g5b0xYQXFvTDZzQWFHckRseG5Uc09HWWs=\",\"unit\":\"THA2MVMzZURPSDFkNmF0ZFlEd2pwS1hRdGxCeDYyb2x3WWlTT0hGTlZkZk0=\",\"alt\":\"3\",\"messages\":[{\"app\":\"payment\",\"payload_location\":\"inline\",\"payload\":{\"outputs\":[{\"amount\":300000,\"address\":\"MG1Y5TISRCVY7MBBFETRZGNMGGE8VMDR\"},{\"amount\":9877,\"address\":\"NZ7QOTJBSNP0W2HYN7LEDQUT3T1THSND\"}],\"inputs\":[{\"unit\":\"UVNOTTVDMT12K0tLR0ZXcG9Bb3RWZHRKMTVXMDdMK3hyUTJqZlBsN2xFaFE=\",\"message_index\":0,\"output_index\":1}]},\"payload_hash\":\"2r8sJ+JHbf8eMQM0TnvIbPYti733vKh6s14rHB1nJkA=\"}],\"payload_commission\":197,\"version\":\"1.0dev\",\"headers_commission\":262,\"authors\":[{\"authentifiers\":{\"r\":\"AN/2JSB0dbAVdQ1+W07QJz9lqdBRq8deyrhdn8GRHXwvXzvSbHboustuBm3BSfMP9Z/3y1huGYUb\\r\\njODUJKLwdDQ=\"},\"address\":\"NZ7QOTJBSNP0W2HYN7LEDQUT3T1THSND\",\"definition\":[\"sig\",{\"pubkey\":\"AvIYCi7oxbOxp1L2ek3a1Z5zJAKRTWPpoZmbuQWslx14\"}]}],\"timestamp\":1530839419}}";

//        byte[] pub = "AnvtSPZg8gWJQR5W0tV69DNoi6Bol30bo1RClBhpChH8".getBytes();

        System.out.println("用私钥对信息进行数字签名");


        //进行签名
        String sinStr = SignUtil.signStr(unit , key);

        System.out.println(sinStr);
        unit = "{\"unit\":{\"walletId\":\"aTVBQ1NMTkcyM2JEZXBMTkl5SmVNazloMDVtNTg1PXp5NXFWUk9RTWduV0g=\",\"unit\":\"M1ZWNTBoNUZqeFdpQllZTkpuMnlzTkZoNlJCeDVVdzNxYjBGZW9QRTdRRlU=\",\"alt\":\"3\",\"messages\":[{\"app\":\"payment\",\"payload_location\":\"inline\",\"payload\":{\"outputs\":[{\"amount\":300000,\"address\":\"LPBBQFI5WJ586KTE3530CNVDUZOA2RZR\"},{\"amount\":9877,\"address\":\"A4SZKLF3MGUHJDY8ME72CAN6TXRE61E4\"}],\"inputs\":[{\"unit\":\"dHc5NjhlY09jeHZZPXk1Z1dKVHpmUkpWRTlid29yYXJiczhEN0g9Z2c3Ymo=\",\"message_index\":0,\"output_index\":1}]},\"payload_hash\":\"2r8sJ+JHbf8eMQM0TnvIbPYti733vKh6s14rHB1nJkA=\"}],\"payload_commission\":197,\"version\":\"1.0dev\",\"headers_commission\":262,\"authors\":[{\"authentifiers\":{\"r\":\"ZYFM1Aqs0b32f3NVBtiI4BVfMlIrHzaR3nMYKK7aoTNduL6jFghML23Cz7C1zOGR56kxvit4Vpo/\\r\\nMFodWCQUrQ==\"},\"address\":\"A4SZKLF3MGUHJDY8ME72CAN6TXRE61E4\",\"definition\":[\"sig\",{\"pubkey\":\"A3tTpgEzx6DFJtXaE5QZILgh8ZWq/u3HiAhowR+9PRcs\"}]}],\"timestamp\":1530839419}}";


        //解签名
        boolean flag = SignUtil.Verify(unit);
        System.out.println(flag);
    }



    @Test
    public void signTest() throws Exception {
        Mnemonic mn = new Mnemonic("","");
        byte[] privateKey = mn.getxPrivKey();

        System.out.println(Arrays.toString(privateKey));
        byte[] puKey = mn.getPubKey();

        BigInteger priKBig = mn.getxPrivKeyBig();
        System.out.println(priKBig);

        ECKey key = ECKey.fromPrivate(privateKey);


        System.out.println("用私钥对信息进行数字签名");


        String str = "lee sang";
        //进行签名
        String sinStr = SignUtil.signStr(str , key);

        System.out.println(sinStr);

        byte[] pubkey = mn.getPubKey();
        String aaa = DSA.encryptBASE64(pubkey);
        ECKey pubk = ECKey.fromPublicOnly(pubkey);

        //解签名
        boolean flag = SignUtil.verifyStr(str , sinStr, pubk);
        System.out.println(flag);

    }




    /** 测试签名问题*/
    @Test
    public void ntruTest() throws InveException {
        String prik = "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";
        String msgS = "l8m1G0HwfWXHbapr7dgTHjPJPwdAAYBELPkVWWjdlLj2o7AFfJzEGAt4R4sCaU6SCSjB/efWtpqRq/6S3XTZCw==";
        byte[] pri = DSA.decryptBASE64(prik);
        byte[] msg = DSA.decryptBASE64(msgS);

        String signatureS = null;

        boolean flag = true;
//        while(flag) {
            byte[] signature = Crypto.sign(msg,pri);

            signatureS = DSA.encryptBASE64(signature);
            System.out.println(signatureS);
            if(signatureS != null){
                if(!signatureS.equals(DSA.encryptBASE64(signature)))
                    flag = false;
            }
//        }


    }


}
