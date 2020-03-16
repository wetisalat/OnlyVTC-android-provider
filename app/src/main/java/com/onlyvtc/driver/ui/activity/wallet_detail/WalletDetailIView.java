package com.onlyvtc.driver.ui.activity.wallet_detail;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.Transaction;

import java.util.ArrayList;

public interface WalletDetailIView extends MvpView {
    void setAdapter(ArrayList<Transaction> myList);
}
