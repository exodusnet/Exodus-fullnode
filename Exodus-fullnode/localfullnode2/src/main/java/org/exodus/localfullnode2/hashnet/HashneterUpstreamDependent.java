package org.exodus.localfullnode2.hashnet;

/**
 * 
 *  All rights reserved.
 * 
 * @Description: push all sharding's Event into hashnet model
 * @author: Francis.Deng
 * @date: December 3, 2018 7:14:32 PM
 * @version: V1.0
 */
public interface HashneterUpstreamDependent {
	int getShardCount();

	// push a sharding's event to hashnet
	void addToHashnet(int shardId);
}
