package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.util.ServiceType;

/**
 * @author junxu.wang
 *
 */
public class GetServiceStatusJson extends JsonBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String JSONNAME="GetServiceStatusResponse";
	
	private List<ServiceCode>serviceCodeList;// List of service code object M      List 
	
	public List<ServiceCode> getServiceCodeList() {
		return serviceCodeList;
	}
	
	public void setServiceCodeList(List<ServiceCode> serviceCodeList) {
		this.serviceCodeList=serviceCodeList;
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
	 
	 @Override
	 public void parseJson(JSONObject jsonObject) throws JSONException {
		 if(jsonObject==null){
			 return;
		 }
		 super.parseJson(jsonObject);
		 
		 if(serviceCodeList==null){
			 serviceCodeList=new ArrayList<ServiceCode>();
		 }else{
			 serviceCodeList.clear();
		 }
		 JSONArray jsonListArray = jsonObject .optJSONArray("serviceCodeList");
		 if(jsonListArray!=null){
			 for (int i = 0; i < jsonListArray.length(); i++) {
				 ServiceCode serviceCode= new ServiceCode();
				 serviceCode.parseJson(jsonListArray.optJSONObject(i));
				 serviceCodeList.add(serviceCode);
			 }
		 }
	 }
		
	 @Override
	 public String getJsonName() {
		 return JSONNAME;
	 }
	 
	 public static class ServiceCode extends JsonAbstract{
		 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private static final String JSONNAME="serviceCode";// Object service code M    Object 
		 
		 private String serviceCode;// Service code M       String 
		 
		 private List<StatusService> statusServiceList;// List of status services M List<Object> 

		@Override
		public void parseJson(JSONObject jsonObject) throws JSONException {
			if(jsonObject==null){
				return;
			}
			
			setServiceCode(jsonObject.optString("serviceCode"));
			
			if(statusServiceList==null){
				statusServiceList=new ArrayList<GetServiceStatusJson.StatusService>();
			}else{
				statusServiceList.clear();
			}
			JSONArray jsonListArray = jsonObject .optJSONArray("statusServiceList");
			if(jsonListArray!=null){
	            for (int i = 0; i < jsonListArray.length(); i++) {
	            	GetServiceStatusJson.StatusService statusService = new StatusService();
	            	statusService.parseJson(jsonListArray.optJSONObject(i));
	            	statusServiceList.add(statusService);
	            }
			}
			
		}

		public String getServiceCode() {
			return serviceCode;
		}

		public void setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
		}

		public List<StatusService> getStatusServiceList() {
			return statusServiceList;
		}

		public void setStatusServiceList(List<StatusService> statusServiceList) {
			this.statusServiceList = statusServiceList;
		}

		@Override
		public String getJsonName() {
			return JSONNAME;
		}
		 
	 }

	 public static class StatusService extends JsonAbstract{
		 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private static final String JSONNAME="statusService";
				 
         private String abi;// Bank abi M       String 

         private String serviceCode;// Service code M      String 

         private String serviceDescription;// description O String 

         private boolean isActive;// true if service is active, false if is not active M boolean 

         private String message;// Message to show in case of service inactive O String 

         private String startDate;// Date of start of inactive service O String 

         private String endDate;//Date of end of inactive service O String  

		public String getAbi() {
			return abi;
		}

		public void setAbi(String abi) {
			this.abi = abi;
		}

		public String getServiceCode() {
			return serviceCode;
		}

		public void setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
		}

		public String getServiceDescription() {
			return serviceDescription;
		}

		public void setServiceDescription(String serviceDescription) {
			this.serviceDescription = serviceDescription;
		}

		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		@Override
		public void parseJson(JSONObject jsonObject) throws JSONException {
			if(jsonObject==null){
				return;
			}
//			JSONObject eventManagementObj = jsonObject.getJSONObject("statusService");
			
			setAbi(jsonObject.optString("abi"));
			
			setServiceCode(jsonObject.optString("serviceCode"));
			
			setServiceDescription(jsonObject.optString("serviceDescription"));// description O String 
			
			setActive(jsonObject.optBoolean("isActive"));// true if service is active, false if is not active M boolean 
			
			setMessage(jsonObject.optString("message"));// Message to show in case of service inactive O String 
			
			setStartDate(jsonObject.optString("startDate"));// Date of start of inactive service O String 
			
			setEndDate(jsonObject.optString("endDate"));//Date of end of inactive service O String
		}
		
		@Override
		public String getJsonName() {
			return JSONNAME;
		}
		 
	 }

}
