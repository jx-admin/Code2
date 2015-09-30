package com.accenture.mbank.view;


import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.BaseMapActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.gmap.GeocodeResult;
import com.accenture.mbank.gmap.HttpConnectionUtil;
import com.accenture.mbank.logic.GetBranchListJson;
import com.accenture.mbank.model.BranchListModel;
import com.accenture.mbank.model.GetBranchListResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.view.BankSlidContainer.SlidControler;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapLayout extends LinearLayout implements SlidControler,
		OnTouchListener {
	Handler handler;

	public Handler getHandler() {
		return handler;
	}

	private MapView mapView;

	private MapController mapCon;
	
	EditText searchEdt;

	View map_view_enable;

	public ImageButton mapLayer, myLocation, showItems;
	public Button searchButton;
	
	public int mPopShowType;

	private MapActivity mapActivity;

	private ViewGroup enter_distance_layout;

	public static boolean debugChinaDevice = false;

	public MapLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mapActivity = (MapActivity) context;
	}

	GeoPoint geoBeijing = new GeoPoint((int) (39.932 * 1000000),
			(int) (116.461 * 1000000));

	LocationListener mLocationListener1;

	LocationListener mLocationListener2;

	LocationManager locationManager;

	public Location mCurrentLocation = null;

	public GeoPoint mCurrentLocationPoint = null;

	private OverlayItem mCurrentOverlayItem;

	private boolean mMapShowingForLocationUpdate = false;

	private boolean mFirstShow;

	private GeoPoint mDefaultCurrentLocation = new GeoPoint((int) (41.892910 * 1000000), (int) (12.48251990 * 1000000));

	private MapItemizedOverlay mCurrentLocationOverlay;

	private MapItemizedOverlay bankOverlay;

	List<Overlay> overlays;

	ViewGroup mPopView;
	
	View searchPoint;
	
	View getDirection;

	MapRouteOverlay mapRouteOverlay;

	EditText distance_input;

	GeoPoint searchGeoPoint;

	private int defaultZoom = 16;

	LinearLayout mRouteDetail;

	public void init() {

		LogManager.d("init");
		mapView = (MapView) findViewById(R.id.map_view);
		map_view_enable = findViewById(R.id.map_view_enable);
		mapView.getLayoutParams().height = BaseActivity.screen_height * 1 / 2;
		mapView.getLayoutParams().width = BaseActivity.screen_width;
		map_view_enable.getLayoutParams().height = BaseActivity.screen_height * 1 / 2;
		mapView.setOnTouchListener(this);
		mapView.setSaveEnabled(true);
		mapView.setBuiltInZoomControls(false);

		mapCon = mapView.getController();
		mapCon.setZoom(defaultZoom);
		
		mapCon.setCenter(mDefaultCurrentLocation);
		//mapView.setTraffic(false);
		//mapView.setSatellite(false);

		RelativeLayout mapContainer = (RelativeLayout)findViewById(R.id.map_container);
		mPopView = (ViewGroup) mapActivity.getLayoutInflater().inflate(R.layout.map_popover, null);
		searchPoint = (ViewGroup) mapActivity.getLayoutInflater().inflate(R.layout.search_point, null);
		getDirection = (ViewGroup) mapActivity.getLayoutInflater().inflate(R.layout.get_directions_view, null);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
		layoutParams.topMargin = 40;
		mapView.addView(mPopView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,MapView.LayoutParams.TOP_LEFT));
		mapContainer.addView(searchPoint,layoutParams);
		mapView.addView(getDirection,new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,MapView.LayoutParams.TOP_LEFT));
		mPopView.setVisibility(View.GONE);
		searchPoint.setVisibility(View.GONE);
		getDirection.setVisibility(View.GONE);
		
		mRouteDetail = (LinearLayout) findViewById(R.id.direction_list_view);
		overlays = mapView.getOverlays();

		Drawable currentLocationMarker = this.getContext().getResources().getDrawable(R.drawable.user_location);
		mCurrentLocationOverlay = new MapItemizedOverlay(currentLocationMarker,this);
		mCurrentLocationOverlay.showPop = false;
		Drawable search = this.getContext().getResources().getDrawable(R.drawable.pin);
		

		bankOverlay = new MapItemizedOverlay(search, this);

		overlays.add(mCurrentLocationOverlay);
		overlays.add(bankOverlay);

		locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

		mLocationListener1 = new LocationListener() {
			public void onLocationChanged(Location location) {
				LogManager.e("GPSListener  " + "latitude:"
								+ Double.toString(location.getLatitude())
								+ " longtitude:"
								+ Double.toString(location.getLongitude())
								+ " from:" + location.getProvider());
				if (isBetterLocation(location, mCurrentLocation)) {
					setCurrentLocation(location);
					if (mMapShowingForLocationUpdate) {
						addCurrentLocationOverlay();
					}
				}

			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		mLocationListener2 = new LocationListener() {
			private boolean mFirstLaunch;

			public void onLocationChanged(Location location) {
				LogManager.e("NetworkListener  " + "latitude:"
								+ Double.toString(location.getLatitude())
								+ " longtitude:"
								+ Double.toString(location.getLongitude())
								+ " from:" + location.getProvider());
				if (isBetterLocation(location, mCurrentLocation)) {
					setCurrentLocation(location);
					if (mMapShowingForLocationUpdate) {
						addCurrentLocationOverlay();
					}
				}
				if (mFirstLaunch == true) {
					mFirstLaunch = false;
					locationManager.removeUpdates(mLocationListener2);
				}
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
		myLocation = (ImageButton) findViewById(R.id.my_location);
		mapLayer = (ImageButton) findViewById(R.id.map_layer);
		showItems = (ImageButton) findViewById(R.id.show_items);
		searchButton = (Button) findViewById(R.id.search_btn);
		searchEdt = (EditText) findViewById(R.id.search_input);
		distance_input = (EditText) findViewById(R.id.distance_input);
		if (BaseActivity.initValue) {
			searchEdt.setText("05387");
			searchEdt.setText("via del tintoretto,200,roma");
		}
		
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getContext() instanceof MainActivity) {
					MainActivity baseactivity = (MainActivity) getContext();
					baseactivity.hideKeyboard(searchButton);
				} else{
					BaseMapActivity baseactivity = (BaseMapActivity) getContext();
					baseactivity.hideKeyboard(searchButton);
				}
				
				ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
				progressOverlay.show(getContext().getString(R.string.loading), new OnProgressEvent() {
					@Override
					public void onProgress() {
						final String keyText = searchEdt.getText().toString();
						searchGeoPoint = searchLocation(keyText);
						if (searchGeoPoint != null) {
							searchBarch(keyText,searchGeoPoint);
						}
					}
				});
			}
		});

		mapLayer.setOnClickListener(new OnClickListener() {
			int i;
			@Override
			public void onClick(View v) {
				i++;
				int value = i % 3;
				switch (value) {
				case 0://normal
					mapView.setTraffic(false);
					mapView.setSatellite(false);
					break;
				case 1: //satellite
					mapView.setTraffic(false);
					mapView.setSatellite(true);
					break;
				case 2: //hybrid
					mapView.setTraffic(true);
					mapView.setSatellite(true);
					//mapView.setHasTransientState(true);
//					mapView.setHovered(true);
//					mapView.setStreetView(true);
				}
			}
		});
		showItems.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (enter_distance_layout.getVisibility() == View.VISIBLE) {
					enter_distance_layout.setVisibility(View.GONE);
				} else {
					enter_distance_layout.setVisibility(View.VISIBLE);
				}
			}
		});
		myLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goToMyLocation();
			}
		});
		handler = new Handler();
		enter_distance_layout = (ViewGroup) findViewById(R.id.enter_distance_layout);

	}

	public View getSearchPoint() {
		return searchPoint;
	}

	public View getGetDirection() {
		return getDirection;
	}
	
	public void searchBarch(String keyText,GeoPoint searchGeoPoint) {
		if (searchGeoPoint != null) {
		} else {
			return;
		}
		double latitude = searchGeoPoint.getLatitudeE6() * 0.000001;
		double longitude = searchGeoPoint.getLongitudeE6() * 0.000001;
		
		int distance = 15000;
		String distanceInput = distance_input.getText().toString();
		if (distance_input.getVisibility() == View.VISIBLE && !distanceInput.equals("")) {
			try {
				distance = Integer.parseInt(distanceInput);
			} catch (Exception e) {
			}
		}
		String postData = GetBranchListJson.GetBranchListReportProtocal(Contants.publicModel, latitude, longitude, distance, keyText);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, getContext());
		GetBranchListResponseModel getBranchListResponse = GetBranchListJson.ParseGetBranchListResponse(httpResult);

		if (getBranchListResponse == null) {
			return;
		}
		branchListModels = getBranchListResponse.getBranchList();
		if (branchListModels.size() > 0) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					addbanks();
				}
			});
		}
	}

	private void goToMyLocation() {
		LogManager.d("mapView height" + mapView.getMeasuredHeight());
		View parent = (View) mapView.getParent();
		LogManager.d("mapView height" + parent.getMeasuredHeight());
		if (mCurrentLocation == null) {
			LogManager.d("找不到当前位置");
			// 因为获取不到位置显示空白，产生误解，所以显示出来，参考邮件2013,10，22
			// map_view_enable.setVisibility(View.VISIBLE);
			// mapView.setVisibility(View.GONE);
			map_view_enable.setVisibility(View.GONE);
			mapView.setVisibility(View.VISIBLE);
			searchButton.setEnabled(true);
			// setMapCenter(mDefaultCurrentLocation);
		} else {
			searchButton.setEnabled(true);
			map_view_enable.setVisibility(View.GONE);
			mapView.setVisibility(View.VISIBLE);
			int getLatitude = (int) (mCurrentLocation.getLatitude() * 1e6);
			int getLongitude = (int) (mCurrentLocation.getLongitude() * 1e6);
			
			if (debugChinaDevice) {
				int latitude = (int) (44.648837 * 1e6);
				int longitude = (int) (10.920087000000001 * 1e6);
				mCurrentLocationPoint = new GeoPoint(latitude, longitude);
			} else {
				mCurrentLocationPoint = new GeoPoint(getLatitude, getLongitude);
			}
			setMapCenter(mCurrentLocationPoint);
		}
	}

	List<BranchListModel> branchListModels;

	private static final long THIRTY_MINUTES = 1000 * 60 * 30;

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	public static final int GPS_SETTING = 2;

	/**
	 * Determines whether one Location reading is better than the current
	 * Location fix
	 * 
	 * @param location
	 *            The new Location that you want to evaluate
	 * @param currentBestLocation
	 *            The current Location fix, to which you want to compare the new
	 *            one
	 */
	private boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}
		if (location == null) {
			return false;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	Handler httpSearchHandler;

	private void setCurrentLocation(Location location) {
		mCurrentLocation = location;
	}

	@Override
	protected void onAttachedToWindow() {
		LogManager.d("onattach");
		super.onAttachedToWindow();
		Location cache = getCachedLocation();
		if (isBetterLocation(cache, mCurrentLocation)) {
			setCurrentLocation(cache);
		}
		
		// Check status of GPS and NETWORK
		boolean gpsProviderFlag = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean networkProviderFlag = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if ((!gpsProviderFlag || !networkProviderFlag) && mFirstShow) {
			LogManager.d( "No location provider is enabled");
			mFirstShow = false;
			buildAlertMessageNoLocationService();
		} else {
			if (gpsProviderFlag) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0,mLocationListener1);
			}
			if (networkProviderFlag) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0,mLocationListener2);
			}

			addCurrentLocationOverlay();
			// (int)(37 * 1000000), (int)(111.37 * 1000000)
			// for direction test
			// addBank(geoBeijing);
			mMapShowingForLocationUpdate = true;
			mapView.invalidate();
			Handler handler = new Handler();

			Runnable myRunnable = new Runnable() {
				public void run() {
					locationManager.removeUpdates(mLocationListener1);
					locationManager.removeUpdates(mLocationListener2);
				}
			};

			handler.postDelayed(myRunnable, 300000);

			httpSearchHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (HttpConnectionUtil.SUCCESS == msg.what) {
						String response = (String) msg.getData().get("response");
						LogManager.d("response : " + response);
						GeocodeResult geocodeResult = new GeocodeResult();
						geocodeResult.setValues(response);
						mapCon.setZoom(defaultZoom);
						mapCon.setCenter(geocodeResult.getGeoPoint());

					} else if(HttpConnectionUtil.SUCCESS_GEOCODER == msg.what){
						int[] response = (int[]) msg.getData().get("response");
						
						GeoPoint geopoint = new GeoPoint(response[0], response[1]);
						mapCon.setZoom(defaultZoom);
						mapCon.setCenter(geopoint);
					} else {
						Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
					}
				}
			};
		}
		goToMyLocation();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

	}

	// Set Map Center position
	public void setMapCenter(GeoPoint point) {
		MapController ctl = mapView.getController();
		if (point == null) {
			Location loc = mCurrentLocation;
			if (loc == null) {
				ctl.setCenter(mDefaultCurrentLocation);
			} else {
				ctl.setCenter(new GeoPoint((int) (loc.getLatitude() * 1e6),(int) (loc.getLongitude() * 1e6)));
			}
		} else {
			ctl.setCenter(point);
		}
	}

	public void addCurrentLocationOverlay() {
		if (overlays.contains(mCurrentLocationOverlay)) {
			overlays.remove(mCurrentLocationOverlay);
		}
		if (mCurrentLocation == null) {
			mCurrentLocationPoint = mDefaultCurrentLocation;
			System.out.println("null");
		} else {
			mCurrentLocationPoint = new GeoPoint((int) (mCurrentLocation.getLatitude() * 1e6),(int) (mCurrentLocation.getLongitude() * 1e6));
			System.out.println("add current");
		}
		mCurrentOverlayItem = new OverlayItem(mCurrentLocationPoint,"CURRENTLOCATION", "CurrentLocation");
		mCurrentLocationOverlay.clearOverlay();
		mCurrentLocationOverlay.addOverlay(mCurrentOverlayItem);
		overlays.add(mCurrentLocationOverlay);
		mapView.postInvalidate();
	}

	private void addbanks() {
		bankOverlay.clearOverlay();
		if (branchListModels != null) {
			for (BranchListModel brankBranchListModel : branchListModels) {
				addBank(brankBranchListModel);
			}
		}
		mapView.invalidate();

	}

	private void addBank(BranchListModel brankBranchListModel) {
		int la = (int) (brankBranchListModel.getLatitude() * 1e6);
		int lo = (int) (brankBranchListModel.getLongitude() * 1e6);
		GeoPoint geoPoint = new GeoPoint(la, lo);

		BankOverlayItem locaItem = new BankOverlayItem(geoPoint,"CURRENTLOCATION", "CurrentLocation");
		locaItem.setBrankBranchListModel(brankBranchListModel);
		bankOverlay.addOverlay(locaItem);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// int size = MeasureSpec.getSize(heightMeasureSpec);
		// int mode = MeasureSpec.getMode(heightMeasureSpec);
		// LogManager.d("size" + size + "mode" + mode);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	private void buildAlertMessageNoLocationService() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getContext());
		builder.setMessage("To get current position, Google's location service and GPS satellites are needed.\nDo you want to enable them all?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									final DialogInterface dialog,
									final int id) {
								Activity activity = (Activity) getContext();
								activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),GPS_SETTING);
								dialog.dismiss();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	public Location getCachedLocation() {
		Location firstLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (firstLocation != null) {
			if ((firstLocation.getTime() - System.currentTimeMillis()) < THIRTY_MINUTES) {
				return firstLocation;
			}
		}
		firstLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (firstLocation != null) {
			if ((firstLocation.getTime() - System.currentTimeMillis()) < THIRTY_MINUTES) {
				return firstLocation;
			}
		}
		return null;
	}

	public MapView getMapView() {
		return mapView;
	}

	public ViewGroup getPopView() {

		return mPopView;
	}

	public LinearLayout getRouteDetail(){
		return mRouteDetail;
	}
	
	public void setStartPoint(GeoPoint point) {

	}

	public void setEndPoint(GeoPoint point) {

	}

	public GeoPoint getStartPoint() {
		return null;
	}

	public GeoPoint getEndPoint() {
		return null;
	}

	BankSlidContainer bankSlidContainer;

	public InnerScrollView parentScrollView;

	@Override
	public void setSlidControler(BankSlidContainer bankSlidContainer) {
		this.bankSlidContainer = bankSlidContainer;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (parentScrollView != null) {
			parentScrollView.requestDisallowInterceptTouchEvent(true);
		}
		return v.onTouchEvent(event);
	}

	private GeoPoint searchLocation(String locationName) {
		if (locationName == null || locationName.equals("")) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					goToMyLocation();
				}
			});
			return mCurrentLocationPoint;
		}
