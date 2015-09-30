package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.accenture.mbank.AccountDetailActivity.RecordAdapter.TYPE;
import com.accenture.mbank.logic.GetMovementsJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.GetMovementsModel;
import com.accenture.mbank.model.MovementsModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.AmountComparator;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountInfoTitle;
import com.accenture.mbank.view.BankImageButton;
import com.accenture.mbank.view.DateDescriptionAmountItem;
import com.accenture.mbank.view.QuickReturnListView;
import com.accenture.mbank.view.QuickReturnListView.OnLoadMoreListener;

public class AccountDetailActivity extends BaseActivity implements OnCheckedChangeListener,OnLoadMoreListener ,OnProgressEvent{
	private static final int ALL = 0;
	private static final int DEPOST = 1;
	private static final int WITH = 2;
	private static final int MOVEMENTS = 3;
	private static final int DETAILS = 4;

	public BalanceAccountsModel balanceAccountModel;
	private ImageButton title_left_btn;

	LinearLayout account_movement_radio_layout;
	LinearLayout account_Detail_Layout;
	LinearLayout card_Detail_Layout;

	List<MovementsModel> allList;

	List<MovementsModel> depostList;

	List<MovementsModel> widthList;

	RecordAdapter allRecordAdapter;

	RecordAdapter depositrecordAdapter;

	RecordAdapter withrecordAdapter;

	RadioButton all, deposit, withdrawals, movementRadioButton,
			detailRadioButton;

	QuickReturnListView allRecordListView, depositRecordListView,
			widthRecordListView;
	Handler handler;
	BankImageButton payments;
	Context mContext;
	String restartingKey;
	String restartingDate;
	View account_content;
	TextView details_account_type, details_overdraftGranted,
			details_overdraftUsed, details_overdraftDiscounted,
			details_totalPresentations;

	AccountsModel accountModel;

	private String bankServiceCode = "";

	private ViewGroup listHeaderView;
	private LinearLayout listHeaderAccountTitle;
	private AccountInfoTitle listHeaderAccountInfoTitle;

	private LinearLayout header_view_content;
	private LinearLayout headerView;
	private ViewGroup headerAccountTitle;
	private AccountInfoTitle headerAccountInfoTitle;

	private LinearLayout account_detail_record;
	private LinearLayout listView_content;
	
