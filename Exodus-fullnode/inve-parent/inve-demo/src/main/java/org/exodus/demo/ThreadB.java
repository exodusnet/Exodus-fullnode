package org.exodus.demo;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;

public class ThreadB extends Thread {
//	Signal signal;
	Cluster node01=null;
//	public ThreadB(Signal signal) {
//		this.signal = signal;
//	}
	@Override
	public void  run() {
		//顶层全节点
				ClusterConfig seedConfig = ClusterConfig.builder()
				        .port(20000)
				        .build();
				Cluster seed = Cluster.joinAwait(seedConfig);
				
				//局部全节点
				ClusterConfig NodeConfig01 = ClusterConfig.builder()
						.seedMembers(seed.address()) 
//						.memberHost("52.78.42.112")
						.addMetadata("shard","-----------------------------")
//				        .port(Integer.parseInt(args[2]))
						.port(15000)
				        .portAutoIncrement(false)
				        .build();
				node01 = Cluster.joinAwait(NodeConfig01);
				
//				signal.setHasDataToProcess(true);
	}
	 

}
