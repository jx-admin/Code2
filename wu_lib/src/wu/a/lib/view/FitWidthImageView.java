package wu.a.lib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * <pre>
 * 已View的高为准，缩放view的宽度以显示全部内容
 * @author junxu.wang
 * @d2015年5月31日
 * </pre>
 */
public class FitWidthImageView extends ImageView {
    float imageWidth;

    float imageHeight;

    public FitWidthImageView(Context context) {
        super(context);
    }

    public FitWidthImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitWidthImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable d=getDrawable();
        if(d!=null){
        	int width = d.getIntrinsicWidth();
        	int height = d.getIntrinsicHeight();
        	
        	int currentWidth = getMeasuredWidth();
        	int currentHeight = getMeasuredHeight();
        	
        	float xx = (float)currentHeight / (float)height;
        	currentWidth = (int)(xx * width);
        	setMeasuredDimension(currentWidth, currentHeight);
        }
    }
}
