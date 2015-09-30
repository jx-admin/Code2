package j.wu.math;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import j.wu.utils.Log;

public class Formate {
	public static String generateMoney(double money) {
        NumberFormat nf2 = NumberFormat.getInstance(Locale.GERMAN);
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
       return nf2.format(money);

    }
	public static void test1(){ 
		   double i=2, j=2.1, k=2.5, m=2.9; 
		   System.out.println("舍掉小数取整:Math.floor(2)=" + (int)Math.floor(i)); 
		   System.out.println("舍掉小数取整:Math.floor(2.1)=" + (int)Math.floor(j)); 
		   System.out.println("舍掉小数取整:Math.floor(2.5)=" + (int)Math.floor(k)); 
		   System.out.println("舍掉小数取整:Math.floor(2.9)=" + (int)Math.floor(m)); 
		                                        
		   /* 这段被注释的代码不能正确的实现四舍五入取整 
		   System.out.println("四舍五入取整:Math.rint(2)=" + (int)Math.rint(i)); 
		   System.out.println("四舍五入取整:Math.rint(2.1)=" + (int)Math.rint(j)); 
		   System.out.println("四舍五入取整:Math.rint(2.5)=" + (int)Math.rint(k)); 
		   System.out.println("四舍五入取整:Math.rint(2.9)=" + (int)Math.rint(m)); 
		   
		   System.out.println("四舍五入取整:(2)=" + new DecimalFormat("0").format(i)); 
		   System.out.println("四舍五入取整:(2.1)=" + new DecimalFormat("0").format(i)); 
		   System.out.println("四舍五入取整:(2.5)=" + new DecimalFormat("0").format(i)); 
		   System.out.println("四舍五入取整:(2.9)=" + new DecimalFormat("0").format(i)); 
		   */ 
		   
		   System.out.println("四舍五入取整:(2)=" + new BigDecimal("2").setScale(2, BigDecimal.ROUND_HALF_UP)); 
		   System.out.println("四舍五入取整:(2.1)=" + new BigDecimal("2.1").setScale(2, BigDecimal.ROUND_HALF_UP)); 
		   System.out.println("四舍五入取整:(2.5)=" + new BigDecimal("2.5323").setScale(2, BigDecimal.ROUND_HALF_UP)); 
		   System.out.println("四舍五入取整:(2.9)=" + new BigDecimal("2.923").setScale(2, BigDecimal.ROUND_HALF_UP));
		 
		   System.out.println("凑整:Math.ceil(2)=" + (int)Math.ceil(i)); 
		   System.out.println("凑整:Math.ceil(2.1)=" + (int)Math.ceil(j)); 
		   System.out.println("凑整:Math.ceil(2.5)=" + (int)Math.ceil(k)); 
		   System.out.println("凑整:Math.ceil(2.9)=" + (int)Math.ceil(m));
		 
		   System.out.println("舍掉小数取整:Math.floor(-2)=" + (int)Math.floor(-i)); 
		   System.out.println("舍掉小数取整:Math.floor(-2.1)=" + (int)Math.floor(-j)); 
		   System.out.println("舍掉小数取整:Math.floor(-2.5)=" + (int)Math.floor(-k)); 
		   System.out.println("舍掉小数取整:Math.floor(-2.9)=" + (int)Math.floor(-m)); 
		   
		   System.out.println("四舍五入取整:(-2)=" + new BigDecimal("-2").setScale(0, BigDecimal.ROUND_HALF_UP)); 
		   System.out.println("四舍五入取整:(-2.1)=" + new BigDecimal("-2.1").setScale(0, BigDecimal.ROUND_HALF_UP)); 
		   System.out.println("四舍五入取整:(-2.5)=" + new BigDecimal("-2.5").setScale(0, BigDecimal.ROUND_HALF_UP)); 
		   System.out.println("四舍五入取整:(-2.9)=" + new BigDecimal("-2.9").setScale(0, BigDecimal.ROUND_HALF_UP));
		 
		   System.out.println("凑整:Math.ceil(-2)=" + (int)Math.ceil(-i)); 
		   System.out.println("凑整:Math.ceil(-2.1)=" + (int)Math.ceil(-j)); 
		   System.out.println("凑整:Math.ceil(-2.5)=" + (int)Math.ceil(-k)); 
		   System.out.println("凑整:Math.ceil(-2.9)=" + (int)Math.ceil(-m)); 
		   } 




