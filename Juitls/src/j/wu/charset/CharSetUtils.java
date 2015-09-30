package j.wu.charset;

import j.wu.utils.Log;
import j.wu.utils.Logger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * <pre>
 * 字符解码工具
 * UTF-8文件编码
 * GB2312==GBK
 * 中文@@ :GB2312 to GB2312
 * 中文@@ :GB2312 to GBK
 * 中文@@ :GBK to GB2312
 * 中文@@ :GBK to GBK
 * 
 * UTF-16:UTF-16BE
 * 中文@@ :UTF-16BE to UTF-16BE
 * ﻿中文@@ :UTF-16 to UTF-16BE
 * 中文@@ :UTF-16 to UTF-16
 * 
 * 中文@@ :UTF-8 to UTF-8
 * @author Administrator
 *
 */
public class CharSetUtils {

	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁 */
	public static final String US_ASCII = "US-ASCII";

	/** ISO 拉丁字母No.1，也叫作 ISO-LATIN-1 */
	public static final String ISO_8859_1 = "ISO-8859-1";

	/** 8 UCS 转换格式 */
	public static final String UTF_8 = "UTF-8";

	/** 16 UCS 转换格式，Big Endian（最低地 存放高位字节）字节顺 */
	public static final String UTF_16BE = "UTF-16BE";

	/** 16 UCS 转换格式，Little-endian（最高地 存放低位字节）字节顺 */
	public static final String UTF_16LE = "UTF-16LE";

	/** 16 UCS 转换格式，字节顺序由可 �的字节顺序标记来标 */
	public static final String UTF_16 = "UTF-16";

	/** 中文超大字符 */
	public static final String GBK = "GBK";

	public static final String GB2312 = "GB2312";

	public static final String GB18030 = "GB18030";

	public static final String BIG5 = "BIG5";

	public static final String[] CHARSETS = { US_ASCII, GB2312, ISO_8859_1,
			GBK, UTF_16BE, UTF_16LE, UTF_16, UTF_8, BIG5 };

	public static void main(String str[]) {
		Charset.forName("SHIFT_JIS");
		charset("中国@@");
	}

	public static String charset(String str) {
		String strDecode = "";
		try {
			for (String charset : CharSetUtils.CHARSETS) {
				// System.out.println(str + "-------" + charset);
				// printByte(str.getBytes(), ":默认");
				// byte[] bytes = str.getBytes(charset);
				// printByte(str.getBytes(charset), charset);
				// System.out.println(new String(bytes, charset) + " :" +
				// charset
				// + " to " + charset);

				// 直接转换
				// for (String charset2 : CharSet.CHARSETS) {
				// System.out.println(new String(str.getBytes(charset),
				// charset2) + " :" + charset + " to " + charset2);
				// }
				// 解码函数
				strDecode = decode(str, charset);
				if (strDecode.length() > 0) {
					break;
				}
				// 单次乱码
				// String str1=new String(str.getBytes(charset));
				// System.out.println(str1+"------"+charset);
				// for (String charset2 : CharSet.CHARSETS) {
				// reset(str,charset2);
				// }
			}
			System.out.println(strDecode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return strDecode;
	}

	public static void printByte(byte[] array, String str) {
		for (byte b : array) {
			System.out.print(b + ",");
		}
		System.out.println(" " + str);
	}

	/**
	 * 字符解码
	 * 
	 * @param str
	 *            字符串
	 * @param encode
	 *            解码方式
	 * @return 解码结果，失败返回""
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String str, String encode)
			throws UnsupportedEncodingException {
		String nStringr = "";
		byte[] datas = str.getBytes(encode);
		if (str.equals(new String(datas, encode))) {
			if (GB2312.equals(encode) || GBK.equals(encode)
					|| UTF_16BE.equals(encode) || UTF_16LE.equals(encode)
					|| UTF_16.equals(encode) || UTF_8.equals(encode)) {
				nStringr = new String(datas, encode);
			} else if (ISO_8859_1.equals(encode)) {
				String newSinger = new String(datas, GB2312);
				if (newSinger.equals(new String(newSinger.getBytes(GB2312),
						GB2312)) && !isUTF_8Code(datas)) {
					byte[] newDatas = newSinger.getBytes(UTF_8);
					nStringr = new String(newDatas, UTF_8);
					Logger.d("ddd", "iso_8859_1---GB2312");
				} else {
					nStringr = new String(datas, UTF_8);
					Logger.d("ddd", "iso_8859_1---utf_8");
				}
			} else {
				String newSinger = new String(datas, GB2312);
				byte[] newDatas = newSinger.getBytes(UTF_8);
				nStringr = new String(newDatas, UTF_8);
			}
		}
		Log.d("ddd", nStringr);
		return nStringr;
	}

	/**
	 * UTF-8 编码方式判断
	 * 
	 * @param datas
	 * @return
	 */
	public static boolean isUTF_8Code(byte datas[]) {
		byte b1 = (byte) 0x80;
		byte b2 = (byte) 0xc0;
		boolean result = false;
		if (datas != null && datas.length > 0) {
			byte charBytes = 0;
			for (byte b : datas) {
				int d = b & 0x000000ff;
				String bs = b + ":" + Integer.toBinaryString(d);

				if ((charBytes & b2) == 0) {// 0字节
					if (b1 == (b & b2)) {// 1字节
						result = false;
						Log.d("ddd", bs + "非法1字节");
						break;
					} else if (b2 == (b & b2)) {// 多字节
						charBytes = (byte) (b << 1);
						Log.d("ddd", bs + "开始多字节");
						result = true;
					} else {// 0字节
						charBytes = b;
						Log.d("ddd", bs + "单字节");
					}
				} else {
					if (b1 == (b & b2)) {
						charBytes <<= 1;
						Log.d("ddd", bs + ":1字节");
					} else {
						result = false;
						Log.d("ddd", bs + "非法1字节");
						break;
					}
				}
			}
		} else {
			result = false;
		}

		return result;
	}

	/*
	 * int p = (bin.read() << 8) + bin.read();
	 * 
	 * String code = null;
	 * 
	 * Log.e("lyric","p="+p);
	 * 
	 * switch (p)
	 * 
	 * { case 0xefbb: code = "UTF-8"; break; case 0xfffe: code = "Unicode";
	 * 
	 * break; case 0xfeff:
	 * 
	 * code = "UTF-16BE"; break; default: code = "GBK"; }
	 */
}
