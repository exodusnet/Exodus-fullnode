package org.exodus.localfullnode2.snapshot;

import org.exodus.bean.message.SnapshotMessage;
import org.exodus.bean.message.SnapshotPoint;
import org.exodus.core.EventBody;

import java.math.BigInteger;
import java.util.HashMap;

public interface DetectAndRepairSnapshotDataDependent {

    BigInteger getCurrSnapshotVersion();

    long getCreatorId();

    int getShardId();

    int getnValue();

    HashMap<BigInteger, String> getTreeRootMap();

    void removeTreeRootMap(BigInteger snapVersion);

    String getDbId();

    HashMap<BigInteger, SnapshotPoint> getSnapshotPointMap();

    void setSnapshotMessage(SnapshotMessage snapshotMessage);

    void putTreeRootMap(BigInteger snapVersion, String msgHashTreeRoot);

    void putSnapshotPointMap(BigInteger snapVersion, SnapshotPoint snapshotPoint);
}
