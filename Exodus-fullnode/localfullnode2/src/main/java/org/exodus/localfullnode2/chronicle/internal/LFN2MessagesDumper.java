package org.exodus.localfullnode2.chronicle.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.exodus.localfullnode2.dep.DepItemsManager;
import org.exodus.localfullnode2.store.rocks.INosql;
import org.exodus.localfullnode2.store.rocks.key.MessageIndexes;
import org.exodus.localfullnode2.sync.msg.MsgIntrospectorDependency;
import org.exodus.localfullnode2.sync.msg.MsgIntrospectorDependent;
import org.exodus.localfullnode2.utilities.GenericArray;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: LFN2MessagesDumper
 * @Description: to provide services to dump messages to chronicle
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Jan 8, 2020
 *
 */
public class LFN2MessagesDumper implements IChronicleDumper {
	private static final Logger logger = LoggerFactory.getLogger(LFN2MessagesDumper.class);

	@Override
	public String[] getMessageHashes() {
		MsgIntrospectorDependent dep = DepItemsManager.getInstance().getItemConcerned(MsgIntrospectorDependency.class);
		INosql nosql = dep.getNosql();
		Map<byte[], byte[]> m = nosql.startWith(MessageIndexes.getMessageHashPrefix().getBytes());

		GenericArray<String> array = new GenericArray<>();
		for (byte[] k : m.keySet()) {
			String h = MessageIndexes.getMessageHash(new String(k));
			array.append(h);

			logger.info("parsed hash : {}", h);
		}

		return array.toArray(new String[array.length()]);
	}

	@Override
	public String[] getSysMessageHashes() {
		MsgIntrospectorDependent dep = DepItemsManager.getInstance().getItemConcerned(MsgIntrospectorDependency.class);
		INosql nosql = dep.getNosql();
		Map<byte[], byte[]> m = nosql.startWith(MessageIndexes.getSysMessageTypeIdPrefix().getBytes());

		GenericArray<String> array = new GenericArray<>();
		for (byte[] k : m.keySet()) {
			String h = MessageIndexes.getSysMessageTypeId(new String(k));
			array.append(h);

			logger.info("parsed hash : {}", h);
		}

		return array.toArray(new String[array.length()]);
	}

	@Override
	public byte[][] getMessagStreamBy(String[] messageHashes) {
		MsgIntrospectorDependent dep = DepItemsManager.getInstance().getItemConcerned(MsgIntrospectorDependency.class);
		INosql nosql = dep.getNosql();
		byte[][] b = new byte[1][];
		List<byte[]> container = new ArrayList<byte[]>();

		if (messageHashes != null && messageHashes.length > 0) {
			for (String mHash : messageHashes) {
				byte[] messageBytes = nosql.get(mHash);

				if (messageBytes != null && messageBytes.length > 0) {
					container.add(messageBytes);
				}
			}
		}

		return container.toArray(b);
	}
}
