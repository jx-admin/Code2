//
//package com.accenture.mbank.view.payment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.accenture.mbank.MainActivity;
//import com.accenture.mbank.R;
//import com.accenture.mbank.logic.RecentTransferJson;
//import com.accenture.mbank.model.AccountsModel;
//import com.accenture.mbank.model.RecentPaymentList;
//import com.accenture.mbank.model.RecentTransferModel;
//import com.accenture.mbank.model.RecentTransferResponseModel;
//import com.accenture.mbank.model.ResponsePublicModel;
//import com.accenture.mbank.net.HttpConnector;
//import com.accenture.mbank.net.ProgressOverlay;
//import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
//import com.accenture.mbank.util.Contants;
//import com.accenture.mbank.util.DestProvider;
//import com.accenture.mbank.util.LogManager;
//import com.accenture.mbank.util.ServiceCode;
//import com.accenture.mbank.util.TimeUtil;
//import com.accenture.mbank.util.TransferState;
//import com.accenture.mbank.util.Utils;
//import com.accenture.mbank.view.RecentPaymentItem;
//
//public class RecentPaymentManager implements OnItemClickListener {
//
//    Switch3DGallery recentPaymentGallery;
//
//    RecentPaymentGalleryAdapter recentPaymentGalleryAdapter;
//
//    ListView recent_payment_ListView;
//
//    /**
//     * 存放过滤出来的020 bankServiceCode的account
//     */
//    private List<AccountsModel> accountsList;
//
//    private RecentPaymentList recentpaymentList;
//
//    private List<RecentPaymentList> allList = new ArrayList<RecentPaymentList>();
//
//    public static final int GET_RECENT_TRANSFER = 0;
//
//    private int galleryIndex;
//
//    Handler handler;
//
//    private ResponsePublicModel responsePublic;
//
//    private Activity activity;
//
//    public RecentPaymentManager(MainActivity activity) {
////        super(activity);
//        this.activity = activity;
//        handler = new Handler();
//    }
//
//    @Override
//    protected void init() {
//        accountsList = new ArrayList<AccountsModel>();
////        layout = (ViewGroup)super.activity.findViewById(R.id.recent_payment);
//        recentPaymentGallery = (Switch3DGallery)layout
//                .findViewById(R.id.recent_payment_account_folders);
//        recentPaymentGalleryAdapter = new RecentPaymentGalleryAdapter(activity);
//        filterAccount();
//        recentPaymentGalleryAdapter.setViewId(R.layout.account_data_opened_content);
//        recentPaymentGallery.setAdapter(recentPaymentGalleryAdapter);
//        recentPaymentGallery.setAnimationDuration(500);
//        recentPaymentGallery.setOnItemClickListener(this);
//
//        recent_payment_ListView = (ListView)layout.findViewById(R.id.recent_payment_item_listview);
//        recentPaymentAdapter = new RecentPaymentAdapter(activity);
//        recent_payment_ListView.setAdapter(recentPaymentAdapter);
//
//    }
//
//    private void filterAccount() {
//        // 容错处理
//        if (Contants.accountsList == null) {
//            recentPaymentGalleryAdapter.setAccountList(accountsList);
//            return;
//        }
//        for (int i = 0; i < Contants.accountsList.size(); i++) {
//            // BankServiceCode为020的account
//            if (Contants.accountsList.get(i).getBankServiceType().getBankServiceCode()
//                    .equals(ServiceCode.CURRENT_ACCOUNT)) {
//                AccountsModel account = new AccountsModel();
//                account = Contants.accountsList.get(i);
//                accountsList.add(account);
//                recentPaymentGalleryAdapter.setAccountList(accountsList);
//            }
//        }
//    }
//
//    RecentPaymentAdapter recentPaymentAdapter;
//
//    public void addRecentPayemntItem(int index) {
//        List<RecentTransferModel> recentTransferList = null;
//        for (int i = 0; i < allList.size(); i++) {
//            if (accountsList.get(galleryIndex).getAccountCode()
//                    .equals(allList.get(i).getAccountCode())) {
//                recentTransferList = allList.get(i).getRecentTransferList();
//            }
//        }
//
//        recentPaymentAdapter.seList(recentTransferList);
//        recentPaymentAdapter.notifyDataSetChanged();
//    }
//
//    private void loadRecentPaymentTransfer(int index) {
//        final String accountCode = accountsList.get(galleryIndex).getAccountCode();
//
//        ProgressOverlay progressOverlay = new ProgressOverlay(activity);
//        progressOverlay.show("loading", new OnProgressEvent() {
//            @Override
//            public void onProgress() {
//                String postData = RecentTransferJson.RecentTransferReportProtocal(
//                        Contants.publicModel, accountCode, 20);
//                HttpConnector httpConnector = new HttpConnector();
//                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
//                        activity);
//                final RecentTransferResponseModel recentTransfer = RecentTransferJson
//                        .ParseRecentTransferResponse(httpResult);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (recentTransfer != null
//                                && recentTransfer.responsePublicModel.isSuccess()) {
//                            for (int i = 0; i < allList.size(); i++) {
//                                if (!accountCode.equals(allList.get(i).getAccountCode())) {
//                                    recentpaymentList = new RecentPaymentList();
//                                    recentpaymentList.setRecentTransferList(recentTransfer
//                                            .getRecentTransferList());
//                                    recentpaymentList.setAccountCode(accountCode);
//                                    allList.add(recentpaymentList);
//                                }
//                            }
//                            if (allList.size() == 0) {
//                                recentpaymentList = new RecentPaymentList();
//                                recentpaymentList.setRecentTransferList(recentTransfer
//                                        .getRecentTransferList());
//                                recentpaymentList.setAccountCode(accountCode);
//                                allList.add(recentpaymentList);
//                            }
//
//                            myhandler.sendEmptyMessage(GET_RECENT_TRANSFER);
//                        } else {
//                            if (responsePublic != null && responsePublic.eventManagement != null) {
//                                // TODO
//                            }
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    private Handler myhandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case GET_RECENT_TRANSFER:
//                    addRecentPayemntItem(galleryIndex);
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    @Override
//    public void onItemClick(AdapterView<?> viewId, View view, int index, long arg3) {
//        galleryIndex = index;
//        loadRecentPaymentTransfer(galleryIndex);
//    }
//
//    public static class RecentPaymentAdapter extends BaseAdapter {
//
//        private List<RecentTransferModel> recentTransferList = new ArrayList<RecentTransferModel>();
//
//        /**
//         * @param recentTransferList 要设置的 recentTransferList
//         */
//        public void seList(List<RecentTransferModel> recentTransferList) {
//            this.recentTransferList = recentTransferList;
//        }
//
//        private LayoutInflater mInflater;
//
//        TextView timeText, typeText, resultText, one_text, four_text, two_text, three_text,
//                five_text;
//
//        Context context;
//
//        public RecentPaymentAdapter(Context context) {
//            this.context = context;
//            this.mInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            return recentTransferList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            RecentPaymentItem child = null;
//            if (convertView != null) {
//                child = (RecentPaymentItem)convertView;
//            } else {
//                child = (RecentPaymentItem)mInflater.inflate(R.layout.recent_payment_item, null);
//                child.init();
//            }
//            child.typeText.setText(recentTransferList.get(position).getType());
//            String date = recentTransferList.get(position).getOperationDate();
//            if (date != null || !"".equals(date)) {
//                try {
//                    date = TimeUtil.changeFormattrString(date, TimeUtil.dateFormat2,
//                            TimeUtil.dateFormat5);
//                } catch (Exception e) {
//                    LogManager.e("formatDate is error" + e.getLocalizedMessage());
//                }
//            }
//            child.timeText.setText(date);
//            // 格式化金额
//            String amount = recentTransferList.get(position).getAmount() + "";
//            amount = Utils.notPlusGenerateFormatMoney(
//                    this.context.getResources().getString(R.string.dollar), amount);
//            child.resultText.setText(amount);
//            if (recentTransferList.get(position).getType().equals(Contants.SIM_TOP_UP)) {
//                // sim top up UI展示
//                child.one_text.setText(recentTransferList.get(position).getBeneficiaryNumber());
//                String operator = DestProvider.getDsstProvider(recentTransferList.get(position)
//                        .getBeneficiaryProvider());
//                child.two_text.setText(operator);
//                String state = TransferState.getTransferState(recentTransferList.get(position)
//                        .getTransferState());
//                child.three_text.setText(state);
//                child.four_text.setVisibility(View.GONE);
//                child.five_text.setVisibility(View.GONE);
//                child.setRecentTransferModel(recentTransferList.get(position));
//            } else if (recentTransferList.get(position).getType().equals(Contants.BANK_TRANSFER)
//                    || recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
//                // bank transfer & transfer entry UI展示
//                if (recentTransferList.get(position).getType().equals(Contants.TRANSFER_ENTRY)) {
//                    child.one_text.setVisibility(View.GONE);
//                } else {
//                    child.one_text.setText(recentTransferList.get(position).getBeneficiaryName());
//                }
//                child.two_text.setText(recentTransferList.get(position).getBeneficiaryIban());
//                child.three_text.setText(recentTransferList.get(position).getDescription());
//                String state = TransferState.getTransferState(recentTransferList.get(position)
//                        .getTransferState());
//                child.four_text.setText(state);
//                child.five_text.setVisibility(View.GONE);
//                child.setRecentTransferModel(recentTransferList.get(position));
//            } else if (recentTransferList.get(position).getType()
//                    .equals(Contants.PREPAID_CARD_RELOAD)) {
//                // prepaid card UI展示
//                child.one_text.setText(recentTransferList.get(position).getBeneficiaryName());
//                child.two_text.setText(recentTransferList.get(position).getBeneficiaryCardNumber());
//                child.three_text.setVisibility(View.GONE);
//                child.four_text.setVisibility(View.GONE);
//                child.five_text.setVisibility(View.GONE);
//                child.setRecentTransferModel(recentTransferList.get(position));
//            }
//            return child;
//        }
//
//    }
//
//}
