
package com.act.mbanking.activity;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Ad_InfoActivity extends NavigationActivity {
    private TextView advNewsTitle;

    private ImageView advNewsImg;

    private TextView advNewsContent;

    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ad_info);
        
        setLeftNavigationText(getString(R.string.back));

        advNewsTitle = (TextView)findViewById(R.id.news_title);
        advNewsImg = (ImageView)findViewById(R.id.news_img);
        advNewsContent = (TextView)findViewById(R.id.news_content);
        handler = new Handler();
        if (Contants.advNewsList == null || Contants.advNewsList.size() == 0) {
            
        } else {
            advNewsTitle.setText(Contants.advNewsList.get(0).getTitle());
            advNewsContent.setText(Contants.advNewsList.get(0).getText());
            if (Contants.advImageRefThumb != null) {
                advNewsImg.setImageBitmap(Contants.advImageRefThumb);
            }else{
                advNewsImg.setImageResource(R.drawable.adv);
            }
        }
    }
    
    protected void onLeftNavigationClick(View v) {
    	finish();
    }
}
