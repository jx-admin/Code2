package com.accenture.mbank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.accenture.mbank.gmap.HttpConnectionUtil;
import com.accenture.mbank.logic.GetBranchListJson;
import com.accenture.mbank.model.BranchListModel;
import com.accenture.mbank.model.GetBranchListResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.view.BankRollContainer;
import com.accenture.mbank.view.BankRollContainerManager;
import com.accenture.mbank.view.BankRollView;
import com.accenture.mbank.view.ContactCustomServiceLayout;
import com.accenture.mbank.view.ContactNewRequestLayout;
import com.accenture.mbank.view.ContactTheifLossLayout;
import com.accenture.mbank.view.ItemExpander;
import com.accenture.mbank.view.MapWrapperLayout;
import com.accenture.mbank.view.MarkerMapOnCLickListener;
import com.accenture.mbank.view.MySupportMapFragment;
import com.accenture.mbank.view.PopView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactSearchActivity extends BaseMapActivity implements
		OnClickListener {

	private LinearLayout menu;

	private ImageButton help;

	private SlidingDrawer slidMenu;

	private View userInfoBtn;

	private View menu_accountsBtn;

	private View menu_investmentsBtn;

	private View menu_cardsBtn;

	private View menu_loansBtn;

	private View menu_paymentBtn;

	private View menu_contactsBtn;

	private View menu_guideBtn;

	private View menu_log_outBtn;

	BankRollContainer bankRollContainer;

	private GoogleAnalytics mGaInstance;

	private Tracker mGaTracker1;
	protected GoogleMap map;
	private MapWrapperLayout mapWrapperLayout;
	private PopView mPopView;
	private EditText searchEdt;
	private ImageButton mapLayer, myLocation, showItems;
	private Button searchButton;
	private EditText distance_input;
	private Handler handler;
	private Handler httpSearchHandler;
	private LatLng pt = null;
	private Bitmap icon;
	private Bitmap iconBank;
	private ViewGroup enter_distance_layout;
	private LatLng myLocationLatLng;
	private ArrayList<BranchListModel> branchListModels;
	private HashMap<Marker,BranchListModel> markBrankBranchList;
	private MarkerMapOnCLickListener markerClickListener;
	private Marker marker;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this); // Add this method.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.contact_search);

		init();
		if (BaseActivity.isLogin) {

			showHelp();
			menu.setVisibility(View.VISIBLE);
		} else {
			hideHelp();
			hideMenu();
		}

	}

	private void hideMenu() {
		// TODO Auto-generated method stub
		menu.setVisibility(View.INVISIBLE);
	}

	public void showHelp() {

		if (help != null)
			help.setVisibility(View.VISIBLE);
	}

	public void hideHelp() {

		if (help != null)
			help.setVisibility(View.INVISIBLE);
	}

	BankRollContainerManager bankRollContainerManager;
	TextView webSitTextView;

	/**
     * 
     */
	void init() {
		menu = (LinearLayout) findViewById(R.id.menu);
		help = (ImageButton) findViewById(R.id.help_btn);
		help.setOnClickListener(this);
		menu.setVisibility(View.VISIBLE);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		bankRollContainer = (BankRollContainer) findViewById(R.id.roll_group);
		bankRollContainer.init();
		BankRollView contactusRollView = (BankRollView) layoutInflater.inflate(
				R.layout.bank_roll_view, null);
		contactusRollView.init();

		contactusRollView.setCloseImage(R.drawable.sphere_contact_us);
		contactusRollView.setShowImage(R.drawable.show_selector);
		final ViewGroup layout = (ViewGroup) layoutInflater.inflate(
				R.layout.contact_us_layout, null);
		contactusRollView.setContent(layout);
		bankRollContainer.addShowAble(contactusRollView);
		contactusRollView.setPadding(0, contactusRollView.height_bottom, 0, 0);

		ItemExpander itemExpander = (ItemExpander) layout
				.findViewById(R.id.customer_service);
		ContactCustomServiceLayout contactCustomServiceLayout = (ContactCustomServiceLayout) layoutInflater
				.inflate(R.layout.contact_customer_service, null);
		webSitTextView = (TextView) contactCustomServiceLayout
				.findViewById(R.id.website);
		webSitTextView.setText(Html.fromHtml("<u>www.mybank.it</u>"));
		webSitTextView.setTextColor(Color.BLUE);
		webSitTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri
						.parse("https://ams-msite.mobility-managed.com/cp/gtw/login/start");
				intent.setData(content_url);
				startActivity(intent);
			}
		});

		itemExpander.setExpandedContainer(contactCustomServiceLayout);

		itemExpander.setTitle(getResources().getString(
				R.string.customer_service));
		// itemExpander.setTitle("CUSTOMER SERVICE");
		itemExpander.setTypeface(Typeface.DEFAULT);
		itemExpander.onChange(" ");
		itemExpander.setExpandable(true);
		itemExpander.setExpand(false);

		itemExpander = (ItemExpander) layout.findViewById(R.id.thief_and_loss);
		ContactTheifLossLayout contactTheifLossLayout = (ContactTheifLossLayout) layoutInflater
				.inflate(R.layout.contact_theif_loss, null);
		itemExpander.setExpandedContainer(contactTheifLossLayout);
		itemExpander.setExpandable(true);
		itemExpander.setExpand(false);
		itemExpander
				.setTitle(getResources().getString(R.string.theft_and_loss));
		// itemExpander.setTitle("THEFT");
		itemExpander.setTypeface(Typeface.DEFAULT);
		itemExpander.onChange(" ");
		if (BaseActivity.isLogin) {
			/*
			 * menu is only visible after user has logged in
			 */
			menu.setVisibility(View.VISIBLE);
			itemExpander = (ItemExpander) layout.findViewById(R.id.new_request);

			ContactNewRequestLayout contactNewRequestLayout = (ContactNewRequestLayout) layoutInflater
					.inflate(R.layout.contact_new_request, null);
			itemExpander.setExpandedContainer(contactNewRequestLayout);
			itemExpander.setExpandable(true);
			itemExpander.setExpand(false);
			itemExpander.setResultVisible(false);
			// itemExpander.setTitle("NEW REQUEST");
			itemExpander.setTitle(getResources()
					.getString(R.string.new_request));
			itemExpander.setTypeface(Typeface.DEFAULT);
			itemExpander.onChange(" ");
			itemExpander.setVisibility(View.VISIBLE);

		}

