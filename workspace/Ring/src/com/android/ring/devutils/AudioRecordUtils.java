package com.android.ring.devutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.log.CLog;
/*
 * https://developer.android.com/guide/appendix/media-formats.html
Android多媒体框架包含获取和编码多种音频格式的支持，所以你可以轻松地把音频合并到你的应用中．如果设备支持，你可以使用MediaRecorder APIs 进行录音．
 
本章向你展示如何写一个应用从设备上的microphone获取音频，然后保存并回放．
 
注：Android模拟器不具有录音的能力，但是真实的设备一般都具有此功能．
 
 
 
执行音频获取
 从设备获取音频比回放音频或视频要复杂一点，但是也还算简单：
 

创建一个android.media.MediaRecorder的新实例．
 使用MediaRecorder.setAudioSource()设置音频源，一般要使用MediaRecorder.AudioSource.MIC.
 使用MediaRecorder.setOutputFormat()设置输出文件的格式．
 使用MediaRecorder.setOutputFile()设置输出文件的名字．
 使用MediaRecorder.setAudioEncoder()设置音频编码．
 调用MediaRecorder 实例的MediaRecorder.prepare()．
 MediaRecorder.start()开始获取音频．
 调用MediaRecorder.stop().停止．
 当你用完MediaRecorder实例后，调用MediaRecorder.release()，就会立即释放资源．

 * The application needs to have the permission to write to external storage

 * if the output file is written to the external storage, and also the

 * permission to record audio. These permissions must be set in the

 * application's AndroidManifest.xml file, with something like:

 *

 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

 * <uses-permission android:name="android.permission.RECORD_AUDIO" />

 *

 */
public class AudioRecordUtils {
	private static final String LOG_TAG = "AudioRecordTest";
	CLog cLog=new CLog(LOG_TAG);
	MediaRecorderUtils mediaRecorderUtils=new MediaRecorderUtils();
	MediaPlayerUtils mediaPlayerUtils=new MediaPlayerUtils();
	
	private List<String> mFileNames = new ArrayList<String>();
	private String path;
	private String suffix=".3gp";
	public AudioRecordUtils(){
		mediaRecorderUtils.onCreate();
		mediaPlayerUtils.onCreate();
		mediaPlayerUtils.setOnCompletionListener(onCompletionListener);


		path = Environment.getExternalStorageDirectory().getAbsolutePath();
		
		
		{
			mFileNames.add("/storage/sdcard0/1392540598987.3gp");
			mFileNames.add("/storage/sdcard0/1392540603987.3gp");
			mFileNames.add("/storage/sdcard0/1392540608987.3gp");
			mFileNames.add("/storage/sdcard0/1392540613988.3gp");
			mFileNames.add("/storage/sdcard0/1392540618988.3gp");
			mFileNames.add("/storage/sdcard0/1392540623988.3gp");
			mFileNames.add("/storage/sdcard0/1392540628989.3gp");
			mFileNames.add("/storage/sdcard0/1392540633990.3gp");
			mFileNames.add("/storage/sdcard0/1392540638990.3gp");
			mFileNames.add("/storage/sdcard0/1392540643990.3gp");
			mFileNames.add("/storage/sdcard0/1392540648991.3gp");
			mFileNames.add("/storage/sdcard0/1392540653991.3gp");

		}

	}
	
	
	
	class RecordButton extends Button {
		
		boolean mStartRecording = true;
		
		
		OnClickListener clicker = new OnClickListener() {
			
			public void onClick(View v) {
				
				if(mStartRecording){
					startTask();
//					mediaRecorderUtils.onStart(outFile);
					setText("Stop recording");
				}else{
//					mediaRecorderUtils.onStop();
					setText("Start recording");
				}
				
				mStartRecording = !mStartRecording;
				
			}
			
		};
		
		
		public RecordButton(Context ctx) {
			
			super(ctx);
			
			setText("Start recording");
			
			setOnClickListener(clicker);
			
		}
		
	}
	
	
	class PlayButton extends Button {
		
		boolean mStartPlaying = true;
		
		
		OnClickListener clicker = new OnClickListener() {
			
			public void onClick(View v) {
				
				if (mStartPlaying) {
					play();
					
//					mediaPlayerUtils.onStart(fileName);
					setText("Stop playing");
					
				} else {
					
//					mediaPlayerUtils.onStop();
					setText("Start playing");
					
				}
				
				mStartPlaying = !mStartPlaying;
				
			}
			
		};
		
		
		public PlayButton(Context ctx) {
			
			super(ctx);
			
			setText("Start playing");
			
			setOnClickListener(clicker);
			
		}
		
	}
	
	
	public View onCreate(Context context) {
		
		
		LinearLayout ll = new LinearLayout(context);
		
		
		ll.addView(new RecordButton(context),
				
				new LinearLayout.LayoutParams(
						
						ViewGroup.LayoutParams.WRAP_CONTENT,
						
						ViewGroup.LayoutParams.WRAP_CONTENT,
						
						0));
		
		
		ll.addView(new PlayButton(context),
				
				new LinearLayout.LayoutParams(
						
						ViewGroup.LayoutParams.WRAP_CONTENT,
						
						ViewGroup.LayoutParams.WRAP_CONTENT,
						
						0));
		return ll;
		
//	        context.setContentView(ll);

	}
	
	Timer mTimer = new Timer(); 
	public void startTask(){
	mTimer.schedule(new TimerTask() { 
		long start=0;
		long perid=1*60*1000;
	            @Override  
	            public void run() {
	            	long cur=System.currentTimeMillis();
	            	if(start==0){
	            		start=cur;
	            		mFileNames.clear();
	            	}else{
		            	cLog.println("stop");
	            		mediaRecorderUtils.onStop();
	            	}
	            	if(cur-start>=perid){
	            		mTimer.cancel();
		            	cLog.println("cancle");
	            	}else{
	            		String file=path+"/"+cur+suffix;
	            		cLog.println(file);
	            		mediaRecorderUtils.onStart(file);
	            		mFileNames.add(file);
	            	}
	            }   
	        },0, 5*1000);   
	}
	
	int times=0;
	OnCompletionListener onCompletionListener=new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			play();
		}
	};
	
	
	public void play(){
		if(times>=mFileNames.size()){
			return;
		}
		cLog.println("play:"+times);
		mediaPlayerUtils.onStart(this.mFileNames.get(times));
		times++;
	}

}

