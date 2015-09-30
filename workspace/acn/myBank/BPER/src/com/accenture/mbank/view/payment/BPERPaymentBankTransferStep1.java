package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.oneMin.demo.slideListView.ItemSlideAdapter.ViewHolder;
import cn.oneMin.demo.slideListView.ItemSlideTouchListener;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.logic.TransferObjectTransfer;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.model.TablesResponseModel;
import com.accenture.mbank.util.AmountItalyInputFilter;
import com.accenture.mbank.util.DesLimitTextChangedListener;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.KeyBoardUtils;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.adapter.BankRecipientAdapter;
import com.custom.view.SwitchButton;
/**
 * Beneficiario(beneficiary value) 
 * Codice IBAN (iban value) 
 * Codice BIC (BIC, only if used) 
 * Codice CUP (CUP, only if used) 
 * Codice CIG(CIG. only if used) 
 * Importo (amount value) 
 * Causale (description value) 
 * Causalevalutaria (purpose currency value,only if used)
 * Data (date value)
 */ 
public class BPERPaymentBankTransferStep1 extends BPERPaymentInputPage implements OnCheckedChangeListener{
	
	public static final String RECENTPAYMENT_TAB="tab1",NEWPAYMENT_TAB="tab2";
	
	TablesResponseModel mTablesResponseModel;
	List<TableContentList> purposeCurrencyTableContentList;
	List<TransferObjectTransfer> recentTransferModel;
	
	long date;
	
	ImageButton beneficiary_ibtn;
	private TabHost tabHost;
	AutoCompleteTextView beneficiary_name_et;
	EditText iban_et;
	EditText bic_et;
	SwitchButton add_phonebook_sbtn;
	EditText cup_et,cig_et;
	TextView currency_tv;
	EditText amount_et;
	ImageView optional_tv_info;
	SwitchButton cup_e_cig_sbtn;
	EditText description_of_payment_et;
	TextView des_length_alerter_tv;
	EditText data_et;
	ImageButton data_ibtn;
	EditText purpose_currency_et;
	LinearLayout purpose_currency_liner;
	ImageButton purpose_currency_ibtn;
	Button confirmation_btn;  
	int fromUser;
	
	TableLayout recent_content;
	
	DesLimitTextChangedListener mDesLimitTextChangedListener;

	
	OnViewClickListener onClickLstener;
	public BPERPaymentBankTransferStep1(Context context,TransferType mTransferType){
		super(context,mTransferType);
	}
	
	int getViewId(){
		return R.layout.bper_payment_bank_transfer;
	}
	
	public void setAdapter(BankRecipientAdapter adapter){
		beneficiary_name_et.setDropDownHorizontalOffset(5);
		beneficiary_name_et.setThreshold(1);
		beneficiary_name_et.setAdapter(adapter);
		beneficiary_name_et.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BankRecipient item=(BankRecipient) arg0.getAdapter().getItem(arg2);
				beneficiary_name_et.setText(item.getName());
				setBeneficiaryIban(item.getIbanCode());
			}
		});
	}
	
	void init(){
		onClickLstener=new OnViewClickListener(){
			@Override
			public void onClick(View v) {
				if(NEWPAYMENT_TAB.equals(tabHost.getCurrentTabTag())&&iban_et.isFocused()&&v.getId()==R.id.root_content){
					if(!checkIban(iban_et.getText().toString().trim(),true)){
						return;
					}
				}
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
					}, calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender
					.get(Calendar.DATE)).show();
					break;
				case R.id.optional_tv_info:
					DialogManager.createMessageDialog(R.string.cup_cig_info, context).show();
					break;
				default:
				}
			}
			};
		super.init(onClickLstener);
		tabHost = (TabHost) contentView.findViewById(R.id.tabhost); 
		tabHost.setup();
		View icon=mLayoutInflater.inflate(R.layout.payment_tab_left_btn, null);
		tabHost.addTab(tabHost.newTabSpec(RECENTPAYMENT_TAB)
				.setIndicator(icon/*, getResources().getDrawable(R.drawable.mumule)*/)  
				.setContent(R.id.tab1));
		
		icon=mLayoutInflater.inflate(R.layout.payment_tab_right_btn, null);
		tabHost.addTab(tabHost.newTabSpec(NEWPAYMENT_TAB)  
				.setIndicator(icon)  
				.setContent(R.id.tab2));
