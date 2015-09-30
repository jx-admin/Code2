package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.accenture.mbank.model.BankRecipient;
import com.accenture.mbank.model.CardRecipient;
import com.accenture.mbank.model.PhoneRecipient;
import com.accenture.mbank.view.adapter.BankRecipientAdapter;
import com.accenture.mbank.view.adapter.CardRecipientAdapter;
import com.accenture.mbank.view.adapter.PhoneRecipientAdapter;
import com.accenture.mbank.view.adapter.RecipientAdapter;
import com.custom.view.MyLetterListView;
import com.custom.view.MyLetterListView.OnTouchingLetterChangedListener;

public class BPERPhoneBook implements TextWatcher {
	
	Context context;

	/**
	 * 分组的布局
	 */
	private LinearLayout titleLayout;

	/**
	 * 分组上显示的字母
	 */
	private TextView title;

	/**
	 * 联系人ListView
	 */
	private ListView contactsListView;

	/**
	 * 联系人列表适配器
	 */
	private RecipientAdapter adapter;

	/**
	 * 定义字母表的排序规则
	 */
//	private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
	

    private MyLetterListView letterListView;

    LayoutInflater lf;
    View contentView;
    

	 EditText search_et;
	public BPERPhoneBook(Context context) {
		this.context=context;
		lf=LayoutInflater.from(context);
		contentView=lf.inflate(R.layout.phonebook_page, null);
        letterListView = (MyLetterListView)contentView.findViewById(R.id.MyLetterListView01);
        letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        
		titleLayout = (LinearLayout) contentView.findViewById(R.id.sort_key_layout);
		title = (TextView) contentView.findViewById(R.id.sort_key);
		contactsListView = (ListView) contentView.findViewById(R.id.contacts_list_view);
		search_et=(EditText) contentView.findViewById(R.id.search_et);
		search_et.addTextChangedListener(this);
	}
	
	public View getView(){
		return contentView;
	}
	
	public void setCardRecipientData(List<CardRecipient> datas){
		adapter = new CardRecipientAdapter(context, R.layout.phonebook_item, datas);
		if (datas!=null&&datas.size() > 0) {
			setupContactsListView();
//			letterListView.setData((String[])adapter.getSections());
		}
	}
	
	public void setPhoneRecipientData(List<PhoneRecipient>datas){
		adapter = new PhoneRecipientAdapter(context, R.layout.phonebook_item, datas);
		if (datas!=null&&datas.size() > 0) {
			setupContactsListView();
//			letterListView.setData((String[])adapter.getSections());
		}
	}
	
	public void setBankRecipientData(List<BankRecipient>datas){
		adapter = new BankRecipientAdapter(context, R.layout.phonebook_item, datas);
		if (datas!=null&&datas.size() > 0) {
			setupContactsListView();
//			letterListView.setData((String[])adapter.getSections());
		}
	}
	
	public void setFile(CharSequence constraint){
		adapter.getFilter().filter(constraint);
	}
	

	/**
	 * 为联系人ListView设置监听事件，根据当前的滑动状态来改变分组的显示位置，从而实现挤压动画的效果。
	 */
	private void setupContactsListView() {
		contactsListView.setAdapter(adapter);
		contactsListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int section = adapter.getSectionForPosition(firstVisibleItem);
				letterListView.setSection(adapter.getSection(section));
				int nextPositiong = adapter.getPositionForSection(section + 1);
				if (firstVisibleItem != lastFirstVisibleItem) {
					MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
					params.topMargin = 0;
					titleLayout.setLayoutParams(params);
					title.setText(adapter.getSection(section));
				}
				if (nextPositiong>=0&&nextPositiong == firstVisibleItem + 1) {
					View childView = view.getChildAt(0);
					if (childView != null) {
						int titleHeight = titleLayout.getHeight();
						int bottom = childView.getBottom();
						MarginLayoutParams params = (MarginLayoutParams) titleLayout .getLayoutParams();
						if (bottom < titleHeight) {
							float pushedDistance = bottom - titleHeight;
							params.topMargin = (int) pushedDistance;
							titleLayout.setLayoutParams(params);
						} else {
							if (params.topMargin != 0) {
								params.topMargin = 0;
								titleLayout.setLayoutParams(params);
							}
						}
					}
				}
				lastFirstVisibleItem = firstVisibleItem;
			}
		});

	}

	/**
	 * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
	 * 
	 * @param sortKeyString
	 *            数据库中读取出的sort key
	 * @return 英文字母或者#
	 */
	private String getSortKey(String sortKeyString) {
		String key = sortKeyString.substring(0, 1).toUpperCase();
		if (key.matches("[A-Z]")) {
			return key;
		}
		return "#";
	}
	
	public void setOnItemClickListener(OnItemClickListener l){
		contactsListView.setOnItemClickListener(l);
	}
    
    private class LetterListViewListener implements OnTouchingLetterChangedListener{

		@Override
		public void onTouchingLetterChanged(int position,final String s) {
			int itemPosition=adapter.getPositionForSection(position);
			contactsListView.setSelection(itemPosition);
		}
    	
    }

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		setFile(s);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

}
