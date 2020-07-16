package org.exodus.bean.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.exodus.utils.SignUtil;
import org.junit.Test;

/**
 * @author Clare
 * @date   2018/12/29 0031
 */
public class ContractMessageTest {

    /**
     * 验证合约消息签名
     * @throws Exception 异常
     */
    @Test
    public void verifySignTest() {
        String message = "{\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"timestamp\":\"1546068595345\",\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"type\":\"2\",\"data\":\"AQIDBAUGBw==\",\"signature\":\"32ErcPLbrTG/l0KWnopl1b4jdAbGYiimVGt2OLIG45ZCxaAZZ0r6LOvm7REbOl6cPSIv3617ffz6KMysSJ38oKwg==\"}";
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
     * 验证合约消息签名(重复验证)
     * @throws Exception 异常
     */
    @Test
    public void repeatVerifyTest() {
        String message = "{\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"timestamp\":\"1546068595345\",\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"type\":\"2\",\"data\":\"AQIDBAUGBw==\",\"signature\":\"32ErcPLbrTG/l0KWnopl1b4jdAbGYiimVGt2OLIG45ZCxaAZZ0r6LOvm7REbOl6cPSIv3617ffz6KMysSJ38oKwg==\"}";
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
     * 验证合约消息签名
     * @throws Exception 异常
     */
    @Test
    public void createMessageAndVerifySignTest() throws Exception {
        byte[] data = new byte[]{1,2,3,4,5,6,7};
        ContractMessage tran = new ContractMessage(
                "shield salmon sport horse cool hole pool panda embark wrap fancy equip",
                data,
                "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7");
        String message = tran.getMessage();
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
     * 验证合约消息签名(重复验证)
     * @throws Exception 异常
     */
    @Test
    public void createMessageAndRepeatVerifyTest() throws Exception {
        byte[] data = new byte[]{1,2,3,4,5,6,7};
        ContractMessage tran = new ContractMessage(
                "shield salmon sport horse cool hole pool panda embark wrap fancy equip",
                data,
                "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7");
        String message = tran.getMessage();
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
