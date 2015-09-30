package org.accenture.product.lemonade.view;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * 
 * @author shaohu.zhu
 * 
 */
public class ScreenSwitcherFactory implements ViewFactory
{

	Context context;

	public ScreenSwitcherFactory(Context context)
	{
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public View makeView()
	{
		// TODO Auto-generated method stub
		// ImageView i = new ImageView(context);
		// i.setMaxHeight(400);
		// i.setAdjustViewBounds(true);
		// i.setBackgroundColor(0xFF000000);
		// i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		// i.setLayoutParams(new ImageSwitcher.LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		// return i;

		LinearLayout linearLayout = new LinearLayout(context);
		//
		// Button phone=new Button(context);
		// phone.setText("电话");
		// linearLayout.addView(phone);
		//
		// Button application=new Button(context);
		// application.setText("应用程序");
		// linearLayout.addView(application);
		//
		// Button message=new Button(context);
		// message.setText("短信");
		// linearLayout.addView(message);
		//
		return linearLayout;

	}
}
