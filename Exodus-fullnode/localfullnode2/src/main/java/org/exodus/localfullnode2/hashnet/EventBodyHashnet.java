package org.exodus.localfullnode2.hashnet;

import org.exodus.core.EventBody;
import org.exodus.localfullnode2.store.EventKeyPair;

/**
 * 
 *  All rights reserved.
 * 
 * @Description: support org.exodus.localfullnode2.store.EventBody involvement in
 *               conjunction with org.exodus.localfullnode2.hashnet.OpenHashnet
 * @author: Francis.Deng
 * @date: November 2, 2018 10:51:41 PM
 * @see org.exodus.localfullnode2.hashnet.OpenHashnet
 * @version: V1.0
 */
public class EventBodyHashnet {
	public synchronized boolean addEvent(OpenHashnet<EventBody> oh, EventBody eb) {
		return oh.addEvent(eb, (ec) -> {

			Event selfParent = oh.getEventByCreatorSeq(eb.getShardId(), eb.getCreatorId(), eb.getCreatorSeq() - 1);
			Event otherParent = oh.getEventByCreatorSeq(eb.getShardId(), eb.getOtherId(), eb.getOtherSeq());

			Event evt = new Event(eb.getShardId(), eb.getCreatorId(), eb.getCreatorSeq(), eb.getOtherId(),
					eb.getOtherSeq(), selfParent, otherParent, eb.getTimeCreated(), eb.getSignature(),
					eb.getGeneration(), eb.getHash(), eb.getTrans());

			EventKeyPair pair = new EventKeyPair(eb.getShardId(), eb.getCreatorId(), eb.getCreatorSeq());
			oh.eventsByKeypair().put(pair, evt);

			return evt;
		});
	}
}
