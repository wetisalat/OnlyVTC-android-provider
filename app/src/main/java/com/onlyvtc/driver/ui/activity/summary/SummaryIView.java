package com.onlyvtc.driver.ui.activity.summary;


import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.Summary;

public interface SummaryIView extends MvpView {

    void onSuccess(Summary object);

    void onError(Throwable e);
}
