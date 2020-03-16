package com.onlyvtc.driver.ui.fragment.upcoming;

import com.onlyvtc.driver.base.BasePresenter;
import com.onlyvtc.driver.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UpcomingTripPresenter<V extends UpcomingTripIView> extends BasePresenter<V> implements UpcomingTripIPresenter<V> {
    @Override
    public void getUpcoming() {
        getCompositeDisposable().add(
                APIClient
                        .getAPIClient()
                        .getUpcoming()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                trendsResponse -> getMvpView().onSuccess(trendsResponse),
                                throwable -> getMvpView().onError(throwable)
                        )
        );
    }

    @Override
    public void assignedCancel(HashMap<String, Object> map) {
        getCompositeDisposable().add(
                APIClient
                        .getAPIClient()
                        .assignedCancel(map)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getMvpView()::onSuccessCancel, getMvpView()::onError)
        );
    }

    @Override
    public void assignedAccepted(HashMap<String, Object> map) {
        getCompositeDisposable().add(
                APIClient
                        .getAPIClient()
                        .assignedAccept(map)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getMvpView()::onSuccessAccepted, getMvpView()::onError)
        );
    }
}
