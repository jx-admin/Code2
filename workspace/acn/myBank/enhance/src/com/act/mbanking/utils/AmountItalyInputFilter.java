package com.act.mbanking.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

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
		
		Log.d(TAG,"source:"+source+" start:"+start+" end:"+end+" dest:"+dest+" dstart:"+dstart+" dend:"+dend);
		
		String s=source.toString();
//		for(int i=0;i<s.length();i++){
//			int c=s.charAt(i);
//			if(c!=','&&(c<'0'||c>'9')){
//				return "";
//			}
//		}
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
		
//		if(dstart>0){
//			s=dest.subSequence(0, dstart).toString()+s;
//		}
//		if(dend<dest.length()-1){
//			s+=dest.subSequence(dend, dest.length());
//		}
		Log.d(TAG,"result:"+source);
		
		return source;

	}

}

