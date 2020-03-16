package com.onlyvtc.driver.ui.fragment.upcoming;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.HistoryList;

import java.util.List;

public interface UpcomingTripIView extends MvpView {

    void onSuccess(List<HistoryList> historyList);

    void onSuccessCancel(Object o);

    void onSuccessAccepted(Object o);

    void onError(Throwable e);
}
