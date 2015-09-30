
package com.accenture.mbank.util;

import it.gruppobper.ams.android.bper.R;
import android.content.Context;

public class TransferState {
    public static final String E = "E";

    public static final String F = "F";

    public static final String RR = "R";

    public static final String V = "V";

    public static final String A = "A";

    public static final String S = "S";

    public static final String C = "C";

    public static final String I = "I";

    public static final String X = "X";

    public static final String EXECUTED = "EXECUTED";

    public static final String FORWARDED = "FORWARDED";

    public static final String REJECTED = "REJECTED";

    public static final String REVOKED = "REVOKED";

    public static final String AUTHORIZED = "AUTHORIZED";

    public static final String SENT = "SENT";

    public static final String CANCELLED = "CANCELLED";

    public static final String SUBMITTED = "SUBMITTED";

    public static final String VOID = "VOID";

	public static String getTransferState(Context context, String state) {
		if (state == null || context == null)
			return null;
		String[] keys = context.getResources().getStringArray(
				R.array.recent_payment_state_key);
		String[] values = context.getResources().getStringArray(
				R.array.recent_payment_state_value);

		String ret = state;
		for (int i = 0; i < keys.length; i++) {
			if (keys[i].equals(state))
				ret = values[i];
		}
		return ret;
	}

    public static String getTransferState(String state) {
        if (state == null) {
            return null;
        } else if (state.equals(E)) {
            return EXECUTED;
        } else if (state.equals(F)) {
            return FORWARDED;
        } else if (state.equals(RR)) {
            return REJECTED;
        } else if (state.equals(V)) {
            return REVOKED;
        } else if (state.equals(A)) {
            return AUTHORIZED;
        } else if (state.equals(S)) {
            return SENT;
        } else if (state.equals(C)) {
            return CANCELLED;
        } else if (state.equals(I)) {
        	return SUBMITTED;
        } else if (state.equals(X)) {
        	return VOID;
        }
        
        return state;
    }
}
