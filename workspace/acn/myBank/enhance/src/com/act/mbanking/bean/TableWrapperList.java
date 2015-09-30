
package com.act.mbanking.bean;

import java.util.List;


public class TableWrapperList {
    private String tableName;

    private List<TableContentList> tableContentList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<TableContentList> getTableContentList() {
        return tableContentList;
    }

    public void setTableContentList(List<TableContentList> tableContentList) {
        this.tableContentList = tableContentList;
    }
}
