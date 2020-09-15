package org.exodus.reward.http;

import org.exodus.reward.RWDException;
import org.json.simple.JSONStreamAware;

final class ParameterException extends RWDException {

	private final JSONStreamAware errorResponse;

	ParameterException(JSONStreamAware errorResponse) {
		this.errorResponse = errorResponse;
	}

	JSONStreamAware getErrorResponse() {
		return errorResponse;
	}

}
