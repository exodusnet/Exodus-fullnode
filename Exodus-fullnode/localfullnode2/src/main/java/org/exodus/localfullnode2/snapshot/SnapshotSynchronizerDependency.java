package org.exodus.localfullnode2.snapshot;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.alibaba.fastjson.JSONObject;
import com.zeroc.Ice.Communicator;

import org.exodus.bean.message.SnapshotMessage;
import org.exodus.bean.message.SnapshotPoint;
import org.exodus.bean.node.LocalFullNode;
import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.*;
import org.exodus.localfullnode2.message.service.ITransactionDbService;
import org.exodus.localfullnode2.message.service.TransactionDbService;
import org.exodus.localfullnode2.staging.StagingArea;
import org.exodus.localfullnode2.store.SnapshotDbService;

/**
 * 
 * 
 *  All rights reserved.
 * 
 * @Description:cover all dependency coming to {@code SnapshotSynchronizer}
 *                    Note:pay more attention on TBD items.
 * @author: Francis.Deng
 * @date: May 6, 2018 11:49:14 PM
 * @version: V1.0
 */
public class SnapshotSynchronizerDependency implements SnapshotSynchronizerDependent, DependentItemConcerned {

	private SnapshotSyncConsumable ssc = new SnapshotSyncConsumer();
	private SS ss;
	private Stat stat;
	private ShardCount shardCount;
	private NValue nValue;
	private PublicKey publicKey;
	private AllQueues allQueues;

	@Override
	public void update(DependentItem item) {
		set(this,item);
	}

	@Override
	public BigInteger getCurrSnapshotVersion() {
		return ss.getCurrSnapshotVersion();
	}

	@Override
	public int getShardCount() {
		return shardCount.get();
	}

	@Override
	public int getnValue() {
		return nValue.get();
	}

	@Override
	public BigInteger getConsMessageMaxId() {
		return stat.getConsMessageMaxId();
	}

	@Override
	public SnapshotSyncConsumable getSnapshotSync() {
		return ssc;
	}

	@Override
	public java.security.PublicKey getPublicKey() {
		return publicKey.get();
	}

	@Override
	public BlockingQueue<JSONObject> getConsMessageVerifyQueue() {
		return allQueues.get().getQueue(JSONObject.class, StagingArea.ConsMessageVerifyQueueName);
	}

	@Override
	public HashMap<BigInteger, SnapshotPoint> getSnapshotPointMap() {
		return ss.getSnapshotPointMap();
	}

	@Override
	public HashMap<BigInteger, String> getTreeRootMap() {
		return ss.getTreeRootMap();
	}

	@Override
	public void setSnapshotMessage(SnapshotMessage snapshotMessage) {
		ss.setSnapshotMessage(snapshotMessage);
	}

	@Override
	public void setMsgHashTreeRoot(String msgHashTreeRoot) {
		ss.setMsgHashTreeRoot(msgHashTreeRoot);
	}

	@Override
	public String getMsgHashTreeRoot() {
		return ss.getMsgHashTreeRoot();
	}

	@Override
	public void putTreeRootMap(BigInteger snapVersion, String msgHashTreeRoot) {
		ss.putTreeRootMap(snapVersion, msgHashTreeRoot);
	}

	@Override
	public void putSnapshotPointMap(BigInteger snapVersion, SnapshotPoint snapshotPoint) {
		ss.putSnapshotPointMap(snapVersion, snapshotPoint);
	}
}
