package wu.a.wuliu;

import wu.a.activity.ProgressDialogUtils;
import wu.a.activity.TitleFooterActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import baidumapsdk.demo.R;

public class FeedBackActivity extends TitleFooterActivity implements OnClickListener{
	private Handler mHandler=new Handler();
	
	public static void start(Context context){
		context.startActivity(new Intent(context,FeedBackActivity.class));
	}
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentLayout(R.layout.feedback);
		
		findViewById(R.id.commit_btn).setOnClickListener(this);
		setTitleText(R.string.feedback_title);
		setTitleLeftButtonImage(R.drawable.back);
		setBottomMenu(View.GONE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.commit_btn){
			EditText feedback_et=(EditText)findViewById(R.id.feedback_content_tv);
			if(feedback_et.getText().length()<=0){
				Toast.makeText(this, "内容为空,您可以填写反馈意见或退出页面！", Toast.LENGTH_SHORT).show();
			}else{
			ProgressDialogUtils.showProgressDialog(this, R.string.submiting);
			new Handler().postDelayed(new Runnable(){
				public void run(){
					((EditText)findViewById(R.id.feedback_content_tv)).setText("");
					ProgressDialogUtils.dismissProgressDialog();
				}
			}, 3000);
			}
		}
	}

}
