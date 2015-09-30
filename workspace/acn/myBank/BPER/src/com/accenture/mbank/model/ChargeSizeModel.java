
package com.accenture.mbank.model;

import java.io.Serializable;

/**
 * ChargeSize object retrived by the getCompanyAmount interface
 * 
 * @author yang.c.li
 */
public class ChargeSizeModel implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rechargeAmount;

    private String commissionAmount;

    private String rechargeDescription;

    /**
     * @return the rechargeAmount
     */
    public String getRechargeAmount() {
        return rechargeAmount;
    }

    /**
     * @param rechargeAmount the rechargeAmount to set
     */
    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    /**
     * @return the commissionAmount
     */
    public String getCommissionAmount() {
        return commissionAmount;
    }

    /**
     * @param commissionAmount the commissionAmount to set
     */
    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    /**
     * @return the rechargeDescription
     */
    public String getRechargeDescription() {
        return rechargeDescription;
    }

    /**
     * @param rechargeDescription the rechargeDescription to set
     */
    public void setRechargeDescription(String rechargeDescription) {
        this.rechargeDescription = rechargeDescription;
    }

}
