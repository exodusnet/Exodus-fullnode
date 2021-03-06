package org.exodus.cfg.localfullnode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参数配置类
 *
 */
public class Config {
	public static final String VERSION_VALUE = "3.0.0"; //

	public static final String KEYS_FILE = "local.keys.json";
	public static final String WALLET_FILE = "local.wallet.json";
	public static final String GOSSIP_SIGNATURE_DATA = "data4TheInterface2CreatedSignature";

	public static String DEFAULT_SEED_PUBIP = "22.22.22.22";
//    public static final String DEFAULT_SEED_PUBIP = "35.170.77.230";
//    public static final String DEFAULT_SEED_PUBIP = "172.17.2.125";
//	public static final String DEFAULT_SEED_PUBIP = "192.168.207.129";
//	public static final String DEFAULT_SEED_PUBIP = "172.17.2.117";
	public static String DEFAULT_SEED_GOSSIP_PORT = "60000";
	public static String DEFAULT_SEED_RPC_PORT = "60001";
	public static String DEFAULT_SEED_HTTP_PORT = "60002";
	// 网络节点gossip间隔(ms)
	public static final int DEFAULT_GOSSIP_NODE_INTERVAL = 1000 * 60 * 1;
	// gossip Event 线程类型
	public static final int GOSSIP_IN_SHARD = 1;
	public static final int GOSSIP_GLOBAL_SHARD = 2;

	// 缓存Event总数量
	public static final int DEFAULT_EVENTS_MAP_SIZE = 50000;
	// 默认每个event打包transaction最大数量
	public static final int DEFAULT_TRANSACTIONS_PER_EVENT = 500;
	// 同步event时间间隔(ms)
	public static final int DEFAULT_GOSSIP_EVENT_INTERVAL = 500;
	// 默认最大同步EVENT数量
	public static final int DEFAULT_SYNC_EVENT_COUNT = 50;
	// 默认最大同步EVENT总大小(byte)
	public static final int DEFAULT_SYNC_EVENT_SPACE = 512 * 1024;
	// 默认最大同步EVENT总大小(byte)
	public static final int DEFAULT_EVENT_MAX_PAYLOAD = 16 * 1024 * 1024;

	// 获取共识event时间间隔(ms)
	public static final int GET_CONSENSUS_TIMEOUT = 150;

	// transaction提交时间间隔(ms)
	public static final int TXS_COMMIT_TIMEOUT = 400;
	// 每次提交transactions最大数量
	public static final int MAX_TXS_COMMIT_COUNT = 6000;

	// transaction验证时间间隔(ms)
	public static final int TXS_VEFRIFY_TIMEOUT = 400;
	// 每次验证transactions最大数量
	public static final int MAX_TXS_VEFRIFY_COUNT = 6000;

	public static final int TXS_EXECUTION_BATCH_SIZE = 5000;

	public static final int DEFAULT_EVENT_STATISTICS_COUNT = 5000;

	// 重构时每次从数据库读取每个hashnet柱子的event数量
	public static final int READ_SIZE_FROM_DB_PER_HASHNETNODE = 3000;
	// 一个消息表能保存200000份
	public static Integer MESSAGES_SPIT_TOTAL = 500000;
	// 交易中间产生的奖励，快照保存500000份
	public static Integer SYSTEM_SPIT_TOTAL = 500000;
	// 消息表前缀
	public static String MESSAGES = "messages";
	// 系统信息表前缀
	public static String SYSTEMAUTOTX = "system_auto_tx";
	public static String SPLIT = "_";

	// 快照点选取：每多少个Event做一次快照
	public static final int EVENT_NUM_PER_SNAPSHOT = 300000;
	// 删除第几代祖先快照之前的Event
	public static final int DEFAULT_SNAPSHOT_CLEAR_GENERATION = 10;
	// 默认最大交易留言字节数
	public static final int DEFAULT_MESSAGE_REMARK_MAX_SIZE = 50;

	// 区块链浏览器获取运行时间的数据库key
	public static final String CREATION_TIME_KEY = "creationTimeKey";
	// 共识message总数的数据库key
	public static final String CONS_MSG_COUNT_KEY = "consMessageCount";
	// 系统自动生成交易总数的数据库key
	public static final String SYS_TX_COUNT_KEY = "sysTxCount";
	// 共识Event总数的数据库key
	public static final String CONS_EVT_COUNT_KEY = "totalConsEventCount";
	// Event总数的数据库key
	public static final String EVT_COUNT_KEY = "eventCount";
	// Event对应Message数量的数据库key
	public static final String EVT_TX_COUNT_KEY = "eventMessageCount";
	// TPS数据的数据库key
	public static final String MESSAGE_TPS_KEY = "messageTps";

