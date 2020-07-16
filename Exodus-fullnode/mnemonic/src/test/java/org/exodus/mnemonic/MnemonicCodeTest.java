package org.exodus.mnemonic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.exodus.utils.SignUtil;
import org.bitcoinj.core.ECKey;
import org.junit.Test;

import javax.crypto.interfaces.DHPrivateKey;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author: zz
 * @Description:
 * @Date:  1:51 2018/6/12 0012
 * @Modified By
 */
public class MnemonicCodeTest {


    /**
     *  公私钥生成方法
     * @throws Exception
     */
    @Test
    public void MnemonicTest() throws Exception {

        List a = new ArrayList();
        byte[] arr = new byte[1];

        //初始化公私钥
        Mnemonic mn = new Mnemonic("","");
        //获取私钥 byte数组类型,bigInteger类型
        byte[] prikey = mn.getxPrivKey();
        BigInteger priBig = mn.getxPrivKeyBig();
        //获取公钥 byte数组类型
        byte[] pubKey = mn.getPubKey();

        //获取助记词
        List<String> words = mn.getMnemonic();

        //获取地址
        String address = mn.getAddress();
        System.out.println(address);

    }



    @Test
    public void privatekeyTest() throws Exception {
        Mnemonic mn = new Mnemonic("","");
        byte[] pri_buf = mn.getxPrivKey();

        ECKey key2 = ECKey.fromPrivate(mn.getxPrivKeyBig());



//        System.out.println(flag);

        DHPrivateKey pri ;

    }


    @Test
    public void getAddressByDefinition() {
        //0ME2EWJTVSWJNLP5FDTK5A7XRQVKC3K3F
        //0Y2FXE34ZLZWNM2NSGMR4SNXQUYODAGH5
        JSONArray definition = JSONObject.parseArray("[\"sig\", {\"pubkey\":\"AsDpTEIMMZSLbOCXnELTcGrc/prLDG/eo+zu23kFHqs3\"}]");
        String data = SignUtil.getSourceString(definition);
        String address = Mnemonic.getAddressByDefinition(data);
        System.out.println(address.toUpperCase());
    }
}
