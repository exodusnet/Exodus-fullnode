package org.exodus.localfullnode2.gossip;

import java.math.BigInteger;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import com.zeroc.Ice.Communicator;

import org.exodus.cfg.localfullnode.Config;
import org.exodus.cluster.Member;
import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.AllQueues;
import org.exodus.localfullnode2.dep.items.BlackList4PubKey;
import org.exodus.localfullnode2.dep.items.CreatorId;
import org.exodus.localfullnode2.dep.items.DirectCommunicator;
import org.exodus.localfullnode2.dep.items.EventFlow;
import org.exodus.localfullnode2.dep.items.LastSeqs;
import org.exodus.localfullnode2.dep.items.Members;
import org.exodus.localfullnode2.dep.items.NValue;
import org.exodus.localfullnode2.dep.items.PrivateKey;
import org.exodus.localfullnode2.dep.items.PublicKey;
import org.exodus.localfullnode2.dep.items.SS;
import org.exodus.localfullnode2.dep.items.ShardCount;
import org.exodus.localfullnode2.dep.items.ShardId;
import org.exodus.localfullnode2.gossip.communicator.DefaultRpcCommunicator;
import org.exodus.localfullnode2.gossip.communicator.GossipCommunicationConsumable;
import org.exodus.localfullnode2.staging.StagingArea;
import org.exodus.localfullnode2.store.AtomicLongArrayWrapper;
import org.exodus.localfullnode2.store.IEventFlow;
import org.exodus.localfullnode2.utilities.HnKeyUtils;

/**
 * 
 *  All rights reserved.
 * 
 * @Description: delve into the source of {@code lastSeqs} and
 *               {@code currSnapshotVersion}
 * @author: Francis.Deng
 * @date: Oct 12, 2018 8:05:18 PM
 * @version: V1.0
 */
public class GossipDependency implements GossipDependent, DependentItemConcerned {

	private Members members;
	private ShardCount shardCount;
	private ShardId shardId;
	private CreatorId creatorId;
	private LastSeqs lastSeqs;
	private PublicKey publicKey;
	private SS ss;
//	private CurrSnapshotVersion currSnapshotVersion;
	private EventFlow eventFlow;
	private BlackList4PubKey blackList4PubKey;
	private PrivateKey privateKey;
	private GossipCommunicationConsumable rpcCommunicator = new DefaultRpcCommunicator();
	private DirectCommunicator directCommunicator;
	private AllQueues allQueues;

	private NValue nValue;

	@Override
	public List<Member> getMembers(int gossipType) {
		return members.get(gossipType);
	}

	@Override
	public Member getMember(int gossipType, int index) {
		return members.get(gossipType).get(index);
	}

	@Override
	public int getShardCount() {
		return shardCount.get();
	}

	@Override
	public int getShardId() {
		return shardId.get();
	}

	@Override
	public long getCreatorId() {
		return creatorId.get();
	}

	@Override
	public long[] getLastSeqsByShardId(int shardId) {
		AtomicLongArrayWrapper longArray = lastSeqs.get().get(new Integer(shardId));
		long[] ls = new long[longArray.length()];
		for (int i = 0; i < longArray.length(); i++) {
			ls[i] = longArray.get(i);
		}

		return ls;
	}

	@Override
	public long[][] getLastSeqs() {
		ConcurrentHashMap<Integer, AtomicLongArrayWrapper> allSeqs = lastSeqs.get();
		long[][] allLastSeqs = new long[allSeqs.size()][];
		for (Entry<Integer, AtomicLongArrayWrapper> s : allSeqs.entrySet()) {
			allLastSeqs[s.getKey().intValue()] = new long[s.getValue().length()];

			for (int i = 0; i < s.getValue().length(); i++) {
				allLastSeqs[s.getKey().intValue()][i] = s.getValue().get(i);
			}
		}

		return allLastSeqs;
	}

	@Override
	public String getPublicKey() {
		return HnKeyUtils.getString4PublicKey(publicKey.get());
	}

	@Override
	public BigInteger getCurrSnapshotVersion() {
		return ss.getCurrSnapshotVersion();
	}

	@Override
	public GossipCommunicationConsumable getGossipCommunication() {
		return rpcCommunicator;
	}

	// Config.GOSSIP_GLOBAL_SHARD nonsupport,hardcode the type.
	@Override
	public int getGossipType() {
		return Config.GOSSIP_IN_SHARD;
	}

	// ensuing that {@link StagingArea} created queue already.
	@Override
	public Queue<byte[]> getMessageQueue() {
		return allQueues.get().getQueue(byte[].class, StagingArea.MessageQueueName);
	}

	@Override
	public IEventFlow getEventFlow() {
		return eventFlow.get();
	}

	public void setEventFlow(IEventFlow evetnFlowImpl) {
		if (eventFlow == null)
			eventFlow = new EventFlow();

		eventFlow.set(evetnFlowImpl);
	}

	@Override
	public List<String> getBlackList4PubKey() {
		return blackList4PubKey.get();
	}

	@Override
	public java.security.PrivateKey getPrivateKey() {
		return privateKey.get();
	}

	@Override
	public void update(DependentItem item) {
		set(this, item);
	}

	@Override
	public Communicator getCommunicator() {
		return directCommunicator.get();
	}

	@Override
	public int getnValue() {
		return nValue.get();
	}

}
