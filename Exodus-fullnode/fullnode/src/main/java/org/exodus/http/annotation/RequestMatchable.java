package org.exodus.http.annotation;

public interface RequestMatchable {
	boolean isMatched(RequestMapper mapper);
}
