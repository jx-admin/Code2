package com.accenture.mbank.logic;

import org.json.JSONException;
import org.json.JSONObject;

import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;

public class InsertTransferResponse extends JsonBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static final String JSONNAME_TRANSFER="InsertTransferResponse";// recentTransfer TransferObject
	private static final String JSONNAME2_PHONE_TOP_UP="SimTopUpResponse";
	private String jsonName;

	String transactionId;

	/***otpState Object containing information about the OTP State M optState Object*/ 
	OtpState otpState;

	String otpByEmail;
	
	public InsertTransferResponse(TransferType mTransferType){
		if(mTransferType==TransferType.PHONE_TOP_UP){
			jsonName=JSONNAME2_PHONE_TOP_UP;
		}else if(mTransferType==TransferType.BANK_TRANSFER||mTransferType==TransferType.TRANSFER_ENTRY){
			jsonName=JSONNAME_TRANSFER;
		}else if(mTransferType==TransferType.CARD_TOP_UP){
			jsonName="InsertRechargeCardResponse";
		}
		
	}
	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public OtpState getOtpState() {
		return otpState;
	}

	public void setOtpState(OtpState otpState) {
		this.otpState = otpState;
	}

	public String getOtpByEmail() {
		return otpByEmail;
	}

	public void setOtpByEmail(String otpByEmail) {
		this.otpByEmail = otpByEmail;
	}
	
	@Override
	public void parseJson(JSONObject jsonObject) throws JSONException {
		super.parseJson(jsonObject);
		JSONObject optStateObj = jsonObject.optJSONObject("otpState");
		if(optStateObj!=null){
			this.otpState=new OtpState();
			this.otpState.parseJson(optStateObj);
		}
		setOtpByEmail(jsonObject.optString("otpByEmail"));
	}

	@Override
	public String getJsonName() {
		// TODO Auto-generated method stub
		return jsonName;
	}

}
