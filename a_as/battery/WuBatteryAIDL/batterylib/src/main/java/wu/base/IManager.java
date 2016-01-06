package wu.base;

import android.content.Context;

/**
 * Created by jx on 2015/12/31.
 */
public interface IManager {
    void create(Context context);
    void start();
    void stop();
    void destroy();
}
