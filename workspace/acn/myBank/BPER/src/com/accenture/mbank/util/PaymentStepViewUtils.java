package com.accenture.mbank.util;

import it.gruppobper.ams.android.bper.R;
import android.view.View;

public class PaymentStepViewUtils {
	public enum Step {  
		STEP1,STEP2,STEP3  
	} 

	View[]stepViews;
	public View view,step1View,step2View,step3View;
	int currenStep=-1;
	public PaymentStepViewUtils(View view){
		this.view=view;
		step1View=view.findViewById(R.id.imageView1);
		step2View=view.findViewById(R.id.imageView2);
		step3View=view.findViewById(R.id.imageView3);
	}
	public void setStep(Step step){
		switch (step) {
		case STEP3:
			step3View.setVisibility(View.VISIBLE);
			step2View.setVisibility(View.GONE);
			step1View.setVisibility(View.GONE);
			break;
		case STEP2:
			step3View.setVisibility(View.GONE);
			step2View.setVisibility(View.VISIBLE);
			step1View.setVisibility(View.GONE);
			break;
		case STEP1:
			step3View.setVisibility(View.GONE);
			step2View.setVisibility(View.GONE);
			step1View.setVisibility(View.VISIBLE);
			break;
		default:
			step3View.setVisibility(View.GONE);
			step2View.setVisibility(View.GONE);
			step1View.setVisibility(View.GONE);
			break;
		}
	}
	

}
