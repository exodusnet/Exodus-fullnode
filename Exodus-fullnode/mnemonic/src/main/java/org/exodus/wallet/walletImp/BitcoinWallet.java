package org.exodus.wallet.walletImp;

import org.exodus.mnemonic.Mnemonic;
import org.exodus.wallet.Wallet;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 2:01 2018/10/8 0008
 * @Modified By
 */
public class BitcoinWallet implements Wallet {

    private Mnemonic mnemonic;
    private String address;


    public BitcoinWallet(String words ,String passphrase) throws Exception {
        this.mnemonic = new Mnemonic(words,passphrase);
        this.address = mnemonic.getBtcAddress();
    }


    @Override
    public String getAddress() throws Exception {
        return address;
    }


    public static boolean isValidAddress(String address) throws Exception {
        return false;
    }
}
