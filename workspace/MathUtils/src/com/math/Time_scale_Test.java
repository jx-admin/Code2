package com.math;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Time_scale_Test {

	public static void main(String args[]){
//		testGetListTime_scale();
		Number_scale.getVales(-30910.09,225611, 8, 0);
		
	}
	static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void testGetListTime_scale(){
		Calendar fromCalendar=Calendar.getInstance();
		long datas[];
		
		fromCalendar.set(1900, 1, 1);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();
		

		fromCalendar.set(2000, 1, 1);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();
		

		fromCalendar.set(2012, 1, 1);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();
		

		fromCalendar.set(2013, 1, 1);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();

		fromCalendar.set(2013, 4, 1);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();

		fromCalendar.set(2013, 5, 1);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();
		

		fromCalendar.set(2013, 5, 10);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();
		

		fromCalendar.set(2013, 5, 20);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();
		

		fromCalendar.set(2013, 5, 27);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();
		

		fromCalendar.setTimeInMillis(System.currentTimeMillis()-Time_scale.ONEHOUR);
		System.out.println("from "+formatter.format(fromCalendar.getTimeInMillis())+" to "+formatter.format(System.currentTimeMillis()));
		datas=Time_scale.getListTime_scale(fromCalendar.getTimeInMillis(),System.currentTimeMillis(),Time_scale.ONEMONTH,false,7);
		printArray(datas);System.out.println();
	}
	
	public static void printArray(long datas[]){
		if(datas==null){
			System.out.println("datas is null");
		}
		for(int i=0;i<datas.length;i++){
			System.out.println("date:"+formatter.format(datas[i]));
		}
	}
	
	public static void tmp(){
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTimeInMillis(System.currentTimeMillis()-Time_scale.ONEMONTH);
		Calendar toCalendar = Calendar.getInstance();
		System.out.println("date:"+formatter.format(fromCalendar.getTimeInMillis()));
		System.out.println("date:"+formatter.format(toCalendar.getTimeInMillis()));
		
		Date currentTime = new Date();
		   String dateString = formatter.format(currentTime);

		for(;!fromCalendar.after(toCalendar);fromCalendar.set(Calendar.DAY_OF_MONTH, fromCalendar.get(Calendar.DAY_OF_MONTH)+1)){

			System.out.println("date:"+formatter.format(fromCalendar.getTimeInMillis()));
		}
		System.out.println("date:"+formatter.format(fromCalendar.getTimeInMillis()));
	}

}
