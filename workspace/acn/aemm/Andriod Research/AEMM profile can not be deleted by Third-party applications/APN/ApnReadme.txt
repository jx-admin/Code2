
APN在Framework源代码中涉及到的部分不多，ApnSettings.java、ApnEditor.java、TelephonyProvider.java
其中，ApnSettings.java负责列表的显示，ApnEditor.java负责编辑页面的显示，TelephonyProvider.java负责将数据存储到文件或者数据库。

对APN所进行的修改由3部分组成：

1 ApnSettings.java 禁其进入ApnEditor界面进行修改  

2 ApnEditor.java 界面禁其使用删除按纽。

3 TelephonyProvider.java在其进行数据的写入或者删除之前进行关键字匹配。

PS：

	删除的操作，没有将任何的APN信息传入，所以，删除的操作在底层屏蔽有些问题。
	
所做的修改如下：

Android\packages\apps\Settings\src\com\android\settings\ApnEditor.java // 上层的屏蔽

line 238 

if( mName.getText().contains("aemm") ) // by wh 
{
	finish() ; 
}

line 289 

if (!mNewApn) {
	if( !mName.getText().contains("aemm") ) // by wh 
		menu.add(0, MENU_DELETE, 0, R.string.menu_delete)
			.setIcon(android.R.drawable.ic_menu_delete);
}
menu.add(0, MENU_SAVE, 0, R.string.menu_save)
	.setIcon(android.R.drawable.ic_menu_save);
menu.add(0, MENU_CANCEL, 0, R.string.menu_cancel)
	.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
return true;

Android\packages\providers\TelephonyProvider\src\com\android\providers\telephony\TelephonyProvider.java		
Update(disable)
line 577 
	// by wh 
				String strNameT = values.getAsString("name");
				Log.d(TAG, "by wh update URL_ID " + strNameT );
				if( strNameT.length() != 0 && strNameT.contains("aemm") )
				{
					Log.d(TAG, "by wh update URL_ID KeyWord Should not be used " );
					return -1 ; 
				}
				Cursor c3 = null ; 
				if( (c3 = query(url , null , null , null , null )) != null )
				{
					c3.moveToFirst();
					int idindex2 = c3.getColumnIndex("name");
					String strName = c3.getString(idindex2);
					Log.d(TAG, "by wh update URL_ID query strName " + strName );
					c3.close() ; 
					if( strName != null && strName.contains("aemm") )
					{
						Log.d(TAG, "by wh update URL_ID query EXISTS " );
						return -1 ; 
					}
				}
//				
				
Delete(disable)
line 515

			// by wh 
            	Log.d(TAG, "by wh delete _ID " + Telephony.Carriers._ID );
				Cursor c3 = null ; 
				if( (c3 = query(url , null , null , null , null )) != null )
				{
					c3.moveToFirst();
					int idindex2 = c3.getColumnIndex("name");
					String strName = c3.getString(idindex2);
					Log.d(TAG, "by wh delete URL_ID query strName " + strName );
					c3.close() ; 
					if( strName != null && strName.contains("aemm") )
					{
						Log.d(TAG, "by wh delete URL_ID Enterprise Item " );
						return -1 ; 
					}
				}
	
先执行Insert，以ROWID为关键字，创建一个空的项，当对新创建的VPN项进行编辑，并选择菜单里面的保存的时候，将会执行：update函数，将数据写入。
如果只将update进行处理，则，除非组件以非正常的形式写入，否则，也不能创建与AEMM的Profile有关的Setting选项。
处理方法：
Update之前，先进行设置的读取，判断要修改的数据是否AEMM的Profile，如果是，直接返回。
Delete同上，删除之前根据URI判断是否AEMM所属的Profile，如果是，直接返回。


	
