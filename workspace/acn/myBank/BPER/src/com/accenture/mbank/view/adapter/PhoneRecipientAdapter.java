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

import com.accenture.mbank.BaseActivity;
import com.accenture.mbank.logic.TransferObjectSim;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;

/**
 * 联系人列表适配器。
 * 
 * @author guolin
 */
public class PhoneRecipientAdapter extends RecipientAdapter<PhoneRecipient>{

	/**
	 * 需要渲染的item布局文件
	 */
	private int resource;
	PhoneRecipientComparator mPhoneRecipientComparator= new PhoneRecipientComparator();

	public PhoneRecipientAdapter(Context context, int textViewResourceId, List<PhoneRecipient>objects) {
		super(context, textViewResourceId, objects);
		resource = textViewResourceId;
		setData(objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhoneRecipient item = (PhoneRecipient) getItem(position);
		LinearLayout layout = null;
		if (convertView == null) {
			layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(resource, null);
		} else {
			layout = (LinearLayout) convertView;
		}
		TextView name = (TextView) layout.findViewById(R.id.title1_tv);
		name.setText(item.getName());
		name = (TextView) layout.findViewById(R.id.title2_tv);
		if(BaseActivity.isOffline){
			name.setText(item.getPhoneNumber());
		}else{
		String certifiedNumber = Contants.getUserInfo.getUserprofileHb().getContactPhone();
		if(certifiedNumber != null && item.getPhoneNumber().equals(certifiedNumber)){
			name.setText(Utils.maskCertifiedNumber(item.getPhoneNumber()));
		} else {
			name.setText(item.getPhoneNumber());
		}
		}
		
		TextView provider = (TextView) layout.findViewById(R.id.title3_tv);
		provider.setVisibility(View.VISIBLE);
		provider.setText(TransferObjectSim.getProviderName(item.getProviderCode()));
		
		
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

    private List<PhoneRecipient> cardRecipientList;

    public void setData(List<PhoneRecipient> datas){
    	this.cardRecipientList=datas;
    	setData(datas,(String)null);
    }
    public void setData(List<PhoneRecipient> datas,CharSequence constraint){
    	super.setData(datas, mPhoneRecipientComparator);
        sectionList.clear();
        sectionMap.clear();
        int positionId=0;
        int sectionId=-1;
        String lastSection="";
        if(datas!=null)
        for(PhoneRecipient item:datas){
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
        	setData((List<PhoneRecipient>) results.values,constraint);

//        	cardRecipientList = (List<BankRecipient>) results.values; // has the filtered values
            notifyDataSetChanged();  // notifies the data with new filtered values
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
            List<PhoneRecipient> FilteredArrList = new ArrayList<PhoneRecipient>();
            List<PhoneRecipient> mOriginalValues=cardRecipientList;
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
                	PhoneRecipient mBankRecipient=mOriginalValues.get(i);
                    if (mBankRecipient.getName().toLowerCase().contains(constraint)) {
                        FilteredArrList.add(mBankRecipient);
                    }else if(mBankRecipient.getPhoneNumber().toLowerCase().contains(constraint)){
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
class PhoneRecipientComparator implements Comparator<PhoneRecipient>{
	
	public int compare(PhoneRecipient arg0, PhoneRecipient arg1) {
		//首先比较年龄，如果年龄相同，则比较名字
		
		int flag=arg0.getName().toUpperCase().compareTo(arg1.getName().toUpperCase());
		if(flag==0){
			return arg0.getPhoneNumber().compareTo(arg1.getPhoneNumber());
		}else{
			return flag;
		}  
	}
	
}
