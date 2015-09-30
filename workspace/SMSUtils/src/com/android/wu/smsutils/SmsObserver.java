package com.android.wu.smsutils;

/**
 写一个短信监听程序，大家很容易想到的就是注册一个广播接收器，来接收系统收到短信时候的广播。
 可是这个广播是有序广播，就是当别的程序先获取到了这个广播再传递给你，
 当然它也可以干掉这个广播，让你接收不到，这样你的程序肯定是接收不到这个广播的了。网上有很多这样的例子，什么提高广播接收器的优先级又是1000又是10000的。。

 大家可以看看这个广播的基本介绍：http://wenku.baidu.com/view/226f9dd5b14e852458fb57ba.html

 这里提到了把intent-filter中的android:priority的值设置为1000，但是在实时中你设置1000一样是接受不到广播的，除非你把手机中的其他接受了这个广播的短信卸载，比如360 GO短信，飞信神马的。他们的优先级总是要比你的高。你可以继续BAIDU，有的人会设置它为10000 9999等等，你会发现设置为10000的时候偶尔可以接收到，仅仅是偶尔。那肿么办呢。

 我下面用的方法不是去接收系统广播，而是去监听短信数据库的变化，当收到短信数据库变化了的时候，去取得最新的那条短信即可。或者你想别的什么操作也可以。

 监听类SmsReceiver.java
 SmsContent.java内容请参考上篇http://www.2cto.com/kf/201203/123797.html博客
 */

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.log.CLog;

/**
 * class name：SmsReceiver<BR>
 * class description：数据库改变监听类<BR>
 * PS：当数据改变的时候，执行里面才change方法<BR>
 * Date:2012-3-16<BR>
 * 
 * @version 1.00
 * @author CODYY)peijiangping
 */
public class SmsObserver extends ContentObserver {
	public static final String TAG = SmsObserver.class.getName();

	public static final void registerContentObserver(Context context) {
		CLog.print(TAG,"register");
		ContentObserver co = new SmsObserver(new Handler(), context);
		context.getContentResolver().registerContentObserver(
				Uri.parse(SMSContants.SMS_URI_ALL), true, co);
	}

	/**
	 * Activity对象
	 */
	private Context context;

	public SmsObserver(Handler handler, Context context) {
		super(handler);
		this.context = context;
	}

	@Override
	public void onChange(boolean selfChange) {
		Log.d(TAG, "onChange：" + selfChange);
		Cursor srcCursor=SMSContants.getCursorNews(context, Coloums.SMSLog.SMS_CONTENT_URI);
		
		
		if (srcCursor != null && srcCursor.moveToFirst()) {
			ContentResolver mContentResolver = context.getContentResolver();
			String address = null;
			Message msg = null;
			do {
				if ((address = srcCursor.getString(Coloums.SMSLog.I_ADDRESS)) == null) {
					address = "00000";
				}
				 Log.d(TAG, "sms record->" +Utils.toStringOnce(srcCursor));
			} while (srcCursor.moveToNext());
		}
		srcCursor.close();
		
		super.onChange(selfChange);
	}
}
