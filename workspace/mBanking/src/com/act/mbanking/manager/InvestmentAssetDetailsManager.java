
package com.act.mbanking.manager;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.AssetDetailModel;
import com.act.mbanking.bean.GetAssetsInformationResponseModel;
import com.act.mbanking.logic.GetAssetsInformationJson;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.TitleViewManager;

/**
 * Investment asset Details manager need a accountCode
 * 
 * @author junxu.wang
 */
public class InvestmentAssetDetailsManager extends MainMenuSubScreenManager {
    protected ExpandableListView myExpandableLv;

    TitleViewManager mTitleViewManager;

    GetAssetsInformationResponseModel getAssetsInformation;

    protected AccountsModel accountsModel;

    private Handler handler;

    private InvestmentDetailsExpandableAdapter mInvestmentDetailsExpandableAdapter;

    public InvestmentAssetDetailsManager(MainActivity activity) {
        super(activity);
        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                }
            };
        };
    }

    @Override
    protected void init() {
        layout = (ViewGroup)activity.findViewById(R.id.investment_details_include);
        mTitleViewManager=new TitleViewManager();
        mTitleViewManager.setView(layout);
        setLeftNavigationText(R.string.back);
        myExpandableLv = (ExpandableListView)layout.findViewById(R.id.details_expandablelv);
        myExpandableLv.setOnGroupExpandListener(new OnGroupExpandListener() {
            // .只展开一个group,点第一个gorup后，再点第二个group时，第一个会自动收缩　　

            @Override
            public void onGroupExpand(int groupPosition) {
                // TODO Auto-generated method stub
                for (int i = 0; i < myExpandableLv.getCount(); i++) {
                    if (groupPosition != i) {
                        myExpandableLv.collapseGroup(i);
                    }
                }
            }
        });
        myExpandableLv.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                    long id) {
                // TODO Auto-generated method stub
                // if(parent.get(groupPosition).isEmpty()){
                // return true;
                // }
                return false;
            }
        });
        mInvestmentDetailsExpandableAdapter = new InvestmentDetailsExpandableAdapter(activity);
        myExpandableLv.setAdapter(mInvestmentDetailsExpandableAdapter);
    }
    @Override
    public boolean onLeftNavigationButtonClick(View v) {

        mainManager.showInvestmentLevel2(true, null);
        return true;
    }

    @Override
    protected void loadData() {

    }

    /**
     * references mBanking_mix#DepositsExpandedContainer
     * InvestmentsLayoutManager
     * 
     * @param context
     * @param accountCode
     */
    private void loadDepositInfo(final Context context, final AccountsModel accountsModel) {
        ProgressOverlay progressOverlay = new ProgressOverlay(context);
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {

                String postData = GetAssetsInformationJson.GetAssetsInformantionReportProtocal(
                        Contants.publicModel, accountsModel.getAccountCode());
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        context);
                getAssetsInformation = GetAssetsInformationJson
                        .parseGetAssetsInformationResponse(httpResult);
                if (!getAssetsInformation.responsePublicModel.isSuccess()) {
                    return;
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        setData();
                    }
                });

            }
        });
    }

    public void setData() {
        myExpandableLv.setAdapter(mInvestmentDetailsExpandableAdapter);

    }

    @Override
    protected void setUI() {
        // TODO Auto-generated method stub

    }

    protected void onShow(Object obj) {
        setAccountsModel((AccountsModel)obj);
        loadDepositInfo(activity, accountsModel);
    }

    public void setAccountsModel(AccountsModel accountsModel) {
        this.accountsModel = accountsModel;
        mTitleViewManager.setTitle(accountsModel.getAccountAlias());
        String lastUpdate = TimeUtil.getDateString(TimeUtil.nowTimeMillis(), TimeUtil.dateFormat13);
        mTitleViewManager.setSubTitle(String.format(activity.getResources().getString(R.string.last_update_on), lastUpdate));
   
    }

    class InvestmentDetailsExpandableAdapter implements ExpandableListAdapter {
        LayoutInflater lf;

        Context context;

        public InvestmentDetailsExpandableAdapter(Context context) {
            this.context = context;
            lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            // TODO Auto-generated method stub

        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isEmpty() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            if (convertView == null) {
                convertView = lf.inflate(R.layout.investment_details_group_item, null);
            }
            TextView tv_1 = (TextView)convertView.findViewById(R.id.tv_1);
            TextView tv_2 = (TextView)convertView.findViewById(R.id.tv_2);
            TextView tv_3 = (TextView)convertView.findViewById(R.id.tv_3);
            ImageView selector_iv = (ImageView)convertView.findViewById(R.id.selector_iv);
            if (isExpanded) {
                selector_iv.setImageResource(R.drawable.arrow_up);
            } else {
                selector_iv.setImageResource(R.drawable.arrow_down);
            }
            String name = "TOTAL GPM:", value = null, present = null;
            if (getAssetsInformation != null) {
                
                value = Utils.formatMoney(getAssetsInformation.getPortfolioValue(), context.getResources().getString(R.string.dollar), false, true, true, true, true);
                present = Utils.generateMoney(getAssetsInformation.getPercentage());
            }
            if (value == null) {
                value = "";
            }
            if (present == null) {
                present = "";
            }
            tv_1.setText(name);
            tv_2.setText(value);
            tv_3.setText(present + "%");

            return convertView;
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            if (getAssetsInformation == null) {
                return 0;
            }
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            // switch(groupPosition){
            // case 0:
            // break;
            // }
            return null;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            // TODO Auto-generated method stub
            return childId;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            int size = 0;
            if (getAssetsInformation != null) {
                List<AssetDetailModel> assetDetailModels = getAssetsInformation.getAssetDetails();
                if (assetDetailModels != null) {
                    size = assetDetailModels.size();
                    if (size > 0) {
                        ++size;
                    }
                }
            }
            return size;
        }

        View firstChile;

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = lf.inflate(R.layout.investment_details_child_item, null);
            }
            TextView tv_1 = (TextView)convertView.findViewById(R.id.tv_1);
            TextView tv_2 = (TextView)convertView.findViewById(R.id.tv_2);
            TextView tv_3 = (TextView)convertView.findViewById(R.id.tv_3);
            TextView tv_4 = (TextView)convertView.findViewById(R.id.tv_4);

            if (childPosition == 0) {
                tv_1.setTextColor(0xff000000);
                tv_1.setText(R.string.description_1);

                tv_2.setVisibility(View.GONE);

                tv_3.setVisibility(View.GONE);

                tv_4.setTextColor(0xff000000);
                tv_4.setText(R.string.ctv);

                View diviv = convertView.findViewById(R.id.diviv);
                diviv.setVisibility(View.VISIBLE);
            } else {
                --childPosition;
                List<AssetDetailModel> list = null;
                if (getAssetsInformation != null) {
                    list = getAssetsInformation.getAssetDetails();
                }
                AssetDetailModel assetDetailModel = null;
                if (list != null && childPosition < list.size()) {
                    assetDetailModel = list.get(childPosition);
                }
                String title = null, quantily = null, price = null, datePrice = null;
                if (assetDetailModel != null) {
                    title = assetDetailModel.getDescription();
                    price = Utils.generateFormatMoney(
                            context.getResources().getString(R.string.dollar),
                            assetDetailModel.getGrossValue());
                }
                if (title == null) {
                    title = "";
                }
                if (price == null) {
                    price = "";
                }

                tv_1.setTextColor(0xff6E6D6E);
                tv_1.setText(title);

                tv_2.setVisibility(View.GONE);
                tv_3.setVisibility(View.GONE);

                tv_4.setTextColor(0xff6E6D6E);
                tv_4.setText(price);

                View diviv = convertView.findViewById(R.id.diviv);
                diviv.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean areAllItemsEnabled() {
            // TODO Auto-generated method stub
            return false;
        }
    }
}
