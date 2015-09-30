package com.aess.aemm.setting;

import java.net.URI;
import java.net.URISyntaxException;
import com.aess.aemm.setting.EmailProfile.EmailArg;
import com.android.email.AccountBackupRestore;
import com.android.email.Email;
import com.android.email.SecurityPolicy;
import com.android.email.mail.Store;
import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailContent.Account;
import com.android.email.provider.EmailContent.AccountColumns;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class EmailSetup {
	public final static String TAG = "EmailSetup";
	public final static int DEFAULT_ACCOUNT_CHECK_INTERVAL = 15;
	public final static String POP3 = "EmailTypePOP";
	public final static String IMAP4 = "EmailTypeIMAP";

	public int setEmailAccount(Context context, EmailArg arg, EmailContent.Account oldAccount)
	{
		Log.i(TAG, "setEmailAccount");
		int ret = -1;
		EmailContent.Account account = null;
		if (null == oldAccount) {
			account = new EmailContent.Account();
		} else {
			account = oldAccount;
		}
		if (null != arg && null != arg.emailAddress) {
			String[] emailParts = arg.emailAddress.split("@");
			if (emailParts.length > 1) {

				URI incomingUri = null;
				URI outgoingUri = null;

				String incomingUserInfo = arg.emailAddress + ":" + arg.incomingPassword;
				String outgoingUserInfo = arg.emailAddress + ":" + arg.outgoingPassword;

				// EmailTypePOP and EmailTypeIMAP
				String incomingSheme = null;
				String outgoingSheme = null;
				if (arg.emailAccountType.equals(POP3)) {
					incomingSheme = arg.incomingSsl ? "pop3+ssl+trustallcerts+" : "pop3+";
					outgoingSheme = arg.outgoingSsl ? "smtp+ssl+trustallcerts+" : "smtp+";
				} else if (arg.emailAccountType.equals(IMAP4)) {
					incomingSheme = arg.incomingSsl ? "imap+ssl+trustallcerts+" : "imap+";
					outgoingSheme = arg.outgoingSsl ? "smtp+ssl+trustallcerts+" : "smtp+";
				}

				try {
					incomingUri = new URI(incomingSheme, incomingUserInfo,
							arg.incomingServer, Integer
									.parseInt(arg.incomingServerPort), null,
							null, null);
					
					outgoingUri = new URI(outgoingSheme, outgoingUserInfo,
							arg.outgoingServer, Integer
									.parseInt(arg.outgoingServerPort), null,
							null, null);
				} catch (URISyntaxException use) {
					use.printStackTrace();
				}
				
				account.setDisplayName(arg.emailAccountName);
				
				account.setEmailAddress(arg.emailAddress);
				account.setStoreUri(context, incomingUri
						.toString());
				account.setSenderUri(context, outgoingUri
						.toString());
				account.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);
				if (incomingUri.toString().startsWith("imap")) {
					account.setDeletePolicy(EmailContent.Account.DELETE_POLICY_ON_DELETE);
				}
				account.setSyncInterval(DEFAULT_ACCOUNT_CHECK_INTERVAL);
				emailAccount = account;
				ret = 1;
			}
		}
		return ret;
	}
	
	public int addEmailAccount(Context context)
	{
		Log.i(TAG, "addEmailAccount");
		int ret = 0;
		if (null != emailAccount) {
			submitEmailAccount(context, emailAccount);
			Email.setServicesEnabled(context);
			ret = 1;
		}
		return ret;
	}
	
	public static int delEmailAccount(Context context, EmailContent.Account eaccent)
	{
		Log.i(TAG, "delEmailAccount");
		int ret = 0;
		if (null != eaccent) {
			if (null != eaccent.mEmailAddress) {
				
			} else if (eaccent.mId > 0) {
				delEmailAccountById(context, eaccent.mId);
			}
			Email.setServicesEnabled(context);
			ret = 1;
		}
		return ret;
	}
	
	public static int delEmailAccountById(Context context, long id)
	{
		Log.i(TAG, "delEmailAccountById");
		int ret = 0;
		
		EmailContent.Account aa = Account.restoreAccountWithId(context, id);
		
		 try {
             String mAccountUri = aa.getStoreUri(context);

             Store.getInstance(mAccountUri , context, null).delete();

             Store.removeInstance(mAccountUri);
             Uri uri = ContentUris.withAppendedId(
                     EmailContent.Account.CONTENT_URI, id);
             context.getContentResolver().delete(uri, null, null);

             AccountBackupRestore.backupAccounts(context);

             SecurityPolicy.getInstance(context).reducePolicies();
         } catch (Exception e) {
        	 ret = -1;
         }
         Email.setServicesEnabled(context);
         return ret;
	}
	
	public static int delEmailAccountByAddress(Context context, String address)
	{
		Log.i(TAG, "delEmailAccountByAdd");
		int ret = 0;
		
		EmailContent.Account aa = Account.restoreAccountWithAddress(context, address);
		if (null == aa) {
			return ret;
		}
		long id = aa.mId;
		 try {
             String mAccountUri = aa.getStoreUri(context);

             Store.getInstance(mAccountUri , context, null).delete();

             Store.removeInstance(mAccountUri);
             Uri uri = ContentUris.withAppendedId(
                     EmailContent.Account.CONTENT_URI, id);
             context.getContentResolver().delete(uri, null, null);

             AccountBackupRestore.backupAccounts(context);

             SecurityPolicy.getInstance(context).reducePolicies();
         } catch (Exception e) {
        	 ret = -1;
         }
         Email.setServicesEnabled(context);
         return ret;
	}
	
	public static int submitEmailAccount(Context context, EmailContent.Account account) {
		int ret = 0;
		if (null != account) {
			saveAccount(context, account);
			Email.setServicesEnabled(context);
			ret = 1;
		}
		return ret;
	}

//	public static int editEmailAccount(Context context, EmailContent.Account account) {
//		if (null != account) {
//			saveAccount(context, account);
//			Email.setServicesEnabled(context);
//		}
//		return 0;
//	}
	
	public static EmailContent.Account systemHaveSameConfig(Context context, String address) {
		EmailContent.Account aa = EmailContent.Account.restoreAccountWithAddress(context, address);
		return aa;
	}
	
	public long getEmailAccountId()
	{
		long id = 0;
		if (null != emailAccount) {
			id = emailAccount.mId;
		}
		return id;
	}

	public String getEmailAccountAddress()
	{
		String address = null;
		if (null != emailAccount) {
			address = emailAccount.mEmailAddress;
		}
		return address;
	}

	private static void saveAccount(Context context, EmailContent.Account account) {
		if (!account.isSaved()) {
			account.save(context);
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
	}
	
	private EmailContent.Account emailAccount = null;
}