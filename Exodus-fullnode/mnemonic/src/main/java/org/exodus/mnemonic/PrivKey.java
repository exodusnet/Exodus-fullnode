package org.exodus.mnemonic;



import org.exodus.utils.DSA;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zz
 * @Description:
 * @Date:  10:03 2018/6/11 0011
 * @Modified By
 */
public class PrivKey {

    private String deviceName;

    private String passphrase;

    private byte deviceTempPrivKey [];
    //
    private byte devicePrevTempPrivKey [];





    public PrivKey(String deviceName, String passphrase) {
        this.deviceName = deviceName;
        this.passphrase = passphrase;

        setDeviceTempPrivKey();
        setDevicePrevTempPrivKey();
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public byte[] getDeviceTempPrivKey() {
        return deviceTempPrivKey;
    }


    public  byte[] getDevicePrevTempPrivKey() {
        return devicePrevTempPrivKey;
    }


    private void setDeviceTempPrivKey() {
        //var deviceTempPrivKey = crypto.randomBytes(32);
        SecureRandom csprng = new SecureRandom();
        byte[] randomBytes = new byte[32];
        csprng.nextBytes(randomBytes);
        this.deviceTempPrivKey = randomBytes;
    }


    private void setDevicePrevTempPrivKey() {
        //var devicePrevTempPrivKey = crypto.randomBytes(32);

        SecureRandom csprng = new SecureRandom();
        byte[] randomBytes = new byte[32];
        csprng.nextBytes(randomBytes);
        this.devicePrevTempPrivKey = randomBytes;
    }



    /**
     *
     * @return
     */
    public Map<String ,String> createKeys(String mnemonic_phrase, byte[] deviceTempPrivKey , byte[] devicePrevTempPrivKey) {
        Map<String ,String> keys = new HashMap<String, String>();

        String deviceTempPrivKeyStr = DSA.encryptBASE64(deviceTempPrivKey);
        String devicePrevTempPrivKeyStr = DSA.encryptBASE64(devicePrevTempPrivKey);

        if(mnemonic_phrase != null) {
            keys.put("mnemonic_phrase",mnemonic_phrase);
        }

        if(deviceTempPrivKey != null && deviceTempPrivKeyStr != "") {
            keys.put("deviceTempPrivKey",deviceTempPrivKeyStr);
        }

        if(devicePrevTempPrivKey != null && devicePrevTempPrivKeyStr != "") {
            keys.put("devicePrevTempPrivKey",devicePrevTempPrivKeyStr);
        }

        return keys;
    }
}
