package wu.a.wuliu;

import baidumapsdk.demo.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class BookMain implements OnClickListener {
	
	private View view;
	private Context context;
	public BookMain(Context context){
		this.context=context;
		view=LayoutInflater.from(context).inflate(R.layout.book_main, null);
//		view.findViewById(R.id.feedback_tv).setOnClickListener(this);
		view.findViewById(R.id.cargo_goods).setOnClickListener(this);
		view.findViewById(R.id.cargo_home).setOnClickListener(this);
	}
	
	public View getView(){
		return view;
	}
	
	public String getTitle(){
		return "下单";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.feedback_tv:
//			context.startActivity(new Intent(context,FeedBackActivity.class));
//			break;
		case R.id.cargo_goods:
			context.startActivity(new Intent(context,CargoGoodsActivity.class));
			break;
		case R.id.cargo_home:
			context.startActivity(new Intent(context,CargoHomeActivity.class));
			
			break;

		default:
			break;
		}
	}

}
