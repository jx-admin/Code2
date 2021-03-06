
package com.act.mbanking.utils;

import static com.act.mbanking.CommonUtilities.SENDER_ID;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.act.mbanking.Contants;
import com.act.mbanking.bean.DashboardDataModel;
import com.act.mbanking.bean.PushNotificationValue;
import com.act.mbanking.bean.SettingModel;
import com.google.android.gcm.GCMRegistrar;

/**
 */
public class Utils {
    public static String months[] = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public static void registerGCM(Context context){
        GCMRegistrar.checkDevice(context);
        GCMRegistrar.checkManifest(context);
        final String regId = GCMRegistrar.getRegistrationId(context);
        if (regId.equals("")) {
            GCMRegistrar.register(context, SENDER_ID);
        } else {
            Log.v("Test GCM", "Already registered" + regId);
        }
    }
    
    public static boolean isInitRegisterPushNotification(Context context){
        SharedPreferences sp = context.getSharedPreferences(Contants.SETTING_FILE_NAME,
                context.MODE_PRIVATE);
        return Contants.getInitSetting(sp);
    }
    
    /**
     * 获取指定月的前一月（年）或后一月（年）
     * 
     * @param dateStr
     * @param addYear
     * @param addMonth
     * @param addDate
     * @return 输入的时期格式为yyyy-MM，输出的日期格式为yyyy-MM
     * @throws Exception
     */
    public static String getLastMonth(String dateStr, int addYear, int addMonth, int addDate) {
        String dateTmp = null;
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date sourceDate = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sourceDate);
            cal.add(Calendar.YEAR, addYear);
            cal.add(Calendar.MONTH, addMonth);
            cal.add(Calendar.DATE, addDate);

            java.text.SimpleDateFormat returnSdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            dateTmp = returnSdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTmp;
    }

    public static String getIMEI(Context context) {
        String imei = ((TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE))
                .getDeviceId();
        return imei;
    }

    public static boolean compareMonth(String selectTime, String lastUpdateTime) {
        Date d = null;
        Date d1 = null;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            d = df.parse(lastUpdateTime);
            d1 = df.parse(selectTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        c.setTime(d1);
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);

        if (year == year1 && month == month1) {
            return true;
        } else {
            return false;
        }
    }

    public static int getDateIndex(List<DashboardDataModel> dashboardData, long dstDate) {
        long m = 0;
        int index = 0;
        for (int i = 0; i < dashboardData.size(); i++) {
            String str = dashboardData.get(i).getLastUpdate();
            // String _dstDate = TimeUtil.getDateString(dstDate,
            // TimeUtil.dateFormat2);
            long nowM = Math.abs(dstDate - TimeUtil.getMillis(str));
            if (i == 0 || nowM < m) {
                m = nowM;
                index = i;
                continue;
            }
        }
        if (index > 0) {
            index = index - 1;
        }
        return index;
    }

    public static String getPerecntages(double divisor, double dividend) {
        double result = (divisor / dividend) * 100;
        if(Double.isNaN(result)){
            result = 0;
        }
        String value = String.format(Locale.US, "%.0f", result);
        value += "%";
        return value;
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

    public static Bitmap getBitmap(Context context, int id) {
        Drawable drawable = context.getResources().getDrawable(id);
        BitmapDrawable bitmap = (BitmapDrawable)drawable;
        return bitmap.getBitmap();
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

    /**
     * @param isSign
     * @param amount
     * @param currency
     * @return
     */
    public static String formatAmount(boolean isSign, double amount, String currency) {
        String sign = "";
        if (isSign) {
            if (amount >= 0) {
                sign = "+";
            } else {
                sign = "-";
            }
        } else {
            if (amount < 0) {
                sign = "-";
            }
        }

        String moneyValue = generateMoney(amount);
        if (moneyValue.indexOf("-") != -1) {
            moneyValue = moneyValue.substring(1, moneyValue.length());
        }

        if (moneyValue == null || moneyValue.length() < 1) {
            return "";
        }

        if (moneyValue.equals("n/a")) {
            return moneyValue;
        }
        return sign + moneyValue + currency;
    }

    /**
     * 格式化金钱.无保留小数
     * 
     * @param amount
     * @param currency
     * @return
     */
    public static String formatAmount(double amount, String currency) {
        int dd = (int)amount;
        // String value = String.format(Locale.US, "%.2f", amount);
        NumberFormat nf2 = NumberFormat.getInstance(Locale.GERMAN);
        // int dd = Integer.parseInt(value);
        String str = nf2.format(dd);
        str += currency;
        return str;
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

        } else if (str.equals("NaN")) {
            str = "n/a";
        } else {
            str += ",00";
        }

        String aa = str.substring(str.indexOf(',') + 1);
        int bb = aa.length();
        if (bb < 2 && bb == 1) {
            str += "0";
        }
        return str;
    }

    /**
     * 
     * @param money 金钱
     * @param currency 货币符号
     * @param cound 是否保留小数
     * @param currencyIsBeforeMoney 货币符号的位置是在钱的前面
     * @param formatLocaleMoney 是否 1,000.00格式化为1.000,00
     * @param isPlus 是否有正负号
     * @param isInteger
     * @return
     */
    public static String formatMoney(double money, String currency, boolean isCound,
            boolean currencyIsBeforeMoney, boolean formatLocaleMoney, boolean isPlus,
            boolean isInteger) {
        String sign = "";
        String value;
        if (isPlus) {
            if (money >= 0) {
                sign = "+";
            }
        } else {
            if (money >= 0) {
                sign = "";
            }
        }

        if (isCound) {
            value = String.format(Locale.US, "%.2f", money);
        } else {
            if (isInteger) {
                int aa = (int)money;
                value = aa + "";
            } else {
                value = money + "";
            }
        }

        double dd;
        String str;
        if (formatLocaleMoney) {
            NumberFormat nf2 = NumberFormat.getInstance(Locale.GERMAN);
            dd = Double.parseDouble(value);
            str = nf2.format(dd);
            if (str.indexOf(",") != -1) {

            } else if (str.equals("NaN")) {
                str = "n/a";
                return str;
            } else {
                str += ",00";
            }

            String aa = str.substring(str.indexOf(',') + 1);
            int bb = aa.length();
            if (bb < 2 && bb == 1) {
                str += "0";
            }
        } else {
            NumberFormat nf2 = NumberFormat.getInstance(Locale.US);
            dd = Double.parseDouble(value);
            str = nf2.format(dd);
            if (str.indexOf(".") != -1) {

            } else if (str.equals("NaN")) {
                str = "n/a";
                return str;
            } else {
                str += ".00";
            }
            
            String aa = str.substring(str.indexOf('.') + 1);
            int bb = aa.length();
            if (bb < 2 && bb == 1) {
                str += "0";
            }
        }
        
        if (currencyIsBeforeMoney) {
            str = currency + " " + sign + str;
        } else {
            str = sign + str + currency;
        }

        return str;
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

}
