package j.wu.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

	public static void main(String[] args) {
		defaultTimeFormater();
	}

	public static void defaultTimeFormater() {
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.FULL, Locale.getDefault());
		String dataStr = dateFormat.format(new Date());
		String timeStr = timeFormat.format(new Date());
		System.out.println(dataStr);
		System.out.println(timeStr);
		System.out.println();

		dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
		timeFormat = DateFormat.getTimeInstance(DateFormat.LONG, Locale.getDefault());
		dataStr = dateFormat.format(new Date());
		timeStr = timeFormat.format(new Date());
		System.out.println(dataStr);
		System.out.println(timeStr);
		System.out.println();
		

		dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
		timeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault());
		dataStr = dateFormat.format(new Date());
		timeStr = timeFormat.format(new Date());
		System.out.println(dataStr);
		System.out.println(timeStr);
		System.out.println();
		

		dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
		timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
		dataStr = dateFormat.format(new Date());
		timeStr = timeFormat.format(new Date());
		System.out.println(dataStr);
		System.out.println(timeStr);
		System.out.println();
		
		dateFormat = DateFormat.getDateInstance(DateFormat.ERA_FIELD, Locale.getDefault());
		timeFormat = DateFormat.getTimeInstance(DateFormat.ERA_FIELD, Locale.getDefault());
		dataStr = dateFormat.format(new Date());
		timeStr = timeFormat.format(new Date());
		System.out.println(dataStr);
		System.out.println(timeStr);
		System.out.println();
		
		SimpleDateFormat sdateFormat = new SimpleDateFormat("yyyy-MMM-dd EEE HH:mm:ss");
		SimpleDateFormat stimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		dataStr = sdateFormat.format(new Date());
		timeStr = stimeFormat.format(new Date());
		System.out.println(dataStr);
		System.out.println(timeStr);
		System.out.println();
	}
}
