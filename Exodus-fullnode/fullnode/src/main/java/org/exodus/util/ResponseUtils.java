package org.exodus.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ResponseUtils {

    public static String normalResponse() {
        return normalResponse("");
    }

    public static String normalResponse(String data) {
        JSONObject obj = new JSONObject();
        obj.put("code", 200);
        obj.put("data", data);
        return JSON.toJSONString(obj);
    }

    public static String paramIllegalResponse() {
        return paramIllegalResponse("params illegal");
    }

    public static String paramIllegalResponse(String errorMessage) {
        JSONObject obj = new JSONObject();
        obj.put("code", 201);
        obj.put("data", errorMessage);
        return JSON.toJSONString(obj);
    }

    public static String handleExceptionResponse() {
        return handleExceptionResponse("handle exception");
    }

    public static String handleExceptionResponse(String errorMessage) {
        JSONObject obj = new JSONObject();
        obj.put("code", 202);
        obj.put("data", errorMessage);
        return JSON.toJSONString(obj);
    }

    public static String response(int code, String data) {
        JSONObject obj = new JSONObject();
        obj.put("code", code);
        obj.put("data", data);
        return JSON.toJSONString(obj);
    }
}
