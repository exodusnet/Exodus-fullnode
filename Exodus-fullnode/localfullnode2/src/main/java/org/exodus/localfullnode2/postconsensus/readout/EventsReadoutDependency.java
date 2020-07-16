package org.exodus.localfullnode2.postconsensus.readout;

import java.util.concurrent.BlockingQueue;

import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.AllQueues;
import org.exodus.localfullnode2.dep.items.ShardCount;
import org.exodus.localfullnode2.hashnet.Event;
import org.exodus.localfullnode2.hashnet.IHashneter;
import org.exodus.localfullnode2.staging.StagingArea;
import org.exodus.core.EventBody;

public class EventsReadoutDependency implements EventsReadoutDependent, DependentItemConcerned {

	private ShardCount shardCount;
	private AllQueues allQueues;

	private IHashneter hashneter;

	@Override
	public void update(DependentItem item) {
		this.set(this, item);

	}

	@Override
	public int getShardCount() {
		return shardCount.get();
	}

	@Override
	public Event[] getAllConsEvents(int shardId) {
		return hashneter.getAllConsEvents(shardId);
	}

	@Override
	public BlockingQueue<EventBody> getShardSortQueue(int shardId) {
		return allQueues.get().getQueue(EventBody.class, StagingArea.ShardSortQueueName, shardId);
	}

	public void setHashneter(IHashneter hashneter) {
		this.hashneter = hashneter;
	}

}
