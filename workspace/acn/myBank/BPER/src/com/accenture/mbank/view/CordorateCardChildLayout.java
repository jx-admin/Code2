package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.accenture.mbank.MainActivity;
import com.accenture.mbank.AccountDetailActivity.RecordAdapter.TYPE;
import com.accenture.mbank.logic.GetMovementsJson;
import com.accenture.mbank.model.AdditionalCard;
import com.accenture.mbank.model.GetMovementsModel;
import com.accenture.mbank.model.MovementsModel;
import com.accenture.mbank.model.SettingModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.AmountComparator;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.TimeUtil;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.CustomScrollView.OnScrollStoppedListener;

public class CordorateCardChildLayout extends ExpandedContainer implements OnCheckedChangeListener , OnProgressEvent,OnScrollStoppedListener{

	RadioButton details;
	RadioButton movements;

	LinearLayout corporate_detail;
	LinearLayout corporate_movement;

	LinearLayout childListView;
	Context mContext;

	public TextView owner_value, card_state_value, card_number_value,expiration_value, plafond_value;
	private Handler mHandler;
	private String restartingKey;
	private String restartingDate;
	private AdditionalCard mAdditionalCard;
	private List<MovementsModel> movementsList;
	
	private int listSize=0;
	private LinearLayout card_list_view;
	private CustomScrollView scrollView;
	
	private TextView mEndLoadTipsTextView;
	private ProgressBar mEndLoadProgressBar;
	
