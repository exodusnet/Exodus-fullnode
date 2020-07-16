package org.exodus.localfullnode2.dep.items;

import org.exodus.bean.wallet.Wallet;
import org.exodus.localfullnode2.dep.DependentItem;

/**
 * 
 * 
 *  All rights reserved.
 * 
 * @Description: Stand for {@link Wallet} item.
 * @author: Francis.Deng
 * @date: Jun 9, 2019 11:43:43 PM
 * @version: V1.0
 */
public class Wal extends DependentItem {
	private Wallet wal;

	public Wallet get() {
		return wal;
	}

	public void set(Wallet wal) {
		this.wal = wal;
		nodifyAll();
	}

}
