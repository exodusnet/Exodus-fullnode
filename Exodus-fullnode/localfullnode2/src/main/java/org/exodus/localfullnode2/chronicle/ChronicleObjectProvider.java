package org.exodus.localfullnode2.chronicle;

import org.exodus.localfullnode2.chronicle.internal.IChronicleDumper;
import org.exodus.localfullnode2.chronicle.internal.IChronicleDumperRestorer;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: ChronicleObjectProvider
 * @Description: to create interface template impls
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Jan 8, 2020
 *
 */
public class ChronicleObjectProvider {
	public static IChronicleDumperRestorer provideInternalDumperRestorer() {
		return null;
	}

	public static IChronicleDumper provideInternalDumper() {
		return null;
	}
}
