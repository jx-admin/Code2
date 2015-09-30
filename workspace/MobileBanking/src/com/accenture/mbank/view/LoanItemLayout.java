
package com.accenture.mbank.view;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetFinancingInfoModel;
import com.accenture.mbank.model.InstallmentsModel;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class LoanItemLayout extends BankLinearLayout implements View.OnClickListener,
        OnCheckedChangeListener, ShowAble {

    private AccountsModel accountsModel;

    Handler handler;

    ImageButton showBtn, closeBtn;

    ImageView line;

    ViewGroup content;

    LinearLayout recorddetailsLayout, installmentsLayout;

    RadioButton details, installments;

    InnerListView loan_item_list_view;

    LinearLayout loan_item_states;

    TextView loanName, date, loan_value;
    
    private GoogleAnalytics mGaInstance;
    
   	private Tracker mGaTracker1;

    /**
     * 上半圆长宽比
     */
    float close_widht_height = (float)(389f / 246f);

    /**
     * 下半圆长宽比
     */
    float show_widht_height = (float)(381f / 136f);

    double cycle_top_height = 80 / 360d;

    /**
     * 上半圆与下半圆的比例
     */
    public static final double cycle_bottom_height = 109d / 187;

    TextView loan_total, loan_account_value, loan_type_value, loan_remanin_value,
            loan_rate_over_value, loan_benchmark_applied_value;

    public LoanItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    void init() {
        showBtn = (ImageButton)findViewById(R.id.loan_show_img);
        closeBtn = (ImageButton)findViewById(R.id.loan_close_img);
        content = (ViewGroup)findViewById(R.id.loan_item_content);
        int height_top = (int)(cycle_top_height * BaseActivity.screen_height);
        closeBtn.getLayoutParams().height = height_top;
        int width = (int)(height_top * close_widht_height);
        closeBtn.getLayoutParams().width = width;

        int height_bottom = (int)(height_top * cycle_bottom_height);

        showBtn.getLayoutParams().height = height_bottom;
        showBtn.getLayoutParams().width = (int)(height_bottom * show_widht_height);

        line = (ImageView)findViewById(R.id.line);
        line.getLayoutParams().height = height_top;

        ViewGroup loanData = (ViewGroup)findViewById(R.id.loan_data);
        loanData.getLayoutParams().height = height_top;
        ViewGroup loanSectionLayout = (ViewGroup)findViewById(R.id.loan_section_layout);
        loanSectionLayout.getLayoutParams().height = height_top;
        loanSectionLayout.getLayoutParams().width = width;

        recorddetailsLayout = (LinearLayout)findViewById(R.id.loan_detail_record);
        installmentsLayout = (LinearLayout)findViewById(R.id.loan_install_record);
        showBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);

        loanName = (TextView)findViewById(R.id.loan_name);
        date = (TextView)findViewById(R.id.loan_date);

        details = (RadioButton)findViewById(R.id.details);
        installments = (RadioButton)findViewById(R.id.installments);
        loan_value = (TextView)findViewById(R.id.loan_value);
        installments.setOnCheckedChangeListener(this);
        details.setOnCheckedChangeListener(this);
        loan_item_list_view = (InnerListView)findViewById(R.id.loan_item_list_view);

        loan_item_list_view.parentScrollView = this.innerScrollView;

        int maxHeight = (int)(BaseActivity.innerscroll_view_max_height * BaseActivity.screen_height);
        loan_item_list_view.setMaxHeight(maxHeight);

        loan_total = (TextView)findViewById(R.id.loan_total);
        loan_account_value = (TextView)findViewById(R.id.loan_account_value);
        loan_type_value = (TextView)findViewById(R.id.loan_type_value);
        loan_remanin_value = (TextView)findViewById(R.id.loan_remanin_value);
        loan_rate_over_value = (TextView)findViewById(R.id.loan_rate_over_value);
        loan_benchmark_applied_value = (TextView)findViewById(R.id.loan_benchmark_applied_value);

    }

    public AccountsModel getAccount() {
        return accountsModel;
    }

    public void setAccountsModel(AccountsModel accountsModel) {
        this.accountsModel = accountsModel;
        setTexts();

    }

    private void setTexts() {

        if (loanName == null) {
            init();
        }
        String name = accountsModel.getAccountAlias();
        loanName.setText(name);
        date.setText(TimeUtil.getDateString(System.currentTimeMillis(), TimeUtil.dateFormat5));
    }

    @Override
    protected void showContent(ImageView show, View scrollToTargetView, View content) {
        // TODO Auto-generated method stub
        super.showContent(show, scrollToTargetView, content);

    }

    public void updateUIByData(final GetFinancingInfoModel getFinancingInfo) {

        handler.post(new Runnable() {

            @Override
            public void run() {
                List<InstallmentsModel> installmentsModels = getFinancingInfo.getInstallments();
                createUIByInstallments(installmentsModels);

                try {

                    String str = Utils.generateFormatMoney(
                            getContext().getResources().getString(R.string.dollar),
                            getFinancingInfo.getResidueAmount());
                    loan_value.setText(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String money = Utils.generateFormatMoney("$", getFinancingInfo.getTotalAmountl());

                loan_total.setText("Total:" + " " + money);

                loan_account_value.setText(getFinancingInfo.getIbanCode());
                String rateType = null;
                if (getFinancingInfo.getRateType().equals("FIXED")) {
                    rateType = getContext().getResources().getString(R.string.not_able);
                } else if (getFinancingInfo.getRateType().equals("VARIABLE")) {
                    String value = getFinancingInfo.getBenchmarksValue().replace('.', ',');
                    rateType = "+" + value + "%" + " " + getFinancingInfo.getBenchmarksDesc();
                }
                
                loan_type_value.setText(getFinancingInfo.getRateType());
                loan_remanin_value.setText(getFinancingInfo.getNumPaymentsRemaning());
                
                String value = getFinancingInfo.getRate().replace('.', ',');
                String rateValue = "+" + value + "%";
                loan_rate_over_value.setText(rateValue);
                loan_benchmark_applied_value.setText(rateType);
                LogManager.d("getFinancingInfo:" + getFinancingInfo.toString());

            }
        });

    }

    @Override
    public void onClick(View v) {

        if (showBtn == v) {
            showContent(showBtn, this, content);
            show();
        } else if (closeBtn == v) {
            closeContent(showBtn, this, content);
        }
    }

    public void setStates() {

    }

    List<InstallmentsModel> installmentsModels;

    private void createUIByInstallments(List<InstallmentsModel> list) {

        LogManager.d("list" + list.size());
        Collections.reverse(list);

        InstallAdapter installAdapter = new InstallAdapter(list, getContext());
        loan_item_list_view.setAdapter(installAdapter);
        installmentsModels = list;
    }

    private void addState() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ExpirationAmountStateItem expirationAmountStateItem = (ExpirationAmountStateItem)inflater
                .inflate(

                R.layout.expiration_amount_state, null);
        expirationAmountStateItem.setUI("08.15.21", "$59070", true);

        loan_item_states.addView(expirationAmountStateItem);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int type = 0;
        if (isChecked) {
        	mGaInstance = GoogleAnalytics.getInstance(getContext());
            mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
            mGaTracker1.sendView("event.loans.detail");
            if (buttonView == details) {                             
                type = 0;
            } else if (buttonView == installments) {                          
                type = 1;
            }
            showType(type);
        }

    }

    private void showType(int type) {

        switch (type) {
            case 0:

                recorddetailsLayout.setVisibility(View.VISIBLE);
                installmentsLayout.setVisibility(View.GONE);
                break;

            case 1:

                recorddetailsLayout.setVisibility(View.GONE);
                installmentsLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    public static class InstallAdapter extends BaseAdapter {

        List<InstallmentsModel> list;

        Context context;

        public InstallAdapter(List<InstallmentsModel> list, Context context) {
            this.list = list;
            this.context = context;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                ExpirationAmountStateItem expirationAmountStateItem = (ExpirationAmountStateItem)inflater
                        .inflate(

                        R.layout.expiration_amount_state, null);

                convertView = expirationAmountStateItem;

            }
            ExpirationAmountStateItem expirationAmountStateItem = (ExpirationAmountStateItem)convertView;
            InstallmentsModel installmentsModel = list.get(position);

            // String amouont = String.format("%.2f",
            // installmentsModel.getAmount());
            // String amouont = String.valueOf(installmentsModel.getAmount());
            String amount = Utils.generateFormatMoney(
                    context.getResources().getString(R.string.dollar),
                    installmentsModel.getAmount());

            boolean paided = false;
            LogManager.d("paid" + installmentsModel.getPaidState());
            if (installmentsModel.getPaidState().equals("S")) {
                paided = true;
            }
            String time = TimeUtil.changeFormattrString(installmentsModel.getDeadlineDate(),
                    TimeUtil.dateFormat2, TimeUtil.dateFormat5);
            expirationAmountStateItem.setUI(time, amount, paided);
            return convertView;
        }
    }

    public void showDelay() {

        showContent(showBtn, this, content);
    }

    @Override
    public void show() {
        listener.onShow(this);
    }

    @Override
    public void close() {

        closeContent(showBtn, this, content);
    }

    ShowListener listener;

    @Override
    public void setShowListener(ShowListener listener) {

        this.listener = listener;
    }

}
