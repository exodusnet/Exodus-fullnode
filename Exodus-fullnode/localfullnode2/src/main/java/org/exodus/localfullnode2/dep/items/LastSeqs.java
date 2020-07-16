package org.exodus.localfullnode2.dep.items;

import java.util.concurrent.ConcurrentHashMap;

import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.store.AtomicLongArrayWrapper;

public class LastSeqs extends DependentItem {
	private ConcurrentHashMap<Integer, AtomicLongArrayWrapper> lastSeqs;

	public ConcurrentHashMap<Integer, AtomicLongArrayWrapper> get() {
		return lastSeqs;
	}

	public void set(ConcurrentHashMap<Integer, AtomicLongArrayWrapper> lastSeqs) {
		this.lastSeqs = lastSeqs;
		nodifyAll();
	}

}
