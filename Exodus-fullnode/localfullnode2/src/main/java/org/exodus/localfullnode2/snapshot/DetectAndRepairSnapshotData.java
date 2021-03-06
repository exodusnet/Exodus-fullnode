package org.exodus.localfullnode2.snapshot;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import org.exodus.bean.message.SnapshotMessage;
import org.exodus.cfg.localfullnode.Config;
import org.exodus.localfullnode2.store.EventKeyPair;
import org.exodus.localfullnode2.store.SnapshotDbService;
import org.exodus.localfullnode2.utilities.StringUtils;

public class DetectAndRepairSnapshotData {
	private static final Logger logger = LoggerFactory.getLogger(DetectAndRepairSnapshotData.class);

	private DetectAndRepairSnapshotDataDependent dep;
	private SnapshotDbService store;

	public void detectAndRepairSnapshotData(DetectAndRepairSnapshotDataDependent dep, SnapshotDbService store) {
		this.dep = dep;
		this.store = store;

		logger.info(">>>>>START<<<<<detectAndRepairSnapshotData");
		SnapshotMessage snapshotMessage = store.queryLatestSnapshotMessage(dep.getDbId());
		if (snapshotMessage != null) {
			dep.setSnapshotMessage(snapshotMessage);
			dep.putSnapshotPointMap(snapshotMessage.getSnapVersion(), snapshotMessage.getSnapshotPoint());
			dep.putTreeRootMap(snapshotMessage.getSnapVersion(),
					snapshotMessage.getSnapshotPoint().getMsgHashTreeRoot());

			EventKeyPair pair = new EventKeyPair(snapshotMessage.getSnapshotPoint().getEventBody().getShardId(),
					snapshotMessage.getSnapshotPoint().getEventBody().getCreatorId(),
					snapshotMessage.getSnapshotPoint().getEventBody().getCreatorSeq());
			logger.info(
					">>>>>INFO<<<<<detectAndRepairSnapshotData:\n snapshotMessage: {},\n snapshotPointMap: {},\n treeRootMap: {}",
					JSON.toJSONString(snapshotMessage), JSON.toJSONString(dep.getSnapshotPointMap()),
					JSON.toJSONString(dep.getTreeRootMap()));
		}
		// 删除之前DEFAULT_SNAPSHOT_CLEAR_GENERATION个版本的快照
		SnapshotMessage sm = snapshotMessage;
		if (null != sm && StringUtils.isNotEmpty(sm.getPreHash())) {
			clearHistoryEventsBySnapshot(sm.getSnapVersion(), sm.getPreHash());
		}

		for (int i = 0; i < Config.DEFAULT_SNAPSHOT_CLEAR_GENERATION; i++) {
			if (snapshotMessage != null && StringUtils.isNotEmpty(snapshotMessage.getPreHash())) {
				snapshotMessage = store.querySnapshotMessageByHash(dep.getDbId(), snapshotMessage.getPreHash());
				dep.putSnapshotPointMap(snapshotMessage.getSnapVersion(), snapshotMessage.getSnapshotPoint());
				dep.putTreeRootMap(snapshotMessage.getSnapVersion(),
						snapshotMessage.getSnapshotPoint().getMsgHashTreeRoot());

				logger.info(
						">>>>>INFO<<<<<detectAndRepairSnapshotData:\n snapshotMessage: {},\n snapshotPointMap: {},\n treeRootMap: {}",
						JSON.toJSONString(snapshotMessage), JSON.toJSONString(dep.getSnapshotPointMap()),
						JSON.toJSONString(dep.getTreeRootMap()));
			} else {
				logger.info(">>>>>BREAK<<<<<detectAndRepairSnapshotData");
				break;
			}
		}
		logger.info(">>>>>END<<<<<detectAndRepairSnapshotData");
	}

	private void clearHistoryEventsBySnapshot(BigInteger vers, String preHash) {
		logger.info(">>>>>START<<<<<clearHistoryEventsBySnapshot:\n vers: {},\n preHash: {}", vers, preHash);
		// 快照消息入库
		if (vers.compareTo(BigInteger.valueOf(Config.DEFAULT_SNAPSHOT_CLEAR_GENERATION)) > 0) {
			// 查询之前第 Config.DEFAULT_SNAPSHOT_CLEAR_GENERATION) 个快照
			int i = Config.DEFAULT_SNAPSHOT_CLEAR_GENERATION - 1;
			while (i > 0) {
				if (StringUtils.isEmpty(preHash)) {
					break;
				} else {
					SnapshotMessage sm = store.querySnapshotMessageByHash(dep.getDbId(), preHash);
					if (null == sm) {
						break;
					}
					preHash = sm.getPreHash();
					i--;
					if (i == 0) {
						// 删除其快照点Event之前的所有Event
						store.deleteEventsBeforeSnapshotPointEvent(dep.getDbId(), sm.getSnapshotPoint().getEventBody(),
								dep.getnValue());
						// 清除之前版本的treeRootMap
						dep.removeTreeRootMap(
								vers.subtract(BigInteger.valueOf(Config.DEFAULT_SNAPSHOT_CLEAR_GENERATION)));
					}
				}
			}
			if (logger.isDebugEnabled()) {
				// logger.debug("========= snapshot message version-{} delete events success.",
				// vers);
			}
		}
		logger.info(">>>>>END<<<<<clearHistoryEventsBySnapshot");
	}

}
