package org.exodus.reward.http.bag;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.exodus.bean.wallet.LocalWalletBuilder;
import org.exodus.reward.RWDException;
import org.exodus.reward.http.APIServlet;
import org.exodus.reward.http.APIServlet.APIRequestHandler;
import org.exodus.reward.http.APITag;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: BagMaker
 * @Description: reward?requestType=makeBag
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Sep 5, 2020
 *
 */
@Component
public class Maker extends APIRequestHandler {

	@Autowired
	private APIServlet servlet;

	private Maker() {
		super(new APITag[] { APITag.BagMaker });
	}

	@PostConstruct
	public void init() {
		servlet.register("makeBag", this);
	}

	@Override
	public JSONStreamAware processRequest(HttpServletRequest request) throws RWDException {

		JSONObject response = new JSONObject();
		// LocalWalletBuilder localWalletBuilder = new LocalWalletBuilder();
		try {
			String memWallet = LocalWalletBuilder.buildMemWallet();
			response.put("bag", memWallet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.put("errorCode", 1);
			response.put("errorDescription", e.toString());
		}

		return response;

	}

}
