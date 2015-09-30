
package com.act.mbanking.manager.view.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.act.mbanking.manager.view.BankImageView;
import com.custom.view.CoverFlow;

/**
 * This class is an adapter that provides base, abstract class for images
 * adapter.
 */
public abstract class AbstractCoverFlowImageAdapter extends BaseAdapter {
	
	protected boolean isTouch;

    protected CoverFlow coverFlow;

    /** The Constant TAG. */
    private static final String TAG = AbstractCoverFlowImageAdapter.class.getSimpleName();

    /** The width. */
    private float width = 0;

    /** The height. */
    private float height = 0;


    public AbstractCoverFlowImageAdapter() {
        super();
    }

    /**
     * Set width for all pictures.
     * 
     * @param width picture height
     */
    public synchronized void setWidth(final float width) {
        this.width = width;
    }

    /**
     * Set height for all pictures.
     * 
     * @param height picture height
     */
    public synchronized void setHeight(final float height) {
        this.height = height;
    }

    public int getSelected(){
    	return isTouch?coverFlow.getSelectedItemPosition():-1;
    }
    
    @Override
    public final Bitmap getItem(final int position) {
        return createBitmap(position,isTouch&& position==coverFlow.getSelectedItemPosition());
    }

    /**
     * Creates new bitmap for the position specified.
     * 
     * @param position position
     * @return Bitmap created
     */
    protected abstract Bitmap createBitmap(int position, boolean hasFocus);

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public final synchronized long getItemId(final int position) {
        return position;
    }

    /*
     * (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public final synchronized BankImageView getView(final int position, final View convertView,
            final ViewGroup parent) {
        BankImageView imageView;
        if (convertView == null) {
            imageView = new BankImageView(parent.getContext());
            imageView.setLayoutParams(new CoverFlow.LayoutParams((int)width, (int)height));
        } else {
            imageView = (BankImageView)convertView;
        }

        Bitmap bitmap=imageView.getBitmap();
        if(bitmap!=null){
        	bitmap.recycle();
        }
        bitmap= getItem(position);

        imageView.setBitmap(bitmap);
        imageView.setTag(Integer.valueOf(position));
        return imageView;
    }

}
