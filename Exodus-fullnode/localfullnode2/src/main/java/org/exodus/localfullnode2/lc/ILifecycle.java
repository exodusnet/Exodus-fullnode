package org.exodus.localfullnode2.lc;

/**
 * 
 *  All rights reserved.
 * 
 * @Description: We see handling process,thread execution... as a life cycle
 *               which could been started,stopped,knowing running
 *               state{@code isRunning()}.
 * @author: Francis.Deng
 * @date: Jan 20, 2019 8:27:50 PM
 * @version: V1.0
 */
public interface ILifecycle {

	void start();

	void stop();

	boolean isRunning();

}
