package org.exodus.plotter4pos;

import java.util.concurrent.CompletableFuture;

import org.exodus.bean.wallet.LocalWalletBuilder;
import org.exodus.bean.wallet.Wallet;
import org.exodus.plotter4pos.commandline.CommandLine;
import org.exodus.plotter4pos.commandline.GenCondition;
import org.exodus.plotter4pos.miner.GenSupervisor;
import org.exodus.plotter4pos.miner.PlotReporter;
import org.exodus.plotter4pos.util.Convert;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: Miner
 * @Description: provide a command interface and ordinate with other actors
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 1, 2020
 *
 */
public class Miner {

	public static void main(String[] args) {
		ActorSystem system = null;
		if (args.length < 1) {
			// print help information
		}

		String action = args[0];
		switch (action) {
		case "generate":
//			if (args.length < 6) {
//				System.out.println("missing args");
//				return;
//			}
//			if (Long.parseLong(args[4]) < 0 || Long.parseLong(args[4]) > 8191) {
//				System.out.println("staggeramt must be 1-8191");
//				return;
//			}
			GenCondition condition = CommandLine.parse(args, new GenCondition());

			system = startGeneration(condition);

			break;
		case "mine":
//			if (args.length < 2) {
//				System.out.println("missing args");
//				return;
//			}
			system = startMing();
			break;
		case "dumpaddr":

			break;
		default:
			System.out.println("invalid action");
			break;
		}

		if (system != null) {
			CompletableFuture<Terminated> f = system.getWhenTerminated().toCompletableFuture();

			// blocking process quitting
			while (!f.isDone()) {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected static ActorSystem startMing() {
		ActorSystem system = null;
		try {
			system = ActorSystem.create();
			ActorRef gensupr = system.actorOf(Props.create(PlotReporter.class));
		} catch (Exception e) {
			e.printStackTrace();

			System.err.println("failed to start mining");
		}

		return system;
	}

	protected static ActorSystem startGeneration(GenCondition cond) {
		long addr, startnonce, plots, staggeramt;
		// addr = Convert.parseUnsignedLong(cond.getAddress());
		startnonce = Convert.parseUnsignedLong(cond.getStartNonce());
		plots = Convert.parseUnsignedLong(cond.getPlots());
		staggeramt = Convert.parseUnsignedLong(cond.getStaggeramt());

		LocalWalletBuilder localWalletBuilder = new LocalWalletBuilder();
		ActorSystem system = null;
		try {
			Wallet wallet = localWalletBuilder.build("plots/" + ".passphrases");
			System.out.println("words: [" + wallet.getMnemonic() + "]");

			system = ActorSystem.create();
			ActorRef gensupr = system.actorOf(Props.create(GenSupervisor.class,
					new GenSupervisor.GenParams(wallet.getAddress(), startnonce, plots, staggeramt)));
		} catch (Exception e) {
			e.printStackTrace();

			System.err.println("failed to build a wallet");
		}

		return system;

	}

}
