
package com.act.mbanking.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.bean.AdvNewsResponseModel;
import com.act.mbanking.logic.AdvNewsJson;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        initdata();
    }

    /**
     * 加载广告数据
     */
    private void initdata() {
        ProgressOverlay progressOverlay = new ProgressOverlay(this);
        progressOverlay.runBackground(new OnProgressEvent() {
            @Override
            public void onProgress() {
                // 广告新闻
                String postData = AdvNewsJson.AdvNewsReportProtocal(Contants.publicModel,
                        Contants.abi);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        SplashScreenActivity.this);
                final AdvNewsResponseModel advNewResponse = AdvNewsJson
                        .ParseAdvNewsResponse(httpResult);
                if (advNewResponse != null && advNewResponse.responsePublicModel.isSuccess()) {
                    // 判断如果加载成功 跳转界面
                    Contants.advNewsList = advNewResponse.getListAdvNews();
                    loadAdvImage();
                    skipScreen();
                } else if (advNewResponse != null && !advNewResponse.responsePublicModel.isSuccess()) {
                    displayErrorMessage(advNewResponse.responsePublicModel.eventManagement.getErrorDescription());
                } else {
                    Contants.advNewsList = advNewResponse.getListAdvNews();
                }
            }
        });
    }

    public void loadAdvImage() {
        HttpConnector httpConnector = new HttpConnector();
        try {
            Bitmap imageRef = BitmapFactory.decodeStream(httpConnector
                    .getImageStream(Contants.advNewsList.get(0).getImageRef()));
            if (imageRef != null) {
                Contants.advImageRef = imageRef;
            } else {
                
            }
            
            Bitmap imageRefThumb = BitmapFactory.decodeStream(httpConnector
                    .getImageStream(Contants.advNewsList.get(0).getImageRefThumb()));
            if (imageRef != null) {
                Contants.advImageRefThumb = imageRefThumb;
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转界面
     */
    private void skipScreen() {
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
