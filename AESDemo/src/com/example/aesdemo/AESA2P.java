package com.example.aesdemo;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import android.util.Log;

/**
 * <pre>
 * Android与PHP的AES加解密
 * http://blog.csdn.net/yudajun/article/details/40481135
 * http://blog.sina.com.cn/s/blog_671d2e4f0101jvh2.html
 * https://actom.me/blog/aes-cbc-%e7%9b%b8%e4%ba%92%e5%8a%a0%e8%a7%a3%e5%af%86-javaphpc.html
 * @author junxu.wang
 * @d2015年12月12日
 * </pre>
 *
 */
public class AESA2P {
	private static final String mode = "AES/CBC/NoPadding";
	private static final String KEY_ALGORITHM = "AES";

	public static void main(String args[]) {

		try {
			String data = "wangkai^_^";
			String key = "wangjunxuwangjun";
			String enData;
			enData = encrypt(data, key);
			Log.d(TAG, "enData=" + enData);
			Log.d(TAG, "deData=" + desEncrypt(enData,key).trim());
//			Log.d(TAG, "deData=" + desEncrypt("fnswRzIB+9G5eJCQ3MlzLdi2rmVSK4olZIZAWy1b6k0=", "1234000000000000"));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static byte[] encrypt(byte[] dataBytes, byte[] key, byte[] iv)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(mode);
		int blockSize = cipher.getBlockSize();

		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength
					+ (blockSize - (plaintextLength % blockSize));
		}

		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
		SecretKeySpec keyspec = new SecretKeySpec(key, KEY_ALGORITHM);
		IvParameterSpec ivspec = new IvParameterSpec(iv);

		cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
		return cipher.doFinal(plaintext);
	}

	public static String encrypt(String data, String key)
			throws InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		byte[] dataBytes = data.getBytes();
		byte[] ivBytes = getKey();// iv.getBytes();
		byte[] encrypted = encrypt(dataBytes, key.getBytes(), ivBytes);
		printArray(ivBytes);
		printArray(encrypted);
		return getBase64(ivBytes, encrypted);
	}

	public static byte[] desEncrypt(byte[] encrypted, byte[] key, byte[] iv)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		printArray(encrypted);
		printArray(iv);
		Cipher cipher = Cipher.getInstance(mode);
		SecretKeySpec keyspec = new SecretKeySpec(key, KEY_ALGORITHM);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
		return cipher.doFinal(encrypted);
	}

	public static String desEncrypt(String data, String key)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] encrypted1 = Base64.decode(data, Base64.DEFAULT);
		printArray(encrypted1);
		byte[] original = desEncrypt(getEndata(encrypted1), key.getBytes(),
				getIv(encrypted1));
		return new String(original);
	}

	private static String getBase64(byte[] iv, byte[] enData) {
		byte[] all = new byte[iv.length + enData.length];
		System.arraycopy(iv, 0, all, 0, iv.length);
		System.arraycopy(enData, 0, all, iv.length, enData.length);
		return Base64.encodeToString(all, Base64.DEFAULT);
	}

	private static byte[] getIv(byte[] enAll) {
		byte[] iv = new byte[16];
		System.arraycopy(enAll, 0, iv, 0, iv.length);
		return iv;
	}

	private static byte[] getEndata(byte[] enAll) {
		byte[] enData = new byte[enAll.length - 16];
		System.arraycopy(enAll, 16, enData, 0, enData.length);
		return enData;
	}

	private static byte[] getKey() {
		KeyGenerator kgen = null;
		try {
			kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		kgen.init(128);
		SecretKey secretKey = kgen.generateKey();
		return secretKey.getEncoded();
	}

	private static final String TAG = "dddd";

	private static void printArray(byte[] data) {
		if (TAG == null) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("leng:").append(data.length).append(':');
		for (byte b : data) {
			sb.append(b).append(',');
		}
		Log.d(TAG, sb.toString());
	}
}
