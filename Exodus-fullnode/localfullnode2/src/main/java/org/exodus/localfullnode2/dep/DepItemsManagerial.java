package org.exodus.localfullnode2.dep;

import org.exodus.localfullnode2.dep.items.AllQueues;
import org.exodus.localfullnode2.dep.items.BlackList4PubKey;
import org.exodus.localfullnode2.dep.items.CreatorId;
import org.exodus.localfullnode2.dep.items.DBId;
import org.exodus.localfullnode2.dep.items.DirectCommunicator;
import org.exodus.localfullnode2.dep.items.EventFlow;
import org.exodus.localfullnode2.dep.items.FirstSeqs;
import org.exodus.localfullnode2.dep.items.LastSeqs;
import org.exodus.localfullnode2.dep.items.LocalFullNodes;
import org.exodus.localfullnode2.dep.items.Members;
import org.exodus.localfullnode2.dep.items.Mnemonic;
import org.exodus.localfullnode2.dep.items.NValue;
import org.exodus.localfullnode2.dep.items.PrivateKey;
import org.exodus.localfullnode2.dep.items.PublicKey;
import org.exodus.localfullnode2.dep.items.SS;
import org.exodus.localfullnode2.dep.items.ShardCount;
import org.exodus.localfullnode2.dep.items.ShardId;
import org.exodus.localfullnode2.dep.items.Stat;
import org.exodus.localfullnode2.dep.items.Wal;

/**
 * 
 * 
 *  All rights reserved.
 * 
 * @Description: item/item concerned model interface
 * @author: Francis.Deng
 * @date: May 8, 2019 2:44:15 AM
 * @version: V1.0
 */
public interface DepItemsManagerial {
	<T> T getItemConcerned(Class<T> itemConcernedClass);

	ShardId attachShardId(DependentItemConcerned... dependentItemConcerneds);

	ShardCount attachShardCount(DependentItemConcerned... dependentItemConcerneds);

	NValue attachNValue(DependentItemConcerned... dependentItemConcerneds);

	LocalFullNodes attachLocalFullNodes(DependentItemConcerned... dependentItemConcerneds);

	DBId attachDBId(DependentItemConcerned... dependentItemConcerneds);

	Mnemonic attachMnemonic(DependentItemConcerned... dependentItemConcerneds);

	PublicKey attachPublicKey(DependentItemConcerned... dependentItemConcerneds);

	Members attachMembers(DependentItemConcerned... dependentItemConcerneds);

	CreatorId attachCreatorId(DependentItemConcerned... dependentItemConcerneds);

	LastSeqs attachLastSeqs(DependentItemConcerned... dependentItemConcerneds);

//	CurrSnapshotVersion attachCurrSnapshotVersion(DependentItemConcerned... dependentItemConcerneds);

	EventFlow attachEventFlow(DependentItemConcerned... dependentItemConcerneds);

	BlackList4PubKey attachBlackList4PubKey(DependentItemConcerned... dependentItemConcerneds);

	PrivateKey attachPrivateKey(DependentItemConcerned... dependentItemConcerneds);

	AllQueues attachAllQueues(DependentItemConcerned... dependentItemConcerneds);

	DirectCommunicator attachDirectCommunicator(DependentItemConcerned... dependentItemConcerneds);

//	UpdatedSnapshotMessage attachUpdatedSnapshotMessage(DependentItemConcerned... dependentItemConcerneds);

	Stat attachStat(DependentItemConcerned... dependentItemConcerneds);

	SS attachSS(DependentItemConcerned... dependentItemConcerneds);

	Wal attachWal(DependentItemConcerned... dependentItemConcerneds);

	FirstSeqs attachFirstSeqs(DependentItemConcerned[] dependentItemConcerneds);
}
