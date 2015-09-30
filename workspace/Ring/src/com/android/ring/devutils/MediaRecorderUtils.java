package com.android.ring.devutils;

import java.io.IOException;

import com.android.log.CLog;

import android.media.MediaRecorder;

public class MediaRecorderUtils {
	private static final CLog clog=new CLog(MediaRecorderUtils.class.getSimpleName());
	private MediaRecorder mRecorder;
	
	public MediaRecorderUtils(){
	}
	
	public void onCreate(){
		clog.println("onCreate");
//		mRecorder = new MediaRecorder();
		
	}
	
	public void onStart(String outFile){
		clog.println("onStart "+outFile);
		
		try {

			mRecorder = new MediaRecorder();
//			mRecorder.reset();
			
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			
			mRecorder.setOutputFile(outFile);
			
			mRecorder.prepare();
			
			mRecorder.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception ee){
			ee.printStackTrace();
		}
		
	}
	
	public void onStop() {
		clog.println("onStop");
		try{
			mRecorder.stop();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void onDestroy(){
		clog.println("onDestroy");
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}
	}
	
}
