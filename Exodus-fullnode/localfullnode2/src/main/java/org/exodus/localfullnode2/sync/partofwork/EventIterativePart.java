package org.exodus.localfullnode2.sync.partofwork;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.exodus.core.EventBody;
import org.exodus.localfullnode2.gossip.persistence.NewGossipEventsPersistence;
import org.exodus.localfullnode2.gossip.persistence.NewGossipEventsPersistenceDependent;
import org.exodus.localfullnode2.staging.StagingArea;
import org.exodus.localfullnode2.store.rocks.RocksJavaUtil;
import org.exodus.localfullnode2.store.rocks.key.EventIndexes;
import org.exodus.localfullnode2.sync.DistributedObjects;
import org.exodus.localfullnode2.sync.ISyncContext;
import org.exodus.localfullnode2.sync.SyncException;
import org.exodus.localfullnode2.sync.SyncWorksInLab.BasedIterativePart;
import org.exodus.localfullnode2.sync.measure.Distribution;
import org.exodus.localfullnode2.sync.source.ILFN2Profile;
import org.exodus.localfullnode2.sync.source.ISyncSource;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: EventSynchronizer
 * @Description: event synchronizer
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 24, 2019
 * @version 2.0 add event sort index by
 *          "events$s${consTimestamp}${shardId}${pair}"
 */
public class EventIterativePart extends BasedIterativePart {
	private static final Logger logger = LoggerFactory.getLogger(EventIterativePart.class);

	@Override
	public void runOnce(ISyncContext context) {
		Distribution myDist = context.getDistribution();
		ISyncSource synSource = context.getSyncSourceProxy();
		DistributedObjects<Distribution, EventBody> distributedObjects;

		ILFN2Profile profile = getSourceProfile(context);

		try {
			distributedObjects = synSource.getNotInDistributionEvents(myDist);
		} catch (Exception e) {
			logger.error("error in retrieving {}", myDist);
			e.printStackTrace();

			throw new SyncException(e);
		}

		if (distributedObjects == null || distributedObjects.getObjects() == null
				|| distributedObjects.getObjects().length == 0) {
			done = true;
			return;
		}
		// int length = distributedObjects.getObjects().length;

		// handle the batch of EventBodies
		NewGossipEventsPersistenceDependency dep = new NewGossipEventsPersistenceDependency(new StagingArea(),
				distributedObjects, profile);
		NewGossipEventsPersistence newGossipEventsPersistence = new NewGossipEventsPersistence();
		newGossipEventsPersistence.persistNewEvents(dep, eb -> {
			RocksJavaUtil rocksJavaUtil = new RocksJavaUtil(dep.getDbId());
			String eventSortKey = EventIndexes.getConcensusEventSortKey(eb);
			rocksJavaUtil.put(eventSortKey, new byte[0]);

			logger.debug("event sort key : {}", eventSortKey);
		});
//		newGossipEventsPersistence.persistNewEvents(new NewGossipEventsPersistenceDependent() {
//
//			@Override
//			public BlockingQueue<EventBody> getEventSaveQueue() {
//				StagingArea stagingArea = new StagingArea();
//				stagingArea.createQueue(EventBody.class, StagingArea.EventSaveQueueName, 10000000, null);
//
//				BlockingQueue<EventBody> q = stagingArea.getQueue(EventBody.class, StagingArea.EventSaveQueueName);
//				for (EventBody eb : distributedObjects.getObjects()) {
//					q.add(eb);
//				}
//
//				return q;
//			}
//
//			@Override
//			public String getDbId() {
//				// return profile.getDBId();
//
//				new MysqlHelper("0_8", profile.getDBId(), Boolean.FALSE);
//				return "0_8";
//			}
//
//			// ignore {@code addTotalEventCount} and {@code getTotalEventCount}
//			@Override
//			public void addTotalEventCount(long delta) {
//				return;
//			}
//
//			@Override
//			public BigInteger getTotalEventCount() {
//				return BigInteger.ZERO;
//			}
//
//		});

		context.join(distributedObjects.getDist());
		logger.info(context.getDistribution().toString());

	}

	private static class NewGossipEventsPersistenceDependency implements NewGossipEventsPersistenceDependent {

//		private final StagingArea stagingArea;
//		private final DistributedObjects<EventBody> distributedObjects;
		private final ILFN2Profile profile;
		private final BlockingQueue<EventBody> eventBodyQueue;

		@SuppressWarnings("unused")
		public NewGossipEventsPersistenceDependency(StagingArea stagingArea,
				DistributedObjects<Distribution, EventBody> distributedObjects, ILFN2Profile profile) {
//			this.stagingArea = stagingArea;
//			this.distributedObjects = distributedObjects;
			this.profile = profile;
			this.eventBodyQueue = initBlockingQueue(stagingArea, distributedObjects);
		}

		@SuppressWarnings("unused")
		private BlockingQueue<EventBody> initBlockingQueue(StagingArea stagingArea,
				DistributedObjects<Distribution, EventBody> distributedObjects) {
			stagingArea.createQueue(EventBody.class, StagingArea.EventSaveQueueName, 10000000, null);

			BlockingQueue<EventBody> q = stagingArea.getQueue(EventBody.class, StagingArea.EventSaveQueueName);
			for (EventBody eb : distributedObjects.getObjects()) {
				q.add(eb);
			}

			return q;
		}

		@Override
		public BlockingQueue<EventBody> getEventSaveQueue() {
//			StagingArea stagingArea = new StagingArea();
//			stagingArea.createQueue(EventBody.class, StagingArea.EventSaveQueueName, 10000000, null);
//
//			BlockingQueue<EventBody> q = stagingArea.getQueue(EventBody.class, StagingArea.EventSaveQueueName);
//			for (EventBody eb : distributedObjects.getObjects()) {
//				q.add(eb);
//			}
//
//			return q;
			return eventBodyQueue;
		}

		@Override
		public String getDbId() {
			return profile.getDBId();

//			new MysqlHelper("0_8", profile.getDBId(), Boolean.FALSE);
//			return "0_8";
		}

		// ignore {@code addTotalEventCount} and {@code getTotalEventCount}
		@Override
		public void addTotalEventCount(long delta) {
			return;
		}

		@Override
		public BigInteger getTotalEventCount() {
			return BigInteger.ZERO;
		}

	}

}
