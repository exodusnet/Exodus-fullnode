package org.exodus.localfullnode2.utilities;

import java.math.BigInteger;
import java.time.Instant;

import org.bitcoinj.core.ECKey;
import org.exodus.bean.message.MessageType;
import org.exodus.bean.message.MessageVersion;
import org.exodus.bean.message.TransactionMessage;
import org.exodus.core.Constant;
import org.exodus.utils.DSA;
import org.exodus.utils.SignUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * All rights reserved.
 * 
 * @Description: signature tools which are able to sign all kinds of messages
 *               using existed public-private key pair.
 * @author: Francis.Deng
 * @see TransactionMessage
 * @date: May 22, 2019 12:39:34 AM
 * @version: V1.0
 */
public class Signer {
	private final String sPublicKey;
	private final String sPrivateKey;

	public Signer(String sPublicKey, String sPrivateKey) {
		super();
		this.sPublicKey = sPublicKey;
		this.sPrivateKey = sPrivateKey;
	}

	/**
	 * signing a "2.0" transaction message
	 */
	public String signTx(String fromAddress, String toAddress, BigInteger amount, BigInteger fee, BigInteger nrgPrice) {
		JSONObject json = buildMessage("2.0", fromAddress, toAddress, amount, fee, nrgPrice);

		String msgSignature = SignUtil.sign(json.toJSONString(), ECKey.fromPrivate(DSA.decryptBASE64(sPrivateKey)));

		json.put("signature", msgSignature);

		return json.toJSONString();
	}

	/**
	 * signing a "2.0" text message
	 */
	public String signTextTx(String fromAddress, byte[] context) {
		JSONObject json = buildTextMessage("2.0", fromAddress, context);

		String msgSignature = SignUtil.sign(json.toJSONString(), ECKey.fromPrivate(DSA.decryptBASE64(sPrivateKey)));

		json.put("signature", msgSignature);

		return json.toJSONString();
	}

	private JSONObject buildTextMessage(String ver, String fromAddress, byte[] context) {
		JSONObject json = new JSONObject();
		json.put("fromAddress", fromAddress);
		// json.put("toAddress", toAddress);
		json.put("amount", "0");
		json.put("timestamp", Instant.now().toEpochMilli());
		json.put("pubkey", sPublicKey);
		if (MessageVersion.DEV_1_0.equals(ver)) {
			json.put("fee", "0");
		} else if (MessageVersion.PRO_2_0.equals(ver)) {
			if (Constant.NEED_FEE) {
				json.put("fee", "0");
				json.put("nrgPrice", "0");
			}
		}

		json.put("type", MessageType.TEXT.getIndex());
		json.put("vers", ver);

		json.put("context", context);

		return json;
	}

	private JSONObject buildMessage(String ver, String fromAddress, String toAddress, BigInteger amount, BigInteger fee,
			BigInteger nrgPrice) {
		JSONObject json = new JSONObject();
		json.put("fromAddress", fromAddress);
		json.put("toAddress", toAddress);
		json.put("amount", amount.toString());
		json.put("timestamp", Instant.now().toEpochMilli());
		json.put("pubkey", sPublicKey);
		if (MessageVersion.DEV_1_0.equals(ver)) {
			json.put("fee", fee.toString());
		} else if (MessageVersion.PRO_2_0.equals(ver)) {
			if (Constant.NEED_FEE) {
				json.put("fee", fee.toString());
				json.put("nrgPrice", nrgPrice.toString());
			}
		}

		json.put("type", MessageType.TRANSACTIONS.getIndex());
		// json.put("remark", this.getRemark());
//		if (StringUtils.isNotEmpty(msgSignature)) {
//			json.put("signature", msgSignature);
//		}
		json.put("vers", ver);

		return json;
	}

}
