
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.act.mbanking.App;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.BaseActivity;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.GetHelpItemResponseModel;
import com.act.mbanking.bean.HelpItemListModel;
import com.act.mbanking.logic.HelpItemJson;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;

public class GuideManager extends MainMenuSubScreenManager {
    List<HelpItemListModel> helpTitles;

    int position = -1;

    Handler handler;

    ListView helpListView;

    ItemDetailsAdapter adapter;

    public GuideManager(MainActivity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        layout = (ViewGroup)activity.findViewById(R.id.guide);

        helpTitles = new ArrayList<HelpItemListModel>();

        helpListView = (ListView)layout.findViewById(R.id.help_list_view);
        adapter = new ItemDetailsAdapter(activity);
        helpListView.setAdapter(adapter);

        handler = new Handler();
        getHelpList();

        setLeftNavigationText(activity.getResources().getString(R.string.dashboard));
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        mainManager.showAggregatedView(true, null);
    	resetSelected();
        return true;
    }

    protected void onShow(Object o){
    	resetSelected();
    }
    @Override
    protected void loadData() {

    }

    @Override
    protected void setUI() {
    	resetSelected();
    }

    private void offlineTest() {
        helpTitles.add(new HelpItemListModel("activation"));
        helpTitles.add(new HelpItemListModel("home"));
        helpTitles.add(new HelpItemListModel("mobile token"));
        helpTitles.add(new HelpItemListModel("login"));
        helpTitles.add(new HelpItemListModel("menu"));
        helpTitles.add(new HelpItemListModel("account"));
        helpTitles.add(new HelpItemListModel("payee list"));
        helpTitles.add(new HelpItemListModel("debit report"));

        helpTitles.add(new HelpItemListModel("activation"));
        helpTitles.add(new HelpItemListModel("home"));
        helpTitles.add(new HelpItemListModel("mobile token"));
        helpTitles.add(new HelpItemListModel("login"));
        helpTitles.add(new HelpItemListModel("menu"));
        helpTitles.add(new HelpItemListModel("account"));
        helpTitles.add(new HelpItemListModel("payee list"));
        helpTitles.add(new HelpItemListModel("debit report"));
    }

    void getHelpList() {
        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show("loading", new OnProgressEvent() {

            @Override
            public void onProgress() {
                String postData = HelpItemJson.HelpItemReportProtocal(Contants.publicModel);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        activity);
                final GetHelpItemResponseModel getHelpItemResponse = HelpItemJson
                        .ParseHelpItemResponse(httpResult);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (getHelpItemResponse != null && getHelpItemResponse.responsePublicModel.isSuccess()) {
                            if (getHelpItemResponse.getHelpItemList() != null) {
                                helpTitles = getHelpItemResponse.getHelpItemList();
                                adapter.setDatas(helpTitles);
                            } else {
                                offlineTest();
                            }
                            BaseAdapter baseAdapter = (BaseAdapter)helpListView.getAdapter();
                            baseAdapter.notifyDataSetChanged();
                        } else {
                            BaseActivity baseActivity = (BaseActivity)activity;
                            baseActivity.displayErrorMessage(getHelpItemResponse.responsePublicModel.eventManagement.getErrorDescription());
                        }
                    }
                });

            }
        });
    }

    class HelpListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return helpTitles.size();
        }

        @Override
        public Object getItem(int position) {
            return helpTitles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(GuideManager.this.activity);

                convertView = (View)inflater.inflate(R.layout.help_item, null);
            }
            TextView text = (TextView)convertView.findViewById(R.id.help_title_text);

            text.setText(helpTitles.get(position).getTitle());
            return convertView;
        }

    }

    class ItemDetailsAdapter extends ExtensibleListAdapter<HelpItemListModel> {

        public ItemDetailsAdapter(Context context) {
            super(context);
        }

        public ItemDetailsAdapter(Context context, LayoutInflater layoutInflater) {
            super(context, layoutInflater);
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
                hodle.details_tv2.setSingleLine(false);
                hodle.details_tv3 = (TextView)convertView.findViewById(R.id.details3_tv);
                hodle.details_tv3.setVisibility(View.GONE);
                hodle.details_tv4 = (TextView)convertView.findViewById(R.id.details4_tv);
                hodle.details_tv4.setVisibility(View.GONE);
                convertView.setTag(hodle);
            }
            hodle = (Holde)convertView.getTag();

            String name = null, details = null;
            int fixedPosition = position;
            if (datas != null) {
                if (fixedPosition < datas.size()) {
                    HelpItemListModel childData = datas.get(fixedPosition);
                    name = childData.getTitle();
                    details = childData.getText();

                    convertView.setClickable(true);
                    hodle.tv_1.setVisibility(View.VISIBLE);
                    hodle.selector_iv.setVisibility(View.VISIBLE);
                    convertView.setBackgroundResource(R.drawable.list_element_closed);
                } else if (isShowOthers && fixedPosition == datas.size()) {
                    hodle.tv_1.setVisibility(View.GONE);
                    hodle.selector_iv.setVisibility(View.GONE);
                    convertView.setBackgroundResource(R.drawable.filter_off_center);
                }
            }

            if (name == null) {
                name = Contants.EMPTY;
            }
            if (details == null) {
                details = Contants.EMPTY;
            }
            hodle.tv_1.setText(name);
            hodle.details_tv.setText(name);
            hodle.details_tv2.setText(details);
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

    }

    public int selectPosition = -1;

    ItemDetailsAdapter.Holde selected;
    OnClickListener itemClicker = new OnClickListener() {

        public void onClick(View v) {
            ItemDetailsAdapter.Holde hodle = (ItemDetailsAdapter.Holde)v.getTag();
            if (hodle.details_lin.getVisibility() == View.VISIBLE) {
                hodle.selector_iv.setImageResource(R.drawable.arrow_down);
                hodle.details_lin.setVisibility(View.GONE);
                selected = null;
                selectPosition = -1;
            } else {
            	resetSelected();
                if (hodle.position == -2) {
                    // loadData(recordCount);
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
}
