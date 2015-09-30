package com.android.accenture.aemm.express.updataservice;

import java.net.URI;
import java.net.URISyntaxException;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Process;
import android.util.Log;

import com.android.email.AccountBackupRestore;
import com.android.email.Email;
import com.android.email.SecurityPolicy;

import com.android.email.mail.AuthenticationFailedException;
import com.android.email.mail.MessagingException;
import com.android.email.mail.Sender;
import com.android.email.mail.Store;
import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailContent.Account;
import com.android.email.provider.EmailContent.AccountColumns;



public class EmailLocalSetup {

	private final static int DEFAULT_ACCOUNT_CHECK_INTERVAL = 15;

	String emailAddress;
	String incomingPassword;
	String outgoingPassword;

	String incomingServer;
	String outgoingServer;

	int incomingServerPort;
	int outgoingServerPort;

	boolean incomingSsl ;
	boolean outgoingSsl ;

	String emailAccountType;
	String emailAccountName;

	public final static String POP3 = "EmailTypePOP";
	public final static String IMAP4 = "EmailTypeIMAP";

	public EmailContent.Account emailAccount;
	public void setExchageAccount()
	{

	}
	private Context mContext;
	public EmailLocalSetup(Context context)
	{
		this.mContext = context;
	}

	public void setParameterValues(String emailAddress,
			String emailAccountType,
			String emailAccountName,
			String incomingServer,
			String incomingServerPort,
			String incomingPassword,
			boolean incomingSsl,
			String outgoingServer,
			String outgoingServerPort,
			String outgoingPassword,
			boolean outgoingSsl)
	{
		this.emailAddress = emailAddress;
		this.incomingPassword = incomingPassword;
		this.outgoingPassword = outgoingPassword;
		this.emailAccountType = emailAccountType;
		this.emailAccountName = emailAccountName;
		this.incomingServer = incomingServer;
		this.incomingServerPort = Integer.parseInt(incomingServerPort);
		this.incomingSsl = incomingSsl;
		this.outgoingServer = outgoingServer;
		this.outgoingServerPort = Integer.parseInt(outgoingServerPort);
		this.outgoingSsl = outgoingSsl;
	}

