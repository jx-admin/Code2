
package com.accenture.mbank;

import com.accenture.mbank.model.HelpItemListModel;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class HelpDetailActivity extends BaseActivity {

    LinearLayout container;

    TextView title;

    public static final String ITEM = "item";
    private GoogleAnalytics mGaInstance;
    private Tracker mGaTracker;
    
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
       // mGaInstance = GoogleAnalytics.getInstance(this);

        // Use the GoogleAnalytics singleton to get a Tracker.
     
		EasyTracker.getInstance().activityStart(this); // Add this method.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.help_detail_layout_);
        container = (LinearLayout)findViewById(R.id.help_detail_container);
        title = (TextView)findViewById(R.id.title_text);
        title.setText("help");
        Intent intent = getIntent();
        HelpItemListModel helpItemListModel = (HelpItemListModel)intent.getSerializableExtra(ITEM);
        if (helpItemListModel == null) {
            offLineTest();
        } else {
            addText(helpItemListModel.getTitle(), helpItemListModel.getText());
        }

    }

    private void offLineTest() {
        addText("actiation",
                "The application has not been activites yet.\n click on activites to access the activitaon area\n provider the activition");
        addText("actiation",
                "The application has not been activites yet.\n click on activites to access the activitaon area\n provider the activition");
        addText("actiation",
                "The application has not been activites yet.\n click on activites to access the activitaon area\n provider the activition");
        addText("actiation",
                "The application has not been activites yet.\n click on activites to access the activitaon area\n provider the activition");
        addText("actiation",
                "The application has not been activites yet.\n click on activites to access the activitaon area\n provider the activition");
    }

    private void addText(String title, String content) {

        LayoutInflater inflater = LayoutInflater.from(this);

        LinearLayout helpDetailItem = (LinearLayout)inflater.inflate(R.layout.help_detail_item,
                null);
        TextView titleText = (TextView)helpDetailItem.findViewById(R.id.help_detail_title);
        TextView contentText = (TextView)helpDetailItem.findViewById(R.id.help_detail_content);
        titleText.setText(title);
        contentText.setText(content);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        container.addView(helpDetailItem, params);
    }

    protected void onBackClick() {

        onBackPressed();
    }
}
