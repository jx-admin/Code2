package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.oneMin.demo.slideListView.ItemSlideAdapter.ViewHolder;
import cn.oneMin.demo.slideListView.ItemSlideTouchListener;
import cn.oneMin.demo.slideListView.ItemSlideTouchListener.OnSlideItemClickListener;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.model.AmountAvailable;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.model.TablesResponseModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.NewPaymentDataUtils.TransferType;
import com.accenture.mbank.util.OnViewClickListener;
import com.accenture.mbank.util.ScrollerLinearMenu;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.adapter.PhoneRecipientAdapter;
import com.custom.view.SwitchButton;

public class BPERPaymentPhoneTopUpStep1 extends BPERPaymentInputPage {

	Button confirmation_btn;

	public static final String RECENTPAYMENT_TAB="tab1",NEWPAYMENT_TAB="tab2";
    int description_of_payment_etriptionLenth;
    
	TablesResponseModel mTablesResponseModel;
	TablesResponseModel purposeCurrencyTablesResponseModel;
	List<TransferObjectSim> recentTransferModel;
    
	String provider;
	
	private TabHost tabHost;
	private String numberNotMasked = "";
	TableLayout recent_content;
	LinearLayout provider_content;
	
	AutoCompleteTextView beneficiary_name_et;
	EditText beneficiary_phone_et;
	Button phonebook_btn;
//	ScrollerLinearMenu mScrollerLinearMenu;
	GridView amount_gv;
	SpinnerAdapter mProviderSpinnerAdapter;
	public SwitchButton add_phonebook_sbtn;
	
	List<PhoneRecipient> mPhoneRecipients;
	
	private boolean error_code_91050_91051 = false;
	
	private static int[]providerImages=new int[]{R.drawable.provider_tim_btn
			,R.drawable.provider_vodafone_btn
			,R.drawable.provider_wind_btn
			,R.drawable.provider_tiscali_btn
			,R.drawable.provider_tre_btn
			,R.drawable.provider_coop_voce_btn
			
	};
	private static int[]providerImages_1=new int[]{R.drawable.provider_tim_standard
			,R.drawable.provider_vodafone_standard
			,R.drawable.provider_wind_standard
			,R.drawable.provider_tiscali_standard
			,R.drawable.provider_tre_standard
			,R.drawable.provider_coop_voce_standard
			
	};
	private static int[]providerImages_2=new int[]{R.drawable.provider_tim_click
			,R.drawable.provider_vodafone_click
			,R.drawable.provider_wind_click
			,R.drawable.provider_tiscali_click
			,R.drawable.provider_tre_click
			,R.drawable.provider_coop_voce_click
			
	};
	
	String providerSelected;

	List<AmountAvailable> mAmountAvailableLs ;
	AmountAvailable mAmountAvailable;
	
	int providerSelectedPosition=-1;
	ImageView providerSelectedView=null;
	OnViewClickListener providerclickListener;
	
	
	Button selectedAmount;
	OnViewClickListener amountClick;
	ListAdapter amountListAdapter;
	
	public BPERPaymentPhoneTopUpStep1(Context context,TransferType mTransferType){
		super(context,mTransferType);
	}
	
	int getViewId(){
		return R.layout.bper_payment_phone_top_up;
	}

