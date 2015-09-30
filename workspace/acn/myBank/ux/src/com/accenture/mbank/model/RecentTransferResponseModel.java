
package com.accenture.mbank.model;

import java.util.List;

public class RecentTransferResponseModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();
    
    private List<RecentTransferModel> recentTransferList;

    public List<RecentTransferModel> getRecentTransferList() {
        return recentTransferList;
    }

    public void setRecentTransferList(List<RecentTransferModel> recentTransferList) {
        this.recentTransferList = recentTransferList;
    }

}
