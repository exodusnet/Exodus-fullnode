package org.exodus.localfullnode2.utilities;

import org.junit.Test;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: GenericArrayTest
 * @Description: GenericArray test case
 * @author Francis.Deng
 * @date Aug 20, 2019
 *
 */
public class GenericArrayTest {

	@Test
	public void testAppendAndGet() {
		GenericArray<String> stringArray = new GenericArray<>();

		String[] ab = { "a", "b" };
		String[] cde = { "c", "d", "e" };
		stringArray.append(ab);
		System.out.println(stringArray.get(0));

		stringArray.append(cde);

		System.out.println(stringArray.get(4));
	}
}
