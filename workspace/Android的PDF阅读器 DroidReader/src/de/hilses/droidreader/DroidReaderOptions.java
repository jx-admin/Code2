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

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

public class DroidReaderOptions extends PreferenceActivity
implements OnPreferenceChangeListener {
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.droidreader_preferences);
		checkEnabledDisabled();
		findPreference("zoom_type").setOnPreferenceChangeListener(this);
		findPreference("dpi_auto").setOnPreferenceChangeListener(this);
		findPreference("tilesize_by_factor").setOnPreferenceChangeListener(this);
	}
	
	protected void checkEnabledDisabled() {
		findPreference("zoom_percent").setEnabled(
				((ListPreference) findPreference("zoom_type")).getValue()=="0");
		findPreference("dpi_manual").setEnabled(
				!((CheckBoxPreference) findPreference("dpi_auto")).isChecked());
		findPreference("tilesize_factor").setEnabled(
				((CheckBoxPreference) findPreference("tilesize_by_factor")).isChecked());
		findPreference("tilesize_x").setEnabled(
				!((CheckBoxPreference) findPreference("tilesize_by_factor")).isChecked());
		findPreference("tilesize_y").setEnabled(
				!((CheckBoxPreference) findPreference("tilesize_by_factor")).isChecked());
	}

	public boolean onPreferenceChange(Preference pref, Object objValue) {
		if(pref.getKey().equals("zoom_type")) {
			findPreference("zoom_percent").setEnabled(((String) objValue).equals("0"));
			return true;
		} else if (pref.getKey().equals("dpi_auto")) {
			findPreference("dpi_manual").setEnabled(!((Boolean) objValue));
			return true;
		} else if (pref.getKey().equals("tilesize_by_factor")) {
			findPreference("tilesize_x").setEnabled(!((Boolean) objValue));
			findPreference("tilesize_y").setEnabled(!((Boolean) objValue));
			findPreference("tilesize_factor").setEnabled(((Boolean) objValue));
			return true;
		}
		return false;
	}
}
