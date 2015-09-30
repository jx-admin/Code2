package com.aess.aemm.setting;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import com.aess.aemm.setting.ExchangeProfile.ExchangeArg;
import com.android.email.mail.store.ExchangeStore;
import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailContent.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class ExchangeSetup {

	public final static String TAG = "ExchangeSetup";

	public int setAccount(Context context, ExchangeArg arg,
			EmailContent.Account oldAccount) {
		Log.i(TAG, "setAccount");
		int ret = 0;
		EmailContent.Account account = null;
		if (null == oldAccount) {
			account = new EmailContent.Account();
		} else {
			account = oldAccount;
		}

		String scheme = arg.bSSL ? "eas+ssl+trustallcerts+" : "eas+";

		account = new EmailContent.Account();

		account.setEmailAddress(arg.address);

		account.setDisplayName(arg.userName);

		account.setSyncLookback(arg.syncday);

		account.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);

		account.setSyncInterval(Account.CHECK_INTERVAL_NEVER);

		int newFlags = account.getFlags();

		newFlags |= EmailContent.Account.FLAGS_NOTIFY_NEW_MAIL
				| Account.FLAGS_INCOMPLETE;

		account.setFlags(newFlags);

		String userInfo = arg.address + ":" + arg.password;
		URI uri = null;
		try {
			uri = new URI(scheme, userInfo, arg.host, 0, null, null, null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (null != uri) {

			account.setStoreUri(context, uri.toString());
			account.setSenderUri(context, uri.toString());

			exchangeAccount = account;
			ret = 1;
		}

		return ret;
	}
	
	int newAccount(Context context, ExchangeArg arg) {
		int ret = 0;
		exchangeAccount = null;
		EmailContent.Account account = null;
		if (null != context && null != arg && null != arg.address) {
			
			String[] emailParts = arg.address.split("@");
			
			if (emailParts.length >= 2) {
				
		        String domain = emailParts[1].trim();
		        String password = arg.password;
		        
				account = new EmailContent.Account();
				
				if (null == arg.userName) {
					arg.userName = arg.address;
				}
				
				account.setDisplayName(arg.userName);
				account.setSenderName(domain);
				account.setEmailAddress(arg.address);
				
				
		        try {
		        	String head = "eas+trustallcerts";
		        	if (true == arg.bSSL) {
		        		head = "eas+ssl+trustallcerts";
		        	}
		        	
//					URI uri = new URI(head, arg.userName, domain, 0, null, null, null);
//		            account.setStoreUri(context, uri.toString());
//		            account.setSenderUri(context, uri.toString());
//		            
//		            account.mHostAuthRecv.mLogin = "\\" + arg.address;
//		            account.mHostAuthRecv.mAddress = arg.host;
		            
					String userInfo = arg.userName + ":" + password;
					String path = null;

					URI uri1 = new URI(head, userInfo, arg.host, 0, path, null, null);
					
					account.setStoreUri(context, uri1.toString());
					account.setSenderUri(context, uri1.toString());
					
		        } catch (URISyntaxException use) {
		        	account = null;
		            return -1;
		        }

				account.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);
				account.setSyncInterval(Account.CHECK_INTERVAL_PUSH);
		        account.setSyncLookback(arg.syncday);
			}
		}
		exchangeAccount = account;
		ret = 1;
		return ret;
	}

	public int addExchangeAccount(Context context) {
		Log.i(TAG, "addExchangeAccount");

		int ret = 0;
		if (null != exchangeAccount && null != context) {
			mAdd = 0;
			
			ExchangeStore.addSystemAccount(context, exchangeAccount, true,
					true, mAccountManagerCallback);
			
			EmailSetup.submitEmailAccount(context, exchangeAccount);

			int itryTimes = 0;
			while (itryTimes < 10) {
				if (mAdd != 0) {
					break;
				}
				try {
					Thread.sleep(3000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				itryTimes++;
			}
			exchangeAccount = null;
//			if (mAdd != 2) {
//				deleteExchangeAccount(context, exchangeAccount.mEmailAddress);
//				ret = 1;
//			}
		}
		
		return ret;

	}
	
	public Boolean deleteExchangeAccount(Context context, String accountName) {

		EmailContent.Account emailcount = new EmailContent.Account();

		emailcount.mEmailAddress = accountName;
		mRemoved = 0;

		AccountManagerFuture<Boolean> amf = ExchangeStore.removeSystemAccount(
				context, emailcount, null);
		
		EmailSetup.delEmailAccountByAddress(context, emailcount.mEmailAddress);
		
		boolean rlt = false;
		try {
			rlt = amf.getResult();
		} catch (OperationCanceledException e) {
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rlt;
	}

//	public Boolean deleteExchangeAccount(Context context, String accountName) {
//		try {
////
////			String easType = "com.android.exchange";
////			android.accounts.Account mAccount = new android.accounts.Account(
////					accountName, easType);
//			EmailContent.Account account = new EmailContent.Account();
//			
//			account.mEmailAddress = accountName;
//			mRemoved = 0;
//			
//			AccountManagerFuture<Boolean> amf = ExchangeStore.removeSystemAccount(context, account, null);
//			boolean rlt = false;
//			rlt = amf.getResult();
////			AccountManager am = AccountManager.get(context);
////			if (null != am) {
////				am.removeAccount(mAccount, mReAccountManagerCallback, null);
////			}
////	
//
////			int itryTimes = 0;
////			while (itryTimes < 10) {
////				if (mRemoved != 0) {
////					break;
////				}
////				try {
////					Thread.sleep(3000);
////				} catch (Exception e) {
////					e.printStackTrace();
////				}
////				itryTimes++;
////			}
////		} catch (Exception e) {
////			//mRemoved = -1;
////		}
//		return rlt;
//	}
	
	public android.accounts.Account getSysExchangeAccount(Context context, String accountName) {
		android.accounts.Account account = null;
		try {
			String easType = "com.android.exchange";

			android.accounts.Account[] acclist = AccountManager.get(context).getAccountsByType(easType);
			for (android.accounts.Account acc : acclist) {
				if (acc.name.equals(accountName)) {
					account = acc;
					break;
				}
			}
		} catch (Exception e) {
		}
		return account;
	}

//	private void addEmail() {
//		exchangeAccount.mFlags &= ~Account.FLAGS_INCOMPLETE;
//		if (null != mcontext && null != exchangeAccount) {
//			if (EmailSetup.submitEmailAccount(mcontext, exchangeAccount) > 0 ) {
//				mAdd = 2;
//			}
//		}
//	}

	@SuppressWarnings("unused")
	private AccountManagerCallback<Boolean> mReAccountManagerCallback = new AccountManagerCallback<Boolean>() {
		public void run(AccountManagerFuture<Boolean> future) {
			mRemoved = 1;
			try {
				if (future.getResult() == true) {
					mRemoved = -1;
				}

			} catch (OperationCanceledException e) {
				mRemoved = -2;
				e.printStackTrace();
			} catch (IOException e) {
				mRemoved = -3;
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				mRemoved = -4;
				e.printStackTrace();
			}
		}
	};

	private AccountManagerCallback<Bundle> mAccountManagerCallback = new AccountManagerCallback<Bundle>() {
		public void run(AccountManagerFuture<Bundle> future) {
			try {
				Bundle bundle = future.getResult();
				bundle.keySet();
				mAdd = 1;
				// addEmail();
				return;
			} catch (OperationCanceledException e) {
				mAdd = -1;
				e.printStackTrace();
			} catch (IOException e) {
				mAdd = -2;
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				mAdd = -3;
				e.printStackTrace();
			}

		}
	};
	
//	private int checkarg(String address, String host) {
//		int ret = 1;
////		if (null != address && null != host) {
////			String[] emailParts = address.split("@");
////			if (emailParts.length > 1) {
////				if (host.equals(emailParts[1])) {
////					ret = 1;
////				}
////			}
////		}
//
//		return ret;
//	}

//	private Context mcontext = null;
	private EmailContent.Account exchangeAccount = null;
	private int mAdd = 0;
	@SuppressWarnings("unused")
	private int mRemoved = 0;
}
