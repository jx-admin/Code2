/*
Copyright 2012 Aphid Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package wu.a.autolayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    addItem(data, "LayoutRelative", ActivityLayoutRelative.class);
    addItem(data, "LayoutAbsolute", ActivityLayoutAbsolute.class);
    addItem(data, "ActivityFrameLayout", ActivityFrameLayout.class);
    addItem(data, "XMLParserSerializerActivity", XMLParserSerializerActivity.class);

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
