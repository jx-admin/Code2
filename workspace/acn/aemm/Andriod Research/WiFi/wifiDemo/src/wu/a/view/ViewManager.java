package wu.a.view;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wjx on 2015/8/28.
 */
public interface ViewManager<T>{
    public static final String EMPTY = "";

    View createView(ViewGroup viewGroup);

    View getView();

    void reset();

    void setData(T data);

    void start();

    void stop();

    void destroy();

    /**
     * View 要关闭时回调
     */
    void setOnCloseListener(OnCloseListener onCloseListener);

    public static interface OnCloseListener {
        void onClose(ViewManager viewManager);
    }
}
