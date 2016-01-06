package com.android.test.wifi;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class EncryptionUtil {
    private final static String TAG = "Launcher.EncryptionUtil";

    private static Cipher aesCipher;

    private static Cipher getAESCipher() throws Exception {
        if (aesCipher == null) {
            aesCipher = Cipher.getInstance("AES");
        }

        return aesCipher;
    }

    public static synchronized String decryptAES(String value, String privateKey) {
        try {
            if (value == null || value.trim().length() == 0) {
                return value;
            }
            byte[] raw = Base64.decodeBase64(privateKey.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = getAESCipher();
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return new String(cipher.doFinal(hexStringToByte(value)));
        } catch (Exception e) {
                Log.e(TAG, "Failed to decrypt via AES.", e);
            return value;
        }
    }

    public static synchronized String encryptAES(String value, String privateKey) {
        try {
            if (value == null || value.trim().length() == 0) {
                return value;
            }
            byte[] raw = Base64.decodeBase64(privateKey.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = getAESCipher();
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return parseByte2HexStr(cipher.doFinal(value.getBytes("utf-8")));
        } catch (Exception e) {
                Log.e(TAG, "Failed to encrypt via AES.", e);
            return value;
        }
    }

    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    private static byte[] hexStringToByte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

//    public static String getDefaultKey() {
//        // "qQ1gvLjWzI0z7FsNLFDN5w=="
//        return "qQ1" + StringUtils.getChar('g' - 'a') + "vLjW"
//                + StringUtils.getChar('z' - 'a') + "I0z7F" + StringUtils.getChar('s' - 'a') + "NLFDN5w==";
//    }
}
