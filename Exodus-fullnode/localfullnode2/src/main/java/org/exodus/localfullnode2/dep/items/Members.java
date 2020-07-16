package org.exodus.localfullnode2.dep.items;

import java.util.Collections;
import java.util.List;

import org.exodus.cfg.localfullnode.Config;
import org.exodus.cluster.Member;
import org.exodus.localfullnode2.dep.DependentItem;

public class Members extends DependentItem {

	private List<Member> membersInSharding;
	private List<Member> membersGlobally;

	public List<Member> get(int inShardingOrGlobally) {
		return inShardingOrGlobally == Config.GOSSIP_GLOBAL_SHARD ? membersGlobally : membersInSharding;
	}

	/**
	 * introduce thread-safe list
	 */

	public void setInSharding(List<Member> members) {
		this.membersInSharding = Collections.synchronizedList(members);
		nodifyAll();
	}

	public void setGlobally(List<Member> members) {
		this.membersGlobally = Collections.synchronizedList(members);
		nodifyAll();
	}

}
