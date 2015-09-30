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

public class CSilverLeaseView extends ListActivity
{
  private Button btnBack;
  private List<News> li = new ArrayList();
  private TextView mText;
  private String title = "";

  private List<News> getRss(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    News localNews1 = new News();
    localNews1.setTitle("30天");
    localNews1.setLink("http://www.kitconet.com/charts/metals/leaserates/ag_go_0030_lsb.gif");
    localArrayList.add(localNews1);
    News localNews2 = new News();
    localNews2.setTitle("60天");
    localNews2.setLink("http://www.kitconet.com/charts/metals/leaserates/ag_go_0060_lsb.gif");
    localArrayList.add(localNews2);
    News localNews3 = new News();
    localNews3.setTitle("6月");
    localNews3.setLink("http://www.kitconet.com/charts/metals/leaserates/ag_go_0182_lsb.gif");
    localArrayList.add(localNews3);
    News localNews4 = new News();
    localNews4.setTitle("1年");
    localNews4.setLink("http://www.kitconet.com/charts/metals/leaserates/ag_go_0365_lsb.gif");
    localArrayList.add(localNews4);
    News localNews5 = new News();
    localNews5.setTitle("详图");
    localNews5.setLink("http://www.sharelynx.com/chartstemp/charts11/kitco2aglrates.php");
    localArrayList.add(localNews5);
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903043);
    this.li = getRss("");
    setListAdapter(new SilverAdapter(this, this.li));
    this.btnBack = ((Button)findViewById(2131230725));
    this.btnBack.setOnClickListener(new CSilverLeaseView.1(this));
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
 * Qualified Name:     com.zrh.goldfree.CSilverLeaseView
 * JD-Core Version:    0.5.4
 */