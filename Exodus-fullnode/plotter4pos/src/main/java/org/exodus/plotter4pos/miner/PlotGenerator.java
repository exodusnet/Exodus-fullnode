package org.exodus.plotter4pos.miner;

import org.exodus.plotter4pos.util.MiningPlot;

import akka.actor.AbstractActor;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: PlotGenerator
 * @Description: attempt to retrieve plotter
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 1, 2020
 *
 */
public class PlotGenerator extends AbstractActor {
	@Override
	public Receive createReceive() {
		return this.receiveBuilder().match(msgGenerate.class, m -> {
			MiningPlot p = new MiningPlot(m.id, m.nonce);
			this.getSender().tell(new msgGenerateResult(m.nonce, p), this.getSelf());
		}).build();
	}

	public static class msgGenerate {
		public long id;
		public long nonce;

		public msgGenerate(long id, long nonce) {
			this.id = id;
			this.nonce = nonce;
		}
	}

	public static class msgGenerateResult {
		public long nonce;
		public MiningPlot plot;

		public msgGenerateResult(long nonce, MiningPlot plot) {
			this.nonce = nonce;
			this.plot = plot;
		}
	}

}
