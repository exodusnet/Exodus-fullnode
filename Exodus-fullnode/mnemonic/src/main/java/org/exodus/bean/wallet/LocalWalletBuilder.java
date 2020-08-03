package org.exodus.bean.wallet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: LocalWalletBuilder
 * @Description: read wallet from local file system or build new wallet in file
 *               system before starting to read it
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 2, 2020
 *
 */
public class LocalWalletBuilder {
	public Wallet build(String location) throws Exception {
		File loc = new File(location);
		Wallet w = null;

		if (!loc.exists()) {// brand new case
			w = buildNewWallet(loc);
		} else {
			w = loadWallet(loc);
		}

		return w;
	}

	private Wallet buildNewWallet(File file) throws Exception {
		Wallet w = WalletBuilder.generateWallet();

		try {
			w = WalletBuilder.generateWallet();

			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(JSONObject.toJSONString(w).getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return w;
	}

	private Wallet loadWallet(File file) throws Exception {
		Wallet w;
		FileInputStream inputStream = new FileInputStream(file);
		byte[] data = new byte[inputStream.available()];
		inputStream.read(data);
		inputStream.close();

		JSONObject walletObj = JSONObject.parseObject(new String(data));
		Keys extKeys = JSONObject.parseObject(walletObj.getString("extKeys"), Keys.class);
		Keys keys = JSONObject.parseObject(walletObj.getString("keys"), Keys.class);
		w = new Wallet.Builder().mnemonic(walletObj.getString("mnemonic")).extKeys(extKeys).keys(keys)
				.address(walletObj.getString("address")).build();

		return w;
	}
}
