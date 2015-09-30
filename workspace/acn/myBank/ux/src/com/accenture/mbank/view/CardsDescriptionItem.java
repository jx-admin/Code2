
package com.accenture.mbank.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.R;
import com.accenture.mbank.logic.GetMovementsJson;
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
import com.accenture.mbank.view.AccountItemLayout.RecordAdapter;
import com.accenture.mbank.view.protocol.ExpandAble;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class CardsDescriptionItem extends LinearLayout implements OnClickListener,
        OnCheckedChangeListener, ExpandAble {

    ToggleButton toggleButton;

    LinearLayout expandedContainer;

    public InnerListView recordAllLayout;

    ViewGroup expanderBar;

    private MovementsModel model;

    RadioButton transactionsRadioBtn, cardsDetailsRadioBtn;

    LinearLayout transactions_details_show, cards_details_display;

    // InnerScrollView transactions_details_scroll;

    TextView personalizedName, availableBalanceValue;

    Handler mHandler;

    BalanceAccountsModel account;

    public BalanceAccountsModel getAccount() {
        return account;
    }

    String paymentMethod;

    // Cards Details
    TextView alias;

    TextView state;

    TextView number_card;

    TextView expiration;

    TextView plafond;

    TextView plafond_name;

    private List<MovementsModel> movements;

    double listViewMaxHeight = 300d / 700d;

    RelativeLayout plafondItem;

    BankImageButton other_movementsBtn;

    String restartingKey;
    
    private GoogleAnalytics mGaInstance;
    
   	private Tracker mGaTracker1;

    public CardsDescriptionItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        if (toggleButton == null) {
            toggleButton = (ToggleButton)findViewById(R.id.cards_expand_btn);
            toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        onExpand();
                    } else {
                        expandedContainer.setVisibility(View.GONE);
                    }
                }
            });

            // Cards展开的内容
            expandedContainer = (LinearLayout)findViewById(R.id.cards_expanded_container);
            expanderBar = (ViewGroup)findViewById(R.id.cards_expander_bar);
            expanderBar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    toggleButton.performClick();
                }
            });

            plafondItem = (RelativeLayout)findViewById(R.id.plafond_item);

            // transactions 和 card details 的ladyout
            transactions_details_show = (LinearLayout)findViewById(R.id.transactions_details_show);
            cards_details_display = (LinearLayout)findViewById(R.id.cards_details_display);
            // transactions_details_scroll =
            // (InnerScrollView)findViewById(R.id.transactions_details_scroll);

            // 球下面的条目
            personalizedName = (TextView)findViewById(R.id.personalizedName);
            availableBalanceValue = (TextView)findViewById(R.id.availableBalanceValue);

            transactionsRadioBtn = (RadioButton)findViewById(R.id.transactions);
            transactionsRadioBtn.setOnCheckedChangeListener(this);
            cardsDetailsRadioBtn = (RadioButton)findViewById(R.id.cardsDetails);
            cardsDetailsRadioBtn.setOnCheckedChangeListener(this);
            transactionsRadioBtn.setChecked(true);
            recordAllLayout = (InnerListView)findViewById(R.id.cards_record);
            recordAllLayout.setMaxHeight((int)(listViewMaxHeight * BaseActivity.screen_height));

            other_movementsBtn = (BankImageButton)findViewById(R.id.btn_carica_movimenti);
            other_movementsBtn.setOnClickListener(this);
            requestLayout();
            alias = (TextView)findViewById(R.id.alias_value);
            state = (TextView)findViewById(R.id.state_value);
            number_card = (TextView)findViewById(R.id.number_card_value);
            expiration = (TextView)findViewById(R.id.expiration_value);
            plafond = (TextView)findViewById(R.id.plafond_value);
            plafond_name = (TextView)findViewById(R.id.plafond_name);
            if (BaseActivity.isOffline) {
                addRecord();
            }
        }
    }

    boolean needOpen;

    BankRollView showView;

    public void expandAndOpen(BankRollView bankRollView) {
        showView = bankRollView;
        needOpen = true;
        if (!toggleButton.isChecked()) {
            toggleButton.performClick();
        }

    }

    public void expand() {
        if (!toggleButton.isChecked()) {
            toggleButton.performClick();
        }
    }

    private void onExpand() {
        loadingMovements(getRestartingKey());
        closeOther();
        // System.out.println("allRecoard" +
        // recordAllLayout.getHeight());
        expandedContainer.setVisibility(View.VISIBLE);
        if (transactionsRadioBtn.isChecked()) {
        }
    }

    /**
     */
    private void closeOther() {
        ViewGroup parent = (ViewGroup)getParent();
        if (parent == null) {
            return;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof CardsDescriptionItem) {
                CardsDescriptionItem itemExpander = (CardsDescriptionItem)child;
                if (itemExpander != this && itemExpander.toggleButton.isChecked()) {
                    itemExpander.toggleButton.performClick();
                }
            }
        }
    }

    public void setAccount(BalanceAccountsModel account, String paymentMethod) {
        this.account = account;
        this.paymentMethod = paymentMethod;
        if (expanderBar == null || expandedContainer == null) {
            init();
        }
        personalizedName.setText(this.account.getPersonalizedName());
        String amount = Utils.generateFormatMoney(
                getContext().getResources().getString(R.string.dollar),
                this.account.getAvailableBalance() + "");

        if (this.paymentMethod.equals("2")) {
            plafondItem.setVisibility(View.INVISIBLE);
        }

        availableBalanceValue.setText(amount);
        updateCardDetails(this.account);
    }

    private void addRecord() {
        ArrayList<MovementsModel> list = new ArrayList<MovementsModel>();
        MovementsModel model = new MovementsModel();
        list.add(model);

        list.add(model.clone());
        list.add(model.clone());
        setRecord(list);
    }

    public void loadingMovements(final String restartingKey) {
        // if (this.movements == null) {
        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        progressOverlay.show("", new OnProgressEvent() {
            @Override
            public void onProgress() {
                MainActivity mainActivity = (MainActivity)getContext();
                final int transactionBy = mainActivity.setting.getShowTransactionBy();
                String postData = GetMovementsJson.getMovementsReportProtocal(Contants.publicModel,
                        paymentMethod, account.getAccountCode(),
                        mainActivity.setting.getOrderListFor(), transactionBy, restartingKey);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        getContext());
                final GetMovementsModel getMovements = GetMovementsJson
                        .parseGetMovementsResponse(httpResult);
                if (getMovements.getMovements() != null && getMovements.getMovements().size() > 0)
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            setRestartingKey(getMovements.responsePublicModel.getRestartingKey());
                            setRecord(getMovements.getMovements());
                        }
                    });
            }
        });
        // }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean arg1) {

        if (arg1) {
            if (buttonView == transactionsRadioBtn) {
                LogManager.d("aaaaa");             
                transactions_details_show.setVisibility(View.VISIBLE);
                cards_details_display.setVisibility(View.GONE);

            } else if (buttonView == cardsDetailsRadioBtn) {            	
                transactions_details_show.setVisibility(View.GONE);
                cards_details_display.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateCardDetails(BalanceAccountsModel account) {
        alias.setText(account.getHolder());
        state.setText(account.getCardState());

        for (int i = 0; i < Contants.cardAccounts.size(); i++) {
            if (Contants.cardAccounts.get(i).getAccountCode().equals(account.getAccountCode())) {
                number_card.setText(Contants.cardAccounts.get(i).getCardNumber());
            }
        }
        String date = TimeUtil.changeFormattrString(account.getExpirationDate(),
                TimeUtil.dateFormat2, TimeUtil.dateFormat7);
        expiration.setText(date);

        for (int i = 0; i < Contants.prepaidCardAccounts.size(); i++) {
            if (!Contants.prepaidCardAccounts.get(i).getAccountCode()
                    .equals(account.getAccountCode())) {
                String plafondValue = Utils.notDecimalGenerateMoney(getContext().getResources()
                        .getString(R.string.dollar), account.getPlafond());
                plafond.setText(plafondValue);
            } else {
                plafond_name.setVisibility(View.GONE);
                plafond.setVisibility(View.GONE);
            }
        }
    }

    RecordAdapter withrecordAdapter;

    private void setRecord(List<MovementsModel> movements) {
        this.movements = movements;
        if (withrecordAdapter == null) {
            withrecordAdapter = new RecordAdapter(movements, getContext(),
                    AccountItemLayout.RecordAdapter.TYPE.CARDS);
            recordAllLayout.setAdapter(withrecordAdapter);
        } else {
            withrecordAdapter.list.addAll(this.movements);
            withrecordAdapter.notifyDataSetChanged();
        }

        // c110106veh13007874
        if (needOpen) {

            needOpen = false;
            showView.show();
        }

    }

    @Override
    public void collapse() {
        if (toggleButton.isChecked()) {
            toggleButton.performClick();
        }
    }

    @Override
    public Object getExpandData() {
        // TODO Auto-generated method stub
        return getAccount();
    }

    @Override
    public void onClick(View view) {
        if (view == other_movementsBtn) {
            MainActivity mainActivity = (MainActivity)getContext();
            final int transactionBy = mainActivity.setting.getShowTransactionBy();

            if (transactionBy == SettingModel.LAST_2_MONTH) {
                if (withrecordAdapter != null) {
                    withrecordAdapter.list.clear();
                }
            }
            loadingMovements(getRestartingKey());
        }
    }

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
}
