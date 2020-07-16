package org.exodus.bean.message;

import java.util.Objects;

/**
 * 节点奖励统计用的贡献信息
 * @author Clare
 * @date   2018/8/31 0031.
 */
public class Contribution {
    private int shardId;
    private long creatorId;
    private long otherId;
    private long otherSeq;

    public Contribution() {
    }

    private Contribution(Builder builder) {
        setShardId(builder.shardId);
        setCreatorId(builder.creatorId);
        setOtherId(builder.otherId);
        setOtherSeq(builder.otherSeq);
    }

    public int getShardId() {
        return shardId;
    }

    public void setShardId(int shardId) {
        this.shardId = shardId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getOtherId() {
        return otherId;
    }

    public void setOtherId(long otherId) {
        this.otherId = otherId;
    }

    public long getOtherSeq() {
        return otherSeq;
    }

    public void setOtherSeq(long otherSeq) {
        this.otherSeq = otherSeq;
    }


    public static final class Builder {
        private int shardId;
        private long creatorId;
        private long otherId;
        private long otherSeq;

        public Builder() {
        }

        public Builder shardId(int val) {
            shardId = val;
            return this;
        }

        public Builder creatorId(long val) {
            creatorId = val;
            return this;
        }

        public Builder otherId(long val) {
            otherId = val;
            return this;
        }

        public Builder otherSeq(long val) {
            otherSeq = val;
            return this;
        }

        public Contribution build() {
            return new Contribution(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contribution that = (Contribution) o;
        return hashCode() == that.hashCode() &&
                shardId == that.shardId &&
                creatorId == that.creatorId &&
                otherId == that.otherId &&
                otherSeq == that.otherSeq;
    }

    @Override
    public int hashCode() {
        return Objects.hash(shardId, creatorId, otherId, otherSeq);
    }
}
