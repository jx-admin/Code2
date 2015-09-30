package com.zrh.goldfree;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

class YinZoushi$1
  implements RadioGroup.OnCheckedChangeListener
{
  public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt)
  {
    if (paramInt == YinZoushi.access$0(this.this$0).getId())
      YinZoushi.access$1(this.this$0, 1);
    while (true)
    {
      return;
      if (paramInt == YinZoushi.access$2(this.this$0).getId())
        YinZoushi.access$1(this.this$0, 2);
      if (paramInt != YinZoushi.access$3(this.this$0).getId())
        continue;
      YinZoushi.access$1(this.this$0, 3);
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.YinZoushi.1
 * JD-Core Version:    0.5.4
 */