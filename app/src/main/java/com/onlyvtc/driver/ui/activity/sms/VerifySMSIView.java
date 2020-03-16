package com.onlyvtc.driver.ui.activity.sms;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.ResponseSMS;

public interface VerifySMSIView extends MvpView {

    void onSuccess(ResponseSMS result);

    void onError(Throwable e);

}
