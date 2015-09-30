package cindy.android.test.synclistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadStateView extends RelativeLayout{
	
	ProgressBar progBar;
	
	LinearLayout downLoadErrMsgBox;
	
	TextView downLoadErrText;
	
	Button btnListLoadErr;

	public LoadStateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		progBar = (ProgressBar) findViewById(R.id.progBar);
		downLoadErrMsgBox = (LinearLayout) findViewById(R.id.downLoadErrMsgBox);
		downLoadErrText = (TextView) findViewById(R.id.downLoadErrText);
		btnListLoadErr = (Button) findViewById(R.id.btnListLoadErr);
	}

	public void startLoad(){
		downLoadErrMsgBox.setVisibility(View.GONE);
		progBar.setVisibility(View.VISIBLE);
	}
	
	public void stopLoad(){
		progBar.setVisibility(View.GONE);
	}
	
	public void showError(){
		downLoadErrMsgBox.setVisibility(View.VISIBLE);
		progBar.setVisibility(View.GONE);
	}
	
	public void showEmpty(){
		downLoadErrMsgBox.setVisibility(View.VISIBLE);
		progBar.setVisibility(View.GONE);
	}
	
	public void setOnReloadClickListener(OnClickListener onReloadClickListener){
		btnListLoadErr.setOnClickListener(onReloadClickListener);
	}
}
