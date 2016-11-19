package wu.a.lib.view;

import java.util.ArrayList;
import java.util.List;

import wu.a.lib.data.json.JsonActivity;
import wu.a.template.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
    ListView mListView = null;
    String mStrDemos[] = {
            "ChartButton",
            "CustomDialogActivity",
            "PathShapeActivity",
            "LineViewActivity",
            "DatePickerDialogActivity",
            "DrawableActivity",
            "FaceTest",
            "CoverFlowActivity",
            "CoverFlowImageActivity",
            "EditViewTestActivity",
            "SwitchButtonActivity",
            "CircleLinearActivity",
            "ViewRoundImageViewActivityDemo",
            "RecyclerBannerView",
            "JsonActivity"

    };
    Class<?> mActivities[] = {
            ChartButtonActivity.class,
            CustomDialogActivity.class,
            PathShapeActivity.class,
            LineViewActivity.class,
            DatePickerDialogActivity.class,
            DrawableActivity.class,
            FaceTest.class,
            CoverFlowActivity.class,
            CoverFlowImageActivity.class,
            EditViewTestActivity.class,
            SwitchButtonActivity.class,
            CircleLinearActivity.class,
            ViewRoundImageViewActivityDemo.class,
            JsonActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mListView = (ListView) findViewById(R.id.listView);
        // ���ListItem�������¼���Ӧ
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < mStrDemos.length; i++) {
            data.add(mStrDemos[i]);
        }
        mListView.setAdapter((ListAdapter) new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
        mListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int index, long arg3) {
                onListItemClick(index);
            }
        });
    }

    private void onListItemClick(int index) {
        if (index < 0 || index >= mActivities.length + 1)
            return;


        Intent intent = null;
        intent = new Intent(this, mActivities[index]);
        this.startActivity(intent);
    }
}
