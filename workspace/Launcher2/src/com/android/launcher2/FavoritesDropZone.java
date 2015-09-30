package com.android.launcher2;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

import com.android.launcher.R;

public class FavoritesDropZone extends ImageView implements OnLongClickListener, DropTarget, DragSource {
    private final Paint mAddPaint = new Paint();
    private Launcher mLauncher;
    private DragController mDragController;
    private IconCache mIconCache;
    private Drawable mEmptyDrawable;
    private boolean mClicksVisible;
    private boolean mOriginatedDrag;
    private boolean mSwitchCommitted;

	public FavoritesDropZone(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FavoritesDropZone(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final int srcColor = context.getResources().getColor(R.color.add_color_filter);
        mAddPaint.setColorFilter(new PorterDuffColorFilter(srcColor, PorterDuff.Mode.SRC_ATOP));
        mEmptyDrawable = getDrawable();
        setOnLongClickListener(this);
        mClicksVisible = false;
    }
    
    public void setLauncher(Launcher launcher) {
    	mLauncher = launcher;
    }
    
    public void setIconCache(IconCache iconCache) {
    	mIconCache = iconCache;
    }
    
    boolean isAcceptable(Object dragInfo, DragSource source) {
    	if ((getTag() != null) && !source.switchingSupported()) {
    		return false;
    	}
        ItemInfo info = (ItemInfo) dragInfo;
        switch (info.itemType) {
        case LauncherSettings.Favorites.ITEM_TYPE_APPLICATION:
        case LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT:
        	break;
        default:
            return false;
        }
        
        return true;
    }

    @Override
    protected void drawableStateChanged() {
    	if (mClicksVisible) {
    		super.drawableStateChanged();
    	}
    }
    
	@Override
	public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
		return isAcceptable(dragInfo, source);
	}

	@Override
	public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo, Rect recycle) {
		return null;
	}

	@Override
	public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
		if (isAcceptable(dragInfo, source)) {
			dragView.setPaint(mAddPaint);

	    	ItemInfo info = (ItemInfo)getTag();
	    	if ((info != null) && (source.switchingSupported()) && (source != this)) {
	    		source.attemptSwitch(info);
	    		disableClicks();
	    	}
		}
	}

	@Override
	public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
		if (isAcceptable(dragInfo, source)) {
			dragView.setPaint(null);

	    	ItemInfo info = (ItemInfo)getTag();
	    	if ((info != null) && (source.switchingSupported()) && (source != this)) {
	    		enableClicks();
	    		source.revertSwitch((ItemInfo)dragInfo);
	    	}
		}
	}

	@Override
	public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
	}

	@Override
	public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    	ItemInfo info = (ItemInfo)getTag();
    	if ((info != null) && (source.switchingSupported()) && (source != this)) {
    		source.commitSwitch(info);
    	}
    	if (source != this) {
    		setInfo((ItemInfo)dragInfo, true);
    	}
    	else {
    		enableClicks();
    	}
	}
	
	public void setInfo(ItemInfo info, boolean persist) {
        switch (info.itemType) {
        case LauncherSettings.Favorites.ITEM_TYPE_APPLICATION:
        case LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT:
            if (info.container == NO_ID && info instanceof ApplicationInfo) {
                info = new ShortcutInfo((ApplicationInfo)info);
            }
    		setTag(info);
    		break;
        default:
            return;
        }

        if (persist) {
	    	int viewId = getId();
	    	int slotId;
	    	switch (viewId) {
	    	case R.id.favorites_dropzone_1:
	    		slotId = 1;
	    		break;
	    	case R.id.favorites_dropzone_2:
	    		slotId = 2;
	    		break;
	    	case R.id.favorites_dropzone_3:
	    		slotId = 3;
	    		break;
	    	case R.id.favorites_dropzone_4:
	    		slotId = 4;
	    		break;
	    	default:
	    		throw new RuntimeException("Assertion failed: Unrecognized favorites drop zone view id");
	    	}
	    	LauncherModel.addOrMoveItemInDatabase(mLauncher, info, LauncherSettings.Favorites.CONTAINER_FAVORITES_BAR, slotId, 0, 0);
        }
        
		enableClicks();
	}
	
	void enableClicks() {
		ItemInfo info = (ItemInfo)getTag();
        switch (info.itemType) {
        case LauncherSettings.Favorites.ITEM_TYPE_APPLICATION:
        case LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT:
           	setImageBitmap(((ShortcutInfo)info).getIcon(mIconCache));
    		mClicksVisible = true;
            break;
        default:
            return;
        }
	}
	
	void disableClicks() {
		mClicksVisible = false;
		setImageDrawable(mEmptyDrawable);
	}

	@Override
	public void onDropCompleted(View target, boolean success) {
		if (!mSwitchCommitted && (target != this)) {
			if (success) {
				setTag(null);
			}
			else {
				enableClicks();
			}
		}
		
		mSwitchCommitted = false;
		mOriginatedDrag = false;
	}

	@Override
	public void setDragController(DragController dragger) {
    	mDragController = dragger;
        mDragController.addDropTarget(this);
	}

	@Override
	public boolean onLongClick(View v) {
		Object info = getTag();
		if (info == null) {
			mLauncher.pickShortcut(this);
		}
		else {
			mDragController.startDrag(this, this, getTag(), DragController.DRAG_ACTION_COPY);
			mOriginatedDrag = true;
        
			// Delay this to avoid a flicker...
			postDelayed(new Runnable() {
					@Override
					public void run() {
						disableClicks();
					}
        		}, 500);
		}
		return true;
	}
	
	public void onAppRemove(ApplicationInfo info) {
		Object thisInfo = getTag();
		if (thisInfo != null) {
			String packageName = info.componentName.getPackageName();
			String thisPackageName = ((ShortcutInfo)thisInfo).intent.getComponent().getPackageName();
			if (packageName.equals(thisPackageName)) {
				LauncherModel.deleteItemFromDatabase(mLauncher, (ItemInfo)thisInfo);
				setTag(null);
				disableClicks();
			}
		}
	}

	@Override
    public boolean switchingSupported() {
		return true;
	}
	
	@Override
    public void attemptSwitch(ItemInfo newInfo) {
		setInfo(newInfo, false);
    	mSwitchCommitted = false;
	}
	
	@Override
    public void revertSwitch(ItemInfo origInfo) {
		setInfo(origInfo, false);
		if (mOriginatedDrag) {
			disableClicks();
		}
		mSwitchCommitted = false;
	}
	
	@Override
    public void commitSwitch(ItemInfo newInfo) {
		setInfo(newInfo, true);
		mSwitchCommitted = true;
	}

	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLauncher.isAllAppsVisible() && ((ev.getAction() & MotionEvent.ACTION_MASK) != MotionEvent.ACTION_CANCEL)) {
        	// Note, we need to dispatch ACTION_CANCEL to our super class to ensure that motion events can be intercepted (normally by AllAppsTransition).
        	return false;
        }
        return super.onTouchEvent(ev);
	}
}
