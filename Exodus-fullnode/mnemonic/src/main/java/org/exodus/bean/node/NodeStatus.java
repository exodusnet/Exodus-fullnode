package org.exodus.bean.node;


import java.util.HashMap;
import java.util.Map;

/**
 * 节点状态
 * @author Clare
 * @date   2018/11/20 0029.
 */
public class NodeStatus {
    public static final int HAS_INVALID = 0;
    public static final int WAIT_CONSENSUS = 1;
    public static final int WAIT_UPDATE_IN_CONSENSUS = 111;
    public static final int WAIT_UPDATE_HAS_CONSENSUSED = 112;
    public static final int WAIT_DELETE_IN_CONSENSUS = 121;
    public static final int WAIT_DELETE_HAS_CONSENSUSED = 122;
    public static final int IN_CONSENSUS = 2;
    public static final int UPDATTING_IN_CONSENSUS = 211;
    public static final int UPDATTING_HAS_CONSENSUSED = 212;
    public static final int DELETTING_IN_CONSENSUS = 221;
    public static final int DELETTING_HAS_CONSENSUSED = 222;
    public static final int HAS_CONSENSUSED = 3;
    public static final int HAS_SHARDED = 4;

    public static final Map<Integer, String> NAMES = new HashMap<Integer, String>(){
        {
            put(HAS_INVALID, "HAS_INVALID");
            put(WAIT_CONSENSUS, "WAIT_CONSENSUS");
            put(WAIT_UPDATE_IN_CONSENSUS, "WAIT_UPDATE_IN_CONSENSUS");
            put(WAIT_UPDATE_HAS_CONSENSUSED, "WAIT_UPDATE_HAS_CONSENSUSED");
            put(WAIT_DELETE_IN_CONSENSUS, "WAIT_DELETE_IN_CONSENSUS");
            put(WAIT_DELETE_HAS_CONSENSUSED, "WAIT_DELETE_HAS_CONSENSUSED");
            put(IN_CONSENSUS, "IN_CONSENSUS");
            put(UPDATTING_IN_CONSENSUS, "UPDATTING_IN_CONSENSUS");
            put(UPDATTING_HAS_CONSENSUSED, "UPDATTING_HAS_CONSENSUSED");
            put(DELETTING_IN_CONSENSUS, "DELETTING_IN_CONSENSUS");
            put(DELETTING_HAS_CONSENSUSED, "DELETTING_HAS_CONSENSUSED");
            put(HAS_CONSENSUSED, "HAS_CONSENSUSED");
            put(HAS_SHARDED, "HAS_SHARDED");
        }
    };
}
