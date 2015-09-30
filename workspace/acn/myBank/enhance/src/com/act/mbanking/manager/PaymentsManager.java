package com.act.mbanking.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.act.mbanking.App;
import com.act.mbanking.Contants;
import com.act.mbanking.NewPaymentDataUtils;
import com.act.mbanking.R;
import com.act.mbanking.activity.BaseActivity;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.activity.NewPayee;
import com.act.mbanking.bean.Account;
import com.act.mbanking.bean.AccountsForServiceModel;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.AmountAvailable;
import com.act.mbanking.bean.BankRecipient;
import com.act.mbanking.bean.CardRecipient;
import com.act.mbanking.bean.CheckBillPaymentResponseModel;
import com.act.mbanking.bean.CheckBillPaymentValueModel;
import com.act.mbanking.bean.CheckRechargeCardResponseModel;
import com.act.mbanking.bean.CheckSimTopUpResponseModel;
import com.act.mbanking.bean.CheckTransferResponseModel;
import com.act.mbanking.bean.CompanyAmountResponseModel;
import com.act.mbanking.bean.DestaccountModel;
import com.act.mbanking.bean.GenerateOTPResponseModel;
import com.act.mbanking.bean.GetCardsResponseModel;
import com.act.mbanking.bean.GetRecipientListModel;
import com.act.mbanking.bean.InfoCardsModel;
import com.act.mbanking.bean.InsertBillPaymentModel;
import com.act.mbanking.bean.PendingTransferModel;
import com.act.mbanking.bean.PhoneRecipient;
import com.act.mbanking.bean.RecentTransferModel;
import com.act.mbanking.bean.ResponsePublicModel;
import com.act.mbanking.bean.SettingModel;
import com.act.mbanking.bean.SimTopUpResponseModel;
import com.act.mbanking.logic.CheckBillPaymentJson;
import com.act.mbanking.logic.CheckRechargeCardJson;
import com.act.mbanking.logic.CheckSimTopUpJson;
import com.act.mbanking.logic.CheckTransferJson;
import com.act.mbanking.logic.CompanyAmountJson;
import com.act.mbanking.logic.GenerateOTPJson;
import com.act.mbanking.logic.GetCardsJson;
import com.act.mbanking.logic.InsertBillPaymentJson;
import com.act.mbanking.logic.InsertRechargeCardJson;
import com.act.mbanking.logic.InsertRecipientJson;
import com.act.mbanking.logic.InsertTransferJson;
import com.act.mbanking.logic.SimTopUpJson;
import com.act.mbanking.manager.view.adapter.CoverFlowImageAdapter;
import com.act.mbanking.manager.view.adapter.NPAccountsModelAdapter;
import com.act.mbanking.manager.view.adapter.NPBankRecipientAdapter;
import com.act.mbanking.manager.view.adapter.NPCardRecipientAdapter;
import com.act.mbanking.manager.view.adapter.NPPhoneRecipientAdapter;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.utils.AmountItalyInputFilter;
import com.act.mbanking.utils.DialogManager;
import com.act.mbanking.utils.KeyBoardUtils;
import com.act.mbanking.utils.LogManager;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.custom.view.CoverFlow;
import com.custom.view.CoverFlow.OnCoverFlowItemSelectedListener;
import com.custom.view.CustomEditText;
import com.custom.view.CustomEditText.OnKeyPreIme;
import com.custom.view.ResizeLayout;
import com.custom.view.ResizeLayout.OnResizeListener;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

/**
 * @author junxu.wang
 */
