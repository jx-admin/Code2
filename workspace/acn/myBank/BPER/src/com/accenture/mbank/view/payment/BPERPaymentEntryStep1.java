package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.accenture.mbank.logic.TransferObjectEntry;
import com.accenture.mbank.model.AccountsModel;
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
import com.custom.view.SwitchButton;

public class BPERPaymentEntryStep1 extends BPERPaymentInputPage implements OnCheckedChangeListener {

	public static final String RECENTPAYMENT_TAB="tab1",NEWPAYMENT_TAB="tab2";
    
	TablesResponseModel mTablesResponseModel;
	TablesResponseModel purposeCurrencyTablesResponseModel;
	List<TransferObjectEntry> recentTransferModel;
    
	long date;
	
	AccountsModel selectedAccountsModel;
	
	private TabHost tabHost;
//	EditText bic_et;
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
	
	TableLayout recent_content;
	TableLayout beneficiary_rg;
	
	DesLimitTextChangedListener mDesLimitTextChangedListener;
	
	OnViewClickListener onClickListener;
	
	public BPERPaymentEntryStep1(Context context,TransferType mTransferType){
		super(context,mTransferType);
	}
	
	int getViewId(){
		return R.layout.bper_payment_transfer_entry;
	}
	
	public ViewGroup getContentView(){
		return super.getContentView();
	}
	
	public void setAccounts(List<AccountsModel> accounts){
		mBPERPaymentAccountPaperUtils.setData(accounts);
		selectedPosition=-1;
		disAblePosition=0;
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
		Holder selectedHolder=(Holder) beneficiary_rg.getChildAt(BPERPaymentEntryStep1.selectedPosition).getTag();
		selectedHolder.rbtn.setChecked(false);
		}
		selectedPosition=mHolder.position;
		onCheckForConfirm(false);
		mHolder.rbtn.setChecked(true);
	}
	
	public void onDisable(int disAblePosition){
		if(BPERPaymentEntryStep1.disAblePosition==disAblePosition){
			return ;
		}
		if(BPERPaymentEntryStep1.disAblePosition>=0){
			Holder mHolder=(Holder) beneficiary_rg.getChildAt(BPERPaymentEntryStep1.disAblePosition).getTag();
			mHolder.onEnable(true);
		}
		BPERPaymentEntryStep1.disAblePosition=disAblePosition;
		if(disAblePosition>=0){
			if(disAblePosition==selectedPosition){
				selectedPosition=-1;
			}
			Holder mHolder=(Holder) beneficiary_rg.getChildAt(disAblePosition).getTag();
			mHolder.onEnable(false);
		}
	}
	
	static int selectedPosition=-1;
	static int disAblePosition=-1;
	class Holder {
		
		View itemView;
		int position;
		AccountsModel mAccountsModel;
		RadioButton rbtn;
		TextView name_tv;
		TextView iban;
		TextView avilable_banlence_tv;
		View divider;
		public Holder(View itemView){
			this.itemView=itemView;
			itemView.setTag(this);
			rbtn=(RadioButton) itemView.findViewById(R.id.rbtn);
			name_tv=(TextView) itemView.findViewById(R.id.name_tv);
			iban=(TextView) itemView.findViewById(R.id.iban);
			avilable_banlence_tv=(TextView) itemView.findViewById(R.id.avilable_banlence_tv);
			divider=itemView.findViewById(R.id.divider);
		}
		public void setData(int position,AccountsModel mAccountsModel){
			this.position=position;
			this.mAccountsModel=mAccountsModel;
			name_tv.setText(mAccountsModel.getAccountAlias());
//			iban.setText(mAccountsModel.getIbanCode());
			
			double available=mAccountsModel.getAvailableBalance();
			avilable_banlence_tv.setText(Utils.generateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), available));
			
			if(disAblePosition==position){
				onEnable(false);
				if(selectedPosition==disAblePosition){
					selectedPosition=-1;
				}
			}else{
				rbtn.setChecked(selectedPosition==position);
				name_tv.setTextColor(name_tv.getResources().getColor(R.color.black));
				itemView.setEnabled(true);
				rbtn.setEnabled(true);
			}
		}
		
		public void onEnable(boolean enable){
			Holder mHolder=(Holder) beneficiary_rg.getChildAt(BPERPaymentEntryStep1.disAblePosition).getTag();
			if(enable){
				name_tv.setTextColor(mHolder.name_tv.getResources().getColor(R.color.black));
				iban.setTextColor(iban.getResources().getColor(R.color.gray));
				avilable_banlence_tv.setTextColor(mHolder.name_tv.getResources().getColor(R.color.black));
			}else{
				name_tv.setTextColor(mHolder.name_tv.getResources().getColor(R.color.light_gray));
				avilable_banlence_tv.setTextColor(mHolder.name_tv.getResources().getColor(R.color.light_gray));
				iban.setTextColor(iban.getResources().getColor(R.color.light_gray));
				rbtn.setChecked(false);
			}
			mHolder.itemView.setEnabled(enable);
			mHolder.rbtn.setEnabled(enable);
		}
		
	}
	
	OnViewClickListener mItemOnClickListener=new OnViewClickListener() {
		
		@Override
		public void onClick(View v) {
			super.onClick(v);
//			Toast.makeText(context, "selected", 0).show();
			onSelected(v);
		}
	};
	
	public void setOnItemClickListener(EntryItemClikListener mEntryItemClikListener){
		this.mEntryItemClikListener=mEntryItemClikListener;
	}
	
	EntryItemClikListener mEntryItemClikListener;
	interface EntryItemClikListener{
		public void onItemClick(View v,AccountsModel mAccountsModel);
	}
	
	void init(){

		onClickListener=new OnViewClickListener(){
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
	            }, calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender
	                    .get(Calendar.DATE)).show();
				break;
			case R.id.purpose_currency_ibtn:
				if(purposeCurrencyTablesResponseModel==null||purposeCurrencyTablesResponseModel.getTablewrapperList()==null||purposeCurrencyTablesResponseModel.getTablewrapperList().size()<=0){
					final ProgressDialog downloading_pd = new ProgressDialog(context);
					downloading_pd.setTitle(R.string.downloading);
					if(!downloading_pd.isShowing()){
						downloading_pd.show();
					}
					NewPaymentDataUtils.getTablesResponseModel(context,new Handler(){public void handleMessage(android.os.Message msg) {
						setPurposeCurrency((TablesResponseModel) msg.obj);
						if(downloading_pd!=null&&downloading_pd.isShowing()){
							downloading_pd.dismiss();
						}
					showPurposeCurrencySelectDialog();
					};}, NewPaymentDataUtils.LOAD_PURPOSE_CURRENCY,"PURPOSE_CURRENCY");

				}else{
					showPurposeCurrencySelectDialog();
				}
				break;
			case R.id.confirmation_btn:
					onConfirm();
				break;
			case R.id.optional_tv_info:
				DialogManager.createMessageDialog(R.string.cup_cig_info, context).show();
				break;
			default:
				break;
			}
		}
		};
		super.init();
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
		 tabHost.setCurrentTab(1);
		 recent_content=(TableLayout) contentView.findViewById(R.id.recent_content);
