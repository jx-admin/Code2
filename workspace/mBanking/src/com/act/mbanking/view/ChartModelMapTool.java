
package com.act.mbanking.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Path;
import android.util.Log;

import com.act.mbanking.bean.ChartModel;
import com.act.mbanking.utils.TimeUtil;
import com.custom.view.RectLD;

/**<pre>
 * List<ChartModel> srcList
 * HashMap<String, ChartModel> srcHashMap;
 * List<ChartModel> outList;
 * 
 * public long from;
 * public long to;
 * @author junxu.wang
 *
 */
public class ChartModelMapTool {
	private static final String TAG="ChartModelMapTool";

    public int level = 0;

    public static final int type_all = 5;

    public static final int type_default = 0;

    public static final int type_investments = 1;

    public static final int type_prepaieds = 2;

    public static final int type_loans = 4;

    public static final int type_credit = 5;

    public static final int type_accounts = 3;

    public int type = 0;
    
    public String title;

//    public ChartModel[] xyValueList;

    Path path;

    public int color;

    private List<ChartModel> srcList;

    public HashMap<String, ChartModel> srcHashMap;

//    public HashMap<String, ChartModel> yearHashMap;
    
    public static final RectLD time_area_year=ChartView.getTimeStyleArea(ChartView.YEAR,null);
    /**
     * src数据范围
     */
    private RectLD time_area_data;
    
    
    public static int getIndex(long time){
    	 int size = (int)((time-time_area_year.left+TimeUtil.ONE_DAY) / TimeUtil.ONE_DAY);
         if(size==0){
         	size=1;
         }
         size=366-size;
         return size; 
    }
    


    /**
     * value by day
     */
//    private List<ChartModel> outList;
    private List<ChartModel> yearList;

    public void setSrcList(List<ChartModel> srcList) {
        this.srcList = srcList;
        if(time_area_data==null){
        	time_area_data=new RectLD();
        }
        time_area_data.left=Long.MAX_VALUE;
        time_area_data.right=Long.MIN_VALUE;
        time_area_data.top=Double.MAX_VALUE;
        time_area_data.bottom=Double.MIN_VALUE;
        
    	generateHashMapAndOutList(true);
    }
    
    public List<ChartModel> getSrcList(){
    	return this.srcList;
    }

//    public List<ChartModel> getOutList() {
//        return outList;
//    }
    
    public List<ChartModel> getYearList() {
    	return yearList;
    }

//    long ONE_DAY = 24 * 60 * 60 * 1000;

//    public ChartModel getOutItem(long time) {
//    	return getNeareastChartModelRight(time,outList);
//    }
    

    /**
     * 比较要时间，请确保只运行一次 把list里的数据通过天来合并起来
     * 
     * @param srcList
     * @return
     */
    private void generateHashMapAndOutList(boolean need) {

        // 暂时不考虑汇总
        if (need) {
        	ChartModel yearChartModel=null;
            srcHashMap = new HashMap<String, ChartModel>();
//            Collections.sort(srcList);
            long tmpTime;
            for (int i = 0; i < srcList.size(); i++) {
                ChartModel srcchartModel = srcList.get(i);
                if(srcchartModel==null){
                	continue;
                }
                if (type == type_credit) {
                    srcchartModel.setValue(-Math.abs(srcchartModel.getValue()));
                }
                tmpTime=srcchartModel.getTimeMills();
                String key = TimeUtil.getDateString(tmpTime, TimeUtil.dateFormat4);
                ChartModel chartModel2 = srcHashMap.get(key);
                if (chartModel2 == null) {
                	chartModel2=srcchartModel.clone();
                    srcHashMap.put(key, chartModel2);
                } else {
                    chartModel2.setValue(chartModel2.getValue() + srcchartModel.getValue());
                }
                
                //area
                if(time_area_data.left>tmpTime){
                	time_area_data.left=tmpTime;
                }else if(time_area_data.right<tmpTime){
                	time_area_data.right=tmpTime;
                }
                if(time_area_data.top>srcchartModel.getValue()){
                	time_area_data.top=srcchartModel.getValue();
                }else if(time_area_data.bottom<srcchartModel.getValue()){
                	time_area_data.bottom=srcchartModel.getValue();
                }
                
                //first in a year
                if(tmpTime<=time_area_year.left){
                	if(yearChartModel==null){
                		yearChartModel=chartModel2;
                	}else if(tmpTime>yearChartModel.getTimeMills()){
                		yearChartModel=chartModel2;
                	}
                }
            }
            
//            if (outList == null) {
//                outList = new ArrayList<ChartModel>();
//            } else {
//                outList.clear();
//            }
//            Iterator cIterator = srcHashMap.entrySet().iterator();
//            while (cIterator.hasNext()) {
//                Map.Entry entry = (Entry)cIterator.next();
//                ChartModel chartModel = (ChartModel)entry.getValue();
//                outList.add(chartModel);
//            }
//
//            if (outList.size()<=1) {
//                return;
//            }
//            Log.d("outIndex","outList size: "+outList.size());
//            Collections.sort(outList);
            
//            yearHashMap=(HashMap<String, ChartModel>) srcHashMap.clone();
           
            //year data
            if(yearList==null){
            	yearList=new ArrayList(ChartView.parseDays(time_area_year.left,time_area_year.right));
            }else{
            	yearList.clear();
            }
            int size=ChartView.parseDays(time_area_year.left,time_area_year.right);
        	if(size==0){
        		size=1;
        	}
        	Calendar calendarFrom = Calendar.getInstance();
    		calendarFrom.setTimeInMillis(time_area_year.left);
    		Calendar calendarTo = Calendar.getInstance();
    		calendarTo.setTimeInMillis(time_area_year.right);
    		ChartModel lastChartModel=yearChartModel;
    		for(int i=0;i<size;++i,calendarFrom.add(Calendar.DAY_OF_MONTH, 1)){
    			ChartModel mChartModel = srcHashMap.get(TimeUtil.getDateString(calendarFrom.getTimeInMillis(), TimeUtil.dateFormat4));
    			if(mChartModel==null){
    				if(lastChartModel!=null){
    					mChartModel=lastChartModel.clone();
    				}else{
    					mChartModel=getNeareastChartModelRight(calendarFrom.getTimeInMillis(), yearList).clone();
    				}
    			}else{
    				mChartModel=mChartModel.clone();
    			}
    			yearList.add(mChartModel);
    			mChartModel.setDate(TimeUtil.getDateString(calendarFrom.getTimeInMillis(),TimeUtil.dateFormat2));
    			
//    				Log.d(TAG,j+" "+i+" paths "+mChartModel.toString());
    			lastChartModel=mChartModel;
    		}
            Collections.sort(yearList);

        } else {
            srcHashMap = new HashMap<String, ChartModel>();
            Collections.sort(srcList);
            for (int i = srcList.size() - 1; i >= 0; i--) {
                ChartModel chartModel = srcList.get(i).clone();

                srcHashMap.put(chartModel.getDay(), chartModel);

            }
//            outList = srcList;
//            Log.d("outIndex","outList size: "+outList.size());
//            Collections.sort(outList);
        }
    }