	public void setAdapter(PhoneRecipientAdapter adapter){
		if(adapter!=null){
			mPhoneRecipients=adapter.getDatas();
		}
		beneficiary_name_et.setDropDownHorizontalOffset(5);
		beneficiary_name_et.setThreshold(1);
		beneficiary_name_et.setAdapter(adapter);
		beneficiary_name_et.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				PhoneRecipient item=(PhoneRecipient) arg0.getAdapter().getItem(arg2);
				setRecipient(item);
			}
		});
	}

	void init() {
		amountClick=new OnViewClickListener() {
			
			@Override
			public void onClick(View v) {
				super.onClick(v);
				if(selectedAmount!=null){
					selectedAmount.setSelected(false);
				}
				selectedAmount=(Button) v;
				selectedAmount.setSelected(true);
				mAmountAvailable=(AmountAvailable) v.getTag();
				onCheckForConfirm(false);
			}
		};
		
		amountListAdapter=new ListAdapter() {
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
				ss.setSpan(mRelativeSizeSpan, ss.length()-4, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //0.5f表示默认字体大�?的一�?�  
				btn.setText(ss);
				btn.setTag(mAmountAvailable);
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
		mProviderSpinnerAdapter=new  SpinnerAdapter(){

			@Override
			public void registerDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void unregisterDataSetObserver(DataSetObserver observer) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return providerImages.length;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return providerImages[position];
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public boolean hasStableIds() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView btn;
				if(convertView==null){
					btn=new ImageView(context);
				}else{
					btn=(ImageView) convertView;
				}
//				if(providerSelectedPosition==position){
//					btn.setImageResource(providerImages_2[position]);
//				}else{
					btn.setImageResource(providerImages_1[position]);
//				}
				btn.setTag(position);
				return btn;
			}

			@Override
			public int getItemViewType(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getViewTypeCount() {
				// TODO Auto-generated method stub
				return 1;
			}

			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		providerclickListener=new OnViewClickListener() {
			
			@Override
			public void onClick(View view) {
				super.onClick(view);
				if(TextUtils.isEmpty(getPhoneNumber())){
					DialogManager.createMessageDialog(R.string.enter_phone_number, context).show();
					focusPhoneNumber();
					return;
				}
//				HorizontalScrollView mHorizontalScrollView=(HorizontalScrollView) view.getParent().getParent(); 
//				mHorizontalScrollView.smoothScrollTo((int)view.getLeft(),0);
				int position=(Integer)view.getTag();
				if(providerSelectedPosition==position){
					return;
				}
				if(providerSelectedView!=null&&(Integer)providerSelectedView.getTag()==providerSelectedPosition){
					providerSelectedView.setImageResource(providerImages_1[providerSelectedPosition]);
				}
				providerSelectedPosition=position;
				mAmountAvailable=null;
				providerSelectedView=(ImageView) view;
				providerSelectedView.setImageResource(providerImages_2[position]);
				if(monProviderclickListener!=null){
					monProviderclickListener.onClick(view, position);
				}
				onCheckForConfirm(false);
			}
		};

		super.init();
		
		beneficiary_name_et = (AutoCompleteTextView) contentView .findViewById(R.id.name_et);
		beneficiary_name_et.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					beneficiary_name_et.setText("");
				}
			}, 3000);
		beneficiary_name_et.addTextChangedListener(confirmTextChangeListener);
		beneficiary_name_et.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
						if(mPhoneRecipients==null||mPhoneRecipients.size()==0){
							return;
						}
						String inputName=beneficiary_name_et.getText().toString();
						if(TextUtils.isEmpty(inputName)){
							return;
						}
 						for(int i=0;i<mPhoneRecipients.size();i++){
							if(mPhoneRecipients.get(i).getName().equals(inputName)){
								add_phonebook_sbtn.setChecked(false);
								add_phonebook_sbtn.setEnabled(false);
								return;
							}
							
						}
						add_phonebook_sbtn.setEnabled(true);
					}
			}
		});
		beneficiary_name_et.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				beneficiary_phone_et.setFocusable(true);
				beneficiary_phone_et.requestFocus();
				beneficiary_phone_et.setFocusableInTouchMode(true);
				return true;
			}
		});
		beneficiary_phone_et = (EditText) contentView.findViewById(R.id.phone_et);
		beneficiary_phone_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				amount_gv.setSelection(-1);
				if(providerSelectedView!=null&&(Integer)providerSelectedView.getTag()==providerSelectedPosition){
					providerSelectedView.setImageResource(providerImages_1[providerSelectedPosition]);
				}
				providerSelectedPosition = -1;
				if(mAmountAvailableLs != null){
					mAmountAvailableLs.clear();
					amount_gv.setAdapter(amountListAdapter);
				}
				disableConfirmButn();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		beneficiary_phone_et.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				beneficiary_phone_et.setText("");
			}
		}, 3000);
		beneficiary_phone_et.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				confirmation_btn.setEnabled(false);
				return false;
			}
		});
