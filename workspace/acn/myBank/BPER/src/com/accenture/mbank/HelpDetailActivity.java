
package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.accenture.mbank.model.HelpItemListModel;

public class HelpDetailActivity extends BaseActivity {

    LinearLayout container;

    public static final String ITEM = "item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 

        setContentView(R.layout.help_detail_layout_);
        container = (LinearLayout)findViewById(R.id.help_detail_container);
		ImageButton help_btn = (ImageButton) findViewById(R.id.help_btn);
		help_btn.setVisibility(View.INVISIBLE);
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

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        container.addView(helpDetailItem, params);
    }

    protected void onBackClick() {

        onBackPressed();
    }
}
