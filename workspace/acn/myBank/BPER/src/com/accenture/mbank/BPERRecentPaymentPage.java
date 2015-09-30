package com.accenture.mbank;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import cn.oneMin.demo.slideListView.ItemSlideAdapter;
import cn.oneMin.demo.slideListView.ItemSlideTouchListener.OnSlideItemClickListener;
import cn.oneMin.demo.slideListView.QuickReturnListView;

import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.model.AccountsForServiceModel;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.BperRecentTransferResponseModel;
import com.accenture.mbank.model.EventManagement;
import com.accenture.mbank.model.GetAccountsByServicesResponseModel;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.view.payment.BPERPaymentAccountPaperUtils;

public class BPERRecentPaymentPage implements OnPageChangeListener {
	Context context;
	LayoutInflater lf;
	ViewGroup contentView;
	BPERPaymentAccountPaperUtils mBPERPaymentAccountPaperUtils;
	QuickReturnListView recent_payment_ListView;
	ItemSlideAdapter recentPaymentAdapter;
	
	public BPERRecentPaymentPage(Context context){
		this.context=context;
		lf=LayoutInflater.from(context);
		contentView=(ViewGroup) lf.inflate(R.layout.bper_recent_payment_page, null);
		mBPERPaymentAccountPaperUtils = new BPERPaymentAccountPaperUtils(contentView.findViewById(R.id.account_include));
		mBPERPaymentAccountPaperUtils.setOnPageChangeListener(this);
		recent_payment_ListView = (QuickReturnListView) contentView.findViewById(R.id.recent_list);
		recentPaymentAdapter=new ItemSlideAdapter(context);
		View headerView = LayoutInflater.from( context).inflate( R.layout.bper_recent_account_paper_medel, null);
		recent_payment_ListView.addHeaderView(headerView);
		recent_payment_ListView.setHeaderView(mBPERPaymentAccountPaperUtils.getContentView());
		headerView.setVisibility(View.INVISIBLE);
		recent_payment_ListView.setAdapter(recentPaymentAdapter);
	}
	
	public ViewGroup getContentView(){
		return contentView;
	}
	
	public void setOnRecentItemClickLinstener(OnSlideItemClickListener mOnRecentItemClickLinstener){
		recentPaymentAdapter.setOnRecentItemClickLinstener(mOnRecentItemClickLinstener);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPageSelected(int arg0) {
		AccountsModel mAccountsModel=mBPERPaymentAccountPaperUtils.getAccountsModel();
		if(mAccountsModel==null){
			return;
		}
		showProgress();
		NewPaymentDataUtils.getTablesResponseModel(context, loadDateHandler,
				NewPaymentDataUtils.LOAD_RECENPAYMENT, mAccountsModel.getAccountCode());
	}
	
	public void loadData() {
		showProgress();
		NewPaymentDataUtils.getAccountsByService(context, loadDateHandler,
				NewPaymentDataUtils.GET_ACCOUNT, new String[] { "054" });
	}
	
	public AccountsModel getAccountsModel(){
		return mBPERPaymentAccountPaperUtils.getAccountsModel();
	}
	
	public TransferObject getRecentTransferModel(int position){
		return (TransferObject) recentPaymentAdapter.getItem(position);
	}
	
	ProgressOverlay mProgressOverlay;
	void showProgress(){
		if(mProgressOverlay==null){
			mProgressOverlay=new ProgressOverlay(context);
			mProgressOverlay.createDialog(context, R.string.downloading);
		}
		mProgressOverlay.showDialog();
	}
	void dismessProgress(){
		mProgressOverlay.dismissDialog();
	}
	
	private Handler loadDateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case NewPaymentDataUtils.GET_ACCOUNT:
				GetAccountsByServicesResponseModel mGetAccountsByServicesResponseModel=(GetAccountsByServicesResponseModel) msg.obj;
				List<AccountsModel> accounts=null;
				if(mGetAccountsByServicesResponseModel!=null&&mGetAccountsByServicesResponseModel.responsePublicModel!=null&&mGetAccountsByServicesResponseModel.responsePublicModel.isSuccess()){
					List<AccountsForServiceModel>  payerAccounts=mGetAccountsByServicesResponseModel.getAccountsForServiceList();
					if(payerAccounts!=null&&payerAccounts.size()>0){
						accounts=payerAccounts.get(0).getAccounts();
						mBPERPaymentAccountPaperUtils.setData(accounts);
						if(accounts!=null){
							if(accounts.size()>0){
								NewPaymentDataUtils.getTablesResponseModel(context, loadDateHandler,
										NewPaymentDataUtils.LOAD_RECENPAYMENT, accounts.get(0).getAccountCode());
							}else{
								dismessProgress();
								DialogManager.createMessageDialog(R.string.payment_getaccount_by_services_empty,"", context,new OnClickListener(){
									
									@Override
									public void onClick(View v) {
										((Activity)context).onBackPressed();
									}},null).show();
							}
						}
					}
				}else{
					dismessProgress();
					((Activity)context).onBackPressed();
					EventManagement mEventManagement=null;
					if(mGetAccountsByServicesResponseModel!=null&&mGetAccountsByServicesResponseModel.responsePublicModel!=null){
						mEventManagement=mGetAccountsByServicesResponseModel.responsePublicModel.eventManagement;
					}
					DialogManager.displayErrorMessage(mEventManagement,-1, context).show();
				}
				break;
			case NewPaymentDataUtils.LOAD_RECENPAYMENT:
				BperRecentTransferResponseModel mBperRecentTransferResponseModel= (BperRecentTransferResponseModel) msg.obj;
				AccountsModel mAccountsModel=mBPERPaymentAccountPaperUtils.getAccountsModel();
				List<TransferObject> recentTransferList=null;
				if(mAccountsModel!=null&&mBperRecentTransferResponseModel!=null&&mBperRecentTransferResponseModel.getRecentTransfers()!=null){
					recentTransferList=mBperRecentTransferResponseModel.getRecentTransfers();
					recentPaymentAdapter.seList(mAccountsModel,recentTransferList);
				}
				dismessProgress();
				if(mBperRecentTransferResponseModel==null||!mBperRecentTransferResponseModel.isSuccess()){
					DialogManager.displayErrorMessage(mBperRecentTransferResponseModel==null?null:mBperRecentTransferResponseModel.getEventManagement(),-1, context).show();
				}
				break;
			default:
				break;
			}
			}
	};
	
}
