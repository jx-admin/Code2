
package com.accenture.mbank.util;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author yang.c.li
 */
public class EncryptUtil {
    private static char[] HEXCHAR = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private static final String AES_CBC_NOPADDING = "AES/CBC/NoPadding";

    private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    
    /**
     * AES 加密使用方法
     */
    public void test() {
        byte[] aesKey = EncryptUtil.createAESKey();
        byte[] aesIV = EncryptUtil.createAESIv();
        // byte[] aesAK = EncryptUtil.arrayCombine(aesKey, aesIV);
        String ciphertext = "123456789";
        try {
            //加密
            byte[] encryptByte = EncryptUtil.AESEncrypt(EncryptUtil.AES_CBC_PKCS5PADDING, aesKey,
                    aesIV, ciphertext.toString().getBytes("UTF-8"));
            //解密
            byte[] decryptByte = EncryptUtil.AESDecrypt(EncryptUtil.AES_CBC_PKCS5PADDING, aesKey,aesIV, encryptByte);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * AES 加密
     * 
     * @param algorithm 
     *            AES/CBC/NoPadding算法只能加密16byte的长�?AES/CBC/PKCS5Padding算法没有长度限制
     * @param keyBits
     * @param keyAlgorithm
     * @param ivBits
     * @param plaintext明文
     * @return
     */
    public static byte[] AESEncrypt(String algorithm, byte[] keyBits, byte[] ivBits,
            byte[] plainByte) throws Exception {
        String keyAlgorithm = "AES";
        Cipher cipher = Cipher.getInstance(algorithm);

        Key key = new SecretKeySpec(keyBits, 0, keyBits.length, keyAlgorithm);
        IvParameterSpec iv = null;

        if (ivBits != null) {
            iv = new IvParameterSpec(ivBits, 0, ivBits.length);
        }

        // 计算密文大小
        int blocksize = 16;
        int ciphertextLength = 0;
        int remainder = plainByte.length % blocksize;

        if (remainder == 0) {
            ciphertextLength = plainByte.length;
        } else {
            ciphertextLength = plainByte.length - remainder + blocksize;
        }

        if (iv == null) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        }

        byte[] ciphertext = new byte[ciphertextLength];

        if (plainByte.length >= ciphertext.length) {
            ciphertext = new byte[ciphertextLength + blocksize]; // 加这句的原因是有某些原因会导致密文等于或是小于明�?.
                                                                 // 这样的话buffer区域就不够大
            System.out.println("It's Here !! ");
        }
        System.out.println("明文长度  : " + plainByte.length);
        System.out.println("密文长度 : " + ciphertext.length);
        System.out.println("blocksize : " + blocksize);
        System.out.println("remainder : " + remainder);

        cipher.doFinal(plainByte, 0, plainByte.length, ciphertext, 0);

        return ciphertext;

    }

    /**
     * AES 解密
     * 
     * @param algorithm AES/CBC/NoPadding
     * @param keyBits
     * @param keyAlgorithm 算法
     * @param ivBits
     * @param ciphertext 密文
     * @return
     */
    public static byte[] AESDecrypt(String algorithm, byte[] keyBits, byte[] ivBits,
            byte[] ciphertext) {
        try {
            String keyAlgorithm = "AES";
            Cipher cipher = Cipher.getInstance(algorithm);
            Key key = new SecretKeySpec(keyBits, 0, keyBits.length, keyAlgorithm);
            IvParameterSpec iv = null;

            if (ivBits != null) {
                iv = new IvParameterSpec(ivBits, 0, ivBits.length);
            }

            if (iv == null) {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key, iv);
            }

            byte[] decrypted = new byte[ciphertext.length];
            cipher.doFinal(ciphertext, 0, ciphertext.length, decrypted, 0);

            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成SHA1摘要
     * 
     * @param plaintext
     * @return
     */
    public static byte[] createSHA1Summary(String plaintext) {
        try {
            byte[] data = plaintext.getBytes("utf-8");
            byte[] digest = new byte[20];
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(data, 0, data.length);
            md.digest(digest, 0, 20);
            return digest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 16进制数组转String
    public static String toHexString(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
            sb.append(HEXCHAR[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    // StringtoBytes
    public static final byte[] toBytes(String s) {
        byte[] bytes;
        bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte)Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    // 将字节转换为十六进制字符
    public static String byteToHexString(byte ib) {
        char[] Digit = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    // 将字节数组转换为十六进制字符
    public static String byteArrayToHexString(byte[] bytearray) {
        StringBuffer strDigest = new StringBuffer();
        for (int i = 0; i < bytearray.length; i++) {
            strDigest.append(byteToHexString(bytearray[i]));
        }
        return strDigest.toString();
    }

    /**
     * 生成AES KEY
     * 
     * @return
     */
    public static byte[] createAESKey() {
        long tempLong = System.currentTimeMillis();
        long id = Runtime.getRuntime().freeMemory();
        Random random = new Random(tempLong + tempLong % 10000 + id);
        byte[] key = new byte[16];
        for (int i = 0; i < key.length; i++) {
            key[i] = (byte)random.nextInt(256);
        }
        return key;
    }

    /**
     * 生成AES IV
     * 
     * @return
     */
    public static byte[] createAESIv() {
        long tempLong = System.currentTimeMillis();
        long id = Runtime.getRuntime().freeMemory();
        Random random = new Random(tempLong + tempLong % 10000 + id);
        byte[] iv = new byte[16];
        for (int i = 0; i < iv.length; i++) {
            iv[i] = (byte)random.nextInt(256);
        }
        return iv;
    }

    public static byte[] arrayCombine(byte[] firstArray, byte[] secondArray) {
        byte[] temp = null;
        if (firstArray == null || secondArray == null)
            return null;

        temp = new byte[firstArray.length + secondArray.length];
        for (int i = 0; i < temp.length; i++) {
            if (i < firstArray.length) {
                temp[i] = firstArray[i];
            } else {
                temp[i] = secondArray[i - firstArray.length];
            }
        }
        return temp;
    }
}
