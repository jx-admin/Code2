/*

Copyright (C) 2010 Hans-Werner Hilse <hilse@web.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

*/

package de.hilses.droidreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openintents.intents.FileManagerIntents;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class DroidReaderActivity extends Activity {
	private static final int REQUEST_CODE_PICK_FILE = 1;
	private static final int REQUEST_CODE_OPTION_DIALOG = 2;
	
	private static final int DIALOG_GET_PASSWORD = 1;
	private static final int DIALOG_ABOUT = 2;
	private static final int DIALOG_GOTO_PAGE = 3;

	protected DroidReaderView mReaderView = null;
	protected DroidReaderDocument mDocument = null;
	
	private Button mButtonPrev = null;
	private Button mButtonNext = null;
	
	private String mFilename;
	private String mPassword;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// first, show the EULA:
		Eula.show(this);
		
		if(mDocument == null)
			mDocument = new DroidReaderDocument();
		
		// Initialize the PdfRender engine
		PdfRender.setFontProvider(new DroidReaderFontProvider(this));
		
		// then build our layout. it's so simple that we don't use
		// XML for now.
		FrameLayout fl = new FrameLayout(this);

		readPreferences();
		
		mReaderView = new DroidReaderView(this, null, mDocument);
		
		View navigationOverlay = getLayoutInflater().inflate(R.layout.navigationoverlay,
				(ViewGroup) findViewById(R.id.navigationlayout));
		
		mButtonPrev = (Button) navigationOverlay.findViewById(R.id.button_prev);
		mButtonNext = (Button) navigationOverlay.findViewById(R.id.button_next);
		
		mButtonPrev.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DroidReaderActivity.this.openPage(-1, true);
			}
		});
		mButtonNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				DroidReaderActivity.this.openPage(+1, true);
			}
		});

		// add the viewing area and the navigation
		fl.addView(mReaderView);
		fl.addView(navigationOverlay, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				Gravity.BOTTOM));
		setContentView(fl);
		
		mButtonPrev.setClickable(false);
		mButtonNext.setClickable(false);
		mButtonPrev.setVisibility(View.INVISIBLE);
		mButtonNext.setVisibility(View.INVISIBLE);
		
		// check if we were called in order to open a PDF:
		Intent intent = getIntent();
		if(intent.getData() != null) {
			// yep:
			mFilename = intent.getData().toString();
			if(mFilename.startsWith("file://")) { 
				mFilename = mFilename.substring(7);
			} else if(mFilename.startsWith("/")) {
				// raw filename
			} else if(mFilename.startsWith("content://com.metago.astro.filesystem/")) {
				// special case: ASTRO file manager
				mFilename = mFilename.substring(37);
			} else {
				Toast.makeText(this, R.string.error_only_file_uris, 
						Toast.LENGTH_SHORT).show();
				mFilename = null;
			}
			if(mFilename!=null) {
				// try to open with no password
				mPassword = "";
				openDocument();
			}
		} else if(savedInstanceState != null) {
			mFilename = savedInstanceState.getString("filename");
			
			if((new File(mFilename)).exists()) {
				mPassword = savedInstanceState.getString("password");
				mDocument.mZoom = savedInstanceState.getFloat("zoom");
				mDocument.mRotation = savedInstanceState.getInt("rotation");
				openDocument(savedInstanceState.getInt("page"),0,0);
				mDocument.offset(
						savedInstanceState.getInt("offsetX"),
						savedInstanceState.getInt("offsetY"),
						false);
			}
			savedInstanceState.clear();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if((mDocument != null) && mDocument.isPageLoaded()) {
			outState.putFloat("zoom", mDocument.mZoom);
			outState.putInt("rotation", mDocument.mRotation);
			outState.putInt("page", mDocument.mPage.no);
			outState.putInt("offsetX", mDocument.mOffsetX);
			outState.putInt("offsetY", mDocument.mOffsetY);
			outState.putString("password", mPassword);
			outState.putString("filename", mFilename);
		}
	}
	
	@Override
	protected void onDestroy() {
		if(mDocument != null)
			mDocument.closeDocument();
		super.onDestroy();
	}
	
	private void readPreferences() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		if(prefs.getString("zoom_type", "0").equals("0")) {
			int zoom = Integer.parseInt(prefs.getString("zoom_percent", "50"));
			if((1 <= zoom) && (1000 >= zoom)) {
				mDocument.setZoom(((float)zoom) / 100, false);
			}
		} else {
			mDocument.setZoom(Float.parseFloat(prefs.getString("zoom_type", "0")), false);
		}
		
		if(prefs.getBoolean("dpi_auto", true)) {
			// read the display's DPI
			mDocument.setDpi((int) metrics.xdpi, (int) metrics.ydpi);
		} else {
			int dpi = Integer.parseInt(prefs.getString("dpi_manual", "160"));
			if((dpi < 1) || (dpi > 4096))
				dpi = 160; // sanity check fallback
			mDocument.setDpi(dpi, dpi);
		}
		
		if(prefs.getBoolean("tilesize_by_factor", true)) {
			// set the tile size for rendering by factor
			Float factor = Float.parseFloat(prefs.getString("tilesize_factor", "1.5"));
			mDocument.setTileMax((int) (metrics.widthPixels * factor), (int) (metrics.heightPixels * factor));
		} else {
			int tilesize_x = Integer.parseInt(prefs.getString("tilesize_x", "640"));
			int tilesize_y = Integer.parseInt(prefs.getString("tilesize_x", "480"));
			if(metrics.widthPixels < metrics.heightPixels) {
				mDocument.setTileMax(tilesize_x, tilesize_y);
			} else {
				mDocument.setTileMax(tilesize_y, tilesize_x);
			}
		}
	}

	/** Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	/** Handles item selections */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		// Zooming:
		
		case R.id.zoom_in:
			mDocument.setZoom(1.5F, true);
			return true;
		case R.id.zoom_out:
			mDocument.setZoom(0.6666F, true);
			return true;
		case R.id.zoom_fit:
			mDocument.setZoom(DroidReaderDocument.ZOOM_FIT, false);
			return true;
		case R.id.zoom_fitw:
			mDocument.setZoom(DroidReaderDocument.ZOOM_FIT_WIDTH, false);
			return true;
		case R.id.zoom_fith:
			mDocument.setZoom(DroidReaderDocument.ZOOM_FIT_HEIGHT, false);
			return true;
		case R.id.zoom_orig:
			mDocument.setZoom(1.0F, false);
			return true;
			
		// Rotation:
			
		case R.id.rotation_left:
			mDocument.setRotation(270, true);
			return true;
		case R.id.rotation_right:
			mDocument.setRotation(90, true);
			return true;
			
		// Page Navigation:
			
		case R.id.goto_first:
			openPage(1, false);
			return true;
		case R.id.goto_last:
			openPage(DroidReaderDocument.PAGE_LAST, false);
			return true;
		case R.id.goto_ask:
			showDialog(DIALOG_GOTO_PAGE);
			return true;
			
		// File menu
			
		case R.id.open_file:
			// present the file manager's "open..." dialog
			Intent intent = new Intent(FileManagerIntents.ACTION_PICK_FILE);
			intent.setData(Uri.parse("file://"));
			intent.putExtra(FileManagerIntents.EXTRA_TITLE, getString(R.string.open_title));
			try {
				startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, R.string.error_no_filemanager_installed, 
						Toast.LENGTH_SHORT).show();
			}
			return true;
		case R.id.options:
			Intent optionsIntent = new Intent(this,DroidReaderOptions.class);
			startActivityForResult(optionsIntent, REQUEST_CODE_OPTION_DIALOG);
			return true;
		case R.id.about:
			// show the about dialog
			showDialog(DIALOG_ABOUT);
			return true;
		case R.id.quit:
			// quit Activity
			finish();
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_PICK_FILE:
			if (resultCode == RESULT_OK && data != null) {
				mFilename = data.getDataString();
				if (mFilename != null) {
					if (mFilename.startsWith("file://")) {
						mFilename = mFilename.substring(7);
					}
					mPassword = "";
					openDocument();
				}
			}
			break;
		case REQUEST_CODE_OPTION_DIALOG:
			readPreferences();
			break;
		}
	}
	
	protected void openDocument(int pageNo, int offsetX, int offsetY) {
		try {
			mDocument.open(mFilename, mPassword, pageNo, offsetX, offsetY);
			openPage(0, true);
		} catch (PasswordNeededException e) {
			showDialog(DIALOG_GET_PASSWORD);
		} catch (WrongPasswordException e) {
			Toast.makeText(this, R.string.error_wrong_password, 
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(this, R.string.error_opening_document, 
					Toast.LENGTH_LONG).show();
		}
	}
	
	protected void openDocument() {
		openDocument(1, 0, 0);
	}
	
	protected void openPage(int no, boolean isRelative) {
		try {
			if(!(no == 0 && isRelative))
				mDocument.openPage(no, isRelative);
			if(mDocument.havePage(-1, true)) {
				mButtonPrev.setClickable(true);
				mButtonPrev.setVisibility(View.VISIBLE);
			} else {
				mButtonPrev.setClickable(false);
				mButtonPrev.setVisibility(View.INVISIBLE);
			}
			if(mDocument.havePage(1, true)) {
				mButtonNext.setClickable(true);
				mButtonNext.setVisibility(View.VISIBLE);
			} else {
				mButtonNext.setClickable(false);
				mButtonNext.setVisibility(View.INVISIBLE);
			}
			this.setTitle(mFilename+" ("+mDocument.mPage.no+")");
		} catch (PageLoadException e) {
			// TODO Auto-generated catch block
		}
	}
	
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case DIALOG_GET_PASSWORD:
			// displays a password dialog, stores entered password
			// in mPassword, or resets it to an empty string if
			// the input is cancelled.
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.prompt_password);
			View passwordinput = getLayoutInflater().inflate(R.layout.passworddialog,
					(ViewGroup) findViewById(R.id.input_password));
			builder.setView(passwordinput);
			builder.setCancelable(false);
			builder.setPositiveButton(R.string.button_pwddialog_open,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						DroidReaderActivity.this.mPassword = 
							((EditText) ((AlertDialog) dialog).findViewById(R.id.input_password)).getText().toString();
						DroidReaderActivity.this.openDocument();
						dialog.dismiss();
					}
				});
			builder.setNegativeButton(R.string.button_pwddialog_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
			AlertDialog dialog = builder.create();
			return dialog;
		case DIALOG_GOTO_PAGE:
			AlertDialog.Builder gotoBuilder = new AlertDialog.Builder(this);
			gotoBuilder.setMessage(R.string.prompt_goto_page);
			View pageinput = getLayoutInflater().inflate(R.layout.pagedialog,
					(ViewGroup) findViewById(R.id.input_page));
			gotoBuilder.setView(pageinput);
			gotoBuilder.setCancelable(false);
			gotoBuilder.setPositiveButton(R.string.button_page_open,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							DroidReaderActivity.this.openPage(
								Integer.parseInt(
									((EditText)
											((AlertDialog) dialog).findViewById(R.id.input_page))
											.getText()
											.toString()), false);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
						}
						dialog.dismiss();
					}
				});
			gotoBuilder.setNegativeButton(R.string.button_page_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
			AlertDialog gotoDialog = gotoBuilder.create();
			return gotoDialog;
		case DIALOG_ABOUT:
			AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);
			WebView aboutWebView = new WebView(this);
			aboutWebView.loadData(readAbout().toString(), "text/html", "UTF-8");
			aboutBuilder.setView(aboutWebView);
			aboutBuilder.setCancelable(false);
			aboutBuilder.setPositiveButton(R.string.button_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			AlertDialog aboutDialog = aboutBuilder.create();
			return aboutDialog;
		}
		return null;
	}
	
	private CharSequence readAbout() {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(this.getAssets().open("about.html")));
			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// We can't do anything...
				}
			}
			return buffer;
		} catch (IOException e) {
			return "";
		}
	}
}
