package org.exodus.bean.message;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息协议格式版本
 */
public class MessageVersion {
    public static final String DEV_1_0 = "1.0dev";
    public static final String PRO_2_0 = "2.0";

    public static final Map<String, String> NAMES = new HashMap<String, String>(){
        {
            put(DEV_1_0, "Transformation snapshot data structure, and add attribution vers into BaseMessage.");
            put(PRO_2_0, "Attribution fee unit change atom to nrg, and add MessageProtoVersion enum.");
        }
    };

    public static void main(String[] args) {
        System.out.println(MessageVersion.NAMES.keySet().contains("1.0dev"));
        System.out.println(MessageVersion.NAMES.keySet().contains("1.0.1dev"));
        System.out.println(MessageVersion.NAMES.containsKey("1.0.1dev"));
        System.out.println(MessageVersion.NAMES.containsKey("1.1dev"));
    }
}
