package wu.a.lib.view;

import wu.a.lib.view.CustomEditText.OnKeyPreIme;
import wu.a.template.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditViewTestActivity extends Activity implements OnKeyPreIme{

	CustomEditText mCustomEditText;
	CustomEditText mCustomEditText2;
	CustomEditText mCustomEditText3;
	TextView tv_output;
	StringBuffer outSb=new StringBuffer();
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittext_test);
		tv_output=(TextView) findViewById(R.id.tv_output);
		mCustomEditText=(CustomEditText) findViewById(R.id.customEditText_test);
		mCustomEditText.setOnKeyPreIme(this);
		mCustomEditText.setFilters(new InputFilter[] {
                new AmountItalyInputFilter()
            });
		mCustomEditText.setOnEditorActionListener(new OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (event == null) {
                    	printLog("onEditorAction is null ,onEditorAction " + actionId);
                    } else {
                    	printLog( "onEditorAction:onEditorAction" + actionId + "key:" + event.getKeyCode());
                    }
//                    checkAmountView();
                    return false;
                }
            });

		mCustomEditText2=(CustomEditText) findViewById(R.id.customEditText_test2);
		mCustomEditText3=(CustomEditText) findViewById(R.id.customEditText_test3);
		mCustomEditText3.setFilters(new InputFilter[] {
                new AmountItalyInputFilter()
        });
	}
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		printLog("\nkey:"+keyCode);
		return false;
	}
	
	
	private void printLog(String text){
		outSb.append(text);
		tv_output.setText(outSb);
	}
	
	/**Amount InputFilter for Italy
	 * @author junxu.wang
	 *
	 */
	public class AmountItalyInputFilter  implements InputFilter {
		public static final String TAG="AmountItalyInputFilter";
		String ch = null;
		public static final String EMPYTY="",COMMA=",";

		
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			
			printLog(source+" s"+start+"-"+end+" d:"+dest+" ds"+dstart+"-"+dend);
			
			String s=source.toString();
//			for(int i=0;i<s.length();i++){
//				int c=s.charAt(i);
//				if(c!=','&&(c<'0'||c>'9')){
//					return "";
//				}
//			}
			int sourceCommaIndex=s.indexOf(',');
			if(sourceCommaIndex>=0){
				int destCommaIndex=dest.toString().indexOf(',');
				if(destCommaIndex>=0){
					s=s.replace(COMMA, EMPYTY);//delete all ',',because of ',' is alread in dest.
				}else{
					int sourceCommaIndex2=s.lastIndexOf(',');
					if(sourceCommaIndex2!=sourceCommaIndex){//more one ',' in source 
						s=s.replace(COMMA, EMPYTY);
					}
				}
			}
			source=s;
			
//			if(dstart>0){
//				s=dest.subSequence(0, dstart).toString()+s;
//			}
//			if(dend<dest.length()-1){
//				s+=dest.subSequence(dend, dest.length());
//			}
			printLog(" result:"+source+"\n");
			
			return source;

		}

	}
}
