
package com.act.mbanking.manager;

import static com.act.mbanking.CommonUtilities.SENDER_ID;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.accenture.mbank.capture.CaptureActivity;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;
import com.act.mbanking.bean.AccountsModel;
import com.act.mbanking.bean.GetFinancingInfoModel;
import com.act.mbanking.bean.GetPushPreferencesModel;
import com.act.mbanking.bean.SettingModel;
import com.act.mbanking.logic.GetFinancingInfoJson;
import com.act.mbanking.logic.GetPushPreferencesJson;
import com.act.mbanking.manager.view.adapter.PushSettingsAdapter;
import com.act.mbanking.manager.view.adapter.TitleDetailAdapter;
import com.act.mbanking.net.HttpConnector;
import com.act.mbanking.net.ProgressOverlay;
import com.act.mbanking.net.ProgressOverlay.OnProgressEvent;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.act.mbanking.utils.ViewUtil;
import com.google.android.gcm.GCMRegistrar;
//import com.act.mbanking.view.CardsLayoutManager;
//import com.act.mbanking.view.RecentPayments;

public class UserInfoManager extends MainMenuSubScreenManager implements OnClickListener {

    public UserInfoManager(MainActivity activity) {
        super(activity);
        sm = activity.setting;
    }

    @Override
    protected void init() {
        layout = (ViewGroup)super.activity.findViewById(R.id.user_info);

        ImageView iv = (ImageView)layout.findViewById(R.id.imageItems);
        iv.setOnClickListener(this);

        iv = (ImageView)layout.findViewById(R.id.imagePin);
        iv.setOnClickListener(this);

        iv = (ImageView)layout.findViewById(R.id.imageAccount);
        iv.setOnClickListener(this);

        iv = (ImageView)layout.findViewById(R.id.imageCards);
        iv.setOnClickListener(this);

        iv = (ImageView)layout.findViewById(R.id.imageLoans);
        iv.setOnClickListener(this);

        iv = (ImageView)layout.findViewById(R.id.imageInvest);
        iv.setOnClickListener(this);

        iv = (ImageView)layout.findViewById(R.id.imagePush);
        iv.setOnClickListener(this);

        vgItems = (ViewGroup)layout.findViewById(R.id.contactLayout);
        vgPin = (ViewGroup)layout.findViewById(R.id.searchLayout);
        vgAccount = (ViewGroup)layout.findViewById(R.id.accountLayout);
        vgCards = (ViewGroup)layout.findViewById(R.id.cardLayout);
        vgLoans = (ViewGroup)layout.findViewById(R.id.loansLayout);
        vginvestments = (ViewGroup)layout.findViewById(R.id.investmentsLayout);
        vgpush = (ViewGroup)layout.findViewById(R.id.pushsettingsLayout);
//        vgpush.setVisibility(View.GONE);
        // vgpushgeneral = (ViewGroup)
        // layout.findViewById(R.id.pushsettingsLayout);

        TextView tv = (TextView)layout.findViewById(R.id.time);
        String head = activity.getString(R.string.last_update_on);
        head = String.format(head,
                TimeUtil.getDateString(new Date().getTime(), TimeUtil.dateFormat13));
        tv.setText(head);

        setLeftNavigationText(activity.getResources().getString(R.string.dashboard));
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        mainManager.showAggregatedView(true, null);
        return true;
    }

