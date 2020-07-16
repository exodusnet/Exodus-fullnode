package org.exodus.contract.ethplugin.invocation;

import org.exodus.contract.ethplugin.core.Repository;
import org.exodus.contract.ethplugin.db.DbFlushManager;

/**
 * 
 * Copyright © CHXX Co.,Ltd. All rights reserved.
 * 
 * @Description:
 * @author: Francis.Deng
 * @date: Nov 15, 2018 11:50:17 PM
 * @version: V1.0
 */
public interface IConstructor {
	Repository getRepository();

	DbFlushManager getDbFlushManager();

	Appendable getAppendedBuf();
}
