package wu.videodemo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

/**
 * Created by jx on 2016/4/26.
 */
public class VedioPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {
    private static final String TAG = "VedioPlayer";
    private static boolean DEBUG = true;
    private static VedioPlayer mVedioPlayer;
    private MediaPlayer mMediaPlayer;
    private Surface mSurface;
    private MediaPlayerCallback mediaPlayerCallback;

    public static VedioPlayer getInstance() {
        if (mVedioPlayer == null) {
            synchronized (VedioPlayer.class) {
                if (mVedioPlayer == null) {
                    mVedioPlayer = new VedioPlayer();
                }
            }
        }
        return mVedioPlayer;
    }

    private VedioPlayer() {
        create();
    }

    public void create() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void start(Context context, Uri uri, TextureView surface) {
        mSurface = new Surface(surface.getSurfaceTexture());
        stop();
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            if (DEBUG) Log.w(TAG, "Unable to open content: " + uri, e);
            e.printStackTrace();
            onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    public void stop() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void destroy() {
        if (mMediaPlayer != null) {
            stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void resume() {
        mMediaPlayer.start();
    }

    public void restart() {
        mMediaPlayer.stop();
        mMediaPlayer.prepareAsync();
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        if (mediaPlayerCallback != null) {
            mediaPlayerCallback.onPrepared(mp);
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (mediaPlayerCallback != null) {
            mediaPlayerCallback.onVideoSizeChanged(mp, width, height);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mediaPlayerCallback != null) {
            mediaPlayerCallback.onCompletion(mp);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mediaPlayerCallback != null) {
            return mediaPlayerCallback.onError(mp, what, extra);
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (mediaPlayerCallback != null) {
            return mediaPlayerCallback.onInfo(mp, what, extra);
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (mediaPlayerCallback != null) {
            mediaPlayerCallback.onBufferingUpdate(mp, percent);
        }
    }

    public void setMediaPlayerCallback(MediaPlayerCallback mediaPlayerCallback) {
        this.mediaPlayerCallback = mediaPlayerCallback;
    }

    public static interface MediaPlayerCallback {
        public void onCompletion(MediaPlayer mp);

        public void onPrepared(MediaPlayer mp);

        public void onVideoSizeChanged(MediaPlayer mp, int width, int height);

        public boolean onError(MediaPlayer mp, int what, int extra);

        public boolean onInfo(MediaPlayer mp, int what, int extra);

        public void onBufferingUpdate(MediaPlayer mp, int percent);
    }
}
