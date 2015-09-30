package com.zrh.goldfree;

import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import java.util.Calendar;

class YinZoushi$2
  implements DatePicker.OnDateChangedListener
{
  public void onDateChanged(DatePicker paramDatePicker, int paramInt1, int paramInt2, int paramInt3)
  {
    YinZoushi.access$4(this.this$0).setText("当前选择的日期:[" + paramDatePicker.getYear() + "-" + (1 + paramDatePicker.getMonth()) + "-" + paramDatePicker.getDayOfMonth() + "]");
    YinZoushi.access$5(this.this$0, paramInt1);
    YinZoushi.access$6(this.this$0, paramInt2);
    YinZoushi.access$7(this.this$0, paramInt3);
    YinZoushi.access$8(this.this$0).set(YinZoushi.access$9(this.this$0), YinZoushi.access$10(this.this$0), YinZoushi.access$11(this.this$0));
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.YinZoushi.2
 * JD-Core Version:    0.5.4
 */