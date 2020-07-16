package org.exodus.bean.node;

import java.util.HashMap;
import java.util.Map;


/**
 * 节点类型
 * @author Clare
 * @date   2018/11/20 0029.
 */
public class NodeTypes {
    public static final int SEED = 0;
    public static final int FULLNODE = 1;
    public static final int LOCALFULLNODE = 2;
    public static final int RELAYNODE = 3;

    public static final Map<Integer, String> NAMES = new HashMap<Integer, String>(){
        {
            put(SEED, "seed");
            put(FULLNODE, "fullnode");
            put(LOCALFULLNODE, "localfullnode");
            put(RELAYNODE, "relaynode");
        }
    };
}
