package org.exodus.localfullnode2.sync.partofwork;

import org.exodus.cfg.localfullnode.Config;
import org.exodus.localfullnode2.store.mysql.MysqlHelper;
import org.exodus.localfullnode2.store.mysql.NewTableCreate;
import org.exodus.localfullnode2.sync.ISyncContext;
import org.exodus.localfullnode2.sync.SyncWorksInLab.SynchronizationWorkInitial;
import org.exodus.localfullnode2.sync.source.ILFN2Profile;
import org.exodus.localfullnode2.sync.source.ISyncSource;

/**
 * 
 *  All rights reserved.
 * 
 * @ClassName: SyncInitializer
 * @Description: initialize mysql tables for messages as well as retrieving
 *               metadata of lfn2
 * @author Francis.Deng [francis_xiiiv@163.com]
 * @date Aug 24, 2019
 *
 */
public class SynchronizationWorkInitializer implements SynchronizationWorkInitial {

	@Override
	public boolean run(ISyncContext context) {
		// ISyncConf conf = context.getConf();
		ISyncSource synSource = context.getSyncSourceProxy();
		ILFN2Profile profile = synSource.getProfile(context);

		MysqlHelper mysqlHelper = new MysqlHelper(profile.getDBId(), 1 == 1);

		NewTableCreate.createMessagesTable(mysqlHelper, Config.MESSAGES + "_0");// messages table
		NewTableCreate.createTransactionsMsgTable(mysqlHelper, Config.SYSTEMAUTOTX + Config.SPLIT + "0");// system
																											// messages
																											// table

		return true;
	}

}
