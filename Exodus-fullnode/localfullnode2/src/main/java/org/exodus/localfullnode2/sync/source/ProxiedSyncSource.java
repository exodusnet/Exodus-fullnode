package org.exodus.localfullnode2.sync.source;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.zeroc.Ice.Communicator;

import org.exodus.core.EventBody;
import org.exodus.localfullnode2.sync.Addr;
import org.exodus.localfullnode2.sync.DistributedObjects;
import org.exodus.localfullnode2.sync.ISyncContext;
import org.exodus.localfullnode2.sync.Mapper;
import org.exodus.localfullnode2.sync.SyncException;
import org.exodus.localfullnode2.sync.measure.ChunkDistribution;
import org.exodus.localfullnode2.sync.measure.Distribution;
import org.exodus.localfullnode2.sync.rpc.DataSynchronizationZerocInvoker;
import org.exodus.localfullnode2.sync.rpc.gen.DistributedEventObjects;
import org.exodus.localfullnode2.sync.rpc.gen.DistributedMessageObjects;
import org.exodus.localfullnode2.sync.rpc.gen.DistributedSysMessageObjects;
import org.exodus.localfullnode2.sync.rpc.gen.Localfullnode2InstanceProfile;
import org.exodus.localfullnode2.sync.rpc.gen.MerkleTreeizedSyncEvent;
import org.exodus.localfullnode2.sync.rpc.gen.MerkleTreeizedSyncMessage;
import org.exodus.localfullnode2.sync.rpc.gen.MerkleTreeizedSyncSysMessage;
import org.exodus.localfullnode2.sync.rpc.gen.SyncEvent;
import org.exodus.localfullnode2.utilities.GenericArray;
import org.exodus.localfullnode2.utilities.merkle.MerklePath;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: ProxiedSyncSource
 * @Description: TODO
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 27, 2019
 *
 */
public class ProxiedSyncSource implements ISyncSource {
	private static final Logger logger = LoggerFactory.getLogger(ProxiedSyncSource.class);

	private Communicator communicator;
	private Addr addresses[];

	@Override
	public ILFN2Profile getProfile(ISyncContext context) {
		Addr preferred = addresses[0];
		CompletableFuture<Localfullnode2InstanceProfile> f;
		try {
			f = DataSynchronizationZerocInvoker.invokeGetLocalfullnode2InstanceProfileAsync(communicator,
					preferred.getIp(), preferred.getPort());
		} catch (Exception e) {
			logger.error("error in invoking <DataSynchronizationZeroc> on {}:{}", preferred.getIp(),
					preferred.getPort());
			e.printStackTrace();
			throw new SyncException(e);
		}

		Localfullnode2InstanceProfile profile;
		try {
			profile = f.get();

			ILFN2Profile nILFN2Profile = new ILFN2Profile() {

				@Override
				public String getDBId() {
					return profile.dbId;
				}

				@Override
				public int getShardId() {
					return profile.shardId;
				}

				@Override
				public int getCreatorId() {
					return profile.creatorId;
				}

				@Override
				public int getNValue() {
					// TODO Auto-generated method stub
					return profile.nValue;
				}

				@Override
				public int getShardCount() {
					// TODO Auto-generated method stub
					return profile.shardCount;
				}
			};

			context.setProfile(nILFN2Profile);
			return nILFN2Profile;

		} catch (Exception e) {
			logger.error("failed in CompletableFuture's get");
			e.printStackTrace();
			throw new SyncException(e);
		}

	}

	@Override
	public DistributedObjects<Distribution, EventBody> getNotInDistributionEvents(Distribution dist) {
		Gson gson = new Gson();
		Addr preferred = addresses[0];
		Addr candidate = null;
		if (addresses.length > 1) {
			candidate = addresses[1];
		}

//		CompletableFuture<DistributedEventObjects> f = DataSynchronizationZerocInvoker
//				.invokeGetNotInDistributionEventsAsync(communicator, preferred.getIp(), preferred.getPort(),
//						JSON.toJSONString(dist));
		CompletableFuture<DistributedEventObjects> f = DataSynchronizationZerocInvoker
				.invokeGetNotInDistributionEventsAsync(communicator, preferred.getIp(), preferred.getPort(),
						gson.toJson(dist));
		DistributedEventObjects deo = null;
		try {
			deo = f.get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SyncException(e);
		}

		MerkleTreeizedSyncEvent[] events = deo.events;
		byte[] rootHash = deo.rootHash;
		String distJson = deo.distJson;
		Distribution nextDist = gson.fromJson(distJson, Distribution.class);
		GenericArray<EventBody> eventBodies = new GenericArray<>();

		if (!nextDist.isNull()) {
			boolean valid = Arrays.stream(events).anyMatch((t) -> {
				// due to interface specification change
				// String merklePathJson = t.merklePathJson;
				byte[][] merklePath = t.merklePath;
				String[] merklePathIndex = t.merklePathIndex;

				SyncEvent syncEvent = t.syncEvent;

				// MerklePath mp = JSON.parseObject(merklePathJson, MerklePath.class);
				MerklePath mp = new MerklePath(merklePath, merklePathIndex);
				return mp.validate(Mapper.transformFrom(syncEvent), rootHash);
			});

			if (!valid)
				throw new RuntimeException("failed in merkle tree validation");

			Arrays.stream(events).parallel().forEach(t -> {
				EventBody eb = new EventBody();
				Mapper.copyProperties(eb, t.syncEvent, true);

				eventBodies.append(eb);
			});

//			return new DistributedObjects<EventBody>(JSON.parseObject(distJson, Distribution.class),
//					eventBodies.toArray(new EventBody[eventBodies.length()]));
			return new DistributedObjects<Distribution, EventBody>(gson.fromJson(distJson, Distribution.class),
					eventBodies.toArray(new EventBody[eventBodies.length()]));
		} else {
			return null;
		}

	}

