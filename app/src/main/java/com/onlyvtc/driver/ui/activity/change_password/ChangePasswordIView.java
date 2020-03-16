package com.onlyvtc.driver.ui.activity.change_password;

import com.onlyvtc.driver.base.MvpView;

public interface ChangePasswordIView extends MvpView {


    void onSuccess(Object object);

    void onError(Throwable e);
}
