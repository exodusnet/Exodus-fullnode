package org.exodus.localfullnode2.snapshot;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.exodus.bean.message.SnapshotMessage;
import org.exodus.bean.message.SnapshotPoint;
import org.exodus.cfg.localfullnode.Config;
import org.exodus.utils.DSA;

public class HandleSnapshotPoint {
	private static final Logger logger = LoggerFactory.getLogger(HandleSnapshotPoint.class);

	private HandleSnapshotPointDependent dep;
	private BigInteger vers;

	public void handleSnapshotPointMessage(HandleSnapshotPointDependent dep, JSONObject msgObject) throws Exception {
		this.dep = dep;
		this.vers = dep.getCurrSnapshotVersion();

		logger.info(">>>>>START<<<<<handleSnapshotPointMessage:\n msgObject: {}", msgObject);

		Boolean lastIdx = msgObject.getBoolean("lastIdx");
		String eHash = msgObject.getString("eHash");
		SnapshotPoint sp = dep.getSnapshotPointMap().get(vers);

		logger.info(">>>>>INFO<<<<<handleSnapshotPointMessage:\n currSnapshotVersion: {},\n snapshotPoint: {}", vers,
				JSON.toJSONString(sp));
//        logger.warn("\nspecif msg: {}, \nvers-{} sp: {}",
//                msgObject.toJSONString(), vers, JSON.toJSONString(sp));
		if (sp == null) {
//            logger.error("node-({}, {}): snapshotPoint-{} missing\nexit...",
//                    node.getShardId(), node.getCreatorId(), vers);
			logger.error(">>>>>ERROR<<<<<handleSnapshotPointMessage:\n snapshotPoint is null");
			logger.error(">>>>>EXIT<<<<<handleSnapshotPointMessage");
			System.exit(-1);
		} else {
			if (null != lastIdx && lastIdx && eHash.equals(DSA.encryptBASE64(sp.getEventBody().getHash()))) {
				// 快照点event时，判断是否基金会节点，然后生成快照
				handleSnapshotPoint(msgObject.getBigInteger("id"));
			} else {
//                logger.warn("node-({}, {}): unknown message: {}",
//                        node.getShardId(), node.getCreatorId(), msgObject.toJSONString());
			}
		}
		logger.info(">>>>>END<<<<<handleSnapshotPointMessage");
	}

	private void handleSnapshotPoint(BigInteger maxMessageId) throws Exception {
		logger.info(">>>>>START<<<<<handleSnapshotPoint:\n maxMessageId: {}", maxMessageId);
		// 快照事件，则生成快照消息并丢入队列
		createSnapshotMessage(dep.getSnapshotPointMap().get(vers), maxMessageId);
		// 恢复参数状态
		dep.removeSnapshotPointMap(vers);
		dep.setTotalFeeBetween2Snapshots(BigInteger.ZERO);

//        logger.info("node-({}, {}): handle snapshotPoint-{} finished!", node.getShardId(), node.getCreatorId(), vers);
		vers = vers.add(BigInteger.ONE);
		logger.info(">>>>>END<<<<<handleSnapshotPoint");
	}

	private void createSnapshotMessage(SnapshotPoint snapshotPoint, BigInteger maxMessageId) throws Exception {
		logger.info(">>>>>START<<<<<createSnapshotMessage:\n snapshotPoint: {},\n maxMessageId:{}",
				JSON.toJSONString(snapshotPoint), maxMessageId);
		// 快照消息丢入队列
		logger.info(">>>>>INFO<<<<<createSnapshotMessage:\n pubKey: {}", dep.getPubKey());
		if (Config.FOUNDATION_PUBKEY.equals(dep.getPubKey())) {
			BigInteger totalFee = dep.getTotalFeeBetween2Snapshots();
			snapshotPoint.setMsgMaxId(maxMessageId);
			snapshotPoint.setTotalFee(totalFee);
			snapshotPoint.setRewardRatio(Config.NODE_REWARD_RATIO);
			// 构造快照消息
			SnapshotMessage snapshotMessage = new SnapshotMessage(dep.getMnemonic(), dep.getAddress(), vers,
					getPreHash(), snapshotPoint);

			logger.info(">>>>>INFO<<<<<createSnapshotMessage:\n snapshotMessage: {}",
					JSON.toJSONString(snapshotMessage));

			// 加入消息队列
			String msg = snapshotMessage.getMessage();
//            logger.info("node-({}, {}): new version-{}, snapshotMsg: {}",
//                    node.getShardId(), node.getCreatorId(), vers, msg);

			dep.getMessageQueue().add(JSON.parseObject(msg).getString("message").getBytes());
			logger.info(">>>>>INFO<<<<<createSnapshotMessage:\n messageQueue.add: {}",
					JSON.parseObject(msg).getString("message"));
		} else {
//            logger.warn("node-({}, {}): new version-{}, no permission!!",
//                    node.getShardId(), node.getCreatorId(), vers);
			logger.info(">>>>>INFO<<<<<createSnapshotMessage: no permission");
		}
		logger.info(">>>>>END<<<<<createSnapshotMessage");
	}

	private String getPreHash() {
		logger.info(">>>>>START<<<<<getPreHash");
		SnapshotMessage preSnapshotMessage = dep.getSnapshotMessage();
		String preHash;
		if (null == preSnapshotMessage) {
//            logger.warn("\n====== node-({}, {}): preSnapshotMessage: null", node.getShardId(), node.getCreatorId());
			preHash = null;
		} else if (null == preSnapshotMessage.getPreHash()) {
//            logger.warn(
//                    "\n====== node-({}, {}): preSnapshotMessage.version: {}, preSnapshotMessage.snapHash: {}, " +
//                    "preSnapshotMessage.getPreHash(): {}",
//                    node.getShardId(), node.getCreatorId(), preSnapshotMessage.getSnapVersion(),
//                    preSnapshotMessage.getHash(),
//                    preSnapshotMessage.getPreHash());
			preHash = preSnapshotMessage.getHash();
		} else {
			preHash = preSnapshotMessage.getHash();
		}
		logger.info(">>>>>RETURN<<<<<getPreHash:\n preHash: {}", preHash);
		return preHash;
	}

}
