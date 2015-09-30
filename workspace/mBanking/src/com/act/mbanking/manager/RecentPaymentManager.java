
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.act.mbanking.App;
import com.act.mbanking.Contants;
import com.act.mbanking.DestProvider;
import com.act.mbanking.R;
import com.act.mbanking.TransferState;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.GetPendingTransferModel;
import com.act.mbanking.bean.PendingTransferModel;
import com.act.mbanking.bean.RecentPaymentList;
import com.act.mbanking.bean.RecentTransferModel;
import com.act.mbanking.bean.RecentTransferResponseModel;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.logic.GetPendingTransferJson;
import com.act.mbanking.logic.RecentTransferJson;
import com.act.mbanking.manager.view.RecentPaymentItem;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.DoubleShadowTextView;
import com.act.mbanking.view.RecentPaymentGalleryAdapter;
import com.custom.view.CoverFlow;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class RecentPaymentManager extends MainMenuSubScreenManager implements OnItemClickListener {
	private static final String EXECUTED="EXECUTED";
	private static final String PENDING="PENDING";

    CoverFlow recentPaymentGallery;

    private TabHost tabhost;

    RecentPaymentGalleryAdapter recentPaymentGalleryAdapter;

    ListView recent_payment_ListView;

    ListView recent_payment_ListView_pending;

    /**
     * 存放过滤出来的020 bankServiceCode的account
     */
    private List<AccountsModel> accountsList;

    private List<RecentPaymentList> allList = new ArrayList<RecentPaymentList>();

    public static final int GET_RECENT_TRANSFER = 0;

    private int galleryIndex;

    Handler handler;

    private ResponsePublicModel responsePublic;
    
   private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    public RecentPaymentManager(MainActivity activity) {
        super(activity);
        handler = new Handler();
    }

    @Override
    protected void init() {
        layout = (ViewGroup)super.activity.findViewById(R.id.recent_payment);
        recentPaymentGallery = (CoverFlow)layout.findViewById(R.id.recent_payment_account_folders);
        setLeftNavigationText(R.string.back);
        recentPaymentGalleryAdapter = new RecentPaymentGalleryAdapter(activity,
                recentPaymentGallery); 
        recentPaymentGalleryAdapter.setViewId(R.layout.account_data_opened_content);
        recentPaymentGallery.setAdapter(recentPaymentGalleryAdapter);
        recentPaymentGallery.setAnimationDuration(500);

        tabhost = (TabHost)layout.findViewById(android.R.id.tabhost);
        tabhost.setup();
        recentPaymentGallery.setOnItemClickListener(this);
        
        recent_payment_ListView = (ListView)layout.findViewById(R.id.recent_payment_item_listview);
        recentPaymentAdapter = new RecentPaymentAdapter(activity);
        recent_payment_ListView.setAdapter(recentPaymentAdapter);
        
        recent_payment_ListView_pending = (ListView)layout.findViewById(R.id.expandableListView1);
        recentPaymentAdapter_pending = new RecentPaymentPendingTransferAdapter(activity);
        recent_payment_ListView_pending .setAdapter(recentPaymentAdapter_pending);
        
        LayoutInflater inflater_tab1 = LayoutInflater.from(activity);
        DoubleShadowTextView title = (DoubleShadowTextView)inflater_tab1.inflate(
                R.layout.tabhost_title_model, null);
        title.setShadowColorDefault(0xffffffff);
        title.setShadowColorSelected(0xff000000);
        title.setText(EXECUTED);
        tabhost.addTab(tabhost.newTabSpec(EXECUTED).setIndicator(title).setContent(R.id.tab1));
        title = (DoubleShadowTextView)inflater_tab1.inflate(R.layout.tabhost_title_model, null);
        title.setShadowColorDefault(0xffffffff);
        title.setShadowColorSelected(0xff000000);
        title.setText(PENDING);
        tabhost.addTab(tabhost.newTabSpec(PENDING).setIndicator(title).setContent(R.id.tab2));
    }
    
    public boolean onLeftNavigationButtonClick(View v) {
    	if(lastMainMenuSubScreenManager!=null){
    		mainManager.showView(lastMainMenuSubScreenManager,true, null);
    	}else{
    		mainManager.showAggregatedView(true, null);
    	}
        return true;
    }
    
    protected void onShow(Object object){
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker("UA-43055680-1");
        mGaTracker1.sendView("event.recent.payment.recover"); 
    	lastMainMenuSubScreenManager=mainManager.getLastView();
        //mainManager 在manger的管理类MainManger里初始化的晚,所以放到这里
        recentPaymentAdapter.setMainManager(mainManager);
        recentPaymentAdapter_pending.setMainManager(mainManager);
        accountsList=App.app.bankTransferAccounts.get(0).getAccounts();
        recentPaymentGalleryAdapter.setAccountList(accountsList);
        recentPaymentGalleryAdapter.notifyDataSetChanged();
    }

    RecentPaymentAdapter recentPaymentAdapter;

    RecentPaymentPendingTransferAdapter recentPaymentAdapter_pending;

    private void loadRecentPaymentTransfer(int index) {
        final String accountCode = accountsList.get(galleryIndex).getAccountCode();

        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show("loading", new OnProgressEvent() {
            @Override
            public void onProgress() {
            	RecentPaymentList recentpaymentList=null;
            		for (int i = 0; i < allList.size(); i++) {
            			if(allList.get(i).getAccountCode().equals(accountCode)){
            				recentpaymentList = allList.get(i);
            				break;
            			}
            		}
            		if(recentpaymentList==null){
            			recentpaymentList = new RecentPaymentList();
            			recentpaymentList.setAccountCode(accountCode);
            			allList.add(recentpaymentList);
            		}
            		if(recentpaymentList.getRecentTransferList()==null||recentpaymentList.getRecentTransferList().size()<=0){
            			String postData = RecentTransferJson.RecentTransferReportProtocal(Contants.publicModel, accountCode, 20);
            			HttpConnector httpConnector = new HttpConnector();
            			String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,activity);
            			final RecentTransferResponseModel recentTransfer = RecentTransferJson.ParseRecentTransferResponse(httpResult);
            			if(recentTransfer.responsePublicModel.isSuccess()){
            				recentpaymentList.setRecentTransferList(recentTransfer.getRecentTransferList());
            			}
            		}

            		if(recentpaymentList.getPendingTransferList()==null||recentpaymentList.getPendingTransferList().size()<=0){
            			String postData = GetPendingTransferJson.getPendingTransferReportProtocal(Contants.publicModel);
            			HttpConnector httpConnector = new HttpConnector();
            			String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,activity);
            			final GetPendingTransferModel recentTransfer = GetPendingTransferJson.parseGetPendingTransferResponse(httpResult);
            			if(recentTransfer.responsePublicModel.isSuccess()){
            				recentpaymentList.setPendingTransferList(recentTransfer.getPendingTransferList());
            			}
            		}
            		recentPaymentAdapter.seList(recentpaymentList.getRecentTransferList());
            		recentPaymentAdapter_pending.seList(recentpaymentList.getPendingTransferList());
            		myhandler.sendEmptyMessage(GET_RECENT_TRANSFER);
            	}
        });
    }


    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_RECENT_TRANSFER:
                	recentPaymentAdapter.notifyDataSetChanged();
                    recentPaymentAdapter_pending.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> viewId, View view, int index, long arg3) {
        galleryIndex = index;
        loadRecentPaymentTransfer(galleryIndex);
        recentPaymentAdapter.setAccountsModel(accountsList.get(galleryIndex));
        recentPaymentAdapter_pending.setAccountsModel(accountsList.get(galleryIndex));
    }

    public static class RecentPaymentAdapter extends BaseAdapter implements OnClickListener {

        private List<RecentTransferModel> recentTransferList = new ArrayList<RecentTransferModel>();

        private AccountsModel accountModel;

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
        
        MainManager mainManager;
        
        public void setMainManager(MainManager mainManager){
            this.mainManager=mainManager;
        }

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
            RecentPaymentItem child = null;
            View recover;
            if (convertView != null) {
                child = (RecentPaymentItem)convertView;
            } else {
                child = (RecentPaymentItem)mInflater.inflate(R.layout.recent_payment_item, null);
                child.init();
                recover=child.findViewById(R.id.recent_recover_btn);
                recover.setOnClickListener(this);
            }
            recover=child.findViewById(R.id.recent_recover_btn);
            recover.setTag(position);
            child.typeText.setText(recentTransferList.get(position).getType());
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

        public AccountsModel getAccountsModel() {
            return accountModel;
        }

        public void setAccountsModel(AccountsModel accountModel) {
            this.accountModel = accountModel;
        }
        
        @Override
        public void onClick(View v) {
            int position=(Integer)v.getTag();// TODO Auto-generated method stub
            NPaymentsManager.startForRecover(mainManager, recentTransferList.get(position), accountModel);
        }

    }
    


    public static class RecentPaymentPendingTransferAdapter extends BaseAdapter implements OnClickListener {

        private List<PendingTransferModel> recentTransferList;

        private AccountsModel accountModel;

        /**
         * @param recentTransferList 要设置的 recentTransferList
         */
        public void seList(List<PendingTransferModel> recentTransferList) {
            this.recentTransferList = recentTransferList;
        }

        private LayoutInflater mInflater;

        TextView timeText, typeText, resultText, one_text, four_text, two_text, three_text,
                five_text;

        Context context;
        
        MainManager mainManager;
        
        public void setMainManager(MainManager mainManager){
            this.mainManager=mainManager;
        }

        public RecentPaymentPendingTransferAdapter(Context context) {
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
        	if(recentTransferList==null){
        		return 0;
        	}
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
            RecentPaymentItem child = null;
            View recover;
            if (convertView != null) {
                child = (RecentPaymentItem)convertView;
            } else {
                child = (RecentPaymentItem)mInflater.inflate(R.layout.recent_payment_item, null);
                child.init();
                recover=child.findViewById(R.id.recent_recover_btn);
                recover.setOnClickListener(this);
            }
            recover=child.findViewById(R.id.recent_recover_btn);
            recover.setTag(position);
            child.typeText.setText(recentTransferList.get(position).getType());
            String date = recentTransferList.get(position).getExecutionDate();
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
                child.one_text.setText(recentTransferList.get(position).getBeneficiaryTitle());
                String operator = DestProvider.getDsstProvider(recentTransferList.get(position)
                        .getBeneficiaryProvider());
                child.two_text.setText(operator);
                String state = TransferState.getTransferState(recentTransferList.get(position)
                        .getBillType());
                child.three_text.setText(state);
                child.three_text.setVisibility(View.VISIBLE);
                child.four_text.setVisibility(View.GONE);
                child.five_text.setVisibility(View.GONE);
//                child.setRecentTransferModel(recentTransferList.get(position));
                child.setAccountModel(getAccountsModel());
            } else if (recentTransferList.get(position).getType().equals(Contants.BANK_TRANSFER)
                    || recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
                // bank transfer & transfer entry UI展示
                if (recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
                    child.one_text.setVisibility(View.GONE);
                } else {
                    child.one_text.setText(recentTransferList.get(position).getBeneficiaryTitle());
                    child.one_text.setVisibility(View.VISIBLE);
                    child.three_text.setVisibility(View.VISIBLE);
                    child.four_text.setVisibility(View.VISIBLE);
                }
                child.two_text.setText(recentTransferList.get(position).getBeneficiaryIban());
                child.three_text.setText(recentTransferList.get(position).getPurposeDescription());
                String state = TransferState.getTransferState(recentTransferList.get(position).getBillType());
                child.four_text.setText(state);
                child.five_text.setVisibility(View.GONE);
//                child.setRecentTransferModel(recentTransferList.get(position));
                child.setAccountModel(getAccountsModel());
            } else if (recentTransferList.get(position).getType()
                    .equals(Contants.PREPAID_CARD_RELOAD)) {
                // prepaid card UI展示
                child.one_text.setText(recentTransferList.get(position).getBeneficiaryTitle());
                child.two_text.setText(recentTransferList.get(position).getBeneficiaryCardNumber());
                child.three_text.setVisibility(View.GONE);
                child.four_text.setVisibility(View.GONE);
                child.five_text.setVisibility(View.GONE);
//                child.setRecentTransferModel(recentTransferList.get(position));
                child.setAccountModel(getAccountsModel());
            }
            return child;
        }

        public AccountsModel getAccountsModel() {
            return accountModel;
        }

        public void setAccountsModel(AccountsModel accountModel) {
            this.accountModel = accountModel;
        }
        
        @Override
        public void onClick(View v) {
            int position=(Integer)v.getTag();// TODO Auto-generated method stub
//            NPaymentsManager.startForRecover(mainManager, recentTransferList.get(position), accountModel);
        }

    }

    @Override
    protected void loadData() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void setUI() {
        // TODO Auto-generated method stub

    }

}