    /**
     * 拿到那一天的数据
     * 
     * @param day
     * @return
     */
    public ChartModel getNearestChartModel(String day) {

        ChartModel chartModel = null;
        if (srcHashMap != null) {
            chartModel = srcHashMap.get(day);

        }
        return chartModel;
    }
    
    /**
     * @param timeMilines
     * @return
     */
    public ChartModel getChartModelFromDays(long timeMilines){
    	return getNearestChartModel(TimeUtil.getDateString(timeMilines, TimeUtil.dateFormat4));
    }


    /**
     * 查找from延伸数据
     * 
     * @param from
     * @param list
     * @return
     */
    public static ChartModel getNeareastChartModelRight(long from, List<ChartModel> list) {
    	ChartModel result = null;
    	if(list!=null&&list.size()>0){
    		for (int i = list.size() - 1; i >= 0; i--) {
    			
    			ChartModel chartModel = list.get(i);
    			if (chartModel.getTimeMills() <= from) {
    				result = list.get(i);
    				break;
    			}
    		}
    		
    	}
        if (result == null) {
        	result=new ChartModel();
        	result.setDate(TimeUtil.getDateString(from, TimeUtil.dateFormat2));
//        	result=list.get(0);
        }
        return result;
    }
    
    public ChartModel[] generateChartModels(long from,long to,List<ChartModel> list){
    	int size = (int)((to - from+TimeUtil.ONE_DAY) / TimeUtil.ONE_DAY);
    	if(size==0){
    		size=1;
    	}
    	ChartModel[] newList=new ChartModel[size];
//        List<ChartModel> outList=new ArrayList<ChartModel>();

    	Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTimeInMillis(from);
		Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTimeInMillis(to);
		ChartModel lastChartModel=null;
		for(int i=0;i<size;++i,calendarFrom.add(Calendar.DAY_OF_MONTH, 1)){
			ChartModel mChartModel=getChartModelFromDays(calendarFrom.getTimeInMillis());
			if(mChartModel==null){
				if(lastChartModel!=null){
					mChartModel=lastChartModel.clone();
				}else{
					mChartModel=getNeareastChartModelRight(calendarFrom.getTimeInMillis(), list).clone();
				}
			}else{
				mChartModel=mChartModel.clone();
			}
			newList[i]=mChartModel;
//			outList.add(mChartModel);
			mChartModel.setDate(TimeUtil.getDateString(calendarFrom.getTimeInMillis(),TimeUtil.dateFormat2));
			
//				Log.d(TAG,j+" "+i+" paths "+mChartModel.toString());
			lastChartModel=mChartModel;
		}
    	return newList;
    }
    
    public List<ChartModel> generateChartModelsToList(long from,long to){
    	int size = (int)((to - from+TimeUtil.ONE_DAY) / TimeUtil.ONE_DAY);
    	if(size==0){
    		size=1;
    	}
        List<ChartModel> outList=new ArrayList<ChartModel>();

    	Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTimeInMillis(from);
		Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTimeInMillis(to);
		ChartModel lastChartModel=null;
		for(int i=0;i<size;++i,calendarFrom.add(Calendar.DAY_OF_MONTH, 1)){
			ChartModel mChartModel=getChartModelFromDays(calendarFrom.getTimeInMillis());
			if(mChartModel==null){
				if(lastChartModel!=null){
					mChartModel=lastChartModel.clone();
				}else{
					mChartModel=getNeareastChartModelRight(calendarFrom.getTimeInMillis(), outList).clone();
				}
			}else{
				mChartModel=mChartModel.clone();
			}
			outList.add(mChartModel);
			mChartModel.setDate(TimeUtil.getDateString(calendarFrom.getTimeInMillis(),TimeUtil.dateFormat2));
			
//				Log.d(TAG,j+" "+i+" paths "+mChartModel.toString());
			lastChartModel=mChartModel;
		}
    	return outList;
    }

}
