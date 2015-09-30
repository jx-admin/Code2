package com.baidu.navi.sdkdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;

/**
 * GPS导航Demo
 */
public class RouteGuideDemo extends Activity{
    private BNaviPoint mStartPoint = new BNaviPoint(116.30142, 40.05087,
            "百度大厦", BNaviPoint.CoordinateType.GCJ02);
    private BNaviPoint mEndPoint = new BNaviPoint(116.39750, 39.90882,
            "北京天安门", BNaviPoint.CoordinateType.GCJ02);
    private List<BNaviPoint> mViaPoints = new ArrayList<BNaviPoint>();
    private Button mBtnAddViaPoint;
    
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_routeguide);
		Button btnStartNavigation = (Button)findViewById(R.id.button_navigation);
		btnStartNavigation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    if (mViaPoints.size() == 0) {
			        launchNavigator();
			    } else {
			        launchNavigatorViaPoints();
			    }
			}
		});
		
		findViewById(R.id.start_nav2_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mViaPoints.size() == 0) {
                    launchNavigator2();
                } else {
                    launchNavigatorViaPoints();
                }
            }
        });
		
		mBtnAddViaPoint = (Button) findViewById(R.id.add_via_btn); 
		mBtnAddViaPoint.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    addViaPoint();
                }
            });
	}

    private void addViaPoint() {
        EditText viaXEditText = (EditText) findViewById(R.id.et_via_x);
        EditText viaYEditText = (EditText) findViewById(R.id.et_via_y);
        double latitude = 0, longitude = 0;
        try {
            latitude = Integer.parseInt(viaXEditText.getText().toString());
            longitude = Integer.parseInt(viaYEditText.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        // 默认坐标系为GCJ
        BNaviPoint viaPoint = new BNaviPoint(longitude/1e5, latitude/1e5,
                "途经点" + (mViaPoints.size()+1));
        mViaPoints.add(viaPoint);
        Toast.makeText(this, "已添加途经点：" + viaPoint.getName(),
                Toast.LENGTH_SHORT).show();
        if (mViaPoints.size() >= 3) {
            mBtnAddViaPoint.setEnabled(false);
        }
    }

	/**
	 * 启动GPS导航. 前置条件：导航引擎初始化成功
	 */
	private void launchNavigator(){
		//这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标
		BaiduNaviManager.getInstance().launchNavigator(this,
				40.05087, 116.30142,"百度大厦", 
		        39.90882, 116.39750,"北京天安门",
				NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, 		 //算路方式
				true, 									   		 //真实导航
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, //在离线策略
				new OnStartNavigationListener() {				 //跳转监听
					
					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(RouteGuideDemo.this, BNavigatorActivity.class);
						intent.putExtras(configParams);
				        startActivity(intent);
					}
					
					@Override
					public void onJumpToDownloader() {
					}
				});
	}
	
    /**
     * 指定导航起终点启动GPS导航.起终点可为多种类型坐标系的地理坐标。
     * 前置条件：导航引擎初始化成功
     */
    private void launchNavigator2(){
        //这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标
        BNaviPoint startPoint = new BNaviPoint(116.307854,40.055878,
                "百度大厦", BNaviPoint.CoordinateType.BD09_MC);
        BNaviPoint endPoint = new BNaviPoint(116.403875,39.915168,
                "北京天安门", BNaviPoint.CoordinateType.BD09_MC);
        BaiduNaviManager.getInstance().launchNavigator(this,
                startPoint,                                      //起点（可指定坐标系）
                endPoint,                                        //终点（可指定坐标系）
                NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME,       //算路方式
                true,                                            //真实导航
                BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, //在离线策略
                new OnStartNavigationListener() {                //跳转监听
                    
                    @Override
                    public void onJumpToNavigator(Bundle configParams) {
                        Intent intent = new Intent(RouteGuideDemo.this, BNavigatorActivity.class);
                        intent.putExtras(configParams);
                        startActivity(intent);
                    }
                    
                    @Override
                    public void onJumpToDownloader() {
                    }
                });
    }

    /**
     * 增加一个或多个途经点，启动GPS导航. 
     * 前置条件：导航引擎初始化成功
     */
    private void launchNavigatorViaPoints(){
        //这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标
//        BNaviPoint startPoint = new BNaviPoint(113.97348, 22.53951, "白石洲");
//        BNaviPoint endPoint   = new BNaviPoint(113.92576, 22.48876, "蛇口");
//        BNaviPoint viaPoint1 = new BNaviPoint(113.94104, 22.54343, "国人通信大厦");
//        BNaviPoint viaPoint2 = new BNaviPoint(113.94863, 22.53873, "中国银行科技园支行");
        List<BNaviPoint> points = new ArrayList<BNaviPoint>();
        points.add(mStartPoint);
        points.addAll(mViaPoints);
        points.add(mEndPoint);
        BaiduNaviManager.getInstance().launchNavigator(this,
                points,                                          //路线点列表
                NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME,       //算路方式
                true,                                            //真实导航
                BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, //在离线策略
                new OnStartNavigationListener() {                //跳转监听
                    
                    @Override
                    public void onJumpToNavigator(Bundle configParams) {
                        Intent intent = new Intent(RouteGuideDemo.this, BNavigatorActivity.class);
                        intent.putExtras(configParams);
                        startActivity(intent);
                    }
                    
                    @Override
                    public void onJumpToDownloader() {
                    }
                });
    }
}
