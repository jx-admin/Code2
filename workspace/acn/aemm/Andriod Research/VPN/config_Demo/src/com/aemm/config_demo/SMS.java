package com.aemm.config_demo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.PackageInfo;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.security.Credentials;
import android.security.KeyStore;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;






//----VPN Setting demo----


import com.aemm.config_demo.Util;

import android.net.vpn.L2tpIpsecProfile;
import android.net.vpn.L2tpIpsecPskProfile;
import android.net.vpn.VpnProfile;
import android.net.vpn.PptpProfile;
import android.net.vpn.L2tpProfile;
import android.net.vpn.VpnManager;
import android.net.vpn.VpnType;



public class SMS extends Activity 
{
	private Context context;
	Button btnSendSMS;
	EditText txtPhoneNo;
	EditText txtMessage;
	
	//-----VPN Setting demo---------
	private VpnManager mVpnManager = new VpnManager(this);
	private VpnProfile mProfile;
	private PptpProfile mPptpProfile;
	private VpnProfile mEditorProfile;
	private VpnProfile mMemProfile;
	private static final String PROFILES_ROOT = VpnManager.PROFILES_PATH + "/";
	private static final String PROFILE_OBJ_FILE = ".pobj";
	
	private KeyStore mKeyStore = KeyStore.getInstance();
	private static final String KEY_PREFIX_IPSEC_PSK = Credentials.VPN + 'i';
	private static final String KEY_PREFIX_L2TP_SECRET = Credentials.VPN + 'l';
	
	//-------Application List--------------
	private PackageManager mPm;
	
