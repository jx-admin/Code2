package wu.a.wuliu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;

public class BaiduMapActivity extends Activity {
	static final String TAG = BaiduMapActivity.class.getSimpleName();

	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void startBaiduMap(){
		if(mLocClient==null){
			mLocClient = new LocationClient(this);
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// ��gps
			option.setCoorType("bd09ll"); // �����������
			option.setScanSpan(1000);
			option.setPoiExtraInfo(true);
			option.setAddrType("all");
			mLocClient.setLocOption(option);
		}
		if(!mLocClient.isStarted()){
			mLocClient.start();
		}
	}

	@Override
	protected void onDestroy() {
		if(mLocClient!=null&&mLocClient.isStarted()){
			mLocClient.stop();
		}
		super.onDestroy();
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			BaiduMapActivity.this.onReceiveLocation(location);
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			BaiduMapActivity.this.onReceivePoi(poiLocation);
		}
	}


	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;
		String new_cityName = location.getCity();
		Log.d(TAG, "baidu city=" + new_cityName);
	}

	public void onReceivePoi(BDLocation poiLocation) {
	}
}
