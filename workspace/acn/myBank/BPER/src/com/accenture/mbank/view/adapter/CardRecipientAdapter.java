package com.accenture.mbank.view.adapter;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;

/**
 * 联系人列表适配器。
 * 
 * @author guolin
 */
public class CardRecipientAdapter extends RecipientAdapter<CardRecipient>{

	/**
	 * 需要渲染的item布局文件
	 */
	private int resource;
	CardRecipientComparator mCardRecipientComparator= new CardRecipientComparator();

	public CardRecipientAdapter(Context context, int textViewResourceId, List objects) {
		super(context, textViewResourceId, objects);
		resource = textViewResourceId;
		setData(objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CardRecipient item = (CardRecipient) getItem(position);
		LinearLayout layout = null;
		if (convertView == null) {
			layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(resource, null);
		} else {
			layout = (LinearLayout) convertView;
		}
		TextView name = (TextView) layout.findViewById(R.id.title1_tv);
		name.setText(item.getName());
		name = (TextView) layout.findViewById(R.id.title2_tv);
		name.setText(item.getCardNumber());
		
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
	
    private List<CardRecipient> cardRecipientList;
    public void setData(List<CardRecipient> datas){
    	this.cardRecipientList=datas;
    	setData(datas,(String)null);
    }
    public void setData(List<CardRecipient> datas,CharSequence constraint){
    	super.setData(datas, mCardRecipientComparator);
        sectionList.clear();
        sectionMap.clear();
        sectionList.clear();
        int positionId=0;
        int sectionId=0;
        String lastSection="";
        if(datas!=null)
        for(CardRecipient item:datas){
        	String keyString=getSortKey(item.getName());
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
        	setData((List<CardRecipient>) results.values,constraint);

//        	cardRecipientList = (List<BankRecipient>) results.values; // has the filtered values
            notifyDataSetChanged();  // notifies the data with new filtered values
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
            List<CardRecipient> FilteredArrList = new ArrayList<CardRecipient>();
            List<CardRecipient> mOriginalValues=cardRecipientList;
//            if (mOriginalValues == null) {
//                mOriginalValues = new ArrayList<String>(arrayList); // saves the original data in mOriginalValues
//            }

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
                	CardRecipient mBankRecipient=mOriginalValues.get(i);
                    if (mBankRecipient.getName().toLowerCase().contains(constraint)) {
                        FilteredArrList.add(mBankRecipient);
                    }else if(mBankRecipient.getCardNumber().toLowerCase().contains(constraint)){
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
}
class CardRecipientComparator implements Comparator<CardRecipient>{
	
	public int compare(CardRecipient arg0, CardRecipient arg1) {
		//首先比较年龄，如果年龄相同，则比较名字
		
		int flag=arg0.getName().toUpperCase().compareTo(arg1.getName().toUpperCase());
		if(flag==0){
			return arg0.getCardNumber().compareTo(arg1.getCardNumber());
		}else{
			return flag;
		}  
	}
	
}
