package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectCard;
import com.accenture.mbank.logic.TransferObjectEntry;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.logic.TransferObjectTransfer;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.util.PaymentStepViewUtils;
import com.accenture.mbank.util.PaymentStepViewUtils.Step;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountInfoTitle;
import com.custom.view.ReSizeSingleTextView;

public class BPERPaymentConfirmPage implements ViewManagerUtils{
	Context context;
	LayoutInflater lf;
	LayoutParams lp;
	
	ViewGroup contentView;
	PaymentStepViewUtils mPaymentStepViewUtils;
	TextView page_title;
	AccountInfoTitle mAccountInfoTitle;
	TableLayout content_tl;
	Button confirm_btn;
	View pinView;
	Button ask_pin_cancel_btn;
	Button ask_pin_continu_btn;
	EditText asp_pin_et;

	/***<pre>
	 *  Beneficiario(beneficiary value) 
	 * * Codice IBAN         (iban value) 
	 * * Codice BIC      (BIC, only if used) 
	 * * Codice CUP    (CUP, only if used) 
	 * * Codice CIG(CIG. only if used) 
	 * * Importo   (amount value) 
	 * * Causale   (description value) 
	 * * Causale valutaria (purpose currency value,only if used) 
	 * * Data esecuzione (date value) 
	 * * Commissioni         (fees value) 

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
	 * @param fees
	 */
	public void showBankTranser(AccountsModel mAccountsModel,String beneficiary,String iban,String bic,String cup,String cig,double amount,String description,String purposeCurrency, long date,double fees,int titleType){
		setAccountsModel(mAccountsModel);
		setPageTitle(TransferType.BANK_TRANSFER.getPageTitleId(titleType));
		content_tl.removeAllViews();
		setText(R.string.beneficiary_tilte, beneficiary);
		setText(R.string.iban_tilte, iban);
		setText(R.string.bic_tilte, bic);
		setText(R.string.cup_tilte, cup);
		setText(R.string.cig_tilte, cig);
		setText(R.string.amount_tilte,Utils.notPlusGenerateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
		setText(R.string.description_tilte, description);
		if(!TextUtils.isEmpty(purposeCurrency)){
			setText(R.string.purpose_currency_tilte, purposeCurrency);
		}
		setText(R.string.date_tilte,TimeUtil.getDateString(date,TimeUtil.dateFormat5));
		setText(R.string.fees_tilte,Utils.notPlusGenerateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), fees));
	}
	
	/**Title of the page: 
	 * * RIEPILOGO BONIFICO VERSO I MIEI CONTI These are the labels in case of “Bank transfer payment: 
	 * * Beneficiario(beneficiary value) 
	 * * Codice IBAN (account IBAN) 
	 * * Importo (amount) 
	 * * Causale (description only if is present) 
	 * * Data esecuzione (date) 
	 * * Commissioni (fees) 

	 * @param mAccountsModel
	 * @param beneficiary
	 * @param iban
	 * @param amount
	 * @param description
	 * @param date
	 * @param fees
	 */
	public void showTranserEntry(AccountsModel mAccountsModel,String beneficiary,String iban,double amount,String description, long date,double fees,int titleType){
		setAccountsModel(mAccountsModel);
		setPageTitle(TransferType.TRANSFER_ENTRY.getPageTitleId(titleType));
		content_tl.removeAllViews();
		if(BaseActivity.isOffline){
			setText(R.string.beneficiary_tilte,beneficiary);
		}else{
			//this beneficiary is accountCode; use it filte getUserInfo --> accounts --> beneficiary title for show.
			for(AccountsModel _accountsModel : Contants.getUserInfo.getUserprofileHb().getAccountList()){
				if(_accountsModel.getAccountCode().equals(beneficiary)){
					beneficiary = _accountsModel.getTitle();
				}
			}
			setText(R.string.beneficiary_tilte,/*Contants.getUserInfo.getUserprofileHb().getCustomerName()*/ beneficiary);
		}
		setText(R.string.iban_tilte, iban);
		setText(R.string.amount_tilte,Utils.notPlusGenerateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
		if(!TextUtils.isEmpty(description)){
			setText(R.string.description_tilte, description);
		}
		setText(R.string.date_tilte,TimeUtil.getDateString(date,TimeUtil.dateFormat5));
		setText(R.string.fees_tilte,Utils.notPlusGenerateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), fees));
	}

	/**Title of the page: 
	 * * RIEPILOGO PAGAMENTORICARICA TELEFONICA These are the labels in case of “Bank transfer payment: 
	 * 
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
	public void showPhoneTopUp(AccountsModel mAccountsModel,String beneficiary,String phoneNumber,String provider,double amount,int titleType){
		setAccountsModel(mAccountsModel);
		setPageTitle(TransferType.PHONE_TOP_UP.getPageTitleId(titleType));
		content_tl.removeAllViews();
		setText(R.string.beneficiary_tilte, beneficiary);
		
		if(BaseActivity.isOffline){
			setText(R.string.phone_number, phoneNumber);
		}else{
		String certifiedNumber = Contants.getUserInfo.getUserprofileHb().getContactPhone();
		if(certifiedNumber != null && phoneNumber.equals(certifiedNumber)){
			setText(R.string.phone_number, Utils.maskCertifiedNumber(phoneNumber));
		} else {
			setText(R.string.phone_number, phoneNumber);
		}
		}

		setText(R.string.provider_fs, provider);
		setText(R.string.recharge_amount,Utils.notPlusGenerateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
	}
	
	/**Title of the page: 
	 * * RIEPILOGO CARTA PREPAGATA These are the labels in case of “Bank transfer payment: 
	 * * Beneficiario(beneficiary value) 
	 * * Carta numero (card number) 
	 * * Importo (amount) 
	 * * Causale (description only if is present) 
	 * * Commissioni (fees) 

	 * @param mAccountsModel
	 * @param amount
	 * @param beneficiary
	 * @param cardNumber
	 */
	public void showCardTopUp(AccountsModel mAccountsModel,String beneficiary,String cardNumber,double amount,String description,double fees,int titleType){
		setAccountsModel(mAccountsModel);
		setPageTitle(TransferType.CARD_TOP_UP.getPageTitleId(titleType));
		content_tl.removeAllViews();
		setText(R.string.beneficiary_tilte, beneficiary);
		setText(R.string.fs_card_number,cardNumber);
		setText(R.string.amount_tilte, Utils.notPlusGenerateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
		if(!TextUtils.isEmpty(description)){
			setText(R.string.description_tilte, description);
		}
		setText(R.string.fees_tilte,Utils.notPlusGenerateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), fees));
	}
	
	/**
	 * @param mAccountsModel
	 * @param mTransferObject
	 * @param amount
	 * @param description
	 * @param fees
	 * @param date
	 */
	public void show(AccountsModel mAccountsModel, TransferObject mTransferObject,double amount,String description,double fees,long date,int newPayment){
		content_tl.removeAllViews();
		if(mTransferObject==null){
			return;
		}
		if(mTransferObject instanceof TransferObjectTransfer){
			TransferObjectTransfer mRecentTransferModel=(TransferObjectTransfer) mTransferObject;
			showBankTranser(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryIban(), mRecentTransferModel.getBeneficiaryBic(), mRecentTransferModel.getBeneficiaryCUP(), mRecentTransferModel.getBeneficiaryCIG(), amount, description, mRecentTransferModel.getPurposeCurrency(), date, fees,newPayment);
		}else if(mTransferObject instanceof TransferObjectEntry){
			TransferObjectEntry mRecentTransferModel=(TransferObjectEntry) mTransferObject;
			showTranserEntry(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryIban(), amount, description, date, fees,newPayment);
		}else if(mTransferObject instanceof TransferObjectSim){
			TransferObjectSim mRecentTransferModel=(TransferObjectSim) mTransferObject;
			showPhoneTopUp(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryNumber(), mRecentTransferModel.getBeneficiaryProviderName(), amount,newPayment);
		}else if(mTransferObject instanceof TransferObjectCard){
			TransferObjectCard mRecentTransferModel=(TransferObjectCard) mTransferObject;
			showCardTopUp(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryCardNumber(), amount, description, fees,newPayment);
		}
	}
	
	public BPERPaymentConfirmPage(Context context){
		this.context=context;
		lf=LayoutInflater.from(context);
		lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		getContentView();
	}
	
	public View getContentView(){
		if(contentView ==null){
			contentView=(ViewGroup) lf.inflate(R.layout.bper_payment_confirm_page, null);
			mPaymentStepViewUtils=new PaymentStepViewUtils(contentView);
			mPaymentStepViewUtils.setStep(Step.STEP2);
			page_title=(TextView) contentView.findViewById(R.id.page_title);
			mAccountInfoTitle = (AccountInfoTitle) contentView.findViewById(R.id.account_title_info);
			mAccountInfoTitle.init(AccountInfoTitle.PAYMENT);
			content_tl=(TableLayout) contentView.findViewById(R.id.table);
			confirm_btn=(Button) contentView.findViewById(R.id.confirmation_btn);
			confirm_btn.setOnClickListener(askPinClickListener);
			
			//
			pinView=contentView.findViewById(R.id.bper_askpin_input);
			ask_pin_cancel_btn=(Button) pinView.findViewById(R.id.cancel_btn);
			ask_pin_continu_btn=(Button) pinView.findViewById(R.id.confirm_btn);
			ask_pin_cancel_btn.setOnClickListener(askPinClickListener);
			ask_pin_continu_btn.setOnClickListener(askPinClickListener);
			asp_pin_et=(EditText) pinView.findViewById(R.id.ask_ping_et);
			asp_pin_et.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					ask_pin_continu_btn.performClick();
					return false;
				}
			});
		}
		return contentView;
	}
	
	public void setPageTitle(int id){
		page_title.setText(id);
	}
	
	public void setAccountsModel(AccountsModel mAccountsModel){
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

	public void setText(int titleTextId,String txt){
		if(TextUtils.isEmpty(txt)){
			return;
		}
		TableRow beneficiary_tr=(TableRow) lf.inflate(R.layout.bper_payment_verification_item, null);
		TextView beneficiary_title_tv=(TextView) beneficiary_tr.findViewById(R.id.tv1);
		if(titleTextId == R.string.beneficiary_tilte){
			TextView beneficiary_value_tv=(TextView) beneficiary_tr.findViewById(R.id.tv2);
			beneficiary_value_tv.setSingleLine(false);
			beneficiary_value_tv.setEllipsize(TruncateAt.END);
			beneficiary_value_tv.setText(txt);
		}else{
			ReSizeSingleTextView beneficiary_value_tv=(ReSizeSingleTextView) beneficiary_tr.findViewById(R.id.tv2);
			beneficiary_value_tv.setReSizeSingleLine(titleTextId==R.string.iban_tilte);
			beneficiary_value_tv.setText(txt);
		}
		beneficiary_title_tv.setText(titleTextId);
		content_tl.addView(beneficiary_tr);
	}

	OnViewClickListener askPinClickListener=new OnViewClickListener() {
		@Override
		public void onClick(View v) {
			super.onClick(v);
			if(v==ask_pin_cancel_btn){
				hiddPin();
			}else if(v==ask_pin_continu_btn){
				hiddPin();
				if(mOnConfirmClickListener!=null){
					mOnConfirmClickListener.onClick(ask_pin_continu_btn);
				}
			}else if(v==confirm_btn){
				showPin();
				if(mOnAskPinClickListener!=null){
					mOnAskPinClickListener.onClick(confirm_btn);
				}
			}
		}
	};

	OnViewClickListener mOnAskPinClickListener;

	public void setOnAskPinClickListener(OnViewClickListener onAskPinClickListener){
		this.mOnAskPinClickListener=onAskPinClickListener;
	}

	OnViewClickListener mOnConfirmClickListener;

	public void setOnConfirmClickListener(OnViewClickListener mOnConfirmClickListener){
		this.mOnConfirmClickListener=mOnConfirmClickListener;
	}

	public void showAskPin(){
		showPin();
	}

	private void showPin(){
		pinView.setVisibility(View.VISIBLE);
		asp_pin_et.setText("");
		asp_pin_et.setFocusable(true);
		asp_pin_et.requestFocus();
		asp_pin_et.setFocusableInTouchMode(true);
		InputMethodManager inputManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(asp_pin_et, InputMethodManager.SHOW_FORCED);
	}
	public boolean hiddPin(){
		if(pinView!=null&&pinView.getVisibility()!=View.GONE){
			pinView.setVisibility(View.GONE);
			InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);  
			if(imm.isActive()){//isOpen若返回true，则表示输入法打开 
				imm.hideSoftInputFromWindow(asp_pin_et.getWindowToken(), 0); //强制隿藿键盘
			}
			return true;
		}
		return false;
	}
	
	public String getPinCode(){
		return asp_pin_et.getText().toString().trim();
	}

	@Override
	public boolean onBackPressed() {
		return hiddPin();
	}
}
