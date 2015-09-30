
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.custom.view.CircleLinearView;

public class Level3CircleManager extends MainMenuSubScreenManager {
	 LayoutInflater lf;
	 List itemDataLs=new ArrayList();
     
    public Level3CircleManager(MainActivity activity) {
        super(activity);
    }

    @Override
    protected void init() {

        lf=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = (ViewGroup)super.activity.findViewById(R.id.level3_circle);
        //create a LineView
        cv=(CircleLinearView)layout.findViewById(R.id.content_cirlin);
//        testLinebutton(activity);
        ViewGroup v=(ViewGroup)layout.findViewById(R.id.time_selector);
        TimeSelectorManager tsm=new TimeSelectorManager(activity, v);
        
        tsm.generateDefaultVerticalTimes();

        tsm.generateButton();
        tsm.setSelected(0);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void setUI() {

    }
    
    public void onShow(Object object){
    	setTitle("LAccount' account");
    	setSubTitle("avilable banlance subtitle");
    	int count=itemDataLs.size();
        for(int i=0;i>count;i++){
        	
        }
        
        
        android.view.ViewGroup.LayoutParams chileLayoutLp=new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        for(int i=0;i<2;i++){//add ChileView
            cv.addView(CircleLinearViewUtils.createItemView(lf,chileLayoutLp,Gravity.CENTER,"Title"+i,"$122323423.34"));
        }
        LinearLayout BottomLin=new LinearLayout(activity);
        BottomLin.setGravity(Gravity.CENTER);
        BottomLin.setLayoutParams(chileLayoutLp);
        android.view.ViewGroup.LayoutParams chileLayoutLp2=new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        BottomLin.addView(CircleLinearViewUtils.createItemView(lf,chileLayoutLp2,Gravity.RIGHT|Gravity.CENTER_VERTICAL,"Title L","Content Left"));
        ImageView diviImg=new ImageView(activity);
        diviImg.setScaleType(ScaleType.FIT_XY);
        diviImg.setImageResource(R.drawable.ic_launcher);
        android.widget.LinearLayout.LayoutParams dividerLp=new android.widget.LinearLayout.LayoutParams(2, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        dividerLp.setMargins(4, 0, 4, 0);
        BottomLin.addView(diviImg,dividerLp);
        BottomLin.addView(CircleLinearViewUtils.createItemView(lf,chileLayoutLp2,Gravity.LEFT|Gravity.CENTER_VERTICAL,"Title R","Content Right"));
        cv.addView(BottomLin);
        
        Button sureBtn=new Button(activity);
        sureBtn.setText("Detaile");
        cv.addView(sureBtn);
    }
    
    public void setTitle(String title){
        ((TextView)layout.findViewById(R.id.layout_title_tv)).setText(title);
    }
    
    public void setSubTitle(String subtitle){
        ((TextView)layout.findViewById(R.id.layout_subtitle_tv)).setText(subtitle);
    }
    
    public void setItemContent(int id,String title,String content){
    	
    }
    private void testLinebutton(Context context){
        
        //create a LineView
        CircleLinearView cv=(CircleLinearView)layout.findViewById(R.id.content_cirlin);
        
        android.view.ViewGroup.LayoutParams chileLayoutLp=new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        for(int i=0;i<2;i++){//add ChileView
            cv.addView(CircleLinearViewUtils.createItemView(lf,chileLayoutLp,Gravity.CENTER,"Title"+i,"$122323423.34"));
        }
        LinearLayout BottomLin=new LinearLayout(context);
        BottomLin.setGravity(Gravity.CENTER);
        BottomLin.setLayoutParams(chileLayoutLp);
        android.view.ViewGroup.LayoutParams chileLayoutLp2=new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        BottomLin.addView(CircleLinearViewUtils.createItemView(lf,chileLayoutLp2,Gravity.RIGHT|Gravity.CENTER_VERTICAL,"Title L","Content Left"));
        ImageView diviImg=new ImageView(context);
        diviImg.setScaleType(ScaleType.FIT_XY);
        diviImg.setImageResource(R.drawable.ic_launcher);
        android.widget.LinearLayout.LayoutParams dividerLp=new android.widget.LinearLayout.LayoutParams(2, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        dividerLp.setMargins(4, 0, 4, 0);
        BottomLin.addView(diviImg,dividerLp);
        BottomLin.addView(CircleLinearViewUtils.createItemView(lf,chileLayoutLp2,Gravity.LEFT|Gravity.CENTER_VERTICAL,"Title R","Content Right"));
        cv.addView(BottomLin);
        
        Button sureBtn=new Button(context);
        sureBtn.setText("Detaile");
        cv.addView(sureBtn);
    }
   
    
}
