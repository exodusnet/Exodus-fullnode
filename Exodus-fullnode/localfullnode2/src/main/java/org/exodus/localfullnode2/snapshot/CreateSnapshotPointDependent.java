package org.exodus.localfullnode2.snapshot;


import com.alibaba.fastjson.JSONObject;
import org.exodus.bean.message.Contribution;
import org.exodus.bean.message.SnapshotPoint;
import org.exodus.bean.node.LocalFullNode;
import org.exodus.core.EventBody;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public interface CreateSnapshotPointDependent{

    BigInteger getCurrSnapshotVersion();

    void setContributions(HashSet<Contribution> contributions);

    BigInteger getTotalConsEventCount();

    int getShardCount();

    int getnValue();

    HashSet<Contribution> getContributions();

    List<LocalFullNode> getLocalFullNodes();

    HashMap<BigInteger, SnapshotPoint> getSnapshotPointMap();

    HashMap<BigInteger, String> getTreeRootMap();

    BigInteger getConsMessageMaxId();

    BlockingQueue<JSONObject> getConsMessageVerifyQueue();

    long getCreatorId();

    int getShardId();

    String getMsgHashTreeRoot();

    void setMsgHashTreeRoot(String msgHashTreeRoot);

    void putTreeRootMap(BigInteger snapVersion, String msgHashTreeRoot);

    void putSnapshotPointMap(BigInteger snapVersion, SnapshotPoint snapshotPoint);

}
