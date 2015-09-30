package com.accenture.mbank.logic;

import org.json.JSONException;
import org.json.JSONObject;

import com.accenture.mbank.model.BankInformationModel;
import com.accenture.mbank.model.CheckTransferResponseModel;
import com.accenture.mbank.model.DestaccountModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

/**
 * @author junxu.wang
 *
 */
public abstract class JsonBase extends JsonAbstract{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4423598600118581196L;

	private String transferId;

    private int resultCode=-1;

    private String resultDescription;

    private EventManagement mEventManagement;

	public EventManagement getEventManagement() {
		return mEventManagement;
	}

	public void setEventManagement(EventManagement mEventManagement) {
		this.mEventManagement = mEventManagement;
	}

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

	public int getResultCode() {
		return resultCode;
	}
	
	public boolean isSuccess(){
		return resultCode==SUCCESS_CODE;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDescription() {
		return resultDescription;
	}

	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}
	
	public void parseJson(JSONObject jsonObject) throws JSONException{
		if (jsonObject == null) {
            return;
        }
		setTransferId(jsonObject.optString("transactionId"));
		setResultCode(jsonObject.optInt("resultCode"));
		setResultDescription(jsonObject.optString("resultDescription"));
		JSONObject eventManagementObj = jsonObject.optJSONObject("eventManagement");
		if(eventManagementObj!=null){
			mEventManagement=new EventManagement();
			mEventManagement.parseJson(eventManagementObj);
		}
	}
	
	public static CheckTransferResponseModel ParseCheckTransferResponse(String json) {
		if (json == null) {
			return null;
		}
		CheckTransferResponseModel checkTransferResponse = new CheckTransferResponseModel();
		BankInformationModel bankInformationModel = new BankInformationModel();
		
		try {
			JSONObject jsonObj = new JSONObject(json);
			JSONObject checkTransferResponseObj = jsonObj.optJSONObject("CheckTransferResponse");
			if(checkTransferResponseObj!=null){
				checkTransferResponse.setResultCode(checkTransferResponseObj.optInt("resultCode"));
				checkTransferResponse.setResultDescription(checkTransferResponseObj.optString("resultDescription"));
				JSONObject eventManagementObj = checkTransferResponseObj.optJSONObject("eventManagement");
				if(eventManagementObj!=null){
					checkTransferResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
					checkTransferResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
				}
				//	            }
			}
			
			JSONObject bankInformationObj = checkTransferResponseObj.optJSONObject("bankInformation");
			if(bankInformationObj!=null){
				bankInformationModel.setSrcBankName(bankInformationObj.optString("srcBankName"));
				bankInformationModel.setSrcBranchName(bankInformationObj.optString("srcBranchName"));
				bankInformationModel.setDestBankName(bankInformationObj.optString("destBankName"));
				bankInformationModel.setDestBranchName(bankInformationObj.optString("destBranchName"));
			}
			checkTransferResponse.setBankInfomation(bankInformationModel);
			checkTransferResponse.setTransferId(checkTransferResponseObj.optString("transferId"));
			checkTransferResponse.setCharges(checkTransferResponseObj.optDouble("charges"));
			
		} catch (Exception e) {
			LogManager.e("ParseCheckTransfer is error " + e.getLocalizedMessage());
		}
		return checkTransferResponse;
	}

	    public static String CheckTransferReportProtocal(String accountCode, double amount,
	            String purposecurrency, String transferType, String purposeDescription,
	            String executionDate, DestaccountModel destaccount, RequestPublicModel publicModel,String currency,String transactionId) {
	        String resultStr = null;
	        try {
	            JSONObject jsonObj = new JSONObject();
	            JSONObject checkTransferObj = new JSONObject();

	            checkTransferObj.put("serviceType", ServiceType.checkTransfer);
	            checkTransferObj.put("bankName", publicModel.getBankName());
	            checkTransferObj.put("enterpriseId", publicModel.getEnterpriseId());
	            checkTransferObj.put("accountCode", accountCode);
	            checkTransferObj.put("amount", amount);
	            checkTransferObj.put("purposeCurrency", purposecurrency);
	            checkTransferObj.put("transferType", transferType);
	            checkTransferObj.put("purposeDescription", purposeDescription);
	            checkTransferObj.put("executionDate", executionDate);
	            checkTransferObj.put("sessionId", publicModel.getSessionId());
	            checkTransferObj.put("token", publicModel.getToken());
	            
	            JSONObject destaccountObj = new JSONObject();
	            destaccountObj.put("title", destaccount.getTitle());
	            destaccountObj.put("state", destaccount.getState());
	            destaccountObj.put("iban", destaccount.getIban());
	            destaccountObj.put("bic", destaccount.getBic());
	            
	            checkTransferObj.put("destAccount", destaccountObj);

	            checkTransferObj.put("customerNumber", publicModel.getCustomerNumber());
	            checkTransferObj.put("channel", publicModel.getChannel());
	            checkTransferObj.put("userAgent", publicModel.getUserAgent());
	            checkTransferObj.put("currency",currency);
	            checkTransferObj.put("transactionId", transactionId);

	            jsonObj.put("CheckTransferRequest", checkTransferObj);
	            resultStr = jsonObj.toString();

	            LogManager.d("CheckTransferRequest  :" + resultStr);
	        } catch (Exception e) {
	            LogManager.e("CheckTransferReportProtocal is error" + e.getLocalizedMessage());
	        }
	        return resultStr;
	    }

}
