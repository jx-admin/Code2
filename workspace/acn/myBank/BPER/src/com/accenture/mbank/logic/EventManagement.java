package com.accenture.mbank.logic;

import org.json.JSONException;
import org.json.JSONObject;

public class EventManagement extends JsonAbstract{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String JSONNAME="EventManagement";

    private String errorCode;

    private String errorDescription;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public void parseJson(JSONObject jsonObject) throws JSONException {
        setErrorCode(jsonObject.optString("errorCode"));
        setErrorDescription(jsonObject.optString("errorDescription"));
	}


	public String getJsonName(){
		return JSONNAME;
	}
}
