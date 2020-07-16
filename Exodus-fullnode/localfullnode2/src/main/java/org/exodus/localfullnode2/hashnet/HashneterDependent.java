package org.exodus.localfullnode2.hashnet;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.exodus.bean.node.LocalFullNode;
import org.exodus.core.EventBody;
import org.exodus.localfullnode2.store.EventStoreDependent;

public interface HashneterDependent {
	int getShardCount();

	int getShardId();

	// the source from which it retrieved
	// EventBody[] getAllQueuedEvents(int shardId);
	// IEventFlow getEventFlow();

	BigInteger getTotalEventCount();

	int getNValue();

	// IEventStore getEventStore();

	int getCreatorId();

	// the destination to which sorted EventBodies were sent
	BlockingQueue<EventBody> getShardSortQueue(int shardId);

	EventStoreDependent getEventStoreDependent();

	List<LocalFullNode> getLocalFullNodes();

	PrivateKey getPrivateKey();
}
