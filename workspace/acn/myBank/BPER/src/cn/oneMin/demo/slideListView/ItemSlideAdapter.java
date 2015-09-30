package cn.oneMin.demo.slideListView;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.oneMin.demo.slideListView.ItemSlideTouchListener.OnSlideItemClickListener;

import com.accenture.mbank.logic.TransferObject;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.nineoldandroids.view.ViewHelper;

public class ItemSlideAdapter extends BaseAdapter{
	private Context context=null;
	private LayoutInflater mInflater=null;
	private List<TransferObject> list;	
	public ItemSlideTouchListener mSlideListViewItemTouchListener=new ItemSlideTouchListener();
	public ItemSlideAdapter(Context context){
		this.context=context;
		mInflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getItem(int position) {
		if(position>=0&&position<list.size()){
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	private AccountsModel accountModel;
	
	/**
	 * @param recentTransferList
	 *            要设置的 recentTransferList
	 */
	public void seList(AccountsModel accountModel,List<TransferObject> recentTransferList) {
		this.accountModel = accountModel;
		setDatas(recentTransferList);
//		this.recentTransferList = recentTransferList;
		notifyDataSetChanged();
	}
	
	
	TextView timeText, typeText, resultText, one_text, four_text, two_text,
	three_text, five_text;
	
	// MainManager mainManager;
	
	// public void setMainManager(MainManager mainManager){
	// this.mainManager=mainManager;
	// }
	
	@Override
	public int getCount() {
		return list==null?0:list.size();
	}
	
	
	
	View titleItem;
	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if(convertView==null) {
			convertView=mInflater.inflate(R.layout.bper_recent_slidelist_items, viewGroup, false);		   					
		}
		final View showView=convertView.findViewById(R.id.show_item);
		final View hideView=convertView.findViewById(R.id.hide_item);
		ViewHelper.setTranslationX(showView, 0);
		ViewHelper.setTranslationX(hideView, 0);
		
		
		((ImageView)convertView.findViewById(R.id.divider)).setVisibility(View.GONE);
		
		ViewHolder mHolder = (ViewHolder) convertView.getTag();
		if (mHolder == null) {
			mHolder = new ViewHolder();
			mHolder.init(convertView);
			convertView.setTag(mHolder);
		}
		
		//content 
//		convertView.setOnClickListener(this);
		mHolder.position=position;
		TransferObject mRecentTransferModel = list .get(position);
		
		//date
		long date = mRecentTransferModel.getDate();//OperationDate();
		String dateStr = TimeUtil.getDateString(date, TimeUtil.dateFormat5);
		mHolder.textView1.setText(dateStr);
		
		// amount
		String amount = mRecentTransferModel.getAmount() + "";
		amount = Utils.notPlusGenerateFormatMoney(this.context.getResources() .getString(R.string.eur), amount);
		mHolder.textView3.setText(amount);
		
		//destription
		if (Contants.BANK_TRANSFER.equals(mRecentTransferModel.getType())) {
			mHolder.textView2.setText(R.string.recent_des_bank_transfer);
		} else if (Contants.TRANSFER_ENTRY.equals(mRecentTransferModel.getType())) {
			mHolder.textView2.setText(R.string.recent_des_transfer_entry);
		} else if (Contants.SIM_TOP_UP.equals(mRecentTransferModel.getType())) {
			mHolder.textView2.setText(R.string.recent_des_sim_top_up);
		} else if (Contants.PREPAID_CARD_RELOAD.equals(mRecentTransferModel.getType())) {
			mHolder.textView2.setText(R.string.recent_des_prepaid_card_reload);
		}
		
		mHolder.invalidate(position);
		convertView.setOnTouchListener(mSlideListViewItemTouchListener);
		return convertView;
	}

	public static class ViewHolder {
		static int selectedId = -1;
		static ViewHolder selectedHolder = null;
		
		
		public TextView textView1, textView2, textView3,TextView4;

		View showView;
		View hideView;
		
		public int position;
		
		public void init(View v) {
			
			textView1 = (TextView) v.findViewById(R.id.textView1);
			textView2 = (TextView) v.findViewById(R.id.textView2);
			textView3 = (TextView) v.findViewById(R.id.textView4);

			showView=v.findViewById(R.id.show_item);
			hideView=v.findViewById(R.id.hide_item);
		}
		
		public void setSelected(boolean check) {
			
		}
		
		public void invalidate(int position) {
			ViewHelper.setTranslationX(showView, 0);
			ViewHelper.setTranslationX(hideView, 0);
			this.position = position;
			if (selectedId == position) {
				setSelected(true);
			} else {
				setSelected(false);
			}
		}
	}
	public AccountsModel getAccountsModel() {
		return accountModel;
	}
	
	public void setAccountsModel(AccountsModel accountModel) {
		this.accountModel = accountModel;
	}
	
	public void setOnRecentItemClickLinstener(OnSlideItemClickListener mOnSlideItemClickListener){
		mSlideListViewItemTouchListener.setOnSlideItemClickListener(mOnSlideItemClickListener);
	}

	public void setDatas(List<TransferObject> list) {
		this.list = list;
	}

	public List<TransferObject> getDatas() {
		return this.list;
	}

}
