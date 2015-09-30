package com.act.sctc.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.act.sctc.Constant;
import com.act.sctc.R;
import com.act.sctc.been.ContractPhoneSetmeal;
import com.act.sctc.been.Phone;
import com.act.sctc.been.PhoneColor;
import com.act.sctc.been.SetMeal;
import com.act.sctc.db.BusinessDetailColumn;
import com.act.sctc.db.DBHelper;
import com.act.sctc.db.FilterColumn;
import com.act.sctc.db.GoodsCartColumn;
import com.act.sctc.db.ItemColumn;
import com.act.sctc.db.ItemObjectRelationColumn;
import com.act.sctc.db.PackageColumn;
import com.act.sctc.db.PhoneColorColumn;
import com.act.sctc.db.PhoneColumn;
import com.act.sctc.db.PhoneContractColumn;
import com.act.sctc.db.ProductDetailColumn;
import com.act.sctc.db.PromotionColumn;
import com.act.sctc.db.ResourceColumn;
import com.act.sctc.util.DbSync;
import com.act.sctc.util.DbSync.DbSyncListener;
import com.act.sctc.util.Logger;
import com.act.test.Goods;
import com.act.test.OfflineData;
import com.custom.view.CustomDialog;
import com.custom.view.utils.BaseManager;

public class TitleContentManager extends BaseManager implements OnClickListener, DbSyncListener{
	public static final String TAG=TitleContentManager.class.getSimpleName();
	private ImageView home_iv;
	private View setting_btn,refresh_btn,shopping_car_btn,customer_btn;
	private DbSync dbSync;
	private CustomDialog downloadDialog;
	private DbRefreshListener dbRefreshListener;
	
	public interface DbRefreshListener {
		public void onDbRefreshed();
	}
	
	public TitleContentManager(View layout){
		super(layout);
		
		iniController();
		iniVariable();
		iniListener();
	}
	
	public void setUpdateVisibility(int visibility){
		refresh_btn.setVisibility(visibility);
	}
	
	public void setShoppingCartEnabled(boolean enabled){
		shopping_car_btn.setEnabled(enabled);
	}
	
	protected void iniController() {
		setting_btn=layout.findViewById(R.id.setting_btn);
		refresh_btn= layout.findViewById(R.id.refresh_btn);
		shopping_car_btn= layout.findViewById(R.id.shopping_car_btn);
		customer_btn= layout.findViewById(R.id.customer_info_btn);
		home_iv=(ImageView) layout.findViewById(R.id.home_iv);
	}

	protected void iniVariable() {
	}

	protected void iniListener() {
		refresh_btn.setOnClickListener(this);
		shopping_car_btn.setOnClickListener(this);
		customer_btn.setOnClickListener(this);
		home_iv.setOnClickListener(this);
	}
	
	public void addSettingBtn(OnClickListener onClickListener,int backgrounId){
		if(backgrounId>0){
			setting_btn.setBackgroundResource(backgrounId);
		}
		setting_btn.setOnClickListener(onClickListener);
		setting_btn.setVisibility(View.VISIBLE);
	}
	
