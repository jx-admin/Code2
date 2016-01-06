package wu.battery;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import wu.base.BaseBroadcastReceiver;
import wu.utils.LogUtils;

/**
 * Created by jx on 2015/12/23.
 */
public class PhoneStateReceiver extends BaseBroadcastReceiver {

    private StringBuilder sb = new StringBuilder();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        sb.append("re:").append(action).append('\n');
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
//            Intent i = new Intent(context, BatteryService.class);
//            i.putExtra(BatteryService.CMD_TYPE, BatteryService.CMD_ACTION_NEW_OUTGOING_CALL);
//            context.startService(i);
            String phoneNumber = intent
                    .getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            sb.append("out：").append(phoneNumber).append('\n');
        } else if ("android.intent.action.PHONE_STATE".equals(action)) {
            String mIncomingNumber;
            mIncomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()) {

                case TelephonyManager.CALL_STATE_RINGING:
//                    Intent i = new Intent(context, BatteryService.class);
//                    i.putExtra(BatteryService.CMD_TYPE, BatteryService.CMD_CALL_STATE_RINGING);
//                    context.startService(i);
                    sb.append("ringing:").append(mIncomingNumber).append('\n');
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    sb.append("OFFHOOK:").append(mIncomingNumber).append('\n');
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
//                    i = new Intent(context, BatteryService.class);
//                    i.putExtra(BatteryService.CMD_TYPE, BatteryService.CMD_CALL_STATE_IDLE);
//                    context.startService(i);
                    sb.append("IDLE:").append(mIncomingNumber).append('\n');
                    break;
            }
        }

        LogUtils.d(sb.toString());
        if (sb.length() > 0) {
            sb.delete(0, sb.length() - 1);
        }
    }

}
