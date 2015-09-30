package com.act.sctc.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.act.sctc.App;
import com.act.sctc.BaseActivity;
import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.WorkStatus;
import com.act.sctc.db.DBHelper;
import com.act.sctc.db.SqlUtils;
import com.act.sctc.util.Utils;
import com.custom.view.utils.LayoutSelector;

public class SaleAdvertiseActivity extends BaseActivity implements OnClickListener{
	private TitleContentManager mContentManager;
	private LayoutSelector join_advertise_selector;
	private ViewGroup join_advertise_selector_layout;
	private ImageView imageView1;
	private TextView normalview,pressview;
	private int index;
	private String btnName;
	private int workStatus;
	private int businessId=-1;
	private long filter_item_id=-1;
	private long phone_id=-1;
	private int category_id=-1;
	
	private static final String ID="id";
	public static void start(Context context,int srcId){
		Intent intent = new Intent(context, SaleAdvertiseActivity.class);
		intent.putExtra(ID, srcId);
		context.startActivity(intent);
	}
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sale_advertise_page);
		mContentManager = new TitleContentManager( findViewById(R.id.title_content));
		Intent intent=getIntent();
		index=intent.getIntExtra(ID, 0);
		Cursor mCursor=DBHelper.getInstance(this).rawQuery("select a.business_id,b.path, a.filter_item_id, a.phone_id,c.category_id from promotion a left join resource b on a.[detail_img] = b.[_id] left join filter c on a.[filter_item_id]=c.[_id] where a._id="+index, null);// getContentResolver().query(AppHomeColumn.CONTENT_URI, null, AppHomeColumn.TYPE+"="+AppHomeColumn.TYPE_BIG, null, null);
		
		if(mCursor!=null&&mCursor.moveToFirst()){
			if(mCursor.getInt(0)>=1&&mCursor.getInt(0)<=3){
				btnName="查看详情";
				mContentManager.setShoppingCartEnabled(false);
			}else{
				btnName="购买";
			}
			businessId=mCursor.getInt(0);
			filter_item_id=mCursor.getLong(2);
			phone_id=mCursor.getLong(3);
			category_id=mCursor.getInt(4);
			imageView1=(ImageView) findViewById(R.id.imageView1);
			imageView1.setImageBitmap(Utils.getOptimizedBitmap(App.IMG_DIR+mCursor.getString(1)));
			mCursor.close();
		}
		workStatus=WorkStatus.SELLING;//??
		join_advertise_selector=new LayoutSelector();
		join_advertise_selector_layout=(ViewGroup) findViewById(R.id.join_advertise_selector_layout);
		join_advertise_selector.setLayout(join_advertise_selector_layout);
		join_advertise_selector.setOnclickListener(this);
		normalview=(TextView) findViewById(R.id.normalview);
		normalview.setText(btnName);
		pressview=(TextView) findViewById(R.id.pressview);
		pressview.setText(btnName);
	}

	@Override
	public void onClick(View v) {
		switch (businessId) {
		case 1:
		case 2:
			ProductDetailActivity.start(this,businessId,-1);
			break;
		case 3:
			if(phone_id>0){
				ProductDetailActivity.start(this,businessId,phone_id);
			}else{
				PhoneProductListActivity.start(this, filter_item_id);
			}
			break;
		default:
			if (workStatus==WorkStatus.SELLING) {
				workStatus=WorkStatus.IN_CART;
				normalview.setText("已购买");
				join_advertise_selector.setEnable(false);
				ShoppingCarActivity.addGoods(0, 0, phone_id,category_id, 1 ,"");
			}	
			break;
		}
	}
	

}
