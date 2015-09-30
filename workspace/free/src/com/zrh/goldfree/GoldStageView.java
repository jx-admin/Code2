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

public class GoldStageView extends ListActivity
{
  private Button btnBack;
  private List<News> li = new ArrayList();
  private TextView mText;
  private String title = "";

  private List<News> getRss(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    News localNews1 = new News();
    localNews1.setTitle("24小时(人民币)");
    localNews1.setLink("http://www.kitco.cn/cn/live_charts/kitcocn/t24_au_rmbg_cn.gif");
    localArrayList.add(localNews1);
    News localNews2 = new News();
    localNews2.setTitle("7天(人民币)");
    localNews2.setLink("http://www.kitco.cn/cn/live_charts/kitcocn/7ds_au_rmbg_cn.gif");
    localArrayList.add(localNews2);
    News localNews3 = new News();
    localNews3.setTitle("30天(人民币)");
    localNews3.setLink("http://www.kitco.cn/cn/live_charts/kitcocn/t30d_au_rmbg_cn.gif");
    localArrayList.add(localNews3);
    News localNews4 = new News();
    localNews4.setTitle("一年(人民币)");
    localNews4.setLink("http://www.kitco.cn/cn/live_charts/kitcocn/t365d_au_rmbg_cn.gif");
    localArrayList.add(localNews4);
    News localNews5 = new News();
    localNews5.setTitle("24小时(美元)");
    localNews5.setLink("http://www.kitco.cn/cn/live_charts/kitcocn/t24_au_usoz_cn.gif");
    localArrayList.add(localNews5);
    News localNews6 = new News();
    localNews6.setTitle("7天(美元)");
    localNews6.setLink("http://www.kitco.cn/cn/live_charts/kitcocn/7ds_au_usoz_cn.gif");
    localArrayList.add(localNews6);
    News localNews7 = new News();
    localNews7.setTitle("30天(美元)");
    localNews7.setLink("http://www.kitco.cn/cn/live_charts/kitcocn/t30d_au_usoz_cn.gif");
    localArrayList.add(localNews7);
    News localNews8 = new News();
    localNews8.setTitle("一年(美元)");
    localNews8.setLink("http://www.kitco.cn/cn/live_charts/kitcocn/t365d_au_usoz_cn.gif");
    localArrayList.add(localNews8);
    return localArrayList;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903043);
    this.li = getRss("");
    setListAdapter(new MyAdapter(this, this.li));
    this.btnBack = ((Button)findViewById(2131230725));
    this.btnBack.setOnClickListener(new GoldStageView.1(this));
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
 * Qualified Name:     com.zrh.goldfree.GoldStageView
 * JD-Core Version:    0.5.4
 */