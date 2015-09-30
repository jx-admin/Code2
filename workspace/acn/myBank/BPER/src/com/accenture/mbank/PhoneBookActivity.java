package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.GetRecipientListModel;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.view.payment.BPERPhoneBook;

public class PhoneBookActivity extends MenuActivity{
	
	GetRecipientListModel mGetRecipientListModel;

    List<BankRecipient> bankRecipientList;

    List<CardRecipient> cardRecipientList;

    List<PhoneRecipient> phoneRecipientList;
    
	BPERPhoneBook mBPERPhoneBook;
	
	 int requestCode;
	 TransferType paymentType;
	 
	 public static void start(Activity context,int flag,TransferType paymentType){
		 Intent intent=new Intent(context,PhoneBookActivity.class);
		 intent.putExtra("requestCode", flag);
		 intent.putExtra("type", paymentType);
		 context.startActivityForResult(intent, flag);
	 }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		requestCode=intent.getIntExtra("requestCode", 0);
		paymentType=(TransferType) intent.getSerializableExtra("type");
		
		mBPERPhoneBook=new BPERPhoneBook(this);
		setContentView(mBPERPhoneBook.getView());
		
		showProgress();
		NewPaymentDataUtils.getRecipientList(this, mHandler, NewPaymentDataUtils.getRecipientList,paymentType);
		mBPERPhoneBook.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent =new Intent();
				intent.putExtra("item",(Serializable)( parent.getAdapter().getItem(position)));
				setResult(RESULT_OK, intent);
				finish();
			}
			
		});
	}

	
	ProgressOverlay mProgressOverlay;
	void showProgress(){
		if(mProgressOverlay==null){
			mProgressOverlay=new ProgressOverlay(this);
			mProgressOverlay.createDialog(this, R.string.downloading);
		}
		mProgressOverlay.showDialog();
	}
	void dismessProgress(){
		mProgressOverlay.dismissDialog();
	}
	
	Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			mGetRecipientListModel=(GetRecipientListModel) msg.obj;
			if(mGetRecipientListModel!=null){
				bankRecipientList=mGetRecipientListModel.getBankRecipientList();
				cardRecipientList=mGetRecipientListModel.getCardRecipientList();
				phoneRecipientList=mGetRecipientListModel.getPhoneRecipientList();
			}
			switch (paymentType) {
			case BANK_TRANSFER:
				mBPERPhoneBook.setBankRecipientData(bankRecipientList);
				break;
			case CARD_TOP_UP:
				mBPERPhoneBook.setCardRecipientData(cardRecipientList);
				break;
			case PHONE_TOP_UP:
				mBPERPhoneBook.setPhoneRecipientData(phoneRecipientList);
				break;
			default:
				break;
			}
			dismessProgress();
		};
	};
}
