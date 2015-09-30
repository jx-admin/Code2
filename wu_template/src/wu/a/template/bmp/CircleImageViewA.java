package wu.a.template.bmp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

/**通过截取画布一个圆形区域与图片的相交部分进行绘制，缺点：锯齿明显，设置Paint,Canvas抗锯齿无效。
 * @author junxu.wang
 *
 * @date : 2015年4月14日 下午6:13:31
 *
 */
public class CircleImageViewA extends View {

	public CircleImageViewA(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CircleImageViewA(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleImageViewA(Context context) {
		super(context);
	}

	private Bitmap bitmap;
	private Rect bitmapRect = new Rect();
	private PaintFlagsDrawFilter pdf = new PaintFlagsDrawFilter(0,
			Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	private Paint paint = new Paint();
	{
		paint.setStyle(Paint.Style.STROKE);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
	}
	private Path mPath = new Path();

	public void setImageBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (null == bitmap) {
			return;
		}
		bitmapRect.set(0, 0, getWidth(), getHeight());
		canvas.save();
		canvas.setDrawFilter(pdf);
		mPath.reset();
		canvas.clipPath(mPath); // makes the clip empty
		mPath.addCircle(getWidth() / 2, getWidth() / 2, getHeight() / 2,
				Path.Direction.CCW);
		canvas.clipPath(mPath, Region.Op.REPLACE);
		canvas.drawBitmap(bitmap, null, bitmapRect, paint);
		canvas.restore();
	}
}
