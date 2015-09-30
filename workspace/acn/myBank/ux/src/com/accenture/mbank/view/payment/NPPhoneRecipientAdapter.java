
package com.accenture.mbank.view.payment;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;

import com.accenture.mbank.R;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.util.AvailableOperator;
import com.accenture.mbank.view.adapter.CoverFlowImageAdapter;
import com.custom.view.DoubleShadowTextView;

/**
 * @author junxu.wang
 */
public class NPPhoneRecipientAdapter extends CoverFlowImageAdapter {

    int mGalleryItemBackground;

    private Context mContext;

    List<PhoneRecipient> datas;

    LayoutInflater lInflater;

    Switch3DGallery.LayoutParams lp;

    int size;

    public NPPhoneRecipientAdapter(Context c) {

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
    public List<PhoneRecipient> getDatas() {
        return datas;
    }

    /**
     * @param datas 要设置的 datas
     */
    public void setDatas(List<PhoneRecipient> datas) {
        this.datas = datas;
    }

    @Override
    public View getView(int position) {
        View convertView = null;
        PhoneRecipient mPhoneRecipient;
        if (datas == null || position >= datas.size()) {
            if (convertView == null) {
                View v = lInflater.inflate(R.layout.new_payee_item, null);
                convertView = v;
                lp = new Switch3DGallery.LayoutParams(size, LayoutParams.WRAP_CONTENT);
                convertView.setLayoutParams(lp);
            }
            String name=null;
            if(tmp_payee!=null){
                mPhoneRecipient=(PhoneRecipient)tmp_payee;
                name=mPhoneRecipient.getName();
            }
            if(!TextUtils.isEmpty(name)){
                DoubleShadowTextView accountname_tv = (DoubleShadowTextView)convertView
                .findViewById(R.id.accountname_tv);
                accountname_tv.setText(name);
            }
        } else {
            if (convertView == null) {
                View v = lInflater.inflate(R.layout.account_data_opened_content, null);
                convertView = v;
                lp = new Switch3DGallery.LayoutParams(size, LayoutParams.WRAP_CONTENT);
                convertView.setLayoutParams(lp);
            }
            
            mPhoneRecipient= datas.get(position);
                DoubleShadowTextView accountname_tv = (DoubleShadowTextView)convertView
                        .findViewById(R.id.accountname_tv);
                accountname_tv.setText(mPhoneRecipient.getName());
                DoubleShadowTextView label3_tv=(DoubleShadowTextView)convertView.findViewById(R.id.label3_tv);
                label3_tv.setText(AvailableOperator.getNameByCode(mPhoneRecipient.getProvider()));
                DoubleShadowTextView account_available_tv = (DoubleShadowTextView)convertView
                        .findViewById(R.id.account_available_tv);
                account_available_tv.setText(mPhoneRecipient.getPhoneNumber());
        }
        return convertView;

    }

}
