package com.act.sctc.util;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.act.sctc.App;
import com.act.sctc.R;
import com.act.sctc.User;
import com.act.sctc.db.BusinessColumn;
import com.act.sctc.db.BusinessDetailColumn;
import com.act.sctc.db.CategoryColumn;
import com.act.sctc.db.DBHelper;
import com.act.sctc.db.FilterColumn;
import com.act.sctc.db.ItemColumn;
import com.act.sctc.db.ItemObjectRelationColumn;
import com.act.sctc.db.PackageColumn;
import com.act.sctc.db.PhoneColorColumn;
import com.act.sctc.db.PhoneColumn;
import com.act.sctc.db.PhoneContractColumn;
import com.act.sctc.db.PhoneContractRelationColumn;
import com.act.sctc.db.ProductDetailColumn;
import com.act.sctc.db.PromotionColumn;
import com.act.sctc.db.ResourceColumn;
import com.act.sctc.util.HttpRequest.HttpRequestListener;
import com.custom.view.CustomDialog;

public class DbSync implements HttpRequestListener {
	public final static boolean USE_DB_SYNC = true;

	private final static String SETTINGS_FILE = "settings.xml";
	private final static String KEY_SYNC_SERVER_URL = "SYNC_SERVER_URL";
	private final static String DEFAULT_SYNC_SERVER_URL = "http://10.202.13.44:8080/MobileSaleServer";

	private static String mServerUrl;
	private static JSONObject mJsonDb;
	private static List<Resource> mDownloadingResourceList = new ArrayList<Resource>();
	private CustomDialog mDownloadDialog;
	private Context mContext;
	private DBHelper mDbHelper;
	private DbSyncListener mSyncListener;

	public interface DbSyncListener {
		public void onDbSyncFinished(boolean succeeded);
	}

	public DbSync() {
	}