	/**
	 * 奖励比例
	 */
	public static final double NODE_REWARD_RATIO = 0.50D;
//	/**
//	 * 上帝钱包
//	 */
//	public static final String GOD_PUBKEY = "Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6";
//	public static final String GOD_ADDRESS = "MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG";
//	/**
//	 * 创世钱包
//	 */
////    // 测试
//	public static final List<String> CREATION_ADDRESSES = Arrays.asList("IFQIIX4Q4HL2AB5VZ2ACP3PJVNRPBYUN",
//			"VL6OO34HEWM4L7OKAK253RBIZ47V73J2", "FJPJKQ6DK7IT7Z7QSTZM6L4BJBYEM3QF", "ASWD2MRMIEYR27PMUXGOOZYCBOXLOXPK");
	public static final String GOD_PUBKEY = "Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6";
	public static final String GOD_ADDRESS = "MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG";

	public static final List<String> CREATION_ADDRESSES = Arrays.asList("FODALDEJMW5YIMUNPBCYB7P5QQPXQZ73",
			"LR4D5HXC4FBNM2RLNJIDTR63QXHIVQRO", "JCKNOS7KFSBZVRFYLJB7OSC35TQOEMF6", "OZGPDQBUUWC4H4XCOVOYKSQY32EX3GGO");

	/**
	 * 基金会钱包
	 */

	// testing environment
//	public static final String FOUNDATION_MNEMONIC = "";
//	public static final String FOUNDATION_PUBKEY = "Alo+zd8kOdUOoGEQzARyx0ZRoTfsWRY5QC6+wm8O9Qfs";
//	public static final String FOUNDATION_ADDRESS = "7APYB7AMH7SOFZPHG6YGJICIHKXRLVXR";

//	public static final List<String> CREATION_TX_LIST = Arrays.asList(
//			"{\"nrgPrice\":\"1000000000\",\"amount\":\"2139981000000000000000000\",\"signature\":\"33AMEBosgSUDUAh4uwqcSqCysEP9yXvimBVeKzA1nWQeHCdHPpejFg+OksTPQW4DxGUvA/TSg1WRrdDo641+uEpY8=\",\"fee\":\"500000\",\"vers\":\"2.0\",\"fromAddress\":\"MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG\",\"remark\":\"\",\"type\":1,\"toAddress\":\"IFQIIX4Q4HL2AB5VZ2ACP3PJVNRPBYUN\",\"timestamp\":1553224878076,\"pubkey\":\"Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6\"}",
//			"{\"nrgPrice\":\"1000000000\",\"amount\":\"15185220000000000000000000\",\"signature\":\"33AOrldMUbAy9Ba5AfTkF5sUzc4/vnjWaisCiiGcIhtH4CUSBFFvmKuRsMX/nZomwta+nPlhbHGhTcIZoBFDF2RYY=\",\"fee\":\"500000\",\"vers\":\"2.0\",\"fromAddress\":\"MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG\",\"remark\":\"\",\"type\":1,\"toAddress\":\"VL6OO34HEWM4L7OKAK253RBIZ47V73J2\",\"timestamp\":1553224878304,\"pubkey\":\"Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6\"}",
//			"{\"nrgPrice\":\"1000000000\",\"amount\":\"4178372000000000000000000\",\"signature\":\"33AJDtCIW1abODA0i0LSWe+n6bsrCFook5XQ7MW/Eg1gzLesidZlnBeo4Ww7zkFX8XRmCIcEpQYwF045I0vycwXyU=\",\"fee\":\"500000\",\"vers\":\"2.0\",\"fromAddress\":\"MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG\",\"remark\":\"\",\"type\":1,\"toAddress\":\"FJPJKQ6DK7IT7Z7QSTZM6L4BJBYEM3QF\",\"timestamp\":1553224878408,\"pubkey\":\"Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6\"}",
//			"{\"nrgPrice\":\"1000000000\",\"amount\":\"78496427000000000000000000\",\"signature\":\"32Bnc6+GDk0UPlhXUfV73dLvLm34HODy++7WAdd7Mt8QF7HoPguZjbO29ntRgTFuDVwKpyZaQxXZFG28Q86L8Dxg==\",\"fee\":\"500000\",\"vers\":\"2.0\",\"fromAddress\":\"MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG\",\"remark\":\"\",\"type\":1,\"toAddress\":\"ASWD2MRMIEYR27PMUXGOOZYCBOXLOXPK\",\"timestamp\":1553224878489,\"pubkey\":\"Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6\"}"
//	);

	// product environment
	public static final String FOUNDATION_MNEMONIC = "";
	public static final String FOUNDATION_PUBKEY = "Amf/TqeRhXFcLmQ6G5O5rJgiQ2zOWeyQInI0EW/bNzUP";
	public static final String FOUNDATION_ADDRESS = "KHCROSWUAN6RZLBNLZ3KFA2U36HVBBBC";

