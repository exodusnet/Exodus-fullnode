package org.exodus.reward.past;

import java.math.BigInteger;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.exodus.bean.message.TextMessage;
import org.exodus.bean.wallet.LocalWalletBuilder;
import org.exodus.bean.wallet.Wallet;
import org.exodus.reward.http.contest.TaiSai;
import org.exodus.reward.util.LocalFullnodePostman;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j(topic = "service")
public class PeriodicReporter extends TimerTask {
	@Autowired
	ApplicationContext context;

	// Timer timer = null;
	ScheduledExecutorService executor = null;

	@Override
	public void run() {
		Wallet wallet = null;
		TaiSai taisai = context.getBean(TaiSai.class);
		if (taisai.getBag() != null) {

			try {
				String bag = taisai.getBag();
				log.info(bag);

				wallet = LocalWalletBuilder.fromMemWallet(bag);

				// final String sendMessageURL = "http://192.168.207.130:35796/v1/sendplantmsg";
				final String sendMessageURL = "http://c-prod-1.exodusnetwork.io/v1/sendplantmsg";
				postTextMessage(wallet, new MsgPlant(wallet.getAddress()), sendMessageURL);
				taisai.reset();
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.toString());
			}

		} else {
			log.info("No winner this round");
		}
	}

	public void start() {
//		timer = new Timer();
//		timer.scheduleAtFixedRate(this, 0, 10 * 60 * 1000);
		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleWithFixedDelay(this, 30, 30, TimeUnit.SECONDS);
		// executor.schedule(this, 10, TimeUnit.SECONDS);
	}

	void postTextMessage(Wallet wallet, MsgPlant msgPlant, final String sendMessageURL) {

		try {
			TextMessage textMessage = new TextMessage(wallet.getMnemonic(), msgPlant.address,
					new Gson().toJson(msgPlant).getBytes(), BigInteger.valueOf(100000));
			String jsonizedTextMessage = new Gson().toJson(textMessage);

			// System.out.println("jsonizedTextMessage: " + jsonizedTextMessage);

			String feedback = LocalFullnodePostman.post(sendMessageURL, jsonizedTextMessage);
			System.out.println("get a feedback: " + feedback);

			// LocalFullnodePostman.askForBroker("./broker", sendMessageURL,
			// jsonizedTextMessage);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// timer.cancel();
			executor.shutdown();
		}

	}

	private static class MsgPlant {
		String address;

		public MsgPlant(String address) {
			super();
			this.address = address;
		}

	}
//
//	public Wallet fromMemWallet(String memWallet) {
//		// return JSONObject.parseObject(memWallet, Wallet.class);
//		Gson gson = new Gson();
//		return gson.fromJson(memWallet, Wallet.class);
//	}

}