	public static void setServerUrl(Context context, String url) {
		if (url == null) {
			return;
		}
		url = url.trim();
		while (url.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		if (url.length() < 10) {
			return;
		}
		try {
			URI.create(url + "/a");
		} catch (Exception e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage("URL格式错误，请重新设置")
					.setPositiveButton("确定", null);
			builder.show();
			return;
		}

		SharedPreferences preferences = context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(KEY_SYNC_SERVER_URL, url);
		editor.commit();

		mServerUrl = url;
	}

	public static String getServerUrl(Context context) {
		if (mServerUrl == null || mServerUrl.length() < 10) {
			SharedPreferences preferences = context.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
			mServerUrl = preferences.getString(KEY_SYNC_SERVER_URL, DEFAULT_SYNC_SERVER_URL);
		}
		return mServerUrl;
	}

	public void sync(Context context, DbSyncListener lisenter) {
		mContext = context;
		mSyncListener = lisenter;
		if (mDbHelper == null) {
			mDbHelper = DBHelper.getInstance(context);
		}
		mDownloadDialog = new CustomDialog(context);
		mDownloadDialog.setCancelable(false);
		mDownloadDialog.show();
		mDownloadDialog.resetView();
		mDownloadDialog.setMessage("1.正在同步系统数据。。。");
		new HttpRequest(this).get(0, mServerUrl + "/ws/dbsync?token=" + App.getCurrentUser().token);
	}

	@Override
	public void onHttpRequestFailed(int tag) {
		Logger.debug("onHttpRequestFailed");
		finishDownload("4.更新错误，请重试！", false);
	}

	@Override
	public void onHttpRequestOK(int tag, byte[] data) {
		Logger.debug("onHttpRequestOK");

		try {
			if (tag == 0) {
				// database information
				dbSync(data);
			}
			downloadNextResource();
		} catch (Exception e) {
			e.printStackTrace();
			finishDownload("4.更新错误，请重试！", false);
		}
	}

	private static class Resource {
		public int type;
		public int length;
		public String path;

		public static Resource get(JSONObject resource) throws JSONException {
			Resource res = new Resource();
			res.type = resource.getInt("type");
			res.length = resource.getInt("len");
			res.path = resource.getString("path");
			return res;
		}
	}

	private void dbSync(byte[] data) throws JSONException, IOException {
		mDownloadingResourceList.clear();

		String response = new String(data, "utf-8");
		mJsonDb = new JSONObject(response);
		int errorCode = mJsonDb.getInt("errCode");
		if (errorCode != 0) {
			throw new IOException(mJsonDb.getString("errMsg"));
		}

		JSONArray resourceArray = mJsonDb.getJSONArray("resource");
		int count = resourceArray.length();
		for (int i = 0; i < count; i++) {
			JSONObject jsonRes = resourceArray.getJSONObject(i);
			Resource res = Resource.get(jsonRes);
			File file = getLocalResourceFile(res);
			if (file != null && (!file.exists() || file.length() != res.length)) {
				mDownloadingResourceList.add(res);
			}
		}
		mDownloadDialog.setMessage("2.正在同步多媒体文件。。。");
	}

	private void finishDownload(String msg, boolean succeeded) {
		mDownloadDialog.setMessage(msg);
		mDownloadDialog.done();
		mDownloadDialog.addPositiveButton(R.string.back, new View.OnClickListener() {
			public void onClick(View v) {
				mDownloadDialog.dismiss();
			}
		});

		mJsonDb = null;
		mDownloadingResourceList.clear();

		mSyncListener.onDbSyncFinished(succeeded);
	}

	private File getLocalResourceFile(Resource res) {
		File file = null;
		if (res.length > 0 && res.path != null && res.path.length() > 0) {
			int pos = res.path.lastIndexOf('/');
			String fileName = res.path.substring(pos + 1);
			if (res.type == ResourceColumn.TYPE_VIDEO) {
				// video
				file = new File(App.VIDEO_DIR + fileName);
			} else if (res.type == ResourceColumn.TYPE_IMG) {
				// image
				file = new File(App.IMG_DIR + fileName);
			}
		}
		return file;
	}

	private void downloadNextResource() throws JSONException {
		if (mDownloadingResourceList.size() > 0) {
			Resource res = mDownloadingResourceList.get(0);
			mDownloadingResourceList.remove(0);
			File file = getLocalResourceFile(res);
			file.getParentFile().mkdirs();
			new HttpRequest(this).get(1, mServerUrl + res.path, file);
		} else {
			dbSync();
		}
	}

	private static class SyncHandler extends Handler {
		private WeakReference<DbSync> dbSyncRef;

		public SyncHandler(DbSync dbSync) {
			super();
			dbSyncRef = new WeakReference<DbSync>(dbSync);
		}

		public void handleMessage(Message msg) {
			DbSync dbSync = dbSyncRef.get();
			if (dbSync != null) {
				switch (msg.what) {
				case 0:
					dbSync.finishDownload("4.更新错误，请重试！", false);
					break;
				case 1:
					dbSync.finishDownload("4.您的设备已完成更新!", true);
					break;
				}
			}
		}
	};

	private void dbSync() throws JSONException {
		mDownloadDialog.setMessage("3.正在初始化数据。。。");

		final SyncHandler handler = new SyncHandler(this);
		new Thread() {
			public void run() {
				try {
					syncBusiness(mJsonDb.getJSONArray("business"));
					syncBusinessDetail(mJsonDb.getJSONArray("businessDetail"));
					syncCategory(mJsonDb.getJSONArray("category"));
					syncFilter(mJsonDb.getJSONArray("filter"));
					syncItem(mJsonDb.getJSONArray("item"));
					syncItemObjectRelation(mJsonDb.getJSONArray("itemObjectRelation"));
					syncPackage(mJsonDb.getJSONArray("Package"));
					syncPhone(mJsonDb.getJSONArray("phone"));
					syncPhoneColor(mJsonDb.getJSONArray("phoneColor"));
					syncPhoneContract(mJsonDb.getJSONArray("phoneContract"));
					syncPhoneContractRelation(mJsonDb.getJSONArray("phoneContractRelation"));
					syncProductDetail(mJsonDb.getJSONArray("productDetail"));
					syncPromotion(mJsonDb.getJSONArray("promotion"));
					syncResource(mJsonDb.getJSONArray("resource"));
					syncUser(mJsonDb.getJSONObject("user"));
					handler.sendMessage(Message.obtain(handler, 1));
				} catch (Exception e) {
					handler.sendMessage(Message.obtain(handler, 0));
					e.printStackTrace();
				}
			}
		}.start();

	}

	private void syncBusiness(JSONArray jsonBusiness) throws JSONException {
		mDbHelper.execSQL("delete from business");
		int count = jsonBusiness.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject biz = jsonBusiness.getJSONObject(i);
			values.put(BusinessColumn._ID, biz.getInt("id"));
			values.put(BusinessColumn.name, biz.getString("nm"));
			values.put(BusinessColumn.img, biz.getInt("img"));
			mDbHelper.insert(BusinessColumn.TABLE_NAME, values);
		}
	}

