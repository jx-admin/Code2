
package com.act.mbanking.secure;

import java.io.IOException;
import java.io.Serializable;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptImple implements ObjectEncryptAble {
    private Cipher encrypter = null, decrypter = null;

    private static final String algorithm = "AES";

    public AESEncryptImple(String key) {

        init(key);
    }

    private void init(String key) {
        key = addZeroForNum(key, 16);
        // 16λ
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        try {
            encrypter = Cipher.getInstance(algorithm);
            encrypter.init(Cipher.ENCRYPT_MODE, skeySpec);
            decrypter = Cipher.getInstance(algorithm);
            decrypter.init(Cipher.DECRYPT_MODE, skeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 数字不足位数左补0
     * @param str
     * @param strLength
     */
    public String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                // sb.append("0").append(str);// 左补0
                sb.append(str).append("0");// 右补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    @Override
    public Serializable encrypt(Serializable o) {
        SealedObject sealedObject = null;
        try {
            sealedObject = new SealedObject(o, encrypter);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sealedObject;
    }

    @Override
    public Serializable decrypt(Serializable o) {
        SealedObject sealedObject = (SealedObject)o;
        try {
            Object i = sealedObject.getObject(decrypter);
            Serializable result = (Serializable)i;
            return result;
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
