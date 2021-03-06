package org.exodus.node;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Object;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import org.exodus.bean.node.BaseNode;
import org.exodus.bean.node.ChainTypes;
import org.exodus.bean.node.FullNode;
import org.exodus.bean.node.LocalFullNode;
import org.exodus.bean.node.RelayNode;
import org.exodus.bean.wallet.KeyPair;
import org.exodus.bean.wallet.Wallet;
import org.exodus.cfg.fullnode.Config;
import org.exodus.cfg.fullnode.Parameters;
import org.exodus.core.Block;
import org.exodus.core.Crypto;
import org.exodus.core.MessageQueue;
import org.exodus.rpc.fullnode.RegisterPrx;
import org.exodus.util.HnKeyUtils;
import org.exodus.util.PathUtils;

/**
 * 一般（公共）节点 Created by Max on 2018/6/3.
 */
public class GeneralNode {
	private static final Logger logger = LoggerFactory.getLogger("GeneralNode.class");
	/**
	 * 节点参数
	 */
	public Parameters parameters = new Parameters();

	// 接口开放状态
	public int openServiceState = 0;

	private ConcurrentHashMap<String, RelayNode> relayNodeList = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, FullNode> fullNodeList = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, LocalFullNode> localFullNodeList = new ConcurrentHashMap<>();
	private String seedPubkey = "";

	public byte[] publicKey = null;
	public byte[] privateKey = null;
	public Wallet wallet;

	private Block currBlock = null;
	private Block pendingBlock = null;
	private long currentSequenceNo;

	private double[] chainTokenPrices = new double[ChainTypes.NAMES.size()];
	ConcurrentHashMap<String, String> ratios = new ConcurrentHashMap<>();

	/**
	 * pbft消息队列
	 */
	public MessageQueue mq = new MessageQueue();

	public Communicator communicator;

