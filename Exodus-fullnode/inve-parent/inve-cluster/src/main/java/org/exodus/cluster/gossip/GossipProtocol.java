package org.exodus.cluster.gossip;

import rx.Observable;

import java.util.concurrent.CompletableFuture;

import org.exodus.transport.Message;

/**
 * Gossip Protocol component responsible for spreading information (gossips) over the cluster members using
 * infection-style dissemination algorithms. It provides reliable cross-cluster broadcast.
 *
 * @author Anton Kharenko
 */
public interface GossipProtocol {

  /**
   * Starts running gossip protocol. After started it begins to receive and send gossip messages
   */
  void start();

  /**
   * Stops running gossip protocol and releases occupied resources.
   */
  void stop();

  /**
   * Spreads given message between cluster members.
   * 
   * @return future result with gossip id once gossip fully spread.
   */
  CompletableFuture<String> spread(Message message);

  /**
   * Listens for gossips from other cluster members.
   */
  Observable<Message> listen();

}
