
package com.act.mbanking.manager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.AggregatedAccount;
import com.act.mbanking.bean.ChartModel;
import com.act.mbanking.bean.GetMovementsModel;
import com.act.mbanking.bean.MovementsModel;
import com.act.mbanking.logic.GetMovementsJson;
import com.act.mbanking.manager.AccountDetailsManager.ItemDetailsAdapter.Holde;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.AccountChartView;
import com.act.mbanking.view.AccountChartView.PointLD;
import com.act.mbanking.view.TitleViewManager;
import com.custom.view.DoubleShadowTextView;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class AccountDetailsManager extends MainMenuSubScreenManager{
	
	public static final String TAG="AccountDetailsManager";

	TitleViewManager mTitleViewManager;

    private TabHost tabhost;

    private ListView all_lv, depositsDatas_lv, withdrawals_lv;

    private ItemDetailsAdapter all_ad, depositsDatas_ad, withdrawals_ad;

    private List<MovementsModel> allDatas = new ArrayList<MovementsModel>();

    private List<MovementsModel> depositsDatas = new ArrayList<MovementsModel>();

    private List<MovementsModel> withdrawals = new ArrayList<MovementsModel>();

    private static String TAG_ALL, TAG_DEPOSITS, TAG_WITHDRAWALS;
    
    private com.act.mbanking.view.AccountChartView mAccountChartView;

    private Handler handler;

    private String restartingKey;

    private AccountsModel accountModel;

    int recordCount = 20;

    private boolean isNeedUpdata = true;

    private List<AccountChartView.PointLD> points = new ArrayList<AccountChartView.PointLD>();
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    public AccountDetailsManager(MainActivity activity) {
        super(activity);
        handler = new Handler();
    }

    @Override
    protected void init() {

        TAG_ALL = activity.getString(R.string.all);
        TAG_DEPOSITS = activity.getString(R.string.deposits);
        TAG_WITHDRAWALS = activity.getString(R.string.withdrawals);

        layout = (ViewGroup)activity.findViewById(R.id.account_details_include);
        mTitleViewManager=new TitleViewManager(layout);
        setLeftNavigationText(R.string.back);

        mAccountChartView = (AccountChartView)layout.findViewById(R.id.account_chartview);
        mAccountChartView.line=2;

        tabhost = (TabHost)layout.findViewById(android.R.id.tabhost);
        tabhost.setup();

        all_lv = (ListView)layout.findViewById(R.id.tab1).findViewById(R.id.expandableListView1);
        depositsDatas_lv = (ListView)layout.findViewById(R.id.tab2).findViewById(
                R.id.expandableListView1);
        withdrawals_lv = (ListView)layout.findViewById(R.id.tab3).findViewById(
                R.id.expandableListView1);
        all_ad = new ItemDetailsAdapter(activity);
        depositsDatas_ad = new ItemDetailsAdapter(activity);
        withdrawals_ad = new ItemDetailsAdapter(activity);
        all_ad.isShowOthers(true);
        all_lv.setAdapter(all_ad);
        depositsDatas_lv.setAdapter(depositsDatas_ad);
        withdrawals_lv.setAdapter(withdrawals_ad);

        LayoutInflater inflater_tab1 = LayoutInflater.from(activity);
        DoubleShadowTextView title = (DoubleShadowTextView)inflater_tab1.inflate(
                R.layout.tabhost_title_model, null);
        title.setShadowColorDefault(0xffffffff);
        title.setShadowColorSelected(0xff000000);
        title.setText(TAG_ALL);
        tabhost.addTab(tabhost.newTabSpec(TAG_ALL).setIndicator(title).setContent(R.id.tab1));
        title = (DoubleShadowTextView)inflater_tab1.inflate(R.layout.tabhost_title_model, null);
        title.setShadowColorDefault(0xffffffff);
        title.setShadowColorSelected(0xff000000);
        title.setText(TAG_DEPOSITS);
        tabhost.addTab(tabhost.newTabSpec(TAG_DEPOSITS).setIndicator(title).setContent(R.id.tab2));
        title = (DoubleShadowTextView)inflater_tab1.inflate(R.layout.tabhost_title_model, null);
        title.setShadowColorDefault(0xffffffff);
        title.setShadowColorSelected(0xff000000);
        title.setText(TAG_WITHDRAWALS);
        tabhost.addTab(tabhost.newTabSpec(TAG_WITHDRAWALS).setIndicator(title)
                .setContent(R.id.tab3));
        ImageButton paymentsBtn = (ImageButton) layout.findViewById(R.id.account_payments);
        paymentsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NPaymentsManager.startForRecover(mainManager, PaymentsManager.FORM_ACCOUNT, accountModel, null, System.currentTimeMillis(), 0d, 0);
        	}
		});
