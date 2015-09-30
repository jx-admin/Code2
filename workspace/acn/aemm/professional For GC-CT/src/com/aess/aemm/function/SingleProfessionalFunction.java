package com.aess.aemm.function;

import android.content.Context;

public class SingleProfessionalFunction {
    private static ProfessionalFunction pf = null;
    
	public static ProfessionalFunction GetProfessionalFunction(Context context)
     {
    	 if(pf == null){
    		 pf = new ProfessionalFunction(context);
    	 }
    	 return pf;
     }
}
