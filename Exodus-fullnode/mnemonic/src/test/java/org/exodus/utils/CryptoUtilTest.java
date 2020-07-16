package org.exodus.utils;

import org.junit.Test;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 5:15 2018/9/10 0010
 * @Modified By
 */
public class CryptoUtilTest {

    /**
     * 由signature 生成hash （轻接点的id）
     * @throws Exception
     */

    @Test
    public void sha256Test() throws Exception {
        String str = "wnjzk5FhBUFK0SIl/NzCP0ZAZZPtF7oy6S2g46Fc/oxi68H3tiko9pGOl4zucc10GSb/WKRkYTCzWeWgTeuxRw==";


        String res = DSA.encryptBASE64(CryptoUtil.sha256hash(str.getBytes()));

        System.out.println(res);
    }

}
