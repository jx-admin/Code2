package com.act.sctc.db;

import com.act.sctc.Constant;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SqlUtils {
	private final static String GET_ITEM="select a._id,a.name from filter a left join category b on a.[category_id]=b.[_id] where a.[category_id]=%d;";
	public static Cursor getFilter(Context context,long categoryId){
		return DBHelper.getInstance(context).rawQuery(String.format(GET_ITEM, categoryId),null);
	}
	
	public static Cursor getItem(Context context,int filterId){
		return DBHelper.getInstance(context).rawQuery("select a._id,a.name from item a where a.[filter_id]="+filterId+" order by sort asc;", null);
	}
	
	public static Cursor getPackage(Context context,long id){
		return DBHelper.getInstance(context).rawQuery("select * from package where _id="+id, null);
	}
	
	public static Cursor getPhonePackage(Context context,long id){
		return DBHelper.getInstance(context).rawQuery("select * from phone_contract where _id="+id, null);
	}
	
	public static long insertShoppingCart(Context context,long productId,long userId,int productType,int acount,String mark){
		ContentValues mPhoneValues=new ContentValues();
//		mPhoneValues.put(ShoppingCartColumn.name,mPhone.getName());
		mPhoneValues.put(GoodsCartColumn.product_id,productId);
		mPhoneValues.put(GoodsCartColumn.count,acount);
		mPhoneValues.put(GoodsCartColumn.mark,mark);
		mPhoneValues.put(GoodsCartColumn.user_id,userId);
		mPhoneValues.put(GoodsCartColumn.product_type,productType);
		
		return DBHelper.getInstance(context).insert(GoodsCartColumn.TABLE_NAME,mPhoneValues);
	}
	
	public static Cursor getShoppingCart(Context context,long userId){
//		return context.getContentResolver().query(ShoppingCartColumn.CONTENT_URI,null,null,null,null);
		return DBHelper.getInstance(context).rawQuery("select * from goods_cart where user_id="+userId, null);
	}
	
	public static int deleteShoppingCart(Context context,int id){
//		return context.getContentResolver().query(ShoppingCartColumn.CONTENT_URI,null,null,null,null);
		return DBHelper.getInstance(context).delete(GoodsCartColumn.TABLE_NAME, id);
	}
	
	public static Cursor getProductNameandPrice(Context context,int categoryId,long id){
		switch (categoryId) {
		case Constant.CATEGORY_TV_CONTRACT:
			break;
		case Constant.CATEGORY_PACKAGE_CONTRACT:
			break;
		case Constant.CATEGORY_TV_PACKAGE:
		case Constant.CATEGORY_PACKAGE_PACKAGE:
		case Constant.CATEGORY_PHONE_PACKAGE:
			return DBHelper.getInstance(context).rawQuery("select a.[_id],a.[name],a.[price] from package a where _id="+id, null);
		case Constant.CATEGORY_PHONE_CONTRACT:
			return DBHelper.getInstance(context).rawQuery("select a.[_id],a.[name],a.[price] from phone_contract a where _id="+id, null);
		case Constant.CATEGORY_PHONE_PRODUCTLIST:
			return DBHelper.getInstance(context).rawQuery("select a.[_id],a.[ad_desc],a.[price] from phone a where _id="+id, null);
		}
		return null;
	}
	
	public static Cursor getCustom(Context contex,String licence){
		return contex.getContentResolver().query(CustomerColumn.CONTENT_URI, null,"licence like "+licence, null,null);
	}

}
