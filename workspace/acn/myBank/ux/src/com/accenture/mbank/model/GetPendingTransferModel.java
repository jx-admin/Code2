
package com.accenture.mbank.model;

import java.util.List;

public class GetPendingTransferModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    private List<PendingTransferModel> pendingTransferList;

    /**
     * @return the pendingTransferList
     */
    public List<PendingTransferModel> getPendingTransferList() {
        return pendingTransferList;
    }

    /**
     * @param pendingTransferList the pendingTransferList to set
     */
    public void setPendingTransferList(List<PendingTransferModel> pendingTransferList) {
        this.pendingTransferList = pendingTransferList;
    }
}
