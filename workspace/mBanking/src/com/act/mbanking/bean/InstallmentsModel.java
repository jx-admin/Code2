package com.act.mbanking.bean;

public class InstallmentsModel {
	private String installmentType;
	private String id;
	private String deadlineDate;
	private double amountCapitalShare;
	private double amountInterestShare;
	private double amount;
	private double applyRate;
	private String paidState;
	/**
	 * @return the installmentType
	 */
	public String getInstallmentType() {
		return installmentType;
	}
	/**
	 * @param installmentType the installmentType to set
	 */
	public void setInstallmentType(String installmentType) {
		this.installmentType = installmentType;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the deadlineDate
	 */
	public String getDeadlineDate() {
		return deadlineDate;
	}
	/**
	 * @param deadlineDate the deadlineDate to set
	 */
	public void setDeadlineDate(String deadlineDate) {
		this.deadlineDate = deadlineDate;
	}
	/**
	 * @return the amountCapitalShare
	 */
	public double getAmountCapitalShare() {
		return amountCapitalShare;
	}
	/**
	 * @param amountCapitalShare the amountCapitalShare to set
	 */
	public void setAmountCapitalShare(double amountCapitalShare) {
		this.amountCapitalShare = amountCapitalShare;
	}
	/**
	 * @return the amountInterestShare
	 */
	public double getAmountInterestShare() {
		return amountInterestShare;
	}
	/**
	 * @param amountInterestShare the amountInterestShare to set
	 */
	public void setAmountInterestShare(double amountInterestShare) {
		this.amountInterestShare = amountInterestShare;
	}
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	/**
	 * @return the applyRate
	 */
	public double getApplyRate() {
		return applyRate;
	}
	/**
	 * @param applyRate the applyRate to set
	 */
	public void setApplyRate(double applyRate) {
		this.applyRate = applyRate;
	}
	/**
	 * @return the paidState
	 */
	public String getPaidState() {
		return paidState;
	}
	/**
	 * @param paidState the paidState to set
	 */
	public void setPaidState(String paidState) {
		this.paidState = paidState;
	}
	
	
}
