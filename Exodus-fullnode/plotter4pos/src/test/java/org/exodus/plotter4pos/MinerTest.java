package org.exodus.plotter4pos;

import org.junit.Test;

public class MinerTest {

	@Test
	public void testStartGeneration() {
		String[] args = { "generate", "--startNonce", "50", "--plots", "10", "--staggeramt", "1" };

		Miner.main(args);

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
