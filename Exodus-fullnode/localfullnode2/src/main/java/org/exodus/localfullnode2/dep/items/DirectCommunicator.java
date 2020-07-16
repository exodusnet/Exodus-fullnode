package org.exodus.localfullnode2.dep.items;

import com.zeroc.Ice.Communicator;

import org.exodus.localfullnode2.dep.DependentItem;

public class DirectCommunicator extends DependentItem {
	private Communicator communicator;

	public Communicator get() {
		return communicator;
	}

	public void set(Communicator communicator) {
		this.communicator = communicator;
		nodifyAll();
	}

}
