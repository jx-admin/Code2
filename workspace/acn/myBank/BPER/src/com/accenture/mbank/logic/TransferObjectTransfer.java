package com.accenture.mbank.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferObjectTransfer  extends TransferObject{
	
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
	
	/////
	
	
	/**-          beneficiaryIban The IBAN code of the Beneficiary M String */
	  	String beneficiaryIban;


	/*** purposeCurrency Transfer Purpose. Mandatory only for amount > 50.000 and country not IT and RSM; String */
		String purposeCurrency;

	/*** baneficiaryState The ID of the state of the beneficiary. Mandatory only for Bank Transfer (i.e. IT) String */
		String baneficiaryState;

	/*** beneficiaryBic The BIC code of the beneficiary String */
		String beneficiaryBic;

	/*** beneficiaryCUP Inserted CUP O */
		String beneficiaryCUP;

	/*** beneficiaryCIG Inserted CIG O */
		String beneficiaryCIG;

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


	public String getBeneficiaryIban() {
		return beneficiaryIban;
	}


	public void setBeneficiaryIban(String beneficiaryIban) {
		this.beneficiaryIban = beneficiaryIban;
	}


//	public String getDescription() {
//		return description;
//	}
//
//
//	public void setDescription(String description) {
//		this.description = description;
//	}


	public String getPurposeCurrency() {
		return purposeCurrency;
	}


	public void setPurposeCurrency(String purposeCurrency) {
		this.purposeCurrency = purposeCurrency;
	}


	public String getBaneficiaryState() {
		return baneficiaryState;
	}


	public void setBaneficiaryState(String baneficiaryState) {
		this.baneficiaryState = baneficiaryState;
	}


	public String getBeneficiaryBic() {
		return beneficiaryBic;
	}


	public void setBeneficiaryBic(String beneficiaryBic) {
		this.beneficiaryBic = beneficiaryBic;
	}


//	public String getTransferState() {
//		return transferState;
//	}
//
//
//	public void setTransferState(String transferState) {
//		this.transferState = transferState;
//	}


	public String getBeneficiaryCUP() {
		return beneficiaryCUP;
	}


	public void setBeneficiaryCUP(String beneficiaryCUP) {
		this.beneficiaryCUP = beneficiaryCUP;
	}


	public String getBeneficiaryCIG() {
		return beneficiaryCIG;
	}


	public void setBeneficiaryCIG(String beneficiaryCIG) {
		this.beneficiaryCIG = beneficiaryCIG;
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

        setBeneficiaryIban(jsonObject.optString("beneficiaryIban"));

//        setDescription(jsonObject.optString("description"));

        setPurposeCurrency(jsonObject.optString("purposeCurrency"));

        setBaneficiaryState(jsonObject.optString("baneficiaryState"));

        setBeneficiaryBic(jsonObject.optString("beneficiaryBic"));

//        setTransferState(jsonObject.optString("transferState"));
        setBeneficiaryCUP(jsonObject.optString("beneficiaryCUP"));
        setBeneficiaryCIG(jsonObject.optString("beneficiaryCIG"));
	}

	public String getJsonName(){
		return JSONNAME;
	}

}
