
package com.accenture.mbank.util;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.LoginActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.MobileBankApplication;
import com.accenture.mbank.model.TableContentList;

public class DialogManager {

	/**
	 * @param eventManagement
	 * @param msgId
	 * @param context
	 * @return
	 */
	public static AlertDialog displayErrorMessage(final com.accenture.mbank.logic.EventManagement eventManagement,int msgId,final Context context) {
		int erroId=0;
		if(eventManagement!=null&&!TextUtils.isEmpty(eventManagement.getErrorCode())){
			erroId=Integer.parseInt(eventManagement.getErrorCode());
		}
		switch(erroId){
		case 91400:
		case 91402:
			return createMessageDialog(R.string.session_expired, null, context, new OnViewClickListener(){

				@Override
				public void onClick(View v) {
					super.onClick(v);
					LoginActivity.logout(context);
				}}, null);
		case 90000:
		default:
			if (context instanceof MainActivity) {
				final MainActivity mainActivity = (MainActivity)context;
				mainActivity.showTab(0);
			}
			if(msgId>0){
				return createMessageDialog(msgId,context);
			}else{
				return createMessageDialog(R.string.service_unavailable,context);
			}
//			return createMessageDialog(eventManagement.getErrorDescription(),context);
		}
	}
	public static AlertDialog displayErrorMessage(final com.accenture.mbank.logic.EventManagement eventManagement,int msgId,final Context context,int negateveId,int positiveId ) {
		int erroId=0;
		if(eventManagement!=null&&!TextUtils.isEmpty(eventManagement.getErrorCode())){
			erroId=Integer.parseInt(eventManagement.getErrorCode());
		}
		switch(erroId){
		case 91400:
		case 91402:
			return createMessageDialog(R.string.session_expired, null, context, negateveId, positiveId ,new OnViewClickListener(){

				@Override
				public void onClick(View v) {
					super.onClick(v);
					LoginActivity.logout(context);
				}}, null);
		case 90000:
		default:
			if (context instanceof MainActivity) {
				final MainActivity mainActivity = (MainActivity)context;
				mainActivity.showTab(0);
			}
			if(msgId>0){
				return createMessageDialog(msgId,"",context,negateveId, positiveId,null,null);
			}else{
				return createMessageDialog(R.string.service_unavailable,"",context,negateveId, positiveId,null,null);
			}
//			return createMessageDialog(eventManagement.getErrorDescription(),context);
		}
	}
	
	/**
	 * @param eventManagement
	 * @param msgId
	 * @param context
	 * @return
	 */
	public static AlertDialog displayErrorMessage(final com.accenture.mbank.model.EventManagement eventManagement,int msgId,final Context context) {
		int erroId=0;
		if(eventManagement!=null&&!TextUtils.isEmpty(eventManagement.getErrorCode())){
			erroId=Integer.parseInt(eventManagement.getErrorCode());
		}
		switch(erroId){
		case 91400:
		case 91402:
			return createMessageDialog(R.string.session_expired, null, context, new OnViewClickListener(){

				@Override
				public void onClick(View v) {
					super.onClick(v);
					LoginActivity.logout(context);
				}}, null);
		case 90000:
		default:
			if (context instanceof MainActivity) {
				final MainActivity mainActivity = (MainActivity)context;
				mainActivity.showTab(0);
			}
			return createMessageDialog(R.string.service_unavailable,context);
//			return createMessageDialog(eventManagement.getErrorDescription(),context);
		}
	}
	/**
	 * @param eventManagement
	 * @param msgId
	 * @param context
	 * @return
	 */
	public static AlertDialog displayErrorMessage(final com.accenture.mbank.model.EventManagement eventManagement,int msgId,final Context context,int negateveId,int positiveId) {
		int erroId=0;
		if(eventManagement!=null&&!TextUtils.isEmpty(eventManagement.getErrorCode())){
			erroId=Integer.parseInt(eventManagement.getErrorCode());
		}
		switch(erroId){
		case 91400:
		case 91402:
			return createMessageDialog(R.string.session_expired, null, context,negateveId,positiveId, new OnViewClickListener(){

				@Override
				public void onClick(View v) {
					super.onClick(v);
					LoginActivity.logout(context);
				}}, null);
		case 90000:
		default:
			if (context instanceof MainActivity) {
				final MainActivity mainActivity = (MainActivity)context;
				mainActivity.showTab(0);
			}
			return createMessageDialog(R.string.service_unavailable,"",context,negateveId,positiveId,null,null);
//			return createMessageDialog(eventManagement.getErrorDescription(),context);
		}
	}
    


    /**
     * @param message
     * @param context
     * @return
     */
    public static AlertDialog createMessageDialog(final int message, final Context context) {
        
    	return createMessageDialog(message,null,context,null,null);

    }

    /**
     * @param message
     * @param context
     * @return
     */
    public static AlertDialog createMessageDialog(final String message, final Context context) {
        return createMessageDialog(0,message,context,null,null);
    }

    /**
     * @param msgId if user must >0
     * @param message be used when msgId<=0
     * @param context
     * @param onNegativeClickListener
     * @param onPositiveClickListenr
     * @return
     */
    public static AlertDialog createMessageDialog(final int msgId,final String message, final Context context,final OnClickListener onNegativeClickListener,final OnClickListener onPositiveClickListenr) {
    	return createMessageDialog(msgId, message, context, -1, -1, onNegativeClickListener, onPositiveClickListenr);
    }
    
