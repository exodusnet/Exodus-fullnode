package org.exodus.reward.http;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.exodus.reward.RWDException;
import org.exodus.reward.http.APIServlet.APIRequestHandler;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Welcome extends APIRequestHandler {

	@Autowired
	private APIServlet servlet;

	private Welcome() {
		super(new APITag[] { APITag.HELLO }, "name");
	}

	@PostConstruct
	public void init() {
		servlet.register("hello", this);
	}

	@Override
	public JSONStreamAware processRequest(HttpServletRequest request) throws RWDException {

		String name = request.getParameter("name");
		JSONObject response = new JSONObject();

		if (name == null || name.length() == 0) {
			response.put("errorCode", 4);
			response.put("errorDescription", "Incorrect parameter");
		} else {
			response.put("name", "welcome," + name);
		}

		return response;

	}

	@Override
	public boolean requirePost() {
		return true;
	}

}
