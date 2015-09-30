
package com.act.mbanking.manager.view.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.act.mbanking.R;
import com.act.mbanking.bean.Account;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.BankRecipient;
import com.custom.view.CoverFlow;
import com.custom.view.DoubleShadowTextView;
import com.custom.view.Switch3DGallery;

/**
 * @author junxu.wang
 */
public class NPBankRecipientAdapter extends CoverFlowImageAdapter {

    int mGalleryItemBackground;

    private Context mContext;

    List<Account> datas;

    LayoutInflater lInflater;

    Switch3DGallery.LayoutParams lp;

    int size;
    
    int moreCount=2;

    public NPBankRecipientAdapter(Context c) {

        mContext = c;
        lInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        size = (int)c.getResources().getDimension(R.dimen.payment_account_card_size);

    }

    public int getCount() {

        return datas != null ? datas.size() + moreCount : moreCount;

    }

    /**
     * @return datas
     */
    public List<Account> getDatas() {
        return datas;
    }

    /**
     * @param datas 要设置的 datas
     */
    public void setDatas(List<Account> datas) {
        this.datas = datas;
    }

    @Override
    public View getView(int position) {
        View convertView = null;
        BankRecipient mBankRecipient;
        int count=getCount();
        if (count-position==2) {
            if (convertView == null) {
                View v = lInflater.inflate(R.layout.new_payee_item, null);
                convertView = v;
                lp = new CoverFlow.LayoutParams(size, size);
                convertView.setLayoutParams(lp);
            }
            String name=null;
            if(tmp_payee!=null){
                mBankRecipient=(BankRecipient)tmp_payee;
                name=mBankRecipient.getName();
            }
            if(!TextUtils.isEmpty(name)){
                DoubleShadowTextView accountname_tv = (DoubleShadowTextView)convertView
                .findViewById(R.id.accountname_tv);
                accountname_tv.setText(name);
            }else{
            	 DoubleShadowTextView accountname_tv = (DoubleShadowTextView)convertView
                 .findViewById(R.id.accountname_tv);
                 accountname_tv.setText(R.string.new__payee);
            }
        }else if (count-position==1) {
            if (convertView == null) {
                View v = lInflater.inflate(R.layout.new_payee_item, null);
                convertView = v;
                lp = new CoverFlow.LayoutParams(size, size);
                convertView.setLayoutParams(lp);
            }
            String name=null;
            if(tmp_payee_by_phone!=null){
                mBankRecipient=(BankRecipient)tmp_payee_by_phone;
                name=mBankRecipient.getName();
            }
            if(!TextUtils.isEmpty(name)){
                DoubleShadowTextView accountname_tv = (DoubleShadowTextView)convertView
                .findViewById(R.id.accountname_tv);
                accountname_tv.setText(name);
            }else{
           	 DoubleShadowTextView accountname_tv = (DoubleShadowTextView)convertView
             .findViewById(R.id.accountname_tv);
             accountname_tv.setText(R.string.new_payee__by_phone);
            }
        } else {
        	if (convertView == null) {
        		View v = lInflater.inflate(R.layout.account_data_closed_item, null);
        		convertView = v;
        		lp = new CoverFlow.LayoutParams(size, size);
        		convertView.setLayoutParams(lp);
        	}
        	DoubleShadowTextView accountname_tv = (DoubleShadowTextView)convertView
        	.findViewById(R.id.accountname_tv);
        	ImageView icon_iv=(ImageView) convertView.findViewById(R.id.icon_iv);
        	
        	Account mAccount=datas.get(position);
        	if(mAccount instanceof BankRecipient){
                accountname_tv.setText(((BankRecipient)mAccount).getName());
                icon_iv.setVisibility(View.GONE);
        	}else{
        		 accountname_tv.setText(((AccountsModel)mAccount).getPhoneNumber());
                 icon_iv.setVisibility(View.VISIBLE);
        	}
        }
        return convertView;

    }

    public void setPhonePayeeItem(Serializable tmp_payee_by_phone) {
        this.tmp_payee_by_phone=tmp_payee_by_phone;
    }
    
    protected Serializable tmp_payee_by_phone; 

}
