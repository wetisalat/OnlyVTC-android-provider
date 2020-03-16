package com.onlyvtc.driver.ui.fragment.incoming_request;

import com.onlyvtc.driver.base.BasePresenter;
import com.onlyvtc.driver.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class IncomingRequestPresenter<V extends IncomingRequestIView>
        extends BasePresenter<V>
        implements IncomingRequestIPresenter<V> {

    @Override
    public void accept(Integer id) {
        getCompositeDisposable().add(
                APIClient
                        .getAPIClient()
                        .acceptRequest("", id)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getMvpView()::onSuccessAccept,
                                getMvpView()::onError));
    }

    @Override
    public void cancel(Integer id) {
        getCompositeDisposable().add(
                APIClient
                        .getAPIClient()
                        .rejectRequest(id)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getMvpView()::onSuccessCancel,
                                getMvpView()::onError));
    }
}
