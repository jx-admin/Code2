package com.accenture.mbank.logic;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class TransferObjectSim  extends TransferObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String providerCodes[]=new String[]{"10013","10015","10019","10017","10020","10022"};

	public static Map<String, String> providerNameMap = new HashMap<String, String>();
	static {providerNameMap.put("10013", "TIM");
	providerNameMap.put("10015", "Vodafone");
	providerNameMap.put("10019", "Wind");
	providerNameMap.put("10017", "Tiscali");
	providerNameMap.put("10020", "Tre");
	providerNameMap.put("10022", "Coop Voce");
	}
	
	public static String getProviderName(String providerCode){
		return providerNameMap.get(providerCode);
	}
	
	private static final String JSONNAME="recentTransfer";// recentTransfer TransferObject


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

	////
	
	/**-          beneficiaryNumber The Phone Number of the Beneficiary M String*/
	  	
	String beneficiaryNumber;
	
	/**-          beneficiaryProvider The Provider of the Beneficiary M String */
	String beneficiaryProvider;

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
	
	

	public String getBeneficiaryNumber() {
		return beneficiaryNumber;
	}


	public void setBeneficiaryNumber(String beneficiaryNumber) {
		this.beneficiaryNumber = beneficiaryNumber;
	}


//	public String getBeneficiaryProvider() {
//		return beneficiaryProvider;
//	}
	
	public String getBeneficiaryProviderCode() {
		return beneficiaryProvider;
	}


	public String getBeneficiaryProviderName() {
		if(providerNameMap.containsKey(beneficiaryProvider)){
			return providerNameMap.get(beneficiaryProvider);
		}else{
			return beneficiaryProvider;
		}
	}


	public void setBeneficiaryProvider(String beneficiaryProvider) {
		this.beneficiaryProvider = beneficiaryProvider;
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

    	setBeneficiaryNumber(jsonObject.optString("beneficiaryNumber"));

    	setBeneficiaryProvider(jsonObject.optString("beneficiaryProvider"));
	}

	public String getJsonName(){
		return JSONNAME;
	}

}
