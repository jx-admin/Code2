package org.accenture.product.lemonade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class LiveWallpaperListActivity	extends ListActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SimpleAdapter simpleAdapter=new SimpleAdapter(this, getData(), R.layout.live_wallpaper_list, new String[]{"aaa","bbb","ccc"}, new int[]{R.id.live_wallpaper_title});
		
	}
	
	private List<Map<String, Object>> getData(){
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
}
