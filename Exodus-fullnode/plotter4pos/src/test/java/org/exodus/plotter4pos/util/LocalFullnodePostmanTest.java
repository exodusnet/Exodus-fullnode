package org.exodus.plotter4pos.util;

import org.junit.Test;

public class LocalFullnodePostmanTest {

	@Test
	public void testPost() {
		Signer signer = new Signer("A5EzYgpcef+fO3s/HsppAfM4JZXN1VC9aVka1YALCZc/",
				"/cJdT9jrlkTDjynRtv8bKoojbqZjGlDdx7H0bM+XAw8=");

		String signedTextTx = signer.signTextTx("ASWD2MRMIEYR27PMUXGOOZYCBOXLOXPK", "hello world".getBytes());
		final String sendMessageURL = "http://192.168.207.130:35796/v1/sendmsg";

		String feedback = LocalFullnodePostman.post(sendMessageURL, signedTextTx);
		System.out.println(feedback);
	}
}
