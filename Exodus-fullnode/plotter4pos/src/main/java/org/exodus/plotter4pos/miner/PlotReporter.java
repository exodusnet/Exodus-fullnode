package org.exodus.plotter4pos.miner;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.exodus.bean.message.TextMessage;
import org.exodus.bean.wallet.LocalWalletBuilder;
import org.exodus.bean.wallet.Wallet;
import org.exodus.plotter4pos.util.Convert;
import org.exodus.plotter4pos.util.LocalFullnodePostman;

import com.google.gson.Gson;

import akka.actor.AbstractActor;
import akka.actor.Cancellable;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

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

	Cancellable tick = null;

	@Override
	public Receive createReceive() {
		return this.receiveBuilder().match(msgRefreshNetState.class, m -> {
			// start to report disk occupation
			LocalWalletBuilder localWalletBuilder = new LocalWalletBuilder();
			Wallet wallet = localWalletBuilder.build("plots/" + ".passphrases", false);

			if (wallet != null) {
				String address = wallet.getAddress();

				List<File> matchedFiles = getFiles(new File("plots/"), Convert.fullHashToId(address.getBytes()) + "");
				if (matchedFiles.size() != 0) {
					File matchedFile = matchedFiles.get(0);
					long fileSize = matchedFile.length();

					final String sendMessageURL = "http://192.168.207.130:35796/v1/sendplantmsg";
					// final String sendMessageURL =
					// "http://c-prod-1.exodusnetwork.io/v1/sendplantmsg";
					postTextMessage(wallet, new MsgPlantResult(address, fileSize), sendMessageURL);

				}
			} else {
				System.err.println("need to generate plot at first");

				log.info("starting terminating...");
				getContext().system().terminate();
			}
		}).build();
	}

	@Override
	public void preStart() {
		tick = getContext().system().scheduler().schedule(Duration.Zero(), Duration.create(10, TimeUnit.MINUTES),
				getSelf(), (Object) new msgRefreshNetState(), getContext().system().dispatcher(), null);
	}

	public static class msgRefreshNetState {
	}

	void postTextMessage(Wallet wallet, MsgPlantResult msgPlantResult, final String sendMessageURL) {
//		Signer signer = new Signer(wallet.getPubKey(), wallet.getPriKey());
//		String signedTextTx = signer.signTextTx(wallet.getAddress(), new Gson().toJson(msgPlantResult).getBytes());

		// (String words, String fromAddress, byte[] context)
		try {
			TextMessage textMessage = new TextMessage(wallet.getMnemonic(), msgPlantResult.address,
					new Gson().toJson(msgPlantResult).getBytes(), BigInteger.valueOf(100000));

			String feedback = LocalFullnodePostman.post(sendMessageURL, new Gson().toJson(textMessage));

			System.out.println("get a feedback: " + feedback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected List<File> getFiles(File folder, String startWith) {
		List<File> matchedFiles = Collections.synchronizedList(new ArrayList<File>());

		if (folder != null && folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File f : files) {
				String name = f.getName();

				if (name.startsWith(startWith)) {
					matchedFiles.add(f);
				}
			}
		}

		return matchedFiles;
	}

}
