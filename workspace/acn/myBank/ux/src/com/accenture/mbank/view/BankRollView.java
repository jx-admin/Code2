
package com.accenture.mbank.view;

import static com.accenture.mbank.CommonUtilities.SENDER_ID;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gcm.GCMRegistrar;

/**
 * 该类是一个球，点show会展开
 * 
 * @author seekting.x.zhang
 */
public class BankRollView extends LinearLayout implements ShowAble, View.OnClickListener {

    private ImageButton show, close;

    private View content;

    /**
     * @return the content
     */
    public View getContent() {
        return content;
    }

    public int height_top;

    public int height_bottom;

    private ShowListener showListener;
    
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;

    public BankRollView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void init() {
        show = (ImageButton)findViewById(R.id.show_img);
        close = (ImageButton)findViewById(R.id.close_img);
        show.setOnClickListener(this);
        close.setOnClickListener(this);
        height_top = (int)(BaseActivity.cycle_top_height * BaseActivity.screen_height);

        height_bottom = (int)(height_top * BaseActivity.cycle_bottom_height);
        LayoutParams layoutParams = (LayoutParams)show.getLayoutParams();
        layoutParams.height = height_bottom;

        LayoutParams layoutParams1 = (LayoutParams)close.getLayoutParams();

        layoutParams1.height = height_top;

    }

    public void setShowImage(int id) {
    	if(R.drawable.btn_investment_show_selector == id){
    	  mGaInstance = GoogleAnalytics.getInstance(getContext());
          mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
          mGaTracker1.sendView("event.investments.deposit");  
          mGaTracker1.sendView("event.investments.asset");
    	}
        show.setImageResource(id);
    }

    public void setCloseImage(int id) {

        close.setImageResource(id);
    }

    public void setContent(View v) {

        this.removeAllViews();
        this.addView(close);
        this.addView(v);
        this.addView(show);
        this.content = v;
        v.setVisibility(View.GONE);
        requestLayout();
    }

    @Override
    public void show() {   	
        if (content != null) {
            content.setVisibility(View.VISIBLE);
            if (content.getVisibility() == View.GONE) {
            } else {
         
                show.setVisibility(View.GONE);
            }
        }
        if (showListener != null) {
            showListener.onShow(this);
        }
    }

    @Override
    public void close() {
        if (content != null) {
            content.setVisibility(View.GONE);
            show.setVisibility(View.VISIBLE);
        }
    }
    protected void notification() {
        final SharedPreferences sp = getContext().getSharedPreferences(Contants.SETTING_FILE_NAME,
                Activity.MODE_PRIVATE);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(
                R.layout.exit_message_dialog_layout, null);

        final Dialog dialog = new Dialog(getContext(), R.style.selectorDialog);
        dialog.setContentView(linearLahyout);
        Button yesButton = (Button)dialog.findViewById(R.id.yes_btn);
        Button noButton = (Button)dialog.findViewById(R.id.no_btn);
        TextView text = (TextView)dialog.findViewById(R.id.exit_message_text);
        text.setText(R.string.is_receive_notifictions);
        dialog.show();
        noButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                show();
                Contants.updataInitSetting(sp.edit(), true);
                registerGCM();
                DialogManager.createMessageDialog(getContext().getResources().getString(R.string.notifictions_confirmation),getContext()).show();
            }
        });
    }
    private void registerGCM() {
        GCMRegistrar.checkDevice(getContext());
        GCMRegistrar.checkManifest(getContext());
        final String regId = GCMRegistrar.getRegistrationId(getContext());
        if (regId.equals("")) {
            GCMRegistrar.register(getContext(), SENDER_ID);
        } else {
            Log.v("Test GCM", "Already registered" + regId);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == show) {
        	if(this.equals(BankRollContainer.pushNotificationRollView)){
        		SharedPreferences sp = getContext().getSharedPreferences(Contants.SETTING_FILE_NAME, Activity.MODE_PRIVATE);
                boolean isInitPushNotification = Contants.getInitSetting(sp);
                if (!isInitPushNotification) {
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
                            notification();
//                        }
//                    });
                            return;
                }
        	}
            show();

        } else if (v == close) {
            close();
        }

    }

    public ShowListener getShowListener() {
        return showListener;
    }

    public void setShowListener(ShowListener showListener) {
        this.showListener = showListener;
    }

}
