package org.accenture.product.lemonade;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MediaPicListActivity extends Activity
{
	public static final int REQUEST_PICK_SDCARD_WALLPAPER = 0;
	MediaPicAdapter adapter;

	public static final int IMAGE_WIDTH = 100;
	private static final int IMAGE_HEIGHT = 100;
	GridView mediaPicList;
	
	private static final String IMAGE_PATH="/sdcard/DCIM";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.media_pic_list);

		TextView textView = new TextView(this);
		textView.setText(R.string.no_media_pic);

		adapter = new MediaPicAdapter(this);
		mediaPicList = (GridView) findViewById(R.id.mediaPicList);

		mediaPicList.setAdapter(adapter);

		mediaPicList.setEmptyView(textView);

		mediaPicList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
			{
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setClassName("com.android.gallery", "com.android.camera.CropImage");
//				String path = (String) adapter.getItemAtPosition(position);
//				Uri photoUri = Uri.parse("file://" + path);
				
				Bitmap bitmap=(Bitmap)adapter.getItemAtPosition(position);
				String s=MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null);
				s="file:///sdcard/eoe.gif";
				Log.e("s-------------------:","s-------------------:"+s);
				Uri photoUri = Uri.parse(s); 
				 
				intent.setData(photoUri);

				intent.putExtra("crop", "true");
//				 intent.putExtra("aspectX", 1);
//				 intent.putExtra("aspectY", 1);
//				 intent.putExtra("outputX", 96);
//				 intent.putExtra("outputY", 96);
				intent.putExtra("noFaceDetection", false);
				intent.putExtra("return-data", true);

				startActivityForResult(intent, REQUEST_PICK_SDCARD_WALLPAPER);
			}

		});

		listImage(IMAGE_PATH);
	}

	@Override
	protected void onPause()
	{
		int count = mediaPicList.getCount();
		for (int i = 0; i < count; i++)
		{
			ImageView v = (ImageView) mediaPicList.getChildAt(i);
			if (v != null)
			{
				if (v.getDrawable() != null)
					v.getDrawable().setCallback(null);
			}
		}
		super.onPause();
	}

	private boolean listImage(String path)
	{
		// SD卡状态
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED))
		{// mounted
			ImageLoadTask task = new ImageLoadTask(this, adapter);
			task.execute(path);
			return true;
		}

		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_PICK_SDCARD_WALLPAPER)
		{
			Bitmap photo = data.getParcelableExtra("data");
			try
			{
				WallpaperManager.getInstance(this).setBitmap(photo);

//				Toast toast = Toast.makeText(this, R.string.set_wallpaper_success, Toast.LENGTH_SHORT);
//				toast.setGravity(Gravity.CENTER, 0, 0);
//				toast.show();
			}
			catch (IOException e)
			{
				Log.e("error:", e.getMessage());
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	class MediaPicAdapter extends BaseAdapter
	{

		Context context;
		private ArrayList<Bitmap> picList;

		public MediaPicAdapter(Context context)
		{
			picList = new ArrayList<Bitmap>();
			this.context = context;
		}

		public void add(Bitmap bitmap)
		{
			picList.add(bitmap);
		}

		public void clear()
		{
			picList.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount()
		{
			return picList.size();
		}

		@Override
		public Bitmap getItem(int position)
		{
			return picList.get(position);
		}

		@Override
		public long getItemId(int id)
		{
			return id;
		}

		int i = 0;

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView imageView = null;
			try
			{
				if (convertView == null)
				{
					imageView = new ImageView(context);
					imageView.setLayoutParams(new GridView.LayoutParams(IMAGE_WIDTH, IMAGE_HEIGHT));
					imageView.setAdjustViewBounds(false);
					imageView.setScaleType(ScaleType.CENTER_CROP);
					imageView.setPadding(2, 2, 2, 2);
				}
				else
				{
					imageView = (ImageView) convertView;
				}

				// FileInputStream is2 = new FileInputStream(new
				// File(picList.get(position)));
				// Bitmap bitmap = BitmapFactory.decodeStream(is2, null, null);
				// is2.close();
				// // WeakReference<Bitmap> weakWidget = new
				// WeakReference<Bitmap>(BitmapFactory.decodeStream(is2, null,
				// null));
				//
				// imageView.setImageBitmap(bitmap);
				
//				Bitmap bitmap=getThumBitmap(picList.get(position));
				imageView.setImageBitmap(picList.get(position));
				
			}
			catch (Exception e)
			{
				Log.e("error", e.getMessage());
			}
			return imageView;
		}
	}
}