//		String url = "http://maps.google.com/maps/api/geocode/json?";
//		Map<String, String> paramMap = new HashMap<String, String>();
//		locationName = java.net.URLEncoder.encode(locationName);
//		paramMap.put("address", locationName);
//		paramMap.put("sensor", "true");
//
//		HttpConnectionUtil httpConnUtil = new HttpConnectionUtil(getContext(),httpSearchHandler, url, paramMap);
//		return httpConnUtil.doStart();
		
		Geocoder geocoder = new Geocoder(getContext());
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocationName(locationName, 1);
			int newLat = (int)(addresses.get(0).getLatitude() * 1000000);
	        int newLng = (int)(addresses.get(0).getLongitude() * 1000000);
	        GeoPoint geoPoint = new GeoPoint(newLat, newLng);
            Message message = httpSearchHandler.obtainMessage();
            Bundle b = new Bundle();
            message.what = HttpConnectionUtil.SUCCESS_GEOCODER;
            b.putIntArray("response", new int[]{newLat,newLng});
            message.setData(b);
            httpSearchHandler.sendMessage(message);
	        return geoPoint;
		} catch (IOException e) {
			Message message = httpSearchHandler.obtainMessage();
            Bundle b = new Bundle();
            message.what = HttpConnectionUtil.ERROR;
            message.setData(b);
            httpSearchHandler.sendMessage(message);
			return null;
		}
		
		
		
	}
}
