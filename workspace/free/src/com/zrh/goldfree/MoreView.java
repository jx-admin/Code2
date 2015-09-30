package com.zrh.goldfree;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import java.util.ArrayList;
import java.util.List;

public class MoreView extends ListActivity
{
  private List<News> li = new ArrayList();
  private TextView mText;
  private String title = "";

  private List<News> getRss(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    News localNews1 = new News();
    localNews1.setTitle("历史查询无广告版");
    localNews1.setLink("");
    localArrayList.add(localNews1);
    News localNews2 = new News();
    localNews2.setTitle("给黄金价格评论");
    localNews2.setLink("");
    localArrayList.add(localNews2);
    News localNews3 = new News();
    localNews3.setTitle("股票期货综合");
    localNews3.setLink("http://news.baidu.com/resource/html/bdhx.html?v=20120502");
    localArrayList.add(localNews3);
    News localNews4 = new News();
    localNews4.setTitle("美元指数与原油价格");
    localNews4.setLink("");
    localArrayList.add(localNews4);
    News localNews5 = new News();
    localNews5.setTitle("工行外汇牌价");
    localNews5.setLink("http://www.icbc.com.cn/ICBCDynamicSite/Optimize/Quotation/QuotationListIframe.aspx");
    localArrayList.add(localNews5);
    News localNews6 = new News();
    localNews6.setTitle("基金查询");
    localNews6.setLink("");
    localArrayList.add(localNews6);
    News localNews7 = new News();
    localNews7.setTitle("彩票开奖查询");
    localNews7.setLink("http://wap.lehecai.com/lottery/draw/?WSID=igr1rv70j6irgen5ph3chtmba5&_ai=3");
    localArrayList.add(localNews7);
    News localNews8 = new News();
    localNews8.setTitle("网站导航");
    localNews8.setLink("");
    localArrayList.add(localNews8);
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
    setListAdapter(new MyAdapter(this, this.li));
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    if (paramInt == 0)
      startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.zrh.goldprice")));
    while (true)
    {
      return;
      if (paramInt == 1)
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.zrh.goldfree")));
      if (paramInt == 2)
      {
        Intent localIntent1 = new Intent();
        localIntent1.setClass(this, GuZhiView.class);
        startActivity(localIntent1);
      }
      if (paramInt == 3)
      {
        Intent localIntent2 = new Intent();
        localIntent2.setClass(this, YuanYouView.class);
        startActivity(localIntent2);
      }
      if (paramInt == 4)
        ShowDetail(4);
      if (paramInt == 5)
      {
        Intent localIntent3 = new Intent();
        localIntent3.setClass(this, JiJinView.class);
        startActivity(localIntent3);
      }
      if (paramInt == 6)
        ShowDetail(6);
      if (paramInt == 7)
      {
        Intent localIntent4 = new Intent();
        localIntent4.setClass(this, Daohang.class);
        startActivity(localIntent4);
      }
      if (paramInt != 8)
        continue;
      Intent localIntent5 = new Intent();
      localIntent5.setClass(this, JiJinView.class);
      startActivity(localIntent5);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.MoreView
 * JD-Core Version:    0.5.4
 */