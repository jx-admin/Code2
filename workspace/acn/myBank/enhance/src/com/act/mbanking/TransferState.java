
package com.act.mbanking;

/**
 * 转账的各种状态
 * 
 * @author yang.c.li
 */
public class TransferState {
    public static final String E = "E";

    public static final String F = "F";

    public static final String R = "R";

    public static final String V = "V";

    public static final String A = "A";

    public static final String S = "S";

    public static final String C = "C";

    public static final String EXECUTED = "EXECUTED";

    public static final String FORWARDED = "FORWARDED";

    public static final String REJECTED = "REJECTED";

    public static final String REVOKED = "REVOKED";

    public static final String AUTHORIZED = "AUTHORIZED";

    public static final String SENT = "SENT";

    public static final String CANCELLED = "CANCELLED";

    public static String getTransferState(String state) {
        if (state == null || state.equals("")) {
            return null;
        } else if (state.equals(E)) {
            return EXECUTED;
        } else if (state.equals(F)) {
            return FORWARDED;
        } else if (state.equals(R)) {
            return REJECTED;
        } else if (state.equals(V)) {
            return REVOKED;
        } else if (state.equals(A)) {
            return AUTHORIZED;
        } else if (state.equals(S)) {
            return SENT;
        } else if (state.equals(C)) {
            return CANCELLED;
        }
        return null;
    }
}
