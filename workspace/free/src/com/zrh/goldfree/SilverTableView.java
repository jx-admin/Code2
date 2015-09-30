package com.zrh.goldfree;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import java.util.ArrayList;
import java.util.List;

public class SilverTableView extends ListActivity
{
  private List<News> li = new ArrayList();
  private TextView mText;
  private String title = "";

  private List<News> getRss(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    News localNews1 = new News();
    localNews1.setTitle("白银实时美元计价");
    localNews1.setLink("http://www.kitco.cn/cn/live_charts/silvercn.gif");
    localArrayList.add(localNews1);
    News localNews2 = new News();
    localNews2.setTitle("白银实时人民币计价");
    localNews2.setLink("http://www.kitco.cn/cn/live_charts/silvercny.gif");
    localArrayList.add(localNews2);
    News localNews3 = new News();
    localNews3.setTitle("白银阶段走势");
    localNews3.setLink("");
    localArrayList.add(localNews3);
    News localNews4 = new News();
    localNews4.setTitle("纸白银");
    localNews4.setLink("http://image.cngold.org/chart/silver/zhisilver.gif");
    localArrayList.add(localNews4);
    News localNews5 = new News();
    localNews5.setTitle("白银T+D");
    localNews5.setLink("http://image.cngold.org/chart/gold/agt_d.png");
    localArrayList.add(localNews5);
    News localNews6 = new News();
    localNews6.setTitle("白银港币计价");
    localNews6.setLink("");
    localArrayList.add(localNews6);
    News localNews7 = new News();
    localNews7.setTitle("钯金美元计价");
    localNews7.setLink("http://www.kitco.cn/cn/live_charts/pladcn.gif");
    localArrayList.add(localNews7);
    News localNews8 = new News();
    localNews8.setTitle("钯金人民币计价");
    localNews8.setLink("http://www.kitco.cn/cn/live_charts/pladcny.gif");
    localArrayList.add(localNews8);
    News localNews9 = new News();
    localNews9.setTitle("钯金港币计价");
    localNews9.setLink("");
    localArrayList.add(localNews9);
    News localNews10 = new News();
    localNews10.setTitle("铑金人民币计价");
    localNews10.setLink("http://www.kitco.cn/cn/past_charts/rh0030lns_cn.gif");
    localArrayList.add(localNews10);
    News localNews11 = new News();
    localNews11.setTitle("基本金属走势图");
    localNews11.setLink("");
    localArrayList.add(localNews11);
    return localArrayList;
  }

  protected void ShowDetail(int paramInt)
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

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903048);
    ((AdView)findViewById(2131230728)).loadAd(new AdRequest());
    this.li = getRss("");
    setListAdapter(new SilverAdapter(this, this.li));
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    if (paramInt == 0)
      ShowDetail(0);
    while (true)
    {
      return;
      if (paramInt == 1)
        ShowDetail(1);
      if (paramInt == 2)
      {
        Intent localIntent1 = new Intent();
        localIntent1.setClass(this, SilverStageView.class);
        startActivity(localIntent1);
      }
      if (paramInt == 3)
        ShowDetail(3);
      if (paramInt == 4)
        ShowDetail(4);
      if (paramInt == 5)
      {
        Intent localIntent2 = new Intent();
        localIntent2.setClass(this, SilverGBView.class);
        startActivity(localIntent2);
      }
      if (paramInt == 6)
        ShowDetail(6);
      if (paramInt == 7)
        ShowDetail(7);
      if (paramInt == 8)
      {
        Intent localIntent3 = new Intent();
        localIntent3.setClass(this, PalladiumGBView.class);
        startActivity(localIntent3);
      }
      if (paramInt == 9)
        ShowDetail(9);
      if (paramInt != 10)
        continue;
      Intent localIntent4 = new Intent();
      localIntent4.setClass(this, BaseMetalView.class);
      startActivity(localIntent4);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.SilverTableView
 * JD-Core Version:    0.5.4
 */