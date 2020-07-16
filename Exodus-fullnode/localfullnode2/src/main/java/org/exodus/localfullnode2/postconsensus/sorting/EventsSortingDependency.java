package org.exodus.localfullnode2.postconsensus.sorting;

import java.util.concurrent.BlockingQueue;

import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.AllQueues;
import org.exodus.localfullnode2.dep.items.ShardCount;
import org.exodus.localfullnode2.staging.StagingArea;
import org.exodus.core.EventBody;

public class EventsSortingDependency implements EventsSortingDependent, DependentItemConcerned {

	private AllQueues allQueues;
	private ShardCount shardCount;

	@Override
	public void update(DependentItem item) {
		set(this, item);
	}

	@Override
	public int getShardCount() {
		return shardCount.get();
	}

	@Override
	public BlockingQueue<EventBody> getShardSortQueue(int shardId) {
		return allQueues.get().getQueue(EventBody.class, StagingArea.ShardSortQueueName, shardId);
	}

	@Override
	public BlockingQueue<EventBody> getConsEventHandleQueue() {
		return allQueues.get().getQueue(EventBody.class, StagingArea.ConsEventHandleQueueName);
	}

}
