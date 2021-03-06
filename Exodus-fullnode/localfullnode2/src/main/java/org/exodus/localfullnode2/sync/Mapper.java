package org.exodus.localfullnode2.sync;

import java.time.Instant;
import java.util.Arrays;

import org.exodus.core.EventBody;
import org.exodus.localfullnode2.sync.rpc.gen.SyncEvent;
import org.exodus.localfullnode2.utilities.GenericArray;
import org.exodus.localfullnode2.utilities.Hash;
import org.exodus.localfullnode2.utilities.merkle.INodeContent;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: Mapper
 * @Description: a helper class to deal with object properties transformation
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 27, 2019
 *
 */
public class Mapper {
	// destination-->source when inverse is true
	public static void copyProperties(EventBody source, SyncEvent destination, boolean inverse) {
		if (!inverse) {
			destination.shardId = source.getShardId();
			destination.selfId = source.getCreatorId();
			destination.selfSeq = source.getCreatorSeq();
			destination.otherId = source.getOtherId();
			destination.otherSeq = source.getOtherSeq();
			destination.messages = source.getTrans();
			destination.timeCreatedNano = source.getTimeCreated().getNano();
			destination.timeCreatedSecond = source.getTimeCreated().getEpochSecond();
			destination.sign = source.getSignature();
			destination.isFamous = source.isFamous();
			destination.hash = source.getHash();
			destination.generation = source.getGeneration();
			destination.consensusTimestampSecond = source.getConsTimestamp() == null ? -1
					: source.getConsTimestamp().getEpochSecond();
			destination.consensusTimestampNano = source.getConsTimestamp() == null ? -1
					: source.getConsTimestamp().getNano();
			destination.otherHash = source.getOtherHash();
			destination.parentHash = source.getParentHash();
		} else {
			source.setShardId(destination.shardId);
			source.setCreatorId(destination.selfId);
			source.setCreatorSeq(destination.selfSeq);
			source.setOtherId(destination.otherId);
			source.setOtherSeq(destination.otherSeq);
			source.setTrans(destination.messages);
			source.setTimeCreated(Instant.ofEpochSecond(destination.timeCreatedNano, destination.timeCreatedSecond));
			source.setSignature(destination.sign);
			source.setFamous(destination.isFamous);
			source.setHash(destination.hash);
			source.setGeneration(destination.generation);
			source.setConsTimestamp(
					Instant.ofEpochSecond(destination.timeCreatedSecond, destination.consensusTimestampNano));
			source.setOtherHash(destination.otherHash);
			source.setParentHash(destination.parentHash);

		}
	}

	public static INodeContent transformFrom(EventBody eb) {
		return new INodeContent() {

			@Override
			public byte[] hash() {
				return eb.getHash();
			}

			@Override
			public boolean equals(INodeContent content) {
				return Arrays.equals(eb.getHash(), content.hash());
			}

		};
	}

	public static INodeContent transformFrom(SyncEvent se) {
		EventBody target = new EventBody();
		copyProperties(target, se, true);
		return transformFrom(target);
	}

	public static INodeContent[] transformFromArray(GenericArray<EventBody> eventBodyArray) {
		GenericArray<INodeContent> nodeContentArray = new GenericArray<>();

		for (EventBody eb : eventBodyArray) {
			nodeContentArray.append(Mapper.transformFrom(eb));
		}

		return nodeContentArray.toArray(new INodeContent[nodeContentArray.length()]);
	}

	public static INodeContent[] transformFromStringArray(GenericArray<String> jsons) {
		GenericArray<INodeContent> nodeContentArray = new GenericArray<>();

		for (String json : jsons) {
			nodeContentArray.append(transformFrom(json));
		}

		return nodeContentArray.toArray(new INodeContent[nodeContentArray.length()]);
	}

	public static INodeContent transformFrom(String json) {
		return new INodeContent() {

			@Override
			public byte[] hash() {
				return Hash.hash(json);
			}

			@Override
			public boolean equals(INodeContent content) {
				return Arrays.equals(hash(), content.hash());
			}

		};
	}
}
