package com.onlyvtc.driver.ui.activity.main;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.Advertisement;
import com.onlyvtc.driver.data.network.model.SettingsResponse;
import com.onlyvtc.driver.data.network.model.TripResponse;
import com.onlyvtc.driver.data.network.model.UserResponse;

import java.util.List;

public interface MainIView extends MvpView {
    void onSuccess(UserResponse user);

    void onError(Throwable e);

    void onSuccessLogout(Object object);

    void onSuccess(TripResponse tripResponse);

    void onSuccess(SettingsResponse response);

    void onSettingError(Throwable e);

    void onSuccessProviderAvailable(Object object);

    void onSuccessFCM(Object object);

    void onSuccessLocationUpdate(TripResponse tripResponse);

    void onAdsSuccess(List<Advertisement> array);

}
