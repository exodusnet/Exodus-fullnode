package org.exodus.plotter4pos.miner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.exodus.plotter4pos.util.Convert;
import org.exodus.plotter4pos.util.MiningPlot;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.RoundRobinPool;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: GenSupervisor
 * @Description: coordinate plotting process with the help of others
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 4, 2020
 *
 */
public class GenSupervisor extends AbstractActor {
	private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	private GenParams condition;

	private ActorRef worker = null;
	private long currentNonce;
	private long recvresults;
	private byte[] outbuffer;

	private long generatedBytesAmount = 0;

	FileOutputStream out;

	public GenSupervisor(GenParams condition) {
		super();
		this.condition = condition;
	}

	@Override
	public Receive createReceive() {
		return this.receiveBuilder().match(PlotGenerator.msgGenerateResult.class, m -> {
			recvresults++;
			processPlot(m.plot, m.nonce);
			long staggeramt = condition.staggeramt;
			long startnonce = condition.startnonce;
			long plots = condition.plots;

			if (recvresults >= staggeramt) {
				System.out.println("Writing from nonce " + currentNonce);
				out.write(outbuffer);
				generatedBytesAmount += outbuffer.length;
				currentNonce += staggeramt;

				if (currentNonce < startnonce + plots) {
					sendWork();
				} else {
					out.close();
//					log.info("starting terminating...");
//					getContext().system().terminate();

					ActorRef reporter = getContext().actorOf(Props.create(PlotReporter.class));
					reporter.tell(new msgPlantResult(condition.address, generatedBytesAmount), this.getSelf());
				}
			}
		}).build();
	}

	private void processPlot(MiningPlot p, long nonce) {
		log.info("starting plotting");
		long off = nonce - currentNonce;
		long staggeramt = condition.staggeramt;
		for (int i = 0; i < MiningPlot.SCOOPS_PER_PLOT; i++) {
			System.arraycopy(p.data, i * MiningPlot.SCOOP_SIZE, outbuffer,
					(int) ((i * MiningPlot.SCOOP_SIZE * staggeramt) + (off * MiningPlot.SCOOP_SIZE)),
					MiningPlot.SCOOP_SIZE);
		}
	}

	@Override
	public void preStart() throws Exception {
		init();
	}

	private void init() {
		// worker = this.getContext().actorOf(Props.create(PlotGenerator.class));
		worker = getContext().actorOf(new RoundRobinPool(1).props(Props.create(PlotGenerator.class)));
		currentNonce = condition.startnonce;
		outbuffer = new byte[(int) (condition.plots * MiningPlot.PLOT_SIZE)];

		String outname = String.valueOf(condition.addr);
		outname += "_";
		outname += condition.startnonce;
		outname += "_";
		outname += condition.plots;
		outname += "_";
		outname += condition.staggeramt;

		try {
			File folder = new File("plots");
			if (!folder.exists()) {
				folder.mkdir();
			}
			out = new FileOutputStream(new File("plots/" + outname), false);

		} catch (FileNotFoundException e) {
			log.error("Failed to open file %s for writing", outname);
			System.out.println("Failed to open file" + outname + "for writing");
			e.printStackTrace();
			getContext().system().whenTerminated();
		}

		sendWork();
	}

	private void sendWork() {
		recvresults = 0;
		System.out.println("Generating from nonce: " + currentNonce);
		for (long i = 0; i < condition.staggeramt; i++) {
			worker.tell(new PlotGenerator.msgGenerate(condition.addr, currentNonce + i), getSelf());
		}
	}

	public static class GenParams {
		public String address;
		public long addr;
		public long startnonce;
		public long plots;
		public long staggeramt;

		public GenParams(String address, long startnonce, long plots, long staggeramt) {
			this.address = address;

			this.addr = Convert.fullHashToId(address.getBytes());
			this.startnonce = startnonce;
			this.plots = plots;
			this.staggeramt = staggeramt;
		}
	}

	public static class msgPlantResult {
		public long generatedBytesAmount;
		public String address;

		public msgPlantResult(String address, long generatedBytesAmount) {
			this.address = address;
			this.generatedBytesAmount = generatedBytesAmount;
		}
	}

}
