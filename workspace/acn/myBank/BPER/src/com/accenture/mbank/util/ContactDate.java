package com.accenture.mbank.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ContactDate {

	/***One day time*/
	public static long oneDay=24*60*60*1000;
	/***format of day per year*/
	private static final String HOLIDAY_FORMATE="MMdd";
	private static final SimpleDateFormat sf = new SimpleDateFormat(HOLIDAY_FORMATE);
	/***<pre>
	 * The Hole day of Italian every year
	January 1
	January 6
	April 25
	May 1
	June 2
	August 15
	November 1
	December 8
	December 25
	december 26
	*/
	private static Map<String,String>ITALIAN_OFF_WORK_DATE;
	public static Map<String,String> getItalianHolidays(){
		if(ITALIAN_OFF_WORK_DATE==null){
			ITALIAN_OFF_WORK_DATE=new HashMap<String,String>();
			ITALIAN_OFF_WORK_DATE.put("0101", "");
			ITALIAN_OFF_WORK_DATE.put("0206", "");
			ITALIAN_OFF_WORK_DATE.put("0425", "");
			ITALIAN_OFF_WORK_DATE.put("0501", "");
			ITALIAN_OFF_WORK_DATE.put("0602", "");
			ITALIAN_OFF_WORK_DATE.put("0815", "");
			ITALIAN_OFF_WORK_DATE.put("1101", "");
			ITALIAN_OFF_WORK_DATE.put("1208", "");
			ITALIAN_OFF_WORK_DATE.put("1225", "");
			ITALIAN_OFF_WORK_DATE.put("1226", "");
		}
		return ITALIAN_OFF_WORK_DATE;
	}
	
	public static int isWorkDay(long millis){
		int isWorkDay=0;
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(millis);
		int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
		if(getItalianHolidays().containsKey(sf.format(millis))){
			System.out.println("holidy:"+sf.format(millis));
			isWorkDay=1;
		}else if(dayOfWeek==Calendar.SUNDAY){
			if(Easter.isEaster(millis)){
				System.out.println("Easter 2");
				isWorkDay=2;
			}else{
				System.out.println("sunday");
				isWorkDay=1;
			}
		}else if(dayOfWeek==Calendar.SATURDAY){
			System.out.println("saturday");
			isWorkDay=1;
		}else if(dayOfWeek==Calendar.MONDAY&&Easter.isEaster(millis-oneDay)){
			System.out.println("Easter Monday");
			isWorkDay=1;
		}else {
			System.out.println(sf.format(millis));
		}
		
		return isWorkDay;
	} 
	
	public static long getWorkDay(long startTime,int workDays){
    	int count=workDays;
    	startTime+=oneDay;
    	do{
    		int back=isWorkDay(startTime);
    		if(back==0){
    			count--;
    			if(count<=0){
    				break;
    			}
    			startTime+=oneDay;
    		}else{
    			startTime+=oneDay*back;
    		}
    		
    	}while(true);
        return startTime;
    }
}
