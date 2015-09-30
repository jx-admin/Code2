package com.act.mbanking.manager;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public abstract class ExtensibleListAdapter<T> extends BaseAdapter {
        LayoutInflater lf;

        Context context;

        int othersId = 4;

        List<T> datas;

        boolean isShowOthers = false;;

        boolean enable = true;

        public ExtensibleListAdapter(Context context) {
            this.context = context;
            lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        public ExtensibleListAdapter(Context context,LayoutInflater layoutInflater) {
            this.context = context;
            lf =layoutInflater;
        }

        public void isShowOthers(boolean show) {
            isShowOthers = show;
        }

        public boolean isEnabled(int position) {
//            if (othersId == position) {
//                return false;
//            }
            return true;
        }

        @Override
        public boolean isEmpty() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (datas != null) {
                count = datas.size();
            }
            if (isShowOthers && count > 0) {
                ++count;
            }
            return count;
        }

        @Override
        public T getItem(int position) {
            if (datas != null && position < datas.size()) {
                return datas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
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

        public void setDatas(List<T> movements) {
            this.datas = movements;

        }
    }
