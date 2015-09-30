package com.act.sctc.been;

import java.io.Serializable;

public class SetMeal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SMEAL_SPECIAL_NET=1;
	public static final int SMEAL_SPECIAL_CALL=2;
	
	public static final int SM_C_YD_QUANQIUTONG=1;
	public static final int SM_C_YD_DONGGANDIDAI=2;
	public static final int SM_C_YD_SHENZHOUXING=3;
	
	private String name;
	private int monthlyRent;
	private int domesticTraffic;
	private int freeInlandCall;
	private String freeInlandCallIntroduce;
	private int freeWifiHour;
	private int freeSMS;
	private int freeMMS;
	private String exceedCall;
	private String exceedTraffic;
	private String freeAnswerRange;
	private String freeServices;
	private int status;
	public final static int IN_SHOPPING_CAR=1,IN_MAKEY=2;
	private double saveRentMonth;
	private double saveRentYear;
	private double saveTrafficMonth;
	private double saveTrafficYear;
	private String vsValue;
	private String vsName="资费优势";
	private int special;
	private int category;
	
	
	
//	vs_name
//	vs_value
	private String desc1;
	private String desc2;
	private int phone_number;
	private long filter_item_id;

	
	
	/**
	 * 6 titles
	 */
	public String[]titles;
	/**
	 * 6 contents
	 */
	public String[]contents;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMonthlyRent() {
		return monthlyRent;
	}
	public void setMonthlyRent(int monthlyRent) {
		this.monthlyRent = monthlyRent;
	}
	public int getDomesticTraffic() {
		return domesticTraffic;
	}
	public void setDomesticTraffic(int domesticTraffic) {
		this.domesticTraffic = domesticTraffic;
	}
	public int getFreeInlandCall() {
		return freeInlandCall;
	}
	public void setFreeInlandCall(int freeInlandCall) {
		this.freeInlandCall = freeInlandCall;
	}
	public String getFreeInlandCallIntroduce() {
		return freeInlandCallIntroduce;
	}
	public void setFreeInlandCallIntroduce(String freeInlandCallIntroduce) {
		this.freeInlandCallIntroduce = freeInlandCallIntroduce;
	}
	public int getFreeWifiHour() {
		return freeWifiHour;
	}
	public void setFreeWifiHour(int freeWifiHour) {
		this.freeWifiHour = freeWifiHour;
	}
	public int getFreeSMS() {
		return freeSMS;
	}
	public void setFreeSMS(int freeSMS) {
		this.freeSMS = freeSMS;
	}
	public int getFreeMMS() {
		return freeMMS;
	}
	public void setFreeMMS(int freeMMS) {
		this.freeMMS = freeMMS;
	}
	public String getExceedCall() {
		return exceedCall;
	}
	public void setExceedCall(String exceedCall) {
		this.exceedCall = exceedCall;
	}
	public String getExceedTraffic() {
		return exceedTraffic;
	}
	public void setExceedTraffic(String exceedTraffic) {
		this.exceedTraffic = exceedTraffic;
	}
	public String getFreeAnswerRange() {
		return freeAnswerRange;
	}
	public void setFreeAnswerRange(String freeAnswerRange) {
		this.freeAnswerRange = freeAnswerRange;
	}
	public String getFreeServices() {
		return freeServices;
	}
	public void setFreeServices(String freeServices) {
		this.freeServices = freeServices;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int state) {
		this.status = state;
	}
	public double getSaveRentMonth() {
		return saveRentMonth;
	}
	public void setSaveRentMonth(double saveCallMonth) {
		this.saveRentMonth = saveCallMonth;
	}
	public double getSaveRentYear() {
		return saveRentYear;
	}
	public void setSaveRentYear(double saveCallYear) {
		this.saveRentYear = saveCallYear;
	}
	public double getSaveTrafficMonth() {
		return saveTrafficMonth;
	}
	public void setSaveTrafficMonth(double saveTrafficMonth) {
		this.saveTrafficMonth = saveTrafficMonth;
	}
	public double getSaveTrafficYear() {
		return saveTrafficYear;
	}
	public void setSaveTrafficYear(double saveTrafficYear) {
		this.saveTrafficYear = saveTrafficYear;
	}
	public String getVSDis() {
		return vsValue;
	}
	public void setVSDis(String vSDis) {
		vsValue = vSDis;
	}
	public int getSpecial() {
		return special;
	}
	public void setSpecial(int special) {
		this.special = special;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getVSTitle() {
		return vsName;
	}
	public void setVSTitle(String vSTitle) {
		vsName = vSTitle;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setDesc1(String desc1) {
		this.desc1 = desc1;
	}
	public String getDesc2() {
		return desc2;
	}
	public void setDesc2(String desc2) {
		this.desc2 = desc2;
	}
	public int getPhoneNumber() {
		return phone_number;
	}
	public void setPhoneNumber(int phone_number) {
		this.phone_number = phone_number;
	}
	public long getFilterItemId() {
		return filter_item_id;
	}
	public void setFilterItemId(long filter_item_id) {
		this.filter_item_id = filter_item_id;
	}

}
