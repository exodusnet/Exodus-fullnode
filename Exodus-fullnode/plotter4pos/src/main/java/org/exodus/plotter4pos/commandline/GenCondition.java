package org.exodus.plotter4pos.commandline;

public class GenCondition {
	// @Param("address")
	// private String address;
	@Param("startNonce")
	private String startNonce;
	@Param("plots")
	private String plots;
	@Param("staggeramt")
	private String staggeramt;

	// for testing
	public GenCondition(String startNonce, String plots, String staggeramt) {
		super();
		// this.address = address;
		this.startNonce = startNonce;
		this.plots = plots;
		this.staggeramt = staggeramt;
	}

	public GenCondition() {
	}

//	public String getAddress() {
//		return address;
//	}

	public String getStartNonce() {
		return startNonce;
	}

	public String getPlots() {
		return plots;
	}

	public String getStaggeramt() {
		return staggeramt;
	}

}
