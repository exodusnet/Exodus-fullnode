package org.exodus.bean.node;

import java.util.HashMap;
import java.util.Map;

/**
 * 跨链支持的主链类型
 * Created by Clare on 2018/12/12 0006.
 */
public class ChainTypes {
    public static final int INVE = 1;
    public static final int BTC = 2;
    public static final int ETH = 3;
//    public static final int SNC = 4;

    public static final Map<Integer, String> NAMES = new HashMap<Integer, String>(){
        {
            put(INVE, "INVE");
            put(BTC, "BTC");
            put(ETH, "ETH");
//            put(SNC, "SNC");
        }
    };
}
