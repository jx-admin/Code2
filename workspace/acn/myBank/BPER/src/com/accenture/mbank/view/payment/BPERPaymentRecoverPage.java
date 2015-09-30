package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectCard;
import com.accenture.mbank.logic.TransferObjectEntry;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.logic.TransferObjectTransfer;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.AmountItalyInputFilter;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DesLimitTextChangedListener;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.util.PaymentStepViewUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.PaymentStepViewUtils.Step;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountInfoTitle;
import com.custom.view.ReSizeSingleEditText;
import com.custom.view.ReSizeSingleTextView;

public class BPERPaymentRecoverPage extends OnConfirmCheckUtils{

	Context context;
	LayoutInflater lf;
	
	ViewGroup contentView;
	TextView page_title;
	PaymentStepViewUtils mPaymentStepViewUtils;
	AccountInfoTitle mAccountInfoTitle;
	TableLayout content_tl;
	ReSizeSingleEditText cup_et;
	ReSizeSingleEditText cig_et;
	EditText amount_et;
	EditText description_et;
	View data_content;
	EditText data_et;
	ImageButton data_ibtn;
	Button confirm_btn;
	long date;
	AccountsModel mAccountsModel;
	boolean des;

	AmountItalyInputFilter mAmountItalyInputFilter;
	DesLimitTextChangedListener mDesLimitTextChangedListener;
	/***<pre>
	Beneficiario Beneficiary name 

	Codice IBAN IBAN code 

	Codice BIC BIC code (optional, show only if present) 

	Codice CUP CUP (optional, show only if present) 

	Codice CIG CIG (optional, show only if present) 

	Importo amount 

	Causale description 

	Data date 

	Causale valutaria Purpose currency (optional, show only if present) 


	 * @param mAccountsModel
	 * @param beneficiary
	 * @param iban
	 * @param bic
	 * @param cup
	 * @param cig
	 * @param amount
	 * @param description
	 * @param purposeCurrency
	 * @param date
	 */
	public void showBankTranser(AccountsModel mAccountsModel,String beneficiary,String iban,String bic,String cup,String cig,double amount,String description,String purposeCurrency, long date,int newPayment){
		setPageTitle(TransferType.BANK_TRANSFER.getPageTitleId(newPayment));
		setAccountsModel(mAccountsModel);
//		page_title.setText(R.string.confirm_bank_title);
		contentClear();
		setText(R.string.beneficiary_tilte, beneficiary);
		setText(R.string.iban_tilte, iban);
		setText(R.string.bic_tilte, bic);

		if(!TextUtils.isEmpty(cup)||!TextUtils.isEmpty(cup)){
			showCupEditText(cup);
			showCigEditText(cig);
			mDesLimitTextChangedListener.setDestriptionMaxLenth(mDesLimitTextChangedListener.DES_LENGTH_ON);
		}else{
			mDesLimitTextChangedListener.setDestriptionMaxLenth(mDesLimitTextChangedListener.DES_LENGTH_OFF);
		}
		mDesLimitTextChangedListener.setCharLimit(true);
		des=true;
		 
		setAmount(amount);
//		setText(R.string.amount_tilte,Utils.generateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
		setDescription(description);
//		setText(R.string.description_tilte, description);
		if(!TextUtils.isEmpty(purposeCurrency)){
			setText(R.string.purpose_currency_tilte, purposeCurrency);
		}
//		setText(R.string.date_tilte,TimeUtil.getDateString(date,TimeUtil.dateFormat5));
		setDate(date);

		data_content.setVisibility(View.VISIBLE);
	}
	
