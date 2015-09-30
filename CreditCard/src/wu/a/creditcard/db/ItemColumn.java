package wu.a.creditcard.db;

import java.util.Map;

import android.net.Uri;

/**<pre>
 * _id, card_id, data_type, data, data1
 * </pre>
 * @author junxu.wang
 *
 */
public class ItemColumn extends DatabaseColumn{
	public static final String TABLE_NAME = "item";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String ITEM_FOREIGN_ID = "foreign_id";
	public static final String ITEM_TYPE = "item_type";
	public static final String ITEM_DATA = "item_data";
	public static final String ITEM_DATA1 = "item_data1";

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
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getTableCreateor() {
		return "CREATE TABLE "+TABLE_NAME+"( _id integer primary key autoincrement, "+
				ITEM_FOREIGN_ID+" integer,"
				+ITEM_TYPE+" integer,"
				+ITEM_DATA+" text,"
				+ITEM_DATA1+" text,"
				+" )";
	}

}
