package com.onlyvtc.driver.ui.fragment.past;


import com.onlyvtc.driver.base.MvpPresenter;

public interface PastTripIPresenter<V extends PastTripIView> extends MvpPresenter<V> {

    void getHistory();

}
