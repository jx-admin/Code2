package wu.a.wuliu;

import wu.a.activity.TitleFooterActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import baidumapsdk.demo.R;

import com.droid.Activity01;
import com.droid.City;

/**
 * <pre>
 * 主页入口
 * @author junxu.wang
 * @d2015年7月16日
 * </pre>
 *
 */
public class AboutActivity extends TitleFooterActivity implements
		OnClickListener, OnCheckedChangeListener {

	public static void start(Activity activity) {
		activity.startActivity(new Intent(activity, AboutActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.about);
		setTitleText("关于我们");
		setTitleLeftButtonImage(R.drawable.back);
		setBottomMenu(View.GONE);
	}

	@Override
	public void onTitleLeftButtonClick(View v) {
		 super.onTitleLeftButtonClick(v);
	}

	@Override
	public void onTitleRightButtonClick(View v) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

	}

}
