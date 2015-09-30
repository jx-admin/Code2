package com.act.mbanking.utils;

    import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.SuperscriptSpan;

    public class JXSuperscriptSpan extends SuperscriptSpan{
        public JXSuperscriptSpan() {
        }
        
        public JXSuperscriptSpan(Parcel src) {
        }
        
        
        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setTextSize(tp.getTextSize()*0.7f);
            tp.baselineShift -= (int) (tp.getTextSize()*0.3f);
        }

        @Override
        public void updateMeasureState(TextPaint tp) {
            tp.setTextSize(tp.getTextSize()*0.7f);
            tp.baselineShift -= (int) (tp.getTextSize()*0.3f);
        }
    }
