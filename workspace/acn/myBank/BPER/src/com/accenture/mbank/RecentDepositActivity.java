package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.accenture.mbank.logic.AccountsByServicesJson;
import com.accenture.mbank.logic.GetCreditPaymentsListJson;
import com.accenture.mbank.logic.GetCreditsListJson;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.CreditModel;
import com.accenture.mbank.model.CreditPaymentModel;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.model.GetCreditList;
import com.accenture.mbank.model.GetCreditPaymentList;
import com.accenture.mbank.model.ServicesModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.ServiceCode;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountInfoTitle;
import com.accenture.mbank.view.QuickReturnListView;
import com.accenture.mbank.view.QuickReturnListView.OnLoadMoreListener;

public class RecentDepositActivity extends BaseActivity implements
		OnCheckedChangeListener, OnLoadMoreListener,
		OnItemClickListener {

	private ViewPager viewPaper;

	private ViewGroup pointViewGroup;

	private RadioButton distinte_btn, effetti_btn;

	private LinearLayout headerView;

	private RadioGroup effetti_radio_group;

	public RadioButton all, pending, not_payed, payed, other;
	
	private TextView insertDate;

	private List<AccountsForServiceModel> accountForServiceList;
	
	private List<AccountsModel> accountsModel;

	private ImageView pointImage;

	private ImageView arrow_left;
	private ImageView arrow_right;

	/**
	 * account title的标志位
	 */
	public int viewPagerIndex = 0;

	private int DISTINTE = 0;
	private int EFFETTI = 1;
	private int ALL = 2;
	private int PENDING = 3;
	private int NOT_PAYED = 4;
	private int PAYED = 6;
	private int OTHER = 7;
	
	private List<CreditModel> creditList;

	public Context mContext;

	private Handler mHandler;

	private OnLoadMoreListener mLoadMoreListener;

	private RecentDepositAdapter detailsAdapter;

	private String restartingKey= "";

	private boolean isLoadMore;
	
	private boolean isCreditPaymentListView = false;
	
	private QuickReturnListView listView, allCreditPaymentListVIew;

	public static final String TUTTI = "Tutti";
	public static final String IN_ESSERE = "In essere";
	public static final String INSOLUTO = "Insoluto";
	public static final String PAGATO = "Pagato";
	public static final String ALTRO = "Altro";

	private List<CreditPaymentModel> allCreditPaymentList;
	RecentEffectsAdapter allCreditpaymentAdapter;
	
	public static final String CREDIT_LIST = "CREDIT_LIST";
	public static final String CREDIT_PAYMENT_LIST = "CREDIT_PAYMENT_LIST";
	public static final String ACCOUNT_MODEL = "ACCOUNT_MODEL";
	public static final String TYPE = "TYPE";
	public static final int EFFETC = 0;
	public static final int DETAILS = 1;
	private int type = DETAILS;
	
	public String _accountCode;
	public String creditID;
	public String insertionDate;
	AccountsModel effectsAccountsModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recent_deposit);
		mContext = this;
		mLoadMoreListener = this;
		mHandler = new Handler();
		
		String _type = getIntent().getStringExtra(RecentDepositActivity.TYPE);
		effectsAccountsModel = (AccountsModel) getIntent().getSerializableExtra(ACCOUNT_MODEL);
		if(_type == null){
			type = DETAILS;
			initUI();
			loadData();
		}else {
			type = Integer.parseInt(_type);
			creditID = getIntent().getStringExtra("CREDIT_ID");
			insertionDate = getIntent().getStringExtra("INSERTION_DATE");
			initUI();
			loadEffectsHeader(effectsAccountsModel);
		}
	}
	
	private void loadEffectsHeader(AccountsModel accountsModel){
		ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.payment_account);
		accountInfotitle.icon.setImageResource(R.drawable.account_detail_icon);
		accountInfotitle.accountType.setTextColor(csl);
		accountInfotitle.accountName.setTextColor(csl);
		accountInfotitle.account_balance_name.setText(R.string.balance_account);
		accountInfotitle.available_balance_name.setText(R.string.available_account);
		accountInfotitle.user_view.setBackgroundResource(R.color.payment_account);
		accountInfotitle.sb_user_view.setBackgroundResource(R.color.payment_account);
		
		accountInfotitle.icon.setImageResource(R.drawable.account_detail_icon);
		accountInfotitle.accountName.setText(accountsModel.getAccountAlias());
		if(accountsModel.getIsPreferred()){
			accountInfotitle.setPerferredStar(accountInfotitle.PAYMENT);
		}
		accountInfotitle.accountType.setText(R.string.account2);
		
		String accountBalanceValue = Utils.generateFormatMoney(getResources().getString(R.string.eur), accountsModel.getAccountBalance());
		accountInfotitle.account_balance_value.setText(accountBalanceValue);
		String availableBalanceValue = Utils.generateFormatMoney(getResources().getString(R.string.eur), accountsModel.getAvailableBalance());
		accountInfotitle.available_balance_value.setText(availableBalanceValue);
		
		loadGetCreditPaymentData(this.isLoadMore, accountsModel.getAccountCode(),creditID,insertionDate,"",getRestartingKey());
	}
	
	private void loadData(){
		ProgressOverlay progressOverlay = new ProgressOverlay(this);
		progressOverlay.show("", new OnProgressEvent() {
			@Override
			public void onProgress() {
				accountForServiceList = getAccountsByService(ServiceCode.RECENT_DEPOSIT);
				if(accountForServiceList != null){
					loadGetCreditList(accountForServiceList.get(0).getAccounts().get(0).getAccountCode(),getRestartingKey());
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							accountsModel = accountForServiceList.get(0).getAccounts();
							viewPaper.setAdapter(new MyPagerViewAdapter(mContext,accountsModel));
							int accountSize=0;
							if (accountsModel != null) {
								accountSize=accountsModel.size();
							}
							if(accountSize>1){
								arrow_left.setVisibility(View.VISIBLE);
								arrow_right.setVisibility(View.VISIBLE);
								arrow_right.setEnabled(accountSize>1);
								arrow_left.setEnabled(accountSize>0&&viewPaper.getCurrentItem()>0);
							}else{
								arrow_left.setVisibility(View.GONE);
								arrow_right.setVisibility(View.GONE);
								pointViewGroup.setVisibility(View.GONE);
								listViewHeaderView.findViewById(R.id.point_viewGroup).setVisibility(View.GONE);
								allCreditPaymentListVIewHeaderView.findViewById(R.id.point_viewGroup).setVisibility(View.GONE);
							}
						}
					});
				}
			}
		});
	}
	
	private LayoutInflater inflater;
	AccountInfoTitle accountInfotitle;
	private LinearLayout listViewHeaderView;
	private LinearLayout allCreditPaymentListVIewHeaderView;
	private void initUI() {
		inflater = LayoutInflater.from(this);
		
		listView = (QuickReturnListView) findViewById(R.id.listView);
		listViewHeaderView = addListHeaderView(inflater, listView,false,type);

		allCreditPaymentListVIew = (QuickReturnListView) findViewById(R.id.all_listView);
		allCreditPaymentListVIewHeaderView = addListHeaderView(inflater, allCreditPaymentListVIew,true,type);
		
		if (type == EFFETC) {
			headerView = (LinearLayout) findViewById(R.id.effects_header_view);
			TextView business_summary = (TextView) headerView.findViewById(R.id.business_summary);
			business_summary.setText(R.string.dettagli_effetti_distinta);
			findViewById(R.id.header_view).setVisibility(View.GONE);
			headerView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			allCreditPaymentListVIew.setVisibility(View.VISIBLE);
			
			accountInfotitle = (AccountInfoTitle) headerView.findViewById(R.id.effects_account_info_title);
			accountInfotitle.init();
			
			
		} else if(type == DETAILS){
			headerView = (LinearLayout) findViewById(R.id.header_view);
			viewPaper = (ViewPager) headerView.findViewById(R.id.account_viewPager);
			
			pointViewGroup = (ViewGroup) headerView.findViewById(R.id.point_viewGroup);
			arrow_left = (ImageView) headerView.findViewById(R.id.arrow_left);
			arrow_left.setOnClickListener(this);
			arrow_right = (ImageView) headerView.findViewById(R.id.arrow_right);
			arrow_right.setOnClickListener(this);
			
			distinte_btn = (RadioButton) headerView.findViewById(R.id.distinte_btn);
			distinte_btn.setOnCheckedChangeListener(this);
			effetti_btn = (RadioButton) headerView.findViewById(R.id.effetti_btn);
			effetti_btn.setOnCheckedChangeListener(this);
			
			insertDate = (TextView)headerView.findViewById(R.id.insertionDateTextView);
			
			MyListener myListener = new MyListener();
			viewPaper.setOnPageChangeListener(myListener);
			effetti_radio_group = (RadioGroup) headerView.findViewById(R.id.effetti_radio_group);
		}
		
		all = (RadioButton) headerView.findViewById(R.id.all);
		all.setOnCheckedChangeListener(this);
		pending = (RadioButton) headerView.findViewById(R.id.pending);
		pending.setOnCheckedChangeListener(this);
		not_payed = (RadioButton) headerView.findViewById(R.id.not_payed);
		not_payed.setOnCheckedChangeListener(this);
		payed = (RadioButton) headerView.findViewById(R.id.payed);
		payed.setOnCheckedChangeListener(this);
		other = (RadioButton) headerView.findViewById(R.id.other);
		other.setOnCheckedChangeListener(this);
		
		listView.setHeaderView(headerView);
		allCreditPaymentListVIew.setHeaderView(headerView);
	}

	private LinearLayout addListHeaderView(LayoutInflater inflater,QuickReturnListView listView,boolean isShowRadioGroup,int type) {
		LinearLayout listHeader = null;
		if(type == RecentDepositActivity.DETAILS){
			listHeader = (LinearLayout) inflater.inflate(R.layout.recent_deposit_header, null);
			insertDate = (TextView)listHeader.findViewById(R.id.insertionDateTextView);
			if(isShowRadioGroup){
				RadioGroup radioGroup = (RadioGroup) listHeader.findViewById(R.id.effetti_radio_group);
				radioGroup.setVisibility(View.VISIBLE);
				insertDate.setText(getResources().getString(R.string.expiration_2));
			}else{
				RadioGroup radioGroup = (RadioGroup) listHeader.findViewById(R.id.effetti_radio_group);
				radioGroup.setVisibility(View.GONE);
				insertDate.setText(getResources().getString(R.string.insertionDate1));
			}
		}else if(type == RecentDepositActivity.EFFETC){
			listHeader = (LinearLayout) inflater.inflate(R.layout.effects_header_view, null);
		}
		listHeader.setVisibility(View.INVISIBLE);
		listView.addHeaderView(listHeader);
		listView.setOnItemClickListener(this);
		return listHeader;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton radioButton, boolean isChecked) {
		if (isChecked) {
			String account = null;
			if(type == DETAILS){
				account = accountsModel.get(viewPagerIndex).getAccountCode();
			}else if(type == EFFETC){
				account = effectsAccountsModel.getAccountCode();
			}
			
			if (radioButton == distinte_btn) {
				isCreditPaymentListView = false;
				insertDate.setText(getResources().getString(R.string.insertionDate1));
				effetti_radio_group.setVisibility(View.GONE);
				showType(DISTINTE);
				detailsAdapter = null;
				listView.reInitialize();
				loadGetCreditData(false, account, "");
			} else if (radioButton == effetti_btn) {
				all.setChecked(true);
				isCreditPaymentListView = true;
				insertDate.setText(getResources().getString(R.string.expiration_2));
				effetti_radio_group.setVisibility(View.VISIBLE);
				showType(EFFETTI);
				allCreditpaymentAdapter = null;
				allCreditPaymentListVIew.reInitialize();
				loadGetCreditPaymentData(false,account,creditID,insertionDate,"","");
			} else if (radioButton == all) {
				showType(ALL);
				allCreditpaymentAdapter = null;
				this.allCreditPaymentList = null;
				loadGetCreditPaymentData(false,account,creditID,insertionDate,"","");
			} else if (radioButton == pending) {
				showType(PENDING);
				this.allCreditPaymentList = null;
				allCreditpaymentAdapter = null;
				loadGetCreditPaymentData(false,account,creditID,insertionDate,IN_ESSERE,"");
			} else if (radioButton == not_payed) {
				showType(NOT_PAYED);
				this.allCreditPaymentList = null;
				allCreditpaymentAdapter = null;
				loadGetCreditPaymentData(false,account,creditID,insertionDate,INSOLUTO,"");
			} else if (radioButton == payed) {
				showType(PAYED);
				allCreditpaymentAdapter = null;
				this.allCreditPaymentList = null;
				loadGetCreditPaymentData(false,account,creditID,insertionDate,PAGATO,"");
			} else if (radioButton == other) {
				showType(OTHER);
				allCreditpaymentAdapter = null;
				this.allCreditPaymentList = null;
				loadGetCreditPaymentData(false,account,creditID,insertionDate,ALTRO,"");
			}
		}
	}

	private void showType(int type) {
		if (type == DISTINTE) {
			show(listView);
		} else if (type == EFFETTI || type == ALL) {
			show(allCreditPaymentListVIew);
		}
	}

	private void show(View v) {
		hideRecords();
		v.setVisibility(View.VISIBLE);
	}
	
	private void hideRecords() {
		listView.setVisibility(View.GONE);
		allCreditPaymentListVIew.setVisibility(View.GONE);
	}

	private void setCreditRecord(List<CreditModel> list) {
		if (detailsAdapter == null) {
			this.creditList = list;
			detailsAdapter = new RecentDepositAdapter(creditList, this);
			listView.setAdapter(detailsAdapter);
		} else {
			this.creditList.addAll(list);
			detailsAdapter.list.addAll(list);
			detailsAdapter.notifyDataSetChanged();
		}
		listView.onLoadMoreComplete();
	}
	
	private void setCreditPaymentRecord(List<CreditPaymentModel> creditPaymentList) {
		if (allCreditpaymentAdapter == null) {
			this.allCreditPaymentList = creditPaymentList;
			allCreditpaymentAdapter = new RecentEffectsAdapter(creditPaymentList, this);
			allCreditPaymentListVIew.setAdapter(allCreditpaymentAdapter);
		} else {
			this.allCreditPaymentList.addAll(creditPaymentList);
			allCreditpaymentAdapter.list.addAll(creditPaymentList);
			allCreditpaymentAdapter.notifyDataSetChanged();
		}
		allCreditPaymentListVIew.onLoadMoreComplete();
	}

	private void loadGetCreditData(boolean isLoadmore,String accountCode,final String restartingKey) {
		ProgressOverlay progressOverlay = new ProgressOverlay(this);
		if (isLoadmore) {
			progressOverlay.runBackground(new OnProgressEvent() {
				@Override
				public void onProgress() {
					loadGetCreditList(accountsModel.get(viewPagerIndex).getAccountCode(),restartingKey);
				}
			});
		} else {
			progressOverlay.show("", new OnProgressEvent() {
				@Override
				public void onProgress() {
					loadGetCreditList(accountsModel.get(viewPagerIndex).getAccountCode(),restartingKey);
				}
			});
		}
	}
	
	
	private void loadGetCreditPaymentData(boolean isLoadmore,final String accountCode,final String creditID,final String insertionDate,final String filter,final String restartingKey) {
		ProgressOverlay progressOverlay = new ProgressOverlay(this);
		if (isLoadmore) {
			progressOverlay.runBackground(new OnProgressEvent() {
				@Override
				public void onProgress() {
					requestGetCreditPaymentList(accountCode,creditID,insertionDate,filter,restartingKey);
				}
			});
		} else {
			progressOverlay.show("", new OnProgressEvent() {
				@Override
				public void onProgress() {
					requestGetCreditPaymentList(accountCode,creditID,insertionDate,filter,restartingKey);
				}
			});
		}
	}

	/**
	 * @return the restartingKey
	 */
	public String getRestartingKey() {
		return restartingKey;
	}

	/**
	 * @param restartingKey
	 *            the restartingKey to set
	 */
	public void setRestartingKey(String restartingKey) {
		this.restartingKey = restartingKey;
	}

	public static class RecentDepositAdapter extends BaseAdapter {

		public List<CreditModel> list;

		Context context;

		public RecentDepositAdapter(List<CreditModel> list, Context context) {
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				LinearLayout recnet_deposit = (LinearLayout) inflater.inflate(R.layout.recent_deposit_item, null);
				convertView = recnet_deposit;
			}
			TextView insertionDate = (TextView) convertView.findViewById(R.id.insertionDate);
			TextView creditType = (TextView) convertView.findViewById(R.id.creditType);
			TextView state = (TextView) convertView.findViewById(R.id.state);
			TextView totalAmount = (TextView) convertView.findViewById(R.id.totalAmount);
			String date = TimeUtil.changeFormattrString(list.get(position).getInsertionDate(), TimeUtil.dateFormat2a,TimeUtil.dateFormat5);
			insertionDate.setText(date);
			creditType.setText(list.get(position).getCreditType());
			state.setText(list.get(position).getState());
			String totalAcmout = Utils.generateFormatMoney(context.getResources().getString(R.string.eur), list.get(position).getTotalAmount());
			totalAmount.setText(totalAcmout);
			return convertView;
		}
	}

	
	public static class RecentEffectsAdapter extends BaseAdapter {

		public List<CreditPaymentModel> list;

		Context context;

		public RecentEffectsAdapter(List<CreditPaymentModel> list, Context context) {
			this.list = list;
			this.context = context;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				LinearLayout recnet_deposit = (LinearLayout) inflater.inflate(R.layout.recent_deposit_item, null);
				convertView = recnet_deposit;
			}
			TextView insertionDate = (TextView) convertView.findViewById(R.id.insertionDate);
			TextView creditType = (TextView) convertView.findViewById(R.id.creditType);
			TextView state = (TextView) convertView.findViewById(R.id.state);
			TextView totalAmount = (TextView) convertView.findViewById(R.id.totalAmount);
			String date = TimeUtil.changeFormattrString(list.get(position).getExecutionDate(), TimeUtil.dateFormat2a,TimeUtil.dateFormat5);
			insertionDate.setText(date);
			creditType.setText(list.get(position).getListTypeItem());
			state.setText(list.get(position).getState());
			String totalAcmout = Utils.generateFormatMoney(context.getResources().getString(R.string.eur), list.get(position).getTotalAmount());
			totalAmount.setText(totalAcmout);
			return convertView;
		}
	}
	
	class MyPagerViewAdapter extends PagerAdapter {
		private List<AccountsModel> accountList;
		private LayoutInflater inflater;
		private Context context;
		public MyPagerViewAdapter(Context context, List<AccountsModel> accountList) {
			this.accountList = accountList;
			this.context = context;
		    inflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return accountList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View container, int index, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup view, int index) {
			ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.payment_account);
			AccountInfoTitle accountInfotitle = new AccountInfoTitle(context);
			accountInfotitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title,view,false);
			accountInfotitle.init();
			accountInfotitle.icon.setImageResource(R.drawable.account_detail_icon);
			accountInfotitle.accountType.setTextColor(csl);
			accountInfotitle.accountName.setTextColor(csl);
			accountInfotitle.account_balance_name.setText(R.string.balance_account);
			accountInfotitle.available_balance_name.setText(R.string.available_account);
			accountInfotitle.user_view.setBackgroundResource(R.color.payment_account);
			accountInfotitle.sb_user_view.setBackgroundResource(R.color.payment_account);
			
			AccountsModel _accountModel = accountList.get(index);
			accountInfotitle.icon.setImageResource(R.drawable.account_detail_icon);
			accountInfotitle.accountName.setText(_accountModel.getAccountAlias());
			if(_accountModel.getIsPreferred()){
				accountInfotitle.setPerferredStar(accountInfotitle.PAYMENT);
			}
			accountInfotitle.accountType.setText(R.string.account2);
			
			String accountBalanceValue = Utils.generateFormatMoney(getResources().getString(R.string.eur), _accountModel.getAccountBalance());
			accountInfotitle.account_balance_value.setText(accountBalanceValue);
			String availableBalanceValue = Utils.generateFormatMoney(getResources().getString(R.string.eur), _accountModel.getAvailableBalance());
			accountInfotitle.available_balance_value.setText(availableBalanceValue);
			
			((ViewPager) view).addView(accountInfotitle, 0);
			
			
			LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			// 设置每个小圆点距离左边的间距
			margin.setMargins(10, 0, 0, 0);
			pointImage = new ImageView(context);
			// 设置每个小圆点的宽高
			pointImage.setLayoutParams(new LayoutParams(15, 15));
			
			if(index == 0){
				// 默认选中第一张图片
				pointImage.setBackgroundResource(R.drawable.page_indicator_focused);
			}else {
				// 其他图片都设置未选中状态// 其他图片都设置未选中状态
				pointImage.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			
			pointViewGroup.addView(pointImage, margin);
			
			return accountInfotitle;
		}
	}

	class MyListener implements OnPageChangeListener {
		/**
		 * 当滑动状态改变时调用
		 */
		public void onPageScrollStateChanged(int arg0) {

		}

		/**
		 * 当前页面被滑动时调用
		 */
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		/**
		 * 当新的页面被选中时调用
		 */
		public void onPageSelected(int index) {
			viewPagerIndex = index;
			// 遍历数组让当前选中图片下的小圆点设置颜色
			int count = pointViewGroup.getChildCount();
			for(int i = 0; i<count;i++){
				pointViewGroup.getChildAt(index).setBackgroundResource(R.drawable.page_indicator_focused);
				if (index != i) {
					pointViewGroup.getChildAt(i).setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}
			distinte_btn.setChecked(true);
			effetti_btn.setChecked(false);
//			listView.setVisibility(View.VISIBLE);
//			allCreditPaymentListVIew.setVisibility(View.GONE);
			
			loadGetCreditData(false,accountsModel.get(viewPagerIndex).getAccountCode(),"");
			arrow_right.setEnabled(index<viewPaper.getAdapter().getCount()-1);
			arrow_left.setEnabled(index>0);
		}
	}

	private List<AccountsForServiceModel> getAccountsByService(String serviceCode) {
		try {
			List<AccountsForServiceModel> accountsForServiceModels = null;
			List<ServicesModel> services = new ArrayList<ServicesModel>();
			ServicesModel service = new ServicesModel();
			service.setServiceCode(serviceCode);
			services.add(service);
			String postData = AccountsByServicesJson.AccountsByServicesReportProtocal(Contants.publicModel,services);
			HttpConnector httpConnector = new HttpConnector();
			String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, this);
			GetAccountsByServicesResponseModel getAccountsByServices = AccountsByServicesJson.ParseGetAccountsByServicesResponse(httpResult);
			if (getAccountsByServices == null) {
				LogManager.d("responsePublicModelgetAccountsByServices = null"+ postData);
				finish();
				return null;
			}
			if (getAccountsByServices.responsePublicModel != null) {
				if (getAccountsByServices.responsePublicModel.isSuccess()) {
					accountsForServiceModels = getAccountsByServices.getAccountsForServiceList();
				} else {
					displayErrorMessage(getAccountsByServices.responsePublicModel.eventManagement.getErrorCode(),getAccountsByServices.responsePublicModel.eventManagement.getErrorDescription());
				}
			}
			return accountsForServiceModels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void loadGetCreditList(final String accountCode,final String restartingKey){
		String postData = GetCreditsListJson.GetCreditsListReportProtocal(Contants.publicModel,accountCode,restartingKey);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, this);
		final GetCreditList getCreditList = GetCreditsListJson.ParseCreditListResponse(httpResult);
		if(getCreditList == null){
			return;
		}
		if (getCreditList.responsePublicModel.isSuccess()) {
			if (getCreditList.getCreditList() != null && getCreditList.getCreditList().size() > 0) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if(restartingKey.equals("") || restartingKey == null){
							if (detailsAdapter != null) {
								detailsAdapter.list.clear();
							}
						}
						setRestartingKey(getCreditList.getRestartingKey());
						isLoadMore = getCreditList.isMoreValues();
						setCreditRecord(getCreditList.getCreditList());
						if (getCreditList.isMoreValues()) {
							listView.setOnLoadListener(mLoadMoreListener);
						} else {
							listView.setOnLoadListener(null);
						}
						listView.onLoadMoreComplete();
					}
				});
			}else{
				if(getCreditList.responsePublicModel.eventManagement.getErrorCode().equals("95001")){
					displayErrorMessage("",mContext.getResources().getString(R.string.receipts_not_founds));
				}else if(getCreditList.responsePublicModel.eventManagement.getErrorCode() != null && !getCreditList.responsePublicModel.eventManagement.getErrorCode().equals("")){
					displayErrorMessage("",mContext.getResources().getString(R.string.receipts_other_message));
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
//						if (detailsAdapter != null) {
							List<CreditModel> credit = new ArrayList<CreditModel>();
							detailsAdapter = new RecentDepositAdapter(credit,mContext);
							listView.setAdapter(detailsAdapter);
							listView.setOnLoadListener(null);
							listView.onLoadMoreComplete();
//						}
					}
				});
			}
		} else {
			if(getCreditList.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_1) || getCreditList.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_2)){
				displayErrorMessage(getCreditList.responsePublicModel.eventManagement.getErrorCode(),getCreditList.responsePublicModel.eventManagement.getErrorDescription());
			}else {
				displayErrorMessage("",mContext.getResources().getString(R.string.receipts_other_message));
			}
		}
	}
	
	private void requestGetCreditPaymentList(String accountCode,String creditID,String insertionDate,String filter,final String restartingKey) {
		String postData = GetCreditPaymentsListJson.GetCreditPaymentsListReportProtocal(Contants.publicModel,accountCode,creditID,insertionDate,filter,restartingKey);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, this);
		final GetCreditPaymentList getCreditPaymentList = GetCreditPaymentsListJson.ParseCreditPaymentsListResponse(httpResult);
		if (getCreditPaymentList.responsePublicModel.isSuccess()) {
			if (getCreditPaymentList.getCreditPaymentList() != null&& getCreditPaymentList.getCreditPaymentList().size() > 0) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if(restartingKey.equals("") || restartingKey == null){
							if (allCreditpaymentAdapter != null) {
								allCreditpaymentAdapter.list.clear();
							}
						}
						isLoadMore = getCreditPaymentList.isMoreValues();
						setRestartingKey(getCreditPaymentList.getRestartingKey());
						setCreditPaymentRecord(getCreditPaymentList.getCreditPaymentList());
						if (getCreditPaymentList.isMoreValues()) {
							allCreditPaymentListVIew.setOnLoadListener(mLoadMoreListener);
						} else {
							allCreditPaymentListVIew.setOnLoadListener(null);
						}
					}
				});
			}else{
				if(getCreditPaymentList.responsePublicModel.eventManagement.getErrorCode().equals("95001")){
					displayErrorMessage("",mContext.getResources().getString(R.string.receipts_not_founds));
				}else if(getCreditPaymentList.responsePublicModel.eventManagement.getErrorCode() != null && !getCreditPaymentList.responsePublicModel.eventManagement.getErrorCode().equals("")){
					displayErrorMessage("",mContext.getResources().getString(R.string.receipts_other_message));
				}
				mHandler.post(new Runnable() {
					@Override
					public void run() {
//						if (allCreditpaymentAdapter != null) {
							List<CreditPaymentModel> creditPaymentList = new ArrayList<CreditPaymentModel>();
							allCreditpaymentAdapter = new RecentEffectsAdapter(creditPaymentList,mContext);
							allCreditPaymentListVIew.setAdapter(allCreditpaymentAdapter);
							allCreditPaymentListVIew.setOnLoadListener(null);
//						}
					}
				});
			}
		}else {
			if(getCreditPaymentList.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_1) || getCreditPaymentList.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_2)){
				displayErrorMessage(getCreditPaymentList.responsePublicModel.eventManagement.getErrorCode(),getCreditPaymentList.responsePublicModel.eventManagement.getErrorDescription());
			}else {
				displayErrorMessage("",mContext.getResources().getString(R.string.receipts_other_message));
			}
			allCreditPaymentListVIew.onLoadMoreComplete();
		}
	}
	
	@Override
	protected void onBackClick() {
		super.onBackClick();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == back) {
			finish();
		}else if(v==arrow_left){
			viewPaper.setCurrentItem(viewPaper.getCurrentItem()-1, true);
		}else if(v==arrow_right){
			viewPaper.setCurrentItem(viewPaper.getCurrentItem()+1, true);
		}
	}
	
	@Override
	public void onLoadMore() {
		if(isCreditPaymentListView){
			for(int i=0;i<effetti_radio_group.getChildCount();i++){
				RadioButton view = (RadioButton) effetti_radio_group.getChildAt(i);
				if(view.isChecked()){
					if(view == all){
						loadGetCreditPaymentData(isLoadMore,accountsModel.get(viewPagerIndex).getAccountCode(),creditID,insertionDate,"",getRestartingKey());
					}else if(view == pending){
						loadGetCreditPaymentData(isLoadMore,accountsModel.get(viewPagerIndex).getAccountCode(),creditID,insertionDate,IN_ESSERE,getRestartingKey());
					}else if(view == not_payed){
						loadGetCreditPaymentData(isLoadMore,accountsModel.get(viewPagerIndex).getAccountCode(),creditID,insertionDate,INSOLUTO,getRestartingKey());
					}else if(view == payed){
						loadGetCreditPaymentData(isLoadMore,accountsModel.get(viewPagerIndex).getAccountCode(),creditID,insertionDate,PAGATO,getRestartingKey());
					}else if(view == other){
						loadGetCreditPaymentData(isLoadMore,accountsModel.get(viewPagerIndex).getAccountCode(),creditID,insertionDate,ALTRO,getRestartingKey());
					}
				}
			}
		}else {
			loadGetCreditData(isLoadMore, accountsModel.get(viewPagerIndex).getAccountCode(),getRestartingKey());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		AccountsModel account = null;
		if(type == DETAILS){
			account = accountsModel.get(viewPagerIndex);
		}else if(type == EFFETC){
			account = effectsAccountsModel;
		}
		index = index - 1;
		Intent intent = new Intent(this, RecentSlipDetail.class);
		intent.putExtra(ACCOUNT_MODEL,account);
		if(arg0 == listView){
			intent.putExtra("CREDIT_LIST", this.creditList.get(index));
			intent.putExtra("TYPE",DETAILS+"");
		}else if(arg0 == allCreditPaymentListVIew){
			intent.putExtra("CREDIT_PAYMENT_LIST", this.allCreditPaymentList.get(index));
			intent.putExtra("TYPE",EFFETC+"");
		}
		startActivity(intent);
	}
}
