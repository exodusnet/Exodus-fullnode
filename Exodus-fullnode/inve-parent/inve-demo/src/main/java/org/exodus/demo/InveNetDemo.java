package org.exodus.demo;

import static java.util.stream.Collectors.joining;

import javax.sound.sampled.Port;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;
import org.exodus.transport.Address;

public class InveNetDemo {

	public static void main(String[] args) throws InterruptedException {
		//顶层全节点
		ClusterConfig seedConfig = ClusterConfig.builder()
		        .port(20000)
		        .build();
		Cluster seed = Cluster.joinAwait(seedConfig);
		
		//局部全节点
		ClusterConfig NodeConfig01 = ClusterConfig.builder()
				.seedMembers(seed.address()) 
//				.memberHost("52.78.42.112")
				.addMetadata("shard","-----------------------------")
//		        .port(Integer.parseInt(args[2]))
				.port(Integer.parseInt(args[0]))
		        .portAutoIncrement(false)
		        .build();
		Cluster node01 = Cluster.joinAwait(NodeConfig01);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ClusterConfig NodeConfig02 = ClusterConfig.builder()
						.seedMembers(seed.address()) 
				        .port(Integer.parseInt(args[1]))
				        .addMetadata("shard","01") //01分片
				        .addMetadata("pubkey","xxxx") //公钥
				        .build();
				Cluster node02 = Cluster.joinAwait(NodeConfig02);
				while(true) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("node02 (" + node01.address() + ") cluster: "
					        + node02.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
					
				}
			}
		}).start();;
//		ClusterConfig NodeConfig02 = ClusterConfig.builder()
//				.seedMembers(seed.address())
//		        .listenAddress("192.168.1.205")
//		        .port(15002)
//		        .addMetadata("shard","01") //01分片
//		        .addMetadata("pubkey","xxxx") //公钥
//		        .build();
//		Cluster node02 = Cluster.joinAwait(NodeConfig02);
//		
//		
//		ClusterConfig NodeConfig03 = ClusterConfig.builder()
//				.seedMembers(seed.address())
//		        .listenAddress("192.168.1.205")
//		        .port(15003)
//		        .addMetadata("shard","02") //02分片
//		        .addMetadata("pubkey","xxxx") //公钥
//		        .build();
//		Cluster node03 = Cluster.joinAwait(NodeConfig03);
		while(true) {
			Thread.sleep(5000);
			System.out.println("Node01 (" + node01.address() + ") cluster: "
			        + node01.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
			
		}
		
//		System.out.println("Node02 (" + node02.address() + ") cluster: "
//		        + node02.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//		
//		System.out.println("Node03 (" + node03.address() + ") cluster: "
//		        + node03.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//		System.out.println("otherMembers接口返回不包含自身和种子节点的邻居列表 (" + node02.address() + ") cluster: "
//		        + node02.otherMembers().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//		
//		System.out.println("findMembersBySliceId接口返回指定片片内邻居不包含种子节点 (" + node03.address() + ") cluster: "
//		        + node03.findMembersByShardId("01").stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//		
		
		
	}

}
