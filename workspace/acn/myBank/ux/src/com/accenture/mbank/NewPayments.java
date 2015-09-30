
package com.accenture.mbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.DestaccountModel;
import com.accenture.mbank.model.PendingTransferModel;
import com.accenture.mbank.model.RecentTransferModel;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.view.payment.PaymentsManager;

public class NewPayments extends BaseActivity implements OnClickListener {
    
    private static final String TAG="NewPayments";

    private LinearLayout menu;

    private ImageButton help;

    private SlidingDrawer slidMenu;

    private View userInfoBtn;

    private View menu_accountsBtn;

    private View menu_investmentsBtn;

    private View menu_cardsBtn;

    private View menu_loansBtn;

    private View menu_paymentBtn;

    private View menu_contactsBtn;

    private View menu_guideBtn;

    private View menu_log_outBtn;

    private Button title_left_btn;

    PaymentsManager pm;

    /**
     * @see PaymentsManager#setRecover
     */
    public static void startForRecover(Activity activity, int showForm, AccountsModel payer,
            DestaccountModel payee, long time, double amount, int paymentType) {
        Intent intent = new Intent(activity, NewPayments.class);
        intent.putExtra("payer", payer);
        intent.putExtra("showForm", showForm);
        intent.putExtra("payee", payee);
        intent.putExtra("time", time);
        intent.putExtra("amount", amount);
        intent.putExtra("paymentType", paymentType);
        activity.startActivity(intent);
    }

    /**
     * @see PaymentsManager#setRecover
     */
    public static void startForRecover(Activity activity, RecentTransferModel recentTransferModel,AccountsModel accountModel) {
        Intent intent = new Intent(activity, NewPayments.class);
        intent.putExtra("RecentTransferModel", recentTransferModel);
        intent.putExtra("showForm", PaymentsManager.FORM_RECENT_PAYMENT);
        intent.putExtra("payer", accountModel);
        activity.startActivity(intent);
    }

