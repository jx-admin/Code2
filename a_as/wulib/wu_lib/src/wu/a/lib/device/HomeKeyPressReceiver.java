package wu.a.lib.device;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.widget.Toast;

import wu.a.lib.base.BaseBroadcastReceiver;

/**
 * Created by jx on 2016/9/8.
 * doc:http://blog.csdn.net/com360/article/details/6663586
 * 现在的这种方式通过广播的方式监听home键，这个比较好使
 */
public class HomeKeyPressReceiver extends BaseBroadcastReceiver {
    String SYSTEM_REASON = "reason";
    String SYSTEM_HOME_KEY = "homekey";
    String SYSTEM_HOME_KEY_LONG = "recentapps";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_REASON);
            if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                //表示按了home键,程序到了后台
                Toast.makeText(context.getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)) {
                //表示长按home键,显示最近使用的程序列表
                Toast.makeText(context.getApplicationContext(), "home long pressed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public IntentFilter getintentFilter() {
        return new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
    }
}
