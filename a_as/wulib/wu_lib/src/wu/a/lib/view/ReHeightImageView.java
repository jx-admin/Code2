package wu.a.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import wu.a.lib.R;

public class ReHeightImageView extends ImageView {

    static String TAG = "ReHeightImageView";
    private float heightMultiplier;

    public ReHeightImageView(Context context) {
        super(context);
    }

    public ReHeightImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
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

    public void setHeightMultiplier(float heightMultiplier) {
        this.heightMultiplier = heightMultiplier;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int currentWidth = getMeasuredWidth();
        int currentHeight = getMeasuredHeight();
        if (heightMultiplier == 0) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                //获取图片的宽高
                int width = getDrawable().getIntrinsicWidth();
                int height = getDrawable().getIntrinsicHeight();
                float xx = (float) (currentWidth - getPaddingLeft() - getPaddingRight()) / (float) width;
                currentHeight = (int) (xx * height);
            }
        } else {
            currentHeight = (int) (heightMultiplier * (currentWidth - getPaddingLeft() - getPaddingRight()));
        }
        setMeasuredDimension(currentWidth, currentHeight + getPaddingTop() + getPaddingBottom());
    }
}
