package org.exodus.cluster.gossip;

public interface GossipConfig {

  int getGossipFanout();

  long getGossipInterval();

  int getGossipRepeatMult();

}
