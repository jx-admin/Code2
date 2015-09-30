\Android\packages\apps\Email\src\com\android\email\activity\setup\AccountSettingsUtils.java
\Android\packages\apps\AccountsAndSyncSettings\src\com\android\settings\ManageAccountsSettings.java
\Android\packages\apps\Email\src\com\android\email\activity\setup\AccountSetupExchange.java
\Android\frameworks\base\core\java\android\accounts\AccountManagerService.java
\Android\packages\apps\AccountsAndSyncSettings\src\com\android\settings\AccountSyncSettings.java
\Android\packages\apps\Email\src\com\android\email\activity\AccountFolderList.java
AccountManagerService.java
	This should only be called by system code. One should only call this after the service – 原代码中的注释
	只有系统代码能够调用AccountManagerService.java里面的接口.
	它所实现的功能：
		检查Account是否合法，另外，如果某个帐户被删除，会发出AccountChanged的广播消息。
		还提供了所有关于Account的操作，比如：获取，添加、删除帐户，修改帐户。确认与修改帐户的认证信息，
AccountSetupExchangeTests.java
	引导Account帐户设置。
ManageAccountsSettings.java
	这个是帐户设置的主界面。
AccountSettingsUtils.java
	具体的帐户可以被修改的项。
AccountSyncSettings.java 
	点击帐户进入帐户管理界面后的Activity。
AccountFolderList.java
	Email管理界面，可以通过这个Activity访问Exchange和Email帐户，选中某个帐户以后长按，可以对帐户进行：更新、编辑、删除等操作。
	编辑界面与Account中的编辑界面是一个Activity，删除只是一个ContextMenu，无法在Account的代码中屏蔽，所以，需要对这个文件进行修改，屏蔽它的删除代码。

调用顺序：AccountSetupExchange.java -> AccountSyncSettings.java -> AccountSettings.java->AccountSettingsUtils.java->EmailContent.java(EmailContent.Account)(insert、update)
->ContentResolver(insert,update)

AccountSettings.java里面，数据的提交、获取，都是通过Editor来获取的，Editor是一个接口类，它的具体实现在：
Z:\Android\frameworks\base\core\java\android\app\ContextImpl.java中，

所做修改： 
AccountSyncSettings.java
line 161 
添加代码：
		if( !(mAccount.name.contains("accenture")) ) // by wh 
		{
			mRemoveAccountArea = (View) findViewById(R.id.remove_account_area);
			mRemoveAccountButton = (Button) findViewById(R.id.remove_account_button);
			mRemoveAccountButton.setOnClickListener(this);
		}
如果当前帐户是AEMM所设定的帐户，则禁止使用删除选项。
AccountSettings.java
line 135 
添加代码：
		if( mAccount.getDisplayName().contains("aemm") ||  mAccount.getDisplayName().contains("accenture"))// by wh 
		{
			finish() ; 
			return ; 
		}
如果所要显示的详细信息是AEMM帐户的信息，则自动跳转到前一个界面。
AccountFolderList.java
line 454
		if( mSelectedContextAccount.getDisplayName().contains("aemm.demo")
			|| mSelectedContextAccount.getDisplayName().contains("accenture")) // by wh 
		{
			return ; 
		}
如果菜单选中的为删除项，而且，所要删除的项为AEMM帐户，则不进行任何操作，直接返回。

如果在底层修改的话，需要修改EmailProvider.java
Update好找，但是Delete方法在上层的时候是启动了一个TASK，用来删除某个选项。


\Android\packages\apps\Email\src\com\android\email\provider\EmailProvider.java
update函数与delete函数
中进行屏蔽
line 1143
		if( values != null )
		{
			String strName = values.getAsString("name"); // by wh 
			if( strName != null )
				if( strName.length() != 0 && strName.contains("aemm") )
				{
					getContext().getContentResolver().notifyChange(uri, null);
					return 0 ; 
				}
		}

		帐户添加的时候所FY调研的情况是需要添加两个数据库，但是更新的时候，因为走的都是一套代码，所以，只需要修改处地方即可。
		
\Android\packages\apps\Email\src\com\android\exchange\provider\ExchangeProvider.java
中update函数与delete函数均为空