package com.onlyvtc.driver.ui.activity.wallet_detail;

import com.onlyvtc.driver.base.BasePresenter;
import com.onlyvtc.driver.data.network.model.Transaction;

import java.util.ArrayList;

public class WalletDetailPresenter<V extends WalletDetailIView> extends BasePresenter<V> implements WalletDetailIPresenter<V> {
    @Override
    public void setAdapter(ArrayList<Transaction> myList) {
        getMvpView().setAdapter(myList);
    }
}
