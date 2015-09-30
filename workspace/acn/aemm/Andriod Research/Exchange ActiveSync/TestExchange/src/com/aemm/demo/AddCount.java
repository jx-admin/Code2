package com.aemm.demo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.android.email.AccountBackupRestore;
import com.android.email.Email;
import com.android.email.ExchangeUtils;

import com.android.email.mail.AuthenticationFailedException;
import com.android.email.mail.MessagingException;
import com.android.email.mail.Store;
import com.android.email.mail.store.ExchangeStore;
import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailContent.Account;
import com.android.email.provider.EmailContent.AccountColumns;
import com.android.email.provider.EmailContent.HostAuth;
import com.android.email.service.EasAuthenticatorService;
import com.android.email.service.EmailServiceProxy;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;






public class AddCount extends Activity {
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.addcount);
	        findviews();
	       
	   
	 }
	 private EditText Edit_address;
	 private EditText Edit_password;
	 private EditText Edit_domain;
	 private EditText Edit_serveraddress;
	 private Button btn_next;
	 private CheckBox ch_ssl;
	 private TextView Text_status;
	 
	 private String strName;
	 private String strPassword;
	 private String strDomain;
	 private String strServerAddress;
	 private String strEmailAddress;
	 public  static  boolean isSsl;
	 public EmailContent.Account account1;
	 
	 private void findviews()
	 {
		 btn_next = (Button)findViewById(R.id.next);
		 Edit_address = (EditText)findViewById(R.id.address);
		 Edit_password = (EditText)findViewById(R.id.password);
		 Edit_domain = (EditText)findViewById(R.id.domain);
		 Edit_serveraddress = (EditText)findViewById(R.id.server);
		 ch_ssl = (CheckBox)findViewById(R.id.checkbox_ssl);
		 Text_status = (TextView)findViewById(R.id.status);
		
		
		 btn_next.setOnClickListener(nextListener);
	 }
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
       AccountManagerCallback<Bundle> mAccountManagerCallback1 = new AccountManagerCallback<Bundle>() {
           public void run(AccountManagerFuture<Bundle> future) {
               try {
                   Bundle bundle = future.getResult();
                   bundle.keySet();
                   finish();
                   
                 
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
	 private void setAccount()
	 {
		
		 	Log.d("set account", "name:" +strName + "domain:" + strDomain);
		 	account1 = TestSetupAccount(strName,strDomain, false, this);
	        // add hostauth data, which should be saved the first time
	        account1.mHostAuthRecv = TestSetupHostAuth("eas",strServerAddress,strName,strDomain,strPassword,isSsl, false,
	        		this);
	        account1.mHostAuthSend = TestSetupHostAuth("eas",strServerAddress,strName,strDomain,strPassword, isSsl, false,
	        		this);
	        Log.d(Email.LOG_TAG, "Begin check of incoming email settings");
	        //
	        // run backups
	        account1.mFlags |= Account.FLAGS_INCOMPLETE;
	        commitSettings(this, account1);
	   
	       // AccountBackupRestore.backupAccounts(this);
	     
	        Log.d(Email.LOG_TAG, "Begin check of incoming email settings");
	     
	      	 ExchangeStore.addSystemAccount(this, account1,
	                     true, true, mAccountManagerCallback);
	      	 
	      	 long account1Id = account1.mId;
	         Account account2 = EmailContent.Account.restoreAccountWithId(this, account1Id);
	         
	         Log.d(Email.LOG_TAG, account2.mEmailAddress);
		
	     
		 
	 }
	 private void finishOnDone() {
	        // Clear the incomplete flag now
		 	account1.mFlags &= ~Account.FLAGS_INCOMPLETE;
	        commitSettings(this, account1);
	       // Email.setServicesEnabled(this);
	       // AccountSetupNames.actionSetNames(this, mAccount.mId, mEasFlowMode);
	        // Start up SyncManager (if it isn't already running)
	      //  ExchangeUtils.startExchangeService(this);
	        finish();
	    }
	 private void setParameterValues()
	 {
		 strName = Edit_address.getText().toString();
		 strPassword = Edit_password.getText().toString();
		 strServerAddress = Edit_serveraddress.getText().toString();
		 strDomain = Edit_domain.getText().toString();
		 
		 
		 if (ch_ssl.isChecked())
			 isSsl = true;
		 else
			 isSsl = false;
		 
		 Text_status.setText(String.valueOf("Start checking ...."));
	 }
	 public static void commitSettings(Context context, EmailContent.Account account) {
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
	        // Update the backup (side copy) of the accounts
	      //  AccountBackupRestore.backupAccounts(context);
	        //we need to write preference file in /data/data/com.android.email/shared_prefs
	        //AndroidMail.Main.xml
	        //UpdateEmailPrefs(account);
	    }
	 
	 public static EmailContent.Account TestSetupAccount(String name,String domain, boolean saveIt, Context context) 
	    {
	    	EmailContent.Account account = new EmailContent.Account();

	        account.mDisplayName = name;
	        account.mEmailAddress = name +"@"+ domain;
	       // account.mSyncKey = "sync-key-" + name;
	        account.mSyncLookback = 2;
	        account.mSyncInterval = EmailContent.Account.CHECK_INTERVAL_PUSH;
	      //  account.mHostAuthKeyRecv = 0;
	      //  account.mHostAuthKeySend = 0;
	        account.mFlags = 9;
	        account.mIsDefault = true;
	       // account.mCompatibilityUuid = "test-uid-" + name;
	       // account.mSenderName = null;
	        account.mRingtoneUri = "content://ringtone-" + name;
	        account.mProtocolVersion = "12.0";
	        account.mNewMessageCount = 0;
	        account.mSecurityFlags = 0;
	        account.mSecuritySyncKey = null;
	        account.mSignature = null;
	        
	        if (!isSsl)//ticker
	        	 account.mProtocolVersion = "2.5";
	        if (saveIt) {
	            account.save(context);
	        }
	        return account;
	    }
	    public static HostAuth TestSetupHostAuth(String protocol,String server, String name,
	    		String domain,String password,boolean isSsl,boolean saveIt, Context context) 
	    {
	        HostAuth hostAuth = new HostAuth();

	        hostAuth.mProtocol = protocol;// + "-" + name;
	        hostAuth.mAddress = server;// "m.google.com";
	        hostAuth.mPort = 0;
	        hostAuth.mFlags = 5;
	        hostAuth.mLogin = domain+ "\\"+ name;
	        hostAuth.mPassword = password;//"y12345678";
	        hostAuth.mDomain =  null;
	        hostAuth.mAccountKey = 0;
	        
	        if (!isSsl)
	        	 hostAuth.mFlags = 4;
	        if (saveIt) {
	            hostAuth.save(context);
	        }
	        return hostAuth;
	    }
	    
	    private OnClickListener nextListener = new OnClickListener()
	    {
	    	public void onClick(View v)
	    	{
	    		 
	    		 Toast.makeText(AddCount.this, Edit_password.getText().toString(), Toast.LENGTH_SHORT).show();   
	    		 setParameterValues();
	    		 try {
					checkAuto();
	    			
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	       		// setAccount();
	    		// UpdateEmailPrefs(account1);
	    		// savaExchangeAccount();
	    	}
	    		
	    };
	    private void checkSettings()throws MessagingException
	    {
	    	
	    }
	    private void checkAuto() throws MessagingException
	    {
	    	// ExchangeUtils.startExchangeService(this.getApplication());
	    	//strName = "tester@aemm.local";
	    	strEmailAddress = strName + "@" + strDomain;
	    	try{
	    		URI uri = new URI("eas+ssl+trustallcerts", strEmailAddress,strDomain,0,//uri.getUserInfo(), uri.getHost(), uri.getPort(),
	    		                    null, null, null);
	    		account1 = new EmailContent.Account();
	    		account1.setEmailAddress(strEmailAddress);
	    		account1.setStoreUri(this, uri.toString());
	 	        account1.setSenderUri(this, uri.toString());
	    	}catch(URISyntaxException use) {
	            /*
	             * This should not happen.
	             */
	            throw new Error(use);
	    	}
		    new Thread() {
		     @Override
		     public void run() {
		       Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		       try{
		    	   Log.d(Email.LOG_TAG, "Begin auto-discover for " + strEmailAddress);
	               
			        Store store = Store.getInstance(
			        		account1.getStoreUri(AddCount.this),
		                    getApplication(), null);
			        Log.d(Email.LOG_TAG, "Begin auto-discover for ");
		            Bundle result = store.autoDiscover(AddCount.this,
		            		strEmailAddress, strPassword);
		           
		       	if (result != null) 
	            	{
	            		int errorCode =
	            			result.getInt(EmailServiceProxy.AUTO_DISCOVER_BUNDLE_ERROR_CODE);
	            		if (errorCode == MessagingException.AUTHENTICATION_FAILED) 
	            		{
	            			//throw new AutoDiscoverAuthenticationException(null);
	            		} else if (errorCode != MessagingException.NO_ERROR) 
	            		{
	            			//setResult(RESULT_OK);
	            			//finish();
	            			//cat not discover
	            			//manul setup
	            			
	            			 HostAuth hostAuth = account1.mHostAuthRecv;
	            			 hostAuth.mLogin = "\\"+strName+"@"+strDomain;//"\tester@aemm.local";
	            			 String userInfo = strEmailAddress+":"+strPassword;
	                         String path = null;
	                         String host = strServerAddress;
	                         String scheme = (isSsl ? "eas+ssl+trustallcerts" : "eas");
	                      
	                         URI uri1 = new URI(
	                        		 scheme,
	                                 userInfo,
	                                 host,
	                                 0,
	                                 path,
	                                 null,
	                                 null);
	                 		account1.setStoreUri(AddCount.this, uri1.toString());
	        	 	        account1.setSenderUri(AddCount.this, uri1.toString());
	        	 	        account1.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);
	            			account1.setSyncInterval(Account.CHECK_INTERVAL_PUSH);
	            			account1.setSyncLookback(1);
	                         Store store1 = Store.getInstance(
	                                 account1.getStoreUri(AddCount.this),
	                                 getApplication(), null);
	                         store1.checkSettings();
	         
	            			
	            			
	            		}
	            		else
	            		{
		            		// The success case is here
		                    //Intent resultIntent = new Intent();
		                    //resultIntent.putExtra("HostAuth", result.getParcelable(
		                    //  EmailServiceProxy.AUTO_DISCOVER_BUNDLE_HOST_AUTH));
		                   // setResult(RESULT_OK, resultIntent);
		            		Parcelable pp = result.getParcelable(
		                            EmailServiceProxy.AUTO_DISCOVER_BUNDLE_HOST_AUTH);
		            		if (pp != null)
		            		{
		            			 HostAuth hostAuth = (HostAuth)pp;
		                         account1.mHostAuthSend = hostAuth;
		                         account1.mHostAuthRecv = hostAuth;
		                         Log.d(Email.LOG_TAG, hostAuth.mAddress);
		                         Log.d(Email.LOG_TAG, hostAuth.mLogin);
		                         //
		                         String userInfo = strEmailAddress+":"+strPassword;
		                         String path = null;
		                         String host = hostAuth.mAddress;//"apa.email.accenture.com";
		                         URI uri1 = new URI(
		                                 "eas+ssl+",
		                                 userInfo,
		                                 host,
		                                 0,
		                                 path,
		                                 null,
		                                 null);
		                 		account1.setStoreUri(AddCount.this, uri1.toString());
		        	 	        account1.setSenderUri(AddCount.this, uri1.toString());
		        	 	        account1.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);
		            			account1.setSyncInterval(Account.CHECK_INTERVAL_PUSH);
		            			account1.setSyncLookback(1);
		                        Store store1 = Store.getInstance(
		                                 account1.getStoreUri(AddCount.this),
		                                 getApplication(), null);
		                        store1.checkSettings();
		                        //Text_status.setText("incoming setting");
	            		}
	            	}
	                    
	            	}
		       }catch(final AuthenticationFailedException afe)
		       {
		    	   
		        } catch (final MessagingException me) {
				// TODO Auto-generated catch block
				me.printStackTrace();
				int exceptionType = me.getExceptionType();
	            // Check for non-fatal errors first
	            if (exceptionType == MessagingException.SECURITY_POLICIES_REQUIRED) {
	                //showSecurityRequiredDialog();
	            	saveAccount();
	            	return;
	            }
			} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
		    
		    }.start();
	    } 
	    private void saveAccount()
	    {
	    	 Log.d(Email.LOG_TAG, "saveAccount");
	    	 int newFlags = account1.getFlags() ;
	    	 newFlags |= EmailContent.Account.FLAGS_NOTIFY_NEW_MAIL;
	    	 
	    	 account1.mSyncLookback = 2;
		     account1.mSyncInterval = EmailContent.Account.CHECK_INTERVAL_PUSH;
		     account1.mDisplayName = "tester";
		     account1.setFlags(newFlags);
		     boolean alsoSyncContacts = true;
		     boolean alsoSyncCalendar = true;
		     // Set the incomplete flag here to avoid reconciliation issues in SyncManager (EAS)
		     account1.mFlags |= Account.FLAGS_INCOMPLETE;
	         commitSettings(this, account1);
	           ExchangeStore.addSystemAccount(getApplication(), account1,
	                    alsoSyncContacts, alsoSyncCalendar, mAccountManagerCallback);
		    
	    }
	    private void savaExchangeAccount()
	    {
	    	Log.d(Email.LOG_TAG, "savaExchangeAccount");
	    	Bundle options = new Bundle();
	    	options.putString("username", "tester@aemm.local");
	        options.putString("password", "Test1234");
	        options.putBoolean("contacts", true);
	        options.putBoolean("calendar", true);
	        Log.d(Email.LOG_TAG, "savaExchangeAccount");

	        // Here's where we tell AccountManager about the new account.  The addAccount
	        // method in AccountManager calls the addAccount method in our authenticator
	        // service (EasAuthenticatorService)
	        final android.accounts.Account account = new android.accounts.Account(options.getString("username"),Email.EXCHANGE_ACCOUNT_MANAGER_TYPE);
	        Log.d(Email.LOG_TAG, "savaExchangeAccount");
	       
	        AccountManager.get(getApplication()).addAccount(Email.EXCHANGE_ACCOUNT_MANAGER_TYPE,
	                null, null, options, null, mAccountManagerCallback1, null);
	       // Log.d(Email.LOG_TAG, "savaExchangeAccount");
	    }
}
