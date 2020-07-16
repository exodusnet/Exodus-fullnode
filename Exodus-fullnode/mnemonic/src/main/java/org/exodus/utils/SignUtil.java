package org.exodus.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.exodus.wallet.walletImp.InveWallet;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;



/**
 *
 * @Author: zz
 * @Description: 签名工具类
 * @Date:  5:19 2018/6/20 0020
 * @Modified By
 */
public class SignUtil {

    private static Logger log = LoggerFactory.getLogger(SignUtil.class);

    /**
     *  Sign message 3.0 version
     * @param message
     * @param privateKey
     * @return
     */
    public static String sign(String message, ECKey privateKey) {
        JSONObject unitObj = JSONObject.parseObject(message);
        String str = getSourceString(unitObj);
        return signStr(str,privateKey);
    }

    /**
     * 对字符串进行签名
     * @param str 需要签名str
     * @param key 签名私钥
     * @return 签名signature
     */
    public static String signStr(String str , ECKey key) {
        ECKey.ECDSASignature signStr = key.sign(Sha256Hash.wrap(Sha256Hash.hash(str.getBytes())));
        byte[] arr = ArrayUtils.addAll(signStr.r.toByteArray(), signStr.s.toByteArray());
        return signStr.r.toByteArray().length + DSA.encryptBASE64(arr);
    }

    /**
     *  Verify message 3.0 version
     * @param message
     * @return 签名结果
     */
    public static boolean verify(String message) {
        JSONObject o = JSONObject.parseObject(message);
        String pubkey = o.getString("pubkey");
        //对地址进行验证
        JSONArray definition = JSONObject.parseArray("[\"sig\", {\"pubkey\":\""+ pubkey +"\"}]");
        String address = InveWallet.getInveAddressByDefinidion(definition);
        if(!o.getString("fromAddress").equals(address)) {
            log.error("the address is not self address ~!");
            return false;
        }
        String signature = o.getString("signature");
        o.remove("signature");
        o.remove("hash");
        String str = getSourceString(o);
        return verifyStr(str, signature, ECKey.fromPublicOnly(DSA.decryptBASE64(pubkey)));
    }
    /**
     *  Verify message 3.0 version
     * @param o
     * @return 签名结果
     */
    public static boolean verify(JSONObject o) {
        //对地址进行验证
        String pubkey = o.getString("pubkey");
        JSONArray definition = JSONObject.parseArray("[\"sig\", {\"pubkey\":\""+ pubkey +"\"}]");
        String address = InveWallet.getInveAddressByDefinidion(definition);
        if(!o.getString("fromAddress").equals(address)) {
            log.error("the address is not self address ~!");
            return false;
        }
        String signature = o.getString("signature");
        o.remove("eHash");
        o.remove("hash");
        o.remove("id");
        o.remove("isStable");
        o.remove("isValid");
        o.remove("signature");
        o.remove("updateTime");
        String str = getSourceString(o);
        return verifyStr(str, signature, ECKey.fromPublicOnly(DSA.decryptBASE64(pubkey)));
    }

    /**
     * verfy str
     * @param str
     * @param signature base64
     * @param key 公钥
     * @return 验证结果
     */
    public static boolean verifyStr(String str, String signature, ECKey key) {
        int check = Integer.valueOf(signature.substring(0,2));
        byte[] arr = DSA.decryptBASE64(signature.substring(2));

        BigInteger r = new BigInteger(Arrays.copyOfRange(arr,0,check));
        BigInteger s = new BigInteger(Arrays.copyOfRange(arr,check, arr.length));

        return key.verify(Sha256Hash.hash(str.getBytes()), new ECKey.ECDSASignature(r,s).encodeToDER());
    }

    /**
     * verfy str
     * @param str
     * @param signature base64
     * @param pubkey 公钥
     * @return 验证结果
     */
    @Deprecated
    public static boolean verifyStr(String str, String signature, byte[] pubkey) {
        int check = Integer.valueOf(signature.substring(0,2));
        byte[] arr = DSA.decryptBASE64(signature.substring(2));

        BigInteger r = new BigInteger(Arrays.copyOfRange(arr,0, check));
        BigInteger s = new BigInteger(Arrays.copyOfRange(arr, check, arr.length));
        return ECKey.verify(Sha256Hash.hash(str.getBytes()), new ECKey.ECDSASignature(r, s), pubkey);
    }



