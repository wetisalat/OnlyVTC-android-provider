package com.onlyvtc.driver.ui.activity.document;

import com.onlyvtc.driver.base.MvpView;
import com.onlyvtc.driver.data.network.model.DriverDocumentResponse;

public interface DocumentIView extends MvpView {

    void onSuccess(DriverDocumentResponse response);

    void onDocumentSuccess(DriverDocumentResponse response);

    void onError(Throwable e);

    void onSuccessLogout(Object object);

}
