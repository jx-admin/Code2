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
    VedioPlayer vp;
    TextureView sufaceView;
    Uri uri = Uri.parse("/storage/emulated/0/Android/data/best.funny.live.shortvideo/cache/aae189283dc47b71fef24e46d3fb18b1eb242bd73393a2944afaee4cba14876b.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout);
        sufaceView = (TextureView) findViewById(R.id.screen);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.resume).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.restart).setOnClickListener(this);
        findViewById(R.id.create).setOnClickListener(this);
        findViewById(R.id.destroy).setOnClickListener(this);
        vp = VedioPlayer.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                vp.start(this, uri, sufaceView);
                break;
            case R.id.stop:
                vp.stop();
                break;
            case R.id.resume:
                vp.resume();
                break;
            case R.id.pause:
                vp.pause();
                break;
            case R.id.restart:
                vp.restart();
                break;
            case R.id.create:
                vp.create();
                break;
            case R.id.destroy:
                vp.destroy();
                break;
        }
    }
}
