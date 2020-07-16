package org.exodus.core;

import com.subgraph.orchid.data.Base32;
import org.exodus.utils.ByteUtil;
import org.apache.commons.lang.StringUtils;
import org.bitcoinj.core.Sha256Hash;
import org.web3j.utils.Numeric;

import java.util.*;

/**
 * @Author: zz
 * @Description:
 * @Date: 下午 6:54 2018/9/17 0017
 * @Modified By
 */
public class Chash {

    /**
     * 验证地址是否合法
     * @param address
     * @return true or false
     */
    public static boolean isValidChash(String address) {
        byte[] chash = Base32.base32Decode(address);
        String binChash = ByteUtil.bytes2bin(chash);

        return verifyEOAAddr(binChash) || verifyContractAddr(binChash);
    }

    /**
     * 验证EOA地址
     * @param bin
     * @return true or false
     */
    private static boolean verifyEOAAddr(String bin) {
        Map<String,String> separated = separateIntoCleanDataAndChecksum(bin);
        byte[] clean_data = ByteUtil.bin2bytes(separated.get("clean_data"));
        byte[] checksum = ByteUtil.bin2bytes(separated.get("checksum"));
        return Arrays.equals(checksum,getChecksum(clean_data));
    }

    /**
     * 验证合约地址
     * @param bin
     * @return true or false
     */
    private static boolean verifyContractAddr(String bin) {
        try {
            Numeric.toBigIntNoPrefix(bin);
        } catch (NumberFormatException e) {
            return false;
        }
        return bin.length() == 160;
    }

    /**
     * 拆分校验码
     * @param bin
     * @return 校验码和校验数据
     */
    private static Map<String, String> separateIntoCleanDataAndChecksum(String bin) {
        int len = bin.length();
        Integer[] arrOffsets = null;
        if(len == 160){
            //根据PI 获取32个有一定规律的数字
            arrOffsets = Chash.calcOffsets(160);
        }else if(len == 288) {
            //TODO
        }else {
            throw new RuntimeException("bad length = " + len + ", bin = " + bin);
        }
        String[] arrFrags = new String[arrOffsets.length + 1];
        String[] arrChecksumBits = new String[arrOffsets.length];

        int start = 0;
        int n = 0;
        for (int i = 0 ; i < arrOffsets.length ; i++) {
            arrFrags[i] = StringUtils.substring(bin,start , arrOffsets[i]);
            arrChecksumBits[i] = StringUtils.substring(bin,arrOffsets[i],arrOffsets[i]+1);
            start = arrOffsets[i] + 1;
            n++;
        }
        //add last frag
        if(start < bin.length()) {
            arrFrags[n] = StringUtils.substring(bin,start);
        }
        String binCleanData = StringUtils.join(arrFrags,"");
        String binChecksum = StringUtils.join(arrChecksumBits,"");
        Map<String ,String> data = new HashMap<>();
        data.put("clean_data" ,binCleanData);
        data.put("checksum",binChecksum);
        return data;
    }

    /**
     * @param binCleanData
     * @param binChecksum
     * @return
     */
    public static String mixChecksumIntoCleanData(String binCleanData, String binChecksum) {
        int binChecksumLen = binChecksum.length();
        if( binChecksumLen != 32) {
            throw new RuntimeException("bad checksum length");
        }
        int len = binCleanData.length() + binChecksumLen;
        Integer[] arrOffsets = null;
        if(len == 160) {
            //根据PI 获取32个有一定规律的数字
            arrOffsets = Chash.calcOffsets(160);
        }else if (len == 288){
            //TODO
        }else {
            throw new RuntimeException("bad length="+len+", clean data = "+binCleanData+", checksum = "+binChecksum);
        }
        String[] arrFrags = new String[arrOffsets.length*2 + binCleanData.length()];

        String[] arrChecksumBits = binChecksum.split("");
//        String[] arrChecksumBits2 = StringUtils.split(binChecksum,"");

        int start = 0;
        int n = 0;
        for(int i = 0 ; i < arrOffsets.length ;i++) {
            int end = arrOffsets[i] - i;
            arrFrags[n++] = StringUtils.substring(binCleanData,start,end);
            arrFrags[n++] = arrChecksumBits[i];
            start = end;
        }
        if(start < binCleanData.length()) {
            arrFrags[n++] = StringUtils.substring(binCleanData,start);
        }
        return StringUtils.join(arrFrags, "");
    }

    private static final String PI = "14159265358979323846264338327950288419716939937510";
    private static final String[] arrRelativeOffsets = PI.split("");

    //计算
    public static Integer[] calcOffsets(int chash_length) {

        List<Integer> arrList = new ArrayList<Integer>();
        int offset = 0;
        int index = 0;
        for(int i=0; offset<chash_length; i++) {
            int relative_offset = Integer.valueOf(arrRelativeOffsets[i]).intValue();
            if(relative_offset == 0) {
                continue;
            }
            offset += relative_offset;
            if(chash_length == 288) {
                offset += 4;
            }
            if (offset >= chash_length) {
                break;
            }
            arrList.add(offset);
            index++;

        }
        if(index != 32) {
            throw new RuntimeException("wrong number of checksum bits");
        }
        Integer[] arrOffsets = new Integer[arrList.size()];

        arrList.toArray(arrOffsets);
        return arrOffsets;
    }

    /**
     * 通过校验数据获取地址校验码
     * @param clean_data
     * @return 校验码
     */
    public static byte[] getChecksum(byte[] clean_data) {
        byte[] full_checksum = Sha256Hash.hash(clean_data);
        byte[] checksum = new byte[4];
        checksum[0] = full_checksum[5];
        checksum[1] = full_checksum[13];
        checksum[2] = full_checksum[21];
        checksum[3] = full_checksum[29];
        return checksum;
    }
}
