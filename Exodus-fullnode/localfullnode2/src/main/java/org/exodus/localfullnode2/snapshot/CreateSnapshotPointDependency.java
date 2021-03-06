package org.exodus.localfullnode2.snapshot;

import com.alibaba.fastjson.JSONObject;
import org.exodus.bean.message.Contribution;
import org.exodus.bean.message.SnapshotPoint;
import org.exodus.bean.node.LocalFullNode;
import org.exodus.core.EventBody;
import org.exodus.localfullnode2.dep.DependentItem;
import org.exodus.localfullnode2.dep.DependentItemConcerned;
import org.exodus.localfullnode2.dep.items.*;
import org.exodus.localfullnode2.staging.StagingArea;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CreateSnapshotPointDependency implements CreateSnapshotPointDependent, DependentItemConcerned {

    private SS ss;
    private Stat stat;
    private ShardCount shardCount;
    private NValue nValue;
    private LocalFullNodes localFullNodes;
    private ShardId shardId;
    private CreatorId creatorId;
    private AllQueues allQueues;

    @Override
    public void update(DependentItem item) {
        set(this, item);
    }

    @Override
    public BigInteger getCurrSnapshotVersion() {
        return ss.getCurrSnapshotVersion();
    }

    @Override
    public void setContributions(HashSet<Contribution> contributions) {
        ss.setContributions(contributions);
    }

    @Override
    public BigInteger getTotalConsEventCount() {
        return stat.getTotalConsEventCount();
    }

    @Override
    public int getShardCount() {
        return shardCount.get();
    }

    @Override
    public int getnValue() {
        return nValue.get();
    }

    @Override
    public HashSet<Contribution> getContributions() {
        return ss.getContributions();
    }

    @Override
    public List<LocalFullNode> getLocalFullNodes() {
        return localFullNodes.get();
    }

    @Override
    public HashMap<BigInteger, SnapshotPoint> getSnapshotPointMap() {
        return ss.getSnapshotPointMap();
    }

    @Override
    public HashMap<BigInteger, String> getTreeRootMap() {
        return ss.getTreeRootMap();
    }

    @Override
    public BigInteger getConsMessageMaxId() {
        return stat.getConsMessageMaxId();
    }

    @Override
    public BlockingQueue<JSONObject> getConsMessageVerifyQueue() {
        return allQueues.get().getQueue(JSONObject.class, StagingArea.ConsMessageVerifyQueueName);
    }

    @Override
    public long getCreatorId() {
        return creatorId.get();
    }

    @Override
    public int getShardId() {
        return shardId.get();
    }

    @Override
    public String getMsgHashTreeRoot() {
        return ss.getMsgHashTreeRoot();
    }

    @Override
    public void setMsgHashTreeRoot(String msgHashTreeRoot) {
        ss.setMsgHashTreeRoot(msgHashTreeRoot);
    }

    @Override
    public void putTreeRootMap(BigInteger snapVersion, String msgHashTreeRoot) {
        ss.putTreeRootMap(snapVersion, msgHashTreeRoot);
    }

    @Override
    public void putSnapshotPointMap(BigInteger snapVersion, SnapshotPoint snapshotPoint) {
        ss.putSnapshotPointMap(snapVersion, snapshotPoint);
    }
}
