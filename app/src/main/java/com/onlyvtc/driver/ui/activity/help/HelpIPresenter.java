package com.onlyvtc.driver.ui.activity.help;


import com.onlyvtc.driver.base.MvpPresenter;

public interface HelpIPresenter<V extends HelpIView> extends MvpPresenter<V> {

    void getHelp();
}
