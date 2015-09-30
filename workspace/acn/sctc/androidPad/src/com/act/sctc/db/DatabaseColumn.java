package com.act.sctc.db;

import java.util.ArrayList;
import java.util.Map;

import android.net.Uri;
import android.provider.BaseColumns;

public abstract class DatabaseColumn implements BaseColumns {
	/**
	 * The identifier to indicate a specific ContentProvider
	 */
	public static final String AUTHORITY = "com.act.sctc.provider";
	
	/**
	 * Classes's name extends from this class.
	 */
	public static final String[] SUBCLASSES = new String[] {
		"com.act.sctc.db.PhoneLocalColumn",
		"com.act.sctc.db.PromotionColumn",
		"com.act.sctc.db.ProductDetailColumn",
		"com.act.sctc.db.PhoneContractColumn",
		"com.act.sctc.db.PhoneContractRelationColumn",
		"com.act.sctc.db.PhoneColumn",
		"com.act.sctc.db.PhoneColorColumn",
		"com.act.sctc.db.PackageColumn",
		"com.act.sctc.db.ItemObjectRelationColumn",
		"com.act.sctc.db.ItemColumn",
		"com.act.sctc.db.FilterColumn",
		"com.act.sctc.db.CategoryColumn",
		"com.act.sctc.db.BusinessColumn",
		"com.act.sctc.db.GoodsCartColumn",
		"com.act.sctc.db.CustomerColumn",
		"com.act.sctc.db.UserColumn",
		"com.act.sctc.db.BusinessDetailColumn",
		"com.act.sctc.db.ResourceColumn",
		};
	private static Object CREATE_TABLE="CREATE TABLE ";

	public String getTableCreateor() {
		return getTableCreator(getTableName(), getTableMap());
	}

	/**
	 * Get sub-classes of this class.
	 * 
	 * @return Array of sub-classes.
	 */
	@SuppressWarnings("unchecked")
	public static final Class<DatabaseColumn>[] getSubClasses() {
		ArrayList<Class<DatabaseColumn>> classes = new ArrayList<Class<DatabaseColumn>>();
		Class<DatabaseColumn> subClass = null;
		for (int i = 0; i < SUBCLASSES.length; i++) {
			try {
				subClass = (Class<DatabaseColumn>) Class.forName(SUBCLASSES[i]);
				classes.add(subClass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				continue;
			}
		}
		return classes.toArray(new Class[0]);
	}

	/**
	 * Create a sentence to create a table by using a hash-map.
	 * 
	 * @param tableName
	 *            The table's name to create.
	 * @param map
	 *            A map to store table columns info.
	 * @return
	 */
	private static final String getTableCreator(String tableName,
			Map<String, String> map) {
		String[] keys = map.keySet().toArray(new String[0]);
		String value = null;
		StringBuilder creator = new StringBuilder();
		creator.append(CREATE_TABLE).append(tableName).append("( ");
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			value = map.get(keys[i]);
			creator.append(keys[i]).append(" ");
			creator.append(value);
			if (i < length - 1) {
				creator.append(",");
			}
		}
		creator.append(")");
		return creator.toString();
	}

	abstract public String getTableName();

	abstract public Uri getTableContent();

	abstract protected Map<String, String> getTableMap();

}
