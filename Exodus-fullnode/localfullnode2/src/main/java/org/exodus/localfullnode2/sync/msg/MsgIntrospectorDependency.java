package org.exodus.localfullnode2.sync.msg;

import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.DBId;
import org.exodus.localfullnode2.store.rocks.INosql;
import org.exodus.localfullnode2.store.rocks.RocksJavaUtil;

public class MsgIntrospectorDependency implements MsgIntrospectorDependent, DependentItemConcerned {
	private DBId dbId;

	@Override
	public void update(DependentItem item) {
		set(this, item);
	}

	@Override
	public String getDbId() {
		return dbId.get();
	}

	@Override
	public INosql getNosql() {
		return new RocksJavaUtil(dbId.get());
	}

}
