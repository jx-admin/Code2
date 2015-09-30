
package com.accenture.mbank.view;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.R;
import com.accenture.mbank.logic.GetAssetsInformationJson;
import com.accenture.mbank.model.AssetDetailModel;
import com.accenture.mbank.model.GetAssetsInformationResponseModel;
import com.accenture.mbank.net.HttpConnector;
import com.accenture.mbank.net.ProgressOverlay;
import com.accenture.mbank.net.ProgressOverlay.OnProgressEvent;
import com.accenture.mbank.util.Contants;
import com.accenture.mbank.util.Utils;

public class AssetManagementExpandContainer extends ExpandedContainer {

    // android:id="@+id/gpm_value_text"
    // android:id="@+id/gpm_percentage_text"
    // android:id="@+id/asset_detail_content"

    TextView gpmValueText, gpmPercentageText;

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
        // TODO Auto-generated method stub
        super.onAttachedToWindow();
        if (gpmValueText == null) {
            init();

        }

    }

    private void init() {
        gpmValueText = (TextView)findViewById(R.id.gpm_value_text);
        gpmPercentageText = (TextView)findViewById(R.id.gpm_percentage_text);
        assetDetailContent = (LinearLayout)findViewById(R.id.asset_detail_content);

    }

    GetAssetsInformationResponseModel getAssetsInformation;

    @Override
    public void onExpand() {
        // TODO Auto-generated method stub
        super.onExpand();

        if (getAssetsInformation != null) {
            return;
        }
        ProgressOverlay progressOverlay = new ProgressOverlay(getContext());
        progressOverlay.show("", new OnProgressEvent() {

            @Override
            public void onProgress() {

                String postData = GetAssetsInformationJson.GetAssetsInformantionReportProtocal(
                        Contants.publicModel, accountCode);
                HttpConnector httpConnector = new HttpConnector();
                String httpResult = httpConnector.requestByHttpPost(Contants.mobile_url, postData,
                        getContext());
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

    private void setData() {

        String value = Utils.generateFormatMoney(
                getContext().getResources().getString(R.string.dollar),
                getAssetsInformation.getPortfolioValue());
        String percentage = Utils.generateMoney(getAssetsInformation.getPercentage());

        List<AssetDetailModel> list = getAssetsInformation.getAssetDetails();

        gpmValueText.setText(value);
        gpmPercentageText.setText(percentage + "%");
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
                getContext().getResources().getString(R.string.dollar), ctv);
        t.setText(str);

    }
}
