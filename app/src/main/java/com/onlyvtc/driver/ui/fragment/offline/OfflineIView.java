package com.onlyvtc.driver.ui.fragment.offline;

import com.onlyvtc.driver.base.MvpView;

public interface OfflineIView extends MvpView {

    void onSuccess(Object object);

    void onError(Throwable e);
}
