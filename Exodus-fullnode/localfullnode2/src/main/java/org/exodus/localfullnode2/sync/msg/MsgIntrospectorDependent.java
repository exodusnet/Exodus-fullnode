package org.exodus.localfullnode2.sync.msg;

import org.exodus.localfullnode2.store.rocks.INosql;

public interface MsgIntrospectorDependent {
	String getDbId();

	INosql getNosql();
}
