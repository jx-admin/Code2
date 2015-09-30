package wu.a.lib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

/**不规则"图形按钮控件",屏蔽透明区域
 * @author junxu.wang
 *
 */
public class TrapezoidImageButton extends ImageButton {

	public TrapezoidImageButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public TrapezoidImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TrapezoidImageButton(Context context) {
		super(context);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isTouchPointInView(event.getX(), event.getY())
				|| event.getAction() != MotionEvent.ACTION_DOWN) {
			return super.onTouchEvent(event);
		} else {
			return false;
		}
	}

	/**
	 * 不规则图形按钮非透明区的点击
	 * 
	 * @param localX
	 * @param localY
	 * @return
	 */
	protected boolean isTouchPointInView(float localX, float localY) {
		boolean result = false;
		// Drawable bk=getDrawable();
		// BitmapDrawable bd=(BitmapDrawable) bk;
		// Bitmap bitmap=bd.getBitmap();
		int x = (int) localX;
		int y = (int) localY;
		if (x < 0 || x >= getWidth())
			return false;
		if (y < 0 || y >= getHeight())
			return false;
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		int pixel = bitmap.getPixel(x, y);
		bitmap.recycle();
		if ((pixel & 0xff000000) != 0) { // 点在非透明区
			return true;
		} else {
			return false;
		}
	}
}
