package com.accenture.mbank.view.payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.accenture.mbank.NewPayee;
import com.accenture.mbank.NewPayee.BillHolder;
import com.accenture.mbank.R;
import com.accenture.mbank.capture.CaptureActivity;
import com.accenture.mbank.logic.GetPaymentTemplatesJson;
import com.accenture.mbank.model.GetPaymentTempLatesResponseModel;
import com.accenture.mbank.model.PendingTransferModel;
import com.accenture.mbank.model.RecentTransferModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.ViewUtil;

public class PaymentsManagerBill implements OnClickListener {
	public static final int CaptureRequestCode=1001;
	
	private ViewGroup layout;
	private EditText bill_holder_et;
	private ImageButton bill_holder_ibtn;
	private Button capture_code_btn;
	private EditText account_number_et;
	private EditText amount_doller_et,amount_et2;
	private EditText payable_to_et;
	private EditText bill_number_et;
	private RadioGroup type_rg;
	private Context context;
	
	public static final String _674="674";
	public static final String _896="896";
	
	public PaymentsManagerBill(Context context){
		this.context=context;
		LayoutInflater layoutInflater=LayoutInflater.from(context);
		layout=(ViewGroup) layoutInflater.inflate(R.layout.new_payee_bill, null);
		initView();
	}
	
	public PaymentsManagerBill(Context context,ViewGroup layout){
		this.context=context;
		this.layout=layout;
		initView();
	}
	
