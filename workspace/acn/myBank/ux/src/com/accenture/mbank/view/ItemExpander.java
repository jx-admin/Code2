
package com.accenture.mbank.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.accenture.mbank.R;
import com.accenture.mbank.view.ExpandedContainer.ExpandBarResultChangeListener;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class ItemExpander extends LinearLayout implements ExpandBarResultChangeListener,
        OnCheckedChangeListener {

    RelativeLayout expandBar;

    public ToggleButton toggleButton;

    private LinearLayout expandContainerLayout;

    /**
     * 展开后显示的那个 View
     */
    public ExpandedContainer expandedContainer;

    TextView titleText, resultText;

    public ViewGroup owner;

    public int level = 0;

    public static final int level_1_width = 20;
    
    private GoogleAnalytics mGaInstance;
    
	private Tracker mGaTracker1;

    public ItemExpander(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        toggleButton = (ToggleButton)findViewById(R.id.expand_btn);
        toggleButton.setOnCheckedChangeListener(this);
       
        expandBar = (RelativeLayout)findViewById(R.id.expander_bar);
        expandBar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {           	 
                if (expandable) {
                	
                    toggleButton.performClick();
                }
            }
        });

        // toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
        // {
        //
        // @Override
        // public void onCheckedChanged(CompoundButton buttonView, boolean
        // isChecked) {
        // // TODO Auto-generated method stub
        // if (isChecked) {
        // expand();
        // } else {
        //
        // close();
        // }
        // resultText.requestFocus();
        // closeKeyboard();
        //
        // }
        // });

        expandContainerLayout = (LinearLayout)findViewById(R.id.expanded_container);
        titleText = (TextView)findViewById(R.id.expand_title);
        resultText = (TextView)findViewById(R.id.expand_result);
        resultText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

//                closeKeyboard();
            }
        });
        performLevel();
    }

    public boolean getExpanded() {
        return toggleButton.isChecked();
    }

    public void performExpand() {
        if (!getExpanded()) {
            toggleButton.performClick();
        }

    }

    public void setLevel(int level) {
        this.level = level;

        performLevel();
    }

    private void performLevel() {
        if (toggleButton == null) {
            init();
        }
        if (toggleButton != null) {
            RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams)toggleButton
                    .getLayoutParams();
            RelativeLayout.LayoutParams titlelayoutParams = (android.widget.RelativeLayout.LayoutParams)titleText
                    .getLayoutParams();

            if (level == 0) {

                layoutParams.rightMargin = 0;
                titlelayoutParams.leftMargin = 0;
            }
            if (level == 1) {
                layoutParams.rightMargin = level_1_width;
                titlelayoutParams.leftMargin = level_1_width;
            }
            if (level == 2) {

                layoutParams.rightMargin = level_1_width * 2;
                titlelayoutParams.leftMargin = level_1_width * 2;
            }
        }
    }

    // @Override
    // public void invalidate() {
    // // TODO Auto-generated method stub
    // performLevel();
    // super.invalidate();
    // }

    public void setExpand(boolean expand) {
        toggleButton.setChecked(expand);
        if (expand) {
            expand();
        } else {

            close();
        }
    }

    private void close() {
        expandContainerLayout.setVisibility(View.GONE);
    }

    public void setExpandButtonBackground(int res) {
        toggleButton.setBackgroundResource(res);
    }

    private void expand() {
        closeOther();

        if (expandable && expandedContainer != null) { 
        	expandedContainer.onExpand();
            expandContainerLayout.setVisibility(View.VISIBLE);
      	  }
    }

    private void closeOther() {
        ViewGroup parent = (ViewGroup)getParent();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            
            if (child instanceof ItemExpander) {
                ItemExpander itemExpander = (ItemExpander)child;
                if (itemExpander != this && itemExpander.toggleButton != null
                        && itemExpander.toggleButton.isChecked()) {
                    itemExpander.toggleButton.performClick();
                }
            }

        }

    }

    boolean expandable = true;

    public void setExpandable(boolean expandable) {

        this.expandable = expandable;
        if (expandable) {
            toggleButton.setOnCheckedChangeListener(this);
            resultText.setTextColor(Color.BLACK);
        } else {
            resultText.setTextColor(Color.GRAY);
            toggleButton.setOnCheckedChangeListener(null);
            close();
        }
    }

    public void setTitle(String str) {
        if (titleText == null) {
            init();
        }
        titleText.setText(str);
    }

    public String getTitle() {
        return titleText.getText().toString();
    }

    public void setTypeface(Typeface tf) {
        if (titleText == null) {
            init();
        }

        titleText.setTypeface(tf);
    }

    public void setResultVisible(boolean isVisible) {

        if (isVisible) {
            resultText.setVisibility(View.VISIBLE);
        } else {
            resultText.setVisibility(View.GONE);
        }
    }

    private void setResult(String text) {

        if (text == null || text.equals("")) {
            text = getResources().getString(R.string.tap_to_set1);
        }
        resultText.setText(text);

    }

    private void setResultAndRequstFocus(String text) {
        setResult(text);
        resultText.requestFocus();

    }

    /**
     * 拿到展开的那个view
     * 
     * @return
     */
    public ExpandedContainer getExpandedContainer() {

        return expandedContainer;
    }

    public void setExpandedContainer(ExpandedContainer v) {

        if (expandContainerLayout == null) {
            init();
        }
        expandContainerLayout.removeAllViews();
        expandContainerLayout.addView(v);

        expandedContainer = v;
        v.setExpandBarResultChangeListener(this);

    }  
    
    public void setPushExpandedContainer(ExpandedContainer v) {

        if (expandContainerLayout == null) {
            init();
        }
       // expandContainerLayout.removeAllViews();
        expandContainerLayout.addView(v);

        expandedContainer = v;
        v.setExpandBarResultChangeListener(this);

    }  

    public Object getValue() {

        if (expandedContainer.init) {
            return null;
        } else {
            return expandedContainer.getValue();
        }

    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(resultText.getWindowToken(), 0);

    }

    @Override
    public void onChange(String str) {
        setResult(str);
    }

    @Override
    public void onFocusChange(String str) {

        setResultAndRequstFocus(str);

    }

    /**
     * 
     */
    public void setEditable(boolean flag) {
        setExpand(flag);
        expandedContainer.setEditable(flag);
    }

    @Override
    public ViewGroup getOwener() {
        return owner;
    }

    public void recover(String text) {
        expandedContainer.onRecover(text);
    }
    
    public void recover(Object object) {
        expandedContainer.onRecover(object);
    }

    public void reset() {
        setExpand(false);
        expandedContainer.resetResult();
    }

    public void onReset() {
        setResult("");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        // TODO Auto-generated method stub
        if (isChecked) {
            expand();
        } else {

            close();
        }
        resultText.requestFocus();
        closeKeyboard();

    }

}
