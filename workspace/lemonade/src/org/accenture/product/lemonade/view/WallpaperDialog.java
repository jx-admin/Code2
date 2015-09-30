package org.accenture.product.lemonade.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.accenture.product.lemonade.Launcher;
import org.accenture.product.lemonade.PersonalizationActivity;
import org.accenture.product.lemonade.R;
import org.accenture.product.lemonade.SettingActivity;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class WallpaperDialog extends Dialog
{

	public static final int LIVE_WALLPAPERS = 0;
	public static final int MEDIA_GALLERY = 1;
	public static final int WALLPAPERS = 2;

	private PersonalizationActivity personalizationActivity;

	public WallpaperDialog(Context context)
	{
		super(context, R.style.dialog_fullscreen);
		personalizationActivity = (PersonalizationActivity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wallpaperdialog);

		ListView listView = (ListView) findViewById(R.id.wallpaperdialoglist);
		// String[] items=new
		// String[]{"Live wallpapers","Media gallery","Wallpapers"};

		// SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),
		// getData(), R.layout.wallpaper_sel_list, new String[]
		//
		// { "title", "img" }, new int[]
		// { R.id.title, R.id.img });
		BaseAdapter simpleAdapter = new WallpaperAdapter(getContext(), getData());
		listView.setAdapter(simpleAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				switch (arg2)
				{
					case LIVE_WALLPAPERS:
						Intent liveWallpapers = new Intent();
						liveWallpapers.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
						personalizationActivity.startActivityForResult(liveWallpapers,
								PersonalizationActivity.REQUEST_PICK_LIVE_WALLPAPER);
						
						break;
					case MEDIA_GALLERY:

						// Intent intent=new
						// Intent(personalizationActivity,MediaPicListActivity.class);
						// personalizationActivity.startActivity(intent);

						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						// personalizationActivity.startActivityForResult(Intent.createChooser(intent,
						// "Select Picture"),PersonalizationActivity.REQUEST_PICK_MEDIA_WALLPAPER);
						personalizationActivity.startActivityForResult(intent,
								PersonalizationActivity.REQUEST_PICK_MEDIA_WALLPAPER);

						break;
					case WALLPAPERS:
						Intent wallpapers = new Intent(getContext(), SettingActivity.class);
						wallpapers.putExtra("type", PersonalizationActivity.WALLPAPER);
						getContext().startActivity(wallpapers);
						break;
				}

				dismiss();
			}

		});
	}

	private List<Map<String, Object>> getData()
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Live wallpapers");
		map.put("img", R.drawable.pic_temp);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Media gallery");
		map.put("img", R.drawable.pic_temp);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Wallpapers");
		map.put("img", R.drawable.pic_temp);
		list.add(map);

		return list;
	}

	@Override
	public void dismiss()
	{

		super.dismiss();
	}

	class WallpaperAdapter extends BaseAdapter
	{
		private Context context;
		List<Map<String, Object>> list;

		public WallpaperAdapter(Context context, List<Map<String, Object>> list)
		{
			this.context = context;
			this.list = list;

		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{

			switch (position)

			{
				case 0:
					ViewHolder viewHolder = new ViewHolder();
					if (convertView == null)
					{
						LayoutInflater inflater = LayoutInflater.from(context);
						convertView = inflater.inflate(R.layout.wallpaper_sel_list_top, null);

						viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
						viewHolder.text = (TextView) convertView.findViewById(R.id.title);
						viewHolder.text.setText("Live wallPapers");
						viewHolder.image.setImageResource(R.drawable.pic_temp);
						convertView.setTag(viewHolder);
					}
					else
					{
						viewHolder = (ViewHolder) convertView.getTag();
						viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
						viewHolder.text = (TextView) convertView.findViewById(R.id.title);
						viewHolder.text.setText("Live wallPapers");
						viewHolder.image.setImageResource(R.drawable.pic_temp);
						convertView.setTag(viewHolder);
					}
					return convertView;
				case 1:
					 viewHolder = new ViewHolder();
				if (convertView == null)
				{
					LayoutInflater inflater = LayoutInflater.from(context);
					convertView = inflater.inflate(R.layout.wallpaper_sel_list, null);

					viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
					viewHolder.text = (TextView) convertView.findViewById(R.id.title);
					viewHolder.text.setText("Media gallery");
					viewHolder.image.setImageResource(R.drawable.pic_temp);
					convertView.setTag(viewHolder);
				}
				else
				{
					viewHolder = (ViewHolder) convertView.getTag();
					viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
					viewHolder.text = (TextView) convertView.findViewById(R.id.title);
					viewHolder.text.setText("Media gallery");
					viewHolder.image.setImageResource(R.drawable.pic_temp);
					convertView.setTag(viewHolder);
				}
				return convertView;
				case 2:
					 viewHolder = new ViewHolder();
					if (convertView == null)
					{
						LayoutInflater inflater = LayoutInflater.from(context);
						convertView = inflater.inflate(R.layout.wallpaper_sel_list_bottom, null);

						viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
						viewHolder.text = (TextView) convertView.findViewById(R.id.title);
						viewHolder.text.setText("Wallpapers");
						viewHolder.image.setImageResource(R.drawable.pic_temp);
						convertView.setTag(viewHolder);
					}
					else
					{
						viewHolder = (ViewHolder) convertView.getTag();
						viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
						viewHolder.text = (TextView) convertView.findViewById(R.id.title);
						viewHolder.text.setText("Wallpapers");
						viewHolder.image.setImageResource(R.drawable.pic_temp);
						convertView.setTag(viewHolder);
					}
					return convertView;
			}

			return null;
		}
	}

	static class ViewHolder
	{
		ImageView image;
		TextView text;

	}
}
