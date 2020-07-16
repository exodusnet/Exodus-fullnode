package org.exodus.localfullnode2.firstseq;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.DBId;
import org.exodus.localfullnode2.dep.items.LastSeqs;
import org.exodus.localfullnode2.dep.items.NValue;
import org.exodus.localfullnode2.dep.items.ShardCount;
import org.exodus.localfullnode2.store.AtomicLongArrayWrapper;

public class FirstSeqsDependency implements FirstSeqsDependent, DependentItemConcerned {
	private ShardCount shardCount;
	private NValue nValue;
	private LastSeqs lastSeqs;
	private DBId dbId;

	@Override
	public void update(DependentItem item) {
		set(this, item);
	}

	@Override
	public int getShardCount() {
		return shardCount.get();
	}

	@Override
	public long[][] getLastSeqs() {
		ConcurrentHashMap<Integer, AtomicLongArrayWrapper> allSeqs = lastSeqs.get();
		long[][] allLastSeqs = new long[allSeqs.size()][];
		for (Entry<Integer, AtomicLongArrayWrapper> s : allSeqs.entrySet()) {
			allLastSeqs[s.getKey().intValue()] = new long[s.getValue().length()];

			for (int i = 0; i < s.getValue().length(); i++) {
				allLastSeqs[s.getKey().intValue()][i] = s.getValue().get(i);
			}
		}

		return allLastSeqs;
	}

	@Override
	public int getnValue() {
		return nValue.get();
	}

	@Override
	public String getDbId() {
		return dbId.get();
	}

}
