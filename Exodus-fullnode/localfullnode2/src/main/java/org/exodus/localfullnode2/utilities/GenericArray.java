package org.exodus.localfullnode2.utilities;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: GenericArray
 * @Description: the object represents basic type array because of generic type
 *               array initialization
 * @author Francis.Deng
 * @date Aug 20, 2019
 * 
 * @param <E>
 */
public class GenericArray<E> implements Iterable<E> {
	private Object[] elements;

	public void append(E... es) {
		elements = ArraysUtil.append(elements, es);
	}

	public int length() {
		if (elements == null)
			return 0;
		return elements.length;
	}

	public E get(int pos) {
		return (E) elements[pos];
	}

	public E last() {
		return (E) get(this.length() - 1);
	}

	// the ease of using for.each statement
	@Override
	public Iterator<E> iterator() {
		return new Iterator<E>() {
			private int position = 0;
			private final int limit = length();

			@Override
			public boolean hasNext() {
				return position < limit;
			}

			@Override
			public E next() {
				E e = get(position++);
				// return (E) get(position++);
				return e;
			}

		};
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (a.length < length())
			// Make a new array of a's runtime type, but my contents:
			return (T[]) Arrays.copyOf(elements, length(), a.getClass());
		System.arraycopy(elements, 0, a, 0, length());
		if (a.length > length())
			a[length()] = null;
		return a;
	}
}
