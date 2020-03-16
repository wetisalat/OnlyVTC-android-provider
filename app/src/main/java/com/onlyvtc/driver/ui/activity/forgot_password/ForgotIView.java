package com.onlyvtc.driver.ui.activity.forgot_password;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.ForgotResponse;

public interface ForgotIView extends MvpView {

    void onSuccess(ForgotResponse forgotResponse);

    void onError(Throwable e);
}
