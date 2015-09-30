package java.util;

public class Calendar {
	
	private long time;

	public final static int YEAR = 1;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * month. This is a calendar-specific value. The first month of the year in
	 * the Gregorian and Julian calendars is <code>JANUARY</code> which is 0;
	 * the last depends on the number of months in a year.
	 * 
	 * @see #JANUARY
	 * @see #FEBRUARY
	 * @see #MARCH
	 * @see #APRIL
	 * @see #MAY
	 * @see #JUNE
	 * @see #JULY
	 * @see #AUGUST
	 * @see #SEPTEMBER
	 * @see #OCTOBER
	 * @see #NOVEMBER
	 * @see #DECEMBER
	 * @see #UNDECIMBER
	 */
	public final static int MONTH = 2;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * day of the month. This is a synonym for <code>DAY_OF_MONTH</code>. The
	 * first day of the month has value 1.
	 * 
	 * @see #DAY_OF_MONTH
	 */
	public final static int DATE = 5;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * day of the month. This is a synonym for <code>DATE</code>. The first
	 * day of the month has value 1.
	 * 
	 * @see #DATE
	 */
	public final static int DAY_OF_MONTH = 5;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * day of the week. This field takes values <code>SUNDAY</code>,
	 * <code>MONDAY</code>, <code>TUESDAY</code>, <code>WEDNESDAY</code>,
	 * <code>THURSDAY</code>, <code>FRIDAY</code>, and
	 * <code>SATURDAY</code>.
	 * 
	 * @see #SUNDAY
	 * @see #MONDAY
	 * @see #TUESDAY
	 * @see #WEDNESDAY
	 * @see #THURSDAY
	 * @see #FRIDAY
	 * @see #SATURDAY
	 */
	public final static int DAY_OF_WEEK = 7;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating
	 * whether the <code>HOUR</code> is before or after noon. E.g., at
	 * 10:04:15.250 PM the <code>AM_PM</code> is <code>PM</code>.
	 * 
	 * @see #AM
	 * @see #PM
	 * @see #HOUR
	 */
	public final static int AM_PM = 9;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * hour of the morning or afternoon. <code>HOUR</code> is used for the
	 * 12-hour clock (0 - 11). Noon and midnight are represented by 0, not by
	 * 12. E.g., at 10:04:15.250 PM the <code>HOUR</code> is 10.
	 * 
	 * @see #AM_PM
	 * @see #HOUR_OF_DAY
	 */
	public final static int HOUR = 10;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * hour of the day. <code>HOUR_OF_DAY</code> is used for the 24-hour
	 * clock. E.g., at 10:04:15.250 PM the <code>HOUR_OF_DAY</code> is 22.
	 * 
	 * @see #HOUR
	 */
	public final static int HOUR_OF_DAY = 11;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * minute within the hour. E.g., at 10:04:15.250 PM the <code>MINUTE</code>
	 * is 4.
	 */
	public final static int MINUTE = 12;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * second within the minute. E.g., at 10:04:15.250 PM the
	 * <code>SECOND</code> is 15.
	 */
	public final static int SECOND = 13;

	/**
	 * Field number for <code>get</code> and <code>set</code> indicating the
	 * millisecond within the second. E.g., at 10:04:15.250 PM the
	 * <code>MILLISECOND</code> is 250.
	 */
	public final static int MILLISECOND = 14;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Sunday.
	 */
	public final static int SUNDAY = 1;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Monday.
	 */
	public final static int MONDAY = 2;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Tuesday.
	 */
	public final static int TUESDAY = 3;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Wednesday.
	 */
	public final static int WEDNESDAY = 4;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Thursday.
	 */
	public final static int THURSDAY = 5;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Friday.
	 */
	public final static int FRIDAY = 6;

	/**
	 * Value of the {@link #DAY_OF_WEEK} field indicating Saturday.
	 */
	public final static int SATURDAY = 7;

	/**
	 * Value of the {@link #MONTH} field indicating the first month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int JANUARY = 0;

	/**
	 * Value of the {@link #MONTH} field indicating the second month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int FEBRUARY = 1;

	/**
	 * Value of the {@link #MONTH} field indicating the third month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int MARCH = 2;

	/**
	 * Value of the {@link #MONTH} field indicating the fourth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int APRIL = 3;

	/**
	 * Value of the {@link #MONTH} field indicating the fifth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int MAY = 4;

	/**
	 * Value of the {@link #MONTH} field indicating the sixth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int JUNE = 5;

	/**
	 * Value of the {@link #MONTH} field indicating the seventh month of the
	 * year in the Gregorian and Julian calendars.
	 */
	public final static int JULY = 6;

	/**
	 * Value of the {@link #MONTH} field indicating the eighth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int AUGUST = 7;

	/**
	 * Value of the {@link #MONTH} field indicating the ninth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int SEPTEMBER = 8;

	/**
	 * Value of the {@link #MONTH} field indicating the tenth month of the year
	 * in the Gregorian and Julian calendars.
	 */
	public final static int OCTOBER = 9;

	/**
	 * Value of the {@link #MONTH} field indicating the eleventh month of the
	 * year in the Gregorian and Julian calendars.
	 */
	public final static int NOVEMBER = 10;

	/**
	 * Value of the {@link #MONTH} field indicating the twelfth month of the
	 * year in the Gregorian and Julian calendars.
	 */
	public final static int DECEMBER = 11;

	/**
	 * Value of the {@link #AM_PM} field indicating the period of the day from
	 * midnight to just before noon.
	 */
	public final static int AM = 0;

	/**
	 * Value of the {@link #AM_PM} field indicating the period of the day from
	 * noon to just before midnight.
	 */
	public final static int PM = 1;

	public final Date getTime() {
		return null;
	}

	public final void setTime(Date date) {

	}

	public final int get(int field) {
		return 0;
	}

	public final void set(int field, int value) {

	}

	public static Calendar getInstance() {
		return null;
	}

	private Calendar() {

	}
	
	public static Calendar getInstance(TimeZone zone)
	{
		return null;
	}
	
	public TimeZone getTimeZone()
	{
		return null;
	}
	
	public void setTimeInMillis( long millis ) {
		this.time = millis;
		computeFields();
	}
    
	public void computeFields(){}

	}
