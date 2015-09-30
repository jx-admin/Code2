package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accenture.mbank.model.AccountsModel;

/**
 * @author junxu.wang
 * @see bper_payment_account_simple_model.xml
 */
public class BPERPaymentAccountSimpleUtils {
	ViewGroup contentView;
	ImageView account_icon;
	ImageView isPerferredStar;
	TextView accountType;
	TextView accountName;
	public BPERPaymentAccountSimpleUtils(ViewGroup contentView){
		this.contentView=contentView;
		account_icon=(ImageView) contentView.findViewById(R.id.account_icon);
		isPerferredStar =(ImageView) contentView.findViewById(R.id.isPreferredStar);
		accountType=(TextView) contentView.findViewById(R.id.accountType);
		accountName=(TextView) contentView.findViewById(R.id.accountName);
		
		int color=contentView.getResources().getColor(R.color.payment_account);
		accountName.setTextColor(color);
		accountType.setTextColor(color);
		account_icon.setImageResource(R.drawable.account_detail_icon);
	}
	
	public void setAccountsModel(AccountsModel mAccountsModel){
		accountName.setText(mAccountsModel.getAccountAlias());
		if(mAccountsModel.getIsPreferred()){
			isPerferredStar.setVisibility(View.VISIBLE);
			isPerferredStar.setImageResource(R.drawable.icona_conto_riepilogo_stella);
		}else{
			isPerferredStar.setVisibility(View.GONE);
		}
	}
}
