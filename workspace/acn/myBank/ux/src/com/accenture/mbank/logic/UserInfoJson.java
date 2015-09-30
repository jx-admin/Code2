
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BankServiceType;
import com.accenture.mbank.model.GetUserInfoResponseModel;
import com.accenture.mbank.model.ProductModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.ServicesModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceType;

public class UserInfoJson {
    /**
     * @param publicModel
     * @return
     */
    public static String UserInfoReportProtocal(RequestPublicModel publicModel) {
        String result = null;
        try {
            JSONObject jsonObj = new JSONObject();
            JSONObject userInfoObj = new JSONObject();
            userInfoObj.put("serviceType", ServiceType.getUserInfo);
            userInfoObj.put("bankName", publicModel.getBankName());
            userInfoObj.put("enterpriseId", publicModel.getEnterpriseId());
            userInfoObj.put("customerCode", publicModel.getCustomerCode());
            userInfoObj.put("channel", publicModel.getChannel());
            userInfoObj.put("userAgent", publicModel.getUserAgent());
            userInfoObj.put("sessionId", publicModel.getSessionId());

            jsonObj.put("GetUserInfoRequest", userInfoObj);
            result = jsonObj.toString();
        } catch (Exception e) {
            LogManager.e("UserInfoReportProtocal is error" + e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * @param json
     * @return
     */
    public static GetUserInfoResponseModel ParseUserInfoResponse(String json) {
        GetUserInfoResponseModel getUserInfoResponse = new GetUserInfoResponseModel();

        if (json == null) {
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getUserInfoObj = jsonObj.getJSONObject("GetUserInfoResponse");

            getUserInfoResponse.responsePublicModel.setResultCode(getUserInfoObj
                    .getInt("resultCode"));
            getUserInfoResponse.responsePublicModel.setResultDescription(getUserInfoObj
                    .optString("resultDescription"));
            if (getUserInfoResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getUserInfoObj.getJSONObject("eventManagement");
                getUserInfoResponse.responsePublicModel.eventManagement
                        .setErrorCode(eventManagementObj.optString("errorCode"));
                getUserInfoResponse.responsePublicModel.eventManagement
                        .setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getUserInfoResponse;
            }

            getUserInfoResponse.responsePublicModel.setTransactionId(getUserInfoObj
                    .optString("transactionId"));
            try {
                getUserInfoResponse.setToken(getUserInfoObj.optString("token"));
                getUserInfoResponse.setBankCode(getUserInfoObj.optString("bankCode"));
                getUserInfoResponse.setAlias(getUserInfoObj.optString("alias"));
                getUserInfoResponse.setBirthDate(getUserInfoObj.optString("birthDate"));
                getUserInfoResponse.setBranchName(getUserInfoObj.optString("branchName"));
                getUserInfoResponse.setBranchCode(getUserInfoObj.optString("branchCode"));
                getUserInfoResponse.setBranchAddress(getUserInfoObj.optString("branchAddress"));
                getUserInfoResponse.setBranchPostalCode(getUserInfoObj
                        .optString("branchPostalCode"));
                getUserInfoResponse.setBranchCity(getUserInfoObj.optString("branchCity"));
                getUserInfoResponse.setCreationDate(getUserInfoObj.optString("creationDate"));
                getUserInfoResponse.setCurrencyChargeCode(getUserInfoObj
                        .optString("currencyChargeCode"));
                getUserInfoResponse.setCustomerCode(getUserInfoObj.optString("customerCode"));
                getUserInfoResponse.setCustomerName(getUserInfoObj.optString("customerName"));
                getUserInfoResponse.setCustomerSurname(getUserInfoObj.optString("customerSurname"));
                getUserInfoResponse.setManagement(getUserInfoObj.optString("management"));
                getUserInfoResponse.setNdgHBCode(getUserInfoObj.optString("ndgHBCode"));
                getUserInfoResponse.setNdgType(getUserInfoObj.optString("ndgType"));
                getUserInfoResponse.setNotes(getUserInfoObj.optString("notes"));
                getUserInfoResponse.setReferenceEmail(getUserInfoObj.optString("referenceEmail"));
                getUserInfoResponse.setReferenceFax(getUserInfoObj.optString("referenceFax"));
                getUserInfoResponse.setReferenceTelephoneNumber1(getUserInfoObj
                        .optString("referenceTelephoneNumber1"));
                getUserInfoResponse.setReferenceTelephoneNumber2(getUserInfoObj
                        .optString("referenceTelephoneNumber2"));
                getUserInfoResponse.setTaxCode(getUserInfoObj.optString("taxCode"));
            } catch (Exception e) {

            }

            JSONObject productObj = getUserInfoObj.getJSONObject("product");
            ProductModel product = new ProductModel();
            product.setProductCode(productObj.optString("productCode"));
            product.setProductDesc(productObj.optString("productDesc"));

            JSONArray servicesArray = productObj.getJSONArray("services");
            List<ServicesModel> servicesList = new ArrayList<ServicesModel>();
            for (int i = 0; i < servicesArray.length(); i++) {
                ServicesModel services = new ServicesModel();
                services.setServiceCode(servicesArray.getJSONObject(i).optString("serviceCode"));
                services.setServiceDescription(servicesArray.getJSONObject(i).optString(
                        "serviceDescription"));
                services.setServiceType(servicesArray.getJSONObject(i).optString("serviceType"));
                services.setServiceId(servicesArray.getJSONObject(i).optString("serviceId"));
                servicesList.add(services);
            }
            product.setServices(servicesList);
            getUserInfoResponse.setProduct(product);

            JSONArray accountsArray = getUserInfoObj.getJSONArray("accounts");
            List<AccountsModel> accountsList = new ArrayList<AccountsModel>();
            for (int j = 0; j < accountsArray.length(); j++) {
                AccountsModel account = new AccountsModel();
                account.setAccountId(accountsArray.getJSONObject(j).optString("accountId"));
                account.setAccountCode(accountsArray.getJSONObject(j).optString("accountCode"));
                try {
                    String ibanCode = accountsArray.getJSONObject(j).optString("ibanCode");
                    account.setIbanCode(ibanCode);
                } catch (Exception e) {
                    account.setIbanCode("");
                }

                account.setIsInformative(accountsArray.getJSONObject(j).optString("isInformative"));
                account.setAccountAlias(accountsArray.getJSONObject(j).optString("accountAlias"));
                account.setBranchCode(accountsArray.getJSONObject(j).optString("branchCode"));
                account.setBankCode(accountsArray.getJSONObject(j).optString("bankCode"));

                BankServiceType bankServiceType = new BankServiceType();
                JSONObject bankServiceTypeObj = accountsArray.getJSONObject(j).getJSONObject(
                        "bankServiceType");
                bankServiceType.setBankServiceCode(bankServiceTypeObj.optString("bankServiceCode"));
                bankServiceType.setServiceCategory(bankServiceTypeObj.optString("serviceCategory"));
                bankServiceType.setSubCategory(bankServiceTypeObj.optString("subCategory"));
                account.setBankServiceType(bankServiceType);
                account.setCardNumber(accountsArray.getJSONObject(j).optString("cardNumber"));
                account.setCardHolder(accountsArray.getJSONObject(j).optString("cardHolder"));
                account.setCardName(accountsArray.getJSONObject(j).optString("cardName"));
                account.setCardState(accountsArray.getJSONObject(j).optString("cardState"));
                
                account.setCurrencyAcc(accountsArray.getJSONObject(j).optString("currencyAcc"));
                account.setFrontendCategory(accountsArray.getJSONObject(j).optString(
                        "frontendCategory"));
                account.setMortageType(accountsArray.getJSONObject(j).optString("mortageType"));

                
                
                account.setCabCode(accountsArray.getJSONObject(j).optString("cabCode"));
                account.setNotes(accountsArray.getJSONObject(j).optString("notes"));
                account.setOpeningDate(accountsArray.getJSONObject(j).optString("openingDate"));
                account.setProdAccount(accountsArray.getJSONObject(j).optString("prodAccount"));
                account.setProdAccountDesc(accountsArray.getJSONObject(j).optString(
                        "prodAccountDesc"));
                account.setPreferred(accountsArray.getJSONObject(j).optString("preferred"));
                account.setPlafond(accountsArray.getJSONObject(j).optDouble("plafond"));
                accountsList.add(account);
            }
            getUserInfoResponse.setAccountList(accountsList);
        } catch (Exception e) {
            LogManager.e("ParseUserInfoResponse is error" + e.getLocalizedMessage());
        }
        return getUserInfoResponse;
    }
}
