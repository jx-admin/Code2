
package com.accenture.mbank.util;

public class DestProvider {
    public static final String Tim = "10013";

    public static final String Vodafone = "10015";

    public static final String Tiscali = "10017";

    public static final String Wind = "10019";

    public static final String Tre = "10020";
    
    
    public static final String _TIM = "Tim";
    
    public static final String _VODAFONE = "Vodafone";
    
    public static final String _TISCALI = "Tiscali";
    
    public static final String _WIND = "Wind";
    
    public static final String _TRE = "Tre";
    
    
    public static String getDsstProvider(String provider){
        if (provider == null || provider.equals("")) {
            return null;
        }else if(provider.equals(Tim)){
            return _TIM;
        }else if(provider.equals(Vodafone)){
            return _VODAFONE;
        }else if(provider.equals(Tiscali)){
            return _TISCALI;
        }else if(provider.equals(Wind)){
            return _WIND;
        }else if(provider.equals(Tre)){
            return _TRE;
        }
        return null;
    }
}
