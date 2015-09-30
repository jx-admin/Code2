package com.act.mbanking.view;

import android.view.View;
import android.widget.TextView;

import com.act.mbanking.R;

/**如果把这个修改成一个View更好
 * @author junxu.wang
 *
 */
public final class TitleViewManager {

	private TextView top_title_tv, title_tv, sub_title_tv;
	
	public TitleViewManager(){}
	
	public TitleViewManager(View layout){
		setView(layout);
	}

	public void setView(View layout) {
		top_title_tv = (TextView) layout.findViewById(R.id.top_title_tv);
		title_tv = (TextView) layout.findViewById(R.id.title_tv);
		title_tv.setText(R.string.account_details_title);
		sub_title_tv = (TextView) layout.findViewById(R.id.sub_title_tv);
	}
	
	public void setTopTitle(String str){
		setText(top_title_tv,str);
	}
	
	public void setTitle(String str){
		setText(title_tv,str);
	}
	
	public void setSubTitle(String str){
		setText(sub_title_tv,str);
	}
	
	private void setText(TextView tv,String str){
		if(str==null){
			if(tv.getVisibility()!=View.GONE){
				tv.setVisibility(View.GONE);
			}
			tv.setVisibility(View.GONE);
		}else{
			if(tv.getVisibility()!=View.VISIBLE){
				tv.setVisibility(View.VISIBLE);
			}
			tv.setText(str);
		}
	}
}
