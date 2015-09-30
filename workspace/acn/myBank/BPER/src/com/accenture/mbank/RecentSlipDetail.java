package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.CreditModel;
import com.accenture.mbank.model.CreditPaymentModel;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountInfoTitle;

public class RecentSlipDetail extends BaseActivity {
	private TextView textView1,textView1Value;
	private TextView textView2,textView2Value;
	private TextView textView3,textView3Value;
	private TextView textView4,textView4Value;
	private TextView textView5,textView5Value;
	private TextView textView6,textView6Value;
	private TextView textView7,textView7Value;
	private TextView textView8,textView8Value;
	private TextView pageTitle;
	private LinearLayout effect_details;
	private AccountInfoTitle accountInfoTitle;
	private ImageView visualizza_effetti_distinta_button;
	
	private int type;
	
	private AccountsModel account;
	
	private CreditModel creditModel;
	
	private CreditPaymentModel creditPaymentModel;
	
	private ColorStateList csl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deposit_slip_detail);
		String _type = getIntent().getStringExtra(RecentDepositActivity.TYPE);
		type = Integer.parseInt(_type);
		account = (AccountsModel) getIntent().getSerializableExtra(RecentDepositActivity.ACCOUNT_MODEL);
		if(type == RecentDepositActivity.EFFETC){
			creditPaymentModel = (CreditPaymentModel) getIntent().getSerializableExtra(RecentDepositActivity.CREDIT_PAYMENT_LIST);
		}else if(type == RecentDepositActivity.DETAILS){
			creditModel = (CreditModel) getIntent().getSerializableExtra(RecentDepositActivity.CREDIT_LIST);
		}
		init();
	}
	
	private void init(){
		csl = (ColorStateList) getResources().getColorStateList(R.color.payment_account);
		
		pageTitle = (TextView)findViewById(R.id.page_title);
		
		accountInfoTitle = (AccountInfoTitle)findViewById(R.id.account_title);
		accountInfoTitle.init();
		
		textView1 = (TextView)findViewById(R.id.textView1);
		textView2 = (TextView)findViewById(R.id.textView2);
		textView3 = (TextView)findViewById(R.id.textView4);
		textView4 = (TextView)findViewById(R.id.textView3);
		textView5 = (TextView)findViewById(R.id.textView5);
		textView6 = (TextView)findViewById(R.id.textView6);
		textView7 = (TextView)findViewById(R.id.textView7);
		textView8 = (TextView)findViewById(R.id.textView8);
		
		textView1Value = (TextView)findViewById(R.id.textView1Value);
		textView2Value = (TextView)findViewById(R.id.textView2Value);
		textView3Value = (TextView)findViewById(R.id.textView3Value);
		textView4Value = (TextView)findViewById(R.id.textView4Value);
		textView5Value = (TextView)findViewById(R.id.textView5Value);
		textView6Value = (TextView)findViewById(R.id.textView6Value);
		textView7Value = (TextView)findViewById(R.id.textView7Value);
		textView8Value = (TextView)findViewById(R.id.textView8Value);
		
		effect_details = (LinearLayout)findViewById(R.id.effect_details);
		
		visualizza_effetti_distinta_button = (ImageView)findViewById(R.id.visualizza_effetti_distinta_button);
		visualizza_effetti_distinta_button.setOnClickListener(this);
		
		if(type == RecentDepositActivity.DETAILS){
			pageTitle.setText(getResources().getString(R.string.recent_deposit_slip_detail));
		}else if(type == RecentDepositActivity.EFFETC){
			pageTitle.setText(getResources().getString(R.string.effects_details_page_title));
			effect_details.setVisibility(View.VISIBLE);
			visualizza_effetti_distinta_button.setVisibility(View.GONE);
		}
		updateUI();
	} 
	
	private void updateUI(){
		accountInfoTitle.icon.setImageResource(R.drawable.account_detail_icon);
		accountInfoTitle.accountType.setTextColor(csl);
		accountInfoTitle.accountName.setTextColor(csl);
		accountInfoTitle.user_view.setBackgroundResource(R.color.payment_account);
		accountInfoTitle.sb_user_view.setBackgroundResource(R.color.payment_account);
		accountInfoTitle.account_balance_name.setText(R.string.balance_account);
		accountInfoTitle.available_balance_name.setText(R.string.available_account);
		accountInfoTitle.accountType.setText(getResources().getString(R.string.account2));
		
		accountInfoTitle.accountName.setText(account.getAccountAlias());
		String accountBalance = Utils.generateFormatMoney(getResources().getString(R.string.eur),account.getAccountBalance());
		accountInfoTitle.account_balance_value.setText(accountBalance);
		String avalableBalance = Utils.generateFormatMoney(getResources().getString(R.string.eur),account.getAvailableBalance());
		accountInfoTitle.available_balance_value.setText(avalableBalance);
		
		if(type == RecentDepositActivity.DETAILS){
			textView1.setText(R.string.insertionDate);
			String date = TimeUtil.changeFormattrString(creditModel.getInsertionDate(), TimeUtil.dateFormat2a,TimeUtil.dateFormat5);
			textView1Value.setText(date);
			textView2.setText(R.string.totalAmount);
			String totalAcmout = Utils.generateFormatMoney(getResources().getString(R.string.eur), creditModel.getTotalAmount());
			textView2Value.setText(totalAcmout);
			textView3.setText(R.string.creditType);
			textView3Value.setText(creditModel.getCreditType());
			textView4.setText(R.string.creditID);
			textView4Value.setText(creditModel.getCreditID());
			textView5.setText(R.string.paymentsNum);
			textView5Value.setText(creditModel.getPaymentsNum() + "");
			textView6.setText(R.string.state_1);
			textView6Value.setText(creditModel.getState());
		}else if(type == RecentDepositActivity.EFFETC){
			textView1.setText(R.string.totalAmount);
			String totalAcmout = Utils.generateFormatMoney(getResources().getString(R.string.eur), creditPaymentModel.getTotalAmount());
			textView1Value.setText(totalAcmout);
			textView2.setText(R.string.state_1);
			textView2Value.setText(creditPaymentModel.getState());
			textView3.setText(R.string.debtorName);
			textView3Value.setText(creditPaymentModel.getDebtorName());
			textView4.setText(R.string.expiration_2);
			String executionDate = TimeUtil.changeFormattrString(creditPaymentModel.getExecutionDate(), TimeUtil.dateFormat2a,TimeUtil.dateFormat5);
			textView4Value.setText(executionDate);
			textView5.setText(R.string.insertionDate);
			String insertionDate = TimeUtil.changeFormattrString(creditPaymentModel.getInsertionDate(), TimeUtil.dateFormat2a,TimeUtil.dateFormat5);
			textView5Value.setText(insertionDate);
			textView6.setText(R.string.list_type_item);
			textView6Value.setText(creditPaymentModel.getPaymentID());
			textView7.setText(R.string.creditID);
			textView7Value.setText(creditPaymentModel.getCreditID());
			textView8.setText(R.string.creditPaymentType);
			textView8Value.setText(creditPaymentModel.getListTypeItem());
		}
	}
	
	@Override
	protected void onBackClick() {
		super.onBackClick();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == visualizza_effetti_distinta_button){
			Intent intent = new Intent(this, RecentDepositActivity.class);
			intent.putExtra(RecentDepositActivity.TYPE, RecentDepositActivity.EFFETC+"");
			intent.putExtra(RecentDepositActivity.ACCOUNT_MODEL,account);
			intent.putExtra("CREDIT_ID",creditModel.getCreditID());
			intent.putExtra("INSERTION_DATE", creditModel.getInsertionDate());
			startActivity(intent);
		}else if (v == back) {
			finish();
		}
	}
}