//        tabhost.addTab(tabhost.newTabSpec(PAYMENTRECOVER).setIndicator(paymentsBtn) .setContent(R.id.tab4));
        
        tabhost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                resetSeleted();
            }
        });
    }

    private void test() {
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {

        mainManager.showAccountLevel2(true, null);
        return true;
    }

    @Override
    protected void onShow(Object object) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("event.account.transaction");
    	reset();
    	isNeedUpdata = true;
    	accountModel = (AccountsModel)object;
    	mTitleViewManager.setTitle(accountModel.getAccountAlias());
        loadData();
    }

    GetMovementsModel getMovements;

    @Override
    protected void loadData() {
        loadData(recordCount);
    }

    void loadData(final int count) {
        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show("", new OnProgressEvent() {
            public void onProgress() {
                String postData = GetMovementsJson.getMovementsReportProtocal(Contants.publicModel,
                        "2", accountModel.getAccountCode(), 1, count, restartingKey);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        activity);
                final GetMovementsModel getMovements = GetMovementsJson
                        .parseGetMovementsResponse(httpResult);
                if (getMovements.getMovements() != null && getMovements.getMovements().size() > 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setRestartingKey(getMovements.responsePublicModel.getRestartingKey());
                            setGetMovements(getMovements);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void setUI() {

    }

    /**
     * @return the restartingKey
     */
    public String getRestartingKey() {
        return restartingKey;
    }

    /**
     * @param restartingKey the restartingKey to set
     */
    public void setRestartingKey(String restartingKey) {
        this.restartingKey = restartingKey;
    }

    class ItemDetailsAdapter extends BaseAdapter {
        LayoutInflater lf;

        Context context;

        private int othersId = 4;

        List<MovementsModel> datas;

        // List<String> list;
        //
        // List<String> list2;

        boolean isShowOthers = false;;

        public boolean enable = true;

        public ItemDetailsAdapter(Context context) {
            this.context = context;
            lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // list = new ArrayList<String>();
            // list2 = new ArrayList<String>();
            // for (int i = 0; i < 10; i++) {
            // list.add("item-" + i);
            // list2.add("item2-" + i);
            // }

        }

        public void isShowOthers(boolean show) {
            isShowOthers = show;
        }

        // @Override
        // public void unregisterDataSetObserver(DataSetObserver observer) {
        // // TODO Auto-generated method stub
        //
        // }
        //
        // @Override
        // public void registerDataSetObserver(DataSetObserver observer) {
        // // TODO Auto-generated method stub
        //
        // }

        public boolean isEnabled(int position) {
            if (othersId == position) {
                return false;
            }
            return true;
        }

        @Override
        public boolean isEmpty() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (datas != null) {
                count = datas.size();
            }
            if (isShowOthers && count > 0) {
                ++count;
            }
            return count;
        }

        @Override
        public Object getItem(int position) {
            if (datas != null && position < datas.size()) {
                return datas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {      
            Holde hodle = null;
            if (convertView == null) {
                convertView = lf.inflate(R.layout.creditcards_details_group_item, null);
                convertView.setOnClickListener(itemClicker);
                hodle = new Holde();
                hodle.tv_1 = (TextView)convertView.findViewById(R.id.tv_1);
                hodle.tv_2 = (TextView)convertView.findViewById(R.id.tv_2);
                hodle.tv_2.setSingleLine();
                hodle.tv_3 = (TextView)convertView.findViewById(R.id.tv_3);
                hodle.selector_iv = (ImageView)convertView.findViewById(R.id.selector_iv);
                hodle.details_lin = (LinearLayout)convertView.findViewById(R.id.details_lin);
                hodle.details_tv = (TextView)convertView.findViewById(R.id.details_tv);
                hodle.details_tv2 = (TextView)convertView.findViewById(R.id.details2_tv);
                hodle.details_tv3 = (TextView)convertView.findViewById(R.id.details3_tv);
                hodle.details_tv4 = (TextView)convertView.findViewById(R.id.details4_tv);
                convertView.setTag(hodle);
            }
            hodle = (Holde)convertView.getTag();

            String name = null, value = null, present = null, details = null, details2 = null, details3 = null, details4 = null;
            int fixedPosition = position;
            if (datas != null) {
                MovementsModel mMovementsModel;
                if (fixedPosition < datas.size()) {
                    mMovementsModel = datas.get(fixedPosition);
                    name = TimeUtil.changeFormattrString(mMovementsModel.getValueDate(),
                            TimeUtil.dateFormat2, TimeUtil.dateFormat3);
                    value = mMovementsModel.getDescription();
                    present = Utils.formatMoney(mMovementsModel.getAmount(),activity.getResources().getString(R.string.dollar), true, true,false, false, true);
                    details = value;
                    details2 = Contants.EMPTY;
                    details3 = String.format(context.getString(R.string.record_date_s__), name);
                    details4 = String.format(context.getString(R.string.currentcy_rate_s__),
                            TimeUtil.changeFormattrString(mMovementsModel.getOperationDate(),
                                    TimeUtil.dateFormat2, TimeUtil.dateFormat3));

                    convertView.setClickable(true);
                    hodle.tv_1.setVisibility(View.VISIBLE);
                    hodle.tv_3.setVisibility(View.VISIBLE);
                    hodle.selector_iv.setVisibility(View.VISIBLE);
                    convertView.setBackgroundResource(R.drawable.list_element_closed);
                    if (mMovementsModel.getAmount() < 0) {
                        hodle.tv_3.setTextColor(0xffB6193F);
                    } else {
                        hodle.tv_3.setTextColor(0xff3BA976);
                    }
                } else if (isShowOthers && fixedPosition == datas.size()) {
                    value = context.getString(R.string.other_transactions);
                    hodle.tv_1.setVisibility(View.GONE);
                    hodle.tv_3.setVisibility(View.GONE);
                    hodle.selector_iv.setVisibility(View.GONE);
                    convertView.setBackgroundResource(R.drawable.filter_off_center);
                }
            }

            if (name == null) {
                name = Contants.EMPTY;
            }
            if (value == null) {
                value = Contants.EMPTY;
            }
            if (present == null) {
                present = Contants.EMPTY;
            }
            if (details == null) {
                details = Contants.EMPTY;
            }
            if (details2 == null) {
                details2 = Contants.EMPTY;
            }
            if (details3 == null) {
                details3 = Contants.EMPTY;
            }
            if (details4 == null) {
                details4 = Contants.EMPTY;
            }
            hodle.tv_1.setText(name);
            hodle.tv_2.setText(value);
            hodle.tv_3.setText(present);
            hodle.details_tv.setText(details);
            hodle.details_tv2.setText(details2);
            hodle.details_tv2.setVisibility(View.GONE);
            hodle.details_tv3.setText(details3);
            hodle.details_tv4.setText(details4);
            hodle.position = position;
            if (isShowOthers && hodle.position == datas.size()) {
                hodle.position = -2;
            }
            if (selectPosition == position) {
                hodle.details_lin.setVisibility(View.VISIBLE);
            } else {
                hodle.details_lin.setVisibility(View.GONE);
            }
            return convertView;
        }

        class Holde {
            TextView tv_1;

            TextView tv_2;

            TextView tv_3;

            ImageView selector_iv;

            LinearLayout details_lin;

            TextView details_tv;

            TextView details_tv2;

            TextView details_tv3;

            TextView details_tv4;

            int position;
        }

        public void setDatas(List<MovementsModel> movements) {
            this.datas = movements;

        }
    }

    public int selectPosition = -1;

    Holde selected;
    OnClickListener itemClicker = new OnClickListener() {

        public void onClick(View v) {
            Holde hodle = (Holde)v.getTag();
            if (hodle.details_lin.getVisibility() == View.VISIBLE) {
                hodle.selector_iv.setImageResource(R.drawable.arrow_down);
                hodle.details_lin.setVisibility(View.GONE);
                selected = null;
                selectPosition = -1;
            } else {
            	resetSeleted();
                if (hodle.position == -2) {
                    loadData(recordCount);
                } else {
                    hodle.selector_iv.setImageResource(R.drawable.arrow_up);
                    hodle.details_lin.setVisibility(View.VISIBLE);
                    selectPosition = hodle.position;
                    selected = hodle;
                }
            }

        }
    };
    private void resetSeleted(){
    	if (selected != null) {
            selected.details_lin.setVisibility(View.GONE);
            selected.selector_iv.setImageResource(R.drawable.arrow_down);
            selected = null;
            selectPosition = -1;
        }
    }

    /**
     * @return the getMovements
     */
    public GetMovementsModel getGetMovements() {
        return getMovements;
    }

    /**
     * @param getMovements the getMovements to set
     */
    public void setGetMovements(GetMovementsModel getMovements) {
        this.getMovements = getMovements;
        long startTime=Long.MAX_VALUE;long endTime=Long.MIN_VALUE,tmpTime=0;
        for (MovementsModel child : getMovements.getMovements()) {
            if (child.getAmount() < 0) {
                withdrawals.add(child);
            } else {
                depositsDatas.add(child);
            }
            
            try {
				tmpTime=TimeUtil.getTimeByString(child.getValueDate(), TimeUtil.dateFormat2);
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}
            startTime=Math.min(startTime, tmpTime);
            endTime=Math.max(endTime, tmpTime);
        }
//        if(isNeedUpdata){
//        	points.clear();
//        	generateHashMapAndOutList( getMovements.getMovements(),points);
        	getAccountChars(accountModel, startTime, endTime, points);
//        }
        allDatas.addAll(getMovements.getMovements());
        all_ad.setDatas(allDatas);
        all_ad.notifyDataSetChanged();
        depositsDatas_ad.setDatas(depositsDatas);
        depositsDatas_ad.notifyDataSetChanged();
        withdrawals_ad.setDatas(withdrawals);
        withdrawals_ad.notifyDataSetChanged();
        mAccountChartView.setData(points);
//        mTitleViewManager.setSubTitle(String.format(activity.getString(R.string.account_details_sutitle),
//                TimeUtil.getDateString(points.get(0).x, TimeUtil.dateFormat10),
//                TimeUtil.getDateString(points.get(points.size() - 1).x, TimeUtil.dateFormat10)));
        mTitleViewManager.setSubTitle(String.format(activity.getString(R.string.account_details_sutitle),
                TimeUtil.getDateString(startTime, TimeUtil.dateFormat10),
                TimeUtil.getDateString(endTime, TimeUtil.dateFormat10)));
        if (points.size() > 0) {
            isNeedUpdata = false;
        }
    }

    private void reset() {
        points.clear();
        allDatas.clear();
        depositsDatas.clear();
        withdrawals.clear();
        restartingKey = null;
        resetSeleted();
    }
    HashMap<Long,Double> MovementsModelDatasByDay=new HashMap<Long,Double>();
    
    Comparator  movementsModelComparable=new Comparator<MovementsModel>() {
    	
    @Override
    public int compare(MovementsModel one,MovementsModel another) {

        if (another == null) {
            return 0;
        }
        try {
            long anotherTime = TimeUtil.getMillis(another.getValueDate());
            long thisTime = TimeUtil.getTimeByString(one.getValueDate(), TimeUtil.dateFormat2);
            if (thisTime > anotherTime) {
                return 1;
            } else if (thisTime < anotherTime) {
                return -1;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }
    };
    /**
     * 比较要时间，请确保只运行一次 把list里的数据通过天来合并起来
     * 
     * @param srcList
     * @return
     */
	public void generateHashMapAndOutList(List<MovementsModel> list,
			List<PointLD> points) {

		MovementsModel movementsModel;
		Double curDobule = 0d;
		// Collections.sort(list, movementsModelComparable);
		try{
		for (int i = 0; i < list.size(); i++) {
			movementsModel = list.get(i).clone();
			Log.d(TAG,"src:"+movementsModel.getValueDate()+" "+movementsModel.getAmount());
	        Calendar fromCalendar = Calendar.getInstance();
	        fromCalendar.setTimeInMillis(TimeUtil.getTimeByString(movementsModel.getValueDate(),
					TimeUtil.dateFormat2));
	        fromCalendar.set(Calendar.HOUR_OF_DAY, 12);
	        fromCalendar.set(Calendar.MINUTE, 0);
	        fromCalendar.set(Calendar.SECOND, 0);
	        fromCalendar.set(Calendar.MILLISECOND, 0);
			Long key = fromCalendar.getTimeInMillis();
			if (MovementsModelDatasByDay.containsKey(key)) {
				curDobule = MovementsModelDatasByDay.get(key);
				curDobule += movementsModel.getAmount();
			} else {
				curDobule = movementsModel.getAmount();
			}
			MovementsModelDatasByDay.put(key, curDobule);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		Iterator cIterator = MovementsModelDatasByDay.entrySet().iterator();
		while (cIterator.hasNext()) {
			Map.Entry entry = (Entry) cIterator.next();
			points.add(0, new AccountChartView.PointLD((Long) entry.getKey(),
					(Double) entry.getValue()));
			Log.d(TAG,"des:"+TimeUtil.getDateString((Long) entry.getKey(), TimeUtil.dateFormat2)+" "+(Double) entry.getValue());
		}
		Collections.sort(points);
	}
	
	/**
     * 比较要时间，请确保只运行一次 把list里的数据通过天来合并起来
     * 
     * @param srcList
     * @return
     */
	public void getAccountChars(AccountsModel accountsModel,long startTime,long endTime, List<PointLD> points) {
		if (accountsModel != null) {
			List<AggregatedAccount> mAggregatedAccounts = accountsModel .getChartAggregatedAccountsList();
			List<ChartModel> mChartModels = null;
			if (mAggregatedAccounts != null && mAggregatedAccounts.size() > 0) {
				AggregatedAccount mAggregatedAccount = mAggregatedAccounts .get(0);
				mChartModels = mAggregatedAccount.getCharts();
			}
			if (mChartModels != null) {
				int chartModel_size = mChartModels.size();
				ChartModel mChartModel;
				for (int chartIndex = 0; chartIndex < chartModel_size; chartIndex++) {
					mChartModel = mChartModels.get(chartIndex);
					long chartTM = mChartModel.getTimeMills();
//					if (chartTM >= startTime && chartTM <= endTime) {
						points.add(new AccountChartView.PointLD(chartTM, mChartModel.getValue()));
//					}
				}
			}
			Collections.sort(points);
		}
	}
}
