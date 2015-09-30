
package com.accenture.mbank.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.accenture.mbank.R;

/**
 * @author seekting.x.zhang
 */
public class ContactSlidContainer extends FrameLayout implements OnClickListener {

    View topMargin;

    View midMargin;

    View bottomMargin;

    int topMarginHeight = 0;

    int midMarginHeight = 0;

    int bottomMarginHeight = 0;

    int slidCloseImageHeight = 0;

    int slidShowImageHeight = 0;

    ImageView slid1CloseImage, slid1ShowImage, slid2CloseImage, slid2ShowImage;

    ImageView slid1ShowImageAni, slid2ShowImageAni, slid1closeImageAni, slid2CloseImageAni;

    ScrollView slid1ContentView, slid2ContentView;

    LinearLayout slidContainerAni;

    int animationTime = 400;

    boolean isSlid1Open;

    boolean isSlid2Open;

    boolean animating = false;

    Handler handler;

    LinearLayout.LayoutParams topMarginLayoutParams, midMarginLayoutParams,
            bottomMarginLayoutParams, slid1CloseImageLayoutParams, slid1ShowImageLayoutParams,
            slid2ShowImageLayoutParams, slid2CloseImageLayoutParams, slid1ContentViewLayoutParams,
            slid2ContentViewLayoutParams;

    public ContactSlidContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactSlidContainer(Context context) {
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

        slid1ContentView = (ScrollView)findViewById(R.id.slid_1_content);
        slid1ContentViewLayoutParams = (LinearLayout.LayoutParams)slid1ContentView
                .getLayoutParams();

        slid2ContentView = (ScrollView)findViewById(R.id.slid_2_content);
        slid2ContentViewLayoutParams = (LinearLayout.LayoutParams)slid2ContentView
                .getLayoutParams();

        slidContainerAni = (LinearLayout)findViewById(R.id.slid_container_ani);
        slid2ShowImageAni = (ImageView)findViewById(R.id.slid_2_cycle_bottom_img_ani);

        slid1ShowImageAni = (ImageView)findViewById(R.id.slid_1_cycle_bottom_img_ani);

        slid1closeImageAni = (ImageView)findViewById(R.id.slid_1_cycle_top_img_ani);
        slid2CloseImageAni = (ImageView)findViewById(R.id.slid_2_cycle_top_img_ani);

        slid1CloseImage.setOnClickListener(this);
        slid2CloseImage.setOnClickListener(this);
        slid1ShowImage.setOnClickListener(this);
        slid2ShowImage.setOnClickListener(this);
        slid1ShowImageAni.setOnClickListener(this);
        slid2ShowImageAni.setOnClickListener(this);

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {

                switch (msg.what) {
                    case ANIMAT_END:
                        animating = false;
                        break;
                    case 2:
                        showSlip1();
                        break;
                    default:
                        break;
                }
            };

        };
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

