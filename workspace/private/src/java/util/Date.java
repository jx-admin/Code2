package java.util;

import java.util.Calendar;

public class Date {
	
    public final static String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public final static String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	
	private long fastTime;
	
	public Date()
	{
		
	}
	
	public long getTime(){
		return 0;
	}

	public Date(long l){
		
	}
	
	public void setTime(long time)
	{
		
	}

  public String toString(){

  	    
  	  	Calendar calendar = Calendar.getInstance();
        if (calendar != null) {
            calendar.setTimeInMillis(fastTime);
        }else{
        	return "Thu Jan 01 00:00:00 UTC 1970";
        }

        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour_of_day = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int year = calendar.get(Calendar.YEAR);

        String yr = Integer.toString(year);

//        TimeZone zone = calendar.getTimeZone();
//        String zoneID = zone.getID();
//        if (zoneID == null) zoneID = "";
        String zoneID = "";
        // The total size of the string buffer
        // 3+1+3+1+2+1+2+1+2+1+2+1+zoneID.length+1+yr.length
        //  = 21 + zoneID.length + yr.length
        StringBuffer sb = new StringBuffer(25 + zoneID.length() + yr.length());

        sb.append(days[dow-1]).append(' ');
        sb.append(months[month]).append(' ');
        appendTwoDigits(sb, day).append(' ');
        appendTwoDigits(sb, hour_of_day).append(':');
        appendTwoDigits(sb, minute).append(':');
        appendTwoDigits(sb, seconds).append(' ');
        if (zoneID.length() > 0) sb.append(zoneID).append(' ');
        appendFourDigits(sb, year);

        return sb.toString();
  }
  
  private static final StringBuffer appendTwoDigits(StringBuffer sb, int number) {
      if (number < 10) {
          sb.append('0');
      }
      return sb.append(number);
  }
  
  private static final StringBuffer appendFourDigits(StringBuffer sb, int number) {
      if (number >= 0 && number < 1000) {
          sb.append('0');
          if (number < 100) {
              sb.append('0');
          }
          if (number < 10) {
              sb.append('0');
          }
      }
      return sb.append(number);
  }

}
