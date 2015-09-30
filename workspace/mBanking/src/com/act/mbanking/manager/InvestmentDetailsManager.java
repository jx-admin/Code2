
package com.act.mbanking.manager;

import java.text.ParseException;
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
import com.act.mbanking.bean.DepositInfo;
import com.act.mbanking.bean.GetDepositDetailsResponseModel;
import com.act.mbanking.bean.GetDepositInfoResponseModel;
import com.act.mbanking.bean.InvestmentDetail;
import com.act.mbanking.logic.GetDepositDetailsRequestJson;
import com.act.mbanking.logic.GetDepositInfoRequestJson;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.view.TitleViewManager;

/**Investment deposit manager
 * need a accountCode
 * @author junxu.wang
 *
 */
public class InvestmentDetailsManager extends MainMenuSubScreenManager {
    protected ExpandableListView myExpandableLv;

    TitleViewManager mTitleViewManager;

    protected GetDepositInfoResponseModel getDepositInfo;

    GetDepositDetailsResponseModel getDepositDetails;

    protected AccountsModel accountsModel;

    private Handler handler;

    private InvestmentDetailsExpandableAdapter mInvestmentDetailsExpandableAdapter;

    public InvestmentDetailsManager(MainActivity activity) {
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
     * @param context
     * @param accountCode
     */
    private void loadDepositInfo(final Context context, final AccountsModel accountsModel) {
            ProgressOverlay progressOverlay = new ProgressOverlay(context);
            progressOverlay.show("", new OnProgressEvent() {

                @Override
                public void onProgress() {
                    String postData = GetDepositInfoRequestJson.GetDepositInfoReportProtocal(Contants.publicModel, accountsModel.getAccountCode());
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,postData, context);
                    getDepositInfo = GetDepositInfoRequestJson.parseGetDepositInfoResponse(httpResult);
                    if (getDepositInfo == null || !getDepositInfo.responsePublicModel.isSuccess()) {
                        return;
                    }

                    String postData2 = GetDepositDetailsRequestJson.GetDepositDetailsReportProtocal(Contants.publicModel,accountsModel.getAccountCode());
                    HttpConnector httpConnector2 = new HttpConnector();
                    String httpResult2 = httpConnector2.requestByHttpPost(Contants.mobile_url,postData2, context);
                    getDepositDetails = GetDepositDetailsRequestJson.parseGetDeponsitDetailsResponse(httpResult2);
                    if (!getDepositDetails.responsePublicModel.isSuccess()) {
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
            String name = null, value = null, present = null;
            if (getDepositInfo != null) {
                switch (groupPosition) {
                    case 0:
                        name = "Shares";
                        present = Utils.generateMoney(getDepositInfo.getShares().getPercentage());
                        value = Utils.formatMoney(getDepositInfo
                                .getShares().getValue(), context.getResources().getString(R.string.dollar), true, true,false, false, true);
                        break;
                    case 1:
                        name = "Bonds";
                        present = Utils.generateMoney(getDepositInfo.getBonds().getPercentage());
                        value = Utils.formatMoney(getDepositInfo
                                .getBonds().getValue(), context.getResources().getString(R.string.dollar), true, true,false, false, true);
                        
                        break;
                    case 2:
                        name = "Funds";
                        present = Utils.generateMoney(getDepositInfo.getFunds().getPercentage());
                        value = Utils.formatMoney(getDepositInfo
                                .getFunds().getValue(), context.getResources().getString(R.string.dollar), true, true,false, false, true);
                        break;
                    case 3:
                        name = "More";
                        present = Utils.generateMoney(getDepositInfo.getOtherSecurities()
                                .getPercentage());
                        value = Utils.formatMoney(getDepositInfo
                                .getOtherSecurities().getValue(), context.getResources().getString(R.string.dollar), true, true,false, false, true);
                        break;
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
            if (getDepositInfo == null) {
                return 0;
            }
            return 4;
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
            List<InvestmentDetail> investmentDetails = getInvestmentDetails(groupPosition);
            if (investmentDetails != null) {
                size = investmentDetails.size()+1;
                if (size <=1) {
                    size=0;
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
                tv_1.setText(R.string.title);

                tv_2.setTextColor(0xff000000);
                tv_2.setText(R.string.quantity);

                tv_3.setTextColor(0xff000000);
                tv_3.setText(R.string.price);

                tv_4.setTextColor(0xff000000);
                tv_4.setText(R.string.date_fu);

                View diviv = convertView.findViewById(R.id.diviv);
                diviv.setVisibility(View.VISIBLE);
            } else {
                --childPosition;
                List<InvestmentDetail> investmentDetails = getInvestmentDetails(groupPosition);
                String title = null, quantily = null, price = null, datePrice = null;
                if (investmentDetails != null && childPosition < investmentDetails.size()) {
                    InvestmentDetail investmentDetail = investmentDetails.get(childPosition);
                    title = investmentDetail.getDescription();
                    quantily = Integer.toString(investmentDetail.getAmount());
                    price = Utils.notPlusGenerateFormatMoney(
                            context.getResources().getString(R.string.dollar),
                            investmentDetail.getPrice());
                    try {
                        long time;
                        time = TimeUtil.getTimeByString(investmentDetail.getPriceDate(),
                                TimeUtil.dateFormat2);
                        datePrice = TimeUtil.getDateString(time, TimeUtil.dateFormat5);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (title == null) {
                    title = "";
                }
                if (quantily == null) {
                    quantily = "";
                }
                if (price == null) {
                    price = "";
                }
                if (datePrice == null) {
                    datePrice = "";
                }

                tv_1.setTextColor(0xff6E6D6E);
                tv_1.setText(title);

                tv_2.setTextColor(0xff6E6D6E);
                tv_2.setText(quantily);

                tv_3.setTextColor(0xff6E6D6E);
                tv_3.setText(price);

                tv_4.setTextColor(0xff6E6D6E);
                tv_4.setText(datePrice);

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

        private List<InvestmentDetail> getInvestmentDetails(int groupPosition) {
            List<InvestmentDetail> investmentDetails = null;
            DepositInfo depositInfo = getDepositInfo(groupPosition);
            if (depositInfo != null) {
                investmentDetails = depositInfo.getInvestmentDetails();
            }
            return investmentDetails;
        }

        private DepositInfo getDepositInfo(int groupPosition) {
            DepositInfo depositInfo = null;
            if (getDepositDetails != null) {
                switch (groupPosition) {
                    case 0:
                        depositInfo = getDepositDetails.getShares();
                        break;
                    case 1:
                        depositInfo = getDepositDetails.getBonds();
                        break;
                    case 2:
                        depositInfo = getDepositDetails.getFunds();
                        break;
                    case 3:
                        depositInfo = getDepositDetails.getOtherSecurities();
                        break;
                }
            }
            return depositInfo;
        }

    }
}
