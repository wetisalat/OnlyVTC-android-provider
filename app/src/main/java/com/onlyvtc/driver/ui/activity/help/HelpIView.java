package com.onlyvtc.driver.ui.activity.help;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.Help;

public interface HelpIView extends MvpView {

    void onSuccess(Help object);

    void onError(Throwable e);
}
