package org.exodus.plotter4pos.util;

import java.util.HashMap;

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
}
