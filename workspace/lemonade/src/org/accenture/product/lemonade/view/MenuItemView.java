package org.accenture.product.lemonade.view;

import java.io.InputStream;

import org.accenture.product.lemonade.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

public class MenuItemView extends View
{
	Bitmap separater_horizontal;
	Bitmap separater_vertical;
	
	private static final String TAG="MenuItemView";
	
	public MenuItemView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		
		
		setBackgroundResource(R.drawable.menu_bar_background);
		
		InputStream inputStream=getResources().openRawResource(R.drawable.menu_bar_separater_horizontal);
		BitmapDrawable bitmapDrawable=new BitmapDrawable(inputStream);
		separater_horizontal=bitmapDrawable.getBitmap();
		
		inputStream=getResources().openRawResource(R.drawable.menu_bar_separater_vertical);
		bitmapDrawable=new BitmapDrawable(inputStream);
		separater_vertical=bitmapDrawable.getBitmap();
		
		try{
			inputStream.close();
		}catch (Exception e) {
			Log.e(TAG,e.getMessage());
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
	
		Paint paint=new Paint();
		int heightCenter=getHeight()/2;
		canvas.drawBitmap(separater_horizontal, 0, heightCenter, paint);
		
		int widthCenter=getWidth()/2;
		int widthOneThird=getWidth()/3;
		canvas.drawBitmap(separater_horizontal, widthCenter, 0, paint);
		
		canvas.drawBitmap(separater_horizontal, widthOneThird, heightCenter, paint);
		canvas.drawBitmap(separater_horizontal, widthOneThird*2, heightCenter, paint);
		
		super.onDraw(canvas);
	}
}