    /**
     * @see PaymentsManager#setRecover
     */
    public static void startForRecover(Activity activity, PendingTransferModel recentTransferModel,
            AccountsModel accountModel) {
        Intent intent = new Intent(activity, NewPayments.class);
        intent.putExtra("PendingTransferModel", recentTransferModel);
        intent.putExtra("showForm", PaymentsManager.FORM_PENDING_PAYMENT);
        intent.putExtra("payer", accountModel);
        activity.startActivity(intent);
//        mainManager.showNewPayment(false, intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_payments_main);

        menu = (LinearLayout)findViewById(R.id.menu);
        help = (ImageButton)findViewById(R.id.help_btn);
        help.setOnClickListener(this);

        findViewById(R.id.back).setVisibility(View.GONE);
        title_left_btn = (Button)findViewById(R.id.back_btn);
        title_left_btn.setOnClickListener(this);
        title_left_btn.setText(R.string.payments);
        title_left_btn.setVisibility(View.VISIBLE);

        initMenu();

        pm = new PaymentsManager(this);
        pm.init();
        Intent intent = getIntent();
        int showForm = intent.getIntExtra("showForm", PaymentsManager.FORM_DEFAULT);
        if (showForm == PaymentsManager.FORM_ACCOUNT) {
            AccountsModel payer = (AccountsModel)intent.getSerializableExtra("payer");
            DestaccountModel payee = (DestaccountModel)intent.getSerializableExtra("payee");
            long time = intent.getLongExtra("time", 0);
            double amount = intent.getDoubleExtra("amount", 0);
            int paymentType = intent.getIntExtra("paymentType", 0);
            pm.setRecover(showForm, payer, payee, time, amount, paymentType);

        } else if (showForm == PaymentsManager.FORM_RECENT_PAYMENT) {
            RecentTransferModel recentTransferModel = (RecentTransferModel)intent
                    .getSerializableExtra("RecentTransferModel");
            AccountsModel payer = (AccountsModel)intent.getSerializableExtra("payer");
            pm.setRecentRecover(showForm,recentTransferModel,payer);
        }else if (showForm == PaymentsManager.FORM_PENDING_PAYMENT) {
        	PendingTransferModel recentTransferModel = (PendingTransferModel)intent
            .getSerializableExtra("PendingTransferModel");
		    AccountsModel payer = (AccountsModel)intent.getSerializableExtra("payer");
		    pm.setRecentRecover(showForm,recentTransferModel,payer);
		}else{
        	pm.setShowForm(PaymentsManager.FORM_DEFAULT);
		}
        pm.onShow(this);
        pm.initData();
    }
    
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG,"key:"+keyCode+" ");
//        if(keyCode==KeyEvent.KEYCODE_BACK){
////            pm.onBackPressed();
//        }
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public void onBackPressed() {
//        pm.onBackPressed();
//        super.onBackPressed();
//    }
    
//    public boolean dispatchKeyEvent(KeyEvent event){
//        Log.d(TAG,"dispatchKeyEvent "+event.getKeyCode());
//        return super.dispatchKeyEvent(event);
//    }


    private void initMenu() {

        userInfoBtn = findViewById(R.id.menu_user_info);
        userInfoBtn.setOnClickListener(this);
        menu_accountsBtn = findViewById(R.id.menu_accounts);
        menu_accountsBtn.setOnClickListener(this);
        menu_investmentsBtn = findViewById(R.id.menu_investments);
        menu_investmentsBtn.setOnClickListener(this);
        menu_cardsBtn = findViewById(R.id.menu_cards);
        menu_cardsBtn.setOnClickListener(this);
        menu_loansBtn = findViewById(R.id.menu_loans);
        menu_loansBtn.setOnClickListener(this);
        menu_paymentBtn = findViewById(R.id.menu_payment);
        menu_paymentBtn.setOnClickListener(this);
        menu_contactsBtn = findViewById(R.id.menu_contacts);
        menu_contactsBtn.setOnClickListener(this);
        menu_guideBtn = findViewById(R.id.menu_guide);
        menu_guideBtn.setOnClickListener(this);
        menu_log_outBtn = findViewById(R.id.menu_log_out);
        menu_log_outBtn.setOnClickListener(this);

        slidMenu = (SlidingDrawer)findViewById(R.id.slid_menu);
    }

    @Override
    public void onClick(View v) {
        MainActivity mainAcitivty = (MainActivity)MainActivity.getContext();
        if (v == title_left_btn) {
            if(!pm.onLeftNavigationButtonClick(v)){
                finish();
            }else{
                return;
            }
        } else if (v == help) {
            Intent intent = new Intent(this, HelpListActivity.class);
            startActivity(intent);
            return;
        } else if (v == userInfoBtn) {
            mainAcitivty.showUserInfo();
            slidMenu.animateClose();
        } else if (v == menu_accountsBtn) {
            mainAcitivty.showTab(MainActivity.ACCOUNTS);
            slidMenu.animateClose();
        } else if (v == menu_cardsBtn) {
            mainAcitivty.showTab(MainActivity.CARDS);
            slidMenu.animateClose();
        } else if (v == menu_contactsBtn) {
            Intent intent = new Intent(this, ContactSearchActivity.class);
            startActivity(intent);
            slidMenu.animateClose();
        } else if (v == menu_guideBtn) {
            Intent intent = new Intent(this, HelpListActivity.class);
            startActivity(intent);
        } else if (v == menu_investmentsBtn) {
            mainAcitivty.showInvestments();
            slidMenu.animateClose();
        } else if (v == menu_loansBtn) {
            mainAcitivty.showLoans();
            slidMenu.animateClose();
        } else if (v == menu_log_outBtn) {
            DialogManager.createMessageExitDialog("Are you sure you want to exit the application?",
                    this).show();
        } else if (v == menu_paymentBtn) {
            mainAcitivty.showTab(MainActivity.PAYMENTS);
            slidMenu.animateClose();
        }
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        pm.onActivityResult(requestCode, resultCode, intent);
    }

}
