package com.onlyvtc.driver.ui.bottomsheetdialog.rating;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.Rating;

public interface RatingDialogIView extends MvpView {

    void onSuccess(Rating rating);

    void onError(Throwable e);
}
