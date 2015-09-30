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

public class PlatinumTechView extends ListActivity
{
  private Button btnBack;
  private List<News> li = new ArrayList();
  private TextView mText;
  private String title = "";

  private List<News> getRss(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    News localNews1 = new News();
    localNews1.setTitle("一年铂金价格走势技术分析图 - 14天和200天简单移动平均");
    localNews1.setLink("http://www.kitco.cn/cn/tech_charts/pt0365lf_ma_cn.gif");
    localArrayList.add(localNews1);
    News localNews2 = new News();
    localNews2.setTitle("两年铂金价格走势技术分析图 - 30天和200天简单移动平均");
    localNews2.setLink("http://www.kitco.cn/cn/tech_charts/pt0730lf_ma_cn.gif");
    localArrayList.add(localNews2);
    News localNews3 = new News();
    localNews3.setTitle("五年铂金价格走势技术分析图 - 60天和200天简单移动平均");
    localNews3.setLink("http://www.kitco.cn/cn/tech_charts/pt1825lf_ma_cn.gif");
    localArrayList.add(localNews3);
    News localNews4 = new News();
    localNews4.setTitle("十年铂金价格走势技术分析图 - 60天和200天简单移动平均");
    localNews4.setLink("http://www.kitco.cn/cn/tech_charts/pt3650lf_ma_cn.gif");
    localArrayList.add(localNews4);
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903043);
    this.li = getRss("");
    setListAdapter(new MyAdapter(this, this.li));
    this.btnBack = ((Button)findViewById(2131230725));
    this.btnBack.setOnClickListener(new PlatinumTechView.1(this));
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
 * Qualified Name:     com.zrh.goldfree.PlatinumTechView
 * JD-Core Version:    0.5.4
 */