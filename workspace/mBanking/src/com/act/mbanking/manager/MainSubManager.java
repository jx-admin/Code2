
package com.act.mbanking.manager;

import com.act.mbanking.activity.MainActivity;

import android.app.Activity;

/**
 * 减少ui负荷
 * 
 * @author seekting.x.zhang
 */
public class MainSubManager extends MainManager {

    public MainSubManager(MainActivity activity) {
        super(activity);
    }

    @Override
    protected void hideAllSubScreenExcept(MainMenuSubScreenManager mainMenuSubScreenManager) {

        mainLayout.removeAllViews();
        if (mainMenuSubScreenManager != null && mainMenuSubScreenManager.getLayout() != null) {
            mainLayout.addView(mainMenuSubScreenManager.getLayout());

            mainMenuSubScreenManager.show();
        }
        currentSubScreenManager = mainMenuSubScreenManager;
    }
}
