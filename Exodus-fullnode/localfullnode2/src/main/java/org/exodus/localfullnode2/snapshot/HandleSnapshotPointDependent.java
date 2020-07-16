package org.exodus.localfullnode2.snapshot;

import com.alibaba.fastjson.JSONObject;
import org.exodus.bean.message.SnapshotMessage;
import org.exodus.bean.message.SnapshotPoint;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface HandleSnapshotPointDependent {

    BigInteger getCurrSnapshotVersion();

    HashMap<BigInteger, SnapshotPoint> getSnapshotPointMap();

    void setTotalFeeBetween2Snapshots(BigInteger totalFeeBetween2Snapshots);

    SnapshotMessage getSnapshotMessage();

    BigInteger getTotalFeeBetween2Snapshots();

    String getPubKey();

    String getMnemonic();

    String getAddress();

    BlockingQueue<byte[]> getMessageQueue();

    void removeSnapshotPointMap(BigInteger snapVersion);

}
