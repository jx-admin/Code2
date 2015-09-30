
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.LoansDetailActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetFinancingInfoModel;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.protocol.ShowAble;
import com.accenture.mbank.view.protocol.ShowListener;

public class LoanItemLayout extends BankLinearLayout implements View.OnClickListener,ShowAble {

    private AccountsModel accountsModel;

    Handler handler;

    ImageButton showBtn, closeBtn;

    ImageView line;

    TextView loanName, date, loan_value ,available_loans_account_value,tvTotalAmountTitle,tvResidueAmountTitle;
    
    ImageView loan_Star;
    
    GetFinancingInfoModel getFinancingInfo;
    
    public LoanItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        init();
    }

    void init() {
        showBtn = (ImageButton)findViewById(R.id.loan_show_img);
        closeBtn = (ImageButton)findViewById(R.id.loan_close_img);
        int height_top = (int)(cycle_top_height * BaseActivity.screen_height);
        closeBtn.getLayoutParams().height = height_top;
        int width = (int)(height_top * close_widht_height);
//        closeBtn.getLayoutParams().width = width;

        int height_bottom = (int)(height_top * cycle_bottom_height);

        showBtn.getLayoutParams().height = height_bottom;
//        showBtn.getLayoutParams().width = (int)(height_bottom * show_widht_height);

        line = (ImageView)findViewById(R.id.line);
        line.getLayoutParams().height = height_top;

        ViewGroup loanData = (ViewGroup)findViewById(R.id.loan_data);
        loanData.getLayoutParams().height = height_top;
        ViewGroup loanSectionLayout = (ViewGroup)findViewById(R.id.loan_section_layout);
        loanSectionLayout.getLayoutParams().height = height_top;
        loanSectionLayout.getLayoutParams().width = width;
        
        showBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);

        loanName = (TextView)findViewById(R.id.loan_name);
        loan_Star = (ImageView)findViewById(R.id.loan_star);
        date = (TextView)findViewById(R.id.loan_date);

        loan_value = (TextView)findViewById(R.id.residue_amount_value);
        available_loans_account_value = (TextView)findViewById(R.id.total_amount_value);
        tvTotalAmountTitle = (TextView) findViewById(R.id.total_amount_value_title);
        tvResidueAmountTitle = (TextView) findViewById(R.id.residue_amount_title);
    }

    public AccountsModel getAccount() {
        return accountsModel;
    }

    public void setAccountsModel(AccountsModel accountsModel) {
        this.accountsModel = accountsModel;
        setTexts();

    }

    private void setTexts() {

        if (loanName == null) {
            init();
        }
        String name = accountsModel.getAccountAlias();
        loanName.setText(name);
        date.setText(TimeUtil.getDateString(System.currentTimeMillis(), TimeUtil.dateFormat5));
        if(accountsModel.getIsPreferred()){
        	loan_Star.setImageResource(R.drawable.icone_star_red);
        }
    }

    public void updateUIByData(final GetFinancingInfoModel getFinancingInfo) {
    	this.getFinancingInfo = getFinancingInfo;
        handler.post(new Runnable() {
            @Override
            public void run() {
            	/*
            	 * residue amount
            	 */
                try {
                    String str = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur),getFinancingInfo.getResidueAmount());
                    loan_value.setText(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String title = getContext().getResources().getString(R.string.residual_capital1);
                title=Utils.toUppString(title);
                tvResidueAmountTitle.setText(title);

                /*
                 * total Amount
                 */
                title = getContext().getResources().getString(R.string.loans_total_amount);
                title=Utils.toUppString(title);
                tvTotalAmountTitle.setText(title);

                String money = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur), getFinancingInfo.getTotalAmountl());
                available_loans_account_value.setText(money);
      
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (showBtn == v) {
        	Intent intent = new Intent(getContext(), LoansDetailActivity.class);
        	intent.putExtra("GETFINANCINGINFO", getFinancingInfo);
        	intent.putExtra("ACCOUNT_MODEL", accountsModel);
        	getContext().startActivity(intent);
        	
	        MainActivity mainActivity = (MainActivity)getContext();
	        mainActivity.overridePendingTransition(R.anim.slide_in_right_to_left, 0);
        } 
    }

    public void setStates() {

    }

    public void showDelay() {
//        showContent(showBtn, this, content);
    }

    @Override
    public void show() {
        listener.onShow(this);
    }

    ShowListener listener;

    @Override
    public void setShowListener(ShowListener listener) {

        this.listener = listener;
    }

	@Override
	public void close() {
		
	}
}
