
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.CardListActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.logic.BalanceJson;
import com.accenture.mbank.model.BalanceAccountsModel;
import com.accenture.mbank.model.GetBalanceResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;

public class CardsLayoutManager extends BankRollContainerManager implements ShowListener{
    
    
	/*
	 * singleton pattern creation
	 */
	private CardsLayoutManager() {
	}

	private static CardsLayoutManager _instance = null;

	public static CardsLayoutManager create() {
		if (_instance == null)
			_instance = new CardsLayoutManager();

		return _instance;
	}
	
    @Override
    public void createUiByData() {
        MainActivity mainActivity = (MainActivity)getContext();
        mainActivity.creditRollView.disableCloseButton();
        mainActivity.ibanRollView.disableCloseButton();
        mainActivity.prepaidRollView.disableCloseButton();

        if (Contants.creditCardAccounts.size() == 0) {       	        	
            mainActivity.creditRollView.setCloseImage(R.drawable.sfera_carte_superiore_disattivo);
            mainActivity.creditRollView.setShowImage(R.drawable.sfera_carte_inferiore_disattivo);
        } else {
            mainActivity.creditRollView.setShowListener(this);
        }

        if (Contants.ibanCardAccounts.size() == 0) {       	        	
            mainActivity.ibanRollView.setCloseImage(R.drawable.sfera_carte_superiore_disattivo);
            mainActivity.ibanRollView.setShowImage(R.drawable.sfera_carte_inferiore_disattivo);
        } else {
            mainActivity.ibanRollView.setShowListener(this);
        }
        
        if (Contants.prepaidCardAccounts.size() == 0) {
            mainActivity.prepaidRollView.setCloseImage(R.drawable.sfera_carte_superiore_disattivo);
            mainActivity.prepaidRollView.setShowImage(R.drawable.sfera_carte_inferiore_disattivo);
        } else {
            mainActivity.prepaidRollView.setShowListener(this);
        }
    }

    @Override
    public void onShow() {
        createUiByData();
    }
    
	@Override
	public void onShow(ShowAble showAble) {
		LogManager.d("onShow ShowAble");
		MainActivity mainActivity = (MainActivity) getContext();
		String bankServiceCode = "";
		if (showAble == mainActivity.creditRollView) {
			LogManager.d("creditRollView");
			bankServiceCode = Contants.CREDIT_CARD_CODE;
		} else if (showAble == mainActivity.ibanRollView) {
			LogManager.d("ibanRollView");
			bankServiceCode = Contants.IBAN_CARD_CODE;
		} else if (showAble == mainActivity.prepaidRollView) {
			LogManager.d("prepaidRollView");
			bankServiceCode = Contants.PREPAID_CARD_CODE;
		}
		Intent intent = new Intent(getContext(), CardListActivity.class);
		intent.putExtra("BANK_SERVICE_CODE", bankServiceCode);
		getContext().startActivity(intent);
		
        mainActivity.overridePendingTransition(R.anim.slide_in_right_to_left, 0);
	}
}
