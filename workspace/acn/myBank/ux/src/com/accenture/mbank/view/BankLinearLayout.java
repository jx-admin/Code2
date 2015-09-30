
package com.accenture.mbank.view;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accenture.mbank.R;
import com.accenture.mbank.model.AccountsModel;

public class BankLinearLayout extends LinearLayout {

    public InnerScrollView innerScrollView;

    public BankLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void setAccountsUI(LinearLayout root, List<AccountsModel> list) {
        root.removeAllViews();
        if (list != null && list.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (AccountsModel amAccountsModel : list) {
                ItemExpander itemExpander = (ItemExpander)inflater.inflate(R.layout.item_expander,
                        null);
                String title = amAccountsModel.getAccountAlias();
                itemExpander.setTitle(title);
                itemExpander.setTypeface(Typeface.DEFAULT);
                itemExpander.setResultVisible(false);
                root.addView(itemExpander);
                setExpandedContainer(itemExpander, amAccountsModel);

            }
        } else {

            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.box_details);
            textView.setText(getResources().getString(R.string.not_available));
            root.addView(textView);
        }
    }

    protected void setExpandedContainer(ItemExpander itemExpander, AccountsModel accountsModel) {

    }

    /**
     * 点半圆的时候展开半圆的动作
     * 
     * @param show
     * @param scrollToTargetView
     * @param content
     */
    protected void showContent(ImageView show, View scrollToTargetView, View content) {

        if (content != null) {
            content.setVisibility(View.VISIBLE);
        }
        if (show != null) {
            show.setVisibility(View.GONE);
        }
        if (innerScrollView != null) {
            innerScrollView.scrollTo(scrollToTargetView);
        }

    }

    public void parentScrollViewScrollTo(int y) {
        if (innerScrollView != null) {
            innerScrollView.scrollTo(y);
        }
    }

    /**
     * 点半圆的时候收缩半圆的动作
     * 
     * @param show
     * @param close
     * @param content
     */
    protected void closeContent(ImageView show, View close, View content) {
        if (content != null) {
            content.setVisibility(View.GONE);
        }
        if (show != null) {
            show.setVisibility(View.VISIBLE);
        }
    }

}
