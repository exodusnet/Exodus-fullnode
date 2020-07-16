package org.exodus.utils;

import org.exodus.mnemonic.Mnemonic;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 2:54 2018/9/27 0027
 * @Modified By
 */
public class ByteUtilTest {


    @Test
    public void byteTest() throws Exception {
        Mnemonic mn = new Mnemonic("","");
        byte[] bytes = mn.getxPrivKey();
        BigInteger big = mn.getxPrivKeyBig();

        BigInteger baa = new BigInteger(bytes);


        String bigs = big.toString();
        System.out.println(baa.toString().equals(bigs));
        System.out.println();
    }


}
