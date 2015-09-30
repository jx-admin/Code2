package com.zrh.goldfree;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity
{
  public static final String TAB_HOME = "tabHome";
  public static final String TAB_MES = "tabMes";
  public static final String TAB_SILVER = "tab_silver";
  public static final String TAB_TOUCH = "tab_touch";
  private RadioGroup group;
  private TabHost tabHost;

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    this.group = ((RadioGroup)findViewById(2131230720));
    this.tabHost = getTabHost();
    this.tabHost.addTab(this.tabHost.newTabSpec("tabHome").setIndicator("tabHome").setContent(new Intent(this, HomeView.class)));
    this.tabHost.addTab(this.tabHost.newTabSpec("tabMes").setIndicator("tabMes").setContent(new Intent(this, GoldTableView.class)));
    this.tabHost.addTab(this.tabHost.newTabSpec("tab_silver").setIndicator("tab_silver").setContent(new Intent(this, SilverTableView.class)));
    this.tabHost.addTab(this.tabHost.newTabSpec("tab_touch").setIndicator("tab_touch").setContent(new Intent(this, MoreView.class)));
    this.tabHost.setCurrentTab(0);
    this.group.setOnCheckedChangeListener(new MainActivity.1(this));
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131165184, paramMenu);
    return true;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.MainActivity
 * JD-Core Version:    0.5.4
 */