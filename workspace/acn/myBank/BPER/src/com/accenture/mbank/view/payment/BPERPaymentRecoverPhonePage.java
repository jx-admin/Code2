package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.AmountAvailable;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.util.PaymentStepViewUtils;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.view.AccountInfoTitle;

public class BPERPaymentRecoverPhonePage{
	Context context;
	LayoutInflater lf;
	LayoutParams lp;
	
	ViewGroup contentView;
	PaymentStepViewUtils mPaymentStepViewUtils;
	TextView page_title;
	AccountInfoTitle mAccountInfoTitle;
	TableLayout content_tl;
	Button confirm_btn;
	///phone 
	GridView amount_gv;

	List<AmountAvailable> mAmountAvailableLs ;
	private AmountAvailable mAmountAvailable;


	/**<pre/>
	 * * Beneficiario(beneficiary value) 
	 * * Numero di telefono         (phone number) 
	 * * Operatore (provider) 
	 * * Taglio ricarica (recharge amount) 

	 * @param mAccountsModel
	 * @param beneficiary
	 * @param phoneNumber
	 * @param provider
	 * @param amount
	 */
	public void showPhoneTopUp(AccountsModel mAccountsModel,String beneficiary,String phoneNumber,String provider,double amount,int newPayment){
		setAccountsModel(mAccountsModel);
		setPageTitle(TransferType.PHONE_TOP_UP.getPageTitleId(newPayment));
//		page_title.setText(R.string.confirm_phone_top_up_title);
		content_tl.removeAllViews();
		setText(R.string.beneficiary_tilte, beneficiary);
		if(BaseActivity.isOffline){
			setText(R.string.phone_number,  Utils.maskCertifiedNumber(phoneNumber));
		}else{
		String certifiedNumber = Contants.getUserInfo.getUserprofileHb().getContactPhone();
		if(certifiedNumber != null && certifiedNumber.equals(phoneNumber)){
			setText(R.string.phone_number, Utils.maskCertifiedNumber(phoneNumber));
		} else {
			setText(R.string.phone_number, phoneNumber);
		}
		}
		setText(R.string.provider_fs, provider);
		setAmount(amount);
	}
	

	/**
	 * @param mAccountsModel
	 * @param mTransferObject
	 */
	public void show(AccountsModel mAccountsModel, TransferObject mTransferObject,int newPayment){
		content_tl.removeAllViews();
		if(mTransferObject==null){
//			setAccountsModel(mAccountsModel);
			return;
		}
		if(mTransferObject instanceof TransferObjectSim){
			TransferObjectSim mRecentTransferModel=(TransferObjectSim) mTransferObject;
			showPhoneTopUp(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryNumber(), mRecentTransferModel.getBeneficiaryProviderName(), mRecentTransferModel.getAmount(),newPayment);
		}
	}
	
	public BPERPaymentRecoverPhonePage(Context context){
		this.context=context;
		lf=LayoutInflater.from(context);
		lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		getContentView();
	}
	
	public ViewGroup getContentView(){
		if(contentView ==null){
			contentView=(ViewGroup) lf.inflate(R.layout.bper_payment_recover_phone_page, null);
			mPaymentStepViewUtils=new PaymentStepViewUtils(contentView);
			page_title=(TextView) contentView.findViewById(R.id.page_title);
			mAccountInfoTitle=(AccountInfoTitle) contentView.findViewById(R.id.account_info);
			mAccountInfoTitle.init(AccountInfoTitle.PAYMENT);
			content_tl=(TableLayout) contentView.findViewById(R.id.table);
			confirm_btn=(Button) contentView.findViewById(R.id.confirmation_btn);
			amount_gv=(GridView) contentView.findViewById(R.id.amount_gv);
			amount_gv.setAdapter(amountListAdapter);
		}
			
		return contentView;
	}
	
	public void setPageTitle(int id){
		page_title.setText(id);
	}
	
	public void setAmount(double amount){
		mAmountAvailable=new AmountAvailable();
		mAmountAvailable.setRechargeAmount((int) amount);
		amount_gv.setAdapter(amountListAdapter);
	}
	
	public void setConfirmClickListener(OnViewClickListener l){
		confirm_btn.setOnClickListener(l);
	}
	
