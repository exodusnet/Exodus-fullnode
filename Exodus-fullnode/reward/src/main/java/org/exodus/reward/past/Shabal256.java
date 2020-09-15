
package org.exodus.reward.past;

public class Shabal256 extends ShabalGeneric {

	/**
	 * Create the engine.
	 */
	public Shabal256() {
		super(256);
	}

	/** @see ShabalGeneric */
	ShabalGeneric dup() {
		return new Shabal256();
	}
}
