
package com.act.mbanking.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.act.mbanking.R;

public class NewRequestManager implements OnClickListener {
    private Context cxt;

    LinearLayout layoutView;

    LayoutInflater inflater;

    public NewRequestManager(Context cxt) {
        this.cxt = cxt;
        inflater = (LayoutInflater)cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
    }

    private void init() {
        layoutView = new LinearLayout(cxt);
        layoutView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

        View child1 = inflater.inflate(R.layout.new_request_item, null);
        layoutView.addView(child1,lp);
        child1.setOnClickListener(this);
        Holde holde = new Holde();
        child1.setTag(holde);
        holde.tv_1 = (TextView)child1.findViewById(R.id.tv_1);
        holde.tv_2 = (TextView)child1.findViewById(R.id.tv_2);
        holde.selector_iv = (ImageView)child1.findViewById(R.id.img_l);
        holde.details_lin = (LinearLayout)child1.findViewById(R.id.content_layout);
        holde.tv_1.setText(R.string.object);
        holde.tv_2.setText(R.string.accounts);
        holde.details_lin.addView(inflater.inflate(R.layout.contact_new_request_object, null),lp);
        

        child1 = inflater.inflate(R.layout.new_request_item, null);
        layoutView.addView(child1,lp);
        child1.setOnClickListener(this);
        holde = new Holde();
        child1.setTag(holde);
        holde.tv_1 = (TextView)child1.findViewById(R.id.tv_1);
        holde.tv_2 = (TextView)child1.findViewById(R.id.tv_2);
        holde.selector_iv = (ImageView)child1.findViewById(R.id.img_l);
        holde.details_lin = (LinearLayout)child1.findViewById(R.id.content_layout);
        holde.details_lin.setVisibility(View.VISIBLE);
        holde.tv_1.setText(R.string.channel);
        holde.tv_2.setText(R.string.tap_to_set);
        holde.details_lin.addView(inflater.inflate(R.layout.contact_new_request_channel, null),lp);

        child1 = inflater.inflate(R.layout.new_request_item, null);
        layoutView.addView(child1,lp);
        child1.setOnClickListener(this);
        holde = new Holde();
        child1.setTag(holde);
        holde.tv_1 = (TextView)child1.findViewById(R.id.tv_1);
        holde.tv_2 = (TextView)child1.findViewById(R.id.tv_2);
        holde.selector_iv = (ImageView)child1.findViewById(R.id.img_l);
        holde.details_lin = (LinearLayout)child1.findViewById(R.id.content_layout);
        holde.tv_1.setText(R.string.details);
        holde.tv_2.setText(R.string.tap_to_set1);
        holde.details_lin.addView(inflater.inflate(R.layout.contact_new_request_description, null),lp);

        child1 = inflater.inflate(R.layout.new_request_item, null);
        layoutView.addView(child1,lp);
        child1.setOnClickListener(this);
        holde = new Holde();
        child1.setTag(holde);
        holde.tv_1 = (TextView)child1.findViewById(R.id.tv_1);
        holde.tv_2 = (TextView)child1.findViewById(R.id.tv_2);
        holde.selector_iv = (ImageView)child1.findViewById(R.id.img_l);
        holde.details_lin = (LinearLayout)child1.findViewById(R.id.content_layout);
        holde.tv_1.setText(R.string.description_);
        holde.tv_2.setText(R.string.tap_to_set1);
        holde.details_lin.addView(inflater.inflate(R.layout.contact_new_request_description, null),lp);
    }

    class Holde {
        TextView tv_1;

        TextView tv_2;

        TextView tv_3;

        ImageView selector_iv;

        LinearLayout details_lin;

        TextView details_tv;

        TextView details_tv2;

        TextView details_tv3;

        TextView details_tv4;

        int position;
    }

    public View getView() {
        return layoutView;
    }

    Holde selected = null;

    int selectPosition = -1;

    @Override
    public void onClick(View v) {
        Holde holde = (Holde)v.getTag();
        if (holde.details_lin.getVisibility() == View.VISIBLE) {
            holde.selector_iv.setImageResource(R.drawable.arrow_down);
            holde.details_lin.setVisibility(View.GONE);
            selected = null;
            selectPosition = -1;
        } else {
        	resetSelected();
            if (holde.position == -2) {
                // loadData(recordCount);
            } else {
                holde.selector_iv.setImageResource(R.drawable.arrow_up);
                holde.details_lin.setVisibility(View.VISIBLE);
                selectPosition = holde.position;
                selected = holde;
            }
        }

    }
    private void resetSelected(){
    	if (selected != null) {
            selected.details_lin.setVisibility(View.GONE);
            selected.selector_iv.setImageResource(R.drawable.arrow_down);
            selected = null;
            selectPosition = -1;
        }
    }
    
    private View getView(Context cxt,int id){
        LinearLayout l=new LinearLayout(cxt);
        l.setOrientation(LinearLayout.VERTICAL);
        for(int i=0;i<9;i++){
            TextView t=new TextView(cxt);
            t.setText("ddddd"+i);
            l.addView(t);
        }
        
        return l;
    }
}
