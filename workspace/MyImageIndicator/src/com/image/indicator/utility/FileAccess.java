package com.image.indicator.utility;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 文件访问和操作类
 * @Description: 文件访问和操作类

 * @File: FileAccess.java

 * @Package com.image.indicator.utility

 * @Author Hanyonglu

 * @Date 2012-6-18 下午04:24:30

 * @Version V1.0
 */
public class FileAccess {
	/**
	 * String --> InputStream
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}
}
