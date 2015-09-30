
package com.act.mbanking.manager.view.adapter;

import java.io.Serializable;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.custom.view.CoverFlow;

//import com.accenture.mbank.util.LogManager;

public abstract class CoverFlowImageAdapter extends AbstractCoverFlowImageAdapter implements
        OnItemSelectedListener, OnItemClickListener, OnTouchListener {

    public CoverFlow getCoverFlow() {
        return coverFlow;
    }

    public void setCoverFlow(CoverFlow coverFlow) {
        this.coverFlow = coverFlow;
        coverFlow.setOnItemSelectedListener(this);
        coverFlow.setOnItemClickListener(this);
        coverFlow.setOnTouchListener(this);
    }

    Handler handler = new Handler();

    public CoverFlowImageAdapter(CoverFlow coverFlow) {
    	setCoverFlow(coverFlow);
    }

    /**
     * @return datas
     */
    public abstract List<?> getDatas();

    public CoverFlowImageAdapter() {

    }

    @Override
    protected Bitmap createBitmap(int position, boolean focus) {

        View v = getView(position);

        if (v == null) {
            return null;
        }
        int width = (int)coverFlow.getImageWidth();
            v.setSelected(focus);

        int height = (int)coverFlow.getImageHeight();
        // LogManager.d("width:adapter" + width);
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        v.measure(widthMeasureSpec, heightMeasureSpec);

        // v.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
        // MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        PaintFlagsDrawFilter filter = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
        canvas.setDrawFilter(filter);

        v.draw(canvas);
        // v.buildDrawingCache();
        // Bitmap bitmap = v.getDrawingCache();

        return bitmap;
    }
    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position,
            final long id) {
    	if(mOnItemClickListener!=null){
    		mOnItemClickListener.onItemClick(parent,view, position, id);
    	}
    	this.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(final AdapterView<?> parent, final View view, final int position,
            final long id) {
//    	this.position=position;
    	if(mOnItemSelectedListener!=null){
    		mOnItemSelectedListener.onItemSelected(parent,view, position, id);
    	}
    	this.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    	if(mOnItemSelectedListener!=null){
    		mOnItemSelectedListener.onNothingSelected(parent);
    	}
    }
    
    OnItemSelectedListener mOnItemSelectedListener;
    OnItemClickListener mOnItemClickListener;
    public void setOnItemSelectedListener(OnItemSelectedListener mOnItemSelectedListener){
    	this.mOnItemSelectedListener=mOnItemSelectedListener;
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
    	this.mOnItemClickListener=mOnItemClickListener;
    }

    public boolean onTouch(View v, MotionEvent event){
    	isTouch=true;
    	return false;
    }
    public abstract View getView(final int position);

    public void setNewItem(Serializable tmp_payee) {
        this.tmp_payee=tmp_payee;
    }
    
    protected Serializable tmp_payee; 

}
