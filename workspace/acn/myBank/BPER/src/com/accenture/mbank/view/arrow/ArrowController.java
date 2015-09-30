package com.accenture.mbank.view.arrow;

import it.gruppobper.ams.android.bper.R;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.accenture.mbank.util.LogManager;

/*
 * Class for the Animation of the blinking arrows at left side
 */
public class ArrowController {

	ScrollView scroll;

	public int THREADHOLD = 2;
	private int ALPHA = 1;  // some small value to make sure the judge is correct

	public void setScrollView(ScrollView scroll)
	{
		this.scroll = scroll;
	}

	AnimationDrawable animUp = null;
	AnimationDrawable animDown = null;
	public boolean flag = false;
	public void updateArrowState() {
		if (scroll == null)
			return;

		Activity mainActivity = (Activity) scroll.getContext();

		View vUpAnim = mainActivity.findViewById(R.id.arrow_up_anim);
		View vDownAnim = mainActivity.findViewById(R.id.arrow_down_anim);

		if (vUpAnim == null || vDownAnim ==null)
			return;

		View vUp = mainActivity.findViewById(R.id.arrow_up);
		View vDown = mainActivity.findViewById(R.id.arrow_down);
		
		if (vUp == null || vDown ==null)
			return;

		vUp.setBackgroundResource(R.drawable.arrow_blink_up);
		vDown.setBackgroundResource(R.drawable.arrow_blink_down);
		
		if (animUp == null || animDown == null) {
			startArrowAnimation();
		}

		int scrollY = scroll.getScrollY(); // scroll view above the seen area
		int height = scroll.getHeight(); // scroll view seen area
		int mearsuredheight = scroll.getChildAt(0).getMeasuredHeight(); // content
																		// total
																		// height
		LogManager.d("-----------------------------Scroll Height" + mearsuredheight);
		LogManager.d("-----------------------------scrollY" + scrollY);
		LogManager.d("-----------------------------height" + height);
		int child = ((ViewGroup)scroll.getChildAt(0)).getChildCount();
		if (child > THREADHOLD) {
			vDownAnim.setVisibility(View.VISIBLE);
			vUpAnim.setVisibility(View.VISIBLE);
		} else {
			vDownAnim.setVisibility(View.INVISIBLE);
			vUpAnim.setVisibility(View.INVISIBLE);
			vUp.setVisibility(View.INVISIBLE);
			vDown.setVisibility(View.INVISIBLE);
			return;
		}
		
		if (scrollY <= 0 + ALPHA) {
//			animUp.stop();
			if(mearsuredheight <= height && flag){
				vDown.setVisibility(View.VISIBLE);
				vDownAnim.setVisibility(View.INVISIBLE);

				vUp.setVisibility(View.VISIBLE);
				vUpAnim.setVisibility(View.INVISIBLE);
			}else {
				vUp.setVisibility(View.VISIBLE);
				vUpAnim.setVisibility(View.INVISIBLE);
			}
		} else if (mearsuredheight <= scrollY + height + ALPHA) {
//			animDown.stop();
			vDown.setVisibility(View.VISIBLE);
			vDownAnim.setVisibility(View.INVISIBLE);
		} else {
			vDownAnim.setVisibility(View.VISIBLE);
			vUpAnim.setVisibility(View.VISIBLE);

			vUp.setVisibility(View.INVISIBLE);
			vDown.setVisibility(View.INVISIBLE);
		}
	}

	private void startArrowAnimation() {
		Activity mainActivity = (Activity) scroll.getContext();
		View vUpAnim = mainActivity.findViewById(R.id.arrow_up_anim);

		vUpAnim.setBackgroundResource(R.anim.arrow_up);
		animUp = (AnimationDrawable) vUpAnim.getBackground();
		
		View vDown = mainActivity.findViewById(R.id.arrow_down_anim);
		vDown.setBackgroundResource(R.anim.arrow_down);
		animDown = (AnimationDrawable) vDown.getBackground();

		animUp.start();
		animDown.start();
	}
}
