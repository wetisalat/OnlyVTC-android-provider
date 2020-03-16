package com.onlyvtc.driver.ui.activity.instant_ride;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.EstimateFare;
import com.onlyvtc.driver.data.network.model.TripResponse;

public interface InstantRideIView extends MvpView {

    void onSuccess(EstimateFare estimateFare);

    void onSuccess(TripResponse response);

    void onError(Throwable e);

}
