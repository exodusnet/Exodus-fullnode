package org.exodus.localfullnode2.snapshot;

import java.util.concurrent.CompletableFuture;

import org.exodus.cluster.Member;
import org.exodus.localfullnode2.rpc.SnapObj;

/**
 * 
 *  All rights reserved.
 * 
 * @Description: provide snapshot sync communication utility
 * @author: Francis.Deng
 * @date: Dec 6, 2018 1:31:23 AM
 * @version: V1.0
 */
public interface SnapshotSyncConsumable {
	// snapshot synchronizing
	CompletableFuture<SnapObj> gossipMySnapVersion4SnapAsync(Member neighbor, String pubkey, String sig, String hash,
			String transCount);
}
