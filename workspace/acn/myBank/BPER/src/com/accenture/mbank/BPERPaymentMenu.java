package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.accenture.mbank.logic.EventManagement;
import com.accenture.mbank.logic.GetServiceStatusJson;
import com.accenture.mbank.logic.GetServiceStatusJson.ServiceCode;
import com.accenture.mbank.logic.GetServiceStatusJson.StatusService;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.KeyBoardUtils;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.view.payment.BPERPayment;
import com.accenture.mbank.view.payment.BPERPaymentBankTransfer;
import com.accenture.mbank.view.payment.BPERPaymentCardTopUp;
import com.accenture.mbank.view.payment.BPERPaymentEntry;
import com.accenture.mbank.view.payment.BPERPaymentPhoneTopUp;
import com.accenture.mbank.view.payment.ViewManagerUtils;
import com.android.log.CLog;


/**new payment activity
 * @author junxu.wang
 *
 */
public class BPERPaymentMenu extends MenuActivity  implements OnClickListener {
	CLog cLog=new CLog(BPERPaymentMenu.class.getSimpleName());
	private View contentView;
	BPERPayment mBperPayment;
	ImageButton bank_transfer_btn;
	ImageButton transfer_entry_btn;
	ImageButton phone_top_up_btn;
	ImageButton card_top_up_btn;
	TransferType mTransferType;
	
	public static BPERPaymentMenu mBPERPaymentMenu;
	
	Stack<ViewManagerUtils> stack=new Stack<ViewManagerUtils>();
	
	public static void start(Context context){
		Intent intent = new Intent(context, BPERPaymentMenu.class);
		context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clearViewManagerUtils();
		showViewManagerUtils(new TypeViewManagerUtils(this));
		mBPERPaymentMenu=this;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	class TypeViewManagerUtils implements ViewManagerUtils, OnClickListener{
		View contentView;
		public TypeViewManagerUtils(Context context){
			contentView=LayoutInflater.from(context).inflate(R.layout.bper_payment_type_page, null);
			bank_transfer_btn=(ImageButton)contentView.findViewById(R.id.bank_transfer_btn);
			transfer_entry_btn=(ImageButton)contentView.findViewById(R.id.transfer_entry_btn);
			phone_top_up_btn=(ImageButton)contentView.findViewById(R.id.phone_top_up_btn);
			card_top_up_btn=(ImageButton)contentView.findViewById(R.id.card_top_up_btn);
			
			bank_transfer_btn.setOnClickListener(this);
			transfer_entry_btn.setOnClickListener(this);
			phone_top_up_btn.setOnClickListener(this);
			card_top_up_btn.setOnClickListener(this);
		}
		
		@Override
		public View getContentView() {
			// TODO Auto-generated method stub
			updateEntryItem();
			return contentView;
		}
		
		@Override
		public boolean onBackPressed() {
			return false;
		}
		
		public void updateEntryItem(){
			if(isOffline){
				return;
			}
			int count=0;
			if(Contants.baseAccounts!=null){
				count+=Contants.baseAccounts.size();
			}

			if(Contants.ibanCardAccounts!=null){
				count+=Contants.ibanCardAccounts.size();
			}
			
			if(count>1){
				transfer_entry_btn.setVisibility(View.VISIBLE);
			}else{
				transfer_entry_btn.setVisibility(View.GONE);
			}
		}
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.bank_transfer_btn:
				mTransferType=TransferType.BANK_TRANSFER;
				mBperPayment=new BPERPaymentBankTransfer(BPERPaymentMenu.this,mTransferType);
				break;
			case R.id.transfer_entry_btn:
				mTransferType=TransferType.TRANSFER_ENTRY;
				mBperPayment=new BPERPaymentEntry(BPERPaymentMenu.this,mTransferType);
				break;
			case R.id.phone_top_up_btn:
				mTransferType=TransferType.PHONE_TOP_UP;
				mBperPayment=new BPERPaymentPhoneTopUp(BPERPaymentMenu.this,mTransferType);
				break;
			case R.id.card_top_up_btn:
				mTransferType=TransferType.CARD_TOP_UP;
				mBperPayment=new BPERPaymentCardTopUp(BPERPaymentMenu.this,mTransferType);
				break;
			}
			mBperPayment.setmOnViewFinish(monViewFinish);
			showProgress();
			NewPaymentDataUtils.getServiceStatus(BPERPaymentMenu.this,loadDateHandler,NewPaymentDataUtils.getServiceStatus,Contants.abi,mBperPayment.getServiceCode());
		}
	}
	
	public interface OnViewFinish{
		public void onfinish();
	}
	
	OnViewFinish monViewFinish=new OnViewFinish(){
		
		@Override
		public void onfinish() {
			onBackPressed();
		}
		
	};
	
	public void showViewManagerUtils(ViewManagerUtils vmu){
		stack.push(vmu);
		setContentView(vmu.getContentView());
	}
	
	public ViewManagerUtils getCurrentViewManagerUtils(){
		if(stack.size()>0){
			return stack.peek();
		}else{
			return null;
		}
	}
	
	public ViewManagerUtils showPop(){
		if(stack.size()>0){
			ViewManagerUtils vmu=stack.pop();
		}
		if(stack.size()>0){
			ViewManagerUtils vmu=stack.peek();
			setContentView(vmu.getContentView());
			return vmu;
		}
		return null;
	}
	
	public void clearViewManagerUtils(){
		stack.clear();
	}
	
	public boolean removeViewManagerUtils(ViewManagerUtils vmu){
		return stack.remove(vmu);
	}
	
	@Override
	public void onBackPressed() {
		if(KeyBoardUtils.hideSoftInputFromWindow(this)){
			return;
		}
		if(stack.size()>0){
			ViewManagerUtils vmu=stack.peek();
			if(vmu!=null&&vmu.onBackPressed()){
				return ;
			}
		}
		if(showPop()==null){
			super.onBackPressed();
		}
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
	
	/**
	 * loading handler
	 */
	private Handler loadDateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewPaymentDataUtils.getServiceStatus:
				dismessProgress();
				GetServiceStatusJson getServiceStatus=(GetServiceStatusJson) msg.obj;
				StatusService mStatusService=null;
				if(getServiceStatus!=null&&getServiceStatus.isSuccess()&&getServiceStatus.getServiceCodeList()!=null&&getServiceStatus.getServiceCodeList().size()>0){
					ServiceCode mServiceCode=getServiceStatus.getServiceCodeList().get(0);
					if(mServiceCode!=null&&mServiceCode.getStatusServiceList()!=null&&mServiceCode.getStatusServiceList().size()>0){
						mStatusService=mServiceCode.getStatusServiceList().get(0);
					}
				}
				if(mStatusService!=null){
					if(mStatusService.isActive()){
						showViewManagerUtils(mBperPayment);
						mBperPayment.loadData(null);
					}else{
						DialogManager.createMessageDialog(mStatusService.getMessage(), BPERPaymentMenu.this).show();
					}
				}else{
					EventManagement mEventManagement=null;
					if(getServiceStatus!=null){
						mEventManagement=getServiceStatus.getEventManagement();
					}
//					DialogManager.displayErrorMessage(mEventManagement,-1, BPERPaymentMenu.this).show();// mark dialog
				}
				
				break;
			}
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
		mBperPayment.onActivityResult(requestCode, resultCode,  data);
//			Toast.makeText(this, "onActivityResult", 1).show();
	}
	
	public void show(){
		stack.clear();
		setContentView(contentView);
	}
	
}

