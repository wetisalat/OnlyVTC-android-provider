package com.onlyvtc.driver.ui.activity.past_detail;


import com.onlyvtc.driver.base.MvpPresenter;

public interface PastTripDetailIPresenter<V extends PastTripDetailIView> extends MvpPresenter<V> {

    void getPastTripDetail(String request_id);
}
