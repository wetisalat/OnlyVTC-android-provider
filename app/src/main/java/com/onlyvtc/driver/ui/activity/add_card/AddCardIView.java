package com.onlyvtc.driver.ui.activity.add_card;

import com.onlyvtc.driver.base.MvpView;

public interface AddCardIView extends MvpView {

    void onSuccess(Object card);

    void onError(Throwable e);
}
