package org.exodus.reward.http;

import static org.exodus.reward.http.JSONResponses.ERROR_INCORRECT_REQUEST;
import static org.exodus.reward.http.JSONResponses.POST_REQUIRED;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.exodus.reward.RWD;
import org.exodus.reward.RWDException;
import org.exodus.reward.util.JSON;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "api")
@Component
public class APIServlet extends HttpServlet {
	public abstract static class APIRequestHandler {

		private final List<String> parameters;
		private final Set<APITag> apiTags;

		protected APIRequestHandler(APITag[] apiTags, String... parameters) {
			this.parameters = Collections.unmodifiableList(Arrays.asList(parameters));
			this.apiTags = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(apiTags)));
		}

		final List<String> getParameters() {
			return parameters;
		}

		final Set<APITag> getAPITags() {
			return apiTags;
		}

		public abstract JSONStreamAware processRequest(HttpServletRequest request) throws RWDException;

		public boolean requirePost() {
			return false;
		}

	}

	private static final boolean enforcePost = RWD.getBooleanProperty("rwd.apiServerEnforcePOST");

	private Map<String, APIRequestHandler> apiRequestHandlers = Collections
			.synchronizedMap(new HashMap<String, APIRequestHandler>());

	public void register(String requestType, APIRequestHandler handler) {
		apiRequestHandlers.put(requestType, handler);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private");
		resp.setHeader("Pragma", "no-cache");
		resp.setDateHeader("Expires", 0);

		JSONStreamAware response = JSON.emptyJSON;

		try {

			long startTime = System.currentTimeMillis();

//			if (API.allowedBotHosts != null && !API.allowedBotHosts.contains(req.getRemoteHost())) {
//				response = ERROR_NOT_ALLOWED;
//				return;
//			}

			String requestType = req.getParameter("requestType");
			if (requestType == null) {
				response = ERROR_INCORRECT_REQUEST;
				return;
			}

			APIRequestHandler apiRequestHandler = apiRequestHandlers.get(requestType);
			if (apiRequestHandler == null) {
				response = ERROR_INCORRECT_REQUEST;
				return;
			}

			if (enforcePost && apiRequestHandler.requirePost() && !"POST".equals(req.getMethod())) {
				response = POST_REQUIRED;
				return;
			}

			try {

				response = apiRequestHandler.processRequest(req);
			} catch (ParameterException e) {
				response = e.getErrorResponse();
			} catch (RWDException | RuntimeException e) {
				log.error("Error processing API request", e);
				response = ERROR_INCORRECT_REQUEST;
			} finally {
			}

			if (response instanceof JSONObject) {
				((JSONObject) response).put("requestProcessingTime", System.currentTimeMillis() - startTime);
			}

		} finally {
			resp.setContentType("text/plain; charset=UTF-8");
			try (Writer writer = resp.getWriter()) {
				response.writeJSONString(writer);
			}
		}

	}
}