	    public static String generateFormatMoneyInt(String currency, double money) {
	        if (money >= 0) {
	        	return "+"+generateMoney(cuL,money,0,0);
	        } 
	        	return generateMoney(cuL,money,0,0);
	    }
	 public static String generateMoney(Locale locale, double money,int minimumFractionDigits,int maxmumFractionDigits){
	        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
	        format.setMinimumFractionDigits(minimumFractionDigits);
	        format.setMaximumFractionDigits(maxmumFractionDigits);
	        return format.format(money);
	    }
	 
	 public static final Locale cuL=new Locale("id", "ID");
	 public static void test(){
		 Log.d("d", ""+generateMoney(- 123.456));
		 Log.d("d", ""+generateMoney( 123));

		 
		 Log.d("d", ""+generateMoney(new Locale("id", "ID"), -123.456,0,2));
		 Log.d("d", ""+generateMoney(Locale.US, 123,0,2));
		 
		 test1();

		 try {
		 double money=123456.123456;
		 Locale locale=Locale.FRANCE;
		 NumberFormat formatFrance = NumberFormat.getInstance(locale);
		 String strFrance=formatFrance.format(money);
		 Log.d("d", locale.getDisplayCountry()+" format "+strFrance);
		 Number number=formatFrance.parse(strFrance);
		 Log.d("d", locale.getDisplayCountry()+" parse "+number.doubleValue());
		 

		 locale=Locale.GERMANY;
		 formatFrance = NumberFormat.getInstance(locale);
		 strFrance=formatFrance.format(money);
		 Log.d("d", locale.getDisplayCountry()+" format "+strFrance);
		 number=formatFrance.parse(strFrance);
		 Log.d("d", locale.getDisplayCountry()+" parse "+number.doubleValue());
		 

		 locale=Locale.GERMANY;
		 formatFrance = NumberFormat.getInstance(locale);
		 strFrance=formatFrance.format(money);
		 Log.d("d", locale.getDisplayCountry()+" format "+strFrance);
		 number=formatFrance.parse(strFrance);
		 Log.d("d", locale.getDisplayCountry()+" parse "+number.doubleValue());

		 locale=Locale.US;
		 formatFrance = NumberFormat.getInstance(locale);
		 strFrance=formatFrance.format(money);
		 Log.d("d", locale.getDisplayCountry()+" format "+strFrance);
		 number=formatFrance.parse(strFrance);
		 Log.d("d", locale.getDisplayCountry()+" parse "+number.doubleValue());

		 locale=Locale.UK;
		 formatFrance = NumberFormat.getInstance(locale);
		 strFrance=formatFrance.format(money);
		 Log.d("d", locale.getDisplayCountry()+" format "+strFrance);
		 number=formatFrance.parse(strFrance);
		 Log.d("d", locale.getDisplayCountry()+" parse "+number.doubleValue());

		 locale=Locale.CHINA;
		 formatFrance = NumberFormat.getInstance(locale);
		 strFrance=formatFrance.format(money);
		 Log.d("d", locale.getDisplayCountry()+" format "+strFrance);
		 number=formatFrance.parse(strFrance);
		 Log.d("d", locale.getDisplayCountry()+" parse "+number.doubleValue());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
//		 format.setMinimumFractionDigits(0);
//			format.setMaximumFractionDigits(0);
//			return format.format(money);
	 }
}