//		tabHost.setCurrentTab(1);
		recent_content=(TableLayout) contentView.findViewById(R.id.recent_content);
		beneficiary_name_et=(AutoCompleteTextView) contentView.findViewById(R.id.beneficiary_name_et);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				beneficiary_name_et.setText("");
			}
		}, 3000);
		beneficiary_name_et.addTextChangedListener(confirmTextChangeListener);
		beneficiary_name_et.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				iban_et.setFocusable(true);
				iban_et.requestFocus();
				iban_et.setFocusableInTouchMode(true);
				return true;
			}
		});
		beneficiary_ibtn=(ImageButton) contentView.findViewById(R.id.phonebook_btn);
		iban_et=(EditText) contentView.findViewById(R.id.iban_et);
		iban_et.addTextChangedListener(confirmTextChangeListener);
		iban_et.setOnFocusChangeListener(new OnFocusChangeListener() {
			String lastIban;
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(!hasFocus&&NEWPAYMENT_TAB.equals(tabHost.getCurrentTabTag())&&checkIban){
					lastIban=getIban();
					if(!lastIban.equals("")){
						checkIban(lastIban, true);
					}
				}else{
					checkIban=true;
				}
			}
		});
		iban_et.addTextChangedListener(new TextWatcher() {	
			String lastSearch;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s==null||s.length()<2){
					lastSearch=null;
					return;
				}
				if(mTablesResponseModel==null){
					return;
				}
				String iban=s.toString().trim().toLowerCase();
				String searchString=iban.substring(0,2);
				if(searchString.equals(lastSearch)){
					return;
				}
				lastSearch=searchString;
				updateShowPurposeCurrency();
//				
//				if("mc".equals(searchString)){
//					searchString="pm";
//				}else if("sm".equals(searchString)){
//					searchString="rsm";
//				}
//				TableContentList mTableContentList=mTablesResponseModel.getTableWrapper(NewPaymentDataUtils.SEPA_COUNTRY_IBAN,searchString);
//				if(mTableContentList==null){
//					mTableContentList=mTablesResponseModel.getTableWrapper(NewPaymentDataUtils.SEPA_COUNTRY_IBAN,iban);
//				}
//				if(mTableContentList==null){
//					((BaseActivity)context).displayErrorMessage("",context.getString(R.string.iban_unexist_error_txt));
//					return;
//				}
//				if(mTableContentList.getDescription()==null||mTableContentList.getDescription().length()!=iban.length()){
//					((BaseActivity)context).displayErrorMessage("",context.getString(R.string.iban_length_error_txt));
//					return result;
//				}
				if(!searchString.equals("it")&&!searchString.equals("sm")){
					bic_et.setVisibility(View.VISIBLE);
				}else {
					bic_et.setVisibility(View.GONE);
				}
				return;
				
			}
		});
		
		bic_et=(EditText) contentView.findViewById(R.id.bic_et);
		bic_et.addTextChangedListener(confirmTextChangeListener);
		add_phonebook_sbtn=(SwitchButton) contentView.findViewById(R.id.add_phonebook_sbtn);
		add_phonebook_sbtn.setOnCheckedChangeListener(this);
		cup_et=(EditText) contentView.findViewById(R.id.cup_et);
		cup_et.addTextChangedListener(confirmTextChangeListener);
		cig_et=(EditText) contentView.findViewById(R.id.cig_et);
		currency_tv=(TextView) contentView.findViewById(R.id.currency_tv);
		currency_tv.setText(NewPaymentDataUtils.getCurrency());
		amount_et=(EditText) contentView.findViewById(R.id.amount_et);
		amount_et.addTextChangedListener(confirmTextChangeListener);
		new AmountItalyInputFilter(amount_et,new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			
			@Override
			public void afterTextChanged(Editable s) {
				updateShowPurposeCurrency();
			}
		});
		optional_tv_info = (ImageView) contentView.findViewById(R.id.optional_tv_info);
		if (optional_tv_info != null) {
			optional_tv_info.setOnClickListener(onClickLstener);
		}
		cup_e_cig_sbtn=(SwitchButton) contentView.findViewById(R.id.cup_e_cig_sbtn);
		cup_e_cig_sbtn.setOnCheckedChangeListener(this);
		description_of_payment_et=(EditText) contentView.findViewById(R.id.description_of_payment_et);
		description_of_payment_et.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				KeyBoardUtils.hideSoftInputFromWindow(context, description_of_payment_et.getWindowToken());
				return false;
			}
		});
		description_of_payment_et.addTextChangedListener(confirmTextChangeListener);
		
		description_of_payment_et.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				LogManager.d("setOnKeyListener: " + keyCode + " -- KeyEvent: " + event.getAction() + "-" + event.getKeyCode());
				
				return false;
			}
		});
		
		des_length_alerter_tv=(TextView) contentView.findViewById(R.id.des_length_alerter_tv);
		mDesLimitTextChangedListener=new DesLimitTextChangedListener(context, description_of_payment_et, des_length_alerter_tv);
		mDesLimitTextChangedListener.setCharLimit(true);
		data_et=(EditText) contentView.findViewById(R.id.data_et);
		data_ibtn=(ImageButton) contentView.findViewById(R.id.data_ibtn);
		data_ibtn.setOnClickListener(onClickLstener);
		data_et.setOnClickListener(onClickLstener);
		purpose_currency_liner=(LinearLayout) contentView.findViewById(R.id.purpose_currency_liner);
		purpose_currency_et=(EditText) contentView.findViewById(R.id.purpose_currency_et);
		purpose_currency_et.addTextChangedListener(confirmTextChangeListener);
		purpose_currency_ibtn=(ImageButton) contentView.findViewById(R.id.purpose_currency_ibtn);
		
		confirmation_btn = (Button) contentView .findViewById(R.id.confirmation_btn);
		confirmation_btn.setOnClickListener(mOnConfirmClickListener);
		confirmation_btn.setOnTouchListener(mOnConfirmTouchListener);
		mDesLimitTextChangedListener.setDestriptionMaxLenth(mDesLimitTextChangedListener.DES_LENGTH_OFF);
	}
	
	public void setOnPurposeCurrencyClickListener(OnViewClickListener l){
		purpose_currency_ibtn.setOnClickListener(l);
		purpose_currency_et.setOnClickListener(l);
	}
	

	ItemSlideTouchListener mItemSlideTouchListener=new ItemSlideTouchListener();
	public TransferObjectTransfer getRecentTransferModel(int position){
		if(recentTransferModel!=null&&position>=0&&position<recentTransferModel.size()){
			return recentTransferModel.get(position);
		}
		return null;
	}
	
	public void setRecentTransferModels(List<TransferObjectTransfer> recentTransferModel,ItemSlideTouchListener.OnSlideItemClickListener monSlideItemClickListener){
		mItemSlideTouchListener.setOnSlideItemClickListener(monSlideItemClickListener);
		this.recentTransferModel=recentTransferModel;
		recent_content.removeAllViews();
		{
			View rowItem=(View) mLayoutInflater.inflate(R.layout.bper_recent_slidelist_items, null);
			TextView textView1=(TextView) rowItem.findViewById(R.id.textView1);
			textView1.setText(R.string.data);
			TextView textView2=(TextView) rowItem.findViewById(R.id.textView2);
			textView2.setText(R.string.beneficiary_tilte);
			TextView textView3=(TextView) rowItem.findViewById(R.id.textView4);
			textView3.setText(R.string.amount_h);
			ImageView imageView=(ImageView) rowItem.findViewById(R.id.imageView1);
			imageView.setVisibility(View.INVISIBLE);
			recent_content.addView(rowItem);
			ImageView divider=new ImageView(context);
			divider.setImageResource(R.drawable.upper_shading);
			recent_content.addView(divider);
		}
		if(recentTransferModel==null||recentTransferModel.size()<=0){
			return;
		}
		String currency=contentView.getContext().getResources().getString(R.string.eur);
		int size=recentTransferModel.size();
		for(int i=0;i<size;i++){
			TransferObjectTransfer accountsModel=recentTransferModel.get(i);
			View rowItem=(View) mLayoutInflater.inflate(R.layout.bper_recent_slidelist_items, null);
			TextView textView1=(TextView) rowItem.findViewById(R.id.textView1);
			String operationDate=TimeUtil.getDateString(accountsModel.getDate(),TimeUtil.dateFormat5);
			textView1.setText(operationDate);
			TextView textView2=(TextView) rowItem.findViewById(R.id.textView2);
			textView2.setText(accountsModel.getBeneficiaryName());
			TextView textView3=(TextView) rowItem.findViewById(R.id.textView4);
			textView3.setText(Utils.notPlusGenerateFormatMoney(currency,accountsModel.getAmount()));
			recent_content.addView(rowItem);
			if(i<size-1){
				recent_content.addView(mLayoutInflater.inflate(R.layout.separation_line_divider, null));
			}
//			rowItem.setTag(accountsModel);
//			rowItem.setOnClickListener(recentTransferItemClick);
			ViewHolder mViewHolder=new ViewHolder();
			mViewHolder.init(rowItem);
			mViewHolder.position=i;
			rowItem.setTag(mViewHolder);
			rowItem.setOnTouchListener(mItemSlideTouchListener);
		}
	}
	
	/**
	 * The IBAN value inserted must be checked by the app. The app must invoke
	 * the getTable interface using the "tableNameList": ["SEPA_COUNTRY_IBAN�? ].
	 * The app must check the first two chars of the iban string: If the first 2
	 * chars of the IBAN string are not in the table SEPA_COUNTRY_IBAN, then the
	 * bank transfer can’t proceed and the app must show the error: “Attenzione!
	 * Il bonifico non è consentito verso la nazione indicata nell’IBAN�?. Stop
	 * the checks and the focus remains on the current page.
	 * 
	 * @param iban
	 * @return
	 */
	boolean checkIban(String iban,boolean showError){
		boolean result=false;
		int msgId=-1;
		if(mTablesResponseModel==null){
			msgId=R.string.iban_unexist_error_txt;
		}else
		if(iban==null||iban.length()<2){
			msgId=R.string.iban_unexist_error_txt;
		}
		if(msgId>0){
			if(showError){
				DialogManager.createMessageDialog(msgId, null, context, new OnViewClickListener(){
					public void onClick(View v) {
//						iban_et.setFocusable(true);
//						iban_et.requestFocus();
//						iban_et.setFocusableInTouchMode(true);
					};
	
				}, null).show();
			}
			return result;
		}
		String searchString=iban.substring(0, 2).toLowerCase();
		if("mc".equals(searchString)){
			searchString="pm";
		}else if("sm".equals(searchString)){
			searchString="rsm";
		}
		TableContentList mTableContentList=mTablesResponseModel.getTableWrapper(NewPaymentDataUtils.SEPA_COUNTRY_IBAN,searchString);
		if(mTableContentList==null){
			mTableContentList=mTablesResponseModel.getTableWrapper(NewPaymentDataUtils.SEPA_COUNTRY_IBAN,iban);
		}
		if(mTableContentList==null){
			if(showError){
				DialogManager.createMessageDialog(R.string.iban_unexist_error_txt, null, context, new OnViewClickListener(){
					public void onClick(View v) {
//						iban_et.setFocusable(true);
//						iban_et.requestFocus();
//						iban_et.setFocusableInTouchMode(true);
					};
	
				}, null).show();
			}
			return result;
		}
		if(!String.valueOf(iban.length()).equals(mTableContentList.getDescription())){
			if(showError){
				DialogManager.createMessageDialog(R.string.iban_length_error_txt, null, context, new OnViewClickListener(){
					public void onClick(View v) {
//						iban_et.setFocusable(true);
//						iban_et.requestFocus();
//						iban_et.setFocusableInTouchMode(true);
					};
	
				}, null).show();
			}
			return result;
		}
		if(showError&&iban.length()>=10){
			String abi=iban.substring(5, 10);
			if("05550".equals(abi)){
				DialogManager.createMessageDialog(R.string.abi_msg_05550, null, context, new OnViewClickListener(){
					public void onClick(View v) {
						iban_et.setFocusable(true);
						iban_et.requestFocus();
						iban_et.setFocusableInTouchMode(true);
					};
	
				}, null).show();
			}else if("06040".equals(abi)){
				DialogManager.createMessageDialog(R.string.abi_msg_06040, null, context, new OnViewClickListener(){
					public void onClick(View v) {
						iban_et.setFocusable(true);
						iban_et.requestFocus();
						iban_et.setFocusableInTouchMode(true);
					};
	
				}, null).show();
			}else if("05414".equals(abi)){
				DialogManager.createMessageDialog(R.string.abi_msg_05414, null, context, new OnViewClickListener(){
					public void onClick(View v) {
						iban_et.setFocusable(true);
						iban_et.requestFocus();
						iban_et.setFocusableInTouchMode(true);
					};
	
				}, null).show();
			}
		}
		if(!searchString.equals("it")&&!searchString.equals("sm")){
			bic_et.setVisibility(View.VISIBLE);
		}else {
			bic_et.setVisibility(View.GONE);
		}
		return true;
	}
	
	/***
	 * If the IBAN starts with “IS�?, “CH�?, “NO�?, “LI�? or “MC�? and the amount is
	 * greater than 12.500
	 * 
	 * IF the first 2 chars of the IBAN are not in the precedent list and are
	 * different from “IT�? and “SM�?
	 * 
	 * and the amount is greater than 50.000.
	 * 
	 * @return
	 */
	private void updateShowPurposeCurrency(){
		String iban=getIban();
		double amount=getAmount();
		if(iban==null||iban.length()<2||amount<=12.5){
			showPurposeCurrency(false);
			return;
		}
		String chars2=iban.substring(0, 2).toLowerCase();
		if(amount>50&&(!"it".equals(chars2)&&!"sm".equals(chars2))){
			showPurposeCurrency(true);
		}else if(amount>12.5&&("is".equals(chars2)||"ch".equals(chars2)||"no".equals(chars2)||"li".equals(chars2)||"mc".equals(chars2))){
			showPurposeCurrency(true);
		}else{
			showPurposeCurrency(false);
		}
	}
	
	private void showPurposeCurrency(boolean visible){
		if(visible){
			purpose_currency_liner.setVisibility(View.VISIBLE);
		}else{
			purpose_currency_liner.setVisibility(View.GONE);
		}
	}
	
	boolean onCheckForConfirm(boolean showError){
		boolean result=false;
		int msgId=0;
		if(TextUtils.isEmpty(beneficiary_name_et.getText())){
			result=true;
			msgId=R.string.beneficiary_name_empty_error;
		}
		else if(!BaseActivity.isOffline&&!checkIban(iban_et.getText().toString().trim(),false)){
			result=true;
			msgId=R.string.iban_empty_error;
		}
		else if(TextUtils.isEmpty(amount_et.getText())){
			result=true;
			msgId=R.string.amount_empty_error;
		}
		else if(TextUtils.isEmpty(description_of_payment_et.getText())){
			result=true;
			msgId=R.string.description_empty_error;
		}
		else if(purpose_currency_liner.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(purpose_currency_et.getText())){
			result=true;
			msgId=R.string.purpose_currency_empty_error;
		}
		else if((cup_et.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(cup_et.getText()))
				||(cig_et.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(cig_et.getText()))){
			result=true;
			msgId=R.string.cup_e_cig_empty_error;
		}
		else if(bic_et.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(bic_et.getText())){
			result=true;
			msgId=R.string.bic_empty_error;
		}
		if(msgId>0&&showError){
			DialogManager.createMessageDialog(msgId, context).show();
		}
		this.confirmation_btn.setEnabled(!result);
		return result;
	}
	
	public void setDate(long date){
		this.date=date;
		data_et.setText(TimeUtil.getDateString(date,TimeUtil.dateFormat5));
	}
	
	public void showPurposeCurrencySelectDialog(){

		new AlertDialog.Builder(context)
		.setIcon(
				android.R.drawable.ic_dialog_info).setAdapter(new ListAdapter() {
					
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
						
						TextView tv;
						if(convertView!=null){
							tv=(TextView) convertView;
						}else{
							tv=(TextView) mLayoutInflater.inflate(R.layout.purpose_item, null);// new TextView(context);
						}
						String tableNameString=purposeCurrencyTableContentList.get(position).getDescription();
						tv.setText(tableNameString);
						return tv;
					}
					
					@Override
					public int getItemViewType(int position) {
						// TODO Auto-generated method stub
						return position;
					}
					
					@Override
					public long getItemId(int position) {
						return position;
					}
					
					@Override
					public Object getItem(int position) {
						return purposeCurrencyTableContentList.get(position);
					}
					
					@Override
					public int getCount() {
						return purposeCurrencyTableContentList==null?0:purposeCurrencyTableContentList.size();
					}
					
					@Override
					public boolean isEnabled(int position) {
						// TODO Auto-generated method stub
						return true;
					}
					
					@Override
					public boolean areAllItemsEnabled() {
						// TODO Auto-generated method stub
						return false;
					}
				},  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mTableContentList=purposeCurrencyTableContentList.get(which);
						purpose_currency_et.setText(mTableContentList.getDescription());
						dialog.dismiss();
					}
				})
				.show();
	}
	
	TableContentList mTableContentList;
	public String getPurposeCurrencyCode(){
		if(mTableContentList!=null){
			return mTableContentList.getCode();
		}
		return null;
	}
	
	public String getPurposeCurrencyName(){
		if(mTableContentList!=null){
			return mTableContentList.getCode();
		}
		return null;
	}
	
	boolean checkIban;
	
	public void setCheckIban(boolean checkIban){
		this.checkIban=checkIban;
	}
	public boolean onBackPressed() {
		checkIban=false;
		return false;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.add_phonebook_sbtn:
			break;
		case R.id.cup_e_cig_sbtn:
			if(isChecked){
				mDesLimitTextChangedListener.setDestriptionMaxLenth(mDesLimitTextChangedListener.DES_LENGTH_ON);
				cup_et.setVisibility(View.VISIBLE);
				cig_et.setVisibility(View.VISIBLE);
			}else{
				mDesLimitTextChangedListener.setDestriptionMaxLenth(mDesLimitTextChangedListener.DES_LENGTH_OFF);
				cup_et.setVisibility(View.GONE);
				cig_et.setVisibility(View.GONE);
			}
			onCheckForConfirm(false);
			break;
			
		default:
			break;
		}
	}
	
	public void setOnTabChangeListener(OnTabChangeListener l){
		tabHost.setOnTabChangedListener(l);
	}
	
	public void setOnBeneficiaryClickListener(OnViewClickListener l){
		beneficiary_ibtn.setOnClickListener(l);
	}
	
	public void setSepaCountryIBAN(TablesResponseModel mTablesResponseModel){
		this.mTablesResponseModel=mTablesResponseModel;
	}
	
	public void setPurposeCurrency(List<TableContentList> purposeCurrencyTableContentList){
		this.purposeCurrencyTableContentList=purposeCurrencyTableContentList;
	}
	
	public List<TableContentList> getPurposeCurrencyTablesResponseModel(){
		return purposeCurrencyTableContentList;
	}
	
	public String getBeneficiary() {
		return beneficiary_name_et.getText().toString();
	}
	
	public String getIban(){
		return iban_et.getText().toString().trim();
	}
	
	public double getAmount(){
		return NewPaymentDataUtils.parseDouble(amount_et.getText().toString().trim());
	}
	
	public String getBIC() {
		return bic_et.getText().toString();
	}
	
	public String getCUP() {
		return cup_et.getText().toString();
	}
	
	public String getCIG() {
		return cig_et.getText().toString();
	}
	
	public String getDescription() {
		return description_of_payment_et.getText().toString();
	}
	
	public long getDate() {
		return date;
	}
	
	public void setNewPayment(boolean newPayment) {
		if(newPayment){
			tabHost.setCurrentTab(1);
		}else{
			tabHost.setCurrentTab(0);
		}
	}
	
	public void setBeneficiaryName(String name){
		beneficiary_name_et.setText(name);
		beneficiary_name_et.dismissDropDown();
	}
	
	public void setBeneficiaryIban(String str){
		iban_et.setText(str);
		checkIban(str,true);
	}

	@Override
	void disableConfirmButn() {
		// TODO Auto-generated method stub
		
	}
}