        slidContainerAni.setVisibility(View.INVISIBLE);
        slid2ShowImageAni.setVisibility(View.INVISIBLE);
        int length = getMeasuredHeight() - topMarginHeight - slidCloseImageHeight;
        slid1ContentView.setVisibility(View.VISIBLE);
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, length);
        translateAnimation.setDuration(animationTime);
        slid1ShowImage.startAnimation(translateAnimation);
        midMargin.startAnimation(translateAnimation);
        slid2CloseImage.startAnimation(translateAnimation);
        slid2ShowImage.startAnimation(translateAnimation);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        scaleAnimation.setDuration(animationTime);
        slid1ContentView.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                animating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                handler.sendEmptyMessageDelayed(ANIMAT_END, ANIMAT_END_DELAY_TIME);
            }
        });

        slid1ShowImage.setVisibility(View.GONE);
        midMargin.setVisibility(View.GONE);
        bottomMargin.setVisibility(View.GONE);
        slid2CloseImage.setVisibility(View.GONE);
        slid2ShowImage.setVisibility(View.GONE);
        slid2ContentView.setVisibility(View.GONE);
        isSlid1Open = true;
        //
        // requestLayout();
    }

    private void closeSlip1() {

        if (!isSlid1Open) {
            return;
        }
        slidContainerAni.setVisibility(View.INVISIBLE);
        slid2ShowImageAni.setVisibility(View.INVISIBLE);
        int length = getMeasuredHeight() - topMarginHeight - slidCloseImageHeight;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, length, 0);
        translateAnimation.setDuration(animationTime);
        slid1ShowImage.startAnimation(translateAnimation);
        slid2CloseImage.startAnimation(translateAnimation);
        slid2ShowImage.startAnimation(translateAnimation);
        midMargin.startAnimation(translateAnimation);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        scaleAnimation.setDuration(animationTime);
        slid1ContentView.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

                animating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                handler.sendEmptyMessageDelayed(ANIMAT_END, ANIMAT_END_DELAY_TIME);
            }
        });

        slid1ShowImage.setVisibility(View.VISIBLE);
        slid1ContentView.setVisibility(View.GONE);
        midMargin.setVisibility(View.VISIBLE);
        bottomMargin.setVisibility(View.VISIBLE);
        slid2CloseImage.setVisibility(View.VISIBLE);
        slid2ShowImage.setVisibility(View.VISIBLE);
        slid2ContentView.setVisibility(View.GONE);
        isSlid1Open = false;
    }

    private void closeSlip2() {

        if (!isSlid2Open) {
            return;
        }
        slidContainerAni.setVisibility(View.VISIBLE);
        slid2ShowImageAni.setVisibility(View.VISIBLE);
        int length = slidCloseImageHeight + slidShowImageHeight + midMarginHeight;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -length, 0);
        translateAnimation.setDuration(animationTime);
        slid1ShowImage.setVisibility(View.INVISIBLE);
        slid2CloseImage.setVisibility(View.INVISIBLE);
        slid2ShowImage.setVisibility(View.INVISIBLE);
        slidContainerAni.startAnimation(translateAnimation);

        int start = slidShowImageHeight + bottomMarginHeight;
        int end = slidCloseImageHeight + slidShowImageHeight + midMarginHeight;
        TranslateAnimation translateAnimationUp = new TranslateAnimation(0, 0, start, 0);
        translateAnimationUp.setDuration(animationTime);
        slid2ShowImageAni.startAnimation(translateAnimationUp);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

                animating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                slid1CloseImage.setVisibility(View.VISIBLE);
                slid1ShowImage.setVisibility(View.VISIBLE);
                slid1ContentView.setVisibility(View.GONE);
                slid2CloseImage.setVisibility(View.VISIBLE);
                slid2ShowImage.setVisibility(View.VISIBLE);
                midMargin.setVisibility(View.VISIBLE);
                bottomMargin.setVisibility(View.VISIBLE);
                isSlid2Open = false;
                handler.sendEmptyMessageDelayed(ANIMAT_END, ANIMAT_END_DELAY_TIME);
            }
        });
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.63f);
        scaleAnimation.setDuration(animationTime);
        slid2ContentView.startAnimation(scaleAnimation);

        slid2ContentView.setVisibility(View.GONE);

    }

    private void snapFrom2to1() {

        if (!isSlid2Open) {
            return;
        }
        slidContainerAni.setVisibility(View.VISIBLE);
        slid2ShowImageAni.setVisibility(View.VISIBLE);
        int length = slidCloseImageHeight + slidShowImageHeight + midMarginHeight;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -length, 0);
        translateAnimation.setDuration(animationTime);
        slid1ShowImage.setVisibility(View.INVISIBLE);
        slid2CloseImage.setVisibility(View.INVISIBLE);
        slid2ShowImage.setVisibility(View.INVISIBLE);
        slidContainerAni.startAnimation(translateAnimation);

        int start = slidShowImageHeight + bottomMarginHeight;
        int end = slidCloseImageHeight + slidShowImageHeight + midMarginHeight;
        TranslateAnimation translateAnimationUp = new TranslateAnimation(0, 0, start, 0);
        translateAnimationUp.setDuration(animationTime);
        slid2ShowImageAni.startAnimation(translateAnimationUp);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

                animating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                slid1CloseImage.setVisibility(View.VISIBLE);
                slid1ShowImage.setVisibility(View.VISIBLE);
                slid1ContentView.setVisibility(View.GONE);
                slid2CloseImage.setVisibility(View.VISIBLE);
                slid2ShowImage.setVisibility(View.VISIBLE);
                midMargin.setVisibility(View.VISIBLE);
                bottomMargin.setVisibility(View.VISIBLE);
                isSlid2Open = false;

                slidContainerAni.setVisibility(View.INVISIBLE);
                slid2ShowImageAni.setVisibility(View.INVISIBLE);
                int start = midMarginHeight+slidCloseImageHeight;
                int length = getMeasuredHeight() - topMarginHeight - slidCloseImageHeight;
                slid1ContentView.setVisibility(View.VISIBLE);
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, start, length+start);
                translateAnimation.setDuration(animationTime);
                slid1ShowImage.startAnimation(translateAnimation);
                midMargin.startAnimation(translateAnimation);
                slid2CloseImage.startAnimation(translateAnimation);
                slid2ShowImage.startAnimation(translateAnimation);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(animationTime);
                slid1ContentView.startAnimation(scaleAnimation);
                scaleAnimation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                        animating = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // TODO Auto-generated method stub
                        handler.sendEmptyMessageDelayed(ANIMAT_END, ANIMAT_END_DELAY_TIME);
                    }
                });

                slid1ShowImage.setVisibility(View.GONE);
                midMargin.setVisibility(View.GONE);
                bottomMargin.setVisibility(View.GONE);
                slid2CloseImage.setVisibility(View.GONE);
                slid2ShowImage.setVisibility(View.GONE);
                slid2ContentView.setVisibility(View.GONE);
                isSlid1Open = true;
                //
                // requestLayout();

            }
        });
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.63f);
        scaleAnimation.setDuration(animationTime);
        slid2ContentView.startAnimation(scaleAnimation);

        slid2ContentView.setVisibility(View.GONE);

    }

    private void showSlip21() {

        int length = slidCloseImageHeight + slidShowImageHeight + midMarginHeight;

        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -length);
        translateAnimation.setDuration(250);

        slid2CloseImage.startAnimation(translateAnimation);
        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, 0, -length);
        translateAnimation2.setDuration(250);
        slid2ShowImage.startAnimation(translateAnimation2);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                slid1CloseImage.setVisibility(View.GONE);
                slid1ShowImage.setVisibility(View.GONE);
                midMargin.setVisibility(View.GONE);
                int length = slidShowImageHeight + topMarginHeight + slidCloseImageHeight;
                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -length,
                        slidShowImageHeight + bottomMarginHeight);
                translateAnimation.setDuration(animationTime);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
                scaleAnimation.setDuration(animationTime);
                slid2ShowImage.startAnimation(translateAnimation);
                slid2ContentView.startAnimation(scaleAnimation);
                slid2ContentView.setVisibility(View.VISIBLE);
                slid2ShowImage.setVisibility(View.GONE);
            }
        });

    }

    private void showSlip2() {

        slidContainerAni.setVisibility(View.VISIBLE);
        slid2ShowImageAni.setVisibility(View.VISIBLE);

        int length = slidCloseImageHeight + slidShowImageHeight + midMarginHeight;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -length);
        translateAnimation.setDuration(animationTime);

        slidContainerAni.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

                animating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                slid2CloseImage.setVisibility(View.VISIBLE);
                isSlid2Open = true;
                handler.sendEmptyMessageDelayed(ANIMAT_END, ANIMAT_END_DELAY_TIME);
            }

        });
        slid2ShowImageAni.setVisibility(INVISIBLE);
        slidContainerAni.setVisibility(View.INVISIBLE);

        length = slidShowImageHeight + bottomMarginHeight;
        TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, 0, length);
        translateAnimation2.setDuration(animationTime);
        slid2ShowImage.startAnimation(translateAnimation2);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.6f);
        scaleAnimation.setDuration(animationTime);
        slid2ContentView.startAnimation(scaleAnimation);
        slid2ContentView.setVisibility(View.VISIBLE);
        slid2CloseImage.setVisibility(View.INVISIBLE);
        slid1CloseImage.setVisibility(View.GONE);
        slid1ShowImage.setVisibility(View.GONE);
        midMargin.setVisibility(View.GONE);
        slid2ShowImage.setVisibility(View.GONE);
        bottomMargin.setVisibility(View.GONE);
        slid1ContentView.setVisibility(View.GONE);

    }

    public void setSlid1CloseImage(int resourse) {
        slid1CloseImage.setImageResource(resourse);
        slid1closeImageAni.setImageResource(resourse);
    }

    public void setSlid2CloseImage(int resourse) {
        slid2CloseImage.setImageResource(resourse);
        slid2CloseImageAni.setImageResource(resourse);
    }

    public void setSlid1ContentLayout(View child) {

        slid1ContentView.removeAllViews();
        slid1ContentView.addView(child);

    }

    @Override
    public void onClick(View v) {

        if (animating) {
            return;
        }
        if (v == slid1CloseImage) {
            closeSlip1();
        } else if (v == slid1ShowImage) {
            showSlip1();

        } else if (v == slid2ShowImage) {
            showSlip2();

        } else if (v == slid2CloseImage) {
            closeSlip2();
        } else if (v == slid1ShowImageAni) {
            showSlip1();
        } else if (v == slid2ShowImageAni) {
            showSlip2();
        } else if (v == slid2ContentView) {

        }
    }

}