    @Override
    protected void onShow(Object object) {
        super.onShow(object);
        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
        progressOverlay.show(activity.getResources().getString(R.string.waiting),
            new OnProgressEvent() {
                @Override
                public void onProgress() {
                    Contants.getPushPreferences = GetPushPreferences();
                }
            });
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void setUI() {

    }

    @Override
    public void onClick(View v) {
        setOpen(v.getId());
    }
    
    protected void notification(){
        final SharedPreferences sp = activity.getSharedPreferences(Contants.SETTING_FILE_NAME, activity.MODE_PRIVATE);
        LayoutInflater inflater = LayoutInflater.from(activity);
        LinearLayout linearLahyout = (LinearLayout)inflater.inflate(R.layout.exit_message_dialog_layout, null);

        final Dialog dialog = new Dialog(activity, R.style.selectorDialog);
        dialog.setContentView(linearLahyout);
        Button yesButton = (Button)dialog.findViewById(R.id.yes_btn);
        Button noButton = (Button)dialog.findViewById(R.id.no_btn);
        TextView text = (TextView)dialog.findViewById(R.id.exit_message_text);
        text.setText(R.string.is_receive_notifictions);
        dialog.show();
        noButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        yesButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showGeneralDetail(vgpush);
                openItem = R.id.imagePush;
                Contants.updataInitSetting(sp.edit(),true);
                Utils.registerGCM(activity);
            }
        });
    }

    private void setOpen(int x) {
        closeDetail();

        if (openItem == x) {
            openItem = -1;
            view = null;
            vg = null;
            return;
        }
        switch (x) {
            case R.id.imageItems: {
                showItemsDetail();
                openItem = x;
                break;
            }
            case R.id.imagePin: {
                showPinDetail();
                openItem = x;
                break;
            }
            case R.id.imageAccount: {
                showOtherDetail(vgAccount);
                openItem = x;
                break;
            }
            case R.id.imageCards: {
                showOtherDetail(vgCards);
                openItem = x;
                break;
            }
            case R.id.imageLoans: {
                showOtherDetail(vgLoans);
                openItem = x;
                break;
            }
            case R.id.imageInvest: {
                showOtherDetail(vginvestments);
                openItem = x;
                break;
            }
            case R.id.imagePush: {
                if (!Utils.isInitRegisterPushNotification(activity)) {
                    notification();
                }else{
                	showGeneralDetail(vgpush);
                    openItem = x;
                }
                
                /*ViewUtil.goToViewByArgu(activity,
						CaptureActivity.class,
						String.valueOf(R.id.search));*/
                break;
            }
            default: {
                break;
            }
        }
    }

    private void showItemsDetail() {
        LayoutInflater li = activity.getLayoutInflater();
        view = li.inflate(R.layout.show_items, null);

        vg = (ViewGroup)vgItems;

        Button btn = (Button)view.findViewById(R.id.btniSave);
        btn.setOnClickListener(btnClick);

        addSubView(0);
        update(view);
    }

    private void showGeneralDetail(ViewGroup vgArug) {
        ListView lv = new ListView(activity);
        if(Contants.getPushPreferences.getPustCategorList() !=null && Contants.getPushPreferences.getPustCategorList().size() > 0){
            PushSettingsAdapter adapter = new PushSettingsAdapter(activity, lv, Contants.getPushPreferences.getPustCategorList());
            lv.setAdapter(adapter);
            lv.setId(R.id.userInfoAccountList);
            view = lv;
            vg = vgArug;
            addSubView(0);
            PushSettingsAdapter.setListViewHeightBasedOnChildren(lv);
        }
    }
    
    
    private void showOtherDetail(ViewGroup vgArug) {

        ListView lv = new ListView(activity);

        ArrayList<HashMap<String, String>> list = getData(vgArug.getId());
        TitleDetailAdapter adapter = new TitleDetailAdapter(activity, lv, list);

        lv.setAdapter(adapter);
        lv.setId(R.id.userInfoAccountList);
        view = lv;

        vg = vgArug;

        addSubView(0);

        TitleDetailAdapter.setListViewHeightBasedOnChildren(lv);
    }


    private void showPinDetail() {
        LayoutInflater li = activity.getLayoutInflater();
        view = li.inflate(R.layout.show_pin, null);

        vg = (ViewGroup)vgPin;

        Button btn = (Button)view.findViewById(R.id.btnpSave);
        btn.setOnClickListener(btnClick);

        addSubView(0);
        updatePin(view);
    }

    
    @SuppressLint("DefaultLocale")
    private ArrayList<HashMap<String, String>> getData(int x) {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        if (R.id.accountLayout == x) {
            for (AccountsModel am : Contants.baseAccounts) {

                HashMap<String, String> item = new HashMap<String, String>();

                String alias = am.getAccountAlias().toLowerCase();
                item.put("title", alias);

                StringBuilder sb = new StringBuilder();
                sb = new StringBuilder();
                String head = activity.getString(R.string.account_alias);
                sb.append(head);
                sb.append(alias);
                sb.append("\n");

                head = activity.getString(R.string.last_name);
                sb.append(head);
                sb.append(Contants.getUserInfo.getCustomerName() + " "
                        + Contants.getUserInfo.getCustomerSurname());
                sb.append("\n");

                head = activity.getString(R.string.a_account);
                sb.append(head);
                sb.append(am.getAccountCode());
                sb.append("\n");

                head = activity.getString(R.string.IBAN);
                sb.append(head);
                sb.append(am.getIbanCode());

                item.put("detail", sb.toString());
                list.add(item);
            }
        } else if (R.id.cardLayout == x) {
            for (AccountsModel am : Contants.cardAccounts) {

                HashMap<String, String> item = new HashMap<String, String>();

                String alias = am.getAccountAlias().toLowerCase();
                item.put("title", alias);

                StringBuilder sb = new StringBuilder();
                sb = new StringBuilder();
                String head = activity.getString(R.string.card_alias);
                sb.append(head);
                sb.append(alias);
                sb.append("\n");

                head = activity.getString(R.string.card_holder);
                sb.append(head);
                sb.append(am.getCardHolder());
                sb.append("\n");

                head = activity.getString(R.string.card_num);
                sb.append(head);
                sb.append(am.getCardNumber());
                sb.append("\n");

                String serviceCode = am.getBankServiceType().getBankServiceCode();
                if (serviceCode.equals("872")) {
                    head = activity.getString(R.string.plafond);
                    String dollar = activity.getString(R.string.dollar);
                    head = String.format(head, dollar + Utils.generateMoneyInt(am.getPlafond()));
                    sb.append(head);
                }

                item.put("detail", sb.toString());

                list.add(item);
            }
        } else if (R.id.loansLayout == x) {
            int num = 0;
            for (AccountsModel am : Contants.loansAccounts) {

                final HashMap<String, String> item = new HashMap<String, String>();

                final String alias = am.getAccountAlias().toLowerCase();
                item.put("title", alias);

                getLoansResidueService(num, alias, am.getAccountCode(), am.getFinanceType());

                list.add(item);
                num++;
            }
        } else if (R.id.investmentsLayout == x) {
            for (AccountsModel am : Contants.investmentAccounts) {

                HashMap<String, String> item = new HashMap<String, String>();

                String alias = am.getAccountAlias().toLowerCase();
                item.put("title", alias);

                StringBuilder sb = new StringBuilder();
                sb = new StringBuilder();
                String head = activity.getString(R.string.alias);
                sb.append(head);
                sb.append(alias);

                item.put("detail", sb.toString());

                list.add(item);
            }
        }
        return list;
    }

