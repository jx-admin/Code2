
package com.accenture.mbank.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.RecentTransferJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.RecentPaymentList;
import com.accenture.mbank.model.RecentTransferModel;
import com.accenture.mbank.model.RecentTransferResponseModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DestProvider;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceCode;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.TransferState;
import com.accenture.mbank.util.Utils;

public class RecentPayments extends LinearLayout implements OnCheckedChangeListener {

    RadioGroup radioGroup;

    // account列表的layout
    LinearLayout accountsLayout;

    // 每个account的layout
    LinearLayout account_layout;

    RecentPaymentList recentpaymentList;

    private List<RecentPaymentList> allList = new ArrayList<RecentPaymentList>();

    // 存放过滤出来的020 bankServiceCode的account
    private List<AccountsModel> accountsList = new ArrayList<AccountsModel>();

    Handler handler;

    public HashMap<RadioButton, LinearLayout> account_HashMap = new HashMap<RadioButton, LinearLayout>();

    public HashMap<RadioButton, String> accountCode_HashMap = new HashMap<RadioButton, String>();

    CompoundButton _buttonView;

    public static final int GET_RECENT_TRANSFER = 0x00;

    private ResponsePublicModel responsePublic;

    public RecentPayments(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        radioGroup = (RadioGroup)findViewById(R.id.type_of_payment_rg);
        accountsLayout = (LinearLayout)findViewById(R.id.accountsLayout);
        addAccount();
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

    public void addAccount() {
        if (BaseActivity.isOffline) {

            return;
        }
        radioGroup.removeAllViews();
        // 过滤BankServiceCode为020的account
        // nullpoint
        if (Contants.accountsList == null) {
            return;
        }
        for (int i = 0; i < Contants.accountsList.size(); i++) {
            if (Contants.accountsList.get(i).getBankServiceType().getBankServiceCode()
                    .equals(ServiceCode.CURRENT_ACCOUNT)) {
                AccountsModel account = new AccountsModel();
                account = Contants.accountsList.get(i);
                accountsList.add(account);
            }
        }

        // 添加radioButton
        for (int i = 0; i < accountsList.size(); i++) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            RadioButton radioButton = (RadioButton)layoutInflater.inflate(
                    R.layout.bank_radio_button_item, null);
            radioButton.setText(accountsList.get(i).getAccountAlias());
            radioButton.setOnCheckedChangeListener(RecentPayments.this);
            radioGroup.addView(radioButton);
            accountCode_HashMap.put(radioButton, accountsList.get(i).getAccountCode());

            LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            account_layout = new LinearLayout(getContext());
            account_layout.setLayoutParams(linearLayout);
            account_layout.setBackgroundResource(R.drawable.box_details);
            account_layout.setOrientation(VERTICAL);
            account_HashMap.put(radioButton, account_layout);

            accountsLayout.addView(account_layout);
        }
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 50);
        View nullView = new View(getContext());
        nullView.setLayoutParams(linearLayout);
        accountsLayout.addView(nullView);
    }

    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_RECENT_TRANSFER:
                    addChild(_buttonView);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void addChild(CompoundButton buttonView) {
        // //////////////////////////////////////////////////
        // LinearLayout layout = (LinearLayout)account_HashMap.get(buttonView);
        // 存放当前AccountCode请求到的RecentTransferList
        List<RecentTransferModel> recentTransferList = null;
        account_layout.removeAllViews();
        final String accountCode = (String)accountCode_HashMap.get(buttonView);
        for (int i = 0; i < allList.size(); i++) {
            if (accountCode.equals(allList.get(i).getAccountCode())) {
                recentTransferList = allList.get(i).getRecentTransferList();
            }
        }

        if (recentTransferList != null) {
            for (int j = 0; j < recentTransferList.size(); j++) {
                LogManager.d("recentTransferList" + recentTransferList.size());
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RecentPaymentItem child = (RecentPaymentItem)inflater.inflate(
                        R.layout.recent_payment_item, null);
                child.init();
                child.typeText.setText(recentTransferList.get(j).getType());
                String date = recentTransferList.get(j).getOperationDate();
                if (date != null || !"".equals(date)) {
                    try {
                        date = TimeUtil.changeFormattrString(date, TimeUtil.dateFormat2,
                                TimeUtil.dateFormat5);
                    } catch (Exception e) {
                        LogManager.e("formatDate is error" + e.getLocalizedMessage());
                    }
                }
                child.timeText.setText(date);
                // 格式化金额
                String amount = recentTransferList.get(j).getAmount() + "";
                amount = Utils.notPlusGenerateFormatMoney(
                        getContext().getResources().getString(R.string.dollar), amount);
                child.resultText.setText(amount);
                if (recentTransferList.get(j).getType().equals(Contants.SIM_TOP_UP)) {
                    // sim top up UI展示
                    // phone Number
                    child.account_text.setText(getResources().getString(R.string.phone_number1)
                            + recentTransferList.get(j).getBeneficiaryNumber());
                    // Operator
                    String operator = DestProvider.getDsstProvider(recentTransferList.get(j)
                            .getBeneficiaryProvider());
                    child.ibanCode_text.setText(getResources().getString(R.string.provider)
                            + operator);
                    String state = TransferState.getTransferState(recentTransferList.get(j)
                            .getTransferState());
                    child.des_text.setText(getResources().getString(R.string.recent_transfer_state)
                            + state);

                    child.bic_text.setVisibility(View.GONE);
                    child.transfer_status_text.setVisibility(View.GONE);

                    child.setTag(recentTransferList.get(j));
                    account_layout.addView(child);
                } else if (recentTransferList.get(j).getType().equals(Contants.BANK_TRANSFER)
                        || recentTransferList.get(j).getType().equals(Contants.TRANSFER_ENTRY)) {
                    // bank transfer & transfer entry UI展示
                    if (recentTransferList.get(j).getType().equals(Contants.TRANSFER_ENTRY)) {
                        child.account_text.setVisibility(View.GONE);
                    } else {
                        child.account_text.setText(getResources().getString(
                                R.string.recent_beneficiary)
                                + recentTransferList.get(j).getBeneficiaryName());
                    }
                    child.ibanCode_text.setText(getResources().getString(R.string.recent_iban)
                            + recentTransferList.get(j).getBeneficiaryIban());
                    child.des_text.setText(getResources().getString(R.string.recent_description)
                            + recentTransferList.get(j).getDescription());
                    String state = TransferState.getTransferState(recentTransferList.get(j)
                            .getTransferState());
                    child.transfer_status_text.setText(getResources().getString(
                            R.string.recent_transfer_state)
                            + state);

                    child.bic_text.setVisibility(View.GONE);
                    child.setTag(recentTransferList.get(j));
                    account_layout.addView(child);
                } else if (recentTransferList.get(j).getType().equals(Contants.PREPAID_CARD_RELOAD)) {
                    // prepaid card UI展示
                    child.account_text.setText(getResources()
                            .getString(R.string.recent_beneficiary)
                            + recentTransferList.get(j).getBeneficiaryName());
                    child.ibanCode_text.setText(getResources().getString(
                            R.string.recent_card_number)
                            + recentTransferList.get(j).getBeneficiaryCardNumber());
                    child.des_text.setVisibility(View.GONE);
                    child.transfer_status_text.setVisibility(View.GONE);
                    child.bic_text.setVisibility(View.GONE);

                    child.setTag(recentTransferList.get(j));
                    account_layout.addView(child);
                }
            }
            // LogManager.d("count-----" + account_layout.getChildCount());
            showAccountView(account_layout);
        }

    }

    void showAccountView(LinearLayout layout) {
        for (int i = 0; i < accountsLayout.getChildCount(); i++) {
            View child = accountsLayout.getChildAt(i);
            child.setVisibility(View.GONE);
        }
        layout.setVisibility(View.VISIBLE);
    }

    public static boolean needUpdate = false;

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        // TODO Auto-generated method stub
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == View.VISIBLE && needUpdate) {

            if (radioGroup == null) {
                return;
            }
            radioGroup.clearCheck();
            account_layout.removeAllViews();
            needUpdate = false;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            // 根据点击的radioButton 获取到当前的accountCode
            final String accountCode = (String)accountCode_HashMap.get(buttonView);
            _buttonView = buttonView;

            // for (int a = 0; a < allList.size(); a++) {
            // if (accountCode.equals(allList.get(a).getAccountCode())) {
            // myhandler.sendEmptyMessage(GET_RECENT_TRANSFER);
            // return;
            // }
            // }

            ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
            progressOverlay.show("loading", new OnProgressEvent() {
                @Override
                public void onProgress() {
                    // getRecentTransfer
                    MainActivity mainActivity = (MainActivity)getContext();
                    int recentPaymentDislayed = mainActivity.setting.getRecentPaymentsDisplayed();
                    String postData = RecentTransferJson.RecentTransferReportProtocal(
                            Contants.publicModel, accountCode, recentPaymentDislayed);
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, getContext());
                    final RecentTransferResponseModel recentTransfer = RecentTransferJson
                            .ParseRecentTransferResponse(httpResult);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (recentTransfer != null
                                    && recentTransfer.responsePublicModel.isSuccess()) {
                                // if (recentTransferList != null) {
                                // recentTransferList = null;
                                // }
                                // recentTransferList =
                                // recentTransfer.getRecentTransferList();

                                for (int i = 0; i < allList.size(); i++) {
                                    if (!accountCode.equals(allList.get(i).getAccountCode())) {
                                        recentpaymentList = new RecentPaymentList();
                                        recentpaymentList.setRecentTransferList(recentTransfer
                                                .getRecentTransferList());
                                        recentpaymentList.setAccountCode(accountCode);
                                        allList.add(recentpaymentList);
                                    }
                                }
                                if (allList.size() == 0) {
                                    recentpaymentList = new RecentPaymentList();
                                    recentpaymentList.setRecentTransferList(recentTransfer
                                            .getRecentTransferList());
                                    recentpaymentList.setAccountCode(accountCode);
                                    allList.add(recentpaymentList);
                                }

                                myhandler.sendEmptyMessage(GET_RECENT_TRANSFER);
                            } else {
                                if (responsePublic != null
                                        && responsePublic.eventManagement != null) {
                                    BaseActivity baseActivity = (BaseActivity)getContext();
                                    baseActivity.displayErrorMessage(responsePublic.eventManagement
                                            .getErrorDescription());
                                }
                            }
                        }
                    });
                }
            });
        }
    }

}
