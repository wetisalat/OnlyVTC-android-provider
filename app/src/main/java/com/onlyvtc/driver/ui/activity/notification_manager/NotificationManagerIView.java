package com.onlyvtc.driver.ui.activity.notification_manager;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.NotificationManager;

import java.util.List;

public interface NotificationManagerIView extends MvpView {

    void onSuccess(List<NotificationManager> managers);

    void onError(Throwable e);

}