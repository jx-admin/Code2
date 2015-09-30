package com.aess.aemm.update;

import com.aess.aemm.view.ViewUtils;
import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

public class UpdateAdapter extends AbstractThreadedSyncAdapter {
	public final static String TAG = "UpdateAdapter";

	public UpdateAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		this.context = context;
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		Log.d(TAG, "onPerformSync");
		if (null != context) {
			Update update = new Update(context);
			
			String type = extras.getString(Update.UpdateType);
			
			if (null != extras) {
				if (null != type && type.equals(String.valueOf(Update.MANUAL))) {
					update.setManual(Update.MANUAL);
				}
			}

			update.doUpate();
		}
		ViewUtils.finishUpdate(context);
	}

	private Context context;
}
