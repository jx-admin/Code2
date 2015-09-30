package com.android.study.abc.examples.memory;

import java.util.ArrayList;
import java.util.List;

import a.w.utils.StoreFormater;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.study.abc.examples.R;

@SuppressLint("NewApi")
public class MemoryActivity extends Activity {

	MemoryUtils mMemoryUtils;
	ListView pkg_stats_ls;
	LayoutInflater lf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.memory_main);
		pkg_stats_ls = (ListView) findViewById(R.id.pkg_stats_ls);
		lf = LayoutInflater.from(this);
		pkg_stats_ls.setAdapter(pkg_stats_ad);
		mMemoryUtils = new MemoryUtils();
	}

	public void onMemoryButtonClicked(View view) {
		mMemoryUtils.initMemoryInfo(this);
		((TextView) findViewById(R.id.memory_tv))
				.setText("T:"
						+ StoreFormater.getStoreSize(
								mMemoryUtils.getTotalSize(), ' ')
						+ " F:"
						+ StoreFormater.getStoreSize(
								mMemoryUtils.getFreeSize(), ' ')
						+ " c:"
						+ StoreFormater.getStoreSize(
								mMemoryUtils.getCachedSize(), ' '));

	}

	public void onMemoryInfoButtonClicked(View view) {
		mMemoryUtils.initMemoryInfo(this);
		((TextView) findViewById(R.id.memory_info_tv)).setText("T:"
				+ StoreFormater.getStoreSize(
						mMemoryUtils.getMemoryInfo().totalMem, ' ')
				+ " F:"
				+ StoreFormater.getStoreSize(
						mMemoryUtils.getMemoryInfo().availMem, ' ')
				+ " t:"
				+ StoreFormater.getStoreSize(
						mMemoryUtils.getMemoryInfo().threshold, ' '));

	}

	public void onMemoryPkgButtonClicked(View view) {
		mMemoryUtils.initMemoryInfo(this);
		totalCacheSize = 0;
		pkgStats.clear();
		mMemoryUtils.queryToatalCache(this, mStatsObserver);
		setPkgCache();
	}

	private void setPkgCache() {
		((TextView) findViewById(R.id.tatol_memory_pkg_tv)).setText("pkg:"
				+ StoreFormater.getStoreSize(totalCacheSize));
	}

	long totalCacheSize;
	List<PackageStats> pkgStats = new ArrayList<PackageStats>();
	private IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			totalCacheSize += pStats.cacheSize;
			pkgStats.add(pStats);
			setPkgCache();
			pkg_stats_ad.notifyDataSetChanged();
		}
	};

	BaseAdapter pkg_stats_ad=new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = lf.inflate(R.layout.memory_pkg_item, null);
			}

			PackageStats mPackageStats = pkgStats.get(position);
			((TextView) convertView.findViewById(R.id.textView1)) .setText(mPackageStats.packageName);
			((TextView) convertView.findViewById(R.id.textView2))
					.setText(String.valueOf(mPackageStats.codeSize));
			((TextView) convertView.findViewById(R.id.textView3))
					.setText(String.valueOf(mPackageStats.dataSize));
			((TextView) convertView.findViewById(R.id.textView4))
					.setText(String.valueOf(mPackageStats.cacheSize));
			((TextView) convertView.findViewById(R.id.textView5))
					.setText(String.valueOf(mPackageStats.dataSize
							+ mPackageStats.cacheSize
							+ mPackageStats.codeSize));

			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pkgStats.size();
		}
	};
}
