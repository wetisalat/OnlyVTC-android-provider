package com.onlyvtc.driver.ui.activity.sociallogin;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.Token;

public interface SocialLoginIView extends MvpView {

    void onSuccess(Token token);

    void onError(Throwable e);
}
