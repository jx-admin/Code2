
package com.act.mbanking.bean;

import java.io.Serializable;
import java.text.ParseException;

import com.act.mbanking.utils.TimeUtil;

public class ChartModel implements Comparable<ChartModel>, Cloneable, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int investmentId;

    private String date;

    private double value;

    /**
     * 叠加之前
     */
    private double beforeAdded;

    /**
     * 叠加之后
     */
    private double afterAdded;

    public double getBeforeAdded() {
        return beforeAdded;
    }

    public void setBeforeAdded(double beforeAdded) {
        this.beforeAdded = beforeAdded;
    }

    public double getAfterAdded() {
        return afterAdded;
    }

    public void setAfterAdded(double afterAdded) {
        this.afterAdded = afterAdded;
    }

    /**
     * @return the investmentId
     */
    public int getInvestmentId() {
        return investmentId;
    }

    /**
     * @param investmentId the investmentId to set
     */
    public void setInvestmentId(int investmentId) {
        this.investmentId = investmentId;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    public long getTimeMills() {

        long time = -1;
        try {
            time = TimeUtil.getTimeByString(getDate(), TimeUtil.dateFormat2);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 返回2012-11-12格式
     * 
     * @return
     */
    public String getDay() {

        long time = getTimeMills();
        String day = TimeUtil.getDateString(time, TimeUtil.dateFormat4);
        return day;

    }

    @Override
    public int compareTo(ChartModel another) {

        if (another == null) {
            return 0;
        }
        try {
            long anotherTime = TimeUtil.getTimeByString(another.getDate(), TimeUtil.dateFormat2);
            long thisTime = TimeUtil.getTimeByString(getDate(), TimeUtil.dateFormat2);
            if (thisTime > anotherTime) {
                return 1;
            } else if (thisTime < anotherTime) {
                return -1;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    
    private static final String fmatToString="investmentId:%d, date:%s";// , value:%d,beforeAdded:%d, afterAdded:%d";
    public String toString(){
    	String str=String.format(fmatToString, investmentId,date);//,value,beforeAdded,afterAdded);
    	return str;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public ChartModel clone() {
        ChartModel chartModel = new ChartModel();
        chartModel.date = this.date;
        chartModel.value = this.value;
        chartModel.investmentId = this.investmentId;
        chartModel.afterAdded = this.afterAdded;
        chartModel.beforeAdded = this.beforeAdded;
        return chartModel;

    }

}
