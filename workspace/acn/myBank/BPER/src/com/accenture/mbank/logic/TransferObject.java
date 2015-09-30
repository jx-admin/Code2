package com.accenture.mbank.logic;

import java.util.Comparator;

import org.json.JSONException;
import org.json.JSONObject;

import com.accenture.mbank.util.TimeUtil;

public class TransferObject  extends JsonAbstract{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static final String JSONNAME="recentTransfer";// recentTransfer TransferObject


	/**-          data The data of the transfer Date*/ 
	private long data;

	/**-          account The Account of the transfer String */
	String account;

	/**-          type The type of the  transfer (fixed value: “BANK TRANSFER”) String */
	String type;

	/**-          amount The Amount of the transfer Big Decimal */
	double amount;

	/**-          beneficiaryName The name of the Beneficiary String */
	String beneficiaryName;

	/**-          description The description of the transfer String */
	String description;

	/*** transferState The state of the Transfer. Domain:'I', 'R’, 'V','A','S''X'; stands for: 'I'=INOLTRATO 'R'=RIFIUTATO 'V'=REVOCATO 'A'=AUTORIZZATO 'S'=SPEDITO 'X' =ANNULLATO; String */
	String transferState;

	public long getDate() {
		return data;
	}


	public void setData(long data) {
		this.data = data;
	}


	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getBeneficiaryName() {
		return beneficiaryName;
	}


	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getTransferState() {
		return transferState;
	}


	public void setTransferState(String transferState) {
		this.transferState = transferState;
	}


	public void parseJson(JSONObject jsonObject) throws JSONException {
       
        setData(TimeUtil.getTimeByString(jsonObject.optString("date"),TimeUtil.dateFormat2));

        setAccount(jsonObject.optString("account"));

        setType(jsonObject.optString("type"));

        setAmount(jsonObject.optDouble("amount"));

        setBeneficiaryName(jsonObject.optString("beneficiaryName"));

        setDescription(jsonObject.optString("description"));

        setTransferState(jsonObject.optString("transferState"));
	}

	public String getJsonName(){
		return JSONNAME;
	}
	
	public static Comparator<TransferObject> dataComparable=new  Comparator<TransferObject>() {

		@Override
		public int compare(TransferObject lhs, TransferObject rhs) {
			// TODO Auto-generated method stub
			if(lhs.getDate()>rhs.getDate()){
				return -1;
			}else if(lhs.getDate()<rhs.getDate()){
				return 1;
			}else{
				return 0;
			}
		}
	};


}
