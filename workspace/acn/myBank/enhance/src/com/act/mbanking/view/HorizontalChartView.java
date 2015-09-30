
package com.act.mbanking.view;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.act.mbanking.App;
import com.act.mbanking.Contants;
import com.act.mbanking.R;
import com.act.mbanking.bean.ChartModel;
import com.act.mbanking.manager.view.BankBitmapDrawable;
import com.act.mbanking.utils.TimeUtil;
import com.act.mbanking.utils.Utils;
import com.custom.view.RectLD;

/**
 * 用来显示横屏展示报表
 * 
 * @author seekting.x.zhang
 */
public class HorizontalChartView extends LinearLayout implements
        android.widget.CompoundButton.OnCheckedChangeListener {

    ListView listView;

    ProgressDialog downloading_pd ;
    List<ChartModelMapTool> allChartModelMapTools;// =
                                                  // Contants.getAllChartModelMapTools();

    List<ChartModelMapTool> targetChartModelMapTools = new ArrayList<ChartModelMapTool>();

    public Adapter adapter;

    Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			chartView.invalidate();
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
    	
    };

    RadioGroup left_right_group;

    RadioButton left_radio, right_radio;

    TextView totalAssets;

    TextView lastUpdateOn;

    public List<ChartModelMapTool> getList() {
        return targetChartModelMapTools;
    }

    public void refresh() {

        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                adapter.notifyDataSetChanged();
            }
        });
    }

    public void setAllList(List<ChartModelMapTool> list) {
        this.allChartModelMapTools = list;
    }

    public void setList(final List<ChartModelMapTool> list) {
        this.targetChartModelMapTools = list;
        adapter.setDatas(list);
//        handler.post(new Runnable() {

//            @Override
//            public void run() {
                // TODO Auto-generated method stub

            	if (chartView != null) {
            		chartView.setDatas(list);
            	}
//            }
//        });
//        refresh();
//        invalidate();
//        postinvalida();
    }

