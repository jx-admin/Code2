package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.accenture.mbank.InvestmentsDetailActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;

public class InvestmentsLayoutManager extends BankRollContainerManager
		implements ShowListener {

	@Override
	public void createUiByData() {
		MainActivity mainActivity = (MainActivity)getContext();
		if (Contants.assertInvestmentAccounts.size() == 0) {
			mainActivity.assetRollView.setCloseImage(R.drawable.sphere_investments_top_off);
			mainActivity.assetRollView.setShowImage(R.drawable.sphere_investments_lower_off);
		} else {
			mainActivity.assetRollView.setShowListener(this);
		}
		
		if (Contants.depositInvestmentAccounts.size() == 0) {
			mainActivity.depositesRollView.setCloseImage(R.drawable.sphere_investments_top_off);
			mainActivity.depositesRollView.setShowImage(R.drawable.sphere_investments_lower_off);
		} else {
			mainActivity.depositesRollView.setShowListener(this);
		}
	}

	protected void setAccountsUI(BankRollView root, List<AccountsModel> list) {
		LinearLayout linearLayout1 = new LinearLayout(getContext());

		LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		linearLayout1.setLayoutParams(layoutParams1);
		linearLayout1.setOrientation(LinearLayout.VERTICAL);
		root.setContent(linearLayout1);
		linearLayout1.setBackgroundResource(R.drawable.box_details);

		LinearLayout linearLayout2 = new LinearLayout(getContext());
		LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		linearLayout2.setLayoutParams(layoutParams2);
		linearLayout2.setOrientation(LinearLayout.VERTICAL);
		root.setContent(linearLayout2);
		linearLayout2.setBackgroundResource(R.drawable.box_details);

		if (list != null && list.size() > 0) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			for (AccountsModel amAccountsModel : list) {
				ItemExpander itemExpander = (ItemExpander) inflater.inflate(
						R.layout.item_expander, null);
				String title = amAccountsModel.getAccountAlias();
				itemExpander.setTitle(title);
				itemExpander.setTypeface(Typeface.DEFAULT);
				itemExpander.setResultVisible(false);
				linearLayout2.addView(itemExpander);
				setExpandedContainer(itemExpander, amAccountsModel);

			}
		} else {

			TextView textView = new TextView(getContext());
			textView.setGravity(Gravity.CENTER);
			textView.setBackgroundResource(R.drawable.box_details);
			textView.setText(root.getResources().getString(
					R.string.not_available));
			linearLayout2.addView(textView);
		}
	}

	public void setExpandedContainer(ItemExpander itemExpander,
			AccountsModel accountsModel) {

		String type = accountsModel.getMortageType();
		LayoutInflater inflater = LayoutInflater.from(getContext());
		if ("GP".equals(type)) {
			AssetManagementExpandContainer assetManagementExpandContainer = (AssetManagementExpandContainer) inflater
					.inflate(R.layout.asset_management_expander, null);
			itemExpander
					.setExpandButtonBackground(R.drawable.investment_expand_selector);

			itemExpander.setExpandedContainer(assetManagementExpandContainer);
			assetManagementExpandContainer.setAccountCode(accountsModel
					.getAccountCode());

		} else if ("DT".equals(type)) {

			DepositsExpandedContainer depositsExpandedContainer = (DepositsExpandedContainer) inflater
					.inflate(R.layout.deposits_expanded_layout, null);
			itemExpander
					.setExpandButtonBackground(R.drawable.investment_expand_selector);

			depositsExpandedContainer.setAccountCode(accountsModel
					.getAccountCode());
			itemExpander.setExpandedContainer(depositsExpandedContainer);
		}
	}

	@Override
	protected void showFromDashBoard(boolean isDelay) {
		BankRollContainer bankRollContainer = getRollContainer();
		for (int j = 0; j < bankRollContainer.getBankRollViewCount(); j++) {
			BankRollView showAble = (BankRollView) bankRollContainer
					.getShowAble(j);
			LinearLayout container = (LinearLayout) showAble.getContent();
			BankRollView needShowBankRollView = null;
			for (int i = 0; i < container.getChildCount(); i++) {
				ItemExpander investmentDetail = (ItemExpander) container
						.getChildAt(i);
				if (investmentDetail.getTitle().equals(
						animateToDashBoardModel.getAccountCode())) {

					needShowBankRollView = showAble;

					needShowBankRollView.show();
					investmentDetail.performExpand();

				}
			}
		}
	}

	@Override
	public void onShow() {
		BankRollView bankRotatView = (BankRollView) getRollContainer()
				.getRollContainer().getChildAt(0);
		BankRollView bankRotatView1 = (BankRollView) getRollContainer()
				.getRollContainer().getChildAt(1);
		createUiByData();
		if (bankRotatView.getContent() == null
				&& Contants.depositInvestmentAccounts.size() > 0) {
			// setAccountsUI(bankRotatView, Contants.depositInvestmentAccounts);
			// setAccountsUI(bankRotatView1, Contants.assertInvestmentAccounts);
			performShowFromDashBoard(true);
		} else {
			performShowFromDashBoard(false);
		}
		// if (bankRotatView1.getContent() == null &&
		// Contants.assertInvestmentAccounts.size() > 0) {
		//
		// }
	}

	@Override
	public void onShow(ShowAble showAble) {
		LogManager.d("onShow ShowAble");
		MainActivity mainActivity = (MainActivity) getContext();
		if(showAble == mainActivity.depositesRollView){
			Intent intent = new Intent(getContext(),InvestmentsDetailActivity.class);
			intent.putExtra("TYPE",InvestmentsDetailActivity.DEPOSIT + "");
			getContext().startActivity(intent);
			
			mainActivity.overridePendingTransition(R.anim.slide_in_right_to_left, 0);
		}else if(showAble == mainActivity.assetRollView){
			Intent intent = new Intent(getContext(),InvestmentsDetailActivity.class);
			intent.putExtra("TYPE",InvestmentsDetailActivity.ASSETS + "");
			getContext().startActivity(intent);
			
			mainActivity.overridePendingTransition(R.anim.slide_in_right_to_left, 0);
		}
		
	}

}
