
package com.accenture.mbank.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.AccountsByServicesJson;
import com.accenture.mbank.logic.CheckRechargeCardJson;
import com.accenture.mbank.logic.CheckSimTopUpJson;
import com.accenture.mbank.logic.CheckTransferJson;
import com.accenture.mbank.logic.GenerateOTPJson;
import com.accenture.mbank.logic.GetRecipientListJson;
import com.accenture.mbank.logic.InsertRechargeCardJson;
import com.accenture.mbank.logic.InsertRecipientJson;
import com.accenture.mbank.logic.InsertTransferJson;
import com.accenture.mbank.logic.SimTopUpJson;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AmountAvailable;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.CheckRechargeCardResponseModel;
import com.accenture.mbank.model.CheckSimTopUpResponseModel;
import com.accenture.mbank.model.CheckTransferResponseModel;
import com.accenture.mbank.model.DestaccountModel;
import com.accenture.mbank.model.EventManagement;
import com.accenture.mbank.model.GenerateOTPResponseModel;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.model.RecentTransferModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.model.ServicesModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.model.SimTopUpResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.DialogManager.OnButtonListener;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceCode;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.PayeeExpandedLayout.BankTransferValue;
import com.accenture.mbank.view.PayeeExpandedLayout.SimTopUpValue;

public class PaymentConfirmLayout extends ScrollView implements OnClickListener, OnButtonListener {

    ImageButton validateButton, askpinButton, confirmTransactionButton;

    Handler handler;

    public ItemExpander typeExpander, dateExpander, amountExpander, reasonExpander,
            accountExpander, payeeExpander, pinExpander;

    ResponsePublicModel responsePublicModel;

    private double fee;

    public PaymentConfirmLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    String serviceCode;

    boolean accountRecover = false;

    public void onTypeSelected(String text) {
        List<AccountsForServiceModel> accountsForServiceModels = null;
        if (text.equals(getResources().getString(R.string.bank_transfer1))) {
            serviceCode = ServiceCode.BANK_TRANSFER_PAYMENT;
            accountsForServiceModels = bankTransferAccounts;
        } else if (text.equals(getResources().getString(R.string.transfer_entry1))) {
            serviceCode = ServiceCode.TRANSFER_ENTRY_PAYMENT;
            accountsForServiceModels = transferEntryAccounts;
        } else if (text.equals(getResources().getString(R.string.card_top_up1))) {
            serviceCode = ServiceCode.SIM_TOP_UP;
            accountsForServiceModels = simTopUpAccounts;
        } else if (text.equals(getResources().getString(R.string.phone_top_up1))) {
            serviceCode = ServiceCode.CARD_RECHARGE_PAYMENT;
            accountsForServiceModels = chargeAccounts;
        }

        updateUiByaccounts(accountsForServiceModels);
        if (accountRecover) {

        } else {
            AccountExpandedLayout accountExpandedLayout = (AccountExpandedLayout)accountExpander.expandedContainer;
            accountExpandedLayout.resetResult();
            LogManager.d("recover servicecode" + serviceCode);
        }

        PayeeExpandedLayout payeeExpandedLayout = (PayeeExpandedLayout)payeeExpander.expandedContainer;
        payeeExpandedLayout.setGetRecipientListModel(getRecipientListModel);
        payeeExpandedLayout.showType(serviceCode);
        payeeExpandedLayout.resetResult();

        TypeOfPayMentExpandedLayout typeOfPayMentExpandedLayout = (TypeOfPayMentExpandedLayout)typeExpander.expandedContainer;
        typeOfPayMentExpandedLayout.serviceCode = serviceCode;
        AmountExpandedLayout amountExpandedLayout = (AmountExpandedLayout)amountExpander.expandedContainer;
        amountExpandedLayout.showType(serviceCode);

        DateExpandedLayout dateExpandedLayout = (DateExpandedLayout)dateExpander.expandedContainer;
        dateExpandedLayout.showType(serviceCode);

        DescriptionExpandedLayout descriptionExpandedLayout = (DescriptionExpandedLayout)reasonExpander.expandedContainer;
        descriptionExpandedLayout.showType(serviceCode);

        setExpandable(true);

    }

