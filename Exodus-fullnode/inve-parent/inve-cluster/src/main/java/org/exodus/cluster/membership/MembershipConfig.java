package org.exodus.cluster.membership;

import java.util.List;
import java.util.Map;

import org.exodus.transport.Address;

public interface MembershipConfig {

  List<Address> getSeedMembers();

  Map<String, String> getMetadata();

  int getSyncInterval();

  int getSyncTimeout();

  String getSyncGroup();

  int getPingInterval();

  int getSuspicionMult();

  String getMemberHost();

  Integer getMemberPort();

}
