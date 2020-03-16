package com.onlyvtc.driver.ui.activity.card;

import com.onlyvtc.driver.base.MvpPresenter;

public interface CardIPresenter<V extends CardIView> extends MvpPresenter<V> {

    void deleteCard(String cardId);

    void card();

    void changeCard(String cardId);
}
