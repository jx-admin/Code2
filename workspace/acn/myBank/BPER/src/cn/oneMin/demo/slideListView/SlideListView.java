package cn.oneMin.demo.slideListView;


import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;


public class SlideListView extends ListView{

    
	private SlideListViewTouchListener touchListener;
	public SlideListView(Context context) {
		super(context);
		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        final int touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
		touchListener=new SlideListViewTouchListener(this,touchSlop);
		setOnTouchListener(touchListener);		
	}
	public SlideListView(Context context,AttributeSet attrs) {
		super(context,attrs);
		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        final int touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
		touchListener=new SlideListViewTouchListener(this,touchSlop);
		setOnTouchListener(touchListener);		
	}
	
}
