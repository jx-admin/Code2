
package com.accenture.mbank.model;

import java.util.List;

public class GetBranchListResponseModel {
    private List<BranchListModel> branchList;

    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public List<BranchListModel> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<BranchListModel> branchList) {
        this.branchList = branchList;
    }

}
