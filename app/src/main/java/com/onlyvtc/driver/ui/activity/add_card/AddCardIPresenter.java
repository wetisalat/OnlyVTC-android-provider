package com.onlyvtc.driver.ui.activity.add_card;

import com.onlyvtc.driver.base.MvpPresenter;

public interface AddCardIPresenter<V extends AddCardIView> extends MvpPresenter<V> {

    void addCard(String stripeToken);
}
