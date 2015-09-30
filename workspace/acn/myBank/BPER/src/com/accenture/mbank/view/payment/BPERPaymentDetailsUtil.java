package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.accenture.mbank.view.ReHeightImageButton;
import com.custom.view.ReSizeSingleTextView;

public class BPERPaymentDetailsUtil implements ViewManagerUtils{
	Context context;
	LayoutInflater lf;
	
	ViewGroup contentView;
	ReHeightImageButton detail_ibtn;
	BPERPaymentAccountSimpleUtils mBPERPaymentAccountSimpleUtils;
	TableLayout content_tl;
	
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
	public void showBankTranser(AccountsModel mAccountsModel,String beneficiary,String iban,String bic,String cup,String cig,double amount,String description,String purposeCurrency, long date,double fees){
		content_tl.removeAllViews();
		mBPERPaymentAccountSimpleUtils.setAccountsModel(mAccountsModel);
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
	public void showTranserEntry(AccountsModel mAccountsModel,String beneficiary,String iban,double amount,String description, long date,double fees){
		content_tl.removeAllViews();
		mBPERPaymentAccountSimpleUtils.setAccountsModel(mAccountsModel);
		setText(R.string.beneficiary_tilte, beneficiary);
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
	public void showPhoneTopUp(AccountsModel mAccountsModel,String beneficiary,String phoneNumber,String provider,double amount){
		content_tl.removeAllViews();
		mBPERPaymentAccountSimpleUtils.setAccountsModel(mAccountsModel);
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
	public void showCardTopUp(AccountsModel mAccountsModel,String beneficiary,String cardNumber,double amount,String description,double fees){
		content_tl.removeAllViews();
		mBPERPaymentAccountSimpleUtils.setAccountsModel(mAccountsModel);
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
	public void show(AccountsModel mAccountsModel, TransferObject mTransferObject,double amount,String description,double fees,long date){
		content_tl.removeAllViews();
		if(mTransferObject==null){
			return;
		}
		if(mTransferObject instanceof TransferObjectTransfer){
			TransferObjectTransfer mRecentTransferModel=(TransferObjectTransfer) mTransferObject;
			showBankTranser(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryIban(), mRecentTransferModel.getBeneficiaryBic(), mRecentTransferModel.getBeneficiaryCUP(), mRecentTransferModel.getBeneficiaryCIG(), amount, description, mRecentTransferModel.getPurposeCurrency(), date, fees);
		}else if(mTransferObject instanceof TransferObjectEntry){
			TransferObjectEntry mRecentTransferModel=(TransferObjectEntry) mTransferObject;
			showTranserEntry(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryIban(), amount, description, date, fees);
		}else if(mTransferObject instanceof TransferObjectSim){
			TransferObjectSim mRecentTransferModel=(TransferObjectSim) mTransferObject;
			showPhoneTopUp(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryNumber(), mRecentTransferModel.getBeneficiaryProviderName(), amount);
		}else if(mTransferObject instanceof TransferObjectCard){
			TransferObjectCard mRecentTransferModel=(TransferObjectCard) mTransferObject;
			showCardTopUp(mAccountsModel, mRecentTransferModel.getBeneficiaryName(), mRecentTransferModel.getBeneficiaryCardNumber(), amount, description, fees);
		}
	}
	
	public BPERPaymentDetailsUtil(Context context){
		this.context=context;
		lf=LayoutInflater.from(context);
		this.contentView=(ViewGroup) lf.inflate(R.layout.bper_payment_detail_page, null);
		//init view
		detail_ibtn=(ReHeightImageButton) contentView.findViewById(R.id.detail_ibtn);
		mBPERPaymentAccountSimpleUtils=new BPERPaymentAccountSimpleUtils(contentView);
		content_tl=(TableLayout) contentView.findViewById(R.id.content_tl);
	}
	
	public ViewGroup getContentView(){
		return contentView;
	}
	
	public void setText(int title,String txt){
		if(TextUtils.isEmpty(txt)){
			return;
		}
		TableRow beneficiary_tr=(TableRow) lf.inflate(R.layout.bper_payment_verification_item, null);
		TextView beneficiary_title_tv=(TextView) beneficiary_tr.findViewById(R.id.tv1);
		ReSizeSingleTextView beneficiary_value_tv=(ReSizeSingleTextView) beneficiary_tr.findViewById(R.id.tv2);
		beneficiary_value_tv.setReSizeSingleLine(title==R.string.iban_tilte);
		beneficiary_title_tv.setText(title);
		beneficiary_value_tv.setText(txt);
		content_tl.addView(beneficiary_tr);
	}
	
	public void setOnbackClickListener(View.OnClickListener onBackClickListener){
		detail_ibtn.setOnClickListener(onBackClickListener);
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
