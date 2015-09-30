
package com.act.mbanking.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.act.mbanking.R;
import com.act.mbanking.manager.view.LegendItem;
import com.act.mbanking.manager.view.LegendItem.LegendItemText;
import com.act.mbanking.manager.view.LegendLinearLayout;

public class LegendManager extends ViewManager implements OnClickListener {

    int minWidth;

    /**
     * 
     */
    LegendLinearLayout legend_items;

    /**
     * 为了确保不布局高度不变而设的
     */
    LinearLayout legend_invisible;

    View legend_bar_btn;

    List<LegendItemText> list;

    public LegendManager(Activity activity, ViewGroup root) {
        super(activity, root);
        list = new ArrayList<LegendItem.LegendItemText>();
    }

    @Override
    protected void init() {
        view = root.findViewById(R.id.legend);
        initSubView();

    }

    public void clear() {

        legend_items.removeAllViews();

        list.clear();
        legend_items.requestLayout();

    }

    public void addLegendItem(LegendItemText legendItemText) {

        list.add(legendItemText);

    }

    public void generateUi() {
        legend_items.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            LegendItemText legendItemText = list.get(i);
            LayoutInflater layoutInflater = LayoutInflater.from(activity);
            LegendItem item = (LegendItem)layoutInflater.inflate(R.layout.legend_item, null);
            item.init();
            item.setTag(legendItemText);
            item.setUi(legendItemText);
            item.setOnClickListener(this);
            legend_items.addView(item);
        }

        // resetLayout();
        // legend_items.measureChildes();
        open();
    }

    private OnLegendItemSelectedListener onLegendItemSelectedListener;

    public OnLegendItemSelectedListener getOnLegendItemSelectedListener() {
        return onLegendItemSelectedListener;
    }

    public void setOnLegendItemSelectedListener(OnLegendItemSelectedListener onLegendItemSelectedListener) {
        this.onLegendItemSelectedListener = onLegendItemSelectedListener;
    }
    
    
    public static interface OnLegendItemSelectedListener {
        
        /**
         * 钮扣被点中所触发的事件
         * @param timeItem :被选中的对象
         * @param position：被选中的位置
         */
        public void onLegendItemSelected(View view,LegendItemText legendItemText, int position);
    }
    
    private void resetLayout() {

        if (legend_items.getChildCount() == 0) {
            return;
        }
        // View parent = (View)legend_items.getParent();
        // int parentWidth = parent.getMeasuredWidth();
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(minWidth,
                LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < legend_items.getChildCount(); i++) {
            LegendItem child = (LegendItem)legend_items.getChildAt(i);
            child.setLayoutParams(p);
            p.weight = 1;
        }
        legend_items.requestLayout();

    }

    boolean isOpen = false;

    @Override
    public void onClick(View v) {

        if (v == legend_bar_btn) {
            if (isOpen) {
                close();
            } else {
                open();
            }
        }else{
            if (onLegendItemSelectedListener != null) {
                LegendItemText legendItemText = (LegendItemText)v.getTag();
                int position = list.indexOf(legendItemText);
                onLegendItemSelectedListener.onLegendItemSelected(v,legendItemText, position);
            }
        }
    }

    private void open() {
        for (int i = 0; i < legend_items.getChildCount(); i++) {
            LegendItem child = (LegendItem)legend_items.getChildAt(i);
            child.open();
        }
        legend_invisible.setVisibility(View.GONE);
        isOpen = true;
        legend_bar_btn.setBackgroundResource(R.drawable.legend_bar_opened);
    }

    private void close() {
        for (int i = 0; i < legend_items.getChildCount(); i++) {
            LegendItem child = (LegendItem)legend_items.getChildAt(i);
            child.close();

        }
        isOpen = false;
        legend_bar_btn.setBackgroundResource(R.drawable.legend_bar_closed);
        legend_invisible.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initSubView() {
        legend_items = (LegendLinearLayout)view.findViewById(R.id.legend_items);
        legend_bar_btn = view.findViewById(R.id.legend_bar_btn);
        legend_invisible = (LinearLayout)view.findViewById(R.id.legend_invisible);

        legend_bar_btn.setOnClickListener(this);
    }

}
