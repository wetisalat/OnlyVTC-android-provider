package com.onlyvtc.driver.ui.activity.earnings;


import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.EarningsList;

public interface EarningsIView extends MvpView {

    void onSuccess(EarningsList earningsLists);

    void onError(Throwable e);
}
