package com.act.sctc.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.act.sctc.R;

public class VideoPlayerActivity extends Activity {
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_video_player);
		
		final VideoView videoView = (VideoView) findViewById(R.id.video_view);
		final Uri mUri = Uri.parse("/sdcard/ad.mp4");
		MediaController mMediaController=new MediaController(VideoPlayerActivity.this);
		videoView
				.setMediaController(mMediaController);
		videoView.setVideoURI(mUri);
		
		videoView.start();
	}

}
