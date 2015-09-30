package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.accenture.mbank.logic.GetAssetsInformationJson;
import com.accenture.mbank.logic.GetDepositInfoRequestJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetAssetsInformationResponseModel;
import com.accenture.mbank.model.GetDepositInfoResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AssetManagementExpandContainer;
import com.accenture.mbank.view.DepositsExpandedContainer;
import com.accenture.mbank.view.EnhanceItemExpander;

public class InvestmentsDetailActivity extends BaseActivity {
	private ListView listView;
	private TextView investment_caption;
	public static final int DEPOSIT = 1;
	public static final int ASSETS = 2;
	int type = 0;

	InvestmentListAdapter investmentListAdapter;
	
	Handler handler;
	
	Context mContext;
	
	AccountsModel accountModel;
	
	List<AccountsModel> accountList = new ArrayList<AccountsModel>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.investment_details);
		handler = new Handler();
		mContext = this;
		accountModel = (AccountsModel) getIntent().getSerializableExtra("ACCOUNT_MODEL");
		if(accountModel != null){
			accountList.add(accountModel);
		}
		
		String _type = getIntent().getStringExtra("TYPE");
		type = Integer.parseInt(_type);
		init();
	}
	
	private void init() {
		investment_caption = (TextView) findViewById(R.id.investment_caption);
		listView = (ListView) findViewById(R.id.investment_list);
		if(type == DEPOSIT){
			investment_caption.setText(getResources().getString(R.string.deposit_2));
			if(accountModel !=null){
				investmentListAdapter = new InvestmentListAdapter(accountList, type, this);
			}else{
				investmentListAdapter = new InvestmentListAdapter(Contants.depositInvestmentAccounts, type, this);
			}
		}else {
			investment_caption.setText(getResources().getString(R.string.assets_management));
			if(accountModel !=null){
				investmentListAdapter = new InvestmentListAdapter(accountList, type, this);
			}else{
				investmentListAdapter = new InvestmentListAdapter(Contants.assertInvestmentAccounts, type, this);
			}
			
			
		}
		
		
		listView.setAdapter(investmentListAdapter);
	}
	
	class InvestmentListAdapter extends BaseAdapter {
		private List<AccountsModel> list;

		Context context;
		LayoutInflater inflater;
		private int type;
		public InvestmentListAdapter(List<AccountsModel> list,int type,Context context) {
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
			inflater = LayoutInflater.from(context);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.investment_deposit_item, null);
				EnhanceItemExpander itemExpander = (EnhanceItemExpander) convertView;
				itemExpander.setTitle(list.get(position).getAccountAlias());
				if (type == InvestmentsDetailActivity.ASSETS) {
					itemExpander.setItem_lable(R.string.total_gpm);
					itemExpander.setResult(Utils.generateFormatMoney(0) + "(0%)");
				}else if(type == InvestmentsDetailActivity.DEPOSIT){
					itemExpander.setItem_lable(R.string.portfolio_1);
					itemExpander.setResult(Utils.generateFormatMoney(0));
				}
				
				if(list.get(position).getIsPreferred()){
					itemExpander.setStarImage(R.drawable.icone_star_orange);
				}
				
				itemExpander.setExpand(false);
				itemExpander.setExpandBarBackground(R.color.white);
				itemExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
				itemExpander.setTypeface(Typeface.DEFAULT);
				setExpandedContainer(itemExpander,type,list.get(position));
			}
			return convertView;
		}
		
		GetDepositInfoResponseModel getDepositInfo;
		private void loadDepositInfo(final String accountCode,final EnhanceItemExpander itemExpander){
			 ProgressOverlay progressOverlay = new ProgressOverlay(context);
		        progressOverlay.show("", new OnProgressEvent() {
		            @Override
		            public void onProgress() {
		                String postData = GetDepositInfoRequestJson.GetDepositInfoReportProtocal(Contants.publicModel, accountCode);
		                HttpConnector httpConnector = new HttpConnector();
		                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,context);
		                getDepositInfo = GetDepositInfoRequestJson.parseGetDepositInfoResponse(httpResult);
		                if (!getDepositInfo.responsePublicModel.isSuccess()) {
		                	displayErrorMessage(getDepositInfo.responsePublicModel.eventManagement.getErrorCode(), getDepositInfo.responsePublicModel.eventManagement.getErrorDescription());
		                    return;
		                }

		                handler.post(new Runnable() {
		                    @Override
		                    public void run() {
		                        DepositsExpandedContainer depositsExpandedContainer = (DepositsExpandedContainer) inflater.inflate(R.layout.deposits_expanded_layout, null);
		                        depositsExpandedContainer.setTag(getDepositInfo);
		                        depositsExpandedContainer.setAccountCode(accountCode);
		        				itemExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
		        				itemExpander.setExpandBarBackground(R.color.white);
		        				String availableBalance = Utils.generateFormatMoney(getDepositInfo.getPortfolioValue());
		        				itemExpander.setResult(availableBalance);
		        				itemExpander.setExpandedContainer(depositsExpandedContainer);
		                    }
		                });
		            }
		        });
		}
		
		GetAssetsInformationResponseModel getAssetsInformation;
		private void loadAssetsInfomation(final String accountCode,final EnhanceItemExpander itemExpander){
			 ProgressOverlay progressOverlay = new ProgressOverlay(context);
				progressOverlay.show("", new OnProgressEvent() {
		        @Override
		        public void onProgress() {
		            String postData = GetAssetsInformationJson.GetAssetsInformantionReportProtocal(Contants.publicModel, accountCode);
		            HttpConnector httpConnector = new HttpConnector();
		            String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,context);
		            getAssetsInformation = GetAssetsInformationJson.parseGetAssetsInformationResponse(httpResult);
		            if (!getAssetsInformation.responsePublicModel.isSuccess()) {
		            	displayErrorMessage(getAssetsInformation.responsePublicModel.eventManagement.getErrorCode(), getAssetsInformation.responsePublicModel.eventManagement.getErrorDescription());
		                return;
		            }
		            handler.post(new Runnable() {
		                @Override
		                public void run() {
		                	AssetManagementExpandContainer assetManagementExpandContainer = (AssetManagementExpandContainer) inflater.inflate(R.layout.asset_management_expander, null);
		                	assetManagementExpandContainer.setTag(getAssetsInformation);
		                	assetManagementExpandContainer.setAccountCode(accountCode);
		                	String value = Utils.generateFormatMoney(getAssetsInformation.getPortfolioValue());
		                    String percentage = Utils.formatPercentDouble(getAssetsInformation.getPercentage());
	        				itemExpander.setResult(value + "(" + percentage + ")");
	        				itemExpander.setExpandBarBackground(R.color.white);
		    				itemExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
		    				itemExpander.setExpandedContainer(assetManagementExpandContainer);
		                }
		            });
		        }
		    });
		}
		
		public void setExpandedContainer(EnhanceItemExpander itemExpander,int type,AccountsModel accountsModel) {
			if (type == InvestmentsDetailActivity.ASSETS) {
				loadAssetsInfomation(accountsModel.getAccountCode(),itemExpander);
			} else if (type == InvestmentsDetailActivity.DEPOSIT) {
				loadDepositInfo(accountsModel.getAccountCode(),itemExpander);
			}
		}
	}
}
