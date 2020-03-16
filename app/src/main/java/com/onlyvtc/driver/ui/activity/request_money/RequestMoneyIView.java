package com.onlyvtc.driver.ui.activity.request_money;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.RequestDataResponse;

public interface RequestMoneyIView extends MvpView {

    void onSuccess(RequestDataResponse response);

    void onSuccess(Object response);

    void onError(Throwable e);

}
