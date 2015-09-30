package com.android.test.demo.anmation;

import com.android.test.demo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class Ballanim extends Activity implements AnimationListener
{
    private ImageView imageView;
    private Animation animation1;
    private Animation animation2;
    class A implements Interpolator
    {

		@Override
		public float getInterpolation(float input)
		{
		
			
			return 0;
		}
    	
    }
	@Override
	public void onAnimationEnd(Animation animation)
	{
		if(imageView.getAnimation() == animation1)
		    imageView.startAnimation(animation2);
		else
			imageView.startAnimation(animation1);
		
		
	}

	@Override
	public void onAnimationRepeat(Animation animation)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ballanim);
		imageView = (ImageView) findViewById(R.id.imageview);
		animation1 = AnimationUtils
				.loadAnimation(this, R.anim.alienin);
		animation2 = AnimationUtils
		.loadAnimation(this, R.anim.alienout);
		animation2.setAnimationListener(this);
		//animation.setRepeatCount(1);
		//animation.setRepeatMode(android.view.animation.Animation.INFINITE);
		animation1.setAnimationListener(this);
		
		imageView.startAnimation(animation1);

	}
}