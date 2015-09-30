
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.accenture.mbank.logic.InsertCommunicationJson;
import com.accenture.mbank.model.TableContentList;
import com.accenture.mbank.util.ContactDate;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.view.DatePickerDialog.DatePickListener;

public class ContactNewRequestChannelContainer extends ExpandedContainer implements android.widget.RadioGroup.OnCheckedChangeListener, OnClickListener {
	
	//group
	private RadioGroup channel_group;
	
	//by mail
	private RadioButton request_by_mail_radio;
	private EditText request_channel_email_email;
	
	//by phone
	private RadioButton request_by_Phone_radio;
	private View request_by_Phone_layout;
	private EditText request_details_phone;
	private EditText request_details_phone_hours_et;
	private Button request_details_phone_hours_btn;
	
	//by branch group
	private RadioButton request_branch_radio;
	private View request_branch_content;
	private TextView informations_tv;
	
	//branch mail
	private RadioButton request_details_branch_email;
	private EditText request_details_branch_email_input;
	
	//branch phone
	private RadioButton request_details_branch_phone;
	private EditText request_details_branch_phone_input;
	private EditText request_details_branch_date_input_et;
	private Button request_details_branch_date_input_btn;
	private EditText request_details_branch_hours_et;
	private Button request_details_branch_hours_btn;
	
	//model
	public enum Type{Email,Phone,Branch,Null};
	public Type type;
	
	private String eMail;
	private String phone;
	private List<TableContentList> branchTableContentLists = null;
	
