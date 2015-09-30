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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * An instance of this class will provide font file names, reading from Preferences
 */
public class DroidReaderFontProvider implements FontProvider {
	/**
	 * Debug helper
	 */
	private static final String TAG = "DroidReaderFontProvider";
	protected final static boolean LOG = false;
	
	/**
	 * Our Activity
	 */
	protected Activity mActivity;
	
	/**
	 * Font buffers
	 */
	protected static HashMap<String,ByteBuffer> mFontCache = new HashMap<String,ByteBuffer>();
	
	/**
	 * Instantiates a new FontProvider
	 * @param activity our Activity to read Preferences from
	 */
	DroidReaderFontProvider(Activity activity) {
		mActivity = activity;
	}

	/**
	 * callback that is used to retrieve font file names
	 * @param fontName the name of the font to load
	 * @param collection the collection of fonts (CID)
	 * @param flags font flags as understood by the MuPDF library
	 */
	@Override
	public String getFontFile(String fontName, String collection, int flags) {
		if(LOG) Log.d(TAG, "(File) Font: " + fontName + " Collection: " + collection + " Flags: " + flags);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		
		String font = null;
		if(fontName.equals("CID-Substitute")) {
			// it's a CID font, get font file name from Preferences:
			String setting = "cid_font_" + collection + (((flags & 0x0001)==1) ? "_mincho" : "_gothic");
			if(LOG) Log.d(TAG, "CID Font, reading setting "+setting);
			font = prefs.getString(setting,
					mActivity.getResources().getString(R.string.prefs_cid_default_font));
			if(LOG) Log.d(TAG, "got font "+font);
		}
		
		return font;
	}

	@Override
	public ByteBuffer getFontBuffer(String fontName, String collection,
			int flags) {
		ByteBuffer newBuffer;
		long bufferLength;
		String fontFile;
		InputStream inputStream;
		
		if(LOG) Log.d(TAG, "(Buffer) Font: " + fontName + " Collection: " + collection + " Flags: " + flags);
		
		if(mFontCache.containsKey(fontName)) {
			if(LOG) Log.d(TAG, "found in our cache.");
			return mFontCache.get(fontName);
		}
		
		if(fontName.equals("Courier")) {
			fontFile = "NimbusMonL-Regu.cff";
		} else if(fontName.equals("Courier-Bold")) {
			fontFile = "NimbusMonL-Bold.cff";
		} else if(fontName.equals("Courier-Oblique")) {
			fontFile = "NimbusMonL-ReguObli.cff";
		} else if(fontName.equals("Courier-BoldOblique")) {
			fontFile = "NimbusMonL-BoldObli.cff";
		} else if(fontName.equals("Helvetica")) {
			fontFile = "NimbusSanL-Regu.cff";
		} else if(fontName.equals("Helvetica-Bold")) {
			fontFile = "NimbusSanL-Bold.cff";
		} else if(fontName.equals("Helvetica-Oblique")) {
			fontFile = "NimbusSanL-ReguItal.cff";
		} else if(fontName.equals("Helvetica-BoldOblique")) {
			fontFile = "NimbusSanL-BoldItal.cff";
		} else if(fontName.equals("Times-Roman")) {
			fontFile = "NimbusRomNo9L-Regu.cff";
		} else if(fontName.equals("Times-Bold")) {
			fontFile = "NimbusRomNo9L-Medi.cff";
		} else if(fontName.equals("Times-Italic")) {
			fontFile = "NimbusRomNo9L-ReguItal.cff";
		} else if(fontName.equals("Times-BoldItalic")) {
			fontFile = "NimbusRomNo9L-MediItal.cff";
		} else if(fontName.equals("Symbol")) {
			fontFile = "StandardSymL.cff";
		} else if(fontName.equals("ZapfDingbats")) {
			fontFile = "Dingbats.cff";
		} else if(fontName.equals("Chancery")) {
			fontFile = "URWChanceryL-MediItal.cff";
		} else {
			if(LOG) Log.d(TAG, "no such font available as a buffer.");
			return null;
		}
		
		try {
			if(LOG) Log.d(TAG, "opening asset: font/"+fontFile);
			inputStream = mActivity.getAssets().open("font/"+fontFile);
		
			bufferLength = inputStream.available();
			if(LOG) Log.d(TAG, "reading asset to direct bytebuffer of length "+bufferLength);
			
			newBuffer = ByteBuffer.allocateDirect((int) bufferLength);
			int len = 0;
			byte[] buffer = new byte[4096];
			do {
				len = inputStream.read(buffer);
				if((len != -1) && (newBuffer.position()+len <= bufferLength))
					newBuffer.put(buffer, 0, len);
			} while(len != -1);
			inputStream.close();
			
			mFontCache.put(fontName, newBuffer);
			return newBuffer;
		} catch(IOException e) {
			if(LOG) Log.e(TAG, "error while loading font asset: "+e.getMessage());
		} catch(Exception e) {
			if(LOG) Log.e(TAG, "caught other exception: "+e.getMessage());
		}
		return null;
	}

	@Override
	public ByteBuffer getCMapBuffer(String cmapName) {
		ByteBuffer newBuffer;
		long bufferLength;
		InputStream inputStream;
		try {
			if(LOG) Log.d(TAG, "opening asset: cmap/"+cmapName);
			inputStream = mActivity.getAssets().open("cmap/"+cmapName);
		
			bufferLength = inputStream.available();
			if(LOG) Log.d(TAG, "reading asset to direct bytebuffer of length "+bufferLength);
			
			newBuffer = ByteBuffer.allocateDirect((int) bufferLength);
			int len = 0;
			byte[] buffer = new byte[4096];
			do {
				len = inputStream.read(buffer);
				if((len != -1) && (newBuffer.position()+len <= bufferLength))
					newBuffer.put(buffer, 0, len);
			} while(len != -1);
			inputStream.close();
			return newBuffer;
		} catch(IOException e) {
			if(LOG) Log.e(TAG, "error while loading cmap asset: "+e.getMessage());
		} catch(Exception e) {
			if(LOG) Log.e(TAG, "caught other exception: "+e.getMessage());
		}
		return null;
	}
}
