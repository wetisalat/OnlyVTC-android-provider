package com.onlyvtc.driver.ui.activity.wallet;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.WalletMoneyAddedResponse;
import com.onlyvtc.driver.data.network.model.WalletResponse;

public interface WalletIView extends MvpView {

    void onSuccess(WalletResponse response);

    void onSuccess(WalletMoneyAddedResponse response);

    void onError(Throwable e);
}
