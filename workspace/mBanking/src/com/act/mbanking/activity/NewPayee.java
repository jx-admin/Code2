
package com.act.mbanking.activity;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.act.mbanking.R;
import com.act.mbanking.bean.BankRecipient;
import com.act.mbanking.bean.CardRecipient;
import com.act.mbanking.bean.GetCardsResponseModel;
import com.act.mbanking.bean.InfoCardsModel;
import com.act.mbanking.bean.PhoneRecipient;
import com.act.mbanking.manager.PaymentsManager;
import com.act.mbanking.utils.AvailableOperator;

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

    public static final int NEW_TRANSFER_PAYEE = 1, NEW_SIM_PAYEE = 2, NEW_CARD_PAYEE = 3;

    private int requestCode;

    private int flag;

    public static final String NAME = "name", NUMBER = "number", OPERATOR = "operator",
            SELECTED = "add",ACCOUNT_CODE="accountCode",VERIFYCARD_STATE="isVerifyCard",INFOCARDSMODEL="tmp_InfoCardsModel";

    private Spinner payment_type_sp;

    private EditText name_et;

    private EditText number_et;

    private EditText operator_et;

    private CheckBox save_cb;
    
    private Button verify_btn;
    
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
        if (flag == NEW_TRANSFER_PAYEE) {
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
        } else if (flag == NEW_CARD_PAYEE) {
            setContentView(R.layout.new_payee_layout_card);
            back_btn=(Button)findViewById(R.id.back_btn);
            back_btn.setOnClickListener(this);
            back_btn.setVisibility(View.VISIBLE);
            name_et = (EditText)findViewById(R.id.new_payee_name_et);
            number_et = (EditText)findViewById(R.id.new_payee_number_et);
            verify_btn=(Button)findViewById(R.id.verify_btn);
            verify_btn.setVisibility(View.VISIBLE);
            verify_btn.setOnClickListener(this);
            if (payee != null && payee instanceof CardRecipient) {
                CardRecipient cardRecipient = (CardRecipient)payee;
                name_et.setText(cardRecipient.getName());
                number_et.setText(cardRecipient.getCardNumber());
            }
            accountCode=i.getStringExtra(ACCOUNT_CODE);
        } else if (flag == NEW_SIM_PAYEE) {
            setContentView(R.layout.new_sim_payee_layout);
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
        findViewById(R.id.new_payee_close_btn).setOnClickListener(this);
        save_cb = (CheckBox)findViewById(R.id.new_payee_save_cb);
        tmp_isVerifyCard=PaymentsManager.VERIFYCARD_INITAL;
    }

    protected void exit() {
        String name = name_et.getText().toString(), number = number_et.getText().toString(), operator = null;
        boolean needAdd = save_cb.isChecked();

        if (flag == NEW_TRANSFER_PAYEE) {
            operator = operator_et.getText().toString();
        } else if (flag == NEW_CARD_PAYEE) {
            if(tmp_isVerifyCard==PaymentsManager.VERIFYCARD_INITAL){
                displayRecapDialog(this,getString(R.string.verify_the_data_card)).show();
                return;
            }else if(tmp_isVerifyCard==PaymentsManager.VERIFYCARD_FAILED){
                displayRecapDialog(this,getString(R.string.verify_the_data_card)).show();
                return;
            }
        } else if (flag == NEW_SIM_PAYEE) {
            operator = AvailableOperator.operatorValues[payment_type_sp.getSelectedItemPosition()];
        }
        Intent intent = new Intent();
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
    
    private void verifyCard(final Context context,final String accountCode,final CardRecipient cr){
        if(downloading_pd==null){
            downloading_pd= new ProgressDialog(context);
            downloading_pd.setTitle(R.string.downloading);
        }
        downloading_pd.show();
        new Thread(){
            public void run(){
                GetCardsResponseModel  mGetCardsResponseModel =PaymentsManager.vertifyCard(NewPayee.this, accountCode, cr);
                List<InfoCardsModel> list=null;
                if (mGetCardsResponseModel!=null){
                    list = mGetCardsResponseModel.getInfoCardListModel();
                }
                if(list != null && list.size() > 0) {
                    tmp_InfoCardsModel = list.get(0);
                    tmp_isVerifyCard = PaymentsManager.VERIFYCARD_SUCCESS;
                } else {
                    tmp_isVerifyCard =PaymentsManager.VERIFYCARD_FAILED ;
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(downloading_pd!=null){
                downloading_pd.dismiss();
            }
            if(tmp_isVerifyCard==PaymentsManager.VERIFYCARD_SUCCESS){
                displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.card_verified)).show();
            }else{
                displayRecapDialog(NewPayee.this,NewPayee.this.getString(R.string.no_card_found)).show();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        if(v==verify_btn){
            final CardRecipient cr=new CardRecipient();
            cr.setCardNumber(number_et.getText().toString());
            cr.setName(name_et.getText().toString());
            verifyCard(this,accountCode,cr);
            
        }else if(v.getId()==R.id.new_payee_close_btn){
            exit();
        }else if(v.getId()==R.id.back_btn){
            finish();
        }
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
    
    ProgressDialog downloading_pd;

}
