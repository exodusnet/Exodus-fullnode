package org.exodus.localfullnode2.sync.source;

import org.exodus.core.EventBody;
import org.exodus.localfullnode2.sync.DistributedObjects;
import org.exodus.localfullnode2.sync.ISyncContext;
import org.exodus.localfullnode2.sync.measure.ChunkDistribution;
import org.exodus.localfullnode2.sync.measure.Distribution;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: ISyncSource
 * @Description: from the perspective of client,the class provide access to rpc
 *               interface.
 * @author Francis.Deng
 * @mailbox francis_xiiiv@163.com
 * @date Aug 15, 2019
 *
 */
public interface ISyncSource {
	ILFN2Profile getProfile(ISyncContext context);// get localfullnode2 metadata and keep it inside the context

	DistributedObjects<Distribution, EventBody> getNotInDistributionEvents(Distribution dist);

	DistributedObjects<ChunkDistribution<String>, String> getNotInDistributionMessages(ChunkDistribution<String> dist);

	DistributedObjects<ChunkDistribution<String>, String> getNotInDistributionSysMessages(
			ChunkDistribution<String> dist);
}
