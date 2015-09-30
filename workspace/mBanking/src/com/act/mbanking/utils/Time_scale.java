package com.act.mbanking.utils;
import java.util.Calendar;


public class Time_scale {

	 /**�����ݷ�Χ����ʱ����̶�
	  * <pre>
	  * ��λ�̶�:һ��
	  * ���̶�����:7��
	  * <1��:(step:1)0,1
	  * <=7��:(step:1)1,2,3,4,5,6,7
	  * 8��:(step:2)1,3,5,7,9
	  * 29��:(step:5)1,6,11,16,21,26,31
	  * >��:(��)
	  * 3����:(step:15)1,16,31,56,71,96,111 ����(step:��):1,2,3
	  * ������:(step:n��)
	  * 
	  * 
	 * @param start		�����ʼλ��
	 * @param end		��ݽ���λ��
	 * @param scaleBase	��λ�̶�
	 * @param isRound	 ��Ե�Ƿ���̶�
	 * @param maxScales	���̶�����
	 * @return
	 */
	public static long[] getListTime_scale(long start,long end,long scaleBase,boolean isRound,int maxScales){
		long datas[];
		long dataArea=end-start;
		if(dataArea==0){
			dataArea=scaleBase;
			datas=new long[]{start,end};
			return datas;
		}
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
		datas[datas.length-1]=toCalcendar.getTimeInMillis();
//		System.out.println("date:"+formatter.format(fromCalendar.getTimeInMillis()));
		return datas;
	}
	
	/**ʱ���ж�,����,�»�����ĵ�λ.
	 * @param value �����3����λ���ڰ���һ����ʾ,����ͨ��value/3.(����3���ڰ���,3�갴��.����value/2.......)
	 * @return DateField of Calendar
	 */
	public static int getDateField(long value){
		value/=4;
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
	
	/**ʱ�䵥λ,ֵ
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
	public static final long ONEHOUR=1000*60*60;
	public static final long ONEDAY=24*ONEHOUR;
	public static final long ONEWEEK=7*ONEDAY;
	public static final long ONEMONTH=31*ONEDAY;
	public static final long ONEYEAR=366*ONEDAY;

}
