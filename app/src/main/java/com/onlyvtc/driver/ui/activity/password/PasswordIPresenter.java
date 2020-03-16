package com.onlyvtc.driver.ui.activity.password;

import com.onlyvtc.driver.base.MvpPresenter;

import java.util.HashMap;

public interface PasswordIPresenter<V extends PasswordIView> extends MvpPresenter<V> {

    void login(HashMap<String, Object> obj);

    void forgot(HashMap<String, Object> obj);

}
