package wu.a.lib.view;

import java.io.Serializable;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public abstract class CoverFlowImageViewAdapter  extends AbstractCoverFlowImageAdapter implements
OnItemSelectedListener, OnItemClickListener {

	protected CoverFlowImage coverFlow;

	public CoverFlowImage getCoverFlowImage() {
	return coverFlow;
	}

	public void setCoverFlowImage(CoverFlowImage coverFlow) {
	this.coverFlow = coverFlow;
	coverFlow.setOnItemSelectedListener(this);
	}

	Handler handler = new Handler();

	public CoverFlowImageViewAdapter(CoverFlowImage coverFlow) {

	this.coverFlow = coverFlow;
	coverFlow.setOnItemClickListener(this);
	}

	/**
	* @return datas
	*/
	public abstract List<?> getDatas();

	public CoverFlowImageViewAdapter() {

	}

	@Override
	protected Bitmap createBitmap(int position, boolean focus) {

	if (position == coverFlow.getSelectedItemPosition()) {
	    focus = true;
	}
	System.out.println(this);
	View v = getView(position);

	if (v == null) {
	    return null;
	}
	int width = (int)coverFlow.getImageWidth();
	if (focus) {
	    v.setSelected(true);
	} else {
	    v.setSelected(false);
	}

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
    public void onItemClick(final AdapterView<?> parent,final  View view, final int position,final  long id) {
		this.position=position;
		this.notifyDataSetChanged();
//		handler.postDelayed(new Runnable() {
//
//		    @Override
//		    public void run() {
//		        for (int i = 0; i < parent.getChildCount(); i++) {
//		            BankImageView image = (BankImageView)parent.getChildAt(i);
//		            int positiona = (Integer)image.getTag();
//		            Bitmap bitmap = createBitmap(positiona, false);
//
//		            BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//		            bitmapDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//		            image.setBitmap(bitmap);
//		        }
//
//		        if (view == null) {
//		            return;
//		        }
//		        final BankImageView image = (BankImageView)view;
//
//		        final Bitmap bitmap = createBitmap(position, true);
//
//		        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//		        bitmapDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
//
//		        image.setBitmap(bitmap);
//		        // LogManager.d("magebi" + bitmap.getWidth() + " " +
//		        // bitmap.getHeight());
		        
//		    }
//		}, 0);
	}
	@Override
	public void onItemSelected(final AdapterView<?> parent, final View view, final int position,
	    final long id) {
		if (coverFlow != null && coverFlow.getOnCoverFlowImageItemSelectedListener() != null) {
            coverFlow.getOnCoverFlowImageItemSelectedListener().onItemSelected(parent, view,
                    position, id);
        }
	

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	// TODO Auto-generated method stub

	}

	public abstract View getView(final int position);

	public void setNewItem(Serializable tmp_payee) {
	this.tmp_payee=tmp_payee;
	}

	protected Serializable tmp_payee; 

	}