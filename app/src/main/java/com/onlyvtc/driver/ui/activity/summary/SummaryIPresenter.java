package com.onlyvtc.driver.ui.activity.summary;


import com.onlyvtc.driver.base.MvpPresenter;

public interface SummaryIPresenter<V extends SummaryIView> extends MvpPresenter<V> {

    void getSummary();
}
