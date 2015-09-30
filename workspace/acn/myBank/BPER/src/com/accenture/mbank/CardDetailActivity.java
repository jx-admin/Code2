package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
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

public class CardDetailActivity extends BaseActivity implements OnCheckedChangeListener, OnLoadMoreListener,OnProgressEvent {
	private ImageButton title_left_btn;
	private BalanceAccountsModel balanceAccountModel;
	private String bankServiceCode;
	private QuickReturnListView card_list_view;
	private ViewGroup listHeaderView;
	private LinearLayout listHeaderAccountTitle;
	private LinearLayout carddetailsLayout;
	
	private LinearLayout header_view_content;
	private LinearLayout headerView;
	ViewGroup headerAccountTitle;
	AccountInfoTitle headerAccountInfoTitle;
	LinearLayout card_lable_layout;
	RadioButton movement,details;
	TextView owner,card_state,card_number,expiration,plafond;
	TextView owner_value,card_state_value,card_number_value,expiration_value,plafond_value;
	BankImageButton payments;
	AccountsModel accountModel;
	private Handler mHandler;
	private String restartingKey;
	private String restartingDate;
	private Context mContext;
	private RecordAdapter cardsAdapter;
	OnLoadMoreListener mLoadMoreListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cards_detail);
		this.mContext = this;
		mLoadMoreListener = this;
		
		mHandler = new Handler();
		accountModel = (AccountsModel) getIntent().getSerializableExtra("ACCOUNT_MODEL");
		balanceAccountModel = (BalanceAccountsModel) getIntent().getSerializableExtra("BALANCE_ACCOUNT");
		bankServiceCode = getIntent().getStringExtra("BANK_SERVICE_CODE");
		findViewById(R.id.back).setVisibility(View.GONE);
		title_left_btn = (ImageButton) findViewById(R.id.back);
		title_left_btn.setOnClickListener(this);
		title_left_btn.setVisibility(View.VISIBLE);
		initUI();

	}
	
	private void initUI() {
		LayoutInflater inflater = LayoutInflater.from(this);
		card_list_view = (QuickReturnListView) findViewById(R.id.cards_list_view);
		listHeaderView = (ViewGroup) inflater.inflate(R.layout.cards_header_layout, null);
		listHeaderAccountTitle = (LinearLayout) listHeaderView.findViewById(R.id.account_title);
		AccountInfoTitle listHeaderAccountInfoTitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title, null);
		listHeaderAccountInfoTitle.init();
		listHeaderAccountTitle.addView(listHeaderAccountInfoTitle);
		listHeaderView.setVisibility(View.INVISIBLE);
		card_list_view.addHeaderView(listHeaderView);
		
		carddetailsLayout = (LinearLayout) findViewById(R.id.cards_detail_record);
		
		header_view_content = (LinearLayout) findViewById(R.id.header_view_content);
		headerView = (LinearLayout) header_view_content.findViewById(R.id.header_view);
		headerAccountTitle = (ViewGroup) headerView.findViewById(R.id.account_title);
		headerAccountInfoTitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title, null);
		if(bankServiceCode.equals(Contants.PREPAID_CARD_CODE) || bankServiceCode.equals(Contants.PREPAID_CARD_CODE_1)){
			headerAccountInfoTitle.setShowType(headerAccountInfoTitle.PREPAID_CARD_USER);
		}
		headerAccountInfoTitle.init();
		if(accountModel.getIsPreferred()){
			headerAccountInfoTitle.setPerferredStar(headerAccountInfoTitle.CARD);
		}
		headerAccountTitle.addView(headerAccountInfoTitle);
		
		card_list_view.setHeaderView(headerView);
		
		card_lable_layout = (LinearLayout) headerView.findViewById(R.id.card_lable);
		movement = (RadioButton) headerView.findViewById(R.id.movement);
		details = (RadioButton) headerView.findViewById(R.id.details);
		movement.setOnCheckedChangeListener(this);
		details.setOnCheckedChangeListener(this);
		
		owner = (TextView) findViewById(R.id.owner);
		owner_value = (TextView) findViewById(R.id.owner_value);
		card_state_value = (TextView) findViewById(R.id.card_state_value);
		card_state = (TextView)findViewById(R.id.card_state);
		card_number_value = (TextView) findViewById(R.id.card_number_value);
		card_number =(TextView) findViewById(R.id.card_number);
		expiration_value = (TextView) findViewById(R.id.expiration_value);
		expiration = (TextView)findViewById(R.id.expiration);
		plafond_value = (TextView) findViewById(R.id.plafond_value);
		plafond =(TextView) findViewById(R.id.plafond);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateUI();
	}
	
	private void updateUI(){
		updateCardUI();
		updateCardDetails();
		loadData(getRestartingKey(),false);
	}
	
	private void updateCardUI() {
		if (bankServiceCode == null || bankServiceCode.equals(""))
			return;
		headerAccountInfoTitle.icon.setImageResource(R.drawable.top_icons_cards);
		
		headerAccountInfoTitle.accountType.setTextColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.accountName.setTextColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.accountName.setText(accountModel.getAccountAlias());
		headerAccountInfoTitle.account_balance_name.setText(R.string.available_account);
		Double availableBanlance = balanceAccountModel !=null ? balanceAccountModel.getAvailableBalance() : 0;
		
		if(bankServiceCode.equals(Contants.CREDIT_CARD_CODE)){
			headerAccountInfoTitle.accountType.setText(getResources().getString(R.string.credit_cards));
			headerAccountInfoTitle.user_view.setBackgroundColor(getResources().getColor(R.color.card_user));
			headerAccountInfoTitle.available_balance_name.setText(R.string.plafond_card);
			String plafond = Utils.notPlusGenerateFormatMoney(getResources().getString(R.string.eur), accountModel.getPlafond());
			headerAccountInfoTitle.available_balance_value.setText(plafond);
		}else if(bankServiceCode.equals(Contants.PREPAID_CARD_CODE) || bankServiceCode.equals(Contants.PREPAID_CARD_CODE_1)){
			headerAccountInfoTitle.accountType.setText(getResources().getString(R.string.prepaid_cards_1));
			headerAccountInfoTitle.prepaid_card_view.setBackgroundColor(getResources().getColor(R.color.card_user));
		}
		
		String money = Utils.generateFormatMoney(getResources().getString(R.string.eur), availableBanlance);
		headerAccountInfoTitle.account_balance_value.setText(money);
	}
	
	private void updateCardDetails(){
		if(bankServiceCode.equals(Contants.CREDIT_CARD_CODE)){
			card_number.setText(R.string.card_number2);
			owner_value.setText(accountModel.getCardHolder());
			card_number_value.setText(accountModel.getCardNumber());
			card_state_value.setText(accountModel.getCardState());
			String expireationDate = TimeUtil.changeFormattrString(accountModel.getExpirationDate(), TimeUtil.dateFormat2,TimeUtil.dateFormat_mm_yy);
			expiration_value.setText(expireationDate);
			Double plafondValue = accountModel.getPlafond();
			String plafondStr = Utils.notPlusGenerateFormatMoney(getResources().getString(R.string.eur),plafondValue);
			plafond_value.setText(plafondStr);
		}else if(bankServiceCode.equals(Contants.PREPAID_CARD_CODE) || bankServiceCode.equals(Contants.PREPAID_CARD_CODE_1)){
			owner.setText(R.string.onwer);
			owner_value.setText(accountModel.getCardHolder());
			card_number.setText(R.string.detail_card_number);
			card_number_value.setText(accountModel.getCardNumber());
			card_state.setText(R.string.state);
			card_state_value.setText(accountModel.getCardState());
			expiration.setText(R.string.expiration);
			String expireationDate = TimeUtil.changeFormattrString(accountModel.getExpirationDate(), TimeUtil.dateFormat2,TimeUtil.dateFormat_mm_yy);
			expiration_value.setText(expireationDate);
			plafond.setVisibility(View.GONE);
			plafond_value.setVisibility(View.GONE);
		}
	}
	
	private void showType(int type) {
		  switch (type) {
			  case 0:
				  carddetailsLayout.setVisibility(View.INVISIBLE);
				  card_list_view.setVisibility(View.VISIBLE);
				  card_lable_layout.setVisibility(View.VISIBLE);
			   break;
			  case 1:
				  carddetailsLayout.setVisibility(View.VISIBLE);
				  card_list_view.setVisibility(View.INVISIBLE);
				  card_lable_layout.setVisibility(View.INVISIBLE);
				  mHandler.post(new Runnable() {
					@Override
					public void run() {
						card_list_view.translationY(0);
						card_list_view.setAdapter(cardsAdapter);
					}
				});
			   break;
			  default:
			   break;
		  }
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int type = 0;
		if (isChecked) {
			if (buttonView == movement) {
				type = 0;
			} else if (buttonView == details) {
				type = 1;
			}
			if(cardsAdapter !=null){
				cardsAdapter.notifyDataSetChanged();				
			}
			showType(type);
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
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == back){
			finish();
		}
	}
	
	private void loadData(final String restartingKey, boolean isLoadmore) {
		ProgressOverlay progressOverlay = new ProgressOverlay(this);
		if(isLoadmore){
			progressOverlay.runBackground(this);
		}else{
			progressOverlay.show("",this);
		}
	}
	List<MovementsModel> cardList;
	public void setRecords(List<MovementsModel> list) {
		this.cardList = list;
		
		if (cardsAdapter == null) {
			if (MainActivity.setting.getOrderListFor()==SettingModel.SORT_AMOUNT_DESC) {
				Collections.sort(cardList, new AmountComparator());
			}
			cardsAdapter = new RecordAdapter(cardList, this);
			card_list_view.setAdapter(cardsAdapter);
		} else {
			cardsAdapter.list.addAll(cardList);
			if (MainActivity.setting.getOrderListFor()==SettingModel.SORT_AMOUNT_DESC) {
				Collections.sort(cardsAdapter.list, new AmountComparator());
			}
			cardsAdapter.notifyDataSetChanged();
			card_list_view.onLoadMoreComplete();
		}
	}
	
	public static class RecordAdapter extends BaseAdapter {

		public List<MovementsModel> list;

		Context context;
		
		public RecordAdapter(List<MovementsModel> list, Context context) {
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
				DateDescriptionAmountItem item = (DateDescriptionAmountItem) inflater.inflate(R.layout.date_desc_amount_item, null);
				item.init();
				item.hideCurrencyDate();
				item.toggleButton.setBackgroundResource(R.drawable.cards_expand_selector);
				convertView = item;
			}
			DateDescriptionAmountItem dateDescriptionAmountItem = (DateDescriptionAmountItem) convertView;
			dateDescriptionAmountItem.setType(TYPE.CARDS);
			dateDescriptionAmountItem.setModel(list.get(position));
			return convertView;
		}
	}

	@Override
	public void onLoadMore() {
		loadData(getRestartingKey(),true);
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
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					setRestartingKey(getMovements.responsePublicModel.getRestartingKey());
					setRestartingDate(getMovements.responsePublicModel.getRestartingDate());
					if (transactionBy == SettingModel.LAST_2_MONTH) {
						if (cardsAdapter != null) {
							cardsAdapter.list.clear();
						}
					}
					setRecords(getMovements.getMovements());
					if(getMovements.getMoreValues().equalsIgnoreCase("true")) {
						card_list_view.setOnLoadListener(mLoadMoreListener);
					}else {
						card_list_view.setOnLoadListener(null);
					}
				}
			});
		}else {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					List<MovementsModel> list = new ArrayList<MovementsModel>();
					setRecords(list);
					card_list_view.setMissData(true);
					card_list_view.onLoadMoreComplete();
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
