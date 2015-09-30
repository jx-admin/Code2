package com.accenture.mbank.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpState extends JsonAbstract {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/***otpState Object containing information about the OTP State M optState Object*/ 
	private static final String JSONNAME="otpState";


	/**** otpErrorCode Error Code Int */ 
	int otpErrorCode;

	/**** otpErrorDescription Error Description String */ 
	String otpErrorDescription;

	/**** otpKeySession The value of the key session  String */ 
	String otpKeySession;

	/**** otpAvailable In case of wrong otp (otpState.errorCode not “12” and “00”) contains the remaining attempts. Int*/
	int otpAvailable;
	
	public int getOtpErrorCode() {
		return otpErrorCode;
	}

	public void setOtpErrorCode(int otpErrorCode) {
		this.otpErrorCode = otpErrorCode;
	}

	public String getOtpErrorDescription() {
		return otpErrorDescription;
	}

	public void setOtpErrorDescription(String otpErrorDescription) {
		this.otpErrorDescription = otpErrorDescription;
	}

	public String getOtpKeySession() {
		return otpKeySession;
	}

	public void setOtpKeySession(String otpKeySession) {
		this.otpKeySession = otpKeySession;
	}

	public int getOtpAvailable() {
		return otpAvailable;
	}

	public void setOtpAvailable(int otpAvailable) {
		this.otpAvailable = otpAvailable;
	}

	@Override
	public void parseJson(JSONObject jsonObject) throws JSONException {
		
		setOtpErrorCode(jsonObject.optInt("otpErrorCode"));

		setOtpErrorDescription(jsonObject.optString("otpErrorDescription"));

		setOtpKeySession(jsonObject.optString("otpKeySession"));

		setOtpAvailable(jsonObject.optInt("otpAvailable"));
	}

	@Override
	public String getJsonName() {
		// TODO Auto-generated method stub
		return JSONNAME;
	}

}
