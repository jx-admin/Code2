package com.android.test.wifi;

import java.util.List;
import android.annotation.SuppressLint;
import android.net.wifi.ScanResult;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import wu.a.view.BaseViewManager;

public class WifiListManager extends BaseViewManager<List<ScanResult>> {
	private RecyclerView rcvWifiList;
    private List<ScanResult> mScanResults;
    MyAdapter adapter;
    private LayoutInflater mLf;
    
    public WifiListManager(Context context){
    	super(context);
        mLf=LayoutInflater.from(context);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private Button btnConnect;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvWifiName);
            btnConnect = (Button) itemView.findViewById(R.id.btnConnect);
        }

        @SuppressLint("NewApi")
		public void render(ScanResult sr) {
            tvName.setText(sr.toString());
        }
    }

    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Context context;
        public MyAdapter(Context  context){
            this.context=context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(mLf.inflate( R.layout.wifi_list_item, null));
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.render(mScanResults.get(i));
        }

        @Override
        public int getItemCount() {
            return mScanResults == null ? 0 : mScanResults.size();
        }
    }

	@Override
	public View createView(ViewGroup viewGroup) {
        View v = mLf.inflate(R.layout.wifi_list, null);
        rcvWifiList = (RecyclerView) v.findViewById(R.id.rcyWifiList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvWifiList.setLayoutManager(manager);
        adapter = new MyAdapter(context);
        rcvWifiList.setAdapter(adapter);
        return v;
	}



	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void setData(List<ScanResult> list) {
        this.mScanResults=list;
        adapter.notifyDataSetChanged();
	}



	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
