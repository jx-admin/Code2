
package com.accenture.mbank.net;

import com.accenture.mbank.R;
import com.accenture.mbank.R.layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * @author seekting.x.zhang
 */
public class ProgressOverlay {
    private final Context context;

    private Dialog dialog = null;

    public interface OnProgressEvent {
        void onProgress();
    }

    public ProgressOverlay(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    /**
     * @param title
     * @param event
     */
    public void show(String title, final OnProgressEvent event) {
        if (title == null || title.equals("")) {
            title = context.getResources().getString(R.string.waiting);
        }
        try {
            dialog = new AlertDialog.Builder(context).setIcon(null).setTitle(title)
                    .setView(View.inflate(context, R.layout.progressdialog, null)).show();
            dialog.setCancelable(false);
        } catch (Exception e) {
        }

        BackgrounThread thread = new BackgrounThread(dialog, event);
        thread.start();

    }

    /**
     * @param title
     * @param event
     */
    public void showRunInMainThread(String title, final OnProgressEvent event) {
        if (title == null || title.equals("")) {
            title = context.getResources().getString(R.string.waiting);
        }
        try {
            dialog = new AlertDialog.Builder(context).setIcon(null).setTitle(title)
                    .setView(View.inflate(context, R.layout.progressdialog, null)).show();
            dialog.setCancelable(false);
        } catch (Exception e) {
        } finally {

            dialog.dismiss();
        }

        event.onProgress();
        dialog.dismiss();

    }

    public void runBackground(final OnProgressEvent event) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    event.onProgress();
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {

                }
            }
        }).start();

    }

    public static class BackgrounThread extends Thread {

        private Dialog mdialog;

        OnProgressEvent event;

        public BackgrounThread(Dialog mdialog, OnProgressEvent onProgressEvent) {
            this.mdialog = mdialog;
            this.event = onProgressEvent;
        }

        @Override
        public void run() {
            try {
                event.onProgress();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (mdialog != null)
                    mdialog.dismiss();
            }
        }

    }
}
