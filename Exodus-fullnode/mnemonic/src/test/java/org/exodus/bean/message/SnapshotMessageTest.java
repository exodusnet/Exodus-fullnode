package org.exodus.bean.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.exodus.utils.SignUtil;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author Clare
 * @date   2019/1/26 0031
 */
public class SnapshotMessageTest {

    /**
     * 验证合约消息签名
     * @throws Exception 异常
     */
    @Test
    public void verifySignTest() {
        String message = "{\"snapVersion\":1,\"signature\":\"32EEnGckTnNH+oPeiYEZAFSyZzZXwweRprmwOkMLEEa6QrUcavryoUm9cLArzcZrDPGxU7COtxo7193yG4gMVirw==\",\"vers\":\"1.1dev\",\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"type\":3,\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"snapshotPoint\":{\"rewardRatio\":0,\"msgHashTreeRoot\":\"TTTT\"},\"timestamp\":1548488162337}";
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
        String message = "{\"snapVersion\":1,\"signature\":\"32EEnGckTnNH+oPeiYEZAFSyZzZXwweRprmwOkMLEEa6QrUcavryoUm9cLArzcZrDPGxU7COtxo7193yG4gMVirw==\",\"vers\":\"1.1dev\",\"fromAddress\":\"4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7\",\"type\":3,\"pubkey\":\"A78IhF6zjQIGzuzKwrjG9HEISz7/oAoEhyr7AnBr3RWn\",\"snapshotPoint\":{\"rewardRatio\":0,\"msgHashTreeRoot\":\"TTTT\"},\"timestamp\":1548488162337}";
        System.out.println(message);
        long num = 100L;
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
     * 验证快照消息签名
     * @throws Exception 异常
     */
    @Test
    public void createMessageAndVerifySignTest() throws Exception {
        SnapshotMessage tran = new SnapshotMessage(
                "shield salmon sport horse cool hole pool panda embark wrap fancy equip",
                "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7",
                BigInteger.ONE,
                null,
                new SnapshotPoint.Builder().msgHashTreeRoot("TTTT").build());
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
        SnapshotMessage tran = new SnapshotMessage(
                "shield salmon sport horse cool hole pool panda embark wrap fancy equip",
                "4PS6MZX6T7ELDSD2RUOZRSYGCC5RHOS7",
                BigInteger.ONE,
                null,
                new SnapshotPoint.Builder().msgHashTreeRoot("TTTT").build());
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
