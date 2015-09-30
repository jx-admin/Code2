package com.act.mbanking.manager;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.utils.JXSuperscriptSpan;

public class CircleLinearViewUtils {
	/**
	 * @param context
	 * @param gravity
	 * @param title
	 * @param content
	 *            the text show in
	 * @return ItemView
	 */
	
	
    public View createItemView(Context context,android.view.ViewGroup.LayoutParams layoutParams,int gravity,String title,String content){
        LinearLayout chileLayout_lin=new LinearLayout(context);
        chileLayout_lin.setOrientation(LinearLayout.VERTICAL);
        chileLayout_lin.setLayoutParams(layoutParams);
        chileLayout_lin.setGravity(gravity);
        TextView chileTitle_tv=new TextView(context,null,R.style.gray_text_black_shadow_style);
        chileTitle_tv.setTextSize(9);
        chileTitle_tv.setGravity(Gravity.CENTER);
        chileTitle_tv.setText(title);
        TextView chileContent_tv=new TextView(context);
        chileContent_tv.setText(content);
        chileContent_tv.setGravity(Gravity.CENTER);
        chileLayout_lin.addView(chileTitle_tv);
        chileLayout_lin.addView(chileContent_tv);
        
        return chileLayout_lin;
    }
    
	public static View createItemView(LayoutInflater layoutInflat,
			android.view.ViewGroup.LayoutParams layoutParams, int gravity,
			String title, String content) {
		LinearLayout chileLayout_lin = (LinearLayout) layoutInflat.inflate( R.layout.level3_circle_iterm_center, null);
		chileLayout_lin.setLayoutParams(layoutParams);
		chileLayout_lin.setGravity(gravity);
		TextView chileTitle_tv = (TextView) chileLayout_lin .findViewById(R.id.chileTitle_tv);
		chileTitle_tv.setText(title);
		TextView chileContent_tv = (TextView) chileLayout_lin .findViewById(R.id.chileContent_tv);
		int decimalChar;
		if (content.length() > 3) {
			decimalChar = content.charAt(content.length() - 3);
			if (decimalChar == ',' || decimalChar == '.') {
				SpannableString msp = new SpannableString(content);
				msp.setSpan(new JXSuperscriptSpan(), content.length() - 3, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 上标
				chileContent_tv.setText(msp);
			}
		} else {
			chileContent_tv.setText(content);
		}

		return chileLayout_lin;
	}

}
