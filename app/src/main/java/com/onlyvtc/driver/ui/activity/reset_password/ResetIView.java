package com.onlyvtc.driver.ui.activity.reset_password;

import com.onlyvtc.driver.base.MvpView;

public interface ResetIView extends MvpView {

    void onSuccess(Object object);

    void onError(Throwable e);
}
