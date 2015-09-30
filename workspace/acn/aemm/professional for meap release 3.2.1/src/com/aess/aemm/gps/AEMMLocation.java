package com.aess.aemm.gps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.aess.aemm.authenticator.Constants;
import com.aess.aemm.commonutils.CommUtils;
import com.aess.aemm.db.GPSContent;
import com.aess.aemm.networkutils.AutoAdress;
import com.aess.aemm.networkutils.HttpHelp;
import com.aess.aemm.protocol.DomXmlBuilder;
import com.aess.aemm.receiver.AlarmReceiver;

public class AEMMLocation {
	public static final String TAG = "AEMMLocation";
	public static final String DEBUGFILENAME = "GPS.xml";
	public static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final int REPTIME = 1000 * 60 * 5;

	public static final int LOCN = 0; // only post info
	public static final int LOCR = 2; // set Provider
	public static final int LOCF = 4; // set Provider and post info

	public static void intentInitLocation(Context cxt) {
		Intent intent = new Intent(AlarmReceiver.GPS);
		intent.putExtra(CommUtils.TYPE, AEMMLocation.LOCF);
		cxt.sendBroadcast(intent);
	}

	public static void intentResetLocation(Context cxt) {
		String id = CommUtils.getSessionId(cxt);
		if (null == id || id.length() < 1) {
			return;
		}
		AccountManager am = AccountManager.get(cxt);
		Account[] accounts = am.getAccountsByType(Constants.ACCOUNT_TYPE);

		if (null == accounts || accounts.length < 1) {
			return;
		}
		Intent intent = new Intent(AlarmReceiver.GPS);
		intent.putExtra(CommUtils.TYPE, AEMMLocation.LOCR);
		cxt.sendBroadcast(intent);
	}

