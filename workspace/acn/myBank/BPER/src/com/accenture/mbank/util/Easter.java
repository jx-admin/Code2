package com.accenture.mbank.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Easter {
 	public static class YearOutOfRangeException extends Exception {
 		private static final long serialVersionUID = 5394938690797595980L;
 	}
 
 	public final static boolean isEaster(long millis) 
// 		throws YearOutOfRangeException 
 		{
 
 		Calendar calendar = new GregorianCalendar();
 		calendar.setTimeInMillis(millis);
 
 		int year = calendar.get(Calendar.YEAR);
 		int dateYMD = year * 10000 + 
 					calendar.get(Calendar.MONTH) * 100 +  
 					calendar.get(Calendar.DAY_OF_MONTH);
 		Date easter = find(year);
 		calendar.setTime(easter);
 		int easterYMD = year * 10000 + 
 					calendar.get(Calendar.MONTH) * 100 +  
 					calendar.get(Calendar.DAY_OF_MONTH);
 		return ( easterYMD == dateYMD );
 	}
 
 
 	public final static Date find(int year) 
// 		throws YearOutOfRangeException
 		{
 
 		if ( (year < 1573) || (year > 2499) ) {
// 			throw new Easter.YearOutOfRangeException();
 			return new Date();
 		}
 
 		int a = year % 19;
 		int b = year % 4;
 		int c = year % 7;
 
 		int m = 0;
 		int n = 0;
 
 		if ( (year >= 1583) && (year <= 1699) ) { m = 22; n = 2; }
 		if ( (year >= 1700) && (year <= 1799) ) { m = 23; n = 3; }
 		if ( (year >= 1800) && (year <= 1899) ) { m = 23; n = 4; }
 		if ( (year >= 1900) && (year <= 2099) ) { m = 24; n = 5; }
 		if ( (year >= 2100) && (year <= 2199) ) { m = 24; n = 6; }
 		if ( (year >= 2200) && (year <= 2299) ) { m = 25; n = 0; }
 		if ( (year >= 2300) && (year <= 2399) ) { m = 26; n = 1; }
 		if ( (year >= 2400) && (year <= 2499) ) { m = 25; n = 1; }
 
 		int d = (19 * a + m) % 30;
 		int e = (2 * b + 4 * c + 6 * d + n) % 7;   
 
 		Calendar calendar = new GregorianCalendar();
 
 		if ( d+e < 10 ) {
 			calendar.set(Calendar.YEAR , year);
 			calendar.set(Calendar.MONTH , Calendar.MARCH);
 			calendar.set(Calendar.DAY_OF_MONTH, d + e + 22);
 		} else {
 			calendar.set(Calendar.MONTH , Calendar.APRIL);
 			int day = d+e-9;
 			if ( 26 == day ) {day = 19;}
 			if ( ( 25 == day ) && ( 28 == d) && ( e == 6 ) && ( a > 10 ) ) { day = 18; }
 			calendar.set(Calendar.DAY_OF_MONTH, day);
 		}
 
 		return calendar.getTime();
 	}
 }
