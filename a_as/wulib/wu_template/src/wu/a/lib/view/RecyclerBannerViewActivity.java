package wu.a.lib.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wu.a.lib.base.BaseActivity;

/**
 * Created by jx on 2016/10/17.
 */
public class RecyclerBannerViewActivity extends BaseActivity {
    RecyclerBannerView recyclerBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerBannerView = new RecyclerBannerView(this);
        setContentView(recyclerBannerView);
    }

    private void init() {

    }

    public void loadData() {
        List<View> items = new ArrayList<View>();
        for (int i = 0; i < 3; i++) {
            TextView tv = new TextView(this);
            tv.setText("test d " + i);
            items.add(tv);
//            recyclerBannerView.addView(tv);
        }
        recyclerBannerView.setData(items);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }
}
