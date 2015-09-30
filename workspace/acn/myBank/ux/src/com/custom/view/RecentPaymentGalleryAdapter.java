
package com.custom.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.accenture.mbank.R;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.adapter.CoverFlowImageAdapter;

/**
 * @author yang.c.li
 */
public class RecentPaymentGalleryAdapter extends CoverFlowImageAdapter {

    int mGalleryItemBackground;

    List<AccountsModel> accountList = new ArrayList<AccountsModel>();

    int viewId;

    LayoutInflater lInflater;

    DoubleShadowTextView account_tv;

    DoubleShadowTextView account_available_value;

    Context context;

    Handler handler;

    public RecentPaymentGalleryAdapter(Context c, CoverFlow coverFlow) {
        super(coverFlow);
        lInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = c;
        handler = new Handler();
    }

    public void setViewId(int id) {
        this.viewId = id;
    }

    public int getCount() {
    	if(accountList==null){
    		return 0;
    	}
        return accountList.size();
    }

    public float getScale(boolean focused, int offset) {
        return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));

    }

    /**
     * @return accountList
     */
    public List<AccountsModel> getAccountList() {
        return accountList;
    }

    /**
     * @param accountList 要设置的 accountList
     */
    public void setAccountList(List<AccountsModel> accountList) {
        this.accountList = accountList;
    }

    @Override
    public View getView(int position) {

        if (accountList == null) {
            return null;
        }
        View v = lInflater.inflate(viewId, null);
        AccountsModel mAccountsModel = accountList.get(position);
        if (mAccountsModel != null) {
            account_tv = (DoubleShadowTextView)v.findViewById(R.id.accountname_tv);
            account_tv.setText(mAccountsModel.getAccountAlias());

            account_available_value = (DoubleShadowTextView)v
                    .findViewById(R.id.account_available_tv);
            double availableBalance = 0;
            if (mAccountsModel.getBalance() != null) {
                availableBalance = mAccountsModel.getBalance().getAvailableBalance();
            }
            
            String amount = Utils.generateFormatMoney(
                    context.getResources().getString(R.string.dollar), accountList.get(position)
                            .getBalance().getAvailableBalance());
            
            account_available_value.setText(amount);
//            account_available_value.setText(Utils.formatMoney(availableBalance, "", true, true,false, false, true));
        }
        return v;
    }

    @Override
    public List<?> getDatas() {
        // TODO Auto-generated method stub
        return null;
    }

}
