package com.onlyvtc.driver.ui.activity.sms;

import com.onlyvtc.driver.base.MvpPresenter;

import java.util.Map;

import retrofit2.http.PartMap;

public interface GetSMSIPresenter<V extends GetSMSIView> extends MvpPresenter<V> {

    void requestSMS(@PartMap Map<String, String> params);

}
