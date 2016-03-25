package wu.a.lib.base;

import android.content.Context;

/**
 * Created by jx on 2015/12/23.
 */
public interface IReceiver {

    public boolean isRegister();

    public void register(Context context);

    public void unRegister();
}
