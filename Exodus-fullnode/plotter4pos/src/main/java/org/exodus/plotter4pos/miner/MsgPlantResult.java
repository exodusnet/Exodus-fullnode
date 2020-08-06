package org.exodus.plotter4pos.miner;

public class MsgPlantResult {
	public long generatedBytesAmount;
	public String address;

	public MsgPlantResult(String address, long generatedBytesAmount) {
		this.address = address;
		this.generatedBytesAmount = generatedBytesAmount;
	}
}