	public GeneralNode() {
		this.wallet = getWallet();
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public String getSeedPubkey() {
		return seedPubkey;
	}

	public void setSeedPubkey(String seedPubkey) {
		this.seedPubkey = seedPubkey;
	}

	public Block getCurrBlock() {
		return currBlock;
	}

	public void setCurrBlock(Block currBlock) {
		this.currBlock = currBlock;
	}

	public Block getPendingBlock() {
		return pendingBlock;
	}

	public void setPendingBlock(Block pendingBlock) {
		this.pendingBlock = pendingBlock;
	}

	public long getCurrentSequenceNo() {
		return currentSequenceNo;
	}

	public void setCurrentSequenceNo(long currentSequenceNo) {
		this.currentSequenceNo = currentSequenceNo;
	}

	public double[] getChainTokenPrices() {
		return chainTokenPrices;
	}

	public void setChainTokenPrices(double[] chainTokenPrices) {
		this.chainTokenPrices = chainTokenPrices;
	}

	public ConcurrentHashMap<String, String> getRatios() {
		return ratios;
	}

	public void setRatios(ConcurrentHashMap<String, String> ratios) {
		this.ratios = ratios;
	}

	public ConcurrentHashMap<String, RelayNode> getRelayNodeList() {
		return relayNodeList;
	}

	public void setRelayNodeList(ConcurrentHashMap<String, RelayNode> relayNodeList) {
		this.relayNodeList = relayNodeList;
	}

	public ConcurrentHashMap<String, FullNode> getFullNodeList() {
		return fullNodeList;
	}

	public void setFullNodeList(ConcurrentHashMap<String, FullNode> fullNodeList) {
		this.fullNodeList = fullNodeList;
	}

	public ConcurrentHashMap<String, LocalFullNode> getLocalFullNodeList() {
		return localFullNodeList;
	}

	public void setLocalFullNodeList(ConcurrentHashMap<String, LocalFullNode> localFullNodeList) {
		this.localFullNodeList = localFullNodeList;
	}

	public Communicator getCommunicator() {
		return communicator;
	}

	/**
	 * load rpc from default.config
	 * 
	 * @param adapter adapter object
	 */
	public void loadRPC(GeneralNode node, ObjectAdapter adapter) {
		try {
			// add rpc
			for (String clsName : Config.RPC_SERVICE_NAMES) {
				Class<?> T = Class.forName(clsName);
				Constructor<Object> cons = (Constructor<Object>) T.getConstructor(GeneralNode.class);
				Object object = cons.newInstance(node);

				String identity = clsName.substring(clsName.lastIndexOf('.') + 1);
				if (identity.toLowerCase().endsWith("impl")) {
					identity = identity.substring(0, identity.length() - 7);
				}
				adapter.add(object, Util.stringToIdentity(identity));
			}
			adapter.activate();
		} catch (Exception e) {
			logger.error("loadRPC error: {}", e);
			System.exit(0);
		}
	}

	/**
	 * generate adapter with a specified port
	 * 
	 * @param communicator
	 * @param adapterName
	 * @param rpcPort
	 * @return
	 */
	public ObjectAdapter generateAdapter(Communicator communicator, String adapterName, int rpcPort) {
		ObjectAdapter adapter = null;
		try {
			adapter = communicator.createObjectAdapterWithEndpoints(adapterName, "default -p " + rpcPort);
		} catch (Exception e) {
			logger.error("bind... [ERROR]port {} bind failure! msg: {}", rpcPort, e);
			System.exit(0);
		}

		return adapter;
	}

	public RegisterPrx buildRegisterConnection2FullNode(String ip, String port) {
		logger.info("buildRegisterConnection2FullNode...");
		RegisterPrx prx = null;
		try {
			prx = RegisterPrx
					.checkedCast(this.getCommunicator().stringToProxy("Register:default -h " + ip + " -p " + port));
		} catch (Exception e) {
			logger.error("{}", e);
		}
		if (null == prx) {
			logger.error(">>>>>> Can not build connnection to full node {}:{}", ip, port);
			throw new Error("Can not build connnection to full node.");
		}
		return prx;
	}

	public RegisterPrx buildRegisterConnection2FullNode(BaseNode fullNode) {
		logger.info("buildRegisterConnection2FullNode...");
		RegisterPrx prx = null;
		String stirngToProxy = null;
		try {
			stirngToProxy = "Register:default -h " + fullNode.getIp() + " -p " + fullNode.getRpcPort();
			prx = RegisterPrx.checkedCast(this.getCommunicator().stringToProxy(stirngToProxy));
		} catch (Exception e) {
			logger.error("{}", e);
		}
		if (null == prx) {
			logger.error(">>>>>> Can not build connnection to full node {}:{}", fullNode.getIp(),
					fullNode.getRpcPort());
			throw new Error("Can not build connnection to full node.");
		}
		return prx;
	}

	public boolean checkAddressesOfFullNodeList() {
		long size = fullNodeList.values().stream().filter(BaseNode::checkIfConsensusNode).count();
		return (size >= parameters.pbftNodeMinSize);
	}

	/**
	 * 初始化hnKey 存在则读取，否则创建并保存
	 */
	public void initHnKey() {
		logger.info("init hashnet key...");
		String fileName = PathUtils.getDataFileDir() + parameters.keysFile;
		File dbfile = new File(fileName);
		if (!dbfile.exists()) {
			// 生成key
			Crypto crypto = new Crypto();
			KeyPair keyPair = crypto.getKeyPair();
			publicKey = keyPair.getPublicKey();
			privateKey = keyPair.getPrivateKey();

			// 写入文件
			File file = new File(PathUtils.getDataFileDir());
			if (!file.isDirectory()) {
				if (!file.mkdirs()) {
					logger.error("create cache dir failed!!! exit...");
					System.exit(-1);
				}
			}
			HnKeyUtils.writeKey2File(keyPair, fileName);
		} else {
			// 读取文件
			KeyPair keyPair = HnKeyUtils.readKeyFromFile(fileName);
			publicKey = keyPair.getPublicKey();
			privateKey = keyPair.getPrivateKey();
		}
	}
}
