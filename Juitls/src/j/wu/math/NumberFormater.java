package j.wu.math;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberFormater {
	private static NumberFormater mNumberFormater;
	private Locale locale;

	public static NumberFormater getInstance() {
		if (mNumberFormater == null) {
			mNumberFormater = new NumberFormater();
		}
		return mNumberFormater;
	}

	public static NumberFormater getInstance(Locale locale) {
		if (mNumberFormater == null) {
			mNumberFormater = new NumberFormater();
		}
		mNumberFormater.setLocale(locale);
		return mNumberFormater;
	}

	private NumberFormater() {
		locale = new Locale("id", "ID");
	}

	private NumberFormater(Locale locale) {
		this.locale = locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String numberFormat(double number, int minimumFractionDigits,
			int maxmumFractionDigits) {
		NumberFormat nf2 = NumberFormat.getInstance(locale);
		nf2.setMinimumFractionDigits(minimumFractionDigits);
		nf2.setMaximumFractionDigits(maxmumFractionDigits);
		return nf2.format(number);
	}

	public String moneyFormat(double money, int minimumFractionDigits,
			int maxmumFractionDigits) {
		NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		format.setMinimumFractionDigits(0);
		format.setMaximumFractionDigits(0);
		return format.format(money);
	}

	/**
	 * newScale scale of the result returned.
	 * 
	 * @param d
	 * @param newScale
	 * @return
	 */
	public static double setScale(double d, int newScale) {
		BigDecimal mBigDecimal = new BigDecimal(Double.toString(d));
		return mBigDecimal.setScale(newScale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}
	public double parse(String valuString) throws ParseException{
			 NumberFormat formatFrance = NumberFormat.getInstance(locale);
			 Number number=formatFrance.parse(valuString);
			 return number.doubleValue();
	}
}