	public CordorateCardChildLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		mHandler = new Handler();
	}

	public void init(LinearLayout card_list_view, CustomScrollView scrollView) {
		this.card_list_view = card_list_view;
		this.scrollView = scrollView;
		movements = (RadioButton) findViewById(R.id.movements);
		movements.setOnCheckedChangeListener(this);
		details = (RadioButton) findViewById(R.id.details);
		details.setOnCheckedChangeListener(this);

		corporate_movement = (LinearLayout) findViewById(R.id.corporate_movement);
		corporate_detail = (LinearLayout) findViewById(R.id.corporate_detail);

		childListView = (LinearLayout) findViewById(R.id.child_item_list_view);

		owner_value = (TextView) findViewById(R.id.owner_value);
		card_state_value = (TextView) findViewById(R.id.card_state_value);
		card_number_value = (TextView) findViewById(R.id.card_number_value);
		expiration_value = (TextView) findViewById(R.id.expiration_value);
		plafond_value = (TextView) findViewById(R.id.plafond_value);
		
		scrollView.setOnScrollStoppedListener(this);
	}
	
	public void setAdditionalCard(AdditionalCard additionalCard){
		this.mAdditionalCard = additionalCard;
	}
	
	private void loadData(final String restartingKey,boolean isLoadMore) {
		ProgressOverlay progressOverlay = new ProgressOverlay(mContext);
		if(isLoadMore){
			progressOverlay.runBackground(this);
		}else {
			progressOverlay.show("",this);
		}
	}
	
	@Override
	public void onExpand() {
		super.onExpand();
		if(movementsList == null || movementsList.size() == 0)
			loadData(getRestartingKey(),false);
	}
	

	public void setRecords(List<MovementsModel> list, String moreValues) {
		if(moreValues.equalsIgnoreCase("true")){
			scrollView.setOnScrollStoppedListener(this);
		} else{
			scrollView.setOnScrollStoppedListener(null);
		}

		if (childListView.getChildCount()>0) {
			childListView.removeViewAt(childListView.getChildCount()-1);
		}
		
		owner_value.setText(mAdditionalCard.getCardHolder());
		card_state_value.setText(mAdditionalCard.getCardState());
		card_number_value.setText(mAdditionalCard.getCardNumber());
		String expireationDate = TimeUtil.changeFormattrString(mAdditionalCard.getCardExpiredDate(), TimeUtil.dateFormat2,TimeUtil.dateFormat_mm_yy);
		expiration_value.setText(expireationDate);
		String plafondStr = Utils.notPlusGenerateFormatMoney(getResources().getString(R.string.eur), mAdditionalCard.getCardPlafond());
		plafond_value.setText(plafondStr);
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		for (MovementsModel movementsModel : list) {
			
			DateDescriptionCorporateAmountItem item = (DateDescriptionCorporateAmountItem) inflater.inflate(R.layout.date_desc_corporate_amount_item, null);
			item.init(childListView,listSize,mContext);
			item.hideCurrencyDate();
			item.toggleButton.setBackgroundResource(R.drawable.cards_expand_selector);
			item.setType(TYPE.CARDS);
			item.setModel(movementsModel);
			childListView.addView(item);
		}
		View mEndRootView = inflater.inflate(R.layout.listfooter_more, null);
		mEndRootView.setVisibility(View.VISIBLE);
		mEndLoadTipsTextView = (TextView) mEndRootView
				.findViewById(R.id.load_more);
		mEndLoadProgressBar = (ProgressBar) mEndRootView
				.findViewById(R.id.pull_to_refresh_progress);
		if (moreValues.equalsIgnoreCase("false")) {
    		mEndLoadTipsTextView.setText(R.string.p2refresh_head_no_load_more);
		} else{
			mEndLoadTipsTextView.setText(R.string.p2refresh_head_load_more);
		}
		childListView.addView(mEndRootView);
	}

	
	/**
	 * @return the restartingKey
	 */
	public String getRestartingKey() {
		return restartingKey;
	}

	/**
	 * @param restartingKey
	 *            the restartingKey to set
	 */
	public void setRestartingKey(String restartingKey) {
		this.restartingKey = restartingKey;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int type = 0;
		if (isChecked) {
			if (buttonView == movements) {
				type = 0;
			} else if (buttonView == details) {
				type = 1;
			}
		showType(type);
	  }
	}
	
	private void showType(int type) {
		  switch (type) {
			  case 0:
				  corporate_movement.setVisibility(View.VISIBLE);
				  childListView.setVisibility(View.VISIBLE);
				  corporate_detail.setVisibility(View.GONE);
				  scrollView.setOnScrollStoppedListener(this);
			   break;
			  case 1:
				  corporate_movement.setVisibility(View.GONE);
				  childListView.setVisibility(View.GONE);
				  corporate_detail.setVisibility(View.VISIBLE);
				  scrollView.setOnScrollStoppedListener(null);
			   break;
			  default:
			   break;
		  }
	}

	@Override
	public void onProgress() {
		
		if (mEndLoadTipsTextView != null) {
			Activity activity = (Activity)mContext;
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
					mEndLoadProgressBar.setVisibility(View.VISIBLE);
				}
			});
			
		}
		
		final int transactionBy = MainActivity.setting.getShowTransactionBy();
		String postData = GetMovementsJson.getMovementsReportProtocal(Contants.publicModel,"1",mAdditionalCard.getCardAccountCode(),MainActivity.setting.getOrderListFor(), transactionBy,restartingKey,restartingDate);
		HttpConnector httpConnector = new HttpConnector();
		String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, mContext);
		if (httpResult != null) {
			final GetMovementsModel getMovements = GetMovementsJson.parseGetMovementsResponse(httpResult);
			if (getMovements.getMovements() != null && getMovements.getMovements().size() > 0) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						setRestartingKey(getMovements.responsePublicModel.getRestartingKey());
						setRestartingDate(getMovements.responsePublicModel.getRestartingDate());
						if (transactionBy == SettingModel.LAST_2_MONTH) {
						
						}
						movementsList = getMovements.getMovements();
						if (MainActivity.setting.getOrderListFor()==SettingModel.SORT_AMOUNT_DESC) {
							Collections.sort(movementsList, new AmountComparator());
						}
						
						setRecords(movementsList,getMovements.getMoreValues());

					}
				});
			} else {
				if (mEndLoadTipsTextView != null) {
					Activity activity = (Activity)mContext;
					activity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							mEndLoadTipsTextView.setText(R.string.p2refresh_head_load_more);
							mEndLoadProgressBar.setVisibility(View.GONE);
						}
					});
					
				}
				scrollView.setOnScrollStoppedListener(this);
			}
		} else{
			if (mEndLoadTipsTextView != null) {
				Activity activity = (Activity)mContext;
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mEndLoadTipsTextView.setText(R.string.p2refresh_head_load_more);
						mEndLoadProgressBar.setVisibility(View.GONE);
					}
				});
				
			}
			scrollView.setOnScrollStoppedListener(this);
		}
		
	}

	public String getRestartingDate() {
		return restartingDate;
	}

	public void setRestartingDate(String restartingDate) {
		this.restartingDate = restartingDate;
	}
	
	private void manageLoadMore() {
		int pointOffset = 100;
    	int pointDown = scrollView.getHeight();
    	int currentPoint = scrollView.getCurrentY();
    	int lenghtScrollContent = card_list_view.getHeight();

    	int total = currentPoint + pointDown;

    	if (total >= lenghtScrollContent - pointOffset) {
    		scrollView.setOnScrollStoppedListener(null);
    		loadData(getRestartingKey(),true);
    	}
	}

	@Override
	public void onScrollStopped() {
		manageLoadMore();
	}
}
