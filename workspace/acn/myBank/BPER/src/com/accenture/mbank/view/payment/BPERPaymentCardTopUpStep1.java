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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
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
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.oneMin.demo.slideListView.ItemSlideAdapter.ViewHolder;
import cn.oneMin.demo.slideListView.ItemSlideTouchListener;
import cn.oneMin.demo.slideListView.ItemSlideTouchListener.OnSlideItemClickListener;

import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectCard;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.model.TablesResponseModel;
import com.accenture.mbank.util.AmountItalyInputFilter;
import com.accenture.mbank.util.DesLimitTextChangedListener;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.KeyBoardUtils;
import com.accenture.mbank.util.NewPaymentDataUtils;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.adapter.CardRecipientAdapter;
import com.custom.view.SwitchButton;

public class BPERPaymentCardTopUpStep1 extends BPERPaymentInputPage implements OnCheckedChangeListener {

	public static final String RECENTPAYMENT_TAB="tab1",NEWPAYMENT_TAB="tab2";
    static final int DES_LENGTH_ON=105,DES_LENGTH_OFF=140;
    int description_of_payment_etriptionLenth;
    
	TablesResponseModel mTablesResponseModel;
	TablesResponseModel purposeCurrencyTablesResponseModel;
	List<TransferObjectCard> recentTransferModel;
	List<AccountsModel> iBanCardaccounts;
    
	long date;
	
	private TabHost tabHost;
	SwitchButton user_my_card_sbtn;
	TableLayout beneficiary_rg;
	View input_benificiary_content;
	AutoCompleteTextView beneficiary_name_et;
	EditText iban_et;
	ImageButton beneficiary_ibtn;
	TextView currency_tv;
	EditText amount_et;
	EditText description_of_payment_et;
	TextView des_length_alerter_tv;
	EditText purpose_currency_et;
	LinearLayout purpose_currency_liner;
	ImageButton purpose_currency_ibtn;
	SwitchButton add_phonebook_sbtn;
	Button confirmation_btn;
	
	TableLayout recent_content;
	
	DesLimitTextChangedListener mDesLimitTextChangedListener;

	OnViewClickListener onClickListener;
	public BPERPaymentCardTopUpStep1(Context context,TransferType mTransferType){
		super(context,mTransferType);
	}
	
	int getViewId(){
		return R.layout.bper_payment_card_top_up;
	}
	
	public ViewGroup getContentView(){
		if(contentView==null){
			contentView=super.getContentView();
			contentView.setOnClickListener(onClickListener);
		}
		return contentView;
	}
	