//    LinearLayout chart_layout;

    public HorizontalChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        downloading_pd = new ProgressDialog(context);
        downloading_pd.setTitle(R.string.waiting);
    }

    public void init() {

        listView = (ListView)findViewById(R.id.list);
        chartView = (ChartView)findViewById(R.id.chart_layout);
        left_right_group = (RadioGroup)findViewById(R.id.left_right_group);
        left_radio = (RadioButton)findViewById(R.id.left_radio);
        right_radio = (RadioButton)findViewById(R.id.right_radio);
        totalAssets = (TextView)findViewById(R.id.horizontal_title_big);
        lastUpdateOn = (TextView)findViewById(R.id.horizontal_title_small);
        left_radio.setOnCheckedChangeListener(this);
        right_radio.setOnCheckedChangeListener(this);

        adapter = new Adapter(getContext(), targetChartModelMapTools);
        listView.setAdapter(adapter);

    }

    String total_account_title;
    public void setTotalAssetText(String str) {
        total_account_title = str;
        if (totalAssets != null) {
            totalAssets.setText(str);
        }
    }

    public void setLastUpdateOnText(String str) {
        if (lastUpdateOn != null) {
            String time = getContext().getResources().getString(R.string.last_update_on);
            time = String.format(time, str);
            lastUpdateOn.setText(time);
        }
    }

    private ChartView chartView;

    public ChartViewWidthPath getChartView() {
        return chartView;
    }

    public void hideLeftRightGroup() {
        left_right_group.setVisibility(View.GONE);
    }

    public void showLeftRightGroup() {

        left_right_group.setVisibility(View.VISIBLE);
    }

    public void setRightText(String str) {

        right_radio.setText(str);
    }

    public void setLeftText(String str) {
        left_radio.setText(str);
    }

    public static class Adapter extends BaseAdapter {

        List<ChartModelMapTool> list;

        Context context;

        public Adapter(Context context, List<ChartModelMapTool> list) {

            this.list = list;
            this.context = context;
        }
        

        int s,e;
        public void setIndexArea(int s, int e) {
        	this.s=s;
        	this.e=e;
        }
        

        @Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return super.isEnabled(position);
		}


		@Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setDatas(List<ChartModelMapTool> list) {
            this.list = list;
            this.notifyDataSetChanged();
        }

        @Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {

				LayoutInflater layoutInflater = LayoutInflater.from(context);
				convertView = layoutInflater.inflate(
						R.layout.horizontal_chart_list_item, null);
			}
			ImageView imageView = (ImageView) convertView .findViewById(R.id.icon);
			TextView title_text = (TextView) convertView .findViewById(R.id.title_text);
			TextView initial = (TextView) convertView .findViewById(R.id.initial);
			TextView final_text = (TextView) convertView .findViewById(R.id.final_text);
			TextView percent = (TextView) convertView .findViewById(R.id.percent);
			ChartModelMapTool chartModelMapTool = list.get(list.size() - position - 1);
			double finalValue = 0, initialValue = 0, percent_value = 0;
			if (chartModelMapTool!= null&&chartModelMapTool.getYearList()!=null){
				if(chartModelMapTool.getYearList().size()>0) {
					ChartModel chartModel=chartModelMapTool.getYearList().get(chartModelMapTool.getYearList().size()-1);
					if(chartModel!=null){
						finalValue = chartModel.getValue();
					}
					chartModel=chartModelMapTool.getYearList().get(s);
					if(chartModel!=null){
						initialValue = chartModel.getValue();
					}
				}
			}

			if (finalValue == 0 && initialValue == 0) {
				imageView.setVisibility(View.GONE);
				title_text.setVisibility(View.GONE);
				initial.setVisibility(View.GONE);
				final_text.setVisibility(View.GONE);
				percent.setVisibility(View.GONE);
			} else {
				String final_text_value = Utils.formatMoney(finalValue, context
						.getResources().getString(R.string.dollar), true, true,
						false, false, true);
				final_text.setText("final:" + final_text_value);
				String initial_value = Utils.formatMoney(initialValue, context
						.getResources().getString(R.string.dollar), true, true,
						false, false, true);
				initial.setText("initial:" + initial_value);

				if (finalValue != 0) {
					percent_value = (finalValue - initialValue) / finalValue;
				}
				percent.setText(Utils.generateMoney(percent_value * 100) + "%");

				BitmapDrawable src = null;

				switch (chartModelMapTool.type) {
				case ChartModelMapTool.type_accounts:
					src = (BitmapDrawable) context.getResources().getDrawable(
							R.drawable.timescale_account__on);
					break;
				case ChartModelMapTool.type_investments:
					src = (BitmapDrawable) context.getResources().getDrawable(
							R.drawable.timescale_investment_on);

					break;
				case ChartModelMapTool.type_credit:
					src = (BitmapDrawable) context.getResources().getDrawable(
							R.drawable.timescale_credit_on);

					break;
				case ChartModelMapTool.type_prepaieds:
					src = (BitmapDrawable) context.getResources().getDrawable(
							R.drawable.timescale_prepaid_on);

					break;
				case ChartModelMapTool.type_loans:
					src = (BitmapDrawable) context.getResources().getDrawable(
							R.drawable.timescale_laons_on);

					break;
				}
				title_text.setText(chartModelMapTool.title);
				BankBitmapDrawable bankBitmapDrawable = new BankBitmapDrawable(
						src, BankBitmapDrawable.drawable_type_cicle);
				bankBitmapDrawable.setMainColor(chartModelMapTool.type);
				bankBitmapDrawable.setBitmapLevel(chartModelMapTool.level);
				imageView.setImageDrawable(bankBitmapDrawable);
				imageView.setVisibility(View.VISIBLE);
				title_text.setVisibility(View.VISIBLE);
				initial.setVisibility(View.VISIBLE);
				final_text.setVisibility(View.VISIBLE);
				percent.setVisibility(View.VISIBLE);

			}
			return convertView;
		}
    }

    public static class AdapterData {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            if (buttonView == left_radio) {
                adapter.setDatas(allChartModelMapTools);
                chartView.setDatas(allChartModelMapTools);
                totalAssets.setText("Total assets" + Contants.total_assets);
            } else if (buttonView == right_radio) {
                adapter.setDatas(targetChartModelMapTools);
                chartView.setDatas(targetChartModelMapTools);
                totalAssets.setText(total_account_title);
            }
            chartView.refresh();
        }
    }

    private OnLeftRightClickListener mOnLeftRightClickListener;

    public void setOnLeftRightClickListener(OnLeftRightClickListener onLeftRightClickListener) {
        mOnLeftRightClickListener = onLeftRightClickListener;
    }

    public static interface OnLeftRightClickListener {

        public void onRightClick(View v);

        public void onLeftClick(View v);
    }
    public void setStyle(final int style) {
//    	downloading_pd.show();
//    	handler.post(new Runnable(){
    		
//    		@Override
//    		public void run() {
    			RectLD dataArea=ChartView.getTimeStyleArea(style,null);
    			int size = (int)((dataArea.width()+TimeUtil.ONE_DAY) / TimeUtil.ONE_DAY);
    			if(size==0){
    				size=1;
    			}
    			int s=366-size;
    			int e=366;
    			adapter.setIndexArea(s, e);
    			adapter.notifyDataSetChanged();
    			chartView.setTimeStyle(s,e,style);
//    			// TODO Auto-generated method stub
    			chartView.postInvalidate();
//    			downloading_pd.dismiss();
//    		}});
    }
}
