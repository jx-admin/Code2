
package com.act.mbanking.manager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.act.mbanking.R;

/**
 * com.act.mbanking.manager.view.BankMenu
 * 
 * @author seekting.x.zhang
 */
public class BankMenu extends LinearLayout implements OnClickListener {

    public View aggregated_view, accounts, investments, cards, loans, new_payment, recent_payments,
            contacts, guide, user_info;

    View products, payments, utilities;

    View close_sesssion_btn;

    private OnMenuClick onMenuClick;

    public OnMenuClick getOnMenuClick() {
        return onMenuClick;
    }

    public void setOnMenuClick(OnMenuClick onMenuClick) {
        this.onMenuClick = onMenuClick;
    }

    public BankMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {

        close_sesssion_btn = findViewById(R.id.close_sesssion_btn);
        close_sesssion_btn.setOnClickListener(this);
        initTitle();

        initItem();

    }

    private void initItem() {
        aggregated_view = findViewById(R.id.menu_item_aggregated_view);
        initItem(aggregated_view, R.string.aggregated_view, R.drawable.menu_tab2_selector);
        aggregated_view.setOnClickListener(this);

        accounts = findViewById(R.id.menu_item_accounts);
        initItem(accounts, R.string.accounts, R.drawable.menu_tab2_selector);
        accounts.setOnClickListener(this);

        investments = findViewById(R.id.menu_item_investments);
        initItem(investments, R.string.investments, R.drawable.menu_tab2_selector);
        investments.setOnClickListener(this);

        cards = findViewById(R.id.menu_item_cards);
        initItem(cards, R.string.cards, R.drawable.menu_tab2_selector);
        cards.setOnClickListener(this);

        loans = findViewById(R.id.menu_item_loans);
        initItem(loans, R.string.loans, R.drawable.menu_tab2_selector);
        loans.setOnClickListener(this);

        new_payment = findViewById(R.id.menu_item_new_payment);
        initItem(new_payment, R.string.new_payment, R.drawable.menu_tab1_selector);
        new_payment.setOnClickListener(this);

        recent_payments = findViewById(R.id.menu_item_recent_payments);
        initItem(recent_payments, R.string.recent_payments, R.drawable.menu_tab1_selector);
        recent_payments.setOnClickListener(this);

        contacts = findViewById(R.id.menu_item_contacts);
        initItem(contacts, R.string.contacts, R.drawable.menu_tab1_selector);
        contacts.setOnClickListener(this);

        guide = findViewById(R.id.menu_item_guide);
        initItem(guide, R.string.guide, R.drawable.menu_tab1_selector);
        guide.setOnClickListener(this);

        user_info = findViewById(R.id.menu_item_user_info);
        initItem(user_info, R.string.user_info, R.drawable.menu_tab1_selector);
        user_info.setOnClickListener(this);
    }

    private void initTitle() {
        products = findViewById(R.id.menu_title_product);
        initTitleItem(products, R.string.products);
        payments = findViewById(R.id.menu_title_payment);
        initTitleItem(payments, R.string.payments);
        utilities = findViewById(R.id.menu_title_utilites);
        initTitleItem(utilities, R.string.utilities);
    }

    /**
     * 为menu item 初始化文字 和背景
     * 
     * @param v
     * @param text
     * @param bg
     */
    private void initItem(View v, int textRes, int bgRes) {
        v.setBackgroundResource(bgRes);
        TextView textView = (TextView)v.findViewById(R.id.menu_item_tv);
        textView.setText(getResources().getString(textRes));
    }

    private void initTitleItem(View v, int textRes) {
        TextView textView = (TextView)v.findViewById(R.id.menu_title_tv);
        textView.setText(getResources().getString(textRes));
    }

    @Override
    public void onClick(View v) {

        if (this.onMenuClick != null) {
            onMenuClick.onMenuClick(v.getId());
        }
    }

    public void hideMenuItem(View v){
        if(v!=null){
            v.setVisibility(View.GONE);
        }
    }
    /**
     * 此为menu的监听器，事件通过传view 的id来判断是哪个按钮触发的
     * 
     * @author seekting.x.zhang
     */
    public static interface OnMenuClick {

        public void onMenuClick(int id);
    }
}
