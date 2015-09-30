package wu.a.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wu.a.lib.device.DisplayActivity;
import wu.a.template.app.AppListActivity;
import wu.a.template.bmp.BmpActivity;
import wu.a.template.bmp.CropImgActivity;
import wu.a.template.bmp.TouchImageViewActivity;
import wu.a.template.media.AudioFocusChangeActivity;
import wu.a.template.media.MediaAcitivity;
import wu.a.template.media.MediaButtonSenderActivity;
import wu.a.template.xml.XMLParserSerializerActivity;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setListAdapter(
	        new SimpleAdapter(
	            this, getData(), android.R.layout.simple_list_item_1, new String[]{"title"},
	            new int[]{android.R.id.text1}
	        )
	    );
	    getListView().setScrollbarFadingEnabled(false);
	  }

	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    return true;
	  }

	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    Intent intent = new Intent(
	        Intent.ACTION_VIEW,
	        Uri.parse("http://openaphid.github.com/")
	    );
	    startActivity(intent);

	    return true;
	  }

	  @SuppressWarnings("unchecked")
	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
	    Intent intent = new Intent(this, (Class<? extends Activity>) map.get("activity"));
	    startActivity(intent);
	  }

	  private List<? extends Map<String, ?>> getData() {
	    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	    addItem(data, "MediaAcitivity", MediaAcitivity.class);
	    addItem(data, "AppListActivity", AppListActivity.class);
	    addItem(data, "MediaButtonSenderActivity", MediaButtonSenderActivity.class);
	    addItem(data, "AudioFocusChangeActivity", AudioFocusChangeActivity.class);
	    addItem(data, "XMLParserSerializerActivity", XMLParserSerializerActivity.class);
	    addItem(data, "CircleImageActivity", BmpActivity.class);
	    addItem(data, "CropImgActivity", CropImgActivity.class);
	    addItem(data, "TouchImageViewActivity", TouchImageViewActivity.class);
	    addItem(data, "CustomView", wu.a.lib.view.MainActivity.class);
	    addItem(data, "DisplayActivity", DisplayActivity.class);

	    return data;
	  }

	  private void addItem(List<Map<String, Object>> data, String title,
	                       Class<? extends Activity> activityClass) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("title", data.size() + ". " + title);
	    map.put("activity", activityClass);
	    data.add(map);
	  }
}