	public static final List<String> CREATION_TX_LIST = Arrays.asList(
			"{\"nrgPrice\":\"1000000000\",\"amount\":\"2139981000000000000000000\",\"signature\":\"33AMEBosgSUDUAh4uwqcSqCysEP9yXvimBVeKzA1nWQeHCdHPpejFg+OksTPQW4DxGUvA/TSg1WRrdDo641+uEpY8=\",\"fee\":\"500000\",\"vers\":\"2.0\",\"fromAddress\":\"MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG\",\"remark\":\"\",\"type\":1,\"toAddress\":\"FODALDEJMW5YIMUNPBCYB7P5QQPXQZ73\",\"timestamp\":1553224878076,\"pubkey\":\"Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6\"}",
			"{\"nrgPrice\":\"1000000000\",\"amount\":\"15185220000000000000000000\",\"signature\":\"33AOrldMUbAy9Ba5AfTkF5sUzc4/vnjWaisCiiGcIhtH4CUSBFFvmKuRsMX/nZomwta+nPlhbHGhTcIZoBFDF2RYY=\",\"fee\":\"500000\",\"vers\":\"2.0\",\"fromAddress\":\"MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG\",\"remark\":\"\",\"type\":1,\"toAddress\":\"LR4D5HXC4FBNM2RLNJIDTR63QXHIVQRO\",\"timestamp\":1553224878304,\"pubkey\":\"Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6\"}",
			"{\"nrgPrice\":\"1000000000\",\"amount\":\"4178372000000000000000000\",\"signature\":\"33AJDtCIW1abODA0i0LSWe+n6bsrCFook5XQ7MW/Eg1gzLesidZlnBeo4Ww7zkFX8XRmCIcEpQYwF045I0vycwXyU=\",\"fee\":\"500000\",\"vers\":\"2.0\",\"fromAddress\":\"MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG\",\"remark\":\"\",\"type\":1,\"toAddress\":\"JCKNOS7KFSBZVRFYLJB7OSC35TQOEMF6\",\"timestamp\":1553224878408,\"pubkey\":\"Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6\"}",
			"{\"nrgPrice\":\"1000000000\",\"amount\":\"78496427000000000000000000\",\"signature\":\"32Bnc6+GDk0UPlhXUfV73dLvLm34HODy++7WAdd7Mt8QF7HoPguZjbO29ntRgTFuDVwKpyZaQxXZFG28Q86L8Dxg==\",\"fee\":\"500000\",\"vers\":\"2.0\",\"fromAddress\":\"MIYC75ZL5QXFYAQIQ2UMY34CFWUWJWWG\",\"remark\":\"\",\"type\":1,\"toAddress\":\"OZGPDQBUUWC4H4XCOVOYKSQY32EX3GGO\",\"timestamp\":1553224878489,\"pubkey\":\"Avg327cX3EtpqOZTqU6RWChQBcjJ3WMDsOWCIB0EBgN6\"}");
	public static final List<String> WHITE_LIST = Arrays.asList("192.168.207.129", "172.17.2.118", "172.17.2.119",
			"172.17.2.117", "172.17.2.120", "172.17.2.123", "172.17.2.126", "172.17.2.127", "172.17.2.46",
			"172.17.2.128", "172.17.2.117", "172.17.2.120", "172.17.2.123", "172.17.2.126", "172.17.2.127",
			"172.17.2.128", "192.168.207.130");

	public static final List<String> BLACK_LIST = Arrays.asList("192.168.0.1");

	// rpc服务
	@Deprecated
	public static final String[] SERVICE_ARRAY = { "org.exodus.localfullnode2.rpc.impl.Light2localImpl",
			"org.exodus.localfullnode2.rpc.impl.Local2localImpl",
			"org.exodus.localfullnode2.sync.rpc.DataSynchronizationZerocImpl" };

	// RPC services mapping list
	public static Map<String, String> RPC_SERVICES_MAPPING_LIST = new HashMap<String, String>() {
		{
			put("Light2local", "org.exodus.localfullnode2.rpc.impl.Light2localImpl");
			put("Local2local", "org.exodus.localfullnode2.rpc.impl.Local2localImpl");
			put("DataSynchronizationZeroc", "org.exodus.localfullnode2.sync.rpc.DataSynchronizationZerocImpl");
			put("PhantomRPCResponder", "org.exodus.localfullnode2.rpc.impl.PhantomRPCResponder");// buit-in fake rpc
																									// responsder
		}
	};

	/**
	 * coin round发生间隔
	 */
	public static int coinFreq = 12;
	/**
	 * 使用RSA
	 */
	public static boolean useRSA = true;
	/**
	 *
	 */
	public static boolean logStack = true;

	// enable or disable snapshot
	public static boolean ENABLE_SNAPSHOT = false;
	// power function's exponent in lost motion model
	public static double LostMotionModel_EXPONENT = 0.5;

	// the port which chronicle server is listening to;zero leads to zero-work.
	public static int chroniclePort = 0;
}
