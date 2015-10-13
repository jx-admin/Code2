package wu.a.lib.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.util.Log;

public final class Shell {
    private final static String TAG = "Shell";
    private final static boolean DEBUG = true;

    public static final String COMMAND_SU = "su";
    public static final String COMMAND_SH = "sh";
    public static final String COMMAND_EXIT = "exit\n";
    public static final String COMMAND_LINE_END = "\n";

    /**
     * Check whether we have the root permission
     */
    public static boolean checkRootPermission() {
        return exec("echo root", true, false).result == 0;
    }

    /**
     * Execute a shell command, default return result msg
     *
     * @param command command
     * @param requireRoot whether need to run with root
     * @return
     * @see Shell#exec(String[], boolean, boolean)
     */
    public static CmdOutput exec(String command, boolean requireRoot) {
        return exec(new String[]{command}, requireRoot, true);
    }

    /**
     * Execute shell commands, default return result msg
     *
     * @param commands command list
     * @param requireRoot whether need to run with root
     * @return
     * @see Shell#exec(String[], boolean, boolean)
     */
    public static CmdOutput exec(List<String> commands, boolean requireRoot) {
        return exec(commands == null
                ? null : commands.toArray(new String[]{}), requireRoot, true);
    }

    /**
     * Execute shell commands, default return result msg
     *
     * @param commands command array
     * @param requireRoot whether need to run with root
     * @return
     * @see Shell#exec(String[], boolean, boolean)
     */
    public static CmdOutput exec(String[] commands, boolean requireRoot) {
        return exec(commands, requireRoot, true);
    }

    /**
     * Execute a shell command
     *
     * @param command command
     * @param requireRoot whether need to run with root
     * @param needResult whether need result msg
     * @return
     * @see Shell#exec(String[], boolean, boolean)
     */
    public static CmdOutput exec(String command, boolean requireRoot, boolean needResult) {
        return exec(new String[]{command}, requireRoot, needResult);
    }

    /**
     * Execute shell commands
     *
     * @param commands command list
     * @param requireRoot whether need to run with root
     * @param needResult whether need result msg
     * @return
     * @see Shell#exec(String[], boolean, boolean)
     */
    public static CmdOutput exec(List<String> commands, boolean requireRoot, boolean needResult) {
        return exec(commands == null
                ? null : commands.toArray(new String[]{}), requireRoot, needResult);
    }

    /**
     * Execute shell commands
     *
     * @param commands command array
     * @param requireRoot whether need to run with root
     * @param needResult whether need result msg
     * @return <ul>
     * <li>if needResult is false, {@link com.qiigame.lib.util.Shell.CmdOutput#successMsg} is null
     * and {@link com.qiigame.lib.util.Shell.CmdOutput#errorMsg} is null.</li>
     * <li>if {@link com.qiigame.lib.util.Shell.CmdOutput#result} is -1, there maybe some exception.</li>
     * </ul>
     */
    public static CmdOutput exec(String[] commands, boolean requireRoot, boolean needResult) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CmdOutput(result, null, null);
        }

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;

        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(requireRoot ? COMMAND_SU : COMMAND_SH);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) {
                    continue;
                }

                os.write(command.getBytes());
                os.writeBytes(COMMAND_LINE_END);
                os.flush();
            }
            os.writeBytes(COMMAND_EXIT);
            os.flush();

            result = process.waitFor();

            if (needResult) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String s;
                while ((s = successResult.readLine()) != null) {
                    if (DEBUG) {
                        Log.d(TAG, "OUTPUT->" + s);
                    }
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    Trace.w(TAG, "ERROR->" + s);
                    errorMsg.append(s);
                }
            }
        } catch (Throwable e) {
            Trace.w(TAG, "Executing commands failed: "
                    + QlUtils.joinStringArray('[', commands, ';', ']'), e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (process != null) {
                process.destroy();
            }
        }
        return new CmdOutput(result
                , successMsg == null ? null : successMsg.toString()
                , errorMsg == null ? null : errorMsg.toString());
    }

    /**
     * Command output
     * <ul>
     * <li>{@link com.qiigame.lib.util.Shell.CmdOutput#result} means result of command,
      * 0 means normal, else means error, same to execute in linux shell</li>
     * <li>{@link com.qiigame.lib.util.Shell.CmdOutput#successMsg} means success message of command result</li>
     * <li>{@link com.qiigame.lib.util.Shell.CmdOutput#errorMsg} means error message of command result</li>
     * </ul>
     */
    public static class CmdOutput {
        /** result of command **/
        public int result;
        /** success message of command output **/
        public String successMsg;
        /** error message of command output **/
        public String errorMsg;

        public boolean success;

        public CmdOutput(int result){
            this.result = result;
            success = 0 == result;
        }

        public CmdOutput(int result, String successMsg, String errorMsg){
            this(result);
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }
}
