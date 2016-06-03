package wu.a.lib.view;

import wu.a.lib.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.view.View;

public class PathShapeView extends View{
	public PathShapeView(Context context) {
		super(context);
	}

	ShapeDrawable mDrawable;

	protected void onDraw(Canvas canvas){
		drawBitmapShader(canvas);
		
		canvas.save();
		canvas.translate(10, 300);
		drawPath(canvas);
		canvas.restore();
	}
	
	private void drawBitmapShader(Canvas canvas){
		Path path = new Path();

		path.moveTo(50, 50);
		path.lineTo(100, 50);
		path.lineTo(100, 0);
		path.lineTo(150, 0);
		path.lineTo(150, 50);
		path.lineTo(200, 50);
		path.lineTo(200, 100);
		path.lineTo(250, 100);
		path.lineTo(250, 150);

		path.lineTo(200, 150);

		path.lineTo(200, 200);
		path.lineTo(150, 200);
		path.lineTo(150, 250);
		path.lineTo(100, 250);
		path.lineTo(100, 200);
		path.lineTo(50, 200);
		path.lineTo(50, 150);

		path.lineTo(0, 150);

		path.lineTo(0, 100);

		path.lineTo(50, 100);
		path.close();

		//生成多边形
		mDrawable = new ShapeDrawable(new PathShape(path, 250, 250));

		//mDrawable.getPaint().setColor(Color.RED);
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.girl);
		float w=255.0f/bitmap.getWidth();
		float h=255.0f/bitmap.getHeight();
		Matrix matrix=new Matrix();
		if(w<h){
			w=h;
		}
		matrix.setScale(w,w);

		Bitmap b1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),matrix,false); 
		//生成符合多边形大小的位图，避免出现分辨率问题

		Shader aShader = new BitmapShader(b1, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
		mDrawable.getPaint().setShader(aShader); //填充位图
		mDrawable.setBounds(0,0,250,250); //设置边界尺寸
		mDrawable.draw(canvas);
	}
	
	private void drawPath(Canvas canvas){
		Path path = new Path();
		Paint paint=new Paint();
		paint.setStyle(Style.FILL);

		path = new Path();
		path.moveTo(0, 10);
		path.lineTo(30, 60);
		path.lineTo(40, 50);
		path.lineTo(60, 120);
		path.lineTo(90, 70);
		path.lineTo(120, 60);
		path.lineTo(150, 130);
		path.lineTo(150, 0);
		path.lineTo(0, 0);
		path.close();
		paint.setColor(0xff0000ff);
		canvas.drawPath(path,paint);
		
		path.reset();
		path.moveTo(0, 0);
		path.lineTo(30, 50);
		path.lineTo(40, 40);
		path.lineTo(60, 80);
		path.lineTo(90, 10);
		path.lineTo(120, 50);
		path.lineTo(150, 80);
		path.lineTo(150, 0);
		path.lineTo(0, 0);
		path.close();
		paint.setColor(0xff00ff00);
		canvas.drawPath(path,paint);
	}

}
