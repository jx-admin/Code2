package wu.a.lib.utils;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

/**
 * API for sending log output.
 *
 * This class wraps {@link android.util.Log}. The v(), d(), i(), w() and e() methods calls
 * the corresponding methods of Log, plus writing logs to a given file when a directory
 * {@link QiiGameLib#LOG_DIR} to save the log files is given.
 */
public final class Trace {
    private final static String TAG = "Trace";
    private static boolean DEBUG = true;


    /**
     * Trace用在多进程情况下，log文件名中加入进程名的hash加以区分
     */
    public static boolean MULTI_PROCESSES_TRACE;

    /**
     * Debug logging enabled or not.
     */
    public static String TAG_PREFIX = TAG;
    /**
     * Dir to save logs.
     */
    public static String LOG_DIR;

    /**
     * Package name of the app which uses this library.
     */
    public static String APP_PACKAGE_NAME;

    public static String APP_PROCESS_NAME;
    public static String APP_PROCESS_NAME_HASH_STRING;

    private static void setLoggingOptions(
            boolean enabled, String dir, String tagPrefix, boolean multiProcesses) {
        LOG_DIR = dir;
        TAG_PREFIX = tagPrefix;
        MULTI_PROCESSES_TRACE = multiProcesses;
        setLoggingEnabled(enabled);
    }
    public static void init(String packageName, boolean loggingEnabled
            , String logDir, String tagPrefix, boolean multiProcessesTrace) {
        APP_PACKAGE_NAME = packageName;
        APP_PROCESS_NAME = QlUtils.getProcessName();
        int hash = APP_PROCESS_NAME.hashCode();
        if (hash > 0) {
            APP_PROCESS_NAME_HASH_STRING = String.valueOf(hash) + "_";
        } else {
            APP_PROCESS_NAME_HASH_STRING = String.valueOf(Math.abs(hash)) + "__";
        }
        setLoggingOptions(loggingEnabled, logDir, tagPrefix, multiProcessesTrace);
    }

    public static void setLoggingEnabled(boolean enabled) {
    	DEBUG = DEBUG || enabled;
        Trace.i(TAG + "Lib"
                , "Tracing enabled? " + DEBUG + ", tag prefix: " + TAG_PREFIX
                + ", log dir: " + LOG_DIR + ' ' + APP_PROCESS_NAME);
        if (!enabled) {
            Trace.flush();
        }
    }
    private final static int MAX_LOG_COUNT_TRACE_ON = 150;
    private final static int MAX_LOG_COUNT_TRACE_OFF = 50;
    private final static long MILLIS_2_KEEP_LOGS = 1000 * 60 * 60 * 24 * 3;

    private final static int ALL_LOG_LEVEL = -1;

    private static Vector<String> sLogCache = new Vector<String>();

    private static final Date sDate = new Date();
    private static final DateFormat sDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");

    private static String sFilterTag;
    private static int sFilterLevel = ALL_LOG_LEVEL;

    private static long sLogFileId;

    final static String LOG_FILE_NAME = "%s%tm%td_%tH%tM%tS_%03d.txt";

    /**
     * Filter logs by tag
     * @param tag Only logs with this given tag is showed
     */
    public static void filter(String tag) {
        sFilterTag = tag;
    }

    /**
     * Filter logs by logging level
     * @param level Only logs with this given logging level is showed
     */
    public static void filter(int level) {
        sFilterLevel = level;
    }

    /**
     * Filter logs by logging level and tag
     */
    public static void filter(int level, String tag) {
        sFilterLevel = level;
        sFilterTag = tag;
    }

    /**
     * Do not filter logs by any means
     */
    public static void resetFilters() {
        sFilterLevel = ALL_LOG_LEVEL;
        sFilterTag = null;
    }

    /**
     * Send a {@link Log#VERBOSE} log message.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }

    /**
     * Send a {@link Log#VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param e An exception to log
     */
    public static void v(String tag, String msg, Throwable e) {
        Log.v(tag, msg, e);
        if (null != LOG_DIR) append(Log.VERBOSE, tag, msg, e);
    }

