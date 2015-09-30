package wu.a.wuliu;

import wu.a.activity.TitleFooterActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import baidumapsdk.demo.R;

import com.droid.City;

public class CargoGoodsActivity extends TitleFooterActivity implements OnClickListener {
	
	public static void start(Activity activity){
		activity.startActivity(new Intent(activity,CargoGoodsActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.cargo_goods);
//		setMenuStatus(this,MENU_BOOK, false);
		setTitleText(R.string.cargo_goods,true);
		setTitleLeftButtonImage(R.drawable.back);
		setBottomMenu(View.GONE);
		findViewById(R.id.xiaomianbao_item).setOnClickListener(this);
		findViewById(R.id.zhongmianbao_item).setOnClickListener(this);
		findViewById(R.id.xianghuo_item).setOnClickListener(this);
	}
	
	@Override
	public void onTitleLeftButtonClick(View v) {
//		super.onTitleLeftButtonClick(v);
//		Activity01.start(this, 12);
		super.onTitleLeftButtonClick(v);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			if(requestCode==12){
				City city=(City) data.getSerializableExtra("city");
				setTitleLeftButtonText(city.name);
			}
		}
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.xiaomianbao_item:
		case R.id.zhongmianbao_item:
		case R.id.xianghuo_item:
		DriverListActivity.start(this);
		break;
		}
		// TODO Auto-generated method stub
		
	}

}