	/**<pre/>
	 Beneficiario Beneficiary name 

	Codice IBAN IBAN code 

	Importo amount 

	Causale description 

	Data date 


	 * @param mAccountsModel
	 * @param beneficiary
	 * @param iban
	 * @param amount
	 * @param description
	 * @param date
	 */
	public void showTranserEntry(AccountsModel mAccountsModel,String beneficiary,String iban,double amount,String description, long date,int newPayment){
		setPageTitle(TransferType.TRANSFER_ENTRY.getPageTitleId(newPayment));
		setAccountsModel(mAccountsModel);
//		page_title.setText(R.string.confirm_entry_title);
		contentClear();
		setText(R.string.beneficiary_tilte, beneficiary);
		setText(R.string.iban_tilte, iban);
		setAmount(amount);
//		setText(R.string.amount_tilte,Utils.generateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
		setDescription(description);
		mDesLimitTextChangedListener.setCharLimit(true);
		des=true;

//		setText(R.string.description_tilte, description);
//		setText(R.string.date_tilte,TimeUtil.getDateString(date,TimeUtil.dateFormat5));
		setDate(date);

		data_content.setVisibility(View.VISIBLE);
	}

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
		setPageTitle(TransferType.PHONE_TOP_UP.getPageTitleId(newPayment));
		setAccountsModel(mAccountsModel);
//		page_title.setText(R.string.confirm_phone_top_up_title);
		contentClear();
		setText(R.string.beneficiary_tilte, beneficiary);
		
		String certifiedNumber = Contants.getUserInfo.getUserprofileHb().getContactPhone();
		if(certifiedNumber != null && phoneNumber.equals(certifiedNumber)){
			setText(R.string.phone_number, Utils.maskCertifiedNumber(phoneNumber));
		} else {
			setText(R.string.phone_number, phoneNumber);
		}
		
