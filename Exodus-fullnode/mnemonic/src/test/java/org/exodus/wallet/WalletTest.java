package org.exodus.wallet;

import org.exodus.wallet.walletImp.InveWallet;
import org.junit.Test;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 5:37 2018/9/11 0011
 * @Modified By
 */
public class WalletTest {

    /**
     * 钱包地址生成案例
     * @throws Exception
     */
    @Test
    public void inveWalletTest() throws Exception {
        InveWallet wallet = InveWallet.getInstance("","");
        String address = wallet.getAddress();
        System.out.println(address);
    }


    /**
     * 使用助记词回复钱包并生成地址
     * @throws Exception
     */
    @Test
    public void inveWalletRecover() throws Exception {
        InveWallet wallet = InveWallet.getInstance("shield salmon sport horse cool hole pool panda embark wrap fancy equip","");
        String address = wallet.getAddress();
        System.out.println(address);
    }


    /**
     * 测试钱包地址合法性
     */
    @Test
    public void inveWalletIsValid() {
        boolean rs = InveWallet.isValidAddress("U2VRUTMT4ZFMQTZTFTHOSUHIZJNFHFTE");
        System.out.println(rs);
    }
}
