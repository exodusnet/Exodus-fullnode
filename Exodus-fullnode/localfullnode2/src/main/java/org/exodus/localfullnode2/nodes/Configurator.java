package org.exodus.localfullnode2.nodes;

import com.zeroc.Ice.Util;

import org.exodus.cfg.core.DBConnectionDescriptorsConf;
import org.exodus.cfg.core.IConfImplant;
import org.exodus.cfg.core.InterValueConfImplant;

/**
 * 
 * 
 *  All rights reserved.
 * 
 * @Description: load a yaml configuration instead of cmd line parameters.
 * @author: Francis.Deng [francis_xiiiv@163.com]
 * @date: Sep 15, 2019 7:12:18 PM
 * @version: V1.0
 */
public abstract class Configurator extends HashneterInitializer {
	protected IConfImplant loadConfObject(String[] args) {
		InterValueConfImplant implant = new InterValueConfImplant();
		implant.init(args);

		return implant;
	}

	protected DBConnectionDescriptorsConf loadConf(String[] args) {
		IConfImplant implant = loadConfObject(args);
		implant.implantStaticConfig();
		implant.implantEnv();
		setCommunicator(Util.initialize(implant.implantZerocConf()));
		nodeParameters(implant.implantNodeParameters());

		return implant.getDbConnection();
	}
}
