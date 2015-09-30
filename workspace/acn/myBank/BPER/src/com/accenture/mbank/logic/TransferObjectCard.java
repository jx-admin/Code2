package com.accenture.mbank.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferObjectCard  extends TransferObject{
	
	private static final String JSONNAME="recentTransfer";// recentTransfer TransferObject

//
//	/**-          data The data of the transfer Date*/ 
//	private long data;
//
//	/**-          account The Account of the transfer String */
//	String account;
//
//	/**-          type The type of the  transfer (fixed value: “BANK TRANSFER”) String */
//	String type;
//
//	/**-          amount The Amount of the transfer Big Decimal */
//	double amount;
//
//	/**-          beneficiaryName The name of the Beneficiary String */
//	String beneficiaryName;
//
//	/**-          description The description of the transfer String */
//	String description;
//
//	/*** transferState The state of the Transfer. Domain:'I', 'R’, 'V','A','S''X'; stands for: 'I'=INOLTRATO 'R'=RIFIUTATO 'V'=REVOCATO 'A'=AUTORIZZATO 'S'=SPEDITO 'X' =ANNULLATO; String */
//	String transferState;
//	
	/////
	
	/**-          beneficiaryCardNumber The Card Number of the Beneficiary M String */
	String beneficiaryCardNumber;

//	public long getData() {
//		return data;
//	}
//
//
//	public void setData(long data) {
//		this.data = data;
//	}
//
//
//	public String getAccount() {
//		return account;
//	}
//
//
//	public void setAccount(String account) {
//		this.account = account;
//	}
//
//
//	public String getType() {
//		return type;
//	}
//
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//
//	public double getAmount() {
//		return amount;
//	}
//
//
//	public void setAmount(double amount) {
//		this.amount = amount;
//	}
//
//
//	public String getBeneficiaryName() {
//		return beneficiaryName;
//	}
//
//
//	public void setBeneficiaryName(String beneficiaryName) {
//		this.beneficiaryName = beneficiaryName;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public String getTransferState() {
//		return transferState;
//	}
//
//
//	public void setTransferState(String transferState) {
//		this.transferState = transferState;
//	}
	

	public String getBeneficiaryCardNumber() {
		return beneficiaryCardNumber;
	}


	public void setBeneficiaryCardNumber(String beneficiaryCardNumber) {
		this.beneficiaryCardNumber = beneficiaryCardNumber;
	}
	/**
     * @return Last4Digits
     */
    public String getLast4Digits(){
        if(beneficiaryCardNumber==null){
            return null;
        }else if(beneficiaryCardNumber.length()<=4){
            return beneficiaryCardNumber;
        }
        return beneficiaryCardNumber.substring(beneficiaryCardNumber.length()-4);
    }



	public void parseJson(JSONObject jsonObject) throws JSONException {
       super.parseJson(jsonObject);
//        setData(jsonObject.optLong("data"));
//
//        setAccount(jsonObject.optString("account"));
//
//        setType(jsonObject.optString("type"));
//
//        setAmount(jsonObject.optDouble("amount"));
//
//        setBeneficiaryName(jsonObject.optString("beneficiaryName"));
//
//        setDescription(jsonObject.optString("description"));
//
//        setTransferState(jsonObject.optString("transferState"));
        //
        setBeneficiaryCardNumber(jsonObject.optString("beneficiaryCardNumber"));
        setBeneficiaryName(jsonObject.optString("beneficiaryCardTitle"));
	}

	public String getJsonName(){
		return JSONNAME;
	}

}
