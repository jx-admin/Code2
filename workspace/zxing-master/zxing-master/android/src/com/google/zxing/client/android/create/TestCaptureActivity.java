package com.google.zxing.client.android.create;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.R;

public class TestCaptureActivity extends Activity {

	public static final String DATA = "data";
	public static final int CaptureRequestCode = 1;

	EditText result_tv;
	ImageView ivImageView;
	Spinner code_types;
	Button encode_btn;
	BarcodeFormat[]barcodeFormats;
	int position=0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.capture_create);
		result_tv = (EditText) findViewById(R.id.editText1);
		((Button) findViewById(R.id.button1)).setOnClickListener(listener);
		((Button) findViewById(R.id.search_btn)).setOnClickListener(listener);
		((Button) findViewById(R.id.code1_btn)).setOnClickListener(listener);// 条形码
		((Button) findViewById(R.id.code2_btn)).setOnClickListener(listener);// 二维码
		code_types=(Spinner) findViewById(R.id.code_types);
		encode_btn=(Button) findViewById(R.id.encode_btn);
		encode_btn.setOnClickListener(listener);
		ivImageView = (ImageView) findViewById(R.id.imageView1);


		barcodeFormats=BarcodeFormat.values();
		code_types.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView=new TextView(TestCaptureActivity.this);
				}
				((TextView)convertView).setText(barcodeFormats[position].toString());
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return barcodeFormats.length;
			}
		});
		
		code_types.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				TestCaptureActivity.this.position=position;
				Toast.makeText(TestCaptureActivity.this, "你点击的是:"+position, 2000).show();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.search_btn:
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_WEB_SEARCH);
				intent.putExtra(SearchManager.QUERY, result_tv.getText()
						.toString());
				startActivity(intent);
				break;
			case R.id.button1:
				goToViewForResult(TestCaptureActivity.this,
						CaptureActivity.class, R.id.search_btn, CaptureRequestCode);
				break;
			case R.id.code1_btn:
				String strconteString = result_tv.getText().toString().trim();
				Bitmap mBitmap = null;
				mBitmap = Encode.creatBarcode(TestCaptureActivity.this,
						strconteString,BarcodeFormat.CODE_128, 300, 300, true);
				if (mBitmap != null) {
					ivImageView.setImageBitmap(mBitmap);
				}
				break;
			case R.id.code2_btn:
				strconteString = result_tv.getText().toString().trim();
				mBitmap = null;
				try {
					if (!strconteString.equals("")) {
						mBitmap = Encode.Create2DCode(strconteString,
								BarcodeFormat.QR_CODE,
								BitmapFactory.decodeResource(getResources(),
										R.drawable.we));
						if (mBitmap != null) {
							ivImageView.setImageBitmap(mBitmap);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case R.id.encode_btn:
				strconteString = result_tv.getText().toString().trim();
				mBitmap = null;
				try {
					if (!strconteString.equals("")) {
						mBitmap = Encode.Create2DCode(strconteString,
								barcodeFormats[position]);
						if (mBitmap != null) {
							ivImageView.setImageBitmap(mBitmap);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}

		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == CaptureRequestCode) {
			String scaString = data.getStringExtra(DATA);
			result_tv.setText(scaString);
		}
	}

	public static final String ViewArgu1 = "ViewArgu1";
	public static final String ViewArgu2 = "ViewArgu2";
	public static final String ViewArgu3 = "ViewArgu3";
	public static final String ViewArgu4 = "ViewArgu4";
	public static final String ViewArguBundle = "ViewArguBundle";

	public static final String formatString = "yyyy-MM-dd";
	public static final String formatString2 = "yyyy��MM��dd��";

	public static void goToView(Context cxt, Class<?> targetClass) {
		Intent intent = new Intent(cxt, targetClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		cxt.startActivity(intent);
	}

	public static void goToViewAndNewTask(Context cxt, Class<?> targetClass) {
		Intent intent = new Intent(cxt, targetClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		cxt.startActivity(intent);
	}

	public static void goToViewForResult(Activity acy, Class<?> targetClass,
			int code) {
		Intent intent = new Intent(acy, targetClass);
		intent.putExtra(ViewArgu1, String.valueOf(code));
		acy.startActivityForResult(intent, code);
	}

	public static void goToViewForResult(Activity acy, Class<?> targetClass,
			int code, int requestCode) {
		Intent intent = new Intent(acy, targetClass);
		intent.putExtra(ViewArgu1, String.valueOf(code));
		acy.startActivityForResult(intent, requestCode);
	}

	public static void goToViewByArgu(Context cxt, Class<?> targetClass,
			String argu1) {
		Intent intent = new Intent(cxt, targetClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ViewArgu1, argu1);
		cxt.startActivity(intent);
	}

	public static void goToViewByArgu(Context cxt, Class<?> targetClass,
			String argu1, String argu2) {
		Intent intent = new Intent(cxt, targetClass);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ViewArgu1, argu1);
		intent.putExtra(ViewArgu2, argu2);
		cxt.startActivity(intent);
	}

	public static String Argu1(Intent intent) {
		if (null != intent) {
			return intent.getStringExtra(ViewArgu1);
		}
		return null;
	}

	public static String Argu2(Intent intent) {
		if (null != intent) {
			return intent.getStringExtra(ViewArgu2);
		}
		return null;
	}

	public static String Argu3(Intent intent) {
		if (null != intent) {
			return intent.getStringExtra(ViewArgu3);
		}
		return null;
	}
}