	AccountsModel mAccountsModel;
	public void setAccountsModel(AccountsModel mAccountsModel){
		if(mAccountsModel==null){
			return;
		}
		this.mAccountsModel=mAccountsModel;
		mAccountInfoTitle.accountName.setText(mAccountsModel.getAccountAlias());
		String money = Utils.generateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), mAccountsModel.getAccountBalance());
		mAccountInfoTitle.account_balance_value.setText(money);
		money = Utils.generateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), mAccountsModel.getAvailableBalance());
		mAccountInfoTitle.available_balance_value.setText(money);
		if(mAccountsModel.getIsPreferred()){
			mAccountInfoTitle.setPerferredStar(AccountInfoTitle.PAYMENT);
		}else{
			mAccountInfoTitle.isPerferredStar.setVisibility(View.GONE);
		}
	}
	
	public void createTableRow(String title,String txt){
		TableRow beneficiary_tr=(TableRow) lf.inflate(R.layout.bper_payment_verification_item, null);
		TextView beneficiary_title_tv=(TextView) beneficiary_tr.findViewById(R.id.tv1);
		TextView beneficiary_value_tv=(TextView) beneficiary_tr.findViewById(R.id.tv2);
		beneficiary_title_tv.setText(title);
		beneficiary_value_tv.setText(txt);
		content_tl.addView(beneficiary_tr,lp);
	}
	
	public void setText(int titleTextId,String txt){
		if(TextUtils.isEmpty(txt)){
			return;
		}
		TableRow beneficiary_tr=(TableRow) lf.inflate(R.layout.bper_payment_verification_item, null);
		TextView beneficiary_title_tv=(TextView) beneficiary_tr.findViewById(R.id.tv1);
		TextView beneficiary_value_tv=(TextView) beneficiary_tr.findViewById(R.id.tv2);
		beneficiary_title_tv.setText(titleTextId);
		beneficiary_value_tv.setText(txt);
		content_tl.addView(beneficiary_tr,lp);
	}

	
	public AmountAvailable getAmount(){
		return mAmountAvailable;
	}

	public interface onProviderclickListener{
		public void onClick(View v,int postion);
	}
	onProviderclickListener monProviderclickListener;
	public void setOnProviderclickListener(onProviderclickListener monProviderclickListener){
		this.monProviderclickListener=monProviderclickListener;
	}
	
	Button selectedAmount;
	OnViewClickListener amountClick=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
			if(selectedAmount!=null){
//				selectedAmount.setSelected(false);
				selectedAmount.setBackgroundResource(R.drawable.phone_topup_taglia_selector);
			}
			selectedAmount=(Button) v;
//			selectedAmount.setSelected(true);
			selectedAmount.setBackgroundResource(R.drawable.phone_topup_taglia_click);
			mAmountAvailable=(AmountAvailable) v.getTag();
		}
	};
	ListAdapter amountListAdapter=new ListAdapter() {
		StyleSpan mStyleSpan=new StyleSpan(android.graphics.Typeface.BOLD_ITALIC);
		RelativeSizeSpan mRelativeSizeSpan=new RelativeSizeSpan(0.5f);
		
		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Button btn;
			if(convertView==null){
				btn=new Button(context);
				btn.setSingleLine();
				btn.setPadding(0, 0, 0, 0);
				btn.setGravity(Gravity.CENTER);
				btn.setTextColor(0xff40734D);
				btn.setBackgroundResource(R.drawable.phone_topup_taglia_selector);
				btn.setOnClickListener(amountClick);
			}else{
				btn=(Button) convertView;
			}
			AmountAvailable mAmountAvailable=mAmountAvailableLs.get(position);
			SpannableString ss=new SpannableString(Utils.notPlusGenerateFormatMoney("",mAmountAvailable.getRechargeAmount())+contentView.getContext().getResources().getString(R.string.eur));
			ss.setSpan(mStyleSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗斜体  
			ss.setSpan(mRelativeSizeSpan, ss.length()-4, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //0.5f表示默认字体大小的一半  
			btn.setText(ss);
			btn.setTag(mAmountAvailable);
			if(BPERPaymentRecoverPhonePage.this.mAmountAvailable!=null&&BPERPaymentRecoverPhonePage.this.mAmountAvailable.getRechargeAmount()==mAmountAvailable.getRechargeAmount()){
//				btn.setSelected(true);
				BPERPaymentRecoverPhonePage.this.mAmountAvailable=mAmountAvailable;
				btn.setBackgroundResource(R.drawable.phone_topup_taglia_click);
				selectedAmount=btn;
			}else{
//				btn.setSelected(false);
				btn.setBackgroundResource(R.drawable.phone_topup_taglia_selector);
			}
			return btn;
		}
		
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mAmountAvailableLs.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mAmountAvailableLs==null?0:mAmountAvailableLs.size();
		}
		
		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}
	};


	public void setAmountAvailableLs(List<AmountAvailable> mAmountAvailableLs) {
		this.mAmountAvailableLs=mAmountAvailableLs;
		amount_gv.setAdapter(amountListAdapter);
	}
}
