
package com.act.mbanking.manager.view.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;

import com.act.mbanking.R;

/**
 * @author junxu.wang
 */
public abstract class NP3DGalleryAdapter<T> extends BaseAdapter {


    private Context mContext;

    List<T> datas;

    int viewId;

    LayoutInflater lInflater;

    private boolean addAble;

    private int account;
    
    protected int clickDisableId=-1;
    protected int size;

    protected LayoutParams lp;

    public NP3DGalleryAdapter(Context c) {

        super();
        mContext = c;
        lInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        size=(int)c.getResources().getDimension(R.dimen.payment_account_card_size);
    }
    
    public void setClickDisableId(int id){
        clickDisableId=id;
    }

    public void setViewId(int id) {
        this.viewId = id;
    }

    public int getCount() {
        if (datas == null) {
            return addAble ? 1 : 0;
        } else {
            return addAble ? datas.size() + 1 : datas.size();
        }
    }

    /**
     * @return datas
     */
    public List<T> getDatas() {
        return datas;
    }

    /**
     * @param datas 要设置的 datas
     */
    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public void setAddable(boolean able) {
        addAble = able;
    }


    @Override
    public Object getItem(int position) {
        if(datas!=null&&position>=0&&position<datas.size()){
            return datas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setNewItem(Serializable tmp_payee) {
        this.tmp_payee=tmp_payee;
    }
    
    protected Serializable tmp_payee; 
}
