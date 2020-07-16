package org.exodus.localfullnode2.dep.items;

import org.exodus.bean.message.SnapshotMessage;
import org.exodus.localfullnode2.dep.DependentItem;

@Deprecated
public class UpdatedSnapshotMessage extends DependentItem {
	private SnapshotMessage snapshotMessage;

	public void set(SnapshotMessage snapshotMessage) {
		this.snapshotMessage = snapshotMessage;
		nodifyAll();
	}

	public SnapshotMessage get() {
		return snapshotMessage;
	}

}
