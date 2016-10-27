package wu.a.lib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import wu.a.lib.R;

/**
 * Created by jx on 2016/5/13.
 */
public class ReHeightFramelayout extends FrameLayout {
    private float heightMultiplier;

    public ReHeightFramelayout(Context context) {
        this(context, null);
    }

    public ReHeightFramelayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReHeightFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReHeightFramelayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.MultiplierSizeViewStyle, defStyle, 0);
        if (null != a) {
            try {
                heightMultiplier = a.getFloat(R.styleable.MultiplierSizeViewStyle_heightMultiplier, 0);
            } finally {
                a.recycle();
            }
        }
    }

    public void setHeightMultiplier(float hscale) {
        this.heightMultiplier = hscale;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (heightMultiplier != 0) {
            int width = getMeasuredWidth();
            int height = (int) (width * heightMultiplier);
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            );
        }
    }
}
