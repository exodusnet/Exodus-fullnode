package org.exodus.localfullnode2.dep.items;

import org.exodus.localfullnode2.dep.DependentItem;

public class ShardCount extends DependentItem {
	private int shardCount;

	public int get() {
		return shardCount;
	}

	public void set(int shardCount) {
		this.shardCount = shardCount;
		nodifyAll();
	}
}
