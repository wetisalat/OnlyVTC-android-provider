package com.onlyvtc.driver.ui.activity.notification_manager;

import com.onlyvtc.driver.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
