
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.logic.GetAssetsInformationJson;
import com.accenture.mbank.model.AssetDetailModel;
import com.accenture.mbank.model.GetAssetsInformationResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;

public class AssetManagementExpandContainer extends ExpandedContainer {
    LinearLayout assetDetailContent;

    Handler handler;

    private String accountCode;

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public AssetManagementExpandContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new Handler();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private void init() {
        assetDetailContent = (LinearLayout)findViewById(R.id.asset_detail_content);
    }

    GetAssetsInformationResponseModel getAssetsInformation;

    @Override
    public void onExpand() {
        super.onExpand();
        getAssetsInformation = (GetAssetsInformationResponseModel) getTag();
        setData();
    }

    private void setData() {
        String value = Utils.generateFormatMoney(getContext().getResources().getString(R.string.eur),getAssetsInformation.getPortfolioValue());
        String percentage = Utils.generateMoney(getAssetsInformation.getPercentage());

        List<AssetDetailModel> list = getAssetsInformation.getAssetDetails();
        
        for (AssetDetailModel assetDetailModel : list) {
            add(assetDetailModel.getDescription(), assetDetailModel.getGrossValue());
        }

    }

    public void add(String description, String ctv) {
        // @+id/asset_detail_descripion
        // @+id/asset_detail_ctv
        if (assetDetailContent == null) {
            init();
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup r = (ViewGroup)inflater.inflate(R.layout.asset_management_detail_item, null);

        assetDetailContent.addView(r);
        TextView t = (TextView)r.findViewById(R.id.asset_detail_descripion);

        t.setText(description);

        t = (TextView)r.findViewById(R.id.asset_detail_ctv);
        String str = Utils.generateFormatMoney(
                getContext().getResources().getString(R.string.eur), ctv);
        t.setText(str);

    }
}
