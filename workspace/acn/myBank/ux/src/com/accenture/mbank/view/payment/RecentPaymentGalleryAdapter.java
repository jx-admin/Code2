
package com.accenture.mbank.view.payment;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;

import com.accenture.mbank.R;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.adapter.CoverFlowImageAdapter;
import com.custom.view.DoubleShadowTextView;

/**
 * @author yang.c.li
 */
public class RecentPaymentGalleryAdapter extends CoverFlowImageAdapter {

    int mGalleryItemBackground;

    List<AccountsModel> accountList;

    int viewId;

    LayoutInflater lInflater;

    DoubleShadowTextView account_tv;

    DoubleShadowTextView account_available_value;

    Switch3DGallery.LayoutParams lp;

    int size;

    Context context;

    public RecentPaymentGalleryAdapter(Context c) {
        lInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = c;
        size = (int)context.getResources().getDimension(R.dimen.payment_account_card_size);
    }

    public void setViewId(int id) {
        this.viewId = id;
    }

    public int getCount() {
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
    public List<?> getDatas() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getView(int position) {
        View convertView = null;
        if (convertView == null) {
            View v = lInflater.inflate(viewId, null);
            convertView = v;
            lp = new Switch3DGallery.LayoutParams(size, LayoutParams.WRAP_CONTENT);
            convertView.setLayoutParams(lp);
            account_tv = (DoubleShadowTextView)convertView.findViewById(R.id.accountname_tv);
            account_tv.setText(accountList.get(position).getAccountAlias());

            account_available_value = (DoubleShadowTextView)convertView
                    .findViewById(R.id.account_available_tv);

            String amount = Utils.generateFormatMoney(
                    context.getResources().getString(R.string.dollar), accountList.get(position)
                            .getBalance().getAvailableBalance());
            account_available_value.setText(amount);

        }
        return convertView;
    }

}
