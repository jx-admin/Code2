
package com.accenture.mbank.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.accenture.mbank.logic.JsonAbstract;


public class DestaccountModel extends JsonAbstract {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /***destAccount This parameter identifies the Account of the transfer recipient  M Account Object */
    private static final String JSONNAME="destAccount";
	
    /*** title The name of the beneficiary.  M 40 String */
    private String title;

    /*** address The address of the beneficiary O 32 String */
    private String address;

    /*** cap The CAP of the beneficiary O 5 String */
    private String cap;

    /*** city The city of the beneficiary O 27 String */
    private String city;

    /*** province The province of the beneficiary O 2 String */
    private String province;

    /*** state The ID of the state of the beneficiary.  M 2 String */
    private String state;

    /*** iban The IBAN code og the beneficiary M 34 String */
    private String iban;

    /*** bic The BIC code of the beneficiary M** 11 String */
    private String bic;

    /*** cup The CUP code of the beneficiary O*** 15 String */
    private String cup;

    /*** cig The CIG code of the beneficiary O*** 10 String */
    private String cig;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getState() {
		if(TextUtils.isEmpty(state)&&iban!=null&&iban.length()>=2){
			setState(iban.substring(0, 2));
		}
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getCup() {
		return cup;
	}

	public void setCup(String cup) {
		this.cup = cup;
	}

	public String getCig() {
		return cig;
	}

	public void setCig(String cig) {
		this.cig = cig;
	}

	@Override
	public void parseJson(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getJsonName() {
		// TODO Auto-generated method stub
		return JSONNAME;
	}
	
	public JSONObject getProtocol() throws JSONException{
        JSONObject destaccountObj = new JSONObject();
        if(!TextUtils.isEmpty(getTitle())){
        	destaccountObj.put("title", getTitle());
        }
        if(!TextUtils.isEmpty(getAddress())){
        	destaccountObj.put("address", getAddress());
        }
        if(!TextUtils.isEmpty(getCap())){
        	destaccountObj.put("cap", getCap());
        }
        if(!TextUtils.isEmpty(getIban())){
        	destaccountObj.put("city", getCity());
        }
        if(!TextUtils.isEmpty(getProvince())){
        	destaccountObj.put("province", getProvince());
        }
        if(!TextUtils.isEmpty(getState())){
        	destaccountObj.put("state", getState());
        }
        if(!TextUtils.isEmpty(getIban())){
        	destaccountObj.put("iban", getIban());
        }
        if(!TextUtils.isEmpty(getBic())){
        	destaccountObj.put("bic", getBic());
        }
        if(!TextUtils.isEmpty(getCup())){
        	destaccountObj.put("cup", getCup());
        }
        if(!TextUtils.isEmpty(getCig())){
        	destaccountObj.put("cig", getCig());
        }
        return destaccountObj;
	}

}