//		 bic_et=(EditText) contentView.findViewById(R.id.bic_et);
		 cup_et=(EditText) contentView.findViewById(R.id.cup_et);
		 cig_et=(EditText) contentView.findViewById(R.id.cig_et);
		 currency_tv=(TextView) contentView.findViewById(R.id.currency_tv);
		 currency_tv.setText(NewPaymentDataUtils.getCurrency());
		 amount_et=(EditText) contentView.findViewById(R.id.amount_et);
		 amount_et.addTextChangedListener(confirmTextChangeListener);
		 new AmountItalyInputFilter(amount_et,null);
		 optional_tv_info = (ImageView) contentView.findViewById(R.id.optional_tv_info);
		 if (optional_tv_info != null) {
			 optional_tv_info.setOnClickListener(onClickListener);
		}
		 
		 cup_e_cig_sbtn=(SwitchButton) contentView.findViewById(R.id.cup_e_cig_sbtn);
		 cup_e_cig_sbtn.setOnCheckedChangeListener(this);
		 description_of_payment_et=(EditText) contentView.findViewById(R.id.description_of_payment_et);
		 description_of_payment_et.addTextChangedListener(confirmTextChangeListener);
		 description_of_payment_et.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					KeyBoardUtils.hideSoftInputFromWindow(context, description_of_payment_et.getWindowToken());
					return false;
				}
			});
		 des_length_alerter_tv=(TextView) contentView.findViewById(R.id.des_length_alerter_tv);
		 mDesLimitTextChangedListener=new DesLimitTextChangedListener(context,description_of_payment_et,des_length_alerter_tv);
		 mDesLimitTextChangedListener.setCharLimit(true);
		 data_et=(EditText) contentView.findViewById(R.id.data_et);
		 data_ibtn=(ImageButton) contentView.findViewById(R.id.data_ibtn);
		 data_ibtn.setOnClickListener(onClickListener);
		 data_et.setOnClickListener(onClickListener);
		 purpose_currency_liner=(LinearLayout) contentView.findViewById(R.id.purpose_currency_liner);
		 purpose_currency_et=(EditText) contentView.findViewById(R.id.purpose_currency_et);
		 purpose_currency_ibtn=(ImageButton) contentView.findViewById(R.id.purpose_currency_ibtn);
		 purpose_currency_ibtn.setOnClickListener(onClickListener);

		 confirmation_btn = (Button) contentView .findViewById(R.id.confirmation_btn);
		 confirmation_btn.setOnClickListener(mOnConfirmClickListener);
		 confirmation_btn.setOnTouchListener(mOnConfirmTouchListener);
		 mDesLimitTextChangedListener.setDestriptionMaxLenth(DesLimitTextChangedListener.DES_LENGTH_OFF);
		 beneficiary_rg=(TableLayout) contentView.findViewById(R.id.beneficiary_rg);
	}
	ItemSlideTouchListener mItemSlideTouchListener=new ItemSlideTouchListener();
	public TransferObject getRecentTransferModel(int position){
		if(recentTransferModel!=null&&position>=0&&position<recentTransferModel.size()){
			return recentTransferModel.get(position);
		}
		return null;
	}
	
	public void setRecentTransferModels(List<TransferObjectEntry> recentTransferModel,OnSlideItemClickListener monSlideItemClickListener){
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
			TransferObjectEntry accountsModel=recentTransferModel.get(i);
			View rowItem=mLayoutInflater.inflate(R.layout.bper_recent_slidelist_items, null);
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
			ViewHolder mViewHolder=new ViewHolder();
			mViewHolder.init(rowItem);
			mViewHolder.position=i;
			rowItem.setTag(mViewHolder);
			rowItem.setOnTouchListener(mItemSlideTouchListener);
		}
	}
	
