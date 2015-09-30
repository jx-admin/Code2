
package com.accenture.mbank;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.maps.MapActivity;

public class BaseMapActivity extends MapActivity {
    protected View back;

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);

        back = (View)findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    finish();
                }
            });
        }

    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    protected String getResourceString(int id) {
        String str = getResources().getString(id);
        return str;

    }

    @Override
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }

}
