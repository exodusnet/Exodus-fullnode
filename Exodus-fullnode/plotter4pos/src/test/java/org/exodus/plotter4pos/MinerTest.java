package org.exodus.plotter4pos;

import org.junit.Test;

public class MinerTest {
	// @Test
	public void testStartGeneration() {
		String[] args = { "generate", "--startNonce", "50", "--plots", "50", "--staggeramt", "1" };

		Miner.main(args);
	}

	@Test
	public void testStartMineing() {
		String[] args = { "mine" };

		Miner.main(args);
	}

}
