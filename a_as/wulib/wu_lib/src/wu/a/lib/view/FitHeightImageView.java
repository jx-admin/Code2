package wu.a.lib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * <pre>
 * 以View当前的宽度为准，调整View的高度，以适应图片全部显示。
 * @author junxu.wang
 * @d2015年5月31日
 * </pre>
 *
 */
public class FitHeightImageView extends ImageView {
    float imageWidth;

    float imageHeight;

    public FitHeightImageView(Context context) {
        super(context);
    }

    public FitHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitHeightImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable d=getDrawable();
        if(d!=null){
        	//获取图片的宽高
        	int width = getDrawable().getIntrinsicWidth();
        	int height = getDrawable().getIntrinsicHeight();
        	
        	int currentWidth = getMeasuredWidth();
        	int currentHeight = getMeasuredHeight();
        	
        	float xx = (float)currentWidth / (float)width;
        	currentHeight=(int)(xx*height);
        	setMeasuredDimension(currentWidth, currentHeight);
        }
    }
}
