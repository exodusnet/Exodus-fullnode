package org.exodus.localfullnode2.postconsensus.exe;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;

import com.alibaba.fastjson.JSONObject;

import org.exodus.bean.message.Contribution;
import org.exodus.core.EventBody;
import org.exodus.localfullnode2.store.rocks.INosql;

public interface EventsExeDependent {
	long getCreatorId();

	String msgHashTreeRoot();

	void setMsgHashTreeRoot(String msgHashTreeRoot);

	// void setTotalConsEventCount(BigInteger totalConsEventCount);
	void addTotalConsEventCount(long delta);

	BigInteger getTotalConsEventCount();

	HashSet<Contribution> getContributions();

	void addConsMessageMaxId(long delta);

	BigInteger getConsMessageMaxId();

	INosql getNosql();

	// the source
	BlockingQueue<EventBody> getConsEventHandleQueue();

	// the destination
	BlockingQueue<JSONObject> getConsMessageVerifyQueue();

	void addContribution(Contribution contribution);

}
