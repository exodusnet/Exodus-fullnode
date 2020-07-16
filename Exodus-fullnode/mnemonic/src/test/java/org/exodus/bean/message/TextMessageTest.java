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
public class TextMessageTest {

    /**
     * 验证文本消息签名
     * @throws Exception 异常
     */
    @Test
    public void verifySignTest() {
        String message = "{\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"context\":\"AQIDBAUGBw==\",\"timestamp\":\"1546068398702\",\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"type\":\"4\",\"signature\":\"32CVeOcw4Z8whxiXKrn0bobZxtaChHbmegcGSF1d1pwm0IkLaym/Y94vyZFl9j6KWTwrdyq9XuWaOopaDJ902s7g==\"}";
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
     * 验证文本消息签名
     * @throws Exception 异常
     */
    @Test
    public void repeatVerifySignTest() {
        String message = "{\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"context\":\"AQIDBAUGBw==\",\"timestamp\":\"1546068398702\",\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"type\":\"4\",\"signature\":\"32CVeOcw4Z8whxiXKrn0bobZxtaChHbmegcGSF1d1pwm0IkLaym/Y94vyZFl9j6KWTwrdyq9XuWaOopaDJ902s7g==\"}";
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
     * 验证文本消息签名
     * @throws Exception 异常
     */
    @Test
    public void createMessageAndVerifySignTest() throws Exception {
        byte[] data = new byte[]{1,2,3,4,5,6,7};
        TextMessage tm = new TextMessage(
                "shield salmon sport horse cool hole pool panda embark wrap fancy equip",
                "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7",
                data,
                BigInteger.valueOf(1000000000L) );
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
     * 验证文本消息签名(重复验证)
     * @throws Exception 异常
     */
    @Test
    public void createMessageAndRepeatVerifyTest() throws Exception {
        byte[] data = new byte[]{1,2,3,4,5,6,7};
        TextMessage tm = new TextMessage(
                "shield salmon sport horse cool hole pool panda embark wrap fancy equip",
                "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7",
                data);
        String message = tm.getMessage();
        System.out.println(message);

        JSONObject object = JSON.parseObject(message);
        System.out.println(object.getString("message"));
        long num = 10000L;
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
