package org.exodus.utils;

import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: zz
 * @Description:
 * @Date: 下午 3:09 2018/9/22 0022
 * @Modified By
 */
public class DSAutilTest {

    public static final String KEY_ALGORITHM = "DSA";
    public static final String SIGNATURE_ALGORITHM = "DSA";

    public static final String DEFAULT_SEED = "$%^*%^()(HJG8awfjas7"; //默认种子
    public static final String PUBLIC_KEY = "DSAPublicKey";
    public static final String PRIVATE_KEY = "DSAPrivateKey";

    @Test
    public void DSATest() throws Exception {
        String str = "!@#$!#^$#&ZXVDF呆军工路爱着你*()_+";
        byte[] data = str.getBytes();

        Map<String, Object> keyMap2 = DSA.initKey();
        // 构建密钥
        PublicKey publicKey = (PublicKey) keyMap2.get(PUBLIC_KEY);
        PrivateKey privateKey = (PrivateKey) keyMap2.get(PRIVATE_KEY);


        System.out.println("私钥format：" + privateKey.getFormat());
        System.out.println("公钥format：" + publicKey.getFormat());





        Map<String , Object> keyMap = new HashMap<>();
        keyMap.put(PRIVATE_KEY ,privateKey);
        keyMap.put(PUBLIC_KEY, publicKey);

        // 产生签名
        String sign = DSA.sign(data, DSA.getPrivateKey(keyMap));

//        boolean flag = SignUtil.verify(sign , publicKey);




        // 验证签名
        boolean verify1 = DSA.verify("aaa".getBytes(), DSA.getPublicKey(keyMap), sign);
        System.err.println("经验证  数据和签名匹配:" + verify1);

        boolean verify = DSA.verify(data, DSA.getPublicKey(keyMap), sign);
        System.err.println("经验证 数据和签名匹配:" + verify);



    }
}