//		final MapLayout maplayout = (MapLayout) layoutInflater.inflate(R.layout.search_width_map, null);
		LinearLayout maplayout = (LinearLayout) layoutInflater.inflate(R.layout.search_width_map_v2, null);
		map = ((MySupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		myLocationLatLng = new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
//		maplayout.init();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 13));
		
		icon = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
		iconBank = BitmapFactory.decodeResource(getResources(), R.drawable.green_dot);
		
		marker = map.addMarker(new MarkerOptions()
	     .icon(BitmapDescriptorFactory.fromBitmap(icon))
	     .position(myLocationLatLng)
	     .flat(false));
		
		markerClickListener = new MarkerMapOnCLickListener(ContactSearchActivity.this);
		markerClickListener.setMarker(marker);
		map.setOnMarkerClickListener(null);
		
//		maplayout.parentScrollView = bankRollContainer;
		searchRollView = (BankRollView) layoutInflater.inflate(R.layout.bank_roll_view, null);
		searchRollView.init();

		searchRollView.setCloseImage(R.drawable.sphere_search_branch);
		searchRollView.setShowImage(R.drawable.show_selector);
//		searchRollView.setBallIcon(R.drawable.icon_black_12);
//		searchRollView.setBallName(R.string.search_branch_fs);
		
		bankRollContainer.addShowAble(searchRollView);
		
		searchRollView.setContent(maplayout);
		
		ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

		transparentImageView.setOnTouchListener(new View.OnTouchListener() {

		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        int action = event.getAction();
		        switch (action) {
		           case MotionEvent.ACTION_DOWN:
		                // Disallow ScrollView to intercept touch events.
		        	   bankRollContainer.requestDisallowInterceptTouchEvent(true);
		                // Disable touch on transparent view
		                return false;

		           case MotionEvent.ACTION_UP:
		                // Allow ScrollView to intercept touch events.
		        	   bankRollContainer.requestDisallowInterceptTouchEvent(false);
		                return true;

		           case MotionEvent.ACTION_MOVE:
		        	   bankRollContainer.requestDisallowInterceptTouchEvent(true);
		                return false;

		           default:
		                return true;
		        }   
		    }
		});
		
		mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.relativeLayoutMap);
		
		mPopView = (PopView) this.getLayoutInflater().inflate(R.layout.map_popover, null);
		searchPoint = (LinearLayout) this.getLayoutInflater().inflate(R.layout.search_point, null);
		RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		searchPoint.setLayoutParams(layoutParam);
		
		handler = new Handler();
		
		httpSearchHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(HttpConnectionUtil.SUCCESS_GEOCODER == msg.what){
					double[] response = (double[]) msg.getData().get("response");
					
					LatLng geopoint = new LatLng(response[0], response[1]);
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(geopoint, 13));
				} else {
					Toast.makeText(ContactSearchActivity.this, "error", Toast.LENGTH_LONG).show();
				}
			}
		};
		
		Button mSearchPointButton = (Button) searchPoint.findViewById(R.id.search_point_button);
		mSearchPointButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pt = map.getCameraPosition().target;
				ProgressOverlay progressOverlay = new ProgressOverlay(ContactSearchActivity.this);
				progressOverlay.show(ContactSearchActivity.this.getString(R.string.loading), new OnProgressEvent() {
					@Override
					public void onProgress() {
						searchBarch("", pt);
					}
				});
			}
		});
		
		mapWrapperLayout.addView(searchPoint);
		searchPoint.setVisibility(View.GONE);
		
		myLocation = (ImageButton) maplayout.findViewById(R.id.my_location);
		myLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goToMyLocation();
			}
		});
		
		mapLayer = (ImageButton) maplayout.findViewById(R.id.map_layer);
		showItems = (ImageButton) maplayout.findViewById(R.id.show_items);
		searchButton = (Button) maplayout.findViewById(R.id.search_btn);
		searchEdt = (EditText) maplayout.findViewById(R.id.search_input);
		distance_input = (EditText) findViewById(R.id.distance_input);
		
		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftKeyboard(ContactSearchActivity.this, searchEdt);
				ProgressOverlay progressOverlay = new ProgressOverlay(ContactSearchActivity.this);
				progressOverlay.show(ContactSearchActivity.this.getString(R.string.loading), new OnProgressEvent() {
					@Override
					public void onProgress() {
						final String keyText = searchEdt.getText().toString();
						LatLng searchGeoPoint = searchLocation(keyText);
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
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					break;
				case 1: //satellite
					map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					break;
				case 2: //hybrid
					map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				}
			}
		});
		
		enter_distance_layout = (ViewGroup) maplayout.findViewById(R.id.enter_distance_layout);
		
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

		initMenu();

	}

	BankRollView searchRollView;

	private void initMenu() {

		userInfoBtn = findViewById(R.id.menu_user_info);
		userInfoBtn.setOnClickListener(this);
		menu_accountsBtn = findViewById(R.id.menu_accounts);
		menu_accountsBtn.setOnClickListener(this);
		menu_investmentsBtn = findViewById(R.id.menu_investments);
		menu_investmentsBtn.setOnClickListener(this);
		menu_cardsBtn = findViewById(R.id.menu_cards);
		menu_cardsBtn.setOnClickListener(this);
		menu_loansBtn = findViewById(R.id.menu_loans);
		menu_loansBtn.setOnClickListener(this);
		menu_paymentBtn = findViewById(R.id.menu_payment);
		menu_paymentBtn.setOnClickListener(this);
		menu_contactsBtn = findViewById(R.id.menu_contacts);
		menu_contactsBtn.setOnClickListener(this);
		menu_guideBtn = findViewById(R.id.menu_guide);
		menu_guideBtn.setOnClickListener(this);
		menu_log_outBtn = findViewById(R.id.menu_log_out);
		menu_log_outBtn.setOnClickListener(this);

		slidMenu = (SlidingDrawer) findViewById(R.id.slid_menu);
	}

	@Override
	public void onClick(View v) {
		MainActivity mainAcitivty = (MainActivity) MainActivity.getContext();
		if (v == help) {
			Intent intent = new Intent(ContactSearchActivity.this,
					HelpListActivity.class);
			startActivity(intent);
			return;
		} else if (v == userInfoBtn) {
			mainAcitivty.showUserInfo();
			slidMenu.animateClose();
		} else if (v == menu_accountsBtn) {
			mainAcitivty.showTab(MainActivity.ACCOUNTS);
			slidMenu.animateClose();
		} else if (v == menu_cardsBtn) {
			mainAcitivty.showTab(MainActivity.CARDS);
			slidMenu.animateClose();
		} else if (v == menu_contactsBtn) {

			Intent intent = new Intent(ContactSearchActivity.this,
					ContactSearchActivity.class);
			startActivity(intent);
			slidMenu.animateClose();
		} else if (v == menu_guideBtn) {
			help.performClick();
		} else if (v == menu_investmentsBtn) {
			mainAcitivty.showInvestments();
			slidMenu.animateClose();
		} else if (v == menu_loansBtn) {
			mainAcitivty.showLoans();
			slidMenu.animateClose();
		} else if (v == menu_log_outBtn) {
			DialogManager.createMessageExitDialog(
					"Are you sure you want to exit the application?", this)
					.show();
		} else if (v == menu_paymentBtn) {
			mainAcitivty.showTab(MainActivity.PAYMENTS);
			slidMenu.animateClose();
		}
		finish();
	}

	private LinearLayout searchPoint;

	public View getSearchPoint() {
		return searchPoint;
	}

	public void setSearchPoint(LinearLayout searchPoint) {
		this.searchPoint = searchPoint;
	}

	private boolean isPopupVisible = false;

	public boolean isPopupVisible() {
		return isPopupVisible;
	}

	public void setPopupVisible(boolean isPopupVisible) {
		this.isPopupVisible = isPopupVisible;
	}

	private void goToMyLocation() {
		myLocationLatLng = new LatLng(getLocation().getLatitude(),
				getLocation().getLongitude());
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocationLatLng,
				13));
	}

	private LatLng searchLocation(String locationName) {
		if (locationName == null || locationName.equals("")) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					goToMyLocation();
				}
			});
			return myLocationLatLng;
		}

		Geocoder geocoder = new Geocoder(this);
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocationName(locationName, 1);
			LatLng geoPoint = new LatLng(addresses.get(0).getLatitude(),
					addresses.get(0).getLongitude());
			Message message = httpSearchHandler.obtainMessage();
			Bundle b = new Bundle();
			message.what = HttpConnectionUtil.SUCCESS_GEOCODER;
			b.putDoubleArray("response", new double[] {
					addresses.get(0).getLatitude(),
					addresses.get(0).getLongitude() });
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

	public static void hideSoftKeyboard(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	public void searchBarch(String keyText, LatLng pt) {
		if (pt != null) {
		} else {
			return;
		}
		double latitude = pt.latitude;
		double longitude = pt.longitude;

		int distance = 15000;
		String distanceInput = distance_input.getText().toString();
		if (distance_input.getVisibility() == View.VISIBLE
				&& !distanceInput.equals("")) {
			try {
				distance = Integer.parseInt(distanceInput);
			} catch (Exception e) {
			}
		}
		String postData = GetBranchListJson.GetBranchListReportProtocal(
				Contants.publicModel, latitude, longitude, distance, keyText);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.public_mobile_url, postData, this);
		GetBranchListResponseModel getBranchListResponse = GetBranchListJson
				.ParseGetBranchListResponse(httpResult);

		if (getBranchListResponse == null) {
			return;
		}
		branchListModels = new ArrayList<BranchListModel>(
				getBranchListResponse.getBranchList());
		if (branchListModels.size() > 0) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					addbanks();
				}
			});
		}
	}

	private void addbanks() {
		map.clear();
		LatLng mapCenter = new LatLng(getLocation().getLatitude(),
				getLocation().getLongitude());
		map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(icon))
				.position(mapCenter).flat(false));

		markBrankBranchList = new HashMap<Marker, BranchListModel>();
		map.setOnMarkerClickListener(markerClickListener);
		if (branchListModels != null) {
			for (BranchListModel brankBranchListModel : branchListModels) {
				addBank(brankBranchListModel);
			}
		}
		initializeMarkerWindowMap();
	}

	private void addBank(BranchListModel brankBranchListModel) {

		LatLng mapMarker = new LatLng(brankBranchListModel.getLatitude(),
				brankBranchListModel.getLongitude());

		Marker marker = map.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(iconBank))
				.position(mapMarker).flat(false));

		markBrankBranchList.put(marker, brankBranchListModel);

	}
	
	public void initializeMarkerWindowMap() {

		 map.setInfoWindowAdapter(new InfoWindowAdapter() {

	            // Use default InfoWindow frame
	            @Override
	            public View getInfoWindow(final Marker arg0) {
	            	
	            	//position of popup window
	            	arg0.setInfoWindowAnchor(5.5f, -0.1f);
	            	BranchListModel brankBranchListModel = markBrankBranchList.get(arg0);
	            	if (brankBranchListModel != null) {
	            		mPopView.setBranchListModel(brankBranchListModel);
	            		ImageButton directionBtn = (ImageButton)mPopView.findViewById(R.id.getdirections);
	            		// Setting custom OnTouchListener which deals with the pressed state
	                    // so it shows up 
//	                    infoButtonListener = new OnInfoWindowElemTouchListener(directionBtn,
//	                            getResources().getDrawable(R.drawable.btn_get_directions),
//	                            getResources().getDrawable(R.drawable.btn_get_directions)) 
//	                    {
//	                        @Override
//	                        protected void onClickConfirmed(View v, Marker marker) {
//	                            // Here we can perform some action triggered after clicking the button
//	                            Toast.makeText(ContactSearchActivity.this, "'s button clicked!", Toast.LENGTH_SHORT).show();
//	                        }
//	                    }; 
//	                    directionBtn.setOnTouchListener(infoButtonListener);
	                    
//	            		directionBtn.setOnClickListener(new OnClickListener() {
//							
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+ map.getMyLocation().getLatitude()+","+map.getMyLocation().getLongitude()+"&daddr="+arg0.getPosition().latitude+","+arg0.getPosition().longitude;
//								Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//								intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//								ContactSearchActivity.this.startActivity(intent);
//							}
//						});
	                 // MapWrapperLayout initialization
	                    // 39 - default marker height
	                    // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge 
//	                    mapWrapperLayout.init(map, getPixelsFromDp(ContactSearchActivity.this, 39 + 20)); 
//	            		mapWrapperLayout.setMarkerWithInfoWindow(arg0, mPopView);
	            		return mPopView;
					} else{
						return null;
					}
	            		
	                
	            }

	            // Defines the contents of the InfoWindow
	            @Override
	            public View getInfoContents(Marker arg0) {

	            	return null;
	            }
	        });
		 	searchPoint.setVisibility(View.GONE);
	        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker arg0) {
					// TODO Auto-generated method stub
					setPopupVisible(true);
		    	  	searchPoint.setVisibility(View.GONE);
		    	  	
		    	  	String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+ myLocationLatLng.latitude+","+myLocationLatLng.longitude+"&daddr="+arg0.getPosition().latitude+","+arg0.getPosition().longitude;
					Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
					intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
					ContactSearchActivity.this.startActivity(intent);

				}
			});
	 }
}
