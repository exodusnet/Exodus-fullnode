package org.exodus.localfullnode2.dep.items;

import org.exodus.localfullnode2.dep.DependentItem;

public class ShardId extends DependentItem {
	private int shardId;

	public void set(int shardId) {
		this.shardId = shardId;
		nodifyAll();
	}

	public int get() {
		return shardId;
	}

}