    private void updateUiByaccounts(List<AccountsForServiceModel> accountsForServiceModels) {
        if (accountsForServiceModels != null && accountsForServiceModels.size() >= 0) {

            AccountExpandedLayout accountExpanderLayout = (AccountExpandedLayout)accountExpander.expandedContainer;

            accountExpanderLayout.createAccountsUI(accountsForServiceModels);

            // accountExpanderLayout.resetResult();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (BaseActivity.isNewVersion) {
            if (visibility == View.VISIBLE) {
                setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        validateButton = (ImageButton)findViewById(R.id.validate_btn);
        validateButton.setOnClickListener(this);
        confirmTransactionButton = (ImageButton)findViewById(R.id.confirm_transaction_btn);
        confirmTransactionButton.setOnClickListener(this);

        typeExpander = (ItemExpander)findViewById(R.id.typeof_payment_step);
        dateExpander = (ItemExpander)findViewById(R.id.date_step);
        amountExpander = (ItemExpander)findViewById(R.id.amount_step);
        accountExpander = (ItemExpander)findViewById(R.id.account_step);
        reasonExpander = (ItemExpander)findViewById(R.id.description_step);
        payeeExpander = (ItemExpander)findViewById(R.id.payee_step);
        pinExpander = (ItemExpander)findViewById(R.id.pin_step);

        setRequirePinUI();
        typeExpander.owner = this;
        payeeExpander.owner = this;
        dateExpander.owner = this;
        amountExpander.owner = this;
        accountExpander.owner = this;
        reasonExpander.owner = this;
        pinExpander.owner = this;
        typeExpander.setExpandable(true);

        setExpandable(false);
        LogManager.d("owner is set");

    }

    AlertDialog validateDialog;

    public void setExpandable(boolean flag) {
        accountExpander.setExpandable(flag);
        dateExpander.setExpandable(flag);
        payeeExpander.setExpandable(flag);
        amountExpander.setExpandable(flag);
        reasonExpander.setExpandable(flag);
        pinExpander.setExpandable(flag);
    }

    @Override
    public void onClick(View v) {
        final String typeCode = serviceCode;
        if (typeCode == null || typeCode.equals("")) {
            // Toast.makeText(getContext(), "please select type of payment",
            // Toast.LENGTH_LONG);
            DialogManager.createMessageDialog("please select type of payment", getContext()).show();
            return;
        }
        Object account = accountExpander.getValue();

        if (account == null) {
            DialogManager.createMessageDialog("please select account", getContext()).show();
            return;
        }
        if (validateButton == v) {
            ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
            progressOverlay.show("", new OnProgressEvent() {
                @Override
                public void onProgress() {
                    validate(typeCode);
                }
            });
        } else if (v == confirmTransactionButton) {

            ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
            progressOverlay.show("", new OnProgressEvent() {

                @Override
                public void onProgress() {

                    if (typeCode.equals(ServiceCode.SIM_TOP_UP)) {

                        confirmTranslationSimTopUp();
                    } else if (typeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)
                            || typeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
                        confirmBankTransfer();
                    } else if (typeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
                        confirmCardRecharge();
                    }

                }
            });

        }
    }

    String typeCode;

    private void validate(final String typeCode) {
        if (typeCode.equals(ServiceCode.SIM_TOP_UP)) {
            if (validateSimTopUp()) {
                showValidateDialog(typeCode);
            }
        } else if (typeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            if (PayeeExpandedLayout.isVerifyCard()) {
                if (validateCard()) {
                    showValidateDialog(typeCode);
                }
            } else {
                BaseActivity baseActivity = (BaseActivity)getContext();
                baseActivity.displayErrorMessage("Verify the date Card!");
            }
        } else if (typeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)
                || typeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            if (validateBankTransfer()) {
                this.typeCode = typeCode;
                if (eventManagement.getErrorCode() != null
                        && eventManagement.getErrorCode().equals("91082")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            DialogManager.createMessageDialog(
                                    eventManagement.getErrorDescription(), getContext(),
                                    PaymentConfirmLayout.this).show();
                        }
                    });
                } else {
                    showValidateDialog(typeCode);
                }
            }
        } else {

        }
    }

    private void showValidateDialog(final String typeCode) {
        // TODO 传到dialog里去
        final ValidateData validateData = new ValidateData();
        Object dateObject = dateExpander.getValue();
        if (dateObject == null) {

        } else {
            long milliSeconds = (Long)dateObject;
            String date = TimeUtil.getDateString(milliSeconds, TimeUtil.dateFormat5);
            validateData.date = getResources().getString(R.string.date1) + ": " + date;
        }

        validateData.type = getResources().getString(R.string.type_of_payment1) + ": "
                + ServiceCode.getNameByCode((String)typeExpander.getValue());

        AccountsModel accountsModel = (AccountsModel)accountExpander.getValue();
        validateData.account = getResources().getString(R.string.account1) + ": "
                + accountsModel.getAccountAlias();
        Object currentAmount = amountExpander.getValue();
        double amountInt = 0;
        if (currentAmount instanceof AmountAvailable) {
            AmountAvailable amountAvailable = (AmountAvailable)currentAmount;
            amountInt = Double.parseDouble(amountAvailable.getDescription());
            validateData.amount = getResources().getString(R.string.amount1)
                    + ": "
                    + Utils.notPlusGenerateFormatMoney(getResources().getString(R.string.dollar),
                            amountAvailable.getRechargeAmount());
        } else {
            try {
                amountInt = Double.parseDouble((String)currentAmount);
            } catch (Exception e) {
                amountInt = 0;
            }
            validateData.amount = getResources().getString(R.string.amount1)
                    + ": "
                    + Utils.notPlusGenerateFormatMoney(
                            getContext().getResources().getString(R.string.dollar), amountInt);
        }
        String reason = null;
        if (reasonExpander.getValue() != null && !reasonExpander.getValue().equals("")) {
            reason = (String)reasonExpander.getValue();
        } else {
            reason = getContext().getResources().getString(R.string.not_able);
        }

        validateData.reason = getResources().getString(R.string.reason) + ": " + reason;

        if (fee == 0) {
            validateData.fees = getResources().getString(R.string.fees) + ": "
                    + Utils.notDecimalGenerateMoney(getResources().getString(R.string.dollar), fee);
        } else {
            validateData.fees = getResources().getString(R.string.fees)
                    + ": "
                    + Utils.notPlusGenerateFormatMoney(getResources().getString(R.string.dollar),
                            fee);
        }

        validateData.beneficiary = getResources().getString(R.string.beneficiary1) + ": "
                + PayeeExpandedLayout.beneficiary;
        validateData.iban = getResources().getString(R.string.iban) + ": "
                + PayeeExpandedLayout.iban;
        String bic = null;
        if (PayeeExpandedLayout.bic == null || PayeeExpandedLayout.bic.equals("")) {
            bic = getContext().getResources().getString(R.string.not_able);
        } else {
            bic = PayeeExpandedLayout.bic;
        }
        validateData.bic = getResources().getString(R.string.bic) + ": " + bic;
        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        OnProgressEvent onProgressEvent = new OnProgressEvent() {
            @Override
            public void onProgress() {

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        validateDialog = DialogManager.createRequirePinDialog(typeCode,
                                validateData, PaymentConfirmLayout.this);
                        validateDialog.show();
                        typeExpander.setExpand(true);
                    }
                });
            }
        };
        progressOverlay.show("", onProgressEvent);
    }

    private void confirmTranslationSimTopUp() {
        AccountsModel accountsModel = generateAccountModel();

        List<AmountAvailable> amountAvailableList = generateAmountAvailableList();
        PayeeExpandedLayout.SimTopUpValue value = (SimTopUpValue)payeeExpander.getValue();
        String postData = SimTopUpJson.SimTopUpReportProtocal(Contants.publicModel,
                accountsModel.getAccountCode(), value.destProvider, value.phoneNumber,
                generatePin(), generateOtp.getOtpKeySession(), Contants.OTP_CHANNEL_MAIL,
                amountAvailableList, "1000",checkSimTopUp);

        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                getContext());
        final SimTopUpResponseModel simTopUp = SimTopUpJson.ParseSimTopUpResponse(httpResult);

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (simTopUp.responsePublicModel.isSuccess()) {
                    DialogManager.createMessageDialog("Transaction success!", getContext()).show();
                    AccountsLayout.needUpdate = true;
                    CardsLayoutManager.needUpdate = true;
                    RecentPayments.needUpdate = true;
                    reset();
                } else {
                    BaseActivity baseActivity = (BaseActivity)getContext();
                    baseActivity.displayErrorMessage(simTopUp.responsePublicModel.eventManagement
                            .getErrorDescription());
                    // DialogManager.createMessageDialog(
                    // simTopUp.responsePublicModel.eventManagement.getErrorDescription(),
                    // getContext()).show();
                }
                setRequirePinUI();
                setExpandable(false);
            }
        });
    }

    private void confirmBankTransfer() {
        AccountsModel accountsModel = generateAccountModel();
        double amountInt = generateAmount();
        String transferType = genereateTransferType();
        String description = generateDescription();
        String dateString = generateDate();
        DestaccountModel destAccount = generateDestAccountModel(transferType);
        String pin = generatePin();
        String postData = InsertTransferJson.InsertTransferReportProtocal(Contants.publicModel,
                accountsModel.getAccountCode(), amountInt, "0", transferType, description,
                dateString, destAccount, pin, generateOtp.getOtpKeySession(),
                Contants.OTP_CHANNEL_MAIL, "USD",checkTransfer);

        LogManager.d("transferType" + transferType);

        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                getContext());
        final ResponsePublicModel insertTransferresponse = InsertTransferJson
                .ParseInsertTransferResponse(httpResult);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (insertTransferresponse.isSuccess()) {
                    DialogManager.createMessageDialog("transfer success!", getContext()).show();
                    reset();

                    AccountsLayout.needUpdate = true;
                    CardsLayoutManager.needUpdate = true;
                    RecentPayments.needUpdate = true;

                } else {
                    BaseActivity baseActivity = (BaseActivity)getContext();
                    baseActivity.displayErrorMessage(insertTransferresponse.eventManagement
                            .getErrorDescription());
                    // DialogManager.createMessageDialog(
                    // insertTransferresponse.eventManagement.getErrorDescription(),
                    // getContext()).show();
                    // Toast.makeText(getContext(), "fail",
                    // Toast.LENGTH_LONG).show();
                }
                setRequirePinUI();
                setExpandable(false);
            }
        });

    }

    private String generatePin() {
        String pin = "";
        if (pinExpander.getValue() != null) {
            pin = pinExpander.getValue().toString();
        }
        return pin;
    }

    private void confirmCardRecharge() {

        String dateString = generateDate();
        PayeeExpandedLayout.PreparedCardValue value = (PayeeExpandedLayout.PreparedCardValue)payeeExpander
                .getValue();
        String description = generateDescription();
        double amountdouble = generateAmount();
        AccountsModel accountsModel = generateAccountModel();
        // RequestPublicModel publicModel,
        // String accountCode, double amount, String destCardCode, String
        // cardNumber,
        // String purposeDescription, String name, String title, String
        // otpValue,
        // String otpKeySession, String otpChannel,String currency
        String title = "poqqibo qeyu";
        String postData = InsertRechargeCardJson.InsertRechargeCardReportProtocal(
                Contants.publicModel, accountsModel.getAccountCode(), amountdouble, value.cardHash,
                value.cardNumber, description, value.name, value.title, generatePin(),
                generateOtp.getOtpKeySession(), Contants.OTP_CHANNEL_MAIL, "1000",checkRechargeCard);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                getContext());
        final ResponsePublicModel insertRechargeCard = InsertRechargeCardJson
                .ParseInsertRechargeCardResponse(httpResult);
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (insertRechargeCard.isSuccess()) {
                    DialogManager.createMessageDialog("transfer success!", getContext()).show();
                    AccountsLayout.needUpdate = true;
                    CardsLayoutManager.needUpdate = true;
                    RecentPayments.needUpdate = true;
                    reset();
                } else {
                    BaseActivity baseActivity = (BaseActivity)getContext();
                    baseActivity.displayErrorMessage(insertRechargeCard.eventManagement
                            .getErrorDescription());
                    // DialogManager.createMessageDialog(
                    // insertRechargeCard.eventManagement.getErrorDescription(),
                    // getContext())
                    // .show();
                    // Toast.makeText(getContext(), "fail",
                    // Toast.LENGTH_LONG).show();
                }
                setRequirePinUI();
                setExpandable(false);
            }

        });
    }

    private DestaccountModel generateDestAccountModel(String type) {

        DestaccountModel destAccount = new DestaccountModel();
        String iban = "";
        String bic = "";
        if (type.equals("1")) {

            PayeeExpandedLayout.TransferEntryValue value = (PayeeExpandedLayout.TransferEntryValue)payeeExpander
                    .getValue();
            if (value.value == null) {
            } else {
                destAccount.setIban(value.value.getIbanCode());
                destAccount.setTitle(value.value.getAccountAlias());
            }

        } else {
            PayeeExpandedLayout.BankTransferValue value = (BankTransferValue)payeeExpander
                    .getValue();
            destAccount.setIban(value.iban);
            destAccount.setTitle(value.name);
            bic = value.bic;
        }
        iban = destAccount.getIban();
        String state = "";
        if (iban == null || iban.length() < 2) {
            state = "IT";
        } else {
            state = iban.substring(0, 2).toUpperCase();
        }
        destAccount.setState(state);
        destAccount.setBic("");
        return destAccount;
    }

    public EventManagement eventManagement = new EventManagement();
    CheckTransferResponseModel checkTransfer;
    private boolean validateBankTransfer() {

        AccountsModel accountsModel = generateAccountModel();

        String transferType = genereateTransferType();
        double amountInt = generateAmount();
        String description = generateDescription();
        String dateString = generateDate();
        DestaccountModel destAccount = generateDestAccountModel(transferType);
        String postData = CheckTransferJson.CheckTransferReportProtocal(
                accountsModel.getAccountCode(), amountInt, "0", transferType, description,
                dateString, destAccount, Contants.publicModel, "1000",null);

        LogManager.d("transferType  validate" + transferType);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                getContext());
        checkTransfer = CheckTransferJson
                .ParseCheckTransferResponse(httpResult);
        fee = checkTransfer.getCharges();
        // LogManager.d("validate:" + stringBuffer.toString());
        if (checkTransfer.isSuccess()) {
            if (checkTransfer.responsePublicModel.eventManagement.getErrorCode().equals("91082")) {
                // BaseActivity activity = (BaseActivity)getContext();
                // activity.displayErrorMessage(checkTransfer.responsePublicModel.eventManagement.getErrorDescription());
                // return false;
                eventManagement.setErrorCode(checkTransfer.responsePublicModel.eventManagement
                        .getErrorCode());
                eventManagement
                        .setErrorDescription(checkTransfer.responsePublicModel.eventManagement
                                .getErrorDescription());
            }

            BankRecipient bankRecipient = new BankRecipient();
            bankRecipient.setIbanCode(destAccount.getIban());
            bankRecipient.setName(destAccount.getTitle());
            if (insertRecipient(InsertRecipientJson.BANK, bankRecipient, null, null)) {
                return true;
            } else {
                BaseActivity activity = (BaseActivity)getContext();
                activity.displayErrorMessage(responsePublicModel.eventManagement
                        .getErrorDescription());
                return false;
            }
        } else {
            BaseActivity activity = (BaseActivity)getContext();
            activity.displayErrorMessage(checkTransfer.responsePublicModel.eventManagement
                    .getErrorDescription());
        }
        return false;
    }

    private String generateDescription() {
        String result = "";
        if (reasonExpander.getValue() != null) {
            result = reasonExpander.getValue().toString();

        }
        return result;
    }

    public AccountsModel generateAccountModel() {
        return (AccountsModel)accountExpander.expandedContainer.getValue();
    }

    private double generateAmount() {
        Object amount = amountExpander.getValue();
        double amountInt = 0;
        if (amount instanceof AmountAvailable) {

            AmountAvailable amountAvailable = (AmountAvailable)amount;
            amountInt = Double.parseDouble(amountAvailable.getDescription());
        } else {
            try {
                amountInt = Double.parseDouble((String)amount);
            } catch (Exception e) {
                amountInt = 0;
            }

        }
        return amountInt;
    }

    private String genereateTransferType() {
        String typeCode = (String)typeExpander.getValue();
        String transferType = "";
        if (typeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            transferType = "0";
        } else if (typeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            transferType = "1";
        }
        return transferType;
    }

    private String generateDate() {
        long date = (Long)dateExpander.getValue();
        String dateString = generateDate(date);
        return dateString;
    }

    private String generateDate(long date) {
        return TimeUtil.getDateString(date, TimeUtil.dateFormat2);
    }
    CheckSimTopUpResponseModel checkSimTopUp;
    private boolean validateSimTopUp() {
        AccountsModel accountsModel = generateAccountModel();

        List<AmountAvailable> amountAvailableList = generateAmountAvailableList();
        if (amountAvailableList == null || amountAvailableList.size() == 0
                || amountAvailableList.get(0) == null) {
            BaseActivity activity = (BaseActivity)getContext();
            activity.displayErrorMessage("Missing amount");
            return false;
        }

        PayeeExpandedLayout.SimTopUpValue value = (SimTopUpValue)payeeExpander.getValue();
        String postData = CheckSimTopUpJson.CheckSimTopUpReportProtocal(Contants.publicModel,
                amountAvailableList, accountsModel.getAccountCode(), value.destProvider,
                value.phoneNumber, "1000",null);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                getContext());
        checkSimTopUp = CheckSimTopUpJson
                .ParseCheckSimTopUpResponse(httpResult);
        fee = checkSimTopUp.getCharges();
        if (checkSimTopUp.responsePublicModel.isSuccess()) {
            PhoneRecipient phoneRecipient = new PhoneRecipient();
            phoneRecipient.setName(value.name);
            phoneRecipient.setPhoneNumber(value.phoneNumber);
            phoneRecipient.setProvider(value.destProvider);
            if (insertRecipient(InsertRecipientJson.PHONE, null, phoneRecipient, null)) {
                return true;
            } else {
                BaseActivity activity = (BaseActivity)getContext();
                activity.displayErrorMessage(responsePublicModel.eventManagement
                        .getErrorDescription());
                return false;
            }
        } else {
            BaseActivity activity = (BaseActivity)getContext();
            activity.displayErrorMessage(checkSimTopUp.responsePublicModel.eventManagement
                    .getErrorDescription());
        }
        return false;

    }
    CheckRechargeCardResponseModel checkRechargeCard;
    private boolean validateCard() {
        // String transferType = genereateTransferType();
        // DestaccountModel destAccount =
        // generateDestAccountModel(transferType);

        // checkRechargeCardObj.put("purposeDescription", purposeDescription);
        // checkRechargeCardObj.put("destCardCode", destCardCode);
        // checkRechargeCardObj.put("cardNumber", cardNumber);
        // checkRechargeCardObj.put("title", title);
        // checkRechargeCardObj.put("name", name);
        // checkRechargeCardObj.put("accountCode", accountCode);
        // checkRechargeCardObj.put("currency", currency);
        String dateString = generateDate();
        AccountsModel accountsModel = generateAccountModel();
        PayeeExpandedLayout.PreparedCardValue value = (PayeeExpandedLayout.PreparedCardValue)payeeExpander
                .getValue();
        double amountdouble = generateAmount();
        String description = generateDescription();
        String title = "10";
        String postData = CheckRechargeCardJson.CheckRechargeCardReportProtocal(
                accountsModel.getAccountCode(), Contants.publicModel, amountdouble, description,
                value.cardHash, value.cardNumber, title, value.beneficiaryName, "1000",null);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                getContext());
        checkRechargeCard = CheckRechargeCardJson
                .ParseCheckRechargeCardResponse(httpResult);
        fee = checkRechargeCard.getCharges();
        if (checkRechargeCard.responsePublicModel.isSuccess()) {
            CardRecipient cardRecipient = new CardRecipient();
            cardRecipient.setCardNumber(value.cardNumber);
            cardRecipient.setName(value.beneficiaryName);
            if (insertRecipient(InsertRecipientJson.CARD, null, null, cardRecipient)) {
                return true;
            } else {
                BaseActivity activity = (BaseActivity)getContext();
                activity.displayErrorMessage(responsePublicModel.eventManagement
                        .getErrorDescription());
                return false;
            }
        } else {
            BaseActivity activity = (BaseActivity)getContext();
            // activity.displayErrorMessage(checkRechargeCard.responsePublicModel
            // .getResultDescription());
            activity.displayErrorMessage(checkRechargeCard.responsePublicModel.eventManagement
                    .getErrorDescription());
        }
        return false;
    }

    private List<AmountAvailable> generateAmountAvailableList() {
        AmountAvailable amountAvailable = (AmountAvailable)amountExpander.getValue();
        List<AmountAvailable> amountAvailableList = new ArrayList<AmountAvailable>();
        amountAvailableList.add(amountAvailable);
        return amountAvailableList;
    }

    public static class ValidateData {

        public String type, account, date, amount, reason, fees, beneficiary, iban, bic;

    }

    private void setRequirePinUI() {
        pinExpander.setVisibility(View.GONE);
        validateButton.setVisibility(View.VISIBLE);
        confirmTransactionButton.setVisibility(View.GONE);
        setExpandable(false);
        pinExpander.setExpand(false);
        typeExpander.setExpandable(true);

    }

    public void reset() {
        LogManager.d("reset");
        typeExpander.reset();
        serviceCode = "";
        accountExpander.reset();
        payeeExpander.reset();
        dateExpander.reset();
        amountExpander.reset();
        reasonExpander.reset();
        pinExpander.reset();
        accountRecover = false;
        setRequirePinUI();
    }

    private void setTranslactionUI() {
        pinExpander.setVisibility(View.VISIBLE);
        validateButton.setVisibility(View.GONE);
        confirmTransactionButton.setVisibility(View.VISIBLE);
        typeExpander.setExpandable(false);
        setExpandable(false);
        pinExpander.setExpandable(true);
        pinExpander.setExpand(true);
    }

    GenerateOTPResponseModel generateOtp;

    public void askPin() {
        validateDialog.dismiss();

        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                // TODO Auto-generated method stub
                MainActivity mainActivity = (MainActivity)getContext();
                String otpChannel = "";
                if (mainActivity.setting.getChannelToRecelvePin() == SettingModel.EMAIL) {
                    otpChannel = "MAIL";
                } else if (mainActivity.setting.getChannelToRecelvePin() == SettingModel.SMS) {
                    otpChannel = "CELL";
                }
                String postData = GenerateOTPJson.GenerateOTPReportProtocal(otpChannel,
                        Contants.publicModel);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        getContext());
                generateOtp = GenerateOTPJson.ParseGenerateOTPResponse(httpResult);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (generateOtp.responsePublicModel.isSuccess()) {
                            // Toast.makeText(getContext(),
                            // "ask pin success ,plealse get pin by email",
                            // Toast.LENGTH_LONG)
                            // .show();
                            DialogManager.createMessageDialog("PIN sent", "proceed", getContext(),
                                    PaymentConfirmLayout.this).show();

                            setTranslactionUI();
                        } else {
                            BaseActivity baseActivity = (BaseActivity)getContext();
                            baseActivity
                                    .displayErrorMessage(generateOtp.responsePublicModel.eventManagement
                                            .getErrorDescription());
                            // DialogManager.createMessageDialog(generateOtp.responsePublicModel.eventManagement.getErrorDescription(),
                            // getContext());
                            // Toast.makeText(getContext(),
                            // "ask pin fail ,plealse get pin by email",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public void recover(RecentTransferModel recentTransferModel) {
        BeneficiaryValue beneficiaryValue = new BeneficiaryValue();

        String type = recentTransferModel.getType();
        String account = recentTransferModel.getAccount();
        String date = recentTransferModel.getOperationDate();
        beneficiaryValue.beneficiaryAccount = recentTransferModel.getBeneficiaryAccount();
        String amount = String.valueOf(recentTransferModel.getAmount());
        String description = recentTransferModel.getDescription();
        beneficiaryValue.beneficiaryName = recentTransferModel.getBeneficiaryName();
        beneficiaryValue.beneficiaryIban = recentTransferModel.getBeneficiaryIban();
        beneficiaryValue.beneficiaryCardNumber = recentTransferModel.getBeneficiaryCardNumber();
        beneficiaryValue.serviceType = recentTransferModel.getType();
        beneficiaryValue.operator = recentTransferModel.getBeneficiaryProvider();
        beneficiaryValue.beneficiaryPhoneNumber = recentTransferModel.getBeneficiaryNumber();

        onRecover(type, account, date, beneficiaryValue, amount, description);
    }

    public void goToPayment(BalanceAccountsModel balanceAccountsModel) {

        setExpandable(true);
        // String type = balanceAccountsModel.getAccountBalance();
        String account = balanceAccountsModel.getAccountCode();
        // String date = balanceAccountsModel.getCurrency();
        // String payee = balanceAccountsModel.getBeneficiaryAccount();
        // String amount =
        // String.valueOf(balanceAccountsModel.getAccountBalance());
        // String description = balanceAccountsModel.getDescription();
        onRecover(null, account, null, null, null, null);
    }

    public static class BeneficiaryValue {
        String beneficiaryAccount;

        String beneficiaryName;

        String beneficiaryIban;

        String beneficiaryCardNumber;

        String beneficiaryPhoneNumber;

        String operator;

        String serviceType;
    }

    private void onRecover(String type, String account, String date, Object beneficiary,
            String amount, String description) {

        updateUiByaccounts(bankTransferAccounts);

        typeExpander.setExpand(false);
        accountExpander.setExpand(false);
        dateExpander.setExpand(false);
        payeeExpander.setExpand(false);
        amountExpander.setExpand(false);
        reasonExpander.setExpand(false);

        typeExpander.recover(type);
        accountExpander.recover(account);
        dateExpander.recover(date);
        // 为了解recover，受益人需要变灰
        if ("TRANSFER ENTRY".equals(type)) {

            PayeeExpandedLayout payeeExpandedLayout = (PayeeExpandedLayout)payeeExpander.expandedContainer;
            payeeExpandedLayout.currentTypeCode = ServiceCode.TRANSFER_ENTRY_PAYMENT;
        }
        payeeExpander.recover(beneficiary);
        amountExpander.recover(amount);
        reasonExpander.recover(description);

        setRequirePinUI();
        if (type == null && beneficiary == null) {
            setExpandable(false);
            accountRecover = true;
        } else {
            setExpandable(true);
            accountRecover = false;
        }
    }

    public void initData() {
        LogManager.d("initData");
        getAccountsByServiceWidthProcessBar();

    }

    public GetRecipientListModel getRecipientListModel;

    private void getRecipientList() {
        String postData = GetRecipientListJson.getRecipientListReportProtocal(Contants.publicModel);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                getContext());
        getRecipientListModel = GetRecipientListJson.parseGetRecipientListResponse(httpResult);

        LogManager.d("getRecipientListModel" + getRecipientListModel);

    }

    /**
     * @return the getRecipientListModel
     */
    public GetRecipientListModel getGetRecipientListModel() {
        return getRecipientListModel;
    }

    public void getAccountsByServiceWidthProcessBar() {
        OnProgressEvent onProgressEvent = new OnProgressEvent() {

            @Override
            public void onProgress() {

                bankTransferAccounts = getAccountsByService("003");

                transferEntryAccounts = getAccountsByService("008");

                simTopUpAccounts = getAccountsByService("022");

                chargeAccounts = getAccountsByService("026");

                getRecipientList();

            }

        };

        if (!BaseActivity.isOffline) {

            ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
            progressOverlay.show("loading", onProgressEvent);

        }
    }

    List<AccountsForServiceModel> bankTransferAccounts;

    List<AccountsForServiceModel> transferEntryAccounts;

    List<AccountsForServiceModel> simTopUpAccounts;

    List<AccountsForServiceModel> chargeAccounts;

    List<AccountsForServiceModel> transferEntryPayeeAccounts;

    private List<AccountsForServiceModel> getAccountsByService(String serviceCode) {
        try {

            List<AccountsForServiceModel> accountsForServiceModels = null;
            List<ServicesModel> services = new ArrayList<ServicesModel>();
            ServicesModel service = new ServicesModel();
            service.setServiceCode(serviceCode);
            services.add(service);
            String postData = AccountsByServicesJson.AccountsByServicesReportProtocal(
                    Contants.publicModel, services);
            HttpConnector httpConnector = new HttpConnector();
            String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                    getContext());
            GetAccountsByServicesResponseModel getAccountsByServices = AccountsByServicesJson
                    .ParseGetAccountsByServicesResponse(httpResult);
            if (getAccountsByServices == null) {

                LogManager.d("responsePublicModelgetAccountsByServices=null" + postData

                );
                return null;
            }

            if (getAccountsByServices.responsePublicModel != null) {
                if (getAccountsByServices.responsePublicModel.isSuccess()) {

                    accountsForServiceModels = getAccountsByServices.getAccountsForServiceList();

                } else {
                    BaseActivity baseActivity = (BaseActivity)getContext();
                    baseActivity
                            .displayErrorMessage(getAccountsByServices.responsePublicModel.eventManagement
                                    .getErrorDescription());
                }
            }

            return accountsForServiceModels;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    boolean isSuccess;

    public boolean insertRecipient(final String recipientType, final BankRecipient bankRecipient,
            final PhoneRecipient phoneRecipient, final CardRecipient cardRecipient) {
        // ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        // progressOverlay.show("", new OnProgressEvent() {
        // @Override
        // public void onProgress() {

        PayeeExpandedLayout payeeExpandedLayout = (PayeeExpandedLayout)payeeExpander.expandedContainer;
        if (!payeeExpandedLayout.addInPayee()) {
            return true;
        }

        String postData = null;
        if (recipientType.equals(InsertRecipientJson.BANK)) {
            postData = InsertRecipientJson.InsertRecipientReportProtocal(Contants.publicModel,
                    recipientType, bankRecipient, null, null);
        } else if (recipientType.equals(InsertRecipientJson.PHONE)) {
            postData = InsertRecipientJson.InsertRecipientReportProtocal(Contants.publicModel,
                    recipientType, null, null, phoneRecipient);
        } else if (recipientType.equals(InsertRecipientJson.CARD)) {
            postData = InsertRecipientJson.InsertRecipientReportProtocal(Contants.publicModel,
                    recipientType, null, cardRecipient, null);
        }
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                getContext());
        responsePublicModel = InsertRecipientJson.ParseInsertRecipientResponse(httpResult);
        if (responsePublicModel.isSuccess()) {
            isSuccess = true;
        } else {
            isSuccess = false;
            DialogManager.createMessageDialog(
                    responsePublicModel.eventManagement.getErrorDescription(), getContext());
        }
        // }g
        // });
        return isSuccess;
    }

    @Override
    public void onButtonClick() {
        // TODO 自动生成的方法存根
        showValidateDialog(this.typeCode);
    }
}
