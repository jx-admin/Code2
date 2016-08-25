package wu.videodemo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

/**
 * Created by jx on 2016/4/26.
 */
public class VideoPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, TextureView.SurfaceTextureListener {
    private static final String TAG = "VedioPlayer";
    private static boolean DEBUG = BuildConfig.DEBUG;
    private static VideoPlayer mVedioPlayer;
    private MediaPlayer mMediaPlayer;
    private Surface mSurface;
    private MediaPlayerCallback mediaPlayerCallback;
    private Uri uri;
    private boolean isPlaying;
    private Context context;
    private TextureView surface;
    private boolean looper;
    private SurfaceTexture surfaceTexture;

    public static VideoPlayer getInstance() {
        if (mVedioPlayer == null) {
            synchronized (VideoPlayer.class) {
                if (mVedioPlayer == null) {
                    mVedioPlayer = new VideoPlayer();
                }
            }
        }
        return mVedioPlayer;
    }

    private VideoPlayer() {
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
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public Uri getUri() {
        return uri;
    }

    public void start(Context context, Uri uri, TextureView surface) {
        start(context, uri, surface, false);
    }

    public void start(Context context, Uri uri, TextureView surface, boolean looper) {
        if (surface == null) {
            Utils.e("erro start witch TexturesView == null");
            return;
        }
        isPlaying = true;
        surface.setSurfaceTextureListener(this);
        this.context = context;
        this.uri = uri;
        this.surface = surface;
        this.looper = looper;
        start(context, uri, surface.getSurfaceTexture(), looper, isPlaying);
    }

    private void start(Context context, Uri uri, SurfaceTexture surfaceTexture, boolean looper, boolean isPlaying) {
        if (!isPlaying || surfaceTexture == null || uri == null) {
            return;
        }
        this.surfaceTexture = surfaceTexture;
        mSurface = new Surface(surfaceTexture);
        if (mMediaPlayer == null) {
            create();
        } else {
            stop();
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setLooping(looper);
            mMediaPlayer.setDataSource(context, uri);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            if (DEBUG) Log.w(TAG, "Unable to open content: " + uri, e);
            e.printStackTrace();
            onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    public void stop() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();

            if (mediaPlayerCallback != null) {
                mediaPlayerCallback.onPlayerStop();
            }
        }
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
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
        try {
            mMediaPlayer.stop();
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.e("restart Error");
            onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        if (mediaPlayerCallback != null) {
            mediaPlayerCallback.onPrepared(mp);
        }
        onStart();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        if (mediaPlayerCallback != null) {
            mediaPlayerCallback.onVideoSizeChanged(mp, width, height);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (DEBUG) {
            Utils.d("onCompletion " + uri);
        }
        if (looper) {
            return;
        }
        if (mediaPlayerCallback != null) {
            mediaPlayerCallback.onCompletion(mp);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (DEBUG) {
            Utils.d("onError " + mp + " what=" + what + " extra=" + extra);
        }
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

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (DEBUG) {
            Utils.d("onSeekComplete " + uri);
        }
        if (mediaPlayerCallback != null) {
            mediaPlayerCallback.onSeekComplete(mp);
        }
    }

    public void setMediaPlayerCallback(MediaPlayerCallback mediaPlayerCallback) {
        this.mediaPlayerCallback = mediaPlayerCallback;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        start(context, uri, surface, looper, isPlaying);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (surface != this.surfaceTexture) {
            return false;
        }
        stop();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public static interface MediaPlayerCallback {
        public void onPlayerStop();

        public void onPlayerStart();

        public void onCompletion(MediaPlayer mp);

        public void onPrepared(MediaPlayer mp);

        public void onVideoSizeChanged(MediaPlayer mp, int width, int height);

        public boolean onError(MediaPlayer mp, int what, int extra);

        public boolean onInfo(MediaPlayer mp, int what, int extra);

        public void onBufferingUpdate(MediaPlayer mp, int percent);

        public void onSeekComplete(MediaPlayer mp);
    }

    private void onStart() {
        if (mediaPlayerCallback != null) {
            mediaPlayerCallback.onPlayerStart();
        }
    }
}
