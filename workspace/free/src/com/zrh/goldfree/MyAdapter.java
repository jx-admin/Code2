package com.zrh.goldfree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class MyAdapter extends BaseAdapter
{
  private List<News> items;
  private LayoutInflater mInflater;

  public MyAdapter(Context paramContext, List<News> paramList)
  {
    this.mInflater = LayoutInflater.from(paramContext);
    this.items = paramList;
  }

  public int getCount()
  {
    return this.items.size();
  }

  public Object getItem(int paramInt)
  {
    return this.items.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    ViewHolder localViewHolder;
    if (paramView == null)
    {
      paramView = this.mInflater.inflate(2130903046, null);
      localViewHolder = new ViewHolder(null);
      localViewHolder.text = ((TextView)paramView.findViewById(2131230726));
      paramView.setTag(localViewHolder);
    }
    while (true)
    {
      News localNews = (News)this.items.get(paramInt);
      localViewHolder.text.setText(localNews.getTitle());
      return paramView;
      localViewHolder = (ViewHolder)paramView.getTag();
    }
  }

  private class ViewHolder
  {
    TextView text;

    private ViewHolder()
    {
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.MyAdapter
 * JD-Core Version:    0.5.4
 */