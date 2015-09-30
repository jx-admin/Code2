package cz.honzovysachy;

import cz.honzovysachy.resouces.S;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;




public class PromotionActivity extends ListActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(3);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));
        setTitle(S.g("PAWN_PROMOTION"));
        getListView().setTextFilterEnabled(true);
        getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				PromotionActivity.this.setResult(arg2);
				PromotionActivity.this.finish();
			}
        	
        });
    }
	private String[] mStrings = {S.g("KNIGHT"), S.g("BISHOP"), S.g("ROOK"), S.g("QUEEN")};
}
