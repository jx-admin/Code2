package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountInfoTitle extends LinearLayout {

	public View account_content;
	public ImageView icon;
	public ImageView isPerferredStar;
	public ImageView isRelations;
	public TextView accountType;
	public TextView accountName;

	public TextView account_balance_name;
	public TextView account_balance_value;

	public TextView available_balance_name;
	public TextView available_balance_value;
	
	public TextView dipiu_balance_name;
	public TextView dipiu_balance_value;

	public LinearLayout user_view;
	public LinearLayout sb_user_view;
	public LinearLayout prepaid_card_view;
	
	public View account_balance_view;
	public View available_balance_view;
	public View dipiu_balance_view;
	
	private int showType = 0;
	public static final int DEFAULT_USER = 0;
	public static final int SMALL_BUSINESS_USER = 1;
	public static final int PREPAID_CARD_USER = 2;
	
	
	public static final int ACCOUNT = 1;
	public static final int CARD = 2;
	public static final int PAYMENT = 3;
	public static final int LOANS = 4;
	public static final int INVESTMENTS = 5;
	public AccountInfoTitle(Context context) {
		super(context);
	}

	public AccountInfoTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
	
	public void init() {
		icon = (ImageView) findViewById(R.id.account_icon);
		accountType = (TextView) findViewById(R.id.accountType);
		accountName = (TextView) findViewById(R.id.accountName);
		prepaid_card_view = (LinearLayout) findViewById(R.id.prepaid_cards_view);
		user_view = (LinearLayout) findViewById(R.id.user_view);
		sb_user_view = (LinearLayout)findViewById(R.id.sb_user_view);
		isRelations =(ImageView) findViewById(R.id.isRelations);
		isPerferredStar =(ImageView) findViewById(R.id.isPreferredStar);
		
		if(showType == DEFAULT_USER){
			account_balance_view = user_view.findViewById(R.id.account_balance_view);
			account_balance_name = (TextView) account_balance_view.findViewById(R.id.balance_name);
			account_balance_name.setText(getResources().getString(R.string.balance_account));
			account_balance_value = (TextView) account_balance_view.findViewById(R.id.balance_value);
			
			available_balance_view = user_view.findViewById(R.id.available_balance_view);
			available_balance_name = (TextView) available_balance_view.findViewById(R.id.balance_name);
			available_balance_name.setText(getResources().getString(R.string.balance_account));
			available_balance_value = (TextView) available_balance_view.findViewById(R.id.balance_value);
			user_view.setVisibility(View.VISIBLE);
			sb_user_view.setVisibility(View.GONE);
			prepaid_card_view.setVisibility(View.GONE);
		}else if(showType == SMALL_BUSINESS_USER){
			account_balance_view = sb_user_view.findViewById(R.id.account_balance_view);
			account_balance_name = (TextView) account_balance_view.findViewById(R.id.balance_name);
			account_balance_name.setText(getResources().getString(R.string.balance_account));
			account_balance_value = (TextView) account_balance_view.findViewById(R.id.balance_value);
			
			available_balance_view = sb_user_view.findViewById(R.id.available_balance_view);
			available_balance_name = (TextView) available_balance_view.findViewById(R.id.balance_name);
			available_balance_name.setText(getResources().getString(R.string.balance_account));
			available_balance_value = (TextView) available_balance_view.findViewById(R.id.balance_value);
			
			dipiu_balance_view = sb_user_view.findViewById(R.id.dipiu_balance_view);
			dipiu_balance_name = (TextView) dipiu_balance_view.findViewById(R.id.balance_name);
			dipiu_balance_name.setText(getResources().getString(R.string.balance_account));
			dipiu_balance_value = (TextView) dipiu_balance_view.findViewById(R.id.balance_value);
			sb_user_view.setVisibility(View.VISIBLE);
			user_view.setVisibility(View.GONE);
			prepaid_card_view.setVisibility(View.GONE);
		} else if(showType == PREPAID_CARD_USER){
			account_balance_view = prepaid_card_view.findViewById(R.id.account_balance_view);
			account_balance_name = (TextView) account_balance_view.findViewById(R.id.balance_name);
			account_balance_name.setText(getResources().getString(R.string.balance_account));
			account_balance_value = (TextView) account_balance_view.findViewById(R.id.balance_value);
			prepaid_card_view.setVisibility(View.VISIBLE);
			user_view.setVisibility(View.GONE);
			sb_user_view.setVisibility(View.GONE);
		}else if(showType==PAYMENT){
			account_content=findViewById(R.id.account_content);
			account_content.setBackgroundResource(R.color.white);
			account_balance_view = user_view.findViewById(R.id.account_balance_view);
			user_view.findViewById(R.id.balance_divider).setBackgroundColor(0xff157A3A);
			account_balance_view.setBackgroundResource(R.drawable.top_account_balance);
			account_balance_name = (TextView) account_balance_view.findViewById(R.id.balance_name);
			account_balance_value = (TextView) account_balance_view.findViewById(R.id.balance_value);
			
			available_balance_view = user_view.findViewById(R.id.available_balance_view);
			available_balance_view.setBackgroundResource(R.drawable.top_available_balance);
			available_balance_name = (TextView) available_balance_view.findViewById(R.id.balance_name);
			available_balance_value = (TextView) available_balance_view.findViewById(R.id.balance_value);
			
			int color=getContext().getResources().getColor(R.color.payment_account);
			accountName.setTextColor(color);
			accountType.setTextColor(color);
			user_view.setBackgroundColor(color);
			icon.setImageResource(R.drawable.account_detail_icon);
			account_balance_name.setText(R.string.account_balance);
			available_balance_name.setText(R.string.available_balance);
		}
	}
	
	public void init(int showType){
		setShowType(showType);
		init();
	}
	
	public void setPerferredStar(int type){
		if(type == ACCOUNT){
			isPerferredStar.setImageResource(R.drawable.icona_conti_stella);
		}else if(type == CARD){
			isPerferredStar.setImageResource(R.drawable.icona_carta_stella);
		}else if(type == PAYMENT){
			isPerferredStar.setImageResource(R.drawable.icona_conto_riepilogo_stella);
		}else if(type == LOANS){
			isPerferredStar.setImageResource(R.drawable.icone_star_red);
		}else if(type == INVESTMENTS){
			isPerferredStar.setImageResource(R.drawable.icone_star_orange);
		}
		isPerferredStar.setVisibility(View.VISIBLE);
	}
	
	public void setRelations(boolean isTrue){
		isRelations.setImageResource(R.drawable.icona_carta_valigia);
		isRelations.setVisibility(View.VISIBLE);
	}
	
	public void setAccount() {

	}
	
	public void setShowType(int showType){
		this.showType = showType;
	}
}
