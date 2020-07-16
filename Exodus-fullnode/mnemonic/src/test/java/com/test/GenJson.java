package com.test;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bitcoinj.core.ECKey;

import org.exodus.mnemonic.Mnemonic;
import org.exodus.utils.DSA;
import org.exodus.utils.SignUtil;
import org.junit.Test;

public class GenJson {
	
	public static String RandomString(int length) {
//		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(36);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	
	public static String RandomWalletId(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+=";
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(64);
			buf.append(str.charAt(num));
		}
		return buf.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static JSONObject getJson(String wallet, String outputsAddress1, String outputAddress2,
									 String inputUnit, String authAddress, String r, String pub, String jsonUnit){
		JSONObject root = new JSONObject();
		JSONObject unit1 = new JSONObject();
		unit1.put("version", "1.0dev");
		unit1.put("alt", "3");
		
		ArrayList<String> messages = new ArrayList<String>();
		JSONArray messageArray = new JSONArray();
		 
		JSONObject payload = new JSONObject();
		payload.put("app", "payment");
		payload.put("payload_location", "inline");
		payload.put("payload_hash", "2r8sJ+JHbf8eMQM0TnvIbPYti733vKh6s14rHB1nJkA=");
		JSONArray outputs = new JSONArray();
		JSONObject output = new JSONObject();
		output.put("address", outputsAddress1);
		output.put("amount", 300000);
		outputs.add(0, output);
		JSONObject output1 = new JSONObject();
		output1.put("address", outputAddress2);
		output1.put("amount", 9877);
		outputs.add(1, output1);
		
		JSONArray inputs = new JSONArray();
		JSONObject input = new JSONObject();
		input.put("unit", inputUnit);
		input.put("message_index", 0);
		input.put("output_index", 1);
		inputs.add(input);
		
		JSONObject subpayload = new JSONObject();
		
		subpayload.put("outputs", outputs);
		subpayload.put("inputs", inputs);
		payload.put("payload", subpayload);
		
		messageArray.add(payload);
		 
		unit1.put("messages", messageArray);
		
		JSONArray authors = new JSONArray(); 
		JSONObject author = new JSONObject();
		author.put("address", outputAddress2);
		JSONObject rData = new JSONObject();
		rData.put("r", r);
		author.put("authentifiers", rData);
		
		
//		JSONObject difinitions = new JSONObject();
		
		JSONArray sig = new JSONArray();
		JSONObject pubkeys = new JSONObject();
		pubkeys.put("pubkey", pub);
		sig.add("sig");
		sig.add(pubkeys);
		
		author.put("definition", sig);
		
		authors.add(0,author);
		
		unit1.put("authors", authors);
		unit1.put("headers_commission", 262);
		unit1.put("payload_commission", 197);
		unit1.put("walletId", wallet);
		unit1.put("unit", jsonUnit);
		unit1.put("timestamp", 1530839419);
		
		root.put("unit", unit1);
		
		return root;
	}


	class Runsas extends Thread {

		private String path;
		public  Runsas(String path) {
			this.path=path;
		}
		@Override
		public void run(){
			try {
				runJson(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {


		runJson("test100w.json");

	}





	@Test
	public void test1() throws Exception {
		runJson("test231-10w.json");
	}
	@Test
	public void test2() throws Exception {
		runJson("test232-10w.json");
	}
	@Test
	public void test3() throws Exception {
		runJson("test233-10w.json");
	}
	@Test
	public void test4() throws Exception {
		runJson("test234-10w.json");
	}
	@Test
	public void test5() throws Exception {
		runJson("test235-10w.json");
	}





	@Test
	public void test6() throws Exception {
		runJson("test236-10w.json");
	}
	@Test
	public void test7() throws Exception {
		runJson("test237-10w.json");
	}
	@Test
	public void test8() throws Exception {
		runJson("test238-10w.json");
	}
	@Test
	public void test9() throws Exception {
		runJson("test239-10w.json");
	}
	@Test
	public void test10() throws Exception {
		runJson("test230-10w.json");
	}


	public static void runJson(String path) throws Exception {
		Mnemonic mn = new Mnemonic("","");
		JSONArray jsons = new JSONArray();
		int n = 0;
		for(int i=0;i<1;i++){
			String privateKey = DSA.encryptBASE64(mn.getxPrivKey());
//	        System.out.println("privatekey:"+privateKey);
	        String pubKey = DSA.encryptBASE64(mn.getPubKey());
//	        System.out.println("pubkey:"+pubKey);
	        String walletId = DSA.encryptBASE64(GenJson.RandomWalletId(44).getBytes());
//	        System.out.println("wallet:"+walletId);
	        String addressByWallet = GenJson.RandomString(32);
//	        System.out.println("address:"+addressByWallet);
	        System.out.println("-------------��"+i+"��----------------------");

	        String authorAddress = GenJson.RandomString(32);
//	        System.out.println("authorAddress&outputsAddress:"+authorAddress);
	        String outputsAddress0 = GenJson.RandomString(32);
//	        System.out.println("outputs0=[0]:"+GenJson.RandomString(32));

	        String inputUnit = DSA.encryptBASE64(GenJson.RandomWalletId(44).getBytes());
//	        System.out.println("inputUnit enbase64:"+inputUnit);

	        String jsonUnit = DSA.encryptBASE64(GenJson.RandomWalletId(44).getBytes());
//	        System.out.println("JsonUnit enbase64:"+jsonUnit);

	        BigInteger priKBig = mn.getxPrivKeyBig();
//	        System.out.println(priKBig);
	        ECKey key = ECKey.fromPrivate(priKBig);

	        String unit = "{\"unit\":{\"version\":\"1.0dev\",\"alt\":\"3\"," +
	                "\"messages\":[{\"app\":\"payment\",\"payload_location\":\"inline\"," +
	                "\"payload_hash\":\"s0RSJMSp5OOuyxeytMAmkxk6hXqEScnchdrq/jZt4MQ=\"," +
	                "\"payload\":{\"outputs\":[{\"fromAddress\":\""+outputsAddress0+"\",\"amount\":999541}," +
	                "{\"fromAddress\":\""+authorAddress+"\",\"amount\":1000000000}]," +
	                "\"inputs\":[{\"unit\":\""+inputUnit+"\",\"message_index\":0,\"output_index\":0}]}}]," +
	                "\"authors\":[{\"fromAddress\":\""+authorAddress+"\"," +
	                "\"authentifiers\":{\"r\":\"----\"}," +
	                "\"definition\":[\"sig\",{\"pubkey\":\""+ DSA.encryptBASE64(mn.getPubKey()) +"\"}]}]," +
	                "\"headers_commission\":262,\"payload_commission\":197," +
	                "\"walletId\":\""+walletId+"\",\"unit\":\""+jsonUnit+"\",\"timestamp\":1530077212}}";





	        String sinStr = null;

	        JSONObject jsonObject = GenJson.getJson(walletId, outputsAddress0,
	        		authorAddress, inputUnit, authorAddress, sinStr, pubKey, jsonUnit);

	        unit = jsonObject.toString();


//	        jsons.add(jsonObject);


			unit = jsonObject.toString();
	        sinStr = SignUtil.Sign(unit,key);
			jsonObject = GenJson.getJson(walletId, outputsAddress0,
					authorAddress, inputUnit, authorAddress, sinStr, pubKey, jsonUnit);



	        boolean flag  = SignUtil.verify(jsonObject.toString());
			System.out.println(flag);

			if(flag) {
				jsons.add(jsonObject);
                n++;
            }

		}

		String jsonStr = jsons.toString(); //��JSON����ת��Ϊ�ַ���
		try{
//			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(path))));
            FileOutputStream fos = new FileOutputStream(new File(path));
            fos.write(jsonStr.getBytes("utf-8"));
            fos.flush();
            fos.close();
            System.out.println(n);
//			writer.write(jsonStr);
//			writer.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}


//        BigInteger priKBig = mn.getxPrivKeyBig();
//        System.out.println(priKBig);
//        ECKey key = ECKey.fromPrivate(priKBig);


//        byte[] output = key.sign();


		String unit = "{\"unit\":{\"version\":\"1.0dev\",\"alt\":\"3\"," +
				"\"messages\":[{\"app\":\"payment\",\"payload_location\":\"inline\"," +
				"\"payload_hash\":\"s0RSJMSp5OOuyxeytMAmkxk6hXqEScnchdrq/jZt4MQ=\"," +
				"\"payload\":{\"outputs\":[{\"fromAddress\":\"DYODR4RQA2ZF4E3AI4URMRZOBI73STHJ\",\"amount\":999541}," +
				"{\"fromAddress\":\"SQKG6SV4VRH5LVYHOY2LH3YMY3R4BSTW\",\"amount\":1000000000}]," +
				"\"inputs\":[{\"unit\":\"3OlcWP7P3OijcfBHhPfJulNzPZu1yoqcKaeyobBdttQ=\",\"message_index\":0,\"output_index\":0}]}}]," +
				"\"authors\":[{\"fromAddress\":\"DYODR4RQA2ZF4E3AI4URMRZOBI73STHJ\"," +
				"\"authentifiers\":{\"r\":\"----------------------------------------------------------------------------------------\"}," +
				"\"definition\":[\"sig\",{\"pubkey\":\""+ DSA.encryptBASE64(mn.getPubKey()) +"\"}]}]," +
				"\"headers_commission\":262,\"payload_commission\":197," +
				"\"walletId\":\"Cjlvn9JXuxKdvAGrXvVgumIBNBBHhlYWeWzfFRiY2r5=\",\"unit\":\"tttnfxmIyDSemgO3TEerF24IZpb6HMS83LbCWBSq1234\",\"timestamp\":1530077212}}";

//        byte[] pub = "AnvtSPZg8gWJQR5W0tV69DNoi6Bol30bo1RClBhpChH8".getBytes();

		System.out.println("��˽Կ����Ϣ��������ǩ��");


//        System.out.println("˽Կformat��" + privateKey);
//        System.out.println("��Կformat��" + "");

		//����ǩ��
//        String sinStr = SignUtil.Sign(unit , key);
//
//        System.out.println(sinStr);
//        unit = "{\"unit\":{\"version\":\"1.0dev\",\"alt\":\"3\"," +
//                "\"messages\":[{\"app\":\"payment\",\"payload_location\":\"inline\"," +
//                "\"payload_hash\":\"s0RSJMSp5OOuyxeytMAmkxk6hXqEScnchdrq/jZt4MQ=\"," +
//                "\"payload\":{\"outputs\":[{\"fromAddress\":\"DYODR4RQA2ZF4E3AI4URMRZOBI73STHJ\",\"amount\":999541}," +
//                "{\"fromAddress\":\"SQKG6SV4VRH5LVYHOY2LH3YMY3R4BSTW\",\"amount\":1000000000}]," +
//                "\"inputs\":[{\"unit\":\"3OlcWP7P3OijcfBHhPfJulNzPZu1yoqcKaeyobBdttQ=\",\"message_index\":0,\"output_index\":0}]}}]," +
//                "\"authors\":[{\"fromAddress\":\"DYODR4RQA2ZF4E3AI4URMRZOBI73STHJ\"," +
//                "\"authentifiers\":{\"r\":\"" + sinStr +"\"}," +
//                "\"definition\":[\"sig\",{\"pubkey\":\""+ DSA.encryptBASE64(mn.getPubKey()) +"\"}]}]," +
//                "\"headers_commission\":262,\"payload_commission\":197," +
//                "\"walletId\":\"Cjlvn9JXuxKdvAGrXvVgumIBNBBHhlYWeWzfFRiY2r5=\",\"unit\":\"tttnfxmIyDSemgO3TEerF24IZpb6HMS83LbCWBSq1234\",\"timestamp\":1530077212}}";
//
////        sinStr = "a7fmPScDzEiskQjgu/axLZuQgRuP8HM4VAF3FO3PatlSdH7rKYlJdhIueF1TYIKO9RVlKZc8BiAeL5UDYV/xdA==";
//
//
//
//        //��ǩ��
//        boolean flag = SignUtil.verify(unit);
//        System.out.println(flag);
//
	}

}
