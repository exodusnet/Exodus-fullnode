package org.exodus.node;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import org.exodus.bean.node.FullNode;
import org.exodus.bean.node.LocalFullNode;
import org.exodus.bean.node.NodeStatus;
import org.exodus.bean.node.NodeTypes;
import org.exodus.bean.node.RelayNode;
import org.exodus.bean.wallet.Keys;
import org.exodus.bean.wallet.Wallet;
import org.exodus.bean.wallet.WalletBuilder;
import org.exodus.cfg.core.IConfImplant;
import org.exodus.cfg.core.InterValueConfImplant;
import org.exodus.core.Block;
import org.exodus.http.NettyHttpServer;
import org.exodus.threads.ExchangeRateThread;
import org.exodus.threads.GossipNodeThread;
import org.exodus.threads.PbftThread;
import org.exodus.util.DbUtils;
import org.exodus.util.FileLockUtils;
import org.exodus.util.HnKeyUtils;
import org.exodus.util.PathUtils;
import org.exodus.util.StringUtils;

/**
 * 种子全节点 Created by Clare on 2018/6/9 0009.
 */
public class Main extends GeneralNode {
	private static final Logger logger = LoggerFactory.getLogger("Main.class");

	/**
	 * 启动gossip网络
	 */
	private void startGossipNetwork() {
		GossipNodeThread thread = new GossipNodeThread(this);
		thread.start();
	}

	/**
	 * 启动rpc服务
	 * 
	 * @param adapter
	 */
	private void runRpcService(ObjectAdapter adapter) {
		logger.info("start rpc service...");
		this.loadRPC(this, adapter);
	}

	/**
	 * 单进程节点判断
	 * 
	 * @return 是否已存在一个运行中进程
	 */
	private boolean isRunning() {
		logger.info(">>>>>> check if running a process.");
		FileLockUtils fileLockUtils = new FileLockUtils(PathUtils.getDataFileDir() + parameters.keysFile);
		try {
			return !fileLockUtils.Lock();
		} catch (IOException e) {
			logger.error("file lock error: {}", e);
			return false;
		}
	}

	/**
	 * 初始化钱包
	 * 
	 * @throws Exception 异常
	 */
	private void initWallet() throws Exception {
		logger.info(">>>>>> init wallet...");
		File dir = new File(PathUtils.getDataFileDir());
		if (!dir.isDirectory()) {
			if (!dir.mkdirs()) {
				logger.error("create cache dir failed!!! exit...");
				System.exit(-1);
			}
		}
		String fileName = PathUtils.getDataFileDir() + parameters.walletFile;
		logger.info(">>>>>> path: {}", fileName);
		File file = new File(fileName);
		if (!file.exists()) {
			newWallet(fileName);
		} else {
			reloadWallet(fileName);
			if (null == wallet) {
				newWallet(fileName);
			}
		}
	}

