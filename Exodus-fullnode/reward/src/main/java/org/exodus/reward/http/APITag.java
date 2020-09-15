package org.exodus.reward.http;

public enum APITag {
	HELLO("hello"), BagMaker("bagmaker"), TaiSai("TaiSai");

	private final String displayName;

	private APITag(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
