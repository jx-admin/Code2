
package com.accenture.mbank;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.accenture.mbank.logic.GetAccountByPhoneNumberJson;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.GetAccountByPhoneNumberModel;
import com.accenture.mbank.model.GetCardsResponseModel;
import com.accenture.mbank.model.InfoCardsModel;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.util.AvailableOperator;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.view.payment.PaymentsManager;

/**
 * @author junxu.wang
 * 
 *         <pre>
 * If the requested operation is "card top up", the app must displays
 * the fields:
 *   --Enter beneficiary name
 *   --Enter the card number
 * and the check box "add to your payee's list"
 */
public class NewPayee extends BaseActivity implements OnClickListener {
    private static final String TAG = "NewPayee";

    public static final String FLAG = "flag", REQESTCODE = "requestCode";

    public static final String PAYEE = "payee";

    public static final int NEW_TRANSFER_PAYEE = 1, NEW_SIM_PAYEE = 2, NEW_CARD_PAYEE = 3,NEW_TRANSFER_PAYEE_BY_PHONE=4,PRECOMPILED_BILL_HOLDER=5,BLANK_BILL_HOLDER=6,MAV_RAV_HOLDER=7;
    
    public static final int ONRESULT_PICKUP_PHONE=1;

    private int requestCode;

    private int flag;

    public static final String NAME = "name", NUMBER = "number", OPERATOR = "operator",
            SELECTED = "add",ACCOUNT_CODE="accountCode",VERIFYCARD_STATE="isVerifyCard",INFOCARDSMODEL="tmp_InfoCardsModel",
            OBJ_DATA="obj_data";

    private Spinner payment_type_sp;

    private EditText name_et;

    private EditText number_et;

    private EditText operator_et;

    private CheckBox save_cb;
    
    private Button back_btn;
    
    private String accountCode;
    
    private int tmp_isVerifyCard ;
    
    private InfoCardsModel tmp_InfoCardsModel;
    
