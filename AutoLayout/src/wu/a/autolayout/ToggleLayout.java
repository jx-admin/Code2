package wu.a.autolayout;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

public class ToggleLayout extends FrameLayout implements OnClickListener{
    private boolean mChecked;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnClickListener mOnClickListener;
    private View childView1,childView2;

	public ToggleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ToggleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ToggleLayout(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		setOnClickListener(this);
	}
	
	private void updateCompoundView(){
		int acount=getChildCount();
		if(acount>1){
			childView1=getChildAt(0);
			childView2=getChildAt(1);
			childView1.setClickable(false);
			childView2.setClickable(false);
		}else if(acount>0){
			childView1=getChildAt(0);
			childView1.setClickable(false);
			childView2=null;
		}else{
			childView1=null;
			childView2=null;
		}
	}
	
	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		updateCompoundView();
	}
	
	@Override
	protected boolean addViewInLayout(View child, int index,
			android.view.ViewGroup.LayoutParams params,
			boolean preventRequestLayout) {
		boolean result = super.addViewInLayout(child, index, params, preventRequestLayout);
		updateCompoundView();
		return result;
	}
	
	@Override
	public void removeAllViews() {
		super.removeAllViews();
		updateCompoundView();
	}
	
	@Override
	public void removeView(View view) {
		super.removeView(view);
		updateCompoundView();
	}
	@Override
	public void removeViewAt(int index) {
		super.removeViewAt(index);
		updateCompoundView();
	}
	
	@Override
	public void removeViews(int start, int count) {
		super.removeViews(start, count);
		updateCompoundView();
	}
	@Override
	public void removeViewInLayout(View view) {
		super.removeViewInLayout(view);
		updateCompoundView();
	}
	
	@Override
	public void removeViewsInLayout(int start, int count) {
		super.removeViewsInLayout(start, count);
		updateCompoundView();
	}
	@Override
	public void removeAllViewsInLayout() {
		super.removeAllViewsInLayout();
		updateCompoundView();
	}
	
//	private float lastPressX,lastPressY;
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		switch(ev.getAction()){
//		case MotionEvent.ACTION_DOWN:
//			lastPressX=ev.getRawX();
//			lastPressY=ev.getRawY();
//			break;
//		}
//		return super.onInterceptTouchEvent(ev);
//	}

	@Override
	public void onClick(View v) {
//		toggle();
	}


    static class SavedState extends BaseSavedState {
        boolean checked;

        /**
         * Constructor called from {@link CompoundButton#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }
        
        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean)in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        @Override
        public String toString() {
            return "CompoundButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + checked + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public boolean performClick() {
        /*
         * XXX: These are tiny, need some surrounding 'expanded touch area',
         * which will need to be implemented in Button if we only override
         * performClick()
         */

        /* When clicked, toggle the state */
        toggle();
        return super.performClick();
    }

    @ViewDebug.ExportedProperty
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * <p>Changes the checked state of this button.</p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if(mChecked){
            	if(childView1!=null){
            		childView1.setVisibility(View.VISIBLE);
            	}
            	if(childView2!=null){
            		childView2.setVisibility(View.GONE);
            	}
            }else{
            	if(childView1!=null){
            		childView1.setVisibility(View.GONE);
            	}
            	if(childView2!=null){
            		childView2.setVisibility(View.VISIBLE);
            	}
            }
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            if(mOnClickListener!=null){
            	mOnClickListener.onClick(this);
            }
        }
    }
    
    
    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(View buttonView, boolean isChecked);
    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
  
        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }
}
