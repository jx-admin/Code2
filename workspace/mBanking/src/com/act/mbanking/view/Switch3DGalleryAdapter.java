
package com.act.mbanking.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.act.mbanking.R;
import com.act.mbanking.bean.AccountsModel;
import com.custom.view.DoubleShadowTextView;

/**
 * @author junxu.wang
 */
public class Switch3DGalleryAdapter extends BaseAdapter {

    int mGalleryItemBackground;

    private Context mContext;

    List<AccountsModel> datas;

    int viewId;

    LayoutInflater lInflater;

    Switch3DGallery.LayoutParams lp;

    public Switch3DGalleryAdapter(Context c) {

        mContext = c;
        lInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lp = new Switch3DGallery.LayoutParams((int)c.getResources().getDimension(
                R.dimen.payment_account_card_size), (int)c.getResources().getDimension(
                R.dimen.payment_account_card_size));

//        datas = new ArrayList<AccountsModel>();
//        for (int i = 0; i < 10; i++) {
//            datas.add(i);
//        }
    }

    public void setViewId(int id) {
        this.viewId = id;
    }

    public int getCount() {
        
        return datas!=null?datas.size() + 1:1;

    }

    public AccountsModel getItem(int position) {

        return datas==null?null:position==datas.size()?datas.get(position-1):datas.get(position);

    }

    public long getItemId(int position) {

        return position;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (datas==null||position >= datas.size()) {
            if (convertView == null) {
                View v = lInflater.inflate(R.layout.new_payee_item, null);
                convertView = v;
            }
        } else {
            if (convertView == null) {
                View v = lInflater.inflate(viewId, null);
                convertView = v;
                // convertView.setLayoutParams(lp);
            }
            if(viewId==R.layout.account_data_opened_content){
                DoubleShadowTextView accountname_tv=(DoubleShadowTextView)convertView.findViewById(R.id.accountname_tv);
                accountname_tv.setText(datas.get(position).getAccountAlias());
            }else if(viewId==R.layout.account_data_closed_item){
                DoubleShadowTextView accountname_tv=(DoubleShadowTextView)convertView.findViewById(R.id.accountname_tv);
                accountname_tv.setText(datas.get(position).getAccountAlias());
            }
        }
        // (
        // (DoubleShadowTextView)convertView.findViewById(R.id.accountname_tv)).setText("i"+position);
        return convertView;

    }

    public float getScale(boolean focused, int offset) {

        return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));

    }

    /**
     * @return datas
     */
    public List<AccountsModel> getDatas() {
        return datas;
    }

    /**
     * @param datas 要设置的 datas
     */
    public void setDatas(List<AccountsModel> datas) {
        this.datas = datas;
    }

}
