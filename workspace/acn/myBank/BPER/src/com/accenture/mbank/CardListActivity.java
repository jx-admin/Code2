package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.accenture.mbank.logic.BalanceJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.GetBalanceResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.CardsLayoutManager;

public class CardListActivity extends BaseActivity {

	private List<AccountsModel> cardList = new ArrayList<AccountsModel>();
	private List<BalanceAccountsModel> balanceAccountsModel = new ArrayList<BalanceAccountsModel>();
	
	private String bankServiceCode = "";

	public List<BalanceAccountsModel> creditCardsAccounts = new ArrayList<BalanceAccountsModel>();
    
    public List<BalanceAccountsModel> ibanCardsAccounts = new ArrayList<BalanceAccountsModel>();

    public List<BalanceAccountsModel> prepaidCardsAccounts = new ArrayList<BalanceAccountsModel>();
	
    List<BalanceAccountsModel> accounts = new ArrayList<BalanceAccountsModel>();
    
    public static final String CARDS_PAYMENTMETHOD = "1";
    
    Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_list);
		
		mHandler = new Handler();
		loadCards(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		bankServiceCode = getIntent().getStringExtra("BANK_SERVICE_CODE");
		
		TextView card_caption = (TextView)findViewById(R.id.card_caption);
		if(bankServiceCode.equals(Contants.IBAN_CARD_CODE)){
			card_caption.setText(R.string.iban_cards_uppercase);
		}else if(bankServiceCode.equals(Contants.CREDIT_CARD_CODE)){
			card_caption.setText(R.string.credit_cards_uppercase);
		}else if(bankServiceCode.equals(Contants.PREPAID_CARD_CODE)){
			card_caption.setText(R.string.prepaid_cards_uppercase);
		}
		
		prepareCardList();
		showCardList();
	}

	/*
	 * Show the card list, using simple adapter
	 */
	private void showCardList() {
		CardListAdapter simpleAdapter = new CardListAdapter(cardList,balanceAccountsModel, this);
		ListView list = (ListView) findViewById(R.id.cardlist);
		list.setAdapter(simpleAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
				AccountsModel accountModel;
				BalanceAccountsModel balanceAccountModel = null;
				accountModel = cardList.get(position);
				
				for(BalanceAccountsModel _balanceAccountModel : balanceAccountsModel){
					if(_balanceAccountModel.getAccountCode().equals(accountModel.getAccountCode())){
						balanceAccountModel = _balanceAccountModel;
					}
				}
				
				LogManager.d("onItemClick:" + position);
				if(accountModel.getBankServiceType().getBankServiceCode().equals(Contants.IBAN_CARD_CODE)){
					Intent intent = new Intent(CardListActivity.this,AccountDetailActivity.class);
					intent.putExtra("ACCOUNT_MODEL", accountModel);
					intent.putExtra("BALANCE_ACCOUNT", balanceAccountModel);
					intent.putExtra("BANK_SERVICE_CODE", bankServiceCode);
		            overridePendingTransition(R.anim.slide_in_right_to_left, 0);
					startActivity(intent);
				}else{
					if(accountModel.getCardRelations().equals("P")){
						//关系卡
						Intent intent = new Intent(CardListActivity.this,CardDetailCorporateActivity.class);
						intent.putExtra("ACCOUNT_MODEL", accountModel);
						intent.putExtra("BALANCE_ACCOUNT", balanceAccountModel);
						intent.putExtra("BANK_SERVICE_CODE", bankServiceCode);
						startActivity(intent);
			            overridePendingTransition(R.anim.slide_in_right_to_left, 0);
					}else{
						Intent intent = new Intent(CardListActivity.this,CardDetailActivity.class);
						intent.putExtra("ACCOUNT_MODEL", accountModel);
						intent.putExtra("BALANCE_ACCOUNT", balanceAccountModel);
						intent.putExtra("BANK_SERVICE_CODE", bankServiceCode);
						startActivity(intent);
			            overridePendingTransition(R.anim.slide_in_right_to_left, 0);
						return;
					}
				}
			}
		});
	}

	public void setAccounts(List<BalanceAccountsModel> accounts) {
        getCreditCardsAccounts(accounts);
    }
	
	/*
	 * Loads card information, using cardsLayoutManager class
	 */
	private void prepareCardList() {
//		if (creditCardsAccounts.size() == 0 && ibanCardsAccounts.size() == 0 && prepaidCardsAccounts.size() == 0) {
//			loadCards(this);
//		}
		
		if (bankServiceCode.equals(Contants.CREDIT_CARD_CODE)){
			balanceAccountsModel = creditCardsAccounts;
			cardList = Contants.creditCardAccounts;
		}else if (bankServiceCode.equals(Contants.IBAN_CARD_CODE)){
			balanceAccountsModel = ibanCardsAccounts;
			cardList = Contants.ibanCardAccounts;
		}else if (bankServiceCode.equals(Contants.PREPAID_CARD_CODE) || bankServiceCode.equals(Contants.PREPAID_CARD_CODE_1)){
			balanceAccountsModel = prepaidCardsAccounts;
			cardList = Contants.prepaidCardAccounts;
		}
	}

	
	
	/**
     * 提取数据
     * 
     * @param balanceAccounts
     */
    public void getCreditCardsAccounts(List<BalanceAccountsModel> balanceAccounts) {
		/*
		 * First clear the lists, in case of called more than once
		 */
		creditCardsAccounts.clear();
		prepaidCardsAccounts.clear();
		ibanCardsAccounts.clear();

        for (BalanceAccountsModel balanceAccount : balanceAccounts) {
            if (balanceAccount.getBankServiceCode().equals(Contants.CREDIT_CARD_CODE)) {
                creditCardsAccounts.add(balanceAccount);
            } else if (balanceAccount.getBankServiceCode().equals(Contants.PREPAID_CARD_CODE) || balanceAccount.getBankServiceCode().equals(Contants.PREPAID_CARD_CODE_1)) {
                prepaidCardsAccounts.add(balanceAccount);
            } else if(balanceAccount.getBankServiceCode().equals(Contants.IBAN_CARD_CODE)){
            	ibanCardsAccounts.add(balanceAccount);
            }
        }
    }
	
    
    public void loadCards() {
		loadCards(this);
	}

    public void loadCards(final Context aContext) {
        ProgressOverlay progressOverlay = new ProgressOverlay(aContext);
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {
                String postData = BalanceJson.BalanceReportProtocal(CARDS_PAYMENTMETHOD,Contants.publicModel);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,aContext);
                GetBalanceResponseModel getBalanceResponse = BalanceJson.parseGetBalanceResponse(httpResult);
                
                if (getBalanceResponse != null &&
                		getBalanceResponse.getBanlaceAccounts() != null &&
                				getBalanceResponse.responsePublicModel.isSuccess()) {
                	accounts = getBalanceResponse.getBanlaceAccounts();
                } else {
                 	if (httpResult == null) {
						/*
						 *  A connection error happened
						 */
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								final MainActivity mainActivity = (MainActivity)aContext;
								mainActivity.showTab(0);
							}
						});
                	}
                	else if (getBalanceResponse != null &&
							!getBalanceResponse.responsePublicModel.isSuccess() &&
							!getBalanceResponse.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_1) &&
							!getBalanceResponse.responsePublicModel.eventManagement.getErrorCode().equals(Contants.ERR_SESSION_EXPIRED_2)) {
						/*
						 * maybe 90000 happens
						 */
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								displayErrorMessage("90000", getResources().getString(R.string.service_unavailable));
							}
						});
					}
                }
                if (accounts != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            setAccounts(accounts);
                            prepareCardList();
                    		showCardList();
                        }
                    });

                }
            }
        });
    }
	
	public static class CardListAdapter extends BaseAdapter {
		
		private List<AccountsModel> list;
		
		private List<BalanceAccountsModel> balanceAccountsModels;

		Context context;

		public CardListAdapter(List<AccountsModel> list,List<BalanceAccountsModel> balanceAccount, Context context) {
			this.list = list;
			this.context = context;
			this.balanceAccountsModels = balanceAccount;
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
			LayoutInflater inflater = LayoutInflater.from(context);
			String availableBalance = null;
			String accountBalance = null;
			if(list.get(position).getBankServiceType().getBankServiceCode().equals(Contants.IBAN_CARD_CODE)){
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.iban_card_item, null);
					
					TextView card_name = (TextView) convertView.findViewById(R.id.iban_card_name);
					TextView avalable_balance_value = (TextView) convertView.findViewById(R.id.avalable_balance_value);
					TextView account_balance_value = (TextView) convertView.findViewById(R.id.account_balance_value);
					ImageView star_icon = (ImageView) convertView.findViewById(R.id.card_list_special_icon);
					ImageView relations_icon = (ImageView) convertView.findViewById(R.id.card_list_relations_icon);
					card_name.setText(list.get(position).getAccountAlias());
					
					if (list.get(position).getCardRelations().equals("P")) {
						relations_icon.setImageResource(R.drawable.icona_carta_valigia);
						relations_icon.setVisibility(View.VISIBLE);
					}else{
						relations_icon.setVisibility(View.GONE);
					}
					
					if (list.get(position).getIsPreferred()) {
						star_icon.setImageResource(R.drawable.icona_carta_stella);
						star_icon.setVisibility(View.VISIBLE);
					}
					
					if(balanceAccountsModels != null && balanceAccountsModels.size() != 0){
						for(BalanceAccountsModel _balanceAccountModel : balanceAccountsModels){
							if(_balanceAccountModel.getAccountCode().equals(list.get(position).getAccountCode())){
								availableBalance = Utils.generateFormatMoney(Contants.COUNTRY, _balanceAccountModel!=null ? _balanceAccountModel.getAvailableBalance():0);
								accountBalance = Utils.generateFormatMoney(Contants.COUNTRY, _balanceAccountModel!=null ?_balanceAccountModel.getAccountBalance():0);
							}
						}
					}else {
						availableBalance = Utils.generateFormatMoney(Contants.COUNTRY, 0);
						accountBalance = Utils.generateFormatMoney(Contants.COUNTRY, 0);
					}
					
					avalable_balance_value.setText(availableBalance);
					account_balance_value.setText(accountBalance);
				}
			}else {
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.card_list_item, null);
					AccountsModel account = list.get(position);
				
					TextView card_list_cardname = (TextView) convertView.findViewById(R.id.card_list_cardname);
					ImageView star_icon = (ImageView) convertView.findViewById(R.id.card_list_special_icon);
					ImageView relations_icon = (ImageView) convertView.findViewById(R.id.card_list_relations_icon);
					TextView card_list_banlance_title = (TextView) convertView.findViewById(R.id.card_list_banlance_title);
					TextView card_list_banlance_value = (TextView) convertView.findViewById(R.id.card_list_banlance_value);
					card_list_cardname.setText(account.getAccountAlias());
					card_list_banlance_title.setText(context.getResources().getString(R.string.available_account));
					
					if (account.getCardRelations().equals("P")) {
						relations_icon.setImageResource(R.drawable.icona_carta_valigia);
						relations_icon.setVisibility(View.VISIBLE);
					}else{
						relations_icon.setVisibility(View.GONE);
					}
					
					if (account.getIsPreferred()) {
						star_icon.setImageResource(R.drawable.icona_carta_stella);
						star_icon.setVisibility(View.VISIBLE);
					}
					
					if(balanceAccountsModels != null && balanceAccountsModels.size() != 0){
						for(BalanceAccountsModel _balanceAccountModel : balanceAccountsModels){
							if(_balanceAccountModel.getAccountCode().equals(account.getAccountCode())){
								availableBalance = Utils.generateFormatMoney(Contants.COUNTRY, _balanceAccountModel!=null ?_balanceAccountModel.getAvailableBalance():0);
							}
						}
					}else {
						availableBalance = Utils.generateFormatMoney(Contants.COUNTRY, 0);
					}
					card_list_banlance_value.setText(availableBalance);
				}
			}
			return convertView;
		}
	}
}
