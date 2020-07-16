package org.exodus.localfullnode2.rpc.impl;

import com.zeroc.Ice.Current;

import org.exodus.localfullnode2.rpc.AppointEvent;
import org.exodus.localfullnode2.rpc.GossipObj;
import org.exodus.localfullnode2.rpc.Local2local;
import org.exodus.localfullnode2.rpc.SnapObj;

/**
 *  All rights reserved.
 * 
 * @ClassName: DefaultLocal2localImpl
 * @Description: Any class which provides the impl of {@code Local2local} should
 *               have to extend the class
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Mar 1, 2020
 *
 */
public class DefaultLocal2localImpl implements Local2local {

	@Override
	public GossipObj gossipMyMaxSeqList4Consensus(String pubkey, String sig, String snapVersion, String snapHash,
			long[] seqs, Current current) {
		throw new RuntimeException("The method should be invoked this way");
	}

	@Override
	public GossipObj gossipMyMaxSeqList4Sync(String pubkey, String sig, int otherShardId, String snapVersion,
			String snapHash, long[] seqs, Current current) {
		throw new RuntimeException("The method should be invoked this way");
	}

	@Override
	public SnapObj gossipMySnapVersion4Snap(String pubkey, String sig, String hash, String transCount,
			Current current) {
		throw new RuntimeException("The method should be invoked this way");
	}

	@Override
	public AppointEvent gossip4AppointEvent(String pubkey, String sig, int shardId, long creatorId, long creatorSeq,
			Current current) {
		throw new RuntimeException("The method should be invoked this way");
	}

	@Override
	public boolean gossipReport4split(String pubkey, String sig, String data, int shardId, String event,
			Current current) {
		throw new RuntimeException("The method should be invoked this way");
	}

	@Override
	public boolean gossip4SplitDel(String pubkey, String sig, String data, int shardId, long creatorId, long creatorSeq,
			String eventHash, boolean isNeedGossip2Center, Current current) {
		throw new RuntimeException("The method should be invoked this way");
	}

	@Override
	public long[] getHeight(Current current) {
		throw new RuntimeException("The method should be invoked this way");
	}

}