	private void syncBusinessDetail(JSONArray jsonBusinessDetail) throws JSONException {
		mDbHelper.execSQL("delete from business_detail");
		int count = jsonBusinessDetail.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject biz = jsonBusinessDetail.getJSONObject(i);
			values.put(BusinessDetailColumn._ID, biz.getInt("id"));
			values.put(BusinessDetailColumn.business_id, biz.getInt("biz"));
			values.put(BusinessDetailColumn.RESOURCE_ID, biz.getInt("res"));
			values.put(BusinessDetailColumn.ICON, biz.getInt("icon"));
			values.put(BusinessDetailColumn.TITLE, biz.getString("title"));
			values.put(BusinessDetailColumn.SUBTITLE, biz.getString("subTitle"));
			values.put(BusinessDetailColumn.SORT, biz.getInt("sort"));
			mDbHelper.insert(BusinessDetailColumn.TABLE_NAME, values);
		}
	}

	private void syncCategory(JSONArray jsonCategory) throws JSONException {
		mDbHelper.execSQL("delete from category");
		int count = jsonCategory.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject cat = jsonCategory.getJSONObject(i);
			values.put(CategoryColumn._ID, cat.getInt("id"));
			values.put(CategoryColumn.business_id, cat.getInt("biz"));
			values.put(CategoryColumn.subclass, cat.getString("sub"));
			values.put(CategoryColumn.name, cat.getString("nm"));
			mDbHelper.insert(CategoryColumn.TABLE_NAME, values);
		}
	}

	private void syncFilter(JSONArray jsonFilter) throws JSONException {
		mDbHelper.execSQL("delete from filter");
		int count = jsonFilter.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject filter = jsonFilter.getJSONObject(i);
			values.put(FilterColumn._ID, filter.getInt("id"));
			values.put(FilterColumn.category_id, filter.getInt("cat"));
			values.put(FilterColumn.used, 1);
			values.put(FilterColumn.sort, filter.getInt("sort"));
			values.put(FilterColumn.name, filter.getString("nm"));
			mDbHelper.insert(FilterColumn.TABLE_NAME, values);
		}
	}

	private void syncItem(JSONArray jsonItem) throws JSONException {
		mDbHelper.execSQL("delete from item");
		int count = jsonItem.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject item = jsonItem.getJSONObject(i);
			values.put(ItemColumn._ID, item.getInt("id"));
			values.put(ItemColumn.filter_id, item.getInt("flt"));
			values.put(ItemColumn.sort, item.getInt("sort"));
			values.put(ItemColumn.name, item.getString("nm"));
			mDbHelper.insert(ItemColumn.TABLE_NAME, values);
		}
	}

	private void syncItemObjectRelation(JSONArray jsonRelation) throws JSONException {
		mDbHelper.execSQL("delete from item_object_relation");
		int count = jsonRelation.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject relation = jsonRelation.getJSONObject(i);
			values.put(ItemObjectRelationColumn._ID, relation.getInt("id"));
			values.put(ItemObjectRelationColumn.item_id, relation.getInt("item"));
			values.put(ItemObjectRelationColumn.object_id, relation.getInt("obj"));
			values.put(ItemObjectRelationColumn.category_id, relation.getInt("cat"));

			mDbHelper.insert(ItemObjectRelationColumn.TABLE_NAME, values);
		}
	}

	private void syncPackage(JSONArray jsonPackage) throws JSONException {
		mDbHelper.execSQL("delete from package");
		int count = jsonPackage.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject pack = jsonPackage.getJSONObject(i);
			values.put(PackageColumn._ID, pack.getInt("id"));
			values.put(PackageColumn.name, pack.getString("nm"));
			values.put(PackageColumn.business_id, pack.getInt("biz"));
			values.put(PackageColumn.price, pack.getInt("price"));
			values.put(PackageColumn.attr_name1, pack.getString("an1"));
			values.put(PackageColumn.attr_value1, pack.getString("av1"));
			values.put(PackageColumn.attr_name2, pack.getString("an2"));
			values.put(PackageColumn.attr_value2, pack.getString("av2"));
			values.put(PackageColumn.attr_name3, pack.getString("an3"));
			values.put(PackageColumn.attr_value3, pack.getString("av3"));
			values.put(PackageColumn.attr_name4, pack.getString("an4"));
			values.put(PackageColumn.attr_value4, pack.getString("av4"));
			values.put(PackageColumn.attr_name5, pack.getString("an5"));
			values.put(PackageColumn.attr_value5, pack.getString("av5"));
			values.put(PackageColumn.attr_name6, pack.getString("an6"));
			values.put(PackageColumn.attr_value6, pack.getString("av6"));
			values.put(PackageColumn.vs_name, pack.getString("vn"));
			values.put(PackageColumn.vs_value, pack.getString("vv"));
			values.put(PackageColumn.desc1, pack.getString("d1"));
			values.put(PackageColumn.desc2, pack.getString("d2"));
			values.put(PackageColumn.phone_number, pack.getInt("phoneNumber"));
			values.put(PackageColumn.filter_item_id, pack.getInt("filter"));
			mDbHelper.insert(PackageColumn.TABLE_NAME, values);
		}
	}

	private void syncPhone(JSONArray jsonPhone) throws JSONException {
		mDbHelper.execSQL("delete from phone");
		int count = jsonPhone.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject phone = jsonPhone.getJSONObject(i);
			values.put(PhoneColumn._ID, phone.getInt("id"));
			values.put(PhoneColumn.thumbnail, phone.getInt("thumbnail"));
			values.put(PhoneColumn.sale_icon, phone.getInt("icon"));
			values.put(PhoneColumn.price, phone.getInt("price"));
			values.put(PhoneColumn.sale_price, phone.getInt("salePrice"));
			values.put(PhoneColumn.ad_desc, phone.getString("d0"));
			values.put(PhoneColumn.ad_desc1, phone.getString("d1"));
			values.put(PhoneColumn.ad_desc2, phone.getString("d2"));
			values.put(PhoneColumn.ad_desc3, phone.getString("d3"));
			values.put(PhoneColumn.ad_desc4, phone.getString("d4"));
			values.put(PhoneColumn.series, phone.getString("ser"));
			values.put(PhoneColumn.brand, phone.getString("brd"));
			values.put(PhoneColumn.type, phone.getString("type"));
			values.put(PhoneColumn.start_time, phone.getString("stTime"));
			values.put(PhoneColumn.look_design, phone.getString("look"));
			values.put(PhoneColumn.os, phone.getString("os"));
			values.put(PhoneColumn.smartphone, phone.getString("smart"));
			values.put(PhoneColumn.cpu_core, phone.getString("cpuCore"));
			values.put(PhoneColumn.cpu_rate, phone.getString("cpuRate"));
			values.put(PhoneColumn.keyborad, phone.getString("keyboard"));
			values.put(PhoneColumn.input, phone.getString("input"));
			values.put(PhoneColumn.op_sign, phone.getString("opSign"));
			values.put(PhoneColumn.net_standard, phone.getString("net"));
			values.put(PhoneColumn.net_rate, phone.getString("netRate"));
			values.put(PhoneColumn.browser, phone.getString("browser"));
			values.put(PhoneColumn.device_mem, phone.getString("devMem"));
			values.put(PhoneColumn.run_mem, phone.getString("runMem"));
			values.put(PhoneColumn.card_mem, phone.getString("cardMem"));
			values.put(PhoneColumn.extend_mem, phone.getString("extMem"));
			values.put(PhoneColumn.screen_szie, phone.getString("scrSize"));
			values.put(PhoneColumn.screen_color, phone.getString("scrColor"));
			values.put(PhoneColumn.screen_resolution, phone.getString("scrRes"));
			values.put(PhoneColumn.gravity, phone.getString("gravity"));
			values.put(PhoneColumn.touch, phone.getString("touch"));
			values.put(PhoneColumn.music, phone.getString("music"));
			values.put(PhoneColumn.video, phone.getString("video"));
			values.put(PhoneColumn.ebook, phone.getString("ebook"));
			values.put(PhoneColumn.camera, phone.getString("camera"));
			values.put(PhoneColumn.sensor, phone.getString("sensor"));
			values.put(PhoneColumn.video_maker, phone.getString("videoMaker"));
			values.put(PhoneColumn.photo_mode, phone.getString("photoMode"));
			values.put(PhoneColumn.continue_photo, phone.getString("conPhoto"));
			values.put(PhoneColumn.resolution_photo, phone.getString("resPhoto"));
			values.put(PhoneColumn.camera_other, phone.getString("camOther"));
			values.put(PhoneColumn.sub_camera, phone.getString("subCam"));
			values.put(PhoneColumn.auto_focus, phone.getString("autoFocus"));
			values.put(PhoneColumn.gps, phone.getString("gps"));
			values.put(PhoneColumn.wifi, phone.getString("wifi"));
			values.put(PhoneColumn.bluetooth, phone.getString("bluetooth"));
			values.put(PhoneColumn.office, phone.getString("office"));
			values.put(PhoneColumn.email, phone.getString("email"));
			values.put(PhoneColumn.calculator, phone.getString("calculator"));
			values.put(PhoneColumn.device_size, phone.getString("devSize"));
			values.put(PhoneColumn.device_quality, phone.getString("devQuality"));
			values.put(PhoneColumn.device_matierial, phone.getString("devMatierial"));
			values.put(PhoneColumn.battery_category, phone.getString("batCat"));
			values.put(PhoneColumn.battery_capacity, phone.getString("batCap"));
			values.put(PhoneColumn.speek_time, phone.getString("spkTime"));
			values.put(PhoneColumn.idle_time, phone.getString("idleTime"));
			values.put(PhoneColumn.headset, phone.getString("headset"));
			values.put(PhoneColumn.attr1, phone.getString("a1"));
			values.put(PhoneColumn.attr2, phone.getString("a2"));
			values.put(PhoneColumn.attr3, phone.getString("a3"));
			values.put(PhoneColumn.attr4, phone.getString("a4"));
			values.put(PhoneColumn.attr5, phone.getString("a5"));
			values.put(PhoneColumn.attr6, phone.getString("a6"));
			values.put(PhoneColumn.attr7, phone.getString("a7"));
			values.put(PhoneColumn.attr8, phone.getString("a8"));
			values.put(PhoneColumn.attr9, phone.getString("a9"));
			values.put(PhoneColumn.attr10, phone.getString("a10"));

			mDbHelper.insert(PhoneColumn.TABLE_NAME, values);
		}
	}

	private void syncPhoneColor(JSONArray jsonPhoneColor) throws JSONException {
		mDbHelper.execSQL("delete from phone_color");
		int count = jsonPhoneColor.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject phoneColor = jsonPhoneColor.getJSONObject(i);
			values.put(PhoneColorColumn._ID, phoneColor.getInt("id"));
			values.put(PhoneColorColumn.phone_id, phoneColor.getInt("phone"));
			values.put(PhoneColorColumn.img1, phoneColor.getInt("img1"));
			values.put(PhoneColorColumn.img2, phoneColor.getInt("img2"));
			values.put(PhoneColorColumn.img3, phoneColor.getInt("img3"));
			values.put(PhoneColorColumn.img4, phoneColor.getInt("img4"));
			values.put(PhoneColorColumn.color, phoneColor.getString("color"));
			values.put(PhoneColorColumn.value, phoneColor.getString("value"));
			values.put(PhoneColorColumn.comment, phoneColor.getString("cmt"));

			mDbHelper.insert(PhoneColorColumn.TABLE_NAME, values);
		}
	}

	private void syncPhoneContract(JSONArray jsonPhoneContract) throws JSONException {
		mDbHelper.execSQL("delete from phone_contract");
		int count = jsonPhoneContract.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject phoneContract = jsonPhoneContract.getJSONObject(i);
			values.put(PhoneContractColumn._ID, phoneContract.getInt("id"));
			values.put(PhoneContractColumn.name, phoneContract.getString("nm"));
			values.put(PhoneContractColumn.type, phoneContract.getInt("type"));
			values.put(PhoneContractColumn.price, phoneContract.getInt("price"));
			values.put(PhoneContractColumn.attr_name1, phoneContract.getString("an1"));
			values.put(PhoneContractColumn.attr_value1, phoneContract.getString("av1"));
			values.put(PhoneContractColumn.attr_name2, phoneContract.getString("an2"));
			values.put(PhoneContractColumn.attr_value2, phoneContract.getString("av2"));
			values.put(PhoneContractColumn.attr_name3, phoneContract.getString("an3"));
			values.put(PhoneContractColumn.attr_value3, phoneContract.getString("av3"));
			values.put(PhoneContractColumn.attr_name4, phoneContract.getString("an4"));
			values.put(PhoneContractColumn.attr_value4, phoneContract.getString("av4"));
			values.put(PhoneContractColumn.attr_name5, phoneContract.getString("an5"));
			values.put(PhoneContractColumn.attr_value5, phoneContract.getString("av5"));
			values.put(PhoneContractColumn.attr_name6, phoneContract.getString("an6"));
			values.put(PhoneContractColumn.attr_value6, phoneContract.getString("av6"));
			mDbHelper.insert(PhoneContractColumn.TABLE_NAME, values);
		}
	}

	private void syncPhoneContractRelation(JSONArray jsonContractRelation) throws JSONException {
		mDbHelper.execSQL("delete from phone_contract_relation");
		int count = jsonContractRelation.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject phoneContract = jsonContractRelation.getJSONObject(i);
			values.put(PhoneContractRelationColumn._ID, phoneContract.getInt("id"));
			values.put(PhoneContractRelationColumn.phone_id, phoneContract.getInt("phone"));
			values.put(PhoneContractRelationColumn.contract_id, phoneContract.getInt("contract"));
			mDbHelper.insert(PhoneContractRelationColumn.TABLE_NAME, values);
		}
	}

	private void syncProductDetail(JSONArray jsonProductDetail) throws JSONException {
		mDbHelper.execSQL("delete from product_detail");
		int count = jsonProductDetail.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject productDetail = jsonProductDetail.getJSONObject(i);
			values.put(ProductDetailColumn._ID, productDetail.getInt("id"));
			values.put(ProductDetailColumn.business_id, productDetail.getInt("biz"));
			values.put(ProductDetailColumn.img_id, productDetail.getInt("img"));
			values.put(ProductDetailColumn.phone_id, productDetail.getInt("phone"));
			values.put(ProductDetailColumn.title, productDetail.getString("title"));
			values.put(ProductDetailColumn.sort, productDetail.getInt("sort"));
			mDbHelper.insert(ProductDetailColumn.TABLE_NAME, values);
		}
	}

	private void syncPromotion(JSONArray jsonPromotion) throws JSONException {
		mDbHelper.execSQL("delete from promotion");
		int count = jsonPromotion.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject promotion = jsonPromotion.getJSONObject(i);
			values.put(PromotionColumn._ID, promotion.getInt("id"));
			values.put(PromotionColumn.img, promotion.getInt("img"));
			values.put(PromotionColumn.detail_img, promotion.getInt("detailImg"));
			values.put(PromotionColumn.business_id, promotion.getInt("biz"));
			values.put(PromotionColumn.filter_item_id, promotion.getInt("item"));
			values.put(PromotionColumn.phone_id, promotion.getInt("phone"));
			values.put(PromotionColumn.name, promotion.getString("nm"));
			mDbHelper.insert(PromotionColumn.TABLE_NAME, values);
		}
	}

	private void syncResource(JSONArray jsonResource) throws JSONException {
		mDbHelper.execSQL("delete from resource");
		int count = jsonResource.length();
		for (int i = 0; i < count; i++) {
			ContentValues values = new ContentValues();
			JSONObject res = jsonResource.getJSONObject(i);
			values.put(ResourceColumn._ID, res.getInt("id"));
			String path = res.getString("path");
			int pos = path.lastIndexOf('/');
			values.put(ResourceColumn.PATH, path.substring(pos + 1));
			values.put(ResourceColumn.TYPE, res.getInt("type"));
			mDbHelper.insert(ResourceColumn.TABLE_NAME, values);
		}
	}

	private void syncUser(JSONObject jsonUser) throws JSONException {
		User user = new User();
		user.userId = jsonUser.getInt("id");
		user.username = jsonUser.getString("nm");
		user.eid = jsonUser.getString("eid");
		user.password = jsonUser.getString("pwd");
		user.token = jsonUser.getString("token");
		App.updateCurrentUser(mContext, user, true);
	}
}
