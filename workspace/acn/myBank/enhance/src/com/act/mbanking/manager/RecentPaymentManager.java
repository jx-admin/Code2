
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.act.mbanking.App;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.TransferState;
import com.act.mbanking.activity.BaseActivity;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.ChargeSizeModel;
import com.act.mbanking.bean.GetPendingTransferModel;
import com.act.mbanking.bean.PendingTransferModel;
import com.act.mbanking.bean.RecentPaymentList;
import com.act.mbanking.bean.RecentTransferModel;
import com.act.mbanking.bean.RecentTransferResponseModel;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.logic.GetPendingTransferJson;
import com.act.mbanking.logic.RecentTransferJson;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.RecentPaymentGalleryAdapter;
import com.custom.view.CoverFlow;
import com.custom.view.DoubleShadowTextView;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class RecentPaymentManager implements OnItemClickListener, OnTabChangeListener {
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

    private int galleryIndex=-1;

    Handler handler;

    private ResponsePublicModel responsePublic;
    
   private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;
  	
  	private BaseActivity activity;
  	
  	ViewGroup layout;
  	
  	public void setLeftNavigationText(int id){
  		
  	}

    public RecentPaymentManager(BaseActivity context) {
    	this.activity=context;
        handler = new Handler();
    }

    public ViewGroup init() {
        this.layout =(ViewGroup)activity.findViewById(R.id.recent_payment);
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
        title.setText(R.string.sa_executed);
        tabhost.addTab(tabhost.newTabSpec(EXECUTED).setIndicator(title).setContent(R.id.tab1));
        title = (DoubleShadowTextView)inflater_tab1.inflate(R.layout.tabhost_title_model, null);
        title.setShadowColorDefault(0xffffffff);
        title.setShadowColorSelected(0xff000000);
        title.setText(R.string.sa_pending);
        tabhost.addTab(tabhost.newTabSpec(PENDING).setIndicator(title).setContent(R.id.tab2));
        tabhost.setOnTabChangedListener(this);
        return layout;
    }
    
    public boolean onLeftNavigationButtonClick(View v) {
//    	if(lastMainMenuSubScreenManager!=null){
//    		mainManager.showView(lastMainMenuSubScreenManager,true, null);
//    	}else{
//    		mainManager.showAggregatedView(true, null);
//    	}
    	galleryIndex=-1;
        return false;
    }
    public void onShow(Object object){
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("event.recent.payment.recover"); 
//    	lastMainMenuSubScreenManager=mainManager.getLastView();
        //mainManager 在manger的管理类MainManger里初始化的晚,所以放到这里
//        recentPaymentAdapter.setMainManager(mainManager);
//        recentPaymentAdapter_pending.setMainManager(mainManager);
        accountsList=App.app.bankTransferAccounts.get(0).getAccounts();
        recentPaymentGalleryAdapter.setAccountList(accountsList);
        recentPaymentGalleryAdapter.notifyDataSetChanged();
		Holder.selectedId=-1;
		Holder.selectedHolder=null;
        if(galleryIndex>=0){
            loadRecentPaymentTransfer(galleryIndex);
            recentPaymentAdapter.setAccountsModel(accountsList.get(galleryIndex));
            recentPaymentAdapter_pending.setAccountsModel(accountsList.get(galleryIndex));
        }
    }

    RecentPaymentAdapter recentPaymentAdapter;

    RecentPaymentPendingTransferAdapter recentPaymentAdapter_pending;

    private void loadRecentPaymentTransfer(int index) {
        final String accountCode = accountsList.get(galleryIndex).getAccountCode();

        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show(activity.getString(R.string.loading), new OnProgressEvent() {
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
//            		if(recentpaymentList.getRecentTransferList()==null||recentpaymentList.getRecentTransferList().size()<=0){
            			String postData = RecentTransferJson.RecentTransferReportProtocal(Contants.publicModel, accountCode, 20);
            			HttpConnector httpConnector = new HttpConnector();
            			String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,activity);
            			final RecentTransferResponseModel recentTransfer = RecentTransferJson.ParseRecentTransferResponse(httpResult);
            			if(recentTransfer.responsePublicModel.isSuccess()){
            				recentpaymentList.setRecentTransferList(recentTransfer.getRecentTransferList());
            			}
//            		}

//            		if(recentpaymentList.getPendingTransferList()==null||recentpaymentList.getPendingTransferList().size()<=0){
            			 postData = GetPendingTransferJson.getPendingTransferReportProtocal(Contants.publicModel);
//            			 httpConnector = new HttpConnector();
            			 httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,activity);
            			final GetPendingTransferModel pendingTransfer = GetPendingTransferJson.parseGetPendingTransferResponse(httpResult);
            			if(pendingTransfer.responsePublicModel.isSuccess()){
            				recentpaymentList.setPendingTransferList(pendingTransfer.getPendingTransferList());
            			}
//            		}
            		recentPaymentAdapter.seList(recentpaymentList.getRecentTransferList());
            		List<PendingTransferModel> mPendingTransferList=recentpaymentList.getPendingTransferList();
            		for(int i=mPendingTransferList.size()-1;i>=0;i--){
            			if(!accountCode.equals(mPendingTransferList.get(i).getAccountCode())){
            				mPendingTransferList.remove(i);
            			}
            		}
            		recentPaymentAdapter_pending.seList(mPendingTransferList);
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

	@Override
	public void onTabChanged(String arg0) {
		Holder.selectedId=-1;
		Holder.selectedHolder=null;
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
        	RecentTransferModel mRecentTransferModel=recentTransferList.get(position);
        	 if (convertView == null) {
             	convertView = (View)mInflater.inflate(R.layout.pending_payment_item, null);
             }
             Holder mHolder=(Holder) convertView.getTag();
             if(mHolder==null){
             	mHolder=new Holder();
             	mHolder.init(convertView);
             	convertView.setTag(mHolder);
             }
//             convertView.findViewById(R.id.recent_recover_btn).setOnClickListener(this);
             mHolder.recentRecoverBtn.setOnClickListener(this);
             mHolder.recentRecoverBtn.setTag(position);
             
            mHolder.typeText.setText(mRecentTransferModel.getType());
            
            String date = mRecentTransferModel.getOperationDate();
            if (date != null || !"".equals(date)) {
                try {
                    date = TimeUtil.changeFormattrString(date, TimeUtil.dateFormat2,
                            TimeUtil.dateFormat5);
                } catch (Exception e) {
                    LogManager.e("formatDate is error" + e.getLocalizedMessage());
                }
            }
            mHolder.timeText.setText(date);
            // 格式化金额
            String amount = Utils.formatMoney(mRecentTransferModel.getAmount(),context.getResources().getString(R.string.dollar), true, true,false, false, true);
            mHolder.resultText.setText(amount);
            
            
            String str=mRecentTransferModel.getBeneficiaryName();
        	if(TextUtils.isEmpty(str)){
        		mHolder.one_text.setVisibility(View.GONE);
        	}else{
        		str=context.getString(R.string.recent_beneficiary, str);
        		mHolder.one_text.setText(str);
        		mHolder.one_text.setVisibility(View.VISIBLE);
        	}
        	
        	str=mRecentTransferModel.getBeneficiaryIban();
        	if(TextUtils.isEmpty(str)){
        		str=mRecentTransferModel.getBeneficiaryNumber();
        		if(TextUtils.isEmpty(str)){
        			str=mRecentTransferModel.getBeneficiaryCardNumber();
        			if(TextUtils.isEmpty(str)){
            			str=mRecentTransferModel.getPostalAccount();
            		}
        		}
        	}
        	if(TextUtils.isEmpty(str)){
        		mHolder.two_text.setVisibility(View.GONE);
        	}else{
        		str=context.getString(R.string.recent_number, str);
        		mHolder.two_text.setText(str);
        		mHolder.two_text.setVisibility(View.VISIBLE);
        	}
        	
        	str=mRecentTransferModel.getDescription();
        	if(TextUtils.isEmpty(str)){
        		mHolder.three_text.setVisibility(View.GONE);
        	}else{
        		str=context.getString(R.string.recent_description, str);
        		mHolder.three_text.setText(str);
        		mHolder.three_text.setVisibility(View.VISIBLE);
        	}
        	
        	str=mRecentTransferModel.getTransferState();
//            String operator = DestProvider.getDsstProvider(mRecentTransferModel .getBeneficiaryProvider());
        	if(TextUtils.isEmpty(str)){
        		mHolder.four_text.setVisibility(View.GONE);
        	}else{
        		str = TransferState.getTransferState(str);
        		str=context.getString(R.string.recent_state, str);
        		mHolder.four_text.setText(str);
        		mHolder.four_text.setVisibility(View.VISIBLE);
        	}

        	mHolder.five_text.setVisibility(View.GONE);
        	
        	
//            if (mRecentTransferModel.getType().equals(Contants.SIM_TOP_UP)) {
//            	// sim top up UI展示
//            	
//            	
//            	
//            } else if (recentTransferList.get(position).getType().equals(Contants.BANK_TRANSFER)
//                    || recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
//                // bank transfer & transfer entry UI展示
//                if (recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
//                    mHolder.one_text.setVisibility(View.GONE);
//                } else {
//                    mHolder.one_text.setText(recentTransferList.get(position).getBeneficiaryName());
//                    mHolder.one_text.setVisibility(View.VISIBLE);
//                    mHolder.three_text.setVisibility(View.VISIBLE);
//                    mHolder.four_text.setVisibility(View.VISIBLE);
//                }
//                mHolder.two_text.setText(recentTransferList.get(position).getBeneficiaryIban());
//                mHolder.three_text.setText(recentTransferList.get(position).getDescription());
//                String state = TransferState.getTransferState(recentTransferList.get(position)
//                        .getTransferState());
//                mHolder.four_text.setText(state);
//                mHolder.five_text.setVisibility(View.GONE);
//            } else if (recentTransferList.get(position).getType()
//                    .equals(Contants.PREPAID_CARD_RELOAD)) {
//                // prepaid card UI展示
//                mHolder.one_text.setText(recentTransferList.get(position).getBeneficiaryName());
//                mHolder.two_text.setText(recentTransferList.get(position).getBeneficiaryCardNumber());
//                mHolder.three_text.setVisibility(View.GONE);
//                mHolder.four_text.setVisibility(View.GONE);
//                mHolder.five_text.setVisibility(View.GONE);
//            }else 
            	if (recentTransferList.get(position).getType().equals(Contants.BILL_PAYMENT)) {
                // Bill Payment展示
//                mHolder.one_text.setText(recentTransferList.get(position).getBeneficiaryName());
//                mHolder.two_text.setText(recentTransferList.get(position).getPostalAccount());
//                String state = TransferState.getTransferState(recentTransferList.get(position).getTransferState());
//                mHolder.four_text.setText(state);
//                mHolder.five_text.setVisibility(View.GONE);
                
                // prepaid card UI展示
                if(PaymentsManagerBill._674
    					.equals(mRecentTransferModel.getBillType())||PaymentsManagerBill._896
    					.equals(mRecentTransferModel.getBillType())){
                    mHolder.typeText.setText(Contants.PRECOMPILED_BILL);
    			}else if(PaymentsManagerBlankBill._123
    					.equals(mRecentTransferModel.getBillType())||PaymentsManagerBlankBill._451
    					.equals(mRecentTransferModel.getBillType())){
                    mHolder.typeText.setText(Contants.BLANK_BILL);
    			}else if(PaymentsManagerMAVRAV.MAV
    					.equals(mRecentTransferModel.getBillType())||PaymentsManagerMAVRAV.RAV
    					.equals(mRecentTransferModel.getBillType())){
                    mHolder.typeText.setText(Contants.MAV_RAV);
    			}
            }
            mHolder.invalidate(position);
            return convertView;
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
            NPaymentsManager.startForRecover(MainActivity.mainManager, recentTransferList.get(position), accountModel);
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
        	PendingTransferModel mPendingTransferModel =recentTransferList.get(position);
            if (convertView == null) {
            	convertView = (View)mInflater.inflate(R.layout.pending_payment_item, null);
            }
            Holder mHolder=(Holder) convertView.getTag();
            if(mHolder==null){
            	mHolder=new Holder();
            	mHolder.init(convertView);
            	convertView.setTag(mHolder);
            }
            mHolder.recentRecoverBtn.setOnClickListener(this);
            mHolder.recentRecoverBtn.setTag(position);
            
            mHolder.typeText.setText(mPendingTransferModel.getType());
            String date = mPendingTransferModel.getExecutionDate();
            if (date != null || !"".equals(date)) {
                try {
                    date = TimeUtil.changeFormattrString(date, TimeUtil.dateFormat2,
                            TimeUtil.dateFormat5);
                } catch (Exception e) {
                    LogManager.e("formatDate is error" + e.getLocalizedMessage());
                }
            }
            mHolder.timeText.setText(date);
            String amount=null;
            if(Contants.SIM_TOP_UP.equals(mPendingTransferModel.getType())){
            	ChargeSizeModel mChargeSizeModel =mPendingTransferModel.getChargeSizeModel();
            	if(mChargeSizeModel!=null){
            		try{
            			double amountd=Double.parseDouble(mChargeSizeModel.getRechargeAmount());
            			mPendingTransferModel.setAmount(amountd);
            			amount = Utils.formatMoney(amountd,context.getResources().getString(R.string.dollar), true, true,false, false, true);
            		}catch(Exception e){
            			e.printStackTrace();
            		}
            	}
            }else{
            // 鏍煎紡鍖栭噾棰�
            amount = Utils.formatMoney(mPendingTransferModel.getAmount(),context.getResources().getString(R.string.dollar), true, true,false, false, true);
            }
            // 格式化金额
            mHolder.resultText.setText(amount);
           
            
            
            String str=mPendingTransferModel.getBeneficiaryName();
            if(TextUtils.isEmpty(str)){
        		str=mPendingTransferModel.getBeneficiaryTitle();
            }
        	if(TextUtils.isEmpty(str)){
        		mHolder.one_text.setVisibility(View.GONE);
        	}else{
        		str=context.getString(R.string.recent_beneficiary, str);
        		mHolder.one_text.setText(str);
        		mHolder.one_text.setVisibility(View.VISIBLE);
        	}
        	
        	str=mPendingTransferModel.getBeneficiaryIban();
        	if(TextUtils.isEmpty(str)){
        		str=mPendingTransferModel.getBeneficiaryPhoneNumber();
        		if(TextUtils.isEmpty(str)){
        			str=mPendingTransferModel.getBeneficiaryCardNumber();
        			if(TextUtils.isEmpty(str)){
            			str=mPendingTransferModel.getPostalAccount();
            		}
        		}
        	}
        	if(TextUtils.isEmpty(str)){
        		mHolder.two_text.setVisibility(View.GONE);
        	}else{
        		str=context.getString(R.string.recent_number, str);
        		mHolder.two_text.setText(str);
        		mHolder.two_text.setVisibility(View.VISIBLE);
        	}
        	
        	str=mPendingTransferModel.getPurposeDescription();
        	if(TextUtils.isEmpty(str)){
        		mHolder.three_text.setVisibility(View.GONE);
        	}else{
        		str=context.getString(R.string.recent_description, str);
        		mHolder.three_text.setText(str);
        		mHolder.three_text.setVisibility(View.VISIBLE);
        	}
        	
        	mHolder.four_text.setVisibility(View.GONE);
        	mHolder.five_text.setVisibility(View.GONE);
        	
//        	if (recentTransferList.get(position).getType().equals(Contants.SIM_TOP_UP)) {
//                // sim top up UI展示
//            	mHolder.one_text.setText(recentTransferList.get(position).getBeneficiaryTitle());
//                String operator = DestProvider.getDsstProvider(recentTransferList.get(position)
//                        .getBeneficiaryProvider());
//                mHolder.two_text.setText(operator);
//                String state = TransferState.getTransferState(recentTransferList.get(position)
//                        .getBillType());
//                mHolder.three_text.setText(state);
//                mHolder.three_text.setVisibility(View.VISIBLE);
//                mHolder.four_text.setVisibility(View.GONE);
//                mHolder.five_text.setVisibility(View.GONE);
//            } else if (recentTransferList.get(position).getType().equals(Contants.BANK_TRANSFER)
//                    || recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
//                // bank transfer & transfer entry UI展示
//                if (recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
//                	mHolder.one_text.setVisibility(View.GONE);
//                } else {
//                	mHolder.one_text.setText(recentTransferList.get(position).getBeneficiaryTitle());
//                	mHolder.one_text.setVisibility(View.VISIBLE);
//                	mHolder.three_text.setVisibility(View.VISIBLE);
//                	mHolder.four_text.setVisibility(View.VISIBLE);
//                }
//                mHolder.two_text.setText(recentTransferList.get(position).getBeneficiaryIban());
//                mHolder.three_text.setText(recentTransferList.get(position).getPurposeDescription());
//                String state = TransferState.getTransferState(recentTransferList.get(position).getBillType());
//                mHolder.four_text.setText(state);
//                mHolder.five_text.setVisibility(View.GONE);
//            } else if (recentTransferList.get(position).getType()
//                    .equals(Contants.PREPAID_CARD_RELOAD)) {
//                // prepaid card UI展示
//            	mHolder.one_text.setText(recentTransferList.get(position).getBeneficiaryTitle());
//            	mHolder.two_text.setText(recentTransferList.get(position).getBeneficiaryCardNumber());
//            	mHolder.three_text.setVisibility(View.GONE);
//            	mHolder.four_text.setVisibility(View.GONE);
//            	mHolder.five_text.setVisibility(View.GONE);
//            }else 
            	if (mPendingTransferModel.getType().equals(Contants.BILL_PAYMENT)) {
//            	 PendingTransferModel mPendingTransferModel=recentTransferList.get(position);
//                // prepaid card UI展示
//                mHolder.one_text.setText(mPendingTransferModel.getBeneficiaryTitle());
//                mHolder.two_text.setText(mPendingTransferModel.getPostalAccount());
//                mHolder.four_text.setText(mPendingTransferModel.getBillNumber());
//                mHolder.five_text.setVisibility(View.GONE);
                if(PaymentsManagerBill._674
    					.equals(mPendingTransferModel.getBillType())||PaymentsManagerBill._896
    					.equals(mPendingTransferModel.getBillType())){
                    mHolder.typeText.setText(Contants.PRECOMPILED_BILL);
    			}else if(PaymentsManagerBlankBill._123
    					.equals(mPendingTransferModel.getBillType())||PaymentsManagerBlankBill._451
    					.equals(mPendingTransferModel.getBillType())){
                    mHolder.typeText.setText(Contants.BLANK_BILL);
    			}else if(PaymentsManagerMAVRAV.MAV
    					.equals(mPendingTransferModel.getBillType())||PaymentsManagerMAVRAV.RAV
    					.equals(mPendingTransferModel.getBillType())){
                    mHolder.typeText.setText(Contants.MAV_RAV);
    			}
            }
            mHolder.invalidate(position);
            return convertView;
        }

        public AccountsModel getAccountsModel() {
            return accountModel;
        }

        public void setAccountsModel(AccountsModel accountModel) {
            this.accountModel = accountModel;
        }
        
        @Override
        public void onClick(View v) {
        	Integer i=(Integer) v.getTag();
        	NPaymentsManager.startForRecover(MainActivity.mainManager, recentTransferList.get(i), accountModel);
        }

    }
    
	static class Holder {
		static int selectedId=-1;
		static Holder selectedHolder=null;
		
		ToggleButton toggleButton;

		LinearLayout recentExpandedContainer;

		public TextView timeText, typeText, resultText, one_text,
				four_text, two_text, three_text, five_text;

		Button recentRecoverBtn;

		ViewGroup recentExpanderBar;
		int position;

		public void init(View v) {
			toggleButton = (ToggleButton) v
					.findViewById(R.id.recent_expand_btn);
			toggleButton
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(
								CompoundButton buttonView, boolean isChecked) {
							if (isChecked) {
								if(selectedId!=position){
									if(selectedHolder!=null){
										selectedHolder.setSelected(false);
										selectedHolder=null;
									}
								}
								selectedId=position;
								selectedHolder=Holder.this;
								recentExpandedContainer
										.setVisibility(View.VISIBLE);
							} else {
								if(selectedId==position){
									selectedId=-1;
									selectedHolder=null;
									recentExpandedContainer
									.setVisibility(View.GONE);
								}
							}
						}
					});

			recentExpandedContainer = (LinearLayout) v
					.findViewById(R.id.recent_expanded_container);
			timeText = (TextView) v.findViewById(R.id.recent_time);
			resultText = (TextView) v
					.findViewById(R.id.recent_expand_result);
			typeText = (TextView) v.findViewById(R.id.recent_expand_type);
			one_text = (TextView) v.findViewById(R.id.one_text);
			two_text = (TextView) v.findViewById(R.id.two_text);
			three_text = (TextView) v.findViewById(R.id.three_text);
			four_text = (TextView) v.findViewById(R.id.four_text);
			five_text = (TextView) v.findViewById(R.id.five_text);
			recentRecoverBtn = (Button) v
					.findViewById(R.id.recent_recover_btn);
//			recentRecoverBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//				}
//			});
			recentExpanderBar = (ViewGroup) v
					.findViewById(R.id.recent_expander_bar);
			recentExpanderBar.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toggleButton.performClick();
				}
			});
		}
		
		public void setSelected(boolean check){
			
			if(check){
				recentExpandedContainer.setVisibility(View.VISIBLE);
				toggleButton.setChecked(true);
			}else{
				recentExpandedContainer.setVisibility(View.GONE);
				toggleButton.setChecked(false);
			}
		}
		
		public void invalidate(int position){
			this.position=position;
			if(selectedId==position){
				setSelected(true);
			}else{
				setSelected(false);
			}
		}
	}

}
