package com.accenture.mbank.view.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.accenture.mbank.model.BankRecipient;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

/**
 * 联系人列表适配器。
 * 
 */
public abstract class RecipientAdapter<T> extends ArrayAdapter implements OnClickListener,SectionIndexer{

	public RecipientAdapter(Context context, int textViewResourceId,
			List objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas==null?0:datas.size();
	}

	@Override
	public void onClick(View v) {
		if(mOnItemClickListener!=null){
			int position =(Integer) v.getTag();
			mOnItemClickListener.onClick(v, getItem(position), position);
		}
	}
	
	OnItemClickListener mOnItemClickListener;
	public interface OnItemClickListener{
		public void onClick(View view,Object item,int position);
	}
	
	public void setOnItemClickListener(OnItemClickListener l){
		this.mOnItemClickListener=l;
	}
	/**
	 * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
	 * 
	 * @param sortKeyString
	 *            数据库中读取出的sort key
	 * @return 英文字母或者#
	 */
	String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}

//	public abstract String getSection(int index);
	public String getSection(int index){
		if(index<0||index>=sectionList.size()){
			return null;
		}
		return sectionList.get(index);
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return sectionList.toArray(new String[sectionList.size()]);
	}

	@Override
	public int getPositionForSection(int section) {
		Integer position= sectionMap.get(section);
		if(position!=null){
			return position;
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		Integer section=positionMap.get(position);
		if(section!=null){
			return section;
		}
		return -1;
	}
	

	List<T> datas;
    List<String> sectionList=new ArrayList<String>();
    Map <Integer, Integer>sectionMap=new HashMap<Integer, Integer>();
    Map <Integer,Integer>positionMap=new HashMap<Integer,Integer>();

    public void setData(List<T> datas,Comparator<T> mComparator){
    	if(datas!=null){
    		Collections.sort(datas, mComparator);
    	}
        this.datas=datas;
    }
    
    public List<T>getDatas(){
    	return datas;
    }

}