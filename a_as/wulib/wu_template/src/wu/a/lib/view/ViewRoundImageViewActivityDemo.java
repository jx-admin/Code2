package wu.a.lib.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import wu.a.template.R;

public class ViewRoundImageViewActivityDemo extends Activity {
    private RoundImageView mQiQiu;
    private RoundImageView mMeiNv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_roundimage_layout);

        mQiQiu = (RoundImageView) findViewById(R.id.id_qiqiu);
        mMeiNv = (RoundImageView) findViewById(R.id.id_meinv);

        mQiQiu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mQiQiu.setType(RoundImageView.TYPE_ROUND);
            }
        });

        mMeiNv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMeiNv.setBorderRadius(90);
            }
        });
    }

}
