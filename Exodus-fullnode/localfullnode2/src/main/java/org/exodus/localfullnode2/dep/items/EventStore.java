package org.exodus.localfullnode2.dep.items;

import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.store.IEventStore;

public class EventStore extends DependentItem {
	private IEventStore eventStore;

	public IEventStore get() {
		return eventStore;
	}

	public void set(IEventStore eventStore) {
		this.eventStore = eventStore;
		nodifyAll();
	}
}
