package org.exodus.localfullnode2.dep.items;

import org.exodus.localfullnode2.dep.DependentItem;

public class CreatorId extends DependentItem {
	private long creatorId;

	public long get() {
		return creatorId;
	}

	public void set(long creatorId) {
		this.creatorId = creatorId;
		nodifyAll();
	}

}
