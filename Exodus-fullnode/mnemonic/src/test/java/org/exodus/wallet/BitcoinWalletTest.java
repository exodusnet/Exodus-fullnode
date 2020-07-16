package org.exodus.wallet;


import org.exodus.mnemonic.Mnemonic;
import org.exodus.wallet.walletImp.BitcoinWallet;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;


/**
 * @Author: zz
 * @Description:
 * @Date: 下午 2:24 2018/10/8 0008
 * @Modified By
 */
public class BitcoinWalletTest {

    @Test
    public void getBitcoinAddress() throws Exception {
        Mnemonic mn = new Mnemonic("","");
        String words = String.join(" ",mn.getMnemonic());
        System.out.println(words);

        BitcoinWallet wallet = new BitcoinWallet(words,"");
        String address = wallet.getAddress();
        System.out.println(address);



    }


}
