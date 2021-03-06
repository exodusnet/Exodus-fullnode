package org.exodus.localfullnode2.store.rocks;

import java.util.HashMap;
import java.util.Map;

public class INosqlSnapshotImpl implements INosql {

	Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public void set(String key) {
		map.put(key, key);
	}

	@Override
	public void put(String key, String value) {
		map.put(key, value);
	}

	@Override
	public void put(String key, byte[] value) {
		map.put(key, value);
	}

	@Override
	public byte[] get(String key) {
		return map.get(key).toString().getBytes();
	}

	@Override
	public void delete(String key) {
		map.remove(key);
	}

	@Override
	public Map<byte[], byte[]> startWith(byte[] prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPrefixKeyExisted(byte[] prefix) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrefixKeyMoreThan(byte[] prefix, int isMoreThan) {
		// TODO Auto-generated method stub
		return false;
	}
}
