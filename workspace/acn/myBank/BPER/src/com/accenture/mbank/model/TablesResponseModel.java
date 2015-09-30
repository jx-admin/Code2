
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
            if (tableWrapper.getTableName().toLowerCase().equals(tableName.toLowerCase())) {
                return tableWrapper;
            }
        }

        return null;
    }
    /**
     * @param tableName name of table
     * @return TableWrapper found, null otherwise
     */
    public List<TableContentList> getTableWrapperList(final String tableName) {
        if (tablewrapperList == null) {
            return null;
        }
        for (TableWrapperList tableWrapper : tablewrapperList) {
            if (tableWrapper.getTableName().toLowerCase().equals(tableName.toLowerCase())) {
                return tableWrapper.getTableContentList();
            }
        }

        return null;
    }
    /**
     * @param tableName name of table
     * @return TableWrapper found, null otherwise
     */
    public TableContentList getTableWrapper(final String tableName,String code) {
        if (tablewrapperList == null) {
            return null;
        }
        for (TableWrapperList tableWrapper : tablewrapperList) {
            if (tableWrapper.getTableName().toLowerCase().equals(tableName.toLowerCase())) {
            	List<TableContentList> mTableContentLists=tableWrapper.getTableContentList();
            	if(mTableContentLists!=null){
            		for(TableContentList mTableContentList:mTableContentLists){
            			if(mTableContentList.getCode().toLowerCase().equals(code)){
            				return mTableContentList;
            			}
            		}
            		
            	}
                return null;
            }
        }

        return null;
    }
}
