package org.exodus.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Enqueue all them messages, in one queue for each type.
 * Like channels.
 */
public class MessageQueue {
    private static final Logger logger = LoggerFactory.getLogger("MessageQueue.class");

    public CancelableLinkedBlockingQueue leaderChangeM;
    public CancelableLinkedBlockingQueue initM;
    public CancelableLinkedBlockingQueue prevoteM;
    public CancelableLinkedBlockingQueue proposeM;
    public CancelableLinkedBlockingQueue voteM;

    public MessageQueue() {
        this.leaderChangeM = new CancelableLinkedBlockingQueue();
        this.initM = new CancelableLinkedBlockingQueue();
        this.prevoteM = new CancelableLinkedBlockingQueue();
        this.proposeM = new CancelableLinkedBlockingQueue();
        this.voteM = new CancelableLinkedBlockingQueue();
    }

    public void add(Message message){
        try {
            if (message instanceof ProposeMessage){
                proposeM.put(message);
                logger.info("put message into proposeM. current proposeM size: {}", proposeM.size());
            }
            if (message instanceof PrevoteMessage) {
                prevoteM.put(message);
                logger.info("put message into prevoteM. current prevoteM size: {}", prevoteM.size());
            }
            if (message instanceof VoteMessage){
                voteM.put(message);
                logger.info("put message into voteM. current voteM size: {}", voteM.size());
            }
        } catch (InterruptedException e) {
            logger.error("{}", e);
        }
    }

//    public static void cancelAll() {
//        initM.cancel();
//        leaderChangeM.cancel();
//        prevoteM.cancel();
//        proposeM.cancel();
//        voteM.cancel();
//    }
}


