
package com.accenture.mbank.view;

import com.accenture.mbank.model.BranchListModel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class BankOverlayItem extends OverlayItem {

    private BranchListModel brankBranchListModel;

    public BranchListModel getBrankBranchListModel() {
        return brankBranchListModel;
    }

    public void setBrankBranchListModel(BranchListModel brankBranchListModel) {
        this.brankBranchListModel = brankBranchListModel;
    }

    public BankOverlayItem(GeoPoint point, String title, String snippet) {
        super(point, title, snippet);
    }

}
