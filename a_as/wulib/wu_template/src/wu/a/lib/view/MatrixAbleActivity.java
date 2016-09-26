package wu.a.lib.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import wu.a.template.R;

/**
 * Created by jx on 2016/9/12.
 */
public class MatrixAbleActivity extends Activity {
    private TextureView text_tv;
    private View textView1;
    private FrameLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matrixable_activity);
        text_tv = (TextureView) findViewById(R.id.text_tv);
        textView1 = (View) findViewById(R.id.textView1);
        content = (FrameLayout) findViewById(R.id.content);

//        MatrixTouchListener mListener = new MatrixTouchListener(text_tv);
//        text_tv.setOnTouchListener(mListener);
//        mListener.setImageBitmap(new TextureViewMatrixAble(text_tv));


        MatrixTouchListener mListener = new MatrixTouchListener(content);
        content.setOnTouchListener(mListener);
        mListener.setImageBitmap(new ViewMatrixAble(textView1));
        Log.e("dddd", "start MatrixAbleActiv8t");
        Log.d("dddd", "start MatrixAbleActiv8t");
    }
}
