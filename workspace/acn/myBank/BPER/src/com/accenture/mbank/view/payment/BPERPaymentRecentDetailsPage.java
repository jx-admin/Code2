package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectCard;
import com.accenture.mbank.logic.TransferObjectEntry;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.logic.TransferObjectTransfer;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.view.AccountInfoTitle;
import com.custom.view.ReSizeSingleTextView;

public class BPERPaymentRecentDetailsPage implements ViewManagerUtils{
	Context context;
	LayoutInflater lf;
	LayoutParams lp;
	
	ViewGroup contentView;
	AccountInfoTitle mAccountInfoTitle;
	TextView page_title;
	TableLayout content_tl;
	Button confirm_btn;
	View pinView;

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
		content_tl.removeAllViews();
		setText(R.string.beneficiary_tilte, beneficiary);
		setText(R.string.iban_tilte, iban);
		setText(R.string.amount_tilte,Utils.notPlusGenerateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
		if(!TextUtils.isEmpty(description)){
			setText(R.string.description_tilte, description);
		}
		setText(R.string.date_tilte,TimeUtil.getDateString(date,TimeUtil.dateFormat5));
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
		content_tl.removeAllViews();
		setText(R.string.beneficiary_tilte, beneficiary);
		setText(R.string.fs_card_number,cardNumber);
		setText(R.string.amount_tilte, Utils.notPlusGenerateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), amount));
		if(!TextUtils.isEmpty(description)){
			setText(R.string.description_tilte, description);
		}
	}
	
	/**
	 * @param mAccountsModel
	 * @param mTransferObject
	 */
	public void show(AccountsModel mAccountsModel, TransferObject mTransferObject,int newPayment){
		content_tl.removeAllViews();
		if(mTransferObject==null){
			return;
		}
		setAccountsModel(mAccountsModel);
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
	
	public BPERPaymentRecentDetailsPage(Context context){
		this.context=context;
		lf=LayoutInflater.from(context);
		lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		getContentView();
	}
	
	public View getContentView(){
		if(contentView ==null){
			contentView=(ViewGroup) lf.inflate(R.layout.bper_payment_recent_detail_page, null);
			mAccountInfoTitle = (AccountInfoTitle) contentView.findViewById(R.id.account_title_info);
			mAccountInfoTitle.init(AccountInfoTitle.PAYMENT);
			page_title=(TextView) contentView.findViewById(R.id.page_title);
			content_tl=(TableLayout) contentView.findViewById(R.id.table);
			confirm_btn=(Button) contentView.findViewById(R.id.confirmation_btn);
			
		}
		return contentView;
	}
	
	public void setPageTitle(int id){
		page_title.setText(id);
	}
	
	public void setAccountsModel(AccountsModel mAccountsModel){
		mAccountInfoTitle.accountName.setText(mAccountsModel .getAccountAlias());
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
		if(TextUtils.isEmpty(txt)){
			return;
		}
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
		ReSizeSingleTextView beneficiary_value_tv=(ReSizeSingleTextView) beneficiary_tr.findViewById(R.id.tv2);
		beneficiary_value_tv.setReSizeSingleLine(titleTextId==R.string.iban_tilte);
		beneficiary_title_tv.setText(titleTextId);
		beneficiary_value_tv.setText(txt);
		content_tl.addView(beneficiary_tr,lp);
	}

	public void setOnConfirmClickListener(OnClickListener mOnConfirmClickListener){
		confirm_btn.setOnClickListener(mOnConfirmClickListener);
	}
	
	@Override
	public boolean onBackPressed() {
		return false;
	}
}
