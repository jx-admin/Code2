package com.accenture.mbank;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.util.Contants;

public class MsgDialgActivty extends Activity {
	protected void onStart() {
		super.onStart();
		Intent i=getIntent();
		String title=i.getStringExtra("title");
		String content=i.getStringExtra("content");
		  if (Contants.publicModel == null||Contants.publicModel.sessionId==null||Contants.publicModel.sessionId.equals("")) {

				Contants.msgTitle=title;
				Contants.msgContext=content;
				Intent notificationIntent = new Intent(this, LoginActivity.class);
				startActivity(notificationIntent);
				finish();
		  }else{
			  notificationMessageDialog(content);
		  }
	}
    
    private void notificationMessageDialog(String message) {
        final Dialog alertDialog;
        alertDialog = new Dialog(this, R.style.selectorDialog);
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.message_dialog_layout,
                null);
        alertDialog.setContentView(linearLahyout);
        Button imageButton = (Button)linearLahyout.findViewById(R.id.ok_btn);
        TextView text = (TextView)linearLahyout.findViewById(R.id.message_text);
        text.setText(message);
        alertDialog.show();
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
        		finish();
            }
        });
    }

}
