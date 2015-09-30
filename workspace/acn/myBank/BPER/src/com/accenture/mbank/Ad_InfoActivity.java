
package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.accenture.mbank.net.AsyncImageLoader;
import com.accenture.mbank.util.Contants;

public class Ad_InfoActivity extends BaseActivity {
    private TextView advNewsTitle;

    private ImageView advNewsImg;

    private TextView advNewsContent;

    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_info);
        int count = getIntent().getIntExtra("INDEX", 0);
		ImageButton help_btn = (ImageButton) findViewById(R.id.help_btn);
		help_btn.setVisibility(View.INVISIBLE);

        advNewsTitle = (TextView)findViewById(R.id.news_title);
        advNewsImg = (ImageView)findViewById(R.id.news_img);
        advNewsContent = (TextView)findViewById(R.id.news_content);
        handler = new Handler();
        if (Contants.advNewsList == null || Contants.advNewsList.size() == 0) {
        	 advNewsTitle.setText(R.string.default_adv);
        } else {
            advNewsTitle.setText(Contants.advNewsList.get(count).getTitle());
            advNewsContent.setText(Contants.advNewsList.get(count).getText());
            if (Contants.advNewsList.get(count).get_imageRefThumb() != null) {
                advNewsImg.setImageBitmap(Contants.advNewsList.get(count).get_imageRefThumb());
            }else if(Contants.advNewsList.get(count).get_imageRef() !=null){
            	advNewsImg.setImageBitmap(Contants.advNewsList.get(count).get_imageRef());
            }
        }
        advNewsImg.invalidate();
    }
    
    protected void onBackClick() {
        super.onBackClick();
        finish();
    }
}