	/**
	 * 创建钱包
	 * 
	 * @param fileName 钱包基本信息保存文件
	 */
	private void newWallet(String fileName) throws Exception {
		wallet = WalletBuilder.generateWallet();
		// 写入文件
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(new File(fileName));
			outputStream.write(JSONObject.toJSONString(wallet).getBytes());
			outputStream.close();
		} catch (Exception e) {
			logger.error("{}", e);
			System.exit(-1);
		} finally {
			if (null != outputStream) {
				outputStream.close();
			}
		}
	}

	/**
	 * 重载钱包
	 * 
	 * @param fileName 钱包基本信息保存文件
	 */
	private void reloadWallet(String fileName) {
		FileInputStream inputStream = null;
		byte[] data = null;
		try {
			inputStream = new FileInputStream(new File(fileName));
			data = new byte[inputStream.available()];
			inputStream.read(data);
		} catch (Exception e) {
			logger.error("{}", e);
			System.exit(-1);
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (null == data) {
			logger.warn("reload wallet is empty.");
			return;
		}
		JSONObject walletObj = JSONObject.parseObject(new String(data));
		Keys extKeys = JSONObject.parseObject(walletObj.getString("extKeys"), Keys.class);
		Keys keys = JSONObject.parseObject(walletObj.getString("keys"), Keys.class);
		wallet = new Wallet.Builder().mnemonic(walletObj.getString("mnemonic")).extKeys(extKeys).keys(keys)
				.address(walletObj.getString("address")).build();
	}

	/**
	 * 重构currBlock
	 */
	private void restructCurrBlock() {
		Block block = SqliteDAO.queryLastBlockFromDatabase(parameters.dbFile);
		if (null != block) {
			this.setCurrBlock(block);
		}
	}

	/**
	 * 初始化数据库
	 */
	private void initDatabase() {
		File dbfile = new File(PathUtils.getDataFileDir() + this.parameters.dbFile);
		if (!dbfile.exists()) {
			// 创建数据
			DbUtils.createDatabase(PathUtils.getDataFileDir(), this.parameters.dbFile);
			// 把自己加入fullNodeList
			String pubkey = HnKeyUtils.getString4PublicKey(this.publicKey);
			FullNode fullNode = new FullNode.Builder().pubkey(pubkey).ip(this.parameters.seedGossipAddress.pubIP)
					.rpcPort(this.parameters.seedGossipAddress.rpcPort)
					.httpPort(this.parameters.seedGossipAddress.httpPort).type(NodeTypes.SEED)
					.status(NodeStatus.HAS_CONSENSUSED).build();
			this.getFullNodeList().put(pubkey, fullNode);
			// 把自己入库
			SqliteDAO.addFullNode(parameters.dbFile, fullNode);
		} else {
			// 重构当前分片区块
			this.restructCurrBlock();
			String pubkey = HnKeyUtils.getString4PublicKey(this.publicKey);
			// 重构localfullNodeList
			List<LocalFullNode> localFullNodes = SqliteDAO.queryLocalFullNodes(this.parameters.dbFile);
			localFullNodes.forEach(n -> this.getLocalFullNodeList().put(n.getPubkey(), n));
			// 重构fullNodeList
			List<FullNode> fullNodes = SqliteDAO.queryFullNodes(this.parameters.dbFile, pubkey);
			fullNodes.forEach(n -> this.getFullNodeList().put(n.getPubkey(), n));
			// 重构relayNodeList
			List<RelayNode> relayNodes = SqliteDAO.queryRelayNodes(this.parameters.dbFile);
			relayNodes.forEach(n -> this.getRelayNodeList().put(n.getPubkey(), n));
		}
	}

	protected IConfImplant loadConfObject(String[] args) {
		InterValueConfImplant implant = new InterValueConfImplant();
		implant.init(args);

		return implant;
	}

	protected void loadConf(String[] args) {
		IConfImplant implant = loadConfObject(args);
		implant.implantStaticConfig(true);
		implant.implantEnv();
		communicator = Util.initialize(implant.implantZerocConf());
		parameters = implant.implantParameters(true);

		// return implant.getDbConnection();
	}

	/**
	 * 初始化
	 * 
	 * @param args 命令行参数
	 */
	private void init(String args[]) {
		try {
//			communicator = Util.initialize(args);
//			if (null == this.parameters) {
//				this.parameters = new Parameters();
//			}
//			this.parameters.init(getCommunicator(), args);
			loadConf(args);

			// 初始化钱包
			initWallet();

			openServiceState = 1;
			// 初始化key
			initHnKey();
			if (isRunning()) {
				logger.error("There is already a running process!");
				System.exit(0);
			}

			// 初始化数据库
			initDatabase();

			// 启动Gossip网络
			startGossipNetwork();

			// 启动rpc接口服务: 全节点注册退出
			ObjectAdapter adapter = generateAdapter(getCommunicator(), "FullNodeAdapter",
					parameters.seedGossipAddress.rpcPort);
			runRpcService(adapter);

			// 开放局部全节点注册接口
			openServiceState = 1;
			while (!checkAddressesOfFullNodeList() || StringUtils.isEmpty(this.getSeedPubkey())) {
				Thread.sleep(parameters.gossipInterval);
			}
			new PbftThread(this).start();
			new ExchangeRateThread(this).start();

			// 开放查询局部全节点信息接口
			openServiceState = 2;

			// 启动http接口
			NettyHttpServer.boostrapHttpService(this);

			communicator.waitForShutdown();
		} catch (Exception e) {
			logger.error("main init error.", e);
		}
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.init(args);
	}
}
