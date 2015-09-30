
package com.accenture.mbank.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BankServiceType;
import com.accenture.mbank.model.GetUserInfoResponseModel;
import com.accenture.mbank.model.NdgHB;
import com.accenture.mbank.model.ProductModel;
import com.accenture.mbank.model.RequestPublicModel;
import com.accenture.mbank.model.ServicesModel;
import com.accenture.mbank.model.UserprofileHb;
import com.accenture.mbank.util.Contants;
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
            userInfoObj.put("customerNumber", publicModel.getCustomerNumber());
            userInfoObj.put("channel", publicModel.getChannel());
            userInfoObj.put("userAgent", publicModel.getUserAgent());
            userInfoObj.put("sessionId", publicModel.getSessionId());
            userInfoObj.put("appVersion",Contants.Ver);

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

		/*
		 * print long string
		 */
		int maxLogSize = 4000;
		for (int i = 0; i <= json.length() / maxLogSize; i++) {
			int start = i * maxLogSize;
			int end = (i + 1) * maxLogSize;
			end = end > json.length() ? json.length() : end;
			LogManager.i(json.substring(start, end));
		}

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject getUserInfoObj = jsonObj.getJSONObject("GetUserInfoResponse");
            getUserInfoResponse.responsePublicModel.setResultCode(getUserInfoObj.getInt("resultCode"));
            getUserInfoResponse.responsePublicModel.setResultDescription(getUserInfoObj.optString("resultDescription"));
            getUserInfoResponse.setMandatoryFlag(getUserInfoObj.optBoolean("mandatoryFlag"));
            getUserInfoResponse.setMarketLink(getUserInfoObj.optString("marketLink"));
            if (getUserInfoResponse.responsePublicModel.getResultCode() != 0) {
                JSONObject eventManagementObj = getUserInfoObj.getJSONObject("eventManagement");
                getUserInfoResponse.responsePublicModel.eventManagement.setErrorCode(eventManagementObj.optString("errorCode"));
                getUserInfoResponse.responsePublicModel.eventManagement.setErrorDescription(eventManagementObj.optString("errorDescription"));
                return getUserInfoResponse;
            }
            getUserInfoResponse.responsePublicModel.setTransactionId(getUserInfoObj.optString("transactionId"));
            getUserInfoResponse.setToken(getUserInfoObj.optString("token"));
            
            JSONObject userprofileHbObj = getUserInfoObj.getJSONObject("userprofileHb");
            UserprofileHb userProfileHB = new UserprofileHb();
            try {
                userProfileHB.setAlias(userprofileHbObj.optString("alias"));
                userProfileHB.setAbiCode(userprofileHbObj.optString("abiCode"));
                userProfileHB.setBankCust(userprofileHbObj.optString("bankCust"));
                userProfileHB.setCabCode(userprofileHbObj.optString("cabCode"));
                userProfileHB.setCciaNumber(userprofileHbObj.optString("cciaNumber"));
                userProfileHB.setCompanyName(userprofileHbObj.optString("companyName"));
                userProfileHB.setContactMail(userprofileHbObj.optString("contactMail"));
                userProfileHB.setContactPhone(userprofileHbObj.optString("contactPhone"));
                userProfileHB.setCreationDate(userprofileHbObj.optString("creationDate"));
                userProfileHB.setCrae(userprofileHbObj.optString("crae"));
                userProfileHB.setBirthDate(userprofileHbObj.optString("birthDate"));
                userProfileHB.setBranchName(userprofileHbObj.optString("branchName"));
                userProfileHB.setBranchCode(userprofileHbObj.optString("branchCode"));
                userProfileHB.setBranchAddress(userprofileHbObj.optString("branchAddress"));
                userProfileHB.setBranchPostalCode(userprofileHbObj.optString("branchPostalCode"));
                userProfileHB.setBranchCity(userprofileHbObj.optString("branchCity"));
                userProfileHB.setCurrencyChargeCode(userprofileHbObj.optString("currencyChargeCode"));
                userProfileHB.setCustomerCode(userprofileHbObj.optString("customerCode"));
                userProfileHB.setCustomerName(userprofileHbObj.optString("customerName"));
                userProfileHB.setCustomerSurname(userprofileHbObj.optString("customerSurname"));
                userProfileHB.setManagment(userprofileHbObj.optString("management"));
                NdgHB ndgHB = new NdgHB();
                JSONObject ndgHBObj = userprofileHbObj.getJSONObject("ndgHB");
                ndgHB.setNdgCode(ndgHBObj.optString("ndgCode"));
                ndgHB.setNdgType(ndgHBObj.optString("ndgType"));
                userProfileHB.setNdgHB(ndgHB);
                userProfileHB.setNote(userprofileHbObj.optString("note"));
                userProfileHB.setReferenceEmail(userprofileHbObj.optString("referenceEmail"));
                userProfileHB.setReferenceFax(userprofileHbObj.optString("referenceFax"));
                userProfileHB.setReferenceTelephoneNumber1(userprofileHbObj.optString("referenceTelephoneNumber1"));
                userProfileHB.setReferenceTelephoneNumber2(userprofileHbObj.optString("referenceTelephoneNumber2"));
                userProfileHB.setTaxCode(userprofileHbObj.optString("taxCode"));
                userProfileHB.setTypeSB(userprofileHbObj.optString("typeSB"));
                userProfileHB.setRiba(userprofileHbObj.optString("riba"));
            } catch (Exception e) {
            	
            }

            JSONObject productObj = userprofileHbObj.getJSONObject("product");
            ProductModel product = new ProductModel();
            product.setProductCode(productObj.optString("productCode"));
            product.setProductDesc(productObj.optString("productDesc"));

            JSONArray servicesArray = productObj.getJSONArray("services");
            List<ServicesModel> servicesList = new ArrayList<ServicesModel>();
            for (int i = 0; i < servicesArray.length(); i++) {
                ServicesModel services = new ServicesModel();
                services.setServiceCode(servicesArray.getJSONObject(i).optString("serviceCode"));
                services.setServiceDescription(servicesArray.getJSONObject(i).optString("serviceDescription"));
                services.setServiceType(servicesArray.getJSONObject(i).optString("serviceType"));
                services.setServiceId(servicesArray.getJSONObject(i).optString("serviceId"));
                servicesList.add(services);
            }
            product.setServices(servicesList);
            userProfileHB.setProduct(product);

            JSONArray accountsArray = userprofileHbObj.getJSONArray("accounts");
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
                account.setTitle(accountsArray.getJSONObject(j).optString("title"));
                account.setBranchCode(accountsArray.getJSONObject(j).optString("branchCode"));
                account.setBankCode(accountsArray.getJSONObject(j).optString("bankCode"));

                BankServiceType bankServiceType = new BankServiceType();
                JSONObject bankServiceTypeObj = accountsArray.getJSONObject(j).getJSONObject("bankServiceType");
                bankServiceType.setBankServiceCode(bankServiceTypeObj.optString("bankServiceCode"));
                bankServiceType.setServiceCategory(bankServiceTypeObj.optString("serviceCategory"));
                bankServiceType.setSubCategory(bankServiceTypeObj.optString("subCategory"));
                account.setBankServiceType(bankServiceType);
                account.setCardNumber(accountsArray.getJSONObject(j).optString("cardNumber"));
                account.setCardHolder(accountsArray.getJSONObject(j).optString("cardHolder"));
                account.setCardName(accountsArray.getJSONObject(j).optString("cardName"));
                account.setCardState(accountsArray.getJSONObject(j).optString("cardState"));
                
                account.setCurrencyAcc(accountsArray.getJSONObject(j).optString("currencyAcc"));
                account.setFrontendCategory(accountsArray.getJSONObject(j).optString("frontendCategory"));
                account.setMortageType(accountsArray.getJSONObject(j).optString("mortageType"));

                account.setCabCode(accountsArray.getJSONObject(j).optString("cabCode"));
                account.setNotes(accountsArray.getJSONObject(j).optString("notes"));
                account.setRate(accountsArray.getJSONObject(j).optDouble("rate"));
                account.setBenchmarksDesc(accountsArray.getJSONObject(j).optString("benchmarksDesc"));
                account.setBenchmarksValue(accountsArray.getJSONObject(j).optDouble("benchmarksValue"));
                account.setOpeningDate(accountsArray.getJSONObject(j).optString("openingDate"));
                account.setProdAccount(accountsArray.getJSONObject(j).optString("prodAccount"));
                account.setProdAccountDesc(accountsArray.getJSONObject(j).optString("prodAccountDesc"));
                account.setPreferred(accountsArray.getJSONObject(j).optString("preferred"));
                account.setPlafond(accountsArray.getJSONObject(j).optDouble("plafond"));
                account.setTypeClient(accountsArray.getJSONObject(j).optString("typeClient"));
                account.setAggregatedCardCode(accountsArray.getJSONObject(j).optString("aggregatedCardCode"));
                account.setCardRelations(accountsArray.getJSONObject(j).optString("cardRelations"));
                account.setPortafoglio(accountsArray.getJSONObject(j).optString("portafoglio"));
                account.setIsPreferred(accountsArray.getJSONObject(j).optBoolean("isPreferred"));
                account.setExpirationDate(accountsArray.getJSONObject(j).optString("expirationDate"));
                accountsList.add(account);
            }
            userProfileHB.setAccountList(accountsList);
            getUserInfoResponse.setUserprofileHb(userProfileHB);
        } catch (Exception e) {
            LogManager.e("ParseUserInfoResponse is error" + e.getLocalizedMessage());
        }
        return getUserInfoResponse;
    }
}
