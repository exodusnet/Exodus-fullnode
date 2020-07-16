package org.exodus.localfullnode2.tools.cfgen;

public interface ICheatSheet {
	SeedQA getSeed();

	FullnodeQA getFullnode();

	Localfullnode2QA getLocalfullnode2();

	ClusterQA getCluster();

	P2pQA getP2p();
}
