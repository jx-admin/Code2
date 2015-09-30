
package com.accenture.mbank.view;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.accenture.mbank.util.LogManager;

/**
 * @author seekting.x.zhang
 */
public class BankSlidContainer extends LinearLayout implements OnClickListener {

    View topMargin;

    View midMargin;

    View bottomMargin;

    int topMarginHeight = 0;

    int midMarginHeight = 0;

    int bottomMarginHeight = 0;

    int slidCloseImageHeight = 0;

    int slidShowImageHeight = 0;

    ImageView slid1CloseImage, slid1ShowImage, slid2CloseImage, slid2ShowImage;

    ViewGroup slid1ContentView, slid2ContentView;

    boolean isSlid1Open;

    boolean isSlid2Open;

    Handler handler;

    LinearLayout.LayoutParams topMarginLayoutParams, midMarginLayoutParams,
            bottomMarginLayoutParams, slid1CloseImageLayoutParams, slid1ShowImageLayoutParams,
            slid2ShowImageLayoutParams, slid2CloseImageLayoutParams, slid1ContentViewLayoutParams,
            slid2ContentViewLayoutParams;

    public BankSlidContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BankSlidContainer(Context context) {
        super(context);
    }

    public static final int ANIMAT_END = 0;

    public static final int ANIMAT_END_DELAY_TIME = 100;

    public void init() {
        topMargin = findViewById(R.id.slid_top_margin);
        topMarginLayoutParams = (LinearLayout.LayoutParams)topMargin.getLayoutParams();

        midMargin = findViewById(R.id.slid_mid_margin);
        midMarginLayoutParams = (LinearLayout.LayoutParams)midMargin.getLayoutParams();

        bottomMargin = findViewById(R.id.slid_bottom_margin);
        bottomMarginLayoutParams = (LinearLayout.LayoutParams)bottomMargin.getLayoutParams();

        slid1CloseImage = (ImageView)findViewById(R.id.slid_1_cycle_top_img);
        slid1CloseImageLayoutParams = (LinearLayout.LayoutParams)slid1CloseImage.getLayoutParams();
        slid1ShowImage = (ImageView)findViewById(R.id.slid_1_cycle_bottom_img);
        slid1ShowImageLayoutParams = (LinearLayout.LayoutParams)slid1ShowImage.getLayoutParams();

        slid2CloseImage = (ImageView)findViewById(R.id.slid_2_cycle_top_img);
        slid2CloseImageLayoutParams = (LinearLayout.LayoutParams)slid2CloseImage.getLayoutParams();
        slid2ShowImage = (ImageView)findViewById(R.id.slid_2_cycle_bottom_img);
        slid2ShowImageLayoutParams = (LinearLayout.LayoutParams)slid2ShowImage.getLayoutParams();

        slid1ContentView = (ViewGroup)findViewById(R.id.slid_1_content);
        slid1ContentViewLayoutParams = (LinearLayout.LayoutParams)slid1ContentView
                .getLayoutParams();

        slid2ContentView = (ViewGroup)findViewById(R.id.slid_2_content);
        slid2ContentViewLayoutParams = (LinearLayout.LayoutParams)slid2ContentView
                .getLayoutParams();

        slid1CloseImage.setOnClickListener(this);
        slid2CloseImage.setOnClickListener(this);
        slid1ShowImage.setOnClickListener(this);
        slid2ShowImage.setOnClickListener(this);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (topMargin == null) {
            return;
        }
        topMarginHeight = topMargin.getMeasuredHeight();

        midMarginHeight = midMargin.getMeasuredHeight();

        bottomMarginHeight = bottomMargin.getMeasuredHeight();

        slidCloseImageHeight = slid1CloseImage.getMeasuredHeight();

        slidShowImageHeight = slid1ShowImage.getMeasuredHeight();
    }

