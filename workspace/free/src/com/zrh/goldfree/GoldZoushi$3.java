package com.zrh.goldfree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

class GoldZoushi$3
  implements View.OnClickListener
{
  public void onClick(View paramView)
  {
    GoldZoushi.access$5(this.this$0, GoldZoushi.access$12(this.this$0).getYear());
    GoldZoushi.access$6(this.this$0, GoldZoushi.access$12(this.this$0).getMonth());
    GoldZoushi.access$7(this.this$0, GoldZoushi.access$12(this.this$0).getDayOfMonth());
    GoldZoushi.access$8(this.this$0).set(GoldZoushi.access$9(this.this$0), GoldZoushi.access$10(this.this$0), GoldZoushi.access$11(this.this$0));
    if (GoldZoushi.access$13(this.this$0) < 0)
    {
      Toast.makeText(this.this$0.getApplicationContext(), "您还未选择时间类型", 0).show();
      GoldZoushi.access$4(this.this$0).setText("您还未选择时间类型");
    }
    while (true)
    {
      return;
      if (GoldZoushi.access$9(this.this$0) < 1977)
      {
        Toast.makeText(this.this$0.getApplicationContext(), "年份不能小于1977年", 0).show();
        GoldZoushi.access$4(this.this$0).setText("年份不能小于1977年");
      }
      if (GoldZoushi.access$8(this.this$0).after(GoldZoushi.access$14(this.this$0)))
      {
        Toast.makeText(this.this$0.getApplicationContext(), "日期不能大于当前日期", 0).show();
        GoldZoushi.access$4(this.this$0).setText("日期不能大于当前日期");
      }
      if ((GoldZoushi.access$13(this.this$0) == 3) && (GoldZoushi.access$8(this.this$0).before(GoldZoushi.access$15(this.this$0))))
      {
        Toast.makeText(this.this$0.getApplicationContext(), "日走势图日期不能早于2006年6月14日", 0).show();
        GoldZoushi.access$4(this.this$0).setText("日走势图日期不能早于2006年6月14日");
      }
      Intent localIntent = new Intent();
      localIntent.setClass(this.this$0, GoldZoushiView.class);
      Bundle localBundle = new Bundle();
      localBundle.putInt("radio", GoldZoushi.access$13(this.this$0));
      localBundle.putInt("nian", GoldZoushi.access$9(this.this$0));
      localBundle.putInt("yue", 1 + GoldZoushi.access$10(this.this$0));
      localBundle.putInt("ri", GoldZoushi.access$11(this.this$0));
      localIntent.putExtras(localBundle);
      this.this$0.startActivity(localIntent);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.GoldZoushi.3
 * JD-Core Version:    0.5.4
 */