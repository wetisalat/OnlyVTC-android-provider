package com.onlyvtc.driver.ui.fragment.dispute;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.DisputeResponse;

import java.util.List;

public interface DisputeIView extends MvpView {

    void onSuccessDispute(List<DisputeResponse> responseList);

    void onSuccess(Object object);

    void onError(Throwable e);
}
