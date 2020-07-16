package org.exodus.demo;

import static java.util.stream.Collectors.joining;

import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;
import org.exodus.transport.Address;

public class LocalFullNode {

	public static void main(String[] args) throws InterruptedException {
		
		ClusterConfig configNode01 = ClusterConfig.builder()
			    .seedMembers(Address.create(args[0], Integer.parseInt(args[1])))
			    .metadata(ImmutableMap.of("name", "localfullnode01","shard",args[2]))
			    .memberHost(args[3])
			    .port(Integer.parseInt(args[4]))
	    		.syncGroup(args[5])
	    		.build();
	   Cluster node01 = Cluster.joinAwait(configNode01);
	   while (true) {
		   Thread.sleep(2000);
		    
		   System.out.println("local full node (" + node01.address() + ") cluster: "
			        + node01.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
	   }


	}

}
