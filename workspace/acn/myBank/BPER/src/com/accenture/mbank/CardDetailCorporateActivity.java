package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AdditionalCard;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.MovementsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountInfoTitle;
import com.accenture.mbank.view.BankImageButton;
import com.accenture.mbank.view.CordorateCardChildLayout;
import com.accenture.mbank.view.CustomScrollView;
import com.accenture.mbank.view.EnhanceItemExpander;

public class CardDetailCorporateActivity extends BaseActivity{
	private ImageButton title_left_btn;
	private BalanceAccountsModel balanceAccountModel;
	private String bankServiceCode;
	private LinearLayout card_list_view;
	private CustomScrollView scrollView;
	
	private LinearLayout headerView;
	ViewGroup headerAccountTitle;
	AccountInfoTitle headerAccountInfoTitle;
	LinearLayout card_lable_layout;
	BankImageButton payments;
	AccountsModel accountModel;
	private String restartingKey;
	private String restartingDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.corporate_card_detail);
		
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
		scrollView = (CustomScrollView)findViewById(R.id.cards_list_view);
		LayoutInflater inflater = LayoutInflater.from(this);
		card_list_view = (LinearLayout) findViewById(R.id.scroolview_content);
		
		headerView = new LinearLayout(this); 
	    headerView.setOrientation(LinearLayout.VERTICAL);
	    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	    headerView.setLayoutParams(layoutParams);
	    headerView.setBackgroundDrawable(this.getResources().getDrawable(R.color.gray));
		headerAccountInfoTitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title, null);
		headerAccountInfoTitle.init();
		headerView.addView(headerAccountInfoTitle);
		card_list_view.addView(headerView);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateUI();
	}
	
	private void updateUI(){
		updateCardUI();
		setRecordsCards(balanceAccountModel.getAdditionalCardsList());
	}
	
	private void updateCardUI() {
		if (bankServiceCode == null || bankServiceCode.equals(""))
			return;
		headerAccountInfoTitle.icon.setImageResource(R.drawable.top_icons_cards);
		headerAccountInfoTitle.accountType.setText(getResources().getString(R.string.corporate_card));
		headerAccountInfoTitle.user_view.setBackgroundColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.sb_user_view.setBackgroundColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.accountType.setTextColor(getResources().getColor(R.color.card_user));
		headerAccountInfoTitle.accountName.setTextColor(getResources().getColor(R.color.card_user));
		if(accountModel.getCardRelations().equals("P")){
			headerAccountInfoTitle.setRelations(true);
		}
		if(accountModel.getIsPreferred()){
			headerAccountInfoTitle.setPerferredStar(headerAccountInfoTitle.CARD);
		}
		Double availableBanlance = balanceAccountModel !=null ? balanceAccountModel.getAvailableBalance() : 0;
		String money = Utils.generateFormatMoney(getResources().getString(R.string.eur), availableBanlance);
		headerAccountInfoTitle.accountName.setText(accountModel.getAccountAlias());
		headerAccountInfoTitle.account_balance_name.setText(R.string.available_balance);
		headerAccountInfoTitle.account_balance_value.setText(money);
		
		headerAccountInfoTitle.available_balance_name.setText(R.string.plafond_card);
		String plafond = Utils.notPlusGenerateFormatMoney(getResources().getString(R.string.eur), accountModel.getPlafond());
		headerAccountInfoTitle.available_balance_value.setText(plafond);
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
	
	public void setRecordsCards(List<AdditionalCard> additionalCardsList) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View convertView = inflater.inflate(R.layout.corporate_card_detail_info, null);
		TextView owner_value = (TextView)convertView.findViewById(R.id.owner_value);
		TextView card_state_value = (TextView)convertView.findViewById(R.id.card_state_value);
		TextView card_number_value = (TextView)convertView.findViewById(R.id.card_number_value);
		TextView plafond_value = (TextView)convertView.findViewById(R.id.plafond_value);
		
		owner_value.setText(accountModel.getCardHolder());
		card_state_value.setText(accountModel.getCardState());
		card_number_value.setText(accountModel.getCardNumber());
		Double plafondValue = accountModel.getPlafond();
		String plafondStr = Utils.notPlusGenerateFormatMoney(this.getResources().getString(R.string.eur),plafondValue);
		plafond_value.setText(plafondStr);
		card_list_view.addView(convertView);
		
		for (AdditionalCard additionalCard : additionalCardsList) {
			convertView = inflater.inflate(R.layout.corporate_card_item_expander, null);
			EnhanceItemExpander itemExpander = (EnhanceItemExpander) convertView;
			itemExpander.setTitle(additionalCard.getCardName());
			itemExpander.setItem_lable(R.string.available_account);
			String availableBalance = Utils.generateFormatMoney(Contants.COUNTRY, additionalCard.getCardBalance());
			itemExpander.setResult(availableBalance);
			itemExpander.setExpand(false);
			itemExpander.setExpandButtonBackground(R.drawable.cards_expand_selector);
			itemExpander.setTypeface(Typeface.DEFAULT);
			setExpandedContainer(itemExpander,additionalCard);
			card_list_view.addView(convertView);
		}
	}
	public void setExpandedContainer(EnhanceItemExpander itemExpander,AdditionalCard additionalCard) {
		LayoutInflater inflater = LayoutInflater.from(this);
		CordorateCardChildLayout cordorateCardChild =  (CordorateCardChildLayout) inflater.inflate(R.layout.corporate_card_child_list, null);
		cordorateCardChild.init(card_list_view,scrollView);
		cordorateCardChild.setAdditionalCard(additionalCard);
		itemExpander.setExpandedContainer(cordorateCardChild);
	}

	
	List<MovementsModel> cardList;

	public String getRestartingDate() {
		return restartingDate;
	}

	public void setRestartingDate(String restartingDate) {
		this.restartingDate = restartingDate;
	}
	

}
