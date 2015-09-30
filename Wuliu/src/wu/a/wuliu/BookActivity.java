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
public class BookActivity extends TitleFooterActivity implements
		OnClickListener, OnCheckedChangeListener {
	private BookMain bookMain;
	private BookManager bookManager;
	private SaleManager saleManager;
	private InfoManager infoManager;

	private RadioGroup footer_menu;

	public static void start(Activity activity) {
		activity.startActivity(new Intent(activity, BookActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		footer_menu = (RadioGroup) findViewById(R.id.footer_menu);
		footer_menu.setOnCheckedChangeListener(this);
		bookMain = new BookMain(this);
		bookManager = new BookManager(this);
		saleManager = new SaleManager(this);
		infoManager=new InfoManager(this);
		footer_menu.check(R.id.menu_book);
		// setTitleText("下单");
		// setTitleLeftButtonText(R.string.select_city);
	}

	@Override
	public void onTitleLeftButtonClick(View v) {
		// super.onTitleLeftButtonClick(v);
		Activity01.start(this, 12);
	}

	@Override
	public void onTitleRightButtonClick(View v) {
		FeedBackActivity.start(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == 12) {
				City city = (City) data.getSerializableExtra("city");
				setTitleLeftButtonText(city.name);
			}
		}
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}
		// TODO Auto-generated method stub

	}

	private int status = -1;

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.menu_book:
			if (status != 0) {
				setToppanel(View.VISIBLE);
				setTitleRightButtonText(R.string.feedback);
				setContentLayout(bookMain.getView());
				// setMenuStatus(this,MENU_BOOK, false);
				setTitleImage(R.drawable.title_jlbhuoyun);
				status = 0;
			}
			break;
		case R.id.menu_book_manager:
			if (status != 1) {
				setToppanel(View.VISIBLE);
				setTitleRightButton(View.GONE);
				setContentLayout(bookManager.getView());
				setTitleText(R.string.history_book_list);
				status = 1;
			}
			break;
		case R.id.menu_activity:
			if (status != 2) {
				setToppanel(View.VISIBLE);
				setTitleRightButton(View.GONE);
				setContentLayout(saleManager.getView());
				setTitleText(R.string.history_book_list);
				status = 2;
			}
			break;
		case R.id.menu_user_info:
			if (status != 3) {
				setToppanel(View.GONE);
//				setTitleRightButton(View.GONE);
				setContentLayout(infoManager.getView());
//				setTitleText(R.string.history_book_list);
				status = 3;
			}
			break;
		}

	}

}
