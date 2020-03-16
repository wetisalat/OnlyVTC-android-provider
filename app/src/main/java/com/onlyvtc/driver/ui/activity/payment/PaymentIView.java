package com.onlyvtc.driver.ui.activity.payment;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.Card;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface PaymentIView extends MvpView {
    void onSuccess(Object card);

    void onSuccess(List<Card> cards);

    void onError(Throwable e);
}
