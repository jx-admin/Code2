
package com.act.mbanking;

import static com.act.mbanking.CommonUtilities.SENDER_ID;
import static com.act.mbanking.CommonUtilities.displayMessage;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.activity.MsgDialgActivty;
import com.act.mbanking.bean.PushNotificationValue;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.logic.RegisterNotificationService;
import com.act.mbanking.manager.OnPushNotificationClick;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.Utils;
import com.google.android.gcm.GCMBaseIntentService;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";
    private static boolean isSave;
    public static OnPushNotificationClick onPushNotificationClick;

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        registerNotificationService(context, registrationId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = "";
        PushNotificationValue pushNotificationValue = new PushNotificationValue();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
        	return ;
        }
        message = bundle.getString(Contants.PAYLOAD);
        pushNotificationValue = Utils.cutString(message);
        if(MainActivity.onResume){
        	Intent dialogIntent = new Intent(context, MsgDialgActivty.class);
        	dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	dialogIntent.putExtra("title", pushNotificationValue.getTitle());
        	dialogIntent.putExtra("content", pushNotificationValue.getMessage());
        	context.startActivity(dialogIntent);
        }else{
            // notifies user
            // generateNotification(context, pushNotificationValue);
            send(context, pushNotificationValue.getTitle(), pushNotificationValue.getTitle(),
                    pushNotificationValue.getMessage());
        }
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    public void send(Context context, String name, String title, String content) {
        // 1.得到NotificationManager
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        // 2.实例化一个通知，指定图标、概要、时间
        Notification n = new Notification(R.drawable.ic_launcher, name, System.currentTimeMillis());
        // 3.指定通知的标题、内容和intent
        Intent intent = new Intent(context, MsgDialgActivty.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        n.setLatestEventInfo(context, title, content, pi);
        // 指定声音
        n.defaults = Notification.DEFAULT_SOUND;
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        // 4.发送通知
        nm.notify((int)System.currentTimeMillis(), n);
    }

    private void registerNotificationService(Context context, String apnsToken) {
        String postData = RegisterNotificationService.RegisterNotificationServiceReportProtocal(
                Contants.publicModel, Utils.getIMEI(context), apnsToken);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, this);
        ResponsePublicModel responsePublicModel = RegisterNotificationService
                .ParseRegisterNotificationServiceResponse(httpResult);
        if (responsePublicModel.isSuccess() && isSave) {
            GCMIntentService.onPushNotificationClick.onPushNotification(true);
        }
        LogManager.d("notification : " + httpResult);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        // TODO Auto-generated method stub

    }

    public static void isSavePush(boolean isSave){
        GCMIntentService.isSave = isSave;
    }
    
    /**
     * @return the onPushNotificationClick
     */
    public OnPushNotificationClick getOnPushNotificationClick() {
        return onPushNotificationClick;
    }

    /**
     * @param onPushNotificationClick the onPushNotificationClick to set
     */
    public static void setOnPushNotificationClick(OnPushNotificationClick onPushNotificationClick) {
        GCMIntentService.onPushNotificationClick = onPushNotificationClick;
    }
}
