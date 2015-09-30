package com.baidu.lbsapi.panoramaviewdemo;

import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.lbsapi.album.IndoorAlbumOnClickListener;
import com.baidu.lbsapi.album.IndoorPhotoAlbumView;
import com.baidu.lbsapi.controller.PanoramaController;
import com.baidu.lbsapi.panoramadata.IndoorPanorama;
import com.baidu.lbsapi.panoramaview.*;
import com.baidu.lbsapi.BMapManager;
import com.baidu.pplatform.comapi.basestruct.GeoPoint;

/**
 * 全景Demo主Activity
 */
public class PanoramaDemoActivityMain extends Activity implements PanoramaViewListener {
   
	private static final String LTAG = PanoramaDemoActivityMain.class.getSimpleName();
    private PanoramaView mPanoView;
    private Button mBtn = null;
    private Button mTextBtn = null;
    private boolean isShowOverlay = true;
    private boolean isShowTextOverlay = true;
    
    private LinearLayout mTopLayout;
    private LinearLayout mLonlatLayout;
    private TextView mTitleText;
    private EditText mEdit;
    private Button mSwitchBtn;
    private EditText mEditLon;
    private EditText mEditLat;
    
    private int mType;
    
    private IndoorPhotoAlbumView mSSPhotoAlbumView;
    
    private PanoramaController panoController;    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //先初始化BMapManager
        DemoApplication app = (DemoApplication) this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(app);

            app.mBMapManager.init(new DemoApplication.MyGeneralListener());
        }
        Intent intent = getIntent();
        if(intent != null) {
        	mType = intent.getIntExtra("type", -1);
        }
        
        setContentView(R.layout.activity_panorama_main);
        
        mTopLayout = (LinearLayout) findViewById(R.id.linearlayout1);
        mLonlatLayout = (LinearLayout) findViewById(R.id.lonlatlayout);
        mTitleText = (TextView) findViewById(R.id.text1);
        mEdit = (EditText) findViewById(R.id.edit1);
        mSwitchBtn = (Button) findViewById(R.id.btn1);
        mEditLon = (EditText) findViewById(R.id.editlon);
        mEditLat = (EditText) findViewById(R.id.editlat);
        
        
        mPanoView = (PanoramaView) findViewById(R.id.panorama);
    	mPanoView.setPanoramaImageLevel(5);
    	mPanoView.setPanoramaViewListener(this);
    	panoController = new PanoramaController();
    	
 
        //UI初始化
        mBtn = (Button) findViewById(R.id.button);
        mTextBtn = (Button) findViewById(R.id.textbutton);
        mBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        if (isShowOverlay) {
		            addImageMarker();
		            mBtn.setText("删除图片标注");
		        } else {
		            removeImageMarker();
		            mBtn.setText("添加图片标注");
		        }
		        isShowOverlay = !isShowOverlay;
			}
		});
        
        mTextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        if (isShowTextOverlay) {
		            addTextMarker();
		            mTextBtn.setText("删除文字标注");
		        } else {
		        	removeTextMarker();
		        	mTextBtn.setText("添加文字标注");
		        }
		        isShowTextOverlay = !isShowTextOverlay;
			}
		});

        mPanoView.setShowTopoLink(true);
        
        mPanoView.setZoomGestureEnabled(true);
        mPanoView.setRotateGestureEnabled(true);

        processType(mType);
        
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				String json = panoController.getIndoorPanoramaRecommendInfo("0100220000130817164838355J5");
			}
		}).start();
      
    }
    
    
    private void processType(int type) {
    	if(type == BMapApiDemoMain.PID) {
        	mBtn.setVisibility(View.GONE);
        	mTextBtn.setVisibility(View.GONE);
        	mTopLayout.setVisibility(View.VISIBLE);
        	mLonlatLayout.setVisibility(View.GONE);
        	mTitleText.setText("全景ID");
        	mEdit.setVisibility(View.VISIBLE);
        	mEdit.setText("0100220000130817164838355J5");
        	mSwitchBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mPanoView.setPanorama(mEdit.getText().toString().trim()); 
				}
			});
    	}else if(type == BMapApiDemoMain.GEO) {
        	mBtn.setVisibility(View.GONE);
        	mTextBtn.setVisibility(View.GONE);
        	mTopLayout.setVisibility(View.VISIBLE);
        	mLonlatLayout.setVisibility(View.VISIBLE);
        	mTitleText.setText("lon\nlat");
        	mEdit.setVisibility(View.GONE);
        	mEditLon.setText("116.404");
        	mEditLat.setText("39.945");
        	mSwitchBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					double lat = Double.parseDouble(mEditLat.getText().toString().trim());
					double lon = Double.parseDouble(mEditLon.getText().toString().trim());
					mPanoView.setPanorama(lon, lat); 
				}
			});
    	}else if(type == BMapApiDemoMain.MARKER) {
        	mBtn.setVisibility(View.VISIBLE);
        	mTextBtn.setVisibility(View.VISIBLE);
        	mTopLayout.setVisibility(View.GONE);
        	new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mPanoView.setPanorama("0100220000130817164838355J5"); 
				}
			}, 1000);
