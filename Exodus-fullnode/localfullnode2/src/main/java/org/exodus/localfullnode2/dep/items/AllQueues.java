package org.exodus.localfullnode2.dep.items;

import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.staging.StagingArea;

public class AllQueues extends DependentItem {
	private StagingArea area;

	public StagingArea get() {
		return area;
	}

	public void set(StagingArea area) {
		this.area = area;
		nodifyAll();
	}

}
