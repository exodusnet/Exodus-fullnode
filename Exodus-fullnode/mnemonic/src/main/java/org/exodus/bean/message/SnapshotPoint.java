package org.exodus.bean.message;


import org.exodus.core.EventBody;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 快照点
 * @author Clare
 * @date   2018/9/7 0007.
 */
public class SnapshotPoint {
    /**
     * 快照点event
     */
    private EventBody eventBody;
    /**
     * 上一个快照点到当前快照点之间所有Event（含当前快照点event）打包的所有交易的hash构建的tree的root hash
     */
    private String msgHashTreeRoot;
    /**
     * 与上一个快照点之间的交易手续费总额
     */
    private BigInteger totalFee = BigInteger.ZERO;
    /**
     * 快照点event时最大messageId（含快照点内打包的消息）
     */
    private BigInteger msgMaxId = BigInteger.ZERO;
    /**
     * 所有柱子节点的有效Event数量(两个快照点之间每个柱子events的不同otherparent信息)
     */
    private ConcurrentHashMap<String, Long> contributions = null;
    /**
     * 手续费奖励分成比例
     */
    private double rewardRatio;

    public SnapshotPoint() {
    }

    private SnapshotPoint(Builder builder) {
        setEventBody(builder.eventBody);
        setMsgHashTreeRoot(builder.msgHashTreeRoot);
        setTotalFee(builder.totalFee);
        setMsgMaxId(builder.msgMaxId);
        setContributions(builder.contributions);
        setRewardRatio(builder.rewardRatio);
    }

    public EventBody getEventBody() {
        return eventBody;
    }

    public void setEventBody(EventBody eventBody) {
        this.eventBody = eventBody;
    }

    public String getMsgHashTreeRoot() {
        return msgHashTreeRoot;
    }

    public void setMsgHashTreeRoot(String msgHashTreeRoot) {
        this.msgHashTreeRoot = msgHashTreeRoot;
    }

    public BigInteger getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigInteger totalFee) {
        this.totalFee = totalFee;
    }

    public BigInteger getMsgMaxId() {
        return msgMaxId;
    }

    public void setMsgMaxId(BigInteger msgMaxId) {
        this.msgMaxId = msgMaxId;
    }

    public ConcurrentHashMap<String, Long> getContributions() {
        return contributions;
    }

    public void setContributions(ConcurrentHashMap<String, Long> contributions) {
        this.contributions = contributions;
    }

    public double getRewardRatio() {
        return rewardRatio;
    }

    public void setRewardRatio(double rewardRatio) {
        this.rewardRatio = rewardRatio;
    }


    public static final class Builder {
        private EventBody eventBody;
        private String msgHashTreeRoot;
        private BigInteger totalFee;
        private BigInteger msgMaxId;
        private ConcurrentHashMap<String, Long> contributions;
        private double rewardRatio;

        public Builder() {
        }

        public Builder eventBody(EventBody val) {
            eventBody = val;
            return this;
        }

        public Builder msgHashTreeRoot(String val) {
            msgHashTreeRoot = val;
            return this;
        }

        public Builder totalFee(BigInteger val) {
            totalFee = val;
            return this;
        }

        public Builder msgMaxId(BigInteger val) {
            msgMaxId = val;
            return this;
        }

        public Builder contributions(ConcurrentHashMap<String, Long> val) {
            contributions = val;
            return this;
        }

        public Builder rewardRatio(double val) {
            rewardRatio = val;
            return this;
        }

        public SnapshotPoint build() {
            return new SnapshotPoint(this);
        }
    }
}
