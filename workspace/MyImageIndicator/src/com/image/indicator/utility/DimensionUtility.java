package com.image.indicator.utility;

import android.content.Context;

/**
 * 单位之间的转换类
 * @Description: 单位之间的转换类

 * @File: DimensionUtility.java

 * @Package com.image.indicator.utility

 * @Author Hanyonglu

 * @Date 2012-6-18 上午07:59:22

 * @Version V1.0
 */
public class DimensionUtility {
	/**
	 * 根据手机的分辨率从 dp的单位转成为 px(像素)
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素)的单位转成为 dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
