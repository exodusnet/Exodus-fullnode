package org.exodus.contract.inve.vm.program.invoke;

import org.exodus.contract.ethplugin.core.Block;
import org.exodus.contract.ethplugin.core.Repository;
import org.exodus.contract.ethplugin.core.Transaction;
import org.exodus.contract.ethplugin.db.BlockStore;
import org.exodus.contract.ethplugin.vm.DataWord;
import org.exodus.contract.inve.vm.program.INVEProgram;

import java.math.BigInteger;

/**
 * @author Roman Mandeleil
 * @since 19.12.2014
 */
public interface INVEProgramInvokeFactory {

    INVEProgramInvoke createProgramInvoke(Transaction tx, Block block,
                                          Repository repository, BlockStore blockStore);

    INVEProgramInvoke createProgramInvoke(INVEProgram program, DataWord toAddress, DataWord callerAddress,
                                          DataWord inValue, DataWord inGas,
                                          BigInteger balanceInt, byte[] dataIn,
                                          Repository repository, BlockStore blockStore,
                                          boolean staticCall, boolean byTestingSuite);
}
