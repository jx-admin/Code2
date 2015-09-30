package com.baidu.lbsapi.album.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import com.baidu.lbsapi.album.util.ScreenUtils;
import com.baidu.lbsapi.album.util.StreetscapeUtil;

/**
 * Created with IntelliJ IDEA.
 *
 * @author wangziji
 * @date 13-12-6
 * Time: 下午1:14
 * To change this template use File | Settings | File Templates.
 */
public class SinglePhotoLayout extends LinearLayout {

    private SSAsyncImageView mImageView;
    private TextView mTextView;
    private LinearLayout mPhotoFrame;
    private Context mContext;
    private String mName;
    public static int sImageFrameWidth;
    public static int sImageFrameMargin;
    public boolean mIsHighlighted = false;

    public SinglePhotoLayout(Context context) {
        super(context);
        mContext = context;
    }

    public SinglePhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public SSAsyncImageView getImageView() {
        return mImageView;
    }
    public SinglePhotoLayout getLayout() {

    	mPhotoFrame = new LinearLayout(mContext);
    	mPhotoFrame.setBackgroundColor(Color.parseColor("#ccd2dc"));
    	mImageView = new SSAsyncImageView(mContext);
    	mImageView.setImageBitmap(StreetscapeUtil.getImageFromAssetsFile(mContext, "ss_photoalbum_background.png"));
    	
    	LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
    		    ScreenUtils.dip2px(79, mContext), ScreenUtils.dip2px(60, mContext));
    	lp1.leftMargin = ScreenUtils.dip2px(10, mContext);
    	lp1.rightMargin = ScreenUtils.dip2px(10, mContext);
    	lp1.topMargin = ScreenUtils.dip2px(14, mContext);
    	lp1.bottomMargin = ScreenUtils.dip2px(5, mContext);
    	mPhotoFrame.addView(mImageView, lp1);
    	
    	LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
    			ViewGroup.LayoutParams.WRAP_CONTENT);

    	mTextView = new TextView(mContext);
    	mTextView.setTextColor(Color.parseColor("#3e3e3e"));
    	mTextView.setTextSize(14);
    	LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
    		    ViewGroup.LayoutParams.WRAP_CONTENT,
    		    ViewGroup.LayoutParams.WRAP_CONTENT);
    	lp3.gravity = Gravity.CENTER;
//    	lp3.bottomMargin = ScreenUtils.dip2px(5, mContext);
    	setOrientation(LinearLayout.VERTICAL);
    	addView(mPhotoFrame,lp2);
    	addView(mTextView, lp3);
    	
    	return this;
    	
    }

    public void setImageUrl(String url) {
        if (mImageView != null)
            mImageView.setImageUrl(url);
    }

    public void setName(String name) {
        this.mName = "";
        if (name.length() > 5) {
            int charCount = 0;
            for (int i = 0; i < name.length(); i++) {
                if (charCount > 9) {
                    break;
                }
                char temp = name.charAt(i);
                try {
                    if (StreetscapeUtil.isChinese(temp)) {
                        charCount += 2;
                    } else {
                        charCount++;
                    }
                    mName += temp;
                } catch (UnsupportedEncodingException e) {
                }
            }
            try {
                char temp = mName.charAt(mName.length() - 1);
                if (StreetscapeUtil.isChinese(temp)) {
                    mName = mName.substring(0, mName.length() - 1);
                } else {
                    mName = mName.substring(0, mName.length() - 2);
                }
                mName += "...";
            } catch (UnsupportedEncodingException e) {
            }

        } else {
            mName = name;
        }
        mTextView.setText(mName);
    }

    public void setHighLight(boolean current) {
        mIsHighlighted = current;
        if (mIsHighlighted) {
            mPhotoFrame.setBackgroundColor(0xff7da0d7);
        } else {
            mPhotoFrame.setBackgroundColor(0xffccd2dc);
        }
    }
}