    /**
     * @param msgId if user must >0
     * @param message be used when msgId<=0
     * @param context 
     * @param negateveId  be used when >0
     * @param positiveId  be used when >0
     * @param onNegativeClickListener
     * @param onPositiveClickListenr
     * @return
     */
    public static AlertDialog createMessageDialog(final int msgId,final String message, final Context context,int negateveId,int positiveId,final OnClickListener onNegativeClickListener,final OnClickListener onPositiveClickListenr) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.message_dialog_layout, null);

        builder.setView(linearLahyout);
        alertDialog = builder.create();

        final Button imageButton = (Button)linearLahyout.findViewById(R.id.ok_btn);
        if(negateveId>0){
    		imageButton.setText(negateveId);
    	}
        TextView text = (TextView)linearLahyout.findViewById(R.id.message_text);
        if(msgId>0){
        	text.setText(msgId);
        }else if(message!=null){
        	text.setText(message);
        }else{
        }
        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if(onPositiveClickListenr!=null){
                	onPositiveClickListenr.onClick(imageButton);
                }else if(onNegativeClickListener!=null){
                	onNegativeClickListener.onClick(imageButton);
                }
                
            }
        });
        if(onNegativeClickListener!=null&&onPositiveClickListenr!=null){
        	if(positiveId>0){
        		imageButton.setText(positiveId);
        	}
        	 final Button imageCancelButton = (Button)linearLahyout.findViewById(R.id.cancel_btn);
        	 if(negateveId>0){
        		 imageCancelButton.setText(negateveId);
        	 }
        	 imageCancelButton.setVisibility(View.VISIBLE);
        	 imageCancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if(onNegativeClickListener!=null){
                	onNegativeClickListener.onClick(imageCancelButton);
                }
                
            }
        });
        }
        
        // alertDialog.show();
        setDialogWidth(alertDialog, context, linearLahyout);

        return alertDialog;

    }
	
    public static AlertDialog createHoursDialog(Context context, OnItemClickListener listener,
            List<TableContentList> tableContentLists) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_singlechoice);
        if(tableContentLists==null){
        }else{
        	for (TableContentList tableContent : tableContentLists) {
        		adapter.add(tableContent.getDescription());
        	}
        }
        listView.setAdapter(adapter);
        listView.setCacheColorHint(0x00000000);
        listView.setBackgroundDrawable(null);
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemClickListener(listener);
        builder.setView(listView);
        final AlertDialog alertDialog = builder.create();
        setDialogWidth(alertDialog, context, listView);
        return alertDialog;
    }

    public static AlertDialog createMessageDialog(final String errorCode,final String message, final Context context) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.message_dialog_layout,
                null);

        builder.setView(linearLahyout);
        alertDialog = builder.create();

        Button imageButton = (Button)linearLahyout.findViewById(R.id.ok_btn);
        TextView text = (TextView)linearLahyout.findViewById(R.id.message_text);
        text.setText(message);
        if (errorCode.equals(Contants.ERR_SESSION_EXPIRED_1) || errorCode.equals(Contants.ERR_SESSION_EXPIRED_2)) {
            text.setText(R.string.session_expired);
            alertDialog.setCancelable(false);
        }
        if (errorCode.equals(Contants.ERR_GENERIC_ERROR)) {
            text.setText(R.string.service_unavailable);
            alertDialog.setCancelable(false);
        }
        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                System.out.println("onclick+===========================");
                alertDialog.dismiss();
                if (errorCode.equals(Contants.ERR_SESSION_EXPIRED_1) || errorCode.equals(Contants.ERR_SESSION_EXPIRED_2)) {
                    if (context instanceof BaseActivity) {
                        final BaseActivity baseActivity = (BaseActivity)context;
                        Intent intent = new Intent(baseActivity, LoginActivity.class);
                        baseActivity.startActivity(intent);
                        MobileBankApplication.logOut();

                        baseActivity.isFinished = true;

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                baseActivity.finish();
                            }
                        }, 3000);

                    }
                }
                
                if (errorCode.equals(Contants.ERR_GENERIC_ERROR) ) {
                	if (context instanceof MainActivity) {
                		final MainActivity mainActivity = (MainActivity)context;
                		mainActivity.showTab(0);
                	}
                }
                
            }
        });
        // alertDialog.show();
        setDialogWidth(alertDialog, context, linearLahyout);

        return alertDialog;

    }

    public static AlertDialog createMessageExitDialog(String message, final Context context) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(
                R.layout.exit_message_dialog_layout, null);

        builder.setView(linearLahyout);
        alertDialog = builder.create();

        Button yesButton = (Button)linearLahyout.findViewById(R.id.yes_btn);
        Button noButton = (Button)linearLahyout.findViewById(R.id.no_btn);
        TextView text = (TextView)linearLahyout.findViewById(R.id.exit_message_text);
        text.setText(message);
        noButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Contants.logOut(); // lyandyjoe
                Contants.clearAll();
                Intent intent = new Intent(((Activity)context), LoginActivity.class);
                ((Activity)context).startActivity(intent);
                BaseActivity.Logout(context);
            }
        });
        // alertDialog.show();
        setDialogWidth(alertDialog, context, linearLahyout);

        return alertDialog;

    }

    private static void setDialogWidth(final AlertDialog alertDialog, Context context, View v) {
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        // lp.width = (int)(display.getWidth()) * 3 / 4; // 设置宽度
        // lp.height = (int)(display.getWidth()) * 2 / 4;
        int width = (int)(display.getWidth()) * 3 / 4;
        int height = (int)(display.getHeight());
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        v.measure(widthMeasureSpec, heightMeasureSpec);

        int pad = (width - v.getMeasuredWidth()) / 2;

        v.setPadding(pad, 0, pad, 0);
        alertDialog.getWindow().setAttributes(lp);
    }

    public static interface OnButtonListener {
        public void onButtonClick();
    }

    public static interface OnPayeeSelectListener {

        public void onPayeeSelected(int index);
    }

}