	public void setDbRefreshListener(DbRefreshListener listener) {
		dbRefreshListener = listener;
	}
	
	Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				downloadDialog.setMessage("2.设备正在同步多媒体文件。");
				mHandler.sendEmptyMessageDelayed(1,1500);
				break;
			case 1:
				mHandler.sendEmptyMessageDelayed(2,1500);
				downloadDialog.setMessage("3.设备正在进行数据初始化。");
				break;
			case 2:
				downloadDialog.setMessage("4.您的设备已更新完成您愉快!");
				downloadDialog.done();
				downloadDialog.addPositiveButton(R.string.back, new View.OnClickListener() {
					public void onClick(View v) {
						downloadDialog.dismiss();
						if (dbRefreshListener != null) {
							dbRefreshListener.onDbRefreshed();
						}
					}
				});
				break;
			case 3:
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	private class UpdateThread extends Thread{
		public void run() {
			DBHelper mDbHelper=DBHelper.getInstance(context);
			
			
			//home small pic(business)
//			delete from resource
			mDbHelper.execSQL("delete from resource");
			mDbHelper.execSQL("insert into resource values(1,\"home_small_video.png\",0);");
			mDbHelper.execSQL("insert into resource values(2,\"home_small_fttx_lan.png\",0);");
			mDbHelper.execSQL("insert into resource values(3,\"home_small_smartphone.png\",0);");
			
			
			mDbHelper.execSQL("delete from business");
			mDbHelper.execSQL("insert into business values(1,\"高清影视\",1);");	
			mDbHelper.execSQL("insert into business values(2,\"光纤宽带\",2);");	
			mDbHelper.execSQL("insert into business values(3,\"智能手机\",3);");
			
			//home big pic(business)
			context.getContentResolver().delete(PromotionColumn.CONTENT_URI, null,null);
			String homeBigImgs[]=new String[]{"home_big_6.png","home_big_5.png","home_big_7.png","home_big_8.png"};
			String homeDetailImgs[]=new String[]{"home_activity_6.png","home_activity_5.png","home_activity_7.png","home_activity_8.png"};
			int homeImgTypes[]=new int[]{3,1,2,0};
			//			delete from resource
			for(int i=0;i<homeBigImgs.length;i++){
				ContentValues values=new ContentValues();
				values.put(ResourceColumn.PATH,homeBigImgs[i]);
				long bigImgId=mDbHelper.insert(ResourceColumn.TABLE_NAME, values);
				values=new ContentValues();
				values.put(ResourceColumn.PATH,homeDetailImgs[i]);
				long bigDetailId=mDbHelper.insert(ResourceColumn.TABLE_NAME, values);
				values=new ContentValues();
				values.put(PromotionColumn.img,bigImgId);
				values.put(PromotionColumn.detail_img,bigDetailId);
				values.put(PromotionColumn.business_id,homeImgTypes[i]);
				long promotionId=mDbHelper.insert(PromotionColumn.TABLE_NAME, values);
				if(promotionId<=0){
					Log.d(TAG,"PromotionColumn insert erro "+promotionId);
				}
			}
			
			
			
			//info
			context.getContentResolver().delete(ProductDetailColumn.CONTENT_URI, null,null);
			String productDetailTitles[]=new String[]{"功能介绍","操作模拟","无忧安装","优惠套餐","无忧安装","套餐介绍","基本信息","详情介绍","设备参数","优惠套餐"};
			String productDetailImgs[]=new String[]{"itv_detail_tab1_1.png","itv_detail_tab1_2.png","itv_install.png","itv_setmeal.png","package_feature1_pic.png", "package_feature2_pic.png",null,"phone_introduce_picture.png",null, "phone_setmeal_picture.png"};
			int buId[]=new int[]{1,1,1,1,2,2,3,3,3,3};
			for (int i = 0; i < productDetailTitles.length; i++) {
				ContentValues values;
				values=new ContentValues();
				values.put(ProductDetailColumn.business_id,buId[i]);
				values.put(ProductDetailColumn.title,productDetailTitles[i]);
				if(productDetailImgs[i]!=null){
					ContentValues imgValues=new ContentValues();
					imgValues.put(ResourceColumn.PATH,productDetailImgs[i]);
					long iconId=mDbHelper.insert(ResourceColumn.TABLE_NAME, imgValues);
					values.put(ProductDetailColumn.img_id, iconId);
				}
				long id=mDbHelper.insert(ProductDetailColumn.TABLE_NAME, values);
				if(id<=0){
					Log.d(TAG, "BusinessDetailColumn insert erro " + i);
				}
			}
			
			
			//营销页（BusinessDetailColumn）
			context.getContentResolver().delete(BusinessDetailColumn.CONTENT_URI, null,null);
			String imgs[]=new String[]{"phone_feature1_pic.png","phone_feature3_pic.png","phone_feature2_pic.png","phone_feature4_pic.png"};
			String icons[]=new String[]{"unit_home_tab_icon_6phone.png","unit_home_tab_icon_7phone.png","unit_home_tab_icon_8phone.png","unit_home_tab_icon_9phone.png"};
			String []btnNames=new String[]{"品牌云集","购机送费","资费便宜 ","智能省钱","上网更快","信号更强","辐射更小","通讯保密"};
			for (int i = 0; i < imgs.length; i++) {
				ContentValues values=new ContentValues();
				values.put(ResourceColumn.PATH,imgs[i]);
				long imgId=mDbHelper.insert(ResourceColumn.TABLE_NAME, values);
				values=new ContentValues();
				values.put(ResourceColumn.PATH,icons[i]);
				long iconId=mDbHelper.insert(ResourceColumn.TABLE_NAME, values);
				values = new ContentValues();
				values.put(BusinessDetailColumn.business_id, 3);
				values.put(BusinessDetailColumn.RESOURCE_ID,imgId);
				values.put(BusinessDetailColumn.TITLE,btnNames[i*2]);
				values.put(BusinessDetailColumn.SUBTITLE,btnNames[i*2+1]);
				values.put(BusinessDetailColumn.ICON,iconId);
				long vid=mDbHelper.insert(BusinessDetailColumn.TABLE_NAME, values);
				if(vid<=0){
					Log.d(TAG, "BusinessDetailColumn insert erro " + i);
				}
			}
			
			imgs=new String[]{"unit_home_broadband_1.png","unit_home_broadband_2.png","unit_home_broadband_3.png"};
			icons=new String[]{"unit_home_tab_icon_10traffic.png","unit_home_tab_icon_11traffic.png","unit_home_tab_icon_12traffic.png"};
			btnNames=new String[]{"百兆宽带","比快更快","多点融合","无处不在","优惠多多","全家共享"};
			for (int i = 0; i < imgs.length; i++) {
				ContentValues values=new ContentValues();
				values.put(ResourceColumn.PATH,imgs[i]);
				long imgId=mDbHelper.insert(ResourceColumn.TABLE_NAME, values);
				values=new ContentValues();
				values.put(ResourceColumn.PATH,icons[i]);
				long iconId=mDbHelper.insert(ResourceColumn.TABLE_NAME, values);
				values = new ContentValues();
				values.put(BusinessDetailColumn.business_id, 2);
				values.put(BusinessDetailColumn.RESOURCE_ID,imgId);
				values.put(BusinessDetailColumn.TITLE,btnNames[i*2]);
				values.put(BusinessDetailColumn.SUBTITLE,btnNames[i*2+1]);
				values.put(BusinessDetailColumn.ICON,iconId);
				long vid=mDbHelper.insert(BusinessDetailColumn.TABLE_NAME, values);
				if(vid<=0){
					Log.d(TAG, "BusinessDetailColumn insert erro " + i);
				}
			}
			
			
			
			imgs=new String[]{ "ad.mp4","itv_feature1_pic.png", "itv_feature2_pic.png","itv_feature3_pic.png","itv_feature4_pic.png"};
			icons=new String[]{"unit_home_tab_icon_1itv.png","unit_home_tab_icon_2itv.png","unit_home_tab_icon_3itv.png","unit_home_tab_icon_4itv.png","unit_home_tab_icon_5itv.png"};
			btnNames=new String[]{"精彩无限","宣传视频","95套直播","电视频道","点播最新","大片热剧","回看暂停","精彩节目","您身边的","高清影视"};
			for (int i = 0; i < imgs.length; i++) {
				ContentValues values=new ContentValues();
				values.put(ResourceColumn.PATH,imgs[i]);
				if(i==0){
					values.put(ResourceColumn.TYPE, 1);
				}
				long imgId=mDbHelper.insert(ResourceColumn.TABLE_NAME, values);
				values=new ContentValues();
				values.put(ResourceColumn.PATH,icons[i]);
				long iconId=mDbHelper.insert(ResourceColumn.TABLE_NAME, values);
				values = new ContentValues();
				values.put(BusinessDetailColumn.business_id, 1);
				values.put(BusinessDetailColumn.RESOURCE_ID,imgId);
				values.put(BusinessDetailColumn.TITLE,btnNames[i*2]);
				values.put(BusinessDetailColumn.SUBTITLE,btnNames[i*2+1]);
				values.put(BusinessDetailColumn.ICON,iconId);
				long vid=mDbHelper.insert(BusinessDetailColumn.TABLE_NAME, values);
				if(vid<=0){
					Log.d(TAG, "BusinessDetailColumn insert erro " + i);
				}
			}
			
			//category
			mDbHelper.execSQL("delete from category");
			mDbHelper.execSQL("insert into category values(101,1,1,\"高清影视-资费套餐\");");
			mDbHelper.execSQL("insert into category values(102,1,2,\"高清影视-优惠合约\");");
			mDbHelper.execSQL("insert into category values(201,2,1,\"光纤宽带-资费套餐\");");
			mDbHelper.execSQL("insert into category values(202,2,2,\"光纤宽带-优惠合约\");");
			mDbHelper.execSQL("insert into category values(301,3,1,\"智能手机-资费套餐\");");
			mDbHelper.execSQL("insert into category values(302,3,2,\"智能手机-优惠合约\");");
			mDbHelper.execSQL("insert into category values(303,3,3,\"智能手机-货架\");");
			
			//filter & item
			{
				//101	1	1	高清影视-资费套餐
				//201	2	1	光纤宽带-资费套餐
				//301	3	1	智能手机-资费套餐
				
				
				int category[]=new int[]{101,
						201,
						301,
						302,
						303};
				String[][] menu_titles=new String[][]{{"产品类型","影视产品"},
						{"宽带服务","宽带类型"},
						{"选择套餐用户类型","月费范围","套餐特点"},
						{"优惠","在网时长"},
						{"天翼年欢惠","本地特惠","品牌","价位","特点"}};
				String [][][]menu2s=new String[][][]{
						{{"只装宽带","优惠套餐"},{"光纤","非光纤"}},
						{{"只装高清影视","优惠套餐"},{"高清影视直播版","高清影视尊享版"}},
						{{"电信天翼用户" ,"移动全球通" ,"移动动感地带" ,"移动神州行" ,"联通3G套餐" ,"联通如意通" ,"联通世界风" ,"联通新势力" ,"首次购买手机"},{"0-50","51-100","101-200","200以上"},{"上网多","通话多"}},
						{{"24个月","30个月","36个月"},{"预存话费送手机","购机入网送话费"}},
						{{"天翼年欢惠"},{"本地特惠"},{"苹果" ,"酷派" ,"小米" ,"华为" ,"联想" ,"诺基亚"},{"0-100" ,"100-500" ,"500-2000" ,"2000-6000"}, {"销量好" ,"大屏" ,"触摸" ,"功能多"}}};
				
				mDbHelper.execSQL("delete from filter");
				mDbHelper.execSQL("delete from item");
				int itemPosition=1;
				for(int ci=0;ci<category.length;ci++){
					String[]filterTitles=menu_titles[ci];
					for(int fi=0;fi<filterTitles.length;fi++){
						ContentValues filterValues=new ContentValues();
						filterValues.put(FilterColumn.category_id, category[ci]);
						filterValues.put(FilterColumn.name, filterTitles[fi]);
						long filterId=mDbHelper.insert(FilterColumn.TABLE_NAME, filterValues);
						if(filterId>0){
							String[]itemNames=menu2s[ci][fi];
							for(int ii=0;ii<menu2s[ci][fi].length;ii++){
								ContentValues itemValues=new ContentValues();
								itemValues.put(ItemColumn._ID, itemPosition);
								itemValues.put(ItemColumn.name, itemNames[ii]);
								itemValues.put(ItemColumn.filter_id, filterId);
								itemValues.put(ItemColumn.sort, ii);
								long itemId=mDbHelper.insert(ItemColumn.TABLE_NAME, itemValues);
								itemPosition++;
							}
						}
					}
				}
			}
			
//			phone
			mDbHelper.execSQL("delete from phone");
			mDbHelper.execSQL("delete from phone_color");
			mDbHelper.execSQL("delete from item_object_relation");
			Cursor ItemIdCursor=mDbHelper.rawQuery("select _id from item where name like '天翼年欢惠'", null);
			long phoneId=0;
			if(ItemIdCursor!=null&&ItemIdCursor.moveToFirst()){
				phoneId=writePhone(phoneId,mDbHelper,readNianhuanhuiPhones(),ItemIdCursor.getLong(0));
				ItemIdCursor.close();
			}
			ItemIdCursor=mDbHelper.rawQuery("select _id from item where name like '本地特惠'", null);
			if(ItemIdCursor!=null&&ItemIdCursor.moveToFirst()){
				phoneId=writePhone(phoneId,mDbHelper,readLocoalContractPhones(),ItemIdCursor.getLong(0));
				ItemIdCursor.close();
			}
			
			//package
			mDbHelper.execSQL("delete from package");
			writePackage(mDbHelper,getSetMealTV(),1,-1);
			writePackage(mDbHelper,getSetMealTrafficSetMeals(),2,-1);
			
			ItemIdCursor=mDbHelper.rawQuery("select _id from item where name like '移动全球通'", null);
			if(ItemIdCursor!=null&&ItemIdCursor.moveToFirst()){
				writePackage(mDbHelper,getSetMealQuanqiutongs(),3,ItemIdCursor.getLong(0));
				ItemIdCursor.close();
			}
			
			ItemIdCursor=mDbHelper.rawQuery("select _id from item where name like '移动动感地带'", null);
			if(ItemIdCursor!=null&&ItemIdCursor.moveToFirst()){
				writePackage(mDbHelper,getSetMealsdonggandidai(),3,ItemIdCursor.getLong(0));
				ItemIdCursor.close();
			}
			
			ItemIdCursor=mDbHelper.rawQuery("select _id from item where name like '移动神州行'", null);
			if(ItemIdCursor!=null&&ItemIdCursor.moveToFirst()){
				writePackage(mDbHelper,getSetMealShengzhouxing(),3,ItemIdCursor.getLong(0));
				ItemIdCursor.close();
			}
			
			//phone contract package
			mDbHelper.execSQL("delete from phone_contract");
			
			ItemIdCursor=mDbHelper.rawQuery("select _id from item where name like '24个月'", null);
			if(ItemIdCursor!=null&&ItemIdCursor.moveToFirst()){
				writePhonePackage(mDbHelper,getContractPhoneSetmeals(),2,ItemIdCursor.getLong(0));
				ItemIdCursor.close();
			}
			//contract no iphone
			ContentValues pkCValues=new ContentValues();
			pkCValues.put(PhoneContractColumn.name,"选择合约");
			pkCValues.put(PhoneContractColumn.type,1);
			pkCValues.put(PhoneContractColumn.price,0);
					pkCValues.put(PhoneContractColumn.attr_name1,"说明：在网25个月，总共获得100元话费返还。");
			long pkId=mDbHelper.insert(PhoneContractColumn.TABLE_NAME, pkCValues);
			
			//shopping cart
			mDbHelper.execSQL("delete from "+GoodsCartColumn.TABLE_NAME);
			
			ArrayList<Goods> mGoodsData = OfflineData.getGoodsData();
			for(int i=0;i<mGoodsData.size();i++){
				Goods mPhone=mGoodsData.get(i);
				ContentValues mPhoneValues=new ContentValues();
//					mPhoneValues.put(ShoppingCartColumn.name,mPhone.getName());
				mPhoneValues.put(GoodsCartColumn.product_id,mPhone.getName());
				mPhoneValues.put(GoodsCartColumn.count,mPhone.count());
				mPhoneValues.put(GoodsCartColumn.mark,mPhone.mark());
				mPhoneValues.put(GoodsCartColumn.user_id,1);
				mPhoneValues.put(GoodsCartColumn.product_type,Constant.CATEGORY_PHONE_CONTRACT);
				
				long sale_iconId=mDbHelper.insert(GoodsCartColumn.TABLE_NAME,mPhoneValues);
			}
			
			mHandler.sendEmptyMessageDelayed(0,1500);
		}
	};
	

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.refresh_btn:
			if (DbSync.USE_DB_SYNC) {
				if (dbSync == null) {
					dbSync = new DbSync();
				}
				dbSync.sync(context, this);
			} else {
				if(downloadDialog==null){
					downloadDialog= new CustomDialog(context);
					downloadDialog.setCancelable(false);
				}
				downloadDialog.show();
				downloadDialog.resetView();
				downloadDialog.setMessage("1.设备正在同步系统数据。");
				new UpdateThread().start();
			}
			
