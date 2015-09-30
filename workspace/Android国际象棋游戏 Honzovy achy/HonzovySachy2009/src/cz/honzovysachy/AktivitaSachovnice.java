/*
 This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package cz.honzovysachy;

import cz.honzovysachy.resouces.S;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class AktivitaSachovnice extends Activity implements MenuItem.OnMenuItemClickListener {
	public static final String SETTINGS = "settings";
	public static final String LOCALE = "locale";
	public static final String TIME_PER_MOVE = "time_per_move";
	public static final String LOCALE_FILE = "/sdcard/strings_hs.txt";
	
	BoardControl mView;
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.clear();
	    menu.add(Menu.NONE, 1, Menu.NONE, S.g("FLIP_BOARD")).setOnMenuItemClickListener(this);
	    if (mView.mThinking) {
	    	menu.add(Menu.NONE, 101, Menu.NONE, S.g("MOVE_NOW")).setOnMenuItemClickListener(this);
			return true;
		}
	    
	    if (!mView.mSavedTaskAndroid.mSetup) {
	    	menu.add(Menu.NONE, 2, Menu.NONE, S.g("MOVE")).setOnMenuItemClickListener(this);
	    	menu.add(Menu.NONE, 3, Menu.NONE, S.g("NEW_GAME")).setOnMenuItemClickListener(this);
	    	menu.add(Menu.NONE, 4, Menu.NONE, S.g("UNDO")).setOnMenuItemClickListener(this);
	    	menu.add(Menu.NONE, 5, Menu.NONE, S.g("REDO")).setOnMenuItemClickListener(this);
	    	menu.add(Menu.NONE, 6, Menu.NONE, S.g("SAVE_GAME")).setOnMenuItemClickListener(this);
	    	menu.add(Menu.NONE, 7, Menu.NONE, S.g("HUMAN_OPONENT")).setOnMenuItemClickListener(this);
	    }
	    menu.add(Menu.NONE, 8, Menu.NONE, S.g("SETTINGS")).setOnMenuItemClickListener(this);
	    menu.add(Menu.NONE, 9, Menu.NONE, S.g("ABOUT")).setOnMenuItemClickListener(this);
	    if (!mView.mSavedTaskAndroid.mSetup) {
	    	menu.add(Menu.NONE, 10, Menu.NONE, S.g("SETUP_BOARD")).setOnMenuItemClickListener(this);
	    }
	    return true;
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
		
	}
	
    @Override
    protected void onStop() {
       super.onStop();
       mView.trySave();
    }
	
    private void setTitle() {
    	setTitle(S.g("HONZOVY_SACHY"));
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.board_view);
        SharedPreferences pref = getBaseContext().getSharedPreferences(SETTINGS, 0);
        int i = pref.getInt(LOCALE, 1);
        S.init(i, LOCALE_FILE);
        setTitle();
        mView = (BoardControl)findViewById(R.id.chess_board);
        mView.mActivity = this;
        CheckBox cWhite = (CheckBox)findViewById(R.id.board_setting_white);
        cWhite.setText(S.g("WHITE"));
        TextView sText = (TextView)findViewById(R.id.setup_board_text);
        sText.setText(S.g("SETUP_TEXT"));
        final Button bOk = (Button)findViewById(R.id.board_setting_ok);
        bOk.setText(S.g("OK"));
        bOk.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				mView.setupBoardOK(false);
			}
        	
        });
        final Button bCancel = (Button)findViewById(R.id.board_setting_cancel);
        bCancel.setText(S.g("CANCEL"));
        bCancel.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				mView.setupBoardOK(true);
			}
        	
        });
        if (mView.mSavedTaskAndroid.mSetup) mView.setupBoard(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode < 10) {
    		mView.replayPromotion(resultCode);
    		return;
    	}
    	switch (resultCode) {
    	case 10: mView.save(data); break;
    	case 11: setTitle(); break;
    	}
    	
    }

	public boolean onMenuItemClick(MenuItem item) {
		if (mView.isPremyslim()) {
			switch (item.getItemId()) {
			case 1: mView.otoc();
				break;
			case 101:
				mView.moveNow();
				break;
			default:
				mView.dlg(S.g("THINKING"));
			}
			
			return true;
		}
		switch (item.getItemId()) {
		case 1: mView.otoc();
			break;
		case 2: 
			mView.hrajTed();
			break;
		case 3: 
			mView.newGame();
			break;
		case 4: 
			mView.undo();
			break;
		case 5: 
			mView.redo();
			break;
		case 6: 
			mView.save();
			break;
		case 7: 
			mView.hh();
			break;
		case 8: 
			mView.settings();
			break;
		case 9:
			/*
			Intent myIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://honzovysachy.sf.net"));
			startActivity(myIntent);
			*/
			Intent intent = new Intent();
			intent.setClass(this, AboutActivity.class);
			startActivity(intent);
			break;
		case 10:
			mView.setupBoard(false);
			break;
		}
		return true;
	}
}