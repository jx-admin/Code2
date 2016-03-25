package wu.a.lib.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

/**
 * Created by jx on 2015/12/23.
 */
public abstract class BaseBroadcastReceiver extends BroadcastReceiver implements IReceiver {
    private Context context;

    public boolean isRegister() {
        return context != null;
    }

    public IntentFilter getintentFilter() {
        return null;
    }

    public void register(Context context) {
        if (isRegister()) {
            return;
        }
        if (context != null) {
            IntentFilter filter = getintentFilter();
            if (filter != null) {
                context.registerReceiver(this, getintentFilter());
                this.context = context;
            }
        }
    }

    public void unRegister() {
        if (context != null) {
            context.unregisterReceiver(this);
            context = null;
        }
    }
}
