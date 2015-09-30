package org.accenture.product.lemonade;

import java.io.IOException;

import org.accenture.product.lemonade.view.WallpaperDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Toast;

/**
 * 
 * @author shaohu.zhu
 */
public class PersonalizationActivity extends Activity
{

	public static final int SKIN = 1;
	public static final int WALLPAPER = 2;
	public static final int TRANSITION = 3;
	public static final int SCENE = 4;
	public static final int ICON = 5;
	public static final int LOCKSCREEN = 6;

	public static final int REQUEST_PICK_LIVE_WALLPAPER = 0;
	public static final int REQUEST_PICK_MEDIA_WALLPAPER = 1;
	public static final int REQUEST_PICK_CUT_WALLPAPER = 2;

	WallPaperChangedReceiver wallpaperChangedReceiver;
	
	private static boolean wallpaperReceiverRegister=false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personalization);
		
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		 
		final Button skin = (Button) findViewById(R.id.skin);
		final Button wallpaper = (Button) findViewById(R.id.wallpaper);
		final Button scene = (Button) findViewById(R.id.scene);
		final Button icon = (Button) findViewById(R.id.icon);
		final Button lockScreen = (Button) findViewById(R.id.lockscreen);
		final AbsoluteLayout absoluteLayout = (AbsoluteLayout) findViewById(R.id.personalizationlayout);
		final Dialog wallpaperDialog = new WallpaperDialog(PersonalizationActivity.this);
		
		skin.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Intent intent = new Intent(PersonalizationActivity.this,
				// SettingActivity.class);
				// intent.putExtra("type", SKIN);
				// startActivityForResult(intent, 0);
			}
		});

		wallpaper.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				absoluteLayout.setBackgroundResource(R.drawable.personalization_main_background2);

				wallpaper.setBackgroundResource(R.drawable.personalization_main_btn_pressed);

				skin.setTextColor((PersonalizationActivity.this.getResources().getColor(R.color.gray)));
				scene.setTextColor((PersonalizationActivity.this.getResources().getColor(R.color.gray)));
				icon.setTextColor((PersonalizationActivity.this.getResources().getColor(R.color.gray)));
				lockScreen.setTextColor((PersonalizationActivity.this.getResources().getColor(R.color.gray)));

				wallpaperDialog.show();

			}
		});

		wallpaperDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
		{

			@Override
			public void onDismiss(DialogInterface dialog)
			{

				wallpaper.setBackgroundResource(R.drawable.personal_main_button_background);

				absoluteLayout.setBackgroundResource(R.drawable.personalization_main_background);
				skin.setTextColor((PersonalizationActivity.this.getResources().getColor(R.color.write)));
				scene.setTextColor((PersonalizationActivity.this.getResources().getColor(R.color.write)));
				icon.setTextColor((PersonalizationActivity.this.getResources().getColor(R.color.write)));
				lockScreen.setTextColor((PersonalizationActivity.this.getResources().getColor(R.color.write)));

			}
		});

		scene.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				 Intent intent = new Intent(PersonalizationActivity.this,
				 SettingActivity.class);
				 intent.putExtra("type", SCENE);
				 startActivity(intent);
			}
		});

		icon.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(PersonalizationActivity.this, SettingActivity.class);
				intent.putExtra("type", ICON);
				startActivity(intent);
			}
		});

		lockScreen.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				 Intent intent = new Intent(PersonalizationActivity.this,SettingActivity.class);
				 intent.putExtra("type", LOCKSCREEN);
				 startActivity(intent);
				 
			}
		});

	}
	
	@Override
	protected void onResume()
	{
		
//		IntentFilter wallpaperChangedFilter = new IntentFilter();// 实例化
//		wallpaperChangedFilter.addAction(Intent.ACTION_WALLPAPER_CHANGED);
//		
//		// 实例化Receiver
//		wallpaperChangedReceiver = new WallPaperChangedReceiver();
//		// 注册Receiver
//		registerReceiver(wallpaperChangedReceiver, wallpaperChangedFilter);
//		
//		wallpaperReceiverRegister=true;
		
		
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void finish()
	{
		
		super.finish();
	}
	
	@Override
	protected void onPause()
	{
//		if (wallpaperChangedReceiver != null && wallpaperReceiverRegister)
//		{
//			// 注销Receiver
//			unregisterReceiver(wallpaperChangedReceiver);
//			wallpaperReceiverRegister=false;
//		}
		
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_PICK_LIVE_WALLPAPER)
		{
			// if(data==null){
			// return;
			// }
			// Toast toast=Toast.makeText(this, R.string.set_wallpaper_success,
			// Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
			// toast.show();
			
			Intent goHome=new Intent(this,Launcher.class);
			startActivity(goHome);
		}
		else if (requestCode == REQUEST_PICK_MEDIA_WALLPAPER)
		{
			if (data == null)
				return;
			Uri uri = data.getData();
			Log.e("data", "data:" + uri.getPath());

			Intent intent = new Intent("com.android.camera.action.CROP");
//			intent.setClassName("com.android.camera", "com.android.camera.CropImage");
			intent.setData(uri);
			
			intent.putExtra("crop", "true");   
			intent.putExtra("noFaceDetection", true);   
			intent.putExtra("return-data", true);   
			
			// aspectX aspectY 是宽高的比例  
			intent.putExtra("aspectX", 3);   
			intent.putExtra("aspectY", 2);   
			
			// outputX outputY 是裁剪图片宽高  
			intent.putExtra("outputX",384 ); 
			intent.putExtra("outputY", 256); 
			
			intent.putExtra("setwallpaper", true);

			startActivityForResult(intent, REQUEST_PICK_CUT_WALLPAPER);
		}
		else if (requestCode == REQUEST_PICK_CUT_WALLPAPER)
		{
			if (data == null)
				return;
			Bitmap photo = data.getParcelableExtra("data");
			try
			{
				WallpaperManager.getInstance(this).setBitmap(photo);

				// Toast toast = Toast.makeText(this,
				// R.string.set_wallpaper_success, Toast.LENGTH_SHORT);
				// toast.setGravity(Gravity.CENTER, 0, 0);
				// toast.show();
			}
			catch (IOException e)
			{
				Log.e("error:", e.getMessage());
			}
			
			Intent goHome=new Intent(this,Launcher.class);
			startActivity(goHome);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

}
