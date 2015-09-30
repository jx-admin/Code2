package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AdditionalCard;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountInfoTitle;
import com.accenture.mbank.view.CordorateCardChildLayout;
import com.accenture.mbank.view.EnhanceItemExpander;
import com.accenture.mbank.view.QuickReturnListView;
import com.accenture.mbank.view.QuickReturnListView.OnLoadMoreListener;

public class CorporateCardsActivityOld extends BaseActivity implements OnLoadMoreListener{
	private QuickReturnListView card_list_view;
	private LinearLayout headerView;
	AccountInfoTitle headerAccountInfoTitle;
	Handler mHandler;
	Context mContext;
	private OnLoadMoreListener mLoadMoreListener;
	public BalanceAccountsModel balanceAccountModel;
	String bankServiceCode;
	private ImageButton title_left_btn;
	private String restartingKey;
	
	AccountsModel card;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.corporate_card_detail);
		this.mContext = this;
		mLoadMoreListener = this;
		mHandler = new Handler();
		card = (AccountsModel)getIntent().getSerializableExtra("ACCOUNT_MODEL");
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
		AccountInfoTitle listHeaderAccountInfoTitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title, null);
		listHeaderAccountInfoTitle.init();
		listHeaderAccountInfoTitle.setVisibility(View.INVISIBLE);
		card_list_view.addHeaderView(listHeaderAccountInfoTitle);

//		headerView = (LinearLayout) findViewById(R.id.headerView);
		headerAccountInfoTitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title, null);
		headerAccountInfoTitle.init();
		headerView.addView(headerAccountInfoTitle);
		card_list_view.setHeaderView(headerView);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateUI();
	}
	
	public void updateUI(){
		if (bankServiceCode == null || bankServiceCode.equals(""))
			return;
		headerAccountInfoTitle.icon.setImageResource(R.drawable.top_icons_cards);
		headerAccountInfoTitle.accountType.setText(getResources().getString(R.string.corporate_card));
		headerAccountInfoTitle.user_view.setBackgroundColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.sb_user_view.setBackgroundColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.accountType.setTextColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.accountName.setTextColor(getResources().getColor(R.color.card_user));
		if(card.getCardRelations().equals("P")){
			headerAccountInfoTitle.setRelations(true);
		}
		if(card.getIsPreferred()){
			headerAccountInfoTitle.setPerferredStar(headerAccountInfoTitle.CARD);
		}
		Double availableBanlance = balanceAccountModel !=null ? balanceAccountModel.getAvailableBalance() : 0;
		String money = Utils.generateFormatMoney(getResources().getString(R.string.eur), availableBanlance);
		headerAccountInfoTitle.accountName.setText(card.getAccountAlias());
		headerAccountInfoTitle.account_balance_name.setText(R.string.available_balance);
		headerAccountInfoTitle.account_balance_value.setText(money);
		
		headerAccountInfoTitle.available_balance_name.setText(R.string.plafond_card);
		String plafond = Utils.generateFormatMoney(getResources().getString(R.string.eur), card.getPlafond());
		headerAccountInfoTitle.available_balance_value.setText(plafond);
		setRecords(balanceAccountModel.getAdditionalCardsList());
	}
	
	private CorPorateCardListAdapter cardsAdapter;
	
	public void setRecords(List<AdditionalCard> additionalCardsList) {
		if (cardsAdapter == null) {
			cardsAdapter = new CorPorateCardListAdapter(additionalCardsList, this);
			cardsAdapter.setAccountsModel(card);
			card_list_view.setAdapter(cardsAdapter);
		} else {
			cardsAdapter.list.addAll(additionalCardsList);
			cardsAdapter.notifyDataSetChanged();
			card_list_view.onLoadMoreComplete();
		}
	}
	
	
	public static class CorPorateCardListAdapter extends BaseAdapter {

		private List<AdditionalCard> list;

		private AccountsModel card;
		Context context;

		public CorPorateCardListAdapter(List<AdditionalCard> additionalCardsList,Context context) {
			this.list = additionalCardsList;
			this.context = context;
		}

		@Override
		public int getCount() {
			return list.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position + 1);
		}

		@Override
		public long getItemId(int position) {
			return position + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(context);
			if (convertView == null) {
				if(position == 0){
					convertView = inflater.inflate(R.layout.corporate_card_detail_info, null);
					TextView owner_value = (TextView)convertView.findViewById(R.id.owner_value);
					TextView card_state_value = (TextView)convertView.findViewById(R.id.card_state_value);
					TextView card_number_value = (TextView)convertView.findViewById(R.id.card_number_value);
					TextView expiration_value = (TextView)convertView.findViewById(R.id.expiration_value);
					TextView plafond_value = (TextView)convertView.findViewById(R.id.plafond_value);
					
					owner_value.setText(card.getCardHolder());
					card_state_value.setText(card.getCardState());
					card_number_value.setText(card.getCardNumber());
					String expireationDate = TimeUtil.changeFormattrString(card.getExpirationDate(), TimeUtil.dateFormat2,TimeUtil.dateFormat_mm_yy);
					expiration_value.setText(!expireationDate.equals("")? expireationDate :"02.14");
					Double plafondValue = card.getPlafond();
					String plafondStr = Utils.generateFormatMoney(context.getResources().getString(R.string.eur),plafondValue);
					plafond_value.setText(plafondStr);
				}else{
					position = position - 1;
					convertView = inflater.inflate(R.layout.corporate_card_item_expander, null);
					EnhanceItemExpander itemExpander = (EnhanceItemExpander) convertView;
					itemExpander.setTitle(list.get(position).getCardName());
					itemExpander.setItem_lable(R.string.available_account);
					String availableBalance = Utils.generateFormatMoney(Contants.COUNTRY, list.get(position).getCardBalance());
					itemExpander.setResult(availableBalance);
					itemExpander.setExpand(false);
					itemExpander.setExpandButtonBackground(R.drawable.cards_expand_selector);
					itemExpander.setTypeface(Typeface.DEFAULT);
					setExpandedContainer(itemExpander,list.get(position));
				}
			}
			return convertView;
		}
		
		public void setExpandedContainer(EnhanceItemExpander itemExpander,AdditionalCard additionalCard) {
			LayoutInflater inflater = LayoutInflater.from(context);
			CordorateCardChildLayout cordorateCardChild =  (CordorateCardChildLayout) inflater.inflate(R.layout.corporate_card_child_list, null);
//			cordorateCardChild.init();
			cordorateCardChild.setAdditionalCard(additionalCard);
			itemExpander.setExpandedContainer(cordorateCardChild);
		}

		public AccountsModel getBalanceAccountModel() {
			return card;
		}

		public void setAccountsModel(AccountsModel card) {
			this.card = card;
		}
	}
	
	@Override
	public void onLoadMore() {
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == back){
			finish();
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
}