//    private List<PushCategoryModel> getSettingData() {
////    	ArrayList<HashMap<String, Object>> menulist = new ArrayList<HashMap<String, Object>>();
//    	List<PushCategoryModel> pushCategoryList = new ArrayList<PushCategoryModel>();
//    	
//        for(PushCategoryModel ppm : (Contants.getPushPreferences.getPustCategorList())){       	
////            HashMap<String, Object> menu1 = new HashMap<String, Object>();
////            HashMap<String, String> item1 = new HashMap<String, String>();
//            PushSettingModel pushsettingModel = new PushSettingModel();
//            String category1 = ppm.getCategory();
//            for (int i = 0; i < (ppm.getPushSettingList().size()); i++) {
//                pushsettingModel = ppm.getPushSettingList().get(i);
//                if (pushsettingModel.getPushSetting() == 1) {
//                    String setValue = "ture";
//                    item1.put(pushsettingModel.getPushDescription(), setValue);
//                }
//                menu1.put(category1, item1);
//            }
//            menulist.add(menu1);
//        }
//        return menulist;
//    }

    private void addSubView(int height) {
        if (null != view && null != vg) {
            if (0 == height) {
                height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, height);
            lp.gravity = Gravity.CLIP_VERTICAL;
            vg.addView(view, lp);

            activity.setting.getOrderListFor();
        }
    }

    private void closeDetail() {
        if (null != view && null != vg) {
            vg.removeView(view);
        }
    }

    RadioGroup rgList, rgDisplay, rgTransfer, rgPinDisplay;

    private OnClickListener btnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btniSave: {
                    int rgCheckedId = rgList.getCheckedRadioButtonId();
                    if (rgCheckedId == R.id.radio0) {
                        sm.setOrderListFor(SettingModel.SORT_DATE_DESC);
                    } else if (rgCheckedId == R.id.radio1) {
                        sm.setOrderListFor(SettingModel.SORT_AMOUNT_DESC);
                    } else {

                    }
                    rgCheckedId = rgDisplay.getCheckedRadioButtonId();
                    if (rgCheckedId == R.id.RadioButton02) {
                        sm.setRecentPaymentsDisplayed(SettingModel.LAST_20);
                    } else if (rgCheckedId == R.id.RadioButton01) {
                        sm.setRecentPaymentsDisplayed(SettingModel.LAST_2_MONTH);
                    } else {

                    }
                    rgCheckedId = rgTransfer.getCheckedRadioButtonId();
                    switch (rgCheckedId) {
                        case R.id.tradio0:
                            sm.setShowTransactionBy(SettingModel.RECENT_10);
                            break;
                        case R.id.tradio1:
                            sm.setShowTransactionBy(SettingModel.RECENT_20);
                            break;
                        case R.id.tradio2:
                            sm.setShowTransactionBy(SettingModel.RECENT_30);
                            break;
                        default:
                            break;
                    }
                    // sm.getOrderListFor()
                    sm.save(activity);
                    closeDetail();

                }
                    break;
                case R.id.btnpSave: {

                    int rgCheckedId = rgPinDisplay.getCheckedRadioButtonId();
                    if (rgCheckedId == R.id.radio0) {
                        sm.setChannelToRecelvePin(SettingModel.EMAIL);
                    } else if (rgCheckedId == R.id.radio1) {
                        sm.setChannelToRecelvePin(SettingModel.SMS);
                    }
                    sm.save(activity);
                    closeDetail();

                }
                    break;
                default: {

                    break;
                }
            }
        }
    };

    public void updatePin(View view) {
        rgPinDisplay = (RadioGroup)view.findViewById(R.id.rgList);
        if (activity.setting.getChannelToRecelvePin() == SettingModel.EMAIL) {
            rgPinDisplay.check(R.id.radio0);
        } else {
            rgPinDisplay.check(R.id.radio1);
        }
    }

    public void update(View view) {
        rgList = (RadioGroup)view.findViewById(R.id.rgList);
        rgDisplay = (RadioGroup)view.findViewById(R.id.rgDisplay);
        rgTransfer = (RadioGroup)view.findViewById(R.id.rgTransfer);
        if (activity.setting.getOrderListFor() == SettingModel.SORT_DATE_DESC) {
            rgList.check(R.id.radio0);
        } else {
            rgList.check(R.id.radio1);
        }
        if (activity.setting.getRecentPaymentsDisplayed() == SettingModel.LAST_20) {
            rgDisplay.check(R.id.RadioButton02);
        } else {
            rgDisplay.check(R.id.RadioButton01);
        }
        if (activity.setting.getShowTransactionBy() == SettingModel.RECENT_10) {
            rgTransfer.check(R.id.tradio0);
        } else if (activity.setting.getShowTransactionBy() == SettingModel.RECENT_20) {
            rgTransfer.check(R.id.tradio1);
        } else {
            rgTransfer.check(R.id.tradio2);
        }
    }

    private void getLoansResidueService(final int position, final String alias,
            final String accountCode, final String financeType) {

        final ProgressOverlay progress = new ProgressOverlay(activity.getApplicationContext());

        progress.show("", new OnProgressEvent() {
            @Override
            public void onProgress() {
                String postData = GetFinancingInfoJson.getFinancingInfoReportProtocal(
                        Contants.publicModel, accountCode, financeType);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        progress.getContext());
                final GetFinancingInfoModel getFinancingInfo = GetFinancingInfoJson
                        .paresgetFinancingInfoResponse(httpResult);
                if (null != getFinancingInfo) {
                    myhandler.post(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder sb = new StringBuilder();
                            sb = new StringBuilder();
                            String head = activity.getString(R.string.alias);
                            sb.append(head);
                            sb.append(alias);
                            sb.append("\n");

                            head = activity.getString(R.string.residual_capital);
                            sb.append(head);
                            sb.append(getFinancingInfo.getResidueAmount());

                            String info = sb.toString();
                            if (null != vg && vg == vgLoans) {
                                ListView lv = (ListView)vg.findViewById(R.id.userInfoAccountList);
                                if (null != lv) {
                                    TitleDetailAdapter tda = (TitleDetailAdapter)lv.getAdapter();

                                    ArrayList<HashMap<String, String>> list = tda.getList();
                                    if (position < list.size()) {
                                        HashMap<String, String> item = list.get(position);
                                        item.put("detail", info);
                                        tda.notifyChanged();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private GetPushPreferencesModel GetPushPreferences() {
        String postData = GetPushPreferencesJson.GetPushPreferencesReportProtocal(Contants.publicModel,Utils.getIMEI(activity));
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData, activity);
        return GetPushPreferencesJson.ParseGetPushPreferencesResponse(httpResult);
    }

    private int openItem = -1;

    private View view;

    private ViewGroup vg;

    private ViewGroup vgItems;

    private ViewGroup vgPin;

    private ViewGroup vgAccount;

    private ViewGroup vgCards;

    private ViewGroup vgLoans;

    private ViewGroup vginvestments;

    private ViewGroup vgpush;

    private Handler myhandler = new Handler();

    SettingModel sm;
}
