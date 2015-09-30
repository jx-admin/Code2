package com.zrh.goldfree;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

class GoldZoushi$1
  implements RadioGroup.OnCheckedChangeListener
{
  public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt)
  {
    if (paramInt == GoldZoushi.access$0(this.this$0).getId())
      GoldZoushi.access$1(this.this$0, 1);
    while (true)
    {
      return;
      if (paramInt == GoldZoushi.access$2(this.this$0).getId())
        GoldZoushi.access$1(this.this$0, 2);
      if (paramInt != GoldZoushi.access$3(this.this$0).getId())
        continue;
      GoldZoushi.access$1(this.this$0, 3);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.GoldZoushi.1
 * JD-Core Version:    0.5.4
 */