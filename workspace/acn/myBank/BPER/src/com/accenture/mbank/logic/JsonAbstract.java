package com.accenture.mbank.logic;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author junxu.wang
 *
 */
public abstract class JsonAbstract implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SUCCESS_CODE = 0;
	public abstract void parseJson( JSONObject jsonObject) throws JSONException;
	public void parseJson(String jsonString){
		if (jsonString == null) {
			return;
		}
		try {
			JSONObject jsonObj = new JSONObject(jsonString);
			jsonObj = jsonObj.optJSONObject(getJsonName());
			parseJson(jsonObj);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public abstract String getJsonName();
}
