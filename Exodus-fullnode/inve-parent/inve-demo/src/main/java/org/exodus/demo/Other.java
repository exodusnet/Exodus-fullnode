package org.exodus.demo;

import static java.util.stream.Collectors.joining;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;
import org.exodus.transport.Address;

public class Other {
    
	public static void main(String[] args) {
		//局部全节点
				ClusterConfig NodeConfig01 = ClusterConfig.builder()
						.seedMembers(Address.create("127.0.1.1", 20000)) 
//						.memberHost("52.78.42.112")
						.addMetadata("shard","-----------------------------")
//				        .port(Integer.parseInt(args[2]))
						.port(Integer.parseInt(args[0]))
				        .portAutoIncrement(false)
				        .build();
				Cluster node01 = Cluster.joinAwait(NodeConfig01);
				
				while(true) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						 
						e.printStackTrace();
					}
					System.out.println("Node01 (" + node01.address() + ") cluster: "
					        + node01.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
					
				}

	}

}