	private void initView(){
		bill_holder_et=(EditText) layout.findViewById(R.id.bill_holder_et);
		bill_holder_ibtn=(ImageButton) layout.findViewById(R.id.bill_holder_ibtn);
		bill_holder_ibtn.setOnClickListener(this);
		capture_code_btn=(Button) layout.findViewById(R.id.capture_code_btn);
		capture_code_btn.setOnClickListener(this);
		account_number_et=(EditText) layout.findViewById(R.id.account_number_et);
		amount_doller_et=(EditText) layout.findViewById(R.id.amount_doller_et);
		amount_et2=(EditText) layout.findViewById(R.id.amount_et2);
		payable_to_et=(EditText) layout.findViewById(R.id.payable_to_et);
		bill_number_et=(EditText) layout.findViewById(R.id.bill_number_et);
		type_rg=(RadioGroup) layout.findViewById(R.id.type_rg);
		
		amount_doller_et.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				amount_et2.setFocusable(true);
				amount_et2.requestFocusFromTouch();
				return true;
			}
		});
	}
	
	public void setVisibility(int visibility){
		layout.setVisibility(visibility);
	}
	
	private void loadData(){
		String dataStr=GetPaymentTemplatesJson.GetPaymentTemplatesReportProtocal(Contants.publicModel);
		GetPaymentTempLatesResponseModel mGetPaymentTempLatesResponseModel =GetPaymentTemplatesJson.ParseGetPaymentTempLatesResponse(dataStr);
	}
	
	public ViewGroup getLayout(){
		return layout;
	}
	
	public void setBillHolderName(String string){
		bill_holder_et.setText(string);
	}
	
	public String getBillHolderName(){
		return bill_holder_et.getText().toString().trim();
	}
	
	public void setBillNumber(String string){
		bill_number_et.setText(string);
	}
	
	public String getBillNumber(){
		return bill_number_et.getText().toString().trim();
	}
	
	public void setAmount(double amount){
		try {
			int m1=(int) Math.floor(amount);
			amount_doller_et.setText(Integer.toString(m1));//Utils.formatMoney(m1, "", true, true, false, false, true));
			m1=(int) (Math.floor(amount*100)-m1*100);
			amount_et2.setText(m1<10?"0"+m1:Integer.toString(m1));
//            NumberFormat format =NumberFormat.getInstance(Locale.ITALY);
//            format.setMinimumFractionDigits(2);
//            format.setMaximumFractionDigits(2);
//            bill_holder_et.setText(format.format(amount));
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void setAmount(String str){
		double amount =0;
        try {
        	str = str.replace(".", "");
        	str = str.replace(',', '.');
            amount = Double.parseDouble(str);
//            amount += 0.001;
//            NumberFormat format = NumberFormat.getInstance();
//            format.setCurrency(Currency.getInstance("EUR"));
//            NumberFormat format =NumberFormat.getInstance(Locale.ITALY);
//            format.setMinimumFractionDigits(2);
//            format.setMaximumFractionDigits(2);
//            str = format.format(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAmount(amount);
//		amount_doller_et.setText(str);
	}
	
	public double getAmount(){
//	
		String str=amount_doller_et.getText().toString().trim()+"."+amount_et2.getText().toString().trim();
//		str = str.replace(".", "");
//        str = str.replace(',', '.');
        double amount=0;
        try {
            amount = Double.parseDouble(str);
//            amount += 0.001;
//            NumberFormat format = NumberFormat.getInstance();
//            format.setCurrency(Currency.getInstance("EUR"));
//            NumberFormat format =NumberFormat.getInstance(Locale.ITALY);
//            format.setMinimumFractionDigits(2);
//            format.setMaximumFractionDigits(2);
//            str = format.format(amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return amount;
	}
	
	public String getAccountNumber(){
		return account_number_et.getText().toString().trim();
	}
	
	public void setAccountNumber(String number){
		account_number_et.setText(number);
	}
	
	public String getPayableTo(){
		return payable_to_et.getText().toString().trim();
	}
	
	public void setType(String type){
		if(_674.equals(type)){
			type_rg.check(R.id._674_rbtn);
		}else if(_896.equals(type)){
			type_rg.check(R.id._896_rbtn);
		}else{
			type_rg.check(-1);
		}
	}

	
	public String getType(){
		String result=null;
		switch(type_rg.getCheckedRadioButtonId()){
			case R.id._674_rbtn:
				result=_674;
				break;
			case R.id._896_rbtn:
				result=_896;
				break;
		}
		return result;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bill_holder_ibtn:
			NewPayee.start((Activity) context, NewPayee.PRECOMPILED_BILL_HOLDER, NewPayee.PRECOMPILED_BILL_HOLDER,null);
			break;
		case R.id.capture_code_btn:
			ViewUtil.goToViewForResult((Activity) context, CaptureActivity.class,R.id.search,CaptureRequestCode);
// 	    	ViewUtil.goToViewByArgu(context, CaptureActivity.class, String.valueOf(R.id.search));
			break;
		}
		
	}
	 public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(requestCode==CaptureRequestCode){
			 String scaString=data.getStringExtra(CaptureActivity.SCANID);
			 String bill_number="";
			 String cc_number="";
			 String amount=null;
			 String type=null;
			 if(TextUtils.isEmpty(scaString)){
				 
			 }else if(scaString.length()==52){
					 bill_number=scaString.substring(2, 21);
					 cc_number=scaString.substring(23, 35);
					 amount=scaString.substring(37, 46)+","+scaString.substring(46, 48);
					 type=scaString.substring(49, 52);
			 }else{
				 int index=0;
				 int length=2;
				 int end=index+length;
				 try{
					 length=Integer.parseInt(scaString.substring(index,end));
					 index=end;
					 end=index+length;
					 bill_number=scaString.substring(index,end);
					 index=end;
					 end=index+2;
					 length=Integer.parseInt(scaString.substring(index,end));
					 index=end;
					 end=index+length;
					 cc_number=scaString.substring(index,end);
					 index=end;
					 end=index+2;
					 length=Integer.parseInt(scaString.substring(index,end));
					 index=end;
					 end=index+length;
					 amount=scaString.substring(index,end-2)+","+scaString.substring(end-2, end);
					 index=end;
					 end=index+1;
					 length=Integer.parseInt(scaString.substring(index,end));
					 index=end;
					 end=index+length;
					 type=scaString.substring(index,end);
				 }catch (Exception e) {
					e.printStackTrace();// TODO: handle exception
				}
			 }
			 setAccountNumber(cc_number);
			 setBillNumber(bill_number);
			 setAmount(amount);
			 setType(type);
			 return true;
		 }else if(requestCode==NewPayee.PRECOMPILED_BILL_HOLDER){
			 mBillHolder=(BillHolder) data.getSerializableExtra(NewPayee.OBJ_DATA);
			 setBillHolderName(mBillHolder.holderName);
			 return true;
		 }
		 
		 return false;
	 }
	 
	 public BillHolder getHolder(){
		 return mBillHolder;
	 }
	 NewPayee.BillHolder mBillHolder;


		public void reCover(Object data) {
			if(data instanceof PendingTransferModel){
				PendingTransferModel mPendingTransferModel=(PendingTransferModel) data;
			setBillHolderName(mPendingTransferModel.getHolderName());
			setAccountNumber(mPendingTransferModel.getPostalAccount());
			setAmount(mPendingTransferModel.getAmount());
			setBillNumber(mPendingTransferModel.getBillNumber());
//			setDueTo();
			setType(mPendingTransferModel.getBillType());
			}else{
				RecentTransferModel recentTransferModel = (RecentTransferModel) data;
				setBillHolderName(recentTransferModel.getHolderName());
				setAccountNumber(recentTransferModel.getPostalAccount());
				setAmount(recentTransferModel.getAmount());
//				setBillNumber(recentTransferModel.getBillNumber());
//				setDueTo();
				setType(recentTransferModel.getBillType());
			}
			
		}
}
