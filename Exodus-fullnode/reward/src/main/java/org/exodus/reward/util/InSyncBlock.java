package org.exodus.reward.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "utils")
public class InSyncBlock {
	private final Object lock = new Object();

	public void awaitTermination() {
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				log.warn("{}", e);
				Thread.currentThread().interrupt();
			}
		}
	}

	public void terminate() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}
}
