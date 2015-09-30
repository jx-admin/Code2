
package com.act.mbanking.manager;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.act.mbanking.App;

public class Manager {

    public Drawable getDrawable(int res) {
        return App.app.getResources().getDrawable(res);
    }
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
    public boolean onDestroyed(){
    	return false;
    }

}
