package org.exodus.bean.message;

import com.alibaba.fastjson.JSONObject;
import org.exodus.core.Constant;
import org.exodus.utils.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FeeUtils {
    /**
     * 计算消息需要花费的手续费
     * @param message 消息
     * @return 手续费
     */
    public static BigInteger calculateMsgFee(String message) {
        JSONObject o = JSONObject.parseObject(message);
        return calculateMsgFee(o);
    }

    /**
     * 计算消息需要花费的手续费
     * @param o 消息
     * @return 手续费
     */
    public static BigInteger calculateMsgFee(JSONObject o) {
        int type = o.getInteger("type");
        if (type == MessageType.TRANSACTIONS.getIndex() ) {
            return calculateFee(o.getString("remark"));
        } else if (type == MessageType.TEXT.getIndex()) {
            return calculateFee(o.getString("context"));
        } else {
            return calculateFee(null);
        }
    }

    /**
     * 计算消息需要花费的手续费
     * @param data 附加信息或者数据
     * @return 手续费
     */
    public static BigInteger calculateFee(String data) {
        long len = (StringUtils.isEmpty(data)) ? 0 : data.length();
        BigInteger needFee = new BigDecimal(len * 1.0 / 1024)
                .multiply(new BigDecimal(Constant.NRG_PEER_KBYTE))
                .toBigInteger();
        needFee = needFee.add(Constant.BASE_NRG);
        return needFee;
    }

    /**
     * 计算消息需要花费的手续费
     * @param dataLen 附加信息或者数据长度
     * @return 手续费
     */
    public static BigInteger calculateFee(long dataLen) {
        BigInteger needFee = new BigDecimal(dataLen * 1.0 / 1024)
                .multiply(new BigDecimal(Constant.NRG_PEER_KBYTE))
                .toBigInteger();
        needFee = needFee.add(Constant.BASE_NRG);
        return needFee;
    }

    public static void main(String[] args) {
        System.out.println(FeeUtils.calculateFee(0));
        System.out.println(FeeUtils.calculateFee(1));
        System.out.println(FeeUtils.calculateFee(10));
        System.out.println(FeeUtils.calculateFee(100));
        System.out.println(FeeUtils.calculateFee(1024));
    }
}