	private void init(){
		
		channel_group=(RadioGroup) findViewById(R.id.request_channel_group);
		channel_group.setOnCheckedChangeListener(this);
		
		request_by_mail_radio=(RadioButton) findViewById(R.id.request_by_mail_radio);
		request_channel_email_email=(EditText) findViewById(R.id.request_channel_email_email);
		request_channel_email_email.addTextChangedListener(mailTextWatcher);
		
		request_by_Phone_radio=(RadioButton) findViewById(R.id.request_by_Phone_radio);
		request_by_Phone_layout=findViewById(R.id.request_by_Phone_layout);
		request_details_phone=(EditText) findViewById(R.id.request_details_phone);
		request_details_phone.addTextChangedListener(phoneTextWatcher);
		request_details_phone.setOnFocusChangeListener(mOnFocusChangeListener);
		request_details_phone_hours_et=(EditText) findViewById(R.id.request_details_phone_hours_et);
		request_details_phone_hours_et.addTextChangedListener(onConfirmTextWatcher);
		request_details_phone_hours_btn=(Button) findViewById(R.id.request_details_phone_hours_btn);
		request_details_phone_hours_btn.setOnClickListener(this);
		request_details_phone_hours_et.setOnClickListener(this);
		
		
		request_branch_radio=(RadioButton) findViewById(R.id.request_branch_radio);
		request_branch_content= findViewById(R.id.request_branch_content);
		informations_tv= (TextView) findViewById(R.id.informations_tv);
		
		request_details_branch_email=(RadioButton) findViewById(R.id.request_details_branch_email);
		request_details_branch_email_input=(EditText) findViewById(R.id.request_details_branch_email_input);
		request_details_branch_email_input.addTextChangedListener(mailTextWatcher);
		request_details_branch_phone=(RadioButton) findViewById(R.id.request_details_branch_phone);
		request_details_branch_phone_input=(EditText) findViewById(R.id.request_details_branch_phone_input);
		request_details_branch_phone_input.addTextChangedListener(phoneTextWatcher);
		request_details_branch_phone_input.setOnFocusChangeListener(mOnFocusChangeListener);
		
		request_details_branch_date_input_et=(EditText) findViewById(R.id.request_details_branch_date_input_et);
		request_details_branch_date_input_et.addTextChangedListener(onConfirmTextWatcher);
		request_details_branch_date_input_btn=(Button) findViewById(R.id.request_details_branch_date_input_btn);
		request_details_branch_hours_et=(EditText) findViewById(R.id.request_details_branch_hours_et);
		request_details_branch_hours_et.addTextChangedListener(onConfirmTextWatcher);
		request_details_branch_hours_btn=(Button) findViewById(R.id.request_details_branch_hours_btn);
		request_details_branch_date_input_btn.setOnClickListener(this);
		request_details_branch_date_input_et.setOnClickListener(this);
		request_details_branch_hours_et.setOnClickListener(this);
		request_details_branch_hours_btn.setOnClickListener(this);
		
		request_details_branch_email.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					request_details_branch_email_input.setEnabled(true);
					request_details_branch_phone.setChecked(false);
					branch = InsertCommunicationJson.BRANCH_EMAIL;
					//
					request_details_branch_date_input_et.setEnabled(true);
					request_details_branch_date_input_btn.setEnabled(true);
					request_details_branch_hours_et.setEnabled(true);
					request_details_branch_hours_btn.setEnabled(true);
				}else{
					request_details_branch_email_input.setEnabled(false);
				}
				onCheckForConfirm();
			}
		});
		request_details_branch_phone.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					request_details_branch_email.setChecked(false);
					request_details_branch_phone_input.setEnabled(true);
					branch = InsertCommunicationJson.BRANCH_PHONE;
					//
					request_details_branch_date_input_et.setEnabled(true);
					request_details_branch_date_input_btn.setEnabled(true);
					request_details_branch_hours_et.setEnabled(true);
					request_details_branch_hours_btn.setEnabled(true);
				}else{
					request_details_branch_phone_input.setEnabled(false);
				}
				onCheckForConfirm();
			}
		});
	}
	
	public void show(Type type){
		if(type==null||this.type==type){
			return;
		}
		this.type=type;
		switch (type) {
		case Email:
			request_by_mail_radio.setChecked(true);
			request_channel_email_email.setVisibility(View.VISIBLE);
			request_by_Phone_radio.setChecked(false);
			request_by_Phone_layout.setVisibility(View.GONE);
			request_branch_radio.setChecked(false);
			request_branch_content.setVisibility(View.GONE);
			showEmail();
			break;
		case Phone:
			request_by_mail_radio.setChecked(false);
			request_channel_email_email.setVisibility(View.GONE);
			request_by_Phone_radio.setChecked(true);
			request_by_Phone_layout.setVisibility(View.VISIBLE);
			request_branch_radio.setChecked(false);
			request_branch_content.setVisibility(View.GONE);
			showPhone();
			break;
		case Branch:
			request_by_mail_radio.setChecked(false);
			request_channel_email_email.setVisibility(View.GONE);
			request_by_Phone_radio.setChecked(false);
			request_by_Phone_layout.setVisibility(View.GONE);
			request_branch_radio.setChecked(true);
			request_branch_content.setVisibility(View.VISIBLE);
			showBranch();
			break;
			
		default:
			request_by_mail_radio.setChecked(false);
			request_channel_email_email.setVisibility(View.GONE);
			request_by_Phone_radio.setChecked(false);
			request_by_Phone_layout.setVisibility(View.GONE);
			request_branch_radio.setChecked(false);
			request_branch_content.setVisibility(View.GONE);
			break;
		}
	}
	
	
	private boolean bInitialized = false;
	
	public ContactNewRequestChannelContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
	
	public void init(List<TableContentList> tableContentList) {
		if (!bInitialized) {
			init();
			show(Type.Null);
			bInitialized = true;
		}
	}
	
	public interface onChannelChangedListener {
		public void onModeChanged(final String code, final String description);
	}
	//email
	private void showEmail() {
		request_channel_email_email.setText(getEmail());
	}
	
	
	//phone
	private List<TableContentList> phoneTableContentLists = null;
	
	private AlertDialog alertDialog = null;
	
	
	public void initPhone(final List<TableContentList> tableContentList) {
		this.phoneTableContentLists = tableContentList;
	}
	public void showPhone(){
		String phone=getPhone();
		if(phone!=null&&phone.length()>4){
			phone=phone.substring(0, phone.length()-4)+"****";
		}
		request_details_phone.setText(phone);
		request_details_phone_hours_et.setText(getContactTime());
	}
	
	public String getContactTime() {
		if(TextUtils.isEmpty(request_details_phone_hours_et.getText())&&phoneTableContentLists!=null&&phoneTableContentLists.size()>0){
			return phoneTableContentLists.get(0).getDescription();
		}
		return request_details_phone_hours_et.getText().toString().trim();
	}
	
	private String branch = InsertCommunicationJson.BRANCH_EMAIL;;
	
	private String branchName;
	
	
	public void setInformations(String informations){
		informations_tv.setText(informations);
	}
	
	/**
	 * @return branchName
	 */
	public String getBranchName() {
		if(branchName==null){
			branchName=Contants.getUserInfo.getUserprofileHb().getBranchName();
		}
		return branchName;
	}
	
	public void showBranch() {
		request_details_branch_email_input.setText(getEmail());
		
		String phone=getPhone();
		if(phone!=null&&phone.length()>4){
			phone=phone.substring(0, phone.length()-4)+"****";
		}
		request_details_branch_phone_input.setText(phone);
		request_details_branch_date_input_et.setText(getBranchContactDate());
		request_details_branch_hours_et.setText(getBranchContactTime());
//            if(InsertCommunicationJson.BRANCH_EMAIL.equals(branch)){
//            	request_details_branch_email.setChecked(true);
//            }else if(InsertCommunicationJson.BRANCH_PHONE.equals(branch)){
//            	request_details_branch_phone.setChecked(true);
//            }
		request_details_branch_email.setChecked(false);
		request_details_branch_phone.setChecked(false);
		//
		request_details_branch_date_input_et.setEnabled(false);
		request_details_branch_date_input_btn.setEnabled(false);
		request_details_branch_hours_et.setEnabled(false);
		request_details_branch_hours_btn.setEnabled(false);
	}
	
	public void setBranchTable(List<TableContentList> tableContentList) {
		this.branchTableContentLists = tableContentList;
	}
	
	public String getBranchContactDate() {
		if(TextUtils.isEmpty(request_details_branch_date_input_et.getText())){
			return  get7WorkDay();
		}
		return String.valueOf(request_details_branch_date_input_et.getText().toString().trim());
	}
	
	public String getBranchContactTime() {
		if(TextUtils.isEmpty(request_details_branch_hours_et.getText())&&branchTableContentLists!=null&&branchTableContentLists.size()>0){
			return branchTableContentLists.get(0).getDescription();
		}
		return request_details_branch_hours_et.getText().toString().trim();
	}
	
	public String getBranch() {
		return branch;
	}
	
	int oneDay = 24 * 60 * 60 * 1000;
	private String get7WorkDay(){
		Calendar calendar = Calendar.getInstance();
		long nextWeekTime = calendar.getTimeInMillis();
        nextWeekTime= ContactDate.getWorkDay(nextWeekTime,7);
		calendar.setTimeInMillis(nextWeekTime);
		SimpleDateFormat format = new SimpleDateFormat(TimeUtil.dateFormat5, Locale.US);
		return format.format(calendar.getTime());
	}
	
	/**model selected*/
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		onCheckForConfirm();
		switch(checkedId){
		case R.id.request_by_mail_radio:
			show(Type.Email);
			break;
		case R.id.request_by_Phone_radio:
			show(Type.Phone);
			break;
		case R.id.request_branch_radio:
			show(Type.Branch);
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.request_details_phone_hours_btn:
		case R.id.request_details_phone_hours_et:
			alertDialog = DialogManager.createHoursDialog(getContext(), new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					request_details_phone_hours_et.setText(phoneTableContentLists.get(position).getDescription());
					alertDialog.dismiss();
				}
			}, phoneTableContentLists);
			alertDialog.show();
			break;
			//branch date click
		case R.id.request_details_branch_date_input_btn:
		case R.id.request_details_branch_date_input_et:
			final Calendar calender = Calendar.getInstance();
			new DatePickerDialog(getContext(), new OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					calender.set(year, monthOfYear, dayOfMonth);
//					setDate(calender.getTimeInMillis());
					
					monthOfYear = monthOfYear + 1;
					// format: dd.MM.yy
					String monthStr = "";
					if(monthOfYear > 0 && monthOfYear < 10){
						monthStr = "0" + monthOfYear;
					}else {
						monthStr = "" + monthOfYear;
					}
					request_details_branch_date_input_et.setText(new StringBuffer().append(dayOfMonth).append('.').append(monthStr).append('.') .append(year % 100).toString());
				
				}
			}, calender.get(Calendar.YEAR), calender.get(Calendar.MONDAY), calender
			.get(Calendar.DATE)).show();
			break;
			//branch hours click
		case R.id.request_details_branch_hours_et:
		case R.id.request_details_branch_hours_btn:
			alertDialog = DialogManager.createHoursDialog(getContext(), new OnItemClickListener() { 
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					request_details_branch_hours_et.setText(branchTableContentLists.get(position).getDescription());
					alertDialog.dismiss();
				} }, branchTableContentLists); alertDialog.show();
				break;
		}
	}
	
	OnFocusChangeListener mOnFocusChangeListener=new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if(v==request_details_branch_phone_input||v==request_details_phone){
				TextView tv=(TextView) v;
				if(hasFocus){
					tv.setText("");
				}else{
					if(TextUtils.isEmpty(tv.getText())){
						String phone=getPhone();
						if(phone!=null&&phone.length()>4){
							phone=phone.substring(0, phone.length()-4)+"****";
						}
						tv.setText(phone);
					}
				}
			}
		}
	};
	
	TextWatcher mailTextWatcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			expandResultChange(String.valueOf(s));
			eMail=s.toString();
			onCheckForConfirm();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	TextWatcher phoneTextWatcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			expandResultChange(String.valueOf(s));
			String string=s.toString().toString().trim();
			if(!TextUtils.isEmpty(string)&&!s.toString().endsWith("*")){
				phone=s.toString();
			}
			onCheckForConfirm();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	TextWatcher onConfirmTextWatcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			onCheckForConfirm();
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	public String getPhone(){
		if(phone==null){
			phone=Contants.getUserInfo.getUserprofileHb().getContactPhone();
		}
		return phone;
	}
	
	public String getEmail(){
		if(eMail==null){
			eMail=Contants.getUserInfo.getUserprofileHb().getContactMail();
		}
		return eMail;
	}
	
	OnConfirmEnableListener mOnConfirmEnableListener;
	public interface OnConfirmEnableListener{
		public void onChennelSelected(boolean islected);
	}
	
	public void setOnConfirmEnableListener(OnConfirmEnableListener mOnConfirmEnableListener){
		this.mOnConfirmEnableListener=mOnConfirmEnableListener;
	}
	
	public boolean onCheckForConfirm(){
		boolean result=true;
		if(request_by_mail_radio.isChecked()){
			if(TextUtils.isEmpty(request_channel_email_email.getText())){
			}else{
				result=false;
			}
		}else if(request_by_Phone_radio.isChecked()){
			if(TextUtils.isEmpty(request_details_phone.getText())){
			}else if(TextUtils.isEmpty(request_details_phone_hours_et.getText())){
			}else{
				result=false;
			}
		}else if(request_branch_radio.isChecked()){
			if(request_details_branch_email.isChecked()){
				if(TextUtils.isEmpty(request_details_branch_email_input.getText())){
				}else{
					result=false;
				}
			}else if(request_details_branch_phone.isChecked()){
				if(TextUtils.isEmpty(request_details_branch_phone_input.getText())){
				}else if(TextUtils.isEmpty(request_details_branch_date_input_et.getText())){
				}else if(TextUtils.isEmpty(request_details_branch_hours_et.getText())){
				}else{
					result=false;
				}
			}
		}
		
		if(mOnConfirmEnableListener!=null){
			mOnConfirmEnableListener.onChennelSelected(!result);
		}
		return result;
	}
	
}
