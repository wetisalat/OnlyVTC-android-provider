package com.onlyvtc.driver.ui.bottomsheetdialog.cancel;

import com.onlyvtc.driver.base.MvpPresenter;

import java.util.HashMap;

public interface CancelDialogIPresenter<V extends CancelDialogIView> extends MvpPresenter<V> {

    void cancelRequest(HashMap<String, Object> obj);

    void getReasons();
}
