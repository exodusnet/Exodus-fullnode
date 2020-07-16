package org.exodus.demo;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.transport.Address;

public class ThreadC extends Thread {
//	Signal signal;
	Cluster node01=null;
//	public ThreadC(Signal signal) {
//		this.signal = signal;
//	}
	@Override
	public void  run() {
		 
				//局部全节点
				ClusterConfig NodeConfig01 = ClusterConfig.builder()
						.seedMembers(Address.create("127.0.1.1", 20000)) 
//						.memberHost("52.78.42.112")
						.addMetadata("shard","-----------------------------")
//				        .port(Integer.parseInt(args[2]))
						.port(15001)
				        .portAutoIncrement(false)
				        .build();
				node01 = Cluster.joinAwait(NodeConfig01);
				
//				signal.setHasDataToProcess(true);
	}
	 

}
