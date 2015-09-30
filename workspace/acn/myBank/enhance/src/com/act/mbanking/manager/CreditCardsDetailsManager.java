
package com.act.mbanking.manager;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.ServiceCode;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.GetMovementsModel;
import com.act.mbanking.bean.MovementsModel;
import com.act.mbanking.logic.GetMovementsJson;
import com.act.mbanking.manager.CreditCardsDetailsManager.ItemDetailsAdapter.Holde;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.custom.view.DoubleShadowTextView;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class CreditCardsDetailsManager extends MainMenuSubScreenManager {

    private TextView top_title_tv, title_tv, sub_title_tv;

    private TabHost tabhost;

    private ListView expandableListView1;

    private TextView credit_card_name_tv, last_update_content_tv, expiration_date_content_tv,
    plafond_title_tv,plafond_content_tv,state_content_tv,card_number_content_tv;

    private ItemDetailsAdapter recent_expandablelv_ad;

    private Handler handler;

    private String restartingKey;

    private AccountsModel accountModel;

    GetMovementsModel getMovements;
    
    private GoogleAnalytics mGaInstance;
    
  	private Tracker mGaTracker1;

    public CreditCardsDetailsManager(MainActivity activity) {
        super(activity);
        handler = new Handler();
    }

    @Override
    protected void init() {
        layout = (ViewGroup)activity.findViewById(R.id.card_details_include);
        setLeftNavigationText(R.string.back);
        top_title_tv = (TextView)layout.findViewById(R.id.top_title_tv);
        title_tv = (TextView)layout.findViewById(R.id.title_tv);
        title_tv.setText("Credit Cards1");
        sub_title_tv = (TextView)layout.findViewById(R.id.sub_title_tv);
        sub_title_tv.setText("From:Oct.27th To:Nov.27th");
        tabhost = (TabHost)layout.findViewById(android.R.id.tabhost);
        tabhost.setup();
        expandableListView1 = (ListView)layout.findViewById(R.id.expandableListView1);
        credit_card_name_tv = (TextView)layout.findViewById(R.id.credit_card_name_tv);
        state_content_tv=(TextView)layout.findViewById(R.id.state_content_tv);
        card_number_content_tv=(TextView)layout.findViewById(R.id.card_number_content_tv);
        
        last_update_content_tv = (TextView)layout.findViewById(R.id.last_update_content_tv);
        expiration_date_content_tv = (TextView)layout.findViewById(R.id.expiration_content_tv);
        plafond_content_tv = (TextView)layout.findViewById(R.id.plafond_content_tv);
        plafond_title_tv=(TextView)layout.findViewById(R.id.plafond_title_tv);
        recent_expandablelv_ad = new ItemDetailsAdapter(activity);
        LayoutInflater inflater_tab1 = LayoutInflater.from(activity);
        DoubleShadowTextView title = (DoubleShadowTextView)inflater_tab1.inflate(
                R.layout.tabhost_title_model, null);
        title.setShadowColorDefault(0xffffffff);
        title.setShadowColorSelected(0xff000000);
        title.setText(activity.getString(R.string.transactions));
        tabhost.addTab(tabhost.newTabSpec(activity.getString(R.string.transactions))
                .setIndicator(title).setContent(R.id.tab1));
        title = (DoubleShadowTextView)inflater_tab1.inflate(R.layout.tabhost_title_model, null);
        title.setShadowColorDefault(0xffffffff);
        title.setShadowColorSelected(0xff000000);
        title.setText(activity.getString(R.string.card_details));
        tabhost.addTab(tabhost.newTabSpec(activity.getString(R.string.card_details))
                .setIndicator(title).setContent(R.id.tab2));
        tabhost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            	resetSelected();
            }
        });
    }

    private void setCardsDetailsText(String card_state,String cardNumber,String cardName, String lastUpdate, String expirationDate,
            String plafond) {
        state_content_tv.setText(card_state);
        card_number_content_tv.setText(cardNumber);
        credit_card_name_tv.setText(cardName);
        last_update_content_tv.setText(lastUpdate);
        expiration_date_content_tv.setText(expirationDate);
        if(accountModel.getBankServiceType().getBankServiceCode().equals(ServiceCode.PREPAID_CARD_CODE)){
        }else if(accountModel.getBankServiceType().getBankServiceCode().equals(ServiceCode.CREDIT_CARD_CODE)){
            plafond_content_tv.setText(plafond);
        }
       
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        mainManager.showCardsLevel2(true, null);
        destroyed();
        return true;
    }

    @Override
    protected void onShow(Object object) {
    	mGaInstance = GoogleAnalytics.getInstance(activity);
        mGaTracker1 = mGaInstance.getTracker(Contants.TRACKER_ID);
        mGaTracker1.sendView("event.card.transaction");
        reset();
        accountModel = (AccountsModel)object;
        title_tv.setText(accountModel.getAccountAlias());
        if(accountModel.getBankServiceType().getBankServiceCode().equals(ServiceCode.PREPAID_CARD_CODE)){
        	plafond_title_tv.setVisibility(View.GONE);
            plafond_content_tv.setVisibility(View.GONE);
        }else if(accountModel.getBankServiceType().getBankServiceCode().equals(ServiceCode.CREDIT_CARD_CODE)){
        	plafond_title_tv.setVisibility(View.VISIBLE);
            plafond_content_tv.setVisibility(View.VISIBLE);
        }
        loadData();
    }

    @Override
    protected void loadData() {
        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show("", new OnProgressEvent() {
            public void onProgress() {
                String postData = GetMovementsJson.getMovementsReportProtocal(Contants.publicModel,
                        Contants.GET_CARDS_MOVEMENTS, accountModel.getAccountCode(), 1, 20,
                        restartingKey);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        activity);
                final GetMovementsModel getMovements = GetMovementsJson
                        .parseGetMovementsResponse(httpResult);
                if (getMovements.responsePublicModel.isSuccess()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setRestartingKey(getMovements.responsePublicModel.getRestartingKey());
                            recent_expandablelv_ad.list = getMovements.getMovements();
                            String lastUpdate = TimeUtil.getDateString(TimeUtil.nowTimeMillis(),TimeUtil.dateFormat13);
                            String plafond = Utils.formatMoney(accountModel.getPlafond(), activity.getResources().getString(R.string.dollar),true, true,false, false, true);
                            
                            setCardsDetailsText(accountModel.getCardState(),accountModel.getCardNumber(),accountModel.getCardHolder(), lastUpdate,TimeUtil.changeFormattrString(accountModel.getExpirationDate(), TimeUtil.dateFormat2,TimeUtil.dateFormat13), plafond);
                            expandableListView1.setAdapter(recent_expandablelv_ad);
                        }
                    });
                }
            }
        });
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

    @Override
    protected void setUI() {

    }

    class ItemDetailsAdapter extends BaseAdapter {
        LayoutInflater lf;

        Context context;

        List<MovementsModel> list;

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
                convertView.setOnClickListener(itemClicker);
                hodle = new Holde();
                hodle.tv_1 = (TextView)convertView.findViewById(R.id.tv_1);
                hodle.tv_2 = (TextView)convertView.findViewById(R.id.tv_2);
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
            if (list != null) {
                MovementsModel mMovementsModel;
                if (fixedPosition < list.size()) {
                    mMovementsModel = list.get(fixedPosition);
                    name = TimeUtil.changeFormattrString(mMovementsModel.getValueDate(),TimeUtil.dateFormat2, TimeUtil.dateFormat3);
                    value = mMovementsModel.getDescription();
                    present = Utils.formatMoney(mMovementsModel.getAmount(),activity.getResources().getString(R.string.dollar), true, true,false, false, true);
                    details = activity.getResources().getString(R.string.operation_date) + TimeUtil.changeFormattrString(mMovementsModel.getOperationDate(),TimeUtil.dateFormat2, TimeUtil.dateFormat13);
                    details2 = activity.getResources().getString(R.string.description_2) +  mMovementsModel.getDescription();
                    details3 = activity.getResources().getString(R.string.cards_amount) + present;
                    details4 = activity.getResources().getString(R.string.currency_date) + TimeUtil.changeFormattrString(mMovementsModel.getOperationDate(),TimeUtil.dateFormat2, TimeUtil.dateFormat13);
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
                } else if (isShowOthers && fixedPosition == list.size()) {
//                    value = "other payments";
                    hodle.tv_1.setVisibility(View.GONE);
                    hodle.tv_3.setVisibility(View.GONE);
                    hodle.selector_iv.setVisibility(View.GONE);
                    convertView.setBackgroundColor(0xffD7D7D7);
                }
            }

            if (name == null) {
                name =Contants.EMPTY;
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
                details2 =Contants.EMPTY;
            }
            if (details3 == null) {
                details3 =Contants.EMPTY;
            }
            if (details4 == null) {
                details4 =Contants.EMPTY;
            }
            hodle.tv_1.setText(name);
            hodle.tv_2.setText(value);
            hodle.tv_3.setText(present);
            hodle.details_tv.setText(details);
            hodle.details_tv2.setText(details2);
            hodle.details_tv3.setText(details3);
            hodle.details_tv4.setText(details4);
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
            	resetSelected();
                if (hodle.position == -2) {
//                    loadData(recordCount);
                } else {
                    hodle.selector_iv.setImageResource(R.drawable.arrow_up);
                    hodle.details_lin.setVisibility(View.VISIBLE);
                    selectPosition = hodle.position;
                    selected = hodle;
                }
            }

        }
    };
    
    private void resetSelected(){
    	if (selected != null) {
            selected.details_lin.setVisibility(View.GONE);
            selected.selector_iv.setImageResource(R.drawable.arrow_down);
            selected = null;
            selectPosition = -1;
        }
    }
    
    private void reset() {
        recent_expandablelv_ad.list = null;
        restartingKey = null;
    }
    
    private void destroyed(){
    	top_title_tv.setText(Contants.EMPTY);
    	title_tv.setText(Contants.EMPTY);
    	sub_title_tv.setText(Contants.EMPTY);
    	credit_card_name_tv.setText(Contants.EMPTY);
    	last_update_content_tv.setText(Contants.EMPTY);
    	expiration_date_content_tv.setText(Contants.EMPTY);
        plafond_content_tv.setText(Contants.EMPTY);
    	
    	resetSelected();
    }
}
