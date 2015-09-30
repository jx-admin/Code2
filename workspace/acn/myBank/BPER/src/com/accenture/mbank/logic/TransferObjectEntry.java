package com.accenture.mbank.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferObjectEntry  extends TransferObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String JSONNAME="recentTransfer";// recentTransfer TransferObject

	/***-          beneficiaryIban The IBAN code of the Beneficiary M String */
	private String beneficiaryIban;

	/**** baneficiaryState The ID of the state of the beneficiary. Mandatory only for Bank Transfer (i.e. IT) String*/ 
	private String baneficiaryState;

	/**** beneficiaryBic The BIC code of the beneficiary String */
	private String beneficiaryBic;

	public String getBeneficiaryIban() {
		return beneficiaryIban;
	}

	public void setBeneficiaryIban(String beneficiaryIban) {
		this.beneficiaryIban = beneficiaryIban;
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

	public void parseJson(JSONObject jsonObject) throws JSONException {
       super.parseJson(jsonObject);

       setBeneficiaryIban(jsonObject.optString("beneficiaryIban"));

       setBaneficiaryState(jsonObject.optString("baneficiaryState"));

       setBeneficiaryBic(jsonObject.optString("beneficiaryBic"));
	}

	public String getJsonName(){
		return JSONNAME;
	}

}
