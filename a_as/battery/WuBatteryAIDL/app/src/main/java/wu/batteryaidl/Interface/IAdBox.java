package wu.batteryaidl.Interface;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by lj on 15-12-24.
 */
public interface IAdBox {
    public String getBoxImage();

    public String getBoxTitle();

    public String getBoxIcon();

    public String getBoxText();

    public long getBoxRequestTime();

    public void setBoxRequestTime(long time);//设置为过期

    public String getPackageName();

    public String getBoxAdCallToAction();

    public boolean boxIsExpired();

    public void boxUnregisterView();

    public void boxRegisterViewForInteraction(View view);

    public void boxRegisterViewForInteraction(View view, ArrayList<View> list);

    //public void boxSetOliAdListener(INativeAd.OliAdListener listener);

    public void boxdestroy();


    //public BitmapUtils getBitmapUtils();

    public void callback(IAdBoxCallback listener);

    public interface IAdBoxCallback {
        public void onFinish();
        public void onError();
    }

}

