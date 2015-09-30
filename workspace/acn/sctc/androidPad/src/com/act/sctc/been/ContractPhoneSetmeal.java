package com.act.sctc.been;

import java.io.Serializable;

/**合约机套餐
 * @author junxu.wang
 *
 */
public class ContractPhoneSetmeal implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int IN_SHOPPING_CAR=1,IN_MAKEY=2;
	/**
	 * 月消费
	 */
	private int monthlyRent;
	/**
	 * 手机款
	 */
	private int phonePrice;
	/**
	 * 总赠送话费
	 */
	private int totalFreeRent;
	/**
	 * 入网当月返还
	 */
	private int freeRentOnce;
	/**
	 * 次月起%d个月返还
	 */
	private int freeRentAfferPerMonth;
	/**
	 * 合约月数
	 */
	private int contractMonth;
	/**
	 * 合约名称
	 */
	private String title;
	/**
	 * 状态
	 */
	private int status;
	

	/**
	 * 6 titles
	 */
	public String[]titles;
	/**
	 * 6 contents
	 */
	public String[]contents;
	public int getMonthlyRent() {
		return monthlyRent;
	}
	public void setMonthlyRent(int setmealRent) {
		this.monthlyRent = setmealRent;
	}
	public int getPhonePrice() {
		return phonePrice;
	}
	public void setPhonePrice(int phonePrice) {
		this.phonePrice = phonePrice;
	}
	public int getTotalFreeRent() {
		return totalFreeRent;
	}
	public void setTotalFreeRent(int totalFreeRent) {
		this.totalFreeRent = totalFreeRent;
	}
	public int getFreeRentOnce() {
		return freeRentOnce;
	}
	public void setFreeRentOnce(int freeRentOnce) {
		this.freeRentOnce = freeRentOnce;
	}
	public int getFreeRentAfferPerMonth() {
		return freeRentAfferPerMonth;
	}
	public void setFreeRentAfferPerMonth(int freeRentAfferPerMonth) {
		this.freeRentAfferPerMonth = freeRentAfferPerMonth;
	}
	public int getContractMonth() {
		return contractMonth;
	}
	public void setContractMonth(int contractMonth) {
		this.contractMonth = contractMonth;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

}
