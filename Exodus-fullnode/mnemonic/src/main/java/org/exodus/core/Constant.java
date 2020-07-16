package org.exodus.core;

import java.math.BigInteger;

public class Constant {
    /**
     * 1INVE与最小单位ATOM的兑换比例
     */
    public static final BigInteger INVE_ATOM_RATIO  = BigInteger.valueOf(1000000000000000000L);
    /**
     * 计费标准，每KByte长度收费NRG_PEER_KBYTE个NRG
     */
    public static final BigInteger NRG_PEER_KBYTE = BigInteger.valueOf(100000000L);
    /**
     * 基本交易NRG费用(nrg)
     */
    public static final BigInteger BASE_NRG = BigInteger.valueOf(500000L);
    /**
     * 默认NRG price
     */
    public static final BigInteger DEFAULT_NRG_PRICE = BigInteger.valueOf(1000000000L);
    /**
     * 是否需要手续费
     */
    public static final boolean NEED_FEE = true;
    /**
     * 助记词单词个数
     */
    public static final int MNEMONIC_WORD_SIZE = 12;
    /**
     * 消息getMessage超时时间
     */
    public static final int MESSAGE_PRESEND_TIME_OUT = 5*60*1000;
    /**
     * 交易消息留言最大字节数
     */
    public static final int MAX_TX_REMARK_SIZE = 50;
    /**
     * 接口调用响应正常状态码
     */
    public static final long INTERFACE_RESPONSE_NORMAL_CODE = 200;
}
