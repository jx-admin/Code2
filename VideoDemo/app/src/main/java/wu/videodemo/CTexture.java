package wu.videodemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by jx on 2016/4/22.
 */
public class CTexture extends TextureView {
    public CTexture(Context context) {
        super(context);
    }

    public CTexture(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CTexture(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void unlockCanvasAndPost(Canvas canvas) {
        super.unlockCanvasAndPost(canvas);
    }
}
