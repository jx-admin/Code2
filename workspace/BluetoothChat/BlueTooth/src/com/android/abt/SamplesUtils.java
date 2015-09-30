package com.android.abt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;

/**UI异步线程
 * @author Administrator
 *
 */
abstract class SamplesUtils {
	/**异步线程
	 * @param context
	 * @param handler
	 * @param message
	 * @param runnable 异步线程
	 * @param dismissListener 完成回调
	 */
	public static void indeterminate(Context context, Handler handler,
			String message, final Runnable runnable,
			OnDismissListener dismissListener) {
		try {

			indeterminateInternal(context, handler, message, runnable,
					dismissListener, true);
		} catch (Exception e) {
			; // nop.
		}
	}

	/**异步线程
	 * @param context
	 * @param handler
	 * @param message
	 * @param runnable
	 * @param dismissListener
	 * @param cancelable 是否允许Cancel
	 */
	public static void indeterminate(Context context, Handler handler,
			String message, final Runnable runnable,
			OnDismissListener dismissListener, boolean cancelable) {
		try {
			indeterminateInternal(context, handler, message, runnable,
					dismissListener, cancelable);
		} catch (Exception e) {
			; // nop.
		}
	}

	private static ProgressDialog createProgressDialog(Context context,
			String message) {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(false);
		dialog.setMessage(message);
		return dialog;
	}

	private static void indeterminateInternal(Context context,
			final Handler handler, String message, final Runnable runnable,
			OnDismissListener dismissListener, boolean cancelable) {
		final ProgressDialog dialog = createProgressDialog(context, message);
		dialog.setCancelable(cancelable);
		if (dismissListener != null) {
			dialog.setOnDismissListener(dismissListener);
		}
		dialog.show();
		new Thread() {
			@Override
			public void run() {
				runnable.run();
				handler.post(new Runnable() {
					public void run() {
						try {
							dialog.dismiss();
						} catch (Exception e) {
							; // nop.
						}
					}
				});
			};
		}.start();
	}
}
