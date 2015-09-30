
package com.act.mbanking.activity;

import com.act.mbanking.R;
import com.act.mbanking.manager.ContactsManagerHelp;
import com.act.mbanking.manager.view.NavigationBar;
import com.act.mbanking.manager.view.NavigationBar.OnNavigationClickListener;
import com.google.android.maps.MapActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class ContactActivity extends MapActivity implements OnNavigationClickListener {
    NavigationBar navigationBar;

    TextView leftButton;

    private ContactsManagerHelp cmhelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contacts_add);
        navigationBar = (NavigationBar)findViewById(R.id.navigation_bar);
        navigationBar.init();
        navigationBar.setLeftText(getString(R.string.back));
        navigationBar.setOnNavigationClickListener(this);

        cmhelp = new ContactsManagerHelp(this);
        cmhelp.init((ViewGroup)findViewById(R.id.detailLayout));
    }

    protected void onLeftNavigationClick(View v) {
        finish();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public void onRightClick(View v) {

    }
}
