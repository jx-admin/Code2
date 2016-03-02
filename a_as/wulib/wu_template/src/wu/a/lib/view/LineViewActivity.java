package wu.a.lib.view;

import wu.a.lib.view.WheelButton.OnItemClickListener;
import wu.a.template.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class LineViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout l = new LinearLayout(this);
		l.setOrientation(LinearLayout.VERTICAL);
		ScrollView sv = new ScrollView(this);
		sv.addView(l);
		this.setContentView(sv);
		testCharButton(this, l);
		testLineView(this, l);
		testCircleLinebutton(this, l);
	}

	private void testCharButton(Context context, ViewGroup root) {
		WheelButton cb = new WheelButton(context);
		LayoutParams lp = new LayoutParams(300, 500);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		root.addView(cb, lp);
		WheelButtonItem chile;
		for (int i = 0; i < 6; i++) {
			chile = new WheelButtonItem();
			chile.weight = (int) (Math.random() * 100);
			chile.backgroundColor = (int) (Math.random() * 0xffffff) + 0xff000000;
			chile.textColor = (0xffffffff - chile.backgroundColor) | 0xff000000;
			chile.text = "btn" + i;
			if (i < 3) {
//				cb.addCenterButton(chile);
			} else if (i == 1) {
				chile.backgroundColor = 0xffffffff;
				chile.clickAble = false;
				chile.visibility = false;
				chile.text = "";
			} else
				cb.addCircleButton(chile);
		}
		 cb.setmCenterUserWeightCount(100);
		 chile=new WheelButtonItem();
		 chile.weight=(int) (Math.random() * 100);
		 chile.backgroundColor=(int) (Math.random()*0xffffff)+0xff000000;
		 chile.textColor=(0xffffffff-chile.backgroundColor)|0xff000000;
		 chile.text="btn"+chile.weight;
		 cb.addCenterButton(chile);
		// chile=new WheelButtonItem();
		// chile.weight=90;
		// chile.backgroundColor=(int) (Math.random()*0xffffff)+0xff000000;
		// chile.textColor=(0xffffffff-chile.backgroundColor)|0xff000000;
		// chile.text="btn"+chile.weight;
		// chile.visibility=false;
		// cb.addCenterButton(chile);
		cb.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(WheelButton parent, WheelButtonItem item,
					int position, int flag) {
				if (flag == WheelButton.ITEM_TYPE_CENTER) {
					Toast.makeText(parent.getContext(), item.text,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(parent.getContext(), item.text,
							Toast.LENGTH_SHORT).show();
				}
			}

		});
	}

	private void testLineView(Context context, ViewGroup root) {
		// LinearLayout l=new LinearLayout(context);
		// this.setContentView(l);

		// create a LineView
		DividerLinearLayout cv = new DividerLinearLayout(context);
		cv.setOrientation(LinearLayout.VERTICAL);
		cv.setBackgroundColor(Color.BLUE);
		// add lineView to ContentView
		LayoutParams lp = new LayoutParams(300,LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		root.addView(cv, lp);

		android.view.ViewGroup.LayoutParams chileLayoutLp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < 2; i++) {// add ChileView
			cv.addView(createItemView(context, chileLayoutLp, Gravity.CENTER,
					"Title" + i, "Content" + i));
		}
		LinearLayout BottomLin = new LinearLayout(context);
		BottomLin.setGravity(Gravity.CENTER);
		BottomLin.setLayoutParams(chileLayoutLp);
		android.view.ViewGroup.LayoutParams chileLayoutLp2 = new LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		BottomLin.addView(createItemView(context, chileLayoutLp2,
				Gravity.CENTER, "Title L", "Content Left"));
		ImageView diviImg = new ImageView(context);
		diviImg.setScaleType(ScaleType.FIT_XY);
		diviImg.setImageResource(R.drawable.ic_launcher);
		android.widget.LinearLayout.LayoutParams dividerLp = new android.widget.LinearLayout.LayoutParams(
				4,LayoutParams.WRAP_CONTENT);
		BottomLin.addView(diviImg, dividerLp);
		BottomLin.addView(createItemView(context, chileLayoutLp2,
				Gravity.CENTER, "Title R", "Content Right"));
		cv.addView(BottomLin);

		Button sureBtn = new Button(context);
		sureBtn.setText("Detaile");
		cv.addView(sureBtn);
	}

	private void testCircleLinebutton(Context context, ViewGroup root) {
		// LinearLayout l=new LinearLayout(context);
		// this.setContentView(l);

		// create a LineView
		CircleLinearView cv = new CircleLinearView(context);
		cv.setBackgroundResource(R.drawable.ic_launcher);
		// add lineView to ContentView
		LayoutParams lp = new LayoutParams(300, 400);
		lp.gravity = Gravity.CENTER;
		root.addView(cv, lp);

		android.view.ViewGroup.LayoutParams chileLayoutLp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		for (int i = 0; i < 2; i++) {// add ChileView
			cv.addView(createItemView(context, chileLayoutLp, Gravity.CENTER,
					"Title" + i, "Content" + i));
		}
		LinearLayout BottomLin = new LinearLayout(context);
		BottomLin.setGravity(Gravity.CENTER);
		BottomLin.setLayoutParams(chileLayoutLp);
		LinearLayout.LayoutParams chileLayoutLp2 = new LinearLayout.LayoutParams(
				0, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		chileLayoutLp2.weight = 1;
		BottomLin.addView(createItemView(context, chileLayoutLp2,
				Gravity.CENTER, "Title L", "Content Left"));
		ImageView diviImg = new ImageView(context);
		diviImg.setScaleType(ScaleType.FIT_XY);
		diviImg.setImageResource(R.drawable.ic_launcher);
		android.widget.LinearLayout.LayoutParams dividerLp = new android.widget.LinearLayout.LayoutParams(
				4, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		BottomLin.addView(diviImg, dividerLp);
		BottomLin.addView(createItemView(context, chileLayoutLp2,
				Gravity.CENTER, "Title R", "Content Right"));
		cv.addView(BottomLin);

		Button sureBtn = new Button(context);
		sureBtn.setText("Detaile");
		cv.addView(sureBtn);
	}

	/**
	 * @param context
	 * @param gravity
	 * @param title
	 * @param content
	 * @return
	 */
	public View createItemView(Context context,
			android.view.ViewGroup.LayoutParams layoutParams, int gravity,
			String title, String content) {
		LinearLayout chileLayout_lin = new LinearLayout(context);
		chileLayout_lin.setOrientation(LinearLayout.VERTICAL);
		chileLayout_lin.setLayoutParams(layoutParams);
		chileLayout_lin.setGravity(gravity);
		TextView chileTitle_tv = new TextView(context);
		chileTitle_tv.setTextSize(9);
		chileTitle_tv.setGravity(Gravity.CENTER);
		chileTitle_tv.setText(title);
		TextView chileContent_tv = new TextView(context);
		chileContent_tv.setText(content);
		chileContent_tv.setGravity(Gravity.CENTER);
		chileLayout_lin.addView(chileTitle_tv);
		chileLayout_lin.addView(chileContent_tv);

		return chileLayout_lin;
	}

}