	public static void postLocation(final Context cxt, final int type) {
		Thread location = new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				int mtype = type;
				AEMMLocation location = AEMMLocation.getInstance(cxt);
				if (AEMMLocation.LOCF == mtype || AEMMLocation.LOCR == mtype
						|| location.getLocListenerCount() < 1) {
					location.setGPSProvider();
				}
				if (AEMMLocation.LOCR != mtype) {
					location.postLoctionInfo();
				}
			}
		});
		location.setName("Location");
		location.start();
	}

	public static AEMMLocation getInstance(Context cxt) {
		if (null == cxt) {
			return null;
		}
		if (null == aeeloc) {
			aeeloc = new AEMMLocation(cxt);
		}
		return aeeloc;
	}

	@SuppressWarnings("deprecation")
	public void onDestroy() {
		if (providerList != null) {
			Settings.System.putString(context.getContentResolver(),
					Settings.System.LOCATION_PROVIDERS_ALLOWED, "");
		}
	}

	public static void cancelGpsTimer(Context cxt) {
		Log.i(TAG, "cancelGpsTimer");

		AlarmManager alarmManager = (AlarmManager) cxt
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(cxt, AlarmReceiver.class);
		intent.setAction(AlarmReceiver.GPS);
		intent.putExtra(CommUtils.TYPE, AEMMLocation.LOCN);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, 0,
				intent, 0);
		alarmManager.cancel(pendingIntent);
	}

	public static void setGpsTimer(Context cxt, long time) {
		Log.i(TAG, String.format("setGpsTimer : %s", String.valueOf(time)));
		if (time < 1) {
			return;
		}
		AlarmManager alarmManager = (AlarmManager) cxt
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(cxt, AlarmReceiver.class);
		intent.setAction(AlarmReceiver.GPS);
		intent.putExtra(CommUtils.TYPE, AEMMLocation.LOCN);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(cxt, 0,
				intent, 0);

		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ time, pendingIntent);
	}

	public int getLocListenerCount() {
		int ret = 0;
		if (null != loclistener) {
			ret = loclistener.size();
		}
		return ret;
	}

	private void init() {
		Log.i(TAG, "init");

		loclistener = new ArrayList<LocationListener>();
	}

	private int ifGetDataFromSp(Context cxt) {
		String tlongitude = CommUtils.getLongitude(context);
		if (null != tlongitude && tlongitude.length() > 1) {
			return -1;
		}
		return 1;
	}

	private int postLoctionInfo() {

		if (null == context) {
			return -1;
		}
		Log.d(TAG, "Report Location Info");

		cancelGpsTimer(context);

		if (ifGetDataFromSp(context) > 0) {
			if (getFromProvider() < 1) {
				getLocationFromSP();
			}
		}

		if (locationSaveInDb() > 0) {
			locationFileClear();
		}
		AutoAdress ad = AutoAdress.getInstance(context);
		String url = ad.getUpdateURL();
		if (null != url) {
			InputStream is = null;
			String locationInfo = DomXmlBuilder.buildInfo(context, false,
					DomXmlBuilder.LOCATION, null);
			
			if (null != locationInfo) {
				is = HttpHelp.aemmHttpPost(context, url, locationInfo,
						DEBUGFILENAME);
				if (null != is) {
					GPSContent.deleteAllGPSContent(context);
				}
			}
		} else {
			Log.d(TAG, "Location report, not have Updata URL");
		}

		long timelong = CommUtils.getLocTime(context);
		setGpsTimer(context, timelong);
		return 1;
	}

	private int getFromProvider() {
		int ret = 0;
		if (null == providerList || providerList.size() < 1) {
			AEMMLocation.intentResetLocation(context);
			return -1;
		}

		for (int x = 0; x < providerList.size(); x++) {
			String provider = providerList.get(x);
			Log.d(TAG, "GPS provider is " + provider);
			if (null != provider) {
				if (!locMag.isProviderEnabled(provider)) {
					continue;
				}
			}

			Location loc = locMag.getLastKnownLocation(provider);
			if (null == loc) {
				continue;
			}

			locationSaveInFile(loc, CommUtils.getTimeString(TIMEFORMAT));
			ret = 1;
		}
		return ret;
	}

	private void getLocationFromSP() {
		Location location;

		CellIDInfo info = getCellIDInfo();
		if (null != info) {
			location = getLocationByNet(info);
			if (null != location) {
				locationSaveInFile(location,
						CommUtils.getTimeString(TIMEFORMAT));
			}
		} else {
			Log.d(TAG, "Cell ID Not Found");
		}
	}

	private final String provider =  "network,gps";
	private void setGPSProvider() {

		try {
			String info = Settings.Secure.getString(context.getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			if (!provider.equals(info)) {
				Settings.Secure.putString(context.getContentResolver(),
						Settings.Secure.LOCATION_PROVIDERS_ALLOWED, provider);
			}
		} catch (SecurityException se) {
			Log.w(TAG, "Requires android.permission.WRITE_SECURE_SETTINGS");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null == locMag) {
			locMag = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		}

		if (null != loclistener) {
			for (int x = 0; x < loclistener.size(); x++) {
				locMag.removeUpdates(loclistener.get(x));
			}
		}
		loclistener.clear();

		providerList = locMag.getAllProviders();
		if (null == providerList) {
			return;
		}

		float range = Float.valueOf(CommUtils.getLocRange(context));
		long time = CommUtils.getLocTime(context);

		for (int x = 0; x < providerList.size(); x++) {
			String provider = providerList.get(x);

			if (null != provider) {
				if (!locMag.isProviderEnabled(provider)) {
					continue;
				}
				LocationListener ll = new MyLocationListner(context);
				locMag.requestLocationUpdates(provider, time, range, ll);
				loclistener.add(ll);
			}

			Location loc = locMag.getLastKnownLocation(provider);
			if (null == loc) {
				continue;
			}

			locationSaveInFile(loc, CommUtils.getTimeString(TIMEFORMAT));
		}
	}

	public class CellIDInfo {
		public int cellId = 0;
		public int MCC = 0;
		public int MNC = 0;
		public int locationAreaCode = 0;
		public String radioType = null;
	}

	private CellIDInfo getCellIDInfo() {

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		int type = tm.getNetworkType();

		if (TelephonyManager.SIM_STATE_READY != tm.getSimState()) {
			return null;
		}
		
		String imei = tm.getNetworkOperator();
		if (null == imei || imei.length() < 5) {
			Log.w(TAG, "imei == null");
			return null;
		}
		
		CellIDInfo info = new CellIDInfo();
		
		info.MCC = Integer.valueOf(imei.substring(0, 3));
		info.MNC = Integer.valueOf(imei.substring(3, 5));

		if (type == TelephonyManager.NETWORK_TYPE_EVDO_A
				|| type == TelephonyManager.NETWORK_TYPE_CDMA
				|| type == TelephonyManager.NETWORK_TYPE_1xRTT) {
			CdmaCellLocation location = (CdmaCellLocation) tm.getCellLocation();
			info.cellId = location.getBaseStationId();
			info.locationAreaCode = location.getNetworkId();

			info.MNC = location.getSystemId();
			info.radioType = "cdma";
		} else if (type == TelephonyManager.NETWORK_TYPE_EDGE
				|| type == 102) {
			//lenovo s2 has error. it type is 102
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
			info.cellId = location.getCid();
			info.locationAreaCode = location.getLac();
			info.radioType = "gsm";
		} else if (type == TelephonyManager.NETWORK_TYPE_GPRS
				|| type == TelephonyManager.NETWORK_TYPE_UMTS) {
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
			if(location!=null){
			info.cellId = location.getCid();
			info.locationAreaCode = location.getLac();
			info.radioType = "gsm";
			}
		}
		return info;
	}

	private Location getLocationByNet(CellIDInfo info) {
		Location loc = null;

		try {
			String Send = getSendString(info);
			Log.d(TAG, "Location info : " + Send);
			StringBuffer sb = getHttpResponse(Send);
			if (sb.length() >= 3) {
				JSONObject data = new JSONObject(sb.toString());
				data = (JSONObject) data.get("location");

				loc = new Location("google");
				loc.setLatitude((Double) data.get("latitude"));
				loc.setLongitude((Double) data.get("longitude"));
				loc.setAccuracy(Float.parseFloat(data.get("accuracy")
						.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return loc;
	}

	private StringBuffer getHttpResponse(String Send)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.google.com/loc/json");

		StringEntity se = new StringEntity(Send);
		post.setEntity(se);
		HttpResponse resp = client.execute(post);
		StatusLine sl = resp.getStatusLine();
		Log.d(TAG, "Status: " + sl.getStatusCode());
		HttpEntity entity = resp.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				entity.getContent()));
		StringBuffer sb = new StringBuffer();
		String result = null;
		while ((result = br.readLine()) != null) {
			sb.append(result);
		}
		Log.d(TAG, "Read Date " + sb.toString());
		return sb;
	}

	private String getSendString(CellIDInfo info) throws JSONException {
		JSONObject holder = new JSONObject();
		holder.put("version", "1.1.0");
		holder.put("host", "maps.google.com");
		if (460 == info.MCC) {
			holder.put("address_language", "zh_CN");
		} else if (525 == info.MCC) {
			holder.put("address_language", "zh-SG");
		} else {
			holder.put("address_language", "en_US");
		}
		holder.put("request_address", true);
		holder.put("radio_type", info.radioType);

		JSONObject cell_towers = new JSONObject();
		cell_towers.put("cell_id", info.cellId);
		cell_towers.put("location_area_code", info.locationAreaCode);
		cell_towers.put("mobile_country_code", info.MCC);
		cell_towers.put("mobile_network_code", info.MNC);

		JSONArray array = new JSONArray();
		array.put(cell_towers);

		holder.put("cell_towers", array);
		return holder.toString();
	}

	private AEMMLocation(Context cxt) {
		if (null != cxt) {
			context = cxt;
			init();
		}
	}

	private int locationSaveInDb() {
		GPSContent gpsdb = new GPSContent();
		String name = CommUtils.getLocaName(context);
		gpsdb.mGPSLongitude = CommUtils.getLongitude(context);
		gpsdb.mGPSLatitude = CommUtils.getLatitude(context);
		gpsdb.mGPSTime = CommUtils.getLocaTime(context);
		if (null == gpsdb.mGPSLongitude || null == gpsdb.mGPSLatitude) {
			return 0;
		}
		if (gpsdb.mGPSLongitude.length() < 1 || gpsdb.mGPSLatitude.length() < 1) {
			return 0;
		}

		String info = String.format("name: %s, Time: %s: Log: %s, Lat: %s",
				name, gpsdb.mGPSTime, gpsdb.mGPSLongitude, gpsdb.mGPSLatitude);

		Log.d(TAG, info);

		Uri uri = gpsdb.add(this.context);
		if (null == uri) {
			Log.w(TAG, "Location Save Fail");
			return -1;
		}
		return 1;
	}

	private int locationSaveInFile(Location location, String time) {
		String name = CommUtils.getLocaName(context);
		if ("gps".equals(name)) {
			return 1;
		}
		CommUtils.setLocaName(context, location.getProvider());
		String loStr = String.format("%f", location.getLongitude());
		String laStr = String.format("%f", location.getLatitude());
		CommUtils.setLongitude(context, loStr);
		CommUtils.setLatitude(context, laStr);
		CommUtils.setLocaTime(context, time);
		return 0;
	}

	private int locationFileClear() {
		CommUtils.setLocaName(context, "");
		CommUtils.setLongitude(context, "");
		CommUtils.setLatitude(context, "");
		CommUtils.setLocaTime(context, "");
		return 1;
	}

	private class MyLocationListner implements LocationListener {

		public MyLocationListner(Context cxt) {
			this.context = cxt;
		}

		public void onLocationChanged(android.location.Location location) {
			if (location != null) {
				locationSaveInFile(location,
						CommUtils.getTimeString(TIMEFORMAT));
			}
		}

		public void onProviderDisabled(String provider) {
			intentResetLocation(context);
		}

		public void onProviderEnabled(String provider) {
			intentResetLocation(context);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			intentResetLocation(context);
		}

		private Context context = null;
	};

	private List<String> providerList = null;
	private Context context = null;
	private LocationManager locMag = null;
	private static AEMMLocation aeeloc = null;
	private List<LocationListener> loclistener = null;
	// private void setGPSProvider() {
	// Criteria criteria = new Criteria();
	// criteria.setAccuracy(Criteria.ACCURACY_FINE);
	// criteria.setAltitudeRequired(false);
	// criteria.setBearingRequired(false);
	// criteria.setCostAllowed(true);
	// criteria.setPowerRequirement(Criteria.POWER_LOW);
	// provider = locMag.getBestProvider(criteria, true);
	// if (provider == null) {
	// AlertDialog.Builder builder = new Builder(context);
	// builder.setMessage("GPS unable");
	// builder.setPositiveButton(android.R.string.ok,
	// new OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	// builder.create().show();
	// } else {
	// Log.i(TAG, "Location Provider : " + provider);
	// }
	// }

	// public static void sendLocationAction(Context cxt) {
	// Intent intent = new Intent(AlarmReceiver.GPS);
	// intent.putExtra(CommUtils.TYPE, AEMMLocation.LOCN);
	// cxt.sendBroadcast(intent);
	// }
}