    public static void start(Activity activity, int requestCode, int flag, Serializable payee) {
        start(activity, requestCode, flag, null,payee);
    }
    public static void start(Activity activity, int requestCode, int flag,String accountCode, Serializable payee) {
        Intent i = new Intent(activity, NewPayee.class);
        i.putExtra(ACCOUNT_CODE, accountCode);
        i.putExtra(FLAG, flag);
        i.putExtra(REQESTCODE, requestCode);
        if (payee != null)
            i.putExtra(PAYEE, payee);
        activity.startActivityForResult(i, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent i = getIntent();
        flag = i.getIntExtra(FLAG, NEW_TRANSFER_PAYEE);
        requestCode = i.getIntExtra(REQESTCODE, RESULT_OK);
        Serializable payee = i.getSerializableExtra(PAYEE);
        switch(flag){
        case NEW_TRANSFER_PAYEE:
            setContentView(R.layout.new_payee_layout);
            name_et = (EditText)findViewById(R.id.new_payee_name_et);
            number_et = (EditText)findViewById(R.id.new_payee_number_et);
            operator_et = (EditText)findViewById(R.id.new_payee_mobile_operator_et);
            if (payee != null && payee instanceof BankRecipient) {
                BankRecipient bankRecipient = (BankRecipient)payee;
                name_et.setText(bankRecipient.getName());
                number_et.setText(bankRecipient.getIbanCode());
                operator_et.setText(bankRecipient.getBic());
            }
        break;
        case NEW_TRANSFER_PAYEE_BY_PHONE: {
            setContentView(R.layout.new_payee_layout_by_phone);
            number_et=(EditText) findViewById(R.id.phone_et);
            findViewById(R.id.pick_phone_ibtn).setOnClickListener(this);
//            name_et = (EditText)findViewById(R.id.new_payee_name_et);
//            number_et = (EditText)findViewById(R.id.new_payee_number_et);
//            operator_et = (EditText)findViewById(R.id.new_payee_mobile_operator_et);
//            if (payee != null && payee instanceof BankRecipient) {
//                BankRecipient bankRecipient = (BankRecipient)payee;
//                name_et.setText(bankRecipient.getName());
//                number_et.setText(bankRecipient.getIbanCode());
//                operator_et.setText(bankRecipient.getBic());
//            }
        } 
        break;
        case NEW_CARD_PAYEE: {
            setContentView(R.layout.new_payee_layout_card);
            back_btn=(Button)findViewById(R.id.back_btn);
            back_btn.setOnClickListener(this);
            back_btn.setVisibility(View.VISIBLE);
            name_et = (EditText)findViewById(R.id.new_payee_name_et);
            number_et = (EditText)findViewById(R.id.new_payee_number_et);
            if (payee != null && payee instanceof CardRecipient) {
                CardRecipient cardRecipient = (CardRecipient)payee;
                name_et.setText(cardRecipient.getName());
                number_et.setText(cardRecipient.getCardNumber());
            }
            accountCode=i.getStringExtra(ACCOUNT_CODE);
            ((Button)findViewById(R.id.proceed_btn)).setText(R.string.verify_proceed);
        } 
        break;
        case NEW_SIM_PAYEE: {
            setContentView(R.layout.new_payee_layout_sim);
            name_et = (EditText)findViewById(R.id.new_payee_name_et);
            number_et = (EditText)findViewById(R.id.new_payee_number_et);
            payment_type_sp = (Spinner)findViewById(R.id.new_payee_mobile_operator_sp);
            ArrayAdapter<String> type_payment_ad = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, AvailableOperator.operatorNames);
            type_payment_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            payment_type_sp.setAdapter(type_payment_ad);

            if (payee != null && payee instanceof PhoneRecipient) {
                PhoneRecipient phoneRecipient = (PhoneRecipient)payee;
                name_et.setText(phoneRecipient.getName());
                number_et.setText(phoneRecipient.getPhoneNumber());
                for (int j = 0; j <= AvailableOperator.operatorValues.length; j++) {
                    if(AvailableOperator.operatorValues[j].equals(phoneRecipient.getProvider())){
                    payment_type_sp.setSelection(j);
                    break;
                    }
                }
            }
        }
        break;
        case PRECOMPILED_BILL_HOLDER:
        case BLANK_BILL_HOLDER:
        case MAV_RAV_HOLDER:
        {
            setContentView(R.layout.new_payee_bill);
            ((TextView)findViewById(R.id.title_tv)).setText(R.string.new_bill_holder);
        }
        break;
        }
        findViewById(R.id.new_payee_close_btn).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.proceed_btn).setOnClickListener(this);
        save_cb = (CheckBox)findViewById(R.id.new_payee_save_cb);
        tmp_isVerifyCard=PaymentsManager.VERIFYCARD_INITAL;
    }
    protected void done(){
    	done(null);
    }
    protected void done(Serializable data) {

        String name = null, number = null, operator = null;
        boolean needAdd=false;
        Intent intent = new Intent();
        if(data!=null){
        	intent.putExtra(OBJ_DATA, data);
        }
        switch(flag){
        case NEW_TRANSFER_PAYEE:
            name = name_et.getText().toString();
            number = number_et.getText().toString();
            needAdd = save_cb.isChecked();
            operator = operator_et.getText().toString();
        break;
        case NEW_CARD_PAYEE:
            name = name_et.getText().toString();
            number = number_et.getText().toString();
            operator = null;
            needAdd = save_cb.isChecked();
        break;
        case NEW_SIM_PAYEE:
        	 name = name_et.getText().toString();
             number = number_et.getText().toString();
             needAdd = save_cb.isChecked();
        	operator = AvailableOperator.operatorValues[payment_type_sp.getSelectedItemPosition()];
        break;
        case PRECOMPILED_BILL_HOLDER:
        case BLANK_BILL_HOLDER:
        case MAV_RAV_HOLDER:
        	BillHolder mBillHolder=new BillHolder();
        	mBillHolder.holderName=((EditText)findViewById(R.id.holders_name_et)).getText().toString();
        	mBillHolder.address=((EditText)findViewById(R.id.address_1st_line_et)).getText().toString();
        	mBillHolder.city=((EditText)findViewById(R.id.city_et)).getText().toString();
        	mBillHolder.zipCode=((EditText)findViewById(R.id.zip_code_et)).getText().toString();
        	mBillHolder.state=((EditText)findViewById(R.id.state_ev)).getText().toString();
            intent.putExtra(OBJ_DATA, mBillHolder);
        break;
        }
        intent.putExtra(VERIFYCARD_STATE, tmp_isVerifyCard);
        intent.putExtra(INFOCARDSMODEL, tmp_InfoCardsModel);
        intent.putExtra(SELECTED, needAdd);
        intent.putExtra(NAME, name);
        intent.putExtra(NUMBER, number);
        setResult(requestCode, intent);
        intent.putExtra(OPERATOR, operator);
        Log.d(TAG, "result(" + name + " , " + number + " , " + operator + " " + needAdd);
        finish();
    }
    
    /**a check on field of the form
     * @return
     */
    private boolean checkField(int paymentType){
    	boolean result=false;
        switch(paymentType){
    	case NEW_TRANSFER_PAYEE:
    		String name = name_et.getText().toString();
        	String number = number_et.getText().toString();
    		if(TextUtils.isEmpty(name)){
    			displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.missing_name)).show();
    		}else
    		if(TextUtils.isEmpty(number)){
    			displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.missing_iban)).show();
    		}else{
    			result=true;
    		}
    		break;
    		case NEW_TRANSFER_PAYEE_BY_PHONE:
    			number = number_et.getText().toString();
        		if(TextUtils.isEmpty(number)){
        			displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.missing_number)).show();
        		}else{
//        			result=true;
                    verifyCard(this,accountCode,number);
        		}
        		break;
    	case NEW_CARD_PAYEE:
    		name = name_et.getText().toString();
    		number = number_et.getText().toString();
    		if(TextUtils.isEmpty(name)){
    			displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.missing_beneficiary_name)).show();
    		}else if(TextUtils.isEmpty(number)){
    			displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.missing_card_number)).show();
    		}else{
    			final CardRecipient cr=new CardRecipient();
                cr.setCardNumber(number_et.getText().toString());
                cr.setName(name_et.getText().toString());
                verifyCard(this,accountCode,cr);
//    			result=true;
    		}
    		break;
    	case NEW_SIM_PAYEE:
    		name = name_et.getText().toString();
            boolean needAdd = save_cb.isChecked();
    		String operator = operator_et.getText().toString();
    		number = number_et.getText().toString();
    		if(TextUtils.isEmpty(number)){
    			displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.missing_number)).show();
    		}else if(payment_type_sp.getSelectedItemPosition()<0){
    			displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.missing_operator)).show();
    		}else if(needAdd&&TextUtils.isEmpty(name)){
    			displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.missing_name)).show();
    		}else{
    			result=true;
    		}
    		break;
    	default:
    		result=true;
    		break;
    	}
    	
    	return result;
    }
    
    public Object getAccountByPhoneNumber(Context context, String accountCode, String number){
    	return null;
    }
    
    private void verifyCard(final Context context,final String accountCode,final Object data){
        if(downloading_pd==null){
            downloading_pd= new ProgressDialog(context);
            downloading_pd.setTitle(R.string.downloading);
        }
        downloading_pd.show();
        new Thread(){
        	public void run(){
        		if(flag==NEW_CARD_PAYEE){
        			GetCardsResponseModel  mGetCardsResponseModel =PaymentsManager.vertifyCard(NewPayee.this, accountCode, (CardRecipient)data);
        			Message msg=mHandler.obtainMessage(NEW_CARD_PAYEE);
        			msg.obj=mGetCardsResponseModel;
        			mHandler.sendMessage(msg);
        		}else if(flag==NEW_TRANSFER_PAYEE_BY_PHONE){
        			String resultData=GetAccountByPhoneNumberJson.getAccountByPhoneNumberReportProtocal(Contants.publicModel, (String)data);
        			HttpConnector httpConnector = new HttpConnector();
        			resultData= httpConnector
        	                .requestByHttpPost(Contants.mobile_url, resultData, NewPayee.this);
        			GetAccountByPhoneNumberModel mGetAccountByPhoneNumberModel=GetAccountByPhoneNumberJson.parseGetAccountByPhoneNumberResponse(resultData);
        			if(mGetAccountByPhoneNumberModel!=null&&mGetAccountByPhoneNumberModel.getAccountList()!=null&&mGetAccountByPhoneNumberModel.getAccountList().size()>0){
        				mGetAccountByPhoneNumberModel.getAccountList().get(0).setPhoneNumber((String)data);
        			}
        			Message msg=mHandler.obtainMessage(NEW_TRANSFER_PAYEE_BY_PHONE);
        			msg.obj=mGetAccountByPhoneNumberModel;
        			mHandler.sendMessage(msg);
        		}
        	}
        }.start();
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	switch(msg.what){
        	case NEW_CARD_PAYEE:
                tmp_isVerifyCard=PaymentsManager.VERIFYCARD_INITAL;
        		GetCardsResponseModel  mGetCardsResponseModel =(GetCardsResponseModel) msg.obj;
        		List<InfoCardsModel> list=null;
    			if (mGetCardsResponseModel!=null){
    				list = mGetCardsResponseModel.getInfoCardListModel();
    			}
    			if(list != null && list.size() > 0) {
    				tmp_InfoCardsModel = list.get(0);
    				tmp_isVerifyCard = PaymentsManager.VERIFYCARD_SUCCESS;
//                    displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.card_verified)).show();
                    done();
    			} else {
    				tmp_isVerifyCard =PaymentsManager.VERIFYCARD_FAILED ;
                    displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.no_card_found)).show();
    			}
        		break;
        	case NEW_TRANSFER_PAYEE_BY_PHONE:
        		GetAccountByPhoneNumberModel mGetAccountByPhoneNumberModel=(GetAccountByPhoneNumberModel) msg.obj;
        		if(mGetAccountByPhoneNumberModel==null||mGetAccountByPhoneNumberModel.responsePublicModel==null){
                    displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.connection_error)).show();
        		}else if(mGetAccountByPhoneNumberModel.getAccountList()==null||mGetAccountByPhoneNumberModel.getAccountList().size()<=0){
                    displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.not_valid_phone_number)).show();
        		}else if(mGetAccountByPhoneNumberModel.responsePublicModel.isSuccess()){
        			done(mGetAccountByPhoneNumberModel.getAccountList().get(0));
        		}else if("91220".equals(mGetAccountByPhoneNumberModel.responsePublicModel.eventManagement.getErrorCode())
        				||"91221".equals(mGetAccountByPhoneNumberModel.responsePublicModel.eventManagement.getErrorCode())
        				||"91222".equals(mGetAccountByPhoneNumberModel.responsePublicModel.eventManagement.getErrorCode())){
        			displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.not_valid_phone_number)).show();
        		}else{
        			displayRecapDialog(NewPayee.this,mGetAccountByPhoneNumberModel.responsePublicModel.eventManagement.getErrorDescription()).show();
        		}
        		break;
        	}
            super.handleMessage(msg);
        	if(downloading_pd!=null){
        		downloading_pd.dismiss();
        	}
        }
    };

    @Override
    public void onClick(View v) {
    	switch(v.getId()){
//            final CardRecipient cr=new CardRecipient();
//            cr.setCardNumber(number_et.getText().toString());
//            cr.setName(name_et.getText().toString());
//            verifyCard(this,accountCode,cr);
//    		break;
    	case R.id.new_payee_close_btn:
    	case R.id.proceed_btn:
        	if(checkField(flag)){
        		done();
        	}
    		break;
    	case R.id.back_btn:
    		exit();
    		break;
    	case R.id.pick_phone_ibtn:
        	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            NewPayee.this.startActivityForResult(intent, ONRESULT_PICKUP_PHONE);
    		break;
    	}
    }
    
    @Override
	public void onBackPressed() {
		super.onBackPressed();
		exit();
	}
	public void exit(){
    	finish();
    }
    
    protected AlertDialog displayRecapDialog(Context context,String msg){
        AlertDialog.Builder builder = new Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.message_dialog_layout,
                null);
        builder.setView(linearLahyout);
        final AlertDialog alertDialog = builder.create();

        Button imageButton = (Button)linearLahyout.findViewById(R.id.ok_btn);
        TextView text = (TextView)linearLahyout.findViewById(R.id.message_text);
        text.setText(msg);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // paymentCofirmLayout.onButtonClick();
                alertDialog.dismiss();
            }
        });
        setDialogWidth(alertDialog, context);
        return alertDialog;
    }

    private static void setDialogWidth(final AlertDialog alertDialog, Context context) {
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.width = (int)(display.getWidth()) * 3 / 4; // 设置宽度
        lp.height = (int)(display.getWidth()) * 2 / 4;
        alertDialog.getWindow().setAttributes(lp);
    }
    
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                Cursor c = null;
                try {
                    c = getContentResolver().query(uri, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER},
                            null, null, null);
                    if (c != null && c.moveToFirst()) {
//                        int index=c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        number_et.setText( c.getString(0));
                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    } 
    
    ProgressDialog downloading_pd;
    
    public static class BillHolder implements Serializable{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String holderName,address,city,zipCode,state;
    }

}
