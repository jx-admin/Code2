package com.math;
import java.util.Calendar;


public class Time_scale {

	 /**根据数据范围计算时间轴刻度
	  * <pre>
	  * 单位刻度:一天
	  * 最多刻度数量:7个
	  * <1天:(step:1)0,1
	  * <=7天:(step:1)1,2,3,4,5,6,7
	  * 8天:(step:2)1,3,5,7,9
	  * 29天:(step:5)1,6,11,16,21,26,31
	  * >月:(月)
	  * 3个月:(step:15)1,16,31,56,71,96,111 或者(step:月):1,2,3
	  * 大于年:(step:n年)
	  * 
	  * 
	 * @param start		数据起始位置
	 * @param end		数据结束位置
	 * @param scaleBase	单位刻度
	 * @param isRound	 边缘是否按整刻度
	 * @param maxScales	最多刻度数量
	 * @return
	 */
	public static long[] getListTime_scale(long start,long end,long scaleBase,boolean isRound,int maxScales){
		long datas[];
		long dataArea=end-start;
		int stepField=getDateField(dataArea);
		long step=(dataArea)/maxScales;
		if(step<scaleBase){
			step=scaleBase;
		}
		long fieldValue=getDateFieldInMillis(stepField);
		step=(step+fieldValue-1)/fieldValue;
		datas=new long[(int) ((dataArea+fieldValue*step-1)/(fieldValue*step))];
//		System.out.println("stepField "+ stepField+" fieldValue " +fieldValue+ " step: "+step);
		Calendar fromCalendar = Calendar.getInstance();
		Calendar toCalcendar = Calendar.getInstance();
		fromCalendar.setTimeInMillis(start);
		toCalcendar.setTimeInMillis(end);
		for(int i=0;fromCalendar.before(toCalcendar);++i,fromCalendar.set(stepField, (int) (fromCalendar.get(stepField)+step))){
			datas[i]=fromCalendar.getTimeInMillis();
//			System.out.println(formatter.format(fromCalendar.getTimeInMillis()));
		}
//		System.out.println("date:"+formatter.format(fromCalendar.getTimeInMillis()));
		return datas;
	}
	
	/**时间判断,是天,月还是年的单位.
	 * @param value 如果想3个单位以内按下一级显示,可以通过value/3.(比如3月内按天,3年按月.或着value/2.......)
	 * @return DateField of Calendar
	 */
	public static int getDateField(long value){
		if(value>ONEYEAR){
			return Calendar.YEAR;
		}else if(value>ONEMONTH){
			return Calendar.MONTH;
//		}else if(value>ONEWEEK){
//			return Calendar.WEEK_OF_MONTH;
		}else {
			return Calendar.DAY_OF_MONTH;
		}
	}
	
	/**时间单位,值
	 * @param field DateField of Calendar
	 * @return value inMillis of one(year,month,day so on)
	 */
	public static long getDateFieldInMillis(int field){
		switch(field){
		case Calendar.YEAR:
			return ONEYEAR;
		case Calendar.MONTH:
			return ONEMONTH;
		case Calendar.WEEK_OF_MONTH:
			return ONEWEEK;
		case Calendar.DAY_OF_MONTH:
			return ONEDAY;
		}
		return ONEDAY;
	}
	static final long ONEHOUR=1000*60*60;
	static final long ONEDAY=24*ONEHOUR;
	static final long ONEWEEK=7*ONEDAY;
	static final long ONEMONTH=31*ONEDAY;
	static final long ONEYEAR=366*ONEDAY;

}
