package com.accenture.mbank.view;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.R;
import com.accenture.mbank.model.BranchListModel;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.LogManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends ItemizedOverlay implements
		OnClickListener, OnGestureListener {
	private GestureDetector gestureScanner = new GestureDetector(this);

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	boolean showPop = true;

	private MapView mMapView;

	private PopView mPopView;

	private View mSearch_point;

	//private View mGetDirectionView;

	/**
	 * 地图的中心坐标
	 */
	private int point_X;
	private int point_Y;

	private LinearLayout mRouteDetail;

	private MapLayout mMapLayout;

	private MapRouteOverlay mRouteOverlay = null;

	ImageButton getDirectionBtn;

	Button mSearchPointButton;

	Context context;

	List<String> directors;

	/**
	 * 地图中心的经纬度
	 */
	float point_longitude = 0;
	float point_latitude = 0;

	int width;
	int height;

	public MapItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		populate();
	}

	public MapItemizedOverlay(Drawable defaultMarker, MapLayout aMapFragment) {
		super(boundCenterBottom(defaultMarker));
		directors = new ArrayList<String>();
		mMapLayout = aMapFragment;
		mMapView = mMapLayout.getMapView();

		point_X = mMapView.getLayoutParams().width / 2;
		point_Y = mMapView.getLayoutParams().height / 2;

		gestureScanner.setIsLongpressEnabled(true);
		mPopView = (PopView) mMapLayout.getPopView();
		mSearch_point = (LinearLayout) mMapLayout.getSearchPoint(); // 拿到SearchPoint组件
		//mGetDirectionView = (LinearLayout) mMapLayout.getGetDirection();
		
		mRouteDetail = mMapLayout.getRouteDetail();
		
		getDirectionBtn = (ImageButton)mPopView.findViewById(R.id.getdirections);
		
		mSearchPointButton = (Button) mSearch_point.findViewById(R.id.search_point_button);
		mSearchPointButton.setOnClickListener(this);
		context = mMapLayout.getContext();
		populate();

	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	public void clearOverlay() {
		mOverlays.clear();
		setLastFocusedIndex(-1);
		populate();
	}

	public void populateItem() {
		setLastFocusedIndex(-1);
		populate();
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		setLastFocusedIndex(-1);
		populate();
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		LogManager.d("size-onTap1" + +this.size() + this);
		mPopView.setVisibility(View.GONE);
		mSearch_point.setVisibility(View.VISIBLE);
		//mGetDirectionView.setVisibility(View.GONE);
		return super.onTap(p, mapView);
	}

	@Override
	protected boolean onTap(int index) {
		LogManager.d("size-onTap2" + this.size() + this);
		if (!showPop) {
			return true;
		}
		OverlayItem item = mOverlays.get(index);

		GeoPoint pt = item.getPoint();
		if (width == 0 && height == 0) {
			width = mMapLayout.getWidth();
			height = mMapLayout.getHeight();
		}

		int popWidth = 2 * width / 3;
		int popHeight = 2 * height / 3;
		MapView.LayoutParams layoutParams = new MapView.LayoutParams(popWidth,popHeight, pt, MapView.LayoutParams.BOTTOM);
		mMapView.updateViewLayout(mPopView, layoutParams);
		float left = (popWidth * 100) / 479;
		float bottom = (popHeight * 20) / 274;

		System.out.println("left:" + left);
		System.out.println("bottom:" + bottom);
		
//		updateGetDirectionView();
		//mGetDirectionView.setVisibility(View.VISIBLE);
		getDirectionBtn.setTag(item);
		getDirectionBtn.setOnClickListener(this);
		layoutParams.x = (int) -left;
		layoutParams.y = (int) -bottom;
		showDetail(item);

		ScaleAnimation scaleAnimation = new ScaleAnimation(0.1f, 1.05f, 0.1f,1.05f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(100);
		mPopView.startAnimation(scaleAnimation);
		
		mMapView.invalidate();
		return true;
	}

//	private void updateGetDirectionView(){
//		int y = point_Y * 2 - 10;
//		int x = point_X / 9;
//		
//		GeoPoint pt = mMapView.getProjection().fromPixels(x, y);
//		int mGetDirection_view_width = getDirectionBtn.getLayoutParams().width;
//		int mGetDirection_view_height = getDirectionBtn.getLayoutParams().height;
//		MapView.LayoutParams getDirectionlayoutParams = new MapView.LayoutParams(mGetDirection_view_width, mGetDirection_view_height,pt, MapView.LayoutParams.BOTTOM);
//		mMapView.updateViewLayout(mGetDirectionView, getDirectionlayoutParams);
//		mMapView.invalidate();
//	}
	
	private void showDetail(OverlayItem item) {
		if (item instanceof BankOverlayItem) {
			BankOverlayItem bankOverlayItem = (BankOverlayItem) item;
			BranchListModel branchListModel = bankOverlayItem.getBrankBranchListModel();
			mPopView.setBranchListModel(branchListModel);
			mPopView.setVisibility(View.VISIBLE);
			mSearch_point.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == getDirectionBtn) {
			//OLD
			GeoPoint start = mMapLayout.mCurrentLocationPoint;
//			// GeoPoint start = new GeoPoint(41907000,12507607);// 测试
//			OverlayItem item = (OverlayItem) v.getTag();
//			GeoPoint end = item.getPoint();
//			if (MapLayout.debugChinaDevice) {
//				int latitude = (int) (44.648837 * 1e6);
//				int longitude = (int) (10.920087000000001 * 1e6);
//				start = new GeoPoint(latitude, longitude);
//			}
//			showRoute(start, end);
			OverlayItem item = (OverlayItem) v.getTag();
			GeoPoint end = item.getPoint();
//			String uri = "geo:"+ end.getLatitudeE6()/ 1E6 + "," + end.getLongitudeE6()/ 1E6;
//			Log.d("uri", uri);
//			Activity activity = (Activity)context;
//			activity.startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
			String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+ start.getLatitudeE6()/ 1E6+","+start.getLongitudeE6()/ 1E6+"&daddr="+end.getLatitudeE6()/ 1E6+","+end.getLongitudeE6()/ 1E6;
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
			intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
			Activity activity = (Activity)context;
			activity.startActivity(intent);
			
		} else if (v == mSearchPointButton) {
			ProgressOverlay progressOverlay = new ProgressOverlay(context);
			progressOverlay.show(context.getString(R.string.loading), new OnProgressEvent() {
				@Override
				public void onProgress() {
					GeoPoint pt = mMapView.getProjection().fromPixels(point_X,point_Y);
					mMapLayout.searchBarch("", pt);
				}
			});
		}
	}

	private void showRoute(GeoPoint start, GeoPoint end) {
		setData(start, end);
		if (mRouteOverlay != null) {
			if (mMapView.getOverlays().contains(mRouteOverlay)) {
				mMapView.getOverlays().remove(mRouteOverlay);
				mMapView.invalidate();
			}
		}
		mRouteOverlay = new MapRouteOverlay(context, mMapView, start, end);
		mRouteOverlay.CalculateRoute();
		mMapView.getOverlays().add(mRouteOverlay);
		mMapView.invalidate();
	}

	private void setData(final GeoPoint start, final GeoPoint end) {
		ProgressOverlay progressOverlay = new ProgressOverlay(context);
		progressOverlay.show(context.getString(R.string.loading), new OnProgressEvent() {
			@Override
			public void onProgress() {
				calculateRoutePoints(start, end);
				handler.sendEmptyMessage(0);
			}
		});
	}

	public List<GeoPoint> calculateRoutePoints(GeoPoint aStartPoint,
			GeoPoint aEndPoint) {
		// Get Points from Google direction API
		// "http://maps.google.com/maps/api/directions/xml?origin=39.928774,116.460801&destination=39.918774,116.458801&sensor=false&mode=walking";
		String url = "http://maps.google.com/maps/api/directions/json?origin="
				+ aStartPoint.getLatitudeE6() / 1e6 + ","
				+ aStartPoint.getLongitudeE6() / 1e6 + "&destination="
				+ aEndPoint.getLatitudeE6() / 1e6 + ","
				+ aEndPoint.getLongitudeE6() / 1e6
				+ "&sensor=true&mode=walking";
		LogManager.d("URL:" + url);

		HttpGet get = new HttpGet(url);
		String strResult = "";
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			HttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = null;
			httpResponse = httpClient.execute(get);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			LogManager.d("Exception");
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(strResult);
			JSONArray jsonArray = jsonObject.getJSONArray("routes");

			JSONObject jsonObject2 = jsonArray.getJSONObject(0);

			JSONArray array = jsonObject2.getJSONArray("legs");
			JSONObject jsonObject3 = array.getJSONObject(0);

			// System.out.println(jsonArray);
			// LogManager.d(jsonArray.toString());

			JSONArray steps = jsonObject3.getJSONArray("steps");

			LogManager.d(steps.toString());
			directors.clear();
			for (int i = 0; i < steps.length(); i++) {
				JSONObject obje = steps.getJSONObject(i);

				String html = obje.optString("html_instructions");
				LogManager.d(html);
				directors.add(html);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 用户轻触触摸屏，由1个MotionEvent ACTION_DOWN触发
	public boolean onDown(MotionEvent e) {
		LogManager.i("onDown");
		return false;
	}

	// 用户按下触摸屏、快速移动后松开，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE, 1个ACTION_UP触发
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		LogManager.i("onFling");
		return false;
	}

	// 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
	public void onLongPress(MotionEvent e) {
		GeoPoint p = mMapView.getProjection().fromPixels((int) e.getX(),
				(int) e.getY());
		// 长按后获取到的坐标
		LogManager.i("onLongPress");
	}

	// 用户按下触摸屏，并拖动，由1个MotionEvent ACTION_DOWN, 多个ACTION_MOVE触发
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		GeoPoint pt = mMapView.getProjection().fromPixels(point_X, point_Y);
		handler.sendEmptyMessage(1);
		LogManager.i("onScroll" + pt.getLongitudeE6() + "----------"
				+ pt.getLatitudeE6());
		return false;
	}

	/*
	 * 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
	 * 注意和onDown()的区别，强调的是没有松开或者拖动的状态
	 */
	public void onShowPress(MotionEvent e) {
		LogManager.i("onShowPress");
	}

	// 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
	public boolean onSingleTapUp(MotionEvent e) {
		LogManager.i("onSingleTapUp");
		return false;
	}

	/*
	 * 在onTouch()方法中，我们调用GestureDetector的onTouchEvent()方法，
	 * 将捕捉到的MotionEvent交给GestureDetector 来分析是否有合适的callback函数来处理用户的手势
	 */
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		LogManager.i("onTouchEvent");
		return gestureScanner.onTouchEvent(event);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				DirectionAdapter dirctionAdapter = new DirectionAdapter();
				for (int i = 0; i < dirctionAdapter.getCount(); i++) {
					View childView = dirctionAdapter.getView(i, null, null);
					mRouteDetail.addView(childView);
				}
				break;
			case 1:
				int y = point_Y / 7;
				int x = point_X - mSearchPointButton.getLayoutParams().width / 2;
				GeoPoint pt = mMapView.getProjection().fromPixels(x, y);
				int mSearch_point_view_width = mSearchPointButton.getLayoutParams().width;
				int mSearch_point_view_height = mSearchPointButton.getLayoutParams().height;
				MapView.LayoutParams layoutParams = new MapView.LayoutParams(mSearch_point_view_width, mSearch_point_view_height,pt, MapView.LayoutParams.CENTER);
				//mMapView.updateViewLayout(mSearch_point, layoutParams);
				if (mPopView.getVisibility()==View.INVISIBLE || mPopView.getVisibility()==View.GONE) {
					mSearch_point.setVisibility(View.VISIBLE);
				}
				
//				updateGetDirectionView();
				break;
			}
		}
	};

	class DirectionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return directors.size();
		}

		@Override
		public Object getItem(int position) {
			return directors.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = (View) inflater.inflate(R.layout.direction_item,
						null);
			}
			TextView text = (TextView) convertView
					.findViewById(R.id.direction_title_text);
			text.setText(Html.fromHtml(directors.get(position)));
			return convertView;
		}

	}
}