			break;
		case R.id.shopping_car_btn:
			if(context.getClass().getSimpleName().equals(ShoppingCarActivity.class.getSimpleName())){
				return;
			}
			Intent i=new Intent(context,ShoppingCarActivity.class);
			context.startActivity(i);
			break;
		case R.id.customer_info_btn:
			break;
		case R.id.home_iv:
			if(context.getClass().getSimpleName().equals(HomePageActivity.class.getSimpleName())){
				return;
			}
			i=new Intent(context,HomePageActivity.class);
			context.startActivity(i);
			if(context instanceof Activity){
				((Activity)context).finish();
			}
			break;
		}
	}
	
	private long writeResource(DBHelper mDbHelper,String path){
		ContentValues img1Values=new ContentValues();
		img1Values.put(ResourceColumn.PATH, path);
		return mDbHelper.insert(ResourceColumn.TABLE_NAME, img1Values);
	}
	
	private long writePhone(long startId,DBHelper mDbHelper,List<Phone>nianhuanhuiPhonesPhones,long itemId){
		for(int i=0;i<nianhuanhuiPhonesPhones.size();i++){
			Phone mPhone=nianhuanhuiPhonesPhones.get(i);
			ContentValues mPhoneValues=new ContentValues();
			startId++;
			mPhoneValues.put(PhoneColumn._ID, startId);
			mPhoneValues.put(PhoneColumn.ad_desc,mPhone.getName());
			
			long sale_iconId=writeResource(mDbHelper,mPhone.getIcon());
			if(sale_iconId>0){
				mPhoneValues.put(PhoneColumn.thumbnail,sale_iconId);
			}
			
			mPhoneValues.put(PhoneColumn.price,mPhone.getOriginalPrice());	
			
			mPhoneValues.put(PhoneColumn.sale_price,mPhone.getPrice());	
			
			mPhoneValues.put(PhoneColumn.attr10,mPhone.getIntroduce());
			
			mPhoneValues.put(PhoneColumn.brand,mPhone.getBrandCategory());
			
			mPhoneValues.put(PhoneColumn.sale_icon,true);
			
			long phoneId=mDbHelper.insert(PhoneColumn.TABLE_NAME, mPhoneValues);
			if(phoneId>0){
				List<PhoneColor>colors = mPhone.getColors();
				for(int cls=0;cls<colors.size();cls++){
					PhoneColor phoneColor=colors.get(cls);
					ContentValues phoneColorValues=new ContentValues();
					
					phoneColorValues.put(PhoneColorColumn.phone_id, phoneId);
					
					phoneColorValues.put(PhoneColorColumn.color, phoneColor.getName());
					
					phoneColorValues.put(PhoneColorColumn.value, phoneColor.getColor());
					
					long img1Id=writeResource(mDbHelper,phoneColor.getPicIds()[0]);
					if(img1Id>0){
						phoneColorValues.put(PhoneColorColumn.img1,img1Id);
					}
					img1Id=writeResource(mDbHelper,phoneColor.getPicIds()[1]);
					if(img1Id>0){
						phoneColorValues.put(PhoneColorColumn.img2,img1Id);
					}
					img1Id=writeResource(mDbHelper,phoneColor.getPicIds()[2]);
					if(img1Id>0){
						phoneColorValues.put(PhoneColorColumn.img3,img1Id);
					}
					img1Id=writeResource(mDbHelper,phoneColor.getPicIds()[3]);
					if(img1Id>0){
						phoneColorValues.put(PhoneColorColumn.img4,img1Id);
					}
					mDbHelper.insert(PhoneColorColumn.TABLE_NAME, phoneColorValues);
					//item_object_relation
					ContentValues relationCValues=new ContentValues();
					relationCValues.put(ItemObjectRelationColumn.item_id, itemId);
					relationCValues.put(ItemObjectRelationColumn.object_id, phoneId);
					mDbHelper.insert(ItemObjectRelationColumn.TABLE_NAME, relationCValues);
				}
			}
			
		}
		return startId;
	}
	
	private void writePackage(DBHelper mDbHelper,List<SetMeal>packages,long businessId,long itemId){
		for(int i=0;i<packages.size();i++){
			SetMeal pk=packages.get(i);
			ContentValues pkCValues=new ContentValues();
			pkCValues.put(PackageColumn.name,pk.getName());
			pkCValues.put(PackageColumn.business_id,businessId);
			pkCValues.put(PackageColumn.price,pk.getMonthlyRent());
			if(pk.titles!=null&&pk.titles.length>0){
				String attr_name="attr_name";
				String attr_value="attr_value";
				for(int att=0;att<pk.titles.length&&att<6;att++){
					pkCValues.put(attr_name+(att+1),pk.titles[att]);
					pkCValues.put(attr_value+(att+1),pk.contents[att]);
				}
			}
			pkCValues.put(PackageColumn.vs_name,pk.getVSTitle());
			pkCValues.put(PackageColumn.vs_value,pk.getVSDis());
			pkCValues.put(PackageColumn.phone_number,pk.getPhoneNumber());
			pkCValues.put(PackageColumn.desc1,pk.getDesc1());
			pkCValues.put(PackageColumn.desc2,pk.getDesc2());
			long pkId=mDbHelper.insert(PackageColumn.TABLE_NAME, pkCValues);
			if(pkId>0&&itemId>0){
				//item_object_relation
				ContentValues relationCValues=new ContentValues();
				relationCValues.put(ItemObjectRelationColumn.item_id, itemId);
				relationCValues.put(ItemObjectRelationColumn.object_id, pkId);
				mDbHelper.insert(ItemObjectRelationColumn.TABLE_NAME, relationCValues);
			}
		}
		
	}
	
	private void writePhonePackage(DBHelper mDbHelper,List<ContractPhoneSetmeal>packages,int businessId,long itemId){
		for(int i=0;i<packages.size();i++){
			ContractPhoneSetmeal pk=packages.get(i);
			ContentValues pkCValues=new ContentValues();
			pkCValues.put(PhoneContractColumn.name,pk.getTitle());
			pkCValues.put(PhoneContractColumn.type,1);
			pkCValues.put(PhoneContractColumn.price,pk.getPhonePrice());
			if(pk.titles!=null&&pk.titles.length>0){
				String attr_name="attr_name";
				String attr_value="attr_value";
				for(int att=0;att<pk.titles.length&&att<6;att++){
					pkCValues.put(attr_name+(att+1),pk.titles[att]);
					pkCValues.put(attr_value+(att+1),pk.contents[att]);
				}
			}
			long pkId=mDbHelper.insert(PhoneContractColumn.TABLE_NAME, pkCValues);
			if(pkId>0&&itemId>0){
				//item_object_relation
				ContentValues relationCValues=new ContentValues();
				relationCValues.put(ItemObjectRelationColumn.item_id, itemId);
				relationCValues.put(ItemObjectRelationColumn.object_id, pkId);
				mDbHelper.insert(ItemObjectRelationColumn.TABLE_NAME, relationCValues);
			}
		}
		
	}
	
	private ArrayList<Phone> readNianhuanhuiPhones(){
		List<PhoneColor>colors = new ArrayList<PhoneColor>();
		String[] note2_color_names = new String[] { "简约白" };
		int[] note2_colors = new int[] { 0xffffffff };
		String[][] note2_picsId = new String[][] {
				{"phone_pic_1.png","phone_pic_2.png",
					"phone_pic_3.png","phone_pic_4.png"} };
		for(int i=0;i<note2_color_names.length;i++){
			PhoneColor pc = new PhoneColor();
			pc.setColor(note2_colors[i]);
			pc.setPicId(note2_picsId[i]);
			pc.setName(note2_color_names[i]);
			colors.add(pc);
		}
		ArrayList<Phone> nianhuanhuiPhonesPhones=new ArrayList<Phone>();
		
		Phone pp = new Phone("华为 C8813DQ 双模双待 4.5寸大屏","nianhuanhui/phone_icon_huawei_c8813dq.png","双模双待 4.5寸大屏",990,1190,Phone.BRAND_HUAWEI);
		pp.setColor(colors);
		nianhuanhuiPhonesPhones.add(pp);
		
		pp = new Phone("苹果 iPhone 5S 16G	无与伦比的高端洋气上档次","nianhuanhui/phone_icon_iphone5s.png","无与伦比的高端洋气上档次",5088,5288,Phone.BRAND_IPHONE);
		pp.setColor(colors);
		nianhuanhuiPhonesPhones.add(pp);
		
		pp = new Phone("苹果 iPhone 5c 16G	生来多彩 耀眼本色","nianhuanhui/phone_icon_iphone5c.png","生来多彩 耀眼本色",3888,4488,Phone.BRAND_IPHONE);
		pp.setColor(colors);
		nianhuanhuiPhonesPhones.add(pp);
		
		pp = new Phone("苹果 iPhone 5 16G	更薄、更轻、更快、更好","nianhuanhui/phone_icon_iphone5.png","更薄、更轻、更快、更好",3988,5288,Phone.BRAND_IPHONE);
		pp.setColor(colors);
		nianhuanhuiPhonesPhones.add(pp);
		
		pp = new Phone("苹果 iPhone 4S 16G 经典传承 值得拥有","nianhuanhui/phone_icon_phone4s.png","经典传承 值得拥有",2988,3288,Phone.BRAND_IPHONE);
		pp.setColor(colors);
		nianhuanhuiPhonesPhones.add(pp);
		
		pp = new Phone("三星 Galaxy Note3 惊鸿大屏 震撼配置","nianhuanhui/phone_icon_sag_galaxy note3.png","惊鸿大屏 震撼配置",4999,5700,Phone.BRAND_IPHONE);
		pp.setColor(colors);
		nianhuanhuiPhonesPhones.add(pp);
		
		pp = new Phone("三星 Galaxy Note3 惊鸿大屏 震撼配置","nianhuanhui/phone_icon_sag_galaxy note3.png","惊鸿大屏 震撼配置",4999,5700,Phone.BRAND_IPHONE);
		pp.setColor(colors);
		nianhuanhuiPhonesPhones.add(pp);
		return nianhuanhuiPhonesPhones;
	}
	public List<Phone> readLocoalContractPhones(){
		List<PhoneColor>colors = new ArrayList<PhoneColor>();
		String[] note2_color_names = new String[] { "简约白" };
		int[] note2_colors = new int[] { 0xffffffff };
		String[][] note2_picsId = new String[][] {
				{"phone_pic_1.png","phone_pic_2.png",
					"phone_pic_3.png","phone_pic_4.png"} };
		for(int i=0;i<note2_color_names.length;i++){
			PhoneColor pc = new PhoneColor();
			pc.setColor(note2_colors[i]);
			pc.setPicId(note2_picsId[i]);
			pc.setName(note2_color_names[i]);
			colors.add(pc);
		}
		
		List<Phone>locoalPhones=new ArrayList<Phone>();
		Phone pp = new Phone();
		pp.setName("华为 Ascend P6	强悍配置 非凡体验 ");
		pp.setBrandCategory(Phone.BRAND_HUAWEI);
		pp.setIcon(("benditehui/phone_icon_huawei_ascend_p6.png"));
		pp.setPrice(2088);
		pp.setOriginalPrice(2688);
		pp.setColor(colors);
		pp.setIntroduce("P6	强悍配置 非凡体验 ");
		locoalPhones.add(pp);

		pp = new Phone();
		pp.setName("小米（MI）2S 全新升级 提速25%");
		pp.setBrandCategory(Phone.BRAND_XIAOMI);
		pp.setIcon(("benditehui/phone_icon_xiaomi_2s.png"));
		pp.setPrice(1888);
		pp.setOriginalPrice(1999);
		pp.setColor(colors);
		pp.setIntroduce("全新升级 提速25%");
		locoalPhones.add(pp);

		pp = new Phone();
		pp.setName("HTC 809D 双模双待 奢华大屏");
		pp.setBrandCategory(Phone.BRAND_HTC);
		pp.setIcon(("benditehui/phone_icon_htc809d.png"));
		pp.setPrice(4880);
		pp.setOriginalPrice(5288);
		pp.setColor(colors);
		pp.setIntroduce("双模双待 奢华大屏");
		locoalPhones.add(pp);

		pp = new Phone();
		pp.setName("华为 S7 pad 手机完美结合");
		pp.setBrandCategory(Phone.BRAND_HUAWEI);
		pp.setIcon(("benditehui/phone_icon_huaweis7.png"));
		pp.setPrice(1399);
		pp.setOriginalPrice(1999);
		pp.setColor(colors);
		pp.setIntroduce("pad 手机完美结合");
		locoalPhones.add(pp);

		pp = new Phone();
		pp.setName("海信EG939 炫彩屏幕 超薄机身");
		pp.setBrandCategory(Phone.BRAND_AILIXIN);
		pp.setIcon(("benditehui/phone_icon_haixin_eg939.png"));
		pp.setPrice(599);
		pp.setOriginalPrice(599);
		pp.setColor(colors);
		pp.setIntroduce("炫彩屏幕 超薄机身");
		locoalPhones.add(pp);

		pp = new Phone();
		pp.setName("华为 C8813D 千元神机 经典之作");
		pp.setBrandCategory(Phone.BRAND_HUAWEI);
		pp.setIcon(("benditehui/phone_icon_huaweic8813d.png"));
		pp.setPrice(799);
		pp.setOriginalPrice(1290);
		pp.setColor(colors);
		pp.setIntroduce("千元神机 经典之作");
		locoalPhones.add(pp);
			return locoalPhones;
	}
	
	public List<SetMeal>getSetMealTV(){
		List<SetMeal>tvSetMeals=new ArrayList<SetMeal>();
		
		SetMeal sm = new SetMeal();		
		sm.titles=new String[]{"资费","标清直播频道","高清直播频道","其他优惠","",""};
		sm.contents=new String[]{"10元/月","60套","无","每部每月4天以上不限时播放记录即免费用","",""};
		sm.setName("高清影视直播版");
		sm.setMonthlyRent(10);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		tvSetMeals.add(sm);		

		sm = new SetMeal();		
		sm.titles=new String[]{"资费","标清直播频道","高清直播频道","带宽","小区","其他优惠"};
		sm.contents=new String[]{"169元/月","60套","无","10M","光纤","预存宽带费送手机"};
		sm.setName("光纤e9_10M含直播版");
		sm.setMonthlyRent(169);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		tvSetMeals.add(sm);		

		sm = new SetMeal();		
		sm.titles=new String[]{"资费","标清直播频道","高清直播频道","特点","",""};
		sm.contents=new String[]{"30元/月","70套以上","10套","直播、点播、回看、时移","",""};
		sm.setName("高清影视直播版");
		sm.setMonthlyRent(30);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		tvSetMeals.add(sm);	

		sm = new SetMeal();		
		sm.titles=new String[]{"资费","标清直播频道","高清直播频道","带宽","小区","其他优惠"};
		sm.contents=new String[]{"169元/月","70套以上","10套","20M","光纤","预存宽带费送手机"};
		sm.setName("e169_20M送尊享版");
		sm.setMonthlyRent(10);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		tvSetMeals.add(sm);
		
		return tvSetMeals;
	}
	public List<SetMeal>getSetMealTrafficSetMeals(){
		List<SetMeal>trafficSetMeals=new ArrayList<SetMeal>();
		
		SetMeal sm = new SetMeal();		
		sm.titles=new String[]{"资费","宽带","小区","固话月租","可同时上网设备",""};
		sm.contents=new String[]{"98元/月","4M","非光纤","0 元","3",""};
		sm.setName("4M高速单宽98元包月");
		sm.setMonthlyRent(98);
		sm.setDesc1("高性价比");
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		trafficSetMeals.add(sm);		

		sm = new SetMeal();		
		sm.titles=new String[]{"资费","宽带","小区","固话月租","iTV","其他优惠"};
		sm.contents=new String[]{"169元/月","20M","光纤","0 元","赠送高清影视尊享版","预存宽带费送手机"};
		sm.setName("20M极速光纤宽带");
		sm.setMonthlyRent(169);
		sm.setPhoneNumber(2);
		sm.setDesc1("赠送高清影视尊享版");
		sm.setDesc2("预存宽带费送手机");
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		trafficSetMeals.add(sm);		

		sm = new SetMeal();		
		sm.titles=new String[]{"资费","宽带","小区","固话月租","可同时上网设备",""};
		sm.contents=new String[]{"98元/月","10M","光纤","0 元","3",""};
		sm.setName("10M光宽带98元包月");
		sm.setMonthlyRent(98);
		sm.setDesc1("高性价比");
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		trafficSetMeals.add(sm);		

		sm = new SetMeal();		
		sm.titles=new String[]{"资费","宽带","小区","固话月租","iTV","其他优惠"};
		sm.contents=new String[]{"299元/月","100M","光纤","0 元","赠送高清影视尊享版","预存宽带费0元购手机"};
		sm.setName("100M不限时极速光宽带");
		sm.setMonthlyRent(299);
		sm.setPhoneNumber(3);
		sm.setDesc1("赠送高清影视尊享版");
		sm.setDesc2("预存宽带费0元购手机");
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		trafficSetMeals.add(sm);
		
		return trafficSetMeals;
	}
	
	public List<SetMeal>getSetMealQuanqiutongs(){
		List<SetMeal>quanqiutong=new ArrayList<SetMeal>();
		SetMeal sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"49元","200MB","100小时","30分钟","30条","6条"};
		sm.setName("天翼乐享3G上网版_49元套餐");
		sm.setMonthlyRent(49);
		sm.setDomesticTraffic(200);
		sm.setFreeInlandCallIntroduce("市话、国内长话（含IP)、国内漫游共240分钟。");
		sm.setFreeInlandCall(100);
		sm.setFreeWifiHour(30);
		sm.setFreeSMS(30);
		sm.setFreeMMS(6);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_NET);
