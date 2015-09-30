



package org.accenture.product.lemonade;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class HandleView extends ImageView {
    private static final int ORIENTATION_HORIZONTAL = 1;

    private Launcher mLauncher;
    private int mOrientation = ORIENTATION_HORIZONTAL;

    public HandleView(Context context) {
        super(context);
    }

    public HandleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HandleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HandleView, defStyle, 0);
        mOrientation = a.getInt(R.styleable.HandleView_direction, ORIENTATION_HORIZONTAL);
        a.recycle();

        setContentDescription(context.getString(R.string.all_apps_button_label));
    }

    @Override
    public View focusSearch(int direction) {
        View newFocus = super.focusSearch(direction);
        if (newFocus == null && !mLauncher.isAllAppsVisible()) {
            final Workspace workspace = mLauncher.getWorkspace();
            workspace.dispatchUnhandledMove(null, direction);
            return (mOrientation == ORIENTATION_HORIZONTAL && direction == FOCUS_DOWN) ?
                    this : workspace;
        }
        return newFocus;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && mLauncher.isAllAppsVisible()) {
            return false;
        }
        return super.onTouchEvent(ev);
    }


    void setLauncher(Launcher launcher) {
        mLauncher = launcher;
    }
}
