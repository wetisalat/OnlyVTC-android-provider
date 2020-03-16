package com.onlyvtc.driver.ui.fragment.past;


import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.HistoryList;

import java.util.List;

public interface PastTripIView extends MvpView {

    void onSuccess(List<HistoryList> historyList);

    void onError(Throwable e);
}