//        	mPanoView.setPanorama("0100220000130817164838355J5"); 
    	}else if(type == BMapApiDemoMain.MERCATOR) {
        	mBtn.setVisibility(View.GONE);
        	mTextBtn.setVisibility(View.GONE);
        	mTopLayout.setVisibility(View.VISIBLE);
        	mLonlatLayout.setVisibility(View.VISIBLE);
        	mTitleText.setText("墨卡托坐标y\nx");
        	mEdit.setVisibility(View.GONE);
        	mEditLat.setText("12971348");
        	mEditLon.setText("4826239");
        	mSwitchBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int x = Integer.parseInt(mEditLat.getText().toString().trim());
					int y = Integer.parseInt(mEditLon.getText().toString().trim());
					mPanoView.setPanorama(x, y); 
					
				}
			});
    	}else if(type == BMapApiDemoMain.INDOOR) {
			mBtn.setVisibility(View.GONE);
			mTextBtn.setVisibility(View.GONE);
			mTopLayout.setVisibility(View.GONE);
			mSSPhotoAlbumView = (IndoorPhotoAlbumView) findViewById(R.id.ssphoto_id);
			mSSPhotoAlbumView.setVisibility(View.VISIBLE);

			mSSPhotoAlbumView.setOnIndoorAlbumClickListener(new IndoorAlbumOnClickListener() {
				
				@Override
				public void onItemClick(String pid) {
					if (mPanoView != null) {
						mPanoView.setPanorama(pid);
					}
					
				}
			});

			mSSPhotoAlbumView.setIndoorPanoramaId("28e700f15aae5418085cb3a7");
    	}
    }
    
    ImageMarker marker1;
    ImageMarker marker2;
    //添加标注
    private void addImageMarker() {
        //天安门坐标
        GeoPoint p = new GeoPoint(39914195,116403818);
        marker1 = new ImageMarker(p);
        marker1.setMarker(getResources().getDrawable(R.drawable.icon_marka));
        marker1.setOnTabMarkListener(new OnTabMarkListener() {
			
			@Override
			public void onTab(int arg0) {
				Log.d(LTAG, "index:" + arg0);
	            Toast.makeText(PanoramaDemoActivityMain.this,
	                    "marker1标注已被点击", Toast.LENGTH_SHORT).show();
			}
		});
        GeoPoint p1 = new GeoPoint(39914195,116403928);
        marker2 = new ImageMarker(p1);
//        marker2.setPitch((float) 50.0);
        marker2.setMarker(getResources().getDrawable(R.drawable.icon_marka));
        marker2.setOnTabMarkListener(new OnTabMarkListener() {
			
			@Override
			public void onTab(int arg0) {
				Log.d(LTAG, "index:" + arg0);
	            Toast.makeText(PanoramaDemoActivityMain.this,
	                    "marker2标注已被点击", Toast.LENGTH_SHORT).show();
			}
		});
        mPanoView.addMarker(marker1);
        mPanoView.addMarker(marker2);
    }

    //删除标注
    private void removeImageMarker() {
    	mPanoView.removeMarker(marker1);
    	mPanoView.removeMarker(marker2);
    }
    
    TextMarker textMark1;
    TextMarker textMark2;
    
    //添加标注
    private void addTextMarker() {
        //天安门坐标
        GeoPoint p = new GeoPoint(39914195,116403218);
        textMark1 = new TextMarker(p);
        textMark1.setFontColor(0xFFFF0000);
        textMark1.setText("你好marker");
        textMark1.setFontSize(36);
        textMark1.setBgColor(0x7F0000FF);
        textMark1.setPadding(20, 20, 20, 20);
//        textMark1.setPitch((float) 50.0);
        textMark1.setOnTabMarkListener(new OnTabMarkListener() {
			
			@Override
			public void onTab(int arg0) {
				Log.d(LTAG, "index:" + arg0);
	            Toast.makeText(PanoramaDemoActivityMain.this,
	                    "textMark1标注已被点击", Toast.LENGTH_SHORT).show();
			}
		});

        GeoPoint p1 = new GeoPoint(39914195,116403928);
        textMark2 = new TextMarker(p1);
        textMark2.setFontColor(Color.RED);
        textMark2.setText("你好marker");
        textMark2.setFontSize(36);
        textMark2.setBgColor(Color .BLUE);
        textMark2.setPadding(20, 20, 20, 20);
        textMark2.setPitch((float) 50.0);
        textMark2.setOnTabMarkListener(new OnTabMarkListener() {
			
			@Override
			public void onTab(int arg0) {
				Log.d("LTAG", "index:" + arg0);
	            Toast.makeText(PanoramaDemoActivityMain.this,
	                    "textMark2标注已被点击", Toast.LENGTH_SHORT).show();
				
			}
		});
        mPanoView.addMarker(textMark1);
        mPanoView.addMarker(textMark2);
    }

    //删除标注
    private void removeTextMarker() {
    	mPanoView.removeMarker(textMark1);
    	mPanoView.removeMarker(textMark2);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mPanoView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPanoView.onResume();
    }

    @Override
    protected void onDestroy() {
        mPanoView.destroy();
        super.onDestroy();
    }
 
    @Override
	public void onLoadPanoramBegin() {
		Log.d(LTAG, "loadPanoramBegin");
	}


	@Override
	public void onLoadPanoramaEnd() {
		Log.d(LTAG, "loadPanoramaEnd");
	}


	@Override
	public void onLoadPanoramaError() {
		Log.d(LTAG, "loadPanoramaError");
	}


}
