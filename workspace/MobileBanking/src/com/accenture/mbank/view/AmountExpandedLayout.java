
package com.accenture.mbank.view;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.CompanyAmountJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AmountAvailable;
import com.accenture.mbank.model.CompanyAmountResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceCode;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.PayeeExpandedLayout.SimTopUpValue;

public class AmountExpandedLayout extends ExpandedContainer implements OnFocusChangeListener,
        OnClickListener, OnItemClickListener {

    EditText amountEdit;

    TextView getAmountText;

    Handler handler;

    String amount;

    public AmountExpandedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    int getCount(String src, char target) {

        int count = 0;
        if (src == null || src.equals("")) {
            return 0;
        }
        for (int i = 0; i < src.length(); i++) {
            char sub = src.charAt(i);
            if (sub == target) {
                count++;
            }

        }
        return count;

    }

    String beforText;

    int start;

    boolean hasFloat;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (amountEdit == null) {
            amountEdit = (EditText)findViewById(R.id.amount_edit);
            amountEdit.setOnFocusChangeListener(this);

            amountEdit.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String text = amountEdit.getText().toString();

                    if (getCount(text, ',') > 1) {
                        hasFloat = true;
                        amountEdit.setText(beforText);
                        Spannable spannable = amountEdit.getText();
                        Selection.setSelection(spannable, AmountExpandedLayout.this.start);
                        hasFloat = false;
                        return;

                    }
                    amount = text.replace(",", ".");
                    if (text != null && !text.equals("")) {
                        String number = amount;
                        number = Utils.notPlusGenerateFormatMoneyItanLocal(getContext()
                                .getResources().getString(R.string.dollar), number);
                        expandResultChange(number);

                    } else {
                        expandResultChange(amountEdit.getText().toString());
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    System.out.println("start" + start + "after" + after + "text" + s.toString());
                    if (hasFloat) {

                    } else {
                        beforText = s.toString();
                        AmountExpandedLayout.this.start = start;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                    LogManager.d("textChange:after" + s.toString());
                }
            });

            getAmountText = (TextView)findViewById(R.id.get_amount_text);
            getAmountText.setOnClickListener(this);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (v == amountEdit && !hasFocus) {
            String text = amountEdit.getText().toString();
            amount = text.replace(",", ".");
            if (text != null && !text.equals("")) {
                String number = amount;
                number = Utils.notPlusGenerateFormatMoneyItanLocal(getContext().getResources()
                        .getString(R.string.dollar), number);
                expandFocusResultChange(number);
            } else {
                expandFocusResultChange(amountEdit.getText().toString());

            }
        }
    }

    List<AmountAvailable> amounts;

    AmountAvailable amountAvailable;

    AlertDialog alertDialog;

    private String currentTypeCode;

    public void showType(String typeCode) {
        resetResult();
        if (typeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            showInputType();
        } else if (typeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            showInputType();
        } else if (typeCode.equals(ServiceCode.SIM_TOP_UP)) {
            showSlectedType();
        } else if (typeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            showInputType();
        }
        currentTypeCode = typeCode;
    }

    private void hideUI() {
        amountEdit.setVisibility(View.GONE);
        getAmountText.setVisibility(View.GONE);
    }

    private void showSlectedType() {
        hideUI();
        getAmountText.setVisibility(View.VISIBLE);
        getAmountText.setText("");
    }

    private void showInputType() {
        hideUI();

        amountEdit.setText("");
        amountEdit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        if (v == getAmountText) {

            if (BaseActivity.isOffline) {
                return;
            }
            ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
            progressOverlay.show("", new OnProgressEvent() {

                @Override
                public void onProgress() {
                    PaymentConfirmLayout paymentConfirmLayout = (PaymentConfirmLayout)expandBarResultListener
                            .getOwener();

                    PayeeExpandedLayout.SimTopUpValue value = (SimTopUpValue)paymentConfirmLayout.payeeExpander
                            .getValue();
                    AccountsModel accountsModel = paymentConfirmLayout.generateAccountModel();
                    if(accountsModel==null){
                        BaseActivity baseActivity = (BaseActivity)getContext();
                        baseActivity.displayErrorMessage("please select account");
                        return;
                    }
                    
                    String postData = CompanyAmountJson.CompanyAmountReportProtocal(
                            accountsModel.getAccountCode(), value.destProvider, value.phoneNumber,
                            Contants.publicModel);
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, getContext());
                    final CompanyAmountResponseModel companyAmount = CompanyAmountJson
                            .ParseCompanyAmountResponse(httpResult);
                    amounts = companyAmount.getAmountAvailable();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (companyAmount.responsePublicModel.isSuccess()) {
                                // CompanyAmount
                                if (amounts == null || amounts.size() <= 0) {
                                    DialogManager.createMessageDialog(
                                            companyAmount.responsePublicModel
                                                    .getResultDescription(), getContext()).show();
                                } else {
                                    alertDialog = DialogManager.createAmountDialog(
                                            AmountExpandedLayout.this, amounts);
                                    alertDialog.show();
                                }
                            } else {
                                BaseActivity baseActivity = (BaseActivity)getContext();
                                baseActivity
                                        .displayErrorMessage(companyAmount.responsePublicModel.eventManagement
                                                .getErrorDescription());
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        amountAvailable = amounts.get(position);
        String result = Utils.generateFormatMoney(
                getContext().getResources().getString(R.string.dollar),
                amountAvailable.getDescription());
        getAmountText.setText(result);
        expandResultChange(result);
        alertDialog.dismiss();

    }

    @Override
    protected void resetResult() {
        super.resetResult();
        amountEdit.setText("");
        getAmountText.setText("");

    }

    @Override
    protected void onRecover(String text) {
        if (text == null || text.equals("")) {
            return;
        }
        amount = text;
        amountEdit.setText(text);
        getAmountText.setText(text);
        amountAvailable = new AmountAvailable();
        amountAvailable.setCommissionAmount(0);

        amountAvailable.setDescription(text);
        int doublemoney = (int)Double.parseDouble(text);
        amountAvailable.setRechargeAmount(doublemoney);

    }

    @Override
    public Object getValue() {
        // TODO Auto-generated method stub
        if (init) {
            return null;
        }

        if (currentTypeCode.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            return amount;
        } else if (currentTypeCode.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            return amount;
        } else if (currentTypeCode.equals(ServiceCode.SIM_TOP_UP)) {
            return amountAvailable;
        } else if (currentTypeCode.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            return amount;
        }

        return null;
    }
}
