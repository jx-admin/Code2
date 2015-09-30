
package com.accenture.mbank;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

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
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.TransferState;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.payment.NewRecentPaymentItem;
import com.accenture.mbank.view.payment.RecentPaymentGalleryAdapter;
import com.accenture.mbank.view.payment.Switch3DGallery;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.custom.view.CoverFlow;


public class RecentPaymentActivity extends BaseActivity implements OnItemClickListener,
        OnClickListener {

    CoverFlow recentPaymentGallery;

    RecentPaymentGalleryAdapter recentPaymentGalleryAdapter;

    ListView recent_payment_ListView;

    /**
     * 存放过滤出来的020 bankServiceCode的account
     */
    private List<AccountsModel> accountsList;

    private RecentPaymentList recentpaymentList;

    private List<RecentPaymentList> allList = new ArrayList<RecentPaymentList>();

    public static final int GET_RECENT_TRANSFER = 0;

    private int galleryIndex;

    Handler handler;

    private ResponsePublicModel responsePublic;

    private LinearLayout menu;

    private ImageButton help;

    private SlidingDrawer slidMenu;

    private View userInfoBtn;

    private View menu_accountsBtn;

    private View menu_investmentsBtn;

    private View menu_cardsBtn;

    private View menu_loansBtn;

    private View menu_paymentBtn;

    private View menu_contactsBtn;

    private View menu_guideBtn;

    private View menu_log_outBtn;

    private Button title_left_btn;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyTracker.getInstance().activityStart(this); // Add this method.
        handler = new Handler();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_recent_payments);
        findViewById(R.id.back).setVisibility(View.GONE);
        title_left_btn = (Button)findViewById(R.id.back_btn);
        title_left_btn.setOnClickListener(this);
        title_left_btn.setText(R.string.payments);
        title_left_btn.setVisibility(View.VISIBLE);

        init();
    }

    private void init() {
        menu = (LinearLayout)findViewById(R.id.menu);

        help = (ImageButton)findViewById(R.id.help_btn);
        help.setOnClickListener(this);
        accountsList = new ArrayList<AccountsModel>();
        recentPaymentGallery = (CoverFlow)findViewById(R.id.recent_payment_account_folders);
        recentPaymentGalleryAdapter = new RecentPaymentGalleryAdapter(this);
        filterAccount();
        recentPaymentGalleryAdapter.setViewId(R.layout.account_data_opened_content);
        recentPaymentGallery.setAdapter(recentPaymentGalleryAdapter);
        recentPaymentGallery.setAnimationDuration(500);
        recentPaymentGallery.setOnItemClickListener(this);

        recent_payment_ListView = (ListView)findViewById(R.id.recent_payment_item_listview);
        recentPaymentAdapter = new RecentPaymentAdapter(this);
        recent_payment_ListView.setAdapter(recentPaymentAdapter);

        initMenu();
    }

    private void filterAccount() {
        // 容错处理
        // if (Contants.accountsList != null) {
        // recentPaymentGalleryAdapter.setAccountList(accountsList);
        // return;
        // }
        // for (int i = 0; i < Contants.accountsList.size(); i++) {
        // BankServiceCode为020的account
        accountsList = Contants.bankTransferAccounts.get(0).getAccounts();
        // if
        // (Contants.accountsList.get(i).getBankServiceType().getBankServiceCode().equals(ServiceCode.CURRENT_ACCOUNT))
        // {
        // AccountsModel account = new AccountsModel();
        // account = Contants.accountsList.get(i);
        // accountsList.add(account);
        recentPaymentGalleryAdapter.setAccountList(accountsList);
        // }
        // }
    }

    RecentPaymentAdapter recentPaymentAdapter;

    public void addRecentPayemntItem(int index) {
        List<RecentTransferModel> recentTransferList = null;
        AccountsModel accountModel = null;
        for (int i = 0; i < allList.size(); i++) {
            if (accountsList.get(/* galleryIndex */index).getAccountCode()
                    .equals(allList.get(i).getAccountCode())) {
                recentTransferList = allList.get(i).getRecentTransferList();
            }
        }
        accountModel = accountsList.get(index);
        recentPaymentAdapter.setAccountsModel(accountModel);
        recentPaymentAdapter.seList(recentTransferList);
        recentPaymentAdapter.notifyDataSetChanged();
    }

    private void loadRecentPaymentTransfer(int index) {
        final String accountCode = accountsList.get(galleryIndex).getAccountCode();

        ProgressOverlay progressOverlay = new ProgressOverlay(this);
        progressOverlay.show("loading", new OnProgressEvent() {
            @Override
            public void onProgress() {
                String postData = RecentTransferJson.RecentTransferReportProtocal(
                        Contants.publicModel, accountCode, 20);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        RecentPaymentActivity.this);
                final RecentTransferResponseModel recentTransfer = RecentTransferJson
                        .ParseRecentTransferResponse(httpResult);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (recentTransfer != null
                                && recentTransfer.responsePublicModel.isSuccess()) {
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
                            if (responsePublic != null && responsePublic.eventManagement != null) {
                                // TODO
                            }
                        }
                    }
                });
            }
        });
    }

    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_RECENT_TRANSFER:
                    addRecentPayemntItem(galleryIndex);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> viewId, View view, int index, long arg3) {
    	mGaInstance = GoogleAnalytics.getInstance(this);
        mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
        mGaTracker1.sendView("view.recent.payment"); // Where myTracker is an instance of Tracker.
        galleryIndex = index;
        loadRecentPaymentTransfer(galleryIndex);
    }

    public static class RecentPaymentAdapter extends BaseAdapter {
        private AccountsModel accountModel;

        public AccountsModel getAccountsModel() {
            return accountModel;
        }

        public void setAccountsModel(AccountsModel accountModel) {
            this.accountModel = accountModel;
        }

        private List<RecentTransferModel> recentTransferList = new ArrayList<RecentTransferModel>();

        /**
         * @param recentTransferList 要设置的 recentTransferList
         */
        public void seList(List<RecentTransferModel> recentTransferList) {
            this.recentTransferList = recentTransferList;
        }

        private LayoutInflater mInflater;

        TextView timeText, typeText, resultText, one_text, four_text, two_text, three_text,
                five_text;

        Context context;

        public RecentPaymentAdapter(Context context) {
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return recentTransferList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewRecentPaymentItem child = null;
            if (convertView != null) {
                child = (NewRecentPaymentItem)convertView;
            } else {
                child = (NewRecentPaymentItem)mInflater.inflate(R.layout.new_recent_payment_item,
                        null);
                child.init();
            }
           /* "SIM TOP UP" and "PREPAID CARD RELOAD"
            It's requested to display, to front end, the values "PHONE TOP UP" and "CARD TOP UP"
*/            String type=recentTransferList.get(position).getType();
            if("SIM TOP UP".equals(type)){
                type="phone top up";
            }else if("PREPAID CARD RELOAD".equals(type)){
                type="card to up";
            }else{
                type=type.toLowerCase();
            }
            child.typeText.setText(type);
            
            String date = recentTransferList.get(position).getOperationDate();
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
            String amount = recentTransferList.get(position).getAmount() + "";
            amount = Utils.notPlusGenerateFormatMoney(
                    this.context.getResources().getString(R.string.dollar), amount);
            child.resultText.setText(amount);
            if (recentTransferList.get(position).getType().equals(Contants.SIM_TOP_UP)) {
                // sim top up UI展示

                child.one_text.setText(recentTransferList.get(position).getBeneficiaryNumber());
                String operator = DestProvider.getDsstProvider(recentTransferList.get(position)
                        .getBeneficiaryProvider());
                child.two_text.setText(operator);
                String state = TransferState.getTransferState(recentTransferList.get(position)
                        .getTransferState());
                child.three_text.setText(state);
                child.three_text.setVisibility(View.VISIBLE);
                child.four_text.setVisibility(View.GONE);
                child.five_text.setVisibility(View.GONE);
                child.setRecentTransferModel(recentTransferList.get(position));
                child.setAccountModel(getAccountsModel());
            } else if (recentTransferList.get(position).getType().equals(Contants.BANK_TRANSFER)
                    || recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
                // bank transfer & transfer entry UI展示
                if (recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
                    child.one_text.setVisibility(View.GONE);
                } else {

                    child.one_text.setText(recentTransferList.get(position).getBeneficiaryName());
                    child.one_text.setVisibility(View.VISIBLE);
                    child.three_text.setVisibility(View.VISIBLE);
                    child.four_text.setVisibility(View.VISIBLE);
                }

                child.two_text.setText(recentTransferList.get(position).getBeneficiaryIban());
                child.three_text.setText(recentTransferList.get(position).getDescription());
                String state = TransferState.getTransferState(recentTransferList.get(position)
                        .getTransferState());
                child.four_text.setText(state);
                child.five_text.setVisibility(View.GONE); 
                child.setRecentTransferModel(recentTransferList.get(position));
                child.setAccountModel(getAccountsModel());
            } else if (recentTransferList.get(position).getType()
                    .equals(Contants.PREPAID_CARD_RELOAD)) {
                // prepaid card UI展示

                child.one_text.setText(recentTransferList.get(position).getBeneficiaryName());
                child.two_text.setText(recentTransferList.get(position).getBeneficiaryCardNumber());
                child.three_text.setVisibility(View.GONE);
                child.four_text.setVisibility(View.GONE);
                child.five_text.setVisibility(View.GONE);
                child.setRecentTransferModel(recentTransferList.get(position));
                child.setAccountModel(getAccountsModel());
            }
            return child;
        }
    }

    private void initMenu() {

        userInfoBtn = findViewById(R.id.menu_user_info);
        userInfoBtn.setOnClickListener(this);
        menu_accountsBtn = findViewById(R.id.menu_accounts);
        menu_accountsBtn.setOnClickListener(this);
        menu_investmentsBtn = findViewById(R.id.menu_investments);
        menu_investmentsBtn.setOnClickListener(this);
        menu_cardsBtn = findViewById(R.id.menu_cards);
        menu_cardsBtn.setOnClickListener(this);
        menu_loansBtn = findViewById(R.id.menu_loans);
        menu_loansBtn.setOnClickListener(this);
        menu_paymentBtn = findViewById(R.id.menu_payment);
        menu_paymentBtn.setOnClickListener(this);
        menu_contactsBtn = findViewById(R.id.menu_contacts);
        menu_contactsBtn.setOnClickListener(this);
        menu_guideBtn = findViewById(R.id.menu_guide);
        menu_guideBtn.setOnClickListener(this);
        menu_log_outBtn = findViewById(R.id.menu_log_out);
        menu_log_outBtn.setOnClickListener(this);

        slidMenu = (SlidingDrawer)findViewById(R.id.slid_menu);
    }

    @Override
    public void onClick(View v) {
        MainActivity mainAcitivty = (MainActivity)MainActivity.getContext();
        if (v == title_left_btn) {
            this.finish();
            return;
        } else if (v == help) {
            Intent intent = new Intent(RecentPaymentActivity.this, HelpListActivity.class);
            startActivity(intent);
            return;
        } else if (v == userInfoBtn) {
            mainAcitivty.showUserInfo();
            slidMenu.animateClose();
        } else if (v == menu_accountsBtn) {
            mainAcitivty.showTab(MainActivity.ACCOUNTS);
            slidMenu.animateClose();
        } else if (v == menu_cardsBtn) {
            mainAcitivty.showTab(MainActivity.CARDS);
            slidMenu.animateClose();
        } else if (v == menu_contactsBtn) {
            Intent intent = new Intent(RecentPaymentActivity.this, ContactSearchActivity.class);
            startActivity(intent);
            slidMenu.animateClose();
        } else if (v == menu_guideBtn) {
            help.performClick();
        } else if (v == menu_investmentsBtn) {
            mainAcitivty.showInvestments();
            slidMenu.animateClose();
        } else if (v == menu_loansBtn) {
            mainAcitivty.showLoans();
            slidMenu.animateClose();
        } else if (v == menu_log_outBtn) {
            DialogManager.createMessageExitDialog("Are you sure you want to exit the application?",
                    this).show();
        } else if (v == menu_paymentBtn) {
            mainAcitivty.showTab(MainActivity.PAYMENTS);
            slidMenu.animateClose();
        }
        finish();
    }
}
