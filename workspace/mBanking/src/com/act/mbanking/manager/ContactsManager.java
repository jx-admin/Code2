
package com.act.mbanking.manager;

import android.view.View;
import android.view.ViewGroup;

import com.act.mbanking.R;
import com.act.mbanking.activity.MainActivity;

public class ContactsManager extends MainMenuSubScreenManager {

    public ContactsManager(MainActivity activity) {
        super(activity);
    }

    @Override
    protected void init() {
        layout = (ViewGroup)activity.findViewById(R.id.contacts);

        help = new ContactsManagerHelp(activity);
        help.init(layout);

        setLeftNavigationText(activity.getResources().getString(R.string.dashboard));
    }

    @Override
    public boolean onLeftNavigationButtonClick(View v) {
        mainManager.showAggregatedView(true, null);
        return true;
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void setUI() {
    }

    private ContactsManagerHelp help;
}
