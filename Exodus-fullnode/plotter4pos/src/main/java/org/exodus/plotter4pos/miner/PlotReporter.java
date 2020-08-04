package org.exodus.plotter4pos.miner;

import org.exodus.plotter4pos.miner.GenSupervisor.msgPlantResult;

import akka.actor.AbstractActor;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: PlotReporter
 * @Description: report disk occupation
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 4, 2020
 *
 */
public class PlotReporter extends AbstractActor {

	@Override
	public Receive createReceive() {
		return this.receiveBuilder().match(msgPlantResult.class, m -> {
			// start to report disk occupation
		}).build();
	}

}
