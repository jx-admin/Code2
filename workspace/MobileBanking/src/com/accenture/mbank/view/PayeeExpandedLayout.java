
package com.accenture.mbank.view;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.GetCardsJson;
import com.accenture.mbank.model.Account;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.GetCardsResponseModel;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.InfoCardsModel;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.AvailableOperator;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DestProvider;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.DialogManager.OnPayeeSelectListener;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceCode;
import com.accenture.mbank.view.PaymentConfirmLayout.BeneficiaryValue;

public class PayeeExpandedLayout extends ExpandedContainer implements OnClickListener,
        OnItemClickListener, OnFocusChangeListener, OnCheckedChangeListener, OnPayeeSelectListener {

    LinearLayout bankTransferLayout, transferEntryLayout, simtopUpLayout, prepaidCardLayout;

    CheckBox addToBeneficiariesCheckBox;

    Handler handler;

    Object value;

    Button payee;

    LinearLayout payees_layout;

    private List<AccountsForServiceModel> accountsForServiceModels;

    private GetRecipientListModel getRecipientListModel;

    public String cardHash;

    private static boolean isVerifyCard = false;

    /**
     * @return isVerifyCard
     */
    public static boolean isVerifyCard() {
        return isVerifyCard;
    }

    /**
     * @return the cardHash
     */
    public String getCardHash() {
        return cardHash;
    }

    /**
     * @param cardHash the cardHash to set
     */
    public void setCardHash(String cardHash) {
        this.cardHash = cardHash;
    }

    /**
     * @param getRecipientListModel the getRecipientListModel to set
     */
    public void setGetRecipientListModel(GetRecipientListModel getRecipientListModel) {
        this.getRecipientListModel = getRecipientListModel;
    }

    public PayeeExpandedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (simTopUpNameEdit == null) {
            simTopUpNameEdit = (EditText)findViewById(R.id.name_edit);
            if (BaseActivity.initValue) {
                simTopUpNameEdit.setText("seekting.x.zhang");
            }
            phoneNumberEdit = (EditText)findViewById(R.id.phone_edit);
            if (BaseActivity.initValue) {
                phoneNumberEdit.setText("13691168978");
            }
            operatorText = (TextView)findViewById(R.id.operator);
            bankTransferLayout = (LinearLayout)findViewById(R.id.bank_transfer_layout);
            transferEntryLayout = (LinearLayout)findViewById(R.id.transfer_entry_layout);
            simtopUpLayout = (LinearLayout)findViewById(R.id.sim_top_up_layout);
            prepaidCardLayout = (LinearLayout)findViewById(R.id.prepaid_card_reloadlayout);
            addToBeneficiariesCheckBox = (CheckBox)findViewById(R.id.ad_to_beneficiaries);
            // banktransfer
            bankTransferNameEditText = (EditText)findViewById(R.id.bank_name_edit);
            ibanEdtiText = (EditText)findViewById(R.id.iban_code);
            bicEditText = (EditText)findViewById(R.id.bic_code);

            // transferEntry

            tranGroup = (RadioGroup)findViewById(R.id.transfer_entry_rg);
            tranGroup.removeAllViews();

            // re

            beneficiaryNameEditText = (EditText)findViewById(R.id.beneficiary_name);

            cardNumberEditText = (EditText)findViewById(R.id.card_number);
            myCardBtn = (ImageButton)findViewById(R.id.my_card_btn);
            vertifyBtn = (ImageButton)findViewById(R.id.vertify_card_btn);
            myCardBtn.setOnClickListener(this);
            vertifyBtn.setOnClickListener(this);

            expandFocusResultChange(simTopUpNameEdit.getText().toString());
            // value = generateValue();
            TextWatcher textWatcher = newTextChangeListener();

            bankTransferNameEditText.addTextChangedListener(textWatcher);
            // ibanEdtiText.addTextChangedListener(textWatcher);
            // bicEditText.addTextChangedListener(textWatcher);

            // phoneNumberEdit.addTextChangedListener(textWatcher);
            simTopUpNameEdit.addTextChangedListener(textWatcher);
            phoneNumberEdit.addTextChangedListener(textWatcher);

            beneficiaryNameEditText.addTextChangedListener(textWatcher);
            cardNumberEditText.addTextChangedListener(textWatcher);

            bankTransferNameEditText.setOnFocusChangeListener(this);
            ibanEdtiText.setOnFocusChangeListener(this);
            bicEditText.setOnFocusChangeListener(this);

            simTopUpNameEdit.setOnFocusChangeListener(this);
            phoneNumberEdit.setOnFocusChangeListener(this);

            beneficiaryNameEditText.setOnFocusChangeListener(this);
            cardNumberEditText.setOnFocusChangeListener(this);
        }
        operatorText.setOnClickListener(this);
        payee = (Button)findViewById(R.id.payees);
        payee.setSingleLine(true);
        payee.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        payee.setOnClickListener(this);
        payees_layout = (LinearLayout)findViewById(R.id.payees_layout);

    }

    AccountsModel selectedAccountsModel;

    /**
     * 此方法别删除，现在搞不大清楚transfer entry里的account是从哪来的
     * 
     * @param accountsForServiceModels
     * @param selectedAccountsModel
     */
    public void createAccountsUI(final List<AccountsForServiceModel> accountsForServiceModels,
            final AccountsModel selectedAccountsModel) {

        this.selectedAccountsModel = selectedAccountsModel;
        boolean accountbyservicemode = false;

        if (BaseActivity.isOffline) {
            return;
        }
        if (this.accountsForServiceModels == accountsForServiceModels) {

        } else {
            this.accountsForServiceModels = accountsForServiceModels;
        }

        for (int index = 0; index < tranGroup.getChildCount(); index++) {
            RadioButton child = (RadioButton)tranGroup.getChildAt(index);
            child.setOnCheckedChangeListener(null);

        }
        tranGroup.removeAllViews();
        if (accountbyservicemode) {
            for (int i = 0; i < accountsForServiceModels.size(); i++) {
                AccountsForServiceModel accountsForServiceModel = accountsForServiceModels.get(i);
                LogManager.d(accountsForServiceModel.getServiceCode());

                for (AccountsModel accountsModel : accountsForServiceModel.getAccounts()) {

                    // LogManager.d(accountsModel.getAccountAlias());
                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    RadioButton radioButton = (RadioButton)layoutInflater.inflate(
                            R.layout.bank_radio_button_item, null);
                    radioButton.setText(accountsModel.getAccountAlias());
                    if (accountsModel == selectedAccountsModel) {
                        radioButton.setEnabled(false);
                    }
                    radioButton.setTag(accountsModel);
                    radioButton.setOnCheckedChangeListener(PayeeExpandedLayout.this);
                    tranGroup.addView(radioButton);

                }
            }

        } else {
            LogManager.d("owner is get");
            PaymentConfirmLayout paymentConfirmLayout = (PaymentConfirmLayout)expandBarResultListener
                    .getOwener();
            List<Account> bankRecipients = paymentConfirmLayout.getRecipientListModel
                    .getBankRecipientList();

            if (bankRecipients != null) {
                for (AccountsForServiceModel bankRecipient : accountsForServiceModels) {

                    List<AccountsModel> accounts = bankRecipient.getAccounts();
                    for (AccountsModel account : accounts) {

                        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                        RadioButton radioButton = (RadioButton)layoutInflater.inflate(
                                R.layout.bank_radio_button_item, null);
                        radioButton.setText(account.getAccountAlias());
                        if (selectedAccountsModel.getAccountAlias() == account.getAccountAlias()) {
                            radioButton.setEnabled(false);
                        }

                        radioButton.setTag(account);
                        radioButton.setOnCheckedChangeListener(PayeeExpandedLayout.this);
                        tranGroup.addView(radioButton);

                    }

                }

            }
        }
        if (currentTypeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            expandFocusResultChange("");
        }
    }

    public void setEnabledPosition(int position) {

        if (accountsForServiceModels != null && accountsForServiceModels.size() >= position) {
            RadioButton radioButton = (RadioButton)tranGroup.getChildAt(position);
            radioButton.setEnabled(false);

        }
    };

    List<String> lists = new ArrayList<String>();

    AlertDialog alertDialog;

    public void onMyCardSelected(AccountsModel accountsModel) {

        beneficiaryNameEditText.setText(accountsModel.getCardHolder());
        cardNumberEditText.setText(accountsModel.getCardNumber());
        isVerifyCard = false;
    }

    @Override
    public void onClick(View v) {

        if (v == operatorText) {
            lists.clear();

            lists.add(AvailableOperator.TIM_NAME);
            lists.add(AvailableOperator.VODAFONE_NAME);
            lists.add(AvailableOperator.TISCALI_NAME);
            lists.add(AvailableOperator.WIND_NAME);
            lists.add(AvailableOperator.TRE_NAME);

            alertDialog = DialogManager.createOperatorDialog(this, lists);
            alertDialog.show();
        } else if (v == myCardBtn) {

            // DialogManager.createMessageDialog("transfer success!",

            // getContext()).show();
            if (prepareCard()) {
                DialogManager.createMyCardDialog(this, myCardAccounts).show();

            } else {
                BaseActivity baseActivity = (BaseActivity)getContext();
                baseActivity.displayErrorMessage("No available card");
            }

        } else if (v == vertifyBtn) {
            ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
            progressOverlay.show("verify...", new OnProgressEvent() {
                @Override
                public void onProgress() {
                    PaymentConfirmLayout paymentConfirmLayout = (PaymentConfirmLayout)expandBarResultListener
                            .getOwener();
                    AccountsModel accountsModel = paymentConfirmLayout.generateAccountModel();

                    String postData = GetCardsJson.GetCardsReportProtocal(Contants.publicModel,
                            beneficiaryNameEditText.getText().toString(), cardNumberEditText
                                    .getText().toString(), accountsModel.getAccountCode());
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, getContext());
                    GetCardsResponseModel getCards = GetCardsJson.parseGetCardResponse(httpResult);
                    List<InfoCardsModel> list = getCards.getInfoCardListModel();
                    BaseActivity baseActivity = (BaseActivity)getContext();
                    if (list != null && list.size() > 0) {
                        setCardHash(list.get(0).getCardHash());
                        isVerifyCard = true;
                        title = list.get(0).getTitle();
                        name = list.get(0).getName();
                        baseActivity.displayErrorMessage("verify success!");
                    } else {
                        baseActivity.displayErrorMessage("verify fail!");
                    }
                }
            });
        } else if (v == payee) {
            // 这里写 showDialog
            DialogManager.createPayeeDialog(this, getRecipientListModel, currentTypeCode).show();
        } else if (v.getParent() == payees_layout) {
            Object o = v.getTag();
            if (o instanceof CardRecipient) {
                CardRecipient resultBankRecipient = (CardRecipient)o;
                beneficiary = resultBankRecipient.getName();
                beneficiaryNameEditText.setText(resultBankRecipient.getName());
                cardNumberEditText.setText(resultBankRecipient.getCardNumber());
            } else if (o instanceof PhoneRecipient) {
                PhoneRecipient resultBankRecipient = (PhoneRecipient)o;
                beneficiary = resultBankRecipient.getPhoneNumber();
                simTopUpNameEdit.setText(resultBankRecipient.getName());
                phoneNumberEdit.setText(resultBankRecipient.getPhoneNumber());
                String destProvider = DestProvider.getDsstProvider(resultBankRecipient
                        .getProvider());
                operatorText.setText(destProvider);
            } else if (o instanceof BankRecipient) {
                BankRecipient resultBankRecipient = (BankRecipient)o;
                beneficiary = resultBankRecipient.getName();
                iban = resultBankRecipient.getIbanCode();
                bic = resultBankRecipient.getBic();
                bankTransferNameEditText.setText(resultBankRecipient.getName());
                ibanEdtiText.setText(resultBankRecipient.getIbanCode());

            }
            payees_layout.removeAllViews();
        }
    }

    private TextWatcher newTextChangeListener() {
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isVerifyCard = false;
                expandResultChange(generateText());
                // value = generateValue();
                createPayeeListButton(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        return textWatcher;
    }

    void createPayeeListButton(String key) {

        if (key.length() <= 0) {
            payees_layout.removeAllViews();
            return;
        }
        payees_layout.removeAllViews();
        if (currentTypeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            List<Account> list1 = getRecipientListModel.getBankRecipientList();
            for (Account str : list1) {
                String name = ((BankRecipient)str).getName();
                generateButton((BankRecipient)str, name, key);

            }
        } else if (currentTypeCode.equals(ServiceCode.SIM_TOP_UP)) {
            List<PhoneRecipient> list1 = getRecipientListModel.getPhoneRecipientList();
            for (PhoneRecipient str : list1) {
                String name = str.getName();
                generateButton(str, name, key);

            }
        } else if (currentTypeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            List<CardRecipient> list1 = getRecipientListModel.getCardRecipientList();
            for (CardRecipient str : list1) {
                String name = str.getName();
                generateButton(str, name, key);

            }
        }
    }

    private void generateButton(Object o, String name, String key) {
        if (name == null || name.length() < key.length()) {
            return;
        }
        String newStr = name.substring(0, key.length());
        if (!newStr.toUpperCase().equals(key.toUpperCase())) {
            return;
        }
        Button button = new Button(getContext());
        button.setEllipsize(TruncateAt.END);
        button.setText(name);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        payees_layout.addView(button);
        button.setOnClickListener(this);
        button.setTag(o);
    }

    String currentTypeCode = "";

    public void showType(final String typeCode) {
        resetResult();
        handler.post(new Runnable() {

            @Override
            public void run() {
                payee.setText(getContext().getResources().getString(R.string.beneficiaries));
                if (typeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
                    payee.setVisibility(View.GONE);
                    payees_layout.setVisibility(View.VISIBLE);
                    showBankTransferUI();
                } else if (typeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
                    payee.setVisibility(View.GONE);
                    payees_layout.setVisibility(View.GONE);
                    showTransferEntryUI();
                } else if (typeCode.equals(ServiceCode.SIM_TOP_UP)) {
                    payee.setVisibility(View.GONE);
                    payees_layout.setVisibility(View.VISIBLE);
                    showSimTopUpUI();
                } else if (typeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
                    payee.setVisibility(View.GONE);
                    payees_layout.setVisibility(View.VISIBLE);
                    showPrepaidCardUI();
                }
                currentTypeCode = typeCode;
            }
        });

    }

    private void hideUI() {
        bankTransferLayout.setVisibility(View.GONE);
        transferEntryLayout.setVisibility(View.GONE);
        simtopUpLayout.setVisibility(View.GONE);
        prepaidCardLayout.setVisibility(View.GONE);
        prepaidCardLayout.setVisibility(View.GONE);
        vertifyBtn.setVisibility(View.GONE);

    }

    EditText bankTransferNameEditText, ibanEdtiText, bicEditText;

    public boolean addInPayee() {
        if (addToBeneficiariesCheckBox.getVisibility() == View.VISIBLE) {

            return addToBeneficiariesCheckBox.isChecked();
        } else {
            return false;
        }
    }

    private void showBankTransferUI() {
        hideUI();
        bankTransferLayout.setVisibility(View.VISIBLE);
        addToBeneficiariesCheckBox.setVisibility(View.VISIBLE);
        if (BaseActivity.initValue) {
            // bankTransferNameEditText.setText("mrdd");
            // ibanEdtiText.setText("IT41X0538712900000001413970");
            // bicEditText.setText("");
            init = false;
        } else {
            /*
             * ibanEdtiText.setText(""); bicEditText.setText("");
             */
        }

    }

    RadioGroup tranGroup;

    String result;

    private void showTransferEntryUI() {
        hideUI();
        transferEntryLayout.setVisibility(View.VISIBLE);
        addToBeneficiariesCheckBox.setVisibility(View.GONE);
        generateTransferEntrys();
    }

    public void generateTransferEntrys() {

        for (int i = 0; i < tranGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton)tranGroup.getChildAt(i);
            radioButton.setOnCheckedChangeListener(this);
        }
    }

    EditText simTopUpNameEdit, phoneNumberEdit;

    TextView operatorText;

    private void showSimTopUpUI() {
        hideUI();
        simtopUpLayout.setVisibility(View.VISIBLE);
        addToBeneficiariesCheckBox.setVisibility(View.VISIBLE);
        // simTopUpNameEdit.setText("");
        // phoneNumberEdit.setText("");
        // operatorText.setText("");

    }

    ImageButton myCardBtn, vertifyBtn;

    EditText beneficiaryNameEditText, cardNumberEditText;

    private void showPrepaidCardUI() {
        hideUI();
        prepaidCardLayout.setVisibility(View.VISIBLE);
        vertifyBtn.setVisibility(View.VISIBLE);
        addToBeneficiariesCheckBox.setVisibility(View.VISIBLE);
        if (BaseActivity.initValue) {
            // beneficiaryNameEditText.setText("seekting");
            // cardNumberEditText.setText("6684");
        } else {
            // beneficiaryNameEditText.setText("");
            // cardNumberEditText.setText("");
        }
    }

    List<AccountsModel> myCardAccounts;

    private boolean prepareCard() {
        boolean result = false;
        List<AccountsModel> accounts = Contants.getUserInfo.getAccountList();

        if (myCardAccounts == null) {
            myCardAccounts = new ArrayList<AccountsModel>();

        }
        myCardAccounts.clear();
        for (AccountsModel accountsModel : accounts) {
            String serviceCode = accountsModel.getBankServiceType().getBankServiceCode();
            if (serviceCode == null || serviceCode.equals("")) {

                continue;
            } else {
                if (serviceCode.equals("867") || serviceCode.equals("887")) {
                    // baseActivity.displayErrorMessage("find 867 or 887");

                    myCardAccounts.add(accountsModel);
                    result = true;
                }
            }
        }
        return result;

    }

    private String getDestProvider() {
        return AvailableOperator.getCodeByName(operatorText.getText().toString());

    }

    private String generateText() {

        if (currentTypeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {

            return bankTransferNameEditText.getText().toString();

        } else if (currentTypeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {

            return result;

        } else if (currentTypeCode.equals(ServiceCode.SIM_TOP_UP)) {
            if (!simTopUpNameEdit.getText().toString().equals("")) {
                return simTopUpNameEdit.getText().toString();
            } else {
                return phoneNumberEdit.getText().toString();
            }
            // return simTopUpNameEdit.getText().toString();
        } else if (currentTypeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {

            return beneficiaryNameEditText.getText().toString();
        }

        return null;

    }

    String title;

    String name;

    private Object generateValue() {

        if (currentTypeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            BankTransferValue value = new BankTransferValue();
            value.iban = ibanEdtiText.getText().toString();
            value.bic = bicEditText.getText().toString();
            value.name = bankTransferNameEditText.getText().toString();
            beneficiary = value.name;
            return value;
        } else if (currentTypeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            TransferEntryValue value = new TransferEntryValue();
            if (account == null) {
            } else {
                value.value = account;
                beneficiary = value.value.getAccountAlias();
            }
            return value;
        } else if (currentTypeCode.equals(ServiceCode.SIM_TOP_UP)) {
            SimTopUpValue value = new SimTopUpValue();
            value.name = simTopUpNameEdit.getText().toString();
            value.phoneNumber = phoneNumberEdit.getText().toString();
            value.destProvider = getDestProvider();
            beneficiary = value.phoneNumber;

            return value;
        } else if (currentTypeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            PreparedCardValue value = new PreparedCardValue();
            value.beneficiaryName = beneficiaryNameEditText.getText().toString();
            beneficiary = value.beneficiaryName;
            value.cardNumber = cardNumberEditText.getText().toString();
            value.cardHash = getCardHash();
            value.title = title;
            value.name = name;
            return value;
        }

        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        operatorText.setText(lists.get(position));
        expandFocusResultChange(generateText());
        // value = generateValue();
        alertDialog.dismiss();

    }

    public static class BankTransferValue {
        String name;

        String iban;

        String bic;
    }

    public static class TransferEntryValue {
        AccountsModel value;
    }

    public static class SimTopUpValue {
        String name;

        String phoneNumber;

        String destProvider;
    }

    public static class PreparedCardValue {
        String beneficiaryName;

        String cardHash;

        String cardNumber;

        String title;

        String name;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (!hasFocus) {
            expandFocusResultChange(generateText());
        }

    }

    AccountsModel account;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            expandFocusResultChange(buttonView.getText().toString());
            Object o = buttonView.getTag();

            if (o instanceof AccountsModel) {
                account = (AccountsModel)buttonView.getTag();

            }
        }
    }

    @Override
    protected void resetResult() {
        super.resetResult();

        // banktransfer
        bankTransferNameEditText.setText("");
        ibanEdtiText.setText("");
        bicEditText.setText("");

        // transfer entry
        for (int i = 0; i < tranGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton)tranGroup.getChildAt(i);
            radioButton.setEnabled(true);
        }
        tranGroup.clearCheck();
        result = "";
        // sim
        simTopUpNameEdit.setText("");
        phoneNumberEdit.setText("");
        operatorText.setText("");
        // card
        beneficiaryNameEditText.setText("");
        cardNumberEditText.setText("");
        expandFocusResultChange("");
        account = null;

    }

    @Override
    protected void onRecover(String text) {
        resetResult();
        LogManager.d("onrecover" + text);
        if (text != null) {
            if (currentTypeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            } else if (currentTypeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            } else if (currentTypeCode.equals(ServiceCode.SIM_TOP_UP)) {
            } else if (currentTypeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
                cardNumberEditText.setText(text);
            }
        }
    }

    @Override
    protected void onRecover(Object object) {
        resetResult();
        if (object != null) {
            BeneficiaryValue beneficiary = (BeneficiaryValue)object;
            if (currentTypeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)
                    || beneficiary.serviceType.equals(Contants.BANK_TRANSFER)) {
                bankTransferNameEditText.setText(beneficiary.beneficiaryName);
                expandFocusResultChange(bankTransferNameEditText.getText().toString());
                ibanEdtiText.setText(beneficiary.beneficiaryIban);
                iban = beneficiary.beneficiaryIban;
            } else if (currentTypeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)
                    || beneficiary.serviceType.equals(Contants.TRANSFER_ENTRY)) {
                for (int i = 0; i < tranGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton)tranGroup.getChildAt(i);
                    if (radioButton.getText().equals(beneficiary.beneficiaryName)) {
                        radioButton.setChecked(true);
                    }
                    if (radioButton.getText().equals(selectedAccountsModel.getAccountAlias())) {
                        radioButton.setEnabled(false);
                    }
                }
            } else if (currentTypeCode.equals(ServiceCode.SIM_TOP_UP)
                    || beneficiary.serviceType.equals(Contants.SIM_TOP_UP)) {
                simTopUpNameEdit.setText(beneficiary.beneficiaryName);
                phoneNumberEdit.setText(beneficiary.beneficiaryPhoneNumber);
                String destProvider = DestProvider.getDsstProvider(beneficiary.operator);
                operatorText.setText(destProvider);
                if (!simTopUpNameEdit.getText().toString().equals("")) {
                    expandFocusResultChange(simTopUpNameEdit.getText().toString());
                } else {
                    expandFocusResultChange(phoneNumberEdit.getText().toString());
                }
            } else if (currentTypeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)
                    || beneficiary.serviceType.equals(Contants.PREPAID_CARD_RELOAD)) {
                beneficiaryNameEditText.setText(beneficiary.beneficiaryName);
                String cardNum = beneficiary.beneficiaryCardNumber.substring(
                        beneficiary.beneficiaryCardNumber.length() - 4,
                        beneficiary.beneficiaryCardNumber.length());
                cardNumberEditText.setText(cardNum);
                expandFocusResultChange(beneficiaryNameEditText.getText().toString());
            }
        } else {
            // 如果是显示的，就屏蔽一下
            for (int i = 0; i < tranGroup.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton)tranGroup.getChildAt(i);
                if (radioButton.getText().equals(selectedAccountsModel.getAccountAlias())) {
                    radioButton.setEnabled(false);
                }
            }
        }
        payees_layout.removeAllViews();
    }

    @Override
    protected Object getValue() {
        return generateValue();
    }

    @Override
    public void setEditable(boolean flag) {
        super.setEditable(flag);

    }

    public static String beneficiary;

    public static String iban;

    public static String bic;

    @Override
    public void onPayeeSelected(int index) {
        // getRecipientListModel

        if (currentTypeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            List<Account> list1 = getRecipientListModel.getBankRecipientList();
            BankRecipient resultBankRecipient = (BankRecipient) list1.get(index);
            // 这里通过resultBankRecipient更新ui
            beneficiary = resultBankRecipient.getName();
            iban = resultBankRecipient.getIbanCode();
            bic = resultBankRecipient.getBic();
            bankTransferNameEditText.setText(resultBankRecipient.getName());
            ibanEdtiText.setText(resultBankRecipient.getIbanCode());
            payee.setText(resultBankRecipient.getName() + "-" + resultBankRecipient.getIbanCode());
        } else if (currentTypeCode.equals(ServiceCode.SIM_TOP_UP)) {
            List<PhoneRecipient> list1 = getRecipientListModel.getPhoneRecipientList();
            PhoneRecipient resultBankRecipient = list1.get(index);
            // 这里通过resultBankRecipient更新ui
            beneficiary = resultBankRecipient.getPhoneNumber();
            simTopUpNameEdit.setText(resultBankRecipient.getName());
            phoneNumberEdit.setText(resultBankRecipient.getPhoneNumber());
            String destProvider = DestProvider.getDsstProvider(resultBankRecipient.getProvider());
            operatorText.setText(destProvider);
            payee.setText(resultBankRecipient.getName() + ":"
                    + resultBankRecipient.getPhoneNumber());
        } else if (currentTypeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            List<CardRecipient> list1 = getRecipientListModel.getCardRecipientList();
            CardRecipient resultBankRecipient = list1.get(index);
            // 这里通过resultBankRecipient更新ui
            beneficiary = resultBankRecipient.getName();
            beneficiaryNameEditText.setText(resultBankRecipient.getName());
            cardNumberEditText.setText(resultBankRecipient.getCardNumber());
            payee.setText(resultBankRecipient.getName() + "-" + resultBankRecipient.getCardNumber());
        }
    }
}
