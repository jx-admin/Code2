package com.xuye.AemmEnableDisable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.util.Log;
import android.view.View.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AemmEnableDisable extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button01 = (Button)findViewById(R.id.button1);
        button01.setOnClickListener(this);
        button02 = (Button)findViewById(R.id.button2);
        button02.setOnClickListener(this);
        button03 = (Button)findViewById(R.id.button3);
        button03.setOnClickListener(this);
        button04 = (Button)findViewById(R.id.button4);
        button04.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.edittext);
        button05 = (Button)findViewById(R.id.button5);
        button05.setOnClickListener(this);
        button06 = (Button)findViewById(R.id.button6);
        button06.setOnClickListener(this);
        button06 = (Button)findViewById(R.id.button6);
        button06.setOnClickListener(this);
        button_enable_install = (Button)findViewById(R.id.button_enable_install);
        button_enable_install.setOnClickListener(this);
        button_disable_install = (Button)findViewById(R.id.button_disable_install);
        button_disable_install.setOnClickListener(this);
        button_enable_mms = (Button)findViewById(R.id.button_enable_mms);
        button_enable_mms.setOnClickListener(this);
        button_disable_mms = (Button)findViewById(R.id.button_disable_mms);
        button_disable_mms.setOnClickListener(this);
        button_enable_sms = (Button)findViewById(R.id.button_enable_sms);
        button_enable_sms.setOnClickListener(this);
        button_disable_sms = (Button)findViewById(R.id.button_disable_sms);
        button_disable_sms.setOnClickListener(this);
        button_add_wifi = (Button)findViewById(R.id.button_add_wifi);
        button_add_wifi.setOnClickListener(this);
        button_delete_wifi = (Button)findViewById(R.id.button_delete_wifi);
        button_delete_wifi.setOnClickListener(this);
        button_enable_audio = (Button)findViewById(R.id.button_enable_audio);
        button_enable_audio.setOnClickListener(this);
        button_disable_audio = (Button)findViewById(R.id.button_disable_audio);
        button_disable_audio.setOnClickListener(this);
        button_enable_video = (Button)findViewById(R.id.button_enable_video);
        button_enable_video.setOnClickListener(this);
        button_disable_video = (Button)findViewById(R.id.button_disable_video);
        button_disable_video.setOnClickListener(this);
        button_enable_application = (Button)findViewById(R.id.button_enable_application);
        button_enable_application.setOnClickListener(this);
        button_disable_application = (Button)findViewById(R.id.button_disable_application);
        button_disable_application.setOnClickListener(this);
        button_add_apn = (Button)findViewById(R.id.button_add_apn);
        button_add_apn.setOnClickListener(this);
        button_delete_apn = (Button)findViewById(R.id.button_delete_apn);
        button_delete_apn.setOnClickListener(this);
        button_add_vpn = (Button)findViewById(R.id.button_add_vpn);
        button_add_vpn.setOnClickListener(this);
        button_delete_vpn = (Button)findViewById(R.id.button_delete_vpn);
        button_delete_vpn.setOnClickListener(this);
        button_add_email = (Button)findViewById(R.id.button_add_email);
        button_add_email.setOnClickListener(this);
        button_delete_email = (Button)findViewById(R.id.button_delete_email);
        button_delete_email.setOnClickListener(this);
    }


    Button button01;
    Button button02;
    Button button03;
    Button button04;
    EditText editText;
    Button button05;
    Button button06;
    Button button_enable_install;
    Button button_disable_install;
    Button button_enable_mms;
    Button button_disable_mms;
    Button button_enable_sms;
    Button button_disable_sms;
    Button button_add_wifi;
    Button button_delete_wifi;
    Button button_enable_audio;
    Button button_disable_audio;
    Button button_enable_video;
    Button button_disable_video;
    Button button_enable_application;
    Button button_disable_application;
    Button button_add_apn;
    Button button_delete_apn;
    Button button_add_vpn;
    Button button_delete_vpn;
    Button button_add_email;
    Button button_delete_email;
    @Override
    public void onClick(View button) {
        if(button == button01) { // enable camera take picture
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmcamera");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x00;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button02) { //disable camera take picture
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmcamera");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x01;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button03) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmdial");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x00;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button04) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmdial");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x01;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button05) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmcomponent");
            ContentValues values = new ContentValues();
            //values.put("Rule", mRuleCount ++);
            values.put("Rule", editText.getText().toString());
            getContentResolver().insert(uri, values);
        } else if(button == button06) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmcomponent");
            //String[] selection = new String[1];
            Log.i(TAG, editText.getText().toString());
            getContentResolver().delete(uri, editText.getText().toString(), null);
        } else if(button == button_enable_install) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmapk");
            ContentValues values = new ContentValues();
            //values.put("Rule", mRuleCount ++);
            values.put("Rule", editText.getText().toString());
            getContentResolver().insert(uri, values);
        } else if(button == button_disable_install) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmapk");
            //String[] selection = new String[1];
            Log.i(TAG, editText.getText().toString());
            getContentResolver().delete(uri, editText.getText().toString(), null);
        } else if(button == button_enable_application) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmapp");
            ContentValues values = new ContentValues();
            //values.put("Rule", mRuleCount ++);
            values.put("Rule", editText.getText().toString());
            getContentResolver().insert(uri, values);
        } else if(button == button_disable_application) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmapp");
            //String[] selection = new String[1];
            Log.i(TAG, editText.getText().toString());
            getContentResolver().delete(uri, editText.getText().toString(), null);
        } else if(button == button_enable_mms) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmmms");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x00;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button_disable_mms) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmmms");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x01;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button_enable_sms) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmsms");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x00;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button_disable_sms) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmsms");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x01;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button_add_wifi) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmwifi");
            ContentValues values = new ContentValues();
            //values.put("Rule", mRuleCount ++);
            values.put("Rule", editText.getText().toString());
            getContentResolver().insert(uri, values);
        } else if(button == button_delete_wifi) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmwifi");
            //String[] selection = new String[1];
            Log.i(TAG, editText.getText().toString());
            getContentResolver().delete(uri, editText.getText().toString(), null);
        } else if(button == button_enable_audio) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmaudio");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x00;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button_disable_audio) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmaudio");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x01;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button_enable_video) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmvideo");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x00;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button_disable_video) {
            FileOutputStream fos;
            Uri uri = new Uri.Builder().build();
            uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmvideo");
            try {
                fos = (FileOutputStream)getContentResolver().openOutputStream(uri, "rw");
                byte[] benable = new byte[1];
                benable[0] = 0x01;
                fos.write(benable);
                fos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else if(button == button_add_apn) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmapn");
            ContentValues values = new ContentValues();
            //values.put("Rule", mRuleCount ++);
            values.put("Rule", editText.getText().toString());
            getContentResolver().insert(uri, values);
        } else if(button == button_delete_apn) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmapn");
            //String[] selection = new String[1];
            Log.i(TAG, editText.getText().toString());
            getContentResolver().delete(uri, editText.getText().toString(), null);
        } else if(button == button_add_vpn) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmvpn");
            ContentValues values = new ContentValues();
            //values.put("Rule", mRuleCount ++);
            values.put("Rule", editText.getText().toString());
            getContentResolver().insert(uri, values);
        } else if(button == button_delete_vpn) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmvpn");
            //String[] selection = new String[1];
            Log.i(TAG, editText.getText().toString());
            getContentResolver().delete(uri, editText.getText().toString(), null);
        } else if(button == button_add_email) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmemail");
            ContentValues values = new ContentValues();
            //values.put("Rule", mRuleCount ++);
            values.put("Rule", editText.getText().toString());
            getContentResolver().insert(uri, values);
        } else if(button == button_delete_email) {
            Uri uri = Uri.parse("content://com.android.accenture.aemm.AemmProvider/aemmemail");
            //String[] selection = new String[1];
            Log.i(TAG, editText.getText().toString());
            getContentResolver().delete(uri, editText.getText().toString(), null);
        }
    }
    
    final static String TAG = "AemmEnableDisalbe";
    static int mRuleCount; 
}