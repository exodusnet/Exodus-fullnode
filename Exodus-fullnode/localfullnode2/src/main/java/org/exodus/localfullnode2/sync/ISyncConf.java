package org.exodus.localfullnode2.sync;

import com.zeroc.Ice.Communicator;

public interface ISyncConf {
	ISyncContext getDefaultContext();

	Communicator getCommunicator();

	String getSynchronizationInitializerClassName();

	String getSyncSourceProxyClassName();

	String[] getSynchronizationWorkClassNames();

	String[] getLFNHostList();

	String getSynchronizationNativeRunnerClassName();

}
