package com.tab.view.demo4;

import com.tab.view.R;

import android.app.Activity;
import android.app.TabActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

/**
 * 使用TabWidget和TabHost和TabActivity来实现
 * <p>
 * TabActivity在API13之后被fragment替代了，所以不建议使用</>
 * 
 * @since 2015年1月13日
 * @author jacksen
 * 
 */
public class FourthActivity extends TabActivity {

	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fourth);

		tabHost = getTabHost();

		tabHost.addTab(tabHost
				.newTabSpec("111")
				.setIndicator("", getResources().getDrawable(R.drawable.wuyong))
				.setContent(R.id.tab1));

		tabHost.addTab(tabHost
				.newTabSpec("222")
				.setIndicator("",
						getResources().getDrawable(R.drawable.gongsunsheng))
				.setContent(R.id.tab2));

		tabHost.addTab(tabHost.newTabSpec("333")
				.setIndicator("", getResources().getDrawable(R.drawable.likui))
				.setContent(R.id.tab3));

		tabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
		tabHost.setCurrentTab(0);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				Toast.makeText(FourthActivity.this, tabId, Toast.LENGTH_SHORT)
						.show();
			}
		});

	}

}
