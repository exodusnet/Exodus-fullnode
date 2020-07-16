package org.exodus.cluster.fdetector;

public interface FailureDetectorConfig {

  int getPingInterval();

  int getPingTimeout();

  int getPingReqMembers();

}