//		sm.setExceedCall("市话、国内长话（含IP)、国内漫游主叫0.15元/分钟，其他按标准资费计收");
//		sm.setExceedTraffic("国内上网0.30元/MB，20G自动断网。");
//		sm.setFreeAnswerRange("全国");
//		sm.setFreeServices("来显、彩铃月功能和免费189邮箱。");
//		sm.setSaveRentMonth(71);
//		sm.setSaveRentYear(852);
//		sm.setSaveTrafficMonth(236.7);
//		sm.setSaveTrafficYear(2840);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"69元","300MB","30小时","150分钟","30条","6条"};
		sm.setName("天翼乐享3G上网版_69元套餐");
		sm.setMonthlyRent(69);
		sm.setDomesticTraffic(300);
		sm.setFreeWifiHour(30);
		sm.setFreeInlandCall(150);
		sm.setFreeSMS(30);
		sm.setFreeMMS(6);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_NET);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"89元","400MB","30小时","240分钟","30条","6条"};
		sm.setName("天翼乐享3G上网版_89元套餐");
		sm.setMonthlyRent(89);
		sm.setDomesticTraffic(400);
		sm.setFreeWifiHour(30);
		sm.setFreeInlandCall(240);
		sm.setFreeSMS(30);
		sm.setFreeMMS(6);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_NET);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);

		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"129元","600MB","60小时","330分钟","60条","12条"};
		sm.setName("天翼乐享3G上网版_129元套餐");
		sm.setMonthlyRent(129);
		sm.setDomesticTraffic(600);
		sm.setFreeWifiHour(60);
		sm.setFreeInlandCall(330);
		sm.setFreeSMS(60);
		sm.setFreeMMS(12);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_NET);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"159元","750MB","60小时","450分钟","60条","12条"};
		sm.setName("天翼乐享3G上网版_159元套餐");
		sm.setMonthlyRent(159);
		sm.setDomesticTraffic(750);
		sm.setFreeWifiHour(60);
		sm.setFreeInlandCall(450);
		sm.setFreeSMS(60);
		sm.setFreeMMS(12);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_NET);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"189元","1024MB","60小时","600分钟","60条","12条"};
		sm.setName("天翼乐享3G上网版_189元套餐");
		sm.setMonthlyRent(189);
		sm.setDomesticTraffic(1024);
		sm.setFreeWifiHour(60);
		sm.setFreeInlandCall(600);
		sm.setFreeSMS(60);
		sm.setFreeMMS(12);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_NET);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"59元","60MB","10小时","160分钟","无","无"};
		sm.setName("天翼乐享3G聊天版_59元套餐");
		sm.setMonthlyRent(59);
		sm.setDomesticTraffic(60);
		sm.setFreeWifiHour(10);
		sm.setFreeInlandCall(160);
		sm.setFreeSMS(0);
		sm.setFreeMMS(0);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_CALL);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"89元","120MB","30小时","360分钟","无","无"};
		sm.setName("天翼乐享3G聊天版_89元套餐");
		sm.setMonthlyRent(89);
		sm.setDomesticTraffic(120);
		sm.setFreeWifiHour(30);
		sm.setFreeInlandCall(360);
		sm.setFreeSMS(0);
		sm.setFreeMMS(0);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_CALL);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"129元","120MB","30小时","660分钟","无","无"};
		sm.setName("天翼乐享3G聊天版_129元套餐");
		sm.setMonthlyRent(129);
		sm.setDomesticTraffic(120);
		sm.setFreeWifiHour(30);
		sm.setFreeInlandCall(660);
		sm.setFreeSMS(0);
		sm.setFreeMMS(0);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_CALL);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"159元","120MB","30小时","900分钟","无","无"};
		sm.setName("天翼乐享3G聊天版_159元套餐");
		sm.setMonthlyRent(159);
		sm.setDomesticTraffic(120);
		sm.setFreeWifiHour(30);
		sm.setFreeInlandCall(900);
		sm.setFreeSMS(0);
		sm.setFreeMMS(0);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_CALL);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		sm = new SetMeal();
		sm.titles=new String[]{"月费","国内流量","WiFi时长","国内语音拨打","短信","彩信"};
		sm.contents=new String[]{"189元","120MB","30小时","1200分钟","无","无"};
		sm.setName("天翼乐享3G聊天版_189元套餐");
		sm.setMonthlyRent(189);
		sm.setDomesticTraffic(120);
		sm.setFreeWifiHour(30);
		sm.setFreeInlandCall(1200);
		sm.setFreeSMS(0);
		sm.setFreeMMS(0);
		sm.setSpecial(SetMeal.SMEAL_SPECIAL_CALL);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		quanqiutong.add(sm);
		
		return quanqiutong;
	}
	public List<SetMeal>getSetMealsdonggandidai(){
		List<SetMeal>donggandidai=new ArrayList<SetMeal>();
		SetMeal sm = new SetMeal();
		sm.titles=new String[]{"月最低消费","本地流量","本地被叫","本地主叫国内","国内漫游主被叫","超值赠送"};
		sm.contents=new String[]{"10元","0.1元/M","免费","0.15元/分钟","0.39元/分钟","用2M流量\n送1分钟市话"};
		sm.setName("GGMM卡");
		sm.setMonthlyRent(10);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		donggandidai.add(sm);
		
		return donggandidai;
	}
	public List<SetMeal>getSetMealShengzhouxing(){
		List<SetMeal>shengzhouxing=new ArrayList<SetMeal>();
		SetMeal sm = new SetMeal();
		sm.titles=new String[]{"月基本费","本地流量","本地被叫","区域内市话主叫","区域外主叫","短信"};
		sm.contents=new String[]{"8元/月","0.0003元/KB","免费","0.08元/分钟","0.20元/分钟","无"};
		sm.setName("8分卡");
		sm.setMonthlyRent(10);
		sm.setVSDis("同档次套餐包含流量和时长全面超过联通，时长最高比联通多200%，流量最高多50%。");
		shengzhouxing.add(sm);
		
		return shengzhouxing;
	}
	
	public List<ContractPhoneSetmeal>getContractPhoneSetmeals(){
		List<ContractPhoneSetmeal> contractPhoneSetmealsontractPhoneSetmealdatasMore= new ArrayList<ContractPhoneSetmeal>();

			ContractPhoneSetmeal sm = new ContractPhoneSetmeal();
			sm.setTitle("289合约");
			sm.titles=new String[]{"月消费","手机款","总赠送话费","入网当月返还","次月起24个月返还",""};
			sm.contents=new String[]{"289元","2890元","2398元","322元","107元",""};
			sm.setMonthlyRent(289);
			sm.setPhonePrice(2890);
			sm.setTotalFreeRent(2398);
			sm.setFreeRentOnce(322);
			sm.setFreeRentAfferPerMonth(107);
			sm.setContractMonth(24);
			contractPhoneSetmealsontractPhoneSetmealdatasMore.add(sm);
			sm = new ContractPhoneSetmeal();
			sm.setTitle("389合约");
			sm.titles=new String[]{"月消费","手机款","总赠送话费","入网当月返还","次月起24个月返还",""};
			sm.contents=new String[]{"389元","0元","5288元","440元","202元",""};
			sm.setMonthlyRent(389);
			sm.setPhonePrice(0);
			sm.setTotalFreeRent(5288);
			sm.setFreeRentOnce(440);
			sm.setFreeRentAfferPerMonth(202);
			sm.setContractMonth(24);
			contractPhoneSetmealsontractPhoneSetmealdatasMore.add(sm);
			sm = new ContractPhoneSetmeal();
			sm.setTitle("129合约");
			sm.titles=new String[]{"月消费","手机款","总赠送话费","入网当月返还","次月起24个月返还",""};
			sm.contents=new String[]{"129元","3998元","1290元","162元","47元",""};
			sm.setMonthlyRent(129);
			sm.setPhonePrice(3998);
			sm.setTotalFreeRent(1290);
			sm.setFreeRentOnce(162);
			sm.setFreeRentAfferPerMonth(47);
			sm.setContractMonth(24);
			contractPhoneSetmealsontractPhoneSetmealdatasMore.add(sm);
			sm = new ContractPhoneSetmeal();
			sm.setTitle("189合约");
			sm.titles=new String[]{"月消费","手机款","总赠送话费","入网当月返还","次月起24个月返还",""};
			sm.contents=new String[]{"189元","3398元","1890元","210元","70元",""};
			sm.setMonthlyRent(189);
			sm.setPhonePrice(3398);
			sm.setTotalFreeRent(1890);
			sm.setFreeRentOnce(210);
			sm.setFreeRentAfferPerMonth(70);
			sm.setContractMonth(24);
			contractPhoneSetmealsontractPhoneSetmealdatasMore.add(sm);
			return contractPhoneSetmealsontractPhoneSetmealdatasMore;
	}

	@Override
	public void onDbSyncFinished(boolean succeeded) {
		if (Logger.DEBUG) {
			Logger.debug("TitleContentManager.onDbSyncFinished: " + succeeded);
		}
		if (succeeded) {
			if (dbRefreshListener != null) {
				dbRefreshListener.onDbRefreshed();
			}
		}
	}

}