//		beneficiary_phone_et.addTextChangedListener(confirmTextChangeForPhoneNumberListener);
		phonebook_btn = (Button) contentView.findViewById(R.id.phonebook_btn);
		provider_content=(LinearLayout) contentView.findViewById(R.id.provider_content);
		for(int i=0;i<mProviderSpinnerAdapter.getCount();i++){
			View itemView=mProviderSpinnerAdapter.getView(i, null, null);
			itemView.setTag(i);
			itemView.setOnClickListener(providerclickListener);
			provider_content.addView(itemView);
		}
		amount_gv = (GridView) contentView.findViewById(R.id.amount_gv);
		amount_gv.setAdapter(amountListAdapter);

		tabHost = (TabHost) contentView.findViewById(R.id.tabhost);
		tabHost.setup();
		Button icon = (Button) mLayoutInflater.inflate( R.layout.payment_tab_left_btn, null);
		icon.setText(R.string.phone_top_up_recent_payment_tab_name);
		tabHost.addTab(tabHost.newTabSpec(RECENTPAYMENT_TAB) .setIndicator(icon/*
								 * , * getResources().getDrawable(R.drawable.mumule)
								 */).setContent(R.id.tab1));

		icon = (Button) mLayoutInflater.inflate(R.layout.payment_tab_right_btn, null);
		icon.setText(R.string.phone_top_up_new_payment_tab_name);
		tabHost.addTab(tabHost.newTabSpec(NEWPAYMENT_TAB).setIndicator(icon) .setContent(R.id.tab2));
		 tabHost.setCurrentTab(1);
		recent_content = (TableLayout) contentView .findViewById(R.id.recent_content);
		add_phonebook_sbtn=(SwitchButton) contentView.findViewById(R.id.add_phonebook_sbtn);
		confirmation_btn = (Button) contentView .findViewById(R.id.confirmation_btn);
		confirmation_btn.setOnClickListener(mOnConfirmClickListener);
		confirmation_btn.setOnTouchListener(mOnConfirmTouchListener);
	}
	
	ItemSlideTouchListener mItemSlideTouchListener=new ItemSlideTouchListener();
	public TransferObject getRecentTransferModel(int position){
		if(recentTransferModel!=null&&position>=0&&position<recentTransferModel.size()){
			return recentTransferModel.get(position);
		}
		return null;
	}

	public void setRecentTransferModels(List<TransferObjectSim> recentTransferModel,OnSlideItemClickListener mOnSlideItemClickListener){
		mItemSlideTouchListener.setOnSlideItemClickListener(mOnSlideItemClickListener);
		this.recentTransferModel=recentTransferModel;
		recent_content.removeAllViews();
		{
			View rowItem=(View) mLayoutInflater.inflate(R.layout.bper_recent_slidelist_items, null);
		TextView textView1=(TextView) rowItem.findViewById(R.id.textView1);
		textView1.setText(R.string.data_operation);
		TextView textView2=(TextView) rowItem.findViewById(R.id.textView2);
		textView2.setText(R.string.provider_fs);
		TextView textView3=(TextView) rowItem.findViewById(R.id.textView3);
		textView3.setVisibility(View.VISIBLE);
		textView3.setText(R.string.number);
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
			TransferObjectSim accountsModel=recentTransferModel.get(i);
			View rowItem=(View) mLayoutInflater.inflate(R.layout.bper_recent_slidelist_items, null);
			TextView textView1=(TextView) rowItem.findViewById(R.id.textView1);
			String operationDate=TimeUtil.getDateString(accountsModel.getDate(),TimeUtil.dateFormat5);
			textView1.setText(operationDate);
			TextView textView2=(TextView) rowItem.findViewById(R.id.textView2);
			textView2.setText(accountsModel.getBeneficiaryProviderName());
			TextView textView3=(TextView) rowItem.findViewById(R.id.textView3);
			textView3.setVisibility(View.VISIBLE);
			
			if(BaseActivity.isOffline){
				textView3.setText(accountsModel.getBeneficiaryNumber());
			}else{
			String certifiedNumber = Contants.getUserInfo.getUserprofileHb().getContactPhone();
			if(certifiedNumber != null && accountsModel.getBeneficiaryNumber().equals(certifiedNumber)){
				textView3.setText(Utils.maskCertifiedNumber(accountsModel.getBeneficiaryNumber()));
			} else {
				textView3.setText(accountsModel.getBeneficiaryNumber());
			}
			}
			
			//textView3.setText(accountsModel.getBeneficiaryNumber());
			TextView textView4=(TextView) rowItem.findViewById(R.id.textView4);
			textView4.setText(Utils.notPlusGenerateFormatMoney(currency,accountsModel.getAmount()));
			recent_content.addView(rowItem);
			if(i<size-1){
				recent_content.addView(mLayoutInflater.inflate(R.layout.separation_line_divider, null));
			}
			ViewHolder mViewHolder=new ViewHolder();
			mViewHolder.position=i;
			mViewHolder.init(rowItem);
			rowItem.setTag(mViewHolder);
			rowItem.setOnTouchListener(mItemSlideTouchListener);
		}
	}
	
	/**
	 * The confirmation button (Continua) is disabled until the user fill all
	 * mandatory fields.
	 * 
	 * After the user clicks confirmation button (Continua), the app must check
	 * all the followings:  all mandatory fields are filled o if phone number
	 * is empty , show the message: ERRORE Numero di telefono non valido o if
	 * provider is not selected , show the message: ERRORE Specificare
	 * l’operatore o if Amount is not selected , show the message: ERRORE
	 * Selezionare un importo
	 * 
	 * @return true if user fill all mandatory fields
	 */
	boolean onCheckForConfirm(boolean showError){
		boolean result=false;
		int msgId=0;
//		if(TextUtils.isEmpty(getBeneficiary())){
//			result=true;
//		}
//		else 
			if(TextUtils.isEmpty(beneficiary_phone_et.getText())){
			result=true;
			msgId=R.string.select_phone_number;
		}
		else if(TextUtils.isEmpty(getProviderCode())){
			result=true;
			msgId=R.string.pls_select_provider;
		}
		else if(getAmount()==null){
			result=true;
			msgId=R.string.pls_set_amount;
		} else if(isError_code_91050_91051()){
			result=true;
		}
		if(msgId>0&&showError){
			DialogManager.createMessageDialog(msgId, context).show();
		}
		this.confirmation_btn.setEnabled(!result);
		return result;
	}

    public boolean onBackPressed() {
    	return false;
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

	public String getBeneficiary() {
		return beneficiary_name_et.getText().toString().trim();
	}
	
	public AmountAvailable  getAmount(){
		return mAmountAvailable;
	}

	public void setNewPayment(boolean newPayment) {
		if(newPayment){
			tabHost.setCurrentTab(1);
		}else{
			tabHost.setCurrentTab(0);
		}
	}
	
	
	
	public void focusPhoneNumber(){
		beneficiary_phone_et.setFocusable(true);
		beneficiary_phone_et.requestFocusFromTouch();
	}
	
	public interface onProviderclickListener{
		public void onClick(View v,int postion);
	}
	
	onProviderclickListener monProviderclickListener;
	public void setOnProviderclickListener(onProviderclickListener monProviderclickListener){
		this.monProviderclickListener=monProviderclickListener;
	}

	public void setOnBeneficiaryClickListener(OnViewClickListener onClickListener) {
		phonebook_btn.setOnClickListener(onClickListener);
	}
	
	public void setRecipient(PhoneRecipient mPhoneRecipient){
		beneficiary_phone_et.removeTextChangedListener(confirmTextChangeForPhoneNumberListener);
		setBeneficiaryName(mPhoneRecipient.getName());
		setPhoneNumber(mPhoneRecipient.getPhoneNumber());
		for(int i=0;i<TransferObjectSim.providerCodes.length;i++){
			if(TransferObjectSim.providerCodes[i].equals(mPhoneRecipient.getProviderCode())){
				provider_content.getChildAt(i).performClick();
			}
		}
		
		beneficiary_phone_et.addTextChangedListener(confirmTextChangeForPhoneNumberListener);
	}
	
	public void setBeneficiaryName(String name) {
		beneficiary_name_et.setText(name);
		beneficiary_name_et.dismissDropDown();
	}
	
	public void setPhoneNumber(String name){
		numberNotMasked = name;
		if(BaseActivity.isOffline){
			beneficiary_phone_et.setText(name);
		}else{
		String certifiedNumber = Contants.getUserInfo.getUserprofileHb().getContactPhone();
		if(certifiedNumber != null && name.equals(certifiedNumber)){
			beneficiary_phone_et.setText(Utils.maskCertifiedNumber(name));
		} else {
			beneficiary_phone_et.setText(name);
		}
		}
		
//		View proView=provider_content.getChildAt(0);
//		if(proView!=null){
//			proView.performClick();
//		}
	}
	
	public String getPhoneNumber(){
		
		String number = beneficiary_phone_et.getText().toString().trim();
		if (number.contains("*")) {
			if(!numberNotMasked.equals("")){
				return numberNotMasked;
			} else {
				return beneficiary_phone_et.getText().toString().trim();
			}
		} else{
			return number;
		}
	}
	
	public String getProviderCode(){
		if(providerSelectedPosition>=0){
			return TransferObjectSim.providerCodes[providerSelectedPosition];
		}
		return null;
	}
	
	public String getProviderName(){
		if(providerSelectedPosition>=0){
			String code= TransferObjectSim.providerCodes[providerSelectedPosition];
			return TransferObjectSim.providerNameMap.get(code);
		}
		return null;
	}
	
	public void setAmountAvailableLs(List<AmountAvailable> mAmountAvailableLs) {
		this.mAmountAvailableLs=mAmountAvailableLs;
		amount_gv.setAdapter(amountListAdapter);
	}

	public boolean isError_code_91050_91051() {
		return error_code_91050_91051;
	}

	public void setError_code_91050_91051(boolean error_code_91050_91051) {
		this.error_code_91050_91051 = error_code_91050_91051;
	}

	@Override
	void disableConfirmButn() {
		// TODO Auto-generated method stub
		confirmation_btn.setEnabled(false);
	}
	
	

}
