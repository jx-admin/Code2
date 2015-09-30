
package com.act.mbanking.utils;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.act.mbanking.App;
import com.act.mbanking.ChartModelManager;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.BaseActivity;
import com.act.mbanking.activity.LoginActivity;
import com.act.mbanking.bean.TableContentList;

public class DialogManager {
    public static Dialog createMessageDialog(final String message, final Context context) {

        final Dialog alertDialog;

        alertDialog = new Dialog(context, R.style.selectorDialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.message_dialog_layout,
                null);

        alertDialog.setContentView(linearLahyout);

        Button imageButton = (Button)linearLahyout.findViewById(R.id.ok_btn);
        TextView text = (TextView)linearLahyout.findViewById(R.id.message_text);
        text.setText(message);
        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                if (message.equals("Not valid token") || message.equals("Missing sessionId")) {
                    if (context instanceof BaseActivity) {
                        BaseActivity baseActivity = (BaseActivity)context;
                        baseActivity.finish();
                    }
                }
            }
        });
        // alertDialog.show();
        setDialogWidth(alertDialog, context);

        return alertDialog;

    }

    private static void setDialogWidth(final Dialog alertDialog, Context context) {
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()) * 3 / 4; // 设置宽度
        // lp.height = (int)(display.getWidth()) * 2 / 4;
        alertDialog.getWindow().setAttributes(lp);
    }

    public static Dialog createMessageExitDialog(String message, final Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(
                R.layout.exit_message_dialog_layout, null);

        final Dialog dialog = new Dialog(context, R.style.selectorDialog);
        dialog.setContentView(R.layout.exit_message_dialog_layout);
        Button yesButton = (Button)dialog.findViewById(R.id.yes_btn);
        Button noButton = (Button)dialog.findViewById(R.id.no_btn);
        TextView text = (TextView)dialog.findViewById(R.id.exit_message_text);
        text.setText(message);
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
                Contants.clearAll();
                ChartModelManager.logout();
                App.app.logOut();
                Intent intent = new Intent(((Activity)context), LoginActivity.class);
                ((Activity)context).startActivity(intent);
                ((Activity)context).finish();
                // System.exit(0);
            }
        });
        // alertDialog.show();
//        setDialogWidth(dialog, context);

        return dialog;

    }
    

    public static AlertDialog createHoursDialog(Context context, OnItemClickListener listener,
            List<TableContentList> tableContentLists) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_singlechoice);
        for (TableContentList tableContent : tableContentLists) {
            adapter.add(tableContent.getDescription());
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
}
