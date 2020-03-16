package com.onlyvtc.driver.ui.fragment.status_flow;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.TimerResponse;

public interface StatusFlowIView extends MvpView {

    void onSuccess(Object object);

    void onWaitingTimeSuccess(TimerResponse object);

    void onError(Throwable e);
}