	OnLoadMoreListener mLoadMoreListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accounts_detail);
		accountModel = (AccountsModel) getIntent().getSerializableExtra("ACCOUNT_MODEL");
		balanceAccountModel = (BalanceAccountsModel) getIntent().getSerializableExtra("BALANCE_ACCOUNT");
		bankServiceCode = getIntent().getStringExtra("BANK_SERVICE_CODE");
		mLoadMoreListener = this;
		this.mContext = this;
		handler = new Handler();

		findViewById(R.id.back).setVisibility(View.GONE);
		title_left_btn = (ImageButton) findViewById(R.id.back);
		title_left_btn.setOnClickListener(this);
		title_left_btn.setVisibility(View.VISIBLE);
		initUI();
	}

	@Override
	protected void onStart() {
		super.onStart();
		updateUI();
	}

	private void addListHeader(LayoutInflater inflater,
			QuickReturnListView listView, int visibility) {
		listHeaderView = (ViewGroup) inflater.inflate(R.layout.account_header_layout, null);
		listHeaderAccountTitle = (LinearLayout) listHeaderView.findViewById(R.id.account_title);
		listHeaderAccountInfoTitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title, null);
		listHeaderAccountInfoTitle.init();
		listHeaderAccountTitle.addView(listHeaderAccountInfoTitle);
		listHeaderView.setVisibility(visibility);
		listView.addHeaderView(listHeaderView);
	}

	private void initUI() {
		LayoutInflater inflater = LayoutInflater.from(this);

		listView_content = (LinearLayout) findViewById(R.id.listView_content);
		//
		allRecordListView = (QuickReturnListView) findViewById(R.id.account_all_record_list);
		addListHeader(inflater, allRecordListView, View.INVISIBLE);
		depositRecordListView = (QuickReturnListView) findViewById(R.id.account_deposit_record_list);
		addListHeader(inflater, depositRecordListView, View.INVISIBLE);
		
		widthRecordListView = (QuickReturnListView) findViewById(R.id.account_with_record_list);
		addListHeader(inflater, widthRecordListView, View.INVISIBLE);

		header_view_content = (LinearLayout) findViewById(R.id.header_view_content);
		headerView = (LinearLayout) header_view_content.findViewById(R.id.header_view);
		headerAccountTitle = (ViewGroup) headerView.findViewById(R.id.account_title);
		headerAccountInfoTitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title, null);
		if(balanceAccountModel.getDipiuBalance() > 0){
			headerAccountInfoTitle.setShowType(headerAccountInfoTitle.SMALL_BUSINESS_USER);
		}
		headerAccountInfoTitle.init();
		headerAccountTitle.addView(headerAccountInfoTitle);

		// load radiobutton
		movementRadioButton = (RadioButton) headerView.findViewById(R.id.movements);
		detailRadioButton = (RadioButton) headerView.findViewById(R.id.details);
		all = (RadioButton) headerView.findViewById(R.id.all);
		deposit = (RadioButton) headerView.findViewById(R.id.deposit);
		withdrawals = (RadioButton) headerView.findViewById(R.id.withdraoals);
		movementRadioButton.setOnCheckedChangeListener(this);
		detailRadioButton.setOnCheckedChangeListener(this);
		withdrawals.setOnCheckedChangeListener(this);
		deposit.setOnCheckedChangeListener(this);
		all.setOnCheckedChangeListener(this);

		account_detail_record = (LinearLayout) findViewById(R.id.account_detail_record);
		account_Detail_Layout = (LinearLayout) findViewById(R.id.account_details);
		card_Detail_Layout = (LinearLayout) findViewById(R.id.cards_details_display);

		account_movement_radio_layout = (LinearLayout) headerView.findViewById(R.id.account_movement);
		payments = (BankImageButton) headerView.findViewById(R.id.account_payments);
		/*// 根据banlanceAccount中的accountCode去所有的account中查找匹配的account的其它数据
		for (int i = 0; i < Contants.baseAccounts.size(); i++) {
			if (balanceAccountModel.getAccountCode().equals(Contants.baseAccounts.get(i).getAccountCode())) {
				accountModel = Contants.baseAccounts.get(i);
			}
		}*/
		if (accountModel != null && accountModel.getIsInformative().equals("Y")) {
			payments.setImageResource(R.drawable.account_btn_dis_payments);
			payments.setOnClickListener(null);
		} else {
			payments.setImageResource(R.drawable.account_payments_selector);
			payments.setOnClickListener(this);
		}

		/*
		 * In case of Credit card or Prepaid card, NOT show button ALL,
		 * WITHDRAW, DEPOSIT Show them when Account or IBAN card
		 */
		if (bankServiceCode != null && (bankServiceCode.equals(Contants.PREPAID_CARD_CODE) || bankServiceCode.equals(Contants.CREDIT_CARD_CODE))) {
			headerView.findViewById(R.id.account_movement_buttons).setVisibility(View.GONE);
		}

		details_account_type = (TextView) findViewById(R.id.details_account_type);
		details_overdraftGranted = (TextView) findViewById(R.id.details_overdraftGranted);
		details_overdraftUsed = (TextView) findViewById(R.id.details_overdraftUsed);
		small_business_detail = (LinearLayout)findViewById(R.id.small_business_detail);
		details_overdraftDiscounted = (TextView) findViewById(R.id.details_overdraftDiscounted);
		details_totalPresentations = (TextView) findViewById(R.id.details_totalPresentations);
		
		allRecordListView.setHeaderView(headerView);
		depositRecordListView.setHeaderView(headerView);
		widthRecordListView.setHeaderView(headerView);
	}
	private LinearLayout small_business_detail;
	private void updateUI() {
		headerAccountInfoTitle.icon.setImageResource(R.drawable.top_icons_account);
		headerAccountInfoTitle.accountType.setText(getResources().getString(R.string.account2));
		headerAccountInfoTitle.accountName.setText(accountModel.getAccountAlias());
		headerAccountInfoTitle.account_balance_name.setText(R.string.balance_account);
		headerAccountInfoTitle.available_balance_name.setText(R.string.available_account);
		
		double accountBalance = balanceAccountModel != null ? balanceAccountModel.getAccountBalance() : 0;
		String money = Utils.generateFormatMoney(accountBalance);
		headerAccountInfoTitle.account_balance_value.setText(money);

		double availableBanlance =  balanceAccountModel != null ? balanceAccountModel.getAvailableBalance() : 0;
		money = Utils.generateFormatMoney(availableBanlance);
		headerAccountInfoTitle.available_balance_value.setText(money);
		
		if(balanceAccountModel.getDipiuBalance() > 0){
			headerAccountInfoTitle.dipiu_balance_name.setText(R.string.dipiu_account);
			String dipiubalance = Utils.generateFormatMoney(balanceAccountModel.getDipiuBalance());
			headerAccountInfoTitle.dipiu_balance_value.setText(dipiubalance);
		}
		
		if(accountModel.getIsPreferred()){
			headerAccountInfoTitle.setPerferredStar(headerAccountInfoTitle.ACCOUNT);
		}
		details_account_type.setText(balanceAccountModel.getAccountType());
		details_overdraftGranted.setText(Utils.generateFormatMoney(balanceAccountModel.getOverdraftGranted()));
		details_overdraftUsed.setText(Utils.generateFormatMoney(balanceAccountModel.getOverdraftUsed()));
		
		if(Contants.getUserInfo.getUserprofileHb().getTypeSB().equals("SB")){
			details_overdraftDiscounted.setText(Utils.generateFormatMoney(balanceAccountModel.getOverdraftDiscounted()));
			details_totalPresentations.setText(Utils.generateFormatMoney(balanceAccountModel.getTotalPresentations()));
			small_business_detail.setVisibility(View.VISIBLE);
		}

		updateCardUI();
		updateCardDetails(balanceAccountModel);
		
		if (this.allList == null || this.allList.size() <= 0) {
			loadData(false);
		} else {
			return;
		}
	}

	private void updateCardUI() {
		if (bankServiceCode == null || bankServiceCode.equals(""))
			return;
		headerAccountInfoTitle.icon.setImageResource(R.drawable.top_icons_cards);
		headerAccountInfoTitle.accountType.setText(getResources().getString(R.string.carta_iban));
		detailRadioButton.setText(getResources().getString(R.string.card_details_btn));
		headerAccountInfoTitle.user_view.setBackgroundColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.sb_user_view.setBackgroundColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.accountType.setTextColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.accountName.setTextColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.account_balance_name.setText(getResources().getString(R.string.available_account));
		headerAccountInfoTitle.available_balance_name.setText(getResources().getString(R.string.balance_account));
		
		Double availableBanlance = balanceAccountModel !=null ? balanceAccountModel.getAvailableBalance() : 0;
		String money = Utils.generateFormatMoney(getResources().getString(R.string.eur), availableBanlance);
		headerAccountInfoTitle.account_balance_value.setText(money);
		
		Double accountBalance = balanceAccountModel !=null ? balanceAccountModel.getAccountBalance() : 0;
		money = Utils.generateFormatMoney(getResources().getString(R.string.eur), accountBalance);
		headerAccountInfoTitle.available_balance_value.setText(money);
		
		if(accountModel.getIsPreferred()){
			headerAccountInfoTitle.setPerferredStar(headerAccountInfoTitle.CARD);
		}
		/*
		 * IBAN Card payments button, pink color
		 */
		if (accountModel != null && accountModel.getIsInformative().equals("Y")) {
			payments.setImageResource(R.drawable.card_btn_dis_payments);
			payments.setOnClickListener(null);
		} else {
			payments.setImageResource(R.drawable.card_btn_payments_selector);
			payments.setOnClickListener(this);
		}
	}

	private void updateCardDetails(BalanceAccountsModel balanceAccount) {
		// Cards Details
		TextView onwer_value = (TextView) findViewById(R.id.onwer_value);
		TextView state_value = (TextView) findViewById(R.id.state_value);
		TextView iban_code_value = (TextView) findViewById(R.id.iban_code_value);
		TextView pan_card_number_value = (TextView) findViewById(R.id.pan_card_number_value);
		TextView expiration_value = (TextView) findViewById(R.id.expiration_value);

		onwer_value.setText(accountModel.getCardHolder());
		state_value.setText(accountModel.getCardState());
		iban_code_value.setText(accountModel.getIbanCode());
		pan_card_number_value.setText(accountModel.getCardNumber());
		String expireationDate = TimeUtil.changeFormattrString(accountModel.getExpirationDate(), TimeUtil.dateFormat2,TimeUtil.dateFormat_mm_yy);
		expiration_value.setText(expireationDate);
	}

	public void setRecords(List<MovementsModel> list) {
		this.allList = list;
		TYPE type;
		if (bankServiceCode == null || bankServiceCode.equals("")) {
			type = RecordAdapter.TYPE.ACCOUNTS;
		} else {
			type = RecordAdapter.TYPE.CARDS;
		}
		generateList();
		if (allRecordAdapter == null) {
			if (MainActivity.setting.getOrderListFor()==SettingModel.SORT_AMOUNT_DESC) {
				Collections.sort(allList, new AmountComparator());
			}
			allRecordAdapter = new RecordAdapter(allList, this,type);
			allRecordListView.setAdapter(allRecordAdapter);
		} else {
			allRecordAdapter.list.addAll(allList);
			if (MainActivity.setting.getOrderListFor()==SettingModel.SORT_AMOUNT_DESC) {
				Collections.sort(allRecordAdapter.list, new AmountComparator());
			}
			allRecordAdapter.notifyDataSetChanged();
			allRecordListView.onLoadMoreComplete();
		}
		if (depositrecordAdapter == null) {
			depositrecordAdapter = new RecordAdapter(depostList, this,type);
			depositRecordListView.setAdapter(depositrecordAdapter);
		} else {
			depositrecordAdapter.list.addAll(depostList);
			depositrecordAdapter.notifyDataSetChanged();
			depositRecordListView.onLoadMoreComplete();
		}
		if (withrecordAdapter == null) {
			withrecordAdapter = new RecordAdapter(widthList, this,type);
			widthRecordListView.setAdapter(withrecordAdapter);
		} else {
			withrecordAdapter.list.addAll(widthList);
			withrecordAdapter.notifyDataSetChanged();
			widthRecordListView.onLoadMoreComplete();
		}
	}

	private void loadData(boolean isLoadmore) {
		ProgressOverlay progressOverlay = new ProgressOverlay(this);
		if(isLoadmore){
			progressOverlay.runBackground(this);
		}else {
			progressOverlay.show("",this);
		}
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if (buttonView == all) {
				showRecordsType(ALL);
			} else if (buttonView == deposit) {
				showRecordsType(DEPOST);
			} else if (buttonView == withdrawals) {
				showRecordsType(WITH);
			} else if (buttonView == movementRadioButton) {
				showType(MOVEMENTS);
			} else if (buttonView == detailRadioButton) {
				showType(DETAILS);
			}
		}
		
		handler.post(new Runnable() {
			@Override
			public void run() {
				allRecordListView.translationY(0);
				depositRecordListView.translationY(0);
				widthRecordListView.translationY(0);
				
				allRecordListView.setAdapter(allRecordAdapter);
				depositRecordListView.setAdapter(depositrecordAdapter);
				widthRecordListView.setAdapter(withrecordAdapter);
			}
		});
		
		if(allRecordAdapter !=null){
			allRecordAdapter.notifyDataSetChanged();
		}
		if(depositrecordAdapter!=null){
			depositrecordAdapter.notifyDataSetChanged();
		}
		if(withrecordAdapter!=null){
			withrecordAdapter.notifyDataSetChanged();
		}
	}

	private void showType(int type) {
		switch (type) {
		case MOVEMENTS:
			account_Detail_Layout.setVisibility(View.GONE);
			card_Detail_Layout.setVisibility(View.GONE);
			account_detail_record.setVisibility(View.INVISIBLE);
			account_movement_radio_layout.setVisibility(View.VISIBLE);
			listView_content.setVisibility(View.VISIBLE);
			break;
		case DETAILS:
			if (bankServiceCode == null || bankServiceCode.equals("")) {
				account_Detail_Layout.setVisibility(View.VISIBLE);
				card_Detail_Layout.setVisibility(View.GONE);
			} else {
				card_Detail_Layout.setVisibility(View.VISIBLE);
				account_Detail_Layout.setVisibility(View.GONE);
			}
			account_detail_record.setVisibility(View.VISIBLE);
			account_movement_radio_layout.setVisibility(View.GONE);
			listView_content.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (payments == v) {
			recover();
		} else if (v == back) {
			finish();
		}
	}

	@Override
	protected void onBackClick() {
		super.onBackClick();
	}

	private void recover() {
		Intent intent = new Intent(this, BPERPaymentMenu.class);
//		overridePendingTransition(R.anim.slide_in_right_to_left, 0);
		startActivity(intent);
		
	}

	/**
	 * 0:all<br>
	 * 1:deposit<br>
	 * 2:withdrawals<br>
	 * 
	 * @param type
	 */
	private void showRecordsType(int type) {
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

	private void hideRecords() {
		allRecordListView.setVisibility(View.GONE);
		depositRecordListView.setVisibility(View.GONE);
		widthRecordListView.setVisibility(View.GONE);
	}

	public static class RecordAdapter extends BaseAdapter {

		public List<MovementsModel> list;

		Context context;

		public static enum TYPE {
			ACCOUNTS, CARDS
		};

		TYPE type;

		public RecordAdapter(List<MovementsModel> list, Context context,
				TYPE type) {
			this.list = list;
			this.context = context;
			this.type = type;

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
				DateDescriptionAmountItem item = (DateDescriptionAmountItem) inflater.inflate(R.layout.date_desc_amount_item, null);
				item.init();
				if (TYPE.ACCOUNTS == type) {
					item.toggleButton.setBackgroundResource(R.drawable.accounts_expand_selector);
				} else if (TYPE.CARDS == type) {
					item.toggleButton.setBackgroundResource(R.drawable.cards_expand_selector);
				}
				convertView = item;
			}
			DateDescriptionAmountItem dateDescriptionAmountItem = (DateDescriptionAmountItem) convertView;
			dateDescriptionAmountItem.setType(type);
			dateDescriptionAmountItem.setModel(list.get(position));

			return convertView;
		}

	}

	@Override
	public void onLoadMore() {
		loadData(true);
	}
	
	@Override
	public void onProgress() {
		final int transactionBy = MainActivity.setting.getShowTransactionBy();
		String paymentMethod;
		if (bankServiceCode == null || bankServiceCode.equals("")) {
			paymentMethod = "2";
		} else {
			paymentMethod = "1";
		}
		String postData = GetMovementsJson.getMovementsReportProtocal(Contants.publicModel, paymentMethod,accountModel.getAccountCode(),MainActivity.setting.getOrderListFor(), transactionBy,restartingKey,restartingDate);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, mContext);
		final GetMovementsModel getMovements = GetMovementsJson.parseGetMovementsResponse(httpResult);
		if (getMovements.getMovements() != null && getMovements.getMovements().size() > 0){
			handler.post(new Runnable() {
				@Override
				public void run() {
					setRestartingKey(getMovements.responsePublicModel.getRestartingKey());
					setRestartingDate(getMovements.responsePublicModel.getRestartingDate());
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
					if(getMovements.getMoreValues().equalsIgnoreCase("true")) {
						allRecordListView.setOnLoadListener(mLoadMoreListener);
						depositRecordListView.setOnLoadListener(mLoadMoreListener);
						widthRecordListView.setOnLoadListener(mLoadMoreListener);
					}else {
						allRecordListView.setOnLoadListener(null);
						depositRecordListView.setOnLoadListener(null);
						widthRecordListView.setOnLoadListener(null);
					}
				}
			});
		}else{
			handler.post(new Runnable() {
				@Override
				public void run() {
					List<MovementsModel> list = new ArrayList<MovementsModel>();
					setRecords(list);
					allRecordListView.setMissData(true);
					depositRecordListView.setMissData(true);
					widthRecordListView.setMissData(true);
				}
			});
		}
	}

	public String getRestartingDate() {
		return restartingDate;
	}

	public void setRestartingDate(String restartingDate) {
		this.restartingDate = restartingDate;
	}
}
