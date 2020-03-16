package com.onlyvtc.driver.ui.activity.regsiter;

import com.onlyvtc.driver.base.MvpPresenter;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RegisterIPresenter<V extends RegisterIView> extends MvpPresenter<V> {

    void register(@PartMap Map<String, RequestBody> params, @Part List<MultipartBody.Part> file);

    void verifyEmail(String email);

    void getSettings();

    void verifyCredentials(String countryCode, String phoneNumber);

    void getTermsConditions();

}
