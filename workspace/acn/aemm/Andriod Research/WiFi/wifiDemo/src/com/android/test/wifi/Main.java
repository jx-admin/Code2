package com.android.test.wifi;

import android.app.Activity;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author abc
 *
 */
public class Main extends Activity implements OnClickListener {
	private Button del_btn,mod_btn,add_btn;
	private TextView meg_tv;
	private EditText old_ssid_et;
	private EditText ssid_et;
	private EditText word_et;
	private Button offon_btn;
	private WifiManageWrap mWifiManageWrap;
	private String info;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }
    private void init(){
    	 meg_tv=(TextView) findViewById(R.id.meg_tv);
    	 old_ssid_et=(EditText) findViewById(R.id.old_ssid_et);
         ssid_et=(EditText) findViewById(R.id.ssid_et);
         word_et=(EditText) findViewById(R.id.word_et);
         offon_btn=(Button) findViewById(R.id.offon_btn);
         offon_btn.setOnClickListener(this);
         del_btn=((Button)findViewById(R.id.del_btn));
         del_btn.setOnClickListener(this);
         mod_btn=((Button)findViewById(R.id.mod_btn));
         mod_btn.setOnClickListener(this);
         add_btn=((Button)findViewById(R.id.add_btn));
         add_btn.setOnClickListener(this);
         info="";
         mWifiManageWrap=new WifiManageWrap(this);
         print(R.string.load_success);
    }
    private void fresh(boolean state){
    	if(state){
    		del_btn.setEnabled(true);
    		mod_btn.setEnabled(true);
    		add_btn.setEnabled(true);
    	}else{
    		del_btn.setEnabled(false);
    		mod_btn.setEnabled(false);
    		add_btn.setEnabled(false);
    	}
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        setButtonState(mWifiManageWrap.mWifiManager.isWifiEnabled());
    }
    private void setButtonState(boolean state){
    	if(state){
    		offon_btn.setText(R.string.close);
    		print(R.string.open_success);
    	}else{
    		offon_btn.setText(R.string.open);
    		print(R.string.close_success);
    	}
    	fresh(state);
    }
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.offon_btn:
			if(mWifiManageWrap.mWifiManager.isWifiEnabled()){
				mWifiManageWrap.mWifiManager.setWifiEnabled(false);
				setButtonState(false);
			}else{
				mWifiManageWrap.mWifiManager.setWifiEnabled(true);
				setButtonState(true);
			}
			break;
		case R.id.del_btn:
			WifiConfiguration conf=mWifiManageWrap.getWifiConfig(ssid_et.getText().toString().trim());
			if(conf!=null){
				print("ÍøÂçÒÆ³ý:"+mWifiManageWrap.mWifiManager.removeNetwork(conf.networkId));
				mWifiManageWrap.mWifiManager.saveConfiguration();
			}else{
				print(R.string.not_find);
			}
			break;
		case R.id.mod_btn:
			conf=mWifiManageWrap.getWifiConfig(old_ssid_et.getText().toString().trim());
			if(conf!=null){
				WifiConfiguration newCong=new WifiConfiguration();
				newCong.SSID = "\""+ssid_et.getText().toString().trim()+"\"";
				newCong.preSharedKey = "\""+word_et.getText()+"\"";
				newCong.networkId=conf.networkId;
				print("ÍøÂçÅäÖÃÐÞ¸Ä:"+mWifiManageWrap.mWifiManager.updateNetwork(newCong));
				mWifiManageWrap.mWifiManager.saveConfiguration();
				old_ssid_et.setText(ssid_et.getText());
			}else{
				print(R.string.not_find);
			}
			break;
		case R.id.add_btn:
			if(mWifiManageWrap.addNetWordLink(getWifiConfiguration())){
				print(R.string.add_success);
				old_ssid_et.setText(ssid_et.getText());
			}else{
				print(R.string.add_fail);
			}
			break;
		}
	}
	private void print(String mes){
		info+=mes+"\n";
		meg_tv.setText(info);
	}
	private void print(int id){
		print(getString(id));
	}
	 /**WifiConfiguration ¶ÔÏó*/
	public WifiConfiguration getWifiConfiguration(){
		WifiConfiguration conf = new WifiConfiguration();
		conf.SSID = "\""+ssid_et.getText().toString().trim()+"\"";
		conf.preSharedKey = "\""+word_et.getText()+"\"";
		conf.status = WifiConfiguration.Status.ENABLED;
		conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		return conf;
	}
}