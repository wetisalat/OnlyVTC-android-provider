package com.onlyvtc.driver.ui.activity.upcoming_detail;


import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.HistoryDetail;

public interface UpcomingTripDetailIView extends MvpView {

    void onSuccess(HistoryDetail historyDetail);

    void onError(Throwable e);
}
