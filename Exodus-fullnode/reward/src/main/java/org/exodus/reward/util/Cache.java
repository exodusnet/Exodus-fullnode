package org.exodus.reward.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * Copyright © exodus. All rights reserved.
 * 
 * @ClassName: Cache
 * @Description: ttl cache
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Sep 16, 2020
 * 
 * @param <T>
 */
public class Cache<T> {
	long avg, count, created, max, min;
	Map<T, Long> map = new HashMap<T, Long>();

	/**
	 * @param min minimal time [ns] to hold an object
	 * @param max maximal time [ns] to hold an object
	 */
	public Cache(long min, long max) {
		created = System.nanoTime();
		this.min = min;
		this.max = max;
		avg = (min + max) / 2;
	}

	public boolean add(T e) {
		boolean result = map.put(e, Long.valueOf(System.nanoTime())) != null;
		onAccess();
		return result;
	}

	public boolean contains(T t) {
		boolean result = map.containsKey(t);
		onAccess();
		return result;
	}

	private void onAccess() {
		count++;
		long now = System.nanoTime();
		for (Iterator<Entry<T, Long>> it = map.entrySet().iterator(); it.hasNext();) {
			long t = it.next().getValue();
			if (now > t + min && (now > t + max || now + (now - created) / count > t + avg)) {
				it.remove();
			}
		}
	}

	public Set<T> all() {
		onAccess();
		return map.keySet();
	}
}
