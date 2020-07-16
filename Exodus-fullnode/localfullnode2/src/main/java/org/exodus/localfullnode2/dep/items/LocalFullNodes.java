package org.exodus.localfullnode2.dep.items;

import java.util.List;

import org.exodus.bean.node.LocalFullNode;
import org.exodus.localfullnode2.dep.DependentItem;

public class LocalFullNodes extends DependentItem {
	private List<LocalFullNode> localFullNodes;

	public void set(List<LocalFullNode> localFullNodes) {
		this.localFullNodes = localFullNodes;
		nodifyAll();
	}

	public List<LocalFullNode> get() {
		return localFullNodes;
	}
}
