package com.zrh.goldfree;

import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import java.util.Calendar;

class GoldZoushi$2
  implements DatePicker.OnDateChangedListener
{
  public void onDateChanged(DatePicker paramDatePicker, int paramInt1, int paramInt2, int paramInt3)
  {
    GoldZoushi.access$4(this.this$0).setText("当前选择的日期:[" + paramDatePicker.getYear() + "-" + (1 + paramDatePicker.getMonth()) + "-" + paramDatePicker.getDayOfMonth() + "]");
    GoldZoushi.access$5(this.this$0, paramInt1);
    GoldZoushi.access$6(this.this$0, paramInt2);
    GoldZoushi.access$7(this.this$0, paramInt3);
    GoldZoushi.access$8(this.this$0).set(GoldZoushi.access$9(this.this$0), GoldZoushi.access$10(this.this$0), GoldZoushi.access$11(this.this$0));
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.GoldZoushi.2
 * JD-Core Version:    0.5.4
 */