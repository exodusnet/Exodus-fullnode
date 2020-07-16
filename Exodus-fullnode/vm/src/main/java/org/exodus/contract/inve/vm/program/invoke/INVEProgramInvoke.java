package org.exodus.contract.inve.vm.program.invoke;

import org.exodus.contract.ethplugin.core.Repository;
import org.exodus.contract.ethplugin.db.BlockStore;
import org.exodus.contract.ethplugin.vm.DataWord;

/**
 * @author Roman Mandeleil
 * @since 03.06.2014
 */
public interface INVEProgramInvoke {

    DataWord getOwnerAddress();

    DataWord getBalance();

    DataWord getOriginAddress();

    DataWord getCallerAddress();

    DataWord getMinGasPrice();

    DataWord getGas();

    long getGasLong();

    DataWord getCallValue();

    DataWord getDataSize();

    DataWord getDataValue(DataWord indexData);

    byte[] getDataCopy(DataWord offsetData, DataWord lengthData);

    DataWord getTimestamp();

    DataWord getGaslimit();

    boolean byTransaction();

    boolean byTestingSuite();

    int getCallDeep();

    Repository getRepository();

    BlockStore getBlockStore();

    boolean isStaticCall();
}
