package org.exodus.localfullnode2.dep.items;

import org.exodus.localfullnode2.dep.DependentItem;

public class Mnemonic extends DependentItem {
	private String mnemonic;

	public void set(String mnemonic) {
		this.mnemonic = mnemonic;
		nodifyAll();
	}

	public String get() {
		return mnemonic;
	}
}
