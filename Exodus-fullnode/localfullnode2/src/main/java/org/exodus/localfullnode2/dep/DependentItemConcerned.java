package org.exodus.localfullnode2.dep;

import java.lang.reflect.Field;

import org.exodus.localfullnode2.utilities.ReflectionUtils;

/**
 * 
 * 
 *  All rights reserved.
 * 
 * @Description: {@code DependentItem} and {@code DependentItemConcerned} models
 *               provide a convenient interaction among Dep object.
 * @author: Francis.Deng
 * @date: May 8, 2019 1:16:07 AM
 * @version: V1.0
 */
public interface DependentItemConcerned {
	void update(DependentItem item);

	/**
	 * inject item into subclass of {@link DependentItemConcerned} if the type is
	 * matched.
	 * 
	 * @param itemWatcher
	 * @param item
	 */
	default void set(DependentItemConcerned itemWatcher, DependentItem item) {
		Field f = ReflectionUtils.findField(itemWatcher.getClass(), item.getClass());

		if (f != null) {
			try {
				ReflectionUtils.setField(f, itemWatcher, item);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
