package org.exodus.http.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.exodus.bean.node.LocalFullNode;
import org.exodus.bean.node.NodeStatus;
import org.exodus.http.DataMap;
import org.exodus.http.annotation.MethodEnum;
import org.exodus.http.annotation.RequestMapper;
import org.exodus.node.GeneralNode;
import org.exodus.rpcImpl.fullnode.RegisterService;
import org.exodus.util.ResponseUtils;
import org.exodus.util.StringUtils;
import org.exodus.util.ValidateUtils;

/**
 * 对外http服务
 * 
 * @author Clare
 * @date 2018/11/17 0029.
 */
public class HttpApiService {
	private static Logger logger = LoggerFactory.getLogger(HttpApiService.class);

	GeneralNode node;

	public HttpApiService(GeneralNode node) {
		this.node = node;
	}

	/**
	 * 获取nrg price
	 * 
	 * @param data 参数
	 * @return nrg price
	 */
	@RequestMapper(value = "/v1/price/nrg", method = MethodEnum.POST)
	public String getNrgPrice(DataMap<String, Object> data) {
		logger.info("get local full node neighbor list ... data: {}", JSON.toJSONString(data));
		try {
			String priceInfo = RegisterService.getNrgPrice();
			return StringUtils.isEmpty(priceInfo) ? ResponseUtils.handleExceptionResponse()
					: ResponseUtils.normalResponse(priceInfo);
		} catch (Exception e) {
			logger.error("getLocalFullNodeList handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 查询局部全节点邻居列表
	 * 
	 * @param data -pubkey 请求者的公钥信息
	 * @return 处理结果 code: 200-成功, 其他-失败 data: 成功则为返回""或者节点列表, 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/getlocalfullnodes", method = MethodEnum.POST)
	public String getLocalFullNodeList(DataMap<String, Object> data) {
		logger.info("get local full node neighbor list ... data: {}", JSON.toJSONString(data));
		if (null == data || data.isEmpty()) {
			logger.error("parameter is empty.");
			return ResponseUtils.paramIllegalResponse();
		}
		String pubkey = data.getString("pubkey");
		if (StringUtils.isEmpty(pubkey)) {
			logger.error("parameter is empty.");
			return ResponseUtils.paramIllegalResponse();
		}
		try {
			long shardCount = node.getLocalFullNodeList().values().stream()
					.filter(n -> n.getStatus() == NodeStatus.HAS_SHARDED).map(LocalFullNode::getShard).distinct()
					.count();
			logger.warn("shardCount: {}", shardCount);
			if (shardCount > 0) {
				String shardId = (shardCount == 1) ? "0"
						: RegisterService.generateShardIdByPubKey((int) shardCount, pubkey);
				if (StringUtils.isEmpty(shardId) || Integer.parseInt(shardId) < 0
						|| Integer.parseInt(shardId) >= shardCount) {
					logger.error("Parameter shardId is illegal！shardId: {}", shardId);
					return ResponseUtils.paramIllegalResponse();
				}
				logger.warn("shardId: {}", shardId);
				List<LocalFullNode> baseNodes = RegisterService.queryLocalfullnodeNeighborList(shardId, node);

				// Francis.Deng 4/1/2019
				// temporal filter of lfns(TFL) due to unresolved issue happened at
				// 3.213.114.163:35793.
				baseNodes = baseNodes.parallelStream()
						.filter(node -> !(node.getIp().equals("3.213.114.163") && node.getHttpPort() == 35793))
						.collect(Collectors.toList());

				return ResponseUtils.normalResponse((baseNodes.size() <= 0) ? "" : JSONArray.toJSONString(baseNodes));
			} else {
				return ResponseUtils.normalResponse();
			}
		} catch (Exception e) {
			logger.error("getLocalFullNodeList handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 区块链浏览器获取localfullnode节点数量
	 * 
	 * @param data -pubkey 请求者的公钥信息
	 * @return 处理结果 code: 200-成功, 其他-失败
	 *
	 *         data: 成功则为返回节点数量, 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/getlocalfullnodesLength", method = MethodEnum.POST)
	public String getLocalFullNodeListLength(DataMap<String, Object> data) {
		logger.info("get local full node neighbor list ... data: {}", JSON.toJSONString(data));
		if (null == data || data.isEmpty()) {
			logger.error("parameter is empty.");
			return ResponseUtils.paramIllegalResponse();
		}
		String pubkey = data.getString("pubkey");
		if (StringUtils.isEmpty(pubkey)) {
			logger.error("parameter is empty.");
			return ResponseUtils.paramIllegalResponse();
		}
		try {
			List<LocalFullNode> list = RegisterService.queryLocalfullnodeList(node);
			int size = 0;
			if (list != null) {
				size = list.size();
			}
			return ResponseUtils.normalResponse(JSONArray.toJSONString(size));

		} catch (Exception e) {
			logger.error("getlocalfullnodesLength handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 注册中继节点
	 * 
	 * @param data - pubkey 申请者的公钥 - ip 申请者的IP地址 - port 申请者的对外服务端口 - data 其他数据
	 * @return 处理结果 code: 200-成功, 其他-失败 data: 成功则为"", 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/relay/register", method = MethodEnum.POST)
	public String registerRelayNode(DataMap<String, Object> data) {
		logger.info("register relay node ... data: {}", JSON.toJSONString(data));
		if (null == data || data.isEmpty()) {
			logger.error("parameter is empty.");
			return ResponseUtils.paramIllegalResponse();
		}

		String pubkey = data.getString("pubkey");
		String ip = data.getString("ip");
		String port = data.getString("port");
		String dataStr = data.getString("data");
		try {
			if (StringUtils.isEmpty(pubkey) || !ValidateUtils.isIp(ip) || !ValidateUtils.isPort(port)
					|| StringUtils.isEmpty(dataStr)) {
				logger.error("parameter pubkey or ip or port or data is illegal.");
				return ResponseUtils.paramIllegalResponse();
			}

			return RegisterService.registerRelayNode(pubkey, ip, Integer.parseInt(port), dataStr, node);
		} catch (Exception e) {
			logger.error("registerRelayNode handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 注销中继节点
	 * 
	 * @param data - pubkey 申请者的公钥
	 * @return 处理结果 code: 200-成功, 其他-失败 data: 成功则为"", 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/relay/logout", method = MethodEnum.POST)
	public String logoutRelayNode(DataMap<String, Object> data) {
		logger.info("log out relay node ... data: {}", JSON.toJSONString(data));
		if (null == data || data.isEmpty()) {
			logger.error("parameter is empty.");
			return ResponseUtils.paramIllegalResponse();
		}

		String pubkey = data.getString("pubkey");
		if (StringUtils.isEmpty(pubkey)) {
			logger.error("parameter is illegal.");
			return ResponseUtils.paramIllegalResponse();
		}
		try {

			return RegisterService.logoutRelayNode(pubkey, node);
		} catch (Exception e) {
			logger.error("registerRelayNode handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 查询中继节点列表
	 * 
	 * @param data 查询所需数据,默认为空
	 * @return 处理结果 code: 200-成功, 其他-失败 data: 成功则为""或者节点列表, 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/relay/list", method = MethodEnum.POST)
	public String queryRelayNodeList(DataMap<String, Object> data) {
		logger.info("get relay node list ... data: {}", JSON.toJSONString(data));
		String dataStr = "";
		if (null != data && !data.isEmpty()) {
			dataStr = data.getString("data");
		}

		try {
			// 处理
			return RegisterService.queryRelayNodeList(dataStr, node);
		} catch (Exception e) {
			logger.error("queryRelayNodeList handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 中继节点心跳更新
	 * 
	 * @param data 查询所需数据,默认为空
	 * @return 处理结果 code: 200-成功, 其他-失败 data: 成功则为"", 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/relay/alive", method = MethodEnum.POST)
	public String updateHeartbeatAlive(DataMap<String, Object> data) {
		logger.info("update heartbeat alive... data: {}", JSON.toJSONString(data));
		String pubkey = data.getString("pubkey");
		if (StringUtils.isEmpty(pubkey)) {
			logger.error("parameter is illegal.");
			return ResponseUtils.paramIllegalResponse();
		}

		try {
			return RegisterService.updateHeartbeatAlive(pubkey, node);
		} catch (Exception e) {
			logger.error("updateHeartbeatAlive handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 注册全节点
	 * 
	 * @param data - pubkey 申请者的公钥 - ip 申请者的IP地址 - port 申请者的对外服务端口 - data 其他数据
	 * @return 处理结果 code: 200-成功, 其他-失败 data: 成功则为"", 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/fullnode/register", method = MethodEnum.POST)
	public String registerFullNode(DataMap<String, Object> data) {
		logger.info("register full node ... data: {}", JSON.toJSONString(data));
		if (null == data || data.isEmpty()) {
			logger.error("parameter is empty.");
			return ResponseUtils.paramIllegalResponse();
		}

		String pubkey = data.getString("pubkey");
		String ip = data.getString("ip");
		String rpcPort = data.getString("rpcport");
		String httpPort = data.getString("httpport");
		String dataStr = data.getString("data");
		try {
			if (StringUtils.isEmpty(pubkey) || !ValidateUtils.isIp(ip) || !ValidateUtils.isPort(rpcPort)
					|| !ValidateUtils.isPort(httpPort)) {
				logger.error("parameter is illegal.");
				return ResponseUtils.paramIllegalResponse();
			}

			return RegisterService.registerFullNode(pubkey, ip, Integer.parseInt(rpcPort), Integer.parseInt(httpPort),
					dataStr, node);
		} catch (Exception e) {
			logger.error("registerFullNode handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 注销全节点
	 * 
	 * @param data - pubkey 申请者的公钥
	 * @return 处理结果 code: 200-成功, 其他-失败 data: 成功则为"", 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/fullnode/logout", method = MethodEnum.POST)
	public String logoutFullNode(DataMap<String, Object> data) {
		logger.info("log out full node ... data: {}", JSON.toJSONString(data));
		if (null == data || data.isEmpty()) {
			logger.error("parameter is empty.");
			return ResponseUtils.paramIllegalResponse();
		}

		String pubkey = data.getString("pubkey");
		if (StringUtils.isEmpty(pubkey)) {
			logger.error("parameter is illegal.");
			return ResponseUtils.paramIllegalResponse();
		}
		try {

			return RegisterService.logoutFullNode(pubkey, node);
		} catch (Exception e) {
			logger.error("logoutFullNode handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 查询全节点列表
	 * 
	 * @param data 查询所需数据,默认为空
	 * @return 处理结果 code: 200-成功, 其他-失败 data: 成功则为""或者节点列表, 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/fullnode/list", method = MethodEnum.POST)
	public String queryFullNodeList(DataMap<String, Object> data) {
		logger.info("get full node list ... data: {}", JSON.toJSONString(data));
		String dataStr = "";
		if (null != data && !data.isEmpty()) {
			dataStr = data.getString("data");
		}

		try {
			// 处理
			return RegisterService.queryFullNodeList(dataStr, node);
		} catch (Exception e) {
			logger.error("queryFullNodeList handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}

	/**
	 * 更新版本
	 * 
	 * @param data 版本更新需要的数据
	 * @return 处理结果 code: 200-成功, 其他-失败 data: 成功则为"", 否则返回失败错误提示
	 */
	@RequestMapper(value = "/v1/version/update", method = MethodEnum.POST)
	public String updateVersion(DataMap<String, Object> data) {
		logger.info("update version... data: {}", JSON.toJSONString(data));
		if (null == data || data.isEmpty()) {
			logger.error("parameter is empty.");
			return ResponseUtils.paramIllegalResponse();
		}

		String pubkey = data.getString("pubkey");
		String version = data.getString("version");
		try {
			if (StringUtils.isEmpty(pubkey) || StringUtils.isEmpty(version)) {
				logger.error("parameter is illegal.");
				return ResponseUtils.paramIllegalResponse();
			}
			// 处理
			return RegisterService.updateVersion(pubkey, version, node);
		} catch (Exception e) {
			logger.error("updateVersion handle error: {}", e);
			return ResponseUtils.handleExceptionResponse();
		}
	}
}
