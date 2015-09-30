package com.apk.infos;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class TableResult extends AbstractTableModel {

	private ArrayList<String> listTitle=new ArrayList<String>();
	private static ArrayList<ArrayList<String>> listDate=null;
	public TableResult(String strpath){
		listTitle.add("文件名称");
		listTitle.add("包名");
		listTitle.add("版本名");
		listTitle.add("版本号");
		listDate=new ArrayList<ArrayList<String>>();
		if (!strpath.equals("")) {
			try {
				listDate=GetInfo.GetApkInfoAll(strpath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return listTitle.size();
	}

	@Override
	public int getRowCount() {
		return listDate.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return listDate.get(arg0).get(arg1);
	}

	@Override
	public String getColumnName(int arg0) {
		return listTitle.get(arg0);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		super.isCellEditable(rowIndex, columnIndex);
		return true;
	}
}
