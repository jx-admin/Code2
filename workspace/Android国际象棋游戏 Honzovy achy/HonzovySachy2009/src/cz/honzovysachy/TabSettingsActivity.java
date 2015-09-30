package cz.honzovysachy;

import cz.honzovysachy.resouces.S;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;

public class TabSettingsActivity extends TabActivity implements View.OnClickListener {
	RadioGroup mGroup;
	
	private static int getRealId(int id)
	{
		switch (id) {
		case R.id.cs: return 3;
		case R.id.en: return 4;
		case R.id.es: return 5;
		case R.id.ca: return 6;
		case R.id.localedefault: return 1;
		case R.id.sdcard: return 2;
		default: return 1;
		}
	}
	
	private static int getId(int realId)
	{
		switch (realId) {
		case 3: return R.id.cs;
		case 4: return R.id.en;
		case 5: return R.id.es;
		case 6: return R.id.ca;
		case 2: return R.id.sdcard;
		default: return R.id.localedefault;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(S.g("SETTINGS"));
        TabHost tabHost = getTabHost();
        LayoutInflater.from(this).inflate(R.layout.settings, tabHost.getTabContentView(), true);
        tabHost.addTab(tabHost.newTabSpec("level").setIndicator(S.g("LEVEL")).setContent(R.id.level));
        tabHost.addTab(tabHost.newTabSpec("locale").setIndicator(S.g("LOCALE")).setContent(R.id.locale));
   		setResult(11, new Intent());
   		
   		// TAB locale
		Button ok = (Button)findViewById(R.id.ok);
		mGroup = (RadioGroup)findViewById(R.id.localegroup);
		SharedPreferences pref = getBaseContext().getSharedPreferences(AktivitaSachovnice.SETTINGS, 0);
		RadioButton selected = (RadioButton)findViewById(getId(pref.getInt(AktivitaSachovnice.LOCALE, 1)));
		selected.setChecked(true);
		ok.setText(S.g("OK"));
		RadioButton cs = (RadioButton)findViewById(R.id.cs);
		RadioButton en = (RadioButton)findViewById(R.id.en);
		RadioButton es = (RadioButton)findViewById(R.id.es);
		RadioButton ca = (RadioButton)findViewById(R.id.ca);
		RadioButton def = (RadioButton)findViewById(R.id.localedefault);
		cs.setText(S.g("CZECH"));
		en.setText(S.g("ENGLISH"));
		es.setText(S.g("SPANISH"));
		ca.setText(S.g("CATALAN"));
		def.setText(S.g("DEFAULT"));
		ok.setOnClickListener(this);
		
		// TAB level
		Button oklevel = (Button)findViewById(R.id.oklevel);
		oklevel.setText(S.g("OK"));
		oklevel.setOnClickListener(this);
		TextView levelstatic = (TextView)findViewById(R.id.levelstatic);
		levelstatic.setText(S.g("TIME_PER_MOVE"));
		EditText time = (EditText)findViewById(R.id.time);
		time.setText(new Integer(pref.getInt(AktivitaSachovnice.TIME_PER_MOVE, 5000)).toString());
 	}
	
	public void onClick(View v) {
		int iTime = 1;
		EditText time = (EditText)findViewById(R.id.time);
		try {
			iTime = Integer.valueOf(time.getText().toString());
		} catch (NumberFormatException n) {
			return;
		}
		SharedPreferences pref = getBaseContext().getSharedPreferences(AktivitaSachovnice.SETTINGS, 0);
		SharedPreferences.Editor editor = pref.edit();
		
		int type;
	    editor.putInt(AktivitaSachovnice.LOCALE, type = getRealId(mGroup.getCheckedRadioButtonId()));
	    editor.putInt(AktivitaSachovnice.TIME_PER_MOVE, iTime);
        editor.commit();
        S.init(type, AktivitaSachovnice.LOCALE_FILE);
        finish();				
	}
	
}
