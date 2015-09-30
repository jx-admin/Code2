
package com.accenture.mbank.model;

import java.util.List;

public class TablesResponseModel {
    private List<TableWrapperList> tablewrapperList;

    public ResponsePublicModel responsePublicModel = new ResponsePublicModel();

    public List<TableWrapperList> getTablewrapperList() {
        return tablewrapperList;
    }

    public void setTablewrapperList(List<TableWrapperList> tablewrapperList) {
        this.tablewrapperList = tablewrapperList;
    }

    /**
     * @param tableName name of table
     * @return TableWrapper found, null otherwise
     */
    public TableWrapperList getTableWrapper(final String tableName) {
        if (tablewrapperList == null) {
            return null;
        }
        for (TableWrapperList tableWrapper : tablewrapperList) {
            if (tableWrapper.getTableName().equals(tableName)) {
                return tableWrapper;
            }
        }

        return null;
    }
}
