package wu.a.lib.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by jx on 2016/1/8.
 */
public class Manager {
    protected View view;
    protected Context context;

    public Manager(Context context) {
        this.context = context;
    }

    public int getLayoutId() {
        return 0;
    }

    public void create(View view) {
        this.view = view;
    }

    public View getView() {
        if (view == null && getLayoutId() > 0) {
            View layout = LayoutInflater.from(context).inflate(getLayoutId(), null);
            create(layout);
        }
        return view;
    }

    public void start() {

    }

    public void stop() {

    }

    public boolean onBackPressed() {
        return false;
    }
}
