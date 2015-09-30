package wu.a.lib.view.dialog;

import wu.a.template.R;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CustomPopu extends PopupWindow implements OnTouchListener {

	public int white = 0xffFFFFFF;
	public int huise = 0xb0000000;

	public CustomPopu() {
		super();
	}

	public CustomPopu(Context cxt) {
		super(cxt);
		_cxt = cxt;
	}

	public CustomPopu(Context cxt, AttributeSet attrs) {
		super(cxt, attrs);
		_cxt = cxt;
	}

	public void setOnClickListener(OnClickListener listener) {
		_listener = listener;
	}

	public void showPopu(View view, int weight, int height) {
		setContentView(view);
		
		initWin(view);

		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);

		ColorDrawable dw = new ColorDrawable(0xffff0000);
		setBackgroundDrawable(dw);

		int[] loc = new int[2];
		view.getLocationOnScreen(loc);

		showAtLocation(view, Gravity.LEFT | Gravity.TOP,
				loc[0] + view.getWidth(), loc[1]);
	}

	private void initWin(View rooView) {
		
		TextView tv = (TextView) rooView.findViewById(R.id.pupdate);

		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.pmessage);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.pinfo);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.ppsword);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.ppsword);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.ppsword);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.pderma);
		setText(tv);

		tv = (TextView) rooView.findViewById(R.id.pmore);
		setText(tv);
	}

	private void setText(TextView tv) {
		if (null != _listener)
			tv.setOnClickListener(_listener);

		tv.setOnTouchListener(this);
	}

	Context _cxt;
	OnClickListener _listener;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v instanceof TextView) {
			TextView tv = (TextView) v;
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				tv.setTextColor(0xFFFFFF00);
				break;
			}
			case MotionEvent.ACTION_UP: {
				tv.setTextColor(0xFFFFFFFF);
				break;
			}
			default: {
				;
			}
			}
		}
		return false;
	}
}
