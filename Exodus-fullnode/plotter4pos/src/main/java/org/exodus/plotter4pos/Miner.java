package org.exodus.plotter4pos;

import java.util.concurrent.CompletableFuture;

import org.exodus.bean.wallet.LocalWalletBuilder;
import org.exodus.bean.wallet.Wallet;
import org.exodus.plotter4pos.commandline.CommandLine;
import org.exodus.plotter4pos.commandline.GenCondition;
import org.exodus.plotter4pos.miner.GenSupervisor;
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

			ActorSystem system = startGeneration(condition);
			CompletableFuture<Terminated> f = system.getWhenTerminated().toCompletableFuture();

			// blocking
			while (!f.isDone()) {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

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
