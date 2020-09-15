package org.exodus.reward.http;

import org.exodus.reward.util.JSON;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

public class JSONResponses {
	public static final JSONStreamAware ERROR_INCORRECT_REQUEST;
	static {
		JSONObject response = new JSONObject();
		response.put("errorCode", 1);
		response.put("errorDescription", "Incorrect request");
		ERROR_INCORRECT_REQUEST = JSON.prepare(response);
	}

	public static final JSONStreamAware POST_REQUIRED;
	static {
		JSONObject response = new JSONObject();
		response.put("errorCode", 1);
		response.put("errorDescription", "This request is only accepted using POST!");
		POST_REQUIRED = JSON.prepare(response);
	}

	public static final JSONStreamAware ERROR_NOT_ALLOWED;
	static {
		JSONObject response = new JSONObject();
		response.put("errorCode", 7);
		response.put("errorDescription", "Not allowed");
		ERROR_NOT_ALLOWED = JSON.prepare(response);
	}
}
