package org.exodus.wallet.walletImp;

import com.alibaba.fastjson.JSONArray;
import org.exodus.core.Chash;
import org.exodus.mnemonic.Mnemonic;
import org.exodus.utils.SignUtil;
import org.exodus.wallet.Wallet;

/**
 * @Author: zz
 * @Description: wallet ： address
 * @Date: 下午 5:11 2018/9/11 0011
 * @Modified By
 */
public class InveWallet implements Wallet {

    private Mnemonic mnemonic;
    private String address;



    /**
     *  初始化钱包
     * @param words 助记词
     * @param passphrase 随机盐
     * @throws Exception
     */
    public InveWallet(String words ,String passphrase) throws Exception {
        mnemonic = new Mnemonic(words,passphrase);
        this.address = mnemonic.getAddress();
    }

    @Deprecated
    static class InveWalletHolder {
        private static InveWallet instance = null;
        public static InveWallet getInstance(String words ,String passphrase) throws Exception {
            if(instance == null) {
                instance = new InveWallet(words, passphrase);
            }
            return instance;
        }
    }

    /**
     * 获取INVE钱包实例
     * @param words 助记词
     * @param passphrase 随机盐
     * @return InveWallet
     * @throws Exception
     */
    @Deprecated
    public static synchronized InveWallet getInstance(String words,String passphrase) throws Exception {
        return InveWalletHolder.getInstance(words,passphrase);
    }

    /**
     * 获取钱包地址
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 验证钱包地址是否正确
     * @param address
     * @return 验证结果
     */
    public static boolean isValidAddress(String address) {
        if(address.length() != 32) {
            return false;
        }
        return (address.length() == 32 && address.toUpperCase().equals(address) && Chash.isValidChash(address));
    }

    /**
     * 根据定义生成地址
     * @param definition 定义 模板 "[\"sig\", {\"pubkey\":\""+ DSA.encryptBASE64(public) +"\"}]"
     * @return
     */
    public static String getInveAddressByDefinidion(JSONArray definition) {
        return Mnemonic.getAddressByDefinition(SignUtil.getSourceString(definition)).toUpperCase();
    }

}
