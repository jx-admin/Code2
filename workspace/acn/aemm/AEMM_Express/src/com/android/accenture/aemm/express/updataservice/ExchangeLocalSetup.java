package com.android.accenture.aemm.express.updataservice;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Process;
import android.util.Log;

import com.android.email.Email;
import com.android.email.mail.AuthenticationFailedException;
import com.android.email.mail.MessagingException;
import com.android.email.mail.Store;
import com.android.email.mail.store.ExchangeStore;
import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailContent.Account;
import com.android.email.provider.EmailContent.HostAuth;
import com.android.email.service.EmailServiceProxy;


public class ExchangeLocalSetup {

	public static final String TAG = "ExchangeLocalSetup";
	//String accountAddress;
	String password;
	String domain;
	String serverAddress;
	String emailAddress;
	String displayName;
	//fix bug2715 cuixiaowei 20110722
	int syncLookback; //add a parameter of sync days. 

	boolean ssl;
	EmailContent.Account exchangeAccount;

	Context mContext;
	public ExchangeLocalSetup(Context mContext)
	{
		this.mContext = mContext;
	}
	boolean bfinish;
	int mRemoved = -2;
	public void setParameterValues(String accountAddress,
			String password,
			String displayName,
			String serverAddress,
			boolean ssl,
			int syncDays)
	{
		this.emailAddress = accountAddress;
		this.password = password;
		this.displayName = displayName;
		this.serverAddress = serverAddress;
		this.ssl = ssl;
		this.bfinish = false;
		this.syncLookback = syncDays;
	}

	//Bug #2792 shxn
	public int validata() {
		checkAuto();
		return 0;
	}
	//Bug #2792 shxn
	
