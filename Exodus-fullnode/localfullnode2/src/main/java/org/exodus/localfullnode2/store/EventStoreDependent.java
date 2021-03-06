package org.exodus.localfullnode2.store;

import org.exodus.core.EventBody;
import java.util.concurrent.BlockingQueue;

public interface EventStoreDependent {
	String getDbId();

	int getnValue();

	int getShardCount();

	long getCreatorId();

	BlockingQueue<EventBody> getEventSaveQueue();
}
