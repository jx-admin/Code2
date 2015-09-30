package com.act.offline;

import java.io.File;
import java.io.IOException;

import wu.a.lib.file.FileUtils;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	private boolean isAll=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		File sdFile=FileUtils.getSdcardForWrite();
		if(sdFile==null){
			Log.d("FileUtils","can't write to FileUtils");
		}else{
			Log.d("FileUtils","FileUtils:"+sdFile.getAbsolutePath());
		}
		if(isAll){
			FileUtils.deleteFile(new File(sdFile.getAbsolutePath()+"/sctc"));
		}
		try {
			FileUtils.getAssetsList(this,"sctc",sdFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("FileUtils",e.getMessage());
		}
		File oldVideoFile=new File(sdFile.getAbsolutePath()+"/ad.mp4");
		File videoFile=new File(sdFile.getAbsolutePath()+"/sctc/video/ad.mp4");
		File vP=videoFile.getParentFile();
		if(!vP.exists()){
			vP.mkdirs();
		}
		if(!videoFile.exists()){
			if(oldVideoFile.exists()){
				oldVideoFile.renameTo(videoFile);
			}
		}
		if(oldVideoFile.exists()){
			oldVideoFile.delete();
		}
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
