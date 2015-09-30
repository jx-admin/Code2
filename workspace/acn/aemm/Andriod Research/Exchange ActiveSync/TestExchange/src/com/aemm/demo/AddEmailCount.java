package com.aemm.demo;


import java.net.URI;
import java.net.URISyntaxException;

import com.android.email.Email;

import com.android.email.mail.AuthenticationFailedException;
import com.android.email.mail.MessagingException;
import com.android.email.mail.Sender;
import com.android.email.mail.Store;
import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailContent.Account;
import com.android.email.provider.EmailContent.HostAuth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddEmailCount extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addemailcount);
        findviews();
        
       
   
 }
	String emailAddress;
	String password;
	String incomingServer;
	String outgoingServer;
	RadioGroup mRadioGroup;
	 private final static int DEFAULT_ACCOUNT_CHECK_INTERVAL = 15;
	private EditText Edit_address;
	private EditText Edit_password;
	private EditText Edit_incoming;
	private EditText Edit_outgoing;
	private EditText Edit_incomingPort;
	private EditText Edit_outgoingPort;
	private RadioGroup radioGroup;
	private RadioButton radioPop3;
	private RadioButton radioImap;
	private Button btn_next;
	int emailType = 1;
	int incomingPort = 0;
    int outgoingPort = 0;
	 public EmailContent.Account emailAccount;
	//private Provider mProvider;
	private void findviews()
	{
		Edit_address = (EditText)findViewById(R.id.emailAddress);
		Edit_password = (EditText)findViewById(R.id.emailPassword);
		btn_next = (Button)findViewById(R.id.EmailNext);
		btn_next.setOnClickListener(nextListener);
		
		Edit_incoming = (EditText)findViewById(R.id.incomingServer);
		Edit_outgoing = (EditText)findViewById(R.id.outgoingServer);
		Edit_incomingPort = (EditText)findViewById(R.id.incomingServerPort);
		Edit_outgoingPort = (EditText)findViewById(R.id.outgoingServerPort);
		radioGroup = (RadioGroup)findViewById(R.id.emailType);
		radioGroup.setOnCheckedChangeListener(mChangeRadio);
		
		radioPop3 = (RadioButton)findViewById(R.id.pop3);
		radioImap = (RadioButton)findViewById(R.id.imap);

		
	}
	private void setParameterValues()
	{
		emailAddress = Edit_address.getText().toString();
		password = Edit_password.getText().toString();
		incomingServer = Edit_incoming.getText().toString();
		outgoingServer = Edit_outgoing.getText().toString();
		try{
			incomingPort = Integer.parseInt(Edit_incomingPort.getText().toString());
			outgoingPort = Integer.parseInt(Edit_outgoingPort.getText().toString());
		}catch(Exception e)
		{
            return;
        }

		
	}
	 private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener()
	 {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			if (checkedId == radioPop3.getId())
			{
				emailType = 0;
				//setvalue
				Edit_address.setText(String.valueOf("fengyun8223@hotmail.com"));
				Edit_password.setText(String.valueOf("551128"));
				Edit_incoming.setText(String.valueOf("pop3.live.com"));
				Edit_outgoing.setText(String.valueOf("smtp.live.com"));
				Edit_incomingPort.setText(String.valueOf("995"));
				Edit_outgoingPort.setText(String.valueOf("587"));
				setParameterValues();
				
				Log.d("AddEmailCount", "emailType is 0");
			}
			else if (checkedId == radioImap.getId())
			{
				emailType = 1;
				Edit_address.setText(String.valueOf("Vivian.yun.feng@gmail.com"));
				Edit_password.setText(String.valueOf("v12345678"));
				Edit_incoming.setText(String.valueOf("imap.gmail.com"));
				Edit_outgoing.setText(String.valueOf("smtp.gmail.com"));
				Edit_incomingPort.setText(String.valueOf("993"));
				Edit_outgoingPort.setText(String.valueOf("465"));
				setParameterValues();
				Log.d("AddEmailCount", "emailType is 1");
			}
		}
		 
	 };
	 private OnClickListener nextListener = new OnClickListener()
	 {
		    public void onClick(View v)
		    {
		    	Toast.makeText(AddEmailCount.this, Edit_password.getText().toString(), Toast.LENGTH_SHORT).show();   
		    	setParameterValues();
		    	try {
		    			OnNext();
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		       		
		    	}
	  };
	  private void OnNext()throws MessagingException
	  {
		  String[] emailParts = emailAddress.split("@");
          String domain = emailParts[1].trim();
          /*
    	   * private static final int POP_PORTS[] = {
            		110, 995, 995, 110, 110
		    };
		    private static final String POP_SCHEMES[] = {
		            "pop3", "pop3+ssl+", "pop3+ssl+trustallcerts", "pop3+tls+", "pop3+tls+trustallcerts"
		    };
		    private static final int IMAP_PORTS[] = {
		            143, 993, 993, 143, 143
		    };
		    private static final String IMAP_SCHEMES[] = {
		            "imap", "imap+ssl+", "imap+ssl+trustallcerts", "imap+tls+", "imap+tls+trustallcerts"
		    };
		     private static final int SMTP_PORTS[] = {
            		587, 465, 465, 587, 587
		    };
		
		    private static final String SMTP_SCHEMES[] = {
		            "smtp", "smtp+ssl+", "smtp+ssl+trustallcerts", "smtp+tls+", "smtp+tls+trustallcerts"
		    };
    	   */
          URI incomingUri = null;
          URI outgoingUri = null;
          String incomingSheme = null;
          String outgoingSheme = null;
          String userInfo = emailAddress+":"+password;
          emailAccount = new EmailContent.Account();
          
          if (emailType == 0)
          {
        	  incomingSheme = "pop3+ssl+";
        	  outgoingSheme = "smtp+tls+";
        	  incomingPort = 995;
        	  outgoingPort = 587;
        	  emailAccount.setDisplayName("pop3Test");
        	  emailAccount.setSenderName("pop3Test");
          }
          else if(emailType == 1)
          {
        	  incomingSheme = "imap+ssl+";
        	  outgoingSheme = "smtp+ssl+";
        	  incomingPort = 993;
        	  outgoingPort = 465;
        	  emailAccount.setDisplayName("imapTest");
        	  emailAccount.setSenderName("pop3Test");
          }
                  
          	  try{
          		  	Log.d("AddEmailCount", incomingServer + ";" + userInfo);
          		  incomingUri = new URI(incomingSheme, userInfo,incomingServer,incomingPort, null, null, null);//uri.getUserInfo(), uri.getHost(), uri.getPort(),
          		  outgoingUri = new URI(outgoingSheme, userInfo,outgoingServer,outgoingPort, null, null, null);//uri.getUserInfo(), uri.getHost(), uri.getPort(),
          		 
          		  emailAccount.setEmailAddress(emailAddress);
          		  emailAccount.setStoreUri(this, incomingUri.toString());
          		  emailAccount.setSenderUri(this, outgoingUri.toString());
          		  emailAccount.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);
          		  if (incomingUri.toString().startsWith("imap")) {
                     // Delete policy must be set explicitly, because IMAP does not provide a UI selection
                     // for it. This logic needs to be followed in the auto setup flow as well.
          			emailAccount.setDeletePolicy(EmailContent.Account.DELETE_POLICY_ON_DELETE);
          		  }
          		  emailAccount.setSyncInterval(DEFAULT_ACCOUNT_CHECK_INTERVAL);
          	  }catch(URISyntaxException use) 
          	  {
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
   		    	   //step 1 .incoming setting
   		    	 Log.d(Email.LOG_TAG, "Begin check of incoming email settings");
                 Store store = Store.getInstance(
                         emailAccount.getStoreUri(AddEmailCount.this),
                         getApplication(), null);
                 store.checkSettings();
                 Log.d(Email.LOG_TAG, "check of incoming email settings ok");
                 
                 //step2 .outgoing setting
                 Log.d(Email.LOG_TAG, "Begin check of outgoing email settings");
               
                 Sender sender = Sender.getInstance(getApplication(),
                		 emailAccount.getSenderUri(AddEmailCount.this));
                 sender.close();
                 sender.open();
                 sender.close();
                 
                 //emailAccount.save(AddEmailCount.this);
                 AddCount.commitSettings(AddEmailCount.this, emailAccount);
                 setResult(RESULT_OK);
                 finish();
                 
   		       }catch(final AuthenticationFailedException afe)
		       {
		    	   
		       } catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
   		     }  
          

          }.start();
          
	  }
	
	  
}
