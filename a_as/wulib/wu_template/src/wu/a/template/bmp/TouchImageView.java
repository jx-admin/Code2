package wu.a.template.bmp;

import wu.a.template.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

/**android中手势操作图片的平移、缩放、旋转
 * @author junxu.wang
 *
 * @date : 2015年5月5日 下午7:20:36
 * 
 * from:http://blog.csdn.net/happy_bug/article/details/7895244
 *
 */
public class TouchImageView extends ImageView {

	float x_down = 0;
	float y_down = 0;
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	float oldRotation = 0;
	/**画布所用*/
	Matrix matrix = new Matrix();
	/**onTouch修改时临时所用*/
	Matrix matrix1 = new Matrix();
	/**onTouch修改前备份的Matrix*/
	Matrix savedMatrix = new Matrix();

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	int mode = NONE;

	boolean matrixCheck = false;
	/**是否允许边缘检测[缩放移动区域限制]*/
	private boolean matrixCheckAble=true;

	int widthScreen;
	int heightScreen;

	Bitmap gintama;

	public TouchImageView(Context context) {
		super(context);
		gintama = BitmapFactory.decodeResource(getResources(), R.drawable.face);
	}
	
	public void setBitmap(Bitmap bmp){
		this.gintama=bmp;
		invalidate();
	}

	protected void onDraw(Canvas canvas) {
		canvas.save();
		canvas.drawBitmap(gintama, matrix, null);
		canvas.restore();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		widthScreen = getWidth();
		heightScreen = getHeight();
	}

	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			x_down = event.getX();
			y_down = event.getY();
			savedMatrix.set(matrix);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			mode = ZOOM;
			oldDist = spacing(event);
			oldRotation = rotation(event);
			savedMatrix.set(matrix);
			midPoint(mid, event);
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == ZOOM) {
				matrix1.set(savedMatrix);
				float rotation = rotation(event) - oldRotation;
				float newDist = spacing(event);
				float scale = newDist / oldDist;
				matrix1.postScale(scale, scale, mid.x, mid.y);// 縮放
				matrix1.postRotate(rotation, mid.x, mid.y);// 旋轉
				matrixCheck = matrixCheck();
				if (matrixCheck == false) {
					matrix.set(matrix1);
					invalidate();
				}
			} else if (mode == DRAG) {
				matrix1.set(savedMatrix);
				matrix1.postTranslate(event.getX() - x_down, event.getY()
						- y_down);// 平移
				matrixCheck = matrixCheck();
				if (matrixCheck == false) {
					matrix.set(matrix1);
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		}
		return true;
	}

	float[] f = new float[9];
	private boolean matrixCheck() {
		matrix1.getValues(f);
		// 图片4个顶点的坐标
		float x1 = f[0] * 0 + f[1] * 0 + f[2];
		float y1 = f[3] * 0 + f[4] * 0 + f[5];
		float x2 = f[0] * gintama.getWidth() + f[1] * 0 + f[2];
		float y2 = f[3] * gintama.getWidth() + f[4] * 0 + f[5];
		float x3 = f[0] * 0 + f[1] * gintama.getHeight() + f[2];
		float y3 = f[3] * 0 + f[4] * gintama.getHeight() + f[5];
		float x4 = f[0] * gintama.getWidth() + f[1] * gintama.getHeight()
				+ f[2];
		float y4 = f[3] * gintama.getWidth() + f[4] * gintama.getHeight()
				+ f[5];
		// 图片现宽度
		double width = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		// 缩放比率判断[最小和最大缩放限制]
		if (width < widthScreen / 3 || width > widthScreen * 3) {
			return matrixCheckAble;
		}
		// 出界判断
		if ((x1 < widthScreen / 3 && x2 < widthScreen / 3
				&& x3 < widthScreen / 3 && x4 < widthScreen / 3)
				|| (x1 > widthScreen * 2 / 3 && x2 > widthScreen * 2 / 3
						&& x3 > widthScreen * 2 / 3 && x4 > widthScreen * 2 / 3)
				|| (y1 < heightScreen / 3 && y2 < heightScreen / 3
						&& y3 < heightScreen / 3 && y4 < heightScreen / 3)
				|| (y1 > heightScreen * 2 / 3 && y2 > heightScreen * 2 / 3
						&& y3 > heightScreen * 2 / 3 && y4 > heightScreen * 2 / 3)) {
			return matrixCheckAble;
		}
		return false;
	}

	/**
	 * <pre>
	 * 触碰两点间距离
	 * @param event
	 * @return
	 * </pre>
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * <pre>
	 * 取手势中心点
	 * @param point
	 * @param event
	 * </pre>
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/**
	 * <pre>
	 * 取旋转角度
	 * @param event
	 * @return
	 * </pre>
	 */
	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}

	// 将移动，缩放以及旋转后的图层保存为新图片
	// 本例中沒有用到該方法，需要保存圖片的可以參考
	public Bitmap CreatNewPhoto() {
		Bitmap bitmap = Bitmap.createBitmap(widthScreen, heightScreen,
				Config.ARGB_8888); // 背景图片
		Canvas canvas = new Canvas(bitmap); // 新建画布
		canvas.drawBitmap(gintama, matrix, null); // 画图片
		canvas.save(Canvas.ALL_SAVE_FLAG); // 保存画布
		canvas.restore();
		return bitmap;
	}

}
