package org.exodus.localfullnode2.sync.rpc;

import java.util.concurrent.CompletableFuture;

import com.zeroc.Ice.Communicator;

import org.exodus.localfullnode2.rpc.RpcConnectionService;
import org.exodus.localfullnode2.sync.rpc.gen.DataSynchronizationPrx;
import org.exodus.localfullnode2.sync.rpc.gen.DistributedEventObjects;
import org.exodus.localfullnode2.sync.rpc.gen.DistributedMessageObjects;
import org.exodus.localfullnode2.sync.rpc.gen.DistributedSysMessageObjects;
import org.exodus.localfullnode2.sync.rpc.gen.Localfullnode2InstanceProfile;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: DataSynchronizationZerocInvoker
 * @Description: Wrap up the invocation to zeroc,it works as zeroc client.
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 27, 2019
 *
 */
public class DataSynchronizationZerocInvoker {

	public static CompletableFuture<DistributedEventObjects> invokeGetNotInDistributionEventsAsync(
			Communicator communicator, String ip, int port, String distJson) {
		DataSynchronizationPrx proxy = RpcConnectionService.buildDataSynchronizationProxy(communicator, ip, port);
		CompletableFuture<DistributedEventObjects> f = proxy.getNotInDistributionEventsAsync(distJson);

		return f;
	}

	public static CompletableFuture<Localfullnode2InstanceProfile> invokeGetLocalfullnode2InstanceProfileAsync(
			Communicator communicator, String ip, int port) {
		DataSynchronizationPrx proxy = RpcConnectionService.buildDataSynchronizationProxy(communicator, ip, port);
		CompletableFuture<Localfullnode2InstanceProfile> f = proxy.getLocalfullnode2InstanceProfileAsync();

		return f;
	}

	public static CompletableFuture<DistributedMessageObjects> invokeGetNotInDistributionMessagesAsync(
			Communicator communicator, String ip, int port, String distJson) {
		DataSynchronizationPrx proxy = RpcConnectionService.buildDataSynchronizationProxy(communicator, ip, port);
		CompletableFuture<DistributedMessageObjects> f = proxy.getNotInDistributionMessagesAsync(distJson);

		return f;
	}

	public static CompletableFuture<DistributedSysMessageObjects> invokeGetNotInDistributionSysMessagesAsync(
			Communicator communicator, String ip, int port, String distJson) {
		DataSynchronizationPrx proxy = RpcConnectionService.buildDataSynchronizationProxy(communicator, ip, port);
		CompletableFuture<DistributedSysMessageObjects> f = proxy.getNotInDistributionSysMessagesAsync(distJson);

		return f;
	}
}
