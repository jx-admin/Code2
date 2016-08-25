package wu.videodemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;

/**
 * Created by jx on 2016/4/26.
 */
public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    VideoPlayer videoPlayer;
    TextureView sufaceView;
    Uri uri = Uri.parse("/storage/emulated/0/Pictures/FunnyVideo/VID_20160610_163104.mp4");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);
        sufaceView = (TextureView) findViewById(R.id.player_view);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.resume).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.restart).setOnClickListener(this);
        findViewById(R.id.create).setOnClickListener(this);
        findViewById(R.id.destroy).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoPlayer = VideoPlayer.getInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoPlayer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                videoPlayer.start(this, uri, sufaceView);
                break;
            case R.id.stop:
                videoPlayer.stop();
                break;
            case R.id.resume:
                videoPlayer.resume();
                break;
            case R.id.pause:
                videoPlayer.pause();
                break;
            case R.id.restart:
                videoPlayer.restart();
                break;
            case R.id.create:
                videoPlayer.create();
                break;
            case R.id.destroy:
                videoPlayer.destroy();
                break;
        }
    }
}
