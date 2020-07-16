package org.exodus.demo;

import static java.util.stream.Collectors.joining;

import com.google.common.collect.ImmutableMap;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;
import org.exodus.transport.Address;

public class SubSeed {

	public static void main(String[] args) throws InterruptedException {
		ClusterConfig configSubSeed03 = ClusterConfig.builder()
				.seedMembers(Address.create("127.0.1.1", 20002))
	    		.metadata(ImmutableMap.of("name", "subSeed03"))
	    		.port(50003)
	    		.syncGroup("02")
	    		.build();
	   Cluster subSeed03 = Cluster.joinAwait(configSubSeed03);
	   
	   while (true) {
		   Thread.sleep(2000);
		   System.out.println("subSeed03 node (" + subSeed03.address() + ") cluster: "
			        + subSeed03.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
	   }
	   
	  
	}

}
