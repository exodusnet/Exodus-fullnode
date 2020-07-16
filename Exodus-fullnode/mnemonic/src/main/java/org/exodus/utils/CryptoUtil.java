package org.exodus.utils;

import org.bitcoinj.core.Sha256Hash;

/**
 * @Author: zz
 * @Description:
 * @Date: 上午 9:50 2018/9/7 0007
 * @Modified By
 */
public class CryptoUtil {

    /**
     * sha256hash
     * @param input byte[]
     * @return byte[]
     */
    public static byte[] sha256hash(byte[] input) {
        return Sha256Hash.hash(input);
    }


}