public class PaymentsManager implements OnClickListener,
		OnItemSelectedListener, OnItemClickListener,
		OnCoverFlowItemSelectedListener {
	public static final String TAG = "PaymentsManager";

	public static final int GET_ACCOUNT = 0;

	public static final int GET_BANK_TRANSFER = 1;

	public static final int GET_TRANSFER_ENTRY = 2;

	public static final int GET_CARD_TOP_UP = 3;

	public static final int GET_PHONE_TOP_UP = 4;

	public static final int GET_RECIPIENTLISTMODEL = 5;

	public static final int GET_ASKPIN = 6;

	public static final int VALIDATE = 7;

	public static final int CONFIRM = 8;

	public static final int GET_AMOUNT_AVAILABLE = 9;

	public static final int VERTIFY_CARD = 10;

	public static final int INSERTRECIPIENT = 11;

	public static final int GET_INIT_DATA = 12;

	public static final int TYPE_UPDATE_BALANCE = 13;

	/**
	 * after clicking on the menu item "new payments", the page displayed ,It's
	 * required that the account list is visible when accessing the page.
	 * <p>
	 * [New Payments UX - Android:1]
	 * <p>
	 * So,PTYPE_NULL=default(BANK_TRANSFER)
	 */
	public static final int BANK_TRANSFER = 1, TRANSFER_ENTRY = 2,
			CARD_TOP_UP = 3, PHONE_TOP_UP = 4, PRECOMPILED_BILL = 5,
			BLANK_BILL = 6, MAV_RAV = 7, PTYPE_NULL = 0;

	private static final int STEP_1 = R.id.new_payments_step1_rb,
			STEP_2 = R.id.new_payments_step2_rb,
			STEP_3 = R.id.new_payments_step3_rb;

	public static final int FORM_DEFAULT = 0, FORM_RECENT_PAYMENT = 1,
			FORM_ACCOUNT = 2, FORM_PENDING_PAYMENT = 3;

	private ArrayAdapter<String> type_payment_ad;

	private NPAccountsModelAdapter account_folders_cf_ad;

	private NPPhoneRecipientAdapter phone_payee_cf_ad;

	private NPCardRecipientAdapter card_payee_cf_ad;

	private NPAccountsModelAdapter entry_payee_cf_ad;

	private NPBankRecipientAdapter bank_payee_cf_ad;

	private ResizeLayout payment_lin;

	private LinearLayout new_payments1_clu;

	private RadioGroup new_payments_step_rg;

	private View payment_type_lin;

	private GoogleAnalytics mGaInstance;

	private Tracker mGaTracker1;

	/**
	 * the graphic of the menu list "--Type of payment" is different from the
	 * requested. It's requested a graphic like iOS application (no radio button
	 * list).
	 * <p>
	 * [New Payments UX - Android:2]
	 * <p>
	 * canceled
	 */
	private Spinner payment_type_sp;

	private CoverFlow account_folders_cf;

	private CoverFlow payee_folders_cf;

	private CoverFlow phone_payee_folders_cf;

	private RadioGroup top_up_amount_gv;

	private LinearLayout amountbar_lin;

	private CustomEditText amount_et,amount_et2;

	private LinearLayout execution_date_lin;

	private EditText execution_date_et;

	private EditText description_et;

	private LinearLayout proceed_lin;

	private Button proceed_btn;

	private TextView result_tv;

	private LinearLayout pin_lin;

	private EditText pin_et;

	private boolean confirmResult;

	ProgressDialog downloading_pd;

	private List<AmountAvailable> mAmountAvailableLs;

	private int amountAvailableIndex = -1;

	GenerateOTPResponseModel generateOtp;

	public static final String dateFormate = "%d/%d/%d";

	private long mTimeInMillis;

	private double amount, fee;

	private AccountsModel curPayer;

	private Object curPayee;

	private int curStep, curPaymentType, curPayerAccoutSeltectId,
			curPayeeSelectId;

	private String curDescription;

	LayoutInflater mLayoutInflater;

	private InfoCardsModel tmp_InfoCardsModel;

	private int tmp_isVerifyCard;

	public static final int VERIFYCARD_INITAL = 0, VERIFYCARD_FAILED = 1,
			VERIFYCARD_SUCCESS = 2;

	private Serializable tmp_payee;

	private Serializable tmp_payee_phone;

	private boolean isTransferByPhone = false;

	private BaseActivity activity;

	private ViewGroup layout;

	private int showForm = PaymentsManager.FORM_DEFAULT;

	private RecentTransferModel recentTransferModel;
	private PendingTransferModel mPendingTransferModel;

	private View payer_detail_lin, payee_detail_lin;

	private TextView payer_detail_title_tv, payer_detail_context_tv,
			payee_detail_title_tv, payee_detail_context_tv;

	/**
	 * flag for amount inputing.
	 * 
	 * @value true if inputing, or false.
	 */
	private boolean inputing;

	// Check transaction requast
	private CheckTransferResponseModel checkTransfer;

	private CheckSimTopUpResponseModel checkSimTopUp;

	private CheckRechargeCardResponseModel checkRechargeCard;

	//

	private PaymentsManagerBill mPaymentsManagerBill;
	private ViewGroup new_payment_bill;
	private CheckBillPaymentResponseModel mCheckBillPaymentResponseModel;

	private PaymentsManagerBlankBill mPaymentsManagerBlankBill;
	private ViewGroup new_payment_blank_bill;

	private PaymentsManagerMAVRAV mPaymentsManagerMAVRAV;
	private ViewGroup new_payment_mav_rav_bill;

	public PaymentsManager(BaseActivity activity2) {
		mLayoutInflater = LayoutInflater.from(activity2);
		this.activity = activity2;
	}

	/**
	 * @param showForm
	 *            {@link #FORM_DEFAULT} {@link #FORM_RECENT_PAYMENT}
	 *            {@link #FORM_ACCOUNT}{@link #FORM_PENDING_PAYMENT}
	 * @param account
	 *            the payer
	 * @param destAccount
	 *            the payee
	 * @param time
	 *            execution date
	 * @param amount
	 *            amount for payment
	 * @param paymentType
	 *            {@link #BANK_TRANSFER} {@link #TRANSFER_ENTRY}
	 *            {@link #CARD_TOP_UP} {@link #PHONE_TOP_UP}
	 */
	public void setRecover(int showForm, AccountsModel account,
			DestaccountModel destAccount, long time, double amount,
			int paymentType) {
		this.showForm = showForm;
		this.curPaymentType = paymentType;
		curPayer = account;
		curPayee = destAccount;
		setDate(time);
		setAmount(amount);
	}

	public void setRecentRecover(int showForm, Serializable data,
			AccountsModel accountModel) {
		this.showForm = showForm;
		curPayer = accountModel;
		int paymentType = 0;

		if (data instanceof PendingTransferModel) {
			mPendingTransferModel = (PendingTransferModel) data;
			setAmount(mPendingTransferModel.getAmount());
        	String beneficiaryName=mPendingTransferModel.getBeneficiaryCardName();
        	if(TextUtils.isEmpty(beneficiaryName)){
        		beneficiaryName=mPendingTransferModel.getBeneficiaryTitle();
            }
        	
			if (Contants.SIM_TOP_UP.equals(mPendingTransferModel.getType())) {
				paymentType = PaymentsManager.PHONE_TOP_UP;
				PhoneRecipient pr = new PhoneRecipient();
				// pr.setId()
				pr.setName(beneficiaryName);
				pr.setPhoneNumber(mPendingTransferModel
						.getBeneficiaryPhoneNumber());
				pr.setProvider(mPendingTransferModel.getBeneficiaryProvider());
				curPayee = pr;
				setDate(System.currentTimeMillis());
			} else if (Contants.TRANSFER_ENTRY.equals(mPendingTransferModel
					.getType())) {
				paymentType = PaymentsManager.TRANSFER_ENTRY;
				DestaccountModel dm = new DestaccountModel();
				dm.setIban(mPendingTransferModel.getBeneficiaryIban());
				dm.setState(mPendingTransferModel.getBeneficiaryCardCode());
				dm.setTitle(beneficiaryName);
				curPayee = dm;
				setDate(System.currentTimeMillis());
			} else if (Contants.BANK_TRANSFER.equals(mPendingTransferModel
					.getType())) {
				paymentType = PaymentsManager.BANK_TRANSFER;
				DestaccountModel dm = new DestaccountModel();
				dm.setIban(mPendingTransferModel.getBeneficiaryIban());
				dm.setState(mPendingTransferModel.getBeneficiaryCardCode());
				dm.setTitle(beneficiaryName);
				curPayee = dm;

				// BankRecipient br = new BankRecipient();
				// br.setId(id)
				// br.setBic(recentTransferModel.get)
				// br.setIbanCode(recentTransferModel.getBeneficiaryIban());
				// br.setName(recentTransferModel.getBeneficiaryName());
				// curPayee = br;
				setDate(System.currentTimeMillis());
			} else if (Contants.PREPAID_CARD_RELOAD
					.equals(mPendingTransferModel.getType())) {
				paymentType = PaymentsManager.CARD_TOP_UP;
				CardRecipient cr = new CardRecipient();
				cr.setCardNumber(mPendingTransferModel
						.getBeneficiaryCardNumber());
				cr.setName(beneficiaryName);
				// cr.setId(id)
				curPayee = cr;
				setDate(System.currentTimeMillis());
			}else if(PaymentsManagerBill._674
					.equals(mPendingTransferModel.getBillType())||PaymentsManagerBill._896
					.equals(mPendingTransferModel.getBillType())){
				paymentType=PRECOMPILED_BILL;
				mPaymentsManagerBill.reCover(mPendingTransferModel);
			}else if(PaymentsManagerBlankBill._123
					.equals(mPendingTransferModel.getBillType())||PaymentsManagerBlankBill._451
					.equals(mPendingTransferModel.getBillType())){
				paymentType=BLANK_BILL;
				mPaymentsManagerBlankBill.reCover(mPendingTransferModel);
			}else if(PaymentsManagerMAVRAV.MAV
					.equals(mPendingTransferModel.getBillType())||PaymentsManagerMAVRAV.RAV
					.equals(mPendingTransferModel.getBillType())){
				paymentType=MAV_RAV;
				mPaymentsManagerMAVRAV.reCover(mPendingTransferModel);
			}
			this.curPaymentType = paymentType;
//			setDate(System.currentTimeMillis());
			this.curDescription = mPendingTransferModel.getPurposeDescription();
		} else {
			RecentTransferModel recentTransferModel = (RecentTransferModel) data;
			// AccountsModel payer = new AccountsModel();
			// payer.setAccountAlias(recentTransferModel.getAccount());
			setAmount(recentTransferModel.getAmount());

			if (Contants.SIM_TOP_UP.equals(recentTransferModel.getType())) {
				paymentType = PaymentsManager.PHONE_TOP_UP;
				PhoneRecipient pr = new PhoneRecipient();
				// pr.setId()
				pr.setName(recentTransferModel.getBeneficiaryName());
				pr.setPhoneNumber(recentTransferModel.getBeneficiaryNumber());
				pr.setProvider(recentTransferModel.getBeneficiaryProvider());
				curPayee = pr;
				setDate(System.currentTimeMillis());
			} else if (Contants.TRANSFER_ENTRY.equals(recentTransferModel
					.getType())) {
				paymentType = PaymentsManager.TRANSFER_ENTRY;
				DestaccountModel dm = new DestaccountModel();
				dm.setIban(recentTransferModel.getBeneficiaryIban());
				dm.setState(recentTransferModel.getBeneficiaryState());
				dm.setTitle(recentTransferModel.getBeneficiaryName());
				curPayee = dm;
				setDate(System.currentTimeMillis());
			} else if (Contants.BANK_TRANSFER.equals(recentTransferModel
					.getType())) {
				paymentType = PaymentsManager.BANK_TRANSFER;
				DestaccountModel dm = new DestaccountModel();
				dm.setIban(recentTransferModel.getBeneficiaryIban());
				dm.setState(recentTransferModel.getBeneficiaryState());
				dm.setTitle(recentTransferModel.getBeneficiaryName());
				curPayee = dm;

				// BankRecipient br = new BankRecipient();
				// br.setId(id)
				// br.setBic(recentTransferModel.get)
				// br.setIbanCode(recentTransferModel.getBeneficiaryIban());
				// br.setName(recentTransferModel.getBeneficiaryName());
				// curPayee = br;
				setDate(System.currentTimeMillis());
			} else if (Contants.PREPAID_CARD_RELOAD.equals(recentTransferModel
					.getType())) {
				paymentType = PaymentsManager.CARD_TOP_UP;
				CardRecipient cr = new CardRecipient();
				cr.setCardNumber(recentTransferModel.getBeneficiaryCardNumber());
				cr.setName(recentTransferModel.getBeneficiaryName());
				// cr.setId(id)
				curPayee = cr;
				setDate(System.currentTimeMillis());
			}else if(PaymentsManagerBill._674
					.equals(recentTransferModel.getBillType())||PaymentsManagerBill._896
					.equals(recentTransferModel.getBillType())){
				paymentType=PRECOMPILED_BILL;
				mPaymentsManagerBill.reCover(recentTransferModel);
			}else if(PaymentsManagerBlankBill._123
					.equals(recentTransferModel.getBillType())||PaymentsManagerBlankBill._451
					.equals(recentTransferModel.getBillType())){
				paymentType=BLANK_BILL;
				mPaymentsManagerBlankBill.reCover(recentTransferModel);
			}else if(PaymentsManagerMAVRAV.MAV
					.equals(recentTransferModel.getBillType())||PaymentsManagerMAVRAV.RAV
					.equals(recentTransferModel.getBillType())){
				paymentType=MAV_RAV;
				mPaymentsManagerMAVRAV.reCover(recentTransferModel);
			}
			this.curPaymentType = paymentType;
			this.curDescription = recentTransferModel.getDescription();
		}
	}

	int oldPosition = -1;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.act.mbanking.manager.MainMenuSubScreenManager#init()
	 */
	public ViewGroup init() {
		layout = (ViewGroup) activity.findViewById(R.id.new_payment);

		payment_lin = (ResizeLayout) layout.findViewById(R.id.payment_lin);
		payment_lin.setOnResizeListener(new OnResizeListener() {

			@Override
			public void OnResize(int w, int h, int oldw, int oldh) {
				Log.d(TAG, "on Resize:" + (h - oldh));
				if (h > oldh) {
					checkAmountView();
				}
			}
		});
		new_payments1_clu = (LinearLayout) layout
				.findViewById(R.id.new_payments1_clu);

		new_payments_step_rg = (RadioGroup) layout
				.findViewById(R.id.new_payments_step_rg);

		payment_type_lin = layout.findViewById(R.id.payment_type_lin);
		payment_type_sp = (Spinner) layout.findViewById(R.id.payment_type_sp);
		payment_type_sp.setOnItemSelectedListener(this);
		type_payment_ad = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, activity.getResources()
						.getStringArray(R.array.new_payments_types));
		type_payment_ad
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		payment_type_sp.setAdapter(type_payment_ad);

		account_folders_cf_ad = new NPAccountsModelAdapter(activity);
		account_folders_cf_ad.setViewId(R.layout.account_data_opened_content);
		account_folders_cf_ad.setAddable(false);
		entry_payee_cf_ad = new NPAccountsModelAdapter(activity);
		entry_payee_cf_ad.setViewId(R.layout.account_data_closed_item);
		bank_payee_cf_ad = new NPBankRecipientAdapter(activity);
		card_payee_cf_ad = new NPCardRecipientAdapter(activity);
		phone_payee_cf_ad = new NPPhoneRecipientAdapter(activity);

		account_folders_cf = (CoverFlow) activity
				.findViewById(R.id.account_folders_cf);
		account_folders_cf.setAdapter(account_folders_cf_ad);
		account_folders_cf.setAnimationDuration(500);
        account_folders_cf_ad.setOnItemClickListener(this);
        account_folders_cf_ad.setOnItemSelectedListener(this);

		phone_payee_folders_cf = (CoverFlow) activity
				.findViewById(R.id.phone_payee_folders_cf);
		phone_payee_folders_cf.setAdapter(phone_payee_cf_ad);
		phone_payee_folders_cf.setAnimationDuration(500);
        phone_payee_cf_ad.setOnItemClickListener(this);
        phone_payee_cf_ad.setOnItemSelectedListener(this);

		payee_folders_cf = (CoverFlow) activity
				.findViewById(R.id.payee_folders_cf);
		// payee_folders_cf.setAdapter(card_folders_cf_ad);
		payee_folders_cf.setAnimationDuration(500);
        card_payee_cf_ad.setOnItemClickListener(this);
        card_payee_cf_ad.setOnItemSelectedListener(this);
        entry_payee_cf_ad.setOnItemClickListener(this);
        entry_payee_cf_ad.setOnItemSelectedListener(this);
        bank_payee_cf_ad.setOnItemClickListener(this);
        bank_payee_cf_ad.setOnItemSelectedListener(this);

		amountbar_lin = (LinearLayout) layout.findViewById(R.id.amountbar_lin);
		amountbar_lin.setOnClickListener(this);
		amount_et = (CustomEditText) amountbar_lin.findViewById(R.id.amount_doller_et);
		amount_et2 = (CustomEditText) amountbar_lin.findViewById(R.id.amount_et2);
		amount_et.setOnClickListener(this);
		amount_et.setOnKeyPreIme(new OnKeyPreIme() {

			@Override
			public boolean onKeyPreIme(int keyCode, KeyEvent event) {
				checkAmountView();
				return false;
			}
		});
		amount_et2.setOnClickListener(this);
		amount_et2.setOnKeyPreIme(new OnKeyPreIme() {

			@Override
			public boolean onKeyPreIme(int keyCode, KeyEvent event) {
				checkAmountView();
				return false;
			}
		});
//		amount_et .setFilters(new InputFilter[] { new AmountItalyInputFilter() });
		amount_et.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event == null) {
					Log.d(TAG, "onEditorAction is null ,onEditorAction "
							+ actionId);
				} else {
					Log.d(TAG, "onEditorAction:onEditorAction" + actionId
							+ "key:" + event.getKeyCode());
				}
				checkAmountView();
				return false;
			}
		});
		amount_et2.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event == null) {
					Log.d(TAG, "onEditorAction is null ,onEditorAction "
							+ actionId);
				} else {
					Log.d(TAG, "onEditorAction:onEditorAction" + actionId
							+ "key:" + event.getKeyCode());
				}
				checkAmountView();
				return false;
			}
		});

		top_up_amount_gv = (RadioGroup) layout
				.findViewById(R.id.top_up_amount_gv);

		execution_date_lin = (LinearLayout) layout
				.findViewById(R.id.execution_date_lin);
		execution_date_et = (EditText) layout
				.findViewById(R.id.execution_date_et);
		execution_date_et.setOnClickListener(this);// setOnFocusChangeListener(this);

		description_et = (EditText) layout.findViewById(R.id.description_et);

		proceed_lin = (LinearLayout) layout.findViewById(R.id.proceed_lin);
		proceed_btn = (Button) layout.findViewById(R.id.proceed_btn);
		proceed_btn.setOnClickListener(this);

		pin_lin = (LinearLayout) layout.findViewById(R.id.pin_lin);
		pin_et = (EditText) layout.findViewById(R.id.pin_et);

		result_tv = (TextView) layout.findViewById(R.id.result_tv);

		payer_detail_lin = layout.findViewById(R.id.payer_detail_lin);
		payee_detail_lin = layout.findViewById(R.id.payee_detail_lin);
		payer_detail_title_tv = (TextView) layout
				.findViewById(R.id.payer_detail_title_tv);
		payer_detail_context_tv = (TextView) layout
				.findViewById(R.id.payer_detail_context_tv);
		payee_detail_title_tv = (TextView) layout
				.findViewById(R.id.payee_detail_title_tv);
		payee_detail_context_tv = (TextView) layout
				.findViewById(R.id.payee_detail_context_tv);

		downloading_pd = new ProgressDialog(activity);
		downloading_pd.setTitle(R.string.downloading);

		new_payment_bill = (ViewGroup) layout.findViewById(R.id.payment_bill);
		mPaymentsManagerBill = new PaymentsManagerBill(activity,
				new_payment_bill);

		new_payment_blank_bill = (ViewGroup) layout
				.findViewById(R.id.payment_blank_bill);
		mPaymentsManagerBlankBill = new PaymentsManagerBlankBill(activity,
				new_payment_blank_bill);

		new_payment_mav_rav_bill = (ViewGroup) layout
				.findViewById(R.id.payment_mav_rav);
		mPaymentsManagerMAVRAV = new PaymentsManagerMAVRAV(activity,
				new_payment_mav_rav_bill);
		return layout;
	}

	private void reset() {

		tmp_isVerifyCard = VERIFYCARD_INITAL;
		tmp_InfoCardsModel = null;

		if (showForm == PaymentsManager.FORM_RECENT_PAYMENT
				|| showForm == FORM_PENDING_PAYMENT) {
			// new_payments_step_rg
			payment_lin.setVisibility(View.VISIBLE);
			showPaymentType(View.GONE);

			showPayer(View.VISIBLE);
			showBillLayout(View.GONE);
			showBlackBillLayout(View.GONE);
			showMavRavLayout(View.GONE);
			showPayee(View.VISIBLE);
			showAmount(View.VISIBLE, amount);

			if (getPaymentType() != CARD_TOP_UP
					&& getPaymentType() != PHONE_TOP_UP) {
				showDate(View.VISIBLE, mTimeInMillis);
			} else {
				showDate(View.GONE, mTimeInMillis);
			}
			if (getPaymentType() != PHONE_TOP_UP) {
				showDescription(View.VISIBLE, curDescription);
			} else {
				showDescription(View.GONE, curDescription);
			}
			//
			new_payments1_clu.setVisibility(View.GONE);
			showPinInput(View.GONE);
			//
			proceed_lin.setVisibility(View.VISIBLE);
			//
			result_tv.setVisibility(View.GONE);

			AccountsModel am = getPayer();
			payer_detail_title_tv.setText(am.getAccountAlias());
			payer_detail_context_tv.setVisibility(View.GONE);

			switch (getPaymentType()) {
			case BANK_TRANSFER:
				Object payee1 = getPayee();
				if (payee1 instanceof AccountsModel) {
					AccountsModel br = (AccountsModel) payee1;
					payee_detail_title_tv.setText(R.string._phone_number);
					payee_detail_context_tv.setText(br.getPhoneNumber());
				} else {
					DestaccountModel br = generateDestAccountModel();
					payee_detail_title_tv.setText(br.getTitle());
					payee_detail_context_tv.setText(br.getIban());
				}
				break;
			case TRANSFER_ENTRY:
				DestaccountModel payee = generateDestAccountModel();
				payee_detail_title_tv.setText(payee.getTitle());
				payee_detail_context_tv.setText(payee.getIban());
				break;
			case CARD_TOP_UP:
				CardRecipient cr = (CardRecipient) getPayee();
				payee_detail_title_tv.setText(cr.getName());
				payee_detail_context_tv.setText(cr.getCardNumber());
				break;
			case PHONE_TOP_UP:
				PhoneRecipient pr = (PhoneRecipient) getPayee();
				payee_detail_title_tv.setText(pr.getName());
				payee_detail_context_tv.setText(pr.getPhoneNumber());
				break;
			case PRECOMPILED_BILL:// = 5,
				showBillLayout(View.VISIBLE);
				showBlackBillLayout(View.GONE);
				showMavRavLayout(View.GONE);
				showPayee(View.GONE);
				showAmount(View.GONE);
				showDate(View.GONE);
				showDescription(View.GONE);
				break;
			case BLANK_BILL:// = 6, 
				showBillLayout(View.GONE);
				showBlackBillLayout(View.VISIBLE);
				showMavRavLayout(View.GONE);
				showPayee(View.GONE);
				showAmount(View.GONE);
				showDate(View.GONE);
				showDescription(View.GONE);
				break;
			case MAV_RAV:// = 7
				showBillLayout(View.GONE);
				showBlackBillLayout(View.GONE);
				showMavRavLayout(View.VISIBLE);
				showPayee(View.GONE);
				showAmount(View.GONE);
				showDate(View.GONE);
				showDescription(View.GONE);
				break;
			}

		} else if (showForm == FORM_ACCOUNT) {
			// new_payments_step_rg
			payment_lin.setVisibility(View.VISIBLE);
			showPaymentType(View.VISIBLE, PTYPE_NULL);

			showPayer(View.VISIBLE);
			showPayee(View.GONE);
			showBillLayout(View.GONE);
			showBlackBillLayout(View.GONE);
			showMavRavLayout(View.GONE);
			showAmount(View.GONE, 0);
			showDate(View.GONE, 0);
			showDescription(View.GONE, "");
			//
			new_payments1_clu.setVisibility(View.GONE);
			showPinInput(View.GONE);
			//
			proceed_lin.setVisibility(View.VISIBLE);
			//
			result_tv.setVisibility(View.GONE);

			AccountsModel am = getPayer();
			payer_detail_title_tv.setText(am.getAccountAlias());
			payer_detail_context_tv.setVisibility(View.INVISIBLE);
		} else {
			// new_payments_step_rg
			payment_lin.setVisibility(View.VISIBLE);
			showPaymentType(View.VISIBLE, PTYPE_NULL);
			showPayer(View.VISIBLE);
			showBillLayout(View.GONE);
			showBlackBillLayout(View.GONE);
			showMavRavLayout(View.GONE);
			showPayee(View.GONE);
			showAmount(View.GONE, 0);
			showDate(View.GONE, 0);
			showDescription(View.GONE, "");
			//
			new_payments1_clu.setVisibility(View.GONE);
			showPinInput(View.GONE);
			//
			proceed_lin.setVisibility(View.VISIBLE);
			//
			result_tv.setVisibility(View.GONE);

		}
	}

	private void showProceedButton(int visibility, int textId) {
		if (proceed_lin.getVisibility() != visibility) {
			proceed_lin.setVisibility(visibility);
		}
		if (textId > 0) {
			proceed_btn.setText(textId);
		}
	}

	private void setProceedButtonClickAble(boolean able) {
		proceed_btn.setClickable(able);
	}

	private void invilidataStep(int step) {
		switch (step) {
		case STEP_1:
			break;
		case STEP_2:
			/**
			 * <pre>
			 *                      - upper blu box --> It's requested to show always (in all type of payments) only the account name 
			 *                      - lower blu box -->  in case of bank transfer --> It's requested to show beneficiary name and IBAN
			 *                      - lower blu box -->  in case of transfer entry --> It's requested to show beneficiary name and IBAN
			 *                      - lower blu box -->  in case of card top up --> It's requested to show beneficiary name and card number
			 * - lower blu box -->  in case of phone top up --> It's requested to show beneficiary name and phone number
			 * */
			AccountsModel am = getPayer();
			TextView payment_account_type_tv = (TextView) layout
					.findViewById(R.id.payment_account_type_tv);
			payment_account_type_tv.setText(am.getAccountAlias());
			TextView payment_account_info_tv = (TextView) layout
					.findViewById(R.id.payment_account_info_tv);
			payment_account_info_tv.setVisibility(View.INVISIBLE);
			// payment_account_info_tv.setText(am.getCardName() + ", " +
			// am.getIbanCode() + ", "
			// + am.getAccountId());

			TextView payment_amount_tv = (TextView) layout
					.findViewById(R.id.payment_amount_tv);
			payment_amount_tv.setText(Utils.formatMoney(this.getAmount(),
					activity.getResources().getString(R.string.dollar), true,
					true, false, false, true));

			switch (getPaymentType()) {
			case BANK_TRANSFER:
				TextView payee_account_type_tv = (TextView) layout
						.findViewById(R.id.payee_account_type_tv);
				TextView payee_account_info_tv = (TextView) layout
						.findViewById(R.id.payee_account_info_tv);
				Object payee1 = getPayee();
				if (payee1 instanceof AccountsModel) {
					AccountsModel br = (AccountsModel) payee1;
					payee_account_type_tv.setText(R.string._phone_number);
					payee_account_info_tv.setText(br.getPhoneNumber());
				} else {
					DestaccountModel br = generateDestAccountModel();
					if (br == null) {
						Toast.makeText(activity, "select correct payee!",
								Toast.LENGTH_SHORT).show();
						return;
					}
					payee_account_type_tv.setText(br.getTitle());
					payee_account_info_tv.setText(br.getIban());
				}
				break;
			case TRANSFER_ENTRY:

				payee_account_type_tv = (TextView) layout
						.findViewById(R.id.payee_account_type_tv);
				DestaccountModel payee = generateDestAccountModel();
				if (payee == null) {
					Toast.makeText(activity, "select correct payee!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				payee_account_type_tv.setText(payee.getTitle());
				payee_account_info_tv = (TextView) layout
						.findViewById(R.id.payee_account_info_tv);
				payee_account_info_tv.setText(payee.getIban());
				break;
			case CARD_TOP_UP:
				payee_account_type_tv = (TextView) layout
						.findViewById(R.id.payee_account_type_tv);
				CardRecipient cr = (CardRecipient) getPayee();
				payee_account_type_tv.setText(cr.getName());
				payee_account_info_tv = (TextView) layout
						.findViewById(R.id.payee_account_info_tv);
				payee_account_info_tv.setText(cr.getCardNumber());
				break;
			case PHONE_TOP_UP:
				payee_account_type_tv = (TextView) layout
						.findViewById(R.id.payee_account_type_tv);
				PhoneRecipient pr = (PhoneRecipient) getPayee();
				payee_account_type_tv.setText(pr.getName());
				payee_account_info_tv = (TextView) layout
						.findViewById(R.id.payee_account_info_tv);
				payee_account_info_tv.setText(pr.getPhoneNumber());
				break;
			case PRECOMPILED_BILL:
				payee_account_type_tv = (TextView) layout
						.findViewById(R.id.payee_account_type_tv);
				payee_account_type_tv.setText(mPaymentsManagerBill
						.getPayableTo());
				payee_account_info_tv = (TextView) layout
						.findViewById(R.id.payee_account_info_tv);
				payee_account_info_tv.setText(mPaymentsManagerBill
						.getBillNumber());
				break;
			case BLANK_BILL:
				payee_account_type_tv = (TextView) layout
				.findViewById(R.id.payee_account_type_tv);
				payee_account_type_tv.setText(R.string.account_number1);
				payee_account_info_tv = (TextView) layout
						.findViewById(R.id.payee_account_info_tv);
				payee_account_info_tv.setText(mPaymentsManagerBlankBill.getAccountNumber());
		break;
			case MAV_RAV:
				payee_account_type_tv = (TextView) layout
						.findViewById(R.id.payee_account_type_tv);
				payee_account_type_tv.setText(R.string.bill_number);
				payee_account_info_tv = (TextView) layout
						.findViewById(R.id.payee_account_info_tv);
				payee_account_info_tv.setText(mPaymentsManagerMAVRAV
						.getBillNumber());

				break;
			}
			break;
		case STEP_3:
			mGaInstance = GoogleAnalytics.getInstance(activity);
			mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
			if (confirmResult) {
				mGaTracker1.sendView("view.newpayment.executed");
				Drawable icon = activity.getResources().getDrawable(
						R.drawable.ok);
				icon.setBounds(0, 0, icon.getMinimumWidth(),
						icon.getMinimumHeight());
				result_tv.setText(R.string.payment_executed_successfully);
				result_tv.setCompoundDrawables(icon, null, null, null);
			} else {
				mGaTracker1.sendView("view.newpayment.failed");
				// layout.findViewById(R.id.result2_tv).setVisibility(View.VISIBLE);
				result_tv.setText(R.string.payment_transaction_failed);
				Drawable icon = activity.getResources().getDrawable(
						R.drawable.error);
				icon.setBounds(0, 0, icon.getMinimumWidth(),
						icon.getMinimumHeight());
				result_tv.setCompoundDrawables(icon, null, null, null);
			}
			break;
		}
	}

	private void showPaymentStep(int step) {
		if (new_payments_step_rg.getCheckedRadioButtonId() == step) {
			return;
		}
		switch (step) {
		case STEP_1:
			new_payments_step_rg.check(step);
			payment_lin.setVisibility(View.VISIBLE);
			new_payments1_clu.setVisibility(View.GONE);
			showProceedButton(View.VISIBLE, R.string.proceed);
			showPinInput(View.GONE);
			result_tv.setVisibility(View.GONE);
			tmp_isVerifyCard = VERIFYCARD_INITAL;
			break;
		case STEP_2:
			new_payments_step_rg.check(step);
			payment_lin.setVisibility(View.GONE);
			new_payments1_clu.setVisibility(View.VISIBLE);
			showProceedButton(View.VISIBLE, R.string.verify_data);
			showPinInput(View.GONE);
			result_tv.setVisibility(View.GONE);
			break;
		case STEP_3:
			new_payments_step_rg.check(step);
			payment_lin.setVisibility(View.GONE);
			new_payments1_clu.setVisibility(View.GONE);
			proceed_lin.setVisibility(View.GONE);
			result_tv.setVisibility(View.VISIBLE);
			break;
		}
	}

	private void showLeftNavigation(boolean visibility) {
		// if (visibility) {
		// activity.setLeftNavigationText(R.string.payments);
		// } else {
		// setLeftNavigationText("");
		// }
	}

	public static final String detailsFormate = "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s";

	public static final String detailsFormateBankTransfer = "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s";

	public static final String detailsFormateBankTransferByPhone = "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s";

	public static final String detailsFormateTransferEntry = "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s";

	public static final String detailsFormateCardTopUp = "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s";

	public static final String detailsFormatePhoneTopUp = "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s";

	public static final String detailsFormatePrecompiledBill = "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s";

	/**
	 * Show in the pop-up dialog when customer click "Verify Data"
	 * 
	 * @return
	 */
	private String getTransferDetails() {
		Object tPayee = getPayee();
		Resources res = activity.getResources();
		String paymentType = generatePaymentTypeName();
		AccountsModel am = getPayer();
		String account = am.getAccountAlias();

		String amount = Utils.formatMoney(getAmount(), activity
				.getResources().getString(R.string.dollar), true, true, false,
				false, true);
		String fees = Utils.formatMoney(this.fee, activity.getResources()
				.getString(R.string.dollar), true, true, false, false, true);
		switch (getPaymentType()) {
		case BANK_TRANSFER:
			if (tPayee instanceof AccountsModel) {
				AccountsModel dm = (AccountsModel) tPayee;
				return String
						.format(detailsFormateBankTransferByPhone, res
								.getString(R.string.type_of_payment1),
								paymentType, res.getString(R.string.account1),
								account, res.getString(R.string.date1),
								TimeUtil.getDateString(generateDate(),
										TimeUtil.dateFormat1), res
										.getString(R.string.amount1), amount,
								res.getString(R.string.reason),
								generateDescription(), res
										.getString(R.string._phone_number), dm
										.getPhoneNumber(), res
										.getString(R.string.fees), fees);
			} else {
				DestaccountModel dm = (DestaccountModel) tPayee;
				return String.format(detailsFormateBankTransfer, res
						.getString(R.string.type_of_payment1), paymentType, res
						.getString(R.string.account1), account, res
						.getString(R.string.date1), TimeUtil.getDateString(
						generateDate(), TimeUtil.dateFormat1), res
						.getString(R.string.amount1), amount, res
						.getString(R.string.payee1), dm.getTitle(), res
						.getString(R.string.iban), dm.getIban(), res
						.getString(R.string.reason), generateDescription(), res
						.getString(R.string.fees), fees);
			}
		case TRANSFER_ENTRY:
			DestaccountModel dm = (DestaccountModel) tPayee;
			return String.format(detailsFormateTransferEntry, res
					.getString(R.string.type_of_payment1), paymentType, res
					.getString(R.string.account1), account, res
					.getString(R.string.date1), TimeUtil.getDateString(
					generateDate(), TimeUtil.dateFormat1), res
					.getString(R.string.amount1), amount, res
					.getString(R.string.payee1), dm.getTitle(), res
					.getString(R.string.reason), generateDescription(), res
					.getString(R.string.fees), fees);
		case CARD_TOP_UP:
			CardRecipient cr = (CardRecipient) tPayee;
			return String.format(detailsFormateCardTopUp,
					res.getString(R.string.type_of_payment1),
					paymentType,
					res.getString(R.string.account1),
					account,
					// res.getString(R.string.date1),
					// TimeUtil.getDateString(generateDate(),
					// TimeUtil.dateFormat1),
					res.getString(R.string.amount1), amount,
					res.getString(R.string.payee1), cr.getName(),
					res.getString(R.string.reason), generateDescription(),
					res.getString(R.string.fees), fees);
		case PHONE_TOP_UP:
			PhoneRecipient pr = (PhoneRecipient) tPayee;
			return String.format(detailsFormatePhoneTopUp,
					res.getString(R.string.type_of_payment1), paymentType,
					res.getString(R.string.account1), account,
					res.getString(R.string.amount1), amount,
					res.getString(R.string.payee1), pr.getPhoneNumber(),
					res.getString(R.string.fees), fees);
		case PRECOMPILED_BILL:
			return String.format(detailsFormatePrecompiledBill, res
					.getString(R.string.type_of_payment1), paymentType, res
					.getString(R.string.account1), account, res
					.getString(R.string.bill_holder1), mPaymentsManagerBill
					.getBillHolderName(), res
					.getString(R.string.bill_number), mPaymentsManagerBill
					.getBillNumber(), res.getString(R.string.amount1),
					amount, res.getString(R.string.bank_free1), Utils
							.formatMoney(this.mCheckBillPaymentResponseModel
									.getCheckBillPaymentValueModel()
									.getCharges(), activity.getResources()
									.getString(R.string.dollar), true, true,
									false, false, true), res
							.getString(R.string.postal_free1), Utils
							.formatMoney(this.mCheckBillPaymentResponseModel
									.getCheckBillPaymentValueModel()
									.getPostalCharges(), activity
									.getResources().getString(R.string.dollar),
									true, true, false, false, true));
		case BLANK_BILL:
			return String.format(detailsFormatePrecompiledBill, res
					.getString(R.string.type_of_payment1), paymentType, res
					.getString(R.string.account1), account, res
					.getString(R.string.bill_holder1), mPaymentsManagerBlankBill
					.getBillHolderName(), res
					.getString(R.string.account_number1), mPaymentsManagerBlankBill.getAccountNumber()
					, res.getString(R.string.amount1),
					amount, res.getString(R.string.bank_free1), Utils
							.formatMoney(this.mCheckBillPaymentResponseModel
									.getCheckBillPaymentValueModel()
									.getCharges(), activity.getResources()
									.getString(R.string.dollar), true, true,
									false, false, true), res
							.getString(R.string.postal_free1), Utils
							.formatMoney(this.mCheckBillPaymentResponseModel
									.getCheckBillPaymentValueModel()
									.getPostalCharges(), activity
									.getResources().getString(R.string.dollar),
									true, true, false, false, true));
		case MAV_RAV:
			return String.format(detailsFormatePrecompiledBill, res
					.getString(R.string.type_of_payment1), paymentType, res
					.getString(R.string.account1), account, res
					.getString(R.string.bill_holder1), mPaymentsManagerMAVRAV
					.getBillHolderName(), res
					.getString(R.string.bill_number), mPaymentsManagerMAVRAV
					.getBillNumber(), res.getString(R.string.amount1),
					amount, res.getString(R.string.bank_free1), Utils
							.formatMoney(this.mCheckBillPaymentResponseModel
									.getCheckBillPaymentValueModel()
									.getCharges(), activity.getResources()
									.getString(R.string.dollar), true, true,
									false, false, true), res
							.getString(R.string.postal_free1), Utils
							.formatMoney(this.mCheckBillPaymentResponseModel
									.getCheckBillPaymentValueModel()
									.getPostalCharges(), activity
									.getResources().getString(R.string.dollar),
									true, true, false, false, true));
		}
		return "";
	}

	private void inviliViewByPaymentType(int paymentsType) {
		// if (showForm == FORM_ACCOUNT) {
		// showPayee(View.VISIBLE);
		// showAmount(View.VISIBLE);
		// }else{
		View foucsView = null;
		switch (paymentsType) {
		case BANK_TRANSFER:
			foucsView = showPayer(View.VISIBLE);
			showBillLayout(View.GONE);
			showBlackBillLayout(View.GONE);
			showMavRavLayout(View.GONE);
			showAmount(View.VISIBLE);
			showPayee(View.GONE);
			showDate(View.GONE, 0);
			showDescription(View.GONE, "");
			setProceedButtonClickAble(false);
			break;
		case TRANSFER_ENTRY:
			foucsView = showPayer(View.VISIBLE);
			showBillLayout(View.GONE);
			showBlackBillLayout(View.GONE);
			showMavRavLayout(View.GONE);
			showAmount(View.VISIBLE);
			showPayee(View.GONE);
			showDate(View.GONE, 0);
			showDescription(View.GONE, "");
			setProceedButtonClickAble(false);
			break;
		case CARD_TOP_UP:
			foucsView = showPayer(View.VISIBLE);
			showBillLayout(View.GONE);
			showBlackBillLayout(View.GONE);
			showMavRavLayout(View.GONE);
			showAmount(View.VISIBLE);
			showPayee(View.GONE);
			showDate(View.GONE, 0);
			showDescription(View.GONE, "");
			setProceedButtonClickAble(false);
			break;
		case PHONE_TOP_UP:
			foucsView = showPayer(View.VISIBLE);
			showBillLayout(View.GONE);
			showBlackBillLayout(View.GONE);
			showMavRavLayout(View.GONE);
			showPayee(View.VISIBLE);
			showAmount(View.GONE);
			showDate(View.GONE, 0);
			showDescription(View.GONE, "");
			setProceedButtonClickAble(false);
			break;
		case PRECOMPILED_BILL:
			showBillLayout(View.VISIBLE);
			showBlackBillLayout(View.GONE);
			showMavRavLayout(View.GONE);
			showPayee(View.GONE);
			showAmount(View.GONE);
			showDate(View.GONE, 0);
			showDescription(View.GONE, "");
			setProceedButtonClickAble(true);
			break;
		case BLANK_BILL:
			showBillLayout(View.GONE);
			showBlackBillLayout(View.VISIBLE);
			showMavRavLayout(View.GONE);
			showPayee(View.GONE);
			showAmount(View.GONE);
			showDate(View.GONE, 0);
			showDescription(View.GONE, "");
			setProceedButtonClickAble(true);

			break;
		case MAV_RAV:
			showBillLayout(View.GONE);
			showBlackBillLayout(View.GONE);
			showMavRavLayout(View.VISIBLE);
			showPayee(View.GONE);
			showAmount(View.GONE);
			showDate(View.GONE, 0);
			showDescription(View.GONE, "");
			setProceedButtonClickAble(true);

			break;
		}
		setFoucs(foucsView);
		// }
	}

	private void inviliAccountView(int paymentsType) {
		showLeftNavigation(paymentsType > 0);
		invilidataPayees();
		if (showForm == FORM_ACCOUNT && App.app.transferEntryAccounts != null) {
			List<AccountsModel> ens = App.app.transferEntryAccounts.get(0)
					.getAccounts();
			int count = -1;
			String src, des = null;
			if (ens != null) {
				count = ens.size();
			}
			if (getPayer() != null) {
				des = getPayer().getAccountCode();
			}
			for (int i = 0; i < count; i++) {
				src = ens.get(i).getAccountCode();
				if (src != null && src.equals(des)) {
					setEntryDiselect(i);
					curPayerAccoutSeltectId = i;
					break;
				}
			}
		} else
			switch (paymentsType) {
			default:
				reset();
				if (allAccounts != null && allAccounts.size() > 0) {
					account_folders_cf_ad.setDatas(allAccounts.get(0)
							.getAccounts());
					account_folders_cf_ad.notifyDataSetChanged();
				} else {
					// loadAccountData(GET_ACCOUNT);
				}

				break;
			case BANK_TRANSFER:
				if (App.app.bankTransferAccounts != null) {
					account_folders_cf_ad.setDatas(App.app.bankTransferAccounts
							.get(0).getAccounts());
					account_folders_cf_ad.notifyDataSetChanged();
				} else {
					// loadAccountData(GET_BANK_TRANSFER);
				}

				if (App.app.getRecipientListModel != null) {
					bank_payee_cf_ad.setDatas(App.app.getRecipientListModel
							.getBankRecipientList());
					payee_folders_cf.setAdapter(bank_payee_cf_ad);
				}

				// account_folders_cf.setVisibility(View.VISIBLE);
				// amountbar_lin.setVisibility(View.GONE);
				// phone_payee_folders_cf.setVisibility(View.GONE);
				// top_up_amount_gv.setVisibility(View.GONE);
				// payee_folders_cf.setVisibility(View.GONE);
				// showDate(View.GONE, 0);
				// showDescription(View.GONE, "");
				break;
			case TRANSFER_ENTRY:
				if (null != App.app.transferEntryAccounts) {
					account_folders_cf_ad
							.setDatas(App.app.transferEntryAccounts.get(0)
									.getAccounts());
					account_folders_cf_ad.notifyDataSetChanged();

					entry_payee_cf_ad.setDatas(App.app.transferEntryAccounts
							.get(0).getAccounts());
					payee_folders_cf.setAdapter(entry_payee_cf_ad);
				} else {
					// loadAccountData(GET_TRANSFER_ENTRY);
				}
				// account_folders_cf.setVisibility(View.VISIBLE);
				// amountbar_lin.setVisibility(View.GONE);
				// phone_payee_folders_cf.setVisibility(View.GONE);
				// top_up_amount_gv.setVisibility(View.GONE);
				// payee_folders_cf.setVisibility(View.GONE);
				// showDate(View.GONE, 0);
				// showDescription(View.GONE, "");
				break;
			case CARD_TOP_UP:
				if (null != App.app.chargeAccounts) {
					account_folders_cf_ad.setDatas(App.app.chargeAccounts
							.get(0).getAccounts());
					account_folders_cf_ad.notifyDataSetChanged();
				} else {
					// loadAccountData(GET_CARD_TOP_UP);
				}

				if (App.app.getRecipientListModel != null) {
					card_payee_cf_ad.setDatas(App.app.getRecipientListModel
							.getCardRecipientList());
					payee_folders_cf.setAdapter(card_payee_cf_ad);
				}

				// account_folders_cf.setVisibility(View.VISIBLE);
				// amountbar_lin.setVisibility(View.GONE);
				// phone_payee_folders_cf.setVisibility(View.GONE);
				// top_up_amount_gv.setVisibility(View.GONE);
				// payee_folders_cf.setVisibility(View.GONE);
				// showDate(View.GONE, 0);
				// showDescription(View.GONE, "");
				break;
			case PHONE_TOP_UP:
				if (null != App.app.simTopUpAccounts) {
					account_folders_cf_ad.setDatas(App.app.simTopUpAccounts
							.get(0).getAccounts());
					account_folders_cf_ad.notifyDataSetChanged();
				} else {
					// loadAccountData(GET_PHONE_TOP_UP);
				}

				if (App.app.getRecipientListModel != null) {
					phone_payee_cf_ad.setDatas(App.app.getRecipientListModel
							.getPhoneRecipientList());
					phone_payee_folders_cf.setAdapter(phone_payee_cf_ad);
				}

				// account_folders_cf.setVisibility(View.VISIBLE);
				// phone_payee_folders_cf.setVisibility(View.GONE);
				// top_up_amount_gv.setVisibility(View.GONE);
				// amountbar_lin.setVisibility(View.GONE);
				// payee_folders_cf.setVisibility(View.GONE);
				// showDate(View.GONE, 0);
				// showDescription(View.GONE, "");
				break;
			case PRECOMPILED_BILL:

				break;
			case BLANK_BILL:

				break;
			case MAV_RAV:

				break;
			}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case GET_ACCOUNT:
				allAccounts = (List<AccountsForServiceModel>) msg.obj;
				if (allAccounts != null && allAccounts.get(0) != null) {
					List<AccountsModel> accounts = allAccounts.get(0)
							.getAccounts();
					int size = accounts.size();
					List<AccountsModel> destLs = null;

					if (App.app.transferEntryAccounts == null) {
						App.app.transferEntryAccounts = new ArrayList<AccountsForServiceModel>();
						AccountsForServiceModel mAccountsForServiceModel = new AccountsForServiceModel();
						destLs = new ArrayList<AccountsModel>();
						mAccountsForServiceModel.setAccounts(destLs);
						App.app.transferEntryAccounts
								.add(mAccountsForServiceModel);
					} else if (App.app.transferEntryAccounts.get(0) == null) {
						AccountsForServiceModel mAccountsForServiceModel = new AccountsForServiceModel();
						destLs = new ArrayList<AccountsModel>();
						mAccountsForServiceModel.setAccounts(destLs);
						App.app.transferEntryAccounts
								.add(mAccountsForServiceModel);
					} else if (destLs == null) {
						destLs = App.app.transferEntryAccounts.get(0)
								.getAccounts();
						if (destLs == null) {
							destLs = new ArrayList<AccountsModel>();
							App.app.transferEntryAccounts.get(0).setAccounts(
									destLs);
						} else {
							destLs.clear();
						}
					}
					for (AccountsModel one : accounts) {
						// if(ServiceCode.TRANSFER_ENTRY_PAYMENT.equals(one.getBankServiceType().getBankServiceCode())){
						destLs.add(one);
						// }
					}
				}
				// if(allAccounts!=null&&allAccounts.size()>0&&allAccounts.get(0)!=null&&allAccounts.get(0).getAccounts()!=null)
				// for(int i=1;i<allAccounts.get(0).getAccounts().size();){
				// allAccounts.get(0).getAccounts().remove(i);
				// }
				App.app.bankTransferAccounts = allAccounts;

				App.app.chargeAccounts = allAccounts;

				App.app.simTopUpAccounts = allAccounts;

				if (showForm == FORM_DEFAULT
						&& (App.app.bankTransferAccounts == null
								|| App.app.bankTransferAccounts.size() <= 0 || App.app.bankTransferAccounts
								.get(0).getAccounts().size() == 0)) {
					payment_type_sp.setEnabled(false);
					Toast.makeText(activity, R.string.none_payer, Toast.LENGTH_LONG).show();
				} else if (App.app.bankTransferAccounts != null
						&& App.app.bankTransferAccounts.size() > 0
						&& App.app.bankTransferAccounts.get(0).getAccounts()
								.size() <= 1) {
					// If the customer has only one account, the
					// "transfer entry" option must be not visible: the customer
					// hasn't to select the payment type "transfer entry".
					type_payment_ad = new ArrayAdapter<String>(activity,
							android.R.layout.simple_spinner_item, activity
									.getResources().getStringArray(
											R.array.new_payments_types1));
					type_payment_ad
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					payment_type_sp.setAdapter(type_payment_ad);
				} else {
					type_payment_ad = new ArrayAdapter<String>(activity,
							android.R.layout.simple_spinner_item, activity
									.getResources().getStringArray(
											R.array.new_payments_types));
					type_payment_ad
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					payment_type_sp.setAdapter(type_payment_ad);
				}
				inviliAccountView(getPaymentType());

				downloading_pd.dismiss();
				break;
			case GET_BANK_TRANSFER:
				App.app.bankTransferAccounts = (List<AccountsForServiceModel>) msg.obj;
				inviliAccountView(BANK_TRANSFER);

				downloading_pd.dismiss();
				break;
			case GET_TRANSFER_ENTRY:
				App.app.transferEntryAccounts = (List<AccountsForServiceModel>) msg.obj;

				inviliAccountView(TRANSFER_ENTRY);

				downloading_pd.dismiss();
				break;
			case GET_CARD_TOP_UP:
				App.app.chargeAccounts = (List<AccountsForServiceModel>) msg.obj;
				inviliAccountView(CARD_TOP_UP);

				downloading_pd.dismiss();
				break;
			case GET_PHONE_TOP_UP:
				App.app.simTopUpAccounts = (List<AccountsForServiceModel>) msg.obj;
				inviliAccountView(PHONE_TOP_UP);

				downloading_pd.dismiss();
				break;
			case GET_RECIPIENTLISTMODEL:
				App.app.getRecipientListModel = (GetRecipientListModel) msg.obj;
				invilidataPayees();
				downloading_pd.dismiss();
				break;
			case GET_ASKPIN:
				downloading_pd.dismiss();
				generateOtp = (GenerateOTPResponseModel) msg.obj;
				if(generateOtp==null||generateOtp.responsePublicModel==null){
					Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_SHORT).show();
				}else if (generateOtp.responsePublicModel.isSuccess()){
					showPinInput(View.VISIBLE);
					if (MainActivity.setting.getChannelToRecelvePin() == SettingModel.EMAIL) {
						Toast.makeText(activity,R.string.pin_success_by_email, Toast.LENGTH_LONG).show();
					} else if (MainActivity.setting.getChannelToRecelvePin() == SettingModel.SMS) {
						Toast.makeText(activity,R.string.pin_success_by_sms, Toast.LENGTH_LONG).show();
					}
				} else {
					activity.displayErrorMessage(generateOtp.responsePublicModel.eventManagement
							.getErrorDescription());
					// DialogManager.createMessageDialog(generateOtp.responsePublicModel.eventManagement.getErrorDescription(),
					// getContext());
					// Toast.makeText(getContext(),
					// "ask pin fail ,plealse get pin by email",Toast.LENGTH_LONG).show();
				}
				break;
			case VALIDATE:
				switch (getPaymentType()) {
				case BANK_TRANSFER:
				case TRANSFER_ENTRY:
					downloading_pd.dismiss();
					checkTransfer = (CheckTransferResponseModel) msg.obj;
					fee = checkTransfer.getCharges();
					// LogManager.d("validate:" +
					// stringBuffer.toString());
					if (checkTransfer.isSuccess()) {
						if ("91082"
								.equals(checkTransfer.responsePublicModel.eventManagement
										.getErrorCode())) {
							// activity.displayErrorMessage(checkTransfer.responsePublicModel.eventManagement
							// .getErrorDescription());
							AlertDialog.Builder builder = new Builder(activity);
							LayoutInflater inflater = LayoutInflater
									.from(activity);
							LinearLayout linearLahyout = (LinearLayout) inflater
									.inflate(R.layout.message_dialog_layout,
											null);
							builder.setView(linearLahyout);
							final AlertDialog alertDialog = builder.create();

							Button imageButton = (Button) linearLahyout
									.findViewById(R.id.ok_btn);
							Button cancel_ibtn = (Button) linearLahyout
									.findViewById(R.id.cancel_btn);
							cancel_ibtn.setVisibility(View.VISIBLE);
							TextView text = (TextView) linearLahyout
									.findViewById(R.id.message_text);
							text.setText(activity
									.getString(R.string.pls_input_right_amount));
							imageButton
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											// paymentCofirmLayout.onButtonClick();
											alertDialog.dismiss();

											displayPaymentRecap();
										}
									});
							cancel_ibtn
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											// paymentCofirmLayout.onButtonClick();
											alertDialog.dismiss();

											setPaymentStep(STEP_1);
										}
									});
							setDialogWidth(alertDialog, activity);
							alertDialog.show();
						} else {
							displayPaymentRecap();
						}

						// BankRecipient bankRecipient = new
						// BankRecipient();
						// bankRecipient.setIbanCode(destAccount.getIban());
						// bankRecipient.setName(destAccount.getTitle());
						// if (insertRecipient(InsertRecipientJson.BANK,
						// bankRecipient, null, null)) {
						// return true;
						// } else {
						// activity.displayErrorMessage(responsePublicModel.eventManagement.getErrorDescription());
						// return false;
						// }
					} else {
						activity.displayErrorMessage(checkTransfer.responsePublicModel.eventManagement
								.getErrorDescription());
					}
					break;
				case CARD_TOP_UP:
					downloading_pd.dismiss();
					checkRechargeCard = (CheckRechargeCardResponseModel) msg.obj;
					fee = checkRechargeCard.getCharges();
					if (checkRechargeCard.responsePublicModel.isSuccess()) {

						displayPaymentRecap();
					} else {
						if ("91083"
								.equals(checkRechargeCard.responsePublicModel.eventManagement
										.getErrorCode())) {
							// activity.displayErrorMessage(checkTransfer.responsePublicModel.eventManagement
							// .getErrorDescription());
							displayRecapDialog(
									activity,
									activity.getString(R.string.pls_input_right_amount))
									.show();
						} else {
							activity.displayErrorMessage(checkRechargeCard.responsePublicModel
									.getResultDescription());
						}
					}
					break;
				case PHONE_TOP_UP:
					downloading_pd.dismiss();
					checkSimTopUp = (CheckSimTopUpResponseModel) msg.obj;
					fee = checkSimTopUp.getCharges();
					if (checkSimTopUp.responsePublicModel.isSuccess()) {
						displayPaymentRecap();
					} else {
						if ("91083"
								.equals(checkSimTopUp.responsePublicModel.eventManagement
										.getErrorCode())) {
							// activity.displayErrorMessage(checkTransfer.responsePublicModel.eventManagement
							// .getErrorDescription());
							displayRecapDialog(
									activity,
									activity.getString(R.string.pls_input_right_amount))
									.show();
						} else {
							activity.displayErrorMessage(checkSimTopUp.responsePublicModel.eventManagement
									.getErrorDescription());
						}
					}
					break;
				case PRECOMPILED_BILL:
				case BLANK_BILL:
				case MAV_RAV:
					downloading_pd.dismiss();
					mCheckBillPaymentResponseModel = (CheckBillPaymentResponseModel) msg.obj;
					if(mCheckBillPaymentResponseModel==null||mCheckBillPaymentResponseModel.responsePublicModel==null){
						Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_SHORT).show();
					}else
					// fee = mCheckBillPaymentResponseModel.getCharges();
					if (mCheckBillPaymentResponseModel.responsePublicModel
							.isSuccess()) {
						if ("91082"
								.equals(mCheckBillPaymentResponseModel.responsePublicModel.eventManagement
										.getErrorCode())) {
							// activity.displayErrorMessage(checkTransfer.responsePublicModel.eventManagement
							// .getErrorDescription());
							displayRecapDialog(
									activity,
									activity.getString(R.string.pls_input_right_amount))
									.show();
						} else {
							displayPaymentRecap();
						}
					} else {
						activity.displayErrorMessage(mCheckBillPaymentResponseModel.responsePublicModel.eventManagement
								.getErrorDescription());
					}
					break;
				}
				break;
			case CONFIRM:
				onConfirmResult(msg.obj);
				if (confirmResult) {
					new PostThread(activity, null, allAccounts, mHandler,
							PaymentsManager.TYPE_UPDATE_BALANCE,
							PostThread.TYPE_UPDATE_BALANCE).start();
				} else {
					downloading_pd.dismiss();
				}
				break;
			case TYPE_UPDATE_BALANCE:
				account_folders_cf_ad.notifyDataSetChanged();
				downloading_pd.dismiss();
				break;
			case GET_AMOUNT_AVAILABLE:
				downloading_pd.dismiss();
				CompanyAmountResponseModel companyAmount = (CompanyAmountResponseModel) msg.obj;
				mAmountAvailableLs = companyAmount.getAmountAvailable();
				if (companyAmount.responsePublicModel.isSuccess()) {
					// CompanyAmount
					if (mAmountAvailableLs == null
							|| mAmountAvailableLs.size() <= 0) {
						DialogManager
								.createMessageDialog(
										companyAmount.responsePublicModel
												.getResultDescription(),
										activity).show();
					} else {
						updateTopupAmountView();
						View v = showAmount(View.VISIBLE, amount);
						setFoucs(v);
						setProceedButtonClickAble(true);
					}
				} else {
					activity.displayErrorMessage(companyAmount.responsePublicModel.eventManagement
							.getErrorDescription());
				}
				break;
			case VERTIFY_CARD:
				downloading_pd.dismiss();
				GetCardsResponseModel getCards = (GetCardsResponseModel) msg.obj;
				List<InfoCardsModel> list = getCards.getInfoCardListModel();
				if (list != null && list.size() > 0) {
					tmp_InfoCardsModel = list.get(0);
					tmp_isVerifyCard = VERIFYCARD_SUCCESS;
					AlertDialog.Builder builder = new Builder(activity);
					LayoutInflater inflater = LayoutInflater.from(activity);
					LinearLayout linearLahyout = (LinearLayout) inflater
							.inflate(R.layout.message_dialog_layout, null);
					builder.setView(linearLahyout);
					final AlertDialog alertDialog = builder.create();

					Button imageButton = (Button) linearLahyout
							.findViewById(R.id.ok_btn);
					TextView text = (TextView) linearLahyout
							.findViewById(R.id.message_text);
					text.setText(activity.getString(R.string.card_verified));
					imageButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// paymentCofirmLayout.onButtonClick();
							alertDialog.dismiss();
							setPaymentStep(R.id.new_payments_step2_rb);
						}
					});
					setDialogWidth(alertDialog, activity);
					alertDialog.show();
				} else {
					tmp_isVerifyCard = VERIFYCARD_FAILED;
					activity.displayErrorMessage(activity
							.getString(R.string.no_card_found));
				}
				break;
			case INSERTRECIPIENT:
				downloading_pd.dismiss();
				onInsertRecipient((ResponsePublicModel) msg.obj);
				break;
			}
			super.handleMessage(msg);
		}

	};

	public void initData() {
		// bankTransferAccounts=App.app.bankTransferAccounts;
		// transferEntryAccounts=App.app.transferEntryAccounts;
		// chargeAccounts=App.app.simTopUpAccounts;
		// simTopUpAccounts=App.app.chargeAccounts;
		// getRecipientListModel=App.app.getRecipientListModel;

		// new PostThread(activity, null, null, mHandler,
		// PaymentsManager.GET_RECIPIENTLISTMODEL,
		// PostThread.TYPE_RECIPIENTLISTMODEL).start();
		allAccounts = App.app.bankTransferAccounts;
		if (showForm == FORM_RECENT_PAYMENT || showForm == FORM_PENDING_PAYMENT) {
			if (getPaymentType() == PHONE_TOP_UP) {
				loadAmountAvilable();
			}
		} else {
			if (App.app.bankTransferAccounts == null
					|| App.app.bankTransferAccounts.size() <= 0
					|| App.app.bankTransferAccounts.get(0).getAccounts() == null
					|| App.app.bankTransferAccounts.get(0).getAccounts().size() <= 0) {
				loadAccountData(GET_ACCOUNT);
			} else {
				// If the customer has only one account, the "transfer entry"
				// option must be not visible: the customer hasn't to select the
				// payment type "transfer entry".
				if (App.app.bankTransferAccounts != null
						&& App.app.bankTransferAccounts.size() > 0
						&& App.app.bankTransferAccounts.get(0).getAccounts()
								.size() <= 1) {
					type_payment_ad = new ArrayAdapter<String>(activity,
							android.R.layout.simple_spinner_item, activity
									.getResources().getStringArray(
											R.array.new_payments_types1));
					type_payment_ad
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					payment_type_sp.setAdapter(type_payment_ad);
				} else {
					type_payment_ad = new ArrayAdapter<String>(activity,
							android.R.layout.simple_spinner_item, activity
									.getResources().getStringArray(
											R.array.new_payments_types));
					type_payment_ad
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					payment_type_sp.setAdapter(type_payment_ad);
				}
			}
		}
		// loadAccountData(BANK_TRANSFER);
		// loadAccountData(TRANSFER_ENTRY);
		// loadAccountData(CARD_TOP_UP);
		// loadAccountData(PHONE_TOP_UP);
	}

	protected void displayPaymentRecap() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(getTransferDetails());

		builder.setPositiveButton(R.string.ask_pin,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						downloading_pd.show();
						new PostThread(activity, null, null, mHandler,
								PaymentsManager.GET_ASKPIN,
								PostThread.TYPE_ASKPIN).start();
					}
				});

		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	protected AlertDialog displayRecapDialog(Context context, String msg) {
		AlertDialog.Builder builder = new Builder(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout linearLahyout = (LinearLayout) inflater.inflate(
				R.layout.message_dialog_layout, null);
		builder.setView(linearLahyout);
		final AlertDialog alertDialog = builder.create();

		Button imageButton = (Button) linearLahyout.findViewById(R.id.ok_btn);
		TextView text = (TextView) linearLahyout
				.findViewById(R.id.message_text);
		text.setText(msg);
		imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// paymentCofirmLayout.onButtonClick();
				alertDialog.dismiss();
				setPaymentStep(STEP_1);
				// displayPaymentRecap();
			}
		});
		setDialogWidth(alertDialog, context);
		return alertDialog;
	}

	private void updateTopupAmountView() {
		top_up_amount_gv.removeAllViews();
		int size = mAmountAvailableLs.size();
		for (int i = 0; i < size; i++) {
			AmountAvailable aa = mAmountAvailableLs.get(i);
			RadioButton rbtn = (RadioButton) mLayoutInflater.inflate(
					R.layout.sim_top_up_radio_model, null);
			rbtn.setId(i);
			rbtn.setText(Utils.notPlusGenerateFormatMoneyInt(Contants.COUNTRY,
					aa.getRechargeAmount()));
			top_up_amount_gv.addView(rbtn);
		}
	}

	private void showPaymentType(int visibility, int selectIndex) {
		payment_type_lin.setVisibility(visibility);
		if (App.app.bankTransferAccounts != null
				&& App.app.bankTransferAccounts.size() <= 1
				&& selectIndex == TRANSFER_ENTRY) {
			--selectIndex;
		}
		payment_type_sp.setSelection(selectIndex);

	}

	private void showPaymentType(int visibility) {
		payment_type_lin.setVisibility(visibility);
	}

	private View showPayer(int visibility) {
		View view = null;
		if (showForm == FORM_RECENT_PAYMENT || showForm == FORM_PENDING_PAYMENT
				|| showForm == FORM_ACCOUNT) {
			view = payer_detail_lin;
			if (payer_detail_lin.getVisibility() != visibility) {
				payer_detail_lin.setVisibility(visibility);
			}
			if (account_folders_cf.getVisibility() != View.GONE) {
				account_folders_cf.setVisibility(View.GONE);
				layout.findViewById(R.id.payer_list_info_tv).setVisibility(
						View.GONE);
			}
		} else /* if(showForm==FORM_DEFAULT) */{
			view = account_folders_cf;
			if (account_folders_cf.getVisibility() != visibility) {
				account_folders_cf.setVisibility(visibility);
				layout.findViewById(R.id.payer_list_info_tv).setVisibility(
						visibility);
			}
			if (payer_detail_lin.getVisibility() != View.GONE) {
				payer_detail_lin.setVisibility(View.GONE);
			}
		}
		return view;
	}

	private void showBillLayout(int visibility) {
		mPaymentsManagerBill.setVisibility(visibility);
		mPaymentsManagerBill.setBillHolderName(Contants.getUserInfo
				.getCustomerName()
				+ " "
				+ Contants.getUserInfo.getCustomerSurname());
	}

	private void showBlackBillLayout(int visibility) {
		mPaymentsManagerBlankBill.setVisibility(visibility);
		mPaymentsManagerBlankBill.setBillHolderName(Contants.getUserInfo
				.getCustomerName()
				+ " "
				+ Contants.getUserInfo.getCustomerSurname());
	}

	private void showMavRavLayout(int visibility) {
		mPaymentsManagerMAVRAV.setVisibility(visibility);
		mPaymentsManagerMAVRAV.setBillHolderName(Contants.getUserInfo
				.getCustomerName()
				+ " "
				+ Contants.getUserInfo.getCustomerSurname());
	}

	private View showPayee(int visibility) {
		View view = null;
		if (showForm == FORM_RECENT_PAYMENT || showForm == FORM_PENDING_PAYMENT) {
			view = payee_detail_lin;
			if (payee_detail_lin.getVisibility() != visibility) {
				payee_detail_lin.setVisibility(visibility);
			}
			if (phone_payee_folders_cf.getVisibility() != View.GONE) {
				phone_payee_folders_cf.setVisibility(View.GONE);
				layout.findViewById(R.id.payee_list_info_tv).setVisibility(
						View.GONE);
			}
			if (payee_folders_cf.getVisibility() != View.GONE) {
				payee_folders_cf.setVisibility(View.GONE);
				layout.findViewById(R.id.payee_list_info_tv).setVisibility(
						View.GONE);
			}
		} else /* if(showForm==FORM_DEFAULT) */{
			if (getPaymentType() == PHONE_TOP_UP) {
				view = phone_payee_folders_cf;
				if (phone_payee_folders_cf.getVisibility() != visibility) {
					phone_payee_folders_cf.setVisibility(visibility);
					layout.findViewById(R.id.payee_list_info_tv).setVisibility(
							visibility);
				}
				if (payee_folders_cf.getVisibility() != View.GONE) {
					payee_folders_cf.setVisibility(View.GONE);
				}
			} else {
				view = payee_folders_cf;
				if (payee_folders_cf.getVisibility() != visibility) {
					payee_folders_cf.setVisibility(visibility);
					layout.findViewById(R.id.payee_list_info_tv).setVisibility(
							visibility);
				}
				if (phone_payee_folders_cf.getVisibility() != View.GONE) {
					phone_payee_folders_cf.setVisibility(View.GONE);
				}
			}
			if (payee_detail_lin.getVisibility() != View.GONE) {
				payee_detail_lin.setVisibility(View.GONE);
			}
		}
		return view;
	}

	private void invilidataPayees() {
		switch (getPaymentType()) {
		case BANK_TRANSFER:
			if (App.app.getRecipientListModel != null) {
				bank_payee_cf_ad.setDatas(App.app.getRecipientListModel
						.getBankRecipientList());
				payee_folders_cf.setAdapter(bank_payee_cf_ad);
			}
			break;
		case TRANSFER_ENTRY:
			if (null != App.app.transferEntryAccounts) {
				entry_payee_cf_ad.setDatas(App.app.transferEntryAccounts.get(0)
						.getAccounts());
				payee_folders_cf.setAdapter(entry_payee_cf_ad);
			} else {
				loadAccountData(GET_TRANSFER_ENTRY);
			}
			break;
		case CARD_TOP_UP:
			if (App.app.getRecipientListModel != null) {
				card_payee_cf_ad.setDatas(App.app.getRecipientListModel
						.getCardRecipientList());
				payee_folders_cf.setAdapter(card_payee_cf_ad);
			}
			break;
		case PHONE_TOP_UP:
			if (App.app.getRecipientListModel != null) {
				phone_payee_cf_ad.setDatas(App.app.getRecipientListModel
						.getPhoneRecipientList());
				phone_payee_folders_cf.setAdapter(phone_payee_cf_ad);
			}
			break;
		case PRECOMPILED_BILL:

			break;
		}

	}

	private View showDescription(int visibility, String description) {
		if (description != null) {
			description_et.setText(description);
		}
		return showDescription(visibility);
	}

	private View showDescription(int visibility) {
		description_et.setVisibility(visibility);
		return description_et;
	}

	private View showDate(int visibility, long timeInMillis) {
		if (timeInMillis == 0) {
			execution_date_et.setText("");// TimeUtil.getDateString(System.currentTimeMillis(),
											// TimeUtil.dateFormat8));
			setDate(System.currentTimeMillis());
		} else {
			execution_date_et.setText(TimeUtil.getDateString(mTimeInMillis,
					TimeUtil.dateFormat8));
		}
		return showDate(visibility);
	}

	private View showDate(int visibility) {
		if (execution_date_lin.getVisibility() != visibility) {
			execution_date_lin.setVisibility(visibility);
		}
		return execution_date_lin;
	}

	private View showAmount(int visibility, double amount) {
		View v = showAmount(visibility);
		if (getPaymentType() == PHONE_TOP_UP) {
			if (amount > 0 && mAmountAvailableLs != null
					&& mAmountAvailableLs.size() > 0) {
				for (int i = mAmountAvailableLs.size() - 1; i >= 0; i--) {
					if (mAmountAvailableLs.get(i).getRechargeAmount() == (int) amount) {
						top_up_amount_gv.check(i);
						break;
					}
				}
			} else {
				top_up_amount_gv.check(-1);
			}
		} else {
			if (amount == 0) {
				amount_et.setText("");
				amount_et2.setText("00");
			} else {
				int m1=(int) Math.floor(amount);
				amount_et.setText(Integer.toString(m1));//Utils.formatMoney(m1, "", true, true, false, false, true));
				m1=(int) (Math.floor(amount*100)-m1*100);
				amount_et2.setText(m1<10?"0"+m1:Integer.toString(m1));
			}
		}
		return v;
	}

	private View showAmount(int visibility) {
		View view = null;
		if (getPaymentType() == PHONE_TOP_UP) {
			view = top_up_amount_gv;
			if (top_up_amount_gv.getVisibility() != visibility) {
				top_up_amount_gv.setVisibility(visibility);
			}
			if (amountbar_lin.getVisibility() != View.GONE) {
				amountbar_lin.setVisibility(View.GONE);
			}
		} else {
			view = amountbar_lin;
			if (amountbar_lin.getVisibility() != visibility) {
				amountbar_lin.setVisibility(visibility);
			}
			if (top_up_amount_gv.getVisibility() != View.GONE) {
				top_up_amount_gv.setVisibility(View.GONE);
			}
		}
		return view;
	}

	private void showPinInput(int visibility) {
		if (pin_lin.getVisibility() == visibility) {
			return;
		}
		pin_lin.setVisibility(visibility);
		if (visibility == View.VISIBLE) {
			pin_et.setText("");
			proceed_btn.setText(R.string.proceed);
		}
	}

	private double getAmount() {

		switch (getPaymentType()) {
		case BANK_TRANSFER:
		case TRANSFER_ENTRY:
		case CARD_TOP_UP:
			try {
				String amountStr = amount_et.getText().toString().trim()+"."+amount_et2.getText().toString().trim();
//				amountStr = amountStr.replace(".", "");
//				amountStr = amountStr.replace(',', '.');
				amount = Double.parseDouble(amountStr);
			} catch (Exception e) {
				amount = 0;
				e.printStackTrace();
			}
			break;
		case PHONE_TOP_UP:
			AmountAvailable aa = generateAmountAvailable();
			if (aa != null) {
				amount = aa.getRechargeAmount();
			} else {
				amount = 0;
			}
			break;
		case PRECOMPILED_BILL:
			amount = mPaymentsManagerBill.getAmount();
			break;
		case BLANK_BILL:
			amount = mPaymentsManagerBlankBill.getAmount();
			break;
		case MAV_RAV:
			amount = mPaymentsManagerMAVRAV.getAmount();
			break;
		}
		return amount;
	}

	private String generateDescription() {
		return description_et.getText().toString();
	}

	private long generateDate() {
		return mTimeInMillis;
	}

	/**
	 * @param stepId
	 *            R.id.new_payments_step1_rb,R.id.new_payments_step2_rb or
	 *            R.id.new_payments_step3_rb
	 */
	private void setPaymentStep(int stepId) {
		if (curStep == stepId) {
			return;
		}
		curStep = stepId;
		showPaymentStep(stepId);
		invilidataStep(stepId);
	}

	private int getPaymentStep() {
		return new_payments_step_rg.getCheckedRadioButtonId();
	}

	private String generatePaymentTypeName() {
		if (showForm == FORM_RECENT_PAYMENT || showForm == FORM_PENDING_PAYMENT) {
			return activity.getResources().getStringArray(
					R.array.new_payments_types)[getPaymentType()];
		}
		return (String) this.payment_type_sp.getSelectedItem();
	}

	/**
	 * type of payment
	 * 
	 * @param position
	 *            from 0 to 4
	 */
	private void setPaymentsType(int position) {
		payment_type_sp.setSelection(position);
		curPaymentType = position;
	}

	private int getPaymentType() {
		int paymentType = curPaymentType;
		if (showForm != PaymentsManager.FORM_RECENT_PAYMENT
				&& showForm != FORM_PENDING_PAYMENT) {
			paymentType = payment_type_sp.getSelectedItemPosition();
			// If the customer has only one account, the "transfer entry" option
			// must be not visible: the customer hasn't to select the payment
			// type "transfer entry".
			if (App.app.bankTransferAccounts != null
					&& App.app.bankTransferAccounts.size() <= 1
					&& paymentType >= 2) {
				++paymentType;
			}
		}
		return paymentType;
	}

	public class PostThread extends Thread {
		public static final int TYPE_DEF = 0, TYPE_BANK_TRANSFER = 1,
				TYPE_TRANSFER_ENTRY = 2, TYPE_CARD_TOP_UP = 3,
				TYPE_PHONE_TOP_UP = 4, TYPE_RECIPIENTLISTMODEL = 5,
				TYPE_ASKPIN = 6, TYPE_VALIDATE = 7, TYPE_CONFIRM = 8,
				TYPE_GET_AMOUNT_AVAILABLE = 9, TYPE_VERTIFY_CARD = 10,
				TYPE_INSERTRECIPIENT = 11, TYPE_GET_INIT_DATA = 12,
				TYPE_GET_ACCOUNT = 13, TYPE_UPDATE_BALANCE = 14;

		Context mContext;

		String url;

		Object postData;

		Handler handler;

		int requastFlag = 0;

		Object resultData;

		int type = TYPE_DEF;

		public PostThread(Context context, String url, Object postData,
				Handler handler, int requastFlag, int type) {
			this.mContext = context;
			this.url = url;
			this.postData = postData;
			this.handler = handler;
			this.requastFlag = requastFlag;
			this.type = type;
		}

		public PostThread(Context context, String url, Object postData,
				Handler handler, int requastFlag) {
			this(context, url, postData, handler, requastFlag, TYPE_DEF);
		}

		public void run() {
			if (App.app.getRecipientListModel == null) {
				NewPaymentDataUtils.getRecipientList(activity);
			}
			switch (type) {
			case TYPE_DEF:
			default:
				HttpConnector httpConnector = new HttpConnector();
				resultData = httpConnector.requestByHttpPost(url,
						(String) postData, mContext);
				break;
			case TYPE_GET_ACCOUNT:
				resultData = NewPaymentDataUtils
						.geBankTransferAccountst(activity);// getAccountsByService(activity,ServiceCode.SERVER_CODES);
				App.app.getRecipientListModel = NewPaymentDataUtils
						.getRecipientList(activity);
				break;
			case TYPE_UPDATE_BALANCE:
				NewPaymentDataUtils.getBalance(activity,
						((List<AccountsForServiceModel>) postData).get(0)
								.getAccounts(), null);
				break;
			case TYPE_BANK_TRANSFER:
				resultData = NewPaymentDataUtils
						.geBankTransferAccountst(activity);
				break;
			case TYPE_TRANSFER_ENTRY:
				resultData = NewPaymentDataUtils
						.getTransferEntryAccounts(activity);
				break;
			case TYPE_CARD_TOP_UP:
				resultData = NewPaymentDataUtils.getChargeAccounts(activity);
				break;
			case TYPE_PHONE_TOP_UP:
				resultData = NewPaymentDataUtils.getSimTopUpAccounts(activity);
				break;
			case TYPE_RECIPIENTLISTMODEL:
				resultData = NewPaymentDataUtils.getRecipientList(activity);
				break;
			case TYPE_ASKPIN:
				resultData = askPin();
				break;
			case TYPE_VALIDATE:
				resultData = validate();
				break;
			case TYPE_CONFIRM:
				resultData = confirm();
				break;
			case TYPE_GET_AMOUNT_AVAILABLE:
				resultData = getAmountAvailableLs();
				break;
			case TYPE_VERTIFY_CARD:
				resultData = vertifyCard(mContext, getPayer()
						.getAccountCode(), (CardRecipient) getPayee());
				break;
			case TYPE_INSERTRECIPIENT:
				resultData = insertRecipient(url, postData);
				break;
			case TYPE_GET_INIT_DATA:

				break;
			}

			if (handler != null) {
				Message msg = handler.obtainMessage(requastFlag, resultData);
				handler.sendMessage(msg);
			}
		}

	}

	public boolean onDestroyed() {
		if (allAccounts != null) {
			allAccounts.clear();
			allAccounts = null;
		}
		if (App.app.bankTransferAccounts != null) {
			App.app.bankTransferAccounts.clear();
			App.app.bankTransferAccounts = null;
		}
		if (App.app.transferEntryAccounts != null) {
			App.app.transferEntryAccounts.clear();
			App.app.transferEntryAccounts = null;
		}
		if (App.app.chargeAccounts != null) {
			App.app.chargeAccounts.clear();
			App.app.chargeAccounts = null;
		}
		if (App.app.simTopUpAccounts != null) {
			App.app.simTopUpAccounts.clear();
			App.app.simTopUpAccounts = null;
		}
		if (App.app.getRecipientListModel != null) {
			if (App.app.getRecipientListModel.getBankRecipientList() != null) {
				App.app.getRecipientListModel.getBankRecipientList().clear();
			}
			if (App.app.getRecipientListModel.getCardRecipientList() != null) {
				App.app.getRecipientListModel.getCardRecipientList().clear();
			}
			if (App.app.getRecipientListModel.getPhoneRecipientList() != null) {
				App.app.getRecipientListModel.getPhoneRecipientList().clear();
			}
			App.app.getRecipientListModel = null;
		}
		return true;
	}

	private List<AccountsForServiceModel> allAccounts;

	// private List<AccountsForServiceModel>
	// bankTransferAccounts=App.app.bankTransferAccounts;

	// private List<AccountsForServiceModel>
	// transferEntryAccounts=App.app.transferEntryAccounts;

	// private List<AccountsForServiceModel>
	// simTopUpAccounts=App.app.simTopUpAccounts;

	// private List<AccountsForServiceModel>
	// chargeAccounts=App.app.chargeAccounts;

	// private List<AccountsForServiceModel>
	// transferEntryPayeeAccounts=App.app.transferEntryPayeeAccounts;

	// private GetRecipientListModel
	// getRecipientListModel=App.app.getRecipientListModel;

	private CompanyAmountResponseModel getAmountAvailableLs() {
		AccountsModel accountsModel = getPayer();
		PhoneRecipient pr = (PhoneRecipient) getPayee();
		if (pr == null) {
			return null;
		}

		String postData = CompanyAmountJson.CompanyAmountReportProtocal(
				accountsModel.getAccountCode(), pr.getProvider(),
				pr.getPhoneNumber(), Contants.publicModel);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		final CompanyAmountResponseModel companyAmount = CompanyAmountJson
				.ParseCompanyAmountResponse(httpResult);
		return companyAmount;
	}

	private AccountsModel getPayer() {
		if (showForm == PaymentsManager.FORM_RECENT_PAYMENT
				|| showForm == FORM_PENDING_PAYMENT
				|| showForm == PaymentsManager.FORM_ACCOUNT) {
			return curPayer;
		}
		if(account_folders_cf_ad.getSelected()<0){
        	return null;
        }
		return (AccountsModel) account_folders_cf.getCoverFlowSelectedItem();
	}

	private String genereateTransferType() {
		if (getPaymentType() == BANK_TRANSFER) {
			return "0";
		} else if (getPaymentType() == TRANSFER_ENTRY) {
			return "1";
		}
		return null;
	}

	private Object getPayee() {
		Object result = null;
		if (showForm == PaymentsManager.FORM_RECENT_PAYMENT
				|| showForm == FORM_PENDING_PAYMENT) {
			result = curPayee;
		} else {
			switch (getPaymentType()) {
			case BANK_TRANSFER:
				BankRecipient br = null;
				AccountsModel accountByPhone = null;
				if(bank_payee_cf_ad.getSelected()<0){
                	
                }else
				if (payee_folders_cf.getCount()
						- payee_folders_cf.getSelectedItemPosition() == 1) {
					accountByPhone = (AccountsModel) tmp_payee_phone;
				} else if (payee_folders_cf.getCount()
						- payee_folders_cf.getSelectedItemPosition() == 2) {
					br = (BankRecipient) tmp_payee;
				} else {
					result = payee_folders_cf.getCoverFlowSelectedItem();
					if (result instanceof AccountsModel) {
						accountByPhone = (AccountsModel) result;
					} else {
						br = (BankRecipient) result;
					}
					// br =
					// (BankRecipient)payee_folders_cf.getCoverFlowSelectedItem();
				}
				if (br != null) {
					DestaccountModel destAccount = new DestaccountModel();
					destAccount.setIban(br.getIbanCode());
					destAccount.setTitle(br.getName());
					destAccount.setState(br.getId());
					destAccount.setBic(br.getBic());
					result = destAccount;
				} else if (null != accountByPhone) {
					// DestaccountModel destAccount = new DestaccountModel();
					// destAccount.setIban(accountByPhone.getIbanCode());
					// destAccount.setTitle(accountByPhone.getAccountAlias());
					// destAccount.setState(accountByPhone.getCardState());
					// destAccount.setBic(accountByPhone.getBankCode());
					result = accountByPhone;
				}
				break;
			case TRANSFER_ENTRY:
				if(entry_payee_cf_ad.getSelected()<0){
                	
                }else{
                	int index=payee_folders_cf.getSelectedItemPosition();
                	if(entry_payee_cf_ad.getClickDisableId()>=0&&index>=entry_payee_cf_ad.getClickDisableId()){
                		++index;
                	}
                AccountsModel am = (AccountsModel)entry_payee_cf_ad.getDatas().get(index);//payee_folders_cf.getCoverFlowSelectedItem();
            	DestaccountModel destAccount = new DestaccountModel();
				destAccount = new DestaccountModel();
				destAccount.setIban(am.getIbanCode());
				destAccount.setTitle(am.getAccountAlias());
				destAccount.setState(am.getCardState());
				destAccount.setBic(am.getAccountId());
				result = destAccount;
                }
				break;
			case CARD_TOP_UP:
				CardRecipient cr=null;
                if(card_payee_cf_ad.getSelected()<0){
                	
                }else
				if (payee_folders_cf.getCount()
						- payee_folders_cf.getSelectedItemPosition() > 1)
					cr = (CardRecipient) payee_folders_cf
							.getCoverFlowSelectedItem();
				else
					cr = (CardRecipient) tmp_payee;
				result = cr;
				break;
			case PHONE_TOP_UP:
				PhoneRecipient pr=null;
                if(phone_payee_cf_ad.getSelected()<0){
                	
                }else if (phone_payee_folders_cf.getCount() - curPayeeSelectId > 1){
					CoverFlowImageAdapter coverFlowViewAdapter = (CoverFlowImageAdapter) phone_payee_folders_cf
							.getAdapter();
					pr = (PhoneRecipient) coverFlowViewAdapter.getDatas().get(
							curPayeeSelectId);
				} else
					pr = (PhoneRecipient) tmp_payee;
				result = pr;
				break;
			}
		}
		return result;
	}

	private DestaccountModel generateDestAccountModel() {
		Object tmpPayee = getPayee();
		if (tmpPayee == null) {
			return null;
		}
		DestaccountModel destAccount = null;
		if (tmpPayee instanceof AccountsModel) {
			AccountsModel accountByPhone = (AccountsModel) tmpPayee;
			destAccount = new DestaccountModel();
			destAccount.setIban(accountByPhone.getIbanCode());
			destAccount.setTitle(accountByPhone.getAccountAlias());
			destAccount.setState(accountByPhone.getCardState());
			destAccount.setBic(accountByPhone.getBankCode());
		} else if (tmpPayee instanceof CardRecipient) {
			CardRecipient cr = (CardRecipient) tmpPayee;
			destAccount = new DestaccountModel();
			destAccount.setIban(cr.getCardNumber());
			destAccount.setTitle(cr.getName());
			destAccount.setState(cr.getLast4Digits());
			destAccount.setBic(cr.getId());
		} else if (tmpPayee instanceof PhoneRecipient) {
			destAccount = new DestaccountModel();
			PhoneRecipient pr = (PhoneRecipient) tmpPayee;
			destAccount.setIban(pr.getPhoneNumber());
			destAccount.setTitle(pr.getName());
			destAccount.setState(pr.getProvider());
			destAccount.setBic(pr.getId());
		} else {
			destAccount = (DestaccountModel) tmpPayee;
		}

		// bic = value.bic;
		String iban = destAccount.getIban();
		String state = "";
		if (iban == null || iban.length() < 2) {
			state = "IT";
		} else {
			state = iban.substring(0, 2).toUpperCase();
		}
		destAccount.setState(state);
		destAccount.setBic("n/a");

		if (TextUtils.isEmpty(destAccount.getTitle())) {
			destAccount.setTitle("n/a");
		}
		if (TextUtils.isEmpty(destAccount.getIban())) {
			destAccount.setIban("n/a");
		}
		return destAccount;
	}

	private AmountAvailable generateAmountAvailable() {
		int index = top_up_amount_gv.getCheckedRadioButtonId();
		if (mAmountAvailableLs != null && index >= 0
				&& index < mAmountAvailableLs.size())
			return mAmountAvailableLs.get(index);
		return null;
	}

	private List<AmountAvailable> generateAmountAvailableList(
			AmountAvailable amountAvailable) {
		List<AmountAvailable> amountAvailableList = new ArrayList<AmountAvailable>();
		amountAvailableList.add(amountAvailable);
		return amountAvailableList;
	}

	private String generatePin() {
		return this.pin_et.getText().toString();
	}

	private CheckTransferResponseModel validateBankTransfer() {

		AccountsModel accountsModel = getPayer();

		String transferType = genereateTransferType();
		double amountInt = getAmount();
		String description = generateDescription();
		String dateString = TimeUtil.getDateString(generateDate(),
				TimeUtil.dateFormat2);
		DestaccountModel destAccount = generateDestAccountModel();
		String transferId=showForm==FORM_PENDING_PAYMENT&&mPendingTransferModel!=null?mPendingTransferModel.getTransferId():"";
		String postData = CheckTransferJson.CheckTransferReportProtocal(
				accountsModel.getAccountCode(), amountInt, "0", transferType,
				description, dateString, destAccount, Contants.publicModel,
				"1000",transferId);

		LogManager.d("transferType  validate" + transferType);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		CheckTransferResponseModel checkTransfer = CheckTransferJson
				.ParseCheckTransferResponse(httpResult);
		return checkTransfer;
	}

	public static GetCardsResponseModel vertifyCard(Context activity,
			String accountCode, CardRecipient cr) {

		String postData = GetCardsJson.GetCardsReportProtocal(
				Contants.publicModel, cr.getName(), cr.getLast4Digits(),
				accountCode);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		GetCardsResponseModel getCards = GetCardsJson
				.parseGetCardResponse(httpResult);
		return getCards;
	}

	private CheckRechargeCardResponseModel validateCard() {
		String dateString = TimeUtil.getDateString(generateDate(),
				TimeUtil.dateFormat2);
		AccountsModel accountsModel = getPayer();
		CardRecipient cr = (CardRecipient) getPayee();// generateCardPayee();
		double amountdouble = getAmount();
		String description = generateDescription();
		String title = "10";
		String transferId=showForm==FORM_PENDING_PAYMENT&&mPendingTransferModel!=null?mPendingTransferModel.getTransferId():"";
		String postData = CheckRechargeCardJson
				.CheckRechargeCardReportProtocal(
						accountsModel.getAccountCode(), Contants.publicModel,
						amountdouble, description,
						tmp_InfoCardsModel.getCardHash(), cr.getCardNumber(),
						tmp_InfoCardsModel.getTitle(), cr.getName(), "1000",transferId);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		CheckRechargeCardResponseModel checkRechargeCard = CheckRechargeCardJson
				.ParseCheckRechargeCardResponse(httpResult);
		return checkRechargeCard;
	}

	private CheckSimTopUpResponseModel validateSimTopUp() {
		AccountsModel accountsModel = getPayer();

		List<AmountAvailable> amountAvailableList = generateAmountAvailableList(generateAmountAvailable());
		PhoneRecipient pr = (PhoneRecipient) getPayee();
		String transferId=showForm==FORM_PENDING_PAYMENT&&mPendingTransferModel!=null?mPendingTransferModel.getTransferId():"";
		String postData = CheckSimTopUpJson.CheckSimTopUpReportProtocal(
				Contants.publicModel, amountAvailableList,
				accountsModel.getAccountCode(), pr.getProvider(),
				pr.getPhoneNumber(), "1000",transferId);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		CheckSimTopUpResponseModel checkSimTopUp = CheckSimTopUpJson
				.ParseCheckSimTopUpResponse(httpResult);
		return checkSimTopUp;
	}

	private String post(String r) {
		HttpConnector httpConnector = new HttpConnector();
		return httpConnector
				.requestByHttpPost(Contants.mobile_url, r, activity);
	}

	/**
	 * Check transaction
	 * 
	 * @return
	 */
	private Object validate() {
		switch (getPaymentType()) {
		case BANK_TRANSFER:
		case TRANSFER_ENTRY:
			checkTransfer = null;
			return validateBankTransfer();
		case CARD_TOP_UP:
			checkRechargeCard = null;
			return validateCard();
		case PHONE_TOP_UP:
			checkSimTopUp = null;
			return validateSimTopUp();
		case PRECOMPILED_BILL:
			AccountsModel accountsModel = getPayer();
			CheckBillPaymentValueModel checkBillPaymentValue = new CheckBillPaymentValueModel();
			checkBillPaymentValue
					.setAccountCode(accountsModel.getAccountCode());
			checkBillPaymentValue.setPurposeDescription("N/A");
			checkBillPaymentValue.setPostalAccount(mPaymentsManagerBill
					.getAccountNumber());
			checkBillPaymentValue.setAmount(mPaymentsManagerBill.getAmount());
			checkBillPaymentValue.setBillNumber(mPaymentsManagerBill
					.getBillNumber());
			checkBillPaymentValue.setBillType(mPaymentsManagerBill.getType());
			checkBillPaymentValue.setHolderName(mPaymentsManagerBill
					.getBillHolderName());
			checkBillPaymentValue.setCurrency("$");
			if(showForm==FORM_PENDING_PAYMENT&&mPendingTransferModel!=null){
				checkBillPaymentValue.setTransferId(mPendingTransferModel.getTransferId());
				
			}
			
			NewPayee.BillHolder mBillHolder = mPaymentsManagerBill.getHolder();
			if (mBillHolder != null) {
				checkBillPaymentValue.setAddress(mBillHolder.address);
				checkBillPaymentValue.setCity(mBillHolder.city);
				checkBillPaymentValue.setDistrict(mBillHolder.state);
				checkBillPaymentValue.setPostalCode(mBillHolder.zipCode);
			} else {
				checkBillPaymentValue.setAddress("N/A");
				checkBillPaymentValue.setCity("N/A");
				checkBillPaymentValue.setDistrict("N/A");
				checkBillPaymentValue.setPostalCode("N/A");
			}
			checkBillPaymentValue.setDueDate(TimeUtil.getDateString(
					System.currentTimeMillis(), TimeUtil.dateFormat2));
			checkBillPaymentValue.setBeneficiaryName(mPaymentsManagerBill.getPayableTo());

			String json = CheckBillPaymentJson.CheckBillPaymentReportProtocal(
					Contants.publicModel, checkBillPaymentValue);
			json = post(json);
			mCheckBillPaymentResponseModel = CheckBillPaymentJson
					.ParseCheckBillPaymentResponse(json);
			return mCheckBillPaymentResponseModel;
		case BLANK_BILL:
			accountsModel = getPayer();
			checkBillPaymentValue = new CheckBillPaymentValueModel();
			checkBillPaymentValue
					.setAccountCode(accountsModel.getAccountCode());
			checkBillPaymentValue.setPostalAccount(mPaymentsManagerBlankBill
					.getAccountNumber());
			checkBillPaymentValue.setAmount(mPaymentsManagerBlankBill
					.getAmount());
			checkBillPaymentValue.setBillType(mPaymentsManagerBlankBill
					.getType());
			checkBillPaymentValue.setHolderName(mPaymentsManagerBlankBill
					.getBillHolderName());
			checkBillPaymentValue
					.setPurposeDescription(mPaymentsManagerBlankBill
							.getDescription());
			checkBillPaymentValue.setCurrency("$");
			if(showForm==FORM_PENDING_PAYMENT&&mPendingTransferModel!=null){
				checkBillPaymentValue.setTransferId(mPendingTransferModel.getTransferId());
				
			}
			mBillHolder = mPaymentsManagerBlankBill.getHolder();
			if (mBillHolder != null) {
				checkBillPaymentValue.setAddress(mBillHolder.address);
				checkBillPaymentValue.setCity(mBillHolder.city);
				checkBillPaymentValue.setDistrict(mBillHolder.state);
				checkBillPaymentValue.setPostalCode(mBillHolder.zipCode);
			} else {
				checkBillPaymentValue.setAddress("N/A");
				checkBillPaymentValue.setCity("N/A");
				checkBillPaymentValue.setDistrict("N/A");
				checkBillPaymentValue.setPostalCode("N/A");
			}
			checkBillPaymentValue.setBeneficiaryName(mPaymentsManagerBlankBill.getPayableTo());
			checkBillPaymentValue.setDueDate(TimeUtil.getDateString(
					System.currentTimeMillis(), TimeUtil.dateFormat2));
			json = CheckBillPaymentJson.CheckBillPaymentReportProtocal(
					Contants.publicModel, checkBillPaymentValue);
			json = post(json);
			mCheckBillPaymentResponseModel = CheckBillPaymentJson
					.ParseCheckBillPaymentResponse(json);
			return mCheckBillPaymentResponseModel;
		case MAV_RAV:
			accountsModel = getPayer();
			checkBillPaymentValue = new CheckBillPaymentValueModel();
			checkBillPaymentValue
					.setAccountCode(accountsModel.getAccountCode());
			checkBillPaymentValue.setPostalAccount(mPaymentsManagerMAVRAV
					.getAccountNumber());
			checkBillPaymentValue.setAmount(mPaymentsManagerMAVRAV.getAmount());
			checkBillPaymentValue.setBillNumber(mPaymentsManagerMAVRAV
					.getBillNumber());
			checkBillPaymentValue.setBillType(mPaymentsManagerMAVRAV.getType());
			checkBillPaymentValue.setHolderName(mPaymentsManagerMAVRAV
					.getBillHolderName());
			checkBillPaymentValue.setPurposeDescription(mPaymentsManagerMAVRAV
					.getDescription());
			checkBillPaymentValue.setCurrency("$");
			if(showForm==FORM_PENDING_PAYMENT&&mPendingTransferModel!=null){
				checkBillPaymentValue.setTransferId(mPendingTransferModel.getTransferId());
				
			}
			mBillHolder = mPaymentsManagerMAVRAV.getHolder();
			if (mBillHolder != null) {
				checkBillPaymentValue.setAddress(mBillHolder.address);
				checkBillPaymentValue.setCity(mBillHolder.city);
				checkBillPaymentValue.setDistrict(mBillHolder.state);
				checkBillPaymentValue.setPostalCode(mBillHolder.zipCode);
			} else {
				checkBillPaymentValue.setAddress("N/A");
				checkBillPaymentValue.setCity("N/A");
				checkBillPaymentValue.setDistrict("N/A");
				checkBillPaymentValue.setPostalCode("N/A");
			}
			checkBillPaymentValue.setBeneficiaryName(mPaymentsManagerMAVRAV.getPayableTo());
			checkBillPaymentValue.setDueDate(TimeUtil.getDateString(
					mPaymentsManagerMAVRAV.getDueTo(), TimeUtil.dateFormat2));
			json = CheckBillPaymentJson.CheckBillPaymentReportProtocal(
					Contants.publicModel, checkBillPaymentValue);
			json = post(json);
			mCheckBillPaymentResponseModel = CheckBillPaymentJson
					.ParseCheckBillPaymentResponse(json);
			return mCheckBillPaymentResponseModel;
		}
		return null;
	}

	public ResponsePublicModel insertRecipient(String recipientType, Object data) {
		// ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
		// progressOverlay.show("", new OnProgressEvent() {
		// @Override
		// public void onProgress() {
		// PayeeExpandedLayout payeeExpandedLayout =
		// (PayeeExpandedLayout)payeeExpander.expandedContainer;
		// if (!payeeExpandedLayout.addInPayee()) {
		// return true;
		// }

		String postData = null;
		postData = InsertRecipientJson.InsertRecipientReportProtocal(
				Contants.publicModel, recipientType, data);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		ResponsePublicModel responsePublicModel = InsertRecipientJson
				.ParseInsertRecipientResponse(httpResult);
		return responsePublicModel;
	}

	private void onInsertRecipient(ResponsePublicModel responsePublicModel) {
		if (responsePublicModel.isSuccess()) {
			switch (getPaymentType()) {
			case BANK_TRANSFER:

				// private NPAccountsModelAdapter entry_payee_cf_ad;
				bank_payee_cf_ad.getDatas().add((BankRecipient) tmp_payee);
				bank_payee_cf_ad.notifyDataSetChanged();
				break;
			case TRANSFER_ENTRY:
				break;
			case CARD_TOP_UP:
				card_payee_cf_ad.getDatas().add((CardRecipient) tmp_payee);
				card_payee_cf_ad.notifyDataSetChanged();
				break;
			case PHONE_TOP_UP:
				// PhoneRecipient object;
				phone_payee_cf_ad.getDatas().add((PhoneRecipient) tmp_payee);
				phone_payee_cf_ad.notifyDataSetChanged();
				break;
			}
			tmp_payee = null;
		} else {
			DialogManager.createMessageDialog(
					responsePublicModel.eventManagement.getErrorDescription(),
					activity).show();
		}
	}

	public GenerateOTPResponseModel askPin() {
		String otpChannel = Contants.getOtpChannelName();
		String postData = GenerateOTPJson.GenerateOTPReportProtocal(otpChannel,
				Contants.publicModel);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		GenerateOTPResponseModel generateOtp = GenerateOTPJson
				.ParseGenerateOTPResponse(httpResult);
		return generateOtp;
	}

	private void onConfirmResult(Object result) {
		switch (getPaymentType()) {
		case BANK_TRANSFER:
		case TRANSFER_ENTRY:
			ResponsePublicModel insertTransferresponse = (ResponsePublicModel) result;
			confirmResult = insertTransferresponse.isSuccess();
			if (confirmResult) {
				// DialogManager.createMessageDialog("transfer success!",
				// activity).show();
			} else {
				// activity.displayErrorMessage(insertTransferresponse.eventManagement
				// .getErrorDescription());
				// DialogManager.createMessageDialog(
				// insertTransferresponse.eventManagement.getErrorDescription(),
				// getContext()).show();
				// Toast.makeText(getContext(), "fail",
				// Toast.LENGTH_LONG).show();
			}
			setPaymentStep(R.id.new_payments_step3_rb);
			break;
		case CARD_TOP_UP:
			downloading_pd.dismiss();
			ResponsePublicModel insertRechargeCard = (ResponsePublicModel) result;
			confirmResult = insertRechargeCard.isSuccess();
			if (confirmResult) {
				// DialogManager.createMessageDialog("transfer success!",
				// activity).show();
			} else {
				// activity.displayErrorMessage(insertRechargeCard.eventManagement
				// .getErrorDescription());
				// DialogManager.createMessageDialog(
				// insertRechargeCard.eventManagement.getErrorDescription(),
				// getContext())
				// .show();
				// Toast.makeText(getContext(), "fail",
				// Toast.LENGTH_LONG).show();
			}
			setPaymentStep(R.id.new_payments_step3_rb);
			break;
		case PHONE_TOP_UP:
			downloading_pd.dismiss();
			SimTopUpResponseModel simTopUp = (SimTopUpResponseModel) result;
			confirmResult = simTopUp.responsePublicModel.isSuccess();
			if (confirmResult) {
				// DialogManager.createMessageDialog("Transaction success!",
				// activity).show();
				reset();
			} else {
				// activity.displayErrorMessage(simTopUp.responsePublicModel.eventManagement
				// .getErrorDescription());
				// DialogManager.createMessageDialog(
				// simTopUp.responsePublicModel.eventManagement.getErrorDescription(),
				// getContext()).show();
			}
			setPaymentStep(R.id.new_payments_step3_rb);
			break;
		case PRECOMPILED_BILL:
		case BLANK_BILL:
		case MAV_RAV:
			InsertBillPaymentModel mInsertBillPaymentModel = (InsertBillPaymentModel) result;
			confirmResult = mInsertBillPaymentModel.responsePublicModel
					.isSuccess();
			if (confirmResult) {
				// DialogManager.createMessageDialog("Transaction success!",
				// activity).show();
				reset();
			} else {
				// activity.displayErrorMessage(simTopUp.responsePublicModel.eventManagement
				// .getErrorDescription());
				// DialogManager.createMessageDialog(
				// simTopUp.responsePublicModel.eventManagement.getErrorDescription(),
				// getContext()).show();
			}
			setPaymentStep(R.id.new_payments_step3_rb);
			downloading_pd.dismiss();
			break;
		}
	}

	private Object confirm() {
		AccountsModel accountsModel = getPayer();
		switch (getPaymentType()) {
		case BANK_TRANSFER:
		case TRANSFER_ENTRY:
			return confirmBankTransfer(checkTransfer);
		case CARD_TOP_UP:
			return confirmCardRecharge(checkRechargeCard);
		case PHONE_TOP_UP:
			return confirmTranslationSimTopUp(checkSimTopUp);
		case PRECOMPILED_BILL:
			CheckBillPaymentValueModel  mCheckBillPaymentValueModel =mCheckBillPaymentResponseModel.getCheckBillPaymentValueModel();
			mCheckBillPaymentValueModel.setBillNumber(mPaymentsManagerBill.getBillNumber());
			mCheckBillPaymentValueModel.setBeneficiaryName(mPaymentsManagerBill.getPayableTo());
			mCheckBillPaymentValueModel.setPurposeDescription("Not valid purpose description");
			mCheckBillPaymentValueModel.setBillType(mPaymentsManagerBill.getType());
			mCheckBillPaymentValueModel.setAccountCode(accountsModel.getAccountCode());
			mCheckBillPaymentValueModel.setPostalAccount(mPaymentsManagerBill.getAccountNumber());
			mCheckBillPaymentValueModel.setCurrency(Contants.COUNTRY);
			mCheckBillPaymentValueModel.setTransferCheck(App.isNewPaymentsUpdate);
			NewPayee.BillHolder mBillHolder = mPaymentsManagerBill.getHolder();
			if (mBillHolder != null) {
				mCheckBillPaymentValueModel.setAddress(mBillHolder.address);
				mCheckBillPaymentValueModel.setCity(mBillHolder.city);
				mCheckBillPaymentValueModel.setDistrict(mBillHolder.state);
				mCheckBillPaymentValueModel.setPostalCode(mBillHolder.zipCode);
			} else {
				mCheckBillPaymentValueModel.setAddress("N/A");
				mCheckBillPaymentValueModel.setCity("N/A");
				mCheckBillPaymentValueModel.setDistrict("N/A");
				mCheckBillPaymentValueModel.setPostalCode("N/A");
			}
			String json = InsertBillPaymentJson
					.InsertBillPaymentReportProtocal(Contants.publicModel,
							mCheckBillPaymentValueModel,
							generateOtp.getOtpKeySession(), generatePin(), Contants.getOtpChannelName());
			HttpConnector httpConnector = new HttpConnector();
			json = httpConnector.requestByHttpPost(
					Contants.mobile_url, json, activity);
			return InsertBillPaymentJson.ParseCheckBillPaymentResponse(json);
		case BLANK_BILL:
			 mCheckBillPaymentValueModel =mCheckBillPaymentResponseModel.getCheckBillPaymentValueModel();
			mCheckBillPaymentValueModel.setBillNumber(mPaymentsManagerBlankBill.getAccountNumber());
			mCheckBillPaymentValueModel.setBeneficiaryName(mPaymentsManagerBlankBill.getPayableTo());
			mCheckBillPaymentValueModel.setPurposeDescription(mPaymentsManagerBlankBill.getDescription());
			mCheckBillPaymentValueModel.setBillType(mPaymentsManagerBlankBill.getType());
			mCheckBillPaymentValueModel.setAccountCode(accountsModel.getAccountCode());
			mCheckBillPaymentValueModel.setPostalAccount(mPaymentsManagerBlankBill.getAccountNumber());
			mCheckBillPaymentValueModel.setCurrency(Contants.COUNTRY);
			mCheckBillPaymentValueModel.setTransferCheck(App.isNewPaymentsUpdate);
			mBillHolder = mPaymentsManagerBlankBill.getHolder();
			if (mBillHolder != null) {
				mCheckBillPaymentValueModel.setAddress(mBillHolder.address);
				mCheckBillPaymentValueModel.setCity(mBillHolder.city);
				mCheckBillPaymentValueModel.setDistrict(mBillHolder.state);
				mCheckBillPaymentValueModel.setPostalCode(mBillHolder.zipCode);
			} else {
				mCheckBillPaymentValueModel.setAddress("N/A");
				mCheckBillPaymentValueModel.setCity("N/A");
				mCheckBillPaymentValueModel.setDistrict("N/A");
				mCheckBillPaymentValueModel.setPostalCode("N/A");
			}
			json = InsertBillPaymentJson
					.InsertBillPaymentReportProtocal(Contants.publicModel,
							mCheckBillPaymentValueModel,
							generateOtp.getOtpKeySession(), generatePin(),
							Contants.OTP_CHANNEL_MAIL);
			httpConnector = new HttpConnector();
			json = httpConnector.requestByHttpPost(
					Contants.mobile_url, json, activity);
			return InsertBillPaymentJson.ParseCheckBillPaymentResponse(json);
		case MAV_RAV:
			mCheckBillPaymentValueModel =mCheckBillPaymentResponseModel.getCheckBillPaymentValueModel();
			mCheckBillPaymentValueModel.setBillNumber(mPaymentsManagerMAVRAV.getBillNumber());
			mCheckBillPaymentValueModel.setBeneficiaryName(mPaymentsManagerMAVRAV.getPayableTo());
			mCheckBillPaymentValueModel.setPurposeDescription(mPaymentsManagerMAVRAV.getDescription());
			mCheckBillPaymentValueModel.setBillType(mPaymentsManagerMAVRAV.getType());
			mCheckBillPaymentValueModel.setAccountCode(accountsModel.getAccountCode());
			mCheckBillPaymentValueModel.setPostalAccount(mPaymentsManagerMAVRAV.getAccountNumber());
			mCheckBillPaymentValueModel.setCurrency(Contants.COUNTRY);
			mCheckBillPaymentValueModel.setTransferCheck(App.isNewPaymentsUpdate);
			mBillHolder = mPaymentsManagerMAVRAV.getHolder();
			if (mBillHolder != null) {
				mCheckBillPaymentValueModel.setAddress(mBillHolder.address);
				mCheckBillPaymentValueModel.setCity(mBillHolder.city);
				mCheckBillPaymentValueModel.setDistrict(mBillHolder.state);
				mCheckBillPaymentValueModel.setPostalCode(mBillHolder.zipCode);
			} else {
				mCheckBillPaymentValueModel.setAddress("N/A");
				mCheckBillPaymentValueModel.setCity("N/A");
				mCheckBillPaymentValueModel.setDistrict("N/A");
				mCheckBillPaymentValueModel.setPostalCode("N/A");
			}
			json = InsertBillPaymentJson
					.InsertBillPaymentReportProtocal(Contants.publicModel,
							mCheckBillPaymentValueModel,
							generateOtp.getOtpKeySession(), generatePin(),
							Contants.OTP_CHANNEL_MAIL);
			httpConnector = new HttpConnector();
			json = httpConnector.requestByHttpPost(
					Contants.mobile_url, json, activity);
			return InsertBillPaymentJson.ParseCheckBillPaymentResponse(json);
		}
		return null;
	}

	private ResponsePublicModel confirmBankTransfer(
			CheckTransferResponseModel checkTransfer) {
		AccountsModel accountsModel = getPayer();
		double amountInt = getAmount();
		String transferType = genereateTransferType();
		String description = generateDescription();
		String dateString = TimeUtil.getDateString(generateDate(),
				TimeUtil.dateFormat2);
		DestaccountModel destAccount = generateDestAccountModel();
		String pin = generatePin();
		String postData = InsertTransferJson.InsertTransferReportProtocal(
				Contants.publicModel, accountsModel.getAccountCode(),
				amountInt, "0", transferType, description, dateString,
				destAccount, pin, generateOtp.getOtpKeySession(),
				Contants.OTP_CHANNEL_MAIL, "USD", checkTransfer);

		LogManager.d("transferType" + transferType);

		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		final ResponsePublicModel insertTransferresponse = InsertTransferJson
				.ParseInsertTransferResponse(httpResult);
		return insertTransferresponse;

	}

	private SimTopUpResponseModel confirmTranslationSimTopUp(
			CheckSimTopUpResponseModel checkSimTopUp) {
		AccountsModel accountsModel = getPayer();
		PhoneRecipient pr = (PhoneRecipient) getPayee();

		List<AmountAvailable> amountAvailableList = generateAmountAvailableList(generateAmountAvailable());
		String postData = SimTopUpJson.SimTopUpReportProtocal(
				Contants.publicModel, accountsModel.getAccountCode(),
				pr.getProvider(), pr.getPhoneNumber(), generatePin(),
				generateOtp.getOtpKeySession(), Contants.OTP_CHANNEL_MAIL,
				amountAvailableList, "1000", checkSimTopUp);

		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		final SimTopUpResponseModel simTopUp = SimTopUpJson
				.ParseSimTopUpResponse(httpResult);
		return simTopUp;
	}

	private ResponsePublicModel confirmCardRecharge(
			CheckRechargeCardResponseModel checkRechargeCard) {
		// String dateString = generateDate();
		// PayeeExpandedLayout.PreparedCardValue value =
		// (PayeeExpandedLayout.PreparedCardValue)payeeExpander
		// .getValue();
		String description = generateDescription();
		double amountdouble = getAmount();
		AccountsModel accountsModel = getPayer();
		CardRecipient cr = (CardRecipient) getPayee();
		// RequestPublicModel publicModel,
		// String accountCode, double amount, String destCardCode, String
		// cardNumber,
		// String purposeDescription, String name, String title, String
		// otpValue,
		// String otpKeySession, String otpChannel,String currency
		String postData = InsertRechargeCardJson
				.InsertRechargeCardReportProtocal(Contants.publicModel,
						accountsModel.getAccountCode(), amountdouble,
						tmp_InfoCardsModel.getCardHash(), cr.getCardNumber(),
						description, cr.getName(),
						tmp_InfoCardsModel.getTitle(), generatePin(),
						generateOtp.getOtpKeySession(),
						Contants.OTP_CHANNEL_MAIL, "1000", checkRechargeCard);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(
				Contants.mobile_url, postData, activity);
		final ResponsePublicModel insertRechargeCard = InsertRechargeCardJson
				.ParseInsertRechargeCardResponse(httpResult);
		return insertRechargeCard;
	}

	protected void startAddItem(int requestCode, int flag, Serializable payee) {
		NewPayee.start(activity, requestCode, flag, payee);
	}

	protected void startAddItem(int requestCode, int flag, String accountCode,
			Serializable payee) {
		NewPayee.start(activity, requestCode, flag, accountCode, payee);
	}

	private void toSelectAccount(String accountCode) {
		if (accountCode == null) {
			return;
		}
		List<AccountsModel> accounts = account_folders_cf_ad.getDatas();
		if (accounts != null)
			for (int i = accounts.size() - 1; i >= 0; i--) {
				if (accounts.get(i).getAccountCode().equals(accounts)) {
					account_folders_cf.setSelection(i, true);
					break;
				}
			}
	}

	private void toSelectBeneficiary(BankRecipient beneficiary) {
		if (beneficiary == null) {
			return;
		}
		List<Account> beneficiarys = bank_payee_cf_ad.getDatas();
		if (beneficiarys == null) {
			return;
		}

		for (int i = beneficiarys.size() - 1; i >= 0; i--) {
			if (beneficiarys.get(i).equals(beneficiary)) {
				payee_folders_cf.setSelection(i, true);
				break;
			}
		}
	}

	private void toSelectBeneficiary(AccountsModel beneficiary) {
		if (beneficiary == null) {
			return;
		}
		List<AccountsModel> beneficiarys = entry_payee_cf_ad.getDatas();
		if (beneficiarys == null) {
			return;
		}

		for (int i = beneficiarys.size() - 1; i >= 0; i--) {
			if (beneficiarys.get(i).equals(beneficiary)) {
				payee_folders_cf.setSelection(i, true);
				break;
			}
		}
	}

	private void toSelectBeneficiary(CardRecipient beneficiary) {
		if (beneficiary == null) {
			return;
		}
		List<CardRecipient> beneficiarys = card_payee_cf_ad.getDatas();
		if (beneficiarys == null) {
			return;
		}

		for (int i = beneficiarys.size() - 1; i >= 0; i--) {
			if (beneficiarys.get(i).equals(beneficiary)) {
				payee_folders_cf.setSelection(i, true);
				break;
			}
		}

	}

	private void toSelectBeneficiary(PhoneRecipient beneficiary) {
		if (beneficiary == null) {
			return;
		}
		List<PhoneRecipient> beneficiarys = phone_payee_cf_ad.getDatas();
		if (beneficiarys == null) {
			return;
		}

		for (int i = beneficiarys.size() - 1; i >= 0; i--) {
			if (beneficiarys.get(i).equals(beneficiary)) {
				phone_payee_folders_cf.setSelection(i, true);
				break;
			}
		}

	}

	private void toSelectBeneficiary(int type, Object beneficiary) {
		switch (type) {
		case BANK_TRANSFER:
			toSelectBeneficiary((BankRecipient) beneficiary);
			break;
		case TRANSFER_ENTRY:
			toSelectBeneficiary((AccountsModel) beneficiary);
			break;
		case CARD_TOP_UP:
			toSelectBeneficiary((CardRecipient) beneficiary);
			break;
		case PHONE_TOP_UP:
			toSelectBeneficiary((PhoneRecipient) beneficiary);
			break;
		}
	}

	private void setDate(long timeInMillis) {
		mTimeInMillis = timeInMillis;
	}

	private void setAmount(double amount) {
		this.amount = amount;
	}

	public void onShow(Object object) {
		reset();
		setPaymentStep(STEP_1);
	}

	@Override
	public void onClick(View v) {
		mGaInstance = GoogleAnalytics.getInstance(activity);
		mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);

		switch (v.getId()) {
		case R.id.amountbar_lin:
			endAountTextInput();
			break;
		case R.id.proceed_btn:
			if (getPaymentStep() == R.id.new_payments_step1_rb) {
				int curType = getPaymentType();
				if (curType == 0) {
					Toast.makeText(activity, R.string.pls_select_payments_type,
							Toast.LENGTH_LONG).show();
					break;
					
				}else if(getPayer()==null){
					Toast.makeText(activity, R.string.pls_select_payer, Toast.LENGTH_LONG).show();
					break;
					
				}
				double generateAmount = getAmount();
				switch (curType) {
				case TRANSFER_ENTRY:
					mGaTracker1
							.sendView("event.newpayment.type.transfer.entry");
				case BANK_TRANSFER:
					mGaTracker1.sendView("event.newpayment.type.bank.transfer");
					if(getPayee()==null){
						Toast.makeText(activity, R.string.pls_select_payee, Toast.LENGTH_LONG).show();
						return;
					}
					if (generateAmount == 0) {
						Toast.makeText(activity, R.string.missing_amount,
								Toast.LENGTH_LONG).show();
						break;
					}
					if (generateDate() <= 0) {
						Toast.makeText(activity, R.string.missing_date,
								Toast.LENGTH_LONG).show();
						break;
					}
					if (TextUtils.isEmpty(generateDescription())) {
						Toast.makeText(activity, R.string.missing_description,
								Toast.LENGTH_LONG).show();
						break;
					}
					setPaymentStep(R.id.new_payments_step2_rb);
					break;
				case CARD_TOP_UP:
					mGaTracker1.sendView("event.newpayment.type.card.topup");

					if (generateAmount == 0) {
						Toast.makeText(activity, R.string.missing_amount,
								Toast.LENGTH_LONG).show();
						break;
					}
					if(getPayee()==null){
						Toast.makeText(activity, R.string.pls_select_payee, Toast.LENGTH_LONG).show();
						return;
					}
					if (tmp_isVerifyCard != VERIFYCARD_SUCCESS) {
						downloading_pd.show();
						new PostThread(activity, null, null, mHandler,
								PaymentsManager.VERTIFY_CARD,
								PostThread.TYPE_VERTIFY_CARD).start();
					} else {
						setPaymentStep(R.id.new_payments_step2_rb);
					}
					break;
				case PHONE_TOP_UP:
					mGaTracker1.sendView("event.newpayment.type.phone.topup");

					if (generateAmount == 0) {
						Toast.makeText(activity, R.string.missing_amount,
								Toast.LENGTH_LONG).show();
						break;
					}
					if(getPayee()==null){
						Toast.makeText(activity, R.string.pls_select_payee, Toast.LENGTH_LONG).show();
						return;
					}
					// if(generateAmount>generateAccountModel().getBalance().getAvailableBalance()){
					// displayRecapDialog(activity,
					// activity.getString(R.string.pls_input_right_amount))
					// .show();
					// break;
					// }
					setPaymentStep(R.id.new_payments_step2_rb);
					break;
				case PRECOMPILED_BILL:
					if (TextUtils.isEmpty(mPaymentsManagerBill
							.getAccountNumber())) {
						activity.displayErrorMessage(activity
								.getString(R.string.missing_account_number));

					} else if (mPaymentsManagerBill.getAmount() <= 0) {
						activity.displayErrorMessage(activity
								.getString(R.string.missing_amount));

					} else {
						setPaymentStep(R.id.new_payments_step2_rb);
					}
					break;
				case BLANK_BILL:
					if (TextUtils.isEmpty(mPaymentsManagerBlankBill
							.getAccountNumber())) {
						activity.displayErrorMessage(activity
								.getString(R.string.missing_account_number));

					} else if (mPaymentsManagerBlankBill.getAmount() <= 0) {
						activity.displayErrorMessage(activity
								.getString(R.string.missing_amount));

					} else {
						setPaymentStep(R.id.new_payments_step2_rb);
					}
					break;
				case MAV_RAV:
					if (TextUtils.isEmpty(mPaymentsManagerMAVRAV
							.getBillNumber())) {
						activity.displayErrorMessage(activity
								.getString(R.string.missing_bill_number));

					} else if (mPaymentsManagerMAVRAV.getAmount() <= 0) {
						activity.displayErrorMessage(activity
								.getString(R.string.missing_amount));

					} else {
						setPaymentStep(R.id.new_payments_step2_rb);
					}
					break;
				default:
					activity.displayErrorMessage(activity
							.getString(R.string.pls_select_payments_type));
					break;
				}

			} else if (getPaymentStep() == R.id.new_payments_step2_rb) {
				if (pin_lin.getVisibility() != View.VISIBLE) {
					downloading_pd.show();
					new PostThread(activity, null, null, mHandler,
							PaymentsManager.VALIDATE, PostThread.TYPE_VALIDATE)
							.start();
				} else {
					if (TextUtils.isEmpty(pin_et.getText())) {
						Toast.makeText(activity, R.string.missing_pin,
								Toast.LENGTH_SHORT).show();
						return;
					}
					downloading_pd.show();
					new PostThread(activity, null, null, mHandler,
							PaymentsManager.CONFIRM, PostThread.TYPE_CONFIRM)
							.start();
				}
			}
			break;
		case R.id.execution_date_et:
			final Calendar calender = Calendar.getInstance();
			// CustomerDatePickerDialog.test(activity, calender, );
			new DatePickerDialog(activity, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					calender.set(year, monthOfYear, dayOfMonth);
					setDate(calender.getTimeInMillis());
					showDate(View.VISIBLE, calender.getTimeInMillis());

					// Toast.makeText(activity,
					// year+"/"+monthOfYear+"/"+dayOfMonth,
					// Toast.LENGTH_SHORT).show();
				}
			}, calender.get(calender.YEAR), calender.get(calender.MONDAY),
					calender.get(calender.DATE)).show();
			setFoucs(showDescription(View.VISIBLE));
			setProceedButtonClickAble(true);
			break;
		case R.id.amount_doller_et:
			switch (getPaymentType()) {
			case BANK_TRANSFER:
			case TRANSFER_ENTRY:
			case CARD_TOP_UP:
				break;
			case PHONE_TOP_UP:
				break;
			default:
				Toast.makeText(activity, R.string.pls_select_payments_type,
						Toast.LENGTH_SHORT).show();
				break;
			}
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		switch (arg0.getId()) {
		case R.id.payment_type_sp:
			curPaymentType = position;
			inviliAccountView(position);
			inviliViewByPaymentType(position);
			// payment_type_sp.invalidate();
			break;
		case R.id.account_folders_cf:
			if (getPaymentType() == TRANSFER_ENTRY) {
				if(account_folders_cf_ad.getSelected()>=0){
            		setEntryDiselect(position);
            	}
			}
			
			break;
		case R.id.payee_folders_cf:
			if (getPaymentType() == TRANSFER_ENTRY) {
				long index = -1;
				if (this.showForm == FORM_ACCOUNT) {
					index = curPayerAccoutSeltectId;
				} else if (showForm == FORM_DEFAULT) {
					index = account_folders_cf.getSelectedItemId();
				}
				if (index == position) {
					if (oldPosition > position) {
						if (position > 0) {
							payee_folders_cf.setSelection(position - 1, true);
						} else if (payee_folders_cf.getCount() - 1 > position) {
							payee_folders_cf.setSelection(position + 1, true);
						}
					} else {
						if (payee_folders_cf.getCount() - 1 > position) {
							payee_folders_cf.setSelection(position + 1, true);
						} else if (position > 0) {
							payee_folders_cf.setSelection(position - 1, true);
						}
					}
				} else {
					oldPosition = position;
				}
			}
			break;
		case R.id.phone_payee_folders_cf:
			if (position != amountAvailableIndex) {
				showAmount(View.GONE, -1);
				amountAvailableIndex = -1;
			}
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.account_folders_cf:
			endAountTextInput();
			curPayerAccoutSeltectId = arg2;
			int paymentType = getPaymentType();
			if (paymentType == TRANSFER_ENTRY) {
				setEntryDiselect(arg2);
			}
			if (getPaymentType() == PHONE_TOP_UP) {
				setFoucs(phone_payee_folders_cf);
			} else {
				setFoucs(amountbar_lin);
			}

			break;
		case R.id.payee_folders_cf:
			endAountTextInput();
			curPayeeSelectId = arg2;
			int payment = getPaymentType();
			// if (getPaymentType() == CARD_TOP_UP) {
			// showDescription(View.VISIBLE);
			// } else if (getPaymentType() != 0) {
			// showDate(View.VISIBLE);
			// }
			if (payment == BANK_TRANSFER && arg2 == arg0.getCount() - 2) {
				isTransferByPhone = false;
				startAddItem(NewPayee.NEW_TRANSFER_PAYEE,
						NewPayee.NEW_TRANSFER_PAYEE, tmp_payee);
			} else if (payment == BANK_TRANSFER && arg2 == arg0.getCount() - 1) {
				isTransferByPhone = true;
				startAddItem(NewPayee.NEW_TRANSFER_PAYEE_BY_PHONE,
						NewPayee.NEW_TRANSFER_PAYEE_BY_PHONE, tmp_payee_phone);
			} else if (payment == CARD_TOP_UP && arg2 == arg0.getCount() - 1) {
				startAddItem(NewPayee.NEW_CARD_PAYEE, NewPayee.NEW_CARD_PAYEE,
						getPayer().getAccountCode(), tmp_payee);
			} else {
				if (payment == CARD_TOP_UP) {
					setFoucs(description_et);
				} else if (payment == BANK_TRANSFER
						|| payment == TRANSFER_ENTRY) {
					setFoucs(execution_date_lin);
				}
			}
			break;
		case R.id.phone_payee_folders_cf:
			curPayeeSelectId = arg2;
			if (arg2 == arg0.getCount() - 1) {
				startAddItem(NewPayee.NEW_SIM_PAYEE, NewPayee.NEW_SIM_PAYEE,
						tmp_payee);
			} else

			if (amountAvailableIndex == arg2 && mAmountAvailableLs != null
					&& mAmountAvailableLs.size() > 0) {
				setFoucs(showAmount(View.VISIBLE));
			} else {
				amountAvailableIndex = arg2;
				loadAmountAvilable();
			}
			break;
		}
	}

	public boolean onLeftNavigationButtonClick(View v) {
		if (getPaymentStep() == STEP_1) {
			return false;
		} else if (getPaymentStep() == STEP_3 && confirmResult) {
			reset();
			setPaymentStep(STEP_1);
			return true;
		} else {
			setPaymentStep(STEP_1);
			return true;
		}
	}

	private void setEntryDiselect(int index) {
        if(entry_payee_cf_ad!=null){
            entry_payee_cf_ad.setClickDisableId(index);
            entry_payee_cf_ad.notifyDataSetChanged();
        }
        
//        if (payee_folders_cf.getVisibility() == View.VISIBLE
//                && index == payee_folders_cf.getSelectedItemPosition()) {
//            if (index + 1 < payee_folders_cf.getCount()) {
//                payee_folders_cf.setSelection(index + 1, true);
//            } else if (index > 0) {
//                payee_folders_cf.setSelection(index - 1, true);
//            }
//        }
    }

	private void loadAccountData(int paymentsType) {
		switch (paymentsType) {
		case GET_BANK_TRANSFER:
			downloading_pd.show();
			new PostThread(activity, null, null, mHandler,
					PaymentsManager.GET_BANK_TRANSFER,
					PostThread.TYPE_BANK_TRANSFER).start();

			break;
		case GET_TRANSFER_ENTRY:
			downloading_pd.show();
			new PostThread(activity, null, null, mHandler,
					PaymentsManager.GET_TRANSFER_ENTRY,
					PostThread.TYPE_TRANSFER_ENTRY).start();

			break;
		case GET_CARD_TOP_UP:
			downloading_pd.show();
			new PostThread(activity, null, null, mHandler,
					PaymentsManager.GET_CARD_TOP_UP,
					PostThread.TYPE_CARD_TOP_UP).start();

			break;
		case GET_PHONE_TOP_UP:
			downloading_pd.show();
			new PostThread(activity, null, null, mHandler,
					PaymentsManager.GET_PHONE_TOP_UP,
					PostThread.TYPE_PHONE_TOP_UP).start();

			break;
		case GET_ACCOUNT:
			downloading_pd.show();
			new PostThread(activity, null, null, mHandler,
					PaymentsManager.GET_ACCOUNT, PostThread.TYPE_GET_ACCOUNT)
					.start();
			break;
		}
	}

	private void loadAmountAvilable() {
		downloading_pd.show();
		new PostThread(activity, null, null, mHandler,
				PaymentsManager.GET_AMOUNT_AVAILABLE,
				PostThread.TYPE_GET_AMOUNT_AVAILABLE).start();
	}

	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		// Toast.makeText(activity, requestCode + " " + requestCode, 0).show();
		if (data == null) {
			return false;
		}
		if (mPaymentsManagerBill
				.onActivityResult(requestCode, resultCode, data)) {
			return true;
		}
		if (mPaymentsManagerBlankBill.onActivityResult(requestCode, resultCode,
				data)) {
			return true;
		}
		if (mPaymentsManagerMAVRAV.onActivityResult(requestCode, resultCode,
				data)) {
			return true;
		}
		String name = data.getStringExtra(NewPayee.NAME), number = data
				.getStringExtra(NewPayee.NUMBER), operator = data
				.getStringExtra(NewPayee.OPERATOR);
		boolean needAdd = data.getBooleanExtra(NewPayee.SELECTED, false);
		Log.d(TAG, "result(" + name + " , " + number + " , " + operator);
		String type = null;
		int existIndex = -1;
		CoverFlow payeeGallery = null;
		switch (requestCode) {
		case NewPayee.NEW_TRANSFER_PAYEE:
			BankRecipient bankRecipient = new BankRecipient();
			bankRecipient.setName(data.getStringExtra(NewPayee.NAME));
			bankRecipient.setIbanCode(data.getStringExtra(NewPayee.NUMBER));
			bankRecipient.setBic(data.getStringExtra(NewPayee.OPERATOR));
			tmp_payee = bankRecipient;
			if (!bank_payee_cf_ad.getDatas().contains(bankRecipient)) {
				type = InsertRecipientJson.BANK;
			} else {
				existIndex = bank_payee_cf_ad.getDatas().indexOf(bankRecipient);
			}
			payeeGallery = payee_folders_cf;
			// insertRecipient(InsertRecipientJson.BANK, bankRecipient);
			break;

		case NewPayee.NEW_TRANSFER_PAYEE_BY_PHONE:
			AccountsModel mAccountsModel = (AccountsModel) data
					.getSerializableExtra(NewPayee.OBJ_DATA);
			// bankRecipient.setIbanCode(data.getStringExtra(NewPayee.NUMBER));
			tmp_payee_phone = mAccountsModel;
			if (!bank_payee_cf_ad.getDatas().contains(mAccountsModel)) {
				type = InsertRecipientJson.BANK;
				bank_payee_cf_ad.getDatas().add(mAccountsModel);
			} else {
				existIndex = bank_payee_cf_ad.getDatas()
						.indexOf(mAccountsModel);
			}
			payeeGallery = payee_folders_cf;
			// insertRecipient(InsertRecipientJson.BANK, bankRecipient);
			break;
		// case NewPayee.TRANSFER_ENTRY:
		// // entry_payee_cf_ad.getDatas().contains(object);
		// break;
		case NewPayee.NEW_CARD_PAYEE:
			CardRecipient cardRecipient = new CardRecipient();
			cardRecipient.setCardNumber(data.getStringExtra(NewPayee.NUMBER));
			cardRecipient.setName(data.getStringExtra(NewPayee.NAME));
			tmp_payee = cardRecipient;
			if (!card_payee_cf_ad.getDatas().contains(cardRecipient)) {
				type = InsertRecipientJson.CARD;
			} else {
				existIndex = card_payee_cf_ad.getDatas().indexOf(cardRecipient);
			}
			payeeGallery = payee_folders_cf;

			tmp_InfoCardsModel = (InfoCardsModel) data
					.getSerializableExtra(NewPayee.INFOCARDSMODEL);
			tmp_isVerifyCard = data.getIntExtra(NewPayee.VERIFYCARD_STATE,
					VERIFYCARD_INITAL);
			// insertRecipient(InsertRecipientJson.CARD,cardRecipient);
			break;
		case NewPayee.NEW_SIM_PAYEE:
			PhoneRecipient phoneRecipient = new PhoneRecipient();
			phoneRecipient.setName(data.getStringExtra(NewPayee.NAME));
			phoneRecipient.setPhoneNumber(data.getStringExtra(NewPayee.NUMBER));
			phoneRecipient.setProvider(data.getStringExtra(NewPayee.OPERATOR));
			tmp_payee = phoneRecipient;
			if (!phone_payee_cf_ad.getDatas().contains(phoneRecipient)) {
				type = InsertRecipientJson.PHONE;
			} else {
				existIndex = phone_payee_cf_ad.getDatas().indexOf(
						phoneRecipient);
			}
			payeeGallery = phone_payee_folders_cf;
			// insertRecipient(InsertRecipientJson.PHONE,
			// phoneRecipient);
			if (!TextUtils.isEmpty(phoneRecipient.getName())
					&& !TextUtils.isEmpty(phoneRecipient.getPhoneNumber())) {
				amountAvailableIndex = existIndex;
				loadAmountAvilable();
			}
			break;
		}

		if (existIndex >= 0) {
			Toast.makeText(activity, R.string.payee_exist, Toast.LENGTH_SHORT)
					.show();
			if (existIndex >= 0 && payeeGallery != null) {
				payeeGallery.setSelection(existIndex, true);
			}
		} else if (needAdd) {
			downloading_pd.show();
			new PostThread(activity, type + "", tmp_payee, mHandler,
					PaymentsManager.INSERTRECIPIENT,
					PostThread.TYPE_INSERTRECIPIENT).start();
		} else if (payeeGallery != null) {
			CoverFlowImageAdapter mCoverFlowViewAdapter = (CoverFlowImageAdapter) payeeGallery
					.getAdapter();
			if (TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {
				mCoverFlowViewAdapter.setNewItem(null);
			} else {
				if (requestCode == NewPayee.NEW_TRANSFER_PAYEE_BY_PHONE) {
					((NPBankRecipientAdapter) mCoverFlowViewAdapter)
							.setPhonePayeeItem(tmp_payee_phone);
				} else {
					mCoverFlowViewAdapter.setNewItem(tmp_payee);
				}
			}
			mCoverFlowViewAdapter.notifyDataSetChanged();
			if (requestCode == NewPayee.NEW_TRANSFER_PAYEE_BY_PHONE) {
				payeeGallery.setSelection(mCoverFlowViewAdapter.getCount()-3, true);
			}
		}

		return true;
	}

	private static void setDialogWidth(final AlertDialog alertDialog,
			Context context) {
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()) * 3 / 4; // 设置宽度
		lp.height = (int) (display.getWidth()) * 2 / 4;
		alertDialog.getWindow().setAttributes(lp);
	}

	/**
	 * stop input on the amount filed,and to next step.
	 */
	private void checkAmountView() {
		if (amountbar_lin.getVisibility() != View.VISIBLE) {
			return;
		}
//		String str = amount_et.getText().toString();
//		str = Utils.formatMoney(Double.valueOf(str).doubleValue(), "", true,
//				true, false, false, true);
//		amount_et.setText(str);
//		Selection.setSelection(amount_et.getText(), str.length());
		if (getAmount() > 0) {
			switch (getPaymentType()) {
			case BANK_TRANSFER:
			case TRANSFER_ENTRY:
				View v = showPayee(View.VISIBLE);
				showDate(View.VISIBLE);
				setFoucs(v);
				break;
			case CARD_TOP_UP:
				v = showPayee(View.VISIBLE);
				showDescription(View.VISIBLE);
				setFoucs(v);
				setProceedButtonClickAble(true);
				break;
			case PHONE_TOP_UP:
				break;
			}
		}
	}

	/**
	 * to stop input on amount filed peremptory.
	 */
	private synchronized void endAountTextInput() {
		if (amount_et != null && amount_et.getWindowToken() != null) {
			if (KeyBoardUtils.hideSoftInputFromWindow(activity,
					amount_et.getWindowToken())) {
				checkAmountView();
			}
		}
	}

	public void setFoucs(View view) {
		if (view == null) {
			return;
		}
		if (view.isFocused()) {
			View p = (View) view.getParent();
			p.setFocusable(true);
			p.requestFocus();
			p.setFocusableInTouchMode(true);
		}
		// view.setFocusable(true);
		// view.requestFocus();
		// view.setFocusableInTouchMode(true);
		view.setFocusable(true);
		view.requestFocusFromTouch();
	}

	public int getShowForm() {
		return showForm;
	}

	public void setShowForm(int showForm) {
		this.showForm = showForm;
	}

}
