package wu.a.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import wu.a.lib.R;

/**
 * Created by Administrator on 2015/11/30.
 */
public class DrawableTextView extends TextView {
    int drawableSize;

    public DrawableTextView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context) {
        this(context, null, 0);
    }

    private void init(Context context, AttributeSet attrs,
                      int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.DrawableTextView, defStyle, 0);
        if (null != a) {
            try {
                drawableSize = (int) a.getDimension(R.styleable.DrawableTextView_leftDrawableSize, 0);
            } finally {
                a.recycle();
            }
        }
        if (drawableSize > 0) {
            Drawable[] drawables = getCompoundDrawables();
            boolean changed = false;
            for (Drawable drawable : drawables) {
                if (drawable != null) {
                    changed = true;
                    Rect src = drawable.getBounds();
                    src.set(src.left, src.left, src.left + drawableSize, src.left + drawableSize);
                }
            }
            if (changed) {
                setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        }
    }
}
