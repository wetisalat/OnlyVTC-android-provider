package com.onlyvtc.driver.ui.activity.earnings;


import com.onlyvtc.driver.base.MvpPresenter;

public interface EarningsIPresenter<V extends EarningsIView> extends MvpPresenter<V> {

    void getEarnings();
}
