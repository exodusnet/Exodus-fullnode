package org.exodus.localfullnode2.membership;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.exodus.bean.node.NodeStatus;
import org.exodus.bean.node.NodeTypes;
import org.exodus.cfg.localfullnode.Config;
import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;
import org.exodus.localfullnode2.nodes.LocalFullNode1GeneralNode;
import org.exodus.localfullnode2.store.rocks.RocksJavaUtil;
import org.exodus.localfullnode2.utilities.StringUtils;

/**
 * 局部全节点启动gossip网络线程
 * <p>
 * allow {@code isInterrupted} to stop loop.
 * 
 * @author Clare
 * @date 2018/6/13 0013.
 */
public class GossipNodeThread extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(GossipNodeThread.class);
	private LocalFullNode1GeneralNode node;
	private String pubkey;
	private BigInteger evtCounts;
	private BigInteger consEvtCounts;
	private BigInteger messageCounts;

	private volatile boolean interrupted = false;

	StringBuilder statisticInfo = new StringBuilder()
			.append("\n*************** node-({},{}): {}-th statistics ***************")
			.append("\n*****total  evts: {}\n*****total cons evts: {}\n*****total cons msgs: {}\n*****interval : {} sec")
			.append("\n*****cons evts: {}\n*****eps: {}\n*****cons msgs: {}\n*****tps: {}");

	public GossipNodeThread(LocalFullNode1GeneralNode node, String pubkey) {
		this.node = node;
		this.pubkey = pubkey;

		this.evtCounts = node.getTotalEventCount();
		this.consEvtCounts = node.getTotalConsEventCount();
		this.messageCounts = node.getConsMessageCount();
	}

	protected ClusterConfig.Builder defaultWanConfig() {
		return ClusterConfig.builder().suspicionMult(ClusterConfig.DEFAULT_WAN_SUSPICION_MULT)
				.syncInterval(ClusterConfig.DEFAULT_WAN_SYNC_INTERVAL)
				.pingTimeout(ClusterConfig.DEFAULT_WAN_PING_TIMEOUT)
				.pingInterval(ClusterConfig.DEFAULT_WAN_PING_INTERVAL)
				.gossipFanout(ClusterConfig.DEFAULT_WAN_GOSSIP_FANOUT)
				.connectTimeout(ClusterConfig.DEFAULT_WAN_CONNECT_TIMEOUT);
	}

	@Override
	public void run() {
		logger.info(">>>>>> start membership network...");

		// logger.info("gossip,rpc =
		// {},{}",node.nodeParameters.selfGossipAddress.gossipPort,node.nodeParameters.selfGossipAddress.rpcPort);

		ClusterConfig nodeConfig;
		nodeConfig = /* ClusterConfig.builder() */defaultWanConfig()
				.seedMembers(org.exodus.transport.Address.create(node.nodeParameters().seedGossipAddress.getPubIP(),
						node.nodeParameters().seedGossipAddress.getGossipPort()))
				.memberHost(node.nodeParameters().selfGossipAddress.getPubIP())
				.port(node.nodeParameters().selfGossipAddress.getGossipPort()).portAutoIncrement(false)
				.addMetadata("level", "" + NodeTypes.LOCALFULLNODE)
				.addMetadata("shard", node.getShardId() < 0 ? "" : "" + this.node.getShardId())
				.addMetadata("index", node.getCreatorId() < 0 ? "" : "" + this.node.getCreatorId())
				.addMetadata("rpcPort", "" + node.nodeParameters().selfGossipAddress.getRpcPort())
				.addMetadata("httpPort", "" + node.nodeParameters().selfGossipAddress.getHttpPort())
				.addMetadata("pubkey", this.pubkey).addMetadata("address", this.node.getWallet().getAddress()).build();
		Cluster cluster = Cluster.joinAwait(nodeConfig);
		logger.info("local full node has started!");

		boolean shardFlag = (this.node.getShardId() < 0 && this.node.getCreatorId() < 0);
		long index = 0;
		// while (true) {
		while (!this.interrupted) {
			index++;
			Instant first = Instant.now();
			int shardCount = this.node.getShardCount();
			List<String> blackPubkeys = new ArrayList<>();
			if (null != node.getBlackList4PubKey() && node.getBlackList4PubKey().size() > 0) {
				blackPubkeys.addAll(node.getBlackList4PubKey());
			}
			if (shardCount > 0) {
				if (!shardFlag) {
					cluster.updateMetadataProperty("shard", "" + this.node.getShardId());
					cluster.updateMetadataProperty("index", "" + this.node.getCreatorId());
					shardFlag = true;
				}
				logger.warn("node size(in localfullnode point of view): {}", cluster.members().size());
				logger.warn("shard node size(in localfullnode point of view): {}",
						cluster.findMembersByShardId("" + node.getShardId()).size() + 1);
				// 片内邻居池（不含自己）
//				node.inshardNeighborPools = cluster.findMembersByShardId("" + node.getShardId()).stream()
//						.filter(member -> "2".equals("" + member.metadata().get("level"))
//								&& Config.WHITE_LIST.contains(member.address().host())
//								&& !Config.BLACK_LIST.contains(member.address().host())
//								&& validatePubkey("" + member.metadata().get("pubkey"))
//								&& !blackPubkeys.contains(member.metadata().get("pubkey"))
//								&& StringUtils.isNotEmpty("" + member.metadata().get("index")))
//						.collect(
//								Collectors.collectingAndThen(
//										Collectors.toCollection(() -> new TreeSet<>(
//												Comparator.comparing(o -> new StringBuilder().append(o.address().host())
//														.append("_").append(o.address().port()).append("_")
//														.append(o.metadata().get("rpcPort")).append("_")
//														.append(o.metadata().get("httpPort")).toString()))),
//										ArrayList::new));
				node.inshardNeighborPools(cluster.findMembersByShardId("" + node.getShardId()).stream()
						.filter(member -> "2".equals("" + member.metadata().get("level"))
								&& Config.WHITE_LIST.contains(member.address().host())
								&& !Config.BLACK_LIST.contains(member.address().host())
								&& validatePubkey("" + member.metadata().get("pubkey"))
								&& !blackPubkeys.contains(member.metadata().get("pubkey"))
								&& StringUtils.isNotEmpty("" + member.metadata().get("index")))
						.collect(
								Collectors.collectingAndThen(
										Collectors.toCollection(() -> new TreeSet<>(
												Comparator.comparing(o -> new StringBuilder().append(o.address().host())
														.append("_").append(o.address().port()).append("_")
														.append(o.metadata().get("rpcPort")).append("_")
														.append(o.metadata().get("httpPort")).toString()))),
										ArrayList::new)));
				// 全局邻居池（不含自己）
//                node.globalNeighborPools = cluster.otherMembers().stream()
//                        .filter(member -> "2".equals(""+member.metadata().get("level"))
//                                && Config.WHITE_LIST.contains(member.address().host())
//                                && !Config.BLACK_LIST.contains(member.address().host())
//                                && validatePubkey(""+member.metadata().get("pubkey"))
//                                && !blackPubkeys.contains(member.metadata().get("pubkey"))
//                                && StringUtils.isNotEmpty(""+member.metadata().get("shard"))
//                                && StringUtils.isNotEmpty(""+member.metadata().get("index")) )
//                        .collect( Collectors.collectingAndThen(
//                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o ->
//                                            new StringBuilder()
//                                                    .append(o.address().host())
//                                                    .append("_").append(o.metadata().get("rpcPort"))
//                                                    .append("_").append(o.metadata().get("httpPort")).toString()))),
//                                ArrayList::new));
				node.globalNeighborPools(cluster.otherMembers().stream()
						.filter(member -> "2".equals("" + member.metadata().get("level"))
								&& Config.WHITE_LIST.contains(member.address().host())
								&& !Config.BLACK_LIST.contains(member.address().host())
								&& validatePubkey("" + member.metadata().get("pubkey"))
								&& !blackPubkeys.contains(member.metadata().get("pubkey"))
								&& StringUtils.isNotEmpty("" + member.metadata().get("shard"))
								&& StringUtils.isNotEmpty("" + member.metadata().get("index")))
						.collect(
								Collectors.collectingAndThen(
										Collectors.toCollection(() -> new TreeSet<>(
												Comparator.comparing(o -> new StringBuilder().append(o.address().host())
														.append("_").append(o.metadata().get("rpcPort")).append("_")
														.append(o.metadata().get("httpPort")).toString()))),
										ArrayList::new)));
				if (logger.isDebugEnabled()) {
					logger.debug("============== {}, localFullNode size: {}",
							node.nodeParameters().selfGossipAddress.gossipPort,
							(node.globalNeighborPools().size() + 1));
					node.globalNeighborPools().forEach(member -> {
						logger.info(">>> {}: {}:{}", node.nodeParameters().selfGossipAddress.gossipPort,
								member.address().host(), member.address().port());
					});
				}
			} else {
				Collection<Member> members = cluster.otherMembers();
				if (logger.isDebugEnabled()) {
					logger.debug("============== {}, localFullNode size: {}",
							node.nodeParameters().selfGossipAddress.gossipPort, (members.size() + 1));
					members.stream()
							.filter(member -> "2".equals("" + member.metadata().get("level"))
									&& Config.WHITE_LIST.contains(member.address().host())
									&& !Config.BLACK_LIST.contains(member.address().host())
									&& validatePubkey("" + member.metadata().get("pubkey"))
									&& !blackPubkeys.contains(member.metadata().get("pubkey")))
							.forEach(member -> {
								logger.info(">>> 0 {}:{}", member.address().host(), member.address().port());
							});
				}
			}

			// 清理过期的消息hash缓存
			clearExpiredMessageHash(node, Instant.now().toEpochMilli());
			// 等待轮循
			waitInterval(first);
			// 统计tps
			statisticsAndShowTpsInfo(index);

			// Force membership to take a long break
//			try {
//				Thread.sleep(2 * 60 * 1000);
//				// this.getClass().wait(2 * 1000 * 60);
//			} catch (InterruptedException e) {
//				// e.printStackTrace();
//				break;
//			}
		}

		CompletableFuture<Void> shutdown = null;
		try {
			shutdown = cluster.shutdown();
			shutdown.get();
		} catch (Exception e) {
			// e.printStackTrace();

			// ignore "Exception in thread "RxScheduledExecutorPool-2"
			// java.lang.IllegalStateException: Fatal Exception thrown on Scheduler.Worker
			// thread"

		} finally {
			while (!cluster.isShutdown()) {
				logger.info("shutdowning membership");
			}

		}

		logger.warn("interrupted={}", this.interrupted);
		logger.warn("<<localfullnode's membership>> is stopped......");
		return;

	}

	/**
	 * 清理过期的消息hash缓存
	 */
	private void clearExpiredMessageHash(LocalFullNode1GeneralNode node, long nowTime) {
		long count = 0;
		for (Map.Entry<String, Long> entry : node.getMessageHashCache().entrySet()) {
			long value = entry.getValue();
			if (nowTime - value > 5 * 1000) {
				node.getMessageHashCache().remove(entry.getKey());
				count++;
			}
		}
		if (count > 0) {
			logger.info("node-({}, {}): MessageHashCache remove {} expired key.", node.getShardId(),
					node.getCreatorId(), count);
		}
	}

	/**
	 * 统计和显示tps信息
	 * 
	 * @param index 第index轮循环
	 */
	private void statisticsAndShowTpsInfo(long index) {
		int freq = 15;
		if (index % freq == 0) {
			BigInteger totalEventCount = node.getTotalEventCount();
			BigInteger totalConsEventCount = node.getTotalConsEventCount();
			BigInteger totalMsgCount = node.getConsMessageCount();

			BigInteger newConsEventCount = totalConsEventCount.subtract(consEvtCounts);
			BigInteger newMgsCount = totalMsgCount.subtract(messageCounts);

			evtCounts = totalEventCount;
			consEvtCounts = totalConsEventCount;
			messageCounts = totalMsgCount;

			BigDecimal interval = new BigDecimal(Config.DEFAULT_GOSSIP_NODE_INTERVAL * freq / 1000.0);
			BigDecimal eps = new BigDecimal(newConsEventCount).divide(interval, 2, BigDecimal.ROUND_HALF_UP);
			BigDecimal tps = new BigDecimal(newMgsCount).divide(interval, 2, BigDecimal.ROUND_HALF_UP);

			setMaxTps(tps);

			logger.info(statisticInfo.toString(), node.getShardId(), node.getCreatorId(), index / freq, totalEventCount,
					totalConsEventCount, totalMsgCount, Config.DEFAULT_GOSSIP_NODE_INTERVAL * freq / 1000.0,
					newConsEventCount, eps, newMgsCount, tps);
		}
	}

	/**
	 * 保存最大tps
	 * 
	 * @param tps
	 */
	public void setMaxTps(BigDecimal tps) {
		try {
			if (tps != null) {
				RocksJavaUtil rocksJavaUtil = new RocksJavaUtil(node.nodeParameters().dbId);
				byte[] tpsByte = rocksJavaUtil.get(Config.MESSAGE_TPS_KEY);
				if (tpsByte != null && tpsByte.length > 0) {
					BigDecimal rocksTPS = new BigDecimal(new String(tpsByte));
					if (tps.compareTo(rocksTPS) > 0) {
						rocksJavaUtil.put(Config.MESSAGE_TPS_KEY, tps + "");
					}
				} else {
					rocksJavaUtil.put(Config.MESSAGE_TPS_KEY, tps + "");
				}
			}
		} catch (Exception ex) {
			logger.error("保存tps报错", ex);
		}
	}

	/**
	 * 等待轮循时间过去
	 * 
	 * @param first 开始事件
	 */
	private void waitInterval(Instant first) {
		long handleInterval = Duration.between(first, Instant.now()).toMillis();
		if (handleInterval < Config.DEFAULT_GOSSIP_NODE_INTERVAL) {
			try {
				Thread.sleep(Config.DEFAULT_GOSSIP_NODE_INTERVAL - handleInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 校验公钥合法性
	 * 
	 * @param pubkeyStr 公钥
	 * @return 是否合法
	 */
	private boolean validatePubkey(String pubkeyStr) {
		if (StringUtils.isEmpty(pubkeyStr)) {
			logger.error("pubkey is null.");
			return false;
		}
//        try {
//            HnKeyUtils.getPublicKey4String(pubkeyStr);
//        } catch (Exception e) {
//            logger.error("validate local full node publickey {}. error: {}", pubkeyStr, e.getMessage());
//            return false;
//        }

		return node.getLocalFullNodes().parallelStream().filter(n -> n.getStatus() == NodeStatus.HAS_SHARDED)
				.anyMatch(p -> p.getPubkey().equals(pubkeyStr));
	}

	public void interruptMe() {
		this.interrupted = true;
	}
}
