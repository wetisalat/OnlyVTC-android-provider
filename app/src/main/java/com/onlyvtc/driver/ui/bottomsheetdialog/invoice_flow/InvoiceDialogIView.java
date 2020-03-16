package com.onlyvtc.driver.ui.bottomsheetdialog.invoice_flow;

import com.onlyvtc.driver.base.MvpView;

public interface InvoiceDialogIView extends MvpView {

    void onSuccess(Object object);

    void onError(Throwable e);
}
