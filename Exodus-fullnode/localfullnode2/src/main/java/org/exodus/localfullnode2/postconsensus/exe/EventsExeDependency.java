package org.exodus.localfullnode2.postconsensus.exe;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;

import com.alibaba.fastjson.JSONObject;

import org.exodus.bean.message.Contribution;
import org.exodus.core.EventBody;
import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.*;
import org.exodus.localfullnode2.staging.StagingArea;
import org.exodus.localfullnode2.store.rocks.INosql;
import org.exodus.localfullnode2.store.rocks.RocksJavaUtil;

/**
 * 
 *  All rights reserved.
 * 
 * @Description: Missing snapshot-related thing.
 * @author: Francis.Deng
 * @date: May 20, 2019 1:35:52 AM
 * @version: V1.0
 */
public class EventsExeDependency implements EventsExeDependent, DependentItemConcerned {

	private CreatorId creatorId;
	private Stat stat;
	private DBId dbId;
	private AllQueues allQueues;
	private SS ss;

	@Override
	public void update(DependentItem item) {
		set(this, item);
	}

	@Override
	public long getCreatorId() {
		return creatorId.get();
	}

	@Override
	public String msgHashTreeRoot() {
		return ss.getMsgHashTreeRoot();
	}

	@Override
	public void setMsgHashTreeRoot(String msgHashTreeRoot) {
		ss.setMsgHashTreeRoot(msgHashTreeRoot);
	}

	@Override
	public void addTotalConsEventCount(long delta) {
		stat.addTotalConsEventCount(delta);
	}

	@Override
	public BigInteger getTotalConsEventCount() {
		return stat.getTotalConsEventCount();
	}

	@Override
	public HashSet<Contribution> getContributions() {
		return ss.getContributions();
	}

	@Override
	public void addConsMessageMaxId(long delta) {
		stat.addConsMessageMaxId(delta);
	}

	@Override
	public BigInteger getConsMessageMaxId() {
		return stat.getConsMessageMaxId();
	}

//	@Override
//	public String dbId() {
//		return dbId.get();
//	}

	@Override
	public INosql getNosql() {
		return new RocksJavaUtil(dbId.get());
	}

	@Override
	public BlockingQueue<EventBody> getConsEventHandleQueue() {
		return allQueues.get().getQueue(EventBody.class, StagingArea.ConsEventHandleQueueName);
	}

	@Override
	public BlockingQueue<JSONObject> getConsMessageVerifyQueue() {
		return allQueues.get().getQueue(JSONObject.class, StagingArea.ConsMessageVerifyQueueName);
	}

	@Override
	public void addContribution(Contribution contribution) {
		ss.addContribution(contribution);
	}

}
