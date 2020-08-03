package org.exodus.localfullnode2.store;

import java.util.concurrent.BlockingQueue;

import org.exodus.core.EventBody;
import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.AllQueues;
import org.exodus.localfullnode2.dep.items.CreatorId;
import org.exodus.localfullnode2.dep.items.DBId;
import org.exodus.localfullnode2.dep.items.NValue;
import org.exodus.localfullnode2.dep.items.ShardCount;
import org.exodus.localfullnode2.staging.StagingArea;

public class EventStoreDependency implements EventStoreDependent, DependentItemConcerned {
	private DBId dbId;
	private NValue nValue;
	private ShardCount shardCount;
	private CreatorId creatorId;
	private AllQueues allQueues;

	@Override
	public String getDbId() {
		return dbId.get();
	}

	@Override
	public int getnValue() {
		return nValue.get();
	}

	@Override
	public int getShardCount() {
		return shardCount.get();
	}

	@Override
	public long getCreatorId() {
		return creatorId.get();
	}

	@Override
	public BlockingQueue<EventBody> getEventSaveQueue() {
		return allQueues.get().getQueue(EventBody.class, StagingArea.EventSaveQueueName);
	}

	@Override
	public void update(DependentItem item) {
		set(this, item);

	}

}