package org.exodus.localfullnode2.hashnet;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.exodus.bean.node.LocalFullNode;
import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.AllQueues;
import org.exodus.localfullnode2.dep.items.CreatorId;
import org.exodus.localfullnode2.dep.items.EventFlow;
import org.exodus.localfullnode2.dep.items.LocalFullNodes;
import org.exodus.localfullnode2.dep.items.NValue;
import org.exodus.localfullnode2.dep.items.PrivateKey;
import org.exodus.localfullnode2.dep.items.ShardCount;
import org.exodus.localfullnode2.dep.items.ShardId;
import org.exodus.localfullnode2.dep.items.Stat;
import org.exodus.localfullnode2.staging.StagingArea;
import org.exodus.core.EventBody;
import org.exodus.localfullnode2.store.EventStoreDependent;

/**
 *  All rights reserved.
 * 
 * @Description: Note that the class depends on {@link EventStoreDependent}
 * @author: Francis.Deng
 * @date: May 19, 2019 2:51:04 AM
 * @version: V1.0
 */
public class HashneterDependency implements HashneterDependent, DependentItemConcerned {

	private ShardCount shardCount;
	private ShardId shardId;
	private EventFlow eventFlow;
	private Stat stat;
	private NValue nValue;
	// private EventStore eventStore;
	private CreatorId creatorId;
	private AllQueues allQueues;
	private LocalFullNodes localFullNodes;
	private PrivateKey privateKey;

	private EventStoreDependent eventStoreDep;

	@Override
	public int getShardCount() {
		return shardCount.get();
	}

	@Override
	public int getShardId() {
		return shardId.get();
	}

//	@Override
//	public IEventFlow getEventFlow() {
//		return eventFlow.get();
//	}

	@Override
	public BigInteger getTotalEventCount() {
		return stat.getTotalEventCount();
	}

	@Override
	public int getNValue() {
		return nValue.get();
	}

//	@Override
//	public IEventStore getEventStore() {
//		return eventStore.get();
//	}

	@Override
	public int getCreatorId() {
		return (int) creatorId.get();
	}

	@Override
	public BlockingQueue<EventBody> getShardSortQueue(int shardId) {
		return allQueues.get().getQueue(EventBody.class, StagingArea.ShardSortQueueName, shardId);
	}

	@Override
	public void update(DependentItem item) {
		this.set(this, item);
	}

	@Override
	public EventStoreDependent getEventStoreDependent() {
		return eventStoreDep;
	}

	public void setEventStoreDependent(EventStoreDependent eventStoreDep) {
		this.eventStoreDep = eventStoreDep;
	}

	@Override
	public List<LocalFullNode> getLocalFullNodes() {
		return localFullNodes.get();
	}

	@Override
	public java.security.PrivateKey getPrivateKey() {
		return privateKey.get();
	}

}
