//
//package com.accenture.mbank.view;
//
//import java.util.List;
//
//import android.content.Context;
//import android.os.Handler;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//
//import com.accenture.mbank.R;
//import com.accenture.mbank.logic.GetDepositDetailsRequestJson;
//import com.accenture.mbank.model.GetDepositDetailsResponseModel;
//import com.accenture.mbank.model.InvestmentDetail;
//import com.accenture.mbank.net.HttpConnector;
//import com.accenture.mbank.net.ProgressOverlay;
//import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
//import com.accenture.mbank.util.Contants;
//import com.accenture.mbank.util.TimeUtil;
//import com.accenture.mbank.view.PortFolioDetailExpandContainer.PortfolioDetailItemData;
//
//public class PortfolioExpandedContainer extends ExpandedContainer {
//
//    ItemExpander sharesItemExpander, bondsItemExpander, fundsItemExpander, moreItemExpander;
//
//    PortFolioDetailExpandContainer bondsExpandContainer, fundsExpandContainer, moreExpandContainer,
//            sharesExpandContainer;
//
//    private String accountCode;
//
//    Handler handler;
//
//    public String getAccountCode() {
//        return accountCode;
//    }
//
//    public void setAccountCode(String accountCode) {
//        this.accountCode = accountCode;
//    }
//
//    public PortfolioExpandedContainer(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        handler = new Handler();
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        // TODO Auto-generated method stub
//        super.onAttachedToWindow();
//        init();
//        test();
//    }
//
//    private void init() {
//        if (sharesItemExpander == null) {
//            sharesItemExpander = (ItemExpander)findViewById(R.id.portfolio_shares_expander);
//            sharesItemExpander.setLevel(2);
//            sharesItemExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
//            sharesItemExpander.setResultVisible(false);
//            sharesItemExpander.setTitle(getResources().getString(R.string.shares));
//
//            bondsItemExpander = (ItemExpander)findViewById(R.id.portfolio_bounds_expander);
//            bondsItemExpander.setLevel(2);
//            bondsItemExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
//            bondsItemExpander.setResultVisible(false);
//            bondsItemExpander.setTitle(getResources().getString(R.string.bonds));
//
//            fundsItemExpander = (ItemExpander)findViewById(R.id.portfolio_funds_expander);
//            fundsItemExpander.setLevel(2);
//            fundsItemExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
//            fundsItemExpander.setResultVisible(false);
//            fundsItemExpander.setTitle(getResources().getString(R.string.funds));
//
//            moreItemExpander = (ItemExpander)findViewById(R.id.portfolio_more_expander);
//            moreItemExpander.setLevel(2);
//            moreItemExpander.setExpandButtonBackground(R.drawable.investment_expand_selector);
//            moreItemExpander.setResultVisible(false);
//            moreItemExpander.setTitle(getResources().getString(R.string.more));
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//
//            bondsExpandContainer = (PortFolioDetailExpandContainer)inflater.inflate(R.layout.portfolio_detail_expander, null);
//            bondsItemExpander.setExpandedContainer(bondsExpandContainer);
//
//            sharesExpandContainer = (PortFolioDetailExpandContainer)inflater.inflate(R.layout.portfolio_detail_expander, null);
//            sharesItemExpander.setExpandedContainer(sharesExpandContainer);
//
//            fundsExpandContainer = (PortFolioDetailExpandContainer)inflater.inflate(R.layout.portfolio_detail_expander, null);
//            fundsItemExpander.setExpandedContainer(fundsExpandContainer);
//
//            moreExpandContainer = (PortFolioDetailExpandContainer)inflater.inflate(R.layout.portfolio_detail_expander, null);
//            moreItemExpander.setExpandedContainer(moreExpandContainer);
//
//        }
//
//    }
//
//    GetDepositDetailsResponseModel getDepositDetails;
//
//    @Override
//    public void onExpand() {
//        // TODO Auto-generated method stub
//        super.onExpand();
//        if (getDepositDetails != null) {
//            return;
//        }
//        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
//        progressOverlay.show("", new OnProgressEvent() {
//
//            @Override
//            public void onProgress() {
//
//                String postData = GetDepositDetailsRequestJson.GetDepositDetailsReportProtocal(
//                        Contants.publicModel, accountCode);
//                HttpConnector httpConnector = new HttpConnector();
//                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
//                        getContext());
//                getDepositDetails = GetDepositDetailsRequestJson
//                        .parseGetDeponsitDetailsResponse(httpResult);
//                if (!getDepositDetails.responsePublicModel.isSuccess()) {
//                    return;
//                }
//
//                handler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        setData();
//                    }
//                });
//
//            }
//        });
//    }
//
//    private void setData() {
//
//        List<InvestmentDetail> shares = getDepositDetails.getShares().getInvestmentDetails();
//        List<InvestmentDetail> bonds = getDepositDetails.getBonds().getInvestmentDetails();
//        List<InvestmentDetail> funds = getDepositDetails.getFunds().getInvestmentDetails();
//        List<InvestmentDetail> mores = getDepositDetails.getOtherSecurities()
//                .getInvestmentDetails();
//
//        setData(shares, sharesExpandContainer);
//        setData(bonds, bondsExpandContainer);
//        setData(funds, fundsExpandContainer);
//        setData(mores, moreExpandContainer);
//    }
//
//    private void setData(List<InvestmentDetail> list,
//            PortFolioDetailExpandContainer portFolioDetailExpandContainer) {
//
//        for (InvestmentDetail investmentDetail : list) {
//            portFolioDetailExpandContainer.add(investmentDetail);
//        }
//    }
//}
