package org.exodus.demo;

public class Signal {
	private boolean hasDataTOProcess;
	
	public synchronized void setHasDataToProcess(boolean hasData) {
		this.hasDataTOProcess = hasData;
		
	}
	
	public synchronized boolean hasDataToProcess() {
		return this.hasDataTOProcess;
	}

}
