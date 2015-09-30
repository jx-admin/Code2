package com.act.mbanking.manager;

import java.util.List;

import com.act.mbanking.bean.AccountsForServiceModel;
import com.act.mbanking.bean.PhoneRecipient;

public abstract class NPaymentPhone extends NPayment<AccountsForServiceModel,PhoneRecipient> {
    
    public List<PhoneRecipient>getPayees(){
        if(getRecipientListModel!=null){
            return getRecipientListModel.getPhoneRecipientList();
        }
        return null;
    }
}