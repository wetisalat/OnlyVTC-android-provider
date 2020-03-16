package com.onlyvtc.driver.ui.activity.setting;

import com.onlyvtc.driver.base.MvpPresenter;

public interface SettingsIPresenter<V extends SettingsIView> extends MvpPresenter<V> {
    void changeLanguage(String languageID);
}
