
package com.accenture.mbank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;

import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.view.payment.RecentPaymentManager;
import com.google.analytics.tracking.android.EasyTracker;


public class RecentPaymentActivity extends BaseActivity implements OnClickListener {

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
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyTracker.getInstance().activityStart(this); // Add this method.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_recent_payments);
        findViewById(R.id.back).setVisibility(View.GONE);
        title_left_btn = (Button)findViewById(R.id.back_btn);
        title_left_btn.setOnClickListener(this);
        title_left_btn.setText(R.string.payments);
        title_left_btn.setVisibility(View.VISIBLE);

        init();
        
        RecentPaymentManager rpm=new RecentPaymentManager(this);
        rpm.init((ViewGroup) findViewById(R.id.content_layout));
        rpm.onShow(null);
    }

    private void init() {
        menu = (LinearLayout)findViewById(R.id.menu);

        help = (ImageButton)findViewById(R.id.help_btn);
        help.setOnClickListener(this);
        initMenu();
    }

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
            this.finish();
            return;
        } else if (v == help) {
            Intent intent = new Intent(RecentPaymentActivity.this, HelpListActivity.class);
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
            Intent intent = new Intent(RecentPaymentActivity.this, ContactSearchActivity.class);
            startActivity(intent);
            slidMenu.animateClose();
        } else if (v == menu_guideBtn) {
            help.performClick();
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
}
