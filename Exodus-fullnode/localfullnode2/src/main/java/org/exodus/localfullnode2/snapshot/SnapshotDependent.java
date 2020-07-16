package org.exodus.localfullnode2.snapshot;

import com.alibaba.fastjson.JSONObject;
import org.exodus.bean.message.Contribution;
import org.exodus.bean.message.SnapshotMessage;
import org.exodus.bean.message.SnapshotPoint;
import org.exodus.bean.node.LocalFullNode;
import org.exodus.core.EventBody;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public interface SnapshotDependent {

    HashSet<Contribution> getContributions();
    void setContributions(HashSet<Contribution> contributions);

    SnapshotMessage getSnapshotMessage();
    void setSnapshotMessage(SnapshotMessage snapshotMessage);

    BigInteger getSystemAutoTxMaxId();
    void setSystemAutoTxMaxId(BigInteger systemAutoTxMaxId);

    //与上一个快照点之间的交易手续费总额
    BigInteger getTotalFeeBetween2Snapshots();
    void setTotalFeeBetween2Snapshots(BigInteger totalFeeBetween2Snapshots);

    BigInteger getConsMessageMaxId();
    void setConsMessageMaxId(BigInteger consMessageMaxId);

    BigInteger getTotalConsEventCount();
    void setTotalConsEventCount(BigInteger totalConsEventCount);

    BigInteger getCurrSnapshotVersion();

    HashMap<BigInteger, SnapshotPoint> getSnapshotPointMap();

    long getCreatorId();

    int getShardId();

    int getnValue();

    HashMap<BigInteger, String> getTreeRootMap();

    int getShardCount();

    List<LocalFullNode> getLocalFullNodes();

    LinkedBlockingQueue<JSONObject> getConsMessageVerifyQueue();

    EventBody getEventBody();

    String getDbId();

    JSONObject getMsgObject();

    LinkedBlockingQueue<JSONObject> getSystemAutoTxSaveQueue();

    LinkedBlockingQueue<JSONObject> getConsMessageSaveQueue();

    void clearHistoryEventsBySnapshot(BigInteger vers, String preHash);

    boolean transfer(String dbId, String fromAddr, String toAddr, BigInteger value);

    String getPubKey();

    String getMnemonic();

    String getAddress();

    ConcurrentLinkedQueue<byte[]> getMessageQueue();

    LinkedBlockingQueue<EventBody> getShardSortQueue(int shardId);

}
