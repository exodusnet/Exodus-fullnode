package org.exodus.demo;

import static java.util.stream.Collectors.joining;

import com.google.common.collect.ImmutableMap;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;

public class SuperSeed {

	public static void main(String[] args) throws InterruptedException {
		ClusterConfig configSuperSeed = ClusterConfig.builder()
	    		.metadata(ImmutableMap.of("name", "super"))
//	    		.memberHost("52.78.139.203")
	    		.syncGroup("9")
	    		.port(10000)
	    		.build();
	   Cluster superSeed = Cluster.joinAwait(configSuperSeed);
	   
	   while (true) {
		   Thread.sleep(2000);
		   System.out.println("super seed node (" + superSeed.address() + ") cluster: "
			        + superSeed.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
	   }

	}

}
