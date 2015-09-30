
package com.accenture.mbank;

import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.accenture.mbank.logic.AdvNewsJson;
import com.accenture.mbank.model.AdvNewsResponseModel;
import com.accenture.mbank.model.ListAdvNewsModel;
import com.accenture.mbank.net.AsyncImageLoader;
import com.accenture.mbank.net.AsyncImageLoader.ImageCallback;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;

public class Ad_InfoActivity extends BaseActivity {
    private TextView title_TextView;

    private TextView advNewsTitle;

    private ImageView advNewsImg;

    private TextView advNewsContent;

    public Handler handler;

    private AsyncImageLoader imageLoader = new AsyncImageLoader();

//    private List<ListAdvNewsModel> listAdvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ad_info);

        title_TextView = (TextView)findViewById(R.id.title_text);
        title_TextView.setText(R.string.advertising_info);

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
    
    protected void onBackClick() {
        super.onBackClick();
        finish();
    }
}
