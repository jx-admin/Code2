
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.accenture.mbank.logic.GetCardsJson;
import com.accenture.mbank.logic.GetFinancingInfoJson;
import com.accenture.mbank.model.AccountsModel;
import com.accenture.mbank.model.GetCardsResponseModel;
import com.accenture.mbank.model.GetFinancingInfoModel;
import com.accenture.mbank.model.InfoCardsModel;
import com.accenture.mbank.model.PushSettingModel;
import com.accenture.mbank.model.ResponsePublicModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;
import com.accenture.mbank.view.protocol.ShowAble;






public class UserInfoLayoutManager extends BankRollContainerManager {

    private boolean isDisplay = false;

    /**
     * currency to display
     */
    private String eur = null;

    private Handler myhandler = new Handler();
    
    List<PushSettingModel> sendpushSettingList = new ArrayList<PushSettingModel>();

    @Override
    public void createUiByData() {
        if (!isDisplay) {
            eur = getContext().getString(R.string.eur);
            LayoutInflater inflater = LayoutInflater.from(getContext());
            BankRollView showItemRotatView1 = (BankRollView)getRollContainer().getRollContainer().getChildAt(0);
            initShowItems(inflater, showItemRotatView1);
            BankRollView pinManagerRotatView1 = (BankRollView)getRollContainer().getRollContainer().getChildAt(1);
            initPinManager(inflater, pinManagerRotatView1);
            BankRollView accountsRotatView1 = (BankRollView)getRollContainer().getRollContainer()
                    .getChildAt(2);
            BankRollView cardsRotatView1 = (BankRollView)getRollContainer().getRollContainer()
                    .getChildAt(3);
            BankRollView loansRotatView1 = (BankRollView)getRollContainer().getRollContainer()
                    .getChildAt(4);
            BankRollView investmentsRotatView1 = (BankRollView)getRollContainer()
                    .getRollContainer().getChildAt(5);
           
            setAccountsUI(accountsRotatView1, Contants.baseAccounts, AccountType.ACCOUNT_BASE);
            setAccountsUI(cardsRotatView1, Contants.cardAccounts, AccountType.ACCOUNT_CARD);
            setAccountsUI(loansRotatView1, Contants.loansAccounts, AccountType.ACCOUNT_LOAN);
            setAccountsUI(investmentsRotatView1, Contants.investmentAccounts,AccountType.ACCOUNT_INVESTMENT);         
            
        }
    }
    
    Handler mHandler=new Handler(){

        public void handleMessage(Message msg) {
        	ResponsePublicModel response=(ResponsePublicModel) msg.obj;
        	if (response.isSuccess()) {
				Toast.makeText(getContext(), "save successful", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getContext(), "save fail", Toast.LENGTH_SHORT).show();
			}
        }
	};
	
	

    private void initPinManager(LayoutInflater inflater, BankRollView root) {
        LinearLayout synthesislinearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams synthesislayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        synthesislinearLayout.setLayoutParams(synthesislayoutParams);
        synthesislinearLayout.setOrientation(LinearLayout.VERTICAL);
        root.setContent(synthesislinearLayout);
        synthesislinearLayout.setBackgroundResource(R.drawable.box_details);
        // 添加expander
        ItemExpander selectReceivePinExpander = (ItemExpander)inflater.inflate(R.layout.item_expander, null);
        synthesislinearLayout.addView(selectReceivePinExpander);

        SelectReceivePinExpandedContainer selectReceivePinExpandedContainer = (SelectReceivePinExpandedContainer)inflater.inflate(R.layout.select_receive_pin_expanded, null);
        selectReceivePinExpandedContainer.init();
        selectReceivePinExpander.setExpandedContainer(selectReceivePinExpandedContainer);
        selectReceivePinExpander.setTitle(root.getResources().getString(R.string.pin_management));
        selectReceivePinExpander.setTypeface(Typeface.DEFAULT_BOLD);
        selectReceivePinExpander.setResultVisible(false);
        selectReceivePinExpander.setEditable(false);
    }