    private void showSlip1() {
        slid1ContentView.setVisibility(View.VISIBLE);
        slid1ShowImage.setVisibility(View.GONE);
        midMargin.setVisibility(View.GONE);
        bottomMargin.setVisibility(View.GONE);
        slid2CloseImage.setVisibility(View.GONE);
        slid2ShowImage.setVisibility(View.GONE);
        slid2ContentView.setVisibility(View.GONE);
        isSlid1Open = true;

    }

    private void closeSlip1() {

        if (!isSlid1Open) {
            return;
        }

        slid1CloseImage.setVisibility(View.VISIBLE);
        slid1ShowImage.setVisibility(View.VISIBLE);

        slid2CloseImage.setVisibility(View.VISIBLE);
        slid2ShowImage.setVisibility(View.VISIBLE);
        midMargin.setVisibility(View.VISIBLE);
        bottomMargin.setVisibility(View.VISIBLE);
        isSlid2Open = false;
        slid2ContentView.setVisibility(View.GONE);
        slid1ContentView.setVisibility(View.GONE);
        postInvalidate();

    }

    private void closeSlip2() {

        if (!isSlid2Open) {
            return;
        }
        slid1ShowImage.setVisibility(View.INVISIBLE);
        slid2CloseImage.setVisibility(View.INVISIBLE);
        slid2ShowImage.setVisibility(View.INVISIBLE);

        slid2ContentView.setVisibility(View.GONE);

        slid1CloseImage.setVisibility(View.VISIBLE);
        slid1ShowImage.setVisibility(View.VISIBLE);
        slid1ContentView.setVisibility(View.GONE);
        slid2CloseImage.setVisibility(View.VISIBLE);
        slid2ShowImage.setVisibility(View.VISIBLE);
        midMargin.setVisibility(View.VISIBLE);
        bottomMargin.setVisibility(View.VISIBLE);
        isSlid2Open = false;

    }

    public void snapFrom2to1() {

        if (true) {
            showSlip1();
            slid1CloseImage.setVisibility(View.VISIBLE);
            return;
        }
        slid1ShowImage.setVisibility(View.INVISIBLE);
        slid2CloseImage.setVisibility(View.INVISIBLE);
        slid2ShowImage.setVisibility(View.INVISIBLE);

        slid2ContentView.setVisibility(View.GONE);

    }

    private void showSlip2() {

        slid2ContentView.setVisibility(View.VISIBLE);
        slid2CloseImage.setVisibility(View.INVISIBLE);
        slid1CloseImage.setVisibility(View.GONE);
        slid1ShowImage.setVisibility(View.GONE);
        midMargin.setVisibility(View.GONE);
        slid2ShowImage.setVisibility(View.GONE);
        bottomMargin.setVisibility(View.GONE);
        slid1ContentView.setVisibility(View.GONE);

        slid2CloseImage.setVisibility(View.VISIBLE);
        isSlid2Open = true;

    }

    public void setSlid1CloseImage(int resourse) {
        slid1CloseImage.setImageResource(resourse);
    }

    public void setSlid2CloseImage(int resourse) {
        slid2CloseImage.setImageResource(resourse);
    }

    public View slid1content;

    public void setSlid1ContentLayout(View child) {

        slid1ContentView.removeAllViews();
        slid1ContentView.addView(child);
        slid1content = child;

    }

    public void setSlid2ContentLayout(SlidControler child) {
        slid2ContentView.removeAllViews();

        child.setSlidControler(this);
        View v = (View)child;

        slid2ContentView.addView(v);
    }

    @Override
    public void onClick(View v) {

        if (v == slid1CloseImage) {
            closeSlip1();
        } else if (v == slid1ShowImage) {
            showSlip1();

        } else if (v == slid2ShowImage) {
            showSlip2();

        } else if (v == slid2CloseImage) {
            closeSlip2();
        }
    }

    static interface SlidControler {

        void setSlidControler(BankSlidContainer bankSlidContainer);
    }
}
