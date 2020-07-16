package org.exodus.demo;

import static java.util.stream.Collectors.joining;

import com.google.common.collect.ImmutableMap;

import org.exodus.cluster.Cluster;
import org.exodus.cluster.ClusterConfig;
import org.exodus.cluster.Member;

import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Example how to create {@link Cluster} instances and join them to cluster.
 * 
 * @author Anton Kharenko
 */
public class ClusterJoinExamples {

  /**
   * Main method.
   */
  public static void main(String[] args) throws Exception {
    // Start seed member Alice
    Cluster alice = Cluster.joinAwait();
    

    // Join Bob to cluster with Alice
    Cluster bob = Cluster.joinAwait(alice.address());

    // Join Carol to cluster with metadata
    Map<String, String> metadata = ImmutableMap.of("name", "Carol", "group","01");
    Cluster carol = Cluster.joinAwait(metadata, alice.address());
    JSONObject object = new JSONObject();
    object.put("members", carol.otherMembers());
    System.out.println(carol.address().port());
    System.out.println(object.toString());
    WriteToJson.writer(carol.address().port(), object.toString());
    
    ClusterConfig configWithChangqing = ClusterConfig.builder()
    		.seedMembers(alice.address())
    		.metadata(ImmutableMap.of("name", "changqing"))
    		.syncGroup("02")
    		.port(1111)
    		.build();
    Cluster cqdeng = Cluster.joinAwait(configWithChangqing);

    // Start Dan on port 3000
    ClusterConfig configWithFixedPort = ClusterConfig.builder()
        .seedMembers(cqdeng.address())
        .metadata(ImmutableMap.of("group", "01"))
        .portAutoIncrement(false)
        .syncGroup("02")
        .port(3000)
        .build();
    Cluster dan = Cluster.joinAwait(configWithFixedPort);

    // Start Eve in separate cluster (separate sync group)
    ClusterConfig configWithSyncGroup = ClusterConfig.builder()
        .seedMembers(alice.address(), cqdeng.address(),bob.address()) // won't join anyway
        .syncGroup("02")
        .build();
    Cluster eve = Cluster.joinAwait(configWithSyncGroup);
    
    Thread.sleep(2000);
    // Print cluster members of each node

    System.out.println("Alice (" + alice.address() + ") cluster: "
        + alice.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));

    System.out.println("Bob (" + bob.address() + ") cluster: "
        + bob.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));

    System.out.println("Carol (" + carol.address() + ") cluster: "
        + carol.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));

    System.out.println("Dan (" + dan.address() + ") cluster: "
        + dan.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));

    System.out.println("Eve (" + eve.address() + ") cluster: " // alone in cluster
        + eve.members().stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
    
//    System.out.println("Eve (" + eve.address() + ") cluster: " // alone in cluster
//            + eve.getGroupById("01").stream().map(Member::toString).collect(joining("\n", "\n", "\n")));
     
  }

}
