import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


public class FormateTest {
	public static void main(String args[]) throws ParseException{
		
		double mony1=-123456789.123456;
		NumberFormat nFormates[]=new NumberFormat[5];
		nFormates[0] = NumberFormat.getCurrencyInstance(Locale.ITALY);
		nFormates[1] = NumberFormat.getCurrencyInstance();
		nFormates[2] = NumberFormat.getCurrencyInstance(Locale.US);
		nFormates[3] = NumberFormat.getCurrencyInstance(Locale.JAPAN);
		for(int i=0;i<nFormates.length;i++){
			DecimalFormat f=(DecimalFormat) nFormates[i];
			if(f!=null){
				f.applyPattern("ก่");
				f.setMinimumFractionDigits(3);
				String str=f.format(mony1);
				Number num=f.parse(str);
				if(num instanceof Double){
					Double d=(Double) num;
					System.out.printf("%f Currency: %s  %s\n",d,f.getCurrency(), str);
				}else {
					long l=(Long) num;
					System.out.printf("%d Currency: %s  %s\n",l,f.getCurrency(), str);
				}

				
			}
		}

		
	}

}