	public static int deleteEmailAccount(Context context, long id)
	{
		int ret = 0;
		
		EmailContent.Account aa = Account.restoreAccountWithId(context, id);
		
		 try {
             String mAccountUri = aa.getStoreUri(context);
			// Delete Remote store at first.
             Store.getInstance(mAccountUri , context, null).delete();
             // Remove the Store instance from cache.
             Store.removeInstance(mAccountUri);
             Uri uri = ContentUris.withAppendedId(
                     EmailContent.Account.CONTENT_URI, id);
             context.getContentResolver().delete(uri, null, null);
             // Update the backup (side copy) of the accounts
             AccountBackupRestore.backupAccounts(context);
             // Release or relax device administration, if relevant
             SecurityPolicy.getInstance(context).reducePolicies();
         } catch (Exception e) {
        	 ret = -1;
         }
         Email.setServicesEnabled(context);
         return ret;
	}
	
	
	public int addEmailAccount(Context context) {
		int ret = -1;
		try {
			ret = checkAccount();
			if (ret == 0) {
				ret = validate();
				if (ret != 0) {
					deleteEmailAccount(context, emailAccount.mId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
//	private int newEmailEmailAccount(Context context) {
//		int ret = -1;
//		if (null!= emailAccount){
//			String[] emailParts = emailAddress.split("@");
//			if (emailParts.length > 1) {
//				emailAccount = new EmailContent.Account();
//			}
//		}
//		return ret;
//	}

	public long getEmailAccountId()
	{
		return emailAccount.mId;
	}

	public String getEmailAccountAddress()
	{
		return emailAccount.mEmailAddress;
	}

	public int checkAccount() {
		int ret = -1;

		if (null != emailAddress) {
			String[] emailParts = emailAddress.split("@");
			if (emailParts.length > 1) {

				URI incomingUri = null;
				URI outgoingUri = null;
				String incomingSheme = null;
				String outgoingSheme = null;
				String incomingUserInfo = emailAddress + ":" + incomingPassword;
				String outgoingUserInfo = emailAddress + ":" + outgoingPassword;

				emailAccount = new EmailContent.Account();

				// EmailTypePOP and EmailTypeIMAP
				if (this.emailAccountType.equals(POP3)) {
					incomingSheme = incomingSsl ? "pop3+ssl+" : "pop3+";
					outgoingSheme = outgoingSsl ? "smtp+tls+" : "smtp+";
				} else if (this.emailAccountType.equals(IMAP4)) {
					incomingSheme = incomingSsl ? "imap+ssl+" : "imap+";
					outgoingSheme = outgoingSsl ? "smtp+ssl+" : "smtp+";
				}

				emailAccount.setDisplayName(emailAccountName);

				try {
					Log.d("AddEmailCount", incomingServer + ";"
							+ outgoingUserInfo);
					incomingUri = new URI(incomingSheme, incomingUserInfo,
							incomingServer, incomingServerPort, null, null,
							null);// uri.getUserInfo(), uri.getHost(),
									// uri.getPort(),
					outgoingUri = new URI(outgoingSheme, outgoingUserInfo,
							outgoingServer, outgoingServerPort, null, null,
							null);// uri.getUserInfo(), uri.getHost(),
									// uri.getPort(),

					emailAccount.setEmailAddress(emailAddress);
					emailAccount.setStoreUri(this.mContext, incomingUri
							.toString());
					emailAccount.setSenderUri(this.mContext, outgoingUri
							.toString());
					emailAccount
							.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);
					if (incomingUri.toString().startsWith("imap")) {
						// Delete policy must be set explicitly, because IMAP
						// does not provide a UI selection
						// for it. This logic needs to be followed in the auto
						// setup flow as well.
						emailAccount
								.setDeletePolicy(EmailContent.Account.DELETE_POLICY_ON_DELETE);
					}
					emailAccount
							.setSyncInterval(DEFAULT_ACCOUNT_CHECK_INTERVAL);
					ret = 0;
				} catch (URISyntaxException use) {
					ret = -1;
					throw new Error(use);
				}
			}
		}

		return ret;
	}

	public int validate()
	{
		int ret = -1;
		Log.d("validatevalidate", "Begin check of incoming email settings");
		//new Thread()
		{
			
			//public void run() 
			{
				//Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				try{
					//step 1 .incoming setting
					commitSettings(mContext, emailAccount);
			        Email.setServicesEnabled(mContext);
//					Log.d(Email.LOG_TAG, "Begin check of incoming email settings");
//					Store store = Store.getInstance(
//							emailAccount.getStoreUri(mContext),
//							mContext, null);

					// !!! DO NOT delete these !!!
					// if you want check if this email is valid, uncomment these lines
					//store.checkSettings();
					//Log.d(Email.LOG_TAG, "check of incoming email settings ok");

					//step2 .outgoing setting
					//Log.d(Email.LOG_TAG, "Begin check of outgoing email settings");

					//Sender sender = Sender.getInstance(mContext,
					//		emailAccount.getSenderUri(mContext));
					//sender.close();
					//sender.open();
					//sender.close();
					ret = 0;
				}
				catch(Exception e)
				{
					//authenticate error
					e.getStackTrace();					
				}

			}  

		}//.start();
		return ret;
	}

	public static void commitSettings(Context context, EmailContent.Account account) {
		if (!account.isSaved()) {
			account.save(context);
			//account.update(context, cv)
		} else {
			ContentValues cv = new ContentValues();
			cv.put(AccountColumns.IS_DEFAULT, account.mIsDefault);
			cv.put(AccountColumns.DISPLAY_NAME, account.getDisplayName());
			cv.put(AccountColumns.SENDER_NAME, account.getSenderName());
			cv.put(AccountColumns.SIGNATURE, account.getSignature());
			cv.put(AccountColumns.SYNC_INTERVAL, account.mSyncInterval);
			cv.put(AccountColumns.RINGTONE_URI, account.mRingtoneUri);
			cv.put(AccountColumns.FLAGS, account.mFlags);
			cv.put(AccountColumns.SYNC_LOOKBACK, account.mSyncLookback);
			account.update(context, cv);
		}
		// Update the backup (side copy) of the accounts
		//  AccountBackupRestore.backupAccounts(context);
		//we need to write preference file in /data/data/com.android.email/shared_prefs
		//AndroidMail.Main.xml
		//UpdateEmailPrefs(account);
	}
}
