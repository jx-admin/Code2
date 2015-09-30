package com.act.mbanking.manager;

import java.util.List;

import com.act.mbanking.bean.AccountsForServiceModel;
import com.act.mbanking.bean.CardRecipient;

public abstract class NPaymentCard extends NPayment<AccountsForServiceModel,CardRecipient> {
    
    public List<CardRecipient>getPayees(){
        if(getRecipientListModel!=null){
            return getRecipientListModel.getCardRecipientList();
        }
        return null;
    }
}