	// observer object used for computing pkg sizes
    private PkgSizeObserver mObserver = new PkgSizeObserver();;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	context = this;
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);        
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        //txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        //txtMessage = (EditText) findViewById(R.id.txtMessage);
        
        /*
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "Content of the SMS goes here..."); 
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
        */
        
        Bundle bundle = getIntent().getExtras();
        String demoItem;
        
		if (bundle != null) {
			demoItem = bundle.getString("demo item");
			
			if(demoItem.equalsIgnoreCase("APN Demo")) {
				//--------App List demo-------
		        Log.i("App List demo", "App List demo enter");
		        onDemoApnList();
			}else if(demoItem.equalsIgnoreCase("VPN Demo")) {
				//--------APN setting demo-------
		        Log.i("APN setting demo", "APN setting demo enter");
		        onDemoVpnList();
			}else if(demoItem.equalsIgnoreCase("AppList Demo")) {
				//-------VPN Setting demo-----------
		        Log.i("VPN setting demo", "VPN: new log 3 !!! ");
		        onDemoAppList();
			}
				
		}
                
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {            	
            	startActivity(new Intent(context, Splash.class));
				finish();
                
            }
        });  
        
        btnSendSMS.setText( bundle.getString("demo item")
				+ " OK --- Go Back ");
       
        
       
        
        
       
        
    }
    
    private long getTotalSize(PackageStats ps) {
        if (ps != null) {
            return ps.cacheSize+ps.codeSize+ps.dataSize;
        }
        return 0 ;
    }
    
    private CharSequence getSizeStr(long size) {
        CharSequence appSize = null;
        if (size == -1) {
             return "zero size";
        }
        appSize = Formatter.formatFileSize(SMS.this, size);
        return appSize;
    }
    
    class PkgSizeObserver extends IPackageStatsObserver.Stub {
        String pkgName;
        PackageStats stats;
        boolean succeeded;
        
        public void onGetStatsCompleted(PackageStats pStats, boolean pSucceeded) {
            
                
                
                succeeded = pSucceeded;
                stats = pStats;
                
           
            if(pSucceeded && pStats != null) {
                Log.i("App List demo", "onGetStatsCompleted::"+pkgName+", ("+
                        pStats.cacheSize+","+
                        pStats.codeSize+", "+pStats.dataSize);
                
                long total = getTotalSize(pStats);
                CharSequence sizeStr = getSizeStr(total);
                Log.i("App List demo", "AppList: total = " + sizeStr.toString());
                
            } else {
                Log.w("App List demo", "Invalid package stats from PackageManager");
            }
            
        }

        public void invokeGetSizeInfo(String packageName) {
            if (packageName == null) {
                return;
            }
            pkgName = packageName;
            Log.i("App List demo", "Invoking getPackageSizeInfo for package:"+
                    packageName);
            mPm.getPackageSizeInfo(packageName, this);
        }
    }
    
    
    private void processSecrets(VpnProfile p) {
        switch (p.getType()) {
            case L2TP_IPSEC_PSK:
                L2tpIpsecPskProfile pskProfile = (L2tpIpsecPskProfile) p;
                String presharedKey = pskProfile.getPresharedKey();
                String key = KEY_PREFIX_IPSEC_PSK + p.getId();
                if (!TextUtils.isEmpty(presharedKey) &&
                        !mKeyStore.put(key, presharedKey)) {
                    Log.i("VPN setting demo", "VPN: keystore write failed: key=" + key);
                }
                pskProfile.setPresharedKey(key);
                
                // pass through

            case L2TP_IPSEC:
            case L2TP:
                L2tpProfile l2tpProfile = (L2tpProfile) p;
                key = KEY_PREFIX_L2TP_SECRET + p.getId();
                if (l2tpProfile.isSecretEnabled()) {
                    String secret = l2tpProfile.getSecretString();
                    
                    Log.i("VPN setting demo", "VPN: secret =" + secret);
                    Log.i("VPN setting demo", "VPN: TextUtils.isEmpty =" + TextUtils.isEmpty(secret));
                    
                    if (!TextUtils.isEmpty(secret) &&
                            !mKeyStore.put(key, secret)) {
                        Log.i("VPN setting demo", "VPN: keystore write failed: key=" + key);
                    }
                    l2tpProfile.setSecretString(key);
                    Log.i("VPN setting demo", "VPN: key=" + key);
                } else {
                    mKeyStore.delete(key);
                }
                break;
        }
    }
    
    private VpnProfile deserialize(File profileObjectFile) throws IOException {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    profileObjectFile));
            VpnProfile p = (VpnProfile) ois.readObject();
       
            ois.close();
            return p;
        } catch (ClassNotFoundException e) {
            Log.i("VPN setting demo", "deserialize a profile", e);
            return null;
        }
    }
    
    // A sanity check. Returns true if the profile directory name and profile ID
    // are consistent.
    private boolean checkIdConsistency(String dirName, VpnProfile p) {
        if (!dirName.equals(p.getId())) {
            Log.i("VPN setting demo", "ID inconsistent: " + dirName + " vs " + p.getId());
            return false;
        } else {
            return true;
        }
    }
    
    static String getProfileDir(VpnProfile p) {
        return PROFILES_ROOT + p.getId();
    }
    
    static void saveProfileToStorage(VpnProfile p) throws IOException {
        File f = new File(getProfileDir(p));
        if (!f.exists()) f.mkdirs();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                new File(f, PROFILE_OBJ_FILE)));
        oos.writeObject(p);
        oos.close();
    }
    
    // Randomly generates an ID for the profile.
    // The ID is unique and only set once when the profile is created.
    private void setProfileId(VpnProfile profile) {
        String id;

        while (true) {
            id = String.valueOf(Math.abs(
                    Double.doubleToLongBits(Math.random())));
            if (id.length() >= 8) break;
        }
        
        /*
        for (VpnProfile p : mVpnProfileList) {
            if (p.getId().equals(id)) {
                setProfileId(profile);
                return;
            }
        }
        */
        Log.i("VPN setting demo", "VPN: dir name = " + id);
        profile.setId(id);
    }
    
    
    
    //---sends a SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {      
    	/*
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, test.class), 0);                
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, pi, null);        
        */
    	
    	String SENT = "SMS_SENT";
    	String DELIVERED = "SMS_DELIVERED";
    	
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
    	
        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "SMS sent ooo", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					    Toast.makeText(getBaseContext(), "Generic failure", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NO_SERVICE:
					    Toast.makeText(getBaseContext(), "No service", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NULL_PDU:
					    Toast.makeText(getBaseContext(), "Null PDU", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_RADIO_OFF:
					    Toast.makeText(getBaseContext(), "Radio off", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				}
			}
        }, new IntentFilter(SENT));
        
        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "SMS delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case Activity.RESULT_CANCELED:
					    Toast.makeText(getBaseContext(), "SMS not delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;					    
				}
			}
        }, new IntentFilter(DELIVERED));        
    	
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI); 
        
       
 
    }    
    
    private void onDemoVpnList()
    {
    	
        mProfile = mVpnManager.createVpnProfile(VpnType.L2TP_IPSEC);
        Log.i("VPN setting demo", "VPN: create VPNprofile type " + (mProfile.getType()).toString());    
       
        mEditorProfile = mProfile;
        mEditorProfile.setName("home");
        mEditorProfile.setServerName("mog.com.cn");
        mEditorProfile.setDomainSuffices("192.168.1.1");
        
        //((PptpProfile)mEditorProfile).setEncryptionEnabled(false);
        //((L2tpProfile)mEditorProfile).setSecretEnabled(true);
        //((L2tpProfile)mEditorProfile).setSecretString("abc");
        //((L2tpIpsecPskProfile)mEditorProfile).setPresharedKey("123");
        ((L2tpIpsecProfile)mEditorProfile).setUserCertificate("user_cert");
        ((L2tpIpsecProfile)mEditorProfile).setCaCertificate("ca_cert");
       
       
        //Intent intent_1 = new Intent(this, SmsReceiver.class);
        //intent_1.putExtra("vpn_profile_1", (Parcelable) mEditorProfile);
        //mMemProfile = (VpnProfile)intent_1.getParcelableExtra("vpn_profile_1");
        
        
        mMemProfile = mEditorProfile;
        setProfileId(mMemProfile);
        Log.i("VPN setting demo", "VPN: VPNprofile ID = " + mMemProfile.getId());    
        
        processSecrets(mMemProfile);
        
        try {
        saveProfileToStorage(mMemProfile);
        }catch (IOException e) {
            Util.showErrorMessage(this, e + ": " + e.getMessage());
        }
 
        //read  VPN¡¡List
        File root = new File(PROFILES_ROOT);
        
        String[] dirs = root.list();
        if (dirs == null) {
        	Log.i("VPN setting demo", "VPN: !root.list ");
        	return;
        }
        for (String dir : dirs) {
            File f = new File(new File(root, dir), PROFILE_OBJ_FILE);
            if (!f.exists()) {
            	Log.i("VPN setting demo", "VPN: !f.exists ");
            	continue;
            }
     
            //try {
            //	f.delete();
            //	continue;
            //    }catch (Exception e) {
            //        Util.showErrorMessage(this, e + ": " + e.getMessage());
            //    }
    
            try {
            	VpnProfile p = (VpnProfile)deserialize(f);
                if (p == null) { 
                	Log.i("VPN setting demo", "VPN: This dir is null. " + dir);    
                	continue;
                }
           
                Log.i("VPN setting demo", "VPN: Id = " + p.getId());
                Log.i("VPN setting demo", "VPN: Name = " + p.getName() );
                Log.i("VPN setting demo", "VPN: DomainSuffices = " + p.getDomainSuffices() );
                Log.i("VPN setting demo", "VPN: RouteList = " + p.getRouteList() );
                Log.i("VPN setting demo", "VPN: ServerName = " + p.getServerName() );
                Log.i("VPN setting demo", "VPN: SavedUsername = " + p.getSavedUsername() );
                
                if( (p.getType() == VpnType.L2TP) || (p.getType() == VpnType.L2TP_IPSEC) ) {
                	
                	Log.i("VPN setting demo", "VPN: SecretString = " + ((L2tpProfile)p).getSecretString() );
                }
                else if(p.getType() == VpnType.L2TP_IPSEC_PSK) {
                	Log.i("VPN setting demo", "VPN: preshared key = " + ((L2tpIpsecPskProfile)p).getPresharedKey() );
                }
                
                if(p.getType() == VpnType.L2TP_IPSEC) {
                	Log.i("VPN setting demo", "VPN: user cert = " + ((L2tpIpsecProfile)p).getUserCertificate());
                	Log.i("VPN setting demo", "VPN: ca cert = " + ((L2tpIpsecProfile)p).getCaCertificate());
                }
                
                Parcel parcel = Parcel.obtain();
                p.writeToParcel(parcel, 0);
                byte[] mOriginalProfileData = parcel.marshall();
                Log.i("VPN setting demo", "VPN: parcel.marshall = " + mOriginalProfileData.toString() );
                Log.i("VPN setting demo", "VPN: parcel.dataSize = " + parcel.dataSize() );
              
            } catch (IOException e) {
                Log.i("VPN setting demo", "retrieveVpnListFromStorage()", e);
            }
        }
        
    }
    
    
    private void onDemoApnList() 
    {       
    	
        ContentValues values = new ContentValues();
        values.put("name", "home");
        values.put("apn", "Accenture.com");
        values.put("numeric", "46000");
        values.put("mcc", "460");
        values.put("mnc", "00");
        values.put("type", "default");
        values.put("current", 1);
        Uri newRow = getBaseContext().getContentResolver().insert(
        		Uri.parse("content://telephony/carriers"), values);
        Log.i("APN setting demo", "add a APN");
        
        int id = 0;
        if(newRow != null)
        {
        	Cursor c = null;

            c = getBaseContext().getContentResolver().query(newRow, null, null, null, null);
            
            //printAllData(c); //Print the entire result set
            
            // Obtain the apn id
            int idindex = c.getColumnIndex("_id");
            c.moveToFirst();
            id = c.getShort(idindex);
            Log.d("APN setting demo", "New ID: " + id + ": Inserting new APN succeeded!");
        }

        ContentValues values_1 = new ContentValues();
        values_1.put("apn_id", id); 
        getBaseContext().getContentResolver().update(
        		Uri.parse("content://telephony/carriers/preferapn"), values_1, null, null);
        
        
        Log.i("APN setting demo", "set mog as default APN ");
        
    }
    
    private void onDemoAppList() 
    {            	
    	    	
    	String pkgName = "123";
    	
    	Log.i("DCD", "net_connhost 0");
    	
    	mPm = getPackageManager();
    	Log.i("App List demo", "AppList: getPackageManager OK ");
    	
    	List<ApplicationInfo> installedAppList = mPm.getInstalledApplications(
                PackageManager.GET_UNINSTALLED_PACKAGES);
    	
    	int N = installedAppList.size();
    	Log.i("App List demo", "AppList: getInstalledApplications OK. size = " + N);
    	
    	//get the package name and app name
        for (int i = (N-1); i >= 0; i--) {
            ApplicationInfo info = installedAppList.get(i);
            pkgName = info.packageName;
            Log.i("App List demo", "AppList: packageName = " + i + pkgName); 
            
            CharSequence label = info.loadLabel(mPm);
            Log.i("App List demo", "AppList: label = " + i + label);
            
            PackageInfo pkgInfo;
            try {
            	pkgInfo = mPm.getPackageInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
            	Log.i("App List demo", "AppList: ver name = " + pkgInfo.versionName);
            }
            catch(Exception e) {
            	Log.i("App List demo", "AppList: getPackageInfo: " + e);
            }
        
            
  

        
        //get the size
        ApplicationInfo appInfo = installedAppList.get(i);
        pkgName = appInfo.packageName;
        
        //mObserver = new PkgSizeObserver();
        
        mObserver.invokeGetSizeInfo(pkgName);
        
        try {
            Thread.sleep(1*100);
        } catch (InterruptedException e) {
        }
        
        // Process the package statistics
        /*
        PackageStats pStats = mObserver.stats;
        boolean succeeded = mObserver.succeeded;
        
        if(succeeded && pStats != null)  {
        	Log.i("App List demo", "AppList: Got size immediately ::"+pkgName+", ("+
                    pStats.cacheSize+","+
                    pStats.codeSize+", "+pStats.dataSize);
        }else {
        	Log.i("App List demo", "AppList: Got none size " );
        }  
        */
        }
        
    	
    	/*
        if (phoneNo.length()>0 && message.length()>0)                
            sendSMS(phoneNo, message);  
            
        else
        	Toast.makeText(getBaseContext(), 
                "Please enter both phone number and message.", 
                Toast.LENGTH_SHORT).show();
        */
        
    }
}