package com.aess.aemm.view;

import com.aess.aemm.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationUtils;

public class TextFlipper extends MessageFlipper{
	private Context context;
	
	public TextFlipper(Context context) {
        super(context);
        this.context=context;
        setAnnimationKind(context,0);
	}
	
    public TextFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
		setAnnimationKind(context,0);
        this.context=context;
    }

	public void setAnnimationKind(Context context,int position){
		   switch (position) {
	        case 0:
	            setInAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.push_up_in));
	            setOutAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.push_up_out));
	            break;
	        case 1:
	            setInAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.push_left_in));
	            setOutAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.push_left_out));
	            break;
	        case 2:
	            setInAnimation(AnimationUtils.loadAnimation(context,
	                    android.R.anim.fade_in));
	            setOutAnimation(AnimationUtils.loadAnimation(context,
	                    android.R.anim.fade_out));
	            break;
	        default:
	            setInAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.hyperspace_in));
	            setOutAnimation(AnimationUtils.loadAnimation(context,
	                    R.anim.hyperspace_out));
	            break;
	        }
	}
	/**
	 * <TextView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:gravity="center_horizontal"
                        android:textSize="26sp"
                        android:text="@string/animation_2_text_3"/>
	 */
	public void addMessage(HallMessagedb msg){
		MessageView mv;
		for(int i=0;i<getChildCount();i++){
			Log.v("VV","i:"+i+msg.getMessage());
			mv=(MessageView) getChildAt(i);
			if(mv.message.getKind()==msg.getKind()){
				mv.setMessage(msg);
				msg=null;
				break;
			}
		}
		if (msg != null) {
			mv = new MessageView(context,msg);
//			LayoutParams lp = new LayoutParams((int) (context.getResources()
//					.getDimension(R.dimen.message_w)), (int) (context
//					.getResources().getDimension(R.dimen.message_h)));
//			lp.gravity = Gravity.CENTER_VERTICAL;
			mv.setId(msg.getKind());
			mv.setBackgroundColor(0xff);
			this.addView(mv);
		}
	}
}
