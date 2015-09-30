package com.act.mbanking.manager;

import java.util.List;

import com.act.mbanking.bean.AccountsForServiceModel;
import com.act.mbanking.bean.BankRecipient;

public abstract class NPaymentBank extends NPayment<AccountsForServiceModel,BankRecipient> {
    
    public List<BankRecipient>getPayees(){
        if(getRecipientListModel!=null){
            return getRecipientListModel.getBankRecipientList();
        }
        return null;
    }
}