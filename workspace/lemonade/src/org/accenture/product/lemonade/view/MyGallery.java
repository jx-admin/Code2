package org.accenture.product.lemonade.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Gallery;

public class MyGallery extends Gallery
{

//	int FLINGTHRESHOLD;
	int SPEED=400;
	
	public MyGallery(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyGallery(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
//		float scale = getResources().getDisplayMetrics().density;
//		FLINGTHRESHOLD = (int) (20.0f * scale + 0.5f);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{

		if (velocityX > SPEED)
		{
			return super.onFling(e1, e2, SPEED, velocityY);
		}
		else if (velocityX < -SPEED)
		{
			return super.onFling(e1, e2, -SPEED, velocityY);
		}
		else
		{
			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		// TODO Auto-generated method stub
		return super.onScroll(e1, e2, distanceX, distanceY);		
	}
	
	
}
