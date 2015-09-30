
package com.accenture.mbank;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.view.BankRollContainer;
import com.accenture.mbank.view.BankRollContainerManager;
import com.accenture.mbank.view.BankRollView;
import com.accenture.mbank.view.ContactCustomServiceLayout;
import com.accenture.mbank.view.ContactNewRequestLayout;
import com.accenture.mbank.view.ContactTheifLossLayout;
import com.accenture.mbank.view.ItemExpander;
import com.accenture.mbank.view.MapLayout;
import com.accenture.mbank.view.protocol.ShowAble;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class ContactSearchActivity extends BaseMapActivity implements OnClickListener {

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

    BankRollContainer bankRollContainer;
    
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyTracker.getInstance().setContext(this);
		EasyTracker.getInstance().activityStart(this); // Add this method.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contact_search);

        init();
        if (BaseActivity.isLogin) {

            showHelp();
            menu.setVisibility(View.VISIBLE);
        } else {
            hideHelp();
            hideMenu();
        }

    }

    private void hideMenu() {
        // TODO Auto-generated method stub
        menu.setVisibility(View.INVISIBLE);
    }

    public void showHelp() {

        if (help != null)
            help.setVisibility(View.VISIBLE);
    }

    public void hideHelp() {

        if (help != null)
            help.setVisibility(View.INVISIBLE);
    }

    BankRollContainerManager bankRollContainerManager;
    TextView webSitTextView;
    /**
     * 
     */
    void init() {
        menu = (LinearLayout)findViewById(R.id.menu);
        help = (ImageButton)findViewById(R.id.help_btn);
        help.setOnClickListener(this);
        menu.setVisibility(View.VISIBLE);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        bankRollContainer = (BankRollContainer)findViewById(R.id.roll_group);
        bankRollContainer.init();
        BankRollView contactusRollView = (BankRollView)layoutInflater.inflate(
                R.layout.bank_roll_view, null);
        contactusRollView.init();
       
        contactusRollView.setCloseImage(R.drawable.sphere_contact_us);
        contactusRollView.setShowImage(R.drawable.show_selector);
        final ViewGroup layout = (ViewGroup)layoutInflater.inflate(R.layout.contact_us_layout, null);
        contactusRollView.setContent(layout);
        bankRollContainer.addShowAble(contactusRollView);
        contactusRollView.setPadding(0, contactusRollView.height_bottom, 0, 0);

        ItemExpander itemExpander = (ItemExpander)layout.findViewById(R.id.customer_service);
        ContactCustomServiceLayout contactCustomServiceLayout = (ContactCustomServiceLayout)layoutInflater
                .inflate(R.layout.contact_customer_service, null);
        webSitTextView = (TextView)contactCustomServiceLayout.findViewById(R.id.website);
        webSitTextView.setText(Html.fromHtml("<u>www.mybank.it</u>"));
        webSitTextView.setTextColor(Color.BLUE);
        webSitTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
               
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://ams-msite.mobility-managed.com/cp/gtw/login/start");   
                intent.setData(content_url);
                startActivity(intent);
            }
        });
        
        
        itemExpander.setExpandedContainer(contactCustomServiceLayout);

        itemExpander.setTitle("customer service");
        // itemExpander.setTitle("CUSTOMER SERVICE");
        itemExpander.setTypeface(Typeface.DEFAULT);
        itemExpander.onChange(" ");
        itemExpander.setExpandable(true);

        itemExpander = (ItemExpander)layout.findViewById(R.id.thief_and_loss);
        ContactTheifLossLayout contactTheifLossLayout = (ContactTheifLossLayout)layoutInflater
                .inflate(R.layout.contact_theif_loss, null);
        itemExpander.setExpandedContainer(contactTheifLossLayout);
        itemExpander.setExpandable(true);
        itemExpander.setTitle("theft and loss");
        // itemExpander.setTitle("THEFT");
        itemExpander.setTypeface(Typeface.DEFAULT);
        itemExpander.onChange(" ");
        if (BaseActivity.isLogin) {
            /*
             * menu is only visible after user has logged in
             */
            menu.setVisibility(View.VISIBLE);
            itemExpander = (ItemExpander)layout.findViewById(R.id.new_request);

            ContactNewRequestLayout contactNewRequestLayout = (ContactNewRequestLayout)layoutInflater
                    .inflate(R.layout.contact_new_request, null);
            itemExpander.setExpandedContainer(contactNewRequestLayout);
            itemExpander.setExpandable(true);
            itemExpander.setResultVisible(false);
//            itemExpander.setTitle("NEW REQUEST");
            itemExpander.setTitle("new request");
            itemExpander.setTypeface(Typeface.DEFAULT);
            itemExpander.onChange(" ");
            itemExpander.setVisibility(View.VISIBLE);
        }

        final MapLayout maplayout = (MapLayout)layoutInflater.inflate(R.layout.search_width_map, null);
        maplayout.init();

        maplayout.parentScrollView = bankRollContainer;
        searchRollView = (BankRollView)layoutInflater.inflate(R.layout.bank_roll_view, null);

        searchRollView.init();
        searchRollView.setCloseImage(R.drawable.sphere_search_branch);
        searchRollView.setShowImage(R.drawable.show_selector);
        bankRollContainer.addShowAble(searchRollView);
        searchRollView.setContent(maplayout);

        bankRollContainerManager = new BankRollContainerManager() {

            @Override
            public void createUiByData() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onShow(ShowAble showAble) {
            mGaInstance = GoogleAnalytics.getInstance(getContext());
           	  mGaTracker1 = mGaInstance.getTracker("UA-42551791-1");
              if (((BankRollView)showAble).getContent() == maplayout){
            	  
                       mGaTracker1.sendView("view.branch"); 
                       }
              	else if(((BankRollView)showAble).getContent() == layout){   
                       mGaTracker1.sendView("view.contact.us");
              	}
                // TODO Auto-generated method stub
                bankRollContainer.setMaxScrollY(searchRollView.getTop());

            }

            @Override
            public void onShow() {
                // TODO Auto-generated method stub

            }

        };
        bankRollContainerManager.setRollContainer(bankRollContainer);
        bankRollContainer.setBankRollContainerManager(bankRollContainerManager);
        initMenu();

    }

    BankRollView searchRollView;

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
        if (v == help) {
            Intent intent = new Intent(ContactSearchActivity.this, HelpListActivity.class);
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
        	
            Intent intent = new Intent(ContactSearchActivity.this, ContactSearchActivity.class);
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
