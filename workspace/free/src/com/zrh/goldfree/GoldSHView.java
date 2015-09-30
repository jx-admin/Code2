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

public class GoldSHView extends ListActivity
{
  private Button btnBack;
  private List<News> li = new ArrayList();
  private TextView mText;
  private String title = "";

  private List<News> getRss(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    News localNews1 = new News();
    localNews1.setTitle("黄金T+D价格");
    localNews1.setLink("http://image.cngold.org/chart/gold/aut_d.png");
    localArrayList.add(localNews1);
    News localNews2 = new News();
    localNews2.setTitle("白银T+D价格");
    localNews2.setLink("http://image.cngold.org/chart/gold/agt_d.png");
    localArrayList.add(localNews2);
    News localNews3 = new News();
    localNews3.setTitle("黄金99.99价格");
    localNews3.setLink("http://image.cngold.org/chart/gold/au99_99.png");
    localArrayList.add(localNews3);
    News localNews4 = new News();
    localNews4.setTitle("黄金99.95价格");
    localNews4.setLink("http://image.cngold.org/chart/gold/au99_95.png");
    localArrayList.add(localNews4);
    News localNews5 = new News();
    localNews5.setTitle("黄金100g价格");
    localNews5.setLink("http://image.cngold.org/chart/gold/au100g.png");
    localArrayList.add(localNews5);
    News localNews6 = new News();
    localNews6.setTitle("铂金99.95价格");
    localNews6.setLink("http://image.cngold.org/chart/gold/pt99_95.png");
    localArrayList.add(localNews6);
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903043);
    this.li = getRss("");
    setListAdapter(new MyAdapter(this, this.li));
    this.btnBack = ((Button)findViewById(2131230725));
    this.btnBack.setOnClickListener(new GoldSHView.1(this));
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
 * Qualified Name:     com.zrh.goldfree.GoldSHView
 * JD-Core Version:    0.5.4
 */