    /**
     * 签名unit   2.0 version
     * @param message
     * @param key
     * @return 签名signature
     */
    @Deprecated
    public static String Sign(String message, ECKey key)  {
        ECKey.ECDSASignature signStr = null;
        try {
            JSONObject msgObj  = (JSONObject)JSONObject.parseObject(message).get("message");
            //objNakedUnit
            msgObj = getNakedUnit(msgObj);
            JSONArray authors  = msgObj.getJSONArray("authors");
            for(int i = 0; i < authors.size();i++) {
                authors.getJSONObject(0).remove("authentifiers");
            }
            String strUnit = new String(getSourceString(msgObj).getBytes(),"utf8");
            signStr = key.sign(Sha256Hash.wrap(Sha256Hash.hash(strUnit.getBytes())));
        } catch (Exception e) {
            System.out.println("Sign failed ~!");
            e.printStackTrace();
        }
        return DSA.encryptBASE64(ArrayUtils.addAll(signStr.r.toByteArray(), signStr.s.toByteArray()));
    }

    /**
     * 签名验证  2.0 version
     * @param message
     * @return 验证结果
     */
    @Deprecated
    public static boolean Verify(String message)  {
        try {
            JSONObject unitObject = (JSONObject)JSONObject.parseObject(message).get("unit");
            JSONArray authors  = unitObject.getJSONArray("authors");
            ECKey key = ECKey.fromPublicOnly(DSA.decryptBASE64(authors.getJSONObject(0).getJSONArray("definition")
                    .getJSONObject(1).getString("pubkey")));
            JSONObject authentifiers = (JSONObject)authors.getJSONObject(0).get("authentifiers");
            for(int i = 0; i < authors.size();i++) {
                authors.getJSONObject(0).remove("authentifiers");
            }
            //objNakedUnit
            String strUnit = new String(getSourceString(getNakedUnit(unitObject)).getBytes(),"utf8");
            byte[] arr = DSA.decryptBASE64( authentifiers.getString("r"));
            if(arr.length > 64) {
               arr = ArrayUtils.remove(arr , 0);
            }
            byte[] rArr = Arrays.copyOfRange(arr,0,32);
            byte[] sArr = Arrays.copyOfRange(arr,32,64);
            ECKey.ECDSASignature ec = new ECKey.ECDSASignature(new BigInteger(rArr), new BigInteger(sArr));
            return key.verify(Sha256Hash.hash(strUnit.getBytes()), ec.encodeToDER());
        } catch (Exception e) {
            System.err.println("verify failed ~!");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取裸unit
     * @param unitObject
     * @return message
     */
    private static JSONObject getNakedUnit(JSONObject unitObject) {
        try{
            unitObject.remove("unit");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            unitObject.remove("walletId");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            unitObject.remove("headers_commission");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            unitObject.remove("payload_commission");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            unitObject.remove("main_chain_index");
        } catch (Exception e){
            e.printStackTrace();
        }
        try{
            unitObject.remove("timestamp");
        } catch (Exception e){
            e.printStackTrace();
        }
        JSONArray messages = unitObject.getJSONArray("messages");
        for(int i = 0; i < messages.size(); i++) {
            try{
                messages.getJSONObject(i).remove("payload");
            } catch (Exception e){
                e.printStackTrace();
            }
            try{
                messages.getJSONObject(i).remove("payload_uri");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return unitObject;
    }

    private final static String  X00 = "\u0000";

    /**
     * 获取unit源数据
     * @param unitObject
     * @return
     */
    public static String getSourceString(Object unitObject) {
        ArrayList arrList  = new ArrayList();
        extractComponents(unitObject, arrList);
        return StringUtils.join(arrList.toArray(), X00);

    }

    public  static void extractComponents(Object ob, List<String> arrList) {
        if(ob instanceof String) {
            arrList.add("s");
            arrList.add((String) ob);
        } else if (ob instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) ob;
            arrList.add("[");
            for (Object aJsonArray : jsonArray) {
                extractComponents(aJsonArray, arrList);
            }
            arrList.add("]");
        } else if (ob instanceof JSONObject) {
            JSONObject json = (JSONObject) ob;
            Set<String> set = json.keySet();
            String[] keys = set.toArray(new String[0]);
            Arrays.sort(keys);
            for(String key : keys) {
                arrList.add(key);
                extractComponents(json.get(key),arrList);
            }
        } else if (ob instanceof Character) {
            arrList.add("c");
            arrList.add(ob.toString());
        } else if (ob instanceof Integer) {
            arrList.add("n");
            arrList.add(ob.toString());
        } else if (ob instanceof Long) {
            arrList.add("n");
            arrList.add(ob.toString());
        } else if (ob instanceof BigInteger) {
            arrList.add("n");
            arrList.add(ob.toString());
        } else if (ob instanceof Double) {
            arrList.add("n");
            arrList.add(ob.toString());
        } else if (ob instanceof BigDecimal) {
            arrList.add("n");
            arrList.add(ob.toString());
        } else if (ob instanceof Boolean) {
            arrList.add("b");
            arrList.add(ob.toString());
        } else {
            throw new RuntimeException("hash: unknown type field~!");
        }
    }

}
