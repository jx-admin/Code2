package com.android.ring.devutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import com.android.log.CLog;

@SuppressLint("NewApi")
public class AudioManagerUtils {
	Context context;
	AudioManager mAudioManager;
	public AudioManagerUtils(Context context){
		this.context=context;
		mAudioManager=(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	int model;
	int ringModel;
	int volume;
	boolean isSilent;
	public void silent(){
		model=AudioManager.STREAM_VOICE_CALL;
		ringModel=mAudioManager.getRingerMode();
		volume=mAudioManager.getStreamVolume(model);
		CLog.print("silent","model="+model+" ringModel="+ringModel+" volume"+volume);
		mAudioManager.setStreamVolume(model, 0, 0);
		isSilent=true;
	}
	public void reset(){
		if(!isSilent){
			return; 
		}
		CLog.print("reset","model="+model+" ringModel="+ringModel+" volume"+volume);
		mAudioManager.setStreamVolume(model, volume, 0);
		if (Build.VERSION.SDK_INT >= 8) {
			mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
			mAudioManager.requestAudioFocus(null, AudioManager.STREAM_RING, AudioManager.AUDIOFOCUS_GAIN);
		}
		isSilent=false;
		
	}

}