	@Override
	public DistributedObjects<ChunkDistribution<String>, String> getNotInDistributionMessages(
			ChunkDistribution<String> dist) {
		Gson gson = new Gson();
		Addr preferred = addresses[0];
		Addr candidate = null;
		if (addresses.length > 1) {
			candidate = addresses[1];
		}

		CompletableFuture<DistributedMessageObjects> f = DataSynchronizationZerocInvoker
				.invokeGetNotInDistributionMessagesAsync(communicator, preferred.getIp(), preferred.getPort(),
						gson.toJson(dist));
		DistributedMessageObjects deo = null;
		try {
			deo = f.get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SyncException(e);
		}

		MerkleTreeizedSyncMessage[] messages = deo.messages;
		byte[] rootHash = deo.rootHash;
		String distJson = deo.distJson;
		ChunkDistribution<String> nextDist = gson.fromJson(distJson, ChunkDistribution.class);
		GenericArray<String> messagesJson = new GenericArray<>();

		if (!nextDist.isNull()) {

			if (rootHash != null && rootHash.length > 0) {
				boolean valid = Arrays.stream(messages).anyMatch((t) -> {
					// due to interface specification change
					// String merklePathJson = t.merklePathJson;
					byte[][] merklePath = t.merklePath;
					String[] merklePathIndex = t.merklePathIndex;

					String messageJson = t.json;

					// MerklePath mp = JSON.parseObject(merklePathJson, MerklePath.class);
					MerklePath mp = new MerklePath(merklePath, merklePathIndex);
					return mp.validate(Mapper.transformFrom(messageJson), rootHash);
				});

				if (!valid)
					throw new RuntimeException("failed in merkle tree validation");
			}

			if (messages != null && messages.length > 0) {
				Arrays.stream(messages).parallel().forEach(t -> {

					messagesJson.append(t.json);
				});

				return new DistributedObjects<ChunkDistribution<String>, String>(
						gson.fromJson(distJson, ChunkDistribution.class),
						messagesJson.toArray(new String[messagesJson.length()]));
			} else {
				return new DistributedObjects<ChunkDistribution<String>, String>(
						gson.fromJson(distJson, ChunkDistribution.class), null);
			}

		} else {
			return null;
		}

	}

	@Override
	public DistributedObjects<ChunkDistribution<String>, String> getNotInDistributionSysMessages(
			ChunkDistribution<String> dist) {
		Gson gson = new Gson();
		Addr preferred = addresses[0];
		Addr candidate = null;
		if (addresses.length > 1) {
			candidate = addresses[1];
		}

		CompletableFuture<DistributedSysMessageObjects> f = DataSynchronizationZerocInvoker
				.invokeGetNotInDistributionSysMessagesAsync(communicator, preferred.getIp(), preferred.getPort(),
						gson.toJson(dist));
		DistributedSysMessageObjects deo = null;
		try {
			deo = f.get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SyncException(e);
		}

		MerkleTreeizedSyncSysMessage[] sysMessages = deo.sysMessages;
		byte[] rootHash = deo.rootHash;
		String distJson = deo.distJson;
		ChunkDistribution<String> nextDist = gson.fromJson(distJson, ChunkDistribution.class);
		GenericArray<String> sysMessagesJson = new GenericArray<>();

		if (!nextDist.isNull()) {

			if (rootHash != null && rootHash.length > 0) {
				boolean valid = Arrays.stream(sysMessages).anyMatch((t) -> {
					// due to interface specification change
					// String merklePathJson = t.merklePathJson;
					byte[][] merklePath = t.merklePath;
					String[] merklePathIndex = t.merklePathIndex;

					String sysMessageJson = t.json;

					// MerklePath mp = JSON.parseObject(merklePathJson, MerklePath.class);
					MerklePath mp = new MerklePath(merklePath, merklePathIndex);
					return mp.validate(Mapper.transformFrom(sysMessageJson), rootHash);
				});

				if (!valid)
					throw new RuntimeException("failed in merkle tree validation");
			}

			if (sysMessages != null && sysMessages.length > 0) {
				Arrays.stream(sysMessages).parallel().forEach(t -> {

					sysMessagesJson.append(t.json);
				});

				return new DistributedObjects<ChunkDistribution<String>, String>(
						gson.fromJson(distJson, ChunkDistribution.class),
						sysMessagesJson.toArray(new String[sysMessagesJson.length()]));
			} else {
				return new DistributedObjects<ChunkDistribution<String>, String>(
						gson.fromJson(distJson, ChunkDistribution.class), null);
			}

		} else {
			return null;
		}

	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	public void setAddresses(String[] addresses) {
		GenericArray<Addr> addrs = new GenericArray<>();
		Arrays.stream(addresses).forEach(t -> {
			addrs.append(new Addr(t));
		});

		this.addresses = addrs.toArray(new Addr[addrs.length()]);
	}

}
