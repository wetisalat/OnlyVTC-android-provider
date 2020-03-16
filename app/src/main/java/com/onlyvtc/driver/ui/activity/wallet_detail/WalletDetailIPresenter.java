package com.onlyvtc.driver.ui.activity.wallet_detail;

import com.onlyvtc.driver.base.MvpPresenter;
import com.onlyvtc.driver.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIPresenter<V extends WalletDetailIView> extends MvpPresenter<V> {
    void setAdapter(ArrayList<Transaction> myList);
}
