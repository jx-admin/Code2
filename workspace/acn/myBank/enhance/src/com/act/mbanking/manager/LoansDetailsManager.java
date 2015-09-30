
package com.act.mbanking.manager;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.GetFinancingInfoModel;
import com.act.mbanking.bean.InstallmentsModel;
import com.act.mbanking.logic.GetFinancingInfoJson;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.TitleViewManager;
import com.custom.view.DividerLinearLayout;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class LoansDetailsManager extends MainMenuSubScreenManager {

	TitleViewManager mTitleViewManager;

    private DividerLinearLayout info_linearview;

    private ListView recent_expandablelv;

    private ItemDetailsAdapter recent_expandablelv_ad;

    private AccountsModel accountModel;

    private Handler handler;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    public LoansDetailsManager(MainActivity activity) {
        super(activity);
        handler = new Handler();
    }

    @Override
    protected void init() {
        layout = (ViewGroup)activity.findViewById(R.id.loans_details_include);
        mTitleViewManager=new TitleViewManager(layout);
        setLeftNavigationText(R.string.loans);
        info_linearview = (DividerLinearLayout)layout.findViewById(R.id.info_linearview);
        recent_expandablelv = (ListView)layout.findViewById(R.id.recent_expandablelv);
        recent_expandablelv_ad = new ItemDetailsAdapter(activity);
        recent_expandablelv_ad.isShowOthers(true);
    }

    private void updateTitle(Context context, DividerLinearLayout cv) {
        cv.removeAllViews();
        
        android.view.ViewGroup.LayoutParams chileLayoutLp = new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        String total_loan = activity.getResources().getString(R.string.total_loan);
        double totalAmount = Double.parseDouble(getFinancingInfo.getTotalAmountl());
        String total_loan_value = Utils.formatMoney(totalAmount, activity.getResources().getString(R.string.dollar), true, true,false, false, true);
        
        cv.addView(createItemView(context, chileLayoutLp, Gravity.CENTER, total_loan,total_loan_value));
        
        String interest_rate = activity.getResources().getString(R.string.interest_rate);
        cv.addView(createItemView(context, chileLayoutLp, Gravity.CENTER,"", interest_rate +" % "+ getFinancingInfo.getRate()));
        LinearLayout BottomLin = new LinearLayout(context);
        BottomLin.setGravity(Gravity.CENTER);
        BottomLin.setLayoutParams(chileLayoutLp);
        android.view.ViewGroup.LayoutParams chileLayoutLp2 = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        String payed_loan = activity.getResources().getString(R.string.payed_loan);
        String payed_loan_value = Utils.formatMoney(accountModel.getPayedloan(), activity.getResources().getString(R.string.dollar), true, true,false, false, true);
        BottomLin.addView(createItemView(context, chileLayoutLp2, Gravity.CENTER, payed_loan,payed_loan_value));
        ImageView diviImg = new ImageView(context);
        diviImg.setScaleType(ScaleType.FIT_XY);
        diviImg.setImageResource(R.drawable.divider_vertical);
        android.widget.LinearLayout.LayoutParams dividerLp = new android.widget.LinearLayout.LayoutParams(4, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        BottomLin.addView(diviImg, dividerLp);
        String outstanding_loan = activity.getResources().getString(R.string.outstanding_loan);
        String outstanding_loan_value = Utils.formatMoney(accountModel.getOutstandingloan(), activity.getResources().getString(R.string.dollar), true, true,false, false, true);
        
        BottomLin.addView(createItemView(context, chileLayoutLp2, Gravity.CENTER, outstanding_loan,outstanding_loan_value));
        cv.addView(BottomLin);
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        mainManager.showLoans(false, null);
        reset();
        return true;
    }

   
    protected void onShow(Object object) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("view.finance.detail");
    	accountModel = (AccountsModel)object;
    	mTitleViewManager.setTitle(accountModel.getAccountAlias());
        String lastUpdate = TimeUtil.getDateString(TimeUtil.nowTimeMillis(),TimeUtil.dateFormat13);
        String time = activity.getResources().getString(R.string.last_update_on);
        time = String.format(time, lastUpdate);
        mTitleViewManager.setSubTitle(time);
       
        loadData();
    }

    GetFinancingInfoModel getFinancingInfo;

    @Override
    protected void loadData() {
        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show("", new OnProgressEvent() {
            @Override
            public void onProgress() {
                String postData = GetFinancingInfoJson.getFinancingInfoReportProtocal(Contants.publicModel, accountModel.getAccountCode(),accountModel.getFinanceType());
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,activity);
                final GetFinancingInfoModel getFinancingInfo = GetFinancingInfoJson.paresgetFinancingInfoResponse(httpResult);
                if (getFinancingInfo != null) {
                    if (getFinancingInfo.responsePublicModel.isSuccess()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setGetFinancingInfo(getFinancingInfo);
                                updateTitle(activity, info_linearview);
                                recent_expandablelv_ad.list = getFinancingInfo.getInstallments();
                                recent_expandablelv.setAdapter(recent_expandablelv_ad);
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    protected void setUI() {
    }

    /**
     * @param context
     * @param gravity
     * @param title
     * @param content
     * @return
     */
    public View createItemView(Context context, android.view.ViewGroup.LayoutParams layoutParams,int gravity, String title, String content) {
        LinearLayout chileLayout_lin =(LinearLayout) LayoutInflater.from(context).inflate(R.layout.linear_margin_model, null);
        chileLayout_lin.setOrientation(LinearLayout.VERTICAL);
        chileLayout_lin.setLayoutParams(layoutParams);
        chileLayout_lin.setGravity(gravity);
        TextView chileTitle_tv = new TextView(context);
        chileTitle_tv.setTextSize(9);
        chileTitle_tv.setTextColor(Color.WHITE);
        chileTitle_tv.setGravity(Gravity.CENTER);
        if(TextUtils.isEmpty(title)){
        	chileTitle_tv.setVisibility(View.GONE);
        }else{
        	chileTitle_tv.setText(title);
        }
        TextView chileContent_tv = new TextView(context);
        chileContent_tv.setGravity(Gravity.CENTER);
        chileContent_tv.setTextColor(Color.WHITE);
        if(TextUtils.isEmpty(content)){
        	chileContent_tv.setVisibility(View.GONE);
        }else{
        	chileContent_tv.setText(content);
        }
        chileLayout_lin.addView(chileTitle_tv);
        chileLayout_lin.addView(chileContent_tv);

        return chileLayout_lin;
    }

    class ItemDetailsAdapter extends BaseAdapter {
        LayoutInflater lf;

        Context context;

        List<InstallmentsModel> list;

        public boolean enable = true;

        boolean isShowOthers = false;;

        public ItemDetailsAdapter(Context context) {
            this.context = context;
            lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        public void isShowOthers(boolean show) {
            isShowOthers = show;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (list != null) {
                return list.get(position);
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
//                convertView.setOnClickListener(itemClicker);
                hodle = new Holde();
                hodle.tv_1 = (TextView)convertView.findViewById(R.id.tv_1);
                hodle.tv_2 = (TextView)convertView.findViewById(R.id.tv_2);
                hodle.tv_3 = (TextView)convertView.findViewById(R.id.tv_3);
                hodle.selector_iv = (ImageView)convertView.findViewById(R.id.selector_iv);
                hodle.details_lin = (LinearLayout)convertView.findViewById(R.id.details_lin);
                convertView.setTag(hodle);
            }
            hodle = (Holde)convertView.getTag();

            String name = null, value = null, present = null;
            int fixedPosition = position;
            if (list != null) {
                InstallmentsModel mInstallmentsModel;
                if (fixedPosition < list.size()) {
                    mInstallmentsModel = list.get(fixedPosition);
                    name = TimeUtil.changeFormattrString(mInstallmentsModel.getDeadlineDate(),TimeUtil.dateFormat2, TimeUtil.dateFormat13);
                    value = mInstallmentsModel.getInstallmentType();
                    present = Utils.formatMoney(mInstallmentsModel.getAmount(), context.getResources().getString(R.string.dollar), true, true,false, false, true);
                    
                    convertView.setClickable(true);
                    hodle.tv_1.setVisibility(View.VISIBLE);
                    hodle.tv_3.setVisibility(View.VISIBLE);
                    hodle.selector_iv.setVisibility(View.VISIBLE);
                    convertView.setBackgroundResource(R.drawable.list_element_closed);
                    if (mInstallmentsModel.getPaidState().equals("N")) {
                        hodle.tv_3.setTextColor(0xffB6193F);
                    } else {
                        hodle.tv_3.setTextColor(0xff3BA976);
                    }
                } else if (isShowOthers && fixedPosition == list.size()) {
                    value = "other payments";
                    hodle.tv_1.setVisibility(View.GONE);
                    hodle.tv_3.setVisibility(View.GONE);
                    hodle.selector_iv.setVisibility(View.GONE);
                    convertView.setBackgroundColor(0xffD7D7D7);
                }
            }

            if (name == null) {
                name = "";
            }
            if (value == null) {
                value = "";
            }
            if (present == null) {
                present = "";
            }
            hodle.tv_1.setText(name);
            hodle.tv_2.setText(value);
            hodle.tv_3.setText(present);
            hodle.position = position;
            if (isShowOthers && hodle.position == list.size()) {
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

    }

    public int selectPosition = -1;

    ItemDetailsAdapter.Holde selected;
    OnClickListener itemClicker = new OnClickListener() {

        public void onClick(View v) {
            ItemDetailsAdapter.Holde hodle = (ItemDetailsAdapter.Holde)v.getTag();
            if (hodle.details_tv.getVisibility() == View.VISIBLE) {
                hodle.selector_iv.setImageResource(R.drawable.arrow_down);
                hodle.details_tv.setVisibility(View.GONE);
                selected = null;
                selectPosition = -1;
            } else {
            	resetSelected();
                hodle.selector_iv.setImageResource(R.drawable.arrow_up);
                hodle.details_tv.setVisibility(View.VISIBLE);
                selectPosition = hodle.position;
                selected = hodle;
            }

        }
    };
    
    private void resetSelected(){
    	if (selected != null) {
            selected.details_tv.setVisibility(View.GONE);
            selected.selector_iv.setImageResource(R.drawable.arrow_down);
        }
    }

    private void reset() {
    	resetSelected();
        recent_expandablelv_ad.list = null;
    }
    
    /**
     * @return the getFinancingInfo
     */
    public GetFinancingInfoModel getGetFinancingInfo() {
        return getFinancingInfo;
    }

    /**
     * @param getFinancingInfo the getFinancingInfo to set
     */
    public void setGetFinancingInfo(GetFinancingInfoModel getFinancingInfo) {
        this.getFinancingInfo = getFinancingInfo;
    }
}
