package org.exodus.plotter4pos.miner;

import org.exodus.plotter4pos.miner.GenSupervisor.msgPlantResult;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

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
	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public Receive createReceive() {
		return this.receiveBuilder().match(msgPlantResult.class, m -> {
			// start to report disk occupation

			log.info("starting terminating...");
			getContext().system().terminate();
		}).build();
	}

}
