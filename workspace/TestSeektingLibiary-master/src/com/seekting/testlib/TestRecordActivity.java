
package com.seekting.testlib;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.seekting.util.record.MediaRecordUtil;

public class TestRecordActivity extends TestActivity implements OnClickListener {

    TextView status;

    Button start, end;

    public TestRecordActivity() {
        name = "Record";

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_record_activity);
        start = (Button)findViewById(R.id.start);
        end = (Button)findViewById(R.id.end);
        status = (TextView)findViewById(R.id.status);

        // start.setOnClickListener(this);
        start.setText("按住说话");
        start.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        start.setText("松开结束");

                        record();
                    }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        start.setText("按住说话");
                        MediaRecordUtil.getInstance().stopRecordAndFile();
                        break;

                    }
                    case MotionEvent.ACTION_MOVE: {

                    }
                }
                start.onTouchEvent(event);
                return true;
            }
        });
        end.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == start) {

            record();
        } else if (v == end) {
            MediaRecordUtil.getInstance().stopRecordAndFile();
        }
    }

    MediaRecordUtil mRecordUtil;

    void record() {
        mRecordUtil = MediaRecordUtil.getInstance();
        int mResult = mRecordUtil.startRecordAndFile();

    }
}
