package com.accenture.mbank.view.payment;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.util.PagerBaseAdapter;
import com.accenture.mbank.util.ScrollerLinearMenu;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.AccountInfoTitle;

public final class BPERPaymentAccountPaperUtils implements OnClickListener, OnPageChangeListener {
	private View contentView;
	private ViewPager accounts_vp;
	private ImageView arrow_left,arrow_right;
	private ScrollerLinearMenu accountIndexs;
	private boolean accounts_vp_enable=true;
	private MyPagerAdapter mMyPagerAdapter;
	private List<AccountsModel> accounts;
	private OnPageChangeListener mOnPageChangeListener;

	public BPERPaymentAccountPaperUtils(View contentView){
		this.contentView=contentView;
		accountIndexs=new ScrollerLinearMenu(contentView);
		accountIndexs.setIndexBitmap(R.drawable.paging_account_selected);
		accounts_vp=(ViewPager) contentView.findViewById(R.id.account_vp);
		accounts_vp.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return !accounts_vp_enable;
			}
		});
		accounts_vp.setOnPageChangeListener(this);
		mMyPagerAdapter = new MyPagerAdapter(accounts_vp.getContext());
		accounts_vp.setAdapter(mMyPagerAdapter);
		arrow_left=(ImageView) contentView.findViewById(R.id.arrow_left);
		arrow_left.setOnClickListener(this);
		arrow_right=(ImageView) contentView.findViewById(R.id.arrow_right);
		arrow_right.setOnClickListener(this);
		accounts_vp.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				 if(event.getAction()== MotionEvent.ACTION_DOWN){
					ViewParent mParent = accounts_vp.getParent();
					if (mParent != null) {
			            mParent.requestDisallowInterceptTouchEvent(true);
			        }
				 }
					accounts_vp.onTouchEvent(event);
				return true;
			}
		});
	}
	
	public View getContentView(){
		return contentView;
	}
	
	public void setData(List<AccountsModel> accounts) {
		this.accounts = accounts;
		int accountSize=0;
		if (accounts != null) {
			accountSize=accounts.size();
		}
		setIndex(accountSize);
		mMyPagerAdapter.notifyDataSetChanged();
		accountIndexs.setSelected(accounts_vp.getCurrentItem());
		if(accountSize>1){
			arrow_left.setVisibility(View.VISIBLE);
			arrow_right.setVisibility(View.VISIBLE);
		arrow_right.setEnabled(accountSize>1);
		arrow_left.setEnabled(accountSize>0&&accounts_vp.getCurrentItem()>0);
		}else{
			arrow_left.setVisibility(View.GONE);
			arrow_right.setVisibility(View.GONE);
		}
	}
	
	public List<AccountsModel> getData(){
		return accounts;
	}

	public AccountsModel getAccountsModel(){
		if(accounts!=null&&accounts_vp.getCurrentItem()<accounts.size()){
			return accounts.get(accounts_vp.getCurrentItem());
		}else {
			return null;
		}
	}
	
	private void setIndex(int counts){
		accountIndexs.setAccount(counts);
	}
	
	public void setCurrentItem(int item){
		accounts_vp.setCurrentItem(item);
	}
	
	public void setOnPageChangeListener(OnPageChangeListener mOnPageChangeListener){
		this.mOnPageChangeListener=mOnPageChangeListener;
	}

	public void setAcountAble(boolean enable){
		accounts_vp_enable=enable;
		if(enable){
			accountIndexs.setVisibility(View.VISIBLE);
			arrow_left.setVisibility(View.VISIBLE);
			arrow_right.setVisibility(View.VISIBLE);
		}else {
			accountIndexs.setVisibility(View.GONE);
			arrow_left.setVisibility(View.GONE);
			arrow_right.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.arrow_left){
			accounts_vp.setCurrentItem(accounts_vp.getCurrentItem()-1, true);
		}else if(id==R.id.arrow_right){
			accounts_vp.setCurrentItem(accounts_vp.getCurrentItem()+1, true);
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		if(mOnPageChangeListener!=null){
			mOnPageChangeListener.onPageScrollStateChanged(arg0);
		}
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if(mOnPageChangeListener!=null){
			mOnPageChangeListener.onPageScrolled(arg0,arg1,arg2);
		}
		
	}
	
	@Override
	public void onPageSelected(int arg0) {
		if(mOnPageChangeListener!=null){
			mOnPageChangeListener.onPageSelected(arg0);
		}
		arrow_right.setEnabled(arg0<accounts_vp.getAdapter().getCount()-1);
		arrow_left.setEnabled(arg0>0);
		accountIndexs.setSelected(arg0);
	}
	
	protected class MyPagerAdapter extends PagerBaseAdapter {
		Context context;
		
		public MyPagerAdapter(Context context) {
			this.context = context;
		}
		
		@Override
		public int getCount() {
			return accounts == null ? 0 : accounts.size();
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Parcelable saveState() {
			return null;
		}
		
		@Override
		public View getView(ViewGroup parent, View contentView, int position) {
			if (contentView == null) {
				AccountInfoTitle mAccountInfoTitle = (AccountInfoTitle) LayoutInflater
						.from(context).inflate(R.layout.account_info_title, null);
				mAccountInfoTitle.init(AccountInfoTitle.PAYMENT);
				contentView = mAccountInfoTitle;
			}
			AccountsModel mAccountsModel = accounts.get(position);
			AccountInfoTitle mAccountInfoTitle = (AccountInfoTitle) contentView;
			if(mAccountsModel.getIsPreferred()){
				mAccountInfoTitle.setPerferredStar(AccountInfoTitle.PAYMENT);
			}else{
				mAccountInfoTitle.isPerferredStar.setVisibility(View.GONE);
			}
			mAccountInfoTitle.accountName.setText(mAccountsModel .getAccountAlias());
			String money = Utils.generateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), mAccountsModel.getAccountBalance());
			mAccountInfoTitle.account_balance_value.setText(money);
			money = Utils.generateFormatMoney(contentView.getContext().getResources().getString(R.string.eur), mAccountsModel.getAvailableBalance());
			mAccountInfoTitle.available_balance_value.setText(money);
			return contentView;
		}
		
	}
}
