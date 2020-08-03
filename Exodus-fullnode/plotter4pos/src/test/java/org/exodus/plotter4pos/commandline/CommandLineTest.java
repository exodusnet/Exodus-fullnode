package org.exodus.plotter4pos.commandline;

import org.junit.Test;

public class CommandLineTest {

	@Test
	public void testParse() {
		//@formatter:off
		//generate --address 1122 --startNonce 10 --plots 10 --staggeramt 4096
		//@formatter:on
		String[] args = { "generate", "--startNonce", "10", "--plots", "10", "--staggeramt", "4096" };

		GenCondition condition = CommandLine.parse(args, new GenCondition());

		System.out.println("plots is:" + condition.getPlots());
	}
}
