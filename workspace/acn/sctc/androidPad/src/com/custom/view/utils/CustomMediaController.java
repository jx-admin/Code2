package com.custom.view.utils;

import java.util.Formatter;
import java.util.Locale;

import com.act.sctc.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;


public class CustomMediaController implements OnClickListener, OnSeekBarChangeListener, OnCompletionListener, OnPreparedListener {

    private MediaPlayerControl  mPlayer;
    private View				video_layout;
    private View				include_mediaplay_controler;
    private SeekBar         	mSeekBar;
    private TextView            mEndTime, mCurrentTime;
    private ImageButton         mPauseButton;
    private ImageButton         mFfwdButton;
    private ImageButton         mRewButton;
//    private ImageButton         mNextButton;
//    private ImageButton         mPrevButton;
    private View				mAnchor;
    private StringBuilder 		mFormatBuilder;
    private Formatter           mFormatter;
    private boolean             mShowing;
    private boolean             mDragging;
    private static final int    sDefaultTimeout = 3000;
    private static final int    FADE_OUT = 1;
    private static final int    SHOW_PROGRESS = 2;
    private Context 			mContext;
    
    public CustomMediaController(View layout){
    	this.mContext=layout.getContext();
    	video_layout=layout;
		include_mediaplay_controler=video_layout.findViewById(R.id.include_mediaplay_controler);
		mPauseButton=(ImageButton) video_layout.findViewById(R.id.mPauseButton);
		mFfwdButton=(ImageButton) video_layout.findViewById(R.id.mFfwdButton);
		mRewButton=(ImageButton) video_layout.findViewById(R.id.mRewButton);
		mSeekBar=(SeekBar) video_layout.findViewById(R.id.seekbar);
		mEndTime=(TextView) video_layout.findViewById(R.id.mEndTime);
		mCurrentTime=(TextView)video_layout. findViewById(R.id.mCurrentTime); 
		mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
		mAnchor=mSeekBar;
		mPauseButton.setOnClickListener(this);
		video_layout.setOnClickListener(this);
		mSeekBar.setOnSeekBarChangeListener(this);
    }
    
    public void setVideoPlayer(MediaPlayerControl mMediaPlayerControl){
		mPlayer=mMediaPlayerControl;
		((VideoView)mPlayer).setOnCompletionListener(this);
		((VideoView)mPlayer).setOnPreparedListener(this);
    }
    

    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons() {
        try {
            if (mPauseButton != null/* && !mPlayer.canPause()*/) {
                mPauseButton.setEnabled(false);
            }
            if (mRewButton != null/* && !mPlayer.canSeekBackward()*/) {
                mRewButton.setEnabled(false);
            }
            if (mFfwdButton != null/* && !mPlayer.canSeekForward()*/) {
                mFfwdButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }
    
	 /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     * @param timeout The timeout in milliseconds. Use 0 to show
     * the controller until hide() is called.
     */
    public void show(int timeout) {
        if (!mShowing && mAnchor != null) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            include_mediaplay_controler.setVisibility(View.VISIBLE);
//            disableUnsupportedButtons();
//            updateFloatingWindowLayout();
//            mWindowManager.addView(mDecor, mDecorLayoutParams);
            mShowing = true;
            updatePausePlay();
        }
        
        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }
    
    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mAnchor == null)
            return;

        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                include_mediaplay_controler.setVisibility(View.GONE);
//                mWindowManager.removeView(mDecor);
            } catch (IllegalArgumentException ex) {
                Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }
	
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	Log.d("MediaController","get msg "+msg.what);
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    setProgress();
                    if (!mDragging && mShowing /*&& mPlayer.isPlaying()*/) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg,500/* 1000 - (pos % 1000)*/);
                    }
                    break;
            }
        }
    };

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        if (mSeekBar != null) {
        	mSeekBar.setProgress(position);
			mSeekBar.setMax(mPlayer.getDuration());
//            if (duration > 0) {
//                // use long to avoid overflow
//                long pos = 1000L * position / duration;
//                mProgress.setProgress( (int) pos);
//            }
            int percent = mPlayer.getBufferPercentage();
            mSeekBar.setSecondaryProgress(percent * 10);
        }
       
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }
	


    private void updatePausePlay() {
       if (/*mRoot == null ||*/ mPauseButton == null||mPlayer==null)
           return;
    	if (mPlayer.isPlaying()) {
    		mPauseButton.setImageResource(android.R.drawable.ic_media_pause);
    	} else {
    		mPauseButton.setImageResource(android.R.drawable.ic_media_play);
    	}
    }

	 @Override
	 public void onClick(View v) {
		 if(mPlayer==null){
			 return;
		 }
		 if(v==mPauseButton){
			 if (mPlayer.isPlaying()) {
				 pause();
			 } else {
				 start();
			 }
			 updatePausePlay();
			 
			 mHandler.removeMessages(SHOW_PROGRESS);
			 mHandler.sendEmptyMessage(SHOW_PROGRESS);
			 mHandler.removeMessages(FADE_OUT);
			 mHandler.sendEmptyMessageDelayed(FADE_OUT, sDefaultTimeout);
		 }else if(v==mSeekBar){
			 int position=mSeekBar.getProgress();
		 }else if(v==video_layout){
			 show(sDefaultTimeout);
		 }
	 }


	@Override
	public void onPrepared(MediaPlayer mp) { 
		int duration = mp.getDuration();
		if (mEndTime != null)
			mEndTime.setText(stringForTime(duration));
		if (mSeekBar != null&&mShowing) {
			mSeekBar.setMax(duration);
		}
//    	show(0);
//    	updatePausePlay();
//		mPauseButton.setImageResource(android.R.drawable.ic_media_play);
	}
	

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mSeekBar != null&&mShowing) {
			mHandler.removeMessages(SHOW_PROGRESS);
			mSeekBar.setProgress(mPlayer.getDuration());
			updatePausePlay();
		}
	}


    public void start() {
        if (mPlayer != null&&!mPlayer.isPlaying()/* && mIsPrepared*/) {
        	mPlayer.start();
//                mStartWhenPrepared = false;
        } else {
//            mStartWhenPrepared = true;
        }
    }


	public void pause() {
		if(mPlayer != null&&mPlayer.isPlaying()){
			mPlayer.pause();
		}
	}


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(fromUser){
			if(mPlayer!=null){
				mPlayer.seekTo(progress);
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		mHandler.removeMessages(FADE_OUT);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessageDelayed(FADE_OUT, sDefaultTimeout);
	}
}
