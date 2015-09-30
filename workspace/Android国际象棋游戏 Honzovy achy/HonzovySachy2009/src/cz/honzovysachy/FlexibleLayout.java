package cz.honzovysachy;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class FlexibleLayout extends LinearLayout {

	public FlexibleLayout(Context context) {
		super(context);
	}
	
	public FlexibleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		//this.setOrientation(w < h ? HORIZONTAL : VERTICAL);
		final int fw = w;
		final int fh = h;
		new Handler().post(new Runnable() {
			public void run() {
				setOrientation(fw > fh ? HORIZONTAL : VERTICAL);
			}
    	});
    }

}
