package com.math;


public class Value_scale {

	 /**根据数据范围计算数据轴刻度
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
		long step=(dataArea)/maxScales;
		if(step<scaleBase){
			step=scaleBase;
		}
		step=step/10*10;//getDateField(step,10);
		datas=new long[(int) ((dataArea+step)/(step))];
//		System.out.println(" step: "+step);
		for(int i=0;start<end;++i,start+=step){
			datas[i]=start;
//			System.out.println(start);
		}
//		System.out.println("date:"+formatter.format(fromCalendar.getTimeInMillis()));
		return datas;
	}

}
