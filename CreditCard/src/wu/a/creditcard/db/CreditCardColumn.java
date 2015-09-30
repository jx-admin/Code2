package wu.a.creditcard.db;

import java.util.Map;

import android.net.Uri;

public class CreditCardColumn extends DatabaseColumn {
	public static final String TAG = CreditCardColumn.class.getSimpleName();

	/** 表名 */
	public static final String TABLE_NAME = "creditcard";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String NICKNAME="nickname";/**卡号*/
	public static final String CARD_number = "card_num";/**卡类型*/
	public static final String CARD_TYPE = "card_type";/**银行卡种*/
	public static final String CARD_CATEGORY = "card_category";/**币种*/
	public static final String CARD_CURRENCY = "card_carrency";/**余额*/
	public static final String CARD_MONEY = "card_money";/**账单日*/
	public static final String CARD_STATEMENT_DATE = "card_statement_date";/**付款日*/
	public static final String CARD_PAYMENT_DUE_DATE = "card_payment_due_date";/**备注*/
	public static final String REMARK = "remark";/**服务费名称*/
	public static final String CARD_CHARGE = "card_charge";

	public String getTableCreateor() {
		return "CREATE TABLE "+TABLE_NAME+"( _id integer primary key autoincrement, "+
	NICKNAME+" text,"
	+CARD_number+" text UNIQUE,"
	+CARD_TYPE+" integer,"
	+CARD_CATEGORY+" text,"
	+CARD_CURRENCY+" text,"
	+CARD_MONEY+" double,"
	+CARD_STATEMENT_DATE+" time,"
	+CARD_PAYMENT_DUE_DATE+" time,"
	+REMARK+" text,"
	+CARD_CHARGE+" text"
	+" )";
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public Uri getTableContent() {
		return CONTENT_URI;
	}

	@Override
	protected Map<String, String> getTableMap() {
		return null;
	}

}
