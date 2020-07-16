package org.exodus.demo;

import static java.util.stream.Collectors.joining;

import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;
import org.exodus.transport.Address;

public class FullNode {

	public static void main(String[] args) throws InterruptedException {
		ClusterConfig configSubSuperSeed01 = ClusterConfig.builder()
				.seedMembers(Address.create("52.78.139.203", 10000))
	    		.metadata(ImmutableMap.of("shard", args[0],"metadata",""))
	    		.memberHost(args[1])
	    		.syncGroup("9")
	    		.port(Integer.parseInt(args[2]))
	    		.build();
	   Cluster subSuperSeed01 = Cluster.joinAwait(configSubSuperSeed01);
	   
	   ClusterConfig configSubSeed01 = ClusterConfig.builder()
	    		.metadata(ImmutableMap.of("name", "subSeed01"))
	    		.memberHost(args[1])
	    		.port(Integer.parseInt(args[3]))
	    		.syncGroup(args[4])
	    		.build();
	   Cluster subSeed01 = Cluster.joinAwait(configSubSeed01);
	   
	   while (true) {
		   Thread.sleep(2000);
		   subSuperSeed01.updateMetadataProperty("metadata" ,subSeed01.members().stream()
		    		 .map(Object -> Object.address().toString())
		    		 .collect(Collectors.joining(",")));
		   
		   System.out.println("full node (" + subSeed01.address() + ") cluster: "
			        + subSeed01.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
	   }

	}

}