	int checkAuto() {
		// ExchangeUtils.startExchangeService(this.getApplication());
		// strName = "tester@aemm.local";
		// strEmailAddress = strName + "@" + strDomain;
		int ret = -1;
		String domain = null;

		String[] emailParts = emailAddress.split("@");
		if (emailParts.length >= 2) {

			domain = emailParts[1].trim();
			
			try {
				// uri.getUserInfo(),
				// uri.getHost(),
				// uri.getPort(),
				URI uri = new URI("eas+ssl+trustallcerts", emailAddress,
						domain, 0, null, null, null);
				exchangeAccount = new EmailContent.Account();
				exchangeAccount.setEmailAddress(emailAddress);
				exchangeAccount.setStoreUri(mContext, uri.toString());
				exchangeAccount.setSenderUri(mContext, uri.toString());
				exchangeAccount.setSyncLookback(syncLookback); // fix bug2715
				// cuixiaowei
				// 20110722
			} catch (URISyntaxException e) {
				//This should not happen.
				e.printStackTrace();
				throw new Error(e);
			}

			try {

				Log.d(Email.LOG_TAG, "Begin auto-discover for " + emailAddress);
				Log
						.d(Email.LOG_TAG, "Begin auto-discover for "
								+ serverAddress);

				exchangeAccount.mHostAuthRecv.mLogin = "\\" + emailAddress;
				exchangeAccount.mHostAuthRecv.mAddress = serverAddress;

				String userInfo = emailAddress + ":" + password;
				String path = null;

				/* "eas+ssl+", */
				URI uri1 = new URI("eas+ssl+trustallcerts", userInfo,
						serverAddress, 0, path, null, null);
				exchangeAccount.setStoreUri(mContext, uri1.toString());
				exchangeAccount.setSenderUri(mContext, uri1.toString());
				exchangeAccount
						.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);
				exchangeAccount.setSyncInterval(Account.CHECK_INTERVAL_PUSH);
				// exchangeAccount.setSyncLookback(1);


			}catch (URISyntaxException e) {
				//This should not happen.
				e.printStackTrace();
				throw new Error(e);
			}
			//Bug #2792 shxn
			ret = 0;
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
			try {
				Store store = Store.getInstance(exchangeAccount
						.getStoreUri(mContext), mContext, null);

				store.checkSettings();
			} catch (final AuthenticationFailedException afe) {
				if (afe != null) {
					Log.w(TAG, afe.toString());
				}
			} catch (final MessagingException me) {
				if (me != null) {
					Log.w(TAG, me.toString());
				}
			} catch (Exception e) {
				if (e != null) {
					Log.e(TAG, e.toString());
				}
			}
			
			try {
				saveAccount();
			} catch (Exception e) {
				if (e != null) {
					Log.e(TAG, e.toString());
				}
			}
			//Bug #2792 shxn
		}
		return ret;
	}


	private int saveAccount()
	{
		int ret = -1;
		Log.d(Email.LOG_TAG, "saveAccount");
		int newFlags = exchangeAccount.getFlags() ;
		newFlags |= EmailContent.Account.FLAGS_NOTIFY_NEW_MAIL;

		//exchangeAccount.mSyncLookback = 2;
		exchangeAccount.mSyncInterval = EmailContent.Account.CHECK_INTERVAL_PUSH;
		exchangeAccount.mDisplayName = displayName;
		exchangeAccount.setFlags(newFlags);
		boolean alsoSyncContacts = true;
		boolean alsoSyncCalendar = true;
		// Set the incomplete flag here to avoid reconciliation issues in SyncManager (EAS)
		exchangeAccount.mFlags |= Account.FLAGS_INCOMPLETE;
		
		ExchangeStore.addSystemAccount(mContext, exchangeAccount,
				alsoSyncContacts, alsoSyncCalendar, mAccountManagerCallback);
		
		EmailLocalSetup.commitSettings(mContext, exchangeAccount);


		int itryTimes = 0;
		while(itryTimes<10)
		{
			if (bfinish)
				break;
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			itryTimes++;
		}

		return ret;



	}
	public int deleteAccount(String accountName)
	{
		try {
			String easType = "com.android.exchange";
			android.accounts.Account mAccount = new android.accounts.Account(accountName, easType);
			AccountManager.get(mContext).removeAccount(mAccount,mReAccountManagerCallback, null);
	
			while(true)
			{
				if (mRemoved != -2)
					break;

				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
			return mRemoved;
		} catch(Exception e) {
		}
		return -1;
	}

	private void finishOnDone() {
		// Clear the incomplete flag now
		exchangeAccount.mFlags &= ~Account.FLAGS_INCOMPLETE;
		EmailLocalSetup.commitSettings(mContext, exchangeAccount);
		// Email.setServicesEnabled(this);
		// AccountSetupNames.actionSetNames(this, mAccount.mId, mEasFlowMode);
		// Start up SyncManager (if it isn't already running)
		//  ExchangeUtils.startExchangeService(this);
		bfinish = true;

	}
	AccountManagerCallback<Boolean> mReAccountManagerCallback = new AccountManagerCallback<Boolean>() {
		public void run(AccountManagerFuture<Boolean> future) {
			boolean failed = true;
			try {
				if (future.getResult() == true) {
					failed = false;
				}

			} catch (OperationCanceledException e) {
				// handled below
			} catch (IOException e) {
				// handled below
			} catch (AuthenticatorException e) {
				// handled below
			}
			if (failed) {
				// showDialog(FAILED_REMOVAL_DIALOG);
				mRemoved = -1;
			} else {
				// finish();
				mRemoved = 0;
			}

		}
	};
	AccountManagerCallback<Bundle> mAccountManagerCallback = new AccountManagerCallback<Bundle>() {
		public void run(AccountManagerFuture<Bundle> future) {
			try {
				Bundle bundle = future.getResult();
				bundle.keySet();
				finishOnDone();

				return;
			} catch (OperationCanceledException e) {
				Log.d(Email.LOG_TAG, "addAccount was canceled");
			} catch (IOException e) {
				Log.d(Email.LOG_TAG, "addAccount failed: " + e);
			} catch (AuthenticatorException e) {
				Log.d(Email.LOG_TAG, "addAccount failed: " + e);
			}

		}
	};
}
