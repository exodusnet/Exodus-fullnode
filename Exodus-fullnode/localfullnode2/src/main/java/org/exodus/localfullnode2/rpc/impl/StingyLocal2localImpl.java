package org.exodus.localfullnode2.rpc.impl;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.zeroc.Ice.Current;

import org.exodus.localfullnode2.dep.DepItemsManager;
import org.exodus.localfullnode2.gossip.l2l.L2LCore;
import org.exodus.localfullnode2.nodes.LocalFullNode1GeneralNode;
import org.exodus.localfullnode2.rpc.GossipObj;
import org.exodus.localfullnode2.store.EventKeyPair;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: StingyLocal2localImpl
 * @Description: The difference between {@code Local2localImpl} and it was that
 *               the class is stingy to throw its own events.
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Mar 1, 2020
 *
 */
public class StingyLocal2localImpl extends DefaultLocal2localImpl {
	private static final Logger logger = LoggerFactory.getLogger(StingyLocal2localImpl.class);

	private volatile LocalFullNode1GeneralNode node;
	private List<Map<EventKeyPair, Map<String, Set<String>>>> splitReportCache;

	public StingyLocal2localImpl(LocalFullNode1GeneralNode node) {
		this.node = node;
	}

	@Override
	public GossipObj gossipMyMaxSeqList4Consensus(String pubkey, String sig, String snapVersion, String snapHash,
			long[] seqs, Current current) {
		logger.info("gossipMyMaxSeqList4Consensus Current:{}", JSON.toJSONString(current));
		GossipObj gossipObj = null;
		BigInteger vers = DepItemsManager.getInstance().attachSS(null).getCurrSnapshotVersion();

		logger.info("gossipMyMaxSeqList4Consensus is running(answering the gossip)");

		long slot = node.getCreatorId();

		L2LCore l2l = new L2LCore();
		gossipObj = l2l.gossipMyMaxSeqList4Consensus(pubkey, sig, snapVersion, snapHash, seqs, vers,
				node.getLocalFullNodes(), node.getEventStore(), node.getShardId(), node.nodeParameters().dbId,
				(int) slot);

		return gossipObj;
	}

}
