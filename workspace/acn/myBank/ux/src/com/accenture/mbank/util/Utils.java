
package com.accenture.mbank.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.util.EncodingUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.accenture.mbank.model.PushNotificationValue;
import com.accenture.mbank.model.SettingModel;

/**
 */
public class Utils {
    public static String months[] = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
    public static Bitmap getBitmap(Context context, int id) {
        Drawable drawable = context.getResources().getDrawable(id);
        BitmapDrawable bitmap = (BitmapDrawable)drawable;
        return bitmap.getBitmap();
    }

    public static String getDateLastMonth(int transactionBy) {
        Date d = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gc.setTime(d);
        gc.add(2, -2);
        gc.set(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DATE));

        if (transactionBy == SettingModel.LAST_20) {
            return sf.format(d);
        }
        return sf.format(gc.getTime());
    }

    public static String isStrNull(String str) {
        if (str == null || str.equals("")) {
            return "";
        } else {
            return str;
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private static SimpleDateFormat smf2 = new SimpleDateFormat("yyyy-MM-dd"); // 用于格式化成

    // 年月日
    public static Date stringToDate(String str) { // 字符串转换成日期(包括年月日)
        Date dt = null;
        try {
            dt = smf2.parse(str);
            return dt;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToStrng(Date dt) { // 日期(包括年月日时分秒)转换成字符串
        String sdate = smf2.format(dt);
        System.out.println(sdate);
        return sdate;
    }

    /**
     * dateStr = "2012-12-13 12:33:56"
     * 
     * @param dateStr
     * @return
     */
    public static String formatDate(String dateStr) {
        Date date = Utils.stringToDate(dateStr);
        String sDate = Utils.dateToStrng(date);
        return sDate;
    }

    public static String generateMoneyInt(double money) {
        // 1,000.00
        // 1.000,00
        String value = String.format(Locale.US, "%.0f", money);
        NumberFormat nf2 = NumberFormat.getInstance(Locale.GERMAN);
        double dd = Double.parseDouble(value);
        String str = nf2.format(dd);

        // if (str.indexOf(",") != -1) {
        //
        // } else {
        // str += ",00";
        // }
        return str;
    }

    public static String generateMoney(double money) {
        // 1,000.00
        // 1.000,00
        String value = String.format(Locale.US, "%.2f", money);
        NumberFormat nf2 = NumberFormat.getInstance(Locale.GERMAN);
        double dd = Double.parseDouble(value);
        String str = nf2.format(dd);

        if (str.indexOf(",") != -1) {
            String ss = str.substring(str.length() - 2, str.length());
            if (ss.indexOf(",") != -1) {
                str += "0";
            }
        } else {
            str += ",00";
        }
        return str;
    }

    public static String notDecimalGenerateMoney(String currency, double money) {
        String value = String.format(Locale.US, "%.2f", money);
        NumberFormat nf2 = NumberFormat.getInstance(Locale.GERMAN);
        double dd = Double.parseDouble(value);
        String str = nf2.format(dd);
        str = currency + str;
        return str;
    }

    public static String notPlusGenerateFormatMoneyInt(String currency, double money) {
        String sign;
        if (money >= 0) {
            sign = "";
        } else {
            sign = "-";
        }

        String moneyValue = generateMoneyInt(money);
        if (moneyValue.indexOf("-") != -1) {
            moneyValue = moneyValue.substring(1, moneyValue.length());
        }

        if (moneyValue == null || moneyValue.length() < 1) {
            return "";
        }

        moneyValue = sign + currency + moneyValue;
        return moneyValue;
    }

    /**
     * 将money转成2位小数
     * 
     * @param money
     * @return
     */
    public static double changeMoney(double money) {
        // 1,000.00
        // 1.000,00
        String value = String.format(Locale.US, "%.2f", money);
        double dd = Double.parseDouble(value);
        return dd;
    }

    public static String generateFormatMoney(String currency, double money) {
        String sign;
        if (money >= 0) {
            sign = "+";
        } else {
            sign = "-";
        }

        String moneyValue = generateMoney(money);
        if (moneyValue.indexOf("-") != -1) {
            moneyValue = moneyValue.substring(1, moneyValue.length());
        }

        if (moneyValue == null || moneyValue.length() < 1) {
            return "";
        }

        moneyValue = sign + currency + moneyValue;
        return moneyValue;
    }

    public static String generateFormatMoneyInt(String currency, double money) {
        String sign;
        if (money >= 0) {
            sign = "+";
        } else {
            sign = "-";
        }

        String moneyValue = generateMoneyInt(money);
        if (moneyValue.indexOf("-") != -1) {
            moneyValue = moneyValue.substring(1, moneyValue.length());
        }

        if (moneyValue == null || moneyValue.length() < 1) {
            return "";
        }

        moneyValue = sign + currency + moneyValue;
        return moneyValue;
    }

    public static String notPlusGenerateFormatMoney(String currency, double money) {
        String sign;
        if (money >= 0) {
            sign = "";
        } else {
            sign = "-";
        }

        String moneyValue = generateMoney(money);
        if (moneyValue.indexOf("-") != -1) {
            moneyValue = moneyValue.substring(1, moneyValue.length());
        }

        if (moneyValue == null || moneyValue.length() < 1) {
            return "";
        }

        moneyValue = sign + currency + " " + moneyValue;
        return moneyValue;
    }

    public static String notPlusGenerateFormatMoneyItanLocal(String currency, String money) {
        if (money.length() == 1 && money.charAt(0) == 46) {
            money = "0.0";
        }
        if (money.charAt(money.length() - 1) == '.') {
            money = money.substring(0, money.length() - 1);
        }

        double m = Double.parseDouble(money);
        String moneyValue = notPlusGenerateFormatMoney(currency, m);
        return moneyValue;
    }

    public static String notPlusGenerateFormatMoney(String currency, String money) {
        if (money.length() == 1 && money.charAt(0) == 46) {
            money = "0.0";
        }

        double m = Double.parseDouble(money);
        String moneyValue = notPlusGenerateFormatMoney(currency, m);
        return moneyValue;
    }

    public static String generateFormatMoney(String currency, String money) {
        if (money.length() == 1 && money.charAt(0) == 46) {
            money = "0.0";
        }

        double m = Double.parseDouble(money);
        String moneyValue = generateFormatMoney(currency, m);
        return moneyValue;
    }

    /**
     * 金额格式化
     * 
     * @param s 金额
     * @param len 小数位数
     * @return 格式后的金额 String str = "100000.2345"; str =
     *         Utils.insertComma("100000.23",str.length());
     */
    public static String insertComma(String s, int len) {

        if (s == null || s.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###.###");
        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###.###,");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        return formater.format(num);
    }

    public static void isHasFile_Del(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean isHasFile(String path) {
        File file = new File(path);
        if (file.exists())
            return true;
        else
            return false;
    }

    /**
     * �ж��Ƿ���SD��
     * 
     * @return
     */
    public static boolean isSDCardExists() {
        return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
    }

    /*
     * public static String getSDCARD(Context context) { if
     * (Environment.getExternalStorageState().equals(
     * Environment.MEDIA_MOUNTED)) { String path =
     * Environment.getExternalStorageDirectory().getPath() + "/." +
     * ADDataManager.GetDataManager(context).Get_AppID() + "_" +
     * DeviceInfo.getIMEI(context); File f = new File(path); if (!f.exists()) {
     * f.mkdirs(); Log.i("crate image cache dir", "" + f.getAbsolutePath()); }
     * return path; } else { return null; } }
     */

    public static InputStream getRes(Context context, String fileName) {
        InputStream is;
        if (fileName == null)
            return null;
        else
            return is = Utils.class.getClassLoader().getResourceAsStream("res/" + fileName);// עRESӦΪSRC���½����ļ���
    }

    public static void writeFileData(String fileName, String message, Context context) {
        try {
            FileOutputStream fout = context.openFileOutput(fileName, context.MODE_PRIVATE);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ���ļ���./data/data/com.tt/files/����

    public String readFileData(String fileName, Context context) {

        String res = "";
        try {
            FileInputStream fin = context.openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;

    }

    public static String readAssets(Context context, String fileName) {
        String str = null;
        try {
            str = Utils.convertStreamToString(context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @param is
     * @return
     * @throws IOException
     */
    public static String convertStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (is != null) {
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
        }
        return sb.toString();
    }

    /**
     * StringתInputStream
     * 
     * @param str
     * @return
     */
    public static InputStream StringToInputStream(String str) {
        InputStream in = new ByteArrayInputStream(str.getBytes());
        return in;
    }

    public static float measureX(Paint paint, String text, double width) {
        float textLenght;
        float x;
        textLenght = paint.measureText(text);
        x = (float)((width - textLenght) / 2f);
        return x;
    }

    public static String getIMEI(Context context) {
        String imei = ((TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE))
                .getDeviceId();
        return imei;
    }

    public static PushNotificationValue cutString(String str) {
        PushNotificationValue value = new PushNotificationValue();
        String title = null;
        String message = null;
        if (str != null && str.contains("|")) {
            int j = str.indexOf("|");
            title = str.substring(0, j);
            message = str.substring(title.length() + 1);
        }

        value.setTitle(title);
        value.setMessage(message);
        return value;
    }
    public static boolean isTopActivity(Context activity) {
    	ActivityManager activityManager = (ActivityManager) activity
    	.getSystemService(Context.ACTIVITY_SERVICE);
    	List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
    	if (tasksInfo.size() > 0) {
    		// 应用程序位于堆栈的顶层
    		 return activity.getApplicationInfo().packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
//    		Log.d(TAG,"---------------包名-----------"
//    				+ tasksInfo.get(0).topActivity.getPackageName());
    	}
    	return false;
    	}
}
