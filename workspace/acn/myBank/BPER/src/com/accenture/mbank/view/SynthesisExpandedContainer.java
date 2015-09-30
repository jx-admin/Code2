
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.MainActivity;
import com.accenture.mbank.database.BankSqliteHelper;
import com.accenture.mbank.util.DialogManager;
import com.accenture.mbank.util.LogManager;
import com.accenture.mbank.view.protocol.SaveEnAble;

public class SynthesisExpandedContainer extends ExpandedContainer implements
        OnSeekBarChangeListener, OnClickListener, SaveEnAble {

    TextView dashboardAccountsText, dashboardCardsText;

    SeekBar dashboardAccountsSeekBar, dashboardCardsSeekBar;

    ImageButton save;

    public SynthesisExpandedContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        // TODO Auto-generated method stub
        super.onAttachedToWindow();

        LogManager.d("onAttachedToWindow");
        init();
        restore();
    }

    public void init() {
        dashboardAccountsText = (TextView)findViewById(R.id.dashboard_account_tv);
        dashboardCardsText = (TextView)findViewById(R.id.dashboard_cards_tv);
        dashboardAccountsSeekBar = (SeekBar)findViewById(R.id.seekbar_accounts);
        dashboardCardsSeekBar = (SeekBar)findViewById(R.id.seekbar_cards);
        save = (ImageButton)findViewById(R.id.syntheis_save);
        dashboardAccountsSeekBar.setOnSeekBarChangeListener(this);
        save.setOnClickListener(this);
        dashboardCardsSeekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar == dashboardAccountsSeekBar) {
            dashboardAccountsText.setText(String.valueOf(progress + 4));

        } else if (seekBar == dashboardCardsSeekBar) {
            dashboardCardsText.setText(String.valueOf(progress + 4));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    public void save() {

        MainActivity mainActivity = (MainActivity)getContext();
        int accounts = dashboardAccountsSeekBar.getProgress() + 4;
        int cards = dashboardCardsSeekBar.getProgress() + 4;
        mainActivity.setting.setDashboardAccounts(accounts);
        mainActivity.setting.setDashboardCards(cards);
        BankSqliteHelper bankSqliteHelper = BankSqliteHelper.getInstance();
        boolean flag = bankSqliteHelper.update(mainActivity.setting);
        if (flag) {
//            mainActivity.displaySuccessMessage("save success!");
            DialogManager.createMessageDialog("",getContext().getResources().getString(R.string.saved_settings),mainActivity).show();
        }

    }

    @Override
    public void restore() {

        if (BaseActivity.isOffline) {
            return;
        }
        MainActivity mainActivity = (MainActivity)getContext();

        int account = mainActivity.setting.getDashboardAccounts();
        int cards = mainActivity.setting.getDashboardCards();

        //
        dashboardAccountsSeekBar.setProgress(account - 4);
        dashboardCardsSeekBar.setProgress(cards - 4);
        dashboardAccountsText.setText(String.valueOf(account));
        dashboardCardsText.setText(String.valueOf(cards));
    }

    @Override
    public void onClick(View v) {

        if (v == save) {
            save();

        }
    }

}
