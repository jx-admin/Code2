package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.accenture.mbank.AccountDetailActivity;
import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;

public class AccountItemLayout extends BankLinearLayout implements
		OnClickListener, ShowAble {

	ImageButton showBtn, closeBtn;

	ImageView line,sb_Star;

	TextView balanceAccountValue, availableAccountValue, carrying_Value,
			accountName, lastUpdateDate;

	ShowListener listener;

	public AccountItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
//		init();

	}

	void init() {
		showBtn = (ImageButton) findViewById(R.id.account_show_img_button);
		closeBtn = (ImageButton) findViewById(R.id.account_close_img);
		int height_top = (int) (cycle_top_height * BaseActivity.screen_height);
		closeBtn.getLayoutParams().height = height_top;
		int width = (int) (height_top * close_widht_height);
//		closeBtn.getLayoutParams().width = width;

		int height_bottom = (int) (height_top * cycle_bottom_height);

		showBtn.getLayoutParams().height = height_bottom;
//		showBtn.getLayoutParams().width = (int) (height_bottom * show_widht_height);

		line = (ImageView) findViewById(R.id.line);
		line.getLayoutParams().height = height_top;

		ViewGroup accountData = (ViewGroup) findViewById(R.id.account_data);
		accountData.getLayoutParams().height = height_top;
		ViewGroup accountSectionLayout = (ViewGroup) findViewById(R.id.account_section_layout);
		accountSectionLayout.getLayoutParams().height = height_top;
		accountSectionLayout.getLayoutParams().width = width;
		
		ViewGroup sbaccountSectionLayout = (ViewGroup) findViewById(R.id.sb_user_account_section_layout);
		sbaccountSectionLayout.getLayoutParams().height = height_top;
		sbaccountSectionLayout.getLayoutParams().width = width;

		showBtn.setOnClickListener(this);
		closeBtn.setOnClickListener(this);

		RelativeLayout sb_user_account_section = (RelativeLayout) sbaccountSectionLayout;
		RelativeLayout account_section = (RelativeLayout) accountSectionLayout;
		if (account!=null && account.getDipiuBalance() > 0) {
			balanceAccountValue = (TextView) sb_user_account_section.findViewById(R.id.balance_account_value);
			availableAccountValue = (TextView) sb_user_account_section.findViewById(R.id.available_account_value);
			carrying_Value = (TextView) sb_user_account_section.findViewById(R.id.carrying_account_value);
			sb_user_account_section.setVisibility(View.VISIBLE);
			account_section.setVisibility(View.GONE);
		} else {
			balanceAccountValue = (TextView) account_section.findViewById(R.id.balance_account_value);
			availableAccountValue = (TextView) account_section.findViewById(R.id.available_account_value);
			sb_user_account_section.setVisibility(View.GONE);
			account_section.setVisibility(View.VISIBLE);
		}
		
		accountName = (TextView) findViewById(R.id.account_name);
		sb_Star = (ImageView) findViewById(R.id.sb_star);
		lastUpdateDate = (TextView) findViewById(R.id.account_last_update_date);
	}

	public BalanceAccountsModel getAccount() {
		return account;
	}

	BalanceAccountsModel account;

	public void setAccount(BalanceAccountsModel account) {
		this.account = account;
		setUiBydata();
	}

	/**
	 * 仅上半球的ui展示，下半球的归record管
	 */
	private void setUiBydata() {
		if (this.account == null) {
			return;
		} else {
			if (balanceAccountValue == null) {
				init();
			}
			try {
				String money = Utils.generateFormatMoney(account.getAccountBalance());
				balanceAccountValue.setText(money);
				
				money = Utils.generateFormatMoney(this.account.getAvailableBalance());
				availableAccountValue.setText(money);

				if(account!=null && this.account.isDipiuActive()){
					String dipiubalance = Utils.generateFormatMoney(this.account.getDipiuBalance());
					carrying_Value.setText(dipiubalance);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			accountName.setText(this.account.getPersonalizedName());
			String date = TimeUtil.getDateString(System.currentTimeMillis(),TimeUtil.dateFormat5);
			this.lastUpdateDate.setText(date);
			if (account.isPreferred()) {
				sb_Star.setImageResource(R.drawable.icona_conti_stella);
			}
		}
	}

	public void addRecord() {

	}

	@Override
	public void onClick(View v) {
		if (showBtn == v) {
			Intent intent = new Intent(getContext(), AccountDetailActivity.class);
			for(AccountsModel accountModel : Contants.accountsList){
				if(accountModel.getAccountCode().equals(account.getAccountCode())){
					intent.putExtra("ACCOUNT_MODEL", accountModel);
				}
			}
        	intent.putExtra("BALANCE_ACCOUNT", account);
        	getContext().startActivity(intent);
        	
            MainActivity mainActivity = (MainActivity)getContext();
            mainActivity.overridePendingTransition(R.anim.slide_in_right_to_left, 0);
		}
	}

	@Override
	public void show() {

		if (listener != null) {
			listener.onShow(this);
		}

	}

	@Override
	public void setShowListener(ShowListener listener) {
		this.listener = listener;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
