package com.android.accenture.aemm.express.updataservice;



import java.util.List;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.accenture.aemm.express.Main;
import com.android.accenture.aemm.express.R;
/**
 * This class provides methods for show notification
 * @author vivian.yun.feng
 *
 */
public class MessageNotification {
	public static final int NOTIFICATION_ID_MESSAGE = 2;
	private static MessageNotification sInstance = null;
	private Context mContext;
	
	  
//    String title = "AEMM";
    
 
    /**
     * Get the MessageNotification instance
     */
    public synchronized static MessageNotification getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MessageNotification(context);
        }
        return sInstance;
    }
    
    /**
     * Private constructor (one time only)
     */
    private MessageNotification(Context context) {
        mContext = context;
//        title = mContext.getString(R.string.notification_message);//"AEMM";)
       
    }
    
    public void showNotification(String tickerText) {
    	
    	Intent m_Intent = new Intent(mContext,Main.class);  
        PendingIntent m_PendingIntent =
             PendingIntent.getActivity(mContext, 0, m_Intent, PendingIntent.FLAG_UPDATE_CURRENT);
    	 
        NotificationManager m_NotificationManager =
            (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	Notification m_Notification =
    		new Notification(R.drawable.icon,tickerText, System.currentTimeMillis());
    	
    	m_Notification.flags |= Notification.FLAG_SHOW_LIGHTS;
    	m_Notification.defaults |= Notification.DEFAULT_LIGHTS;
    	
    	m_Notification.setLatestEventInfo(mContext, mContext.getString(R.string.notification_message), tickerText, m_PendingIntent ); 
       	m_NotificationManager.notify(NOTIFICATION_ID_MESSAGE, m_Notification);
    	
    }
    public void cancelNotification(){
    	
    	NotificationManager m_NotificationManager =
            (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    	m_NotificationManager.cancel(NOTIFICATION_ID_MESSAGE);
    	
    }
    public boolean IsForeground(String processName)
    {
    	boolean bRet = false;
    	ActivityManager activityManager = (ActivityManager) mContext.getSystemService(
    			Context.ACTIVITY_SERVICE ); 
    	List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
    	for(RunningAppProcessInfo appProcess : appProcesses){    
    		if (appProcess.processName.equals(processName))
    		{
    			if(appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND){    
    				Log.i("Foreground App", appProcess.processName);    
    				bRet = true;
    			}
    			if(appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND){    
    				Log.i("IMPORTANCE_BACKGROUND App", appProcess.processName);  
    				bRet = false;
    			}
    			break;
    		}
    	} 
    	return bRet;
    }

}
