package com.act.mbanking.manager;

import java.util.List;

import com.act.mbanking.bean.AccountsForServiceModel;

public abstract class NPaymentEntry extends NPayment<AccountsForServiceModel,AccountsForServiceModel> {
    
    public List<AccountsForServiceModel>getPayees(){
        return getPayers();
    }
}