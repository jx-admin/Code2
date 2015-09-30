package com.accenture.mbank.view.adapter;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.model.Account;
import com.accenture.mbank.model.BankRecipient;

@SuppressLint("UseSparseArrays")
public class BankRecipientAdapter extends RecipientAdapter<BankRecipient> {

	/**
	 * 需要渲染的item布局文件
	 */
	private int resource;

	public BankRecipientAdapter(Context context, int textViewResourceId, List<BankRecipient>objects) {
		super(context, textViewResourceId, objects);
		resource = textViewResourceId;
		setData(objects);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BankRecipient item = (BankRecipient) getItem(position);
		LinearLayout layout = null;
		if (convertView == null) {
			layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(resource, null);
		} else {
			layout = (LinearLayout) convertView;
		}
		TextView name = (TextView) layout.findViewById(R.id.title1_tv);
		name.setText(item.getName());
		name = (TextView) layout.findViewById(R.id.title2_tv);
		name.setText(item.getIbanCode());
		
		LinearLayout sortKeyLayout = (LinearLayout) layout.findViewById(R.id.sort_key_layout);
		TextView sortKey = (TextView) layout.findViewById(R.id.sort_key);
		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			sortKey.setText(getSection(section));
			sortKeyLayout.setVisibility(View.VISIBLE);
		} else {
			sortKeyLayout.setVisibility(View.GONE);
		}
		layout.setTag(position);
		return layout;
	}
	
	
    private List<BankRecipient> cardRecipientList;
    public void setData(List<BankRecipient> datas){
    	this.cardRecipientList=datas;
    	setData(datas, (String)null);
    }
    void setData(List<BankRecipient> datas,CharSequence filter){
    	super.setData(datas, mAccountComparator);
        sectionList.clear();
        sectionMap.clear();
        sectionList.clear();
        int positionId=0;
        int sectionId=-1;
        String lastSection="";
        if(datas!=null)
        for(Account item:datas){
        	String keyString=getSortKey(((BankRecipient)item).getName());
        	if(!lastSection.equals(keyString)){
        		++sectionId;
        		sectionMap.put(sectionId, positionId);
        		sectionList.add(keyString);
        		lastSection=keyString;
        	}
        	positionMap.put(positionId, sectionId);
        	positionId++;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
            	setData((List<BankRecipient>) results.values,constraint);

//            	cardRecipientList = (List<BankRecipient>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<BankRecipient> FilteredArrList = new ArrayList<BankRecipient>();
                List<BankRecipient> mOriginalValues=cardRecipientList;
//                if (mOriginalValues == null) {
//                    mOriginalValues = new ArrayList<String>(arrayList); // saves the original data in mOriginalValues
//                }

                /********
                 * 
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)  
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    int size=mOriginalValues.size();
                    for (int i = 0; i < size; i++) {
                    	BankRecipient mBankRecipient=mOriginalValues.get(i);
                        if (mBankRecipient.getIbanCode().toLowerCase().contains(constraint)) {
                            FilteredArrList.add(mBankRecipient);
                        }else if(mBankRecipient.getName().toLowerCase().contains(constraint)){
                            FilteredArrList.add(mBankRecipient);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
    AccountComparator mAccountComparator=new AccountComparator();
    class AccountComparator implements Comparator<BankRecipient>{
    	
    	public int compare(BankRecipient arg0, BankRecipient arg1) {
    		BankRecipient user0=(BankRecipient)arg0;
    		BankRecipient user1=(BankRecipient)arg1;
    		
    		//首先比较年龄，如果年龄相同，则比较名字
    		
    		int flag=user0.getName().toUpperCase().compareTo(user1.getName().toUpperCase());
    		if(flag==0){
    			return user0.getIbanCode().compareTo(user1.getIbanCode());
    		}else{
    			return flag;
    		}  
    	}
    	
    }

}
