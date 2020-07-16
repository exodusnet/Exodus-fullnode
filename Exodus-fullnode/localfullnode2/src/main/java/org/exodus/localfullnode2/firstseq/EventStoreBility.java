package org.exodus.localfullnode2.firstseq;

import java.math.BigInteger;

import org.exodus.localfullnode2.firstseq.FirstSeqsbility.IEventStoreBility;
import org.exodus.localfullnode2.store.EventKeyPair;
import org.exodus.localfullnode2.store.rocks.RocksJavaUtil;
import org.exodus.localfullnode2.store.rocks.key.FirstSeqKey;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: EventStoreBility
 * @Description: first seq format: FirstSeqKey - BigInteger
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 28, 2019
 *
 */
public class EventStoreBility implements IEventStoreBility {

	private RocksJavaUtil rocksJavaUtil;
	private long[][] lastSeqs;

	public EventStoreBility(String dbId, long[][] lastSeqs) {
		rocksJavaUtil = new RocksJavaUtil(dbId);
		this.lastSeqs = lastSeqs;
	}

	@Override
	public boolean exist(EventKeyPair pair) {
		byte[] ebByte = rocksJavaUtil.get(pair.toString());
		return (null != ebByte && ebByte.length > 0);
	}

	@Override
	public BigInteger getLastSeq(int shardId, int idInShard) {
		return new BigInteger(String.valueOf(lastSeqs[shardId][idInShard]));
	}

	@Override
	public void put(FirstSeqKey firstSeqKey, BigInteger seq) {
		rocksJavaUtil.put(firstSeqKey.toBytes(), seq.toByteArray());
	}

}
