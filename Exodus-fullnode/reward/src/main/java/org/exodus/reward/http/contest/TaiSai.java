package org.exodus.reward.http.contest;

import java.math.BigInteger;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.exodus.reward.RWDException;
import org.exodus.reward.http.APIServlet;
import org.exodus.reward.http.APIServlet.APIRequestHandler;
import org.exodus.reward.http.APITag;
import org.exodus.reward.past.PeriodicReporter;
import org.exodus.reward.past.Shabal256;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaiSai extends APIRequestHandler {

	private BigInteger lowest = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);
	private int round = 0;
	private String bag = null;

	final int MAX_ROUND = 100;

	@Autowired
	private APIServlet servlet;
	@Autowired
	private PeriodicReporter pReporter;

	private TaiSai() {
		super(new APITag[] { APITag.TaiSai }, "gensig", "proof", "bag");
	}

	@PostConstruct
	public void init() {
		servlet.register("participateinTaiSai", this);
		pReporter.start();
	}

	@Override
	public JSONStreamAware processRequest(HttpServletRequest request) throws RWDException {
		round++;
		byte[] gensig = request.getParameter("gensig").getBytes();
		byte[] proof = request.getParameter("proof").getBytes();
		String bag = request.getParameter("bag");

		Shabal256 md = new Shabal256();
		md.reset();
		md.update(gensig);
		md.update(proof);
		byte[] hash = md.digest();
		BigInteger num = new BigInteger(1,
				new byte[] { hash[7], hash[6], hash[5], hash[4], hash[3], hash[2], hash[1], hash[0] });

		if (num.compareTo(lowest) < 0) {
			lowest = num;
			this.bag = bag;
		}

		if (round >= MAX_ROUND) {
			// who is luck star
		}

		JSONObject response = new JSONObject();
		return response;

	}

	public String getBag() {
		return bag;
	}

	public boolean requirePost() {
		return true;
	}

	public void reset() {
		lowest = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16);
		round = 0;
		bag = null;
	}

}
