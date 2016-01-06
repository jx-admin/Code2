package wu.phone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import wu.base.BaseBroadcastReceiver;
import wu.utils.LogUtils;

/**
 * 要在AndroidManifest.xml注册广播接收器:
 * <receiver android:name=".PhoneReceiver">
 * <intent-filter>
 * <action android:name="android.intent.action.PHONE_STATE"/>
 * <action android:name="android.intent.action.NEW_OUTGOING_CALL" />
 * </intent-filter>
 * 还要添加权限:
 * <uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>
 * <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"></uses-permission>
 * p.s拨出电话使用receiver outgoing_call.
 * phonglistener bi receiver READ_PHONE_STATE快一步。
 * 拨电话:outgoing（有号码）-接听（无号码）-挂断（无号码），[注，电话播出到运营商就为接听了，接听和挂断是phonelistener和READ_PHONE_STATE的状态]
 * 拒接来电：ring-挂断（拒接） 有号码
 * 应答来电：ring（有号码）-接听（无号码）-挂断（无号码）
 */
public class PhoneReceiver extends BaseBroadcastReceiver {
    private TelephonyManager tManager;

    @Override
    public IntentFilter getintentFilter() {
        return super.getintentFilter();
    }

    @Override
    public void register(Context context) {
        if (context != null) {
            tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
        }
        super.register(context);
        listener.register(context);
    }

    @Override
    public void unRegister() {
        super.unRegister();
        listener.unRegister();
        tManager = null;
    }

    private StringBuilder sb = new StringBuilder();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        sb.append("re:").append(action).append('\n');
        //如果是去电
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent
                    .getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            sb.append("out：").append(phoneNumber).append('\n');
        } else if ("android.intent.action.PHONE_STATE".equals(action)) {
            //查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
            //如果我们想要监听电话的拨打状况，需要这么几步 :
            /* 第一：获取电话服务管理器TelephonyManager manager = this.getSystemService(TELEPHONY_SERVICE);
            * 第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new MyPhoneStateListener(),
                    * PhoneStateListener.LISTEN_CALL_STATE);这里的PhoneStateListener.LISTEN_CALL_STATE就是我们想要
                    * 监听的状态改变事件，初次之外，还有很多其他事件哦。
            * 第三步：通过extends PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
            * 第四步：这一步很重要，那就是给应用添加权限。android.permission.READ_PHONE_STATE
            * */
// 如果是来电
            String mIncomingNumber;
            mIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            switch (tManager.getCallState()) {

                case TelephonyManager.CALL_STATE_RINGING:
                    sb.append("ringing:").append(mIncomingNumber).append('\n');
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
//                    if (mIncomingFlag) {
//                        Log.i(TAG, "incoming ACCEPT :" + mIncomingNumber);
//                    }
                    sb.append("OFFHOOK:").append(mIncomingNumber).append('\n');
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
//                    if (mIncomingFlag) {
//                        Log.i(TAG, "incoming IDLE");
//                    }
                    sb.append("IDLE:").append(mIncomingNumber).append('\n');
                    break;
            }
        }

        LogUtils.d(sb.toString());
        if (sb.length() > 0) {
            sb.delete(0, sb.length() - 1);
        }
    }

    /**
     * 实现手机电话状态的监听，主要依靠两个类：TelephoneManger和PhoneStateListener。
     * 注意：对手机的某些信息进行读取是需要一定许可（permission）的。
     * <p>
     * 它们对应PhoneStateListener.LISTEN_CALL_STATE所监听到的内容:
     * int CALL_STATE_IDLE   空闲状态，没有任何活动。
     * int CALL_STATE_OFFHOOK  摘机状态，至少有个电话活动。该活动或是拨打（dialing）或是通话，或是 on hold。并且没有电话是ringing or waiting
     * int CALL_STATE_RINGING  来电状态，电话铃声响起的那段时间或正在通话又来新电，新来电话不得不等待的那段时间。
     */
    MyPhoneStateListener listener = new MyPhoneStateListener();

    class MyPhoneStateListener extends PhoneStateListener {
        private Context context;

        public boolean isRegister() {
            return context != null;
        }

        public void register(Context context) {
            if (isRegister()) {
                return;
            }
            this.context = context;
            //设置一个监听器
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
            LogUtils.d("register phont listener");
        }

        public void unRegister() {
            if (context != null) {
                //设置一个监听器
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
                tm.listen(this, PhoneStateListener.LISTEN_NONE);
                context = null;
                LogUtils.d("unregister phont listener");
            }
        }

        private StringBuilder sb = new StringBuilder();

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            sb.append("onCallStateChanged").append('\n');
            //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    sb.append("挂断").append(incomingNumber).append('\n');
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    sb.append("接听").append(incomingNumber).append('\n');
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    sb.append("响铃:").append(incomingNumber).append('\n');
                    //输出来电号码
                    break;
            }
            LogUtils.d(sb.toString());
            if (sb.length() > 0) {
                sb.delete(0, sb.length() - 1);
            }
        }
    }

}
