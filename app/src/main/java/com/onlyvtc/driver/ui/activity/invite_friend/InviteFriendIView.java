package com.onlyvtc.driver.ui.activity.invite_friend;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.UserResponse;

public interface InviteFriendIView extends MvpView {

    void onSuccess(UserResponse response);

    void onError(Throwable e);

}
