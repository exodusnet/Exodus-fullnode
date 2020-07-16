package org.exodus.utils;

/**
 * @Author: zz
 * @Description:
 * @Date: 上午 11:41 2018/6/23 0023
 * @Modified By
 */
public class ByteUtil {

    /**
     * int转换成byte数组
     * @param value
     * @return bytes
     */
    public static byte[] int2bytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * 二进制字符串转换成byte
     * @param str
     * @return byte
     */
    public static byte str2byte(String str) {
        byte result=0;
        for(int a=str.length()-1,j=0;a>=0;a--,j++){
            result+=(Byte.parseByte(str.charAt(a)+"")*Math.pow(2, j));
        }
        return result;
    }

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * byte数组转换成10进制字符串
     * @param bytes
     * @return hex string
     */
    public static String bytes2hex(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) { //
            buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
            buf[index++] = HEX_CHAR[b & 0xf];
        }
        return new String(buf);
    }

    /**
     * 16进制字符串转换成byte数组
     * @param hexString
     * @return bytes
     */
    public static byte[] hexString2bytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (char2byte(hexChars[pos]) << 4 | char2byte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * char 转换成byte
     * @param c
     * @return
     */
    private static byte char2byte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 将8位byte数组 转换成二进制字符串
     * @param b
     * @return binary string
     */
    public static String getBinaryStrFromByte(byte b) {
        String result = "";
        byte a = b;
        ;
        for (int i = 0; i < 8; i++) {
            byte c = a;
            a = (byte) (a >> 1);//
            a = (byte) (a << 1);
            if (a == c) {
                result = "0" + result;
            } else {
                result = "1" + result;
            }
            a = (byte) (a >> 1);
        }
        return result;
    }

    /**
     *  int数组转换成byte数组
     * @param intarr
     * @return bytes
     */
    public  static byte []  intArr2byteArr(Integer  [] intarr) {
        int bytelength = intarr.length * 4;//长度
        byte[] bt = new byte[bytelength];//开辟数组
        int curint = 0;
        for (int j = 0, k = 0; j < intarr.length; j++, k += 4) {
            curint = intarr[j];
            bt[k] = (byte) ((curint >> 24) & 0b1111_1111);//右移4位，与1作与运算
            bt[k + 1] = (byte) ((curint >> 16) & 0b1111_1111);
            bt[k + 2] = (byte) ((curint >> 8) & 0b1111_1111);
            bt[k + 3] = (byte) ((curint >> 0) & 0b1111_1111);
        }
        return bt;
    }

    //TODO 需要优化二进制转化成字符串
    //
    String zeroStrubg = "00000000";
    /**
     * byte数组转换为二进制字符串
     * @param b
     * @return binary string
     */
    public static String bytes2bin(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String str = Long.toString(b[i] & 0xff, 2);
            int len = str.length();
                for(int j = 0 ; j < 8-len ; j++) {
                    str = 0 + str;
                }
            result.append(str);
        }
//        System.out.println(result.length());
        return result.toString();
    }

    /**
     * byte转换为二进制字符串,每个字节以""隔开
     * @param bt
     * @return binary string
     */
    public static String byte2bin(byte bt) {
        String str = Long.toString(bt & 0xff, 2);
        int len = str.length();
        for(int j = 0 ; j < 8-len ; j++) {
            str = 0 + str;
        }
        return str;
    }

    /**
     * 8位二进制字符串 转换成 byte数组
     * @param bin
     * @return bytes
     */
    public static byte[] bin2bytes(String bin) {
        int len = bin.length()/8;
        byte[] buf = new byte[len];
        for(int i = 0 ;i < len ; i++) {
            String str = bin.substring(i*8 ,(i+1)*8);
            byte result = ByteUtil.str2byte(str);
            buf[i] = result;
        }
        return buf;
    }
}