		setText(R.string.provider_fs, provider);
		setAmount(amount);
//		setText(R.string.amount_tilte,Utils.generateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
	}
	
	/**<pre/>
	 * * Beneficiario(beneficiary value) 
	 * * Carta numero (card number) 
	 * * Importo (amount) 
	 * * Causale (description only if is present) 

	 * @param mAccountsModel
	 * @param beneficiary
	 * @param cardNumber
	 * @param amount
	 * @param description
	 * @param fees
	 */
	public void showCardTopUp(AccountsModel mAccountsModel,String beneficiary,String cardNumber,double amount,String description,int newPayment){
		setPageTitle(TransferType.CARD_TOP_UP.getPageTitleId(newPayment));
		setAccountsModel(mAccountsModel);
//		page_title.setText(R.string.confirm_card_top_up_title);
		contentClear();
		setText(R.string.beneficiary_tilte, beneficiary);
		setText(R.string.fs_card_number,cardNumber);
		setAmount(amount);
//		setText(R.string.amount_tilte,Utils.generateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
		setDescription(description);
		mDesLimitTextChangedListener.setCharLimit(false);
		des=false;

//		setText(R.string.description_tilte, description);

		data_content.setVisibility(View.GONE);
	}

	/**
	 * @param mAccountsModel
	 * @param mTransferObject
	 */
	public void show(AccountsModel mAccountsModel, TransferObject mTransferObject,int newPayment){
		if(mTransferObject==null){
//			setAccountsModel(mAccountsModel);
			return;
		}
		if(mTransferObject instanceof TransferObjectTransfer){
			TransferObjectTransfer mRecentTransferModel=(TransferObjectTransfer) mTransferObject;
			showBankTranser(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryIban(), mRecentTransferModel.getBeneficiaryBic(), mRecentTransferModel.getBeneficiaryCUP(), mRecentTransferModel.getBeneficiaryCIG(), mRecentTransferModel.getAmount(), mRecentTransferModel.getDescription(), mRecentTransferModel.getPurposeCurrency(), mRecentTransferModel.getDate(),newPayment);
		}else if(mTransferObject instanceof TransferObjectEntry){
			TransferObjectEntry mRecentTransferModel=(TransferObjectEntry) mTransferObject;
			showTranserEntry(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryIban(),  mRecentTransferModel.getAmount(), mRecentTransferModel.getDescription(), mRecentTransferModel.getDate(),newPayment);
		}else if(mTransferObject instanceof TransferObjectSim){
			TransferObjectSim mRecentTransferModel=(TransferObjectSim) mTransferObject;
			showPhoneTopUp(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryNumber(), mRecentTransferModel.getBeneficiaryProviderName(), mRecentTransferModel.getAmount(),newPayment);
		}else if(mTransferObject instanceof TransferObjectCard){
			TransferObjectCard mRecentTransferModel=(TransferObjectCard) mTransferObject;
			showCardTopUp(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryCardNumber(),mRecentTransferModel.getAmount(), mRecentTransferModel.getDescription(),newPayment);
		}
	}
	
	public void setAccountsModel(AccountsModel mAccountsModel){
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
	
	public BPERPaymentRecoverPage(Context context){
		this.context=context;
		lf=LayoutInflater.from(context);
		getContentView();
	}
	
	public View getContentView(){
		if(contentView ==null){
			contentView=(ViewGroup) lf.inflate(R.layout.bper_payment_recover_page, null);
			page_title=(TextView) contentView.findViewById(R.id.page_title);
			mPaymentStepViewUtils=new PaymentStepViewUtils(contentView);
			mPaymentStepViewUtils.setStep(Step.STEP1);
			mAccountInfoTitle=(AccountInfoTitle) contentView.findViewById(R.id.account_info);
			mAccountInfoTitle.init(AccountInfoTitle.PAYMENT);
			content_tl=(TableLayout) contentView.findViewById(R.id.table);
			confirm_btn=(Button) contentView.findViewById(R.id.confirmation_btn);
			//
			amount_et=(EditText) contentView.findViewById(R.id.amount_et);
			amount_et.addTextChangedListener(confirmTextChangeListener);
			mAmountItalyInputFilter=new AmountItalyInputFilter(amount_et,null);
			description_et=(EditText) contentView.findViewById(R.id.description_of_payment_et);
			description_et.addTextChangedListener(confirmTextChangeListener);
			mDesLimitTextChangedListener=new DesLimitTextChangedListener(context,description_et,(TextView)contentView.findViewById(R.id.des_length_alerter_tv));
			data_content=contentView.findViewById(R.id.data_content);
			data_et=(EditText) contentView.findViewById(R.id.data_et);
			data_ibtn=(ImageButton) contentView.findViewById(R.id.data_ibtn);
			data_ibtn.setOnClickListener(dataOnClickListener);
			data_et.setOnClickListener(dataOnClickListener);
			 setDate(System.currentTimeMillis());
		}
		return contentView;
	}
	
	public void setPageTitle(int id){
		page_title.setText(id);
	}

	OnViewClickListener dataOnClickListener=new OnViewClickListener(){
		@Override
		public void onClick(View v) {
			super.onClick(v);
			switch (v.getId()) {
			case R.id.data_ibtn:
			case R.id.data_et:
				final Calendar calender = Calendar.getInstance();
	            new DatePickerDialog(context, new OnDateSetListener() {

	                @Override
	                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	                    calender.set(year, monthOfYear, dayOfMonth);
	                    setDate(calender.getTimeInMillis());

	                    // Toast.makeText(activity,
	                    // year+"/"+monthOfYear+"/"+dayOfMonth,
	                    // Toast.LENGTH_SHORT).show();
	                }
	            }, calender.get(calender.YEAR), calender.get(calender.MONDAY), calender
	                    .get(calender.DATE)).show();
				break;
			}
		}
	};

	boolean onCheckForConfirm(boolean showError){
		boolean result=false;
		int msgId=-1;
//		if(TextUtils.isEmpty(beneficiary_name_et.getText())){
//			result=true;
//			DialogManager.createMessageDialog(R.string.beneficiary_name_empty_error, context).show();
//		}
//		else 
//			if(TextUtils.isEmpty(iban_et.getText())){
//			result=true;
//			DialogManager.createMessageDialog(R.string.card_number_empty, context).show();
//		}
//		else 
		if(getAmount()<=0){
			result=true;
			msgId=R.string.amount_empty_error;
		}else if(cig_et!=null&&cup_et!=null&&(TextUtils.isEmpty(cig_et.getText())!=TextUtils.isEmpty(cup_et.getText()))){
			result=true;
			msgId=R.string.cup_e_cig_empty_error;
		}
		else if(des&&TextUtils.isEmpty(description_et.getText())){
			result=true;
			msgId=R.string.description_empty_error;
		}
//		else if(TextUtils.isEmpty(purpose_currency_et.getText())){
//			result=false;
//			((BaseActivity)context).displayErrorMessage("",context.getString(R.string.purpose_currency_empty_error));
//		}
//		else if(TextUtils.isEmpty(cup_et.getText())||TextUtils.isEmpty(cig_et.getText())){
//			result=false;
//			((BaseActivity)context).displayErrorMessage("",context.getString(R.string.cup_e_cig_empty_error));
//		}
//		else if(TextUtils.isEmpty(bic_et.getText())){
//			result=false;
//			((BaseActivity)context).displayErrorMessage(context.getString(R.string.bic_empty_error));
//		}
		if(showError&&msgId>0){
			DialogManager.createMessageDialog(msgId, context).show();
		}
		this.confirm_btn.setEnabled(!result);
		return result;
	}
	
	public void setDate(long date){
		this.date=date;
		data_et.setText(TimeUtil.getDateString(date,TimeUtil.dateFormat5));
	}
	
	public void setConfirmClickListener(OnViewClickListener l){
		confirm_btn.setOnClickListener(l);
	}
	
	private void contentClear(){
		content_tl.removeAllViews();
		cup_et=null;
		cig_et=null;
	}
	
	public void setText(int titleTextId,String txt){
		if(TextUtils.isEmpty(txt)){
			return;
		}
		TableRow beneficiary_tr=(TableRow) lf.inflate(R.layout.bper_payment_verification_item, null);
		TextView beneficiary_title_tv=(TextView) beneficiary_tr.findViewById(R.id.tv1);
		ReSizeSingleTextView beneficiary_value_tv=(ReSizeSingleTextView) beneficiary_tr.findViewById(R.id.tv2);
		beneficiary_value_tv.setReSizeSingleLine(titleTextId==R.string.iban_tilte);
		beneficiary_title_tv.setText(titleTextId);
		beneficiary_value_tv.setText(txt);
		content_tl.addView(beneficiary_tr);
	}
	
	public void showCupEditText(String txt){
		if(TextUtils.isEmpty(txt)){
			return;
		}
		if(cup_et==null){
			cup_et=new ReSizeSingleEditText(context);
			cup_et.setHint(R.string.cup_hint);
			cup_et.setReSizeSingleLine(true);
			cup_et.addTextChangedListener(confirmTextChangeListener);
		}
		cup_et.setText(txt);
		content_tl.addView(cup_et);
	}
	
	public String getCup(){
		if(cup_et==null){
			return null;
		}
		return cup_et.getText().toString();
	}

	public void showCigEditText(String txt){
		if(TextUtils.isEmpty(txt)){
			return;
		}
		if(cig_et==null){
			cig_et=new ReSizeSingleEditText(context);
			cig_et.setHint(R.string.cig_hint);
			cig_et.setReSizeSingleLine(true);
			cig_et.addTextChangedListener(confirmTextChangeListener);
		}
		cig_et.setText(txt);
		content_tl.addView(cig_et);
	}
	
	public String getCig(){
		if(cig_et==null){
			return null;
		}
		return cig_et.getText().toString();
	}

	public void setAmount(double amount){
		mAmountItalyInputFilter.fromUser=AmountItalyInputFilter.FROM_WATCHER;
		amount_et.setText(NewPaymentDataUtils.formateAmount(amount, 2, 2));
	}

	
	public double getAmount(){
		return NewPaymentDataUtils.parseDouble(amount_et.getText().toString().trim());
	}
	public String getDescription(){
		return description_et.getText().toString().trim();
	}
	public void setDescription(String des){
		if(des==null){
			des="";
		}
		description_et.setText(des);
	}
	public long getData(){
		return date;
	}
	
}