    /**
     * @deprecated Please use {@link #d(String, String)} or {@link #d(String, String, Throwable)}
     */
    public static void d(String msg) {
        d(TAG, msg, null);
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param e An exception to log
     */
    public static void d(String tag, String msg, Throwable e) {
        Log.d(tag, msg, e);
        if (null != LOG_DIR) append(Log.DEBUG, tag, msg, e);
    }

    /**
     * @deprecated Please use {@link #i(String, String)}
     */
    public static void i(String msg) {
        i(TAG, msg);
    }

    /**
     * Send a {@link Log#INFO} log message.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        Log.i(tag, msg);
        if (null != LOG_DIR) append(Log.INFO, tag, msg);
    }

    /**
     * Send a {@link Log#WARN} log message.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    /**
     * Send a {@link Log#WARN} log message.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param e An exception to log
     */
    public static void w(String tag, Throwable e) {
        w(tag, null, e);
    }

    /**
     * Send a {@link Log#WARN} log message.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param e An exception to log
     */
    public static void w(String tag, String msg, Throwable e) {
        Log.w(tag, msg, e);
        if (null != LOG_DIR) append(Log.WARN, tag, msg, e);
    }

    /**
     * @deprecated Please use {@link #e(String, String)} or {@link #e(String, String, Throwable)}
     */
    public static void e(String msg) {
        e(TAG, msg);
    }

    /**
     * @deprecated Please use {@link #e(String, String)} or {@link #e(String, String, Throwable)}
     */
    public static void e(Throwable e) {
        e(TAG, null, e);
    }

    /**
     * Send a {@link Log#ERROR} log message.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    /**
     * Send a {@link Log#ERROR} log message.
     * @param tag Used to identify the source of a log message. It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg, Throwable e) {
        Log.e(tag, msg, e);
        if (null != LOG_DIR) append(Log.ERROR, tag, msg, e);
    }

    /**
     * @deprecated Please use {@link #e(String, String)} or {@link #e(String, String, Throwable)}
     */
    public static void ep(String msg) {
        e(msg);
    }

    private static void append(int level, String tag, String msg) {
        append(level, tag, msg, null);
    }

    private static void append(int level, String tag, String msg, Throwable e) {
        if ((null != sFilterTag && !sFilterTag.equals(tag))
                || (ALL_LOG_LEVEL != sFilterLevel && sFilterLevel != level)) {
            return;
        }

        sDate.setTime(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder(sDateFormat.format(sDate));
        sb.append(' ');
        switch (level) {
            case Log.VERBOSE:
                sb.append('V');
                break;
            case Log.WARN:
                sb.append('W');
                break;
            case Log.ERROR:
                sb.append('E');
                break;
            case Log.DEBUG:
                sb.append('D');
                break;
            case Log.INFO:
                sb.append('I');
                break;
            default:
                sb.append('O');
        }
        sb.append('/').append(tag).append('(')
                .append(android.os.Process.myPid()).append("): ");
        if (!TextUtils.isEmpty(msg)) sb.append(msg);
        if (null != e) sb.append(Log.getStackTraceString(e));
        sb.append("\r\n");

        sLogCache.add(sb.toString());

        if (sLogCache.size()
                >= (DEBUG ? MAX_LOG_COUNT_TRACE_ON : MAX_LOG_COUNT_TRACE_OFF)) {
            flush();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    static void flushLocked(Vector<String> logs) {
        OutputStream bos = null;
        try {
            if (!Storage.isOnSdcard(LOG_DIR) || Storage.isSdcardMounted()) {
                File dir = new File(LOG_DIR);
                if (dir.exists()) {
                    if (!dir.isDirectory()) {
                        dir.delete();
                        dir.mkdirs();
                    }
                } else {
                    dir.mkdirs();
                }
                if (++sLogFileId >= 1000) {
                    sLogFileId = 1;
                }

                long t = System.currentTimeMillis();
                String fileName = String.format(LOG_FILE_NAME
                        , (MULTI_PROCESSES_TRACE ? APP_PROCESS_NAME_HASH_STRING : "")
                        , t, t, t, t, t, sLogFileId);

                File file = new File(dir, fileName);
                if (!file.exists()) file.createNewFile();
                bos = new BufferedOutputStream(new FileOutputStream(file));
                for (String s : logs) {
                    bos.write(s.getBytes());
                }
            }
        } catch (Throwable e) {
            Log.w(TAG, "Failed to save cached logs", e);
        } finally {
            if (null != bos) close(bos);
        }
        logs.clear();
    }

    private static boolean sIsCleaning;

    /**
     * Remove all log files created 3 days ago synchronously.
     */
    public static void cleanUpLocked() {
        if (sIsCleaning) {
            if (DEBUG) {
                Log.d(TAG, "Cleaning up is on progress");
            }
            return;
        }

        sIsCleaning = true;

        int n = 0;
        long now = System.currentTimeMillis();

        Log.i(TAG, "Start removing aged logs in " + LOG_DIR);

        try {
            if (TextUtils.isEmpty(LOG_DIR)) {
                if (DEBUG) {
                    Log.d(TAG, "Not cleaning up logs as giving path is null");
                }
                return;
            }

            if (Storage.isOnSdcard(LOG_DIR) && !Storage.isSdcardMounted()) {
                if (DEBUG) {
                    Log.d(TAG, "Not cleaning up logs as SDCard is not mounted");
                }
                return;
            }

            File file = new File(LOG_DIR);
            if (!file.exists() || !file.isDirectory()) {
                if (DEBUG) {
                    Log.d(TAG, "The specified log path does not exist? It's not a directory?");
                }
                return;
            }

            File[] files = file.listFiles();
            Comparator<File> fileComparator;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fileComparator = new Comparator<File>() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    public int compare(File f1, File f2) {
                        return Long.compare(f1.lastModified(), f2.lastModified());
                    }
                };
            } else {
                fileComparator = new Comparator<File>() {
                    public int compare(File f1, File f2) {
                        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                    }
                };
            }
            boolean sorted;
            try {
                // java.lang.NullPointerException
                //     at java.util.TimSort.sort(TimSort.java:169)
                //     at java.util.Arrays.sort(Arrays.java:2038)
                Arrays.sort(files, fileComparator);
                sorted = true;
            } catch (Throwable e) {
                sorted = false;
            }

            for (File f : files) {
                if (!f.exists()) continue;
                if (now - f.lastModified() >= MILLIS_2_KEEP_LOGS) {
                    f.delete();
                    n++;
                    if (DEBUG) {
                        Log.d(TAG, "Removed: " + f.getName());
                    }
                } else if (sorted) {
                    break;
                }
            }
        } catch (Throwable ignored) {
        } finally {
            sIsCleaning = false;

            Log.i(TAG, "Removed " + n + " aged logs in "
                    + (System.currentTimeMillis() - now) + " milliseconds");
        }
    }

    /**
     * @deprecated Please use {@link #cleanUp}
     */
    public static void clearLog() {
        cleanUp();
    }

    /**
     * Remove all log files created 3 days ago asynchronously.
     */
    public static void cleanUp() {
        new Thread() {
            @Override
            public void run() {
                cleanUpLocked();
            }
        }.getState();
    }

    public static void flush() {
        if (sLogCache.size() <= 0) return;

        final Vector<String> log2Save = (Vector<String>) sLogCache.clone();
        sLogCache.clear();
        new Thread (){
            @Override
            public void run() {
                flushLocked(log2Save);
            }
        }.start();
    }

    public static void flushLocked() {
        flushLocked(sLogCache);
    }
    
    /**
     * Close a closeable quietly.
    *
    * Caller must check if stream is null.
    *
    */
   public static void close(Closeable stream) {
       try {
           stream.close();
       } catch (Exception ex){
       } catch (Throwable ignored) {
       }
   }
}
