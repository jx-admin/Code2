package org.accenture.product.lemonade.settings;

import java.util.List;

import org.accenture.product.lemonade.actions.LauncherActions;
import org.accenture.product.lemonade.actions.LauncherActions.Action;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.ListAdapter;

public class LauncherActionPreference extends DialogPreference {

	public LauncherActionPreference(Context context ,AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		List<Action> actions = LauncherActions.getInstance().getList();
		CharSequence[] titles = new CharSequence[actions.size()];
		CharSequence[] values = new CharSequence[actions.size()];
		for(int i = 0; i < actions.size(); i++) {
			titles[i] = actions.get(i).getName();
			values[i] = String.valueOf(i);
		}

	//	this.setEntries(titles);
	//	this.setEntryValues(values);
	}

	@Override
	protected void onClick() {
		AlertDialog.Builder builder = new Builder(getContext());
		builder.setTitle(this.getTitle());
		final ListAdapter adapter = LauncherActions.getInstance().getSelectActionAdapter();

		builder.setAdapter(adapter, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Action act = (Action)adapter.getItem(which);
				Intent intent = LauncherActions.getInstance().getIntentForAction(act);
				String sIntent = intent.toUri(0).toString();
				persistString(sIntent);
			}
		});
		builder.create().show();
	}
}
