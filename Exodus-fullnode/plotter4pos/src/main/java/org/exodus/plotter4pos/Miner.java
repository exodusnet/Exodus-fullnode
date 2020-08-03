package org.exodus.plotter4pos;

import org.exodus.bean.wallet.LocalWalletBuilder;
import org.exodus.bean.wallet.Wallet;
import org.exodus.plotter4pos.commandline.CommandLine;
import org.exodus.plotter4pos.commandline.GenCondition;
import org.exodus.plotter4pos.miner.GenSupervisor;
import org.exodus.plotter4pos.util.Convert;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: Miner
 * @Description: TODO
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 1, 2020
 *
 */
public class Miner {

	public static void main(String[] args) {
		if (args.length < 1) {
			// print help information
		}

		String action = args[0];
		switch (action) {
		case "generate":
			if (args.length < 6) {
				System.out.println("missing args");
				return;
			}
			if (Long.parseLong(args[4]) < 0 || Long.parseLong(args[4]) > 8191) {
				System.out.println("staggeramt must be 1-8191");
				return;
			}
			GenCondition condition = CommandLine.parse(args, new GenCondition());

			startGeneration(condition);
			break;
		case "mine":
			if (args.length < 2) {
				System.out.println("missing args");
				return;
			}

			break;
		case "dumpaddr":

			break;
		default:
			System.out.println("invalid action");
			break;
		}
	}

	protected static void startGeneration(GenCondition cond) {
		long addr, startnonce, plots, staggeramt;
		// addr = Convert.parseUnsignedLong(cond.getAddress());
		startnonce = Convert.parseUnsignedLong(cond.getStartNonce());
		plots = Convert.parseUnsignedLong(cond.getPlots());
		staggeramt = Convert.parseUnsignedLong(cond.getStaggeramt());

		LocalWalletBuilder localWalletBuilder = new LocalWalletBuilder();
		try {
			Wallet wallet = localWalletBuilder.build("plots/" + ".passphrases");

			ActorSystem system = ActorSystem.create();
			ActorRef gensupr = system.actorOf(Props.create(GenSupervisor.class, new GenSupervisor.GenParams(
					Convert.fullHashToId(wallet.getPubKey().getBytes()), startnonce, plots, staggeramt)));
		} catch (Exception e) {
			e.printStackTrace();

			System.err.println("failed to build a wallet");
		}

	}

}