    private void initShowItems(LayoutInflater inflater, BankRollView root) {
        LinearLayout synthesislinearLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams synthesislayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        synthesislinearLayout.setLayoutParams(synthesislayoutParams);
        synthesislinearLayout.setOrientation(LinearLayout.VERTICAL);
        synthesislinearLayout.setBackgroundResource(R.drawable.box_details);
        // 添加expander
        ItemExpander synthesisExpander = (ItemExpander)inflater.inflate(R.layout.item_expander,null);
        synthesislinearLayout.addView(synthesisExpander);

        SynthesisExpandedContainer synthesisExpandedContainer = (SynthesisExpandedContainer)inflater.inflate(R.layout.synthesis_expanded, null);
        synthesisExpandedContainer.init();
        synthesisExpander.setExpandedContainer(synthesisExpandedContainer);
        synthesisExpander.setTitle(root.getResources().getString(R.string.synthesis));
        synthesisExpander.setTypeface(Typeface.DEFAULT_BOLD);
        synthesisExpander.setResultVisible(false);
        synthesisExpander.setEditable(false);

        ItemExpander listExpander = (ItemExpander)inflater.inflate(R.layout.item_expander, null);
        ListsExpandedContainer listsExpandedContainer = (ListsExpandedContainer)inflater.inflate(R.layout.lists_expanded, null);

        listExpander.setExpandedContainer(listsExpandedContainer);
        listExpander.setTitle(root.getResources().getString(R.string.lists));
        listExpander.setTypeface(Typeface.DEFAULT_BOLD);
        listExpander.setResultVisible(false);
        listExpander.setExpand(false);
        synthesislinearLayout.addView(listExpander);

        root.setContent(synthesislinearLayout);
    }

    @Override
    public void onShow(ShowAble showAble) {
    }
 
