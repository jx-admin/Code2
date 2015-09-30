
package com.accenture.mbank.util;

public class ServiceCode {
    public static final String SIM_TOP_UP = "022";

    public static final String SIM_TOP_UP_NAME = "sim top up";

    public static final String BANK_TRANSFER_PAYMENT = "003";

    public static final String BANK_TRANSFER_PAYMENT_NAME = "bank transfer";

    public static final String TRANSFER_ENTRY_PAYMENT = "008";

    public static final String TRANSFER_ENTRY_PAYMENT_NAME = "transfer entry";

    public static final String CARD_RECHARGE_PAYMENT = "026";

    public static final String CARD_RECHARGE_PAYMENT_NAME = "prepaid card reload";
    
    public static final String SERVER_CODES[]=new String[]{ServiceCode.TRANSFER_ENTRY_PAYMENT,ServiceCode.BANK_TRANSFER_PAYMENT,ServiceCode.SIM_TOP_UP,ServiceCode.CARD_RECHARGE_PAYMENT};
    
    public static final String RECENT_DEPOSIT = "112";
    
    public static final String RECENT_DEPOSIT_NAME = "recent deposit"; 
    
    /**
     * 最近账户
     */
    public static final String CURRENT_ACCOUNT = "020";
    
    public static final String CURRENT_ACCOUNT_NAME = "current account";
    
    public static final String MU = "034";
    
    public static final String MU_NAME = "MU";
    
    public static final String PR = "041";
    
    public static final String PR_NAME = "PR";

    public static final String getNameByCode(String code) {

        if (code == null || code.equals("")) {
            return null;

        } else if (code.equals(SIM_TOP_UP)) {
            return SIM_TOP_UP_NAME;

        } else if (code.equals(BANK_TRANSFER_PAYMENT)) {
            return BANK_TRANSFER_PAYMENT_NAME;

        } else if (code.equals(CARD_RECHARGE_PAYMENT)) {
            return CARD_RECHARGE_PAYMENT_NAME;

        } else if (code.equals(TRANSFER_ENTRY_PAYMENT)) {
            return TRANSFER_ENTRY_PAYMENT_NAME;
        }else if(code.equals(CURRENT_ACCOUNT)){
            return CURRENT_ACCOUNT_NAME;
        }else if(code.equals(MU)){
            return MU_NAME;
        }else if(code.equals(PR)){
        	return PR_NAME;
        }else if(code.equals(RECENT_DEPOSIT)){
        	return RECENT_DEPOSIT_NAME;
        }
        return null;
    }
}
