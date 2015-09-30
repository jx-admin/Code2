package wu.a.wuliu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import baidumapsdk.demo.R;

public class TimeSelectManager implements OnClickListener {
	
	private View view;
	private Context context;
	
	WheelView hours;
	WheelView day;
	Calendar calendar;
	public TimeSelectManager(Context context){
		this.context=context;
		view=LayoutInflater.from(context).inflate(R.layout.time_select_layout, null);
		view.findViewById(R.id.empty).setOnClickListener(this);
		view.findViewById(R.id.cancel).setOnClickListener(this);
		view.findViewById(R.id.sure).setOnClickListener(this);

	    hours = (WheelView) view.findViewById(R.id.hour);
	    NumericWheelAdapter hourAdapter = new NumericWheelAdapter(context, 0, 23,"%s时");
	    hourAdapter.setItemResource(R.layout.wheel_text_item);
	    hourAdapter.setItemTextResource(R.id.text);
	    hours.setViewAdapter(hourAdapter);

//	    final WheelView mins = (WheelView) view.findViewById(R.id.mins);
//	    NumericWheelAdapter minAdapter = new NumericWheelAdapter(context, 0, 59, "%02d");
//	    minAdapter.setItemResource(R.layout.wheel_text_item);
//	    minAdapter.setItemTextResource(R.id.text);
//	    mins.setViewAdapter(minAdapter);
//	    mins.setCyclic(true);
//	    
//	    final WheelView ampm = (WheelView) view.findViewById(R.id.ampm);
//	    ArrayWheelAdapter<String> ampmAdapter =
//	        new ArrayWheelAdapter<String>(context, new String[] {"AM", "PM"});
//	    ampmAdapter.setItemResource(R.layout.wheel_text_item);
//	    ampmAdapter.setItemTextResource(R.id.text);
//	    ampm.setViewAdapter(ampmAdapter);

	    // set current time
	    calendar = Calendar.getInstance(Locale.US);
	    hours.setCurrentItem(calendar.get(Calendar.HOUR));
//	    mins.setCurrentItem(calendar.get(Calendar.MINUTE));
//	    ampm.setCurrentItem(calendar.get(Calendar.AM_PM));
	    
	    day = (WheelView) view.findViewById(R.id.day);
	    day.setViewAdapter(new DayArrayAdapter(context, calendar));        

	}
	/**
     * Day adapter
     *
     */
    private class DayArrayAdapter extends AbstractWheelTextAdapter {
//        DateFormat format_EEE = new SimpleDateFormat("EEE");
        DateFormat df_MMM_d=new SimpleDateFormat("MMMd EEE");
        // Count of days to be shown
        private final int daysCount = 20;
        
        // Calendar
        Calendar calendar;
        
        /**
         * Constructor
         */
        protected DayArrayAdapter(Context context, Calendar calendar) {
            super(context, R.layout.time2_day, NO_RESOURCE);
            this.calendar = calendar;
            
//            setItemTextResource(R.id.time2_monthday);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            int day = /*-daysCount/2 + */index;
            Calendar newCalendar = (Calendar) calendar.clone();
            newCalendar.roll(Calendar.DAY_OF_YEAR, day);
            
            TextView monthday = (TextView) super.getItem(index, cachedView, parent);
//            TextView weekday = (TextView) view.findViewById(R.id.time2_weekday);
//            if (day == 0) {
//                weekday.setText(EMPTY);
//            } else {
//                weekday.setText(format_EEE.format(newCalendar.getTime()));
//            }

//            TextView monthday = (TextView) view.findViewById(R.id.time2_monthday);
            if (day == 0) {
                monthday.setText(TODAY);
                monthday.setTextColor(0xFF0000F0);
            } else {
                monthday.setText(df_MMM_d.format(newCalendar.getTime()));
                monthday.setTextColor(0xFF111111);
            }

            return monthday;
        }
        
        @Override
        public int getItemsCount() {
            return daysCount + 1;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return EMPTY;
        }
    }
	public View getView(){
		return view;
	}
	
	public String getTitle(){
		return "下单";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sure:
			if(mOnTimeSelectedListener!=null){
				int dayIndex=day.getCurrentItem();
				int hour=hours.getCurrentItem();
	            Calendar newCalendar = (Calendar) calendar.clone();
	            newCalendar.roll(Calendar.DAY_OF_YEAR, dayIndex);
	            newCalendar.set(Calendar.HOUR_OF_DAY, hour);
//	            Log.d("time","time:"+(new SimpleDateFormat("MMMd EEE hh").format(newCalendar.getTime())));
				mOnTimeSelectedListener.onTimeSelected(newCalendar.getTime());
			}
			break;
		case R.id.empty:
		case R.id.cancel:
			if(mOnTimeSelectedListener!=null){
				mOnTimeSelectedListener.onTimeSelected(null);
			}
			break;
//		case R.id.feedback_tv:
//			context.startActivity(new Intent(context,FeedBackActivity.class));
//			break;
		case R.id.cargo_goods:
			context.startActivity(new Intent(context,CargoGoodsActivity.class));
			break;
		case R.id.cargo_home:
			context.startActivity(new Intent(context,CargoHomeActivity.class));
			
			break;

		default:
			break;
		}
	}
	
	public static interface OnTimeSelectedListener{
		public void onTimeSelected(Date time);
	}
	private OnTimeSelectedListener mOnTimeSelectedListener;
	public void setOnTimeSelectedListener(OnTimeSelectedListener mOnTimeSelectedListener){
		this.mOnTimeSelectedListener=mOnTimeSelectedListener;
	}
	

}
