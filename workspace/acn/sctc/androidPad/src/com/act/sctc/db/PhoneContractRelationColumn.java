package com.act.sctc.db;

import java.util.Map;

import android.net.Uri;

public class PhoneContractRelationColumn extends DatabaseColumn {
	public static final String TAG = PhoneContractRelationColumn.class.getSimpleName();

	/** 表名 */
	public static final String TABLE_NAME = "phone_contract_relation";

	public static final String phone_id = "phone_id";
	public static final String contract_id = "contract_id";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

	public String getTableCreateor() {
		return "create table phone_contract_relation( _id integer primary key autoincrement, phone_id integer not null REFERENCES phone(_id), contract_id integer not null REFERENCES phone_contract(_id) )";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return TABLE_NAME;
	}

	@Override
	public Uri getTableContent() {
		// TODO Auto-generated method stub
		return CONTENT_URI;
	}

	@Override
	protected Map<String, String> getTableMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
