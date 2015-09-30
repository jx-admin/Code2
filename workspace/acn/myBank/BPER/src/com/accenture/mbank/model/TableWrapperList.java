
package com.accenture.mbank.model;

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
    public int getLength(String code){
    	if(tableContentList==null){
    		return -1;
    	}
    	code=code.toLowerCase();
    	for(TableContentList mTableContentList:tableContentList){
    		if(mTableContentList.getCode().equalsIgnoreCase(code)){
    			try{return Integer.parseInt(mTableContentList.getDescription());}catch(Exception e){e.printStackTrace();}
    		}
    	}
    	return -1;
    }
}
