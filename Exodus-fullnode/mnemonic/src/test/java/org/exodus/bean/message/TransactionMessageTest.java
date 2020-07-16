package org.exodus.bean.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.exodus.utils.SignUtil;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author Clare
 * @date   2018/12/29 0031
 */
public class TransactionMessageTest {


    /**
     * 验证交易消息签名
     * @throws Exception 异常
     */
    @Test
    public void verifySignTest() {
        String message = "{\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"toAddress\":\"G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H\",\"amount\":\"1000000\",\"timestamp\":\"1546068716681\",\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"fee\":\"0\",\"type\":\"1\",\"remark\":\"\",\"signature\":\"33AIKbX5d+3kvh6PSIfumeS/LTP7kDle6VuSI8TtT5WAYeOpFi54QB1YEm6z5QWL6+RDLL7ocAE0bi+a1p6xbkEwY=\"}";
        System.out.println(message);

        boolean flag = false;
        //进行验证
        try {
            flag = SignUtil.verify(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(flag);
        System.out.println("over");
    }


    /**
     * 验证交易消息签名(重复验证)
     * @throws Exception 异常
     */
    @Test
    public void repeatVerifySignTest() {
        String message = "{\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"toAddress\":\"G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H\",\"amount\":\"1000000\",\"timestamp\":\"1546068716681\",\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"fee\":\"0\",\"type\":\"1\",\"remark\":\"\",\"signature\":\"33AIKbX5d+3kvh6PSIfumeS/LTP7kDle6VuSI8TtT5WAYeOpFi54QB1YEm6z5QWL6+RDLL7ocAE0bi+a1p6xbkEwY=\"}";
        System.out.println(message);

        long num = 10000L;
        boolean flag = false;
        while (num-->0) {
            //进行验证
            try {
                flag = SignUtil.verify(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(flag);
        }
        System.out.println("over");
    }

    /**
     * 验证交易消息签名
     * @throws Exception 异常
     */
    @Test
    public void createMessageAndVerifySignTest() throws Exception {
        TransactionMessage tm = new TransactionMessage(
                "shield salmon sport horse cool hole pool panda embark wrap fancy equip",
                "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7",
                "G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H",
                new BigInteger("1000000000000000000000000"),
                BigInteger.ZERO,
                BigInteger.valueOf(1000000000L),
                "111");
        String message = tm.getMessage();
        System.out.println(message);

        JSONObject object = JSON.parseObject(message);
        System.out.println(object.getString("message"));
        boolean flag = false;
        //进行验证
        try {
            flag = SignUtil.verify(object.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(flag);
        System.out.println("over");
    }


    /**
     * 验证交易消息签名(重复验证)
     * @throws Exception 异常
     */
    @Test
    public void createMessageAndRepeatVerifySignTest() throws Exception {
        TransactionMessage tm = new TransactionMessage(
                "shield salmon sport horse cool hole pool panda embark wrap fancy equip",
                "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7",
                "G5RPQVHDFV3ASSWRDRQZSIN3IAUXRL4H",
                BigInteger.valueOf(1000000),
                BigInteger.ZERO,
                BigInteger.valueOf(1000000000L));
        String message = tm.getMessage();
        System.out.println(message);

        JSONObject object = JSON.parseObject(message);
        System.out.println(object.getString("message"));
        long num = 100L;
        boolean flag = false;
        while (num-->0) {
            //进行验证
            try {
                flag = SignUtil.verify(object.getString("message"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(flag);
        }
        System.out.println("over");
    }
}
