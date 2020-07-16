package org.exodus.localfullnode2.message;

import java.util.concurrent.BlockingQueue;

import com.alibaba.fastjson.JSONObject;

import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.AllQueues;
import org.exodus.localfullnode2.staging.StagingArea;

public class MessagesVerificationDependency implements MessagesVerificationDependent, DependentItemConcerned {

	private AllQueues allQueues;

	@Override
	public void update(DependentItem item) {
		set(this, item);
	}

	@Override
	public BlockingQueue<JSONObject> getConsMessageVerifyQueue() {
		return allQueues.get().getQueue(JSONObject.class, StagingArea.ConsMessageVerifyQueueName);
	}

	@Override
	public BlockingQueue<JSONObject> getConsMessageHandleQueue() {
		return allQueues.get().getQueue(JSONObject.class, StagingArea.ConsMessageHandleQueueName);
	}

}
