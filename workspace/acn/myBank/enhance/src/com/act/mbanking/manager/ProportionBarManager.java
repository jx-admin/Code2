
package com.act.mbanking.manager;

import com.act.mbanking.R;
import com.act.mbanking.view.ProportionBar;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProportionBarManager extends ViewManager {

    TextView loansTitleTextView;

    TextView loansValueTextView;

    ProportionBar proportionBar;

    public ProportionBarManager(Activity activity, ViewGroup root) {
        super(activity, root);
        init();
    }

    public ProportionBarManager(Activity activity, ViewGroup root, View v) {

        super(activity, root, v);
    }

    @Override
    protected void init() {
        this.view = root.findViewById(R.id.proportion_bar_layout);
        initSubView();
    }

    @Override
    protected void initSubView() {
        loansTitleTextView = (TextView)view.findViewById(R.id.loans_title);
        loansValueTextView = (TextView)view.findViewById(R.id.loans_value);
        proportionBar = (ProportionBar)view.findViewById(R.id.pb);
    }

}