//	boolean onCheckForConfirm(){
//		boolean result=true;
//		if(getBeneficiary()==null){
//			result=false;
//			((BaseActivity)context).displayErrorMessage("",context.getString(R.string.pls_select_payee));
//		}
////		else if(TextUtils.isEmpty(iban_et.getText())){
////			result=false;
////			((BaseActivity)context).displayErrorMessage(context.getString(R.string.iban_empty_error));
////		}
//		else if(TextUtils.isEmpty(amount_et.getText())){
//			result=false;
//			((BaseActivity)context).displayErrorMessage("",context.getString(R.string.amount_empty_error));
//		}
//		else if(TextUtils.isEmpty(description_of_payment_et.getText())){
//			result=false;
//			((BaseActivity)context).displayErrorMessage("",context.getString(R.string.description_empty_error));
//		}
//		else if(TextUtils.isEmpty(purpose_currency_et.getText())){
//			result=false;
//			((BaseActivity)context).displayErrorMessage("",context.getString(R.string.purpose_currency_empty_error));
//		}
//		else if(TextUtils.isEmpty(cup_et.getText())||TextUtils.isEmpty(cig_et.getText())){
//			result=false;
//			((BaseActivity)context).displayErrorMessage("",context.getString(R.string.cup_e_cig_empty_error));
//		}
////		else if(TextUtils.isEmpty(bic_et.getText())){
////			result=false;
////			((BaseActivity)context).displayErrorMessage(context.getString(R.string.bic_empty_error));
////		}
//		return result;
//	}
	
	public void setDate(long date){
		this.date=date;
		data_et.setText(TimeUtil.getDateString(date,TimeUtil.dateFormat5));
	}
	
	private void showPurposeCurrencySelectDialog(){
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
	}

	
	boolean onCheckForConfirm(boolean showError){
		boolean result=false;
		int msgId=0;
		if(getBeneficiary()==null){
			result=true;
		}else if(getAmount()<=0){
			msgId=R.string.amount_empty_erro_msg;
			result=true;
		}else if(TextUtils.isEmpty(getDescription())){
			msgId=R.string.description_empty_erro_msg;
			result=true;
		}
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
		case R.id.cup_e_cig_sbtn:
			if(isChecked){
				mDesLimitTextChangedListener.setDestriptionMaxLenth(DesLimitTextChangedListener.DES_LENGTH_ON);
				cup_et.setVisibility(View.VISIBLE);
				cig_et.setVisibility(View.VISIBLE);
			}else{
				mDesLimitTextChangedListener.setDestriptionMaxLenth(DesLimitTextChangedListener.DES_LENGTH_OFF);
				cup_et.setVisibility(View.GONE);
				cig_et.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}
	
	public void setOnTabChangeListener(OnTabChangeListener l){
		 tabHost.setOnTabChangedListener(l);
	}
	
	 public void setSepaCountryIBAN(TablesResponseModel mTablesResponseModel){
		 this.mTablesResponseModel=mTablesResponseModel;
	 }
	 
	 public void setPurposeCurrency(TablesResponseModel purposeCurrencyTablesResponseModel){
		 this.purposeCurrencyTablesResponseModel=purposeCurrencyTablesResponseModel;
	 }

	public AccountsModel getBeneficiary() {
		if(selectedPosition>=0){
			if(beneficiary_rg==null||selectedPosition>=beneficiary_rg.getChildCount()){
				selectedPosition=-1;
				return null;
			}
			Holder mHolder=(Holder) beneficiary_rg.getChildAt(selectedPosition).getTag();
			return mHolder.mAccountsModel;
		}
		return null;
	}
	
	public double getAmount(){
		return NewPaymentDataUtils.parseDouble(amount_et.getText().toString().trim());
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

	@Override
	void disableConfirmButn() {
		// TODO Auto-generated method stub
		
	}
}
