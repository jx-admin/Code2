
package com.accenture.mbank.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.NewPayments;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.GetMovementsJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.GetMovementsModel;
import com.accenture.mbank.model.MovementsModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.payment.PaymentsManager;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class AccountItemLayout extends BankLinearLayout implements OnClickListener,
        OnCheckedChangeListener, ShowAble {

    Handler handler;

    ImageButton showBtn, closeBtn;

    ImageView line;

    ViewGroup content;

    LinearLayout recordAllLayout, recordDepositLayout, recordWithLayout;

    RadioButton all, deposit, withdrawals;

    TextView balanceAccountValue, availableAccountValue, accountName, date;

    View payments;

    ShowListener listener;

    InnerListView allRecordListView, depositRecordListView, widthRecordListView;

    BankImageButton older_operationsBtn;

    String restartingKey;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    /**
     * @return the restartingKey
     */
    public String getRestartingKey() {
        return restartingKey;
    }

    /**
     * @param restartingKey the restartingKey to set
     */
    public void setRestartingKey(String restartingKey) {
        this.restartingKey = restartingKey;
    }

    public AccountItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();

    }

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
    public static final double cycle_bottom_height = 108.5d / 187;

    void init() {
        showBtn = (ImageButton)findViewById(R.id.account_show_img);
        closeBtn = (ImageButton)findViewById(R.id.account_close_img);
        content = (ViewGroup)findViewById(R.id.account_item_content);
        int height_top = (int)(cycle_top_height * BaseActivity.screen_height);
        closeBtn.getLayoutParams().height = height_top;
        int width = (int)(height_top * close_widht_height);
        closeBtn.getLayoutParams().width = width;

        int height_bottom = (int)(height_top * cycle_bottom_height);

        showBtn.getLayoutParams().height = height_bottom;
        showBtn.getLayoutParams().width = (int)(height_bottom * show_widht_height);

        line = (ImageView)findViewById(R.id.line);
        line.getLayoutParams().height = height_top;

        ViewGroup accountData = (ViewGroup)findViewById(R.id.account_data);
        accountData.getLayoutParams().height = height_top;
        ViewGroup accountSectionLayout = (ViewGroup)findViewById(R.id.account_section_layout);
        accountSectionLayout.getLayoutParams().height = height_top;
        accountSectionLayout.getLayoutParams().width = width;

        recordAllLayout = (LinearLayout)findViewById(R.id.account_all_record);
        recordDepositLayout = (LinearLayout)findViewById(R.id.account_deposit_record);
        recordWithLayout = (LinearLayout)findViewById(R.id.account_with_record);
        showBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);

        if (BaseActivity.isOffline) {
            addRecord();
        }
        balanceAccountValue = (TextView)findViewById(R.id.balance_account_value);
        availableAccountValue = (TextView)findViewById(R.id.available_account_value);
        accountName = (TextView)findViewById(R.id.account_name);
        date = (TextView)findViewById(R.id.account_date);

        all = (RadioButton)findViewById(R.id.all);
        deposit = (RadioButton)findViewById(R.id.deposit);
        withdrawals = (RadioButton)findViewById(R.id.withdraoals);
        withdrawals.setOnCheckedChangeListener(this);
        deposit.setOnCheckedChangeListener(this);
        all.setOnCheckedChangeListener(this);
        payments = findViewById(R.id.account_payments);
        payments.setOnClickListener(this);
        allRecordListView = (InnerListView)findViewById(R.id.account_all_record_list);
        depositRecordListView = (InnerListView)findViewById(R.id.account_deposit_record_list);
        widthRecordListView = (InnerListView)findViewById(R.id.account_with_record_list);
        older_operationsBtn = (BankImageButton)findViewById(R.id.btn_older_operations);
        older_operationsBtn.setOnClickListener(this);
        allRecordListView.parentScrollView = innerScrollView;
        depositRecordListView.parentScrollView = innerScrollView;
        widthRecordListView.parentScrollView = innerScrollView;

        int maxHeight = (int)(BaseActivity.innerscroll_view_max_height * BaseActivity.screen_height);

        allRecordListView.setMaxHeight(maxHeight);
        depositRecordListView.setMaxHeight(maxHeight);
        widthRecordListView.setMaxHeight(maxHeight);

    }

    public BalanceAccountsModel getAccount() {
        return account;
    }

    BalanceAccountsModel account;

    public void setAccount(BalanceAccountsModel account) {
        this.account = account;

        setUiBydata();
    }

    /**
     * 仅上半球的ui展示，下半球的归record管
     */
    private void setUiBydata() {
        if (this.account == null) {
            return;
        } else {

            if (balanceAccountValue == null) {
                init();
            }

            try {
                String money = this.account.getAccountBalance();

                money = Utils.generateFormatMoney(
                        getContext().getResources().getString(R.string.dollar), money);
                balanceAccountValue.setText(money);
                availableAccountValue.setText(money);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // availableAccountValue.setText(String.valueOf(this.account.getAvailableBalance()));
            accountName.setText(this.account.getPersonalizedName());
            String date = TimeUtil.getDateString(System.currentTimeMillis(), TimeUtil.dateFormat5);
            this.date.setText(date);
        }
    }

    public void addRecord() {

    }

    List<MovementsModel> allList;

    List<MovementsModel> depostList;

    List<MovementsModel> widthList;

    RecordAdapter allRecordAdapter;

    RecordAdapter depositrecordAdapter;

    RecordAdapter withrecordAdapter;

    public void setRecords(List<MovementsModel> list) {
        MainActivity mainActivity = (MainActivity)getContext();

        this.allList = list;
        LogManager.d("accounts size=" + list.size());

        LogManager.d("showDelay" + this + "accounts size=" + list.size());

        generateList();
        // createUiBylist(recordAllLayout, allList);
        // createUiBylist(recordDepositLayout, depostList);
        // createUiBylist(recordWithLayout, widthList);

        if (allRecordAdapter == null) {
            allRecordAdapter = new RecordAdapter(allList, getContext(), RecordAdapter.TYPE.ACCOUNTS);
            allRecordListView.setAdapter(allRecordAdapter);
        } else {
            allRecordAdapter.list.addAll(allList);
            allRecordAdapter.notifyDataSetChanged();
            
        }
        if (depositrecordAdapter == null) {
            depositrecordAdapter = new RecordAdapter(depostList, getContext(),
                    RecordAdapter.TYPE.ACCOUNTS);
            depositRecordListView.setAdapter(depositrecordAdapter);
           
        } else {
            depositrecordAdapter.list.addAll(depostList);
            depositrecordAdapter.notifyDataSetChanged();
           
        }
        if (withrecordAdapter == null) {
            withrecordAdapter = new RecordAdapter(widthList, getContext(),
                    RecordAdapter.TYPE.ACCOUNTS);
            widthRecordListView.setAdapter(withrecordAdapter);
            
        } else {
            withrecordAdapter.list.addAll(widthList);
            withrecordAdapter.notifyDataSetChanged();
           
        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                showContent(showBtn, AccountItemLayout.this, content);
            }
        }, 500);

    }

    private void generateList() {
        depostList = new ArrayList<MovementsModel>();
        widthList = new ArrayList<MovementsModel>();
        for (MovementsModel model : allList) {
            if (model.getAmount() >= 0) {
                depostList.add(model.clone());
            } else {
                widthList.add(model.clone());
            }
        }
    }

    private void hideRecords() {

        recordAllLayout.setVisibility(View.GONE);
        recordDepositLayout.setVisibility(View.GONE);
        recordWithLayout.setVisibility(View.GONE);

        allRecordListView.setVisibility(View.GONE);

        depositRecordListView.setVisibility(View.GONE);
        widthRecordListView.setVisibility(View.GONE);

    }

    // public void createUiBylist(LinearLayout layout, List<MovementsModel>
    // list) {
    //
    // layout.removeAllViews();
    // for (MovementsModel model : list) {
    // addRecord(layout, model);
    //
    // }
    // }

    // private void addRecord(LinearLayout layout, MovementsModel
    // movementsModel) {
    // LayoutInflater inflater = LayoutInflater.from(getContext());
    // DateDescriptionAmountItem child =
    // (DateDescriptionAmountItem)inflater.inflate(
    // R.layout.date_desc_amount_item, null);
    // child.setModel(movementsModel);
    // layout.addView(child);
    // }

    @Override
    protected void showContent(ImageView show, View scrollToTargetView, View content) {
        // TODO Auto-generated method stub
        super.showContent(show, scrollToTargetView, content);

        if (this.allList == null || this.allList.size() <= 0) {

        } else {
            return;
        }
        loadData(getRestartingKey());
    }

    private void loadData(final String restartingKey) {
        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                MainActivity mainActivity = (MainActivity)getContext();
                final int transactionBy = mainActivity.setting.getShowTransactionBy();
                String postData = GetMovementsJson.getMovementsReportProtocal(Contants.publicModel,
                        "2", account.getAccountCode(), mainActivity.setting.getOrderListFor(),
                        transactionBy, restartingKey);

                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        getContext());
                final GetMovementsModel getMovements = GetMovementsJson
                        .parseGetMovementsResponse(httpResult);
                if (getMovements.getMovements() != null && getMovements.getMovements().size() > 0)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setRestartingKey(getMovements.responsePublicModel.getRestartingKey());
                            if (transactionBy == SettingModel.LAST_2_MONTH) {

                                if (allRecordAdapter != null) {
                                    allRecordAdapter.list.clear();

                                }
                                if (depositrecordAdapter != null) {
                                    depositrecordAdapter.list.clear();
                                }
                                if (withrecordAdapter != null) {

                                    withrecordAdapter.list.clear();
                                }
                            }
                            setRecords(getMovements.getMovements());
                        }
                    });

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (showBtn == v) {
            showContent(showBtn, this, content);
            this.show();
        } else if (closeBtn == v) {
            closeContent(showBtn, this, content);
        } else if (payments == v) {
        	mGaInstance = GoogleAnalytics.getInstance(getContext());
            mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
        	mGaTracker1.sendView("event.accounts.payments");
            recover();
        } else if (older_operationsBtn == v) {
            loadData(getRestartingKey());
        }

    }

    private void recover() {
        if (BaseActivity.isNewVersion) {
            AccountsModel payer = new AccountsModel();
            payer.setAccountAlias(account.getPersonalizedName());
            payer.setAccountCode(account.getAccountCode());
            payer.setBankCode(account.getBankServiceCode());
            NewPayments.startForRecover((Activity)getContext(), PaymentsManager.FORM_ACCOUNT,
                    payer, null, System.currentTimeMillis(), 0d, 0);
        } else {
        MainActivity mainActivity = (MainActivity)getContext();
        mainActivity.showTab(3);
        mainActivity.tab4.show(0);
        mainActivity.paymentConfirmLayout.goToPayment(this.account);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int type = 0;
        if (isChecked) {
        	mGaInstance = GoogleAnalytics.getInstance(getContext());
            mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
            mGaTracker1.sendView("event.accounts.show.movement"); 
            if (buttonView == all) {            	
            } else if (buttonView == deposit) {           	 
                type = 1;
            } else if (buttonView == withdrawals) {
                type = 2;                
            }
            showRecordsType(type);
        }
    }

    /**
     * 0:all<br>
     * 1:deposit<br>
     * 2:withdrawals<br>
     * 
     * @param type
     */
    private void showRecordsType(int type) {
    	mGaInstance = GoogleAnalytics.getInstance(getContext());
        mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
        mGaTracker1.sendView("event.accounts.show.movement");
        if (type == 0) {        	
             show(allRecordListView);
            
        } else if (type == 1) {       	
            show(depositRecordListView);
           
        } else if (type == 2) {
            show(widthRecordListView);
           
        }
    }

    private void show(View v) {
        hideRecords();

        v.setVisibility(View.VISIBLE);
    }

    public static class RecordAdapter extends BaseAdapter {

        List<MovementsModel> list;

        Context context;

        public static enum TYPE {
            ACCOUNTS, CARDS
        };

        TYPE type;

        public RecordAdapter(List<MovementsModel> list, Context context, TYPE type) {
            this.list = list;
            this.context = context;
            this.type = type;

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
                DateDescriptionAmountItem item = (DateDescriptionAmountItem)inflater.inflate(
                        R.layout.date_desc_amount_item, null);
                item.init();
                if (TYPE.ACCOUNTS == type) {
                    item.toggleButton.setBackgroundResource(R.drawable.accounts_expand_selector);
                } else if (TYPE.CARDS == type) {
                    item.toggleButton.setBackgroundResource(R.drawable.cards_expand_selector);
                }
                convertView = item;
            }
            DateDescriptionAmountItem dateDescriptionAmountItem = (DateDescriptionAmountItem)convertView;
            dateDescriptionAmountItem.setModel(list.get(position));

            return convertView;
        }

    }

    public void showDelay(boolean isDelay) {

        if (isDelay) {
            showContent(showBtn, this, content);
        } else {
            showContent(showBtn, this, content);
            show();
        }
    }

    @Override
    public void show() {

        if (listener != null) {
            listener.onShow(this);
        }

    }

    @Override
    public void close() {
        closeContent(showBtn, this, content);
    }

    @Override
    public void setShowListener(ShowListener listener) {
        this.listener = listener;
    }

}
