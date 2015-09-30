package com.act.mbanking.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.HelpItemListModel;

public class GuideDetailManager extends MainMenuSubScreenManager {
    LinearLayout container;
    
	public GuideDetailManager(MainActivity activity) {
		super(activity);
	}

	@Override
	protected void init() {
        layout = (ViewGroup)activity.findViewById(R.id.guidedetail);
        
        container = (LinearLayout)layout.findViewById(R.id.help_detail_container);
        
        setLeftNavigationText(activity.getResources().getString(R.string.guide));
	}
	
    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        mainManager.showGuide(true, null);
        return true;
    }
	
	@Override
    protected void onShow(Object object) {
		HelpItemListModel helpItemListModel = (HelpItemListModel) object;
        if (helpItemListModel == null) {
            offLineTest();
        } else {
            addText(helpItemListModel.getTitle(), helpItemListModel.getText());
        }
    };

	@Override
	protected void loadData() {

	}

	@Override
	protected void setUI() {

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

        LayoutInflater inflater = LayoutInflater.from(activity);

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
}
