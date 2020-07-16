package org.exodus.localfullnode2.dep.items;

import org.exodus.localfullnode2.dep.DependentItem;

public class DBId extends DependentItem {
	private String dbId;

	public String get() {
		return dbId;
	}

	public void set(String dbId) {
		this.dbId = dbId;
		nodifyAll();
	}
}
