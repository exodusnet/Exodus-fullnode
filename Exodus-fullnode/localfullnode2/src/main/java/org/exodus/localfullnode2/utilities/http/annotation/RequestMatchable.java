package org.exodus.localfullnode2.utilities.http.annotation;

public interface RequestMatchable {
	boolean isMatched(RequestMapper mapper);
}
