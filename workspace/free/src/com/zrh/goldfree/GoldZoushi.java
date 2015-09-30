package com.zrh.goldfree;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import java.util.Calendar;

public class GoldZoushi extends Activity
{
  private Button btnChakan;
  private Calendar calend;
  private Calendar day06142006;
  private DatePicker dpicker;
  private RadioGroup.OnCheckedChangeListener mChangeRadio = new GoldZoushi.1(this);
  private int mCurrRadio;
  private int mNian;
  private RadioGroup mRadioGroup;
  private RadioButton mRadioNian;
  private RadioButton mRadioRi;
  private RadioButton mRadioYue;
  private int mRi;
  private int mYue;
  private Calendar newcal;
  private TextView tview;

  public static int getSDKVersionNumber()
  {
    int i;
    try
    {
      int j = Integer.valueOf(Build.VERSION.SDK_INT).intValue();
      i = j;
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      i = 0;
    }
  }

  @SuppressLint({"NewApi"})
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903049);
    this.mCurrRadio = -1;
    this.newcal = Calendar.getInstance();
    this.calend = Calendar.getInstance();
    this.day06142006 = Calendar.getInstance();
    this.day06142006.set(2006, 6, 14);
    this.dpicker = ((DatePicker)findViewById(2131230742));
    if (getSDKVersionNumber() >= 11)
      this.dpicker.setCalendarViewShown(false);
    this.dpicker.setFocusableInTouchMode(false);
    this.dpicker.setFocusable(false);
    this.tview = ((TextView)findViewById(2131230741));
    this.mNian = this.calend.get(1);
    this.mYue = this.calend.get(2);
    this.mRi = this.calend.get(5);
    this.tview.setText("当前选择的日期:[" + this.mNian + "-" + (1 + this.mYue) + "-" + this.mRi + "]");
    this.dpicker.init(this.calend.get(1), this.calend.get(2), this.calend.get(5), new GoldZoushi.2(this));
    this.mRadioGroup = ((RadioGroup)findViewById(2131230737));
    this.mRadioNian = ((RadioButton)findViewById(2131230738));
    this.mRadioYue = ((RadioButton)findViewById(2131230739));
    this.mRadioRi = ((RadioButton)findViewById(2131230740));
    this.mRadioGroup.setOnCheckedChangeListener(this.mChangeRadio);
    this.btnChakan = ((Button)findViewById(2131230744));
    this.btnChakan.setOnClickListener(new GoldZoushi.3(this));
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.zrh.goldfree.GoldZoushi
 * JD-Core Version:    0.5.4
 */