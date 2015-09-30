
package com.accenture.mbank.view.payment;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accenture.mbank.R;
import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.view.adapter.CoverFlowImageAdapter;
import com.custom.view.DoubleShadowTextView;

/**
 * @author junxu.wang
 */
public class NPCardRecipientAdapter extends CoverFlowImageAdapter {

    int mGalleryItemBackground;

    private Context mContext;

    List<CardRecipient> datas;

    LayoutInflater lInflater;

    Switch3DGallery.LayoutParams lp;

    int size;

    public NPCardRecipientAdapter(Context c) {

        mContext = c;
        lInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        size = (int)c.getResources().getDimension(R.dimen.payment_account_card_size);

    }

    public int getCount() {

        return datas != null ? datas.size() + 1 : 1;

    }

    public float getScale(boolean focused, int offset) {

        return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));

    }

    /**
     * @return datas
     */
    public List<CardRecipient> getDatas() {
        return datas;
    }

    /**
     * @param datas 要设置的 datas
     */
    public void setDatas(List<CardRecipient> datas) {
        this.datas = datas;
    }

    @Override
    public View getView(int position) {
        View convertView = null;
        CardRecipient mCardRecipient;

        if (datas == null || position >= datas.size()) {
            if (convertView == null) {
                View v = lInflater.inflate(R.layout.new_payee_item, null);
                convertView = v;
                lp = new Switch3DGallery.LayoutParams(size, size);
                convertView.setLayoutParams(lp);
                
            }
            String name=null;
            if(tmp_payee!=null){
                mCardRecipient=(CardRecipient)tmp_payee;
                name=mCardRecipient.getName();
            }
            if(!TextUtils.isEmpty(name)){
                DoubleShadowTextView accountname_tv = (DoubleShadowTextView)convertView
                .findViewById(R.id.accountname_tv);
                accountname_tv.setText(name);
            }
        } else {
            mCardRecipient= datas.get(position);
            if (convertView == null) {
                View v = lInflater.inflate(R.layout.account_data_closed_item, null);
                convertView = v;
                lp = new Switch3DGallery.LayoutParams(size, size);
                convertView.setLayoutParams(lp);
            }
            DoubleShadowTextView accountname_tv = (DoubleShadowTextView)convertView
            .findViewById(R.id.accountname_tv);
            accountname_tv.setText(mCardRecipient.getName());
        }
        return convertView;

    }

}
