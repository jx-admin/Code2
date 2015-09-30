
package com.accenture.mbank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.accenture.mbank.logic.JsonBase;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectCard;
import com.accenture.mbank.logic.TransferObjectEntry;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.logic.TransferObjectTransfer;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class BperRecentTransferResponseModel extends JsonBase{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3552913461504993243L;

	private static final String JSONNAME="GetRecentTransferResponse";
	
    /***recentTransfers An object containing the following four  lists of transfer List Objects M Object*/ 
    @Deprecated
    List<TransferObject>recentTransfers;

    /***recentBankTransferList A list of recentBankTransfer Objects M List<TransferObject>*/
    private List<TransferObjectTransfer> recentBankTransferList;
    
    /***recentTransferEntryList A list of recentTransferEntry Objects M List< TransferObject>*/
    private List< TransferObjectEntry> recentTransferEntryList;

    /***recentSimTopUpList A list of recentSimTopUp Objects M List< TransferObject>*/ 
    private List< TransferObjectSim> recentSimTopUpList;
    
    /***recentPrepaidCardRechargeList A list of recentPrepaidCardRecharge Objects M List< TransferObject >*/ 
    List< TransferObjectCard >recentPrepaidCardRechargeList;

	public List<TransferObject> getRecentTransfers() {
		return recentTransfers;
	}

	public void setRecentTransfers(List<TransferObject> recentTransfers) {
		this.recentTransfers = recentTransfers;
	}

	public List<TransferObjectTransfer> getRecentBankTransferList() {
		return recentBankTransferList;
	}

	public void setRecentBankTransferList(
			List<TransferObjectTransfer> recentBankTransferList) {
		this.recentBankTransferList = recentBankTransferList;
	}

	public List<TransferObjectEntry> getRecentTransferEntryList() {
		return recentTransferEntryList;
	}

	public void setRecentTransferEntryList(
			List<TransferObjectEntry> recentTransferEntryList) {
		this.recentTransferEntryList = recentTransferEntryList;
	}

	public List<TransferObjectSim> getRecentSimTopUpList() {
		return recentSimTopUpList;
	}

	public void setRecentSimTopUpList(List<TransferObjectSim> recentSimTopUpList) {
		this.recentSimTopUpList = recentSimTopUpList;
	}

	public List<TransferObjectCard> getRecentPrepaidCardRechargeList() {
		return recentPrepaidCardRechargeList;
	}

	public void setRecentPrepaidCardRechargeList(
			List<TransferObjectCard> recentPrepaidCardRechargeList) {
		this.recentPrepaidCardRechargeList = recentPrepaidCardRechargeList;
	}

	@Override
	public String getJsonName() {
		return JSONNAME;
	}
	
	/**
	 * 
	 * @param publicModel
	 * @param abi
	 *            Contants.abi
	 * @return
	 */
	public static String GetServiceStatusReportProtocal(
			RequestPublicModel publicModel, String abi,String serviceCode) {
		String result = null;
		try {
			JSONObject jsonObj = new JSONObject();
			JSONObject obj = new JSONObject();
//			obj.put("transactionId", publicModel.gett);
			obj.put("sessionId",publicModel.getSessionId());
//			obj.put("operationId", value);
			obj.put("serviceCode",serviceCode);
			obj.put("serviceType", ServiceType.getServiceStatus);
			obj.put("bankName", publicModel.getBankName());
			obj.put("enterpriseId", publicModel.getEnterpriseId());
			obj.put("customerNumber", publicModel.getCustomerNumber());
			obj.put("channel", publicModel.getChannel());
			obj.put("userAgent", publicModel.getUserAgent());
			obj.put("token", publicModel.getToken());
			obj.put("abi", abi);

			jsonObj.put("GetServiceStatusRequest", obj);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String RecentTransferReportProtocal(RequestPublicModel publicModel,
            String accountCode, int recentPaymentDislayed) {
        String resultStr = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject getRecenTransferObj = new JSONObject();

            getRecenTransferObj.put("serviceType", ServiceType.getRecentTransfer);
            getRecenTransferObj.put("enterpriseId", publicModel.getEnterpriseId());
            getRecenTransferObj.put("bankName", publicModel.getBankName());
            getRecenTransferObj.put("customerNumber", publicModel.getCustomerNumber());
            getRecenTransferObj.put("channel", publicModel.getChannel());
            getRecenTransferObj.put("userAgent", publicModel.getUserAgent());
            getRecenTransferObj.put("accountCode", accountCode);
            getRecenTransferObj.put("numberOfTnx", recentPaymentDislayed);
            getRecenTransferObj.put("sessionId", publicModel.getSessionId());
            getRecenTransferObj.put("token", publicModel.getToken());

            jsonObj.put("GetRecentTransferRequest", getRecenTransferObj);

            resultStr = jsonObj.toString();
        } catch (Exception e) {
            LogManager.d("RecentTransferReportProtocal id error" + e.getLocalizedMessage());
        }
        return resultStr;
    }
	 
	 @Override
	 public void parseJson(JSONObject jsonObject) throws JSONException {
		 if(jsonObject==null){
			 return;
		 }
		 super.parseJson(jsonObject);
		 JSONObject recentTransfers=jsonObject.getJSONObject("recentTransfers");
		 if(recentTransfers==null){
			 return;
		 }
		 this.recentTransfers=new ArrayList<TransferObject>();
		 JSONArray jsonListArray = recentTransfers .optJSONArray("recentBankTransferList");
		 
		 if(recentBankTransferList==null){
			 recentBankTransferList=new ArrayList<TransferObjectTransfer>();
		 }else{
			 recentBankTransferList.clear();
		 }
		 if(jsonListArray!=null){
			 for (int i = 0; i < jsonListArray.length(); i++) {
				 TransferObjectTransfer mTransferObjectTransfer= new TransferObjectTransfer();
				 mTransferObjectTransfer.parseJson(jsonListArray.optJSONObject(i));
				 recentBankTransferList.add(mTransferObjectTransfer);
				 this.recentTransfers.add(mTransferObjectTransfer);
			 }
		 }
		 
		 jsonListArray = recentTransfers .optJSONArray("recentTransferEntryList");
		 
		 if(recentTransferEntryList==null){
			 recentTransferEntryList=new ArrayList<TransferObjectEntry>();
		 }else{
			 recentTransferEntryList.clear();
		 }
		 if(jsonListArray!=null){
			 for (int i = 0; i < jsonListArray.length(); i++) {
				 TransferObjectEntry mTransferObjectTransfer= new TransferObjectEntry();
				 mTransferObjectTransfer.parseJson(jsonListArray.optJSONObject(i));
				 recentTransferEntryList.add(mTransferObjectTransfer);
				 this.recentTransfers.add(mTransferObjectTransfer);
			 }
		 }
		 
		 jsonListArray = recentTransfers .optJSONArray("recentSimTopUpList");
		 
		 if(recentSimTopUpList==null){
			 recentSimTopUpList=new ArrayList<TransferObjectSim>();
		 }else{
			 recentSimTopUpList.clear();
		 }
		 if(jsonListArray!=null){
			 for (int i = 0; i < jsonListArray.length(); i++) {
				 TransferObjectSim mTransferObjectTransfer= new TransferObjectSim();
				 mTransferObjectTransfer.parseJson(jsonListArray.optJSONObject(i));
				 recentSimTopUpList.add(mTransferObjectTransfer);
				 this.recentTransfers.add(mTransferObjectTransfer);
			 }
		 }
		 
		 jsonListArray = recentTransfers .optJSONArray("recentPrepaidCardRechargeList");
		 
		 if(recentPrepaidCardRechargeList==null){
			 recentPrepaidCardRechargeList=new ArrayList<TransferObjectCard>();
		 }else{
			 recentPrepaidCardRechargeList.clear();
		 }
		 if(jsonListArray!=null){
			 for (int i = 0; i < jsonListArray.length(); i++) {
				 TransferObjectCard mTransferObjectTransfer= new TransferObjectCard();
				 mTransferObjectTransfer.parseJson(jsonListArray.optJSONObject(i));
				 recentPrepaidCardRechargeList.add(mTransferObjectTransfer);
				 this.recentTransfers.add(mTransferObjectTransfer);
			 }
		 }
		 
		 Collections.sort(recentBankTransferList,TransferObject.dataComparable);
		 Collections.sort(recentTransferEntryList,TransferObject.dataComparable);
		 Collections.sort(recentSimTopUpList,TransferObject.dataComparable);
		 Collections.sort(recentPrepaidCardRechargeList,TransferObject.dataComparable);
		 Collections.sort(this.recentTransfers,TransferObject.dataComparable);
		 
	 }
	 

}
