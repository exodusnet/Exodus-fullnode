package org.exodus.localfullnode2.snapshot;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import org.exodus.cluster.Member;
import org.exodus.localfullnode2.dep.DepItemsManager;
import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.DirectCommunicator;
import org.exodus.localfullnode2.rpc.Local2localPrx;
import org.exodus.localfullnode2.rpc.RpcConnectionService;
import org.exodus.localfullnode2.rpc.SnapObj;

/**
 * 
 *  All rights reserved.
 * 
 * @Description: access peer node to get sync snap object via zeroc
 *               <p>
 *               {@link GossipEventThread::gossip2Local}
 * @author: Francis.Deng
 * @date: May 7, 2019 1:42:45 AM
 * @version: V1.0
 */
public class SnapshotSyncConsumer implements SnapshotSyncConsumable, DependentItemConcerned {
	static final Logger logger = LoggerFactory.getLogger(SnapshotSyncConsumer.class);

	private DirectCommunicator dc = DepItemsManager.getInstance().attachDirectCommunicator(null);

	@Override
	public CompletableFuture<SnapObj> gossipMySnapVersion4SnapAsync(Member neighbor, String pubkey, String sig,
			String hash, String messageMaxId) {

		Local2localPrx nprx = RpcConnectionService.buildConnection2localFullNode(dc.get(), neighbor);

		CompletableFuture<SnapObj> snapResult = nprx.gossipMySnapVersion4SnapAsync(pubkey, sig, hash, messageMaxId);

		logger.info(">>>>>RETURN<<<<<gossipMySnapVersion4SnapAsync:\n snapResult: {}", JSON.toJSONString(snapResult));
		return snapResult;
	}

	@Override
	public void update(DependentItem item) {
		set(this, item);
	}
}