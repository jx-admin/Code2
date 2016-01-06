package wu.batteryaidl.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.util.Log;

/**
 * Created by Administrator on 2015/12/29.
 */
public class SmsAndMissedCallHelper {

    private static final String TAG = "@@@SmsAndCall";
    private ContentResolver mContentResolver;
    private ContentObserver mObserver;

    public SmsAndMissedCallHelper(Context context, Handler handler, final IMissedCallAndSmsCallback callback) {
        mContentResolver = context.getContentResolver();
        mObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                int missedSMS = getNewSmsCount();
                int missedCall = getMissedCallCount();
                if(callback != null) {
                    callback.onChange(missedSMS, missedCall);
                }
            }
        };
    }

    public void registerObservers() {
        unregisterObservers();
        mContentResolver.registerContentObserver(Uri.parse("content://sms"), true, mObserver);
        mContentResolver.registerContentObserver(CallLog.Calls.CONTENT_URI, true, mObserver);
    }

    public synchronized void unregisterObservers() {
        try {
            if (mObserver != null) {
                mContentResolver.unregisterContentObserver(mObserver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getNewSmsCount() {
        int result = 0;
        Cursor csr = mContentResolver.query(Uri.parse("content://sms"), null, "type = 1 and read = 0", null, null);
        if (csr != null) {
            result = csr.getCount();
            csr.close();
        }
        return result;
    }

    private int getMissedCallCount() {
        int count = 0;
        Cursor csr = mContentResolver.query(CallLog.Calls.CONTENT_URI, new String[]{CallLog.Calls.NUMBER, CallLog.Calls.TYPE, CallLog.Calls.NEW}, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (csr != null && csr.moveToFirst()) {
            int type = csr.getInt(csr.getColumnIndex(CallLog.Calls.TYPE));
            switch (type) {
                case CallLog.Calls.MISSED_TYPE:
                    if (csr.getInt(csr.getColumnIndex(CallLog.Calls.NEW)) == 1) {
                        Log.v(TAG, "you have a missed call");
                        count++;
                    }
                    break;
            }
            csr.close();
        }
        return count;
    }

    public static interface IMissedCallAndSmsCallback {
        public void onChange(int smsCount, int callCount);
    }

}
