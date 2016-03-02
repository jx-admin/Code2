package wu.a.lib.view;

import java.util.ArrayList;
import java.util.List;

import wu.a.template.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

public class CoverFlowImageActivity extends Activity {

	/** Called when the activity is first created. */
	CoverFlowImage cf;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		ViewGroup cv=(ViewGroup) LayoutInflater.from(this).inflate(R.layout.coverflowimage_layout, null);
		setContentView(cv);

		cf = (CoverFlowImage) findViewById(R.id.Gallery01);
		cf.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(CoverFlowImageActivity.this, "position " + position,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				Toast.makeText(CoverFlowImageActivity.this, "none select",
						Toast.LENGTH_SHORT).show();
			}
		});

		// cf.setBackgroundResource(R.drawable.shape);

		RecentPaymentGalleryAdapter<String> layoutAdapter = new RecentPaymentGalleryAdapter<String>(this,cf);
		List <String>strs=new ArrayList<String>();//;
		for(int i=0;i<32;i++){
			strs.add(String.valueOf('A'+i));
		}
		layoutAdapter.setDatas(strs);
		cf.setAdapter(layoutAdapter);
//		cf.setTranslateModeX(true);
//		cf.setTranslateModeY(true);
//		cf.setTranslateModeZ(true);
//		cf.setRotationModeY(true);
//		cf.setSelection(2, true);
		// cf.setAnimationDuration(1000);

	}

}
class RecentPaymentGalleryAdapter <T>extends CoverFlowImageViewAdapter {

    int mGalleryItemBackground;

    List<T> accountList = new ArrayList<T>();

    int viewId;

    LayoutInflater lInflater;

    Context context;

    Handler handler;

    public RecentPaymentGalleryAdapter(Context c, CoverFlowImage coverFlow) {
        super(coverFlow);
        lInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = c;
        handler = new Handler();
    }

    public void setViewId(int id) {
        this.viewId = id;
    }

    public int getCount() {
    	if(accountList==null){
    		return 0;
    	}
        return accountList.size();
    }

    public float getScale(boolean focused, int offset) {
        return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset)));

    }

    @Override
    public View getView(int position) {

        if (accountList == null) {
            return null;
        }
        View vv=lInflater.inflate(R.layout.account_data_closed_item, null);
        TextView v = (TextView) vv.findViewById(R.id.accountname_tv);
        String mAccountsModel = (String) accountList.get(position);
        if (mAccountsModel != null) {
        	v.setText(mAccountsModel);
        }

        Gallery.LayoutParams lp = new Gallery.LayoutParams(50, 50);
        vv.setLayoutParams(lp);
        return vv;
    }

    /**
     * @param accountList Ҫ���õ� accountList
     */
    public void setDatas(List<T> accountList) {
        this.accountList = accountList;
    }

    @Override
    public List<T> getDatas() {
    	return accountList;
    }

}
