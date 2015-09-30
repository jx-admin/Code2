
package com.accenture.mbank.util;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.LoginActivity;
import com.accenture.mbank.MobileBankApplication;
import com.accenture.mbank.R;
import com.accenture.mbank.model.Account;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AmountAvailable;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.view.AmountExpandedLayout;
import com.accenture.mbank.view.PayeeExpandedLayout;
import com.accenture.mbank.view.PaymentConfirmLayout;
import com.accenture.mbank.view.PaymentConfirmLayout.ValidateData;

public class DialogManager {

    public static AlertDialog createRequirePinDialog(String codeType, ValidateData validateData,
            final PaymentConfirmLayout paymentConfirmLayout) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(paymentConfirmLayout.getContext());
        LayoutInflater inflater = LayoutInflater.from(paymentConfirmLayout.getContext());
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.require_pin_dialog,
                null);

        builder.setView(linearLahyout);
        alertDialog = builder.create();
        TextView typeText, accountText, dateText, amountText, reasonText, feesText, beneficiaryText, ibanText, bicText;
        typeText = (TextView)linearLahyout.findViewById(R.id.require_pin_type_text);
        accountText = (TextView)linearLahyout.findViewById(R.id.require_pin_account_text);
        dateText = (TextView)linearLahyout.findViewById(R.id.require_pin_date_text);
        beneficiaryText = (TextView)linearLahyout.findViewById(R.id.require_pin_beneficiary_text);
        ibanText = (TextView)linearLahyout.findViewById(R.id.require_pin_iban_text);
        bicText = (TextView)linearLahyout.findViewById(R.id.require_pin_bic_text);
        amountText = (TextView)linearLahyout.findViewById(R.id.require_pin_amount_text);
        reasonText = (TextView)linearLahyout.findViewById(R.id.require_pin_reason_text);
        feesText = (TextView)linearLahyout.findViewById(R.id.require_pin_fees_text);

        if (codeType.equals(ServiceCode.SIM_TOP_UP)) {
            typeText.setText(validateData.type);
            accountText.setText(validateData.account);
            beneficiaryText.setText(validateData.beneficiary);
            dateText.setVisibility(View.GONE);
            ibanText.setVisibility(View.GONE);
            bicText.setVisibility(View.GONE);
            reasonText.setVisibility(View.GONE);
            amountText.setText(validateData.amount);
            feesText.setText(validateData.fees);
        } else if (codeType.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            typeText.setText(validateData.type);
            accountText.setText(validateData.account);
            dateText.setText(validateData.date);
            beneficiaryText.setText(validateData.beneficiary);
            ibanText.setText(validateData.iban);
            bicText.setText(validateData.bic);
            reasonText.setText(validateData.reason);
            amountText.setText(validateData.amount);
            feesText.setText(validateData.fees);
        } else if (codeType.equals(ServiceCode.TRANSFER_ENTRY_PAYMENT)) {
            typeText.setText(validateData.type);
            accountText.setText(validateData.account);
            dateText.setText(validateData.date);
            beneficiaryText.setText(validateData.beneficiary);
            ibanText.setVisibility(View.GONE);
            bicText.setVisibility(View.GONE);
            reasonText.setText(validateData.reason);
            amountText.setText(validateData.amount);
            feesText.setText(validateData.fees);
        } else if (codeType.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            typeText.setText(validateData.type);
            accountText.setText(validateData.account);
            dateText.setVisibility(View.GONE);
            beneficiaryText.setText(validateData.beneficiary);
            ibanText.setVisibility(View.GONE);
            bicText.setVisibility(View.GONE);
            amountText.setText(validateData.amount);
            reasonText.setText(validateData.reason);
            feesText.setText(validateData.fees);
        }

        View cancel = (View)linearLahyout.findViewById(R.id.require_pin_cancel_btn);

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        View requirePin = (View)linearLahyout.findViewById(R.id.require_pin_ask_pin_btn);

        requirePin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                paymentConfirmLayout.askPin();
            }
        });

        setDialogWidth(alertDialog, paymentConfirmLayout.getContext(), linearLahyout);

        return alertDialog;

    }

    public static AlertDialog createOperatorDialog(PayeeExpandedLayout payeeExpandedLayout,
            List<String> list) {

        final AlertDialog alertDialog;
        Context context = payeeExpandedLayout.getContext();
        AlertDialog.Builder builder = new Builder(context);

        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_singlechoice);
        for (String str : list) {

            adapter.add(str);
        }
        listView.setAdapter(adapter);
        listView.setCacheColorHint(0x00000000);
        listView.setBackgroundDrawable(null);
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemClickListener(payeeExpandedLayout);

        // <ListView
        // android:id="@+id/help_list_view"
        // android:layout_width="fill_parent"
        // android:layout_height="fill_parent"
        // android:background="@null"
        // android:cacheColorHint="#00000000"
        // android:divider="@drawable/line"
        // android:dividerHeight="2dip"
        // android:listSelector="@drawable/list_item_over"
        // android:scrollbars="none" >
        // </ListView>
        builder.setView(listView);
        alertDialog = builder.create();

        setDialogWidth(alertDialog, context, listView);

        return alertDialog;

    }

    public static AlertDialog createMyCardDialog(final PayeeExpandedLayout payeeExpandedLayout,
            final List<AccountsModel> myCardAccounts) {

        final AlertDialog alertDialog;
        Context context = payeeExpandedLayout.getContext();
        AlertDialog.Builder builder = new Builder(context);

        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_singlechoice);
        for (AccountsModel str : myCardAccounts) {

            adapter.add(str.getAccountAlias());
        }
        listView.setAdapter(adapter);
        listView.setCacheColorHint(0x00000000);
        listView.setBackgroundDrawable(null);
        listView.setBackgroundColor(Color.WHITE);
        builder.setView(listView);
        alertDialog = builder.create();
        setDialogWidth(alertDialog, context, listView);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                payeeExpandedLayout.onMyCardSelected(myCardAccounts.get(arg2));
                alertDialog.dismiss();
            }
        });

        return alertDialog;

    }

    public static AlertDialog createAmountDialog(AmountExpandedLayout amountExpandedLayout,
            List<AmountAvailable> list) {

        final AlertDialog alertDialog;
        Context context = amountExpandedLayout.getContext();
        AlertDialog.Builder builder = new Builder(context);

        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_singlechoice);
        String dollor = amountExpandedLayout.getContext().getResources().getString(R.string.dollar);
        for (AmountAvailable str : list) {
            adapter.add(Utils.generateFormatMoney(dollor, str.getDescription()));
        }
        listView.setAdapter(adapter);
        listView.setCacheColorHint(0x00000000);
        listView.setBackgroundDrawable(null);
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemClickListener(amountExpandedLayout);

        builder.setView(listView);
        alertDialog = builder.create();

        // alertDialog.show();
        setDialogWidth(alertDialog, context, listView);

        return alertDialog;

    }

    public static AlertDialog createHoursDialog(Context context, OnItemClickListener listener,
            List<TableContentList> tableContentLists) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_singlechoice);
        for (TableContentList tableContent : tableContentLists) {
            adapter.add(tableContent.getDescription());
        }
        listView.setAdapter(adapter);
        listView.setCacheColorHint(0x00000000);
        listView.setBackgroundDrawable(null);
        listView.setBackgroundColor(Color.WHITE);
        listView.setOnItemClickListener(listener);
        builder.setView(listView);
        final AlertDialog alertDialog = builder.create();
        setDialogWidth(alertDialog, context, listView);
        return alertDialog;
    }

    public static AlertDialog createMessageDialog(final String message, final Context context) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.message_dialog_layout,
                null);

        builder.setView(linearLahyout);
        alertDialog = builder.create();

        Button imageButton = (Button)linearLahyout.findViewById(R.id.ok_btn);
        TextView text = (TextView)linearLahyout.findViewById(R.id.message_text);
        text.setText(message);
        if (message.equals("Not valid token")) {
            text.setText("Session expired!");
        }
        if (message.equals("Not valid token") || message.equals("Missing sessionId")) {
            alertDialog.setCancelable(false);
        }
        imageButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                System.out.println("onclick+===========================");
                alertDialog.dismiss();
                if (message.equals("Not valid token") || message.equals("Missing sessionId")) {
                    if (context instanceof BaseActivity) {
                        final BaseActivity baseActivity = (BaseActivity)context;
                        Intent intent = new Intent(baseActivity, LoginActivity.class);
                        baseActivity.startActivity(intent);
                        MobileBankApplication.logOut();

                        baseActivity.isFinished = true;

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                baseActivity.finish();
                            }
                        }, 3000);

                    }
                }
            }
        });
        // alertDialog.show();
        setDialogWidth(alertDialog, context, linearLahyout);

        return alertDialog;

    }

    public static AlertDialog createMessageDialog(final String message, final Context context,
            final PaymentConfirmLayout paymentCofirmLayout) {
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.message_dialog_layout,
                null);

        builder.setView(linearLahyout);
        alertDialog = builder.create();

        Button imageButton = (Button)linearLahyout.findViewById(R.id.ok_btn);
        TextView text = (TextView)linearLahyout.findViewById(R.id.message_text);
        text.setText(message);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentCofirmLayout.onButtonClick();
                alertDialog.dismiss();
            }
        });
        setDialogWidth(alertDialog, context, linearLahyout);

        return alertDialog;

    }

    public static AlertDialog createMessageDialog(final String message, final String buttonText,
            final Context context, final PaymentConfirmLayout paymentCofirmLayout) {
        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.message_dialog_layout,
                null);

        builder.setView(linearLahyout);
        alertDialog = builder.create();

        Button imageButton = (Button)linearLahyout.findViewById(R.id.ok_btn);
        imageButton.setText(buttonText);
        TextView text = (TextView)linearLahyout.findViewById(R.id.message_text);
        text.setText(message);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        setDialogWidth(alertDialog, context, linearLahyout);

        return alertDialog;

    }

    public static AlertDialog createMessageExitDialog(String message, final Context context) {

        final AlertDialog alertDialog;
        AlertDialog.Builder builder = new Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(
                R.layout.exit_message_dialog_layout, null);

        builder.setView(linearLahyout);
        alertDialog = builder.create();

        Button yesButton = (Button)linearLahyout.findViewById(R.id.yes_btn);
        Button noButton = (Button)linearLahyout.findViewById(R.id.no_btn);
        TextView text = (TextView)linearLahyout.findViewById(R.id.exit_message_text);
        text.setText(message);
        noButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Contants.logOut(); // lyandyjoe
                Contants.clearAll();
                Intent intent = new Intent(((Activity)context), LoginActivity.class);
                ((Activity)context).startActivity(intent);
                ((Activity)context).finish();
                // System.exit(0);
            }
        });
        // alertDialog.show();
        setDialogWidth(alertDialog, context, linearLahyout);

        return alertDialog;

    }

    private static void setDialogWidth(final AlertDialog alertDialog, Context context, View v) {
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        // lp.width = (int)(display.getWidth()) * 3 / 4; // 设置宽度
        // lp.height = (int)(display.getWidth()) * 2 / 4;
        int width = (int)(display.getWidth()) * 3 / 4;
        int height = (int)(display.getHeight());
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        v.measure(widthMeasureSpec, heightMeasureSpec);

        int pad = (width - v.getMeasuredWidth()) / 2;

        v.setPadding(pad, 0, pad, 0);
        alertDialog.getWindow().setAttributes(lp);
    }

    public static AlertDialog createPayeeDialog(final PayeeExpandedLayout payeeExpandedLayout,
            GetRecipientListModel getRecipientListModel, String serviceType) {

        final AlertDialog alertDialog;
        Context context = payeeExpandedLayout.getContext();
        AlertDialog.Builder builder = new Builder(context);

        ListView listView = new ListView(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.select_dialog_singlechoice);

        if (serviceType.equals(ServiceCode.BANK_TRANSFER_PAYMENT)) {
            List<Account> list1 = getRecipientListModel.getBankRecipientList();
            for (Account str : list1) {
                adapter.add(((BankRecipient)str).getName());
            }
        } else if (serviceType.equals(ServiceCode.SIM_TOP_UP)) {
            List<PhoneRecipient> list1 = getRecipientListModel.getPhoneRecipientList();
            for (PhoneRecipient str : list1) {
                adapter.add(str.getName());
            }
        } else if (serviceType.equals(ServiceCode.CARD_RECHARGE_PAYMENT)) {
            List<CardRecipient> list1 = getRecipientListModel.getCardRecipientList();
            for (CardRecipient str : list1) {
                adapter.add(str.getName());
            }
        }
        listView.setAdapter(adapter);
        listView.setCacheColorHint(0x00000000);
        listView.setBackgroundDrawable(null);
        listView.setBackgroundColor(Color.WHITE);

        builder.setView(listView);
        alertDialog = builder.create();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                payeeExpandedLayout.onPayeeSelected(arg2);
                alertDialog.dismiss();
            }
        });
        // alertDialog.show();
        setDialogWidth(alertDialog, context, listView);

        return alertDialog;

    }

    public static interface OnButtonListener {
        public void onButtonClick();
    }

    public static interface OnPayeeSelectListener {

        public void onPayeeSelected(int index);
    }
}