	public void setAdapter(CardRecipientAdapter adapter){
		beneficiary_name_et.setDropDownHorizontalOffset(5);
		beneficiary_name_et.setThreshold(1);
		beneficiary_name_et.setAdapter(adapter);
		beneficiary_name_et.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CardRecipient item=(CardRecipient) arg0.getAdapter().getItem(arg2);
				beneficiary_name_et.setText(item.getName());
				iban_et.setText(item.getCardNumber());
			}
		});
	}
	
	void init(){
		onClickListener=new OnViewClickListener(){
		@Override
		public void onClick(View v) {
			super.onClick(v);
			switch (v.getId()) {
			case R.id.purpose_currency_ibtn:
				AlertDialog dialog=new AlertDialog.Builder(context)
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
									tv= new TextView(context);
								}
								String tableNameString=purposeCurrencyTablesResponseModel.getTablewrapperList().get(position).getTableName();
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
								// TODO Auto-generated method stub
								return position;
							}
							
							@Override
							public Object getItem(int position) {
								// TODO Auto-generated method stub
								return purposeCurrencyTablesResponseModel.getTablewrapperList().get(position);
							}
							
							@Override
							public int getCount() {
								// TODO Auto-generated method stub
								return purposeCurrencyTablesResponseModel.getTablewrapperList().size();
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
								purpose_currency_et.setText((String) ((AlertDialog)dialog).getListView().getAdapter().getItem(which));
								dialog.dismiss();
							}
					    })
					    .show();
				break;
			case R.id.confirmation_btn:
				onConfirm();
				break;
			case R.id.birth_data_et:
			case R.id.data_ibtn:
				final Calendar calender = Calendar.getInstance();
				new DatePickerDialog(context, new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						calender.set(year, monthOfYear, dayOfMonth);
						setDate(calender.getTimeInMillis());
					}
				}, calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender
				.get(Calendar.DATE)).show();
				break;
			default:
				break;
			}
		}
		};
		super.init();
		 tabHost = (TabHost) contentView.findViewById(R.id.tabhost); 
		 tabHost.setup();
		 Button icon=(Button) mLayoutInflater.inflate(R.layout.payment_tab_left_btn, null);
		 icon.setText(R.string.phone_top_up_recent_payment_tab_name);
		 tabHost.addTab(tabHost.newTabSpec(RECENTPAYMENT_TAB)
				 .setIndicator(icon/*, getResources().getDrawable(R.drawable.mumule)*/)  
				 .setContent(R.id.tab1));

		 icon=(Button) mLayoutInflater.inflate(R.layout.payment_tab_right_btn, null);
		 icon.setText(R.string.phone_top_up_new_payment_tab_name);
		 tabHost.addTab(tabHost.newTabSpec(NEWPAYMENT_TAB) .setIndicator(icon) .setContent(R.id.tab2));
		 tabHost.setCurrentTab(1);
		 recent_content=(TableLayout) contentView.findViewById(R.id.recent_content);
		 user_my_card_sbtn=(SwitchButton) contentView.findViewById(R.id.user_my_card_sbtn);
		 user_my_card_sbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				showInputBeneficiary(!isChecked);
				onCheckForConfirm(false);
			}
		});
		 beneficiary_rg=(TableLayout) contentView.findViewById(R.id.beneficiary_rg);
		 input_benificiary_content=contentView.findViewById(R.id.input_benificiary_content);
		 beneficiary_name_et=(AutoCompleteTextView) contentView.findViewById(R.id.name_et);
		 beneficiary_name_et.postDelayed(new Runnable() {
			
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
		 iban_et=(EditText) contentView.findViewById(R.id.phone_et);
		 iban_et.postDelayed(new Runnable() {
			 
			 @Override
			 public void run() {
				 // TODO Auto-generated method stub
				 iban_et.setText("");
			 }
		 }, 3000);
		 iban_et.addTextChangedListener(confirmTextChangeListener);
		 add_phonebook_sbtn=(SwitchButton) contentView.findViewById(R.id.add_phonebook_sbtn);
		 add_phonebook_sbtn.setOnCheckedChangeListener(this);
		 currency_tv=(TextView) contentView.findViewById(R.id.currency_tv);
		 currency_tv.setText(NewPaymentDataUtils.getCurrency());
		 amount_et=(EditText) contentView.findViewById(R.id.amount_et);
		 amount_et.addTextChangedListener(confirmTextChangeListener);
		 new AmountItalyInputFilter(amount_et,null);
		 description_of_payment_et=(EditText) contentView.findViewById(R.id.description_of_payment_et);
		 description_of_payment_et.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				KeyBoardUtils.hideSoftInputFromWindow(context, description_of_payment_et.getWindowToken());
				return false;
			}
		});
		 description_of_payment_et.addTextChangedListener(confirmTextChangeListener);
		 des_length_alerter_tv=(TextView) contentView.findViewById(R.id.des_length_alerter_tv);
		 mDesLimitTextChangedListener=new DesLimitTextChangedListener(context,description_of_payment_et,des_length_alerter_tv);
		 purpose_currency_liner=(LinearLayout) contentView.findViewById(R.id.purpose_currency_liner);
		 purpose_currency_et=(EditText) contentView.findViewById(R.id.purpose_currency_et);
		 purpose_currency_et.addTextChangedListener(confirmTextChangeListener);
		 purpose_currency_ibtn=(ImageButton) contentView.findViewById(R.id.purpose_currency_ibtn);
		 purpose_currency_ibtn.setOnClickListener(onClickListener);
		 
		 birth_date_container = (LinearLayout) contentView.findViewById(R.id.birth_date_container);
		 birth_data_et = (EditText) birth_date_container.findViewById(R.id.birth_data_et);
		 data_ibtn = (ImageButton) birth_date_container.findViewById(R.id.data_ibtn);
		 birth_data_et.setOnClickListener(onClickListener);
		 data_ibtn.setOnClickListener(onClickListener);
		 
		 confirmation_btn = (Button) contentView .findViewById(R.id.confirmation_btn);
		 confirmation_btn.setOnClickListener(mOnConfirmClickListener);
		 confirmation_btn.setOnTouchListener(mOnConfirmTouchListener);
		 mDesLimitTextChangedListener.setDestriptionMaxLenth(DES_LENGTH_OFF);
	}
	
	public LinearLayout birth_date_container;
	private EditText birth_data_et;
	private ImageButton data_ibtn;
	
	public void setDate(long date){
		this.date=date;
		birth_data_et.setText(TimeUtil.getDateString(date,TimeUtil.dateFormat5));
	}
	
	public void showInputBeneficiary(boolean input){
		if(input){
		beneficiary_rg.setVisibility(View.GONE);
		input_benificiary_content.setVisibility(View.VISIBLE);
		}else{
			beneficiary_rg.setVisibility(View.VISIBLE);
			input_benificiary_content.setVisibility(View.GONE);
		}
	}

	IbanCardManager mIbanCardManager=new IbanCardManager();
	public void setAccounts(List<AccountsModel> accounts){
		mIbanCardManager.setAccounts(accounts);
	}
	public void onDisable(int accountPos){
		mIbanCardManager.onDisable(accountPos);
	}
	
	class IbanCardManager{
		public int selectedPosition=-1;
		public int disAblePosition=-1;

		public void setAccounts(List<AccountsModel> accounts){
			iBanCardaccounts=accounts;
			user_my_card_sbtn.setEnabled(accounts!=null&&accounts.size()>0);
			selectedPosition=-1;
			disAblePosition=-1;
			AccountsModel payer=getAccountsModel();
			if(accounts!=null){
				int length=accounts.size();
				for(int i=0;i<length;i++){
					Holder mHolder;
					if(i+1<beneficiary_rg.getChildCount()-1){
						mHolder=(Holder) beneficiary_rg.getChildAt(i+1).getTag();
					}else{
						View itemView=mLayoutInflater.inflate(R.layout.bper_payment_entry_beneficiary_item, null);
						itemView.setOnClickListener(mItemOnClickListener);
						beneficiary_rg.addView(itemView);
						mHolder=new Holder(itemView);
					}
					AccountsModel mAccountsModel=accounts.get(i);
					if(payer!=null&&payer.getAccountCode().equals(mAccountsModel.getAccountCode())){
						disAblePosition=i;
					}
					mHolder.setData(i, mAccountsModel);
					if(i==length-1){
						mHolder.divider.setVisibility(View.GONE);
					}else{
						mHolder.divider.setVisibility(View.VISIBLE);
					}
				}
				for(int i=beneficiary_rg.getChildCount()-1;i>length;i--){
					beneficiary_rg.getChildAt(i).setVisibility(View.GONE);
				}
			}
			mPaymentStepViewUtils.view.setFocusable(true);
			mPaymentStepViewUtils.view.requestFocusFromTouch();
		}
		
		public void onSelected(View v){
			Holder mHolder=(Holder) v.getTag();
			if(mHolder.position==selectedPosition){
				return ;
			}
			if(selectedPosition>=0){
			Holder selectedHolder=(Holder) beneficiary_rg.getChildAt(selectedPosition).getTag();
			selectedHolder.rbtn.setChecked(false);
			}
			selectedPosition=mHolder.position;
			onCheckForConfirm(false);
			mHolder.rbtn.setChecked(true);
		}
		
		public void onDisable(int accountPos){
			if(beneficiary_rg.getVisibility()!=View.VISIBLE){
				return;
			}
			AccountsModel mAccountsModel=getAccountsModel();
			String accountCode=null;
			if(mAccountsModel!=null){
				accountCode=mAccountsModel.getAccountCode();
			}
			accountPos=-1;
			if(accountCode!=null&&iBanCardaccounts!=null){
				int size=iBanCardaccounts.size();
				for(int i=0;i<size;i++){
					if(accountCode.equals(iBanCardaccounts.get(i).getAccountCode())){
						accountPos=i;
						break;
					}
				}
			}
			if(disAblePosition==accountPos){
				return ;
			}
			if(disAblePosition>=0){
				Holder mHolder=(Holder) beneficiary_rg.getChildAt(disAblePosition).getTag();
				mHolder.onEnable(true);
			}
			disAblePosition=accountPos;
			if(disAblePosition>=0){
				Holder mHolder=(Holder) beneficiary_rg.getChildAt(disAblePosition).getTag();
				mHolder.onEnable(false);
			}
		}
		
		public Object getCurrentItem(){
			if(iBanCardaccounts!=null&&selectedPosition>=0&&selectedPosition<iBanCardaccounts.size()){
				return iBanCardaccounts.get(selectedPosition);
			}
			return null;
		}
		
		class Holder {
			
			View itemView;
			int position;
			AccountsModel mAccountsModel;
			RadioButton rbtn;
			TextView name_tv;
			TextView iban;
			TextView avilable_banlence_tv;
			View divider;
			View isPreferredStar;
			public Holder(View itemView){
				this.itemView=itemView;
				itemView.setTag(this);
				rbtn=(RadioButton) itemView.findViewById(R.id.rbtn);
				name_tv=(TextView) itemView.findViewById(R.id.name_tv);
				iban=(TextView) itemView.findViewById(R.id.iban);
				avilable_banlence_tv=(TextView) itemView.findViewById(R.id.avilable_banlence_tv);
				divider=itemView.findViewById(R.id.divider);
				isPreferredStar=itemView.findViewById(R.id.isPreferredStar);
			}
			public void setData(int position,AccountsModel mAccountsModel){
				this.position=position;
				this.mAccountsModel=mAccountsModel;
				if(mAccountsModel.getAccountAlias()!=null){
					name_tv.setText(mAccountsModel.getAccountAlias());
				}else{
					name_tv.setText("");
				}
				
				if(mAccountsModel.getCardHolder()!=null){
					iban.setText(mAccountsModel.getCardHolder());
				}else{
					iban.setText("");
				}
				
				String cardNumber=mAccountsModel.getCardNumber();
				if(cardNumber==null){
					cardNumber="";
				}
				avilable_banlence_tv.setText(cardNumber);
				if(mAccountsModel.getIsPreferred()){
					isPreferredStar.setVisibility(View.VISIBLE);
				}else{
					isPreferredStar.setVisibility(View.GONE);
				}
				
				if(disAblePosition==position){
					onEnable(false);
				}else{
					rbtn.setChecked(selectedPosition==position);
					name_tv.setTextColor(name_tv.getResources().getColor(R.color.black));
					itemView.setEnabled(true);
					rbtn.setEnabled(true);
				}
			}
			
			public void onEnable(boolean enable){
				Holder mHolder=(Holder) beneficiary_rg.getChildAt(disAblePosition).getTag();
				if(enable){
					name_tv.setTextColor(mHolder.name_tv.getResources().getColor(R.color.black));
					iban.setTextColor(iban.getResources().getColor(R.color.gray));
					avilable_banlence_tv.setTextColor(mHolder.name_tv.getResources().getColor(R.color.black));
				}else{
					name_tv.setTextColor(mHolder.name_tv.getResources().getColor(R.color.light_gray));
					avilable_banlence_tv.setTextColor(mHolder.name_tv.getResources().getColor(R.color.light_gray));
					iban.setTextColor(iban.getResources().getColor(R.color.light_gray));
					rbtn.setChecked(false);
					if(selectedPosition==position){
						selectedPosition=-1;
					}
				}
				mHolder.itemView.setEnabled(enable);
				mHolder.rbtn.setEnabled(enable);
			}
			
		}
		
		OnViewClickListener mItemOnClickListener=new OnViewClickListener() {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
//				Toast.makeText(context, "selected", 0).show();
				onSelected(v);
			}
		};
	}
	
	ItemSlideTouchListener mItemSlideTouchListener=new ItemSlideTouchListener();
	public TransferObject getRecentTransferModel(int position){
		if(recentTransferModel!=null&&position>=0&&position<recentTransferModel.size()){
			return recentTransferModel.get(position);
		}
		return null;
	}
	
	public void setRecentTransferModels(List<TransferObjectCard> recentTransferModel,OnSlideItemClickListener mOnSlideItemClickListener){
		mItemSlideTouchListener.setOnSlideItemClickListener(mOnSlideItemClickListener);
		this.recentTransferModel=recentTransferModel;
		recent_content.removeAllViews();
		{
			View rowItem=(View) mLayoutInflater.inflate(R.layout.bper_recent_slidelist_items, null);
		TextView textView1=(TextView) rowItem.findViewById(R.id.textView1);
		textView1.setText(R.string.data);
		TextView textView2=(TextView) rowItem.findViewById(R.id.textView2);
		textView2.setText(R.string.beneficiary_tilte);
		TextView textView4=(TextView) rowItem.findViewById(R.id.textView4);
		textView4.setText(R.string.amount_h);
		ImageView imageView=(ImageView) rowItem.findViewById(R.id.imageView1);
		imageView.setVisibility(View.INVISIBLE);
		recent_content.addView(rowItem);
		ImageView divider=new ImageView(context);
		divider.setImageResource(R.drawable.upper_shading);
		recent_content.addView(divider);
		}
		if(recentTransferModel==null||recentTransferModel.size()<=0){
			return;
		}String currency=contentView.getContext().getResources().getString(R.string.eur);
		int size=recentTransferModel.size();
		for(int i=0;i<size;i++){
			TransferObjectCard accountsModel=recentTransferModel.get(i);
			View rowItem=(View) mLayoutInflater.inflate(R.layout.bper_recent_slidelist_items, null);
			TextView textView1=(TextView) rowItem.findViewById(R.id.textView1);
			String operationDate=TimeUtil.getDateString(accountsModel.getDate(),TimeUtil.dateFormat5);
			textView1.setText(operationDate);
			TextView textView2=(TextView) rowItem.findViewById(R.id.textView2);
			String beneficiary=accountsModel.getBeneficiaryName();
			if(beneficiary==null){
				beneficiary="";
			}
			textView2.setText(beneficiary);
			TextView textView4=(TextView) rowItem.findViewById(R.id.textView4);
			textView4.setText(Utils.notPlusGenerateFormatMoney(currency,accountsModel.getAmount()));
			recent_content.addView(rowItem);
			if(i<size-1){
				recent_content.addView(mLayoutInflater.inflate(R.layout.separation_line_divider, null));
			}
			ViewHolder mViewHolder=new ViewHolder();
			mViewHolder.init(rowItem);
			mViewHolder.position=i;
			rowItem.setTag(mViewHolder);
			rowItem.setOnTouchListener(mItemSlideTouchListener);
		}
	}
	
	/***
	 * If the IBAN starts with “IS”, “CH”, “NO”, “LI” or “MC” and the amount is
	 * greater than 12.500
	 * 
	 * IF the first 2 chars of the IBAN are not in the precedent list and are
	 * different from “IT” and “SM”
	 * 
	 * and the amount is greater than 50.000.
	 * 
	 * @return
	 */
	private void updateShowPurposeCurrency(){
		String iban=getCardNumber();
		if(iban==null||iban.length()<2){
			return ;
		}
		double amoun=getAmount();
		String chars2=iban.substring(0, 2).toLowerCase();
		if("is".equals(chars2)||"ch".equals(chars2)||"no".equals(chars2)||"li".equals(chars2)||"mc".equals(chars2)){
			if(amoun>12.5){
				showPurposeCurrency(true);
			}
		}else if(amoun>50&&(!"it".equals(chars2)&&!"sm".equals(chars2))){
			TableContentList mTableContentList=mTablesResponseModel.getTableWrapper(NewPaymentDataUtils.SEPA_COUNTRY_IBAN,chars2);
				if(mTableContentList==null){
					showPurposeCurrency(true);
				}
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
		if(beneficiary_rg.getVisibility()==View.VISIBLE&&mIbanCardManager.selectedPosition<0){
			result=true;
		}else
		if(input_benificiary_content.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(beneficiary_name_et.getText())){
			result=true;
			msgId=R.string.beneficiary_name_empty_error;
		}
		else if(input_benificiary_content.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(iban_et.getText())){
			result=true;
			msgId=R.string.card_number_empty;
		}
		else if(TextUtils.isEmpty(amount_et.getText())){
			result=true;
			msgId=R.string.amount_empty_error;
		}
//		else if(TextUtils.isEmpty(description_of_payment_et.getText())){
//			result=true;
//			msgId=R.string.description_empty_error;
//		}
		else if(purpose_currency_liner.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(purpose_currency_et.getText())){
			result=true;
			msgId=R.string.purpose_currency_empty_error;
		}
//		else if(bic_et.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(bic_et.getText())){
//			result=true;
//			msgId=R.string.bic_empty_error;
//		}
		if(msgId>0&&showError){
			DialogManager.createMessageDialog(msgId, context).show();
		}
		this.confirmation_btn.setEnabled(!result);
		return result;
	}
	

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.add_phonebook_sbtn:
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
	 
	 public void setPurposeCurrency(TablesResponseModel purposeCurrencyTablesResponseModel){
		 this.purposeCurrencyTablesResponseModel=purposeCurrencyTablesResponseModel;
	 }

	public String getBeneficiary() {
		String beneficiary=null;
		if(user_my_card_sbtn.isChecked()){
			Object current=mIbanCardManager.getCurrentItem();
			if(current!=null){
				beneficiary=((AccountsModel)current).getCardHolder();
			}
		}else{
			beneficiary= beneficiary_name_et.getText().toString();
		}
		return beneficiary;
	}

	public String getCardNumber(){
		String result=null;
		if(user_my_card_sbtn.isChecked()){
			Object current=mIbanCardManager.getCurrentItem();
			if(current!=null){
				result=((AccountsModel)current).getCardNumber();
				if(result!=null&&result.length()>4){
					result=result.substring(result.length()-4);
				}
			}
		}else{
			result= iban_et.getText().toString();
		}
		return result;
	}
	
	public String getLastCardDigits(){
		String result=getCardNumber();
		if(result!=null&&result.length()>4){
			result=result.substring(result.length()-4);
		}
		return result;
	}

	public double getAmount(){
		return NewPaymentDataUtils.parseDouble(amount_et.getText().toString().trim());
	}

	public String getDescription() {
		return description_of_payment_et.getText().toString();
	}

	public String getPurposeCurrency() {
		return purpose_currency_et.getText().toString();
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
	}
	
	public boolean isAddPhoneBook(){
		return add_phonebook_sbtn.isChecked()&&input_benificiary_content.getVisibility()==View.VISIBLE;
	}

	@Override
	void disableConfirmButn() {
		// TODO Auto-generated method stub
		
	}
	
}
