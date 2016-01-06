package wu.a.view;

import android.content.Context;
import android.util.Log;
import android.view.View;

/**
 * Created by wjx on 2015/9/1.
 */
public abstract class BaseViewManager<T> implements ViewManager<T> {
    private static final String TAG = BaseViewManager.class.getSimpleName();
    protected Context context;
    protected View view;
    protected OnCloseListener mOnCloseListener;

    public BaseViewManager(Context context) {
        this.context = context;
    }

    public View getView() {
        if (view == null) {
            createView(null);
        }
        return view;
    }

    public void start() {

    }

    public void stop() {

    }

    protected void onClose() {
        destroy();
        if (mOnCloseListener != null) {
            Log.d(TAG, "onClose <--  call onclose");
            mOnCloseListener.onClose(this);
        }
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.mOnCloseListener = onCloseListener;
    }
}