    protected void setAccountsUI(BankRollView root, List<AccountsModel> list,
            AccountType accountType) {

        LinearLayout linearLayout1 = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(layoutParams1);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        root.setContent(linearLayout1);
        linearLayout1.setBackgroundResource(R.drawable.box_details);

        if (list != null && list.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (AccountsModel amAccountsModel : list) {
                ItemExpander itemExpander = (ItemExpander)inflater.inflate(R.layout.item_expander,null);
                String title = amAccountsModel.getAccountAlias().toUpperCase(Locale.US);
                itemExpander.setTitle(title);
                itemExpander.setTypeface(Typeface.DEFAULT_BOLD);
                itemExpander.setResultVisible(false);
                linearLayout1.addView(itemExpander);
                itemExpander.setExpand(false);
                setExpandedContainer(itemExpander, amAccountsModel, accountType);
            }
        } else {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.box_details);
            textView.setText(root.getResources().getString(R.string.not_available));
            linearLayout1.addView(textView);
        }
    }
    public void setExpandedContainer(ItemExpander itemExpander, AccountsModel accountsModel,AccountType accountType) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (accountType == AccountType.ACCOUNT_BASE) {
            BaseAccountContainer container = (BaseAccountContainer)inflater.inflate(R.layout.base_account, null);
            TextView account_alias = (TextView)container.findViewById(R.id.account_alias);
            TextView account_name = (TextView)container.findViewById(R.id.account_name);
            TextView account_code = (TextView)container.findViewById(R.id.account_code); 
            TextView account_iban = (TextView)container.findViewById(R.id.account_iban);
            account_alias.setText(accountsModel.getAccountAlias());
            account_name.setText(Contants.getUserInfo.getUserprofileHb().getCustomerName() + " " + Contants.getUserInfo.getUserprofileHb().getCustomerSurname());
            account_code.setText(accountsModel.getAccountCode());
            account_iban.setText(accountsModel.getIbanCode());
            itemExpander.setExpandedContainer(container);
        } else if (accountType == AccountType.ACCOUNT_CARD) {
            CardAccountContainer container = (CardAccountContainer)inflater.inflate(R.layout.card_account, null);
            TextView card_alias = (TextView)container.findViewById(R.id.card_alias);
            TextView card_name = (TextView)container.findViewById(R.id.card_name);
            TextView card_number = (TextView)container.findViewById(R.id.card_number);
            card_alias.setText(accountsModel.getAccountAlias());
            card_name.setText(accountsModel.getCardHolder());
            String serviceCode = accountsModel.getBankServiceType().getBankServiceCode();
            if (serviceCode.equals("872")) {
                TextView plafond = (TextView)container.findViewById(R.id.plafond);
                ViewGroup parent = (ViewGroup)plafond.getParent();
                parent.setVisibility(View.VISIBLE);

                String str = Utils.generateFormatMoney(Contants.COUNTRY, accountsModel.getPlafond());
                plafond.setText(str);

            }

            card_number.setText(accountsModel.getCardNumber());
            itemExpander.setExpandedContainer(container);
        } else if (accountType == AccountType.ACCOUNT_LOAN) {
            final AccountsModel accountsModel2 = accountsModel;
            final String accountCode = accountsModel.getAccountCode();
            final String financeType = accountsModel.getFinanceType();
            final ItemExpander itemExpander1 = itemExpander;
            final LayoutInflater inflater1 = inflater;
            ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
            progressOverlay.show("", new OnProgressEvent() {

                @Override
                public void onProgress() {
                    String postData = GetFinancingInfoJson.getFinancingInfoReportProtocal(
                            Contants.publicModel, accountCode, financeType);
                    HttpConnector httpConnector = new HttpConnector();
                    String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url,
                            postData, getContext());
                    final GetFinancingInfoModel getFinancingInfo = GetFinancingInfoJson
                            .paresgetFinancingInfoResponse(httpResult);
                    if (getFinancingInfo != null) {
                        myhandler.post(new Runnable() {
                            @Override
                            public void run() {
                                setLoans(inflater1, itemExpander1, getFinancingInfo,accountsModel2);
                            }
                        });
                    }
                }
            });
        } else if (accountType == AccountType.ACCOUNT_INVESTMENT) {
            InvestmentAccountContainer container = (InvestmentAccountContainer)inflater.inflate(R.layout.investment_account, null);
            TextView investment_alias = (TextView)container.findViewById(R.id.investment_alias);
            investment_alias.setText(accountsModel.getAccountAlias());
            itemExpander.setExpandedContainer(container);
        }
    }

    @Override
    public void onShow() {
        createUiByData();
        updateArrowState();
    }
    
    private void setLoans(LayoutInflater inflater, ItemExpander itemExpander,
            GetFinancingInfoModel getFinancingInfo,AccountsModel accountModel) {
        LoanAccountContainer container = (LoanAccountContainer)inflater.inflate(
                R.layout.loan_account, null);
        TextView loan_alias = (TextView)container.findViewById(R.id.loan_alias);
        TextView total_capital = (TextView)container.findViewById(R.id.total_capital);
        loan_alias.setText(accountModel.getAccountAlias());

        total_capital.setText(Utils.generateFormatMoney(eur, getFinancingInfo.getResidueAmount()));
        itemExpander.setExpandedContainer(container);
    }

    @SuppressWarnings("unused")
    private List<InfoCardsModel> getCardsInfo(AccountsModel accountModel) {
        String postData = GetCardsJson.GetCardsReportProtocal(Contants.publicModel,
                accountModel.getAccountAlias(), accountModel.getAccountCode(),
                accountModel.getAccountCode(),null);
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                getContext());
        GetCardsResponseModel cardsInfo = GetCardsJson.parseGetCardResponse(httpResult);
        return cardsInfo.getInfoCardListModel();
    }

    private enum AccountType {
        ACCOUNT_BASE, ACCOUNT_CARD, ACCOUNT_LOAN, ACCOUNT_INVESTMENT;
    }
}
