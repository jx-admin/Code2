package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
//import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.PopupWindow;

import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;

@SuppressLint("NewApi")
public class ChartActivity extends BaseActivity {

	private ViewGroup chartsLayout;
	public ViewGroup chartsWindow;

	private String currentScreen;

	private PopupWindow popupwindow = null;

	public void setPopupWindow(PopupWindow popupwindow) {
		this.popupwindow = popupwindow;
	}
	
	static Context context;

	public static Context getContext() {
		return context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		
		setContentView(R.layout.activity_chart);
		chartsLayout = (ViewGroup) findViewById(R.id.charts_layout);
		chartsWindow = (ViewGroup) findViewById(R.id.charts_total_window);

		currentScreen = getIntent().getStringExtra("CURRENT_SCREEN");
		LogManager.d(currentScreen);

		ChartActivity.context = this;

		MainActivity mainAcitivty = (MainActivity) MainActivity.getContext();
		if (currentScreen.equals("ACCOUNTS")) {
			mainAcitivty.accountRotateLayoutManager.chartLayout = this.chartsLayout;
			mainAcitivty.accountRotateLayoutManager.showChart();
		} else if (currentScreen.equals("CARDS")) {
			mainAcitivty.cardsRotateLayoutManager.chartLayout = this.chartsLayout;
			mainAcitivty.cardsRotateLayoutManager.showChart();
		} else if (currentScreen.equals("INVESTMENTS")) {
			mainAcitivty.investmentRotateLayoutManager.chartLayout = this.chartsLayout;
			mainAcitivty.investmentRotateLayoutManager.showChart();
		} else if (currentScreen.equals("LOANS")) {
			mainAcitivty.loansRotateLayoutManager.chartLayout = this.chartsLayout;
			mainAcitivty.loansRotateLayoutManager.showChart();
		}
		
		setShowAnimation(chartsWindow, Contants.TIME_FADE_IN_ANIMATION,
				new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation animation) {
				/*
				 * Enable rotate after the animation finish
				 */
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}
		});
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//			ActionBar actionBar = ActionBar.getSupportActionBar();
			ActionBar actionBar = getActionBar();
			if(actionBar!=null){
				actionBar.hide();
			}
		}
	}

	/*
	 * Disable back key in chart graph
	 */
	@Override
	public void onBackPressed() {
		return;
	}

	/*
	 * When screen move to portrait, finish and switch to MainActivity
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		LogManager.d("ChartActivity onConfigurationChanged");
		if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
			LogManager.d("ChartActivity onConfigurationChanged new SCREEN_ORIENTATION_SENSOR_LANDSCAPE");

			/*
			 * Dismiss the pop up window in the chart.
			 */
	        if (popupwindow != null)
	        	popupwindow.dismiss();

			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//			this.requestWindowFeature(Window.FEATURE_NO_TITLE);

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
//				ActionBar actionBar = ActionBar.getSupportActionBar();
				ActionBar actionBar = getActionBar();
				if(actionBar!=null){
					actionBar.hide();
				}
			}
//			setHideAnimation(chartsWindow, Contants.TIME_FADE_OUT_ANIMATION,
//					new AnimationListener() {
//
//						@Override
//						public void onAnimationEnd(Animation animation) {
//							finish();
//						}
//
//						@Override
//						public void onAnimationRepeat(Animation animation) {
//						}
//
//						@Override
//						public void onAnimationStart(Animation animation) {
//						}
//					});
			finish();

		}
		super.onConfigurationChanged(newConfig);
	}

}
