
package com.accenture.mbank;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.accenture.mbank.logic.HelpItemJson;
import com.accenture.mbank.model.GetHelpItemResponseModel;
import com.accenture.mbank.model.HelpItemListModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.google.analytics.tracking.android.EasyTracker;

public class HelpListActivity extends BaseActivity {

    ListView helpListView;

    Button back;

    List<HelpItemListModel> helpTitles;

    private TextView title;

    Handler handler;

    int position = -1;

    AdapterView<?> parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasyTracker.getInstance().activityStart(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.help_list);

        title = (TextView)findViewById(R.id.title_text);
        title.setText("help");
        helpTitles = new ArrayList<HelpItemListModel>();

        helpListView = (ListView)findViewById(R.id.help_list_view);
        helpListView.setAdapter(new HelpListAdapter());

        helpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View child = parent.getChildAt(i);
                    child.setBackgroundDrawable(null);

                }
                HelpListActivity.this.position = position;

                Intent intent = new Intent(HelpListActivity.this, HelpDetailActivity.class);
                HelpItemListModel helpItemListModel = helpTitles.get(position);

                intent.putExtra(HelpDetailActivity.ITEM, helpItemListModel);
                startActivity(intent);
                view.setBackgroundResource(R.drawable.list_item_over);

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        view.setBackgroundDrawable(null);
                    }
                }, 500);

            }
        });
        handler = new Handler();
        if (BaseActivity.isOffline) {
            offlineTest();

        } else {
            getHelpList();
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // if (position != -1) {
        // parent.getChildAt(position);
        // helpListView.setSelection(position);
        // }
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
        ProgressOverlay progressOverlay = new ProgressOverlay(this);
        progressOverlay.show("loading", new OnProgressEvent() {

            @Override
            public void onProgress() {
                String postData = HelpItemJson.HelpItemReportProtocal(Contants.publicModel);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        HelpListActivity.this);
                final GetHelpItemResponseModel getHelpItemResponse = HelpItemJson
                        .ParseHelpItemResponse(httpResult);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (getHelpItemResponse != null
                                && getHelpItemResponse.responsePublicModel.isSuccess()) {
                            if (getHelpItemResponse.getHelpItemList() != null) {
                                helpTitles = getHelpItemResponse.getHelpItemList();
                            } else {
                                offlineTest();
                            }
                            BaseAdapter baseAdapter = (BaseAdapter)helpListView.getAdapter();
                            baseAdapter.notifyDataSetChanged();
                        } else {
                            displayErrorMessage(getHelpItemResponse.responsePublicModel.eventManagement
                                    .getErrorDescription());
                        }
                    }
                });

            }
        });
    }

    class HelpListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return helpTitles.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return helpTitles.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(HelpListActivity.this);

                convertView = (View)inflater.inflate(R.layout.help_item, null);
            }
            TextView text = (TextView)convertView.findViewById(R.id.help_title_text);

            text.setText(helpTitles.get(position).getTitle());
            return convertView;
        }

    }

    protected void onBackClick() {

        finish();
    }
}
