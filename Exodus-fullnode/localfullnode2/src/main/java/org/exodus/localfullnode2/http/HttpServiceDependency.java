package org.exodus.localfullnode2.http;

import org.exodus.localfullnode2.nodes.LocalFullNode1GeneralNode;
import org.exodus.localfullnode2.utilities.http.HttpServiceImplsDependent;

public class HttpServiceDependency implements HttpServiceImplsDependent {
	private LocalFullNode1GeneralNode node;

	public LocalFullNode1GeneralNode getNode() {
		return node;
	}

	public void setNode(LocalFullNode1GeneralNode node) {
		this.node = node;
	}

}
