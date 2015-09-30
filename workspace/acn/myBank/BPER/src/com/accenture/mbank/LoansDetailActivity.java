/**
 * 
 */
package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetFinancingInfoModel;
import com.accenture.mbank.model.InstallmentsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountInfoTitle;
import com.accenture.mbank.view.ExpirationAmountStateItem;
import com.accenture.mbank.view.QuickReturnListView;
import com.accenture.mbank.view.QuickReturnListView.OnLoadMoreListener;

/**
 * @author yang.c.li
 * 
 */
public class LoansDetailActivity extends BaseActivity implements OnCheckedChangeListener,OnLoadMoreListener {

	AccountInfoTitle listHeaderAccountInfoTitle;
	QuickReturnListView loan_item_list_view;
	ViewGroup listHeaderView;
	ViewGroup listFooterView;
	LinearLayout listHeaderAccountTitle;

	LinearLayout header_view_content;
	AccountInfoTitle headerAccountInfoTitle;
	LinearLayout headerView;
	ViewGroup headerAccountTitle;

	TextView loan_total, loan_account_value, loan_type_value,
			rate_mancanti_value, date_fine_paino_value,
			riferimento_applicato_value, tasso_finito_value,
			garanzia_ipotecaria_value, tasso_finito_text;

	LinearLayout recorddetailsLayout;
	LinearLayout installmentsLayout;
	RadioButton details, installments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loans_detail);
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(this);
		
		// 初始化listView并添加 header
		loan_item_list_view = (QuickReturnListView) findViewById(R.id.loan_item_list_view);
		listHeaderView = (ViewGroup) inflater.inflate(R.layout.loans_header_layout, null);
		listHeaderAccountTitle = (LinearLayout) listHeaderView.findViewById(R.id.account_title);
		listHeaderAccountInfoTitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title, null);
		listHeaderAccountInfoTitle.init();
		listHeaderAccountInfoTitle.account_balance_view.setBackgroundResource(R.drawable.top_loans_amount);
		listHeaderAccountInfoTitle.available_balance_view.setBackgroundResource(R.drawable.top_loans_residual_capital);
		listHeaderAccountTitle.addView(listHeaderAccountInfoTitle);
		listHeaderView.setVisibility(View.INVISIBLE);
		loan_item_list_view.addHeaderView(listHeaderView);
		
		recorddetailsLayout = (LinearLayout) findViewById(R.id.loan_detail_record);
		
		//
		header_view_content = (LinearLayout) findViewById(R.id.header_view_content);
		headerView = (LinearLayout) header_view_content.findViewById(R.id.header_view);
		headerAccountTitle = (ViewGroup) headerView.findViewById(R.id.account_title);
		headerAccountInfoTitle = (AccountInfoTitle) inflater.inflate(R.layout.account_info_title, null);
		headerAccountInfoTitle.init();
		headerAccountTitle.addView(headerAccountInfoTitle);
		loan_item_list_view.setHeaderView(headerView);
		
		installmentsLayout = (LinearLayout) headerView.findViewById(R.id.loan_install_record);
		
		details = (RadioButton) headerView.findViewById(R.id.details);
		installments = (RadioButton) headerView.findViewById(R.id.installments);
		installments.setOnCheckedChangeListener(this);
		details.setOnCheckedChangeListener(this);
		
		loan_account_value = (TextView) findViewById(R.id.loan_account_value);
		loan_type_value = (TextView) findViewById(R.id.loan_type_value);
		rate_mancanti_value = (TextView) findViewById(R.id.rate_mancanti_value);
		date_fine_paino_value = (TextView) findViewById(R.id.date_fine_paino_value);
		riferimento_applicato_value = (TextView) findViewById(R.id.riferimento_applicato_value);
		tasso_finito_text = (TextView) findViewById(R.id.tasso_finito_text);
		tasso_finito_value = (TextView) findViewById(R.id.tasso_finito_value);
		garanzia_ipotecaria_value = (TextView) findViewById(R.id.garanzia_ipotecaria_value);
	}

	@Override
	protected void onStart() {
		super.onStart();
		updateUI();
	}

	private void updateUI() {
		GetFinancingInfoModel getFinancingInfo = (GetFinancingInfoModel) getIntent().getSerializableExtra("GETFINANCINGINFO");
		AccountsModel accountsModel = (AccountsModel) getIntent().getSerializableExtra("ACCOUNT_MODEL");
		loan_account_value.setText(getFinancingInfo.getIbanCode());
		/*String rateType = null;
		if (getFinancingInfo.getRateType().equals("FIXED")) {
			rateType = getResources().getString(R.string.not_able);
		} else if (getFinancingInfo.getRateType().equals("VARIABLE")) {
			String value = getFinancingInfo.getBenchmarksValue().replace('.',',');
			rateType = "+" + value + "%" + " " + getFinancingInfo.getBenchmarksDesc();
		}*/

		headerAccountInfoTitle.account_balance_view.setBackgroundResource(R.drawable.top_loans_amount);
		headerAccountInfoTitle.available_balance_view.setBackgroundResource(R.drawable.top_loans_residual_capital);
		headerAccountInfoTitle.icon.setImageResource(R.drawable.top_icons_financing);
		ColorStateList csl = (ColorStateList) getResources().getColorStateList(R.color.loans_balance_bg);
		headerAccountInfoTitle.accountName.setTextColor(csl);
		headerAccountInfoTitle.accountName.setText(accountsModel.getAccountAlias());
		headerAccountInfoTitle.accountType.setTextColor(csl);
		headerAccountInfoTitle.accountType.setText(getResources().getString(R.string.financing_title_name));
		headerAccountInfoTitle.account_balance_name.setText(getResources().getString(R.string.residual_capital1));
		
		if(accountsModel.getIsPreferred()){
			headerAccountInfoTitle.setPerferredStar(headerAccountInfoTitle.LOANS);
		}
		
		String residueAmount = Utils.generateFormatMoney(getResources().getString(R.string.eur), getFinancingInfo.getResidueAmount());
		headerAccountInfoTitle.account_balance_value.setText(residueAmount);
		headerAccountInfoTitle.available_balance_name.setText(getResources().getString(R.string.loans_total_amount1));
		String totalAmount = Utils.generateFormatMoney(getResources().getString(R.string.eur), getFinancingInfo.getTotalAmountl());
		headerAccountInfoTitle.available_balance_value.setText(totalAmount);

		if (getFinancingInfo.getRateType().equals("FISSO")) {
			riferimento_applicato_value.setText("TASSO FISSO");
			loan_type_value.setText(getFinancingInfo.getRateType() + " " + getFinancingInfo.getDuration());
			tasso_finito_text.setText(R.string.tasso_finito);
			tasso_finito_value.setText("+" + Utils.formatPercentDouble(getFinancingInfo.getEndRate()));
		} else {
			riferimento_applicato_value.setText(getFinancingInfo.getBenchmarksValue().replace('.',',') + "% " + getFinancingInfo.getBenchmarksDesc());
			loan_type_value.setText(getFinancingInfo.getRateType() + " " + getFinancingInfo.getDuration());
			tasso_finito_text.setText(R.string.spread_applicato);
			tasso_finito_value.setText("+" + Utils.formatPercentDouble(Double.parseDouble(getFinancingInfo.getRate())));
		}
		
		loan_account_value.setText(accountsModel.getIbanCode());
//		loan_type_value.setText(getFinancingInfo.getDuration());
		rate_mancanti_value.setText(getFinancingInfo.getNumPaymentsRemaning());
		String endDate = TimeUtil.changeFormattrString(getFinancingInfo.getEndDate(), TimeUtil.dateFormat2a, TimeUtil.dateFormat5);
		date_fine_paino_value.setText(endDate);
//		riferimento_applicato_value.setText(getFinancingInfo.getRateType());
//		String rateValue = getFinancingInfo.getRate().replace('.', ',');
//		rateValue = "+" + rateValue + "%";
//		tasso_finito_value.setText(rateValue);
		
		String isWarranty = "";
		if(getFinancingInfo.isWarranty()){
			isWarranty = "SI";
		}else {
			isWarranty = "NO";
		}
		garanzia_ipotecaria_value.setText(isWarranty);

		createUIByInstallments(getFinancingInfo.getInstallments());
	}
	
	private void showType(int type) {
		  switch (type) {
			  case 0:
			   recorddetailsLayout.setVisibility(View.INVISIBLE);
			   loan_item_list_view.setVisibility(View.VISIBLE);
			   installmentsLayout.setVisibility(View.VISIBLE);
			   break;
			  case 1:
			   recorddetailsLayout.setVisibility(View.VISIBLE);
			   loan_item_list_view.setVisibility(View.INVISIBLE);
			   installmentsLayout.setVisibility(View.INVISIBLE);
			   break;
			  default:
			   break;
		  }
	}
	

	InstallAdapter installAdapter;

	private void createUIByInstallments(List<InstallmentsModel> list) {
		LogManager.d("list" + list.size());
		Collections.reverse(list);
		installAdapter = new InstallAdapter(list, this);
		loan_item_list_view.setAdapter(installAdapter);
		installmentsModels = list;
	}

	List<InstallmentsModel> installmentsModels;
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == back){
			finish();
		}
	}
	
	public static class InstallAdapter extends BaseAdapter {

		List<InstallmentsModel> list;

		Context context;

		public InstallAdapter(List<InstallmentsModel> list, Context context) {
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
				ExpirationAmountStateItem expirationAmountStateItem = (ExpirationAmountStateItem) inflater.inflate(R.layout.expiration_amount_state, null);
				convertView = expirationAmountStateItem;
			}
			ExpirationAmountStateItem expirationAmountStateItem = (ExpirationAmountStateItem) convertView;
			InstallmentsModel installmentsModel = list.get(position);
			String amount = Utils.notPlusGenerateFormatMoney(context.getResources().getString(R.string.eur), installmentsModel.getAmount());

			boolean paided = false;
			LogManager.d("paid" + installmentsModel.getPaidState());
			if (installmentsModel.getPaidState().equals(Contants.PAID_STATUS)) {
				paided = true;
			}
			String time = TimeUtil.getDateString(installmentsModel.getDeadlineDate(),TimeUtil.dateFormat5);
			expirationAmountStateItem.setUI(time, amount, paided);
			return convertView;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int type = 0;
		if (isChecked) {
			if (buttonView == details) {
				type = 0;
			} else if (buttonView == installments) {
				type = 1;
			}
	   showType(type);
	  }
	}

	@Override
	public void onLoadMore() {
		installAdapter.list.addAll(installmentsModels);
		installAdapter.notifyDataSetChanged();
	}

}
