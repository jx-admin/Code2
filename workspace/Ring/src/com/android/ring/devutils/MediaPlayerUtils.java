package com.android.ring.devutils;

import java.io.IOException;

import com.android.log.CLog;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MediaPlayerUtils {
	private static final CLog clog=new CLog(MediaPlayerUtils.class.getSimpleName());
	private MediaPlayer mPlayer;
	
	public void onCreate(){
		clog.println("onCreate");
		mPlayer = new MediaPlayer();
		
	}
	
	public void setOnCompletionListener(OnCompletionListener listener){
		mPlayer.setOnCompletionListener(listener);
	}
	
	public void onStart(String fileName){
		clog.println("onStart "+fileName);
		try {
			
			mPlayer.reset();
			
			mPlayer.setDataSource(fileName);
			
			mPlayer.prepare();
			
			mPlayer.start();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	public void onStop(){
		clog.println("onStop");
		mPlayer.stop();
	}
	
	public void onDestroy(){
		clog.println("onDestroy");
		if(mPlayer!=null){
			mPlayer.release();
			mPlayer=null;
		}
	}
	
}
