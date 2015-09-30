package com.act.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

/**
 * @author real
 * 
 */
public class Utils {

	/**
	 * 把Cursor中的所有元素转换成String
	 * 
	 * @param mCursor
	 * @return
	 */
	public static String toString(Cursor mCursor) {
		String str = "";
		if (mCursor == null) {
			str = "cursor is null pointer";
		} else {
			final String _n = "\n", _t = "\t";
			int coloumnCount = mCursor.getColumnCount();
			int rowCount = mCursor.getCount();
			str = coloumnCount + "列" + rowCount + "行\n序号";
			for (int i = 0; i < coloumnCount; i++) {
				str += _t + mCursor.getColumnName(i);
			}
			// String coloumnNames[]=mCursor.getColumnNames();
			// for(String name:coloumnNames){
			// str+="\t "+name;
			// }
			if (mCursor.moveToFirst()) {
				int currentRow = 0;
				do {
					str += _n + currentRow;
					for (int j = 0; j < coloumnCount; j++) {
						str += _t + mCursor.getString(j);
					}
					currentRow++;
				} while (mCursor.moveToNext());
			}
		}
		return str;
	}

	/**
	 * 转换Cursor当前的元素，且不作游标移动操作
	 * 
	 * @param mCursor
	 * @return
	 */
	public static String toStringOnce(Cursor mCursor) {
		String str = "";
		if (mCursor == null) {
			str = "cursor is null pointer";
		} else {
			int coloumnCount = mCursor.getColumnCount();
			final String _t = "\t";
			for (int j = 0; j < coloumnCount; j++) {
				str += _t + mCursor.getString(j);
			}
		}
		return str;
	}

	/**
	 * 分别吐司Cursor中每一个元素
	 * 
	 * @param context
	 * @param mCursor
	 */
	public static void ToastCursor(Context context, Cursor mCursor) {
		String str;
		if (mCursor == null) {
			Toast.makeText(context, "cursor is null", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(context, "cursor size:" + mCursor.getCount(),
					Toast.LENGTH_SHORT).show();
		}
		int coloumnCount = mCursor.getColumnCount();
		int currentRow = 0;
		final String space = "  ";
		if (mCursor.moveToFirst()) {
			do {
				str = String.valueOf(currentRow);
				for (int j = 0; j < coloumnCount; j++) {
					str += space + mCursor.getString(j);
				}
				currentRow++;
				Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
			} while (mCursor.moveToNext());
		}
	}

	/**
	 * 时间转换：秒->时：分：秒
	 * 
	 * @param mss
	 * @return
	 */
	public static String formatDuring(long mss) {
		// long hours = mss /3600;// (60 * 60);
		// long minutes = (mss % (1000 * 60 * 60)) / 60;
		// long seconds = (mss % (1000 * 60));

		long miao = mss % 60;
		mss /= 60;
		long fen = mss % 60;
		mss /= 60;
		long shi = mss % 24;
		long tian = mss / 24;

		return
		// ":"+hours + ":" + minutes + ":" + seconds
		// +"\n"+
		tian + ":" + shi + ":" + fen + ":" + miao;
	}

	/**
	 * 时间--〉几天，几小时，几分前
	 * 
	 * @param callTime
	 * @return
	 */
	public static String getdays(long callTime) {
		int DAY = 60 * 24;
		String value = "";
		long newTime = new Date().getTime();
		long duration = (newTime - callTime) / (1000 * 60);
		if (duration < 60) {
			value = duration + "分钟前";
		} else if (duration < DAY) {
			value = (duration / 60) + "小时前";
		} else if (duration < DAY * 2) {
			value = "昨天";
		} else if (duration < DAY * 3) {
			value = "前天";
		} else if (duration < DAY * 7) {
			value = (duration / DAY) + "天前";
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("M月dd日");
			value = sdf.format(new Date(callTime));
		}
		return value;
	}

	public static String numberFormat(String telNo) {
		telNo = telNo.replace(" ", "");
		if (telNo.startsWith("+86")) {
			telNo = telNo.substring(3);
		}
		return telNo;
	}

	// 时间格式
	public static final SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat sfd2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat longSfd = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat slongSfd = new SimpleDateFormat(
			"yy-MM-dd HH:mm:ss");
}
