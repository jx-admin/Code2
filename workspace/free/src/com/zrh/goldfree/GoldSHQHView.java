package com.zrh.goldfree;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class GoldSHQHView extends ListActivity
{
  private Button btnBack;
  private List<News> li = new ArrayList();
  private TextView mText;
  private String title = "";

  private List<News> getRss(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    News localNews1 = new News();
    localNews1.setTitle("黄金期货1301");
    localNews1.setLink("http://image.cngold.org/chart/futures/gold/au1301.gif");
    localArrayList.add(localNews1);
    News localNews2 = new News();
    localNews2.setTitle("黄金期货1302");
    localNews2.setLink("http://image.cngold.org/chart/gold/au1302.gif");
    localArrayList.add(localNews2);
    News localNews3 = new News();
    localNews3.setTitle("黄金期货1303");
    localNews3.setLink("http://image.cngold.org/chart/gold/au1303.gif");
    localArrayList.add(localNews3);
    News localNews4 = new News();
    localNews4.setTitle("黄金期货1304");
    localNews4.setLink("http://image.cngold.org/chart/gold/au1304.gif");
    localArrayList.add(localNews4);
    News localNews5 = new News();
    localNews5.setTitle("黄金期货1305");
    localNews5.setLink("http://image.cngold.org/chart/gold/au1305.gif");
    localArrayList.add(localNews5);
    News localNews6 = new News();
    localNews6.setTitle("黄金期货1306");
    localNews6.setLink("http://image.cngold.org/chart/futures/gold/au1306.gif");
    localArrayList.add(localNews6);
    News localNews7 = new News();
    localNews7.setTitle("黄金期货1307");
    localNews7.setLink("http://image.cngold.org/chart/futures/gold/au1307.gif");
    localArrayList.add(localNews7);
    News localNews8 = new News();
    localNews8.setTitle("黄金期货1308");
    localNews8.setLink("http://image.cngold.org/chart/futures/gold/au1308.gif");
    localArrayList.add(localNews8);
    News localNews9 = new News();
    localNews9.setTitle("黄金期货1309");
    localNews9.setLink("http://image.cngold.org/chart/futures/gold/au1309.gif");
    localArrayList.add(localNews9);
    News localNews10 = new News();
    localNews10.setTitle("黄金期货1310");
    localNews10.setLink("http://image.cngold.org/chart/futures/gold/au1310.gif");
    localArrayList.add(localNews10);
    News localNews11 = new News();
    localNews11.setTitle("黄金期货1311");
    localNews11.setLink("http://image.cngold.org/chart/futures/gold/au1311.gif");
    localArrayList.add(localNews11);
    News localNews12 = new News();
    localNews12.setTitle("黄金期货1312");
    localNews12.setLink("http://image.cngold.org/chart/futures/gold/au13012.gif");
    localArrayList.add(localNews12);
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903043);
    this.li = getRss("");
    setListAdapter(new MyAdapter(this, this.li));
    this.btnBack = ((Button)findViewById(2131230725));
    this.btnBack.setOnClickListener(new GoldSHQHView.1(this));
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    News localNews = (News)this.li.get(paramInt);
    Intent localIntent = new Intent();
    localIntent.setClass(this, DetailView.class);
    Bundle localBundle = new Bundle();
    localBundle.putString("title", localNews.getTitle());
    localBundle.putString("desc", localNews.getDesc());
    localBundle.putString("link", localNews.getLink());
    localIntent.putExtras(localBundle);
    startActivity(localIntent);
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.GoldSHQHView
 * JD-Core Version:    0.5.4
 */