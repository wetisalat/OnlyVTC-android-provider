package com.onlyvtc.driver.ui.activity.splash;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.CheckVersion;

public interface SplashIView extends MvpView {

    void verifyAppInstalled();

    void onSuccess(Object user);

    void onSuccess(CheckVersion user);

    void onError(Throwable e);

    void onCheckVersionError(Throwable e);
}
