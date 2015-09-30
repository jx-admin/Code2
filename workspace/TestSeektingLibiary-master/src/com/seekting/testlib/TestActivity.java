
package com.seekting.testlib;

import android.os.Bundle;

import com.seekting.activity.BaseActivity;

public class TestActivity extends BaseActivity {

    protected String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String toString() {
        return name;
    }
}
