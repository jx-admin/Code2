package com.zrh.goldfree;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

class YinZoushi$3
  implements View.OnClickListener
{
  public void onClick(View paramView)
  {
    YinZoushi.access$5(this.this$0, YinZoushi.access$12(this.this$0).getYear());
    YinZoushi.access$6(this.this$0, YinZoushi.access$12(this.this$0).getMonth());
    YinZoushi.access$7(this.this$0, YinZoushi.access$12(this.this$0).getDayOfMonth());
    YinZoushi.access$8(this.this$0).set(YinZoushi.access$9(this.this$0), YinZoushi.access$10(this.this$0), YinZoushi.access$11(this.this$0));
    if (YinZoushi.access$13(this.this$0) < 0)
    {
      Toast.makeText(this.this$0.getApplicationContext(), "您还未选择时间类型", 0).show();
      YinZoushi.access$4(this.this$0).setText("您还未选择时间类型");
    }
    while (true)
    {
      return;
      if (YinZoushi.access$9(this.this$0) < 1987)
      {
        Toast.makeText(this.this$0.getApplicationContext(), "年份不能小于1987年", 0).show();
        YinZoushi.access$4(this.this$0).setText("年份不能小于1987年");
      }
      if (YinZoushi.access$8(this.this$0).after(YinZoushi.access$14(this.this$0)))
      {
        Toast.makeText(this.this$0.getApplicationContext(), "日期不能大于当前日期", 0).show();
        YinZoushi.access$4(this.this$0).setText("日期不能大于当前日期");
      }
      if ((YinZoushi.access$13(this.this$0) == 3) && (YinZoushi.access$8(this.this$0).before(YinZoushi.access$15(this.this$0))))
      {
        Toast.makeText(this.this$0.getApplicationContext(), "日走势图日期不能早于2006年6月14日", 0).show();
        YinZoushi.access$4(this.this$0).setText("日走势图日期不能早于2006年6月14日");
      }
      Intent localIntent = new Intent();
      localIntent.setClass(this.this$0, YinZoushiView.class);
      Bundle localBundle = new Bundle();
      localBundle.putInt("radio", YinZoushi.access$13(this.this$0));
      localBundle.putInt("nian", YinZoushi.access$9(this.this$0));
      localBundle.putInt("yue", 1 + YinZoushi.access$10(this.this$0));
      localBundle.putInt("ri", YinZoushi.access$11(this.this$0));
      localIntent.putExtras(localBundle);
      this.this$0.startActivity(localIntent);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.YinZoushi.3
 * JD-Core Version:    0.5.4
 */