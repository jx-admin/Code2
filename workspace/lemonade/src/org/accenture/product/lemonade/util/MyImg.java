package org.accenture.product.lemonade.util;

import java.io.IOException;
import java.io.InputStream;

import org.accenture.product.lemonade.R;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

public class MyImg
{

	/**
	 * 倒影
	 * 
	 * @param originalImage
	 * @return
	 */
	public static Bitmap createReflectedImage(Bitmap originalImage,boolean isShadow)
	{
		// The gap we want between the reflection and the original image
		final int reflectionGap = 0;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// This will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);		

		// Create a Bitmap with the flip matrix applied to it.
		// We only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height - height /3, width, height /3, matrix, false);	//?

		// Create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 3), Config.ARGB_8888);

		// Create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		
		// Draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);		
		
		// Draw in the reflection
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		// Create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
				+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);		//颜色控制
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
//		paint.setXfermode(new AvoidXfermode(Color.RED, 10, AvoidXfermode.Mode. AVOID));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);		

		if(isShadow){
			Paint shadow=new Paint();
			LinearGradient shadowShader = new LinearGradient(0, 0, 15, 0
					, 0xff000000, 0x00000000, TileMode.CLAMP);		//颜色控制
			shadow.setShader(shadowShader);
			shadow.setXfermode(new PorterDuffXfermode(Mode.DARKEN));
			canvas.drawRect(0, 0, 15, height, shadow);
		}
		
		return bitmapWithReflection;
	}
	
	public static Bitmap createRedBitmap(Bitmap orgImage){
		Bitmap result=Bitmap.createBitmap(orgImage);
		Canvas canvas=new Canvas(result);
		Paint paint=new Paint();
		paint.setColor(Color.RED);
		paint.setAlpha(120);
		canvas.drawRect(5, 5, result.getWidth()-8, result.getHeight()-3, paint);
//		canvas.drawRect(0, 0, result.getWidth(), result.getHeight(), paint);
		return result;
	}
	
	public static Bitmap createThumbtackImage(Context context,Bitmap orgImage){ 
		Bitmap result=Bitmap.createBitmap(orgImage.getWidth()+10, orgImage.getHeight()+20, Config.ARGB_8888);
		Canvas canvas=new Canvas(result);
		Paint paint=new Paint();
		
		canvas.drawBitmap(orgImage, 0, 0, paint);
		
		InputStream is = context.getResources().openRawResource(R.drawable.screenswitcher_lock_screen_title);
		BitmapDrawable bd = new BitmapDrawable(is);
		
		try
		{
			is.close();
		}
		catch (IOException e)
		{
			Log.e("createThumbtackImage",e.getMessage());
		}
		
//		NinePatchDrawable bitmapDrawable=(NinePatchDrawable)context.getResources().getDrawable(R.drawable.screenswitcher_lock_screen_title);
		canvas.drawBitmap(bd.getBitmap(), result.getWidth()-25, -20, paint);
//			canvas.drawRect(0, 0, result.getWidth(), result.getHeight(), paint);
//		
//		Paint shadow=new Paint();
//		LinearGradient shadowShader = new LinearGradient(0, 0, 0, 15
//				, 0xff000000, 0xff000000, TileMode.CLAMP);		//颜色控制
//		shadow.setShader(shadowShader);
//		shadow.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
//		canvas.drawRect(0, 0, orgImage.getWidth(), 15, shadow);
		
		return result;
	}
	
	public static Bitmap createGrayImage(Bitmap originalImage)
	{
	
		Canvas canvas = new Canvas(originalImage);

		//蒙板
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, 0, 0, originalImage.getHeight()
				, Color.WHITE, Color.WHITE, TileMode.CLAMP);		//颜色控制
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
//		paint.setXfermode(new AvoidXfermode(Color.RED, 10, AvoidXfermode.Mode. AVOID));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, 0, originalImage.getWidth(), originalImage.getHeight(), paint);
		
		
		// The gap we want between the reflection and the original image
//		final int reflectionGap = 4;
		// Create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		
		// Draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);

		return originalImage;
	}

}
