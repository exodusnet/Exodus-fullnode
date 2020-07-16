package org.exodus.demo;

import static java.util.stream.Collectors.joining;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;
import org.exodus.transport.Address;

public class DoubleLayerGossip {
	
	 public static void main(String[] args) throws Exception {
		 
		 ClusterConfig configSubSuperSeed01 = ClusterConfig.builder()
					.seedMembers(Address.create("127.0.1.1", 10000))
		    		.metadata(ImmutableMap.of("shard", "01","metadata",""))
		    		
		    		.syncGroup("9")
		    		.port(10006)
		    		.build();
		   Cluster subSuperSeed01 = Cluster.joinAwait(configSubSuperSeed01);
		   
		   ClusterConfig configSubSuperSeed02 = ClusterConfig.builder()
				    .seedMembers(Address.create("127.0.1.1", 10000))
		    		.metadata(ImmutableMap.of("shard", "02","metadata",""))
		    		.syncGroup("9")
		    		.port(10002)
		    		.build();
		   Cluster subSuperSeed02 = Cluster.joinAwait(configSubSuperSeed02);
		   
		   
		   ClusterConfig configSubSuperSeed03 = ClusterConfig.builder()
				    .seedMembers(Address.create("127.0.1.1", 10000))
		    		.metadata(ImmutableMap.of("shard", "03","metadata",""))
		    		.syncGroup("9")
		    		.port(10008)
		    		.build();
		   Cluster subSuperSeed03 = Cluster.joinAwait(configSubSuperSeed03);
		   
		   
		   ClusterConfig configSubSuperSeed04= ClusterConfig.builder()
				    .seedMembers(Address.create("127.0.1.1", 10000))
		    		.metadata(ImmutableMap.of("shard", "04","metadata",""))
		    		.syncGroup("9")
		    		.port(10004)
		    		.build();
		   Cluster subSuperSeed04 = Cluster.joinAwait(configSubSuperSeed04);
		 
		   //top layer
		   ClusterConfig configSubSeed01 = ClusterConfig.builder()
		    		.metadata(ImmutableMap.of("name", "subSeed01"))
		    		.port(20001)
		    		.syncGroup("01")
		    		.build();
		   Cluster subSeed01 = Cluster.joinAwait(configSubSeed01);
		   
		   ClusterConfig configSubSeed02 = ClusterConfig.builder()
		    		.metadata(ImmutableMap.of("name", "subSeed02"))
		    		.port(20002)
		    		.syncGroup("02")
		    		.build();
		   Cluster subSeed02 = Cluster.joinAwait(configSubSeed02);
		   
		   ClusterConfig configSubSeed03 = ClusterConfig.builder()
		    		.metadata(ImmutableMap.of("name", "subSeed03"))
		    		.port(20003)
		    		.syncGroup("03")
		    		.build();
		   Cluster subSeed03 = Cluster.joinAwait(configSubSeed03);
		   
		   //end top layer
		   
		   //localfullnode
		   ClusterConfig configNode01 = ClusterConfig.builder()
				    .seedMembers(subSeed01.address())
				    .metadata(ImmutableMap.of("name", "localfullnode01","shard","01"))
				    .port(30001)
		    		.syncGroup("01")
		    		.build();
		   Cluster node01 = Cluster.joinAwait(configNode01);
		   
		   ClusterConfig configNode02 = ClusterConfig.builder()
				    .seedMembers(subSeed01.address())
				    .metadata(ImmutableMap.of("name", "localfullnode02","shard","01"))
				    .port(30002)
		    		.syncGroup("01")
		    		.build();
		   Cluster node02 = Cluster.joinAwait(configNode02);
		   
		  
		   ClusterConfig configNode03 = ClusterConfig.builder()
				    .seedMembers(subSeed02.address())
				    .metadata(ImmutableMap.of("name", "localfullnode03","shard","02"))
				    .port(30003)
		    		.syncGroup("02")
		    		.build();
		   Cluster node03 = Cluster.joinAwait(configNode03);
		  
		   ClusterConfig configNode04 = ClusterConfig.builder()
				    .seedMembers(subSeed03.address())
				    .metadata(ImmutableMap.of("name", "localfullnode04","shard","03"))
				    .port(30004)
		    		.syncGroup("03")
		    		.build();
		   Cluster node04 = Cluster.joinAwait(configNode04);
		   
		   ClusterConfig configNode05 = ClusterConfig.builder()
				    .seedMembers(subSeed03.address())
				    .metadata(ImmutableMap.of("name", "localfullnode05","shard","02"))
				    .port(30005)
		    		.syncGroup("03")
		    		.build();
		   Cluster node05 = Cluster.joinAwait(configNode05);
		    
		   while (true) {
			   
		     Thread.sleep(2000);
//		       Collections.shuffle((List<?>) subSeed01.members());
			   subSuperSeed01.updateMetadataProperty("metadata" ,subSeed01.members().stream()
			    		 .map(Object -> Object.address().toString())
			    		 .limit(1)
			    		 .collect(Collectors.joining(",")));
//			   Collections.shuffle((List<?>) subSeed02.members());
			   subSuperSeed02.updateMetadataProperty("metadata", subSeed02.members().stream()
					     .map(Object -> Object.address().toString())
			    		 .limit(2)
			    		 .collect(Collectors.joining(",")));
//			   Collections.shuffle((List<?>) subSeed03.members());
			   subSuperSeed03.updateMetadataProperty("metadata", subSeed03.members().stream()
			    		 .map(Object -> Object.address().toString())
			    		 .limit(2)
			    		 .collect(Collectors.joining(",")));
			  
		    System.out.println("localfullnode01 (" + node01.address() + ") cluster: "
		        + node01.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));

//		    System.out.println("subSuperSeed01 (" + subSuperSeed01.address() + ") cluster: "
//		        + subSuperSeed01.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));

//		    System.out.println("localfullnode03 (" + node03.address() + ") cluster: "
//		        + node03.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//		    
//		    System.out.println("localfullnode04 (" + node04.address() + ") cluster: "
//			        + node04.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//
//
//		    System.out.println("top01 (" + top01.address() + ") cluster: "
//		        + top01.findMembersByShardId("01").stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//		    
//		    System.out.println("top02 (" + top02.address() + ") cluster: "
//			        + top02.findMembersByShardId("02").stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//
//		    System.out.println("kuazu (" + top01.address() + ") cluster: " // alone in cluster
//		        + top01.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
//		    
		 
			}
			    
		   
		  }

}
