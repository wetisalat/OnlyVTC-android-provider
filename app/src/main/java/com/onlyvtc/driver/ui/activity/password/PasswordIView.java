package com.onlyvtc.driver.ui.activity.password;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.ForgotResponse;
import com.onlyvtc.driver.data.network.model.User;

public interface PasswordIView extends MvpView {

    void onSuccess(ForgotResponse forgotResponse);

    void onSuccess(User object);

    void onError(Throwable e);
}
