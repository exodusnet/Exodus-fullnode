package org.exodus.reward.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: LocalFullnodePostman
 * @Description: with the help of <code>Signer</code>
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 3, 2020
 *
 */
public class LocalFullnodePostman {
	// sendMessageURL is like "http://localhost:35893/v1/sendmsg"
	public static String post(final String sendMessageURL, final String signedMessage) {
		HashMap<String, String> params = new HashMap<String, String>() {
			{
				put("message", signedMessage);
			}
		};

		try {
			String sendMessageURLResult = HttpUtils.httpPost(sendMessageURL, params);

			return sendMessageURLResult;
		} catch (Exception e) {
			e.printStackTrace();

			throw new RuntimeException("failed to send to " + sendMessageURL);
		}

	}

	public static String rawPost(final String sendMessageURL, final String signedMessage) {
		Map<String, String> headers = new HashMap<String, String>() {
			{
				put("Content-Type", "text/plain");
			}
		};

		HashMap<String, String> params = new HashMap<String, String>() {
			{
				put("message", signedMessage);
			}
		};

		String parameters = JSON.toJSONString(params);

		try {
			String sendMessageURLResult = HttpSender.sendPost(sendMessageURL, parameters, headers);

			return sendMessageURLResult;
		} catch (Exception e) {
			e.printStackTrace();

			throw new RuntimeException("failed to send to " + sendMessageURL);
		}

	}

	public static void askForBroker(final String localFolder, final String sendMessageURL, final String signedMessage) {
		FileWriter out = null;
		HashMap<String, String> params = new HashMap<String, String>() {
			{
				put("message", signedMessage);
			}
		};

		String parameters = JSON.toJSONString(params);

		JSONObject json = new JSONObject();
		json.put("url", sendMessageURL);
		json.put("contentType", "text/plain");
		json.put("method", "POST");
		json.put("content", parameters);

		File folder = new File(localFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		try {
			out = new FileWriter(localFolder + "/" + System.currentTimeMillis() + ".brk");
			out.write(json.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
