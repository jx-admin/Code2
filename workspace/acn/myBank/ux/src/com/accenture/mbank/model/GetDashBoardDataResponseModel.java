
package com.accenture.mbank.model;

import java.util.List;

public class GetDashBoardDataResponseModel {
    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public List<DashBoardModel> dashboardsList;

    /**
     * @return the dashboardsList
     */
    public List<DashBoardModel> getDashboardsList() {
        return dashboardsList;
    }

    /**
     * @param dashboardsList the dashboardsList to set
     */
    public void setDashboardsList(List<DashBoardModel> dashboardsList) {
        this.dashboardsList = dashboardsList;
    }

}
